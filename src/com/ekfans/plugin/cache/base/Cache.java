package com.ekfans.plugin.cache.base;

import com.ctc.wstx.util.DataUtil;
import com.ekfans.base.channel.model.Channel;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.store.model.*;
import com.ekfans.base.system.model.MessageConfig;
import com.ekfans.base.system.model.MessageConfigDetail;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.user.model.Bank;
import com.ekfans.plugin.cache.engine.CacheEngine;
import com.ekfans.plugin.cache.engine.DefaultCacheEngine;
import com.ekfans.plugin.cache.engine.MemCachedEngine;
import com.ekfans.plugin.cache.service.*;
import com.ekfans.plugin.cache.service.storeOrderCache.*;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.ResourceBundleUtil;
import com.ekfans.pub.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Cache对象 根据Key获取缓存对象 Cache对象可以根据情况实现不同的缓存引擎
 *
 * @author Lgy
 * @version 1.0
 * @Title: Cache.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: 成都易科远见科技有限公司
 * @date 2013-12-16
 */
public class Cache {
    // 缓存实现引擎
    public static boolean isMemcached = true;

    public static CacheEngine engine = null;

    public Cache() {
        // 初始化缓存处理工具memcache
        try {
            ResourceBundleUtil rbu = new ResourceBundleUtil();
            isMemcached = Boolean.parseBoolean(rbu.getProperty("system.cache.memcache"));
        } catch (Exception ex) {
            ex.printStackTrace();
            isMemcached = false;
        }
        init();
    }

    // 初始化缓存
    public static void init() {
        if (isMemcached) {
            engine = new MemCachedEngine();
            engine.init();
        } else {
            engine = new DefaultCacheEngine();
        }
    }

    /**
     * 根据Key值获取资源束值
     *
     * @param key
     * @return
     */
    public static String getAppResource(String key) {
        // 定义工具的缓存Service
        IToolsCacheService toolsCache = new ToolsCacheServiceImpl();
        // 调用方法获取值并返回
        return toolsCache.getAppResourceByKey(key);
    }

    /**
     * 根据Key值获取资源束值
     *
     * @param key
     * @return
     */
    public static String getResource(String key) {
        // 定义工具的缓存Service
        IToolsCacheService toolsCache = new ToolsCacheServiceImpl();
        // 调用方法获取值并返回
        return toolsCache.getResourceByKey(key);
    }

    /**
     * 根据Key值获取资源束值
     *
     * @param key
     * @return
     */
    public static String getBCSResource(String key) {
        // 定义工具的缓存Service
        IToolsCacheService toolsCache = new ToolsCacheServiceImpl();
        // 调用方法获取值并返回
        return toolsCache.getBCSResourceByKey(key);
    }

    /**
     * 根据角色ID获取店铺权限
     *
     * @param roleId
     * @return
     */
    public static List<StorePurview> getStorePurviewByRoleId(String roleId) {
        // 定义店铺的缓存Service
        IStoreCacheService storeCacheService = new StoreCacheService();
        // 调用方法获取集合并返回
        return storeCacheService.getStorePurviewsByRoleId(roleId);
    }

    /**
     * 根据角色ID获取店铺权限
     *
     * @param roleId
     * @param purviewId
     * @return
     */
    public static StorePurview getStorePurview(String roleId, String purviewId, boolean upStatus) {
        // 定义店铺的缓存Service
        IStoreCacheService storeCacheService = new StoreCacheService();
        // 调用方法获取集合并返回
        return storeCacheService.getStorePurview(roleId, purviewId, upStatus);
    }

    /**
     * 根据角色ID获取店铺权限
     *
     * @param purviewId
     * @return
     */
    public static StorePurview getStorePurview(String purviewId) {
        // 定义店铺的缓存Service
        IStoreCacheService storeCacheService = new StoreCacheService();
        // 调用方法获取集合并返回
        return storeCacheService.getStorePurview(purviewId);
    }

