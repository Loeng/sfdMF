package com.ekfans.controllers.store.count;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreInventoryWarningService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: StoreInventoryWarningController
* @Description: 跟新商店　的预警信息
* @author qxh
* @date 2014-4-27 下午9:41:27
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreInventoryWarningController {
    
    @Resource
    private IStoreInventoryWarningService storeInventoryWarningService;
    @Resource
    private IStoreService storeService;
    /**
     * 
    * @Title: 去往库存预警页面
    * @Description: TODO 去往库存预警页面
    * @param @param request 
    * @param @param session 
    * @param @return    设定文件
    * @return ModelAndView    返回类型
    * @throws
     */
    @RequestMapping(value="/store/count/inventoryWarning")
    public String appraisal(HttpServletRequest request) {
        
        
        HttpSession session=request.getSession();
        
        Store store = (Store) session.getAttribute(SystemConst.STORE);
        //获取store的预警信息
        store= storeInventoryWarningService.getInventoryWarningInfo(store.getId());
        
        request.setAttribute("store", store);
        
        return "/store/count/inventoryWarning";
    }
    
    
    @RequestMapping(value="/store/count/inventoryWarning/updateWarning")
    public String updateWarning(HttpServletRequest request){
        //预警电话
        String warnMobile=request.getParameter("warningMobile");
        //预警邮箱
        String warnEmail=request.getParameter("warningEmail");
        
        HttpSession session=request.getSession();
        Store s = (Store) session.getAttribute(SystemConst.STORE);
        Store store = storeService.getStore(s.getId());
        
       
        if(StringUtil.isEmpty(warnEmail)&&StringUtil.isEmpty(warnMobile)){
            request.setAttribute("store", store);
            request.setAttribute("returnType", "failure");
            return "/store/count/inventoryWarning";
        }
        else if(StringUtil.isEmpty(warnEmail)&&warnMobile!=null){
            store=storeInventoryWarningService.updateInventoryWarning(store.getId(), "", warnMobile);
            request.setAttribute("store", store);
            request.setAttribute("returnType", "success");
            
            return "/store/count/inventoryWarning";
        }
        else if(StringUtil.isEmpty(warnMobile)&&warnEmail!=null){
            store=storeInventoryWarningService.updateInventoryWarning(store.getId(), warnEmail, "");
            request.setAttribute("store", store);
            request.setAttribute("returnType", "success");
            
            return "/store/count/inventoryWarning";
        }else{
        store=storeInventoryWarningService.updateInventoryWarning(store.getId(), warnEmail, warnMobile);
        
        request.setAttribute("store", store);
        request.setAttribute("returnType", "success");
        
        return "/store/count/inventoryWarning";
        }
    }

}
