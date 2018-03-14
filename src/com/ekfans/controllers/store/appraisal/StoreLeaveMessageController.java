package com.ekfans.controllers.store.appraisal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Consult;
import com.ekfans.base.store.service.IStoreConsultService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class StoreLeaveMessageController {
    
    @Autowired
    private IStoreConsultService storeConsultService;
    
    
    /**
     * 
    * @Title: getLeaveMessageByCondition
    * @Description: TODO(留言管理)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value = "/store/appraisal/leaveMessage")
    public String getLeaveMessageByCondition(HttpServletRequest request){
	// 得到userName
	String userName = request.getParameter("userName");
	// 得到beginDate
	String beginDate = request.getParameter("beginDate");
	// 得到endDate
	String endDate = request.getParameter("endDate");
	// 得到status
	String status = request.getParameter("status");	
	
	// 分页
        Pager pager = new Pager();
        String currentpageStr = request.getParameter("pageNum");//获取当前页码
        // 默认展示第一页
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            currentPage = Integer.parseInt(currentpageStr);
        }
        pager.setCurrentPage(currentPage);
        
        
        List<Consult> storeConsults = 
            storeConsultService.getLeaveByCondition(userName,beginDate,endDate,status,pager);
        request.setAttribute("storeConsults",storeConsults);
        
        return "/store/appraisal/leaveMessage";
    }
}
