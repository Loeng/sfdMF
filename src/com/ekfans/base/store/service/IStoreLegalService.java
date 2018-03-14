package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.StoreLegal;
import com.ekfans.base.store.model.StoreLegalResume;

/**
 * 企业法人信息Service接口
 * 
 * @ClassName: IStoreLegalService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreLegalService {

	/**
	 * 根据id获取企业法人信息（id和企业id相同）
	 */
	public StoreLegal getStoreLegalById(String id);
	
	/**
	 * 保存或更新企业法人信息和企业法人简历信息
	 * 
	 * @param sl 法人信息
	 * @param slrlist 法人简历信息
	 * @return true:成功，false:失败
	 */
	public boolean updateOrSaveStoreLegal(StoreLegal sl, List<StoreLegalResume> slrlist);
}