package com.ekfans.controllers.ccwccApp;

import com.ekfans.base.metal.model.PreciousMetalCategory;
import com.ekfans.base.metal.service.IPreciousMetalCategoryService;
import com.ekfans.base.metal.service.IPreciousMetalDetailService;
import com.ekfans.base.metal.service.IPreciousMetalService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by liuguoyu on 2017/4/12.
 */
@Controller
public class AppPreciousController extends BasicController {

    @Autowired
    IPreciousMetalCategoryService categoryService;
    @Autowired
    IPreciousMetalService metalService;
    @Autowired
    IPreciousMetalDetailService detailService;

    @ResponseBody
    @RequestMapping(value = "/ccwcc/precious/categorys", method = RequestMethod.GET)
    public Response getCategoryList(HttpServletRequest request, HttpServletResponse response) {
        ResponseListBean listBean = new ResponseListBean();
        try {
            List<PreciousMetalCategory> categorys = categoryService.getCategorys();
            listBean.setItemList(categorys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(listBean);
    }

    @ResponseBody
    @RequestMapping(value = "/ccwcc/precious/metals", method = RequestMethod.GET)
    public Response getMetalsByCategory(HttpServletRequest request, HttpServletResponse response) {
        String categoryId = request.getParameter("categoryId");
        ResponseListBean listBean = new ResponseListBean();
        try {
            List<AppPreciousBean> metals = metalService.getNewPreciousMetalByCategoryWithApp(categoryId, null);
            listBean.setItemList(metals);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(listBean);
    }

    @ResponseBody
    @RequestMapping(value = "/ccwcc/precious/quotation", method = RequestMethod.GET)
    public Response getMetalPrices(HttpServletRequest request, HttpServletResponse response) {
        String metalId = request.getParameter("metalId");
        String dateNum = request.getParameter("dateNum");
        ResponseListBean listBean = new ResponseListBean();
        try {
            int dateNo = 0;
            if (!StringUtil.isEmpty(dateNum)) {
                try {
                    dateNo = Integer.parseInt(dateNum);
                } catch (Exception e) {

                }
            }

            List<AppPreciousDetailBean> metals = detailService.getPreciousDetail(metalId, dateNo);
            listBean.setItemList(metals);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(listBean);
    }

}
