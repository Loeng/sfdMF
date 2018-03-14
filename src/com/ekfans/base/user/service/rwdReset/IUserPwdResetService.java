package com.ekfans.base.user.service.rwdReset;

import javax.servlet.http.HttpSession;

import com.ekfans.base.user.model.User;

/**
 * 
* @ClassName: IUserPwdResetService
* @Description: TODO(前台密码重置service)
* @author 成都易科远见科技有限公司
* @date 2014-5-18 上午12:20:29
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IUserPwdResetService {
    /**
     * 
    * @Title: resetPWD
    * @Description: TODO(密码重置操作)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param userAccount
    * @param @param newPassword
    * @param @param pwdStrong
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean resetPWD(String userAccount,String userAccountType,String newPassword,String pwdStrong);
    
    /**
     * 
    * @Title: testAccountForPhone
    * @Description: TODO(验证此手机号对应的账户是否存在)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param phone
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean testAccountForPhone(String phone); 
    
    /**
     * 
    * @Title: testAccountForEmail
    * @Description: TODO(验证此邮箱对应的账户是否存在)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param email
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean testAccountForEmail(String email);
    
    /**
     * 
    * @Title: emailReset
    * @Description: TODO(重置密码)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param email
    * @param @return    设定文件
    * @return User    返回类型
    * @throws
     */
    public User emailReset(String email);
    
    /**
     * 
    * @Title: sendCommand
    * @Description: TODO(给用户的手机发送一个验证码)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param phone
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean sendCommand(String phone,HttpSession session);
    
    /**
     * 
    * @Title: checkYZM
    * @Description: TODO(对用户的验证码进行效验)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param uyzm
    * @param @param session
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean checkYZM(String uyzm,HttpSession session);
    
    /**
     * 用户找回密码操作（这是重置密码）
     * @param user
     */
    public void updateTempPwd(User user);
}
