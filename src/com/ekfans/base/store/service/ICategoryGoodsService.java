package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.CategoryGoods;

/**
 * @ClassName: CategoryGoodsService  
 * @Description: TODO(货物类别Service接口) 
 * @Copyright: Copyright (c) 2016年3月29日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年3月29日下午3:56:37
 * @version 1.0
 */
public interface ICategoryGoodsService {


	/**
	 * @Title getCategoryGoodsList
	 * @Description TODO() 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<CategoryGoods>
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午3:57:27
	 */
	public List<CategoryGoods> getCategoryGoodsList();

}