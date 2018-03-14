package com.ekfans.plugin.webService.bcs;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ekfans.base.store.dao.IAccountBankLogDao;
import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.AccountBank;
import com.ekfans.base.store.model.AccountBankLog;
import com.ekfans.base.store.model.AccountLog;
import com.ekfans.base.store.model.Store;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@SuppressWarnings("unchecked")
public class BCSClientService {

	/**
	 * 客户注册通知
	 * 
	 * @param store
	 * @param bank
	 * @param bankLog
	 */
	public static boolean regdit(Store store, AccountBank bank, AccountBankLog bankLog) {

		Map<String, Object> xmlMap = new HashMap<String, Object>();

		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		xmlMap.put("CUST_CERT_TYPE", bank.getCustomerType() ? "21" : (store.getOrgId().length() <= 15 ? "02" : "01"));
		xmlMap.put("CUST_CERT_NO", store.getOrgId());
		xmlMap.put("SIT_NO", store.getId());
		xmlMap.put("CUST_NAME", bank.getCompanyName());
		xmlMap.put("CUST_ACCT_NAME", bank.getAccountName());
		xmlMap.put("CUST_SPE_ACCT_NO", bank.getAccountNo());
		// -----等待银行提供对照表
		String bankName = bank.getBankName();
		xmlMap.put("CUST_SPE_ACCT_BKTYPE", !StringUtil.isEmpty(bankName) && bankName.indexOf("长沙银行股份有限") != -1 ? "0" : "1");
		xmlMap.put("CUST_SPE_ACCT_BKID", bank.getBankNo());
		xmlMap.put("CUST_SPE_ACCT_BKNAME", bankName);
		xmlMap.put("CUST_PHONE_NUM", bank.getMobile());
		xmlMap.put("CUST_TELE_NUM", bank.getTelPhone());
		xmlMap.put("CUST_ADDR", bank.getAddress());
		xmlMap.put("RMRK", bank.getNote());
		xmlMap.put("ENABLE_ECDS", bank.getEnableEcds() ? "1" : "0");
		xmlMap.put("IS_PERSON", bank.getCustomerType() ? "0" : "1");

		bankLog.setAccountName(bank.getAccountName());
		bankLog.setAccountNo(bank.getAccountNo());
		bankLog.setAddress(bank.getAddress());
		bankLog.setBankName(bank.getBankName());
		bankLog.setBankNo(bank.getBankNo());
		bankLog.setCompanyName(bank.getCompanyName());
		bankLog.setCreateTime(DateUtil.getSysDateTimeString());
		bankLog.setEnableEcds(bank.getEnableEcds());
		bankLog.setMobile(bank.getMobile());
		bankLog.setNote(bank.getNote());
		bankLog.setTelPhone(bank.getTelPhone());
		bankLog.setUserId(bank.getUserId());
		bankLog.setType("FMSCUST0001");
		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSCUST0001", bankLog.getId(), xmlMap, bankLog);

		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		String ReturnCode = reMap.get("ReturnCode").toString();
		if ("00000000".equals(ReturnCode)) {
			String Account_No = reMap.get("ACCOUNT_NO").toString();
			store.setAccountNo(Account_No);
			store.setAccountStatus(true);
			if (!StringUtil.isEmpty(bankName) && bankName.indexOf("长沙银行股份有限") == -1) {
				store.setAccountDate(DateUtil.getSysDateTimeString());
				store.setAccountSuccess(true);
			}
			return true;
		}

		return false;
	}

	/**
	 * 客户解约
	 * 
	 * @param store
	 * @param bank
	 * @param bankLog
	 */
	public static boolean unregdit(Store store, AccountBankLog bankLog) {

		Map<String, Object> xmlMap = new HashMap<String, Object>();

		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		xmlMap.put("SIT_NO", store.getId());
		bankLog.setType("FMSCUST0004");
		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSCUST0004", bankLog.getId(), xmlMap, bankLog);

		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		String ReturnCode = reMap.get("ReturnCode").toString();
		if ("00000000".equals(ReturnCode)) {
			store.setAccountNo(null);
			store.setAccountStatus(false);
			store.setAccountDate(null);
			store.setAccountSuccess(false);
			store.setAccount(new BigDecimal(0.00));
			return true;
		}

		return false;
	}

