package com.ekfans.controllers.system.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.model.ShopPurview;
import com.ekfans.base.system.service.IShopPurviewService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;

/**
 * 后台系统的总Controller
 * 
 * @ClassName: SystemController
 * @author yikelizi
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@Scope("prototype")
public class SystemController extends BasicController {
	
	@Resource
	private IShopPurviewService shopPurviewService;

	/**
	 * 系统后台（首页）
	 */
	@RequestMapping(value = "/system")
	public String index() {
		return "/system/index";
	}

	/**
	 * 系统后台（头部）
	 */
	@RequestMapping(value = "/system/top")
	public String top() {
		Manager manager = (Manager) getSession().getAttribute(SystemConst.MANAGER);

		List<ShopPurview> purviewList = shopPurviewService.getTopPurviewsByManager(manager);
		
		getRequest().setAttribute("topPurviewList", purviewList);
		
		return "/system/common/top";
	}

	/**
	 * 系统后台（左侧）
	 */
	@RequestMapping(value = "/system/left/{topId}")
	public String left(@PathVariable String topId) {
		Manager manager = (Manager) getSession().getAttribute(SystemConst.MANAGER);
		
		List<ShopPurview> purviewList = shopPurviewService.getPurviewsByManager(manager, topId);
		
		getRequest().setAttribute("leftPurviewList", purviewList);
		
		return "/system/common/left";
	}

	/**
	 * 系统后台（右侧）
	 */
	@RequestMapping(value = "/system/right")
	public String right() {
		return "/system/common/right";
	}

	/**
	 * 系统后台（右侧欢迎页面）
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/welcome")
	public String welcom() {
		return "/system/common/welcome";
	}
}
