package com.ekfans.controllers.ccwccApp;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
public class AppSupplyBuyController extends BasicController {

    @Autowired
    ISupplyBuyService supplyBuyService;

    @ResponseBody
    @RequestMapping(value = "/ccwcc/supply/list", method = RequestMethod.GET)
    public Response getSulllyBuyList(HttpServletRequest request, HttpServletResponse response) {
        String pageNum = request.getParameter("pageNum");
        String rowNum = request.getParameter("rowNum");
        String type = request.getParameter("type");

        String categoryId = request.getParameter("categoryId");
        String productType = request.getParameter("productType");
        String destination = request.getParameter("destination");


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
            if (StringUtil.isEmpty(type)) {
                type = "0";
            }
            Pager pager = new Pager(currentPageNo);
            pager.setRowsPerPage(rowsPerpage);
            List<SupplyBuy> supplyBuys = supplyBuyService.appSupplyShow(pager, type, productType, destination, categoryId, request);
            listBean.setCurrentPage(pager.getCurrentPage());
            listBean.setItemList(supplyBuys);
            listBean.setTotalPage(pager.getTotalPage());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(listBean);
    }

    @ResponseBody
    @RequestMapping(value = "/ccwcc/supply/categorys", method = RequestMethod.GET)
    public Response getSulllyBuyCategoryList(HttpServletRequest request, HttpServletResponse response) {
        String productType = request.getParameter("productType");

        ResponseListBean listBean = new ResponseListBean();
        try {
            IProductCategoryService productCategoryService = SpringContextHolder.getBean(IProductCategoryService.class);
            List<ProductCategory> list = productCategoryService.getCcwccCategorys(productType);
            List<Map<String, String>> rList = new ArrayList<>();
            if (list != null) {
                for (ProductCategory category : list) {
                    if (category == null) {
                        continue;
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("id", category.getId());
                    map.put("name", category.getName());
                    rList.add(map);
                }
            }

            listBean.setItemList(rList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(listBean);
    }

    @ResponseBody
    @RequestMapping(value = "/ccwcc/supply/view", method = RequestMethod.GET)
    public Response getSulllyBuy(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        SupplyBuy supplyBuy = null;
        try {
            supplyBuy = supplyBuyService.getSupplyBuyById(id);
            if (supplyBuy != null) {
                BasicRequest br = new BasicRequest(request);
                supplyBuy.setShareUrl(br.getWebFullUrlPrex() + "/ccwcc/supply/share/" + id);
                IProductCategoryService productCategoryService = SpringContextHolder.getBean(IProductCategoryService.class);
                ProductCategory pc = productCategoryService.getCategoryById(supplyBuy.getCategoryId());
                if (pc != null) {
                    supplyBuy.setCategoryName(pc.getName());
                }
                if(StringUtil.isEmpty(supplyBuy.getAddress()) && !StringUtil.isEmpty(supplyBuy.getAreaId())){
                    ISystemAreaService systemAreaService = SpringContextHolder.getBean(ISystemAreaService.class);
                    supplyBuy.setAddress(systemAreaService.getAreaFullNameByAreaIdAndDelimiter(supplyBuy.getAreaId(),""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(supplyBuy);
    }

    @RequestMapping(value = "/ccwcc/supply/share/{supplyId}")
    public String contentView(@PathVariable String supplyId, HttpServletRequest request) {
        SupplyBuy supplyBuy = supplyBuyService.getSupplyBuyById(supplyId);
        if (supplyBuy != null) {
            IProductCategoryService categoryService = SpringContextHolder.getBean(IProductCategoryService.class);
            ProductCategory category = categoryService.getCategoryById(supplyBuy.getCategoryId());
            if (category != null) {
                request.setAttribute("categoryName", category.getName());
            }

            String createTime = supplyBuy.getCreateTime();
            String endTime = supplyBuy.getEndTime();
            if (!StringUtil.isEmpty(endTime)) {
                request.setAttribute("endTime", endTime.substring(0, 11));
            }
            if (!StringUtil.isEmpty(createTime) && createTime.indexOf("-") != -1) {
                createTime = createTime.substring(createTime.indexOf("-") + 1);
                createTime = createTime.replace("-", "/");
                createTime = createTime.substring(0,createTime.length()-3);
            }
            supplyBuy.setCreateTime(createTime);
            if(StringUtil.isEmpty(supplyBuy.getAddress()) && !StringUtil.isEmpty(supplyBuy.getAreaId())){
                ISystemAreaService systemAreaService = SpringContextHolder.getBean(ISystemAreaService.class);
                supplyBuy.setAddress(systemAreaService.getAreaFullNameByAreaIdAndDelimiter(supplyBuy.getAreaId(),""));
            }
        }
        request.setAttribute("supplyBuy", supplyBuy);
        return "/ccwcc/supply/supplyShare";
    }

}