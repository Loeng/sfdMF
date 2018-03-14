package com.ekfans.base.store.service;

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

import com.ekfans.base.store.dao.IStoreChannelDao;
import com.ekfans.base.store.model.StoreChannel;
import com.ekfans.base.store.model.StoreChannelConfig;
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
public class StoreChannelService implements IStoreChannelService {
	// 定义DAO
	@Autowired
	private IStoreChannelDao storeChannelDao;

	/**
	 * 添加频道
	 */
	public boolean addChannel(StoreChannel channel) {
		if (channel == null) {
			return false;
		}
		try {
			// 设置创建时间为当前系统时间
			channel.setCreateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法添加频道
			storeChannelDao.addBean(channel);
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
	public StoreChannel getChannelById(String id,String storeId) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		if (StringUtil.isEmpty(storeId)) {
            return null;
        }
		// 定义查询SQL
        StringBuffer sql = new StringBuffer(" select channel from StoreChannel as channel where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 添加查询条件
        sql.append(" and channel.id  = :id");
        paramMap.put("id", id);
        
        sql.append(" and channel.storeId = :storeId");
        paramMap.put("storeId", storeId);
		try {
		    // 调用DAO方法查找频道
		    List<StoreChannel> list = storeChannelDao.list(sql.toString(), paramMap);
		    if(list != null && list.size() > 0){
		        return list.get(0);
		    }
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
			storeChannelDao.deleteById(id);
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
	public boolean modifyChannel(StoreChannel ad) {
		try {
			// 设置修改时间为当前系统时间
			ad.setUpdateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法修改频道
			storeChannelDao.updateBean(ad);
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
	public List<StoreChannel> listChannel(Pager pager, String name) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select channel from StoreChannel as channel where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and channel.name  like :name");
			paramMap.put("name", "%" + name + "%");
		}

		sql.append("order by channel.position asc");

		try {
			// 调用DAO方法查找频道
			List<StoreChannel> list = storeChannelDao.list(pager, sql.toString(), paramMap);

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
			if (storeChannelDao.get(id) != null) {
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
	 * @return List<StoreChannel> 返回类型
	 * @throws
	 */
	public List<StoreChannel> getAllChannel(HttpServletRequest request) {
		// 定义参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer(" from StoreChannel as channel");
		// 频道状态为正常
		sql.append(" where channel.status = :status");
		paramMap.put("status", true);

		// 按照频道排序升序来排序
		sql.append(" order by channel.position asc");

		try {
			// 调用DAO执行SQL
			List<StoreChannel> list = storeChannelDao.list(sql.toString(), paramMap);
			List<StoreChannel> channelList = new ArrayList<StoreChannel>();

			// 如果取出来的频道集合不为空，并且数量大于0，则循环集合，并将每个频道设置链接路径
			if (list != null && list.size() > 0) {
				// 定义BasicRequest
				BasicRequest brequest = new BasicRequest(request);
				// 便利集合
				for (int i = 0; i < list.size(); i++) {
					// 取出每个StoreChannel频道
					StoreChannel channel = list.get(i);
					if (channel != null) {
						// 获取链接路径，并放入频道对象
						channel.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_CHANNEL, channel.getId()));
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
	public boolean processHtml(String channelId,String storeId,String storeName, HttpServletRequest request) {
		// 如果传进来的频道或者html字符串为空，则返回失败
		if (StringUtil.isEmpty(channelId)) {
			return false;
		}

		StoreChannel channel = null;
		ServletContext servletContext = request.getSession().getServletContext();
		try {
			// 创建Context对象
			Map<String, Object> context = new HashMap<String, Object>();

			// 根据传进来的频道主键获取频道对象
			channel = getChannelById(channelId,storeId);

			// 如果获取的频道为空，则返回列表页
			if (channel == null || StringUtil.isEmpty(channel.getViewPath())) {
				return false;
			}

			IStoreChannelConfigService channelConfigService = SpringContextHolder.getBean(IStoreChannelConfigService.class);
			// 获取频道配置的详细参数
			Map<String, StoreChannelConfig> map = channelConfigService.getChannelConfigsByChannelId(request, channelId, storeId);
			context.put("configMap", map);

			// 将频道ID放入Context对象
			context.put("channelId", channelId);
			// 设置状态为True
			context.put("configStatus", "false");
			List<StoreChannel> channelList = getAllChannel(request);
			context.put("channelList", channelList);
			context.put("channel", channel);
			// 从缓存中获取网站名
			context.put("webName", Cache.getSystemContentConfig("网站名称"));
			// 从缓存中获取网站logo
			context.put("webLogo", Cache.getSystemContentConfig("网站Logo"));
			// 从缓存中获取网站的底部版权信息
			context.put("webCopyright", Cache.getSystemContentConfig("底部版权信息"));
			// 从缓存获取网站底部联系信息
			context.put("webContactInformation", Cache.getSystemContentConfig("底部联系信息"));
			// 从缓存获取购物指南并将其放入servletContext
			context.put("shoppingGuide", Cache.getContentsByCategoryName("购物指南"));
			// 从缓存获取配送方式并将其放入servletContext
			context.put("shippingMethod", Cache.getContentsByCategoryName("配送方式"));
			// 从缓存获支付方式并将其放入servletContext
			context.put("paymentMethod", Cache.getContentsByCategoryName("支付方式"));
			// 从缓存获售后服务并将其放入servletContext
			context.put("AfterSalesService", Cache.getContentsByCategoryName("售后服务"));
			
			
			context.put("storeName", storeName);
			context.put("ctxpath", request.getContextPath());
            
            BasicRequest brequest = new BasicRequest(request);
            if(brequest != null){
                context.put("webroot", brequest.getWebFullUrlPrex());
            }
            
			Properties config = new Properties();
			config.put(JetConfig.TEMPLATE_PATH, servletContext.getRealPath("/"));
			// 创建一个默认的 JetEngine
			JetEngine engine = JetEngine.create(config);
			// 获取一个模板对象
			JetTemplate template = engine.getTemplate("/WEB-INF/pages/" + channel.getViewPath() + ".jetx");
			String htmlPath = "/storepage/" +storeId + "/" + channelId + ".html";
			
			// 店铺头部文件
			JetTemplate template1 = engine.getTemplate("/WEB-INF/pages/web/store/top.jetx");
            String htmlPath1 = "/storepage/" +storeId + "/top.html";
            
			File file = new File(servletContext.getRealPath(htmlPath));
			File file1 = new File(servletContext.getRealPath(htmlPath1));
			if(!new File(file.getParent()).exists()){
			    new File(file.getParent()).mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

			if (!file1.exists()) {
                file1.createNewFile();
            }
			
			FileOutputStream fos = new FileOutputStream(file);
			FileOutputStream fos1 = new FileOutputStream(file1);
			template.render(context, fos);
			template1.render(context, fos1);
			fos.close();
			fos1.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}
