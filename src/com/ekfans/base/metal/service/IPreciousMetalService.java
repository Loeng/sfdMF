package com.ekfans.base.metal.service;

import java.util.List;

import com.ekfans.base.metal.model.PreciousMetal;
import com.ekfans.controllers.ccwccApp.AppPreciousBean;
import com.ekfans.pub.util.Pager;

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
public interface IPreciousMetalService {

	/**
	 * 根据ID获取对象
	 * 
	 * @param id
	 * @return
	 */
	public PreciousMetal getMetalById(String id);

	/**
	 * 根据品类ID获取该品类下得品目
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<PreciousMetal> getMetalsByCategoryId(String categoryId);

	/**
	 * 新增或修改
	 * 
	 * @param metal
	 * @return
	 */
	public boolean saveOrUpdate(PreciousMetal metal);

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	public boolean deleteByIds(String[] ids);

	/**
	 * 获取首页最新行情
	 * 
	 * @param categoryId
	 * @return
	 */
	public List<PreciousMetal> getNewPreciousMetalByCategory(String categoryId, Pager pager);

	/**
	 * 获取跌/涨幅排名
	 * 
	 * @param num
	 * @param flag
	 *            false跌，true涨
	 * @return
	 */
	public List<PreciousMetal> runUps(int num, boolean flag);


	/**
	 * 获取APP最新行情
	 *
	 * @param categoryId
	 * @return
	 */
	public List<AppPreciousBean> getNewPreciousMetalByCategoryWithApp(String categoryId, Pager pager);
}