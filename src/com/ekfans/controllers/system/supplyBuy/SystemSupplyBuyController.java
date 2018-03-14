package com.ekfans.controllers.system.supplyBuy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.store.model.Intel;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IIntelService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.plugin.cache.base.Cache;

@Controller
public class SystemSupplyBuyController extends BasicController {
    @Autowired
    private ISupplyBuyService supplyBuyService;

    @Autowired
    private IStoreService storeService;
    
    @Autowired
    private IProductCategoryService categoryService;
    
    @Resource
    private IIntelService intelService;
    /**
     * 跳转到源流需求添加页面
     */
    @RequestMapping (value="/system/supplyBuy/Add/{productType}/{type}")
    public String safeAdd(@PathVariable String productType, @PathVariable String type, HttpServletRequest request,HttpSession session){ 
    	//根据商品类型查询分类(0成品 1原料 2危废品)
    	List<ProductCategory> categories =  categoryService.getCategoriesByType(productType);
    	request.setAttribute("categories", categories);
        request.setAttribute("productType",productType);
        request.setAttribute("type",type);
        return "/system/supplyBuy/supplyBuyAdd";
    }
    
    /**
     * 新增
     * @return
     */
    @RequestMapping(value = "/system/supplyBuy/save")
    public String save(@ModelAttribute SupplyBuy sb,HttpServletRequest request) {
    		String storeName = sb.getStoreName().split(",")[0];
    		//拼接交货地
	        String[] destinationStrs = null;
	        String destination = "";
	        if(!StringUtil.isEmpty(sb.getDestination())){
	        	destinationStrs = sb.getDestination().split(",");
	        }
	        if(destinationStrs!=null){
	        	destination += destinationStrs[0];
	        	if(!"县".contains(destinationStrs[1])){
	        		destination += "" +destinationStrs[1];
	        	}
	        }
	        sb.setDestination(destination);
	        sb.setStoreName(storeName);
	        //拼接资质ID
            String[] intelId = getRequest().getParameterValues("intelId"); // 资质ID集合
            String intelligenceIds = "";
            if(intelId != null && intelId.length > 0){
                for (int i = 0;i<intelId.length;i++) {
                    if(i==0){
                        intelligenceIds = intelligenceIds + intelId[i]; 
                    }else{
                        intelligenceIds = intelligenceIds + "-" + intelId[i];
                    }
                }
            }
            sb.setIntelligenceIds(intelligenceIds);
            //设置状态正常
            sb.setStatus("1");
            //设置创建时间
            sb.setCreateTime(DateUtil.getSysDateTimeString());
            sb.setCheckStatus(1);
            // 利用Service的方法添加等级
            if (supplyBuyService.add(sb)) {
                request.setAttribute("productType", sb.getProductType());
                request.setAttribute("addOk", true);
                request.setAttribute("type", sb.getType());
                // 添加成功，返回
                return "/system/supplyBuy/supplyBuyAdd";
            }else{
                // 添加失败，返回false
                request.setAttribute("addOk", false);
                request.setAttribute("productType", sb.getProductType());
                request.setAttribute("type", sb.getType());
                return "/system/supplyBuy/supplyBuyAdd";
            }
            
    }
    
