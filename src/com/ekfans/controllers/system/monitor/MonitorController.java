package com.ekfans.controllers.system.monitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.webService.monitor.util.MonitorSyncPushAll;

@Controller
@Scope("prototype")
public class MonitorController extends BasicController {

	private Logger log = LoggerFactory.getLogger(MonitorController.class);

	/**
	 * 启动/重启批量同步数据任务
	 */
	@RequestMapping(value = "/push/monitor/start")
	public void pushAll(HttpServletRequest request, HttpServletResponse response) {
		MonitorSyncPushAll.push(request);
	}

	/**
	 * 获得同步任务状态
	 */
	@RequestMapping(value = "/push/monitor/status", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String pushStatus(HttpServletRequest request, HttpServletResponse response) {
		String status = MonitorSyncPushAll.getCurrPushStatus(request).toString();
		return status;
	}

}
