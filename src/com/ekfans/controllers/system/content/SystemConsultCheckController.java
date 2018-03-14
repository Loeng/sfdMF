package com.ekfans.controllers.system.content;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Consult;
import com.ekfans.base.store.service.IStoreConsultService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: SystemConsultCheckController
* @Description: TODO(咨询留言审核)
* @author HJC
* @date 2014-6-13 上午11:42:27
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class SystemConsultCheckController {
    @Autowired private IStoreConsultService consultService;
    
    /**
     * 
    * @Title: consultList
    * @Description: TODO(待审核留言列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/system/consultlist")
    public String consultList(HttpServletRequest request){
		String person = request.getParameter("person");
		String content = request.getParameter("content");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String consultType = "1";
		String pageNum = request.getParameter("pageNum");
		
		Pager pager = new Pager();
		if(StringUtil.isEmpty(pageNum)){
		    pageNum = "1";
		}
		pager.setCurrentPage(Integer.parseInt(pageNum));
		List<Consult> consults = 
		    consultService.getConsultListByCondition(content,person,beginDate,endDate,consultType,pager);
		
		request.setAttribute("person",person);
		request.setAttribute("content",content);
		request.setAttribute("beginDate",beginDate);
		request.setAttribute("endDate",endDate);
		request.setAttribute("pageNum",pageNum);
		request.setAttribute("pager",pager);
		System.out.println(consults);
		request.setAttribute("consults",consults);
		
		return "/system/content/consultCheck/consultCheckList";
    }
    
    /**
     * 
    * @Title: consultList
    * @Description: TODO(待审核咨询列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/system/advisorylist")
    public String advisoryList(HttpServletRequest request){
		String person = request.getParameter("person");
		String content = request.getParameter("content");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String consultType = "0";
		String pageNum = request.getParameter("pageNum");
		
		Pager pager = new Pager();
		if(StringUtil.isEmpty(pageNum)){
		    pageNum = "1";
		}
		pager.setCurrentPage(Integer.parseInt(pageNum));
		List<Consult> consults = 
		    consultService.getConsultListByCondition(content,person,beginDate,endDate,consultType,pager);
		
		request.setAttribute("person",person);
		request.setAttribute("content",content);
		request.setAttribute("beginDate",beginDate);
		request.setAttribute("endDate",endDate);
		request.setAttribute("pageNum",pageNum);
		request.setAttribute("pager",pager);
		System.out.println(consults);
		request.setAttribute("consults",consults);
		
		return "/system/content/consultCheck/advisoryCheckList";
    }
    
    
    
    
    /**
     * 
    * @Title: goToConsultConsultCheck
    * @Description: TODO(跳往内容审核页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/system/goToconsultCheck")
    public String goToConsultConsultCheck(HttpServletRequest request){
	String consultId = request.getParameter("consultId");
	Consult consult = 
	    consultService.getConsultDetailByConsultId(consultId);
	request.setAttribute("consult",consult);
	return "/system/content/consultCheck/consultCheck";
    }
    
    /**
     * 
    * @Title: showConsultDetail
    * @Description: TODO(显示审核内容的详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/system/showConsultDetail")
    public String showConsultDetail(HttpServletRequest request){
	String consultId = request.getParameter("consultId");
	Consult consult = consultService.getConsultDetailByConsultId(consultId);
	request.setAttribute("consult",consult);
	return "/system/content/consultCheck/consultCheckListModel";
    }
    
    /**
     * 
    * @Title: consultCheck
    * @Description: TODO(留言审核)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/system/Check")
    public String consultCheck(HttpServletRequest request,HttpSession session){
		String consultId = request.getParameter("consultId");
		String checkMan = ((Manager)session.getAttribute(SystemConst.MANAGER)).getRealName();
		String checkStatus = request.getParameter("checkStatus");
		String checkNote = request.getParameter("checkNote");
		consultService.checkConsult(consultId,checkMan,Integer.parseInt(checkStatus),checkNote);
		return "redirect:/system/consultlist";
    }
    
    /**
     * 
    * @Title: advisoryCheck
    * @Description: TODO(咨询审核)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @param session
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/system/advisoryCheck")
    public String advisoryCheck(HttpServletRequest request,HttpSession session){
	String consultId = request.getParameter("consultId");
	String checkMan = ((Manager)session.getAttribute(SystemConst.MANAGER)).getRealName();
	String checkStatus = request.getParameter("checkStatus");
	String checkNote = request.getParameter("checkNote");
	consultService.checkConsult(consultId,checkMan,Integer.parseInt(checkStatus),checkNote);
	return "redirect:/system/advisorylist";
    }
}