	/**
	 * 获取用户可用余额
	 * 
	 *
	 */
	public static BigDecimal[] getAvlBal(String storeId) {
		BigDecimal[] prices = new BigDecimal[2];
		prices[0] = new BigDecimal(0.00);
		prices[1] = new BigDecimal(0.00);
		if (StringUtil.isEmpty(storeId)) {
			return prices;
		}
		Map<String, Object> xmlMap = new HashMap<String, Object>();

		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		xmlMap.put("SIT_NO", storeId);

		Session session = null;
		Transaction transaction = null;

		try {
			IAccountBankLogDao bankLogDao = SpringContextHolder.getBean(IAccountBankLogDao.class);
			IStoreDao storeDao = SpringContextHolder.getBean(IStoreDao.class);
			session = bankLogDao.createSession();
			transaction = session.beginTransaction();
			Store store = (Store) storeDao.get(storeId);
			AccountBankLog bankLog = new AccountBankLog();
			bankLog.setCreateTime(DateUtil.getSysDateTimeString());
			bankLog.setUserId(storeId);
			bankLog.setType("FMSCUST0003");
			bankLogDao.addBean(bankLog, session);

			Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSCUST0003", bankLog.getId(), xmlMap, bankLog);

			String ReturnCode = reMap.get("ReturnCode").toString();
			String ReturnMessage = reMap.get("ReturnMessage").toString();
			bankLog.setErrorMessage(ReturnMessage);
			if ("00000000".equals(ReturnCode)) {
				String ACCT_BAL = reMap.get("ACCT_BAL").toString();
				String AVL_BAL = reMap.get("AVL_BAL").toString();
				if (!StringUtil.isEmpty(ACCT_BAL)) {
					prices[0] = new BigDecimal(ACCT_BAL);
				}
				if (!StringUtil.isEmpty(AVL_BAL)) {
					prices[1] = new BigDecimal(AVL_BAL);
					store.setAccount(prices[1]);
					storeDao.updateBean(store, session);
				}
			}
			bankLogDao.updateBean(bankLog, session);

			session.flush();
			transaction.commit();
			session.close();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return prices;
	}

	/**
	 * 客户入金操作（充值--仅先绑定长沙银行账户的客户）
	 * 
	 * @param store
	 * @param bankLog
	 * @param accountLog
	 * @param price
	 */
	public static boolean sufficient(Store store, AccountBankLog bankLog, AccountLog accountLog, BigDecimal price) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();

		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		xmlMap.put("SIT_NO", store.getId());
		xmlMap.put("MCH_TRANS_NO", bankLog.getMchTransNo());
		xmlMap.put("CURR_COD", "01");
		xmlMap.put("TRANS_AMT", price.doubleValue());

		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSPAY0012", accountLog.getId(), xmlMap, bankLog);

		String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		if ("00000000".equals(ReturnCode)) {
			// 资金监管系统交易流水号
			String FMS_TRANS_NO = reMap.get("FMS_TRANS_NO").toString();
			// 交易状态 1:交易成功；2：交易失败；3：处理中
			String TRANS_STS = reMap.get("TRANS_STS").toString();
			// 交易金额
			String TRANS_AMT = reMap.get("TRANS_AMT").toString();
			// 交易完成时间
			String TRANS_TIME = reMap.get("TRANS_TIME").toString();
			accountLog.setCreateTime(TRANS_TIME);
			accountLog.setFmsTransNo(FMS_TRANS_NO);
			accountLog.setPrice(new BigDecimal(TRANS_AMT));
			accountLog.setStatus(TRANS_STS);
			accountLog.setType("FMSPAY0012");
			accountLog.setUserId(store.getId());
			bankLog.setType("FMSPAY0012");
			if ("1".equals(TRANS_STS)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public static boolean payForOrder(String storeId, String receiveId, String orderId, String type, BigDecimal price, AccountBankLog bankLog, AccountLog accountLog) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();

		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		// 订单编号
		xmlMap.put("CTRT_NO", orderId);
		// 付款方席位号
		xmlMap.put("BUYER_SIT_NO", storeId);
		// 收款方席位号
		xmlMap.put("SELLER_SIT_NO", receiveId);
		// 功能号
		xmlMap.put("FUNC_CODE", type);
		// 交易金额
		DecimalFormat df = new DecimalFormat("#.00");
		String decimalStr = df.format(price).equals(".00") ? "0.00" : df.format(price);
		if (decimalStr.startsWith(".")) {
			decimalStr = "0" + decimalStr;
		}
		xmlMap.put("TX_AMT", decimalStr);
		// 买方佣金金额
		xmlMap.put("SVC_AMT", 0.00);
		// 卖方佣金金额
		xmlMap.put("BVC_AMT", 0.00);
		// 币别 目前只支持：01-人民币
		xmlMap.put("CURR_COD", "01");
		// 商户交易流水号
		xmlMap.put("MCH_TRANS_NO", accountLog.getId());
		// 银票机构编号
		xmlMap.put("ORGNO", "");
		// 使用票据数
		xmlMap.put("TICKET_NUM", "");
		// 使用票据数
		xmlMap.put("List", "");

		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSTRAN0011", bankLog.getId(), xmlMap, bankLog);

		String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		if ("00000000".equals(ReturnCode)) {
			// 资金监管系统交易流水号
			String FMS_TRANS_NO = reMap.get("FMS_TRANS_NO").toString();
			// 交易完成时间
			String TRANS_TIME = reMap.get("TRANS_TIME").toString();
			accountLog.setCreateTime(TRANS_TIME);
			accountLog.setFmsTransNo(FMS_TRANS_NO);
			accountLog.setPrice(price);
			accountLog.setStatus("1");
			accountLog.setType("FMSTRAN0011");
			accountLog.setUserId(storeId);
			accountLog.setReceiveId(receiveId);
			bankLog.setType("FMSTRAN0011");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 交易状态查询,根据原请求交易流水号查询原交易处理状态，用于出入金交易、现货交易（冻结、解冻、付款）的未明结果查询 --------待定
	 * 
	 * @param transNo
	 * @param funcCode
	 * @param bankLog
	 * @param accountLog
	 * @return
	 */
	public static Map<String, Object> getPayInfo(String transNo, String funcCode, AccountBankLog bankLog, AccountLog accountLog) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();
		// 原交易流水号
		xmlMap.put("OLD_TRANS_NO", transNo);
		// 功能号 0-出入金交易，1-冻结解冻交易，2-现货交易
		xmlMap.put("FUNC_CODE", funcCode);

		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSTRAN0010", accountLog.getId(), xmlMap, bankLog);

		String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		if ("00000000".equals(ReturnCode)) {
			// // 1:交易成功；2：交易失败；3：状态未知；4：未找到交易记录
			// String TRANS_STS = reMap.get("TRANS_STS").toString();
			//
			// // 返回码
			// String OLD_RECODE = reMap.get("OLD_RECODE").toString();
			// // 返回信息
			// String OLD_REMSG = reMap.get("OLD_REMSG").toString();
			// 资金监管系统交易流水号
			String FMS_TRANS_NO = reMap.get("FMS_TRANS_NO").toString();

			accountLog.setFmsTransNo(FMS_TRANS_NO);
			accountLog.setStatus("1");
			accountLog.setType("FMSTRAN0010");
			bankLog.setType("FMSTRAN0010");

		}
		return reMap;
	}

	/**
	 * 交易状态查询,根据原请求交易流水号查询原交易处理状态，用于出入金交易、现货交易（冻结、解冻、付款）的未明结果查询 --------待定
	 *
	 * @param bankLog
	 * @param accountLog
	 * @return
	 */
	public static Map<String, Object> getOrderPayInfo(String orderId, AccountBankLog bankLog, AccountLog accountLog) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();
		// 商户编号
		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		// 订单编号
		xmlMap.put("CTRT_NO", orderId);

		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSTRAN0004", bankLog.getId(), xmlMap, bankLog);

		String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		bankLog.setType("FMSTRAN0004");
		if ("00000000".equals(ReturnCode)) {
			return reMap;
		} else {
			return null;
		}
	}

	/**
	 * 客户出金(客户提取现金)
	 * @param bankLog
	 * @param accountLog
	 * @return
	 */
	public static boolean takeMony(String storeId, String salStoreId, BigDecimal price, BigDecimal charge, AccountBankLog bankLog, AccountLog accountLog) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();

		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");

		// 商户编号
		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		// 席位号
		xmlMap.put("SIT_NO", storeId);
		xmlMap.put("OTHER_SIT_NO", StringUtil.isEmpty(salStoreId)?"":salStoreId);
		// 商户交易流水号
		xmlMap.put("MCH_TRANS_NO", accountLog.getId());
		// 币别
		xmlMap.put("CURR_COD", "01");
		// 交易金额
		xmlMap.put("TRANS_AMT", df.format(price));
		// 手续费 -- 前期手续费由平台支撑
		xmlMap.put("TRANS_FEE", df.format(charge));

		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSPAY0002", bankLog.getId(), xmlMap, bankLog);

		String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		if ("00000000".equals(ReturnCode)) {

			// 资金监管系统交易流水号
			String FMS_TRANS_NO = reMap.get("FMS_TRANS_NO").toString();
			// 交易状态
			String TRANS_STS = reMap.get("TRANS_STS").toString();
			// 交易金额
			String TRANS_AMT = reMap.get("TRANS_AMT").toString();
			// 手续费金额
			// String TOTALAMT = reMap.get("TOTALAMT").toString();
			// 交易完成时间
			String TRANS_TIME = reMap.get("TRANS_TIME").toString();

			accountLog.setFmsTransNo(FMS_TRANS_NO);
			accountLog.setStatus(TRANS_STS);
			accountLog.setPrice(new BigDecimal(TRANS_AMT));
			accountLog.setCreateTime(TRANS_TIME);
			accountLog.setType("FMSPAY0002");
			bankLog.setType("FMSPAY0002");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取用户交易信息
	 *
	 * @param bankLog
	 * @param accountLog
	 * @return
	 */
	public static Map<String, Object> getUserInfo(String storeId, String startTime, String endTime, String pageNo, String orderId, String rowsPerpage, AccountBankLog bankLog,
			AccountLog accountLog) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();

		// 功能号 1-按席位号查询; 2-按证件查询；3-虚拟账号
		xmlMap.put("FUNC_CODE", "1");
		// 商户编号
		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		// 席位号
		xmlMap.put("SIT_NO", storeId);
		// 证件类型
		xmlMap.put("MBR_CERT_TYPE", null);
		// 证件号码
		xmlMap.put("MBR_CERT_NO", null);
		// 虚拟账号
		xmlMap.put("MBR_SERVICE_NO", null);
		// 订单编号 若输入订单号，则只查输入订单的冻结记录
		xmlMap.put("CTRT_NO", orderId);
		// 开始日期
		xmlMap.put("START_DATE", startTime);
		// 结束日期
		xmlMap.put("END_DATE", endTime);

		// 分页开始索引
		xmlMap.put("PAGE_NUMBER", pageNo);
		// 分页大小
		xmlMap.put("PAGE_SIZE", rowsPerpage);

		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSTRAN0009", bankLog.getId(), xmlMap, bankLog);

		// String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		bankLog.setType("FMSTRAN0009");
		return reMap;
	}

	/**
	 * 获取用户充值/提现信息
	 * 
	 * @param transNo
	 * @param funcCode
	 * @param bankLog
	 * @param accountLog
	 * @return
	 */
	public static Map<String, Object> getAccountInfo(String storeId, String startTime, String endTime, String pageNo, String rowsPerpage, AccountBankLog bankLog,
			AccountLog accountLog) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();

		// 商户编号
		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		// 席位号
		xmlMap.put("SIT_NO", storeId);
		// 开始时间 YYYY-MM-DD HH24:MI:SS
		xmlMap.put("START_DATE", !StringUtil.isEmpty(startTime) ? (startTime + " 00:00:00") : "");
		// 结束时间 YYYY-MM-DD HH24:MI:SS
		xmlMap.put("END_DATE", !StringUtil.isEmpty(endTime) ? (endTime + " 23:59:59") : "");
		// 分页开始索引
		xmlMap.put("PAGE_NUMBER", pageNo);
		// 分页大小
		xmlMap.put("PAGE_SIZE", rowsPerpage);

		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSPAY0003", bankLog.getId(), xmlMap, bankLog);
		// String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		bankLog.setType("FMSPAY0003");
		return reMap;
	}

	/**
	 * 查询银行列表
	 * 
	 * @param bankNo
	 * @param bankName
	 * @param pager
	 * @return
	 */
	public static List<Map<String, Object>> getBankNo(String bankNo, String bankName, Pager pager, AccountBankLog bankLog) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();

		// 查询参数（行号）
		xmlMap.put("BANK_CODE", !StringUtil.isEmpty(bankNo) ? bankNo : "");
		// 查询参数（行名）
		xmlMap.put("BANK_NAME", !StringUtil.isEmpty(bankName) ? bankName : "");
		// 是否模糊查询 0:不是 1:是
		xmlMap.put("IS_VAGUE", "1");
		// 页码条数
		xmlMap.put("PAGE_SIZE", pager.getRowsPerPage());
		// 查询页码
		xmlMap.put("PAGE_NUMBER", pager.getCurrentPage());

		Map<String, Object> reMap = BcsClient.getMapFromBCS("UPP3009", bankLog.getId(), xmlMap, bankLog);
		// String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		bankLog.setErrorMessage(ReturnMessage);
		bankLog.setType("UPP3009");
		List<Map<String, Object>> returnList = null;
		if (reMap.containsKey("List")) {
			returnList = (List<Map<String, Object>>) reMap.get("List");
		}
		return returnList;
	}

