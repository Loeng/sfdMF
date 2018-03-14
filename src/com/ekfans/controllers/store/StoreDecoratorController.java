package com.ekfans.controllers.store;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.basic.controller.BasicController;

/**
 * 
 * 店铺中心SitMesh跳转Controller
 * @Title: StoreDecoratorController.java
 * @Description:易科B2B2C平台
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司 
 * @date 2014-3-22 下午02:54:29 
 * @version V1.0
 */
@Controller
public class StoreDecoratorController extends BasicController {
	@RequestMapping(value = "/store/decorator/index")
	public String storeIndex(){
		return "decorator/storeIndexDecortor";
	}
}
