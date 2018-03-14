package com.ekfans.plugin.wuliubao.yunshu.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.base.store.service.IWftransportService;
import com.ekfans.controllers.ccwccApp.Response;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IAttentionService;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddHuoForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarSource;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.FindHuoForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoListForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoResponse;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.RecommendCheForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.ShelvesGoodsForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.WlbUser;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.transaction.Purview;
import com.ekfans.pub.util.transaction.WebSocketUtil;

/**
 * 物流宝货源控制类(得到货源列表,添加货源,查找用户货源,得到货源详情,删除货源,修改货源上架下架货源 货源推荐车源  )
 * @author Administrator
 *
 */
@Controller
public class WlbAppGoodsController {

	@Autowired
	private IWftransportService service;
	@Autowired
	private IAttentionService attentionService;
	 // 定义错误日志
 	public static Logger log = LoggerFactory.getLogger(WlbAppGoodsController.class);
	
 	/**
	 *  得到所有审核通过,上架的货源信息
	 * @param request,通过车源地starPlace(湖南),车辆类型carName(不锈钢罐车),经营范围wfpysParentId(008000000),货物类型categoryName(液体),车辆载重wfpTotal筛选货源信息
	 * @return 所有的车源信息
	 */
	@RequestMapping(value="/wlbApp/getHuoList", produces = "application/json;charset=UTF-8",method=RequestMethod.POST)
	public @ResponseBody Response getHuoList(@RequestBody byte[] by,HttpServletRequest request){
		try{
			HuoListForm hf=(HuoListForm) new HuoListForm().getObject(by);
		    Pager page = new Pager();
			// 将从页面获取的分页数据转化成int型
			int currentPage = 1;
			if (!StringUtil.isEmpty(hf.currentpageStr)) {
				currentPage = Integer.parseInt(hf.currentpageStr);
			}
			// 设置要查询的页码
			page.setCurrentPage(currentPage);
			page.setRowsPerPage(10);
			//分页,出发地代码,目的地代码,运输名称,类型(0,1),所属公司id,审核状态(0,1,2),是否上下架(0,1),车辆名称,车辆材质,货物类型,危废品运输界定父类ID,是否过了运输截止日期
			List<HuoResponse> list = service.getHuoList(page,hf.starPlace,"","","1","","1","1",hf.carName,"",hf.categoryName,hf.wfpysParentId,hf.wfpTotal,hf.carLength,true,hf,request);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("Pager", page);
			map.put("Goods", list);
			return new Response().success(map);
	         } catch (JSONException e) {
		 			e.printStackTrace();
					// 记日志
					log.error(e.getMessage());
					return new Response().failure("请求参数异常!");
			}catch (NumberFormatException e) {
	 			e.printStackTrace();
				// 记日志
				log.error(e.getMessage());
				return new Response().failure("字段[currentPage]必须为数字!");
			}catch (Exception e) {
			// 记日志
			e.printStackTrace();
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	@RequestMapping(value="/wlbApp/addHuoYuan")
	@ResponseBody
	public Response addHuoYuan(HttpServletRequest request,@RequestBody byte[] by){
		try {
			AddHuoForm af = (AddHuoForm) new AddHuoForm().getObject(by);
			Wftransport w=new Wftransport();
			List<String> start = new ArrayList<>();
			List<String> end = new ArrayList<>();
	        if(!StringUtil.isEmpty(af.endProvinceId)){
				end.add(af.endProvinceId);
			}
	        if(!StringUtil.isEmpty(af.endCityId)){
				end.add(af.endCityId);
			}
			if(!StringUtil.isEmpty(af.endAreaId)){
				end.add(af.endAreaId);
			}
			w.setEndPlace(StringUtil.arrayToString(end, "|"));
	        if(!StringUtil.isEmpty(af.startProvinceId)){
				start.add(af.startProvinceId);
			}
			if(!StringUtil.isEmpty(af.startCityId)){
				start.add(af.startCityId);
			}
			if(!StringUtil.isEmpty(af.startAreaId)){
				start.add(af.startAreaId);
			}
			w.setStartPlace(StringUtil.arrayToString(start, "|"));
			   if(af.startFullPath!=null){
				   w.setStartFullPath(StringUtil.arrayToString(af.startFullPath, ","));
			   }
			   if(af.endFullPath!=null){
				   w.setEndFullPath(StringUtil.arrayToString(af.endFullPath, ","));
			   }
			   w.setStartHabitatAddress(af.startHabitatAddress);
			   w.setEndHabitatAddress(af.endHabitatAddress);
			   w.setSupplyName(af.supplyName);
			   w.setWfpysParentId(af.wfpysParentId);
			   w.setWfpysName(af.wfpysName);
			   w.setCategoryName(af.categoryName);
				try {
					if(!StringUtil.isEmpty(af.status)){
						w.setStatus(Integer.parseInt(af.status));
						}
					if(!StringUtil.isEmpty(af.wfpTotal)){
					w.setWfpTotal(new BigDecimal(af.wfpTotal));
					}
					if(!StringUtil.isEmpty(af.cargoVolume)){
					w.setCargoVolume(new BigDecimal(af.cargoVolume));
					}
					if(!StringUtil.isEmpty(af.carLength)){
						w.setCarLength(new BigDecimal(af.carLength));
					}
					if(!StringUtil.isEmpty(af.sizeUnit)){
						w.setSizeUnit(af.sizeUnit);
					}
				} catch (Exception e) {
					return new Response().failure("货物重量,体积,要求车辆长度,上下架状态为数字!");
				}
				w.setValidTime(af.validTime);
				w.setEndTime(af.endTime);
				w.setPhone(af.phone);
				w.setLinkMan(af.linkMan);
				w.setContent(af.content);
				w.setCarName(af.carName);
				w.setTankMaterialName(af.tankMaterialName);
			//设为货源
			w.setType(1);
			//未审核
			w.setCheckStatus(0);
			w.setCreateTime(DateUtil.getSysDateTimeString());
			w.setUpdateTime(DateUtil.getSysDateTimeString());
			// 单位空默认为:吨
			if(StringUtil.isEmpty(af.unit)){
				w.setUnit("吨");
			}else{
				w.setUnit(af.unit);
			}
			// 货物体积单位空默认为:m³
			if(!StringUtil.isEmpty(af.volumeUnit)){
				w.setVolumeUnit(af.volumeUnit);
			}
			String token = request.getHeader("WLB-Token");
			if(!StringUtil.isEmpty(token)){
				WlbUser wlbUser = (WlbUser)Cache.engine.get(token);
				if(wlbUser!=null){
				  w.setStoreId(wlbUser.getUserId());
				  if(attentionService.isAttention(wlbUser.getUserId())){
					  w.setIsAttention("1");
				  }else{
					  w.setIsAttention("0");
				  };
				}
			}
			Boolean b=service.add(w);
			if(b){
		        HuoResponse h = new HuoResponse(w);
				Map<String,HuoResponse> map = new HashMap<String,HuoResponse>();
				map.put("Wftransport", h);
				//发送三分地后台管理提示通知
				WebSocketUtil.sendSystemMessage(Purview.HYSH, "有一条危废货源信息需要审核!");
				return new Response().success(map);
			}
		
		} catch (JSONException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("请求参数异常!");
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
		
		return new Response().failure();
	}
	

	
	/**
	 * 得到货源详情信息
	 * @return
	 */
	@RequestMapping(value="/wlbApp/getHuoDetail/{id}" ,method = RequestMethod.GET)
	@ResponseBody
	public Response getHuoDetail(@PathVariable String id){
		if(!StringUtil.isEmpty(id)){
			Object huo  = (Object) service.getWftransportById(id,1);
		    	Map<String,Object> map = new HashMap<String,Object>();
				map.put("Response", huo);
				return new Response().success(map);
		}
		   return new Response().failure("传来的id为空");
	}
	
	/**
	 * 根据ids删除货源,如果ids中有,,说明是多个id
	 * @return
	 */
	@RequestMapping(value="/wlbApp/deleteHuo/{ids}" ,method = RequestMethod.GET)
	@ResponseBody
	public Response deleteHuo(@PathVariable String ids){
		if(!StringUtil.isEmpty(ids)){
		   boolean b = service.deleteWftransportById(ids);
		   if(b){
			   return new Response().success("删除成功");
		   }
		   return new Response().failure("删除失败!");
		}
		 return new Response().failure("传来的ids为空!");
	}
	
	/**
	 * 修改货源信息
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/wlbApp/modifyHuo" ,method = RequestMethod.POST)
	@ResponseBody
	public Response modifyHuo(@RequestBody byte[] by){
		try {
			AddHuoForm af = (AddHuoForm) new AddHuoForm().getObject(by);
			if(!StringUtil.isEmpty(af.id)){
				Wftransport w = service.getWftransportById(af.id);
				if(w!=null){
					   List<String> start = new ArrayList<>();
						List<String> end = new ArrayList<>();
				        if(!StringUtil.isEmpty(af.endProvinceId)){
							end.add(af.endProvinceId);
						}
				        if(!StringUtil.isEmpty(af.endCityId)){
							end.add(af.endCityId);
						}
						if(!StringUtil.isEmpty(af.endAreaId)){
							end.add(af.endAreaId);
						}
						w.setEndPlace(StringUtil.arrayToString(end, "|"));
				        if(!StringUtil.isEmpty(af.startProvinceId)){
							start.add(af.startProvinceId);
						}
						if(!StringUtil.isEmpty(af.startCityId)){
							start.add(af.startCityId);
						}
						if(!StringUtil.isEmpty(af.startAreaId)){
							start.add(af.startAreaId);
						}
					   w.setStartPlace(StringUtil.arrayToString(start, "|"));
					   w.setEndTime(af.endTime);
					   w.setEndTime(af.endTime);
					   if(af.startFullPath!=null){
						w.setStartFullPath(StringUtil.arrayToString(af.startFullPath, ","));
					   }
					   if(af.endFullPath!=null){
							 w.setEndFullPath(StringUtil.arrayToString(af.endFullPath, ","));
						}
						w.setStartHabitatAddress(af.startHabitatAddress);
						w.setEndHabitatAddress(af.endHabitatAddress);
						w.setSupplyName(af.supplyName);
						w.setWfpysParentId(af.wfpysParentId);
						w.setWfpysName(af.wfpysName);
						w.setCategoryName(af.categoryName);
						if(!StringUtil.isEmpty(af.wfpTotal)){
						w.setWfpTotal(new BigDecimal(af.wfpTotal));
						}
						if(!StringUtil.isEmpty(af.cargoVolume)){
						w.setCargoVolume(new BigDecimal(af.cargoVolume));
						}
						if(!StringUtil.isEmpty(af.carLength)){
							w.setCarLength(new BigDecimal(af.carLength));
						}
						if(!StringUtil.isEmpty(af.sizeUnit)){
							w.setSizeUnit(af.sizeUnit);
						}
						if(!StringUtil.isEmpty(af.unit)){
							w.setUnit(af.unit);
						}
						// 货物体积单位
						if(!StringUtil.isEmpty(af.volumeUnit)){
							w.setVolumeUnit(af.volumeUnit);
						}
						w.setValidTime(af.validTime);
						w.setPhone(af.phone);
						w.setLinkMan(af.linkMan);
						w.setContent(af.content);
						w.setCarName(af.carName);
						w.setTankMaterialName(af.tankMaterialName);
						w.setCheckStatus(0);
						w.setStatus(0);
						// 更新时间
						w.setUpdateTime(DateUtil.getSysDateTimeString());
						boolean b = service.updateWftransport(w);
						if(b){
							//发送三分地后台管理提示通知
							WebSocketUtil.sendSystemMessage(Purview.HYSH, "有一条危废货源信息需要审核!");
							return new Response().success("修改成功");
						}
						return new Response().failure("修改失败!");
				}
				        return new Response().failure("未得到货源信息!!");
			}
		}catch (JSONException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("请求参数异常!");
		}catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("要求车源长度,货物重量和体积为数字!");
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
		
		 return new Response().failure();
	}
	
	/**
	 * 根据货源的id得到货源信息在推荐车源
	 * ,通过车源地starPlace(地区代码),车辆类型carName(不锈钢罐车),经营范围wfpysParentId(008000000),货物类型categoryName(液体),车辆载重wfpTotal推荐车源信息
	 * @param by
	 * @return
	 */
	@RequestMapping(value="/wlbApp/recommendChe", produces = "application/json;charset=UTF-8",method=RequestMethod.POST)
	public @ResponseBody Response recommendChe(@RequestBody byte[] by,HttpServletRequest request){
		try{
		    RecommendCheForm rf=(RecommendCheForm) new RecommendCheForm().getObject(by);
		    Pager page = new Pager();
			// 将从页面获取的分页数据转化成int型
			int currentPage = 1;
			if (!StringUtil.isEmpty(rf.currentpageStr)) {
				currentPage = Integer.parseInt(rf.currentpageStr);
			}
			// 设置要查询的页码
			page.setCurrentPage(currentPage);
			page.setRowsPerPage(10);
		if(!StringUtil.isEmpty(rf.id)){
		   Wftransport wftransport = service.getWftransportById(rf.id);
		   if(wftransport!=null){
			   String startPlace = wftransport.getStartPlace();
			   String wfpysParentId = wftransport.getWfpysParentId();
	           List<CarSource> list = service.recommendChe(page,startPlace,"","","0","","1","1","","","","","",true,rf.userId,request);
				if(list==null||list.isEmpty()){
					list=service.recommendChe(page,"","","","0","","1","1","","","",wfpysParentId,"",true,rf.userId,request);
							if(list==null || list.isEmpty()){
							list=service.recommendChe(page,"","","","0","","1","1","","","","","",true,rf.userId,request);
					}
				}
			
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("Pager", page);
				map.put("RecommendChe", list);
				return new Response().success(map);
		   }
		}
		       return new Response().failure("未得到货源id信息!");
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 上架或下架货源
	 * @param 
	 * @return
	 */
	@RequestMapping(value="/wlbApp/shelvesGoods", produces = "application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public  Response shelvesGoods(@RequestBody byte[] by){
		try {
			ShelvesGoodsForm sf = (ShelvesGoodsForm) new ShelvesGoodsForm().getObject(by);
			if(StringUtil.isEmpty(sf.id)){
				return new Response().failure("商品id为空!");
			}
			    Wftransport wftransport = service.getWftransportById(sf.id);
			    if(wftransport!=null&&!StringUtil.isEmpty(sf.status)){
			    	if(sf.status.equals("1")&&wftransport.getCheckStatus()!=1){
			    		return new Response().failure("商品未审核通过,无法上架");
			    	}
			    	wftransport.setStatus(Integer.parseInt(sf.status));
			    	// 更新时间
			    	wftransport.setUpdateTime(DateUtil.getSysDateTimeString());
					 boolean b = service.updateWftransport(wftransport);
					 if(b){
						 return new Response().success("商品上下架成功");
					 }
			    }
			   return new Response().failure("货源id为空!");
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return new Response().failure("货源上下架失败!");
		}
	
	}
	
	/**
	 * 获取用户的货源
	 * @return
	 */
	@RequestMapping(value="/wlbApp/findHuoYuan")
	@ResponseBody
	public Response findHuoYuan(HttpServletRequest request,@RequestBody byte[] by){
	try {
		FindHuoForm ff=(FindHuoForm) new FindHuoForm().getObject(by);
		  Pager page = new Pager();
			// 将从页面获取的分页数据转化成int型
			int currentPage = 1;
			if (!StringUtil.isEmpty(ff.currentpageStr)) {
				currentPage = Integer.parseInt(ff.currentpageStr);
			}
			// 设置要查询的页码
			page.setCurrentPage(currentPage);
			page.setRowsPerPage(10);
		//获取用户token
		String token = request.getHeader("WLB-Token");
		//通过token获取用户id
		String userid = token.split("_")[0];
		List<HuoResponse> listHuo=new ArrayList<HuoResponse>();
			if(!StringUtil.isEmpty(userid)){
				//得到所有的用户货源
				if("0".equals(ff.status)){
					List<Wftransport> all= service.getByStoreId(userid,page);
					 if(all!=null){
						  for (Wftransport wftransport : all) {
							int status=  wftransport.getStatus();
							int checkStatus=  wftransport.getCheckStatus();
							String endTime=wftransport.getEndTime();
							String sysTime=DateUtil.getSysDateTimeString();
			            	if(StringUtil.isEmpty(endTime) || endTime.compareTo(sysTime)<0){		  
							
							  HuoResponse h=new HuoResponse(wftransport, "5");
							    listHuo.add(h);
						  }else{
							  if(status==1&&checkStatus==1){
								    HuoResponse h=new HuoResponse(wftransport, "1");
								    listHuo.add(h);
								  }else if(status==0&&checkStatus==1){
									    HuoResponse h=new HuoResponse(wftransport,"2");
									    listHuo.add(h);
								  }else if(checkStatus==0){
									    HuoResponse h=new HuoResponse(wftransport,"3");
									    listHuo.add(h);
								  }else if(checkStatus==2){
									    HuoResponse h=new HuoResponse(wftransport,"4");
									    listHuo.add(h);
								  }
						  }
						}
				    }
				//   return (Response) listHuo;
				}
				//得到已上架货源(条件:)
				if("1".equals(ff.status)){
				    List<Wftransport> all = service.getByStoreId(userid,page,"1","1");
				    if(all!=null){
						  for (Wftransport wftransport : all) {
							  HuoResponse h=new HuoResponse(wftransport,ff.status);
								listHuo.add(h);
						}
				    }
				
				}
				//得到下架货源
				if("2".equals(ff.status)){
				    List<Wftransport> all = service.getByStoreId(userid,page,"0","1");
				    if(all!=null){
						  for (Wftransport wftransport : all) {
							  HuoResponse h=new HuoResponse(wftransport, ff.status);
								listHuo.add(h);
						}
				    }
				
				}
				//得到正在审核的货源
				if("3".equals(ff.status)){
				    List<Wftransport> all = service.getByStoreId(userid,page,"","0");
				    if(all!=null){
						  for (Wftransport wftransport : all) {
							  HuoResponse h=new HuoResponse(wftransport,ff.status);
								listHuo.add(h);
						}
				    }
				
				}
				//得到审核未通过的货源
				if("4".equals(ff.status)){
				    List<Wftransport> all = service.getByStoreId(userid,page,"","2");
				    if(all!=null){
						  for (Wftransport wftransport : all) {
							  HuoResponse h=new HuoResponse(wftransport,ff.status);
								listHuo.add(h);
						}
				    }
				
				}
				//得到已完成的货源
				if("5".equals(ff.status)){
					List<Wftransport> all= service.getByStoreId(userid,page,1);
					  if(all!=null){
						  for (Wftransport wftransport : all) {
							  HuoResponse h=new HuoResponse(wftransport, ff.status);
								listHuo.add(h);
						}
				    }
				}
			
			  Map<String,Object> m = new HashMap<String,Object>();
			  m.put("Pager", page);
			  m.put("Wftransports", listHuo);
			  return new Response().success(m);
		}
		return new Response().failure("用户id不正确!");
	} catch (Exception e) {
		e.printStackTrace();
		// 记日志
		log.error(e.getMessage());
		return new Response().failure();
	}
		
	
}
}

















