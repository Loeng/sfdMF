package com.ekfans.plugin.message.util;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.EncryptionAndDecryption;
import com.ekfans.pub.util.StringUtil;

/**
 * 消息发送帮助类
 * 
 * @ClassName: MessageHelper
 * @author 成都易科远见科技有限公司
 * @date 2014-5-31 上午11:18:21
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class MessageHelper {

	/**
	 * 将消息正文中的占位符替换成相应的值
	 * 
	 * @Title: contentMessageStr
	 * @Description: 将消息正文中的站位符替换成相应的值
	 * @param @param message 消息正文
	 * @param @param user 会员对象
	 * @param @param product 商品对象
	 * @param @param order 订单对象
	 * @param @param verifyCode 验证码
	 * @return void 返回类型
	 * @throws
	 */
	public static String contentMessageStr(String message, User user, Product product, String verifyCode, String judgment, HttpServletRequest 	request) {

		// 如果keyMap为空或者长度小于或等于0，则返回空
		if (StringUtil.isEmpty(message)) {
			return null;
		}

		if (message.contains(MessageConst.MESSAGE_JUDGMENT)) {
			message = message.replaceAll(MessageConst.MESSAGE_JUDGMENT_REP, judgment);
		}

		// 替换正文中的商城访问路径
		if (message.contains(MessageConst.MESSAGE_URL)) {
			BasicRequest brequest = new BasicRequest(request);
			message = message.replaceAll(MessageConst.MESSAGE_URL_REP, brequest.getWebFullUrlPrex());
		}

		// 替换正文中的邮箱注册激活路径
		if (message.contains(MessageConst.MESSAGE_USER_REG_MAIL_ADDR)) {
			// 先获取文件访问的前缀
			BasicRequest brequest = new BasicRequest(request);
			String url = brequest.getWebFullUrlPrex();
			if (!url.endsWith("/")) {
				url = url + "/";
			}
			long currentNumber = new Date().getTime();
			String currentDatePwd = "11" + EncryptionAndDecryption.encrypt(SystemConst.DESKEY, String.valueOf(currentNumber)) + "11";
			// 拼写路径
			String returnUrl = "";
			Cookie[] cookies = request.getCookies();
			if (cookies != null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					Cookie cookie = cookies[i];
					if (cookie != null && "gylReturnUrl".equals(cookie.getName())) {
						returnUrl = cookie.getValue();
						if (!StringUtil.isEmpty(returnUrl)) {
							break;
						}
					}
				}
			}

			url = url + "web/reg/activation?type=" + user.getType() + "&tempcurrent=" + URLEncoder.encode(currentDatePwd) + "&name=" + user.getName() + "&returnUrl=" + returnUrl;

			message = message.replaceAll(MessageConst.MESSAGE_USER_REG_MAIL_ADDR_REP, url);
		}

		// 会员修改邮箱验证路径
		if (message.contains(MessageConst.MESSAGE_USER_MODIFY_MAIL_ADDR)) {
			// 先获取文件访问的前缀
			BasicRequest brequest = new BasicRequest(request);
			String url = brequest.getWebFullUrlPrex();
			if (!url.endsWith("/")) {
				url = url + "/";
			}
			DateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 拼写路径
			url = url + "web/userCenter/emailValidate?userName=" + user.getId() + "&userEmail=" + user.getEmailValiDate() + "&code=" + verifyCode + "&time=" + fmtDateTime.format(new Date());
			message = message.replaceAll(MessageConst.MESSAGE_USER_MODIFY_MAIL_ADDR_REP, url);
		}

		// 会员找回密码邮箱验证路径
		if (message.contains(MessageConst.MESSAGE_USER_FORGET_MAIL_ADDR)) {
			// 先获取文件访问的前缀
			BasicRequest brequest = new BasicRequest(request);
			String url = brequest.getWebFullUrlPrex();
			if (!url.endsWith("/")) {
				url = url + "/";
			}

			// 拼写路径
			url = url + "web/findpassword/jumpPasswordPageTwo?type=" + user.getType() + "&name=" + user.getName();
			message = message.replaceAll(MessageConst.MESSAGE_USER_FORGET_MAIL_ADDR_REP, url);
		}

		// 替换正文中的时间占位符
		if (message.contains(MessageConst.MESSAGE_DATE_TIME)) {
			message = message.replaceAll(MessageConst.MESSAGE_DATE_TIME_REP, DateUtil.getSysDateTimeString());
		}

		// 替换正文中的手机占位符
		if (message.contains(MessageConst.MESSAGE_USER_MOBILE)) {
			if (user != null) {
				message = message.replaceAll(MessageConst.MESSAGE_USER_MOBILE_REP, user.getMobile());
			} else {
				message = message.replaceAll(MessageConst.MESSAGE_USER_MOBILE_REP, "");
			}
		}

		// 替换正文中的用户名占位符
		if (message.contains(MessageConst.MESSAGE_USER_NAME)) {
			if (user != null) {
				message = message.replaceAll(MessageConst.MESSAGE_USER_NAME_REP, user.getName());
			} else {
				message = message.replaceAll(MessageConst.MESSAGE_USER_NAME_REP, "");
			}
		}

		// 替换正文中的用户昵称占位符
		if (message.contains(MessageConst.MESSAGE_USER_NICKNAME)) {
			if (user != null) {
				message = message.replaceAll(MessageConst.MESSAGE_USER_NICKNAME_REP, user.getNickName());
			} else {
				message = message.replaceAll(MessageConst.MESSAGE_USER_NICKNAME_REP, "");
			}
		}

		// 替换正文中的邮箱占位符
		if (message.contains(MessageConst.MESSAGE_USER_EMAIL)) {
			if (user != null) {
				message = message.replaceAll(MessageConst.MESSAGE_USER_EMAIL_REP, user.getEmail());
			} else {
				message = message.replaceAll(MessageConst.MESSAGE_USER_EMAIL_REP, "");
			}
		}

		// 替换正文中的验证码占位符
		if (message.contains(MessageConst.MESSAGE_VERIFY_CODE)) {
			if (!StringUtil.isEmpty(verifyCode)) {
				message = message.replaceAll(MessageConst.MESSAGE_VERIFY_CODE_REP, verifyCode);
			} else {
				message = message.replaceAll(MessageConst.MESSAGE_VERIFY_CODE_REP, "");
			}
		}

		// 替换正文中的商品名称占位符
		if (message.contains(MessageConst.MESSAGE_PRODUCT_NAME)) {
			if (product != null) {
				message = message.replaceAll(MessageConst.MESSAGE_PRODUCT_NAME_REP, product.getName());
			} else {
				message = message.replaceAll(MessageConst.MESSAGE_PRODUCT_NAME_REP, "");
			}
		}
		return message;
	}
}
