package com.ekfans.base.store.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.system.model.Manager;

/**
 * 
 * 
 * @ClassName: IStoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IAccreditService {

	public Accredit getAccreditByStoreAndType(String storeId, String type);

	public String getChildIdsByAccId(String accId);

	public Boolean saveOrUpdate(HttpServletRequest request, HttpServletResponse response, String rzType);

	/**
	 * 审核资质
	 * 
	 * @param rzType
	 * @param storeId
	 * @param manager
	 * @return
	 */
	public boolean checkAccs(String rzType, String storeId, Manager manager);
	
	public List<Accredit> getAccreditList(String storeId, Boolean checked);
}