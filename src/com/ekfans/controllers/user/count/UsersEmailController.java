package com.ekfans.controllers.user.count;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.message.util.MessageUtil;
import com.ekfans.pub.util.EncryptionAndDecryption;
import com.ekfans.pub.util.StringUtil;
@Controller
public class UsersEmailController extends BasicController {
    @Autowired
    private IUserService userService;   
    @Autowired
    private IUserDao userDao;
    /**
     * 
    * @Title: toUpdate
    * @Description: TODO(跳转到修改页面)
    * 详细业务流程:
    * (跳转到修改页面)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/user/count/email/toUpdateUserEmail")
    public String toUpdate(HttpServletRequest request ,HttpSession session) {
		User u = (User)session.getAttribute(SystemConst.USER);
		User user = userService.getUser(u.getId());
		// 给前端视图传递设置成功的参数
		request.setAttribute("parentPurviewId", "account");
		request.setAttribute("purviewId","toUpdateUserEmail");
		request.setAttribute("email",user.getEmail());
		//去往更新Email的页面
	        return "/userCenter/customer/count/toUpdateUserEmail";
    }
    
    /**
     * 
    * @Title: userSendMail
    * @Description: TODO(给验证的邮箱发送一封邮件)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/user/sendCheckEmail")
    public String userSendMail(HttpServletRequest request,HttpSession session){
	// 获取需要验证的邮箱
	String email = request.getParameter("email");
	
	String emailTitle = "找回密码";
	String emailContent = 
	    "重置密码连接："+getBasePath()+"/web/user/resetPwdUrl?address="+email+"&type=email";
	//发送邮件
	MessageUtil.sendMail(email, emailTitle, emailContent);
	return "/userCenter/customer/count/validateUserEmail";
    }   
    
    /**
     * 修改完成，保存操作
    * @Title: update
    * @Description: TODO(修改完成，保存操作)
    * 详细业务流程:
    * (修改完成，保存操作)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/user/count/email/updateUserEmail") 
    public String update(HttpServletRequest request , HttpSession session) {
	try
	{
	    // 获取登陆成功的用户对象
	    User u = (User)session.getAttribute(SystemConst.USER);
	    User user = userService.getUser(u.getId());
	    //通过请求获取email
	    String email = request.getParameter("email");
	    if(StringUtil.isEmpty(email)){
	        request.setAttribute("updateOk",false);
	        request.setAttribute("email",user.getEmail());
	        return "/userCenter/customer/count/toUpdateUserEmail";
	    }
	    //给email字段赋值
	    user.setEmail(email);
	    //调用Service更新数据库
	    if(userService.updateEmail(user)) {
		request.setAttribute("updateOk",true);
		request.setAttribute("email",email);
		request.setAttribute("parentPurviewId", "account");
		request.setAttribute("purviewId","toUpdateUserEmail");
		return "/userCenter/customer/count/validateUserEmail";
	    }
	}
	catch(Exception e)
	{
	    e.printStackTrace();
	}
	return "error";
    }
    /**
     * @Title: checkEmail
     * @Description: TODO(验证前台邮箱)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param request
     * @param session
     * @return String 返回类型
     * @throws
      */
     @RequestMapping (value="/user/security/checkEmail" ,method=RequestMethod.POST)
     @ResponseBody
     public String checkEmail(HttpServletRequest request,HttpSession session){
         User user = (User) session.getAttribute(SystemConst.USER);
         //根据userid更改数据库密码
         try {
             user = (User) userDao.get(user.getId());
             String email  =  request.getParameter("email");
             String newemail = request.getParameter("newEmail");
             //判断邮箱是否为空
             if(email!=null && email!=""){
                 //判断输入邮箱是否相同
                 if(email.equals(user.getEmail())){
                     user.setEmailValiDate(email);
                 }
             }else{
                user.setEmailValiDate(newemail);
             }
             userDao.updateBean(user);
             //发送链接到邮箱,
             return "1";
         } catch (Exception e) {
             e.printStackTrace();
         }
         return "0";
     }
     /**
      * @Title: EmailValidate
      * @Description: TODO(用户点击邮箱链接激活邮箱)
      * 详细业务流程:
      * (详细描述此方法相关的业务处理流程)
      * @param request
      * @return String 返回类型
      * @throws
       */
      @RequestMapping(value="/user/security/emailValidate")
      public String EmailValidate(HttpServletRequest request){
          //获取激活码
          String verificationCode = request.getParameter("verificationCode");
          //获取时间
          String time = request.getParameter("time");
          //获取用户ID
          String id = request.getParameter("userId");
          //解密时间
          time = EncryptionAndDecryption.decrypt(SystemConst.DESKEY, time);
          //解密激活码
          verificationCode = EncryptionAndDecryption.decrypt(SystemConst.DESKEY, verificationCode);
          //解密用户ID
          id = EncryptionAndDecryption.decrypt(SystemConst.DESKEY, id);
          DateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
          try {
              //获取邮箱中的时间
              Date old_Date = fmtDateTime.parse(time);
              //获取当前时间
              Calendar cal = Calendar.getInstance();
              cal.setTime(old_Date);
              //邮箱时间+1天
              cal.add(Calendar.DATE, 1);
             
              //如果邮箱时间+1天小于当前时间则过期
              if(new Date().compareTo(cal.getTime())>0){
                  //返回到过期页面
                  request.setAttribute("mark", 3);
              }
              User user =(User) userDao.get(id);
              user.setEmail(user.getEmailValiDate());
              userDao.updateBean(user);
              request.setAttribute("mark", 1);
              request.setAttribute("name", user.getEmailValiDate());
          } catch (Exception e) {
              e.printStackTrace();
          }
          request.setAttribute("jud", 1);
          //返回激活页面
          return "/web/purchase/reg/storeReg_three";
      }
}
