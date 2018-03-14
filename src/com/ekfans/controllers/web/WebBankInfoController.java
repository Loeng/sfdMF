package com.ekfans.controllers.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.service.IBankService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;

/**
 * 商品价格走势图查看
 * 
 * @ClassName: WebPriceChangeController
 * @Description: TODO
 * @author cgm
 * @date 2014-6-11 上午10:23:04
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class WebBankInfoController extends BasicController {
	@Autowired
	private IBankService bankService;

	@RequestMapping(value = "/web/zxrz/loadbanks")
	public String hostPricesJsonLoad(HttpServletRequest request, HttpSession session) {
		Pager pager = new Pager(1);
		pager.setRowsPerPage(3);
		List<Bank> banks = bankService.list(pager, null, null,"1","1",null,null,null,null);
		request.setAttribute("bankList", banks);
		return "/web/channel/zxrz/bankinfos";
	}
}
