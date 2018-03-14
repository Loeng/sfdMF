package com.ekfans.base.finance.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.finance.dao.IBankPurviewDao;
import com.ekfans.base.finance.model.BankPurview;
import com.ekfans.pub.util.StringUtil;

/**
 * 企业权限功能菜单Service接口实现
 * 
 * @ClassName: StorePurviewService
 * @author Lgy
 * @date 2014-03-21 上午11:35:38 
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("unchecked")
public class BankPurviewService implements IBankPurviewService {

	@Autowired
	IBankPurviewDao bankPurviewDao;

	/**
	 * 根绝角色ID获取店铺所对应的权限
	 */
	@Override
	public List<BankPurview> getBankPurviewByRoleIdAndType(String roleId, Boolean type) {
		// 如果传进来的角色ID为空，则返回null
		if (StringUtil.isEmpty(roleId)) {
			return null;
		}

		// 定义SQL参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写SQL语句，关联 StorePurview表和 StorePuviewRel表
		StringBuffer sql = new StringBuffer("select sp from BankPurview as sp,BankPurviewRel as spr where 1=1");

		sql.append(" and spr.roleId = :roleId");
		paramMap.put("roleId", roleId);

		// 将两个表关联起来
		sql.append(" and spr.purviewId = sp.id");

		// 状态为正常显示的权限
		sql.append(" and sp.status = :status");
		paramMap.put("status", true);

		sql.append(" and spr.type = :type");
		paramMap.put("type", type);

		// 按照级别倒序，排序位置升序排序
		sql.append(" order by sp.level desc,sp.position asc");

		try {
			// 调用方法查询SQL
			List<BankPurview> purviewList = bankPurviewDao.list(sql.toString(), paramMap);

			// 如果查询出来的集合为空，或者长度为0，则返回null
			if (purviewList == null || purviewList.size() <= 0) {
				return null;
			}

			List<BankPurview> returnList = new ArrayList<BankPurview>();
			// 定义一个MAP用来遍历集合
			Map<String, BankPurview> map = new HashMap<String, BankPurview>();

			// 循环查询出的list
			for (int i = 0; i < purviewList.size(); i++) {
				// 获取权限对象
				BankPurview purView = (BankPurview) purviewList.get(i);

				// 如果获取的对象为空，则跳过
				if (purView == null) {
					continue;
				}

				if (StringUtil.isEmpty(purView.getParentId())) {
					// 如果Map中已经存在该对象，则将该对象的ChildList放入 purview中，并将purView重新放入Map
					if (map.containsKey(purView.getId())) {
						// 从Map中的对象中取出ChildList并放入purView的ChildList中
						purView.setChildList(map.get(purView.getId()).getChildList());
						// 从Map中移除该权限对象
						map.remove(purView.getId());
					}
					// 将该对象放入返回集合中
					returnList.add(purView);
				} else {

					// 判断Map中是否存在该对象，如果存在，则将Map中对象的childList取出放入开对象中
					if (map.containsKey(purView.getId())) {
						purView.setChildList(map.get(purView.getId()).getChildList());
					}

					// 如果该对象的父ID不为空，则判断map中存在该对象的父对象，则将该对象放入父对象的ChildList中。
					// 否则，则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					BankPurview parentPurview = null;
					// 如果map中存在该对象的父对象则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					if (map.containsKey(purView.getParentId())) {
						// 从Map中获取该对象的父对象
						parentPurview = map.get(purView.getParentId());
						// 如果获取的父对象不为空，则将该对象放入父对象的ChildList中
						if (parentPurview != null) {
							parentPurview.getChildList().add(purView);
						}
						map.remove(parentPurview.getId());
						// 将该父对象放入MAP中
						map.put(parentPurview.getId(), parentPurview);
					} else {
						// 创建伪父对象
						parentPurview = new BankPurview();
						// 将该对象放入父对象的ChildList中
						parentPurview.getChildList().add(purView);
						// set 父对象的ID
						parentPurview.setId(purView.getParentId());
						// 将该父对象放入MAP中
						map.put(parentPurview.getId(), parentPurview);
					}
				}
			}

			return returnList;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 显示出所有的店铺角色权限
	 */
	public List<BankPurview> bankPurviewList() {

		// 定义SQL参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写SQL语句，关联 StorePurview表和 StorePuviewRel表
		StringBuffer sql = new StringBuffer("select sp from BankPurview as sp where 1=1");

		// 状态为正常显示的权限
		sql.append(" and sp.status = :status");
		paramMap.put("status", true);

		// 按照级别倒序，排序位置升序排序
		sql.append(" order by sp.level desc,sp.position asc");

		try {
			// 调用方法查询SQL
			List<BankPurview> purviewList = bankPurviewDao.list(sql.toString(), paramMap);

			// 如果查询出来的集合为空，或者长度为0，则返回null
			if (purviewList == null || purviewList.size() <= 0) {
				return null;
			}

			List<BankPurview> returnList = new ArrayList<BankPurview>();
			// 定义一个MAP用来遍历集合
			Map<String, BankPurview> map = new HashMap<String, BankPurview>();

			// 循环查询出的list
			for (int i = 0; i < purviewList.size(); i++) {
				// 获取权限对象
				BankPurview purView = (BankPurview) purviewList.get(i);

				// 如果获取的对象为空，则跳过
				if (purView == null) {
					continue;
				}

				if (StringUtil.isEmpty(purView.getParentId())) {
					// 如果Map中已经存在该对象，则将该对象的ChildList放入 purview中，并将purView重新放入Map
					if (map.containsKey(purView.getId())) {
						// 从Map中的对象中取出ChildList并放入purView的ChildList中
						purView.setChildList(map.get(purView.getId()).getChildList());
						// 从Map中移除该权限对象
						map.remove(purView.getId());
					}
					// 将该对象放入返回集合中
					returnList.add(purView);
				} else {

					// 判断Map中是否存在该对象，如果存在，则将Map中对象的childList取出放入开对象中
					if (map.containsKey(purView.getId())) {
						purView.setChildList(map.get(purView.getId()).getChildList());
					}

					// 如果该对象的父ID不为空，则判断map中存在该对象的父对象，则将该对象放入父对象的ChildList中。
					// 否则，则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					BankPurview parentPurview = null;
					// 如果map中存在该对象的父对象则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					if (map.containsKey(purView.getParentId())) {
						// 从Map中获取该对象的父对象
						parentPurview = map.get(purView.getParentId());
						// 如果获取的父对象不为空，则将该对象放入父对象的ChildList中
						if (parentPurview != null) {
							parentPurview.getChildList().add(purView);
						}
						map.remove(parentPurview.getId());
						// 将该父对象放入MAP中
						map.put(parentPurview.getId(), parentPurview);
					} else {
						// 创建伪父对象
						parentPurview = new BankPurview();
						// 将该对象放入父对象的ChildList中
						parentPurview.getChildList().add(purView);
						// set 父对象的ID
						parentPurview.setId(purView.getParentId());
						// 将该父对象放入MAP中
						map.put(parentPurview.getId(), parentPurview);
					}
				}
			}

			return returnList;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 用于修改的得到整个权限的页面
	 */
	public List<BankPurview> bPurviewList(String roleId, Boolean type, String parentRoleId, Boolean parentType) {
		// 定义SQL参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写SQL语句，关联 StorePurview表和 StorePuviewRel表
		StringBuffer sql = new StringBuffer("select sp from BankPurview as sp");

		if (!StringUtil.isEmpty(parentRoleId)) {
			sql.append(" ,BankPurviewRel as rel");
		}
		sql.append(" where 1=1");

		if (!StringUtil.isEmpty(parentRoleId)) {
			sql.append(" and sp.id = rel.purviewId");
			sql.append(" and rel.roleId = :parentRoleId");
			paramMap.put("parentRoleId", parentRoleId);
			sql.append(" and rel.type = :parentType");
			paramMap.put("parentType", parentType);
		}

		// 状态为正常显示的权限
		sql.append(" and sp.status = :status");
		paramMap.put("status", true);

		// 按照级别倒序，排序位置升序排序
		sql.append(" order by sp.level desc,sp.position asc");
		// 定义SQL参数Map
		Map<String, Object> paramMap2 = new HashMap<String, Object>();

		// 拼写SQL语句，关联 StorePurview表和 StorePuviewRel表
		StringBuffer sql2 = new StringBuffer("select sp from BankPurview as sp,BankPurviewRel as spr where 1=1");

		sql2.append(" and spr.roleId = :roleId");
		paramMap2.put("roleId", roleId);

		// 将两个表关联起来
		sql2.append(" and spr.purviewId = sp.id");

		// 状态为正常显示的权限
		sql2.append(" and sp.status = :status");
		paramMap2.put("status", true);

		sql2.append(" and spr.type = :type");
		paramMap2.put("type", type);

		// 按照级别倒序，排序位置升序排序
		sql2.append(" order by sp.level desc,sp.position asc");
		try {
			// 调用方法查询SQL
			List<BankPurview> purviewList = bankPurviewDao.list(sql.toString(), paramMap);
			// 调用方法得到根据roleID得到的权限集合
			List<BankPurview> havePurviewList = bankPurviewDao.list(sql2.toString(), paramMap2);
			// 循环比较用户的id是否选中，如果选中给check属性赋值true
			if (havePurviewList != null) {
				for (BankPurview sp : purviewList) {
					for (BankPurview haveSp : havePurviewList) {
						if (haveSp.getId().equals(sp.getId())) {
							sp.setChecked(true);
						}
					}
				}
			}
			// 如果查询出来的集合为空，或者长度为0，则返回null
			if (purviewList == null || purviewList.size() <= 0) {
				return null;
			}

			List<BankPurview> returnList = new ArrayList<BankPurview>();
			// 定义一个MAP用来遍历集合
			Map<String, BankPurview> map = new HashMap<String, BankPurview>();

			// 循环查询出的list
			for (int i = 0; i < purviewList.size(); i++) {
				// 获取权限对象
				BankPurview purView = (BankPurview) purviewList.get(i);

				// 如果获取的对象为空，则跳过
				if (purView == null) {
					continue;
				}

				if (StringUtil.isEmpty(purView.getParentId())) {
					// 如果Map中已经存在该对象，则将该对象的ChildList放入 purview中，并将purView重新放入Map
					if (map.containsKey(purView.getId())) {
						// 从Map中的对象中取出ChildList并放入purView的ChildList中
						purView.setChildList(map.get(purView.getId()).getChildList());
						// 从Map中移除该权限对象
						map.remove(purView.getId());
					}
					// 将该对象放入返回集合中
					returnList.add(purView);
				} else {

					// 判断Map中是否存在该对象，如果存在，则将Map中对象的childList取出放入开对象中
					if (map.containsKey(purView.getId())) {
						purView.setChildList(map.get(purView.getId()).getChildList());
					}

					// 如果该对象的父ID不为空，则判断map中存在该对象的父对象，则将该对象放入父对象的ChildList中。
					// 否则，则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					BankPurview parentPurview = null;
					// 如果map中存在该对象的父对象则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					if (map.containsKey(purView.getParentId())) {
						// 从Map中获取该对象的父对象
						parentPurview = map.get(purView.getParentId());
						// 如果获取的父对象不为空，则将该对象放入父对象的ChildList中
						if (parentPurview != null) {
							parentPurview.getChildList().add(purView);
						}
						map.remove(parentPurview.getId());
						// 将该父对象放入MAP中
						map.put(parentPurview.getId(), parentPurview);
					} else {
						// 创建伪父对象
						parentPurview = new BankPurview();
						// 将该对象放入父对象的ChildList中
						parentPurview.getChildList().add(purView);
						// set 父对象的ID
						parentPurview.setId(purView.getParentId());
						// 将该父对象放入MAP中
						map.put(parentPurview.getId(), parentPurview);
					}
				}
			}

			return returnList;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param roleId
	 * @param purviewId
	 * @return
	 */
	public BankPurview getPurviewById(String roleId, String purviewId) {
		if (StringUtil.isEmpty(purviewId)) {
			return null;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer("select purview from BankPurview as purview");
		if (!StringUtil.isEmpty(roleId)) {
			sql.append(",BankPurviewRel as rel");
		}

		sql.append(" where 1=1");
		sql.append(" and purview.id = :purviewId");
		paramMap.put("purviewId", purviewId);

		if (!StringUtil.isEmpty(roleId)) {
			sql.append(" and rel.purviewId = purview.id");
			sql.append(" and rel.roleId = :roleId");
			paramMap.put("roleId", roleId);
		}

		try {
			List<BankPurview> list = bankPurviewDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	public BankPurview getPurviewById(String purviewId) {
		if (StringUtil.isEmpty(purviewId)) {
			return null;
		}

		try {
			return (BankPurview) bankPurviewDao.get(purviewId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
