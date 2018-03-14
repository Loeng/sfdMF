package com.ekfans.base.wfOrder.service;

import java.util.ArrayList;
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

import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.wfOrder.dao.IContractRelDao;
import com.ekfans.base.wfOrder.dao.IScrapWfpDao;
import com.ekfans.base.wfOrder.dao.IScrapWfpTransDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ScrapWfp;
import com.ekfans.base.wfOrder.model.ScrapWfpTrans;
import com.ekfans.base.wfOrder.util.ContractHelper;
import com.ekfans.base.wfOrder.util.ScrapWfpHelper;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class ScrapWfpService implements IScrapWfpService {
	Logger log = LoggerFactory.getLogger(ScrapWfpService.class);

	@Autowired
	private IScrapWfpDao wfpDao;
	@Autowired
	private IScrapWfpTransDao wfpTransDao;

	/**
	 * 根据ID获取对象
	 * 
	 * @param scrapId
	 * @return
	 */
	@Override
	public ScrapWfp getScrapWfpById(String scrapId) {
		if (StringUtil.isEmpty(scrapId)) {
			return null;
		}
		try {
			return (ScrapWfp) wfpDao.get(scrapId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 保存危废申报
	 * 
	 * @param wfp
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public boolean SaveOrUpdate(ScrapWfp wfp, HttpServletRequest request, HttpServletResponse response) {
		if (wfp == null || StringUtil.isEmpty(wfp.getLineId())) {
			return false;
		}

		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		Boolean isUpdate = false;
		if (!StringUtil.isEmpty(wfp.getId())) {
			isUpdate = true;
		}
		// 定义事务处理
		Session session = null;
		Transaction transaction = null;
		try {
			// 创建事务处理Session并开始事务处理
			session = wfpDao.createSession();
			transaction = session.beginTransaction();
			if (!isUpdate) {
				wfp.setCreator(store.getId());
				wfp.setCreateTime(DateUtil.getSysDateTimeString());
			}
			wfp.setUpdateTime(DateUtil.getSysDateTimeString());
			String production = request.getParameter("production");
			String handleNote = request.getParameter("handleNote");
			wfp.setProduction(production);
			wfp.setHandleNote(handleNote);
			wfp.setCheckStatus("0");
			// 设置检测报告
			String currentPath = "/customerfiles/store/" + store.getId() + "/wfp/" + DateUtil.getNoSpSysDateString();
			// 获取PDF文件路径转换为服务端路径
			String reports = FileUploadHelper.uploadFile("reports", currentPath, request, response);
			wfp.setReports(reports);

			if ("0".equals(wfp.getPacks())) {
				wfp.setPacks(request.getParameter("packsOther"));
			}
			if ("0".equals(wfp.getTransTool())) {
				wfp.setTransTool(request.getParameter("transToolOther"));
			}
			if ("0".equals(wfp.getTransType())) {
				wfp.setTransType(request.getParameter("transTypeOther"));
			}

			// 截取字符串 :将2653.00里程截取为2653.00
			if(!StringUtil.isEmpty(wfp.getCourse())){
				String lc = wfp.getCourse().replaceAll("里程", "");
				lc = wfp.getCourse().replaceAll("公里", "");
				wfp.setCourse(lc);
			}
			if (isUpdate) {
				wfpDao.updateBean(wfp, session);
			} else {
				wfpDao.addBean(wfp, session);
			}

			Boolean chilsStatus = ScrapWfpHelper.saveOrUpdateWfpContracts(wfp, wfpTransDao, session, isUpdate, request);

			if (chilsStatus) {
				// 提交事物
				session.flush();
				transaction.commit();
				session.close();
				return true;
			} else {
				transaction.rollback();
				session.close();
				return false;
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 变更运输合同
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean changeTrans(HttpServletRequest request, HttpServletResponse response) {
		String scrapId = request.getParameter("scrapId");
		String contractId = request.getParameter("contractId");
		ScrapWfp wfp = getScrapWfpById(scrapId);
		if (wfp == null) {
			return false;
		}

		ScrapWfp owfp = null;
		if (!StringUtil.isEmpty(wfp.getParentId())) {
			owfp = getScrapWfpById(wfp.getParentId());
		} else {
			owfp = getChildWfp(scrapId);
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = wfpDao.createSession();
			transaction = session.beginTransaction();

			// 更新选择的申报
			Boolean chilsStatus = ScrapWfpHelper.saveOrUpdateWfpContracts(wfp, wfpTransDao, session, true, request);

			// 更新子/父申报
			if (chilsStatus && owfp != null) {
				chilsStatus = ScrapWfpHelper.saveOrUpdateWfpContracts(owfp, wfpTransDao, session, true, request);
			}

			IContractService contractService = SpringContextHolder.getBean(IContractService.class);
			IContractRelDao relDao = SpringContextHolder.getBean(IContractRelDao.class);
			// 更新合同
			Contract contract = contractService.getContractById(contractId);
			if (contract != null) {
				chilsStatus = ContractHelper.saveOrUpdateContractRels(true, contractId, session, request, relDao);
			}

			session.flush();
			transaction.commit();
			session.close();
			return chilsStatus;
		} catch (Exception e) {
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

	/**
	 * 审核危废申报
	 * 
	 * @param wfp
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public boolean Check(String scrapId, HttpServletRequest request, HttpServletResponse response) {
		if (StringUtil.isEmpty(scrapId)) {
			return false;
		}
		try {
			ScrapWfp sb = (ScrapWfp) wfpDao.get(scrapId);
			sb.setCheckStatus("1");
			sb.setUpdateTime(DateUtil.getSysDateString());
			sb.setCheckTime(DateUtil.getSysDateString());
			wfpDao.updateBean(sb);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 获取危废报废集合
	 * 
	 * @param contractId
	 * @param contractName
	 * @param wfName
	 * @param checkStatus
	 * @param pager
	 * @return
	 */
	public List<ScrapWfp> getWfpList(String contractId, String contractName, String contractNo, String wfName, String checkStatus, String storeId, String viewType, Pager pager) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select sw,con.partyAORpartyB from ScrapWfp as sw,Contract as con where 1=1");

		sql.append(" and sw.contractId = con.id");
		if (!StringUtil.isEmpty(contractId)) {
			sql.append(" and sw.contractId = :contractId");
			paramMap.put("contractId", contractId);
		}

		if (!StringUtil.isEmpty(contractName)) {
			sql.append(" and sw.contractName like :contractName");
			paramMap.put("contractName", "%" + contractName + "%");
		}

		if (!StringUtil.isEmpty(contractNo)) {
			sql.append(" and sw.contractNo like :contractNo");
			paramMap.put("contractNo", "%" + contractNo + "%");
		}

		if (!StringUtil.isEmpty(wfName)) {
			sql.append(" and sw.wfName like :wfName");
			paramMap.put("wfName", "%" + wfName + "%");
		}

		if (!StringUtil.isEmpty(checkStatus)) {
			sql.append(" and sw.checkStatus = :checkStatus");
			paramMap.put("checkStatus", checkStatus);
		}

		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and sw.creator = :creator");
			paramMap.put("creator", storeId);
		}
		if (!StringUtil.isEmpty(viewType)) {
			sql.append(" and sw.type = :type");
			paramMap.put("type", "0".equals(viewType) ? "1" : "0");
		}
		sql.append(" order by sw.updateTime desc");

		try {
			List<Object[]> list = null;
			if (pager != null) {
				list = wfpDao.list(pager, sql.toString(), paramMap);
			} else {
				list = wfpDao.list(sql.toString(), paramMap);
			}

			List<ScrapWfp> wfps = new ArrayList<ScrapWfp>();
			for (Object[] obj : list) {
				if (obj == null) {
					continue;
				}
				ScrapWfp wfp = (ScrapWfp) obj[0];
				String partAorB = obj[1].toString();

				if (wfp == null) {
					continue;
				}
				wfp.setContractChoseAOrB(partAorB);
				if ("1".equals(viewType)) {
					if ("0".equals(wfp.getCheckStatus())) {
						wfp.setNowStatus("等待环保厅审核");
						wfp.setCanOrder(false);
					} else if ("2".equals(wfp.getCheckStatus())) {
						wfp.setNowStatus("环保厅审核未通过");
						wfp.setCanOrder(false);
					} else {
						ScrapWfp childWfp = getChildWfp(wfp.getId());
						if (childWfp == null) {
							wfp.setNowStatus("等待处置方(合同乙方)提交申报");
							wfp.setCanOrder(false);
						} else {
							if ("0".equals(childWfp.getCheckStatus())) {
								wfp.setNowStatus("等待环保厅审核处置方(合同乙方)申报信息");
								wfp.setCanOrder(false);
							} else if ("2".equals(childWfp.getCheckStatus())) {
								wfp.setNowStatus("处置方(合同乙方)申报信息审核未通过");
								wfp.setCanOrder(false);
							} else {
								wfp.setNowStatus("环保厅审核通过");
								wfp.setCanOrder(false);
							}
						}
					}
				} else {
					if ("0".equals(wfp.getCheckStatus())) {
						wfp.setNowStatus("等待环保厅审核");
						wfp.setCanOrder(false);
					} else if ("2".equals(wfp.getCheckStatus())) {
						wfp.setNowStatus("申报信息审核未通过");
						wfp.setCanOrder(false);
					} else {
						wfp.setNowStatus("环保厅审核通过");
						wfp.setCanOrder(true);
					}
				}
				wfps.add(wfp);
			}
			return wfps;

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 获取危废报废集合
	 * 
	 * @param contractId
	 * @param contractName
	 * @param wfName
	 * @param checkStatus
	 * @param pager
	 * @return
	 */
	public List<ScrapWfp> getWfpListLoad(String contractId, String contractName, String contractNo, String wfName, String checkStatus, String storeId, String viewType, Pager pager) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select sw from ScrapWfp as sw where 1=1");

		if (!StringUtil.isEmpty(contractId)) {
			sql.append(" and sw.contractId = :contractId");
			paramMap.put("contractId", contractId);
		}

		if (!StringUtil.isEmpty(contractName)) {
			sql.append(" and sw.contractName like :contractName");
			paramMap.put("contractName", "%" + contractName + "%");
		}

		if (!StringUtil.isEmpty(contractNo)) {
			sql.append(" and sw.contractNo like :contractNo");
			paramMap.put("contractNo", "%" + contractNo + "%");
		}

		if (!StringUtil.isEmpty(wfName)) {
			sql.append(" and sw.wfName like :wfName");
			paramMap.put("wfName", "%" + wfName + "%");
		}

		if (!StringUtil.isEmpty(checkStatus)) {
			sql.append(" and sw.checkStatus = :checkStatus");
			paramMap.put("checkStatus", checkStatus);
		}

		if (!StringUtil.isEmpty(storeId)) {
			sql.append(" and sw.buyId = :creator");
			paramMap.put("creator", storeId);
		}
		if (!StringUtil.isEmpty(viewType)) {
			sql.append(" and sw.type = :type");
			paramMap.put("type", "0");
		}
		sql.append(" order by sw.updateTime desc");

		try {
			List<ScrapWfp> list = null;
			if (pager != null) {
				list = wfpDao.list(pager, sql.toString(), paramMap);
			} else {
				list = wfpDao.list(sql.toString(), paramMap);
			}
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 删除在线申报
	 * 
	 * @param scrapWfpId
	 * @return
	 */
	public boolean deleteScrapWfp(String scrapWfpId, HttpServletRequest request) {
		if (StringUtil.isEmpty(scrapWfpId)) {
			return false;
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = wfpDao.createSession();
			transaction = session.beginTransaction();
			BasicRequest br = new BasicRequest(request);
			ScrapWfp wfp = getScrapWfpById(scrapWfpId);
			if (wfp == null) {
				return false;
			}
			FileUtil.deleteFile(br.getFileFullUrl(wfp.getReports()));
			wfpDao.delete(wfp, session);

			Map<String, Object> paramMap = new HashMap<String, Object>();
			StringBuffer delSql = new StringBuffer(" from ScrapWfpTrans as swt where 1=1");
			delSql.append(" and swt.scrapWfpId = :scrapWfpId");
			paramMap.put("scrapWfpId", scrapWfpId);
			wfpTransDao.delete(delSql.toString(), paramMap, session);

			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
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

	/**
	 * 根据申报ID获取在线申报所关联的运输企业
	 * 
	 * @param scrapWfpId
	 * @return
	 */
	public List<ScrapWfpTrans> getTransByWfpId(String scrapWfpId) {
		if (StringUtil.isEmpty(scrapWfpId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(" from ScrapWfpTrans as swt where 1=1");
		sql.append(" and swt.scrapWfpId = :scrapWfpId");
		paramMap.put("scrapWfpId", scrapWfpId);
		sql.append(" order by swt.createTime desc");
		try {
			List<ScrapWfpTrans> transList = wfpTransDao.list(sql.toString(), paramMap);
			return transList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	public ScrapWfp getChildWfp(String parentId) {
		if (StringUtil.isEmpty(parentId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select sw from ScrapWfp as sw where 1=1");

		sql.append(" and sw.parentId = :parentId");
		paramMap.put("parentId", parentId);
		try {
			List<ScrapWfp> wfpList = wfpDao.list(sql.toString(), paramMap);
			if (wfpList != null && wfpList.size() > 0) {
				return wfpList.get(0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 根据申报单ID获取该申报单关联的有效运输企业
	 * 
	 * @param scrapWfpId
	 * @return
	 */
	public List<Store> getTransStoreByWfpId(String scrapWfpId) {
		if (StringUtil.isEmpty(scrapWfpId)) {
			return null;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select distinct(store),swt.transMan,swt.transPhone,swt.transZipCode from ScrapWfpTrans as swt,Store as store where 1=1");
		sql.append(" and swt.scrapWfpId = :scrapWfpId");
		paramMap.put("scrapWfpId", scrapWfpId);
		sql.append(" and swt.transId = store.id");
		sql.append(" order by swt.createTime desc");
		try {
			List<Object[]> transList = wfpTransDao.list(sql.toString(), paramMap);
			if (transList != null && transList.size() > 0) {
				List<Store> storeList = new ArrayList<Store>();
				for (Object[] obj : transList) {
					if (obj == null || obj.length <= 0) {
						continue;
					}
					Store store = (Store) obj[0];
					String transMan = obj[1].toString();
					String transPhone = obj[2].toString();
					String transZipCode = obj[3].toString();
					store.setContactName(transMan);
					store.setContactPhone(transPhone);
					try {
						store.setZipCode(Integer.parseInt(transZipCode));
					} catch (Exception e) {
						// TODO: handle exception
					}
					storeList.add(store);
				}
				return storeList;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
}
