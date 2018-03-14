package com.ekfans.controllers.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
 * @ClassName: hjcProductListController
 * @Description: TODO(前台-搜索供需)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-19 下午02:32:49
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class SuppLyBuyListController {

	@Autowired
	private ISupplyBuyService supplyBuyService;
	@Autowired
    private IStoreService storeService;


	/**
	 * load供求供应关系
	 */
	@RequestMapping(value = "/web/gySupplyBuy/list")
	public String gySupplyBuyList(HttpServletRequest request) {
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
		pager.setRowsPerPage(10);
		// 得到所有的商品
		List<SupplyBuy> sbList = supplyBuyService.webListSupplyBuy(pager, "1", "1", "0", "1",null);
		request.setAttribute("sbList", sbList);
		return "/web/channel/gqxx/loadGy";
	}
	/**
     * load采购供应关系
     */
    @RequestMapping(value = "/web/cgSupplyBuy/list")
    public String cgSupplyBuyList(HttpServletRequest request) {
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
        pager.setRowsPerPage(10);
        List<SupplyBuy> sbList = supplyBuyService.webListSupplyBuy(pager, "1", "1", "1", "1",null);
        request.setAttribute("sbList", sbList);
        return "/web/channel/gqxx/loadCg";
    }
	
    /**
     * load企业
     */
    @RequestMapping(value = "/web/gxStore/list")
    public String storeList(HttpServletRequest request) {
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
        pager.setRowsPerPage(5);
        List<Store> yList = storeService.getWebList(pager, "1");
        List<Store> nyList = storeService.getWebList(pager, "2");
        request.setAttribute("yList", yList);
        request.setAttribute("nyList", nyList);
        return "/web/channel/gqxx/loadStore";
    }
    
    /**
     * 跳转到更多供需信息
     */
    @RequestMapping(value = "/web/supplyBuy/gdlist")
    public String gdSupplyBuyList(HttpServletRequest request) {
        return "/web/channel/gqxx/gdSupplyBuyList";
    }
    
    /**
     * 更多的供求信息关系
     */
    @RequestMapping(value = "/web/supplyBuy/gdlistLoad")
    public String gdGySupplyBuyList(HttpServletRequest request) {
     // 定义分页
        Pager pager = new Pager();
        // 从页面获取分页数据
        String currentpageStr = request.getParameter("pageNum");
        String type = request.getParameter("type");
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
        pager.setRowsPerPage(10);
        List<SupplyBuy> sbList = supplyBuyService.webListSupplyBuy(pager, "1", "1", type, "1",null);
        request.setAttribute("sbList", sbList);
        request.setAttribute("pager", pager);
        request.setAttribute("type", type);
        return "/web/channel/gqxx/gdlistLoad";
    }
}
