package com.ekfans.base.channel.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import jetbrick.template.JetConfig;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.channel.dao.IChannelDao;
import com.ekfans.base.channel.model.Channel;
import com.ekfans.base.channel.model.ChannelConfig;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.plugin.page.util.BasicConst;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 频道业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@SuppressWarnings("unchecked")
@Service
public class ChannelService implements IChannelService {
	// 定义DAO
	@Autowired
	private IChannelDao channelDao;

	/**
	 * 添加频道
	 */
	public boolean addChannel(Channel channel) {
		if (channel == null) {
			return false;
		}
		try {
			// 设置创建时间为当前系统时间
			channel.setCreateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法添加频道
			channelDao.addBean(channel);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据id查找频道
	 */
	public Channel getChannelById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			// 调用DAO方法查找频道
			return (Channel) channelDao.get(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据id删除频道
	 */
	public boolean deleteChannel(String id) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		try {
			// 调用DAO方法查找频道
			channelDao.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据id修改频道
	 */
	public boolean modifyChannel(Channel ad) {
		try {
			// 设置修改时间为当前系统时间
			ad.setUpdateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法修改频道
			channelDao.updateBean(ad);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 查找频道列表
	 */
	public List<Channel> listChannel(Pager pager, String name) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select channel from Channel as channel where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and channel.name  like :name");
			paramMap.put("name", "%" + name + "%");
		}
		sql.append(" order by channel.position asc");
		try {
			// 调用DAO方法查找频道
			List<Channel> list = channelDao.list(pager, sql.toString(), paramMap);

			List<Channel> channelList = new ArrayList<Channel>();
			List<Channel> returnList = new ArrayList<Channel>();

			Map<String, List<Channel>> map = new HashMap<String, List<Channel>>();

//			// 如果取出来的频道集合不为空，并且数量大于0，则循环集合，并将每个频道设置链接路径
//			if (list != null && list.size() > 0) {
//				for (int i = 0; i < list.size(); i++) {
//					// 取出每个Channel频道
//					Channel channel = list.get(i);
//					if (channel == null) {
//						continue;
//					}
//
//					if (StringUtil.isEmpty(channel.getParentId())) {
//						channelList.add(channel);
//					} else {
//						List<Channel> channels = null;
//						if (map.containsKey(channel.getParentId())) {
//							channels = map.get(channel.getParentId());
//						}
//						if (channels == null) {
//							channels = new ArrayList<Channel>();
//						}
//						channels.add(channel);
//						map.put(channel.getParentId(), channels);
//					}
//				}
//
//				// 遍历集合
//				for (int i = 0; i < channelList.size(); i++) {
//					// 取出每个Channel频道
//					Channel channel = channelList.get(i);
//					if (channel != null) {
//						List<Channel> channels = map.get(channel.getId());
//						channel.setChilds(channels);
//						returnList.add(channel);
//					}
//				}
//			}
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查商品id是否存在
	 */
	public boolean checkId(String id) {
		try {
			if (channelDao.get(id) != null) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取所有频道
	 * 
	 * @param @return 设定文件
	 * @return List<Channel> 返回类型
	 * @throws
	 */
	public List<Channel> getAllChannel(HttpServletRequest request, String type) {
		// 定义参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer(" from Channel as channel");
		// 频道状态为正常
		sql.append(" where channel.status = :status");
		paramMap.put("status", true);

		if (!StringUtil.isEmpty(type)) {
			sql.append(" and channel.type = :type");
			paramMap.put("type", type);
		}
		// 按照频道排序升序来排序
		sql.append(" order by channel.position asc");

		try {
			// 调用DAO执行SQL
			List<Channel> list = channelDao.list(sql.toString(), paramMap);
			List<Channel> channelList = new ArrayList<Channel>();
			List<Channel> returnList = new ArrayList<Channel>();

			Map<String, List<Channel>> map = new HashMap<String, List<Channel>>();

			// 如果取出来的频道集合不为空，并且数量大于0，则循环集合，并将每个频道设置链接路径
			if (list != null && list.size() > 0) {
				// 定义BasicRequest
				BasicRequest brequest = null;
				if (request != null) {
					brequest = new BasicRequest(request);
				}

				for (int i = 0; i < list.size(); i++) {
					// 取出每个Channel频道
					Channel channel = list.get(i);
					if (channel != null) {
						if (brequest != null) {
							// 获取链接路径，并放入频道对象
							channel.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_CHANNEL, channel.getId()));
						}
						if (!StringUtil.isEmpty(channel.getParentId())) {
							List<Channel> channels = null;
							if (map.containsKey(channel.getParentId())) {
								channels = map.get(channel.getParentId());
							}
							if (channels == null) {
								channels = new ArrayList<Channel>();
							}
							channels.add(channel);
							map.put(channel.getParentId(), channels);

						} else {
							channelList.add(channel);
						}
					}
				}

				// 便利集合
				for (int i = 0; i < channelList.size(); i++) {
					// 取出每个Channel频道
					Channel channel = channelList.get(i);
					if (channel != null) {
						List<Channel> channels = map.get(channel.getId());
						channel.setChilds(channels);
						returnList.add(channel);
					}
				}
			}
			return returnList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 获取所有频道
	 * 
	 * @param @return 设定文件
	 * @return List<Channel> 返回类型
	 * @throws
	 */
	public List<Channel> getTopChannels(HttpServletRequest request, String type) {
		// 定义参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer(" from Channel as channel");
		// 频道状态为正常
		sql.append(" where channel.status = :status");
		paramMap.put("status", true);

		if (!StringUtil.isEmpty(type)) {
			sql.append(" and channel.type = :type");
			paramMap.put("type", type);
		}
		sql.append(" and (channel.parentId = null or channel.parentId = '' or channel.parentId = ' ')");
		// 按照频道排序升序来排序
		sql.append(" order by channel.position asc");

		try {
			// 调用DAO执行SQL
			List<Channel> list = channelDao.list(sql.toString(), paramMap);
			List<Channel> channelList = new ArrayList<Channel>();

			// 如果取出来的频道集合不为空，并且数量大于0，则循环集合，并将每个频道设置链接路径
			if (list != null && list.size() > 0) {
				// 定义BasicRequest
				BasicRequest brequest = null;
				if (request != null) {
					brequest = new BasicRequest(request);
				}
				// 便利集合
				for (int i = 0; i < list.size(); i++) {
					// 取出每个Channel频道
					Channel channel = list.get(i);
					if (channel != null) {
						if (brequest != null) {
							// 获取链接路径，并放入频道对象
							channel.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_CHANNEL, channel.getId()));
						}
						// 将频道放入返回集合
						channelList.add(channel);
					}
				}
			}
			return channelList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 处理编辑保存
	 * 
	 * @Title: processHtml
	 * @Description: 处理HTML语言，将之解析后写入XML，并生成HTML 详细业务流程:
	 *               处理HTML语言，将之解析后写入XML，并生成HTML
	 * @param @param channelId
	 * @param @param htmlStr
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean processHtml(String channelId, HttpServletRequest request) {
		// 如果传进来的频道或者html字符串为空，则返回失败
		if (StringUtil.isEmpty(channelId)) {
			return false;
		}

		Channel channel = null;
		ServletContext servletContext = request.getSession().getServletContext();
		try {
			// 创建Context对象
			Map<String, Object> context = new HashMap<String, Object>();

			// 根据传进来的频道主键获取频道对象
			channel = getChannelById(channelId);

			// 如果获取的频道为空，则返回列表页
			if (channel == null || StringUtil.isEmpty(channel.getViewPath())) {
				return false;
			}

			IChannelConfigService channelConfigService = SpringContextHolder.getBean(IChannelConfigService.class);
			// 获取频道配置的详细参数
			Map<String, ChannelConfig> map = channelConfigService.getChannelConfigsByChannelId(request, channelId);
			context.put("configMap", map);

			// 将频道ID放入Context对象
			context.put("channelId", channelId);
			// 设置状态为True
			context.put("configStatus", "false");
			List<Channel> channelList = getAllChannel(request, channel.getType() + "");
			if ("1".equals(channel.getType())) {
				request.setAttribute("dzChannelList", channelList);
			} else if ("0".equals(channel.getType())) {
				request.setAttribute("xzChannelList", channelList);
			} else {
				request.setAttribute("channelList", channelList);
			}
			context.put("channel", channel);

			// 从缓存中获取网站名
			context.put("webName", Cache.getSystemContentConfig("网站名称"));
			// 从缓存中获取网站logo
			context.put("webLogo", Cache.getSystemContentConfig("网站Logo"));

			// 从缓存中获取优选商城logo
			// context.put("yxLogo", Cache.getSystemContentConfig("优选商城LOGO"));
			// context.put("companyLogo",
			// Cache.getSystemContentConfig("企业中心LOGO"));

			// 从缓存中获取网站的底部版权信息
			context.put("webCopyright", Cache.getSystemContentConfig("底部版权信息"));
			// 从缓存获取网站底部联系信息
			context.put("webContactInformation", Cache.getSystemContentConfig("底部联系信息"));
			// 从缓存获取大宗帮助中心
			context.put("dzHelp", Cache.getContentsByCategoryName("大宗帮助中心"));
			// 从缓存获取优选商城帮助中心
			context.put("yxHelp", Cache.getContentsByCategoryName("优选帮助中心"));
			// 从缓存获取热线电话
			context.put("webPhone", Cache.getSystemContentConfig("热线电话"));
			context.put("ctxpath", request.getContextPath());

			BasicRequest brequest = new BasicRequest(request);
			if (brequest != null) {
				context.put("webroot", brequest.getWebFullUrlPrex());
			}

			Properties config = new Properties();
			config.put(JetConfig.TEMPLATE_PATH, servletContext.getRealPath("/"));
			// 创建一个默认的 JetEngine
			JetEngine engine = JetEngine.create(config);
			// 获取一个模板对象
			JetTemplate template = engine.getTemplate("/WEB-INF/pages/" + channel.getViewPath() + ".jetx");
			String htmlPath = "/" + channelId + ".html";
			File file = new File(servletContext.getRealPath(htmlPath));
			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fos = new FileOutputStream(file);
			template.render(context, fos);
			fos.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
