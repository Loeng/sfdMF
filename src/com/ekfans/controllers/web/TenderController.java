package com.ekfans.controllers.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.tender.model.TenderLog;
import com.ekfans.base.tender.service.ITenderLogService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;

/**
 * 注册Controller
 * 
 * @ClassName: TenderController
 * @author 成都易科远见科技有限公司
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class TenderController extends BasicController {

	@Resource
	private IUserService userService;
	@Resource
	private IStoreService storeService;
	@Resource
	private ITenderLogService logService;

	/**
	 * 检查用户是否重复报名
	 * 
	 * @return true：重复报名，false：没有
	 */
	@RequestMapping(value = "/web/checktenderlog/{storeId}/{tenderId}")
	@ResponseBody
	public Boolean checkHasLog(@PathVariable String storeId, @PathVariable String tenderId) {
		Boolean status = logService.checkHasLog(storeId, tenderId);
		return status;
	}

	/**
	 * 用户报名投标
	 * 
	 * @return true:报名成功， false:报名失败
	 */
	@RequestMapping(value = "/web/tenderlog/add")
	@ResponseBody
	public Boolean addTenderLog(TenderLog tenderLog) {
		tenderLog.setCreateTime(DateUtil.getSysDateTimeString());
		Boolean status = logService.addTenderLog(tenderLog);
		return status;
	}
}
