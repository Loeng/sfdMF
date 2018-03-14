package com.ekfans.controllers.system.warehouse;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.store.model.Warehouse;
import com.ekfans.base.store.model.WarehouseLog;
import com.ekfans.base.store.model.WarehouseOrder;
import com.ekfans.base.store.service.IWarehouseLogService;
import com.ekfans.base.store.service.IWarehouseOrderService;
import com.ekfans.base.store.service.IWarehouseService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemWareHouseLogController extends BasicController {
    
	
    @Autowired
    private IWarehouseService warehouseService;
    
    @Autowired
    private IProductService productService;
    
    @Autowired
    private IWarehouseLogService warehouseLogService;
    
    @Autowired
    private IWarehouseOrderService warehouseOrderService;
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
    @RequestMapping(value = "/system/warehouse/goWarehouseAdd")
    public String add(HttpServletRequest request){
        return "/system/warehouse/goWarehouseAdd";
    }
    
    @RequestMapping(value = "/system/product/plistSearch")
    public String plistSearch(HttpServletRequest request) {
        try {
            // 定义分页
            Pager pager = new Pager();
            // 从页面获取页码
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
            // 利用Service的方法查找店铺
            List<Product> products = productService.getList(pager, null,"2");
            request.setAttribute("products", products);
            request.setAttribute("pager", pager);
            
            
            return "/system/warehouse/warehouseAndProduct";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
    
    @RequestMapping(value = "/system/warehouse/plistSearch")
    public String wlistSearch(HttpServletRequest request) {
        try {
            // 定义分页
            Pager pager = new Pager();
            // 从页面获取页码
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
            // 利用Service的方法查找店铺
            List<Warehouse> whs = warehouseService.list(pager, null, "true");
            request.setAttribute("whs", whs);
            request.setAttribute("pager", pager);
            
            
            return "/system/warehouse/warehouseAndWarehouse";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
    
    /**
     * 新增保存操作
     */
    @RequestMapping(value = "/system/warehouse/goWarehouseSave")
    public String save(HttpServletRequest request,HttpServletResponse response){
        // 页面上得到商品id
        String proId = request.getParameter("productId");
        // 页面上得到的仓库id
        String whId = request.getParameter("warehouseId");
        // 页面上得到的商品数量
        String number = request.getParameter("number");
        // 设置图标保存路径
        String currentPath = "/customerfiles/warehouseLog/orderId/";
        // 调用方法获取分类图标，返回图标路径
        String picPath = FileUploadHelper.uploadFile("orderId", currentPath, request, response);
        // 页面上得到的备注
        String note = request.getParameter("note");
        // 设置操作人
        Manager manager = (Manager)request.getSession().getAttribute(SystemConst.MANAGER);
        String id = manager.getId();
        WarehouseLog whl = new WarehouseLog();
        whl.setProductId(proId);
        whl.setNumber(Integer.valueOf(number));
        whl.setCreateTime(DateUtil.getSysDateTimeString());
        whl.setCreator(id);
        whl.setPic(picPath);
        whl.setNote(note);
        whl.setType(true);
        whl.setWareHouse(whId);
        //得到对应商品并设置入库仓库
        Product p = productService.getProductById(proId);
        if(p != null){
            p.setWareHouseId(whId);
        }
        if(productService.modifyProduct(p) && warehouseLogService.add(whl)){
            request.setAttribute("addOk", true);
            return "/system/warehouse/goWarehouseAdd";
        }else{
            request.setAttribute("addOk", false);
            return "/system/warehouse/goWarehouseAdd";
        }
    }
    
    /**
     * 仓库日志列表
     */
    @RequestMapping(value = "/system/warehouseLog/warehouseLogList")
    public String warehouseLogList(HttpServletRequest request) {
        try {
            // 定义分页
            Pager pager = new Pager();
            // 从页面获取页码
            String currentpageStr = request.getParameter("pageNum");
            //从前台得到的数据
            String name = request.getParameter("name");
            String status = request.getParameter("status");
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
            // 利用Service的方法查找店铺
            List<WarehouseLog> whls = warehouseLogService.list(pager, name, status);
            request.setAttribute("whls", whls);
            request.setAttribute("name", name);
            request.setAttribute("status", status);
            request.setAttribute("pager", pager);
            
            
            return "/system/warehouse/warehouseLogList";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
    /**
     * 
    * @Title: wareHouseLogDetail
    * @Description: TODO(仓库日志详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/warehouseLog/detail/{id}")
    public String wareHouseLogDetail(@PathVariable String id,HttpServletRequest request){
        WarehouseLog whl = warehouseLogService.getWarehouseLogById(id);
        request.setAttribute("whl", whl);
        return "/system/warehouse/warehouseLogDetail";
    }
    
    /**
     * 
    * @Title: add
    * @Description: TODO(跳转到出库页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/system/warehouse/chuWarehouseAdd")
    public String chuWarehouse(HttpServletRequest request){
        return "/system/warehouse/chuWarehouseAdd";
    }
    /**
     * load出的仓单信息
     */
    @RequestMapping(value = "/system/warehouseOrder/search")
    public String loadWareOrder(HttpServletRequest request){
        // 得到id
        String id = request.getParameter("id");
        WarehouseOrder who = warehouseOrderService.getWarehouseOrderById(id);
        if(who != null){
            request.setAttribute("who", who);
            return "/system/warehouse/loadWarehouseOrder"; 
        }
        return "/system/warehouse/loadWarehouseOrder"; 
    }
    
    /**
     * 出货操作
     */
    @RequestMapping(value = "/system/warehouse/chuWarehouseSave")
    public String chuWarehouseSave(HttpServletRequest request,HttpServletResponse response){
        //页面得到出仓单id
        String whoId = request.getParameter("id");
        // 页面上得到商品id
        String proId = request.getParameter("productId");
        // 页面上得到的仓库id
        String whId = request.getParameter("warehouseId");
        // 页面上得到的商品数量
        String number = request.getParameter("number");
        // 设置图标保存路径
        String currentPath = "/customerfiles/warehouseLog/orderId/";
        // 调用方法获取分类图标，返回图标路径
        String picPath = FileUploadHelper.uploadFile("orderId", currentPath, request, response);
        // 页面上得到的备注
        String note = request.getParameter("note");
        // 设置操作人
        Manager manager = (Manager)request.getSession().getAttribute(SystemConst.MANAGER);
        String id = manager.getId();
        WarehouseLog whl = new WarehouseLog();
        whl.setProductId(proId);
        whl.setNumber(Integer.valueOf(number));
        whl.setCreateTime(DateUtil.getSysDateTimeString());
        whl.setCreator(id);
        whl.setPic(picPath);
        whl.setNote(note);
        whl.setType(false);
        whl.setWareHouse(whId);
        //得到对应商品并设置入库仓库
        WarehouseOrder who = warehouseOrderService.getById(whoId);
        if(who != null){
            who.setStatus(true);
        }
        if(warehouseOrderService.update(who) && warehouseLogService.add(whl)){
            request.setAttribute("addOk", true);
            return "/system/warehouse/chuWarehouseAdd";
        }else{
            request.setAttribute("addOk", false);
            return "/system/warehouse/chuWarehouseAdd";
        }
    }
}
