package com.ekfans.base.wfOrder.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.wfOrder.dao.IScrapWfpTransDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ScrapWfp;
import com.ekfans.base.wfOrder.model.ScrapWfpTrans;
import com.ekfans.base.wfOrder.service.IContractService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

public class ScrapWfpHelper {

	/**
	 * 保存申报关联运输合同对象
	 * 
	 * @param scrapWfpId
	 * @param transDao
	 * @param session
	 * @param isUpdae
	 * @param request
	 * @return
	 */
	public static boolean saveOrUpdateWfpContracts(ScrapWfp scrapWfp, IScrapWfpTransDao transDao, Session session, Boolean isUpdate, HttpServletRequest request) {
		// 如果传过来的申报ID为空，则返回失败
		if (scrapWfp == null) {
			return false;
		}

		// 从页面获取关联的运输合同ID
		String transContracts = request.getParameter("transContracts");
		String transStoreIds = request.getParameter("transStoreIds");
		if (StringUtil.isEmpty(transStoreIds)) {
			return false;
		}

		// 如果传过来的Dao对象为空，则调用反射机制来获取新对象
		if (transDao == null) {
			transDao = SpringContextHolder.getBean(IScrapWfpTransDao.class);
		}

		try {
			// 如果是更新，则先删除数据库中的管理
			if (isUpdate) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				StringBuffer delSql = new StringBuffer(" from ScrapWfpTrans as swt where 1=1");
				delSql.append(" and swt.scrapWfpId = :scrapWfpId");
				paramMap.put("scrapWfpId", scrapWfp.getId());
				if (session != null) {
					transDao.delete(delSql.toString(), paramMap, session);
				} else {
					transDao.delete(delSql.toString(), paramMap);
				}
			}

			Map<String, Boolean> map = new HashMap<String, Boolean>();
			if (!StringUtil.isEmpty(transContracts)) {
				// 以分号隔开运输合同ID
				String[] contractIds = transContracts.split(";");
				IContractService contractService = SpringContextHolder.getBean(IContractService.class);
				IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);

				// 便利运输合同ID
				for (int i = 0; i < contractIds.length; i++) {
					// 获取当前的运输合同ID
					String contractId = contractIds[i];
					// 如果为空，则跳过
					if (StringUtil.isEmpty(contractId)) {
						continue;
					}

					// 获取运输合同对象
					Contract contract = contractService.getContractById(contractId);
					if (contract == null) {
						continue;
					}

					// 获取运输企业店铺对象
					Store store = storeService.getStore(contract.getSecondId());
					if (store == null) {
						continue;
					}
					map.put(store.getId(), true);
					ScrapWfpTrans trans = new ScrapWfpTrans();
					trans.setContractId(scrapWfp.getContractId());
					trans.setCreateTime(DateUtil.getSysDateTimeString());
					trans.setUpdateTime(DateUtil.getSysDateTimeString());
					trans.setEmergencyPlan(store.getEmergencyPlanFile());
					trans.setEndTime(scrapWfp.getEndTime());
					trans.setScrapWfpId(scrapWfp.getId());
					trans.setStartTime(scrapWfp.getStartTime());
					trans.setTransId(store.getId());
					trans.setTransContractId(contractId);
					String transAddress = request.getParameter(store.getId() + "TransAddress");
					trans.setTransAddress(transAddress);
					String transMan = request.getParameter(store.getId() + "TransMan");
					trans.setTransMan(transMan);
					String transPhone = request.getParameter(store.getId() + "TransPhone");
					trans.setTransPhone(transPhone);
					String transZipCode = request.getParameter(store.getId() + "TransZipCode");
					trans.setTransZipCode(transZipCode);
					if (session != null) {
						transDao.addBean(trans, session);
					} else {
						transDao.addBean(trans);
					}
				}
			}

			if (!StringUtil.isEmpty(transStoreIds)) {
				// 以分号隔开运输合同ID
				String[] storeIds = transStoreIds.split(";");
				IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);

				// 便利运输合同ID
				for (int i = 0; i < storeIds.length; i++) {
					// 获取当前的运输合同ID
					String storeId = storeIds[i];
					// 如果为空，则跳过
					if (StringUtil.isEmpty(storeId) || map.containsKey(storeId)) {
						continue;
					}
					// 获取运输企业店铺对象
					Store store = storeService.getStore(storeId);
					if (store == null) {
						continue;
					}
					map.put(store.getId(), true);
					ScrapWfpTrans trans = new ScrapWfpTrans();
					trans.setContractId(scrapWfp.getContractId());
					trans.setCreateTime(DateUtil.getSysDateTimeString());
					trans.setUpdateTime(DateUtil.getSysDateTimeString());
					trans.setEmergencyPlan(store.getEmergencyPlanFile());
					trans.setEndTime(scrapWfp.getEndTime());
					trans.setScrapWfpId(scrapWfp.getId());
					trans.setStartTime(scrapWfp.getStartTime());
					trans.setTransId(store.getId());
					String transAddress = request.getParameter(store.getId() + "TransAddress");
					trans.setTransAddress(transAddress);
					String transMan = request.getParameter(store.getId() + "TransMan");
					trans.setTransMan(transMan);
					String transPhone = request.getParameter(store.getId() + "TransPhone");
					trans.setTransPhone(transPhone);
					String transZipCode = request.getParameter(store.getId() + "TransZipCode");
					trans.setTransZipCode(transZipCode);
					if (session != null) {
						transDao.addBean(trans, session);
					} else {
						transDao.addBean(trans);
					}
				}
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}
}
