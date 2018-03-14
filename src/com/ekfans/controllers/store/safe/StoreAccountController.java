package com.ekfans.controllers.store.safe;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.dao.IAccountBankLogDao;
import com.ekfans.base.store.dao.IAccountLogDao;
import com.ekfans.base.store.model.AccountBank;
import com.ekfans.base.store.model.AccountBankLog;
import com.ekfans.base.store.model.AccountLog;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccountBankService;
import com.ekfans.base.store.service.IAccountLogService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.dao.IBankBindingDao;
import com.ekfans.base.user.dao.IBankDao;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.Bank;
import com.ekfans.base.user.model.BankBinding;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IBankBinDingLogService;
import com.ekfans.base.user.service.IBankBindingService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.message.service.UserMessageService;
import com.ekfans.plugin.webService.bcs.BCSClientService;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.PasswordStrengthUtil;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * 
 * @ClassName: StoreSafeController
 * @Description: TODO(账户中心管理Controller)
 * @author Zjin
 * @date 2014-11-13 上午10:40:26
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */

@Controller
@SuppressWarnings("unchecked")
public class StoreAccountController extends BasicController {
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IBankDao bankDao;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IBankBindingService bankBindingService;
	@Autowired
	private IBankBindingDao bankdingDao;
	@Autowired
	private IBankBinDingLogService bankdingLogService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAccountBankService accountBankService;
	@Autowired
	private IAccountLogService accountLogService;

