package com.ekfans.controllers.system.content;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.system.model.SystemContentConfig;
import com.ekfans.base.system.service.ISystemContentConfigService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.service.ISystemConfigCacheService;
import com.ekfans.plugin.cache.service.SystemConfigCacheService;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemContentConfigController extends BasicController {

	private Logger log = LoggerFactory.getLogger(SystemContentConfigController.class);
	@Autowired
	private ISystemContentConfigService contentConfigService;

	// 跳转
	@RequestMapping(value = "/system/content/contentConfig")
	public String add(HttpServletRequest request) throws Exception {
		List<SystemContentConfig> systemContentConfigs = contentConfigService.getConfigList();
		request.setAttribute("systemContentConfigs", systemContentConfigs);
		return "/system/content/contentConfigAdd";
	}

	// 更改或保存
	@RequestMapping(value = "/system/content/contentConfig/saveOrUpdate")
	public String save(HttpServletRequest request, HttpServletResponse response) {
		try {
			String[] id = request.getParameterValues("id");
			for (int i = 0; i < id.length; i++) {
				String idStr = id[i];
				if (!StringUtil.isEmpty(idStr)) {
					SystemContentConfig systemContentConfig = contentConfigService.getById(idStr);
					if (systemContentConfig != null && "img".equals(systemContentConfig.getValueType())) {
						// 设置图标保存路径
						String currentPath = "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/categoryIco/";
						// 调用方法获取分类图标，返回图标路径
						String picturePath = FileUploadHelper.uploadFile("value" + idStr, currentPath, request, response);
						// 把Logo放入缓存
						Cache.engine.add("yxLogo", picturePath);
						systemContentConfig.setValue(picturePath);
					} else {
						String value = request.getParameter(idStr + "Value");
						systemContentConfig.setValue(value);
					}
					contentConfigService.updateContentConfig(systemContentConfig);
				}
			}
			List<SystemContentConfig> systemContentConfigs = contentConfigService.getConfigList();
			request.setAttribute("systemContentConfigs", systemContentConfigs);
			request.setAttribute("addOK", true);
			ISystemConfigCacheService cacheService = new SystemConfigCacheService();
			cacheService.refreshContentConfig();
			// 获取热线
			getSession().setAttribute("webPhone", Cache.getSystemContentConfig("热线电话"));
			// 从缓存中获取网站名
			getSession().setAttribute("webName", Cache.getSystemContentConfig("网站名称"));
			// 从缓存中获取网站logo
			getSession().setAttribute("webLogo", Cache.getSystemContentConfig("网站Logo"));
			// 从缓存中获取网站logo
			// getSession().setAttribute("yxLogo",
			// Cache.getSystemContentConfig("优选商城LOGO"));
			// getSession().setAttribute("companyLogo",
			// Cache.getSystemContentConfig("企业中心LOGO"));
			// 从缓存中获取网站的底部版权信息
			getSession().setAttribute("webCopyright", Cache.getSystemContentConfig("底部版权信息"));
			// 从缓存获取网站底部联系信息
			getSession().setAttribute("webContactInformation", Cache.getSystemContentConfig("底部联系信息"));
			// 从缓存获取网站底部联系信息
			getSession().setAttribute("servieRule", Cache.getSystemContentConfig("服务条款"));
			// 从缓存获取购物指南并将其放入servletContext
			getSession().setAttribute("shoppingGuide", Cache.getContentsByCategoryName("采购流程"));
			// 从缓存获取配送方式并将其放入servletContext
			getSession().setAttribute("shippingMethod", Cache.getContentsByCategoryName("服务与支持"));
			// 从缓存获支付方式并将其放入servletContext
			getSession().setAttribute("myMethod", Cache.getContentsByCategoryName("我的账户"));

			return "/system/content/contentConfigAdd";
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return "error";
	}
}
