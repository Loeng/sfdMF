package com.ekfans.plugin.webService.bcs.service;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.ekfans.base.store.dao.IStoreDao;
import com.ekfans.base.store.model.AccountBankLog;
import com.ekfans.base.store.model.AccountLog;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccountBankLogService;
import com.ekfans.base.store.service.IAccountLogService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.webService.bcs.BcsHelper;
import com.ekfans.plugin.webService.bcs.BcsSignHelper;
import com.ekfans.plugin.webService.bcs.BcsXmlHelper;
import com.ekfans.pub.util.StringUtil;

public class BcsServiceHelper {

	public static String getReturnStr(String paramStr) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (StringUtil.isEmpty(paramStr)) {
			System.out.println("no param");
			return BcsXmlHelper.mapToServiceXml(paramMap);
		}

		try {
			IAccountBankLogService bankLogService = SpringContextHolder.getBean(IAccountBankLogService.class);
			// 创建一个新的字符串
			StringReader read = new StringReader(paramStr);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			SAXBuilder builder = new SAXBuilder();
			Document docum = builder.build(source);
			Element serviceElement = docum.getRootElement();
			Element headerElement = serviceElement.getChild("Header");

			// Element bodyElement = serviceElement.getChild("Body");
			// /验签----
			Boolean checkStatus = false;
			String encrypt = Cache.getBCSResource("Encrypt");
			if ("1".equals(encrypt.trim())) {
				// /验签----
				// checkStatus =
				// BcsSignHelper.checkSign(headerElement.getAttributeValue("SignData"),
				// bodyElement.toString());
				String bodyStr = paramStr.substring(paramStr.indexOf("<Body>"), paramStr.indexOf("</Body>") + 7);
				checkStatus = BcsSignHelper.checkSign(headerElement.getChildText("SignData"), bodyStr);

			}
			String ServiceCode = headerElement.getChildText("ServiceCode");

			paramMap.put("ServiceCode", ServiceCode);

			AccountBankLog bankLog = new AccountBankLog();
			bankLog.setType("FMSCUST0005");
			bankLog.setMessage(paramStr);
			bankLogService.addLog(bankLog, null);
			paramMap.put("ExternalReference", bankLog.getId());

			if ("1".equals(encrypt.trim()) && !checkStatus) {
				paramMap.put("ReturnCode", "9999");
				paramMap.put("ReturnMessage", "签名验证错误");
				return BcsXmlHelper.mapToServiceXml(paramMap);
			}

			String rXml = null;
			if ("FMSCUST0005".equals(ServiceCode)) {
				rXml = FMSCUST0005(docum, paramMap, bankLog);
			} else if ("FMSPAY0005".equals(ServiceCode)) {
				rXml = FMSPAY0005(docum, paramMap, bankLog);
			}
			return rXml;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return BcsXmlHelper.mapToServiceXml(paramMap);
	}

