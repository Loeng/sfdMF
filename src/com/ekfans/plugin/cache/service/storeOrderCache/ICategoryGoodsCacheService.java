package com.ekfans.plugin.cache.service.storeOrderCache;

import java.util.List;

import com.ekfans.base.store.model.CategoryGoods;

/**
 * 
* @ClassName: ICategoryGoodsCacheService
* @Description: TODO(危废运输频道页-列表展示所用缓存)
* @author ekfans
* @date 2014-5-25 下午04:54:57
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ICategoryGoodsCacheService {
	public List<CategoryGoods> getList();
}
