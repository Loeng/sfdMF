package com.ekfans.base.metal.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.metal.model.PreciousMetalDetail;
import com.ekfans.controllers.ccwccApp.AppPreciousDetailBean;

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
public interface IPreciousMetalDetailService {

	/**
	 * 根据分类ID和日期查询该分类下的明细
	 * 
	 * @param categoryId
	 * @param date
	 * @return
	 */
	public Map<String, PreciousMetalDetail> getDetailsByDateAndCategory(String categoryId, String date);

	/**
	 * 批量新增明细
	 * 
	 * @param metalIds
	 * @param request
	 * @return
	 */
	public boolean saveDetails(String[] metalIds, HttpServletRequest request);

	/**
	 * 新增或更新
	 * 
	 * @param detail
	 * @return
	 */
	public boolean saveOrUpdate(PreciousMetalDetail detail);

	/**
	 * 获取前台图标的JSON数据
	 * 
	 * @param metaId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String getJsonStr(String metaId);

	public List<AppPreciousDetailBean> getPreciousDetail(String metaId, int dateNum);
}