package com.ekfans.plugin.webService.bcs;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

public class BcsHelper {
	public static Object[] checkBcsInfo(Store store, String MCH_NO, String SIT_NO, String ACCOUNT_NO) {
		Object[] obj = new Object[2];
		obj[0] = store;

		String sysMchNo = Cache.getBCSResource("MCH_NO");
		if (StringUtil.isEmpty(MCH_NO) || !sysMchNo.equals(MCH_NO)) {
			obj[1] = "商户编号不符，平台编号:" + sysMchNo + ";传过来的编号:" + MCH_NO;
			return obj;
		}

		IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);

		store = storeService.getStore(SIT_NO);
		if (store == null || !ACCOUNT_NO.equals(store.getAccountNo())) {
			store = null;
			obj[1] = "虚拟账号" + ACCOUNT_NO + "所对应的企业不存在。";
			return obj;
		}
		obj[0] = store;
		return obj;
	}
}
