package com.ekfans.plugin.wftong.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;

import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccreditService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.store.util.AccreditHelper;
import com.ekfans.base.user.model.Account;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IAccountService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.service.AppUserCacheService;
import com.ekfans.plugin.cache.service.IAppUserCacheService;
import com.ekfans.plugin.message.service.UserMessageService;
import com.ekfans.plugin.wftong.AppServerController;
import com.ekfans.plugin.wftong.base.friend.service.IFriendRelationService;
import com.ekfans.plugin.wftong.base.log.model.WftLoginLog;
import com.ekfans.plugin.wftong.base.log.service.IWftLoginLogService;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.plugin.wftong.util.HxUtil;
import com.ekfans.plugin.wftong.util.MyJSONObject;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.EncDec.MD5Util;

/**
 * appUser Controller
 * 
 * @Title: AppUserController
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年4月12日11:05:21
 * @version 1.0
 */
public class AppUserController {
	// 响应
	private MyJSONObject jsonResponse;
	// 请求主体
	private MyJSONObject jsonRequest;
	// request
	private HttpServletRequest request;
	// 当前用户对象
	private AppUser appUser;

	private IUserService userService = SpringContextHolder.getBean(IUserService.class);
	private IAccountService accountService = SpringContextHolder.getBean(IAccountService.class);
	private IWftLoginLogService wftLoginLogService = SpringContextHolder.getBean(IWftLoginLogService.class);
	private IFriendRelationService friendService = SpringContextHolder.getBean(IFriendRelationService.class);
	private IAccreditService accreditService = SpringContextHolder.getBean(IAccreditService.class);
	private IStoreService storeService = SpringContextHolder.getBean(IStoreService.class);

	/**
	 * 构造
	 * @param jsonRequest 请求主体，用于获取请求参数
	 * @param jsonResponse 响应主体，用于返回结果数据
	 * @param appUser 当前用户
	 * @param request 
	 */
	public AppUserController(MyJSONObject jsonRequest, MyJSONObject jsonResponse, AppUser appUser, HttpServletRequest request) {
		this.jsonRequest = jsonRequest;
		this.jsonResponse = jsonResponse;
		this.appUser = appUser;
		this.request = request;
	}

	/**
	 * 登录
	 * @throws JSONException
	 */
	public void login() throws Exception {
		String userName = jsonRequest.getString("userName");
		String passWord = jsonRequest.getString("passWord");
		String longitudes = jsonRequest.getString("longitudes");
		String phoneNo = jsonRequest.getString("phoneNo");

		// 基本验证
		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(passWord)) {
			// 返回消息
			jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.login.noinput"));
			return;
		}

		// 尝试登录
		User user = userService.getUserByName(userName);
		Account account = null;

