package com.ekfans.plugin.wuliubao.yunshu.base.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 物流宝app运输端用户信息相关业务处理接口
 * @author pp
 * @Date 2017年6月30日15:20:26
 */
public interface IWlbAppUserService {
    
	/**
	 * 用户注册
	 * @param type 
	 * @param 
	 * @return '1' 注册成功  '2' 手机号已注册  '3' 验证码不正确 '4' 密码为空 '5' 手机号为空  '6' 密码强度不够   '7'密码长度超过20字符  '0' 运行期异常
	 * @throws Exception 
	 */
	String register(String password, String SecurityCode,String mobile, String type) throws Exception;
	
	/**
	 * 用户登录
	 * @param 
	 * @return '0' 未确认登录类型 '${token}' 登录成功 '2'用户名密码不正确 '3' 验证码不正确 '4' 密码为空 '5' 手机号或用户名为空   '6' 用户类型不正确
	 * @throws Exception 
	 */
	String login(String type,String password, String SecurityCode,String mobile,String registrationID,String userType) throws Exception;
    
	/**
	 * 用户修改密码
	 * @param newpassword
	 * @param SecurityCode
	 * @param mobile
	 * @return  '1' 修改成功 '2'手机号未注册  '3' 验证码不正确  '4' 密码为空  '5' 手机号为空  '6' 密码强度不够
	 * @throws Exception 
	 */
	String modifyPW(String newpassword, String SecurityCode,String mobile) throws Exception;
	
	/**
	 *  用户登录时修改密码
	 * @param newpassword
	 * @param password
	 * @param request
	 * @return  "0" 用户不存在  "1" 修改成功  "2"  密码错误
 	 * @throws Exception 
	 */
	String logModifyPW(String newpassword,String password,HttpServletRequest request) throws Exception;
	
	/**
	 * 编辑用户资料:上传图像 修改昵称 修改联系电话
	 * @param request
	 * @param stream
	 * @param mobile
	 * @param nickName
	 * @return
	 */
	Map<String,Object> editUser(HttpServletRequest request,String safeMobile,String nickName) throws Exception;
	/**
	 * 上传图片
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> FileUpload(String stream,HttpServletRequest request) throws Exception;

}
