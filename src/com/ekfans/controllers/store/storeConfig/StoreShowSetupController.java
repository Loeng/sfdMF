package com.ekfans.controllers.store.storeConfig;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreChannel;
import com.ekfans.base.store.model.StoreChannelConfig;
import com.ekfans.base.store.service.IStoreChannelConfigService;
import com.ekfans.base.store.service.IStoreChannelService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
/**
 * 
* @ClassName: StoreShowSetup
* @Description: TODO(跳转到外观设置页面)
* @author ekfans
* @date 2014-4-14 上午11:45:39
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class StoreShowSetupController extends BasicController{
	
    @Autowired
    IStoreChannelService storeChannelService;
    @Autowired
    IStoreChannelConfigService channelConfigService;
    
    /**
     * 
    * @Title: list
    * @Description: TODO(跳转到外观设置页面)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param response
    * @param session
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/store/channel/setup")
    public String list(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        StoreChannel channel = null;
        try {
            // 获取登录对象
            Store store = (Store)session.getAttribute(SystemConst.STORE);
            //如果store对象为空则返回登陆页面
            if(store == null){
                return "/store/login"; 
            }
         // 定义分页
            Pager pager = new Pager();
            // 从页面获取频道名称
            String name = request.getParameter("name");
            // 从页面获取页码
            String currentpageStr = request.getParameter("pageNum");
            // 将从页面获取的分页数据转化成int型
            int currentPage = 1;
            if (!StringUtil.isEmpty(currentpageStr)) {
                currentPage = Integer.parseInt(currentpageStr);
            }
            // 设置要查询的页码
            pager.setCurrentPage(currentPage);
            // 查询店铺首页频道
            channel = storeChannelService.getChannelById("index",store.getId());
            // 如果获取的频道为空，则返回列表页
            if (channel == null || StringUtil.isEmpty(channel.getViewPath())) {
                return "/web/store/configError";
            }
            // 获取频道配置的详细参数
            Map<String, StoreChannelConfig> map = channelConfigService.getChannelConfigsByChannelId(request, channel.getId(),store.getId());
            request.setAttribute("configMap", map);
            // 将频道ID放入页面
            request.setAttribute("channelId", channel.getId());
            // 设置状态为True
            request.setAttribute("configStatus", "true");
            List<StoreChannel> channelList = storeChannelService.getAllChannel(request);
            request.setAttribute("channelList", channelList);
            request.setAttribute("storeName", store.getStoreName());
            request.setAttribute("channel", channel);
            request.setAttribute("pager", pager);
            request.setAttribute("name", name);
        } catch (Exception e) {
            return "/web/store/configError";
        }
        return "/store/channel/storeConfig";
    }  
    /**
    * @Title: add
    * @Description: TODO(前台商铺配置点击新增跳转)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value ="/store/channel/storeadd")
    public String add(){
        return "/store/channel/storechannelAdd";
    }
    @RequestMapping(value ="/store/channel/save")
    public String storeSaveChannel(StoreChannel channel, HttpServletRequest request, HttpSession session){
        // 获取登录对象
        Store store = (Store)session.getAttribute(SystemConst.STORE);
        //如果store对象为空则返回登陆页面
        if(store == null){
            return "/store/login"; 
        }
        try {
            if (StringUtil.isEmpty(channel.getName()) || StringUtil.isEmpty(channel.getId())) {
                // 如果为空，返回添加失败
                request.setAttribute("addOk", false);
                return "store/channel/storechannelAdd";
            }
            channel.setCreateTime(DateUtil.getSysDateTimeString());
            channel.setUpdateTime(DateUtil.getSysDateTimeString());
            channel.setStoreId(store.getId());
            // 利用Service的方法添加频道
            if (storeChannelService.addChannel(channel)) {
                request.setAttribute("addOk", true);
                // 添加成功，返回
                return "redirect:/store/channel/setup";
            }
        } catch (Exception e) {
            throw new RuntimeException("添加失败");
        }
        return "error";
    }
}
