package com.ekfans.base.user.service;

import com.ekfans.base.store.dao.ICarUserDao;
import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreInfo;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dao.IAccountDao;
import com.ekfans.base.user.dao.IIntegralDao;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.model.IntegralLog;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.model.UserInfo;
import com.ekfans.base.user.util.UUIDUtil;
import com.ekfans.base.user.util.UserConst;
import com.ekfans.controllers.web.vo.NumBerAttribute;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.ccb.CcbPayUtil;
import com.ekfans.plugin.wftong.base.easemob.dao.IGroupsDao;
import com.ekfans.plugin.wftong.base.easemob.model.Groups;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.EncDec.MD5Util;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.PasswordStrengthUtil;
import com.ekfans.pub.util.StringUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;


/**
 * 会员业务实现Service
 *
 * @author Lgy
 * @version 1.0
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @date 2014-1-3
 */
@Service
@SuppressWarnings("unchecked")
public class UserService implements IUserService {

    // 定义错误日志
    public static Logger log = LoggerFactory.getLogger(UserService.class);
    // 定义userDao
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IIntegralDao intergralDao;
    @Autowired
    private IAccountDao accountDao;
    @Autowired
    private ICarUserDao carUserDao;
    @Autowired
    private IStoreDao storeDao;
    @Autowired
    private IGroupsDao groupsDao;

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    public boolean addUser(User user, IntegralLog inter, HttpServletRequest request) {
        // 定义Session
        Session session = null;
        // 定义事务处理
        Transaction transaction = null;
        try {
            // 创建Session
            session = userDao.createSession();
            // 开启事务处理
            transaction = session.beginTransaction();

            user.setId(UUIDUtil.getMarkedUUID32(User.SINGLE_MARK));
            // 设置创建时间为当前系统时间
            user.setCreateTime(DateUtil.getSysDateTimeString());
            user.setCcbUserId(CcbPayUtil.createCcbPayId());
            // 当用户名以及密码不为空时，将密码MD5加密，添加用户
            if (!StringUtil.isEmpty(user.getPassword()) && !StringUtil.isEmpty(user.getName())) {
                MD5Util m = new MD5Util();
                user.setPassword(m.getMD5ofStr(user.getPassword()));
                // 将用户资料保存到数据库
                userDao.addBean(user, session);
                // 保存成功同步数据到第三方
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("email", user.getEmail());
                paramMap.put("password", user.getPassword());
                paramMap.put("mobile", user.getMobile());
                paramMap.put("nickName", user.getNickName());
                paramMap.put("companyId", user.getId());
                paramMap.put("type", user.getType());
                List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
                // 将list放入map
                Map<String, Object> map = new HashMap<String, Object>();
                listMap.add(paramMap);
                map.put("user", listMap);
                // 设置参数
                Map<String, Object> tempMap = new HashMap<String, Object>();
                // 接口编号
                tempMap.put("type", "000");
                // 数据
                tempMap.put("dataInfo", map);
                if (inter != null) {
                    // 设置积分创建时间
                    inter.setCreateTime(DateUtil.getSysDateTimeString());

                    // 设置来源(管理员修改等级)
                    inter.setSource(UserConst.USER_INTEGRAL_SOURCE_LEVEL);
                    // 设置用户id
                    inter.setUserId(user.getId());
                    // 设置状态
                    inter.setStatus(true);
                    // 设置类型为增加
                    inter.setType(UserConst.USER_INTEGRAL_TYPE_ADD);
                    // 保存积分日志
                    intergralDao.addBean(inter, session);
                }
                // 事物处理提交以及关闭事物
                session.flush();
                transaction.commit();
                session.close();
                // 返回true
                return true;
            }

            return false;
        } catch (Exception e) {
            // 回滚
            if (transaction != null) {
                transaction.rollback();
            }
            if (session != null && session.isOpen()) {
                session.close();
            }
            // 记日志
            log.error(e.getMessage());
        } finally {
        }
        return false;

    }

    /**
     * 根据id查找用户
     *
     * @param id
     * @return
     */
    public User getUser(String id) {
        try {
            User user = (User) userDao.get(id);
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
            // 记日志
            log.error(e.getMessage());
        }
        return null;
    }

