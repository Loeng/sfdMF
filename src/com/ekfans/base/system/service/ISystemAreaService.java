package com.ekfans.base.system.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.system.model.SystemArea;

/**
 * 地区管理实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface ISystemAreaService {
	/**
	 * 获取数据库中的所有地区，并按照上下级关系分类好，保存成Map形式
	 * 
	 * @return
	 */
	public Map<String, List<String>> getRootArea();

	/**
	 * 根据父地区ID获取下级地区集合
	 * 
	 * @param parentId
	 *            父地区ID
	 * @return
	 */
	public List<SystemArea> getChildAreasByParentId(String parentId);

	/**
	 * 根据地区ID和分隔符来获取地区全称
	 * 
	 * @param areaId
	 *            地区ID
	 * @param delimiter
	 *            地区之间的分隔符
	 * @return
	 */
	public String getAreaFullNameByAreaIdAndDelimiter(String areaId, String delimiter);
	
	/**
	 * 根据地区ID和分隔符来获取地区全称
	 * 
	 * @param areaId
	 *            地区ID
	 * @param delimiter
	 *            地区之间的分隔符
	 * @return
	 */
	public String getAreaFullNameByAreaIdAndDelimiterByLevel(String areaId, String delimiter,int level);

	/**
	 * 根据地区ID获取地区对象
	 * 
	 * @param areaId
	 * @return
	 */
	public SystemArea getSystemAreaById(String areaId);

	/**
	 * 获取所有的地区，并分类好
	 * 
	 * @param objMap
	 * @param childMap
	 */
	public void getAllSystemAreas(Map<String, SystemArea> objMap, Map<String, List<String>> childMap);

	/**
	 * 获取所有的地区
	 * 
	 * @param objMap
	 * @param childMap
	 */
	public Map<String, SystemArea> getAllSystemAreas();

	/**
	 * 
	 * @Title: getProvinceList
	 * @Description: TODO(查询出所有的省) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<SystemArea> 返回类型
	 * @throws
	 */
	public List<SystemArea> getProvinceList();

	/**
	 * 查询出所有的省　市　地区
	 */
	public List<SystemArea> getSystemAreaList(String id);

	/**
	 * 得到所有的省市区
	 * 
	 */
	public List<SystemArea> getSystemAreaLists();

	/**
	 * 
	 * @Title: getCityAreaList
	 * @Description: TODO(根据省名字获取市或者根据市名字获取县区) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param obj
	 * @return List<SystemArea> 返回类型
	 * @throws
	 */
	public List<SystemArea> getCityAreaList(String obj);

	/**
	 * 
	 * @Title: findCityName
	 * @Description: TODO(根据省查找所属市) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param provincialName
	 * @return List<SystemArea> 返回类型
	 * @throws
	 */
	public List<SystemArea> findCityName(String provincialName);

	/**
	 * 
	 * @Title: findCityName
	 * @Description: TODO(根据市查找所属区县) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param cityName
	 * @return List<SystemArea> 返回类型
	 * @throws
	 */
	public List<SystemArea> findAreaName(String provincialName, String cityName);
}
