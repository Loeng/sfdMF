package com.ekfans.controllers.user.appraisal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Consult;
import com.ekfans.base.store.service.IStoreConsultService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: UserConsultMessage
* @Description: TODO 用户中心咨询管理
* @author 成都易科远见科技有限公司
* @date May 15, 2014 1:36:32 PM
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class UserConsultMessageController extends BasicController{
    @Autowired
    private IStoreConsultService storeConsultService;
    /**
     * 
    * @Title: 去往咨询管理页面
    * @Description: TODO 去往咨询管理页面
    * @param @param request 
    * @param @param session 
    * @param @return    设定文件
    * @return ModelAndView    返回类型
    * @throws
     */
    @RequestMapping(value="/user/appraisal/cobsultMessage")
    public String appraisal(HttpServletRequest request,HttpSession session) {
	//从缓存中取得userId
	User user=(User) request.getSession().getAttribute(SystemConst.USER);
	String creator = user.getId();
	//获取查询参数
	//获取查询商品名
	String productName = request.getParameter("productName");
	//获取是否查询回复
	String replyStatus = request.getParameter("replyStatus");
	//获取开始时间
	String beginDate = request.getParameter("beginDate");
	//获取结束时间
	String endDate = request.getParameter("endDate");
	
	
	// 定义分页
	Pager pager = new Pager();
	// 从页面获取分页数据
	String currentpageStr = request.getParameter("pageNum");
    // 默认展示第一页
    int currentPage = 1;
    if (!StringUtil.isEmpty(currentpageStr)) {
         //有值,替换掉默认页码
        currentPage = Integer.parseInt(currentpageStr);
    }
	// 设置要查询的页码
	pager.setCurrentPage(currentPage);
	// 设置煤业只显示4条咨询
	pager.setRowsPerPage(4);
	List<Consult> consults = 
	    storeConsultService.getConsultByCondition(pager,creator,productName
	    		,beginDate,endDate,replyStatus);
	if(null != consults){
		for(Consult cs : consults){
			cs.setChildList(storeConsultService.getByParentId(cs.getId()));
		}
	}
	
	
	request.setAttribute("productName", productName);
	request.setAttribute("replyStatus", replyStatus);
	request.setAttribute("beginDate", beginDate);
	request.setAttribute("endDate", endDate);
	request.setAttribute("cur","cobsultMessage");
	request.setAttribute("pager",pager);
	request.setAttribute("consults",consults);	
	request.setAttribute("currentPage",currentPage);
        return "/userCenter/customer/appraisal/cobsultMessage";
    }
}
