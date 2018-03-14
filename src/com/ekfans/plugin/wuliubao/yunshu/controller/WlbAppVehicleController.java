package com.ekfans.plugin.wuliubao.yunshu.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

import com.ekfans.base.store.dao.ICarInfoDao;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.base.store.service.IWftransportService;
import com.ekfans.controllers.ccwccApp.Response;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddVehicleForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarNoRepeatForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarSource;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CheListForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.ReleaseCarSourceForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.Vehicle;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 物流宝车源车辆操作相关接口
 * @author pp
 * @Date 2017年7月10日16:50:29
 */
@Controller
public class WlbAppVehicleController {
	@Autowired
	private IWftransportService service;
	@Resource
	private ICarInfoDao carDao;
	// 定义错误日志
	public static Logger log = LoggerFactory.getLogger(WlbAppVehicleController.class);
	/**
	 * 车源信息控制方法
	 * @param request,通过车源地starPlace(湖南),车辆类型carName(不锈钢罐车),经营范围wfpysParentId(008000000),货物类型categoryName(液体),车辆载重wfpTotal筛选车源信息
	 * @return 所有的车源信息
	 */

	@RequestMapping(value="/wlbApp/getCheList", produces = "application/json;charset=UTF-8",method=RequestMethod.POST)
	public @ResponseBody Response getCheList(@RequestBody byte[] by,HttpServletRequest request){
		try{
			CheListForm cf=(CheListForm) new CheListForm().getObject(by);
		    Pager page = new Pager();
			// 将从页面获取的分页数据转化成int型
			int currentPage = 1;
			if (!StringUtil.isEmpty(cf.currentpageStr)) {
				currentPage = Integer.parseInt(cf.currentpageStr);
			}
			// 设置要查询的页码
			page.setCurrentPage(currentPage);
			page.setRowsPerPage(10);
			//分页,出发地代码,目的地代码,运输名称,类型(0,1),所属公司id,审核状态(0,1,2),是否上下架(0,1),车辆名称,车辆材质,货物类型,危废品运输界定父类ID,是否过了运输截止日期
			List<CarSource> list = service.getCheList(page,cf.starPlace,"","","0","","1","1",cf.carName,"","",cf.wfpysParentId,cf.wfpTotal,true,cf,request);
			Map<String,Object> map = new HashMap<String,Object>();
		    map.put("Pager", page);
			map.put("CarSourceList", list);
		   
			return new Response().success(map);
	         }catch (NumberFormatException e) {
	 			e.printStackTrace();
				// 记日志
				log.error(e.getMessage());
				return new Response().failure("字段[currentPage]必须为数字!");
			} catch (Exception e) {
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	/**
	 * 添加车辆
	 * @param te
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wlbApp/addVehicle",method = RequestMethod.POST)
	@ResponseBody
	public Response addVehicle(@RequestBody byte[] te,HttpServletRequest request){
		try {
			AddVehicleForm add = (AddVehicleForm) new AddVehicleForm().getObject(te);
			Vehicle v = service.addVehicle(add, request);
			return new Response().success(v);
		}catch (JSONException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("请求参数异常!");
		}catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[carLength][carWidth][carHeight][wfpTotal]必须为数字!");
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	/**
	 * 添加车辆是否车牌号重复
	 * @param userID 用户id
	 * @param carNo  车牌号
	 * @return
	 */
	@RequestMapping(value="/wlbApp/carNoRepeat",method = RequestMethod.POST)
	@ResponseBody
	public Response carNoRepeat(@RequestBody byte[] te){
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			CarNoRepeatForm carNo = (CarNoRepeatForm) new CarNoRepeatForm().getObject(te);
			String statusCode = service.carNoRepeat(carNo.carNumber, carNo.userID);
			map.put("statusCode", statusCode);
			return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 删除车辆
	 * @param te
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wlbApp/deleteVehicle/{Vehicleid}",method = RequestMethod.GET)
	@ResponseBody
	public Response deleteVehicle(@PathVariable String Vehicleid){
		try {
			carDao.deleteById(Vehicleid);
			return new Response().success();
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 更新车辆
	 * @param te
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wlbApp/updateVehicle",method = RequestMethod.POST)
	@ResponseBody
	public Response updateVehicle(@RequestBody byte[] te,HttpServletRequest request){
		
		try {
			AddVehicleForm add = (AddVehicleForm) new AddVehicleForm().getObject(te);
			Vehicle v =  service.updateVehicle(add, request);
			if(v==null){
				return new Response().failure();
			}
			return new Response().success(v);
		} catch (JSONException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("请求参数异常!");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[wfpTotal][carLength][carWidth][carHeight]必须为数字!");
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	
	/**
	 * 获取用户车辆
	 * @param te
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wlbApp/getUserVehicle/{currentPage}",method = RequestMethod.GET)
	@ResponseBody
	public Response getUserVehicle(HttpServletRequest request,@PathVariable String currentPage){
		
		Pager page = new Pager();
		int current = 1;
		try {
		if (!StringUtil.isEmpty(currentPage)) {
			current = Integer.parseInt(currentPage);
		}
		page.setCurrentPage(current);
		//获取用户token
		String token = request.getHeader("WLB-Token");
		//通过token获取用户id
		String userid = token.split("_")[0];
		Map<String,Object> map = new HashMap<String,Object>();
		List<Vehicle> list=service.getUserVehicle(userid,page);
		map.put("Pager", page);
		map.put("UserVehicle", list);
		return new Response().success(map);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[currentPage]必须为数字!");
		}catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}

	/**
	 * 发布车源信息
	 * @param te
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wlbApp/releaseCarSource")
	@ResponseBody
	public Response ReleaseCarSource(@RequestBody byte[] te,HttpServletRequest request){
		try {
			ReleaseCarSourceForm release = (ReleaseCarSourceForm) new ReleaseCarSourceForm().getObject(te);
			CarSource c =service.releaseCarSource(release,request);
			return new Response().success(c);
		}  catch (JSONException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("请求参数异常!");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[wfpTotal][carLength][carWidth][carHeight]必须为数字!");
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	/**
	 * 修改车源信息
	 * @param te
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/wlbApp/updataCarSource")
	@ResponseBody
	public Response updataCarSource(@RequestBody byte[] te,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			ReleaseCarSourceForm carSource = (ReleaseCarSourceForm) new ReleaseCarSourceForm().getObject(te);
			map  = service.updateCarSource(carSource);
			return new Response().success(map);
		} catch (JSONException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("请求参数异常!");
		}  catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[wfpTotal][carLength][carWidth][carHeight]必须为数字!");
		}  catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	/**
	 * 删除车源信息
	 * @param carSourceId
	 * @return
	 */
	@RequestMapping(value="/wlbApp/deleteCarSource/{carSourceId}")
	@ResponseBody
	public Response deleteCarSource(@PathVariable String carSourceId){
		try {
			service.deleteWftransportById(carSourceId);
			return new Response().success();
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	/**
	 * 获取用户车源
	 * @return
	 */
	@RequestMapping(value="/wlbApp/getUserCarSource/{currentPage}/{status}")
	@ResponseBody
	public Response getUserCarSource(HttpServletRequest request,@PathVariable String currentPage,@PathVariable String status){
		Pager page = new Pager();
		int current = 1;
		try {
		if (!StringUtil.isEmpty(currentPage)) {
			current = Integer.parseInt(currentPage);
		}
		page.setCurrentPage(current);
		//获取用户token
		String token = request.getHeader("WLB-Token");
		//通过token获取用户id
		String userid = token.split("_")[0];
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<CarSource> list=service.getUserCarSource(userid,page,status);
		map.put("Pager", page);
		map.put("UserCarSource", list);
		return new Response().success(map);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[currentPage]必须为数字!");
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 通过车源id获取用户车源详情
	 * @return
	 */
	@RequestMapping(value="/wlbApp/getUserCarSourceId/{carSourceId}")
	@ResponseBody
	public Response getCarSource(HttpServletRequest request,@PathVariable String carSourceId){
		try {
		Map<String,Object> map = new HashMap<String,Object>();
		CarSource carSource=service.getCarSource(carSourceId);
		map.put("CarSource", carSource);
		return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}	
}
