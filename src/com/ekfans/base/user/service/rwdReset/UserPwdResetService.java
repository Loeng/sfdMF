package com.ekfans.base.user.service.rwdReset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.plugin.message.util.MessageUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: UserPwdResetService
* @Description: TODO(前台密码重置service重置)
* @author 成都易科远见科技有限工资
* @date 2014-5-18 上午12:23:06
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("unchecked")
public class UserPwdResetService implements IUserPwdResetService {
    private Logger log = LoggerFactory.getLogger(UserPwdResetService.class);
    @Autowired private IUserDao userDao;

    /**
     * 根据userAccount的类型查询出对应的User
     * 完成密码重置
     */
    @Override
    public boolean resetPWD(String userAccount,String userAccountType,String newPassword,String pwdStrong) {
	try {
	    if(StringUtil.isEmpty(userAccount)){
		return false;
	    }
	    
	    StringBuffer hql = new StringBuffer("select u from User as u where 1=1");
	    Map<String,Object> params = new HashMap<String,Object>();
	    if("email".equalsIgnoreCase(userAccountType)){
		// 根据手机号码查出账户
		hql.append(" and u.email=:userAccount");
		params.put("userAccount",userAccount);
	    }else if("phone".equalsIgnoreCase(userAccountType)){
		// 根据邮箱查出账户
		hql.append(" and u.mobile=:userAccount");
		params.put("userAccount",userAccount);
	    }
	    
	    // 执行查询
	    List<User> users = userDao.list(hql.toString(),params);
	    if(users!=null && users.size()==1){
		MD5Util m = new MD5Util();
		User user = users.get(0);
		user.setPassword(m.getMD5ofStr(newPassword));
		user.setPasswordStrong(pwdStrong);
		userDao.updateBean(user);
		return true;
	    }
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
	return false;
    }

    /**
     * 验证此邮箱对应的账户是否存在
     */
    @Override
    public boolean testAccountForEmail(String email) {
	try {
	    if(StringUtil.isEmpty(email)){
		return false;
	    }
	    StringBuffer hql = new StringBuffer("select u from User as u where 1=1");
	    Map<String,Object> params = new HashMap<String,Object>();
	    hql.append(" and u.email=:email");
	    params.put("email",email);
	    List<User> users = userDao.list(hql.toString(),params);
	    if(users!=null && users.size()==1){
		return true;
	    }
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
	return false;
    }

    /**
     * 验证此手机对应的账户是否存在
     */
    @Override
    public boolean testAccountForPhone(String phone) {
	try {
	    if(StringUtil.isEmpty(phone)){
		return false;
	    }
	    StringBuffer hql = new StringBuffer("select u from User as u where 1=1");
	    Map<String,Object> params = new HashMap<String,Object>();
	    hql.append(" and u.mobile=:mobile");
	    params.put("mobile",phone);
	    List<User> users = userDao.list(hql.toString(),params);
	    if(users!=null && users.size()==1){
		return true;
	    }
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
	return false;
    }

    /**
     * 发送随机验证码
     */
    @Override
    public boolean sendCommand(String phone,HttpSession session) {
	try {
	    MessageUtil mu = new MessageUtil();
	    // 获得随机生成的效验码
	    int rn = mu.randomNo();
	    // 获得向用户发送的短信内容 
	    String content = mu.getContent(rn);
	    // 发送短信,并记录入日志  这里还需将验证码绑定到session上,方便用户效验
	    boolean status = 
		MessageUtil.sendPhoneMessage(phone,content);
	    if(status){
		session.setAttribute("yzm",rn+"");
	    }
	    return true;
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
	return false;
    }

    /**
     * 对用户的输入的验证码进行校验
     */
    @Override
    public boolean checkYZM(String uyzm,HttpSession session) {
	try {
	    if(StringUtil.isEmpty(uyzm) || session==null){
		return false;
	    }
	    String sessionYZM = (String)session.getAttribute("yzm");
	    if(uyzm.equalsIgnoreCase(sessionYZM)){
		return true;
	    }
	}
	catch(Exception e) {
	    log.error(e.getMessage());
	}
	return false;
    }

	@Override
	public User emailReset(String email) {
		 Map<String,Object> params = new HashMap<String,Object>();
		try {
			StringBuffer hql = new StringBuffer("from User u where 1=1 and u.email=:email");
		    params.put("email",email);
		    List<User> users = userDao.list(hql.toString(),params);
		    return users.get(0);
		}catch(Exception e) {
		    log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public void updateTempPwd(User user) {
		try {
			userDao.updateBean(user);
		} catch (Exception e) {
			System.out.println("className:UserPwdResetService,methodName:updateTempPwd===这个方法中有错误");
			log.error(e.getMessage());
		}
	}

}
