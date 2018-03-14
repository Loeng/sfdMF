package com.ekfans.controllers.store.qualification;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.EmergencyPlan;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IEmergencyPlanService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
@Controller
public class EmergencyPlanController extends BasicController{
	@Autowired
	private IEmergencyPlanService quaService;
	@Autowired
	private IStoreService storeService;
	
	@RequestMapping(value = "/store/qualification/save")
	@ResponseBody
	public String save(EmergencyPlan qua,HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		if(quaService.save(qua, request, response)){
			return "1";//添加成功
		}else{
			return "0";//添加失败
		}
	}
	/**
	 * 进入新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/store/qualification/add")
	public String add() {
		return "/userCenter/store/qualification/qualificationAdd";
	}
	/**
	 * 修改状态  是否有效 把无效修改成有效
	 * @param qua
	 * @param id
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/store/qualification/startUse")
	@ResponseBody
	public String startUse(String quaId,HttpServletRequest request, HttpServletResponse response,
			HttpSession session){
		if(quaService.startUse(quaId, request, response)){
			return "1";//启用成功
		}else{
			return "0";//启用失败
		}
	}
	@RequestMapping(value = "/store/qualification/update")
	@ResponseBody
	public String update(EmergencyPlan qua,String id,HttpServletRequest request, HttpServletResponse response,
			HttpSession session){
		Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
		qua.setStoreId(store.getId());
		// 设置附件保存路径
		String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
		// 调用方法获取分类图标，返回图标路径
		String file = FileUploadHelper.uploadFile("file", currentPath, request, response);
		qua.setFile(file);
		//设置更新时间
		qua.setUpdateTime(DateUtil.getSysDateString());
		
		if(quaService.updateBean(qua)){
			return "1";
		}else{
			return "0";
		}
	}
	
	/**
	 * 单个删除
	 * @param quaId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/qualification/delQua")
	@ResponseBody
	public String delQua(String quaId,HttpServletRequest request){
		if(!StringUtil.isEmpty(quaId)){
			//根据页面传过来的id查询对象
			EmergencyPlan qua=quaService.getQuaById(quaId);
			//如果状态为有效的话不能删除
			if(qua.getType().equals("1")){
				return "2";
			}
			if(quaService.deleteById(quaId)){
				return "1";//删除成功
			}
		}
		return "0";//删除失败
	}
	
	/**
	 * 批量删除
	 * @param quaIds
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/qualification/delQuas")
	@ResponseBody
	public String delQuas(String[] quaIds,HttpServletRequest request){
		if(quaIds!=null || quaIds.length>0){
			EmergencyPlan qua=new EmergencyPlan();
			for(int i=0;i<quaIds.length;i++){
				//根据页面传过来的id得到对象
				qua=quaService.getQuaById(quaIds[i]);
				//如果状态为有效的话不能删除
				if(qua.getType().equals("1")){
					return "2";
				}
			}
			if(quaService.delete(quaIds)){
				return "1";//删除成功
			}
		}
		return "0";//删除失败
	}
	
	/**
	 * 进入查看或者修改页面
	 * @param seeType
	 * @param quaId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/store/qualification/getQuaById")
	public String getQuaById(int seeType,String quaId,HttpServletRequest request){
		if(!StringUtil.isEmpty(quaId)){
			//根据页面传过来的id得到对象
			EmergencyPlan qua=quaService.getQuaById(quaId);
			//把数据传到页面
			request.setAttribute("qua", qua);
			if(seeType==1){//进入查看页面
				return "/userCenter/store/qualification/qualificationShow";
			}else if(seeType==2){//进入修改页面
				return "/userCenter/store/qualification/qualificationUpdate";
			}
		}
		return null;
	}
	@RequestMapping(value = "/store/qualification/getQuaList")
	public String getQuaList(String startTime,String endTime,
			HttpServletRequest request,HttpServletResponse response){
		Pager page = new Pager();
		// 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            currentPage = Integer.parseInt(currentpageStr);
        }
        // 设置要查询的页码
        page.setCurrentPage(currentPage);
        page.setRowsPerPage(10);
        //得到集合
        List<EmergencyPlan> list=quaService.getQuaByStoreId(page, startTime, endTime, request, response);
		//把数据传到页面
        request.setAttribute("quaList", list);
		request.setAttribute("pager", page);
        request.setAttribute("currentpageStr", currentpageStr);
        request.setAttribute("startTime", startTime);
        request.setAttribute("endTime", endTime);
        
		return "/userCenter/store/qualification/qualificationList";
	}
}