		// 如果用户对象为空，则查找子账户
		if (user == null) {
			// 调用Service根据用户名查询对象
			account = accountService.getAccountLoginByName(userName);
		}
		if (user == null && account == null) {
			jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.login.noName"));
			return;
		} else {
			String realPwd = ""; // 真实密码
			String inputPwd = new MD5Util().getMD5ofStr(passWord); // 用户输入的密码

			if (user == null) {
				realPwd = account.getPassword();
				user = userService.getUser(account.getStoreId());
			} else {
				realPwd = user.getPassword();
			}

			if (user.getVerificationStatus()) {
				if (user.getStatus() != 1) {
					// 未被启用
					jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.login.nouse"));
					return;
				}
				if (realPwd.equals(inputPwd)) {
					String appUserId = null;
					if (account != null) {
						// 子账号登录
						appUserId = account.getId();
					} else {
						// 账号登录
						appUserId = user.getId();
					}

					// 构造AppUser模拟对象，整合账号信息，用于传输和缓存
					AppUser appUser = userService.getAppUserById(appUserId);

					if (appUser != null) {
						// 检查hx,如果环信id为空，检查所有用户并更新
						if (StringUtil.isEmpty(appUser.getHxUserName())) {
							List<AppUser> appUsers = userService.getAllAppUsersNoRegHx();

							// 创建环信账户
							HxUtil.createNewIMUserBatchGen(30L, Long.valueOf(appUsers.size()), appUsers);
						}
						appUser = userService.getAppUserById(appUserId);
						if (StringUtil.isEmpty(appUser.getHxUserName())) {
							jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.login.HxError"));
							return;
						}

						// 保存登录日志
						WftLoginLog loginLog = new WftLoginLog();
						loginLog.setCreateTime(DateUtil.getSysDateTimeString());
						loginLog.setType("1");
						loginLog.setUserId(user.getId());
						loginLog.setAddress(longitudes);
						loginLog.setDeviceModel(phoneNo);
						wftLoginLogService.save(loginLog);

						// 将用户登录信息进行缓存
						IAppUserCacheService service = new AppUserCacheService();
						service.addOrUpdAppUserByToken(appUser.getToken(), appUser);

						// 绑定数据,重新new一个将所有对象数据传入
						jsonResponse.put(AppServerController.STATUS_RETURN, "1");
						jsonResponse.putBean(appUser);
					} else {
						jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.login.typeError"));
						return;
					}
				} else {
					jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.login.pwdError"));
					return;
				}
			} else {
				jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.login.noactiv"));
				return;
			}
		}
	}

	/**
	 * 根据ID获取用户信息
	 */
	public void getUserInfo() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "searchId", "type");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();
		String searchId = jsonRequest.getString("searchId");
		String type = jsonRequest.getString("type");

		AppUser appUser = null;
		switch (type) {
		case "hx":
			appUser = userService.getAppUserByHxId(searchId);
			break;
		case "user":
			appUser = userService.getAppUserById(searchId);
			break;

		default:
			break;
		}
		if (appUser != null) {
			// 查询是否为好友关系
			appUser.setFriendStatus(friendService.getFriendStatus(userId, searchId));

			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.putBean(appUser);
		}
	}

	public void getZz() throws Exception {
		String storeId = this.appUser.getStoreId();

		List<Accredit> list = new ArrayList<>();
		Accredit accreditWf = accreditService.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_WF);
		Accredit accreditPw = accreditService.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_PW);
		Accredit accreditYs = accreditService.getAccreditByStoreAndType(storeId, AccreditHelper.ACC_RZTYPE_YS);

		if (accreditPw != null) {
			list.add(accreditPw);
		}
		if (accreditYs != null) {
			list.add(accreditYs);
		}
		if (accreditWf != null) {
			list.add(accreditWf);
		}

		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
	}

	public void updateStoreInfo() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "storeName");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String storeName = jsonRequest.getString("storeName");
		String notes = jsonRequest.getString("notes");
		String contactName = jsonRequest.getString("contactName");
		String contactPhone = jsonRequest.getString("contactPhone");

		String storeId = this.appUser.getStoreId();
		Store store = storeService.getStore(storeId);

		// 更新状态 0失败，1成功，2已认证成功无法修改
		jsonResponse.put("updataStatus", "0");

		// 认证成功后不能再修改
		if (store != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			if (!"3".equals(store.getCommonStatus())) {
				store.setStoreName(storeName);
				store.setNotes(notes);
				store.setContactName(contactName);
				store.setContactPhone(contactPhone);

				if (storeService.update(store)) {
					// 覆盖状态
					jsonResponse.put("updataStatus", "1");
				}
			} else {
				// 覆盖状态
				jsonResponse.put("updataStatus", "2");
			}
		}
	}

	public void getStoreBankInfo() throws Exception {

		String storeId = this.appUser.getStoreId();
		Store store = storeService.getStore(storeId);

		// 银行认证（ 0：未认证，1：认证中，2：认证失败，3：认证成功）
		String bankStatus = "0";
		if (store != null && !StringUtil.isEmpty(store.getBankStatus())) {
			bankStatus = store.getBankStatus();
		}
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		jsonResponse.put("bankStatus", bankStatus);
	}

	public void sendPhoneYan() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "phone", "type");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String phone = jsonRequest.getString("phone");
		String type = jsonRequest.getString("type");

		Random r = new Random();
		String verifyCode = Integer.toString(r.nextInt(999999));

		// 构造一个空对象
		User user = new User();
		user.setMobile(phone);

		UserMessageService service = null;
		switch (type) {
			case "reg":
				service = new UserMessageService(request, "userRegisteVerifyMobile", user, verifyCode, "");
				break;
			case "repwd":
				service = new UserMessageService(request, "userPwdResetMobile", userService.getUserByName(phone), verifyCode, "");
				break;
			default:
				break;
		}
		service.run();

		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		jsonResponse.put("verifyCode", verifyCode);
	}

	/*
	 * 更换密码 1:成功，2:用户不存在，3:失败
	 */
	public void unsetPwd() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "name", "password");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 获取电话号码
		String name = jsonRequest.getString("name");
		// 获取需更新的密码
		String password = jsonRequest.getString("password");

		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		// 默认
		jsonResponse.put("updateStatus", "3");

		boolean success = false;

		// 根据用户名查找用户
		User user = userService.getUserName(name, null);
		Account account = null;
		if (user == null) {
			account = accountService.getAccountLoginByName(name);
			if (account != null) {
				account.setPassword(new MD5Util().getMD5ofStr(password));
				success = accountService.updateAccount(account);
			}
		} else {
			user.setPassword(new MD5Util().getMD5ofStr(password));
			success = userService.updateUser(user);
		}

		if (user == null && account == null) {
			jsonResponse.put("updateStatus", "2");
			return;
		}
		if (success) {
			jsonResponse.put("updateStatus", "1");
			return;
		}

	}

	/*
	 * 修改密码1:成功，2:密码错误，3:失败
	 */
	public void updatePwd() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "password", "oldpassword");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 原密码
		String oldpassword = jsonRequest.getString("oldpassword");
		// 获取需更新的密码
		String password = jsonRequest.getString("password");

		// md5
		String oldPasswordmd5 = new MD5Util().getMD5ofStr(oldpassword);
		String passwordmd5 = new MD5Util().getMD5ofStr(password);

		AppUser appUser = this.appUser;
		String status = "3";
		boolean success = false;

		// 根据用户名查找用户
		User user = userService.getUserName(appUser.getName(), null);
		Account account;
		if (user != null) {
			// 更新账号
			if (user.getPassword().equals(oldPasswordmd5)) {
				user.setPassword(passwordmd5);
				success = userService.updateUser(user);
			} else {
				status = "2";
			}
		} else {
			// 更新子账号
			account = accountService.getAccountLoginByName(appUser.getName());
			if (account != null) {
				if (account.getPassword().equals(oldPasswordmd5)) {
					account.setPassword(passwordmd5);
					success = accountService.updateAccount(account);
				} else {
					status = "2";
				}
			}
		}

		if (success) {
			status = "1";
		}

		jsonResponse.put("updateStatus", status);
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");

	}

	public void getServerPhone() throws Exception {
		String phone = Cache.getSystemContentConfig("热线电话");
		jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		jsonResponse.put("serverPhone", phone);
	}

	public void appUserReg() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "username", "password");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// 帐号
		String username = jsonRequest.getString("username");
		// 密码
		String password = jsonRequest.getString("password");

		// 验证用户名
		if(userService.checkUserName(username)) {
			jsonResponse.put(AppServerController.MSG_RETURN, "该帐号已存在！");
			return;
		}

		if (userService.mobileReg(username, password, request)) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}


	}
}