	/**
	 * 跨行出金手续费查询
	 * 
	 * @param bankNo
	 * @param bankName
	 * @param pager
	 * @return
	 */
	public static Double getCharge(BigDecimal price, String storeId, AccountBankLog bankLog) {
		Map<String, Object> xmlMap = new HashMap<String, Object>();

		// 提现金额
		xmlMap.put("AMT", new java.text.DecimalFormat("#.00").format(price));
		// 付款方商户编号
		xmlMap.put("MCH_NO", Cache.getBCSResource("MCH_NO"));
		// 付款方席位号
		xmlMap.put("SIT_NO", storeId);
		// 币别 目前只支持：CNY-人民币
		xmlMap.put("CURR_COD", "01");

		Map<String, Object> reMap = BcsClient.getMapFromBCS("FMSPAY0001", bankLog.getId(), xmlMap, bankLog);
		// String ReturnCode = reMap.get("ReturnCode").toString();

		String ReturnCode = reMap.get("ReturnCode").toString();
		String ReturnMessage = reMap.get("ReturnMessage").toString();
		Double charge = 0.00;
		bankLog.setErrorMessage(ReturnMessage);
		bankLog.setType("FMSPAY0001");
		try {
			if ("00000000".equals(ReturnCode) && reMap.containsKey("TOTALAMT")) {
				String TOTALAMT = reMap.get("TOTALAMT").toString();
				if (!StringUtil.isEmpty(TOTALAMT)) {
					charge = Double.parseDouble(TOTALAMT);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return charge;
	}

}
