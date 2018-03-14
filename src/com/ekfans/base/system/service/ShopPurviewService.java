package com.ekfans.base.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.dao.IShopPurviewDao;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.model.ShopPurview;
import com.ekfans.pub.util.StringUtil;

/**
 * 系统权限菜单Service接口实现
 * 
 * @ClassName: ShopPurviewService
 * @author Lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class ShopPurviewService implements IShopPurviewService {
	
	private Logger log = LoggerFactory.getLogger(ShopPurviewService.class);
	@Resource
	private IShopPurviewDao shopPurviewDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ShopPurview> getAllShopPurview() {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = "from ShopPurview where status=:status order by level asc,position asc";
		map.put("status", true);
		
		try {
			return shopPurviewDao.list(hql, map);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ShopPurview> getPurviewsByRoleIdOrMark(String roleId) {
		if (roleId == null) {
			return null;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		String hql1 = "from ShopPurview where status=:status order by level asc,position asc";
		String hql2 = "select sp from ShopPurview sp,RolePurviewRel rpr where sp.status=:status and sp.id=rpr.purviewId and rpr.roleId=:roleId order by sp.level asc,sp.position asc";
		map.put("status", true);
		map.put("roleId", roleId);
		
		try {
			// 查询所有权限查单
			List<ShopPurview> list1 = shopPurviewDao.list(hql1, map);
			// 查询角色所属权限菜单
			List<ShopPurview> list2 = shopPurviewDao.list(hql2, map);
			
			// 在所有权限查单中标记用户选中的权限查单
			if(list2 != null && list2.size() > 0){
				for (ShopPurview sp1 : list1) {
					String id1 = sp1.getId();
					for (ShopPurview sp2 : list2) {
						String id2 = sp2.getId();
						if(id1.equals(id2)){
							sp1.setChecked(true);
							break;
						}
					}
				}
			}
			return list1;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据管理员获取管理员权限的菜单
	 * 
	 * @param manager
	 * @return
	 */
	public List<ShopPurview> getPurviewsByManager(Manager manager, String topId) {
		// 如果传进来的管理员对象为空，则返回null
		if (manager == null) {
			return null;
		}

		// 定义返回LIST
		List returnList = new ArrayList();

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select sp from ShopPurview as sp,RolePurviewRel as rpr where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 角色ID与权限的关系
		sql.append(" and rpr.roleId = :roleId");
		paramMap.put("roleId", manager.getRoleId());

		// 建立权限与关系表的关系
		sql.append(" and sp.id = rpr.purviewId");

		// 如果传过来的首级ID不为空，则加入首级ID的条件
		if (!StringUtil.isEmpty(topId)) {
			sql.append(" and sp.topId=:topId");
			paramMap.put("topId", topId);
		}

		// 权限的状态是放开
		sql.append(" and sp.status = '1'");

		// 根据位置进行排序
		sql.append(" order by sp.level desc,sp.position asc");

		try {
			// 调用DAO执行SQL获取返回LIST
			List shopPurviewList = shopPurviewDao.list(sql.toString(), paramMap);

			// 如果数据库查出的数据为空，则返回空
			if (shopPurviewList == null || shopPurviewList.size() <= 0) {
				return returnList;
			}

			// 定义一个MAP用来遍历集合
			Map<String, ShopPurview> map = new HashMap<String, ShopPurview>();
			// 循环查询出的list
			for (int i = 0; i < shopPurviewList.size(); i++) {
				// 获取权限对象
				ShopPurview purView = (ShopPurview) shopPurviewList.get(i);

				// 如果获取的对象为空，则跳过
				if (purView == null) {
					continue;
				}

				if (!StringUtil.isEmpty(purView.getParentId()) && !StringUtil.isEmpty(topId) && topId.equals(purView.getParentId())) {
					// 如果Map中已经存在该对象，则将该对象的ChildList放入 purview中，并将purView重新放入Map
					if (map.containsKey(purView.getId())) {
						// 从Map中的对象中取出ChildList并放入purView的ChildList中
						purView.setChildList(map.get(purView.getId()).getChildList());
						// 从Map中移除该权限对象
						map.remove(purView.getId());
					}
					// 将该对象放入返回集合中
					returnList.add(purView);
					continue;
				} else {

					// 判断Map中是否存在该对象，如果存在，则将Map中对象的childList取出放入开对象中
					if (map.containsKey(purView.getId())) {
						purView.setChildList(map.get(purView.getId()).getChildList());
					}

					// 如果该对象的父ID不为空，则判断map中存在该对象的父对象，则将该对象放入父对象的ChildList中。
					// 否则，则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					ShopPurview parentPurview = null;
					// 如果map中存在该对象的父对象则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					if (map.containsKey(purView.getParentId())) {
						// 从Map中获取该对象的父对象
						parentPurview = map.get(purView.getParentId());
						// 如果获取的父对象不为空，则将该对象放入父对象的ChildList中
						if (parentPurview != null) {
							parentPurview.getChildList().add(purView);
						}
					} else {
						// 创建伪父对象
						parentPurview = new ShopPurview();
						// 将该对象放入父对象的ChildList中
						parentPurview.getChildList().add(purView);
						// set 父对象的ID
						parentPurview.setId(purView.getParentId());
						// 将该父对象放入MAP中
						map.put(parentPurview.getId(), parentPurview);
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnList;
	}

	/**
	 * 根据管理员获取管理员权限的菜单
	 * 
	 * @param manager
	 * @return
	 */
	public List<ShopPurview> getTopPurviewsByManager(Manager manager) {
		// 如果传进来的管理员对象为空，则返回null
		if (manager == null) {
			return null;
		}

		// 定义返回LIST
		List returnList = new ArrayList();

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select sp from ShopPurview as sp,RolePurviewRel as rpr where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 角色ID与权限的关系
		sql.append(" and rpr.roleId = :roleId");
		paramMap.put("roleId", manager.getRoleId());

		// 建立权限与关系表的关系
		sql.append(" and sp.id = rpr.purviewId");

		// 权限的状态是放开
		sql.append(" and sp.status = '1'");

		// 仅获取第一级菜单
		sql.append(" and (sp.parentId = '' or  sp.parentId = ' ' or sp.parentId = null)");

		// 根据位置进行排序
		sql.append(" order by sp.level desc,sp.position asc");

		try {
			// 调用DAO执行SQL获取返回LIST
			returnList = shopPurviewDao.list(sql.toString(), paramMap);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnList;
	}

	/**
	 * 将Map对象的Value转换成List
	 * 
	 * @param map
	 * @return
	 */
	public static List<ShopPurview> mapByList(Map<String, ShopPurview> map) {
		List<ShopPurview> list = new ArrayList<ShopPurview>();
		Set<Entry<String, ShopPurview>> set = map.entrySet();
		Iterator<Entry<String, ShopPurview>> it = set.iterator();
		// 将map对象里面的属性循环遍历出来
		while (it.hasNext()) {
			Entry<String, ShopPurview> entry = it.next();
			// 得到value值，装到list里面，也可以entry.getKey()。
			// 如果2个都需要装。可以弄成一个对象来装
			list.add(entry.getValue());
		}
		return list;
	}

	/**
	 * 根据管理员和上级菜单获取管理员权限的下级菜单
	 * 
	 * @param manager
	 * @return
	 */
	public List getChildPurViewsByManager(Manager manager, String parentId) {
		// 如果传进来的管理员对象为空，则返回null
		if (manager == null) {
			return null;
		}

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select sp from ShopPurview as sp,RolePurviewRel as rpr where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 角色ID与权限的关系
		sql.append(" and rpr.roleId = :roleId");
		paramMap.put("roleId", manager.getRoleId());

		// 建立权限与关系表的关系
		sql.append(" and sp.id = rpr.purviewId");

		// 如果传进来的父ID不为空，则加上父ID条件
		if (!StringUtil.isEmpty(parentId)) {
			sql.append(" and sp.parentId = :parentId");
			paramMap.put("parentId", parentId);
		} else {
			sql.append(" and (sp.parentId is null or sp.parentId = '' or sp.parentId = ' ')");
		}

		// 权限的状态是放开
		sql.append(" and sp.status = '1'");

		// 根据位置进行排序
		sql.append(" order by sp.position asc");
		try {
			// 调用DAO执行SQL获取返回LIST
			List shopPurviewList = shopPurviewDao.list(sql.toString(), paramMap);
			return shopPurviewList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 查询出所有一级菜单
	 */
	public List<ShopPurview> getFristPurViews() {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select sp from ShopPurview as sp where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 角色ID与权限的关系
		sql.append(" and sp.level = 1");
		try {
			List<ShopPurview> shopPurviews = shopPurviewDao.list(sql.toString(), paramMap);
			return shopPurviews;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询出对应的子集菜单
	 */
	public List<ShopPurview> getChildPurViews(String id) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select sp from ShopPurview as sp where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 角色ID与权限的关系
		if (!StringUtil.isEmpty(id)) {
			sql.append(" and sp.parentId = :id");
			paramMap.put("id", id);
		}
		try {
			List<ShopPurview> shopPurviews = shopPurviewDao.list(sql.toString(), paramMap);
			return shopPurviews;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用于修改的根据roleId得到权限集合
	 */
	public List<ShopPurview> getShopPurviewbyRoleId(String roleId) {
		// 定义返回LIST
		List returnList = new ArrayList();

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select sp from ShopPurview as sp where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 权限的状态是放开
		sql.append(" and sp.status = '1'");

		// 根据位置进行排序
		sql.append(" order by sp.level desc,sp.position asc");
		// 如果传进来的管理员对象为空，则返回null
		if (roleId == null) {
			return null;
		}

		// 定义查询SQL
		StringBuffer sql2 = new StringBuffer(" select sp from ShopPurview as sp,RolePurviewRel as rpr where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap2 = new HashMap<String, Object>();

		// 角色ID与权限的关系
		sql2.append(" and rpr.roleId = :roleId");
		paramMap2.put("roleId", roleId);

		// 建立权限与关系表的关系
		sql2.append(" and sp.id = rpr.purviewId");

		// 权限的状态是放开
		sql2.append(" and sp.status = '1'");

		// 根据位置进行排序
		sql2.append(" order by sp.level desc,sp.position asc");

		try {
			// 调用DAO执行SQL获取返回LIST
			List<ShopPurview> shopPurviewList = shopPurviewDao.list(sql.toString(), paramMap);
			List<ShopPurview> HaveShopPurviewList = shopPurviewDao.list(sql2.toString(), paramMap2);
			// 循环比较用户的id是否选中，如果选中给check属性赋值true
			if (HaveShopPurviewList != null) {
				for (ShopPurview sp : shopPurviewList) {
					for (ShopPurview haveSp : HaveShopPurviewList) {
						if (haveSp.getId().equals(sp.getId())) {
							sp.setChecked(true);
						}
					}
				}
			}
			// 如果数据库查出的数据为空，则返回空
			if (shopPurviewList == null || shopPurviewList.size() <= 0) {
				return null;
			}

			// 定义一个MAP用来遍历集合
			Map<String, ShopPurview> map = new HashMap<String, ShopPurview>();
			// 循环查询出的list
			for (int i = 0; i < shopPurviewList.size(); i++) {
				// 获取权限对象
				ShopPurview purView = (ShopPurview) shopPurviewList.get(i);

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
					continue;
				} else {

					// 判断Map中是否存在该对象，如果存在，则将Map中对象的childList取出放入开对象中
					if (map.containsKey(purView.getId())) {
						purView.setChildList(map.get(purView.getId()).getChildList());
					}

					// 如果该对象的父ID不为空，则判断map中存在该对象的父对象，则将该对象放入父对象的ChildList中。
					// 否则，则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					ShopPurview parentPurview = null;
					// 如果map中存在该对象的父对象则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					if (map.containsKey(purView.getParentId())) {
						// 从Map中获取该对象的父对象
						parentPurview = map.get(purView.getParentId());
						// 如果获取的父对象不为空，则将该对象放入父对象的ChildList中
						if (parentPurview != null) {
							parentPurview.getChildList().add(purView);
						}
					} else {
						// 创建伪父对象
						parentPurview = new ShopPurview();
						// 将该对象放入父对象的ChildList中
						parentPurview.getChildList().add(purView);
						// set 父对象的ID
						parentPurview.setId(purView.getParentId());
						// 将该父对象放入MAP中
						map.put(parentPurview.getId(), parentPurview);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnList;
	}

	@Override
	public List<ShopPurview> getManagerAllShopPurview(Manager manager) throws Exception {
		// 如果传进来的管理员对象为空，则返回null
				if (manager == null) {
					return null;
				}

				// 定义返回LIST
				List returnList = new ArrayList();

				// 定义查询SQL
				StringBuffer sql = new StringBuffer(" select sp from ShopPurview as sp,RolePurviewRel as rpr where 1=1");
				// 定义参数MAP
				Map<String, Object> paramMap = new HashMap<String, Object>();

				// 角色ID与权限的关系
				sql.append(" and rpr.roleId = :roleId");
				paramMap.put("roleId", manager.getRoleId());

				// 建立权限与关系表的关系
				sql.append(" and sp.id = rpr.purviewId");

				// 权限的状态是放开
				sql.append(" and sp.status = '1'");

				// 根据位置进行排序
				sql.append(" order by sp.level desc,sp.position asc");
				// 调用DAO执行SQL获取返回LIST
				List shopPurviewList = shopPurviewDao.list(sql.toString(), paramMap);
		        return shopPurviewList;
	}
}