    /**
     * 供需列表
     */
    @RequestMapping (value="/system/supplyBuy/list/{productType}/{type}")
    public String supplyList(@PathVariable String productType, @PathVariable String type, HttpServletRequest request,HttpSession session){ 
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
                currentPage = Integer.parseInt(currentpageStr);
           }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        //页面上传来的参数
        //标题
        String title = request.getParameter("title");
        //创建时间
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        //状态
        String status = request.getParameter("status");
        List<SupplyBuy> supplyBuys = supplyBuyService.listSupplyBuy(pager, title, beginDate, endDate, null, type, status, productType,null,null,null);
        //绑定会页面
        request.setAttribute("supplyBuys",supplyBuys);
        request.setAttribute("title",title);
        request.setAttribute("beginDate",beginDate);
        request.setAttribute("endDate",endDate);
        request.setAttribute("status",status);
        request.setAttribute("pager",pager);
        request.setAttribute("productType",productType);
        request.setAttribute("type",type);
        request.setAttribute("currentpageStr",currentPage);
        return "/system/supplyBuy/supplyBuyList";
    } 
    
    /**
     * 关闭供需操作
     * 
     * @param req
     * @return
     */
    @RequestMapping(value = "/system/supplyBuy/delete", method = RequestMethod.POST)
    @ResponseBody
    public Boolean deleteStore(HttpServletRequest req) {
        String ids = req.getParameter("ids");
        String[] idList = ids.split(",");
        //得到对应信息
        for(int i = 0;i < idList.length;i++){
            SupplyBuy sb = supplyBuyService.getSupplyBuyById(idList[i]);
            sb.setStatus("0");
            supplyBuyService.update(sb);
        }
        return true;
       
    }
    
    /**
     * 详情源流需求信息
     */
    @RequestMapping (value="/system/supplyBuy/query/{id}/{type}")
    public String supplyQuery(@PathVariable String id,@PathVariable String type,HttpServletRequest request,HttpSession session){ 
        SupplyBuy sb = supplyBuyService.getSupplyBuyById(id);
        request.setAttribute("sb",sb); 
        if(!StringUtil.isEmpty(sb.getStoreId())){
        	Store s = storeService.getStore(sb.getStoreId());
        	sb.setStoreName(s.getStoreName());
        }
//        request.setAttribute("s",s);
        //得到所有的资质集合
        String intelligenceIds = sb.getIntelligenceIds();
        //返回页面的纸质集合
        List<Intel> ilist = new ArrayList<Intel>();
        if(!StringUtil.isEmpty(intelligenceIds)){
            String[] intelligenceIdsList = intelligenceIds.split("-");
            for(int i = 0;i<intelligenceIdsList.length;i++){
                ilist.add(intelService.getIntelById(intelligenceIdsList[i]));
            }
        }
        List<ProductCategory> categories =  categoryService.getCategoriesByType(sb.getProductType());
        request.setAttribute("ilist", ilist);
        request.setAttribute("categories", categories);
        if("1".equals(type)){
            return "/system/supplyBuy/supplyBuyUpdate";
        }else if("0".equals(type)){
            return "/system/supplyBuy/supplyBuyQuery";
        } else if("2".equals(type)){
            return "/system/supplyBuy/supplyBuyCheckQuery";  
        }else if("3".equals(type)){
            return "/system/supplyBuy/supplyBuyCheck";  
        }
        return "error";
        
    }
    
    /**
     * 修改供需
     */
    @RequestMapping(value = "/system/supplyBuy/update")
    public String supplyBuyUpdate(@ModelAttribute SupplyBuy sb,HttpServletRequest request){
    	List<ProductCategory> categories =  categoryService.getCategoriesByType(sb.getProductType());
        String[] intelId = getRequest().getParameterValues("intelId"); // 资质ID集合
        String intelligenceIds = "";
        if(intelId != null && intelId.length > 0){
            for (int i = 0;i<intelId.length;i++) {
                if(i==0){
                    intelligenceIds = intelligenceIds + intelId[i]; 
                }else{
                    intelligenceIds = intelligenceIds + "-" + intelId[i];
                }
            }
        }
        //拼接交货地
        String[] destinationStrs = null;
        String destination = "";
        if(!StringUtil.isEmpty(sb.getDestination())){
        	destinationStrs = sb.getDestination().split(",");
        }
        if(destinationStrs!=null){
        	destination += destinationStrs[0];
        	if(!"县".contains(destinationStrs[1])){
        		destination += "" +destinationStrs[1];
        	}
        }
        sb.setDestination(destination);
        sb.setIntelligenceIds(intelligenceIds);
        sb.setCheckStatus(0);
        sb.setUpdateTime(DateUtil.getSysDateTimeString());
        if(supplyBuyService.update(sb)){
        	if(!StringUtil.isEmpty(sb.getStoreId())){
        		Store s = storeService.getStore(sb.getStoreId());
        		sb.setStoreName(s.getStoreName());
        	}
            request.setAttribute("categories",categories);
            request.setAttribute("sb",sb);
//            request.setAttribute("s",s);
            request.setAttribute("addOk", true);
            return "/system/supplyBuy/supplyBuyUpdate";
        }else{
        	if(!StringUtil.isEmpty(sb.getStoreId())){
        		Store s = storeService.getStore(sb.getStoreId());
        		sb.setStoreName(s.getStoreName());
        	}
            request.setAttribute("sb",sb);
//            request.setAttribute("s",s);
            request.setAttribute("addOk", false);
            return "/system/supplyBuy/supplyBuyUpdate";
        }
      
    }
    
    /**
     * 审核供需
     */
    @RequestMapping(value = "/system/supplyBuy/check")
    public String supplyBuyCheck(HttpSession session,HttpServletRequest request){
        String id = request.getParameter("id");
        String checkStatus = request.getParameter("checkStatus");
        String checkNote = request.getParameter("checkNote");
        SupplyBuy sb = supplyBuyService.getSupplyBuyById(id);
        List<ProductCategory> categories =  categoryService.getCategoriesByType(sb.getProductType());
        if(!StringUtil.isEmpty(sb.getStoreId())){
    		Store s = storeService.getStore(sb.getStoreId());
    		sb.setStoreName(s.getStoreName());
    	}
        // 从session中获取管理员对象
        Manager manager = (Manager) session.getAttribute(SystemConst.MANAGER);
        // 设置审核人
        sb.setCheckMan(manager.getRealName());
        sb.setCheckStatus(Integer.valueOf(checkStatus));
        sb.setCheckNote(checkNote);
        sb.setCheckTime(DateUtil.getSysDateTimeString());
        
        if(supplyBuyService.update(sb)){
            request.setAttribute("sb",sb);
            request.setAttribute("categories",categories);
            request.setAttribute("addOk", true);

            // 审核通过更新缓存
            Cache.refrefshIndexSupplyBuyInfo(sb.getType(), sb.getProductType());
            return "/system/supplyBuy/supplyBuyCheck";
        }else{
            request.setAttribute("sb",sb);
            request.setAttribute("categories",categories);
            request.setAttribute("addOk", false);
            return "/system/supplyBuy/supplyBuyCheck";
        }
      
    }
    /**
     * 供需审核列表
     */
    @RequestMapping (value="/system/supplyBuy/checkList/{productType}/{type}/{status}")
    public String supplyCheckList(@PathVariable String productType, @PathVariable String type,  @PathVariable String status, HttpServletRequest request,HttpSession session){ 
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
                currentPage = Integer.parseInt(currentpageStr);
           }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        //页面上传来的参数
        //标题
        String title = request.getParameter("title");
        //创建时间
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        //状态
        List<SupplyBuy> supplyBuys = supplyBuyService.listSupplyBuy(pager, title, beginDate, endDate, null, type, status, productType,"1","0",null);
        //绑定会页面
        request.setAttribute("supplyBuys",supplyBuys);
        request.setAttribute("title",title);
        request.setAttribute("beginDate",beginDate);
        request.setAttribute("endDate",endDate);
        request.setAttribute("status",status);
        request.setAttribute("pager",pager);
        request.setAttribute("productType",productType);
        request.setAttribute("type",type);
        request.setAttribute("currentpageStr",currentPage);
        return "/system/supplyBuy/supplyBuyCheckList";
    } 
   
}
