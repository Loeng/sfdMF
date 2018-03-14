package com.ekfans.base.store.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.ICarInfoDao;
import com.ekfans.base.store.dao.IWfpysDao;
import com.ekfans.base.store.dao.IWftransportDao;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.Wfpys;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IAttentionService;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddVehicleForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarSource;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CheListForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoListForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoResponse;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.ReleaseCarSourceForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.Vehicle;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.WlbUser;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.transaction.Purview;
import com.ekfans.pub.util.transaction.WebSocketUtil;

/**
 * @ClassName: WftransportService  
 * @Description: TODO(WftransportService实现) 
 * @Copyright: Copyright (c) 2016年3月15日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年3月15日下午2:15:57
 * @version 1.0
 */
@Service
public class WftransportService implements IWftransportService{

	@Autowired
	private IWftransportDao dao;
	@Resource
	private ICarInfoDao carDao;
	@Autowired
	private IWfpysDao wfpysDao;
	@Autowired
	private IAttentionService attentionService;
	
	@Override
	public boolean add(Wftransport wftransport) {
		try {
			dao.addBean(wftransport);
			if(wftransport.getType()==0){
				//发送三分地后台管理提示通知
				WebSocketUtil.sendSystemMessage(Purview.CYSH, "危废车源");
			}
			if(wftransport.getType()==1){
				//发送三分地后台管理提示通知
				WebSocketUtil.sendSystemMessage(Purview.HYSH, "危废货源");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Wftransport getWftransportById(String id) {
		try {
			Wftransport wftransport = (Wftransport)dao.get(id);
			return wftransport;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getWftransportById(String id,int a) {
		try {
			Wftransport wftransport = (Wftransport)dao.get(id);
			//得到经营范围id
		    String pid=	wftransport.getWfpysParentId();
		    int type=wftransport.getType();
		    if(type==0){
		    	
				  //返回车源列表包装类的一个构造参数
					List<Wfpys> cheList=new ArrayList<Wfpys>();
				    //如果不含,说明是一个经营范围
					   if(pid==null||pid==""){
						   cheList.add(new Wfpys());
					    }
				    if(pid!=null&&pid.indexOf(",")<0){
				    	//根据这个id查询经营范围详细信息
				    	Wfpys wp= (Wfpys) wfpysDao.get(pid);
				    	//将查到的放入列表
				    	cheList.add(wp);
				    	
				    }
				    if(pid!=null&&pid.indexOf(",")!=-1){
				    	String[] pids=pid.split(",");
				    	for (String p : pids) {
				    		Wfpys wps= (Wfpys) wfpysDao.get(p);
				    		cheList.add(wps);
						}
				    }
			    CarSource c= new CarSource(wftransport);
			     return c;
		    }else if(type==1){
		    	  //返回车源列表包装类的一个构造参数
				List<Wfpys> huoList=new ArrayList<Wfpys>();
			    //如果不含,说明是一个经营范围
				  if(pid==null||pid==""){
					  huoList.add(new Wfpys());
				    }
			    if(pid!=null&&pid.indexOf(",")<0){
			    	//根据这个id查询经营范围详细信息
			    	Wfpys wp= (Wfpys) wfpysDao.get(pid);
			    	//将查到的放入列表
			    	huoList.add(wp);
			    	
			    }
			    if(pid!=null&&pid.indexOf(",")!=-1){
			    	String[] pids=pid.split(",");
			    	for (String p : pids) {
			    		Wfpys wps= (Wfpys) wfpysDao.get(p);
			    		huoList.add(wps);
					}
			    }
		       return new HuoResponse(wftransport);
			
		    }
			
			return wftransport;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean deleteWftransportById(String ids) {
		try {
			if(ids.indexOf(",") < 0){
				dao.deleteById(ids);
				return true;
			}else{
				String [] id = ids.split(",");
				for (int i = 0; i < id.length; i++) {
					dao.deleteById(id[i]);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateWftransport(Wftransport wftransport) {
		try {
			dao.updateBean(wftransport);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	
	    //物流宝得到推荐车源信息
		@SuppressWarnings("unchecked")
		@Override
		public List<CarSource> recommendChe(Pager page, String startPlace,
				String endPlace, String name, String type, String storeId, String checkStatus,
				String status,String carName, String tankMaterialName, 
				String categoryName, String wfpysParentId,String wfpTotal, boolean endTimeSwitch,String userId,HttpServletRequest request) {
			Map<String,Object> map = new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
			// 出发地
			if(!StringUtil.isEmpty(startPlace)){
			     	sql.append(" and w.startPlace like :startPlace");
				    map.put("startPlace", "%" + startPlace + "%");
			}
			// 目的地
			if(!StringUtil.isEmpty(endPlace)){
			  if(endPlace.equals("华东")||endPlace.equals("华北")||endPlace.equals("华中")||endPlace.equals("华南")||endPlace.equals("西南")
					||endPlace.equals("西北")||endPlace.equals("东北")){
				  sql.append(" and (w.endFullPath like :endFullPath");
				   map.put("endFullPath", "%" + endPlace + "%");
				      GeographySearch(sql,endPlace,"endFullPath");
			  }else{
				   sql.append(" and w.endFullPath like :endFullPath");
				   map.put("endFullPath", "%" + endPlace + "%");
			  }
			}
			// 名称
			if(!StringUtil.isEmpty(name)){
				sql.append(" and w.name like :name");
				map.put("name", "%"+name+"%");
			}
			// 类型
			if(!StringUtil.isEmpty(type)){
				sql.append(" and w.type = :type");
				map.put("type", Integer.valueOf(type));
			}
			// 企业ID
			if(!StringUtil.isEmpty(storeId)){
				sql.append(" and w.storeId = :storeId");
				map.put("storeId", storeId);
			}
			// 审核状态
			if(!StringUtil.isEmpty(checkStatus)){
				sql.append(" and w.checkStatus = :checkStatus");
				map.put("checkStatus", Integer.valueOf(checkStatus));
			}
			// 车源状态
			if(!StringUtil.isEmpty(status)){
				sql.append(" and w.status = :status");
				map.put("status", Integer.valueOf(status));
			}
			// 车辆类型
			if(!StringUtil.isEmpty(carName)){
				sql.append(" and w.carName = :carName");
				map.put("carName", carName);
			}
			// 货物类别
			if(!StringUtil.isEmpty(categoryName)){
				sql.append(" and w.categoryName = :categoryName");
				map.put("categoryName", categoryName);
			}
			// 罐体材质
			if(!StringUtil.isEmpty(tankMaterialName)){
				sql.append(" and w.tankMaterialName = :tankMaterialName");
				map.put("tankMaterialName", tankMaterialName);
			}
			// 危险品分类ID
			if(!StringUtil.isEmpty(wfpysParentId)){
				sql.append(" and w.wfpysParentId = :wfpysParentId");
				map.put("wfpysParentId", wfpysParentId);
			}
			//车辆载重量
			if(!StringUtil.isEmpty(wfpTotal)){
				if(wfpTotal.indexOf("10")!=-1 && wfpTotal.indexOf("20")==-1){
					sql.append(" and w.wfpTotal< :wfpTotal");
					map.put("wfpTotal",new BigDecimal(10));
				}
				if(wfpTotal.indexOf("10")!=-1 && wfpTotal.indexOf("20")!=-1){
					sql.append(" and w.wfpTotal> :wfpTotal1 and w.wfpTotal< :wfpTotal2 ");
					map.put("wfpTotal1",new BigDecimal(10));
					map.put("wfpTotal2",new BigDecimal(20));
				}
				if(wfpTotal.indexOf("20")!=-1 && wfpTotal.indexOf("35")!=-1){
					sql.append(" and w.wfpTotal> :wfpTotal1 and w.wfpTotal< :wfpTotal2 ");
					map.put("wfpTotal1",new BigDecimal(20));
					map.put("wfpTotal2",new BigDecimal(35));
				}
				if(wfpTotal.indexOf("35")!=-1 && wfpTotal.indexOf("20")==-1){
					sql.append(" and w.wfpTotal> :wfpTotal");
					map.put("wfpTotal",new BigDecimal(35));
				}
			}
			// 有效期判断
			if(endTimeSwitch){
				String time = DateUtil.getSysDateTimeString();
				sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
				map.put("time", time);
			}
			// 根据需求排序
			sql.append(" order by w.createTime desc, w.status desc, w.checkStatus desc");
			try {
				List<Wftransport> wftransportsTemp = dao.list(page, sql.toString(), map);
				//用来返回车源列表的list
				List<CarSource> car = new ArrayList<CarSource>();
				
				//遍历所有的车源列表
				for(Wftransport w : wftransportsTemp){
				    CheListForm ch = new CheListForm();
				    ch.userId=userId;
					car.add(new CarSource(w,ch,request));
                }
				return car;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Wftransport> getList(Pager page, String startPlace,
			String endPlace, String name, String type, String storeId, String checkStatus,
			String status,String carName, String tankMaterialName, 
			String categoryName, String wfpysParentId, boolean endTimeSwitch) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		// 出发地
		if(!StringUtil.isEmpty(startPlace)){
		   if(startPlace.equals("华东")||startPlace.equals("华北")||startPlace.equals("华中")||startPlace.equals("华南")||startPlace.equals("西南")
				||startPlace.equals("西北")||startPlace.equals("东北")){
			   sql.append(" and (w.startFullPath like :startFullPath");
			    map.put("startFullPath", "%" + startPlace + "%");
			      GeographySearch(sql,startPlace,"startFullPath");
		   }else{
		     	sql.append(" and w.startFullPath like :startFullPath");
			    map.put("startFullPath", "%" + startPlace + "%");
		   }
		}
		// 目的地
		if(!StringUtil.isEmpty(endPlace)){
		  if(endPlace.equals("华东")||endPlace.equals("华北")||endPlace.equals("华中")||endPlace.equals("华南")||endPlace.equals("西南")
				||endPlace.equals("西北")||endPlace.equals("东北")){
			  sql.append(" and (w.endFullPath like :endFullPath");
			   map.put("endFullPath", "%" + endPlace + "%");
			      GeographySearch(sql,endPlace,"endFullPath");
		  }else{
			   sql.append(" and w.endFullPath like :endFullPath");
			   map.put("endFullPath", "%" + endPlace + "%");
		  }
		}
		// 名称
		if(!StringUtil.isEmpty(name)){
			sql.append(" and w.name like :name");
			map.put("name", "%"+name+"%");
		}
		// 类型
		if(!StringUtil.isEmpty(type)){
			sql.append(" and w.type = :type");
			map.put("type", Integer.valueOf(type));
		}
		// 企业ID
		if(!StringUtil.isEmpty(storeId)){
			sql.append(" and w.storeId = :storeId");
			map.put("storeId", storeId);
		}
		// 审核状态
		if(!StringUtil.isEmpty(checkStatus)){
			sql.append(" and w.checkStatus = :checkStatus");
			map.put("checkStatus", Integer.valueOf(checkStatus));
		}
		// 车源状态
		if(!StringUtil.isEmpty(status)){
			sql.append(" and w.status = :status");
			map.put("status", Integer.valueOf(status));
		}
		// 车辆类型
		if(!StringUtil.isEmpty(carName)){
			sql.append(" and w.carName = :carName");
			map.put("carName", carName);
		}
		// 货物类别
		if(!StringUtil.isEmpty(categoryName)){
			sql.append(" and w.categoryName = :categoryName");
			map.put("categoryName", categoryName);
		}
		// 罐体材质
		if(!StringUtil.isEmpty(tankMaterialName)){
			sql.append(" and w.tankMaterialName = :tankMaterialName");
			map.put("tankMaterialName", tankMaterialName);
		}
		// 危险品分类ID
		if(!StringUtil.isEmpty(wfpysParentId)){
			sql.append(" and w.wfpysParentId = :wfpysParentId");
			map.put("wfpysParentId", wfpysParentId);
		}
		// 有效期判断
		if(endTimeSwitch){
			String time = DateUtil.getSysDateTimeString();
			sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
			map.put("time", time);
		}
		// 根据需求排序
		sql.append(" order by w.updateTime desc, w.status desc, w.checkStatus desc");
		try {
			List<Wftransport> wftransportsTemp = dao.list(page, sql.toString(), map);
			List<Wftransport> wftransports = new ArrayList<Wftransport>();
			for (int i = 0; i < wftransportsTemp.size(); i++) {
				Wftransport wftransport = wftransportsTemp.get(i);
				String start = wftransport.getStartFullPath();
				String end = wftransport.getEndFullPath();
				// 截取数组
				String startStr[] = start.split(",");
				String endStr[] = end.split(",");
				// 当起始地地区出现三级地区截取前2级显示
				if(startStr.length > 1){
					start = startStr[0] + startStr[1];
				}else{
					// 不够三级地区将","替换为空,用于页面显示
					start = start.replace(",", "");
				}
				// 当起目的地区出现三级地区截取前2级显示
				if(endStr.length > 1){
					end = endStr[0] + endStr[1];
				}else{
					// 不够三级地区将","替换为空,用于页面显示
					end = end.replace(",", "");
				}
				wftransport.setStartFullPath(start);
				wftransport.setEndFullPath(end);
				wftransports.add(wftransport);
			}
			return wftransports;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//物流宝得到车源列表实现方法
	@SuppressWarnings("unchecked")
	@Override
	public List<CarSource> getCheList(Pager page, String startPlace,
			String endPlace, String name, String type, String storeId, String checkStatus,
			String status,JSONArray carName, String tankMaterialName, 
			String categoryName, JSONArray wfpysParentId,JSONArray wfpTotal, boolean endTimeSwitch,CheListForm cf,HttpServletRequest request) throws JSONException {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		Map<String, SystemArea> areas = Cache.getSystemAreas();
		        //出发地
				if(!StringUtil.isEmpty(cf.startProvinceId)){
				    SystemArea are = areas.get(cf.startProvinceId);
				    if(are!=null){
				    	String startFullPath=are.getAreaName();
				    	// 出发地
						if(!StringUtil.isEmpty(startFullPath)){
						   if(startFullPath.equals("华东")||startFullPath.equals("华北")||startFullPath.equals("华中")||startPlace.equals("华南")||startPlace.equals("西南")
								||startFullPath.equals("西北")||startFullPath.equals("东北")){
							   sql.append(" and (w.startFullPath like :startFullPath");
							    map.put("startFullPath", "%" + startFullPath + "%");
							      GeographySearch(sql,startFullPath,"startFullPath");
						   }else{
						     	sql.append(" and w.startFullPath like :startFullPath");
							    map.put("startFullPath", "%" + startFullPath + "%");
						   }
						}
				    }
				}
			    if(!StringUtil.isEmpty(cf.startCityId)){
				    SystemArea are = areas.get(cf.startCityId);
				    String startFullPath=are.getAreaName();
				    sql.append(" and w.startFullPath like :startFullPath");
				    map.put("startFullPath", "%" + startFullPath + "%");
				    
				}
				if(!StringUtil.isEmpty(cf.startAreaId)){
				    SystemArea are = areas.get(cf.startAreaId);
				    String startFullPath=are.getAreaName();
				    sql.append(" and w.startFullPath like :startFullPath");
				    map.put("startFullPath", "%" + startFullPath + "%");
				}
				//目的地
				if(!StringUtil.isEmpty(cf.endProvinceId)){
					SystemArea are = areas.get(cf.startProvinceId);
				    if(are!=null){
				    	String endFullPath=are.getAreaName();
				    	// 目的地
						if(!StringUtil.isEmpty(endFullPath)){
						  if(endFullPath.equals("华东")||endFullPath.equals("华北")||endFullPath.equals("华中")||endFullPath.equals("华南")||endFullPath.equals("西南")
								||endFullPath.equals("西北")||endFullPath.equals("东北")){
							  sql.append(" and (w.endFullPath like :endFullPath");
							   map.put("endFullPath", "%" + endFullPath + "%");
							      GeographySearch(sql,endFullPath,"endFullPath");
						  }else{
							   sql.append(" and w.endFullPath like :endFullPath");
							   map.put("endFullPath", "%" + endFullPath + "%");
						  }
					   }
				    }
				}
				if(!StringUtil.isEmpty(cf.endCityId)){
					SystemArea are = areas.get(cf.endCityId);
					String endFullPath=are.getAreaName();
					sql.append(" and w.endFullPath like :endFullPath");
					map.put("endFullPath", "%" + endFullPath + "%");
				}
				if(!StringUtil.isEmpty(cf.endAreaId)){
					SystemArea are = areas.get(cf.endAreaId);
					String endFullPath=are.getAreaName();
					sql.append(" and w.endFullPath like :endFullPath");
					map.put("endFullPath", "%" + endFullPath + "%");
				}
		// 名称
		if(!StringUtil.isEmpty(name)){
			sql.append(" and w.name like :name");
			map.put("name", "%"+name+"%");
		}
		// 类型
		if(!StringUtil.isEmpty(type)){
			sql.append(" and w.type = :type");
			map.put("type", Integer.valueOf(type));
		}
		// 企业ID
		if(!StringUtil.isEmpty(storeId)){
			sql.append(" and w.storeId = :storeId");
			map.put("storeId", storeId);
		}
		// 审核状态
		if(!StringUtil.isEmpty(checkStatus)){
			sql.append(" and w.checkStatus = :checkStatus");
			map.put("checkStatus", Integer.valueOf(checkStatus));
		}
		// 车源状态
		if(!StringUtil.isEmpty(status)){
			sql.append(" and w.status = :status");
			map.put("status", Integer.valueOf(status));
		}
		// 车辆类型
		if(carName!=null&&carName.length()>0){
			 sql.append(" and (w.type = 56");
		     for(int i=0;i<carName.length();i++){
			    sql.append(" or w.carName = :carName"+i);
				map.put("carName"+i, carName.getString(i));
			 }
		     sql.append(")");
		}
		// 罐体材质
		if(!StringUtil.isEmpty(tankMaterialName)){
			sql.append(" and w.tankMaterialName = :tankMaterialName");
			map.put("tankMaterialName", tankMaterialName);
		}
        //经营范围
		if(wfpysParentId!=null&&wfpysParentId.length()>0){
			sql.append(" and (w.type = 56");
			for(int i=0;i<wfpysParentId.length();i++){
			     sql.append(" or w.wfpysParentId like :wfpysParentId"+i);
				 map.put("wfpysParentId"+i,"%"+wfpysParentId.getString(i)+"%");
			}
		    sql.append(")");    
		}
		
		//车辆载重量
		if(wfpTotal!=null&&wfpTotal.length()>0){
			sql.append(" and (w.type = 56");
			 for(int i=0;i<wfpTotal.length();i++){
				    if(wfpTotal.getString(i).equals("10吨以下")){
						sql.append(" or w.wfpTotal< 10");
					}
					if(wfpTotal.getString(i).equals("10-20吨")){
						sql.append(" or (w.wfpTotal>= 10 and w.wfpTotal<=20)");
					}
					if(wfpTotal.getString(i).equals("20-35吨")){
						sql.append(" or (w.wfpTotal>= 20 and w.wfpTotal<= 35)");
					}
					if(wfpTotal.getString(i).equals("35吨以上")){
						sql.append(" or w.wfpTotal> 35");
					}			
			}
			sql.append(")");
		}
		
		// 有效期判断
		if(endTimeSwitch){
			String time = DateUtil.getSysDateTimeString();
			sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
			map.put("time", time);
		}
		// 根据需求排序
		sql.append(" order by w.updateTime desc, w.status desc, w.checkStatus desc");
		try {
			List<Wftransport> wftransportsTemp = dao.list(page, sql.toString(), map);

			//用来返回车源列表的list
			List<CarSource> car = new ArrayList<CarSource>();
			
			//遍历所有的车源列表
			for(Wftransport w : wftransportsTemp){
				car.add(new CarSource(w, cf,request));
			}
			return car;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//物流宝得到货源列表实现方法
	@SuppressWarnings("unchecked")
	@Override
	public List<HuoResponse> getHuoList(Pager page, String startPlace,
			String endPlace, String name, String type, String storeId, String checkStatus,
			String status,JSONArray carName, String tankMaterialName, 
			JSONArray categoryName, JSONArray wfpysParentId,JSONArray wfpTotal,JSONArray carLength, boolean endTimeSwitch,
			HuoListForm hf,HttpServletRequest request) throws JSONException {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		Map<String, SystemArea> areas = Cache.getSystemAreas();
		//出发地
		if(!StringUtil.isEmpty(hf.startProvinceId)){
		    SystemArea are = areas.get(hf.startProvinceId);
		    if(are!=null){
		    	String startFullPath=are.getAreaName();
		    	// 出发地
				if(!StringUtil.isEmpty(startFullPath)){
				   if(startFullPath.equals("华东")||startFullPath.equals("华北")||startFullPath.equals("华中")||startPlace.equals("华南")||startPlace.equals("西南")
						||startFullPath.equals("西北")||startFullPath.equals("东北")){
					   sql.append(" and (w.startFullPath like :startFullPath");
					    map.put("startFullPath", "%" + startFullPath + "%");
					      GeographySearch(sql,startFullPath,"startFullPath");
				   }else{
				     	sql.append(" and w.startFullPath like :startFullPath");
					    map.put("startFullPath", "%" + startFullPath + "%");
				   }
				}
		    }
		}
	    if(!StringUtil.isEmpty(hf.startCityId)){
		    SystemArea are = areas.get(hf.startCityId);
		    String startFullPath=are.getAreaName();
		    sql.append(" and w.startFullPath like :startFullPath");
		    map.put("startFullPath", "%" + startFullPath + "%");
		    
		}
		if(!StringUtil.isEmpty(hf.startAreaId)){
		    SystemArea are = areas.get(hf.startAreaId);
		    String startFullPath=are.getAreaName();
		    sql.append(" and w.startFullPath like :startFullPath");
		    map.put("startFullPath", "%" + startFullPath + "%");
		}
		//目的地
		if(!StringUtil.isEmpty(hf.endProvinceId)){
			SystemArea are = areas.get(hf.startProvinceId);
		    if(are!=null){
		    	String endFullPath=are.getAreaName();
		    	// 目的地
				if(!StringUtil.isEmpty(endFullPath)){
				  if(endFullPath.equals("华东")||endFullPath.equals("华北")||endFullPath.equals("华中")||endFullPath.equals("华南")||endFullPath.equals("西南")
						||endFullPath.equals("西北")||endFullPath.equals("东北")){
					  sql.append(" and (w.endFullPath like :endFullPath");
					   map.put("endFullPath", "%" + endFullPath + "%");
					      GeographySearch(sql,endFullPath,"endFullPath");
				  }else{
					   sql.append(" and w.endFullPath like :endFullPath");
					   map.put("endFullPath", "%" + endFullPath + "%");
				  }
			   }
		    }
		}
		if(!StringUtil.isEmpty(hf.endCityId)){
			SystemArea are = areas.get(hf.endCityId);
			String endFullPath=are.getAreaName();
			sql.append(" and w.endFullPath like :endFullPath");
			map.put("endFullPath", "%" + endFullPath + "%");
		}
		if(!StringUtil.isEmpty(hf.endAreaId)){
			SystemArea are = areas.get(hf.endAreaId);
			String endFullPath=are.getAreaName();
			sql.append(" and w.endFullPath like :endFullPath");
			map.put("endFullPath", "%" + endFullPath + "%");
		}
		// 名称
		if(!StringUtil.isEmpty(name)){
			sql.append(" and w.name like :name");
			map.put("name", "%"+name+"%");
		}
		// 类型
		if(!StringUtil.isEmpty(type)){
			sql.append(" and w.type = :type");
			map.put("type", Integer.valueOf(type));
		}
		// 企业ID
		if(!StringUtil.isEmpty(storeId)){
			sql.append(" and w.storeId = :storeId");
			map.put("storeId", storeId);
		}
		// 审核状态
		if(!StringUtil.isEmpty(checkStatus)){
			sql.append(" and w.checkStatus = :checkStatus");
			map.put("checkStatus", Integer.valueOf(checkStatus));
		}
		// 车源状态
		if(!StringUtil.isEmpty(status)){
			sql.append(" and w.status = :status");
			map.put("status", Integer.valueOf(status));
		}
		// 车辆类型
		if(carName!=null&&carName.length()>0){
			 sql.append(" and (w.type = 56");
		     for(int i=0;i<carName.length();i++){
			    sql.append(" or w.carName = :carName"+i);
				map.put("carName"+i, carName.getString(i));
			 }
		     sql.append(")");
		}
		// 货物类别
		if(categoryName!=null&&categoryName.length()>0){
			sql.append(" and (w.type = 56");
			for(int i=0;i<categoryName.length();i++){
				sql.append(" or w.categoryName = :categoryName"+i);
				map.put("categoryName"+i, categoryName.getString(i));
			}
			sql.append(")");
		}
		// 罐体材质
		if(!StringUtil.isEmpty(tankMaterialName)){
			sql.append(" and w.tankMaterialName = :tankMaterialName");
			map.put("tankMaterialName", tankMaterialName);
		}

		//经营范围
		if(wfpysParentId!=null&&wfpysParentId.length()>0){
			sql.append(" and (w.type = 56");
			for(int i=0;i<wfpysParentId.length();i++){
			     sql.append(" or w.wfpysParentId like :wfpysParentId"+i);
				 map.put("wfpysParentId"+i,"%"+wfpysParentId.getString(i)+"%");
			}
		    sql.append(")");    
		}
		
		//车辆载重量
		if(wfpTotal!=null&&wfpTotal.length()>0){
			sql.append(" and (w.type = 56");
			 for(int i=0;i<wfpTotal.length();i++){
				    if(wfpTotal.getString(i).equals("10吨以下")){
						sql.append(" or w.wfpTotal< 10");
					}
					if(wfpTotal.getString(i).equals("10-20吨")){
						sql.append(" or (w.wfpTotal>= 10 and w.wfpTotal<=20)");
					}
					if(wfpTotal.getString(i).equals("20-35吨")){
						sql.append(" or (w.wfpTotal>= 20 and w.wfpTotal<=35)");
					}
					if(wfpTotal.getString(i).equals("35吨以上")){
						sql.append(" or w.wfpTotal> 35");
					}			
			}
			sql.append(")");
		}
		
		//车辆长度
		if(carLength!=null&&carLength.length()>0){
			sql.append(" and (w.type = 56");
			for(int i=0;i<carLength.length();i++){
					sql.append(" or w.carLength = :carLength"+i);
					map.put("carLength"+i,new BigDecimal(carLength.getString(i)));
			}
			sql.append(")");
		}
		
		// 有效期判断
		if(endTimeSwitch){
			String time = DateUtil.getSysDateTimeString();
			sql.append(" and (w.validTime > :time or w.validTime is null or w.validTime = '')");
			map.put("time", time);
		}
		// 根据需求排序
		sql.append(" order by w.createTime desc, w.status desc, w.checkStatus desc");
		try {
			List<Wftransport> wftransportsTemp = dao.list(page, sql.toString(), map);
			//用来返回车源列表的list
			List<HuoResponse> huo = new ArrayList<HuoResponse>();
			
			//遍历所有的车源列表
			for(Wftransport w : wftransportsTemp){
				huo.add(new HuoResponse(w,hf,request));
			}
			return huo;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	/**
	 * 根据地理区域搜索车货源
	 * @param sql
	 * @param map
	 * @param place
	 * @param i 
	 */
	private void GeographySearch(StringBuffer sql, String place, String i) {
	    if(place.equals("华东")){
			 sql.append(" or w."+i+" like '%上海%'");
			 sql.append(" or w."+i+" like '%江苏%'");
			 sql.append(" or w."+i+" like '%浙江%'");
			 sql.append(" or w."+i+" like '%安徽%'");
			 sql.append(" or w."+i+" like '%福建%'");
			 sql.append(" or w."+i+" like '%江西%'");
			 sql.append(" or w."+i+" like '%山东%'");
			 sql.append(" or w."+i+" like '%台湾%')");
			 return;
		 }
	    if(place.equals("华北")){
			 sql.append(" or w."+i+" like '%北京%'");
			 sql.append(" or w."+i+" like '%天津%'");
			 sql.append(" or w."+i+" like '%山西%'");
			 sql.append(" or w."+i+" like '%河北%'");
			 sql.append(" or w."+i+" like '%内蒙古%')");
			 return;
		 }
	    if(place.equals("华中")){
			 sql.append(" or w."+i+" like '%河南%'");
			 sql.append(" or w."+i+" like '%湖南%'");
			 sql.append(" or w."+i+" like '%湖北%')");
			 return;
		 }
	    if(place.equals("华南")){
			 sql.append(" or w."+i+" like '%广东%'");
			 sql.append(" or w."+i+" like '%广西%'");
			 sql.append(" or w."+i+" like '%海南%'");
			 sql.append(" or w."+i+" like '%香港%'");
			 sql.append(" or w."+i+" like '%澳门%')");
			 return;
		 }
	    if(place.equals("西南")){
			 sql.append(" or w."+i+" like '%四川%'");
			 sql.append(" or w."+i+" like '%贵州%'");
			 sql.append(" or w."+i+" like '%云南%'");
			 sql.append(" or w."+i+" like '%重庆%'");
			 sql.append(" or w."+i+" like '%西藏%')");
			 return;
		 }
	    if(place.equals("西北")){
			 sql.append(" or w."+i+" like '%陕西%'");
			 sql.append(" or w."+i+" like '%甘肃%'");
			 sql.append(" or w."+i+" like '%青海%'");
			 sql.append(" or w."+i+" like '%宁夏%'");
			 sql.append(" or w."+i+" like '%新疆%'");
			 sql.append(" or w."+i+" like '%内蒙古%')");
			 return;
		 }
	    if(place.equals("东北")){
			 sql.append(" or w."+i+" like '%黑龙江%'");
			 sql.append(" or w."+i+" like '%吉林%'");
			 sql.append(" or w."+i+" like '%内蒙古%'");
			 sql.append(" or w."+i+" like '%辽宁%'");
			 return;
		 }
	  
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Wftransport> getSysList(Pager page, String startPlace,
			String endPlace, String name, String type,String isStore, String checkStatus,String status) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		// 根据发布人类型搜索
		if(!StringUtil.isEmpty(isStore) && "store".equals(isStore)){
			// 搜索会员发布
			sql.append(" and (w.storeId != null)");
		}else if(!StringUtil.isEmpty(isStore) && "tourist".equals(isStore)){
			// 搜索游客发布
			sql.append(" and (w.storeId is null)");
		}
		// 出发地
		if(!StringUtil.isEmpty(startPlace)){
			sql.append(" and w.startPlace = :startPlace");
			map.put("startPlace", startPlace);
		}
		// 目的地
		if(!StringUtil.isEmpty(endPlace)){
			sql.append(" and w.endPlace = :endPlace");
			map.put("endPlace", endPlace);
		}
		// 名称
		if(!StringUtil.isEmpty(name)){
			sql.append(" and w.name = :name");
			map.put("name", name);
		}
		// 类型
		if(!StringUtil.isEmpty(type)){
			sql.append(" and w.type = :type");
			map.put("type", Integer.valueOf(type));
		}
		// 审核状态
		if(!StringUtil.isEmpty(checkStatus)){
			sql.append(" and w.checkStatus = :checkStatus");
			map.put("checkStatus", Integer.valueOf(checkStatus));
		}
		// 车源状态
		if(!StringUtil.isEmpty(status)){
			sql.append(" and w.status = :status");
			map.put("status", Integer.valueOf(status));
		}
		// 有效期判断
		String time = DateUtil.getSysDateTimeString();
		sql.append(" and (w.endTime > :time or w.endTime is null or w.endTime = '')");
		map.put("time", time);
		// 更新时间排序
		sql.append(" order by w.updateTime desc");
		try {
			List<Wftransport> wftransports = dao.list(page, sql.toString(), map);
			return wftransports;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Wftransport> getNewList(String wfType) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Wftransport> wftransports = new ArrayList<Wftransport>();
		for (int typeNum = 0; typeNum <2 ; typeNum++){
			StringBuffer sql = new StringBuffer(" from Wftransport as w where 1=1");
			String type = "" + typeNum;
			sql.append(" and w.type = :type");
			map.put("type", Integer.valueOf(type));
			sql.append(" and w.status = 1");
			sql.append(" and w.checkStatus = 1");
			String time = DateUtil.getSysDateTimeString();
			sql.append(" and w.endTime > :time"); //选择未结束的项目
			map.put("time", time);
			sql.append(" order by w.updateTime");
			
			try {
				List<Wftransport> wftransportsTemp = dao.list(sql.toString(), map);
				for (int i = 0; i < wftransportsTemp.size(); i++) {
					Wftransport wftransport = wftransportsTemp.get(i);
					String start = wftransport.getStartFullPath();
					String end = wftransport.getEndFullPath();
					// 截取数组
					String startStr[] = start.split(",");
					String endStr[] = end.split(",");
					// 当起始地地区出现三级地区截取前2级显示
					if(startStr.length > 1){
						start = startStr[0] + startStr[1];
					}else{
						// 不够三级地区将","替换为空,用于页面显示
						start = start.replace(",", "");
					}
					// 当起目的地区出现三级地区截取前2级显示
					if(endStr.length > 1){
						end = endStr[0] + endStr[1];
					}else{
						// 不够三级地区将","替换为空,用于页面显示
						end = end.replace(",", "");
					}
					wftransport.setStartFullPath(start);
					wftransport.setEndFullPath(end);
					wftransports.add(wftransport);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		//按createTime排序
		Comparator<Wftransport> comparator = new Comparator<Wftransport>(){
			public int compare(Wftransport w1, Wftransport w2) {
				int result;
		        long longstr1 = Long.valueOf(w1.getCreateTime().replaceAll("[-\\s:]",""));
		        long longstr2 = Long.valueOf(w2.getCreateTime().replaceAll("[-\\s:]",""));
		        if (longstr1 > longstr2){
					result = 1;
				}else{
					result = -1;
				}
				return result;
		   }
		};
		try{
			Collections.sort(wftransports,comparator);
		}catch (Exception e) {
			e.printStackTrace();
		}			

		 		 
		return wftransports;	
	}


	@Override
	public Vehicle addVehicle(AddVehicleForm form,HttpServletRequest request) throws JSONException,NumberFormatException,Exception {
		CarInfo w = new CarInfo();
		//获取用户token
		String token = request.getHeader("WLB-Token");
		//通过token获取用户id
		String userid = token.split("_")[0];
		if(form.wfpysParentId!=null){
			w.setWfpysParentId(StringUtil.arrayToString(form.wfpysParentId, ","));
		}
		if(form.wfpysName!=null){
			w.setWfpysName(StringUtil.arrayToString(form.wfpysName, "|"));
		}
		w.setCarNo(form.carNumber);
		w.setCarType(form.carName);
		w.setEndTime(form.endTime);
		w.setTankMaterialName(form.tankMaterialName);
		w.setFullWeight(new BigDecimal(form.wfpTotal).doubleValue());
		if(!StringUtil.isEmpty(form.unit)){
			w.setUnit(form.unit);	
		}
		if(!StringUtil.isEmpty(form.sizeUnit)&&form.sizeUnit.equals("毫米")){
		    w.setCarLength(new BigDecimal(form.carLength).divide(new BigDecimal(1000)));
		    w.setCarHeight(new BigDecimal(form.carHeight).divide(new BigDecimal(1000)));
		    w.setCarWidth(new BigDecimal(form.carWidth).divide(new BigDecimal(1000)));
		}else{
			w.setCarLength(new BigDecimal(form.carLength));
			w.setCarHeight(new BigDecimal(form.carHeight));
			w.setCarWidth(new BigDecimal(form.carWidth));
		}
		w.setLinkMan(form.linkMan);
		w.setPhone(form.phone);
		w.setXszFile(FileUtil.baseStringToImage(request,form.dIBD, "/customerfiles/avatar/"));
	    w.setYszFile(FileUtil.baseStringToImage(request,form.rTQBD, "/customerfiles/avatar/"));
	    w.setStoreId(userid);
	    w.setCreateTime(DateUtil.getSysDateTimeString());
	    w.setUpdateTime(DateUtil.getSysDateTimeString());
	    carDao.saveOrUpdateBean(w);
	    Vehicle v = new Vehicle(w);
	    return v;
	}

	@Override
	public List<Vehicle> getUserVehicle(String id,Pager page) throws Exception {
		List<CarInfo> wf = carDao.getUserCarInfo(id, page);
		List<Vehicle> list = new ArrayList<Vehicle>();
		for(CarInfo car : wf){
			list.add(new Vehicle(car));
		}
		return list;
	}
 

	@SuppressWarnings("unchecked")
	public List<Wftransport> getByStoreId(String userId,Pager page,String status,String checkStatus) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		// 企业ID
		if(!StringUtil.isEmpty(userId)){
			sql.append(" and w.storeId = :storeId");
			map.put("storeId", userId);
		}
		if(!StringUtil.isEmpty(status)){
			sql.append(" and w.status = :status");
			map.put("status", Integer.parseInt(status));
		}
		if(!StringUtil.isEmpty(checkStatus)){
			sql.append(" and w.checkStatus = :checkStatus");
			map.put("checkStatus", Integer.parseInt(checkStatus));
		}
		String time = DateUtil.getSysDateTimeString();
		sql.append(" and w.endTime >= :time ");
		map.put("time", time);
		sql.append(" order by w.createTime desc");
		try {
			List<Wftransport> wftransports = dao.list(page,sql.toString(), map);
		         return wftransports;
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Wftransport> getByStoreId(String userId,String checkStatus) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		// 企业ID
		if(!StringUtil.isEmpty(userId)){
			sql.append(" and w.storeId = :storeId");
			map.put("storeId", userId);
		}
		
		if(!StringUtil.isEmpty(checkStatus)){
			sql.append(" and w.checkStatus = :checkStatus");
			map.put("checkStatus", Integer.parseInt(checkStatus));
		}
		
		sql.append(" order by w.createTime desc");
		try {
			List<Wftransport> wftransports = dao.list(null,sql.toString(), map);
		         return wftransports;
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Wftransport> getByStoreId(String userId,Pager page,int a) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		// 企业ID
		if(!StringUtil.isEmpty(userId)){
			sql.append(" and w.storeId = :storeId");
			map.put("storeId", userId);
		}
		// 有效期判断
		String time = DateUtil.getSysDateTimeString();
		
		sql.append(" and (w.endTime < :time or w.endTime is null or w.endTime = '')");
		map.put("time", time);
		sql.append(" order by w.createTime desc");
	
		try {
			List<Wftransport> wftransports = dao.list(page,sql.toString(), map);
		         return wftransports;
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Wftransport> getByStoreId(String userId,Pager page) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select w from Wftransport as w where 1=1");
		// 企业ID
		if(!StringUtil.isEmpty(userId)){
			sql.append(" and w.storeId = :storeId");
			map.put("storeId", userId);
		}
		sql.append(" order by w.createTime desc");
		try {
			List<Wftransport> wftransports = dao.list(page,sql.toString(), map);
		         return wftransports;
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return null;
	}

	@Override
	public CarSource releaseCarSource(ReleaseCarSourceForm form,HttpServletRequest request) throws Exception {
		WlbUser user = (WlbUser) Cache.engine.get(request.getHeader("WLB-Token"));
		Wftransport w = new Wftransport();
		w.setStoreId(user.getUserId());
		 if(attentionService.isAttention(user.getUserId())){
			  w.setIsAttention("1");
		  }else{
			  w.setIsAttention("0");
		  };
		w.setStatus(0);
	    if(form.startFullPath!=null){
			w.setStartFullPath(StringUtil.arrayToString(form.startFullPath, ","));
		}
		if(form.endFullPath!=null){
			w.setEndFullPath(StringUtil.arrayToString(form.endFullPath, ","));
		}
	    w.setStartPlace(form.startProvinceId);
		if(form.wfpysParentId!=null){
			w.setWfpysParentId(StringUtil.arrayToString(form.wfpysParentId, ","));
		}
		if(form.wfpysName!=null){
			w.setWfpysName(StringUtil.arrayToString(form.wfpysName, "|"));
		}
		List<String> start = new ArrayList<>();
		List<String> end = new ArrayList<>();
        if(!StringUtil.isEmpty(form.endProvinceId)){
			end.add(form.endProvinceId);
		}
        if(!StringUtil.isEmpty(form.endCityId)){
			end.add(form.endCityId);
		}
		if(!StringUtil.isEmpty(form.endAreaId)){
			end.add(form.endAreaId);
		}
		w.setEndPlace(StringUtil.arrayToString(end, "|"));
        if(!StringUtil.isEmpty(form.startProvinceId)){
			start.add(form.startProvinceId);
		}
		if(!StringUtil.isEmpty(form.startCityId)){
			start.add(form.startCityId);
		}
		if(!StringUtil.isEmpty(form.startAreaId)){
			start.add(form.startAreaId);
		}
		w.setStartPlace(StringUtil.arrayToString(start, "|"));
	    w.setHabitatAddress(form.habitatAddress);
	    w.setCarNumber(form.carNumber);
	    w.setCarName(form.carName);
	    w.setEndTime(form.endTime);
	    w.setTankMaterialName(form.tankMaterialName);
	    w.setWfpTotal(new BigDecimal(form.wfpTotal));
	    if(!StringUtil.isEmpty(form.unit)){
			w.setUnit(form.unit);	
		}
	    if(!StringUtil.isEmpty(form.sizeUnit)&&form.sizeUnit.equals("毫米")){
		    w.setCarLength(new BigDecimal(form.carLength).divide(new BigDecimal(1000)));
		    w.setCarHeight(new BigDecimal(form.carHeight).divide(new BigDecimal(1000)));
		    w.setCarWidth(new BigDecimal(form.carWidth).divide(new BigDecimal(1000)));
		}else{
			w.setCarLength(new BigDecimal(form.carLength));
			w.setCarHeight(new BigDecimal(form.carHeight));
			w.setCarWidth(new BigDecimal(form.carWidth));
		}
	    w.setLinkMan(form.linkMan);
	    w.setPhone(form.phone);
	    w.setCreateTime(DateUtil.getSysDateTimeString());
        w.setUpdateTime(DateUtil.getSysDateTimeString());
	    w.setType(0);
		dao.addBean(w);
		CarSource c = new CarSource(w);
		//发送三分地后台管理提示通知
		WebSocketUtil.sendSystemMessage(Purview.CYSH, "有一条危废车源信息需要审核!");
		return c;
	}

	@Override
	public Vehicle updateVehicle(AddVehicleForm form, HttpServletRequest request) throws Exception {
		CarInfo w = (CarInfo) carDao.get(form.id);
		if(w==null){
		  return null;
		}
		w.setCarNo(form.carNumber);
		if(form.wfpysParentId!=null){
			w.setWfpysParentId(StringUtil.arrayToString(form.wfpysParentId, ","));
		}
		if(form.wfpysName!=null){
			w.setWfpysName(StringUtil.arrayToString(form.wfpysName, "|"));
		}
		w.setCarType(form.carName);
		w.setEndTime(form.endTime);
		w.setTankMaterialName(form.tankMaterialName);
		w.setFullWeight(new BigDecimal(form.wfpTotal).doubleValue());
		if(!StringUtil.isEmpty(form.unit)){
			w.setUnit(form.unit);	
		}
		if(!StringUtil.isEmpty(form.sizeUnit)&&form.sizeUnit.equals("毫米")){
		    w.setCarLength(new BigDecimal(form.carLength).divide(new BigDecimal(1000)));
		    w.setCarHeight(new BigDecimal(form.carHeight).divide(new BigDecimal(1000)));
		    w.setCarWidth(new BigDecimal(form.carWidth).divide(new BigDecimal(1000)));
		}else{
			w.setCarLength(new BigDecimal(form.carLength));
			w.setCarHeight(new BigDecimal(form.carHeight));
			w.setCarWidth(new BigDecimal(form.carWidth));
		}
		w.setLinkMan(form.linkMan);
		w.setPhone(form.phone);
		if(!StringUtil.isEmpty(form.dIBD)){
			w.setXszFile(FileUtil.baseStringToImage(request,form.dIBD, "/customerfiles/avatar/"));	
		}
		if(!StringUtil.isEmpty(form.rTQBD)){
			w.setXszFile(FileUtil.baseStringToImage(request,form.rTQBD, "/customerfiles/avatar/"));	
		}
	    w.setUpdateTime(DateUtil.getSysDateTimeString());
	    dao.updateBean(w);
	    Vehicle v = new Vehicle(w);
	    return v;
	}

	@Override
	public Map<String,Object> updateCarSource(ReleaseCarSourceForm form) throws Exception {
		Wftransport w = (Wftransport) dao.get(form.id);
		Map<String,Object> map = new HashMap<String,Object>();
		if(w==null){
		    map.put("statusCode", "2");
			return map;
		}
		if(w.getCheckStatus()!=1&&form.status.equals("1")){
			map.put("statusCode", "0");
			return map;
		}
		if(form.wfpysParentId!=null){
			w.setWfpysParentId(StringUtil.arrayToString(form.wfpysParentId, ","));
		}
		if(form.wfpysName!=null){
			w.setWfpysName(StringUtil.arrayToString(form.wfpysName, "|"));
		}
		List<String> start = new ArrayList<>();
		List<String> end = new ArrayList<>();
        if(!StringUtil.isEmpty(form.endProvinceId)){
			end.add(form.endProvinceId);
		}
        if(!StringUtil.isEmpty(form.endCityId)){
			end.add(form.endCityId);
		}
		if(!StringUtil.isEmpty(form.endAreaId)){
			end.add(form.endAreaId);
		}
		w.setEndPlace(StringUtil.arrayToString(end, "|"));
        if(!StringUtil.isEmpty(form.startProvinceId)){
			start.add(form.startProvinceId);
		}
		if(!StringUtil.isEmpty(form.startCityId)){
			start.add(form.startCityId);
		}
		if(!StringUtil.isEmpty(form.startAreaId)){
			start.add(form.startAreaId);
		}
		w.setStartPlace(StringUtil.arrayToString(start, "|"));
		if(form.startFullPath!=null){
			w.setStartFullPath(StringUtil.arrayToString(form.startFullPath, ","));
		}
		if(form.endFullPath!=null){
			w.setEndFullPath(StringUtil.arrayToString(form.endFullPath, ","));
		}
		if(!StringUtil.isEmpty(form.habitatAddress)){
			w.setHabitatAddress(form.habitatAddress);
		}
		if(!StringUtil.isEmpty(form.carNumber)){
			w.setCarNumber(form.carNumber);
		}
		if(!StringUtil.isEmpty(form.carName)){
			 w.setCarName(form.carName);
		}
		if(!StringUtil.isEmpty(form.endTime)){
			w.setEndTime(form.endTime);
		}
		if(!StringUtil.isEmpty(form.tankMaterialName)){
			w.setTankMaterialName(form.tankMaterialName);
		}
		if(!StringUtil.isEmpty(form.wfpTotal)){
			w.setWfpTotal(new BigDecimal(form.wfpTotal));
		}
		if(!StringUtil.isEmpty(form.unit)){
			w.setUnit(form.unit);
		}
		if(!StringUtil.isEmpty(form.sizeUnit)&&form.sizeUnit.equals("毫米")){
			if(!StringUtil.isEmpty(form.carLength)){
				w.setCarLength(new BigDecimal(form.carLength).divide(new BigDecimal(1000)));	
			}
			if(!StringUtil.isEmpty(form.carHeight)){
				w.setCarHeight(new BigDecimal(form.carHeight).divide(new BigDecimal(1000)));
			}
		    if(!StringUtil.isEmpty(form.carWidth)){
		    	w.setCarWidth(new BigDecimal(form.carWidth).divide(new BigDecimal(1000)));	
		    }
		}else{
			if(!StringUtil.isEmpty(form.carLength)){
				w.setCarLength(new BigDecimal(form.carLength));
			}
			if(!StringUtil.isEmpty(form.carHeight)){
				w.setCarHeight(new BigDecimal(form.carHeight));
			}
		    if(!StringUtil.isEmpty(form.carWidth)){
		    	w.setCarWidth(new BigDecimal(form.carWidth));
		    }
		}
		if(!StringUtil.isEmpty(form.linkMan)){
			w.setLinkMan(form.linkMan);
		}
		if(!StringUtil.isEmpty(form.phone)){
			w.setPhone(form.phone);
		}
		w.setCheckStatus(0);
		w.setStatus(0);
		w.setUpdateTime(DateUtil.getSysDateTimeString());
	    dao.updateBean(w);
	    CarSource c = new CarSource(w);
	    map.put("statusCode", "1");
		map.put("carSource", c);
		//发送三分地后台管理提示通知
		WebSocketUtil.sendSystemMessage(Purview.CYSH, "有一条危废车源信息需要审核!");
		return map;
	}

	@Override
	public List<CarSource> getUserCarSource(String userid,Pager page,String status) throws Exception {
		List<Wftransport> wf = dao.getUserWftransport(userid,0,page,status);
		List<CarSource> list = new ArrayList<CarSource>();
		for(Wftransport w : wf){
			list.add(new CarSource(w));
		}
		return list;
	}
	
	public List<Wfpys> getWfpys(String pid){
		try {
			
			//返回车源列表包装类的一个构造参数
			List<Wfpys> l=new ArrayList<Wfpys>();
			//得到一个车源里的经营范围id
		//    String pid=	w.getWfpysParentId();
		    //如果不含,说明是一个经营范围
		    if(pid.indexOf(",")<0){
		    	//根据这个id查询经营范围详细信息
		    	Wfpys wp= (Wfpys) wfpysDao.get(pid);
		    	//将查到的放入列表
		    	l.add(wp);
		    	
		    }
		    if(pid.indexOf(",")!=-1){
		    	String[] pids=pid.split(",");
		    	for (String p : pids) {
		    		Wfpys wps= (Wfpys) wfpysDao.get(p);
		    		l.add(wps);
				}
		    }
		    return l;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public CarSource getCarSource(String carSourceId) throws Exception {
		Wftransport w = (Wftransport) dao.get(carSourceId);
		return new CarSource(w);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String carNoRepeat(String carNo, String userID) throws Exception {
		StringBuffer sql = new StringBuffer("from CarInfo where 1=1 and storeId = :userID and carNo = :carNo");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("carNo", carNo);
		map.put("userID", userID);
		List<CarInfo> car = (List<CarInfo>) carDao.list(sql.toString(), map);
		if(car==null||car.size()==0){
			return "1";
		}
		return "0";
	}

	@Override
	public List<Wftransport> getWftransportList(String carNumber) throws Exception {
		StringBuffer sql = new StringBuffer("from Wftransport where 1=1 and carNumber = :carNumber");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("carNumber", carNumber);
		@SuppressWarnings("unchecked")
		List<Wftransport> w = (List<Wftransport>) dao.list(sql.toString(), map);
		return w;
	}

}