	/**
	 * @Title: safelist
	 * @Description: TODO(企业账户中心跳转) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param session
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/accountmanager/list")
	public String safelist(HttpServletRequest request, HttpSession session) {
		// 从登陆中获取用户store
		Store store = (Store) session.getAttribute((String) SystemConst.STORE);
		// 从登陆中获取用户user
		User user = (User) session.getAttribute((String) SystemConst.USER);
		request.setAttribute("store", store);
		user = userService.getUser(user.getId());
		request.setAttribute("user", user);

		return "/userCenter/store/account/account_setting";
	}

	@RequestMapping(value = "/store/account/checkpassword", method = RequestMethod.POST)
	@ResponseBody
	public String checkPassword(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SystemConst.USER);
		// 根据userid更改数据库密码
		try {
			user = (User) userDao.get(user.getId());
			String pass = request.getParameter("password");
			String newPass = request.getParameter("newPassword");
			int passStreng = PasswordStrengthUtil.getInstance().strength(newPass);
			newPass = new MD5Util().getMD5ofStr(newPass);
			pass = new MD5Util().getMD5ofStr(pass);
			if (pass.equals(user.getPassword())) {
				user.setPassword(newPass);
				if (passStreng >= 3) {
					user.setPasswordStrong("3");
				} else {
					user.setPasswordStrong(String.valueOf(passStreng));
				}
				userDao.updateBean(user);
				session.removeAttribute(SystemConst.USER);
				session.setAttribute(SystemConst.USER, user);
				return "1";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	/**
	 * @Title: EmailValidate
	 * @Description: TODO(用户点击邮箱链接激活邮箱) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "web/userCenter/emailValidate")
	public String EmailValidate(HttpServletRequest request) {
		// 获取激活码
		String verificationCode = request.getParameter("code");
		// 获取时间
		String time = request.getParameter("time");
		// 获取用户ID
		String id = request.getParameter("userName");
		// 获取用户的Email
		String Email = request.getParameter("userEmail");
		// 获取账户的类型
		// String type = request.getParameter("type");
		DateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 获取邮箱中的时间
			Date old_Date = fmtDateTime.parse(time);
			// 获取当前时间
			Calendar cal = Calendar.getInstance();
			cal.setTime(old_Date);
			// 邮箱时间+1天
			cal.add(Calendar.DATE, 1);
			// 如果邮箱时间+1天小于当前时间则过期
			if (new Date().compareTo(cal.getTime()) > 0) {
				// 返回到过期页面
				request.setAttribute("mark", 2);
			}
			User user = (User) userDao.get(id);
			if (user.getEmailValiDate().equals(Email) && user.getVerificationCode().equals(verificationCode)) {
				user.setEmail(Email);
				user.setEmailValiDate("");
				userDao.updateBean(user);
				request.setAttribute("mark", 1);
				request.setAttribute("name", Email);
				request.setAttribute("jud", 1);
			} else {
				request.setAttribute("mark", 3);
				request.setAttribute("jud", 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/web/purchase/reg/storeReg_three";

	}

	/**
	 * @Title: checkEmail
	 * @Description: TODO(验证前台邮箱) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param session
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/checkEmail", method = RequestMethod.POST)
	@ResponseBody
	public String checkEmail(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SystemConst.USER);
		// 根据userid更改数据库密码
		try {
			user = (User) userDao.get(user.getId());
			String email = request.getParameter("email");
			String newemail = request.getParameter("newEmail");
			// 判断邮箱是否为空
			if (email != null && email != "") {
				// 判断输入邮箱是否相同
				if (!email.equals(user.getEmail())) {
					return "0";
				} else {
					user.setEmailValiDate(newemail);
				}
			} else {
				user.setEmailValiDate(newemail);
			}
			String code = new MD5Util().getMD5ofStr(user.getName() + SystemConst.DESKEY);
			user.setVerificationCode(code);
			userDao.updateBean(user);
			// 发送链接到邮箱,
			user.setEmail(newemail);
			UserMessageService service = new UserMessageService(request, "userModifyVerifyEmail", user, code, "");
			service.run();
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}

	@RequestMapping(value = "/store/account/phoneyanzheng")
	@ResponseBody
	public Object phoneYZ(HttpServletRequest request, HttpSession session) {
		User user = (User) session.getAttribute(SystemConst.USER);
		String newPhone = request.getParameter("newPhone");
		Random r = new Random();
		String num = Integer.toString(r.nextInt(999999));
		num = "111111";
		UserMessageService service = new UserMessageService(request, "userModifyVerifyMobile", user, num, "");
		service.run();
		session.setAttribute(newPhone, num);
		return "0";
	}

	@RequestMapping(value = "/store/account/phone")
	@ResponseBody
	public String updatePhone(HttpServletRequest request, HttpSession session) {
		try {
			// 从session获取登录用户
			User user = (User) session.getAttribute(SystemConst.USER);
			// 获取原手机号码
			String oldPhoen = request.getParameter("oldphone");
			// 获取新手机号码
			String newPhoen = request.getParameter("newphone");
			// 获取用户输入的验证码
			String yz = request.getParameter("yz");
			// 从session中获取发送手机号码的验证码
			String number = (String) session.getAttribute(newPhoen);
			// 验证码错误
			if (!yz.equals(number)) {
				return "2";
			} else {
				if (!StringUtil.isEmpty(oldPhoen)) {
					// 判断原手机号码是否相同
					if (user.getMobile().equals(oldPhoen)) {
						user.setMobile(newPhoen);
						userDao.updateBean(user);
						session.removeAttribute(newPhoen);
					} else {
						// 原手机号码错误
						return "1";
					}
				} else {
					user.setMobile(newPhoen);
					userDao.updateBean(user);
					session.removeAttribute(newPhoen);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "4";
		}
		return "0";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/add")
	public String addBankCard(HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer("from Bank");
		try {
			List<Bank> bank = bankDao.list(buffer.toString(), null);
			if (bank != null) {
				request.setAttribute("bank", bank);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/userCenter/store/account/bankCardAdd";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/view")
	public String bankView(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		AccountBank bank = accountBankService.getBanksByUserId(store.getId());

		request.setAttribute("store", store);
		request.setAttribute("bank", bank);
		return "/userCenter/store/account/accountBinding";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/saveorupdate")
	@ResponseBody
	public String accountSaveOrUpdate(AccountBank bank, HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		String orgId = request.getParameter("orgId");
		String storeName = request.getParameter("storeName");
		store.setOrgId(orgId);
		store.setStoreName(storeName);
		store.setAccountStatus(false);
		bank.setUserId(store.getId());
		bank.setCreateTime(DateUtil.getSysDateTimeString());
		bank.setStatus("2");
		bank.setEnableEcds(true);

		String bankName = bank.getBankName();
		if (bankName.indexOf("长沙银行股份有限公司") == -1) {
			store.setAccountSuccess(true);
		} else {
			store.setAccountSuccess(false);
		}

		if (accountBankService.saveOrUpdate(bank, store)) {
			request.setAttribute("store", store);
			request.setAttribute("bank", bank);
			return "1";
		} else {
			bank.setStatus("");
		}

		request.setAttribute("store", store);
		request.setAttribute("bank", bank);
		return "2";
	
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/unregedit")
	@ResponseBody
	public String unregedit(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());

		AccountBank bank = accountBankService.getBanksByUserId(store.getId());

		Boolean status = accountBankService.unRegidst(bank, request);
		if (status) {
			return "1";
		} else {
			return "0";
		}
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/getlistprice")
	@ResponseBody
	public String getlistprice(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		BigDecimal price = new BigDecimal(0.00);
		try {
			price = accountLogService.getAvlBal(store.getId());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return price.toString();
	}

	@RequestMapping(value = "/store/account/bank/load")
	public String loadBanks(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		String pageNo = request.getParameter("pageNo");
		String searchBankNo = request.getParameter("searchBankNo");
		String key = request.getParameter("key");

		if (!StringUtil.isEmpty(searchBankNo) || !StringUtil.isEmpty(key)) {
			int currentPageNo = 0;
			try {
				currentPageNo = Integer.parseInt(pageNo);
			} catch (Exception e) {
				currentPageNo = 0;
			}
			Pager pager = new Pager(currentPageNo);
			pager.setRowsPerPage(10);
			pager.setCurrentPage(currentPageNo);

			// IBankInfService bankInfService =
			// SpringContextHolder.getBean(IBankInfService.class);
			// List<BankInf> banks = bankInfService.getBankInfos(key, pager);
			List<Map<String, Object>> banks = accountLogService.getBankNo(store != null ? store.getId() : "", searchBankNo, key, pager);
			request.setAttribute("banks", banks);
			request.setAttribute("pager", pager);
			request.setAttribute("key", key);
			request.setAttribute("searchBankNo", searchBankNo);
		}
		return "/userCenter/store/account/banks";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/password")
	public String accountPassword(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());

		request.setAttribute("store", store);
		return "/userCenter/store/account/password";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/pswUpdate")
	public String suAccountPassword(HttpServletRequest request) {
		String storeId = request.getParameter("storeId");
		Store store = storeService.getStore(storeId);

		MD5Util m = new MD5Util();

		String password = request.getParameter("password");
		String rePassword = request.getParameter("rePassword");
		if (!StringUtil.isEmpty(store.getPayPassword())) {
			String oldPsw = request.getParameter("oldPassword");
			if (StringUtil.isEmpty(oldPsw) || !m.getMD5ofStr(oldPsw).equals(store.getPayPassword())) {
				request.setAttribute("store", store);
				request.setAttribute("eMessage", "原密码输入错误");
				request.setAttribute("success", false);
				return "/userCenter/store/account/password";
			}
		}

		if (StringUtil.isEmpty(password) || StringUtil.isEmpty(rePassword) || !password.equals(rePassword)) {
			request.setAttribute("store", store);
			request.setAttribute("eMessage", "两次输入的新密码不一致，请重新输入");
			request.setAttribute("success", false);
			return "/userCenter/store/account/password";
		}

		store.setPayPassword(m.getMD5ofStr(password));
		Boolean status = storeService.updateStoreOnly(store);
		request.setAttribute("success", status);
		if (!status) {
			request.setAttribute("eMessage", "保存失败，请稍后再试或联系管理员");
		}
		request.getSession().setAttribute(SystemConst.STORE, store);
		request.setAttribute("store", store);
		return "/userCenter/store/account/password";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/account")
	public String accountView(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());
		AccountBank account = accountBankService.getBanksByUserId(store.getId());
		BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
		if (prices == null || prices.length <= 0) {
			prices = new BigDecimal[2];
			prices[0] = new BigDecimal(0.00);
			prices[1] = new BigDecimal(0.00);
		}
		request.setAttribute("prices", prices);
		request.setAttribute("store", store);
		request.setAttribute("account", account);
		request.getSession().setAttribute(SystemConst.STORE, store);
		return "/userCenter/store/account/accountView";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/chongzhi")
	public String chongzhi(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());

		String price = request.getParameter("czPrice");
		String pwd = request.getParameter("czPwd");
		MD5Util md5Util = new MD5Util();
		if ((!StringUtil.isEmpty(store.getPayPassword()) && StringUtil.isEmpty(pwd))
				|| (!StringUtil.isEmpty(store.getPayPassword()) && !store.getPayPassword().equals(md5Util.getMD5ofStr(pwd)))) {
			request.setAttribute("subStatus", false);
			request.setAttribute("errorMessage", "支付/提现密码错误！");
			BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
			if (prices == null || prices.length <= 0) {
				prices = new BigDecimal[2];
				prices[0] = new BigDecimal(0.00);
				prices[1] = new BigDecimal(0.00);
			}
			request.setAttribute("prices", prices);
			request.setAttribute("store", store);
			request.getSession().setAttribute(SystemConst.STORE, store);
			return "/userCenter/store/account/accountView";
		}

		BigDecimal payPrice = new BigDecimal(0.00);
		try {
			payPrice = new BigDecimal(price);
		} catch (Exception e) {
			request.setAttribute("subStatus", false);
			request.setAttribute("errorMessage", "充值金额输入错误，请重新输入");
			BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
			if (prices == null || prices.length <= 0) {
				prices = new BigDecimal[2];
				prices[0] = new BigDecimal(0.00);
				prices[1] = new BigDecimal(0.00);
			}
			request.setAttribute("prices", prices);
			request.setAttribute("store", store);
			request.getSession().setAttribute(SystemConst.STORE, store);
			return "/userCenter/store/account/accountView";
		}

		Boolean status = accountLogService.sufficient(store.getId(), payPrice);
		if (status) {
			request.setAttribute("subStatus", true);
			request.setAttribute("errorMessage", "充值成功");
		} else {
			request.setAttribute("subStatus", false);
			request.setAttribute("errorMessage", "充值失败，请检查您银行账户余额");
		}
		BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
		if (prices == null || prices.length <= 0) {
			prices = new BigDecimal[2];
			prices[0] = new BigDecimal(0.00);
			prices[1] = new BigDecimal(0.00);
		}
		request.setAttribute("prices", prices);
		request.setAttribute("store", store);
		request.getSession().setAttribute(SystemConst.STORE, store);
		return "/userCenter/store/account/accountView";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/tixian")
	public String tixian(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		store = storeService.getStore(store.getId());

		String price = request.getParameter("txPrice");
		String chargePriceStr = request.getParameter("chargePrice");
		String pwd = request.getParameter("txPwd");
		MD5Util md5Util = new MD5Util();
		if ((!StringUtil.isEmpty(store.getPayPassword()) && StringUtil.isEmpty(pwd))
				|| (!StringUtil.isEmpty(store.getPayPassword()) && !store.getPayPassword().equals(md5Util.getMD5ofStr(pwd)))) {
			request.setAttribute("subStatus", false);
			request.setAttribute("errorMessage", "支付/提现密码错误！");
			BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
			if (prices == null || prices.length <= 0) {
				prices = new BigDecimal[2];
				prices[0] = new BigDecimal(0.00);
				prices[1] = new BigDecimal(0.00);
			}
			request.setAttribute("prices", prices);
			request.setAttribute("store", store);
			request.getSession().setAttribute(SystemConst.STORE, store);
			return "/userCenter/store/account/accountView";
		}

		BigDecimal payPrice = new BigDecimal(0.00);
		BigDecimal chargePrice = new BigDecimal(0.00);
		try {
			payPrice = new BigDecimal(price);
			chargePrice = new BigDecimal(chargePriceStr);
		} catch (Exception e) {
			request.setAttribute("subStatus", false);
			request.setAttribute("errorMessage", "提现金额输入错误，请重新输入");
			BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
			if (prices == null || prices.length <= 0) {
				prices = new BigDecimal[2];
				prices[0] = new BigDecimal(0.00);
				prices[1] = new BigDecimal(0.00);
			}
			request.setAttribute("prices", prices);
			request.setAttribute("store", store);
			request.getSession().setAttribute(SystemConst.STORE, store);
			return "/userCenter/store/account/accountView";
		}

		Boolean status = accountLogService.takeMoney(store.getId(), payPrice, chargePrice);
		if (status) {
			request.setAttribute("subStatus", true);
			request.setAttribute("errorMessage", "提现成功，资金将会在24小时内到账");
		} else {
			request.setAttribute("subStatus", false);
			request.setAttribute("errorMessage", "提现失败，请检查您银行账户余额");
		}
		BigDecimal[] prices = BCSClientService.getAvlBal(store.getId());
		if (prices == null || prices.length <= 0) {
			prices = new BigDecimal[2];
			prices[0] = new BigDecimal(0.00);
			prices[1] = new BigDecimal(0.00);
		}
		request.setAttribute("prices", prices);
		request.setAttribute("store", store);
		request.getSession().setAttribute(SystemConst.STORE, store);
		return "/userCenter/store/account/accountView";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/sxf")
	@ResponseBody
	public void getSXF(HttpServletRequest request, HttpServletResponse response) {
		String priceStr = request.getParameter("price");
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);
		BigDecimal price = new BigDecimal(0.00);
		try {
			price = new BigDecimal(priceStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Double charge = accountLogService.getCharge(price, store != null ? store.getId() : "");
		backWriteStr(response, charge + "");
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/loglist")
	public String accountListView(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pageNo = request.getParameter("pageNo");

		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		// request.setAttribute("store", store);

		int currentPageNo = 1;
		try {
			currentPageNo = Integer.parseInt(pageNo);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(15);

		IAccountLogDao accountLogDao = SpringContextHolder.getBean(IAccountLogDao.class);
		IAccountBankLogDao bankLogDao = SpringContextHolder.getBean(IAccountBankLogDao.class);

		AccountLog accountLog = new AccountLog();
		try {
			accountLog.setUserId(store.getId());
			accountLog.setCreateTime(DateUtil.getSysDateTimeString());
			accountLogDao.addBean(accountLog);
		} catch (Exception e) {
			// TODO: handle exception
		}

		AccountBankLog bankLog = new AccountBankLog();
		bankLog.setUserId(store.getId());
		bankLog.setCreateTime(DateUtil.getSysDateTimeString());
		try {
			bankLogDao.addBean(bankLog);
		} catch (Exception e) {
			// TODO: handle exception
		}

		Map<String, Object> logMap = BCSClientService.getAccountInfo(store.getId(), startTime, endTime, currentPageNo + "", "15", bankLog, accountLog);

		if (logMap != null && logMap.size() > 0) {
			try {
				pager.setTotalPage(Integer.parseInt((String) logMap.get("PAGE_COUNT")));
				pager.setTotalRow(Integer.parseInt((String) logMap.get("TOTAL")));
			} catch (Exception e) {
				// TODO: handle exception
			}
			List<Map<String, Object>> logList = (List<Map<String, Object>>) logMap.get("List");
			request.setAttribute("logList", logList);
		}
		request.setAttribute("pager", pager);
		Session session = null;
		Transaction transaction = null;
		try {
			session = accountLogDao.createSession();
			transaction = session.beginTransaction();

			accountLogDao.updateBean(accountLog, session);
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
		return "/userCenter/store/account/accountLogList";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/exportlist")
	public String accountListExport(HttpServletRequest request) {
		Store store = (Store) request.getSession().getAttribute(SystemConst.STORE);

		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String pageNo = request.getParameter("pageNo");

		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		// request.setAttribute("store", store);

		int currentPageNo = 1;
		try {
			currentPageNo = Integer.parseInt(pageNo);
		} catch (Exception e) {
			// TODO: handle exception
		}
		Pager pager = new Pager(currentPageNo);
		pager.setRowsPerPage(15);

		IAccountLogDao accountLogDao = SpringContextHolder.getBean(IAccountLogDao.class);
		IAccountBankLogDao bankLogDao = SpringContextHolder.getBean(IAccountBankLogDao.class);

		AccountLog accountLog = new AccountLog();
		try {
			accountLog.setUserId(store.getId());
			accountLog.setCreateTime(DateUtil.getSysDateTimeString());
			accountLogDao.addBean(accountLog);
		} catch (Exception e) {
			// TODO: handle exception
		}

		AccountBankLog bankLog = new AccountBankLog();
		bankLog.setCreateTime(DateUtil.getSysDateTimeString());
		bankLog.setUserId(store.getId());
		Map<String, Object> logMap = BCSClientService.getAccountInfo(store.getId(), startTime, endTime, currentPageNo + "", "15", bankLog, accountLog);

		if (logMap != null && logMap.size() > 0) {
			try {
				pager.setTotalPage(Integer.parseInt((String) logMap.get("PAGE_COUNT")));
				pager.setTotalRow(Integer.parseInt((String) logMap.get("TOTAL")));
			} catch (Exception e) {
				// TODO: handle exception
			}
			List<Map<String, Object>> logList = (List<Map<String, Object>>) logMap.get("List");
			request.setAttribute("logList", logList);
		}
		request.setAttribute("pager", pager);
		Session session = null;
		Transaction transaction = null;
		try {
			session = accountLogDao.createSession();
			transaction = session.beginTransaction();

			accountLogDao.updateBean(accountLog, session);
			bankLogDao.addBean(bankLog, session);

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
		return "/userCenter/store/account/accountView";
	}

	/**
	 * @Title: addBankCard
	 * @Description: TODO(跳转到新增银行页面) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/ajaxadd")
	public String ajaxAddBankCard(HttpServletRequest request) {
		StringBuffer buffer = new StringBuffer("from Bank");
		try {
			List<Bank> bank = bankDao.list(buffer.toString(), null);
			if (bank != null) {
				request.setAttribute("banks", bank);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/userCenter/store/wfpOrder/bankCardAddLoad";
	}

	/**
	 * @Title: updateBankCard
	 * @Description: TODO(修改银行卡跳转) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param request
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bank/update/{id}")
	public String updateBankCard(@PathVariable String id, HttpServletRequest request) {
		try {
			// 根据ID查询绑定的银行
			BankBinding bing = (BankBinding) bankdingDao.get(id);
			StringBuffer buffer = new StringBuffer("from Bank");
			List<Bank> bank = bankDao.list(buffer.toString(), null);
			if (bank != null) {
				request.setAttribute("bank", bank);
			}
			request.setAttribute("bankbing", bing);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/userCenter/store/account/bankCardUpdate";
	}

	/**
	 * @Title: deleteBankCard
	 * @Description: TODO(删除银行卡) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/deleteBnak/{id}/{count}")
	public String deleteBankCard(@PathVariable String id, @PathVariable int count, HttpSession session) {
		try {
			// 从缓存中获取User
			User user = (User) session.getAttribute(SystemConst.USER);
			// 调用删除方法
			bankBindingService.deletBank(id, count, user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/store/account/bank/list";
	}

	@RequestMapping(value = "/store/account/bankupdate")
	@ResponseBody
	public Object updateBankding(HttpServletRequest request, BankBinding bank) {
		try {
			if (bank == null)
				return "2";
			bankBindingService.updateBank(bank);
		} catch (Exception e) {
			e.printStackTrace();
			return "2";
		}
		return "1";
	}

	/**
	 * @Title: checkEmail
	 * @Description: TODO(新增银行卡) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param request
	 * @param session
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/store/account/bankAdd")
	@ResponseBody
	public Object card(HttpServletRequest request, HttpSession session, BankBinding bank) {
		User user = (User) session.getAttribute(SystemConst.USER);
		try {
			user = (User) userDao.get(user.getId());
			bank.setStoreId(user.getId());
			bank.setCreateTime(DateUtil.getSysDateTimeString());
			bank.setStatus(true);
			if (bank != null) {
				if (bankBindingService.CheckBankBindIng(bank)) {
					bankBindingService.addBank(bank);
					// 重新放入session
					session.setAttribute(SystemConst.USER, user);
					return "1";
				} else {
					return "3";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "2";
	}

}