    /**
     * @param key
     * @return String 返回类型
     * @throws
     * @Title: getSystemContentConfig
     * @Description: TODO(获取系统信息配置) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public static String getSystemContentConfig(String key) {
        // 定义系统信息配置Service
        ISystemConfigCacheService systemConfigCacheService = new SystemConfigCacheService();
        // 调用方法获取系统配置并返回
        return systemConfigCacheService.getContentConfig(key);
    }

    /**
     * @param key
     * @return String 返回类型
     * @throws
     * @Title: getSystemParamConfig
     * @Description: TODO(获取系统参数配置) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public static String getSystemParamConfig(String key) {
        // 定义系统参数配置Service
        ISystemConfigCacheService systemConfigCacheService = new SystemConfigCacheService();
        // 调用方法获取系统配置并返回
        return systemConfigCacheService.getParamConfig(key);
    }

    /**
     * 消息发送配置
     *
     * @param @return 设定文件
     * @return MessageConfig 返回类型
     * @throws
     * @Title: getMessageConfig
     * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public static MessageConfig getMessageConfig() {
        ISystemConfigCacheService systemConfigService = new SystemConfigCacheService();
        return systemConfigService.getMessageConfig();
    }

    /**
     * @param @param  ccid
     * @param @return 设定文件
     * @return List<ContentCategory> 返回类型
     * @throws
     * @Title: getChildCategorys
     * @Description: TODO(根据资讯分类名分类获取资讯的名字) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public static ContentCategory getContentsByCategoryName(String categoryName) {
        IStoreCacheService storeCacheService = new StoreCacheService();
        return storeCacheService.getBottomContentByCategoryName(categoryName);
    }

    /**
     * @param @param  ccid
     * @param @return 设定文件
     * @return List<ContentCategory> 返回类型
     * @throws
     * @Title: getChildCategorys
     * @Description: TODO(根据资讯分类名分类获取资讯的名字) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public static String getContentId(String categoryName) {
        IStoreCacheService storeCacheService = new StoreCacheService();
        ContentCategory category = storeCacheService.getContentByCategoryName(categoryName);
        String reStr = "";
        if (null != category) {
            reStr = category.getId();
        }
        return reStr;
    }

    /*
     * 根据咨询id获取资讯分类id
     */
    public static String getContentCatgId(String contentId) {
        IStoreCacheService storeCacheService = new StoreCacheService();
        ContentCategoryRel rel = storeCacheService.getCategoryByCatgId(contentId);
        String reStr = "";
        if (null != rel) {
            reStr = rel.getCategoryId();
        }
        return reStr;
    }

    /**
     * @param @param  ccid
     * @param @return 设定文件
     * @return List<ContentCategory> 返回类型
     * @throws
     * @Title: getChildCategorys
     * @Description: TODO(根据资讯分类名分类获取资讯的名字) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public static String getContentsFullPath(String categoryName, String parentId) {
        IStoreCacheService storeCacheService = new StoreCacheService();
        ContentCategory category = storeCacheService.getContentByConditions(categoryName, parentId);
        String reStr = "";
        if (null != category) {
            reStr = category.getFullPath();
        }
        return reStr;
    }

    public static Map<String, SystemArea> getSystemAreas() {
        ISystemConfigCacheService systemConfigService = new SystemConfigCacheService();
        return systemConfigService.getAllSystemArea();
    }

    /**
     * 根据商品分类ID和商品类型获取商品集合
     *
     * @param categoryId
     * @param productType
     * @return List<Product> 返回类型
     * @throws
     * @Title: getProductsByCategory
     * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public static List<String> getProductsByCategory(String categoryId, String productType) {
        IProductCacheService productService = new ProductCacheServiceImpl();
        return productService.getProductsByCategory(categoryId, productType);
    }

    /**
     * 根据商品ID获取商品对象
     *
     * @return List<Product> 返回类型
     * @throws
     * @Title: getProductsByCategory
     * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    public static Product getProductById(String productId) {
        IProductCacheService productService = new ProductCacheServiceImpl();
        return productService.getProductById(productId);
    }

    /**
     * 根据类型获取频道集合
     *
     * @param type
     * @return
     */
    public static List<Channel> getChannels(String type) {
        ISystemConfigCacheService service = new SystemConfigCacheService();
        return service.getChannels(type);
    }

