package com.ekfans.base.store.model;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.store.service.ICreditEstimatesService;

/**
 * 信用测算信息Controller
 * 
 * @ClassName: CreditEstimatesController
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@Scope("prototype")
public class CreditEstimatesController {

	@Resource
	private ICreditEstimatesService creditEstimatesService;
	
	/**
	 * 跳转到信用测算页面
	 */
	@RequestMapping(value = "/store/creditEstimates/jumpToCreditEstimatesPage")
	public String jumpToCreditEstimatesPage(){
		
		return "/userCenter/store/authenticate/companyEvaluate";
	}
}
