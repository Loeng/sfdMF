package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.channel.model.Channel;
import com.ekfans.base.channel.service.IChannelService;
import com.ekfans.base.channel.util.ChannelConfigConst;
import com.ekfans.base.content.model.AdDetail;
import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ShopAd;
import com.ekfans.base.content.service.IAdDetailService;
import com.ekfans.base.content.service.IContentCategoryService;
import com.ekfans.base.content.service.IContentService;
import com.ekfans.base.content.service.IShopAdService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.product.util.ProductConst;
import com.ekfans.base.store.dao.IStoreChannelConfigDao;
import com.ekfans.base.store.model.StoreChannelConfig;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.AdvertXmlUtil;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.plugin.page.util.BasicConst;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 频道配置业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
public class StoreChannelConfigService implements IStoreChannelConfigService {
	// 定义DAO
	@Autowired
	private IStoreChannelConfigDao storeChannelConfigDao;

	/**
	 * 根据频道ID获取频道配置集合，以Key-value形式返回
	 * 
	 * @Title: getChannelConfigsByChannelId
	 * @param @param channelId
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, StoreChannelConfig> getChannelConfigsByChannelId(HttpServletRequest request, String channelId, String storeId) {
		// 定义返回MAP;
		Map<String, StoreChannelConfig> map = new HashMap<String, StoreChannelConfig>();

		// 如果传进来的频道主键为空，则返回一个新的集合
		if (StringUtil.isEmpty(channelId) || StringUtil.isEmpty(storeId)) {
			return map;
		}

		// 定义SQL的Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL 查询该频道所有配置
		StringBuffer sql = new StringBuffer(" from StoreChannelConfig as cc where 1=1");
		// 关联频道ID
		sql.append(" and cc.channelId = :channelId");
		paramMap.put("channelId", channelId);

		// 关联店铺ID
		sql.append(" and cc.storeId = :storeId");
		paramMap.put("storeId", storeId);
		BasicRequest brequest = new BasicRequest(request);
		try {
			IChannelService channeService = SpringContextHolder.getBean(IChannelService.class);
			String productType = "";
			Channel channel = (Channel) channeService.getChannelById(channelId);
			if (channel != null) {
				if (StringUtil.isEmpty(channel.getType())) {
					productType = "";
				} else if ("0".equals(channel.getType())) {
					productType = ProductConst.PRODUCT_TYPE_XIAOZONG;
				} else if ("1".equals(channel.getType())) {
					productType = ProductConst.PRODUCT_TYPE_DAZONG;
				}
			}

			// 调用DAO查询SQL
			List<StoreChannelConfig> configList = storeChannelConfigDao.list(sql.toString(), paramMap);
			// 如果查出来的配置集合为空或者长度小于或等于0,则返回空Map
			if (configList == null || configList.size() <= 0) {
				return map;
			}

			// 通过反射机制获取ProductService
			IProductService productService = SpringContextHolder.getBean(IProductService.class);
			// 通过反射机制获取ProductCategoryService
			IProductCategoryService categoryService = SpringContextHolder.getBean(IProductCategoryService.class);
			// 通过反射机制获取ContentService
			IContentService contentService = SpringContextHolder.getBean(IContentService.class);
			// 通过反射机制获取ContentCategoryService
			IContentCategoryService contentCategoryService = SpringContextHolder.getBean(IContentCategoryService.class);
			// 通过反射机制获取AdService
			IShopAdService adService = SpringContextHolder.getBean(IShopAdService.class);
			// 通过反射机制获取AdService
			IAdDetailService adDetailService = SpringContextHolder.getBean(IAdDetailService.class);

			// 偏离查询出来的配置集合
			for (int i = 0; i < configList.size(); i++) {
				// 获取频道配置对象
				StoreChannelConfig config = configList.get(i);
				// 如果获取的频道配置对象为空，则跳过该条，继续循环
				if (config == null || StringUtil.isEmpty(config.getObjectId())) {
					continue;
				}

				// 如果该条频道配置类型为商品
				if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_PRODUCT.equals(config.getConfigType())) {
					// 调用方法查询商品集合
					List<Product> productList = productService.getProductsByCategoryId(config.getObjectId(), productType, config.getShowNumber());
					// 如果查出的商品集合不为空并且长度大于0，则放入Map中，key值为： 配置类型+配置位置
					if (productList != null && productList.size() > 0) {
						List<Product> details = new ArrayList<Product>();
						for (int j = 0; j < productList.size(); j++) {
							Product p = productList.get(j);
							if (p != null) {
								p.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_PRODUCT, p.getId()));
								details.add(p);
							}
						}

						config.setDetails(details);
					}

				} else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_PRODUCT_CATEGORY.equals(config.getConfigType())) {
					// 如果该条频道配置类型为商品分类
					List<ProductCategory> categoryList = categoryService.getStoreCategories(storeId, config.getObjectId(), false, config.getShowNumber());
					// 如果查出的商品分类集合不为空并且长度大于0，则放入Map中，key值为： 配置类型+配置位置
					if (categoryList != null && categoryList.size() > 0) {
						List<ProductCategory> details = new ArrayList<ProductCategory>();
						for (int j = 0; j < categoryList.size(); j++) {
							ProductCategory category = categoryList.get(j);
							if (category != null) {
								category.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_PRODUCT_CATEGORY, category.getId()));
								details.add(category);
							}
						}

						config.setDetails(details);
					}

				} else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_CONTENT.equals(config.getConfigType())) {
					Pager pager=new Pager(1);
					// 如果该条频道配置类型为资讯
					List<Content> contentList = contentService.getContentsByCategory(config.getObjectId(), config.getShowNumber(),pager);
					// 如果查出的资讯集合不为空并且长度大于0，则放入Map中，key值为： 配置类型+配置位置
					if (contentList != null && contentList.size() > 0) {
						List<Content> details = new ArrayList<Content>();
						for (int j = 0; j < contentList.size(); j++) {
							Content c = contentList.get(j);
							if (c != null) {
								c.setLinkUrl(brequest.getContentWebUrl(c.getId(), null));
								details.add(c);
							}
						}
						config.setDetails(details);
					}

				} else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_CONTENT_CATEGORY.equals(config.getConfigType())) {
					// 如果该条频道配置类型为资讯分类
					// 如果该条频道配置类型为商品分类
					List<ContentCategory> categoryList = contentCategoryService.getContentCategorysByParentId(config.getObjectId(), false, config.getShowNumber());
					// 如果查出的商品分类集合不为空并且长度大于0，则放入Map中，key值为： 配置类型+配置位置
					if (categoryList != null && categoryList.size() > 0) {
						List<ContentCategory> details = new ArrayList<ContentCategory>();
						for (int j = 0; j < categoryList.size(); j++) {
							ContentCategory cc = categoryList.get(j);
							if (cc != null) {
								cc.setLinkUrl(brequest.getWebUrl(BasicConst.WEB_URL_CONTENT_CATEGORY, cc.getId()));
								details.add(cc);
							}
						}
						config.setDetails(details);
					}

				} else if (ChannelConfigConst.CHANNEL_CONFIG_TYPE_AD.equals(config.getConfigType())) {
					// 如果该条频道配置类型为广告
					ShopAd ad = adService.getAdvertById(config.getObjectId());
					if (ad != null) {
						List<AdDetail> details = adDetailService.getDeailsByAdvertId(ad.getId());
						ad.setDetailist(details);
						AdvertXmlUtil.getAdHtmlWithWeb(request, ad);
						config.setDetails(ad);
					}
				}
				map.put(config.getConfigType() + config.getConfigPosition(), config);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * 根据频道ID、配置类型以及配置位置获取配置对象
	 * 
	 * @Title: getChannelConfigsByChannelId
	 * @param @param channelId
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public StoreChannelConfig getChannelConfig(String channelId, String storeId, String configType, String position) {
		// 如果传进来的频道ID、配置类型、配置位置 其中一个为空，则返回null
		if (StringUtil.isEmpty(channelId) || StringUtil.isEmpty(storeId) || StringUtil.isEmpty(configType) || StringUtil.isEmpty(position)) {
			return null;
		}

		// 定义SQL参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("select config from StoreChannelConfig as config where 1=1");

		// 关联频道ID
		sql.append(" and config.channelId = :channelId");
		paramMap.put("channelId", channelId);

		// 关联店铺ID
		sql.append(" and config.storeId = :storeId");
		paramMap.put("storeId", storeId);

		// 关联配置类型
		sql.append(" and config.configType = :configType");
		paramMap.put("configType", configType);

		int configPosition = 1;
		try {
			configPosition = Integer.parseInt(position);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 关联配置位置
		sql.append(" and config.configPosition = :position");
		paramMap.put("position", configPosition);

		try {
			// 调用DAO查询SQL，返回集合
			List<StoreChannelConfig> configList = storeChannelConfigDao.list(sql.toString(), paramMap);
			// 如果查出来的集合不为空，并且长度大于0，则返回第一个元素
			if (configList != null && configList.size() > 0) {
				return configList.get(0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 根据频道ID、配置类型以及配置位置删除配置对象
	 * 
	 * @Title: getChannelConfigsByChannelId
	 * @param @param channelId
	 * @param @return 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public boolean removeChannelConfig(HttpServletRequest request, String channelId, String storeId, String configType, String position) {
		// 如果传进来的频道ID、配置类型、配置位置 其中一个为空，则返回null
		if (StringUtil.isEmpty(channelId) || StringUtil.isEmpty(storeId) || StringUtil.isEmpty(configType) || StringUtil.isEmpty(position)) {
			return false;
		}

		// 定义SQL参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义查询SQL
		StringBuffer sql = new StringBuffer("select config from StoreChannelConfig as config where 1=1");

		// 关联频道ID
		sql.append(" and config.channelId = :channelId");
		paramMap.put("channelId", channelId);

		// 关联店铺ID
		sql.append(" and config.storeId = :storeId");
		paramMap.put("storeId", storeId);

		// 关联配置类型
		sql.append(" and config.configType = :configType");
		paramMap.put("configType", configType);

		int configPosition = 1;
		try {
			configPosition = Integer.parseInt(position);
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 关联配置位置
		sql.append(" and config.configPosition = :position");
		paramMap.put("position", configPosition);

		try {
			List<StoreChannelConfig> configList = storeChannelConfigDao.list(sql.toString(), paramMap);
			ServletContext servletContext = request.getSession().getServletContext();
			if (configList != null && configList.size() > 0) {
				for (int i = 0; i < configList.size(); i++) {
					StoreChannelConfig config = configList.get(i);
					if (config == null) {
						continue;
					}
					if (!StringUtil.isEmpty(config.getConfigIcon())) {
						try {

							FileUtil.deleteFile(servletContext.getRealPath("/") + "/" + config.getConfigIcon());
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					storeChannelConfigDao.delete(config);
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}

	/**
	 * 更新或保存频道配置
	 * 
	 * @Title: saveOrUpdate
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param config
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean saveOrUpdate(StoreChannelConfig config) {
		// 如果传进来的配置为空，则返回失败
		if (config == null) {
			return false;
		}

		try {
			// 如果传进来的频道配置的ID不为空，则是更新
			if (!StringUtil.isEmpty(config.getId())) {
				storeChannelConfigDao.updateBean(config);
			} else {
				// 如果传进来的频道配置的ID为空，则是新增
				storeChannelConfigDao.addBean(config);
			}
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
}
