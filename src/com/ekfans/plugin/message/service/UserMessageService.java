package com.ekfans.plugin.message.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ekfans.base.system.model.Message;
import com.ekfans.base.system.model.MessageConfigDetail;
import com.ekfans.base.user.dao.IUserDao;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.message.util.MessageConst;
import com.ekfans.plugin.message.util.MessageHelper;
import com.ekfans.plugin.message.util.MessageUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 消息服务Service实现类
 * 
 * @ClassName: MessageService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 成都易科远见科技有限公司
 * @date 2014-5-30 上午09:41:17
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@SuppressWarnings("rawtypes")
public class UserMessageService implements Runnable {
	
	@Autowired
	private IUserDao userDao;
	
	public static Logger log = LoggerFactory.getLogger(UserMessageService.class);
	private HttpServletRequest request;
	private String methodName;
	private User user;
	private String verfyCode;
	private String judgment;

	public UserMessageService(HttpServletRequest request, String methodName, User user, String verfyCode, String judgment) {
		this.request = request;
		this.methodName = methodName;
		this.user = user;
		this.verfyCode = verfyCode;
		this.judgment = judgment;
	}

	/**
	 * 获取随机验证码
	 * 
	 * @Title: randomNo 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @return 设定文件
	 * @return int 返回类型
	 * @throws
	 */
	public int randomNo() {
		Random ra = new Random();
		return ra.nextInt(999999);
	}

