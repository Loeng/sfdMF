package com.ekfans.controllers.user.collect;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.StoreCollect;
import com.ekfans.base.store.service.IStoreCollectService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * 收藏店铺Controller
 * 
 * @Title: UserPasswordController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author liulin
 * @date 2014-4-1 上午17:23:47 
 * @version V1.0
 */
@Controller
public class UserStoreCollectController extends BasicController{
 // 定义Service
    @Autowired
    private IStoreCollectService storeCollectService;
    
    @RequestMapping(value="/user/collect/storeCollect")
    public String list(HttpSession session,HttpServletRequest request){
        // 定义分页
        Pager pager = new Pager();
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
        
        // 获取登陆用户的信息,完整的对象
        User user = (User)session.getAttribute(SystemConst.USER);
        if(user == null) {
            return "redirect:/web/login";
        }
        String userId = user.getId();
        //得到对应的收藏店铺
        List<StoreCollect> scs = storeCollectService.getList(pager, userId);
        request.setAttribute("scs", scs);
        request.setAttribute("pager", pager);
        return "/userCenter/customer/collect/storeCollect";
    }
    
    /**
     * 删除店铺收藏
     * 
     * @return
     */
    @RequestMapping(value = "/user/storeCollect/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable String id) {
        // 利用Service的方法根据id删除商品s
        storeCollectService.delete(id);
        // 删除成功，返回true
        return true;
    }
    
    /**
     * 
    * @Title: loadStoreCollect
    * @Description: TODO(load出的店铺收藏信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/user/storeCollect/load")
    public String loadStoreCollect(HttpSession session,HttpServletRequest request){
        // 定义分页
        Pager pager = new Pager();
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
        
        // 获取登陆用户的信息,完整的对象
        User user = (User)session.getAttribute(SystemConst.USER);
        if(user == null) {
            return "redirect:/web/login";
        }
        String userId = user.getId();
        //得到对应的收藏店铺
        List<StoreCollect> scs = storeCollectService.getList(pager, userId);
        request.setAttribute("scs", scs);
        request.setAttribute("pager", pager);
        return "/userCenter/customer/collect/storeCollectLoad";
    }
}
