package com.ekfans.base.system.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.log.service.ILoginLogService;
import com.ekfans.base.system.dao.IManagerDao;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 系统后台用户Service接口实现
 * 
 * @ClassName: ManagerDao
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class ManagerService implements IManagerService {
	
	private Logger log = LoggerFactory.getLogger(ManagerService.class);
	@Resource
	private IManagerDao managerDao;
	@Resource
	private ILoginLogService loginLogService;
	
	@SuppressWarnings("unchecked")
	@Override
	public int sysUserLogin(String name, String password, String verifyCode, String ip, HttpSession session) {
		// 1:用户名不能为空，2:密码不能为空，3:密码长度6-32位，4:验证码不能为空，5:验证码错误，6:用户不存在
		// 7:密码错误，8:登陆失败，9:登陆成功
		if(StringUtil.isEmpty(name)){
			return 1;
		}
		if(StringUtil.isEmpty(password)){
			return 2;
		}
		if(password.length() < 6 || password.length() > 32){
			return 3;
		}
		if(StringUtil.isEmpty(verifyCode)){
			return 4;
		}
		Object oldvfc = null;
		if(session != null){
			oldvfc = session.getAttribute("SESSION_SECURITY_CODE"); // 获取session中验证码
			verifyCode = verifyCode.trim(); // 去除验证码前后空格
		}
		// 校验验证码输入是否正确
		if(oldvfc != null && (oldvfc.toString()).equalsIgnoreCase(verifyCode)){
			// 定义参数MAP
			Map<String, Object> params = new HashMap<String, Object>();
			// 定义查询HQL
			String hql = "from Manager where name=:name and status=:status";
			// 设置参数
			params.put("name", name.trim());
			params.put("status", true);
			
			try {
				List<Manager> list = this.managerDao.list(hql, params);
				if(list != null && list.size() > 0){
					Manager su = list.get(0);
					// 验证密码是否正确
					String md5Password = new MD5Util().getMD5ofStr(password);
					if((su.getPassword()).equals(md5Password)){
						String note = "用户(" + su.getName() + ")于(" + DateUtil.getSysDateTimeString() + ")登陆";
						if (loginLogService.addLoginLog(su.getId(), ip, 1, 1, note)) {
							session.setAttribute(SystemConst.MANAGER, su); // 用户信息放入session
							return 9;
						}
						return 8;
					}
					return 7;
				}
				return 6;
			} catch (Exception e) {
				log.error("根据用户名查找用户错误，错误信息：" + e.getMessage());
				return 8;
			}
		}
		return 5;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Manager getManagerByID(String id) {
		// 如果传进来的id为空，则返回null
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		
		try {
			// 定义参数MAP
			Map<String, Object> params = new HashMap<String, Object>();
			// 定义查询HQL
			String hql = "select m,sr.name from Manager m,ShopRole sr where m.roleId=sr.id and m.id=:id";
			// 设置参数
			params.put("id", id);
			// 查询
			List<Object[]> list = managerDao.list(hql, params);
			if(list == null || list.size() <= 0){
				return null;
			}
			
			Object[] objA = list.get(0);
			Manager ma = (Manager)objA[0];
			ma.setRoleName(objA[1] == null ? "" : objA[1].toString());
			
			return ma;
		} catch (Exception e) {
			log.error("根据ID查找用户错误，错误信息：" + e.getMessage());
		}
		return null;
	}

	@Override
	public int addManager(Manager manager) {
		// 1:成功，2：失败，3：管理员名为空，4：管理员名不在4-20个字符内，5：密码为空
		// 6：密码不在6-32个字符内，7：关联角色为空
		if (manager == null) {
			return 2;
		}
		try {
			manager.setName(manager.getName().trim());
			if(StringUtil.isEmpty(manager.getName())){
				return 3;
			}
			if(manager.getName().length() < 4 || manager.getName().length() > 20){
				return 4;
			}
			if(StringUtil.isEmpty(manager.getPassword())){
				return 5;
			}
			if(manager.getPassword().length() < 6 || manager.getPassword().length() > 32){
				return 6;
			}
			if(StringUtil.isEmpty(manager.getRoleId())){
				return 7;
			}
			
			// 将密码MD5加密
			manager.setPassword(new MD5Util().getMD5ofStr(manager.getPassword()));
			// 设置创建时间为当前系统时间
			manager.setCreateTime(DateUtil.getSysDateTimeString());
			
			// 调用DAO方法添加管理员
			managerDao.addBean(manager);
			
			return 1;
		} catch (Exception e) {
			log.error("保存系统后台用户错误，错误信息：" + e.getMessage());
			return 2;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Manager> listManager(Pager pager, String name, String roleId, String status, String mobile, String email) {
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询HQL
		StringBuffer hql = new StringBuffer("from Manager manager where 1=1");
		
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			hql.append(" and manager.name like :name");
			paramMap.put("name", "%" + name + "%");
		}
		// 如果查询条件输入了roleId，添加查询条件
		if (!StringUtil.isEmpty(roleId)) {
			hql.append(" and manager.roleId=:roleId");
			paramMap.put("roleId", roleId);
		}
		// 如果查询条件输入了status，添加查询条件
		if (!StringUtil.isEmpty(status)) {
			// 添加查询条件
			hql.append(" and manager.status=:status");
			paramMap.put("status", Boolean.parseBoolean(status));
		}
		// 如果查询条件输入了mobile，添加查询条件
		if (!StringUtil.isEmpty(mobile)) {
			hql.append(" and manager.mobile like :mobile");
			paramMap.put("mobile", "%" + mobile + "%");
		}
		// 如果查询条件输入了mobile，添加查询条件
		if (!StringUtil.isEmpty(email)) {
			hql.append(" and manager.email like :email");
			paramMap.put("email", "%" + email + "%");
		}
		//添加时间倒序排列
		hql.append(" order by manager.createTime desc");
		
		try {
			// 调用DAO方法查找管理员
			return managerDao.list(pager, hql.toString(), paramMap);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean deleteManager(String ids) {
		// 如果传进来的id为空，则返回false
		if (StringUtil.isEmpty(ids)) {
			return false;
		}
		
		try {
			StringBuffer hql = new StringBuffer("delete from Manager where id in(");
			if(ids.indexOf(",") >= 0){
				String[] idA = ids.split(",");
				for (int i = 0; i < idA.length; i++) {
					hql.append((i == 0 ? "'" + idA[i] : ",'" + idA[i]) + "'");
				}
			}else{
				hql.append("'" + ids + "'");
			}
			hql.append(")");
			
			// 调用DAO方法删除管理员
			managerDao.delete(hql.toString(), new HashMap<String, Object>());
			
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 修改管理员
	 * 
	 * @param manager
	 *            管理员对象
	 * @return
	 */
	@Override
	public int modifyManager(Manager manager) {
		// 1:成功，2：失败，3：管理员名为空，4：管理员名不在4-20个字符内，5：密码为空
		// 6：密码不在6-32个字符内，7：关联角色为空
		if (manager == null) {
			return 2;
		}
		// 设置修改时间为当前系统时间
		manager.setUpdateTime(DateUtil.getSysDateTimeString());
		
		try {
			manager.setName(manager.getName().trim());
			if(StringUtil.isEmpty(manager.getName())){
				return 3;
			}
			if(manager.getName().length() < 4 || manager.getName().length() > 20){
				return 4;
			}
			if(StringUtil.isEmpty(manager.getPassword())){
				return 5;
			}
			if(manager.getPassword().length() < 6 || manager.getPassword().length() > 32){
				return 6;
			}
			if(StringUtil.isEmpty(manager.getRoleId())){
				return 7;
			}
			
			// 找到数据库中该管理员
			Manager man = (Manager) managerDao.get(manager.getId());
			// 比对密码，如果改动了，重新加密
			if (!man.getPassword().equals(manager.getPassword())) {
				// 将密码MD5加密
				manager.setPassword(new MD5Util().getMD5ofStr(manager.getPassword()));
			}
			// 调用DAO方法修改管理员
			managerDao.updateBean(manager);
			
			return 1;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return 2;
	}

}
