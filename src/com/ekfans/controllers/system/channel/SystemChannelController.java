package com.ekfans.controllers.system.channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.channel.model.Channel;
import com.ekfans.base.channel.model.ChannelConfig;
import com.ekfans.base.channel.service.IChannelConfigService;
import com.ekfans.base.channel.service.IChannelService;
import com.ekfans.base.channel.util.ChannelConfigConst;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.base.content.service.IShopAdService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.service.ISystemConfigCacheService;
import com.ekfans.plugin.cache.service.SystemConfigCacheService;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.plugin.page.util.BasicConst;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemChannelController extends BasicController {
	// 定义Service
	@Autowired
	private IChannelService channelService;

	@Autowired
	private IChannelConfigService channelConfigService;

	/**
	 * 跳转到新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/channel/add")
	public String add(HttpServletRequest request) {

		List<Channel> channelList = channelService.getTopChannels(request, null);
		request.setAttribute("channelList", channelList);
		return "/system/channel/channelAdd";
	}

	/**
	 * 新增频道
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/channel/save")
	public String save(Channel channel, HttpServletRequest request) {
		try {
			if (StringUtil.isEmpty(channel.getName()) || StringUtil.isEmpty(channel.getId())) {
				// 如果为空，返回添加失败
				request.setAttribute("addOk", false);
				return "system/channel/channelAdd";
			}
			// 利用Service的方法添加频道
			if (channelService.addChannel(channel)) {
				request.setAttribute("addOk", true);
				ISystemConfigCacheService cacheService = new SystemConfigCacheService();
				cacheService.refreshChannel(channel.getType() + "");
				List<Channel> channelList = channelService.getTopChannels(request, null);
				request.setAttribute("channelList", channelList);
				// 添加成功，返回
				return "/system/channel/channelAdd";
			}
		} catch (Exception e) {
			throw new RuntimeException("添加失败");
		}
		return "error";
	}

	/**
	 * 删除频道
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/channel/delete/{id}")
	@ResponseBody
	public Object delete(@PathVariable String id) {
		try {
			Channel channel = channelService.getChannelById(id);
			String type = "";
			if (channel != null) {
				type = channel.getType() + "";
			}
			// 利用Service的方法根据id删除频道
			if (channelService.deleteChannel(id)) {
				ISystemConfigCacheService cacheService = new SystemConfigCacheService();
				cacheService.refreshChannel(type);
				// 删除成功，返回true
				return true;
			}
		} catch (Exception e) {
			// 删除失败，返回false
			return false;
		}
		return "error";
	}

	/**
	 * 修改频道
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/channel/modify")
	public String modify(Channel channel, HttpServletRequest request) {
		try {
			Channel oldChannel = channelService.getChannelById(channel.getId());

			// 利用Service的方法修改频道
			if (channelService.modifyChannel(channel)) {
				// 修改成功，返回true
				request.setAttribute("modifyOk", true);
				request.setAttribute("channel", channel);
				ISystemConfigCacheService cacheService = new SystemConfigCacheService();
				if (!oldChannel.getType().equals(channel.getType())) {
					cacheService.refreshChannel(oldChannel.getType() + "");
				}
				cacheService.refreshChannel(channel.getType() + "");
				List<Channel> channelList = channelService.getTopChannels(request, null);
				request.setAttribute("channelList", channelList);
				return "system/channel/channelModify";
			}
		} catch (Exception e) {
			throw new RuntimeException("修改失败");
		}
		return "error";
	}

	/**
	 * 查询频道信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/channel/detail/{id}")
	public String detail(@PathVariable String id, HttpServletRequest request) {
		// 利用Service的方法根据id查找频道
		Channel channel = channelService.getChannelById(id);
		// 绑定页面显示需要的数据
		request.setAttribute("channel", channel);
		List<Channel> channelList = channelService.getTopChannels(request, null);
		request.setAttribute("channelList", channelList);
		return "/system/channel/channelModify";
	}

	/**
	 * 查询频道信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/channel/eyeChannel/{id}")
	public String eyeChannel(@PathVariable String id, HttpServletRequest request) {
		try {
			// 利用Service的方法根据id查找频道
			Channel channel = channelService.getChannelById(id);
			request.setAttribute("channel", channel);
			return "system/channel/channelQuery";
		} catch (Exception e) {

		}
		return "error";
	}

	/**
	 * 查找频道列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/channel/list")
	public String list(HttpServletRequest request) throws Exception {
		try {
			// 定义分页
			Pager pager = new Pager();
			// 从页面获取频道名称
			String name = request.getParameter("name");
			// 从页面获取页码
			String currentpageStr = request.getParameter("pageNum");
			// 将从页面获取的分页数据转化成int型
			int currentPage = 1;
			if (!StringUtil.isEmpty(currentpageStr)) {
				currentPage = Integer.parseInt(currentpageStr);
			}
			// 设置要查询的页码
			pager.setCurrentPage(currentPage);
			// 利用Service的方法查找频道
			List<Channel> list = channelService.listChannel(pager, name);
			List<Channel> channels = new ArrayList<Channel>();
			if (list != null && list.size() > 0) {
				BasicRequest brequest = new BasicRequest(request);
				for (int i = 0; i < list.size(); i++) {
					Channel channel = list.get(i);
					if (channel != null) {
						channel.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_CHANNEL, channel.getId()));
						channels.add(channel);
					}
				}
			}
			request.setAttribute("channels", channels);
			request.setAttribute("pager", pager);
			request.setAttribute("name", name);
			return "/system/channel/channelList";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "error";
	}

	/**
	 * 检查频道id是否存在
	 * 
	 * @return
	 */
	@RequestMapping(value = "/system/channel/checkId/{id}")
	@ResponseBody
	public Object checkId(@PathVariable String id) {
		try {
			if (!channelService.checkId(id)) {
				// 如果id不存在，返回true
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 频道配置
	 * 
	 * @Title: config
	 * @Description: TODO根据频道读取频道相应的配置，并将之放入配置页面进行编辑配置
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/channel/config/{channelId}")
	public String channelConfig(@PathVariable String channelId, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Channel channel = null;
		try {
			// 根据传进来的频道主键获取频道对象
			channel = channelService.getChannelById(channelId);
			// 如果获取的频道为空，则返回列表页
			if (channel == null || StringUtil.isEmpty(channel.getViewPath())) {
				return "/web/channel/configError";
			}
			// 从缓存中获取网站名
			session.setAttribute("webName", Cache.getSystemContentConfig("网站名称"));
			// 从缓存中获取网站logo
			session.setAttribute("webLogo", Cache.getSystemContentConfig("网站Logo"));
			// 从缓存中获取网站的底部版权信息
			session.setAttribute("webCopyright", Cache.getSystemContentConfig("底部版权信息"));
			// 从缓存获取网站底部联系信息
			session.setAttribute("webContactInformation", Cache.getSystemContentConfig("底部联系信息"));
			// 从缓存获取大宗帮助中心
			session.setAttribute("dzHelp", Cache.getContentsByCategoryName("大宗帮助中心"));
			// 从缓存获取热线电话
			session.setAttribute("webPhone", Cache.getSystemContentConfig("热线电话"));
			session.setAttribute("ctxpath", request.getContextPath());

			// 获取频道配置的详细参数
			Map<String, ChannelConfig> map = channelConfigService.getChannelConfigsByChannelId(request, channelId);
			request.setAttribute("configMap", map);
			// 将频道ID放入页面
			request.setAttribute("channelId", channelId);
			// 设置状态为True
			request.setAttribute("configStatus", "true");
			List<Channel> channelList = channelService.getAllChannel(request, channel.getType());
			if ("1".equals(channel.getType())) {
				request.setAttribute("dzChannelList", channelList);
			} else if ("0".equals(channel.getType())) {
				request.setAttribute("xzChannelList", channelList);
			}
			List<Channel> allChannel = channelService.getAllChannel(request, null);
			request.setAttribute("channelList", allChannel);
			request.setAttribute("channel", channel);
		} catch (Exception e) {
			return "/web/channel/configError";
		}
		System.out.println("--------------------------------------------"+channel.getViewPath());
		return channel.getViewPath();
	}

	/**
	 * 频道配置单个模块
	 * 
	 * @Title: config
	 * @Description: TODO根据频道读取频道相应的配置，并将之放入配置页面进行编辑配置
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/channel/configRemove/{channelId}/{configType}/{position}")
	@ResponseBody
	public Object configRemove(@PathVariable String channelId, @PathVariable String configType, @PathVariable String position, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 调用Service方法获取频道配置
			return channelConfigService.removeChannelConfig(request, channelId, configType, position);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	/**
	 * 频道配置单个模块
	 * 
	 * @Title: config
	 * @Description: TODO根据频道读取频道相应的配置，并将之放入配置页面进行编辑配置
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/channel/configAlone/{channelId}/{configType}/{position}")
	public String config(@PathVariable String channelId, @PathVariable String configType, @PathVariable String position, HttpServletRequest request, HttpServletResponse response) {
		try {
			// 调用Service方法获取频道配置
			ChannelConfig config = channelConfigService.getChannelConfig(channelId, configType, position);

			if (config == null) {
				config = new ChannelConfig();
				config.setChannelId(channelId);
				config.setConfigPosition(Integer.parseInt(position));
				config.setConfigType(configType);
				config.setObjectType(configType);
			}

			request.setAttribute("channelConfig", config);
			// 如果配置类型为广告
			if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_AD.equals(configType)) {
				// 获取广告名称
				IShopAdService adService = SpringContextHolder.getBean(IShopAdService.class);
				String adName = adService.getAdNameById(config.getObjectId());
				// 将获取的广告名称放入Request
				request.setAttribute("adName", adName);

			} else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_PRODUCT.equals(configType) || ChannelConfigConst.CHANNEL_CONFIG_TYPE_PRODUCT_CATEGORY.equals(configType) || ChannelConfigConst.CHANNEL_CONFIG_TYPE_PRODUCT_CATEGORY_PRODUCT_LIST.equals(configType)||ChannelConfigConst.CHANNEL_CONFIG_TYPE_QG.equals(configType)||ChannelConfigConst.CHANNEL_CONFIG_TYPE_GY.equals(configType)) {
				// 如果配置类型为商品
				IProductCategoryService categoryService = SpringContextHolder.getBean(IProductCategoryService.class);
				// 调用Service查询关联的商品分类的全称
				String categoryFullName = categoryService.getCategoryFullNameByCategoryId(config.getObjectId(), ">");
				request.setAttribute("categoryName", categoryFullName);
			} else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_CONTENT.equals(configType)) {
				// 如果配置类型为资讯
				IContentCategoryService categoryService = SpringContextHolder.getBean(IContentCategoryService.class);
				// 调用Service查询关联的资讯分类的全称
				String categoryFullName = categoryService.getCategoryFullNameByCategoryId(config.getObjectId(), ">");
				request.setAttribute("categoryName", categoryFullName);
			} else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_CONTENT_CATEGORY.equals(configType)) {
				// 如果配置类型为资讯分类
				IContentCategoryService categoryService = SpringContextHolder.getBean(IContentCategoryService.class);
				// 调用Service查询关联的资讯分类的全称
				String categoryFullName = categoryService.getCategoryFullNameByCategoryId(config.getObjectId(), ">");
				request.setAttribute("categoryName", categoryFullName);
			} else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_PRODUCT_CATEGORY_PRODUCT_LIST.equals(configType)) {
				// 如果配置类型为商品分类商品列表
				// IContentCategoryService categoryService =
				// SpringContextHolder.getBean(IContentCategoryService.class);
				// 调用Service查询关联的资讯分类的全称
				// String categoryFullName =
				// categoryService.getCategoryFullNameByCategoryId(config.getObjectId(),
				// ">");
			}else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_CY.equals(configType)){
//				IWftransportService wftransportService = SpringContextHolder.getBean(IWftransportService.class);
				
			}
			return "/web/channel/commons/config/channelConfigEdit";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "/web/channel/configError";
		}
	}

	/**
	 * 频道配置单个模块
	 * 
	 * @Title: config
	 * @Description: TODO根据频道读取频道相应的配置，并将之放入配置页面进行编辑配置
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/channel/configAlone/save")
	@ResponseBody
	public Object configSave(ChannelConfig config, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (config == null) {
				return false;
			}

			// 设置图标保存路径
			String currentPath = "/customerfiles/system/config/" + DateUtil.getNoSpSysDateString();
			// 调用方法获取分类图标，返回图标路径
			String picturePath = FileUploadHelper.uploadFile("configIcon", currentPath, request, response);
			config.setConfigIcon(picturePath);
			return channelConfigService.saveOrUpdate(config);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return false;
		}
	}

	/**
	 * 频道配置
	 * 
	 * @Title: config
	 * @Description: TODO根据频道读取频道相应的配置，并将之放入配置页面进行编辑配置
	 * @param @param request
	 * @param @param response
	 * @param @return 设定文件
	 * @return Object 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/system/channel/configSave/{channelId}")
	@ResponseBody
	public Object configSave(@PathVariable String channelId, HttpServletRequest request, HttpServletResponse response) {
		// 调用Service处理
		return channelService.processHtml(channelId, request);
	}

}
