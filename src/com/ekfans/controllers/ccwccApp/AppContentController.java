package com.ekfans.controllers.ccwccApp;

import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentModel;
import com.ekfans.base.content.service.IContentModelService;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguoyu on 2017/4/12.
 */
@Controller
public class AppContentController extends BasicController {

    @Autowired
    IContentService contentService;
    @Autowired
    IContentModelService contentModelService;

    @RequestMapping(value = "/ccwcc/content/view/{contentId}")
    public String contentView(@PathVariable String contentId, HttpServletRequest request) {
        Content content = contentService.getContent(contentId);
        if (content != null) {
            content.setCreateTime(content.getCreateTime().substring(0, 11));
        }
        List<ContentModel> models = contentModelService.getContentModel2ByContentId(contentId, null);
        request.setAttribute("models", models);
        request.setAttribute("content", content);
        return "/ccwcc/content/contentView";
    }

    @RequestMapping(value = "/ccwcc/content/share/{contentId}")
    public String contentShare(@PathVariable String contentId, HttpServletRequest request) {
        Content content = contentService.getContent(contentId);
        if (content != null) {
            content.setCreateTime(content.getCreateTime().substring(0, 11));
        }
        List<ContentModel> models = contentModelService.getContentModel2ByContentId(contentId, null);
        request.setAttribute("models", models);
        request.setAttribute("content", content);
        return "/ccwcc/content/contentShare";
    }

    @ResponseBody
    @RequestMapping(value = "/ccwcc/content/list", method = RequestMethod.GET)
    public Response getContentList(HttpServletRequest request, HttpServletResponse response) {
        String pageNum = request.getParameter("pageNum");
        String rowNum = request.getParameter("rowNum");
        ResponseListBean listBean = new ResponseListBean();
        try {
            int currentPageNo = 1;
            int rowsPerpage = 15;
            if (!StringUtil.isEmpty(pageNum)) {
                try {
                    currentPageNo = Integer.parseInt(pageNum);
                } catch (Exception e) {

                }
            }
            if (!StringUtil.isEmpty(rowNum)) {
                try {
                    rowsPerpage = Integer.parseInt(rowNum);
                } catch (Exception e) {

                }
            }
            BasicRequest br = new BasicRequest(request);

            Pager pager = new Pager(currentPageNo);
            pager.setRowsPerPage(rowsPerpage);
            List<Map<String, Object>> cs = new ArrayList<>();
//            List<Content> contents = contentService.listContent(pager, "00000058", null);
            List<Content> contents = contentService.listContent(pager, null, null, null, null, "00000058");
            if (contents != null) {
                for (Content content : contents) {
                    if (content == null) {
                        continue;
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", content.getId());
                    map.put("name", content.getContentName());
                    map.put("note", content.getNavigationText());
                    map.put("time", content.getCreateTime());
                    map.put("url", br.getWebFullUrlPrex() + "/ccwcc/content/view/" + content.getId());
                    map.put("sharUrl", br.getWebFullUrlPrex() + "/ccwcc/content/share/" + content.getId());
                    List list = new ArrayList();
                    if (!StringUtil.isEmpty(content.getNavigationImage())) {
                        if (!(content.getNavigationImage().toLowerCase()).startsWith("http")) {
                            list.add(br.getWebFullUrlPrex() + content.getNavigationImage());
                        } else {
                            list.add(content.getNavigationImage());
                        }
                    }
                    if (!StringUtil.isEmpty(content.getAppPic1())) {
                        if (content.getAppPic1().toLowerCase().startsWith("http")) {
                            list.add(content.getAppPic1());
                        } else {
                            list.add(br.getWebFullUrlPrex() + content.getAppPic1());
                        }
                    }
                    if (!StringUtil.isEmpty(content.getAppPic2())) {
                        if (content.getAppPic2().toLowerCase().startsWith("http")) {
                            list.add(content.getAppPic2());
                        } else {
                            list.add(br.getWebFullUrlPrex() + content.getAppPic2());
                        }
                    }
                    if (!StringUtil.isEmpty(content.getAppPic3()) && list.size() <= 2) {
                        if (content.getAppPic3().toLowerCase().startsWith("http")) {
                            list.add(content.getAppPic3());
                        } else {
                            list.add(br.getWebFullUrlPrex() + content.getAppPic3());
                        }
                    }
                    map.put("pics", list);
                    map.put("type", content.getContentType());
                    map.put("label", content.getContentLabel());
                    cs.add(map);
                }
            }
            listBean.setCurrentPage(pager.getCurrentPage());
            listBean.setItemList(cs);
            listBean.setTotalPage(pager.getTotalPage());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(listBean);
    }
}
