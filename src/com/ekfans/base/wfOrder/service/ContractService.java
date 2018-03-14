package com.ekfans.base.wfOrder.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.ekfans.base.content.service.AdDetailService;
import com.ekfans.base.store.dao.StoreDao;
import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dao.UserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.base.wfOrder.dao.ContractContentDao;
import com.ekfans.base.wfOrder.dao.ContractDao;
import com.ekfans.base.wfOrder.dao.ContractDetailsDao;
import com.ekfans.base.wfOrder.dao.ContractRelDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ContractCars;
import com.ekfans.base.wfOrder.util.ContractHelper;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ContractService implements IContractService {
	// 定义错误处理日志
	public static Logger log = LoggerFactory.getLogger(AdDetailService.class);
	@Autowired
	private ContractDao contractDao;
	@Autowired
	private StoreDao storeDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ContractContentDao ccDao;
	@Autowired
	private ContractDetailsDao cdDao;
	@Autowired
	private ContractRelDao crDao;

	/**
	 * 保存合同
	 */
	@Override
	public boolean addContract(Contract con) {
		try {
			if (con.getId() != null && !con.getId().equals("")) {
				contractDao.updateBean(con);
			} else {
				contractDao.addBean(con);
			}
		} catch (Exception e) {
			log.error("保存或更新合同信息失败:" + con.getFile());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 查询登陆用户的合同列表
	 */
	@Override
	public List<Contract> getContractAllList(Pager pager, String stroeid) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from Contract c where 1=1 ");
		if (!StringUtil.isEmpty(stroeid)) {
			hql.append(" and c.creatorId = creatorId");
			params.put("creatorId", stroeid);
			try {
				List<Contract> conreactList = contractDao.list(pager, hql.toString(), params);
				// 根据企业ID获取企业名称
				if (conreactList != null && conreactList.size() > 0) {
					for (int i = 0; i < conreactList.size(); i++) {
						Contract c = conreactList.get(i);
						Store store = (Store) storeDao.get(c.getFirstId());
						c.setFirstName(store.getStoreName());
						// Store storetwo = (Store)
						// storeDao.get(c.getSecondId());
						c.setSecondName(store.getStoreName());
					}
				}
				return conreactList;
			} catch (Exception e) {
				e.printStackTrace();
				log.error("查询企业列表失败!");
			}
		} else {
			log.error("获取登录用户信息失败!");
		}
		return null;
	}

	/**
	 * 根据条件搜索企业列表
	 */
	@Override
	public List<Contract> getContractByParams(Pager pager, String storeId, String contractName, String firstName, String secondName, String startTime, String endTime, String type, String contractNo, String viewType) {
		List<Contract> contractList = new ArrayList<Contract>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select contract,store1.storeName,store2.storeName from Contract as contract,Store as store1 ,Store as store2 where contract.isDel='0' ");
		hql.append(" and contract.firstId = store1.id");
		hql.append(" and contract.secondId = store2.id");

		// 判断登陆用户ID
		if (!StringUtil.isEmpty(storeId)) {
			hql.append(" and (contract.firstId = :storeId or contract.secondId = :storeId)");
			params.put("storeId", storeId);
		}
		// 判断合同名称
		if (!StringUtil.isEmpty(contractName)) {
			hql.append(" and contract.name like :contractName");
			params.put("contractName", "%" + contractName + "%");
		}
		// 判断开始时间是否为空
		if (!StringUtil.isEmpty(startTime)) {
			hql.append(" and contract.startTime >= :startTime");
			params.put("startTime", startTime);
		}
		// 判断结束时间是否为空
		if (!StringUtil.isEmpty(endTime)) {
			hql.append(" and contract.endTime <= :endTime");
			params.put("endTime", endTime);
		}
		if (!StringUtil.isEmpty(viewType)) {
			if ("0".equals(viewType)) {
				// 当前企业作为处置企业（乙方）
				hql.append(" and contract.secondId = :secondId");
				params.put("secondId", storeId);
				hql.append(" and contract.type ='0'");
			} else if ("1".equals(viewType)) {
				hql.append(" and contract.firstId = :firstId");
				params.put("firstId", storeId);
				hql.append(" and contract.type ='0'");
			} else {
				// hql.append(" and contract.firstId = :firstId");
				// params.put("firstId", storeId);
				hql.append(" and contract.type ='1'");
			}

		}

		// 判断类型是否为空
		if (!StringUtil.isEmpty(type)) {
			hql.append(" and contract.type = :type");
			if (!StringUtil.isEmpty(storeId)) {
				if (type.equals("危废处置合同")) {
					params.put("type", "0");
				} else {
					params.put("type", "1");
				}
			} else {
				params.put("type", type);
			}
		}
		// 判断甲方名称
		if (!StringUtil.isEmpty(firstName)) {
			hql.append(" and contract.firstId in (select s1.id from Store as s1 where s1.storeName like :firstName)");
			params.put("firstName", "%" + firstName + "%");
		}
		// 判断乙方名称
		if (!StringUtil.isEmpty(secondName)) {
			hql.append(" and contract.secondId in (select s2.id from Store as s2 where s2.storeName like :secondName)");
			params.put("secondName", "%" + secondName + "%");
		}
		// 判断合同编号
		if (!StringUtil.isEmpty(contractNo)) {
			hql.append(" and contract.contractNo =:contractNo");
			params.put("contractNo", contractNo);
		}
		// 根据更新时间排序后再根据创建时间排序
		hql.append(" order by  contract.updateTime desc contract.createTime desc");
		try {
			List<Object[]> list = contractDao.list(pager, hql.toString(), params);
			for (Object[] object : list) {
				Contract c = (Contract) object[0];
				if (c == null) {
					continue;
				}
				c.setFirstName((String) object[1]);
				c.setSecondName((String) object[2]);
				contractList.add(c);
			}
		} catch (Exception e) {
			log.error("查询失败!");
		}
		return contractList;
	}

	/**
	 * @Title: orderChoseContracts
	 * @Description: TODO(根据查询条件搜索企业列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param firstName
	 * @param secondName
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return List<Contract> 返回类型
	 * @throws
	 */
	public List<Contract> orderChoseContracts(Pager pager, String storeId, String contractName, String storeName, String startTime, String endTime, String type, String contractNo, String viewType) {
		List<Contract> contractList = new ArrayList<Contract>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select contract,store1.storeName,store2.storeName from Contract as contract,Store as store1 ,Store as store2 where contract.isDel='0' ");
		hql.append(" and contract.firstId = store1.id");
		hql.append(" and contract.secondId = store2.id");

		// 判断登陆用户ID
		if (!StringUtil.isEmpty(storeId)) {
			hql.append(" and (contract.firstId = :storeId or contract.secondId = :storeId)");
			params.put("storeId", storeId);
		}
		// 判断合同名称
		if (!StringUtil.isEmpty(contractName)) {
			hql.append(" and contract.name like :contractName");
			params.put("contractName", "%" + contractName + "%");
		}
		// 判断开始时间是否为空
		if (!StringUtil.isEmpty(startTime)) {
			hql.append(" and contract.startTime >= :startTime");
			params.put("startTime", startTime);
		}
		// 判断结束时间是否为空
		if (!StringUtil.isEmpty(endTime)) {
			hql.append(" and contract.endTime <= :endTime");
			params.put("endTime", endTime);
		}
		if (!StringUtil.isEmpty(viewType)) {
			if ("0".equals(viewType)) {
				// 当前企业作为处置企业（乙方）
				hql.append(" and contract.secondId = :secondId");
				params.put("secondId", storeId);
				hql.append(" and contract.type ='0'");
			} else if ("1".equals(viewType)) {
				hql.append(" and contract.firstId = :firstId");
				params.put("firstId", storeId);
				hql.append(" and contract.type ='0'");
			} else {
				// hql.append(" and contract.firstId = :firstId");
				// params.put("firstId", storeId);
				hql.append(" and contract.type ='1'");
			}

		}

		// 判断类型是否为空
		if (!StringUtil.isEmpty(type)) {
			hql.append(" and contract.type = :type");
			if (!StringUtil.isEmpty(storeId)) {
				if (type.equals("危废处置合同")) {
					params.put("type", "0");
				} else {
					params.put("type", "1");
				}
			} else {
				params.put("type", type);
			}
		}
		// 判断企业名称
		if (!StringUtil.isEmpty(storeName)) {
			hql.append(" and (contract.firstId in (select s1.id from Store as s1 where s1.storeName like :storeName) or contract.secondId in (select s2.id from Store as s2 where s2.storeName like :storeName2))");
			params.put("storeName", "%" + storeName + "%");
			params.put("storeName2", "%" + storeName + "%");
		}

		// 判断合同编号
		if (!StringUtil.isEmpty(contractNo)) {
			hql.append(" and contract.contractNo =:contractNo");
			params.put("contractNo", contractNo);
		}
		// 根据更新时间排序后再根据创建时间排序
		hql.append(" order by  contract.updateTime desc contract.createTime desc");
		try {
			List<Object[]> list = contractDao.list(pager, hql.toString(), params);
			for (Object[] object : list) {
				Contract c = (Contract) object[0];
				if (c == null) {
					continue;
				}
				c.setFirstName((String) object[1]);
				c.setSecondName((String) object[2]);
				contractList.add(c);
			}
		} catch (Exception e) {
			log.error("查询失败!");
		}
		return contractList;

	}

	/**
	 * 根据ID查询合同ID
	 */
	@Override
	public Contract getContractById(String contractId) {
		try {
			// 如果合同id不为空
			if (!StringUtil.isEmpty(contractId)) {
				// 得到合同
				Contract con = (Contract) contractDao.get(contractId);
				// 如果合同不为空
				if (con != null) {
					// 根据合同甲方的id得到企业对象
					Store store = (Store) storeDao.get(con.getFirstId());
					con.setFirstName(store.getStoreName());
					// 根据乙方id得到企业对象
					Store store1 = (Store) storeDao.get(con.getSecondId());
					con.setSecondName(store1.getStoreName());
				}
				return con;
			}
		} catch (Exception e) {
			log.error("合同ID不存在查询失败");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新合同信息
	 */
	@Override
	public boolean updateContract(Contract con) {
		try {
			contractDao.updateBean(con);
			return true;
		} catch (Exception e) {
			log.error("更新失败!合同ID：" + con.getId());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteContractById(String contractId) {
		// 定义SESSION
		Session session = null;
		// 定义事务处理Transaction
		Transaction transaction = null;
		try {
			// 调用DAO创建SESSION
			session = contractDao.createSession();
			// 开始事务处理
			transaction = session.beginTransaction();
			// 查询合同里面的合同含量
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuffer hql = new StringBuffer(" from ContractContent cc where cc.contractId=:contractId");
			StringBuffer hql2 = new StringBuffer(" from ContractDetails cd where cd.contractId=:contractId");
			StringBuffer hql3 = new StringBuffer(" from Contract c where c.id=:contractId");
			map.put("contractId", contractId);

			cdDao.delete(hql2.toString(), map, session);
			ccDao.delete(hql.toString(), map, session);
			contractDao.delete(hql3.toString(), map, session);
			session.flush();
			transaction.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			log.error("删除合同失败");
			e.printStackTrace();
		} finally {
			// 关闭SESSION
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	@Override
	public boolean isContractNo(String contractNo) {
		StringBuffer buffer = new StringBuffer("select contractNo from Contract where contractNo ='" + contractNo + "'");
		try {
			List list = contractDao.list(buffer.toString(), null);
			if (list != null && list.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Contract getContractByContractNo(String storeId, String contractNo) {
		String hql = "from Contract where (firstId='" + storeId + "' or secondId='" + storeId + "') and contractNo='" + contractNo + "'";

		try {
			List<Contract> list = contractDao.list(hql, null);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据登陆的storeid 查询与storeid相关的合同
	 */
	@Override
	public List<Contract> getContractByStoreId(Pager pager, Store store, String id, String contractName) {
		List<Contract> contractList = new ArrayList<Contract>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer buffer = new StringBuffer("from Contract c where c.contractCheckStatus=:check and (c.firstId =:firstId or c.secondId =:secondId) ");
		params.put("check", "1");
		params.put("firstId", store.getId());
		params.put("secondId", store.getId());
		if (!StringUtil.isEmpty(contractName)) {
			buffer.append(" and c.name like :name");
			params.put("name", "%" + contractName + "%");
		}
		if (!StringUtil.isEmpty(id)) {
			// 销售合同
			if (id.equals("1")) {
				buffer.append(" and c.type =:type");
				params.put("type", "0");
			} else {
				buffer.append(" and c.type =:type");
				params.put("type", "1");
			}
		}
		// 转换日期格式 yyyy-mm-dd
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		buffer.append(" and c.endTime>='" + df.format(new Date()) + "' order by c.updateTime");
		try {
			List<Contract> contract = contractDao.list(pager, buffer.toString(), params);
			if (contract != null && contract.size() > 0) {
				if (id.equals("1")) {
					for (int i = 0; i < contract.size(); i++) {
						Contract c = contract.get(i);
						String otherId = "";
						// 甲方发布
						if (c.getFirstId().equals(store.getId())) {
							otherId = c.getSecondId();
						}
						// 乙方发布
						if (c.getSecondId().equals(store.getId())) {
							otherId = c.getFirstId();
						}
						StringBuffer buf = new StringBuffer("select id from User where id = '" + otherId + "' and type ='3'");
						// 判断交易对方是否是核心企业
						List<User> user = userDao.list(buf.toString(), null);
						if (user != null && user.size() > 0) {
							contractList.add(c);
						}
					}
				} else {
					contractList = contract;
				}
				return contractList;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询运输合同
	 */
	@Override
	public Contract getContract(String firstId, String secondeId) {
		StringBuffer buffer = new StringBuffer("select * from Contract c where (c.firstId ='" + firstId + "' and c.secondId='" + secondeId + "') or (c.firstId ='" + secondeId + "' and c.secondId='" + firstId + "')");
		try {
			List<Contract> con = contractDao.list(buffer.toString(), null);
			if (con != null) {
				return con.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 保存合同
	 * 
	 * @see
	 * com.ekfans.base.store.service.IContractService#saveContract(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * com.ekfans.base.store.model.Contract)
	 */
	public boolean saveContract(HttpServletRequest request, HttpServletResponse response, Contract contract) {

		// 获取登录用户名称
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		// 定义SESSION
		Session session = null;
		// 定义事务处理Transaction
		Transaction transaction = null;

		try {
			// 调用DAO创建SESSION
			session = contractDao.createSession();
			// 开始事务处理
			transaction = session.beginTransaction();
			// 设置合同类型
			contract.setType(request.getParameter("type"));
			// 设置合同甲方
			contract.setFirstId(request.getParameter("storeIdfirstId"));
			contract.setFirstName(request.getParameter("firstId"));
			// 设置合同乙方
			contract.setSecondId(request.getParameter("storeIdSecondId"));
			contract.setSecondName(request.getParameter("secondId"));
			// 设置创建人
			contract.setCreatorId(store.getId());
			// 设置创建时间
			contract.setCreateTime(DateUtil.getSysDateTimeString());
			// 设置更新时间
			contract.setUpdateTime(DateUtil.getSysDateTimeString());
			// 定义合同附件保存位置
			String contractPDF = "/customerfiles/" + store.getId() + "/contract/" + DateUtil.getNoSpSysDateString() + "/";
			// 获取PDF文件路径转换为服务端路径
			contractPDF = FileUploadHelper.uploadFile("contractPDF", contractPDF, request, response);
			contract.setFile(contractPDF);

			if ("0".equals(contract.getType())) {
				contract.setPayType(request.getParameter("buyPayType"));
				String ykjStr = request.getParameter("buyYkjPrice");
				try {
					contract.setYkjPrice(Double.parseDouble(ykjStr));
				} catch (Exception e) {
				}

			} else {
				contract.setPayType(request.getParameter("transPayType"));
				String ykjStr = request.getParameter("transYkjPrice");
				try {
					contract.setYkjPrice(Double.parseDouble(ykjStr));
				} catch (Exception e) {
				}
			}

			// 保持数据
			contractDao.addBean(contract, session);

			// 调用Helper方法保存含量数据
			boolean saveStatus = false;
			if ("0".equals(contract.getType())) {
				saveStatus = ContractHelper.saveOrUpdateContractContents(false, contract.getId(), session, request);
				if (contract.getPartyAORpartyB().equals("0") && store.getId().equals(contract.getFirstId()) || contract.getPartyAORpartyB().equals("1") && store.getId().equals(contract.getSecondId())) {
					saveStatus = ContractHelper.saveOrUpdateContractRels(false, contract.getId(), session, request, crDao);
				}
			} else {
				saveStatus = ContractHelper.saveOrUpdateContractCars(false, contract.getId(), session, request);
			}
			// 如果成功，则提交事物并返回成功，否则，返回失败
			if (saveStatus) {
				// 失误提交
				session.flush();
				transaction.commit();
				session.close();
				return true;
			} else {
				if (transaction != null) {
					transaction.rollback();
				}
				return false;
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			// 关闭SESSION
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	@Override
	public boolean updateContract(HttpServletRequest request, HttpServletResponse response, Contract contract) {
		// 获取登录用户名称
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		// 定义SESSION
		Session session = null;
		// 定义事务处理Transaction
		Transaction transaction = null;

		try {
			// 调用DAO创建SESSION
			session = contractDao.createSession();
			// 开始事务处理
			transaction = session.beginTransaction();
			// 定义合同附件保存位置
			String contractPDF = "/customerfiles/" + store.getId() + "/contract/" + DateUtil.getNoSpSysDateString() + "/";
			// 获取PDF文件路径转换为服务端路径
			contractPDF = FileUploadHelper.uploadFile("contractPDF", contractPDF, request, response);
			contract.setFile(contractPDF);

			if ("0".equals(contract.getType())) {
				contract.setPayType(request.getParameter("buyPayType"));
				String ykjStr = request.getParameter("buyYkjPrice");
				try {
					contract.setYkjPrice(Double.parseDouble(ykjStr));
				} catch (Exception e) {
				}

			} else {
				contract.setPayType(request.getParameter("transPayType"));
				String ykjStr = request.getParameter("transYkjPrice");
				try {
					contract.setYkjPrice(Double.parseDouble(ykjStr));
				} catch (Exception e) {
				}
			}
			contract.setUpdateTime(DateUtil.getSysDateTimeString());

			if (!StringUtil.isEmpty(contract.getId())) {
				// 保持数据
				contractDao.updateBean(contract, session);
			} else {
				// 保持数据
				contractDao.addBean(contract, session);
			}

			// 调用Helper方法保存含量数据
			boolean saveStatus = false;
			if ("0".equals(contract.getType())) {
				saveStatus = ContractHelper.saveOrUpdateContractContents(true, contract.getId(), session, request);
				if (contract.getPartyAORpartyB().equals("0") && store.getId().equals(contract.getFirstId()) || contract.getPartyAORpartyB().equals("1") && store.getId().equals(contract.getSecondId())) {
					saveStatus = ContractHelper.saveOrUpdateContractRels(true, contract.getId(), session, request, crDao);
				}
			} else {
				saveStatus = ContractHelper.saveOrUpdateContractCars(true, contract.getId(), session, request);
			}
			// 如果成功，则提交事物并返回成功，否则，返回失败
			if (saveStatus) {
				// 失误提交
				session.flush();
				transaction.commit();
				session.close();
				return true;
			} else {
				if (transaction != null) {
					transaction.rollback();
				}
				return false;
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			// 关闭SESSION
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return false;
	}

	@Override
	public Contract getContractByScrapId(String scrapId) {
		String hql = "select c from WfpScrap ws,ContractOrder co,Contract c where ws.id=co.scrapId and co.scrapId='" + scrapId + "' and co.contractId=c.id";

		try {
			List<Contract> list = contractDao.list(hql, null);
			return list == null || list.size() <= 0 ? null : list.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<ContractCars> getCarsByContract(String contractId) {
		if (StringUtil.isEmpty(contractId)) {
			return null;
		}
		String sql = " from ContractCars as cc where cc.contractId = '" + contractId + "'";
		try {
			List<ContractCars> list = contractDao.list(sql, null);
			return list;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public List<Contract> getYsContractsByContractId(String contractId) {
		if (StringUtil.isEmpty(contractId)) {
			return null;
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select c,store.storeName from Contract as c ,ContractRel cr ,Store store where 1=1");
		sql.append(" and cr.contractId = :contractId");
		paramMap.put("contractId", contractId);
		sql.append(" and cr.ysContractId = c.id");
		sql.append(" and c.secondId = store.id");
		sql.append(" order by c.updateTime desc");

		try {
			List<Object[]> conList = contractDao.list(sql.toString(), paramMap);
			List<Contract> returnList = new ArrayList<Contract>();
			if (conList != null && conList.size() > 0) {
				for (Object[] obj : conList) {
					Contract con = (Contract) obj[0];
					String storeName = (String) obj[1];
					if (con != null) {
						con.setSecondName(storeName);
						returnList.add(con);
					}
				}
			}
			return returnList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 根据条件搜索企业列表
	 */
	@Override
	public List<Contract> getContracts(Pager pager, String storeId, String contractName, String firstName, String secondName, String startTime, String endTime, String type, String checkStatus, String contractNo, String viewType) {
		List<Contract> contractList = new ArrayList<Contract>();
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select contract,store1.storeName,store2.storeName from Contract as contract,Store as store1 ,Store as store2 where contract.isDel='0' ");
		hql.append(" and contract.firstId = store1.id");
		hql.append(" and contract.secondId = store2.id");

		// 判断登陆用户ID
		if (!StringUtil.isEmpty(storeId)) {
			hql.append(" and (contract.firstId = :storeId or contract.secondId = :storeId)");
			params.put("storeId", storeId);
		}
		// 判断合同名称
		if (!StringUtil.isEmpty(contractName)) {
			hql.append(" and contract.name like :contractName");
			params.put("contractName", "%" + contractName + "%");
		}
		// 判断开始时间是否为空
		if (!StringUtil.isEmpty(startTime)) {
			hql.append(" and contract.startTime >= :startTime");
			params.put("startTime", startTime);
		}
		// 判断结束时间是否为空
		if (!StringUtil.isEmpty(endTime)) {
			hql.append(" and contract.endTime <= :endTime");
			params.put("endTime", endTime);
		}

		// 判断类型是否为空
		if (!StringUtil.isEmpty(type)) {
			hql.append(" and contract.type = :type");
			params.put("type", type);
		}

		if (!StringUtil.isEmpty(checkStatus)) {
			hql.append(" and contract.contractCheckStatus = :contractCheckStatus");
			params.put("contractCheckStatus", checkStatus);
		}

		// 判断甲方名称
		if (!StringUtil.isEmpty(firstName)) {
			hql.append(" and contract.firstId in (select s1.id from Store as s1 where s1.storeName like :firstName)");
			params.put("firstName", "%" + firstName + "%");
		}
		// 判断乙方名称
		if (!StringUtil.isEmpty(secondName)) {
			hql.append(" and contract.secondId in (select s2.id from Store as s2 where s2.storeName like :secondName)");
			params.put("secondName", "%" + secondName + "%");
		}
		// 判断合同编号
		if (!StringUtil.isEmpty(contractNo)) {
			hql.append(" and contract.contractNo =:contractNo");
			params.put("contractNo", contractNo);
		}
		// 根据更新时间排序后再根据创建时间排序
		hql.append(" order by  contract.updateTime desc contract.createTime desc");
		try {
			List<Object[]> list = contractDao.list(pager, hql.toString(), params);
			for (Object[] object : list) {
				Contract c = (Contract) object[0];
				if (c == null) {
					continue;
				}
				c.setFirstName((String) object[1]);
				c.setSecondName((String) object[2]);
				contractList.add(c);
			}
		} catch (Exception e) {
			log.error("查询失败!");
		}
		return contractList;
	}

	public boolean updateYsContract(String conId, HttpServletRequest request) {
		if (StringUtil.isEmpty(conId)) {
			return false;
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = crDao.createSession();
			transaction = session.beginTransaction();
			ContractHelper.saveOrUpdateContractRels(true, conId, session, request, crDao);
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

	public List<CarInfo> getCarUsersByContract(String contractId, String storeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select info from CarInfo as info,ContractCars as cars where 1=1");
		sql.append(" and info.id = cars.carInfoId");
		sql.append(" and cars.contractId = :contractId");
		map.put("contractId", contractId);
		sql.append(" and info.storeId = :storeId");
		map.put("storeId", storeId);
		try {
			List<CarInfo> carUsers = crDao.list(sql.toString(), map);
			return carUsers;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 校验合同所关联的运输合同是否需要公里数
	 * 
	 * @param contractId
	 * @return
	 */
	public boolean checkContractNeedMileage(String contractId) {
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select count(contract.id) from Contract as contract,ContractRel as rel where 1=1");
		sql.append(" and rel.ysContractId = contract.id");
		sql.append(" and rel.contractId = :contractId");
		map.put("contractId", contractId);
		sql.append(" and contract.weightType = :weightType");
		map.put("weightType", "1");
		try {
			List list = contractDao.list(sql.toString(), map);
			if (list == null || list.size() <= 0) {
				return false;
			}
			Long counts = (Long) list.get(0);
			if (counts > 0) {
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return false;
	}
}
