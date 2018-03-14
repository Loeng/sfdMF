package com.ekfans.plugin.webService.warehouse;

import java.util.ArrayList;
import java.util.List;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.webService.monitor.vo.containar.MonitorSyncReqBizData;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 数据转换类，用于三分地项目的业务数据，包装成三分地监控系统需要的业务数据
 * @author 成都易科远见有限公司
 *
 */
public class WareHouseDataConvert {

	/**
	 * 初始化通用删除
	 * @param store
	 * @param user
	 * @return
	 */
	public static MonitorSyncReqBizData initDelCommon(String ids) {
		MonitorSyncReqBizData data = new MonitorSyncReqBizData();

		data.setIds(ids);
		return data;
	}

	/**
	 * 初始化企业
	 * @param store
	 * @param user
	 * @return
	 */
	public static MonitorSyncReqBizData initStore(Store store) {
		MonitorSyncReqBizData data = new MonitorSyncReqBizData();

		User user = null;
		if (store.getUserEntity() == null || StringUtil.isEmpty(store.getUserEntity().getType())) {
			IUserDao userDao = SpringContextHolder.getBean(IUserDao.class);
			try {
				user = (User) userDao.get(store.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			user = store.getUserEntity();
		}

		// set企业信息
		WareHouseCompany company = new WareHouseCompany();
		company.setId(store.getId());
//		company.setOrgId(MonitorSyncConst.ORG_ID);
		company.setName(store.getStoreName());
		company.setContactMen(store.getContactName());
		company.setContactTel(store.getContactPhone());
		company.setContactFax(store.getContactFax());
		company.setContactAddress(store.getMailingAddress());
		company.setCreateTime(DateUtil.getSysDateTimeString());
		company.setUpdateTime(DateUtil.getSysDateTimeString());
		if (user != null) {
			company.setType(user.getType());
			company.setContactMobile(user.getMobile());
			company.setContactMail(user.getEmail());
			// 获取关联用户信息
			WareHouseAccount houseAccount =  new WareHouseAccount();
			houseAccount.setId(user.getId());
			houseAccount.setCompanyId(company.getId());
			houseAccount.setPassword(user.getPassword());
			houseAccount.setRoleId("00000001");
			houseAccount.setName(user.getName());
			houseAccount.setType(user.getType());
			houseAccount.setCreateTime(DateUtil.getSysDateTimeString());
			houseAccount.setOrgId("");
			houseAccount.setStatus(true);
			company.setAccount(houseAccount);
		}
		
		List<WareHouseCompany> companies = new ArrayList<>();
		companies.add(company);
		data.setHosueCompanies(companies);
		return data;
	}

	
}
