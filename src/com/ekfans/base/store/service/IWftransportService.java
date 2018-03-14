package com.ekfans.base.store.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

import com.ekfans.base.store.model.Wfpys;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddVehicleForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarSource;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CheListForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoListForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoResponse;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.ReleaseCarSourceForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.Vehicle;
import com.ekfans.pub.util.Pager;

/**
 * @ClassName: IWftransportService  
 * @Description: TODO(危废运输Service接口) 
 * @Copyright: Copyright (c) 2016年3月15日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年3月15日下午2:06:40
 * @version 1.0
 */
public interface IWftransportService {

	/**
	 * @Title add
	 * @Description TODO(新增) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param wftransport
	 * @return Wftransport
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午2:08:11
	 */
	public boolean add(Wftransport wftransport);
	
	//物流宝推荐车源
	public List<CarSource> recommendChe(Pager page, String startPlace,
			String endPlace, String name, String type, String storeId, String checkStatus,
			String status,String carName, String tankMaterialName, 
			String categoryName, String wfpysParentId,String wfpTotal, boolean endTimeSwitch,String userId,HttpServletRequest request) ;
	
	/**
	 * @Title getWftransportById
	 * @Description TODO(根据ID查询) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return Wftransport
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午2:08:39
	 */
	public Wftransport getWftransportById(String id);
	public Object getWftransportById(String id,int a);
	
	/**
	 * @Title deleteWftransportById
	 * @Description TODO(批量删除) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param ids
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午2:09:48
	 */
	public boolean deleteWftransportById(String ids);
	
	/**
	 * @Title updateWftransport
	 * @Description TODO(更新) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param wftransport
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午2:10:27
	 */
	public boolean updateWftransport(Wftransport wftransport);
	
	/**
	 * @Title getList
	 * @Description TODO(根据企业查询列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param startPlace
	 * @param endPlace
	 * @param name
	 * @param type
	 * @param endTimeSwitch true 将会过滤过期信息 false 不过滤
	 * @return List<Wftransport>
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午2:15:12
	 */
	public List<Wftransport> getList(Pager pager,String startPlace,String endPlace, String name, String type, 
			String storeId, String checkStatus,String status,String carName, String tankMaterialName, 
			String categoryName, String wfpysParentId, boolean endTimeSwitch);

	/**
	 * @Title getSysList
	 * @Description TODO(后台危废运输列表) 详细业务流程: (查询游客添加和企业添加的运输列表)
	 * @param page
	 * @param startPlace
	 * @param endPlace
	 * @param name
	 * @param type
	 * @param checkStatus
	 * @param status
	 * @return List<Wftransport>
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午4:04:28
	 */
	public List<Wftransport> getSysList(Pager page, String startPlace,
			String endPlace, String name, String type, String isStore, String checkStatus,String status);
	
	public List<Wftransport> getNewList(String type);
	
	/**
	 * 物流宝添加车源
	 * @param form
	 * @param request
	 * @throws Exception
	 */
	public Vehicle addVehicle(AddVehicleForm form,HttpServletRequest request) throws Exception;
	
	/**
	 * 发布车源  
	 * @param form
	 * @return
	 * @throws Exception
	 */
	public CarSource releaseCarSource(ReleaseCarSourceForm form,HttpServletRequest request) throws Exception;
	
	/**
	 * 物流宝修改车源信息
	 * @param form
	 * @return"0"车源不存在  "1" 修改成功
	 * @throws Exception
	 */
	public Map<String,Object> updateCarSource(ReleaseCarSourceForm form) throws Exception;
	/**
	 * 物流宝获取用户车辆
	 * @param userid
	 * @return
	 * @throws Exception 
	 */
	public List<Vehicle> getUserVehicle(String userid,Pager page) throws Exception;
	
	/**
	 * 物流宝获取用户车源
	 * @param userid
	 * @return
	 * @throws Exception 
	 */
	public List<CarSource> getUserCarSource(String userid,Pager page,String status) throws Exception;
	
	/**
	 * 用户车牌号获取车源列表
	 * @param carNumber
	 * @return
	 * @throws Exception
	 */
	public List<Wftransport> getWftransportList(String carNumber) throws Exception;
	
	/**
	 * 物流宝获取用户车源
	 * @param userid
	 * @return
	 * @throws Exception 
	 */
	public CarSource getCarSource(String carSourceId) throws Exception;
	
	/**
	 * 物流宝更新车辆信息
	 * @param form
	 * @param request
	 * @return "0"车辆不存在  "1" 修改成功
	 * @throws Exception
	 */
	public Vehicle updateVehicle(AddVehicleForm form,HttpServletRequest request) throws Exception;
	
	/**
	 * 判断用户的车牌号是否重复
	 * @param carNo 车牌号
	 * @param userID 用户id
	 * @return 1:车牌号未存在  0:车牌号已存在
	 * @throws Exception
	 */
	public String carNoRepeat(String carNo,String userID)throws Exception;
	/**
	 * 物流宝得到车源列表实现方法
	 * @param page
	 * @param startPlace
	 * @param endPlace
	 * @param name
	 * @param type
	 * @param storeId
	 * @param checkStatus
	 * @param status
	 * @param carName
	 * @param tankMaterialName
	 * @param categoryName
	 * @param wfpysParentId
	 * @param wfpTotal
	 * @param endTimeSwitch
	 * @return
	 * @throws JSONException 
	 */
	public List<CarSource> getCheList(Pager page, String startPlace,
			String endPlace, String name, String type, String storeId, String checkStatus,
			String status,JSONArray carName, String tankMaterialName, 
			String categoryName, JSONArray wfpysParentId,JSONArray wfpTotal, boolean endTimeSwitch,CheListForm cf,HttpServletRequest request) throws JSONException;
	
	//物流宝得到货源列表实现方法
	public List<HuoResponse> getHuoList(Pager page, String startPlace,
			String endPlace, String name, String type, String storeId, String checkStatus,
			String status,JSONArray carName, String tankMaterialName, 
			JSONArray categoryName, JSONArray wfpysParentId,JSONArray wfpTotal,JSONArray carLength, boolean endTimeSwitch,HuoListForm hf,HttpServletRequest request) throws JSONException ;
	
	//根据用户id查询所有货源信息
	List<Wftransport> getByStoreId(String userId, Pager page);
	public List<Wftransport> getByStoreId(String userId,Pager page,int a);

	List<Wftransport> getByStoreId(String userId, Pager page, String status, String checkStatus);
	public List<Wftransport> getByStoreId(String userId,String checkStatus);
	public List<Wfpys> getWfpys(String pid);
}
