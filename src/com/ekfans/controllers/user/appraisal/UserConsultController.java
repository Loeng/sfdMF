package com.ekfans.controllers.user.appraisal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ekfans.base.store.model.Consult;
import com.ekfans.base.store.service.IStoreConsultService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: UserConsultController
* @Description: TODO  用户中心的留言管理
* @author 成都易科远见科技有限公司
* @date May 27, 2014 7:26:40 PM
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class UserConsultController extends BasicController{
    
    @Autowired
    private IStoreConsultService storeConsultService;
    /**
     * 
    * @Title: view
    * @Description: TODO 跳转到留言页面，并且在页面显示出留言  条件查询留言
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @return    设定文件
    * @return ModelAndView    返回类型
    * @throws
     */
    @RequestMapping(value="/user/consult")
    public ModelAndView view(HttpServletRequest request){
	Pager pager = new Pager();
	User user=(User) request.getSession().getAttribute(SystemConst.USER);
	String creator = user.getId();
	String storeId = request.getParameter("storeId");
	String beginDate = request.getParameter("beginDate");
	String endDate = request.getParameter("endDate");
	String replyStatus = request.getParameter("replyStatus");
	// 从页面获取分页数据
	String currentpageStr = request.getParameter("pageNum");

	// 将从页面获取的分页数据转化成int型
	int currentPage = 1;
	if (!StringUtil.isEmpty(currentpageStr)) {
		try {
			currentPage = Integer.parseInt(currentpageStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 设置要查询的页码
	pager.setCurrentPage(currentPage);
	List<Consult> consults = storeConsultService.getConsultByUserId(creator,storeId,beginDate,endDate,replyStatus,pager);
	for(Consult cs : consults){
	    cs.setChildList(storeConsultService.getByParentId(cs.getId()));
	}
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("pager", pager);
	map.put("consults", consults);
	map.put("creator", creator);
	map.put("storeId", storeId);
	map.put("beginDate", beginDate);
	map.put("endDate", endDate);
	map.put("replyStatus", replyStatus);
	request.setAttribute("cur","consult");
	return new ModelAndView("/userCenter/customer/appraisal/userConsult", map);
    }
}