    public User getUserByName(String name) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }

        // 定义参数Map
        Map<String, Object> params = new HashMap<String, Object>();
        // 定义hql
        String hql = "from User where name=:name or email=:name";
        // setting data
        params.put("name", name);
        try {
            List<User> list = userDao.list(hql, params);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据用户名查找店铺用户
     *
     * @param name
     * @return
     */
    public User getStoreUserByName(String name) {
        // 如果传进来的管理员用户名为空，则返回null
        if (StringUtil.isEmpty(name)) {
            return null;
        }

        // 定义SQL
        StringBuffer sql = new StringBuffer(" from User as user where 1=1");

        // 定义SQL的参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 匹配用户名,只要用户输入的内容和 用户名 或者 电子邮件或者 手机号其中一个相等就可以登录
        sql.append(" and (user.name = :name");
        sql.append(" or user.email = :name");
        sql.append(" or user.mobile = :name)");
        // 将用户名放入参数MAP
        paramMap.put("name", name);

        try {
            // 调用DAO方法执行SQL获取对象集合
            List<User> list = userDao.list(sql.toString(), paramMap);
            // 如果集合不为空并且集合的长度大于0，则获取集合的第一个对象并返回
            if (list != null && list.size() > 0) {
                return (User) list.get(0);
            }
        } catch (Exception e) {
            // 记日志
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * 根据id删除用户
     */
    public boolean deleteUser(String id) {
        // 如果传进来的id为空，则返回false
        if (StringUtil.isEmpty(id)) {
            return false;
        }
        try {
            // 删除旧头像
            User user = (User) userDao.get(id);
            if (!StringUtil.isEmpty(user.getHeadPortrait())) {
                File file = new File(user.getHeadPortrait());
                if (file.exists()) {
                    file.delete();
                }
            }
            // 调用SERVICE的方法删除会员
            userDao.deleteById(id);
            return true;
        } catch (Exception e) {
            // 记日志
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 修改用户资料
     */
    public boolean modifyUser(User user, IntegralLog inter, IntegralLog oldInter, String path) {
        // 如果传进来的会员对象为空，则返回false
        if (user == null) {
            return false;
        }
        // 根据日志是否记录操作者，看是否修改了会员等级
        if (inter != null && !StringUtil.isEmpty(inter.getOperation())) {
            // 定义Session
            Session session = null;
            // 定义事务处理
            Transaction transaction = null;
            try {
                // 创建Session
                session = userDao.createSession();
                // 开启事务处理
                transaction = session.beginTransaction();

                // 找到数据库中该用户
                User u = (User) userDao.get(user.getId());
                if (u != null && !StringUtil.isEmpty(u.getCcbUserId())) {
                    user.setCcbUserId(u.getCcbUserId());
                } else {
                    user.setCcbUserId(CcbPayUtil.createCcbPayId());
                }
                // 比对密码，如果改动了，重新加密
                if (!u.getPassword().equals(user.getPassword())) {
                    // 将密码MD5加密
                    if (!StringUtil.isEmpty(user.getPassword())) {
                        MD5Util m = new MD5Util();
                        user.setPassword(m.getMD5ofStr(user.getPassword()));
                    }
                }

                // 如果上传了新头像删除旧头像
                if (!StringUtil.isEmpty(path)) {
                    File file = new File(user.getHeadPortrait());
                    if (file.exists()) {
                        file.delete();
                    }
                    // 设置新头像
                    user.setHeadPortrait(path);
                }
                // 设置更新时间
                user.setUpdateTime(DateUtil.getSysDateTimeString());

                userDao.updateBean(user, session);

                // 设置积分创建时间
                inter.setCreateTime(DateUtil.getSysDateTimeString());
                // 设置来源(管理员修改等级)
                inter.setSource(UserConst.USER_INTEGRAL_SOURCE_LEVEL);
                // 设置用户id
                inter.setUserId(user.getId());
                // 设置状态
                inter.setStatus(true);
                // 设置类型为增加
                inter.setType(UserConst.USER_INTEGRAL_TYPE_ADD);
                // 删除旧日志
                if (oldInter != null) {
                    intergralDao.delete(oldInter, session);
                }
                // 保存积分日志
                intergralDao.addBean(inter, session);

                // 事物处理提交以及关闭事物
                session.flush();
                transaction.commit();
                session.close();
                return true;
            } catch (Exception e) {
                // 回滚
                if (transaction != null) {
                    transaction.rollback();
                }
                if (session != null && session.isOpen()) {
                    session.close();
                }
                // 记日志
                log.error(e.getMessage());
            }
        } else {
            // 没有修改等级时
            try {
                // 如果上传了新头像删除旧头像
                if (!StringUtil.isEmpty(path)) {
                    File file = new File(user.getHeadPortrait());
                    if (file.exists()) {
                        file.delete();
                    }
                    // 设置新头像
                    user.setHeadPortrait(path);
                }

                // 找到数据库中该用户
                User u = (User) userDao.get(user.getId());
                // 比对密码，如果改动了，重新加密
                if (!u.getPassword().equals(user.getPassword())) {
                    // 将密码MD5加密
                    if (!StringUtil.isEmpty(user.getPassword())) {
                        MD5Util m = new MD5Util();
                        user.setPassword(m.getMD5ofStr(user.getPassword()));
                    }
                }
                // 设置修改时间为当前系统时间
                user.setUpdateTime(DateUtil.getSysDateTimeString());
                // 调用DAO的方法修改会员
                userDao.updateBean(user);
                return true;
            } catch (Exception e) {
                // 记日志
                log.error(e.getMessage());
            }

        }
        return false;
    }

    /**
     * 查找用户列表
     */
    public List<User> listUser(Pager pager, String name, String status, String mobile, String email, String cardNumber, String nickName) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select u from User as u where 1=1");

        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(name)) {
            sql.append(" and u.name like :name");
            paramMap.put("name", "%" + name + "%");
        }

        // 如果查询条件输入了status，添加查询条件
        if (!StringUtil.isEmpty(status)) {
            // 添加查询条件
            sql.append(" and u.status = :status");
            paramMap.put("status", Integer.valueOf(status));
        }

        // 如果查询条件输入了mobile，添加查询条件
        if (!StringUtil.isEmpty(mobile)) {
            sql.append(" and u.mobile like :mobile");
            paramMap.put("mobile", "%" + mobile + "%");
        }

        // 如果查询条件输入了email，添加查询条件
        if (!StringUtil.isEmpty(email)) {
            sql.append(" and u.email like :email");
            paramMap.put("email", "%" + email + "%");
        }

        // 如果查询条件输入了cardNumber，添加查询条件
        if (!StringUtil.isEmpty(cardNumber)) {
            sql.append(" and u.cardNumber like :cardNumber");
            paramMap.put("cardNumber", "%" + cardNumber + "%");
        }

        // 如果查询条件输入了nickName，添加查询条件
        if (!StringUtil.isEmpty(nickName)) {
            sql.append(" and u.nickName like :nickName");
            paramMap.put("nickName", "%" + nickName + "%");
        }
        // 只查询会员类型为用户的会员
        String type = UserConst.USER_TYPE_CUSTOMER;
        sql.append(" and u.type = " + type);
        sql.append(" order by u.createTime desc");
        try {
            // 调用DAO方法查找用户
            List<User> list = userDao.list(pager, sql.toString(), paramMap);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 更新数据库中的会员对象
     */
    public boolean updateUser(User user) {
        try {
            // 调用Dao方法更新会员对象
            userDao.updateBean(user);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return false;
    }

    /**
     * 设置Email
     */
    public boolean updateEmail(User user) {
        // 创建Store对象获取传过来的store参数
        try {
            userDao.updateBean(user);
            return true;
        } catch (Exception e) {
            // 记日志
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 设置手机
     */
    public boolean updateMobile(User user) {
        // 创建Store对象获取传过来的store参数
        try {
            userDao.updateBean(user);
            return true;
        } catch (Exception e) {
            // 记日志
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 判断昵称是否重复可用
     */
    @SuppressWarnings("rawtypes")
    @Override
    public boolean existNickName(String nickName, String id) throws Exception {
        // 创建SQL语句
        StringBuffer sql = new StringBuffer("select user from User as user where 1=1");
        // 将昵称和ID作为查询条件
        sql.append(" and user.nickName=:nickName");
        sql.append(" and user.id !=:id");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("nickName", nickName);
        map.put("id", id);
        List list = userDao.list(sql.toString(), map);
        if (list != null && list.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据userId获得当前用户的积分信息
     */
    @SuppressWarnings("rawtypes")
    @Override
    public List<IntegralLog> getIntegralByUserId(String userId) {
        try {
            StringBuffer hql = new StringBuffer("select il.integral,il.type,il.source,il.createTime,");

            hql.append("il.status,il.invalidTime,il.orderId,il.operation,m.realName");

            hql.append(" from IntegralLog as il,Manager as m where 1=1");

            hql.append(" and il.operation = m.id");
            Map<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(userId)) {
                hql.append(" and il.userId=:userId");
                params.put("userId", userId);
            }
            hql.append(" order by il.createTime desc");
            List list = userDao.list(hql.toString(), params);
            // 定义返回的集合
            List<IntegralLog> integralLogs = new ArrayList<IntegralLog>();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Object[] objects = (Object[]) list.get(i);
                    // 定义接收数据的容器
                    IntegralLog integralLog = new IntegralLog();
                    integralLog.setIntegral((Double) objects[0]);
                    integralLog.setType((String) objects[1]);
                    integralLog.setSource((String) objects[2]);
                    integralLog.setCreateTime((String) objects[3]);
                    integralLog.setStatus((Boolean) objects[4]);
                    integralLog.setInvalidTime((String) objects[5]);
                    // 将这笔订单的积分存储到userId字段中
                    integralLog.setUserId(userId);
                    integralLog.setOrderId((String) objects[6]);
                    integralLog.setOperation((String) objects[7]);
                    integralLog.setRealName((String) objects[8]);
                    integralLogs.add(integralLog);
                }
                return integralLogs;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean judgmentRepeat(String email, String userName, String mobile) {
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer hql = new StringBuffer("from User u where 1=1");

        // 如果email不为空，添加查询条件
        if (!StringUtil.isEmpty(email)) {
            hql.append(" and u.email=:email");
            map.put("email", email.trim());
        }
        // 如果用户名不为空，添加查询条件
        if (!StringUtil.isEmpty(userName)) {
            hql.append(" and u.name=:name");
            map.put("name", userName.trim());
        }
        // 如果手机号不为空，添加查询条件
        if (!StringUtil.isEmpty(mobile)) {
            hql.append(" and u.mobile=:mobile");
            map.put("mobile", mobile.trim());
        }

        try {
            List<User> list = userDao.list(hql.toString(), map);
            // 如果查询出的列表为空，即没有重复对象，返回false
            if (null == list || list.size() <= 0) {
                return false;
            }
            // 如果有重复对象，返回true
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    @Override
    public User getUserName(String userName, String type) {
        // param data
        Map<String, Object> map = new HashMap<String, Object>();
        // hql
        StringBuffer hql = new StringBuffer("from User user where 1=1");
        // setting data
        if (!StringUtil.isEmpty(type)) {
            hql.append(" and user.type =:type");
            map.put("type", type.trim());
        }
        if (!StringUtil.isEmpty(userName)) {
            hql.append(" and user.name =:name");
            map.put("name", userName.trim());
        }

        try {
            List<User> list = userDao.list(hql.toString(), map);
            if (list == null || list.size() <= 0) {
                return null;
            }
            return list.get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean checkUserName(String name) {
        String hql1 = "select count(*) from User where name='" + name.trim() + "' or mobile='" + name.trim() + "' or email='" + name.trim() + "'";
        String hql2 = "select count(*) from Account where name='" + name.trim() + "'";

        try {
            List<Long> list1 = userDao.list(hql1, null);
            if (list1.get(0) > 0) {
                return true;
            }

            List<Long> list2 = userDao.list(hql2, null);
            if (list2.get(0) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return true;
    }

    @Override
    public Boolean checkUserNameUpdate(String oldName, String newName) {
        // param data
        Map<String, Object> params = new HashMap<String, Object>();
        // hql
        String hql = "from User where name!=:oldName and name=:newName";
        // setting data
        params.put("oldName", oldName.trim());
        params.put("newName", newName.trim());

        try {
            List<User> list = this.userDao.list(hql, params);

            return (list == null || list.size() <= 0) ? false : true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return true;
        }
    }

    @Override
    public NumBerAttribute getRegCustomerNumber() {
        // 个人用户注册数量
        int userNumber = Integer.valueOf(Cache.getResource("user.reg"));
        // 企业用户注册数量
        int companyNumber = Integer.valueOf(Cache.getResource("company.reg"));
        // 多少企业收到款
        int companyIn = Integer.valueOf(Cache.getResource("company.income"));

        // param data
        Map<String, Object> params = new HashMap<String, Object>();
        // hql
        String hql = "SELECT (SELECT COUNT(*) FROM User t WHERE t.type=:typeo),(SELECT COUNT(*) FROM User t WHERE t.type<>:typet) FROM User";
        // setting data
        params.put("typeo", "0");
        params.put("typet", "0");

        Pager pager = new Pager();
        pager.setRowsPerPage(1);

        try {
            List<Object[]> list = this.userDao.list(hql, params);
            if (list != null && list.size() > 0) {
                NumBerAttribute na = new NumBerAttribute();
                for (Object[] obj : list) {
                    na.setUserNumber(Integer.valueOf(obj[1].toString()) + userNumber);
                    na.setCompanyNumber(companyNumber);
                    na.setCompanyIn(companyIn);
                }

                return na;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean saveUserOrStore(String userName, String password, String storeName, String type, String email, HttpServletRequest request) {
        Pattern pattern1 = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Pattern pattern2 = Pattern.compile("^(1[0-9][0-9])\\d{8}$");

        User user = new User();
        Store store = null;
        Session session = null;
        Transaction tran = null;

        user.setId(UUIDUtil.getMarkedUUID32(User.SINGLE_MARK));
        user.setName(userName.trim());

        if (pattern1.matcher(userName.trim()).matches()) {
            user.setEmail(userName.trim());
            user.setVerificationStatus(false);
        } else if (pattern2.matcher(userName.trim()).matches()) {
            user.setMobile(userName.trim());
            user.setVerificationStatus(true);
        }

        if ("0".equals(type)) {
            user.setNickName(userName.trim());
        } else {
            user.setNickName(storeName.trim());
        }
        user.setPassword(new MD5Util().getMD5ofStr(password));
        user.setType(type);
        user.setStatus(1);
        user.setCreateTime(DateUtil.getSysDateTimeString());
        user.setEmail(email);
        int passwordStrength = PasswordStrengthUtil.getInstance().strength(password);
        if (passwordStrength >= 3) {
            user.setPasswordStrong("3");
        } else {
            user.setPasswordStrong(String.valueOf(passwordStrength));
        }

        try {
            session = this.userDao.createSession();
            tran = session.beginTransaction();

            userDao.addBean(user, session);

            if ("0".equals(type)) {
                UserInfo ui = new UserInfo();
                ui.setUserId(user.getId());

                userDao.addBean(ui, session);
            } else {
                store = new Store();
                store.setId(user.getId());
                store.setCreateTime(user.getCreateTime());
                store.setStoreName(storeName.trim());
                // 1:供应商角色，2:采购商角色，3:处置企业角色，4:运输企业角色
                store.setRoleId(type);
                // 同步需要，对保存无影响
                store.setUserEntity(user);
                storeDao.addBean(store, session);
            }
            tran.commit();
            session.flush();
            request.getSession().setAttribute(SystemConst.STORE, store);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (tran != null) {
                tran.rollback();
            }
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public Boolean supplementUser(User user, String type, String companyName, String mail) {
        Session session = null;
        Transaction tran = null;
        try {
            session = this.userDao.createSession();
            tran = session.beginTransaction();

            // 状态变更
            user.setTouristType("0");
            user.setEmail(mail);
            user.setType(type);
            user.setUpdateTime(DateUtil.getSysDateTimeString());

            Store store = (Store) storeDao.get(user.getId(), session);
            store.setStoreName(companyName);
            store.setRoleId(type);
            store.setUpdateTime(user.getCreateTime());

            userDao.updateBean(user, session);
            storeDao.updateBean(store, session);

            tran.commit();
            session.flush();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (tran != null) {
                tran.rollback();
            }
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Boolean mobileReg(String userName, String password, HttpServletRequest request) {
        Session session = null;
        Transaction tran = null;

        String uuid = UUIDUtil.getMarkedUUID32(User.SINGLE_MARK);
        String storeName = userName + "_待完善信息";

        User user = new User();
        user.setId(uuid);
        user.setVerificationStatus(true);
        user.setTouristType("1");
        user.setName(userName.trim());
        user.setPassword(new MD5Util().getMD5ofStr(password));
        user.setStatus(1);
        user.setCreateTime(DateUtil.getSysDateTimeString());
        int passwordStrength = PasswordStrengthUtil.getInstance().strength(password);
        if (passwordStrength >= 3) {
            user.setPasswordStrong("3");
        } else {
            user.setPasswordStrong(String.valueOf(passwordStrength));
        }

        Store store = new Store();
        store.setId(uuid);
        store.setCreateTime(user.getCreateTime());
        store.setStoreName(storeName.trim());


        try {
            session = this.userDao.createSession();
            tran = session.beginTransaction();

            userDao.addBean(user, session);

            storeDao.addBean(store, session);

            tran.commit();
            session.flush();
            request.getSession().setAttribute(SystemConst.STORE, store);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (tran != null) {
                tran.rollback();
            }
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }


    @Override
    public Boolean activationUser(String name, String type) {
        // param data
        Map<String, Object> params = new HashMap<String, Object>();
        // hql
        String hql = "from User where name=:name and type=:type";
        // setting date
        params.put("name", name);
        params.put("type", type);

        try {
            List<User> list = this.userDao.list(hql, params);
            User user = list.get(0);
            user.setVerificationStatus(true);
            this.userDao.updateBean(user);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public Boolean saveCoreStore(String companyName, String unitType, String contactName, String contactSex, String contactPhone, String contactTime, String type) {
        User user = new User();

        Session session = null;
        Transaction tran = null;

        user.setType(type);
        user.setStatus(1);
        user.setCreateTime(DateUtil.getSysDateTimeString());
        user.setVerificationStatus(true);

        try {
            session = this.userDao.createSession();
            tran = session.beginTransaction();

            this.userDao.addBean(user, session);

            if (!"0".equals(type)) {
                Store store = new Store();
                store.setId(user.getId());
                store.setStoreName(companyName.trim());
                // 1:供应商角色，2:采购商角色，3:核心企业角色
                store.setRoleId(type);

                this.userDao.addBean(store, session);

                StoreInfo si = new StoreInfo();
                si.setId(user.getId());
                si.setContactName(contactName.trim());
                si.setUnitType(Integer.valueOf(unitType));
                si.setContactName(contactName.trim());
                si.setContactSex("0".equals(contactSex) ? false : true);
                si.setContactPhone(contactPhone.trim());
                si.setContactTime(contactTime.trim());

                this.userDao.addBean(si, session);
            }
            tran.commit();
            session.flush();

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            if (tran != null) {
                tran.rollback();
            }
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public Boolean updateUserAndUserInfo(User user, UserInfo userInfo) {
        Session session = null;
        Transaction tran = null;

        try {
            session = this.userDao.createSession();
            tran = session.beginTransaction();

            if (user != null) {
                this.userDao.updateBean(user, session);
            }
            if (userInfo != null) {
                if (StringUtil.isEmpty(userInfo.getId())) {
                    this.userDao.addBean(userInfo, session);
                } else {
                    this.userDao.updateBean(userInfo, session);
                }
            }
            tran.commit();
            session.flush();

            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            if (tran != null) {
                tran.rollback();
            }
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * 生成六位随机验证码
     */
    @Override
    public String phoneRandom(HttpSession session) {
        Random random = new Random();
        int x = random.nextInt(999999);
        if (x > 100000) {
            System.out.println(x);
            session.setAttribute("phoneRandom", Integer.toString(x));
            return Integer.toString(x);
        }
        return "";
    }

    @Override
    public List<User> findUserByType(String type) {
        StringBuffer buffer = new StringBuffer("from User where type='" + type + "'");
        try {
            List<User> user = userDao.list(buffer.toString(), null);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getUserByType(String type) {
        StringBuffer buffer = new StringBuffer("from User where type='" + type + "'");
        try {
            return userDao.list(buffer.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getUserNumber() {
        String hql = "select count(*) from User u,Store s where u.id=s.id and u.status=1 and u.verificationStatus=true";

        try {
            List<Long> list = userDao.list(hql, null);

            return (list == null || list.size() <= 0) ? 0 : list.get(0).intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return 0;
    }

    @Override
    public boolean saveUser(User user) {
        try {
            this.userDao.addBean(user);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateUseData(Class clz, String ids, boolean use) {
        if (!StringUtil.isEmpty(ids)) {
            return userDao.updateUseData(clz, ids, use);
        }
        return false;
    }

    @Override
    public AppUser getAppUserByHxId(String id) {
        AppUser appUser = null;
        String userId = userDao.getUserIdByHxId(id);
        String accoundId = accountDao.getAccountIdByHxId(id);

        if (!StringUtil.isEmpty(accoundId)) {
            appUser = getAppUserById(accoundId);
        } else if (!StringUtil.isEmpty(userId)) {
            appUser = getAppUserById(userId);
        }
        return appUser;
    }

    @Override
    public AppUser getAppUserById(String id) {
        AppUser appUser = null;

        if (StringUtil.isEmpty(id)) {
            return null;
        }
        try {
            User user = (User) userDao.get(id);
            Account account = null;
            Store store = null;

            // 如果用户对象为空，则查找子账户
            if (user == null) {
                account = (Account) accountDao.get(id);
                user = (User) userDao.get(account.getStoreId());
            }
            if (user != null) {
                store = (Store) storeDao.get(user.getId());

                // 只允许企业类型为（1：产生企业 3：处置企业 4:运输企业）的企业登录
                if (user.getType().equals("2") || user.getType().equals("5")) {
                    return null;
                }
            }

            // 保证数据结构
            if (user == null) {
                user = new User();
            }
            if (store == null) {
                store = new Store();
            }

            // 构造AppUser模拟对象，整合账号信息，用于传输和缓存
            if (account != null) {
                appUser = new AppUser();
                appUser.setAvatar(account.getHeadPortrait());
                appUser.setToken(account.getId() + "_" + DateUtil.getSysDateString());
                appUser.setUserId(account.getId());
                appUser.setName(account.getName());
                appUser.setUserType("1");
                appUser.setHxUserName(account.getHxUserName());
                appUser.setHxPassword(account.getHxPassWord());
                appUser.setNickName(account.getContactName());
                appUser.setFriendValidSwitch(account.getFriendPhoneSwitch());
                appUser.setAllPhoneSwitch(account.getAllPhoneSwitch());
                appUser.setFriendPhoneSwitch(account.getFriendPhoneSwitch());
            } else if (user != null) {
                appUser = new AppUser();
                appUser.setTouristType(user.getTouristType());
                appUser.setAvatar(user.getHeadPortrait());
                appUser.setToken(user.getId() + "_" + DateUtil.getSysDateString());
                appUser.setUserId(user.getId());
                appUser.setName(user.getName());
                appUser.setUserType("0");
                appUser.setHxUserName(user.getHxUserName());
                appUser.setHxPassword(user.getHxPassWord());
                appUser.setNickName(!StringUtil.isEmpty(user.getNickName()) ? user.getNickName() : user.getId());
                appUser.setFriendValidSwitch(user.getFriendPhoneSwitch());
                appUser.setAllPhoneSwitch(user.getAllPhoneSwitch());
                appUser.setFriendPhoneSwitch(user.getFriendPhoneSwitch());
            }

            // 追加公共部分信息
            if (appUser != null) {
                appUser.setEmail(user.getEmail());
                appUser.setPhone(user.getMobile());
                appUser.setStoreId(store.getId());
                appUser.setStoreRefer(store.getStoreRefer());
                appUser.setNotes(store.getNotes());
                appUser.setContactName(store.getContactName());
                appUser.setContactPhone(store.getContactPhone());
                appUser.setCompanyType(user.getType());
                appUser.setCompanyName(store != null ? store.getStoreName() : "");
                appUser.setSalered("3".equals(store.getSalerStatus()) ? "1" : "0");
                appUser.setBuyered("3".equals(store.getBuyerStatus()) ? "1" : "0");
                appUser.setTransed("3".equals(store.getTransStatus()) ? "1" : "0");
                appUser.setCommoned("3".equals(store.getCommonStatus()) ? "1" : "0");
            }

            // 不显示电话判断
            if (!appUser.getAllPhoneSwitch() || !appUser.getFriendPhoneSwitch()) {
                appUser.setPhone("");
            }
        } catch (Exception e) {
            log.error("没找到User或Account");
        }
        return appUser;
    }

    @Override
    public boolean updateAppUser(AppUser appUser) {
        if (appUser == null) {
            return false;
        }
        String userId = appUser.getUserId();
        try {
            User user = (User) userDao.get(userId);
            Account account = null;
            // 如果用户对象为空，则查找子账户
            if (user == null) {
                account = (Account) accountDao.get(userId);
            }
            // 暂时只有一个字段需要更新
            if (user != null) {
                user.setHeadPortrait(appUser.getAvatar());
                user.setHxUserName(appUser.getHxUserName());
                user.setHxPassWord(appUser.getHxPassword());
                user.setFriendPhoneSwitch(appUser.getFriendPhoneSwitch());
                user.setFriendValidSwitch(appUser.getFriendValidSwitch());
                user.setAllPhoneSwitch(appUser.getAllPhoneSwitch());
                ;
                // 同时更新时间
                user.setUpdateTime(DateUtil.getSysDateTimeString());
                userDao.updateBean(user);
                return true;
            } else if (account != null) {
                account.setHeadPortrait(appUser.getAvatar());
                account.setHxUserName(appUser.getHxUserName());
                account.setHxPassWord(appUser.getHxPassword());
                account.setFriendPhoneSwitch(appUser.getFriendPhoneSwitch());
                account.setFriendValidSwitch(appUser.getFriendValidSwitch());
                account.setAllPhoneSwitch(appUser.getAllPhoneSwitch());
                ;
                // 同时更新时间
                account.setUpdateTime(DateUtil.getSysDateTimeString());
                accountDao.updateBean(account);
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return false;
    }

    @Override
    public AppUser getAppUserByGroupId(String groupId) {
        try {
            Groups groups = (Groups) groupsDao.get(groupId);
            if (groups != null && !StringUtil.isEmpty(groups.getUserId())) {
                return getAppUserById(groups.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AppUser> getAllAppUsersNoRegHx() {
        List<AppUser> appUsers = new ArrayList<>();

        List<User> users = userDao.getAllUserNoRegHx();
        List<Account> accounts = accountDao.getAllAccountNoRegHx();

        if (users != null && users.size() > 0) {
            for (User user : users) {
                AppUser appUser = getAppUserById(user.getId());
                if (appUser != null) {
                    appUsers.add(appUser);
                }
            }
        }

        if (accounts != null && accounts.size() > 0) {
            for (Account account : accounts) {
                AppUser appUser = getAppUserById(account.getId());
                if (appUser != null) {
                    appUsers.add(appUser);
                }
            }
        }

        return appUsers;
    }

    public Boolean updateAllUserForCcb() {
        StringBuffer sql = new StringBuffer(" from User as user where 1=1");
        Map<String, Object> paramMap = new HashMap<>();
        try {
            List<User> users = userDao.list(sql.toString(), paramMap);
            for (User user : users) {
                if (!StringUtil.isEmpty(user.getCcbUserId())) {
                    continue;
                }
                user.setCcbUserId(CcbPayUtil.createCcbPayId());
                updateUser(user);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
}