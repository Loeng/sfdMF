package com.ekfans.controllers.ccwccApp;

import com.ekfans.base.ccwcc.model.CcwccPush;
import com.ekfans.base.ccwcc.service.ICcwccPushService;
import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by liuguoyu on 2017/4/19.
 */
@Controller
public class SystemCcwccPushController extends BasicController {
    @Autowired
    ICcwccPushService pushService;
    @Autowired
    IContentService contentService;
    @Autowired
    ISupplyBuyService supplyBuyService;

    @RequestMapping(value = "/system/ccwccpush/add")
    public String goAdd() {
        return "/system/ccwcc/ccwccPushAdd";
    }

    @RequestMapping(value = "/system/ccwccpush/save")
    @ResponseBody
    public void saveOrUpdate(CcwccPush ccwccPush, HttpServletRequest request, HttpServletResponse response) {
        String pushNow = request.getParameter("pushNow");
//        String pushId = request.getParameter("pushId");
//        String type = request.getParameter("type");
//        String linkUrl = request.getParameter("linkUrl");
//        String sourceId = request.getParameter("sourceId");
//        String content = request.getParameter("content");
//        String sourceName = request.getParameter("sourceName");
//
//        CcwccPush ccwccPush =  new CcwccPush();
        Boolean status = pushService.saveOrUpdate(ccwccPush, (!StringUtil.isEmpty(pushNow) && "1".equals(pushNow)) ? true : false,request);
        backWriteStr(response, status + "");
    }


    @RequestMapping(value = "/system/ccwcc/loadcontent")
    public String loadContent(HttpServletRequest request) {
        String name = request.getParameter("name");
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
                // TODO: handle exception
            }
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        List<Content> contents = contentService.listContent(pager, name, null, null, null, null);
        request.setAttribute("name", name);
        request.setAttribute("pager", pager);
        request.setAttribute("contents", contents);
        return "/system/ccwcc/contentLoad";
    }

    @RequestMapping(value = "/system/ccwcc/loadsupply/{type}")
    public String loadContent(@PathVariable String type, HttpServletRequest request) {
        String title = request.getParameter("title");
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
                // TODO: handle exception
            }
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        List<SupplyBuy> supplyBuys = supplyBuyService.webListSupplyTable(pager, type, title);
        request.setAttribute("title", title);
        request.setAttribute("pager", pager);
        request.setAttribute("type", type);
        request.setAttribute("supplyBuys", supplyBuys);
        return "/system/ccwcc/supplyLoad";
    }


    @RequestMapping(value = "/system/ccwccpush/list")
    public String list(HttpServletRequest request) {
        String currentPageNo = request.getParameter("pageNum");
        String content = request.getParameter("content");
        String type = request.getParameter("type");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String pushStartTime = request.getParameter("pushStartTime");
        String pushEndTime = request.getParameter("pushEndTime");
        String status = request.getParameter("status");

        Pager pager = new Pager();
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentPageNo)) {
            try {
                currentPage = Integer.parseInt(currentPageNo);
            } catch (Exception e) {
            }
        }

        List<CcwccPush> pushList = pushService.getCcwccPushList(pager, content, status, type, startTime, endTime, pushStartTime, pushEndTime);
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        request.setAttribute("content", content);
        request.setAttribute("type", type);
        request.setAttribute("startTime", startTime);
        request.setAttribute("endTime", endTime);
        request.setAttribute("pushStartTime", pushStartTime);
        request.setAttribute("pushEndTime", pushEndTime);
        request.setAttribute("pager", pager);
        request.setAttribute("pushList", pushList);
        request.setAttribute("status", status);
        return "/system/ccwcc/ccwccPushList";
    }

    @RequestMapping(value = "/system/ccwccpush/delete")
    @ResponseBody
    public Boolean remove(HttpServletRequest request) {
        String ids = request.getParameter("ids");
        if (StringUtil.isEmpty(ids)) {
            return false;
        }
        try {
            String[] idArray = ids.split(";");
            return pushService.remove(idArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequestMapping(value = "/system/ccwccpush/edit/{id}")
    public String goEdit(@PathVariable String id, HttpServletRequest request) {
        CcwccPush push = pushService.getById(id);
        request.setAttribute("ccwccPush", push);
        return "/system/ccwcc/ccwccPushEdit";
    }

}
