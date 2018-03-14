package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.StoreLegalResume;

/**
 * 企业法人简历信息Service接口
 * 
 * @ClassName: IStoreLegalResumeService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreLegalResumeService {

	/**
	 * 根据企业id获取法人简历
	 * 
	 * @param storeId 企业id
	 */
	public List<StoreLegalResume> getStoreLegalResumeByStoreId(String storeId);
}