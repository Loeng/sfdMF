package com.ekfans.controllers.system.finance;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.model.MortgageApplication;
import com.ekfans.base.store.service.IMortgageApplicationService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 后台（贷款申请Controller）
 * 
 * @ClassName: SystemMortgageApplicationController
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@Scope("prototype")
public class SystemMortgageApplicationController {

	private Logger log = LoggerFactory.getLogger(SystemMortgageApplicationController.class);
	@Resource
	private IMortgageApplicationService mortgageApplicationService;
	
	/**
	 * 查询金融申请列表
	 * 
	 * @param type 会员类型（0：个人会员，1：供应商，2：采购商，3：处置企业，4：运输企业）
	 * @param matype 1：订单借贷，2：信用借贷，3：合同借贷，4：危险废品订单借贷
	 */
	@RequestMapping(value = "/system/finance/jumpMAList/{type}/{matype}")
	public String jumpMAList(@PathVariable String type, @PathVariable int matype, HttpServletRequest request){
		String pageNum = request.getParameter("pageNum"); // 页码
		String orderId = request.getParameter("orderId"); // 订单号
		String storeName = request.getParameter("storeName"); // 企业名称
		//String contractNo = request.getParameter("contractNo"); // 企业名称
		String status = request.getParameter("status");
		
		// 分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(pageNum)) {
			try {
				currentPage = Integer.parseInt(pageNum);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(10);
		
		List<MortgageApplication> malist = this.mortgageApplicationService.getSysMaList(pager, null, matype, orderId, storeName, status);
		
		// 绑定页面显示需要的数据
		request.setAttribute("malist", malist);
		request.setAttribute("pager", pager);
		request.setAttribute("orderId", orderId);
		request.setAttribute("storeName", storeName);
		
		if(matype == 1){
			return "/system/finance/manage/order_list";
		}else if(matype == 2){
			return "/system/finance/manage/xy_list";
		}else if(matype == 3){
			return "/system/finance/manage/qy_list";
		}
		return "/system/finance/manage/wfp_list";
	}
}
