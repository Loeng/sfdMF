package com.ekfans.controllers.system.viratualLog;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.ViratualLog;
import com.ekfans.base.user.service.IViratualLogService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class UserViratualLogController extends BasicController {

    @Resource
    private IViratualLogService viratualLogService;
    
    /**
     * 跳转到 修改账户页面
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/viratualLog/modify")
    public String modify() {
        //绑定的参数
        String jugg = getRequest().getParameter("jugg");
        getRequest().setAttribute("jugg", jugg);
        return "/system/user/viratualLog/userViratualLogModify";
    }
    
    /**
     * 查询用户的账户资金
    * @param nameValue
    * @param session
    * @return int 返回类型
    * @throws request.setAttribute("modifyOk", true);
     */
    @RequestMapping(value = "/system/user/amountLog/getUserNameAmountLog/{nameValue}/{jugg}")
    @ResponseBody
    public String getUserNameIntegral(@PathVariable String nameValue,@PathVariable String jugg) {
        
        String integration=viratualLogService.getUserNameIntegral(nameValue,jugg);
        
        return integration;
    }
   
    /**
     * 
    * @Title: saveInteral
    * @Description: TODO(保存后台更改用户的账户)
    * 详细业务流程:
    * 保存后台更改用户的积分
    * @param interalLog
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value ="/system/user/viratualLog/saveViratualLog")
    public String saveInteral(ViratualLog v,HttpServletRequest request){
        //得到绑定对应需要查询的数据是什么角色
        String jugg = request.getParameter("jugg");
        Manager manager = (Manager)request.getSession().getAttribute(SystemConst.MANAGER);
        String id = manager.getId();
        String flag= viratualLogService.updateIntegral(v,id);
        request.setAttribute("returnType", flag);
        request.setAttribute("jugg", jugg);
        return "/system/user/viratualLog/userViratualLogModify";
        
    }
    /**
     * 
    * @Title: saveInteral
    * @Description: TODO(账户资金的列表)
    * 详细业务流程:
    * 保存后台更改用户的积分
    * @param interalLog
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value ="/system/user/amountLog/amountLogList")
    public String integralLogList(HttpServletRequest request){
        //得到需要查询的是什么角色的数据
        String jugg = request.getParameter("jugg");
        //查询的用户名
        String name=request.getParameter("name");
        String status=request.getParameter("status");
        //开始时间
        String beginDate=request.getParameter("beginDate");
        String endDate=request.getParameter("endDate");
        
        //页码号
        String currentpageStr = request.getParameter("pageNum");
        
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            try {
                currentPage = Integer.parseInt(currentpageStr);
            } catch (Exception e) {
            }
        }
        // 定义分页  设置要查询的页码
        Pager pager = new Pager();
        pager.setCurrentPage(currentPage);
        List<ViratualLog> viratualLogs= viratualLogService.integralLogList(pager, name, status,beginDate, endDate,jugg);
        request.setAttribute("viratualLogs", viratualLogs);
        request.setAttribute("pager", pager);
        request.setAttribute("name", name);
        request.setAttribute("endDate",endDate);
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("status",status);
        request.setAttribute("jugg", jugg);
       
       return "/system/user/viratualLog/userViratualLogList";
    }
}