    public static MessageConfigDetail getMessageConfigDetail(String id) {
        ISystemConfigCacheService service = new SystemConfigCacheService();
        return service.getMessageConfigDetail(id);
    }

    public static ProductCategory getProductCategory(String categoryId) {
        IProductCategoryCacheService cacheService = new productCategoryCachService();
        return cacheService.getCategoryById(categoryId);
    }

    public static List<String> getChildProductCategorys(String parentId) {
        IProductCategoryCacheService cacheService = new productCategoryCachService();
        return cacheService.getChildCategoryIds(parentId);
    }

    public static List<ProductCategory> getProductCategorys() {
        IProductCategoryCacheService cacheService = new productCategoryCachService();
        return cacheService.getCategories();
    }

    public static Channel getChannelById(String id) {
        ISystemConfigCacheService service = new SystemConfigCacheService();
        return service.getChannelById(id);
    }

    // 加载所有的省份集合
    public static List<SystemArea> getProvinces() {
        ISystemConfigCacheService systemConfigService = new SystemConfigCacheService();
        return systemConfigService.allProvinces();
    }

    // 加载合作银行
    public static List<Bank> getBankList() {
        ISystemConfigCacheService systemConfigService = new SystemConfigCacheService();
        return systemConfigService.getBankList();
    }

    // 加载车辆类型
    public static List<VehicleType> getVehicleTypeList() {
        IVehicleTypeCacheService vehicleTypeCacheService = new VehicleTypeCacheService();
        return vehicleTypeCacheService.getList();
    }

    // 加载货物类型
    public static List<CategoryGoods> getCategoryGoodsList() {
        ICategoryGoodsCacheService categoryGoodsCacheService = new CategoryGoodsCacheService();
        return categoryGoodsCacheService.getList();
    }

    // 加载危废品分类标准
    public static List<Wfpys> getWfpysList() {
        IWfpysCacheService wfpysCacheService = new WfpysCacheService();
        return wfpysCacheService.getList();
    }

    // 加载罐体材质
    public static List<TankMaterial> getTankMaterialList() {
        ITankMaterialCacheService tankMaterialCacheService = new TankMaterialCacheService();
        return tankMaterialCacheService.getList();
    }

    // 通过token找到用户
    public static AppUser getAppUserByToken(String token, HttpServletRequest request) {
        IAppUserCacheService service = new AppUserCacheService();
        return service.getAppUserByToken(token, request);
    }

    /**
     * 校验TOKEN是否有效，返回UserId
     *
     * @param token
     * @return
     */
    public static String checkToken(String token) {
        if (StringUtil.isEmpty(token)) {
            return null;
        }

        AppUser appUser = getAppUserByToken(token, null);
        if (appUser != null) {
            return appUser.getUserId();
        }
        return null;
    }

    public static SystemArea getSystemAreaById(String areaId) {
        if (StringUtil.isEmpty(areaId)) {
            return null;
        }
        ISystemConfigCacheService service = new SystemConfigCacheService();
        return service.getSystemAreaById(areaId);
    }

    public static List<List<Product>> getIndexProductXsgp() {
        IProductCacheService service = new ProductCacheServiceImpl();
        return service.getWebIndexProductXsgp();
    }

    public static void refrefshIndexProductXsgp() {
        System.out.println("刷新销售挂牌："+ DateUtil.getSysDateTimeString());
        IProductCacheService service = new ProductCacheServiceImpl();
        service.refrefshWebIndexProductXsgp();
    }

	public static List<SupplyBuy> getIndexSupplyBuyInfo(String type,String productType) {
        ISupplyBuyCacheService service = new SupplyBuyCacheServiceImpl();
        return service.getWebIndexSupplyBuyInfo( type, productType);
	}

    public static void refrefshIndexSupplyBuyInfo(String type,String productType) {
        System.out.println("刷新供需："+ DateUtil.getSysDateTimeString());
        ISupplyBuyCacheService service = new SupplyBuyCacheServiceImpl();
        service.refrefshWebIndexSupplyBuyInfo(type, productType);
    }
}
