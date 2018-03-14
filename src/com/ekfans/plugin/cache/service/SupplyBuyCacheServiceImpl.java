package com.ekfans.plugin.cache.service;

import java.util.List;
import java.util.Map;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.utils.SupplyBuyCacheKeyUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@SuppressWarnings("unchecked")
public class SupplyBuyCacheServiceImpl implements ISupplyBuyCacheService {
    /**
     * 根据商品分类ID和商品类型获取商品集合
     * 
     * @Title: getSupplyBuysByCategory
     * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
     * @param categoryId
     * @param SupplyBuyType
     * @return List<SupplyBuy> 返回类型
     * @throws
     */


    @Override
    public SupplyBuy getSupplyBuyById(String SupplyBuyId) {
        // 如果傳過來的商品分類ID為空，或者商品分類為空，則返回NULL
        if (StringUtil.isEmpty(SupplyBuyId)) {
            return null;
        }
        // 获取在缓存中的KEY
        String cacheKey = SupplyBuyCacheKeyUtil.getSupplyBuyCacheKey(SupplyBuyId);
        // 從緩存中獲取商品对象
        SupplyBuy SupplyBuy = (SupplyBuy) Cache.engine.get(cacheKey);
        // 如果获取到的集合为空则调用Service查询
        if (SupplyBuy == null) {
            ISupplyBuyService SupplyBuyService = SpringContextHolder.getBean(ISupplyBuyService.class);
            SupplyBuy = SupplyBuyService.getSupplyBuyById(SupplyBuyId);
            Cache.engine.add(cacheKey, SupplyBuy);
        }
        return SupplyBuy;
    }

    @Override
    public void refreshSupplyBuy(String SupplyBuyId) {
        // 如果傳過來的商品分類ID為空，或者商品分類為空，則返回NULL
        if (StringUtil.isEmpty(SupplyBuyId)) {
            return;
        }
        // 获取在缓存中的KEY
        String cacheKey = SupplyBuyCacheKeyUtil.getSupplyBuyCacheKey(SupplyBuyId);
        Cache.engine.remove(cacheKey);
        this.getSupplyBuyById(SupplyBuyId);
    }


    @Override
    public List<SupplyBuy> getWebIndexSupplyBuyInfo(String type,String productType) {
        // 获取在缓存中的KEY
        String cacheKey = SupplyBuyCacheKeyUtil.getSupplyBuyWebIndexKey(type);
        // 從緩存中獲取商品对象
        List<SupplyBuy> SupplyBuysIndex = (List<SupplyBuy>) Cache.engine.get(cacheKey);
        // 如果获取到的集合为空则调用Service查询
        if (SupplyBuysIndex == null) {
    		// 定义分页
    		Pager pager = new Pager();
    		// 将从页面获取的分页数据转化成int型
    		int currentPage = 1;
    		// 设置要查询的页码
    		pager.setCurrentPage(currentPage);
    		pager.setRowsPerPage(24);
            ISupplyBuyService SupplyBuyService = SpringContextHolder.getBean(ISupplyBuyService.class);
            SupplyBuysIndex = SupplyBuyService.getIndexSupplyBuy(pager, null, null, type, productType, null, null, null, null);
            Cache.engine.add(cacheKey, SupplyBuysIndex);
        }
        return SupplyBuysIndex;
    }

    @Override
    public void refrefshWebIndexSupplyBuyInfo(String type, String productType) {
        // 获取在缓存中的KEY
        String cacheKey = SupplyBuyCacheKeyUtil.getSupplyBuyWebIndexKey(type);
        ISupplyBuyService SupplyBuyService = SpringContextHolder.getBean(ISupplyBuyService.class);
		// 定义分页
		Pager pager = new Pager();
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		// 设置要查询的页码
		pager.setCurrentPage(currentPage);
		pager.setRowsPerPage(24);
        List<SupplyBuy> SupplyBuysIndex = SupplyBuyService.getIndexSupplyBuy(pager, null, null, type, productType, null, null, null, null);
        if (SupplyBuysIndex != null && SupplyBuysIndex.size() > 0) {
            Cache.engine.remove(cacheKey);
            Cache.engine.add(cacheKey, SupplyBuysIndex);
        }
    }

	@Override
	public List<String> getSupplyBuysByCategory(String categoryId, String SupplyBuyType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refrefshSupplyBuysByCategory(String categoryId, String SupplyBuyType) {
		// TODO Auto-generated method stub
		
	}
}
