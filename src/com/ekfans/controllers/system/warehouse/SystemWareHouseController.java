package com.ekfans.controllers.system.warehouse;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Warehouse;
import com.ekfans.base.store.service.IWarehouseService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.service.SystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemWareHouseController extends BasicController {
    
	@Autowired
    private SystemAreaService systemAreaService;
    @Autowired
    private IWarehouseService warehouseService;
    /**
     * 
    * @Title: add
    * @Description: TODO(跳转到新增仓库页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/warehouse/warehouseAdd")
    public String add(HttpServletRequest request){
        return "/system/warehouse/warehouseAdd";
    }
    
    /**
     * 新增保存操作
     */
    @RequestMapping(value = "/system/warehouse/warehouseSave")
    public String save(HttpServletRequest request,Warehouse wh){
        wh.setCreateTime(DateUtil.getSysDateTimeString());
        // 设置操作人
        Manager manager = (Manager)request.getSession().getAttribute(SystemConst.MANAGER);
        String id = manager.getId();
        wh.setCreator(id);
        Boolean istrue = warehouseService.add(wh);
        request.setAttribute("addOk", istrue);
       return "/system/warehouse/warehouseAdd";
    }
    
    /**
     * 查找仓库列表
     * 
     * @return
     */
    @RequestMapping(value = "/system/warehouse/list")
    public String list(HttpServletRequest request) {
            // 定义分页
            Pager pager = new Pager();
            // 从页面获取频道名称
            String name = request.getParameter("name");
            // 从页面获取状态
            String status = request.getParameter("status");
            // 从页面获取页码
            String currentpageStr = request.getParameter("pageNum");
            // 将从页面获取的分页数据转化成int型
            int currentPage = 1;
            if (!StringUtil.isEmpty(currentpageStr)) {
                try {
                    currentPage = Integer.parseInt(currentpageStr);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            // 设置要查询的页码
            pager.setCurrentPage(currentPage);
            // 利用Service的方法查找频道
            List<Warehouse> whs = warehouseService.list(pager, name, status);
            request.setAttribute("whs", whs);
            request.setAttribute("pager", pager);
            request.setAttribute("name", name);
            request.setAttribute("status", status);
            return "/system/warehouse/warehouseList";
    }

    /**
     * 删除仓库
     * 
     * @return
     */
    @RequestMapping(value = "/system/warehouse/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable String id) {
            // 利用Service的方法根据id删除用户和积分日志
            if (warehouseService.delete(id)) {
                // 删除成功，返回true
                return true;
            }else{
                return false;
            }
    }
    
    /**
     * 跳转到修改或者详情页面
     * 
     * @return
     */
    @RequestMapping(value = "/system/warehouse/detail/{id}/{type}")
    public String edit(@PathVariable String id,@PathVariable String type,HttpServletRequest request) {
        // 利用Service的方法根据id查找活动
        Warehouse wh = warehouseService.getWarehouseById(id);
        // 如果activity不为空
        if (wh == null) {
            return "redirect:/system/warehouse/list";
        }
        /*--- 省市区回显结束 ---*/
        request.setAttribute("wh", wh);
        request.setAttribute("type", type);
        return "system/warehouse/warehouseDetail";
    }
    
    /**
     * 修改仓库
     * 
     * @return
     */
    @RequestMapping(value = "/system/activity/modify")
    @ResponseBody
    public Object modify(Warehouse wh, HttpServletRequest request, HttpServletResponse response) {
        // 验证活动名是否为空
        if (StringUtil.isEmpty(wh.getName())) {
            // 如果为空，返回添加失败
            return "false";
        }
        // 设置操作人
        Manager manager = (Manager)request.getSession().getAttribute(SystemConst.MANAGER);
        String id = manager.getId();
        wh.setCreator(id);
        // 利用Service的方法修改活动
        if (warehouseService.update(wh)) {
            return "true";
        }else{
            return "false";
        }
    }
}
