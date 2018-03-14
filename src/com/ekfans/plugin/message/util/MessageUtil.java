package com.ekfans.plugin.message.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ekfans.base.system.model.MessageConfig;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.StringUtil;

/**
 * 消息发送Util类
 * 
 * @ClassName: MessageUtil
 * @author 成都易科远见科技有限公司
 * @date 2014-5-19 下午02:49:07
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class MessageUtil {
	public static Logger log = LoggerFactory.getLogger(MessageUtil.class);

	/**
	 * 发送短信
	 * 
	 * @Title: sendMessage 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param phoneNo
	 * @param @param content
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean sendPhoneMessage(String phoneNo, String content) {
		// 如果传过来的手机号为空，则返回失败
		if (StringUtil.isEmpty(phoneNo)) {
			return false;
		}

		// 如果传过来的短信内容为空，则返回失败
		if (StringUtil.isEmpty(content)) {
			return false;
		}

		URL url = null;
		String CorpID="LKSDK0005461";//账户名
		String Pwd="sfd@8998";//密码
		try {
			String send_content=URLEncoder.encode(content.replaceAll("<br/>", " "), "GBK");//发送内容
			url = new URL("http://mb345.com/WS/BatchSend.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+phoneNo+"&Content="+send_content+"&Cell=&SendTime=");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader in = null;
		int inputLine = 0;
		try {
			System.out.println("开始发送短信手机号码为 ："+phoneNo);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
		} catch (Exception e) {
			System.out.println("网络异常,发送短信失败！");
			inputLine=-2;
		}
		System.out.println("结束发送短信返回值：  "+inputLine);
		return true;
	}

	/**
	 * 发送邮件实现类
	 * 
	 * @Title: sendMail
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param mailAddr
	 * @param @param title
	 * @param @param content
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @throws
	 */
	public static boolean sendMail(String mailAddr, String title, String content) {
		// 如果传进来的收件地址为空，或者邮件地址格式不正确，则返回失败
		if (StringUtil.isEmpty(mailAddr) || mailAddr.indexOf("@") == -1 || mailAddr.indexOf(".") == -1) {
			return false;
		}

		// 从缓存中获取MessageConfig对象
		MessageConfig messageConfig = Cache.getMessageConfig();

		// 如果获取的MessageConfig为空或者端口等配置为空，则返回失败
		if (messageConfig == null || StringUtil.isEmpty(messageConfig.getHost())) {
			return false;
		}
//         MessageConfig messageConfig = new MessageConfig();
//        messageConfig.setHost("smtp.exmail.qq.com");
//        messageConfig.setMailAddress("lgy@ekfans.com");
//        messageConfig.setMailName("成都易科远见科技有限公司");
//        messageConfig.setPassword("ekfansMAIL2014");
//        messageConfig.setUserName("lgy@ekfans.com");
		Properties props = new Properties();
		// 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
		props.put("mail.smtp.host", messageConfig.getHost());
		// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
		props.put("mail.smtp.auth", "true");
		// 用刚刚设置好的props对象构建一个session
		Session session = Session.getDefaultInstance(props);
		// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
		// 用（你可以在控制台（console)上看到发送邮件的过程）
		session.setDebug(false);
		// 用session为参数定义消息对象
		MimeMessage message = new MimeMessage(session);
		try {
			String from = "";
			// 设置发件人 如果存在中文 则 中文<邮件地址> 形式
			if (!StringUtil.isEmpty(messageConfig.getMailName())) {
				from = javax.mail.internet.MimeUtility.encodeText(messageConfig.getMailName());
				if (!StringUtil.isEmpty(messageConfig.getMailAddress())) {
					// 加载发件人地址
					message.setFrom(new InternetAddress(from + " <" + messageConfig.getMailAddress() + ">"));
				} else {
					// 加载发件人地址
					message.setFrom(new InternetAddress(from + " <" + messageConfig.getUserName() + ">"));
				}
			} else {
				if (!StringUtil.isEmpty(messageConfig.getMailAddress())) {
					// 加载发件人地址
					message.setFrom(new InternetAddress(messageConfig.getMailAddress()));
				} else {
					// 加载发件人地址
					message.setFrom(new InternetAddress(messageConfig.getUserName()));
				}
			}
			// 加载收件人地址
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddr));

			if (!StringUtil.isEmpty(title)) {
				// 加载标题
				message.setSubject(title);
			}
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart("related");

			// 设置邮件的文本内容
			// BodyPart contentPart = new MimeBodyPart();
			// contentPart.setText(content, "text/html; charset=utf-8");
			// multipart.addBodyPart(contentPart);

			MimeBodyPart messageBodyPart = new MimeBodyPart();// 创建一个包含HTML内容的MimeBodyPart
			// 设置HTML内容
			messageBodyPart.setContent(content, "text/html; charset=utf-8");
			multipart.addBodyPart(messageBodyPart);
			// 添加附件
			// BodyPart messageBodyPart = new MimeBodyPart();
			// DataSource source = new FileDataSource(affix);
			// 添加附件的内容
			// messageBodyPart.setDataHandler(new DataHandler(source));
			// 添加附件的标题
			// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			// sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
			// messageBodyPart.setFileName("=?GBK?B?"+
			// enc.encode(affixName.getBytes()) + "?=");
			// multipart.addBodyPart(messageBodyPart);

			// 将multipart对象放到message中
			message.setContent(multipart);
			// 保存邮件
			message.saveChanges();
			// 发送邮件
			Transport transport = session.getTransport("smtp");
			// 连接服务器的邮箱
			transport.connect(messageConfig.getHost(), messageConfig.getUserName(), messageConfig.getPassword());
			// 把邮件发送出去
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception e) {
			// 记录日志
			e.printStackTrace();
			log.error("Email Send Error: mailAddr:" + mailAddr + ";title:" + title + ";content:" + content + ";ErrorCode:" + e.getMessage());
		}

		return false;
	}

	public int randomNo() {
		Random ra = new Random();
		return ra.nextInt(999999);
	}

	public String getContent(int randomNo) {
		StringBuffer content = new StringBuffer("您正在验证手机，验证码为：");
		content.append(randomNo);
		return content.toString();
	}

	/**
	 * 测试方法
	 * 
	 * @Title: main
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param args 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public static void main(String[] args) {
		sendMail("389134703@qq.com", "测试邮件2", "邮件发件测试22！");
	}
}
