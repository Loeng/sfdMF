package com.ekfans.controllers.system.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.system.model.MessageConfig;
import com.ekfans.base.system.model.MessageConfigDetail;
import com.ekfans.base.system.service.IMessageConfigDetailService;
import com.ekfans.base.system.service.IMessageConfigService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.service.ISystemConfigCacheService;
import com.ekfans.plugin.cache.service.SystemConfigCacheService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 后台邮件模板管理
 * 
 * @ClassName: MailConfigController
 * @author liuguoyu
 * @date 2014-4-22 下午02:50:03
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */

@Controller
public class MessageConfigController extends BasicController {
	// 定义Service
	@Autowired
	IMessageConfigService messageConfigService;

	// 定义信息配置模板Service
	@Autowired
	IMessageConfigDetailService messageConfigDetailService;

	/**
	 * 邮箱管理页面Action方法
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/message/config")
	public String config(HttpServletRequest request) {
		// 调用Service从数据库查询MailConfig
		MessageConfig messageConfig = messageConfigService.getMessageConfig();
		// 如果获取的为空，则创建一个新对象
		if (messageConfig == null) {
			messageConfig = new MessageConfig();
		}
		request.setAttribute("messageConfig", messageConfig);
		return "system/systemConfigs/messageConfig";
	}

	/**
	 * 更新数据库中的邮件模板对象
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/message/save")
	public String saveOrUpdate(MessageConfig config, HttpServletRequest request) {

		// 调用Service方法保存数据，返回保存状态
		boolean status = messageConfigService.saveMessageConfig(config);
		ISystemConfigCacheService cacheService = new SystemConfigCacheService();
		cacheService.refreshMessageConfig();
		// 如果返回的状态成功，则提示成功，否则提示失败
		if (status) {
			request.setAttribute("message", "success");
		} else {
			request.setAttribute("message", "fail");
		}
		request.setAttribute("messageConfig", config);

		return "system/systemConfigs/messageConfig";
	}

	/**
	 * 
	 * @Title: detail
	 * @Description: TODO(查询信息配置模板列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param configDetail
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/message/detailList")
	public String detailList(HttpServletRequest request) {
		// 定义分页
		Pager pager = new Pager();

		// 从页面获取信息配置模板名称
		String name = request.getParameter("name");
		// 从页面获取信息配置模板id
		String id = request.getParameter("id");
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		// 利用Service的方法查找信息配置模板的列表
		List<MessageConfigDetail> configDetails = messageConfigDetailService.listDetail(pager, name, id);
		request.setAttribute("configDetails", configDetails);
		request.setAttribute("pager", pager);
		request.setAttribute("name", name);
		request.setAttribute("id", id);
		return "/system/systemConfigs/messageDetailList";
	}

	/**
	 * 
	 * @Title: detail
	 * @Description: TODO(根据id查找新配配置模板) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/message/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		// 利用Service的方法根据id查找频道
		MessageConfigDetail configDetail = messageConfigDetailService.getDetailById(id);
		request.setAttribute("configDetail", configDetail);
		return "system/systemConfigs/messageDetailModify";
	}

	/**
	 * 
	 * @Title: detail
	 * @Description: TODO(根据id查找新配配置模板) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/message/query/{id}")
	public String query(@PathVariable String id, HttpServletRequest request) {
		// 利用Service的方法根据id查找频道
		MessageConfigDetail configDetail = messageConfigDetailService.getDetailById(id);
		request.setAttribute("configDetail", configDetail);
		return "system/systemConfigs/messageDetailQuery";
	}

	/**
	 * 
	 * @Title: modify
	 * @Description: TODO(修改信息配置模板) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param configDetail
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/message/modify")
	public String modify(MessageConfigDetail configDetail, HttpServletRequest request, HttpSession session) {
		// 验证模板名和排序是否为空
		String name = configDetail.getName();
		Integer index = configDetail.getIndex();
		if (name == null || StringUtil.isEmpty(name) || index == null) {
			// 为空返回修改失败
			request.setAttribute("modifyOk", false);
			request.setAttribute("configDetail", configDetail);
			return "system/systemConfigs/messageDetailModify";
		}
		// 利用Service的方法修改频道
		if (messageConfigDetailService.modifyDetail(configDetail)) {
			// 修改成功，返回true
			request.setAttribute("modifyOk", true);
			request.setAttribute("configDetail", configDetail);
			session.setAttribute("configDetail", configDetail);
			return "system/systemConfigs/messageDetailModify";
		} else {
			// 修改失败，返回false
			request.setAttribute("modifyOk", false);
			request.setAttribute("configDetail", configDetail);
			return "system/systemConfigs/messageDetailModify";
		}
	}
}
