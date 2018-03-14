package com.ekfans.plugin.wuliubao.yunshu.base.service;

import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.util.UUIDUtil;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppAptitude;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppUserDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.WlbUser;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.PasswordStrengthUtil;
import com.ekfans.pub.util.StringUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Service
public class WlbAppUserService implements IWlbAppUserService {
    // 定义错误日志
    public static Logger log = LoggerFactory.getLogger(WlbAppUserService.class);
    @Autowired
    private IWlbAppUserDao userdao;
    @Autowired
    private IStoreDao storeDao;
    @Autowired
    private IWlbAppAptitude apt;

    @Override
    public String register(String password, String SecurityCode, String mobile, String type) throws Exception {
        Session session = null;
        Transaction tran = null;
        if (mobile == null) {
            return "5";
        }
        if (password == null) {
            return "4";
        }
        if (password.length() > 20) {
            return "7";
        }
        int passwordStrength = PasswordStrengthUtil.getInstance().strength(password);
        if (passwordStrength == 0) {
            return "6";
        }
        if (SecurityCode == null) {
            return "3";
        }
        if (Cache.engine.get(mobile) == null || !Cache.engine.get(mobile).equals(SecurityCode)) {
            return "3";
        }
        User user = userdao.getUser(mobile);
        if (user != null) {
            return "2";
        }
        user = new User();
        user.setName(mobile.trim());
        user.setStatus(1);
        user.setId(UUIDUtil.getMarkedUUID32(User.SINGLE_MARK));
        user.setPassword(new MD5Util().getMD5ofStr(password));
        user.setMobile(mobile.trim());
        user.setType(type);
        user.setCreateTime(DateUtil.getSysDateTimeString());
        if (passwordStrength >= 3) {
            user.setPasswordStrong("3");
        } else {
            user.setPasswordStrong(String.valueOf(passwordStrength));
        }
        user.setCreateTime(DateUtil.getSysDateTimeString());
        user.setVerificationStatus(true);
        Store store = new Store();
        store.setId(user.getId());
        store.setCreateTime(user.getCreateTime());
        store.setStoreName(mobile.trim());
        store.setRoleId("4");
        // 与官网登录同步需要，对保存无影响
        store.setUserEntity(user);
        Aptitude ap = new Aptitude();
        ap.setAuthenticatorId(user.getId());
        ap.setCreateTime(DateUtil.getSysDateTimeString());
        try {
            session = this.userdao.createSession();
            tran = session.beginTransaction();
            storeDao.addBean(store, session);
            userdao.addBean(user, session);
            apt.addBean(ap, session);
            tran.commit();
            session.flush();
            return "1";
        } catch (Exception e) {
            // 回滚
            if (tran != null) {
                tran.rollback();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
            // 记日志
            log.error(e.getMessage());
            return "0";
        } finally {
        }
    }

    @Override
    public String login(String type, String password, String SecurityCode, String mobile, String registrationID, String userType) throws Exception {
        if (StringUtil.isEmpty(type)) {
            return "0";
        }
        if (StringUtil.isEmpty(mobile)) {
            return "5";
        }
        if (StringUtil.isEmpty(userType) || !userType.equals("4") && !userType.equals("1")) {
            return "6";
        }
        //手机验证码登录
        if (type.equals("1")) {
            if (StringUtil.isEmpty(SecurityCode)) {
                return "3";
            }
            if (Cache.engine.get(mobile) == null || !Cache.engine.get(mobile).equals(SecurityCode)) {
                return "3";
            }
            //获取用户
            User user = userdao.getUser(mobile);
            if (user == null) {
                String a = this.register("111111", SecurityCode, mobile, userType);
                if (!a.equals("1")) {
                    return "2";
                }
            }
        }
        //获取用户
        User user = userdao.getUser(mobile);
        if (user == null) {
            return "7";
        }
        if (!user.getType().equals(userType)) {
            return "6";
        }
        //创建token
        String token = user.getId() + "_" + DateUtil.getFullSysDateTimeString();
        //账号密码登录
        if (type.equals("0")) {
            if (StringUtil.isEmpty(password)) {
                return "4";
            }
            if (!new MD5Util().getMD5ofStr(password).equals(user.getPassword())) {
                return "2";
            }
        }
        //获取用户资质
        Aptitude ap = userdao.getUserAptitude(user.getId());
        Session session = null;
        Transaction tran = null;

        try {
            session = userdao.createSession();
            tran = session.beginTransaction();
            if (ap == null) {
                //如果资质为null  创建资质
                ap = new Aptitude();
                ap.setAuthenticatorId(user.getId());
                ap.setCreateTime(DateUtil.getSysDateTimeString());
                apt.addBean(ap, session);
            }
            //设置极光推送id
            if (!StringUtil.isEmpty(registrationID)) {
                user.setRegistrationID(registrationID);
            }
            user.setLastLoginTime(DateUtil.getSysDateTimeString());
            user.setLoginNum(user.getLoginNum() + 1);
            //获取用户以前登录的token
            String userToken = user.getWlbToken();
            //如果以前的token不为空  那么从缓存中删除该token
            if (!StringUtil.isEmpty(userToken)) {
                Cache.engine.remove(userToken);
            }
            user.setWlbToken(token);
            userdao.updateBean(user, session);
            session.flush();
            tran.commit();
            session.close();
        } catch (Exception e) {
            // 回滚
            if (tran != null) {
                tran.rollback();
            }
            e.printStackTrace();
            throw new Exception();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }

        }
        //包装用户数据  用于数据传输
        System.out.println("Before WlbUser --------------------------------");
        WlbUser wlbUser = new WlbUser(user, ap, token);
        System.out.println("After WlbUser --------------------------------");
        try {
            Cache.engine.add(token, wlbUser);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Add WlbUser To Cache--------------------------------");
        return token;
    }

    @Override
    public String modifyPW(String newpassword, String SecurityCode, String mobile) throws Exception {
        if (StringUtil.isEmpty(newpassword)) {
            return "4";
        }
        if (newpassword.length() > 20) {
            return "7";
        }
        if (StringUtil.isEmpty(SecurityCode)) {
            return "3";
        }
        if (StringUtil.isEmpty(mobile)) {
            return "5";
        }
        int passwordStrength = PasswordStrengthUtil.getInstance().strength(newpassword);
        if (passwordStrength == 0) {
            return "6";
        }
        //获取用户
        User user = userdao.getUser(mobile);
        if (user == null) {
            return "2";
        }
        if (!Cache.engine.get(mobile).equals(SecurityCode)) {
            return "3";
        }
        user.setPassword(new MD5Util().getMD5ofStr(newpassword));
        user.setUpdateTime(DateUtil.getSysDateTimeString());
        if (passwordStrength >= 3) {
            user.setPasswordStrong("3");
        } else {
            user.setPasswordStrong(String.valueOf(passwordStrength));
        }
        userdao.updateBean(user);
        return "1";
    }

    @Override
    public String logModifyPW(String newpassword, String password, HttpServletRequest request) throws Exception {
        //获取用户token
        String token = request.getHeader("WLB-Token");
        //通过token获取用户id
        String[] userid = token.split("_");
        //获取用户
        User user = (User) userdao.get(userid[0]);
        if (user == null) {
            return "0";
        }
        if (!user.getPassword().equals(new MD5Util().getMD5ofStr(password))) {
            return "2";
        }
        if (StringUtil.isEmpty(newpassword)) {
            return "4";
        }
        if (newpassword.length() > 20) {
            return "7";
        }
        int passwordStrength = PasswordStrengthUtil.getInstance().strength(newpassword);
        if (passwordStrength == 0) {
            return "6";
        }
        if (passwordStrength >= 3) {
            user.setPasswordStrong("3");
        } else {
            user.setPasswordStrong(String.valueOf(passwordStrength));
        }
        user.setPassword(new MD5Util().getMD5ofStr(newpassword));

        userdao.updateBean(user);
        return "1";
    }

    @Override
	public Map<String,Object> editUser(HttpServletRequest request, String safeMobile, String nickName) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		// 3:未获取到用户信息 4:编辑成功
		//获取用户token
		String token = request.getHeader("WLB-Token");
		//通过token获取用户id
		String[] userid = token.split("_");
		String id=userid[0];
        WlbUser appuser = (WlbUser) Cache.engine.get(token);
		//获取用户
		User  user = (User) userdao.get(id);
        if(user==null){
        	map.put("statusCode", "3");
        	map.put("user", appuser);
        	return map;
        }
		if(!StringUtil.isEmpty(nickName)){
			user.setNickName(nickName);
		}
		if(!StringUtil.isEmpty(safeMobile)){
			user.setSafeMobile(safeMobile);
		}
		// 设置修改时间为当前系统时间
		user.setUpdateTime(DateUtil.getSysDateTimeString());
		// 调用DAO的方法修改会员
		userdao.updateBean(user);
		appuser.setNickName(nickName);
		appuser.setSafeMobile(safeMobile);
		Cache.engine.remove(token);
		Cache.engine.add(token, appuser);
		map.put("statusCode", "4");
    	map.put("user", appuser);
    	return map;
	}

    public Map<String,Object> FileUpload(String stream, HttpServletRequest request) throws Exception {
        String token = request.getHeader("WLB-Token");
        //通过token获取用户id
        String[] userid = token.split("_");
        String id = userid[0];
        WlbUser appuser = (WlbUser) Cache.engine.get(token);
        //获取用户
        Map<String,Object> map = new HashMap<String,Object>();
        User user = (User) userdao.get(id);
        if (user == null) {
        	map.put("statusCode", "3");
        	map.put("user", appuser);
            return map;
        }
        String relativePath = "";
        if (!StringUtil.isEmpty(stream)) {
            // 设置图片路径
            relativePath = FileUtil.baseStringToImage(request, stream, "/customerfiles/avatar/");
            user.setHeadPortrait(relativePath);
        }
        // 设置修改时间为当前系统时间
        user.setUpdateTime(DateUtil.getSysDateTimeString());
        // 调用DAO的方法修改会员
        userdao.updateBean(user);
        appuser.setHeadPortrait(user.getHeadPortrait());
        Cache.engine.remove(token);
        Cache.engine.add(token, appuser);
        map.put("statusCode", "4");
    	map.put("user", appuser);
        return map;
   }

}















