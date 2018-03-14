package com.ekfans.controllers.system.user;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.IntegralLog;
import com.ekfans.base.user.service.IIntegralService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class UserIntegralController extends BasicController {

    @Resource
    private IIntegralService IintegralService;
    
    /**
     * 跳转到 修改会员 积分的界面
     * 
     * @return
     */
    @RequestMapping(value = "/system/user/integral/modify")
    public String modify() {
        return "/system/user/integral/userIntegralModify";
    }
    
    /**
     * 查询用户的积分
    * @param nameValue
    * @param session
    * @return int 返回类型
    * @throws request.setAttribute("modifyOk", true);
     */
    @RequestMapping(value = "/system/user/integral/getUserNameIntegral")
    @ResponseBody
    public String getUserNameIntegral(HttpServletRequest request) {
    	String name = request.getParameter("name");
        
        String integration=IintegralService.getUserNameIntegral(name);
        
        return integration;
    }
   
    /**
     * 
    * @Title: saveInteral
    * @Description: TODO(保存后台更改用户的积分)
    * 详细业务流程:
    * 保存后台更改用户的积分
    * @param interalLog
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value ="/system/user/integral/saveInteral")
    public String saveInteral(IntegralLog interalLog,HttpServletRequest request){
	
        Manager manager = (Manager)request.getSession().getAttribute(SystemConst.MANAGER);
        String id = manager.getId();
        String flag= IintegralService.updateIntegral(interalLog,id);
        request.setAttribute("returnType", flag);
        return "/system/user/integral/userIntegralModify";
        
    }
    
   
    /**
     * 
    * @Title: saveInteral
    * @Description: TODO(保存后台更改用户的积分)
    * 详细业务流程:
    * 保存后台更改用户的积分
    * @param interalLog
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value ="/system/user/integral/integralLogList")
    public String integralLogList(HttpServletRequest request){
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
        List<IntegralLog>lil= IintegralService.integralLogList(pager, name, status,beginDate, endDate);
        request.setAttribute("integralLogs", lil);
        request.setAttribute("pager", pager);
        request.setAttribute("name", name);
        request.setAttribute("endDate",endDate);
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("status",status);
       
       return "/system/user/integral/userIntegralLogList";
    }
    
    /**
     * 删除日志
     * @return
     */
    @RequestMapping(value = "/system/user/integral/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable String id) {
            // 利用Service的方法根据id删除用户
            if(StringUtil.isEmpty(id)){
                return false;
            }
          boolean flag= IintegralService.deleteUserIngegral(id);
          return flag;
    }
    
}
