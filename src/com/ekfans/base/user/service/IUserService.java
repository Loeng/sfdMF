package com.ekfans.base.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ekfans.base.user.model.IntegralLog;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.model.UserInfo;
import com.ekfans.controllers.web.vo.NumBerAttribute;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.pub.util.Pager;

/**
 * 会员实现Service接口
 *
 * @author Lgy
 * @version 1.0
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @date 2014-1-3
 */
public interface IUserService {

    /**
     * @param type
     * @return User 返回类型
     * @throws
     * @Title: findUserByType
     * @Description: TODO(根据类型查询用户)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     */
    public List<User> findUserByType(String type);

    /**
     * 根据id删除用户
     *
     * @param id
     * @return
     */
    public boolean deleteUser(String id);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    public boolean addUser(User user, IntegralLog inter, HttpServletRequest request);

    /**
     * 根据id查找用户
     *
     * @param id
     * @return
     */
    public User getUser(String id);

    /**
     * 根据用户名查找用户
     */
    public User getUserByName(String name);

    /**
     * 根据用户名查找店铺用户
     *
     * @param name
     * @return
     */
    public User getStoreUserByName(String name);

    /**
     * 修改用户资料
     *
     * @param product
     */

    public boolean modifyUser(User user, IntegralLog inter, IntegralLog oldInter, String path);

    /**
     * 查询用户列表
     *
     * @param pager      分页
     * @param name       用户名
     * @param status     状态
     * @param mobile     手机号
     * @param email      电子邮箱
     * @param cardNumber 身份证号
     * @param nickName   昵称
     * @return
     */
    public List<User> listUser(Pager pager, String name, String status, String mobile, String email, String cardNumber, String nickName);

    /**
     * 更新会员对象
     *
     * @param user
     * @return
     * @throws Exception
     */
    public boolean updateUser(User user) throws Exception;

    public boolean updateMobile(User user);

    public boolean updateEmail(User user);

    /**
     * @param @param  nickName
     * @param @param  id
     * @param @return
     * @param @throws Exception    设定文件
     * @return boolean    返回类型
     * @throws
     * @Title: existNickName
     * @Description: TODO 判断昵称是否重复可用
     * 详细业务流程:
     * 通过查找拥有昵称 不同ID或者userId的对象是否存在来判断昵称是否重复可用
     */
    public boolean existNickName(String nickName, String id) throws Exception;

    /**
     * @param @return 设定文件
     * @return IntegralLog    返回类型
     * @throws
     * @Title: getIntegralbByUserId
     * @Description: TODO(根据用户名获取用户的积分信息)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     */
    public List<IntegralLog> getIntegralByUserId(String userId);

    /**
     * @param email
     * @return boolean 返回类型
     * @throws
     * @Title: judgmentRepeat
     * @Description: TODO(判断邮件或用户名或手机是否重复【重复：true, 不重复:false】)
     * 详细业务流程:
     * (在邮件注册是判断邮件是否重复)
     */
    public boolean judgmentRepeat(String email, String userName, String mobile);

    /**
     * 根据(用户名和用户类型)查账用户
     *
     * @param userName 用户名
     * @param type     用户类型
     * @return User
     */
    public User getUserName(String userName, String type);

    /**
     * 验证用户名是否重复
     *
     * @param userName
     * @return true：有该用户，false：没有
     */
    public Boolean checkUserName(String userName);

    /**
     * 修改时验证用户名时候重复
     *
     * @param oldName
     * @param newName
     * @return true：有该用户，false：没有
     */
    public Boolean checkUserNameUpdate(String oldName, String newName);

    /**
     * 大宗采购首页（获取企业用户和个人用户注册数量）
     *
     * @return
     */
    public NumBerAttribute getRegCustomerNumber();

    /**
     * 个人用户注册，供应商注册，采购商注册
     *
     * @param userName  用户名
     * @param password  密码
     * @param storeName 企业名称
     * @return
     */
    public Boolean saveUserOrStore(String userName, String password, String storeName, String type, String email, HttpServletRequest request);

    /**
     * 完善企业信息
     *
     * @param user
     * @param type
     * @param companyName
     * @param mail
     * @return
     */
    public Boolean supplementUser(User user, String type, String companyName, String mail);


    /**
     * 移动端注册
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public Boolean mobileReg(String userName, String password, HttpServletRequest request);

    /**
     * 激活用户
     *
     * @param name 用户名
     * @return
     */
    public Boolean activationUser(String name, String type);

    /**
     * 核心企业申请
     *
     * @param companyName  企业名称
     * @param unitType     单位类型
     * @param contactName  联系人姓名
     * @param contactSex   联系人性别
     * @param contactPhone 联系人电话
     * @param contactTime  联系时间
     * @param type         会员类型（0：个人会员，1：供应商，2：采购商，3：核心企业）
     * @return
     */
    public Boolean saveCoreStore(String companyName, String unitType, String contactName, String contactSex, String contactPhone, String contactTime, String type);

    public Boolean updateUserAndUserInfo(User user, UserInfo userInfo);

    /**
     * @return String 返回类型
     * @throws
     * @Title: phoneRandom
     * @Description: TODO(获取六位随机数)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     */
    public String phoneRandom(HttpSession session);

    /**
     * @param type
     * @return List<User> 返回类型
     * @throws
     * @Title: getUserByType
     * @Description: TODO(分局用户类型查询用户)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     */
    public List<User> getUserByType(String type);

    public int getUserNumber();

    /**
     * @param
     * @return
     * @throws
     * @Title: saveUser
     * @Description: TODO(新增企业)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     */
    public boolean saveUser(User user);

    /**
     * 更新第三方使用情况
     *
     * @param ids 包含多个id字符串，id采用逗号隔开
     * @param use
     * @return
     */
    public boolean updateUseData(Class clz, String ids, boolean use);

    /**
     * 从数据库得到User或者Account对象，包装成AppUser返回
     *
     * @param id
     * @param request
     * @return
     */
    public AppUser getAppUserById(String id);

    /**
     * 获取所有app未注册环信用户
     *
     * @return
     */
    public List<AppUser> getAllAppUsersNoRegHx();

    /**
     * 从数据库得到User或者Account对象，包装成AppUser返回
     *
     * @param id
     * @param request
     */
    public AppUser getAppUserByHxId(String id);

    /**
     * 更新User或者Account对象
     * 注意：这里是代理更新，只有部分字段做了更新！
     *
     * @param id
     * @return
     */
    public boolean updateAppUser(AppUser appUser);

    /**
     * 通过groupid找到用户对象
     *
     * @param groupId
     * @param request
     * @return
     */
    public AppUser getAppUserByGroupId(String groupId);

    public Boolean updateAllUserForCcb();
}
