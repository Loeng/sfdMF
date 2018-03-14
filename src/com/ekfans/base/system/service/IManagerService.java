package com.ekfans.base.system.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.ekfans.base.system.model.Manager;
import com.ekfans.pub.util.Pager;

/**
 * 系统后台用户Service接口
 * 
 * @ClassName: IManagerService
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IManagerService {

	/**
	 * 用户登陆操作，并保存登陆日志
	 * 
	 * @param name 用户名
	 * @param password 密码
	 * @param verifyCode_new 用户输入的验证码
	 * @param ip ip地址
	 * @param session
	 * @return 1:用户名不能为空，2:密码不能为空，3:密码长度6-32位，4:验证码不能为空，5:验证码错误，6:用户不存在，
	 *         7:密码错误，8:登陆失败，9:登陆成功
	 */
	public int sysUserLogin(String name, String password, String verifyCode, String ip, HttpSession session);

	/**
	 * 根据id查找管理员
	 */
	public Manager getManagerByID(String id);

	/**
	 * 添加管理员
	 * 
	 * @return 1:成功，2：失败，3：管理员名为空，4：管理员名不在4-20个字符内，5：密码为空
	 *         6：密码不在6-32个字符内，7：关联角色为空
	 */
	public int addManager(Manager manager);

	/**
	 * 查找管理员列表
	 * 
	 * @param pager 分页
	 * @param name 管理员名
	 * @param roleId 角色id
	 * @param status 状态
	 * @param mobile 手机
	 * @param email 电子邮箱
	 * @return List<Manager>
	 */
	public List<Manager> listManager(Pager pager, String name, String roleId, String status, String mobile, String email);

	/**
	 * 根据id删除管理员
	 * 
	 * @param ids id集合
	 * @return true:成功，false:失败
	 */
	public boolean deleteManager(String ids);

	/**
	 * 修改管理员
	 *
	 * @return 1:成功，2：失败，3：管理员名为空，4：管理员名不在4-20个字符内，5：密码为空
	 *         6：密码不在6-32个字符内，7：关联角色为空
	 */
	public int modifyManager(Manager manager);
}