	/**
	 * 会员注册成功通知
	 * 
	 * @Title: getUserRegisteMessageContent
	 * @param @param userName
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public boolean userRegiste() {

		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_REGISTE);

		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}

		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“短信”，并且短信内容不为空，则发送短信
			if (("0".equals(configDetail.getMessageType()) || "1".equals(configDetail.getMessageType()))
					&& !StringUtil.isEmpty(configDetail.getMobileContent())) {
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageContent = configDetail.getMobileContent();
				// 调用Helper方法替换正文的占位符
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, null, null, request);

				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendPhoneMessage(user.getMobile(), messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMobileNo(user.getMobile());
					message.setMessageContent(messageContent);
					message.setSendType(0);
					returnflag = messageBeanService.saveMessage(message);
				}

				// 如果短信发送失败，则将消息记录入日志
				if (!returnflag) {
					log.error("Mobile message failed ,mobile number:" + user.getMobile() + ";message:" + messageContent);
					return returnflag;
				}
			}

			// 如果消息发送类型为“所有”或者“邮件”，则发送邮件
			if ("0".equals(configDetail.getMessageType()) || "2".equals(configDetail.getMessageType())) {
				// TODO 解析邮件标题和正文，将该替换的内容替换掉，然后调用方法发送邮件
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageTitle = configDetail.getMailTitle();
				String messageContent = configDetail.mailContent;
				// 调用Helper方法替换正文和标题的占位符
				messageTitle = MessageHelper.contentMessageStr(messageTitle, user, null, null, null, request);
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, null, null, request);
				// 如果是及时发送，则调用方法发送邮件
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送邮件
					returnflag = MessageUtil.sendMail(user.getEmail(), messageTitle, messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMailAddress(user.getEmail());
					message.setMessageTitle(messageTitle);
					message.setMessageContent(messageContent);
					message.setSendType(1);
					returnflag = messageBeanService.saveMessage(message);
				}
			}

			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 会员注册验证短信通知
	 * 
	 * @Title: getUserRegisteMessageContent
	 * @param @param user 会员对象
	 * @param @param verifyCode 验证码
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public boolean userRegisteVerifyMobile() {

		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_REGISTE_VERIFY);

		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}

		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“短信”，并且短信内容不为空，则发送短信
			if (("0".equals(configDetail.getMessageType()) || "1".equals(configDetail.getMessageType()))
					&& !StringUtil.isEmpty(configDetail.getMobileContent())) {
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageContent = configDetail.getMobileContent();
				// 调用Helper方法替换正文的占位符
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, verfyCode, null, request);

				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendPhoneMessage(user.getMobile(), messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMobileNo(user.getMobile());
					message.setMessageContent(messageContent);
					message.setSendType(0);
					returnflag = messageBeanService.saveMessage(message);
				}
				// 如果短信发送失败，则将消息记录入日志
				if (!returnflag) {
					log.error("Mobile message failed ,mobile number:" + user.getMobile() + ";message:" + messageContent);
					return returnflag;
				}
			}
			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 会员注册验证通知邮件
	 * 
	 * @Title: getContent
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param randomNo
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public boolean userRegisteVerifyEmail() {

		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_REGISTE_VERIFY);

		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}

		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“邮件”，则发送邮件
			if ("0".equals(configDetail.getMessageType()) || "2".equals(configDetail.getMessageType())) {
				// TODO 解析邮件标题和正文，将该替换的内容替换掉，然后调用方法发送邮件
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageTitle = configDetail.getMailTitle();
				String messageContent = configDetail.mailContent;
				// 调用Helper方法替换正文和标题的占位符
				messageTitle = MessageHelper.contentMessageStr(messageTitle, user, null, verfyCode, judgment, request);
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, verfyCode, judgment, request);
				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendMail(user.getEmail(), messageTitle, messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMailAddress(user.getEmail());
					message.setMessageTitle(messageTitle);
					message.setMessageContent(messageContent);
					message.setSendType(1);
					returnflag = messageBeanService.saveMessage(message);
				}
			}

			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 会员修改手机/邮箱成功短信通知
	 * 
	 * @Title: getUserRegisteMessageContent
	 * @param @param userName
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public boolean userModifyMobile() {

		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_MODIFY);
		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}
		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“短信”，并且短信内容不为空，则发送短信
			if (("0".equals(configDetail.getMessageType()) || "1".equals(configDetail.getMessageType()))
					&& !StringUtil.isEmpty(configDetail.getMobileContent())) {
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageContent = configDetail.getMobileContent();
				// 调用Helper方法替换正文的占位符
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, null, null, request);

				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendPhoneMessage(user.getMobile(), messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMobileNo(user.getMobile());
					message.setMessageContent(messageContent);
					message.setSendType(0);
					returnflag = messageBeanService.saveMessage(message);
				}

				// 如果短信发送失败，则将消息记录入日志
				if (!returnflag) {
					log.error("Mobile message failed ,mobile number:" + user.getMobile() + ";message:" + messageContent);
					return returnflag;
				}
			}

			// 如果消息发送类型为“所有”或者“邮件”，则发送邮件
			if ("0".equals(configDetail.getMessageType()) || "2".equals(configDetail.getMessageType())) {
				// TODO 解析邮件标题和正文，将该替换的内容替换掉，然后调用方法发送邮件
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageTitle = configDetail.getMailTitle();
				String messageContent = configDetail.mailContent;
				// 调用Helper方法替换正文和标题的占位符
				messageTitle = MessageHelper.contentMessageStr(messageTitle, user, null, null, null, request);
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, null, null, request);
				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendMail(user.getEmail(), messageTitle, messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMailAddress(user.getEmail());
					message.setMessageTitle(messageTitle);
					message.setMessageContent(messageContent);
					message.setSendType(1);
					returnflag = messageBeanService.saveMessage(message);
				}
			}

			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 会员修改手机/邮箱成功邮件通知
	 * 
	 * @Title: getContent
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param randomNo
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public boolean userModifyEmail() {

		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_MODIFY);

		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}

		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“短信”，并且短信内容不为空，则发送短信
			if (("0".equals(configDetail.getMessageType()) || "1".equals(configDetail.getMessageType()))
					&& !StringUtil.isEmpty(configDetail.getMobileContent())) {
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageContent = configDetail.getMobileContent();
				// 调用Helper方法替换正文的占位符
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, null, null, request);

				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendPhoneMessage(user.getMobile(), messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMobileNo(user.getMobile());
					message.setMessageContent(messageContent);
					message.setSendType(0);
					returnflag = messageBeanService.saveMessage(message);
				}

				// 如果短信发送失败，则将消息记录入日志
				if (!returnflag) {
					log.error("Mobile message failed ,mobile number:" + user.getMobile() + ";message:" + messageContent);
					return returnflag;
				}
			}

			// 如果消息发送类型为“所有”或者“邮件”，则发送邮件
			if ("0".equals(configDetail.getMessageType()) || "2".equals(configDetail.getMessageType())) {
				// TODO 解析邮件标题和正文，将该替换的内容替换掉，然后调用方法发送邮件
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageTitle = configDetail.getMailTitle();
				String messageContent = configDetail.mailContent;
				// 调用Helper方法替换正文和标题的占位符
				messageTitle = MessageHelper.contentMessageStr(messageTitle, user, null, null, null, request);
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, null, null, request);
				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendMail(user.getEmail(), messageTitle, messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMailAddress(user.getEmail());
					message.setMessageTitle(messageTitle);
					message.setMessageContent(messageContent);
					message.setSendType(1);
					returnflag = messageBeanService.saveMessage(message);
				}
			}

			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 会员修改手机短信验证
	 * 
	 * @Title: getUserRegisteMessageContent
	 * @param @param user 会员对象
	 * @param @param verifyCode 验证码
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public boolean userModifyVerifyMobile() {
		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_MODIFY_VERIFY);

		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}

		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“短信”，并且短信内容不为空，则发送短信
			if (("0".equals(configDetail.getMessageType()) || "1".equals(configDetail.getMessageType()))
					&& !StringUtil.isEmpty(configDetail.getMobileContent())) {
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageContent = configDetail.getMobileContent();
				// 调用Helper方法替换正文的占位符
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, verfyCode, null, request);

				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendPhoneMessage(user.getMobile(), messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);

					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMobileNo(user.getMobile());
					message.setMessageContent(messageContent);
					message.setSendType(0);
					returnflag = messageBeanService.saveMessage(message);
				}

				// 如果短信发送失败，则将消息记录入日志
				if (!returnflag) {
					log.error("Mobile message failed ,mobile number:" + user.getMobile() + ";message:" + messageContent);
					return returnflag;
				}
			}
			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 会员修改邮箱验证邮件
	 * 
	 * @Title: getContent
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param randomNo
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public boolean userModifyVerifyEmail() {
		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_MODIFY_VERIFY);

		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}
		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“邮件”，则发送邮件
			if ("0".equals(configDetail.getMessageType()) || "2".equals(configDetail.getMessageType())) {
				// TODO 解析邮件标题和正文，将该替换的内容替换掉，然后调用方法发送邮件
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageTitle = configDetail.getMailTitle();
				String messageContent = configDetail.mailContent;
				// 调用Helper方法替换正文和标题的占位符
				if (!StringUtil.isEmpty(user.getEmailValiDate())) {
					user.setEmail(user.getEmailValiDate());
				}
				messageTitle = MessageHelper.contentMessageStr(messageTitle, user, null, verfyCode, null, request);
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, verfyCode, null, request);
				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendMail(user.getEmail(), messageTitle, messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);

					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMailAddress(user.getEmail());
					message.setMessageTitle(messageTitle);
					message.setMessageContent(messageContent);
					message.setSendType(1);
					returnflag = messageBeanService.saveMessage(message);
				}
			}

			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 会员找回密码验证短信
	 */
	public boolean userPwdResetMobile() {

		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_PWD_RESET);

		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}

		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“短信”，并且短信内容不为空，则发送短信
			if (("0".equals(configDetail.getMessageType()) || "1".equals(configDetail.getMessageType()))
					&& !StringUtil.isEmpty(configDetail.getMobileContent())) {
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageContent = configDetail.getMobileContent();
				// 调用Helper方法替换正文的占位符
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, verfyCode, null, request);

				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendPhoneMessage(user.getMobile(), messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);

					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMobileNo(user.getMobile());
					message.setMessageContent(messageContent);
					message.setSendType(0);
					returnflag = messageBeanService.saveMessage(message);
				}

				// 如果短信发送失败，则将消息记录入日志
				if (!returnflag) {
					log.error("Mobile message failed ,mobile number:" + user.getMobile() + ";message:" + messageContent);
					return returnflag;
				}
			}
			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	/**
	 * 会员找回密码邮件
	 */
	public boolean userPwdResetEmail() {
		// 从数据库中查询消息设置明细
		MessageConfigDetail configDetail = Cache.getMessageConfigDetail(MessageConst.MESSAGE_DETAIL_USER_PWD_RESET);

		// 如果从数据库查询出来的消息对象为空或者对象的状态为false，或者消息发送类型为空，则返回成功标识
		if (configDetail == null || !configDetail.status || StringUtil.isEmpty(configDetail.getMessageType())) {
			return true;
		}

		// 定义返回状态
		boolean returnflag = false;
		try {
			// 如果消息发送类型为“所有”或者“邮件”，则发送邮件
			if ("0".equals(configDetail.getMessageType()) || "2".equals(configDetail.getMessageType())) {
				// TODO 解析邮件标题和正文，将该替换的内容替换掉，然后调用方法发送邮件
				// TODO 解析短信内容，将该替换的内容替换进去，然后发送短信
				String messageTitle = configDetail.getMailTitle();
				String messageContent = configDetail.mailContent;
				// 调用Helper方法替换正文和标题的占位符
				messageTitle = MessageHelper.contentMessageStr(messageTitle, user, null, verfyCode, null, request);
				messageContent = MessageHelper.contentMessageStr(messageContent, user, null, verfyCode, judgment, request);
				// 如果是及时发送，则调用方法发送短信
				if ("0".equals(configDetail.getSendType())) {
					// 调用方法发送短消息
					returnflag = MessageUtil.sendMail(user.getEmail(), messageTitle, messageContent);
				} else {
					// 使用驻入的方法定义Service
					com.ekfans.base.system.service.IMessageService messageBeanService = SpringContextHolder
							.getBean(com.ekfans.base.system.service.IMessageService.class);
					// 将消息写入数据库，留于每天定时发送消息
					Message message = new Message();
					message.setMailAddress(user.getEmail());
					message.setMessageTitle(messageTitle);
					message.setMessageContent(messageContent);
					message.setSendType(1);
					returnflag = messageBeanService.saveMessage(message);
				}
			}

			return returnflag;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	
	
	
	@Override
	public void run() {
		// 如果传进来的方法名为空，则返回(不执行)
		if (StringUtil.isEmpty(this.methodName)) {
			return;
		}
		try {
			// 获取Class
			Class clazz = this.getClass();
			// 获取方法
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method != null && methodName.equals(method.getName())) {
					method.invoke(this);
					break;
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
