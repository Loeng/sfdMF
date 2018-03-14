package com.ekfans.base.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IAccreditDao;
import com.ekfans.base.store.dao.IAccreditRelDao;
import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.dao.IWfpysRelDao;
import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.util.AccreditHelper;
import com.ekfans.base.store.util.StoreConst;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 运输企业车辆信息Service接口实现
 * 
 * @ClassName: CreditEstimatesService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class AccreditService implements IAccreditService {

	private Logger log = LoggerFactory.getLogger(AccreditService.class);
	@Autowired
	private IAccreditDao accreditDao;
	@Autowired
	private IAccreditRelDao accreditRelDao;
	@Autowired
	private IStoreDao storeDao;
	@Autowired
	private IWfpysRelDao wfpysRelDao;
	@Autowired
	private IWfpysService wfpysService;

	@Override
	public Accredit getAccreditByStoreAndType(String storeId, String type) {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer(" from Accredit as ac where 1=1");
			if (!StringUtil.isEmpty(storeId)) {
				sql.append(" and ac.storeId = :storeId");
				paramMap.put("storeId", storeId);
			}

			if (!StringUtil.isEmpty(type)) {
				sql.append(" and ac.rzType = :rzType");
				paramMap.put("rzType", type);
			}
			List<Accredit> acList = accreditDao.list(sql.toString(), paramMap);
			if (acList != null && acList.size() > 0) {
				Accredit accredit = acList.get(0);
				if (accredit != null) {
					String detailIds="";
					if(AccreditHelper.ACC_RZTYPE_YS.equals(accredit.getRzType())){
						detailIds =wfpysService.getChildIdsByAccId(accredit.getId());
					}else{
						detailIds = this.getChildIdsByAccId(accredit.getId());
					}
					accredit.setChildIds(detailIds);
					return accredit;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public String getChildIdsByAccId(String accId) {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer("select ar.detailId from AccreditRel as ar");
			sql.append(" where ar.accreditId = :accreditId");
			paramMap.put("accreditId", accId);
			sql.append(" order by ar.position asc");
			List<String> acList = accreditRelDao.list(sql.toString(), paramMap);
			if (acList == null || acList.size() <= 0) {
				return null;
			}
			StringBuffer returnStr = new StringBuffer();
			for (String detailId : acList) {
				if (!StringUtil.isEmpty(detailId)) {
					returnStr.append(detailId).append(";");
				}
			}
			return returnStr.toString();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean saveOrUpdate(HttpServletRequest request, HttpServletResponse response, String rzType) {
		// TODO Auto-generated method stub
		if (StringUtil.isEmpty(rzType)) {
			return false;
		}
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		try {
			store = (Store) storeDao.get(store.getId());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = accreditDao.createSession();
			transaction = session.beginTransaction();

			if ("cz".equals(rzType)) {
				AccreditHelper.saveOrUpdateWfAccredit(request, response, accreditDao, accreditRelDao, store, session);
				AccreditHelper.saveOrUpdatePwAccredit(request, response, accreditDao, store, session);
				store.setBuyerStatus("1");
				store.setUpdateTime(DateUtil.getSysDateTimeString());
				storeDao.updateBean(store, session);
			} else if ("cs".equals(rzType)) {
				AccreditHelper.saveOrUpdatePwAccredit(request, response, accreditDao, store, session);
				store.setSalerStatus("1");
				store.setUpdateTime(DateUtil.getSysDateTimeString());
				storeDao.updateBean(store, session);
			} else {
				AccreditHelper.saveOrUpdateYSAccredit(request, response, accreditDao,wfpysRelDao,store, session);
				store.setTransStatus("1");
				store.setUpdateTime(DateUtil.getSysDateTimeString());
				storeDao.updateBean(store, session);
			}

			session.flush();
			transaction.commit();
			session.close();
			return true;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error(e.getMessage());
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return null;
	}

	/**
	 * 审核资质
	 * 
	 * @param rzType
	 * @param storeId
	 * @param manager
	 * @return
	 */
	@SuppressWarnings("unused")
	public boolean checkAccs(String rzType, String storeId, Manager manager) {
		if (StringUtil.isEmpty(storeId) || StringUtil.isEmpty(rzType) || manager == null) {
			return false;
		}

		Store store = null;
		try {
			store = (Store) storeDao.get(storeId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (store == null) {
			return false;
		}

		Accredit pwAccredit = null;
		Accredit wfAccredit = null;
		Accredit ysAccredit = null;
		if ("cz".equals(rzType)) {
			pwAccredit = this.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_PW);
			wfAccredit = this.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_WF);
			if (pwAccredit == null || wfAccredit == null) {
				return false;
			}
			pwAccredit.setCheckMan(manager.getName());
			pwAccredit.setCheckTime(DateUtil.getSysDateTimeString());
			pwAccredit.setCheckStatus(true);

			wfAccredit.setCheckMan(manager.getName());
			wfAccredit.setCheckTime(DateUtil.getSysDateTimeString());
			wfAccredit.setCheckStatus(true);

			store.setBuyerStatus(StoreConst.RZ_TYPE_SUC);
			store.setSalerStatus(StoreConst.RZ_TYPE_SUC);

		} else if ("cs".equals(rzType)) {
			pwAccredit = this.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_PW);
			if (pwAccredit == null) {
				return false;
			}
			pwAccredit.setCheckMan(manager.getName());
			pwAccredit.setCheckTime(DateUtil.getSysDateTimeString());
			pwAccredit.setCheckStatus(true);
			store.setSalerStatus(StoreConst.RZ_TYPE_SUC);
		} else {
			ysAccredit = this.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_YS);
			if (ysAccredit == null) {
				return false;
			}
			ysAccredit.setCheckMan(manager.getName());
			ysAccredit.setCheckTime(DateUtil.getSysDateTimeString());
			ysAccredit.setCheckStatus(true);
			store.setTransStatus(StoreConst.RZ_TYPE_SUC);
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = accreditDao.createSession();
			transaction = session.beginTransaction();

			if (wfAccredit != null) {
				accreditDao.updateBean(wfAccredit, session);
			}
			if (pwAccredit != null) {
				accreditDao.updateBean(pwAccredit, session);
			}
			if (ysAccredit != null) {
				accreditDao.updateBean(ysAccredit, session);
			}
			storeDao.updateBean(store, session);
			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	@Override
	public List<Accredit> getAccreditList(String storeId, Boolean checked) {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer(" from Accredit as ac where 1=1");
			if (!StringUtil.isEmpty(storeId)) {
				sql.append(" and ac.storeId = :storeId");
				paramMap.put("storeId", storeId);
			}

			if (checked != null) {
				sql.append(" and ac.checkStatus = :checked");
				paramMap.put("checked", checked);
			}
			List<Accredit> acList = accreditDao.list(sql.toString(), paramMap);
			return acList;
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
		}
		return null;
	}
}