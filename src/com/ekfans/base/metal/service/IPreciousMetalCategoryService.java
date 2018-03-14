package com.ekfans.base.metal.service;

import java.util.List;

import com.ekfans.base.metal.model.PreciousMetalCategory;

/**
 * 品目Service接口
 *
 * @ClassName: IInquiryService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IPreciousMetalCategoryService {

	/**
	 * 获取所有品目
	 *
	 * @return
	 */
	public List<PreciousMetalCategory> getCategorys();

	/**
	 * 根据分类ID获取对象
	 *
	 * @param categoryId
	 * @return
	 */
	public PreciousMetalCategory getCategoryById(String categoryId);

	/**
	 * 根据ID删除品目
	 *
	 * @param categoryId
	 * @return
	 */
	public Boolean deleteById(String categoryId);

	/**
	 * 新增或修改
	 *
	 * @param category
	 * @return
	 */
	public Boolean addOrUpdate(PreciousMetalCategory category);

}