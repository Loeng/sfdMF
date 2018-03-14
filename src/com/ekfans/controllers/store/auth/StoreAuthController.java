package com.ekfans.controllers.store.auth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.StoreFinancialData;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreFinancialDataService;
import com.ekfans.base.store.service.ILegalResumeService;
import com.ekfans.base.store.service.IStoreInfoService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.store.auth.VO.AttributeVO;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class StoreAuthController extends BasicController {
	
	@Resource
	private IStoreService storeService;
	@Resource
	private IStoreInfoService storeInfoService;
	@Resource
	private ILegalResumeService legalResumeService; 
	@Resource
	private IStoreFinancialDataService financialDataService; 
	
	

	/**
	 * 跳转第一步页面
	 */
	@RequestMapping(value = "/store/auth/jumpAuthOnePage")
	public String jumpAuthOnePage(HttpServletRequest request){
		Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
		
		// 绑定页面显示需要的数据
		request.setAttribute("store",  this.storeService.getStore(store.getId()));
		request.setAttribute("storeInfo", this.storeInfoService.getStoreInfoById(store.getId()));
		
		return "/userCenter/store/authenticate/auth_one";
	}
	
	/**
	 * 保存第一步认证信息
	 * 
	 * @return 1:成功，2:失败，3:请输入企业logo，4:请输入营业执照
	 */
	@RequestMapping(value = "/store/auth/updateAuthOne")
	@ResponseBody
	public int updateAuthOne(@ModelAttribute AttributeVO vo, HttpServletRequest request, HttpServletResponse response){
		// 企业LOGO
		String logo = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		logo = FileUploadHelper.uploadFile("storeLogo", logo, request, response);
		// 营业执照
		String blc = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		blc = FileUploadHelper.uploadFile("businessLicense", blc, request, response);
		
		if(StringUtil.isEmpty(logo)){
			return 3;
		}
		if(StringUtil.isEmpty(blc)){
			return 4;
		}
		
		/*vo.getStore().setStoreLogo(logo);
		vo.getStore().setBusinessLicense(blc);
		
		if(this.storeService.updateStoreOrStoreInfoOrLgOrCa(vo.getStore(), vo.getSi(), null, null, 1)){
			HttpSession session = request.getSession();
			session.removeAttribute(SystemConst.STORE);
			session.setAttribute(SystemConst.STORE, this.storeService.getStore(vo.getStore().getId()));
			return 1;
		}*/
		return 2;
	}
	
	/**
	 * 跳转第二步页面
	 */
	@RequestMapping(value = "/store/auth/jumpAutoTwoPage")
	public String jumpAutoTwoPage(HttpServletRequest request){
		Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
		
		// 绑定页面显示需要的数据
		request.setAttribute("store",  this.storeService.getStore(store.getId()));
		request.setAttribute("storeInfo", this.storeInfoService.getStoreInfoById(store.getId()));
		request.setAttribute("lrlist", this.legalResumeService.getLegalResumeByStoreId(store.getId()));
		
		return "/userCenter/store/authenticate/auth_two";
	}
	
	/**
	 * 保存第二步认证信息
	 */
	@RequestMapping(value = "/store/auth/updateAuthTwo")
	@ResponseBody
	public Boolean updateAuthTwo(@ModelAttribute AttributeVO vo, HttpServletRequest request, HttpServletResponse response){
		Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
		
		/*if(this.storeService.updateStoreOrStoreInfoOrLgOrCa(store, vo.getSi(), vo.getLrlist(), null, 2)){
			HttpSession session = request.getSession();
			session.removeAttribute(SystemConst.STORE);
			session.setAttribute(SystemConst.STORE, this.storeService.getStore(store.getId()));
			return true;
		}*/
		return false;
	}
	
	/**
	 * 跳转到第三步页面
	 */
	@RequestMapping(value = "/store/auth/jumpAutoThreePage")
	public String jumpAutoThreePage(HttpServletRequest request){
		Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
		
		// 绑定页面显示需要的数据
		request.setAttribute("store",  this.storeService.getStore(store.getId()));
		//request.setAttribute("fdlist",  this.financialDataService.getFinancialDataByStoreId(store.getId()));
		
		return "/userCenter/store/authenticate/auth_three";
	}
	
	/**
	 * 保存第三步认证信息
	 */
	@RequestMapping(value = "/store/auth/updateAuthThree")
	@ResponseBody
	public Boolean updateAuthTwo(HttpServletRequest request){
		Store store = (Store)request.getSession().getAttribute(SystemConst.STORE);
		SimpleDateFormat sdf = null;
		String[] dType = request.getParameterValues("dType"); // 类型
		String[] dsid = request.getParameterValues("dsid"); // 上年末
		String[] deid = request.getParameterValues("deid"); // 本年上月末
		String[] dsMoney = request.getParameterValues("dsMoney"); // 上年末
		String[] deMoney = request.getParameterValues("deMoney"); // 本年上月末
		
		List<StoreFinancialData> list = null;
		if(dType != null){
			sdf = new SimpleDateFormat("yyyy");
			int year = Integer.valueOf(sdf.format(new Date())) - 1;
			
			list = new ArrayList<StoreFinancialData>();
			
			for (int i = 0; i < dType.length; i++) {
				StoreFinancialData fd = new StoreFinancialData();
				fd.setId((dsid == null || dsid.length == 0) ? "": dsid[i]);
				fd.setStoreId(store.getId());
				fd.setDataTime(year + "");
				fd.setDataType(dType[i]);
				try {
					fd.setMoney(StringUtil.isEmpty(dsMoney[i]) ? 0.00 : Double.valueOf(dsMoney[i]));
				} catch (Exception e) {
					fd.setMoney(0.00);
				}
				list.add(fd);
			}
			
			sdf = new SimpleDateFormat("yyyy-MM");
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.MONTH, -1);
			for (int i = 0; i < dType.length; i++) {
				StoreFinancialData fd = new StoreFinancialData();
				fd.setId((deid == null || deid.length == 0) ? "": deid[i]);
				fd.setStoreId(store.getId());
				fd.setDataTime(sdf.format(ca.getTime()));
				fd.setDataType(dType[i]);
				try {
					fd.setMoney(StringUtil.isEmpty(deMoney[i]) ? 0.00 : Double.valueOf(deMoney[i]));
				} catch (Exception e) {
					fd.setMoney(0.00);
				}
				list.add(fd);
			}
		}
		
		if(this.storeService.updateStoreOrStoreInfoOrLgOrCa(store, null, null, list, 3)){
			HttpSession session = request.getSession();
			session.removeAttribute(SystemConst.STORE);
			session.setAttribute(SystemConst.STORE, this.storeService.getStore(store.getId()));
			return true;
		}
		return false;
	}
}
