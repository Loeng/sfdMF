package com.ekfans.base.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.dao.ISystemAreaDao;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.service.SystemAreaService;

/**
 * 地区业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class SystemAreaService implements ISystemAreaService {

	public static Logger log = LoggerFactory.getLogger(SystemAreaService.class);
	// 定義DAO
	@Autowired
	private ISystemAreaDao systemAreaDao;

	/**
	 * 获取数据库中的所有地区，并按照上下级关系分类好，保存成Map形式
	 * 
	 * @return
	 */

	@Override
	public Map<String, List<String>> getRootArea() {
		// 调用缓存方法获取跟地区的Id
		String rootId = Cache.getResource("area.parent.code");

		// 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select sa.pingYin,sa.id from SystemArea as sa where 1=1");
		// 查询根地区
		sql.append(" and sa.parentCode = :rootId");
		paramMap.put("rootId", rootId);
		// 排序：按照首拼字母升序，ID升序
		sql.append(" order by sa.pingYin asc");

		// 定义返回的List
		Map<String, List<String>> returnList = new LinkedHashMap<String, List<String>>();

		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List areaList = systemAreaDao.list(sql.toString(), paramMap);
			// 如果查出来的地区集合为空，或者集合长度小于或等于0，则返回空
			if (areaList == null || areaList.size() <= 0) {
				return null;
			}

			// 定义一个List用来存放A-G的地区
			List<String> agList = new ArrayList<String>();
			// 定义一个List用来存放H-N的地区
			List<String> hnList = new ArrayList<String>();
			// 定义一个List用来存放O-T的地区
			List<String> otList = new ArrayList<String>();
			// 定义一个List用来存放U-Z的地区
			List<String> uzList = new ArrayList<String>();
			// 定义一个List用来存放其他的地区
			List<String> otherList = new ArrayList<String>();
			// 循环查出的地区列表
			for (int i = 0; i < areaList.size(); i++) {
				// 取出SystemArea
				Object[] obj = (Object[]) areaList.get(i);
				// 如果取出的SystemArea对象为空，则跳过
				if (obj == null || obj.length <= 0) {
					continue;
				}
				String pinYing = (String) obj[0];
				String areaId = (String) obj[1];

				// 如果地区对象的首拼不为空，则根据首拼来分类，否则，放入其他集合
				if (!StringUtil.isEmpty(pinYing) && !StringUtil.isEmpty(areaId)) {
					// 如果地区对象的首拼小于G，则放入A-G的集合中
					if (pinYing.compareTo("G") <= 0) {
						agList.add(areaId);
					} else if (pinYing.compareTo("N") <= 0) {
						// 如果地区对象的首拼小于N，则放入H-N的集合中
						hnList.add(areaId);
					} else if (pinYing.compareTo("T") <= 0) {
						// 如果地区对象的首拼小于T，则放入O-T的集合中
						otList.add(areaId);
					} else {
						// 将剩余的放入U-Z的集合中
						uzList.add(areaId);
					}
				} else {
					otherList.add(areaId);
				}

			}

			// 将A-G的地区集合放入返回的集合中
			returnList.put("A-G", agList);
			// 将H-N的地区集合放入返回的集合中
			returnList.put("H-N", hnList);
			// 将O-T的地区集合放入返回的集合中
			returnList.put("O-T", otList);
			// 将U-Z的地区集合放入返回的集合中
			returnList.put("U-Z", uzList);
			// 将剩余的地区集合放入返回的集合中
			returnList.put("Other", otherList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnList;
	}

	/**
	 * 根据父地区ID获取下级地区集合
	 * 
	 * @param parentId
	 *            父地区ID
	 * @return
	 */
	public List<SystemArea> getChildAreasByParentId(String parentId) {
		// 如果传进来的地区ID为空，则返回空串
		if (StringUtil.isEmpty(parentId)) {
			return null;
		}

		// 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select distinct sa from SystemArea as sa where 1=1");
		// 根据父ID查询
		sql.append(" and sa.parentCode = :parentId");
		paramMap.put("parentId", parentId);
		// 排序：按照首拼字母升序，ID升序
		sql.append(" order by sa.id asc");

		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List<SystemArea> areaList = systemAreaDao.list(sql.toString(), paramMap);
			return areaList;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 根据地区ID和分隔符来获取地区全称
	 * 
	 * @param areaId
	 *            地区ID
	 * @param delimiter
	 *            地区之间的分隔符
	 * @return
	 */
	public String getAreaFullNameByAreaIdAndDelimiter(String areaId, String delimiter) {
		// 如果传进来的地区ID为空，则返回空串
		if (StringUtil.isEmpty(areaId)) {
			return "";
		}
		// 调用缓存方法获取跟地区的Id
		String rootId = Cache.getResource("area.parent.code");
		// 定义返回的地区全称
		String areaFullName = "";
		boolean hasParent = true;
		while (hasParent) {
			SystemArea area = Cache.getSystemAreaById(areaId);
			if (area != null) {
				areaFullName = area.getAreaName() + delimiter + areaFullName;
				// 如果取出的父级ID不为空，并且不为根地区的ID，则将状态设为true,并且将父ID设为AreaID
				if (!StringUtil.isEmpty(area.getParentCode()) && !area.getParentCode().equals(rootId)) {
					hasParent = true;
					areaId = area.getParentCode();
				} else {
					// 否则将状态设置为false
					hasParent = false;
				}
			} else {
				// 否则将状态设置为false
				hasParent = false;
			}
		}
		return areaFullName;
	}

	/**
	 * 根据地区ID和分隔符来获取地区全称
	 * 
	 * @param areaId
	 *            地区ID
	 * @param delimiter
	 *            地区之间的分隔符
	 * @return
	 */
	public String getAreaFullNameByAreaIdAndDelimiterByLevel(String areaId, String delimiter, int level) {
		// 如果传进来的地区ID为空，则返回空串
		if (StringUtil.isEmpty(areaId)) {
			return "";
		}

		// 调用缓存方法获取跟地区的Id
		String rootId = Cache.getResource("area.parent.code");

		// 定义返回的地区全称
		String areaFullName = "";
		boolean hasParent = true;
		while (hasParent) {
			SystemArea area = Cache.getSystemAreaById(areaId);
			if (area != null) {
				areaFullName = area.getAreaName() + delimiter + areaFullName;
				// 如果取出的父级ID不为空，并且不为根地区的ID，则将状态设为true,并且将父ID设为AreaID
				if (!StringUtil.isEmpty(area.getParentCode()) && !area.getParentCode().equals(rootId)) {
					hasParent = true;
					areaId = area.getParentCode();
				} else {
					// 否则将状态设置为false
					hasParent = false;
				}
			} else {
				// 否则将状态设置为false
				hasParent = false;
			}
		}
		return areaFullName;
	}

	/**
	 * 根据地区ID获取地区对象 如果id不行 就用name
	 * 
	 * @param areaId
	 * @return
	 */
	public SystemArea getSystemAreaById(String areaId) {
		// 如果传进来的地区ID为空，则返回空
		if (StringUtil.isEmpty(areaId)) {
			return null;
		}
		// 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select distinct(sa) from SystemArea as sa where 1=1");
		// 查询根地区
		sql.append(" and sa.id = :areaId");
		paramMap.put("areaId", areaId);

		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List areaList = systemAreaDao.list(sql.toString(), paramMap);
			// 如果查出的List不为空，并且长度大于0，则取出第一条数据
			if (areaList != null && areaList.size() > 0) {
				return (SystemArea) areaList.get(0);
			} else {
				// 继续查找
				StringBuffer temp = new StringBuffer("select distinct(sa) from SystemArea as sa where 1=1");
				// 查询根地区name查找
				temp.append(" and sa.areaName = :areaName");
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.put("areaName", areaId);
				List tmepList = systemAreaDao.list(temp.toString(), tempMap);
				if (temp != null && tmepList.size() > 0) {
					return (SystemArea) tmepList.get(0);
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 获取所有的地区，并分类好
	 * 
	 * @param objMap
	 * @param childMap
	 */
	public void getAllSystemAreas(Map<String, SystemArea> objMap, Map<String, List<String>> childMap) {
		// 调用缓存方法获取跟地区的Id
		String rootId = Cache.getResource("area.parent.code");

		// 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select sa from SystemArea as sa where 1=1");
		// 查询根地区
		sql.append(" and sa.id != :rootId");
		paramMap.put("rootId", rootId);
		// 排序：按照首拼字母升序，ID升序
		sql.append(" order by sa.id desc");

		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List areaList = systemAreaDao.list(sql.toString(), paramMap);
			// 如果查出来的地区集合为空，或者集合长度小于或等于0，则返回空
			if (areaList == null || areaList.size() <= 0) {
				return;
			}

			if (objMap == null) {
				objMap = new HashMap<String, SystemArea>();
			}
			if (childMap == null) {
				childMap = new HashMap<String, List<String>>();
			}
			// 遍历所有取出来的地区对象，并分类
			for (int i = 0; i < areaList.size(); i++) {
				SystemArea area = (SystemArea) areaList.get(i);
				if (area != null) {
					objMap.put(area.getId(), area);
					List<String> list = null;
					if (childMap.containsKey(area.getParentCode())) {
						list = childMap.get(area.getParentCode());
					} else {
						list = new ArrayList<String>();
					}
					list.add(area.getId());
					childMap.put(area.getParentCode(), list);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有的地区
	 */
	public Map<String, SystemArea> getAllSystemAreas() {

		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select sa from SystemArea as sa where 1=1");
		// 排序：按照首拼字母升序，ID升序
		sql.append(" order by sa.id asc");

		// 定义一个MAP用来分类
		Map<String, SystemArea> childs = new HashMap<String, SystemArea>();

		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List<SystemArea> areaList = systemAreaDao.list(sql.toString(), null);
			// 如果查出来的地区集合为空，或者集合长度小于或等于0，则返回空
			if (areaList == null || areaList.size() <= 0) {
				return null;
			}

			// 定义一个MAP用来分类
			Map<String, SystemArea> areaMap = new HashMap<String, SystemArea>();

			// 循环获取到的地区集合
			for (int i = 0; i < areaList.size(); i++) {
				// 获取到当前循环到的地址对象
				SystemArea area = areaList.get(i);
				// 如果获取阿东的地区对象为空，则跳过
				if (area == null) {
					continue;
				}
				areaMap.put(area.getId(), area);
			}

			// 循环获取到的地区集合
			for (int i = 0; i < areaList.size(); i++) {
				// 获取到当前循环到的地址对象
				SystemArea area = areaList.get(i);
				// 如果获取阿东的地区对象为空，则跳过
				if (area == null) {
					continue;
				}

				// 定义一个List集合
				SystemArea fullAera = areaMap.get(area.getParentCode());

				// 如果map中包含该地区的上级ID，则从Map中获取集合
				if (childs.containsKey(area.getParentCode())) {
					fullAera = childs.get(area.getParentCode());
				}

				List<SystemArea> mapChilds = null;
				if (fullAera == null) {
					fullAera = new SystemArea();
				}
				mapChilds = fullAera.getChildList();
				// 如果集合不为空，则直接将地区加入集合
				if (mapChilds != null) {
					mapChilds.add(area);
				} else {
					// 如果集合为空，则新建一个集合，并讲地区加入集合
					mapChilds = new ArrayList<SystemArea>();
					mapChilds.add(area);
				}
				fullAera.setChildList(mapChilds);
				// 将集合放入Map
				childs.put(area.getParentCode(), fullAera);

				// 将该地区对象放到Map中
				if (childs.containsKey(area.getId())) {
					SystemArea newArea = childs.get(area.getId());
					area.setChildList(newArea.getChildList());
				}
				childs.put(area.getId(), area);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return childs;
	}

	/**
	 * 查询出所有的省
	 */
	public List<SystemArea> getProvinceList() {
		// / 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select distinct(sa) from SystemArea as sa where 1=1");
		// 根据父ID查询
		String rootCode = Cache.getResource("area.parent.code");
		sql.append(" and sa.parentCode =:rootCode");
		paramMap.put("rootCode", rootCode);
		sql.append(" order by sa.id asc,LENGTH(sa.areaName) asc");
		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List<SystemArea> List = systemAreaDao.list(sql.toString(), paramMap);

			return List;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询出所有的省　市　地区
	 */
	public List<SystemArea> getSystemAreaList(String id) {
		// / 定义SQL的参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 拼写读取地区的SQL
		StringBuffer sql = new StringBuffer("select distinct(sa) from SystemArea as sa where 1=1");
		if (StringUtil.isEmpty(id) || "0".equals(id)) {
			String rootId = Cache.getResource("area.parent.code");
			if (StringUtil.isEmpty(rootId)) {
				sql.append(" and sa.parentCode =").append(rootId);
			} else {
				// 根据父ID查询
				String rootCode = Cache.getResource("area.parent.code");
				sql.append(" and sa.parentCode = :rootCode");
				paramMap.put("rootCode", rootCode);
			}

		} else {
			// 根据父ID查询
			sql.append(" and sa.parentCode = :parentId");
			paramMap.put("parentId", id);

		}
		// 排序：地区的名字长度
		sql.append(" order by sa.id asc,LENGTH(sa.areaName) asc");
		try {
			// 调用DAO方法执行SQL，并返回一个LIST
			List<SystemArea> List = systemAreaDao.list(sql.toString(), paramMap);
			return List;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SystemArea> getSystemAreaLists() {

		// 定义返回LIST
		List returnList = new ArrayList();

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select distinct(sa) from SystemArea as sa where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" order by sa.pingYin asc");
		try {
			// 调用DAO执行SQL获取返回LIST
			List SystemAreas = systemAreaDao.list(sql.toString(), paramMap);

			// 如果数据库查出的数据为空，则返回空
			if (SystemAreas == null || SystemAreas.size() <= 0) {
				return returnList;
			}

			// 定义一个MAP用来遍历集合
			Map<String, SystemArea> map = new HashMap<String, SystemArea>();
			// 循环查询出的list
			for (int i = 0; i < SystemAreas.size(); i++) {
				// 获取权限对象
				SystemArea systemArea = (SystemArea) SystemAreas.get(i);

				// 如果获取的对象为空，则跳过
				if (systemArea == null) {
					continue;
				}

				String rootCode = Cache.getResource("area.parent.code");
				if (rootCode.equals(systemArea.getParentCode())) {
					// 如果Map中已经存在该对象，则将该对象的ChildList放入 purview中，并将purView重新放入Map
					if (map.containsKey(systemArea.getId())) {
						// 从Map中的对象中取出ChildList并放入purView的ChildList中
						systemArea.setChildList(map.get(systemArea.getId()).getChildList());
						// 从Map中移除该权限对象
						map.remove(systemArea.getId());
					}
					// 将该对象放入返回集合中
					returnList.add(systemArea);
					continue;
				} else {

					// 判断Map中是否存在该对象，如果存在，则将Map中对象的childList取出放入开对象中
					if (map.containsKey(systemArea.getId())) {
						systemArea.setChildList(map.get(systemArea.getId()).getChildList());
					}

					// 如果该对象的父ID不为空，则判断map中存在该对象的父对象，则将该对象放入父对象的ChildList中。
					// 否则，则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					SystemArea parentPurview = null;
					// 如果map中存在该对象的父对象则创建一个新的权限对象(伪父对象)，将该对象放入新建的对象ChildList中，并将新对象放入MAP中。
					if (map.containsKey(systemArea.getParentCode())) {
						// 从Map中获取该对象的父对象
						parentPurview = map.get(systemArea.getParentCode());
						// 如果获取的父对象不为空，则将该对象放入父对象的ChildList中
						if (parentPurview != null) {
							parentPurview.getChildList().add(systemArea);
						}
					} else {
						// 创建伪父对象
						parentPurview = new SystemArea();
						// 将该对象放入父对象的ChildList中
						parentPurview.getChildList().add(systemArea);
						// set 父对象的ID
						parentPurview.setId(systemArea.getParentCode());
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

	@Override
	public List<SystemArea> getCityAreaList(String obj) {
		List<SystemArea> are = new ArrayList<SystemArea>();
		String sql = "select t.id,t.areaName,t.areaEnName,t.areaAbbreviation,t.zipCode,t.areaCode,t.parentCode,t.pingYin "
				+ "from SystemArea t,SystemArea s where t.parentCode=s.id and s.areaName=:areaName";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("areaName", obj);
		try {
			List<Object[]> list = systemAreaDao.list(sql.toString(), paramMap);
			for (Object[] objS : list) {
				SystemArea sa = new SystemArea();
				sa.setId((String) objS[0]);
				sa.setAreaName((String) objS[1]);
				sa.setAreaEnName((String) objS[2]);
				sa.setAreaAbbreviation((String) objS[3]);
				sa.setZipCode((String) objS[4]);
				sa.setAreaCode((String) objS[5]);
				sa.setParentCode((String) objS[6]);
				sa.setPingYin((String) objS[7]);
				are.add(sa);
			}

			return are;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<SystemArea> findCityName(String provincialName) {
		// 临时存储集合
		List<SystemArea> are = new ArrayList<SystemArea>();
		// 根据省查询下面全部市
		String hql = "select distinct t.id,t.areaName,t.areaEnName,t.areaAbbreviation,t.zipCode,t.areaCode,t.parentCode,t.pingYin "
				+ "from SystemArea t,SystemArea s where t.parentCode=s.id and s.areaName=:areaName";
		// 设置参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("areaName", provincialName);

		try {
			// 执行查询操作
			List<Object[]> list = systemAreaDao.list(hql, paramMap);
			for (Object[] objS : list) {
				// 把查询出来的值映射到实体类中
				SystemArea sa = new SystemArea();
				sa.setId((String) objS[0]);
				sa.setAreaName((String) objS[1]);
				sa.setAreaEnName((String) objS[2]);
				sa.setAreaAbbreviation((String) objS[3]);
				sa.setZipCode((String) objS[4]);
				sa.setAreaCode((String) objS[5]);
				sa.setParentCode((String) objS[6]);
				sa.setPingYin((String) objS[7]);

				are.add(sa);
			}

			return are;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<SystemArea> findAreaName(String provincialName, String cityName) {
		// 临时存储集合
		List<SystemArea> are = new ArrayList<SystemArea>();
		// 根据省和市查询下面全部区县
		String hql = "select distinct t.id,t.areaName,t.areaEnName,t.areaAbbreviation,t.zipCode,t.areaCode,t.parentCode,t.pingYin "
				+ "from SystemArea t,SystemArea s,SystemArea sa " + "where sa.id=s.parentCode and s.id=t.parentCode and sa.areaName=:provincialName and s.areaName=:cityName";
		// 设置参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("provincialName", provincialName);
		paramMap.put("cityName", cityName);

		try {
			// 执行查询操作
			List<Object[]> list = systemAreaDao.list(hql, paramMap);
			for (Object[] objS : list) {
				// 把查询出来的值映射到实体类中
				SystemArea sa = new SystemArea();
				sa.setId((String) objS[0]);
				sa.setAreaName((String) objS[1]);
				sa.setAreaEnName((String) objS[2]);
				sa.setAreaAbbreviation((String) objS[3]);
				sa.setZipCode((String) objS[4]);
				sa.setAreaCode((String) objS[5]);
				sa.setParentCode((String) objS[6]);
				sa.setPingYin((String) objS[7]);

				are.add(sa);
			}

			return are;
		} catch (Exception e) {
			return null;
		}
	}

}
