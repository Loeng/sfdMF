package com.ekfans.controllers.system.metal;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.metal.model.PreciousMetal;
import com.ekfans.base.metal.model.PreciousMetalCategory;
import com.ekfans.base.metal.model.PreciousMetalDetail;
import com.ekfans.base.metal.service.IPreciousMetalCategoryService;
import com.ekfans.base.metal.service.IPreciousMetalDetailService;
import com.ekfans.base.metal.service.IPreciousMetalService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.datacrawling.SmmDataUtil;
import com.ekfans.pub.util.datacrawling.SmmModel;

@Controller
@Scope("prototype")
public class SystemMetalDetailController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SystemMetalDetailController.class);
	@Resource
	private IPreciousMetalService metalService;
	@Resource
	private IPreciousMetalDetailService detailService;
	@Resource
	private IPreciousMetalCategoryService categoryService;

	/**
	 * 跳转到品目详情列表
	 */
	@RequestMapping(value = "/system/metal/list")
	public String detailList(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<PreciousMetalCategory> categorys = categoryService.getCategorys();
			request.setAttribute("categorys", categorys);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "/system/metal/metalDetailList";
	}

	/**
	 * LOAD品目详情加载界面
	 */
	@RequestMapping(value = "/system/metal/showdetails/{categoryId}/{dateTime}")
	public String loadDetailList(@PathVariable String categoryId, @PathVariable String dateTime, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (StringUtil.isEmpty(dateTime)) {
				dateTime = DateUtil.getSysDateString();
			}
			List<PreciousMetal> metals = metalService.getMetalsByCategoryId(categoryId);
			List<PreciousMetal> rList = new LinkedList<PreciousMetal>();
			if (metals != null && metals.size() > 0) {
				Map<String, PreciousMetalDetail> detailMap = detailService.getDetailsByDateAndCategory(categoryId, dateTime);
				for (PreciousMetal metal : metals) {
					metal.setDetail(detailMap != null ? detailMap.get(metal.getId()) : new PreciousMetalDetail());
					rList.add(metal);
				}
			}
			List<SmmModel> list =  SmmDataUtil.getListSmmModel(categoryId);
			request.setAttribute("smmModelList", list);
			request.setAttribute("chosedDate", dateTime);
			request.setAttribute("metals", rList);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "/system/metal/metalDetails";
	}

	/**
	 * 批量保存
	 */
	@RequestMapping(value = "/system/metal/detail/savebatch")
	@ResponseBody
	public String saveDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] metalIds = request.getParameterValues("metalId");
			boolean status = detailService.saveDetails(metalIds, request);
			if (status) {
				return "1";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "0";
	}

	/**
	 * 单个保存
	 */
	@RequestMapping(value = "/system/metal/detail/save")
	@ResponseBody
	public String saveDetail(HttpServletRequest request, HttpServletResponse response) {
		try {
			String metalId = request.getParameter("metalId");
			String priceType = request.getParameter("priceType");
			String price = request.getParameter("price");
			String startPrice = request.getParameter("startPrice");
			String endPrice = request.getParameter("endPrice");
			String riseAndDrop = request.getParameter("riseAndDrop");
			String dateTime = request.getParameter("dateTime");
			String smmId = request.getParameter("smmId");
            
			PreciousMetalDetail detail = new PreciousMetalDetail();
			detail.setCreateTime(DateUtil.getSysDateTimeString());
			detail.setDateTime(dateTime);
			detail.setMetalId(metalId);
			detail.setSmmId(smmId);
			if ("0".equals(priceType)) {
				detail.setPriceType(false);
				detail.setPrice(new BigDecimal(price));
				detail.setStartPrice(detail.getPrice());
				detail.setEndPrice(detail.getPrice());
			} else {
				detail.setPriceType(true);
				detail.setStartPrice(new BigDecimal(startPrice));
				detail.setEndPrice(new BigDecimal(endPrice));
				detail.setPrice((detail.getStartPrice().add(detail.getEndPrice())).divide(new BigDecimal(2)));
			}
			try {
				detail.setRiseAndDrop(Double.parseDouble(riseAndDrop));
			} catch (Exception e) {
				// TODO: handle exception
			}
			PreciousMetal metal = metalService.getMetalById(metalId);
			metal.setSmmId(smmId);
			metalService.saveOrUpdate(metal);
			Boolean status = detailService.saveOrUpdate(detail);
			if (status) {
				return "1";
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "0";
	}
}