	/**
	 * 客户签约通知
	 * 
	 * @param paramXml
	 */
	public static String FMSCUST0005(Document dom, Map<String, Object> paramMap, AccountBankLog bankLog) {
		String errorMessage = "";
		Store store = null;
		// 商户编号
		String MCH_NO = "";
		// 客户席位号
		String SIT_NO = "";

		// 签约时间
		String ACT_TIME = "";

		Map<String, Object> rMap = new HashMap<String, Object>();
		try {
			Element serviceElement = dom.getRootElement();

			Element bodyElement = serviceElement.getChild("Body");
			Element requestElement = bodyElement.getChild("Request");
			// 商户编号
			MCH_NO = requestElement.getChildText("MCH_NO");
			// 客户席位号
			SIT_NO = requestElement.getChildText("SIT_NO");
			// 签约时间
			ACT_TIME = requestElement.getChildText("ACT_TIME");
			// 客户虚拟账号
			String ACCOUNT_NO = requestElement.getChildText("ACCOUNT_NO");
			// 调用帮助类校验传过来的信息并获取店铺信息
			Object[] obj = BcsHelper.checkBcsInfo(store, MCH_NO, SIT_NO, ACCOUNT_NO);
			if (obj != null && obj.length > 0) {
				store = (Store) obj[0];
				errorMessage = (String) obj[1];
			}
		} catch (Exception e) {
		}

		// 如果店铺为空，并且错误信息不为空，则保存日志并结束该次访问
		if (store == null) {
			rMap.put("ReturnMessage", errorMessage);
			rMap.put("ReturnCode", "99999999");

			return BcsXmlHelper.mapToServiceXml(rMap);
		}

		// 创建事务处理机制
		Session session = null;
		Transaction transaction = null;
		try {
			IStoreDao storeDao = SpringContextHolder.getBean(IStoreDao.class);
			IAccountBankLogService bankLogService = SpringContextHolder.getBean(IAccountBankLogService.class);
			// 开始事务处理
			session = storeDao.createSession();
			transaction = session.beginTransaction();
			// 设置签约时间
			store.setAccountDate(ACT_TIME);
			// 将状态设置成已签约
			store.setAccountStatus(true);
			store.setAccountSuccess(true);
			// 更新店铺表
			storeDao.updateBean(store, session);

			// 设置接口编码
			bankLog.setReMessage(BcsXmlHelper.mapToServiceXml(paramMap));
			// 调用Service方法保存日志
			bankLogService.updateLog(bankLog, session);

			// 提交事物
			session.flush();
			transaction.commit();
			session.close();
			paramMap.put("ReturnCode", "00000000");
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

		return BcsXmlHelper.mapToServiceXml(paramMap);
	}

	// public static void main(String[] args) {
	// String reXmlStr =
	// "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Service><Header><aa>111</aa><bb>22</bb></Header><Body><Request><MCH_NO>商户编号</MCH_NO><SIT_NO>客户席位号</SIT_NO><ACT_TIME>签约时间</ACT_TIME><ACCOUNT_NO>客户虚拟账号</ACCOUNT_NO></Request></Body></Service>";
	// IBcsService bcsService = new BcsService();
	// bcsService.FMSCUST0005(reXmlStr);
	// }

	/**
	 * 入金交易推送
	 * 
	 * @param paramXml
	 * @return
	 */
	public static String FMSPAY0005(Document dom, Map<String, Object> paramMap, AccountBankLog bankLog) {

		Store store = null;
		// 商户编号
		String MCH_NO = "";
		// 客户席位号
		String SIT_NO = "";
		// 交易金额
		String TRANS_AMT = "";
		// 交易流水号
		String FMS_TRANS_NO = "";
		// 原市场入金交易流水号
		String ORI_MCH_TRANS_NO = "";
		// 交易完成时间
		String TRANS_TIME = "";
		try {
			Element serviceElement = dom.getRootElement();

			Element bodyElement = serviceElement.getChild("Body");
			Element requestElement = bodyElement.getChild("Request");
			// 商户编号
			MCH_NO = requestElement.getChildText("MCH_NO");
			// 客户席位号
			SIT_NO = requestElement.getChildText("SIT_NO");
			// 交易金额
			TRANS_AMT = requestElement.getChildText("TRANS_AMT");
			// 交易流水号
			FMS_TRANS_NO = requestElement.getChildText("FMS_TRANS_NO");
			// 原市场入金交易流水号
			ORI_MCH_TRANS_NO = requestElement.getChildText("ORI_MCH_TRANS_NO");
			// 交易完成时间
			TRANS_TIME = requestElement.getChildText("TRANS_TIME");
			IStoreDao storeDao = SpringContextHolder.getBean(IStoreDao.class);
			// 调用帮助类校验传过来的信息并获取店铺信息
			store = (Store) storeDao.get(SIT_NO);
		} catch (Exception e) {
		}

		// 如果店铺为空，并且错误信息不为空，则保存日志并结束该次访问
		if (store == null) {
			return BcsXmlHelper.mapToServiceXml(paramMap);
		}

		// 创建事务处理机制
		Session session = null;
		Transaction transaction = null;
		try {
			IStoreDao storeDao = SpringContextHolder.getBean(IStoreDao.class);
			IAccountBankLogService bankLogService = SpringContextHolder.getBean(IAccountBankLogService.class);
			IAccountLogService accountLogService = SpringContextHolder.getBean(IAccountLogService.class);
			// 开始事务处理
			session = storeDao.createSession();
			transaction = session.beginTransaction();
			store.setAccount(store.getAccount().add(new BigDecimal(TRANS_AMT)));
			// 更新店铺表
			storeDao.updateBean(store, session);

			AccountLog accLog = new AccountLog();
			accLog.setUserId(store.getId());
			accLog.setFmsTransNo(FMS_TRANS_NO);
			accLog.setOriMchTransNo(ORI_MCH_TRANS_NO);
			accLog.setPrice(new BigDecimal(TRANS_AMT));
			accLog.setCreateTime(TRANS_TIME);
			accLog.setStatus("1");
			accountLogService.addLog(accLog, session);

			paramMap.put("FMS_TRANS_NO", FMS_TRANS_NO);
			paramMap.put("MCH_TRANS_NO", accLog.getId());

			bankLog.setReMessage(BcsXmlHelper.mapToServiceXml(paramMap));
			bankLogService.updateLog(bankLog, session);

			// 提交事物
			session.flush();
			transaction.commit();
			session.close();

			return BcsXmlHelper.mapToServiceXml(paramMap);
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return BcsXmlHelper.mapToServiceXml(paramMap);
	}
}
