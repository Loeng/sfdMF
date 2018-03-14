package com.ekfans.controllers.web;

import com.ekfans.base.metal.model.PreciousMetal;
import com.ekfans.base.metal.model.PreciousMetalCategory;
import com.ekfans.base.metal.service.IPreciousMetalCategoryService;
import com.ekfans.base.metal.service.IPreciousMetalDetailService;
import com.ekfans.base.metal.service.IPreciousMetalService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author HJC
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 * @ClassName: ProductConsultController
 * @Description: TODO(前台-商品咨询)
 * @date 2014-6-11 上午10:23:04
 */
@Controller
public class WebMetalController extends BasicController {
    @Autowired
    private IPreciousMetalService metalService;
    @Autowired
    private IPreciousMetalCategoryService categoryService;
    @Autowired
    private IPreciousMetalDetailService detailService;

    @RequestMapping(value = "/web/metal/loadprices")
    public String newPrices(HttpServletRequest request, HttpSession session) {
        String categoryId = Cache.getResource("index.metal.category");
        List<PreciousMetal> metals = metalService.getNewPreciousMetalByCategory(categoryId, null);
        request.setAttribute("priceMetals", metals);
        return "/web/channel/xhjy/loadPrices";
    }

    @RequestMapping(value = "/web/metal/hots")
    public String hostPrices(HttpServletRequest request, HttpSession session) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        request.setAttribute("dateStr", format.format(DateUtil.toDate(DateUtil.getSysDateString())));

        String categoryId = request.getParameter("chosedCategory");
        if (StringUtil.isEmpty(categoryId)) {
            categoryId = Cache.getResource("index.metal.category");
        }
        List<PreciousMetalCategory> categorys = categoryService.getCategorys();
        request.setAttribute("topCategorys", categorys);
        List<PreciousMetal> metals = metalService.getMetalsByCategoryId(categoryId);
        request.setAttribute("topMetals", metals);
        if (metals != null && metals.size() > 0) {
            // String endDate = DateUtil.getSysDateString();
            // String startDate =
            // DateUtil.toString(DateUtil.addDays(DateUtil.toDate(endDate),
            // -7));
            // String jsonStr = detailService.getJsonStr(metals.get(0).getId(),
            // startDate, endDate);
            // request.setAttribute("jsonStr", jsonStr);
            request.setAttribute("chosedMetal", metals.get(0).getId());
        }
        request.setAttribute("chosedCategoryId", categoryId);
        return "/web/channel/xhjy/loadHots";
    }

    @RequestMapping(value = "/web/metal/indexcategorys")
    public String indexLoadCategorys(HttpServletRequest request, HttpSession session) {
        String categoryId = request.getParameter("chosedCategory");

        List<PreciousMetalCategory> categorys = categoryService.getCategorys();
        if (StringUtil.isEmpty(categoryId)) {
			categoryId = Cache.getResource("index.metal.category");
//            categoryId = categorys.get(0).getId();
        }
        request.setAttribute("categorys", categorys);
        request.setAttribute("chosedCategoryId", categoryId);
        return "/web/channel/index/metalCategoryList";

        // Pager pager = new Pager(1);
        // pager.setRowsPerPage(6);
        // List<PreciousMetal> metals =
        // metalService.getNewPreciousMetalByCategory(categoryId, pager);
        // request.setAttribute("metals", metals);
    }

    @RequestMapping(value = "/web/metal/indexmetals/{categoryId}/{pageNo}")
    public String indexLoadMetals(@PathVariable String categoryId, @PathVariable String pageNo, HttpServletRequest request, HttpSession session) {
        if (StringUtil.isEmpty(categoryId)) {
            categoryId = Cache.getResource("index.metal.category");
        }
        int pageNum = 1;
        try {
            pageNum = Integer.parseInt(pageNo);
        } catch (Exception e) {
            // TODO: handle exception
        }

        Pager pager = new Pager(pageNum);
        pager.setRowsPerPage(6);
        List<PreciousMetal> metals = metalService.getNewPreciousMetalByCategory(categoryId, pager);
        request.setAttribute("metals", metals);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("pager", pager);
        return "/web/channel/index/metalList";
    }

    @RequestMapping(value = "/web/metal/hots/metalload")
    @ResponseBody
    public void hostPricesMetalLoad(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        String categoryId = request.getParameter("chosedCategory");
        if (StringUtil.isEmpty(categoryId)) {
            categoryId = Cache.getResource("index.metal.category");
        }
        List<PreciousMetal> metals = metalService.getMetalsByCategoryId(categoryId);
        List<Map<String, String>> jsonList = new LinkedList<Map<String, String>>();
        for (PreciousMetal metal : metals) {
            if (metal == null) {
                continue;
            }
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put("id", metal.getId());
            jsonMap.put("name", metal.getName() + "-" + metal.getSpec());
            jsonList.add(jsonMap);
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/json;charset=UTF-8");
        System.out.println(JsonUtil.listToJson(jsonList));
        backWriteStr(response, JsonUtil.listToJson(jsonList));
    }

    @RequestMapping(value = "/web/metal/hots/jsonload")
    @ResponseBody
    public String hostPricesJsonLoad(HttpServletRequest request, HttpSession session) {
        String metalId = request.getParameter("metalId");
        PreciousMetal metal = metalService.getMetalById(metalId);
//		String endDate = DateUtil.getSysDateString();
//		String startDate = DateUtil.toString(DateUtil.addDays(DateUtil.toDate(endDate), -7));
        String jsonStr = detailService.getJsonStr(metal.getId());
        return jsonStr;
    }

    /**
     * 获取金属涨幅排名
     *
     * @param request
     * @param pageNum
     * @return
     */
    @RequestMapping("/web/metal/getRunUps")
    public String getRunUps(HttpServletRequest request, int num) {
        // 如果查询不到数据，原因很有可能是录入数据时，没有录入riseAndDrop跌涨价格
        List<PreciousMetal> reListz = metalService.runUps(num, true); // 涨
        List<PreciousMetal> reListd = metalService.runUps(num, false); // 跌
        if (reListz != null) {
            reListz.addAll(reListd);
        }
        request.setAttribute("reList", reListz);
        return "/web/channel/xhjy/runUp";
    }
}
