package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.LegalResume;

/**
 * 企业法人信息Service接口
 * 
 * @ClassName: ILegalResumeService
 * @Description: 
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface ILegalResumeService {

	public List<LegalResume> getLegalResumeByStoreId(String storeId);
}