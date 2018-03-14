package com.ekfans.controllers.store.storeConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
/**
 * 
* @ClassName: StoreIconSetup
* @Description: TODO(跳转到店招店标设置页面)
* @author ekfans
* @date 2014-4-14 上午11:50:38
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreIconSetupController extends BasicController{
    @Autowired
    IStoreService storeService;
    
    /**
     * 
    * @Title: list
    * @Description: TODO(跳转到设置logo页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param response
    * @param session
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/icon/setup")
    public String list(HttpServletRequest request,
            HttpSession session){
        // 获取登录对象
        Store s = (Store)session.getAttribute(SystemConst.STORE);
        //如果store对象为空则返回登陆页面
        if(s == null){
            return "/store/login"; 
        }
        Store store = storeService.getStore(s.getId());
        //得到店铺的logo信息
        String logo = store.getStoreLogo();
        request.setAttribute("logo", logo);
        return "store/config/storeIconSetup";
    }
    
    /**
     * 
    * @Title: modifyStoreLogo
    * @Description: TODO(修改店铺logo)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return boolean 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/icon/modify")
    public String modifyStoreLogo(HttpServletRequest request,HttpSession session,HttpServletResponse response){
     // 获取登录对象
        Store s = (Store)session.getAttribute(SystemConst.STORE);
        //如果store对象为空则返回登陆页面
        if(s == null){
            return "/store/login"; 
        }
        // 设置图标保存路径
        String currentPath = "/customerfiles/store/" + DateUtil.getNoSpSysDateString() + "/storeLogo/";
        // 调用方法获取分类图标，返回图标路径
        String categoryPicPath = FileUploadHelper.uploadFile("logo", currentPath, request, response);
        
        //得到完整的store对象
        Store store = storeService.getStore(s.getId());
        //设置店铺logo
        store.setStoreLogo(categoryPicPath);
        //调用sevice执行流程
        if( storeService.updateNotes(store)){
            request.setAttribute("updateOk",true);
            request.setAttribute("logo",store.getStoreLogo());
            return "store/config/storeIconSetup";
         }else{
        return "error";
         }
    }
}
