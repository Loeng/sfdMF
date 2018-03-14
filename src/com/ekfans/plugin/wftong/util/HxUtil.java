package com.ekfans.plugin.wftong.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wftong.base.easemob.model.ClientSecretCredential;
import com.ekfans.plugin.wftong.base.easemob.model.Constants;
import com.ekfans.plugin.wftong.base.easemob.model.Credential;
import com.ekfans.plugin.wftong.base.easemob.model.EndPoints;
import com.ekfans.plugin.wftong.base.easemob.model.HTTPMethod;
import com.ekfans.plugin.wftong.base.easemob.model.Roles;
import com.ekfans.plugin.wftong.base.easemob.util.JerseyUtils;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.pub.util.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 环信接口帮助类
 * @author 成都易科远见有限公司
 *
 */
public class HxUtil {
	public static final JsonNodeFactory factory = new JsonNodeFactory(false);
	private static final Logger LOGGER = LoggerFactory.getLogger(HxUtil.class);
	private static final String APPKEY = Constants.APPKEY;
	private static final String WLBAPPKEY = Constants.WLBAPPKEY;
	private static final String HX_HEAD = Cache.getResource("hxIdHeader");
	// 通过app的client_id和client_secret来获取app管理员token
	private static Credential credential = new ClientSecretCredential(Constants.APP_CLIENT_ID, Constants.APP_CLIENT_SECRET, Roles.USER_ROLE_APPADMIN);

	/**
	 * 删除群组
	 * 
	 */
	public static ObjectNode deleteChatGroups(String chatgroupid) {
		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]).path(chatgroupid);

			objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_DELETE, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 群组批量添加成员
	 * 
	 * @param toAddBacthChatgroupid
	 * @param usernames
	 * @return
	 */
	public static ObjectNode addUsersToGroupBatch(String toAddBacthChatgroupid, ObjectNode usernames) {
		ObjectNode objectNode = factory.objectNode();
		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);
			objectNode.put("message", "Bad format of Appkey");
			return objectNode;
		}
		if (StringUtils.isBlank(toAddBacthChatgroupid.trim())) {
			LOGGER.error("Property that named toAddBacthChatgroupid must be provided .");
			objectNode.put("message", "Property that named toAddBacthChatgroupid must be provided .");
			return objectNode;
		}
		// check properties that must be provided
		if (null != usernames && !usernames.has("usernames")) {
			LOGGER.error("Property that named usernames must be provided .");
			objectNode.put("message", "Property that named usernames must be provided .");
			return objectNode;
		}

		try {
			objectNode = JerseyUtils.sendRequest(
					EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]).path(toAddBacthChatgroupid).path("users"),
					usernames, credential, HTTPMethod.METHOD_POST, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 在群组中减少一个人
	 * 
	 */
	public static ObjectNode deleteUserFromGroup(String chatgroupid, String userName) {
		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		try {
			JerseyWebTarget webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]).path(chatgroupid).path("users")
					.path(userName);

			objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_DELETE, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 创建群组
	 * 
	 */
	public static ObjectNode creatChatGroups(ObjectNode dataObjectNode) {

		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		// check properties that must be provided
		if (!dataObjectNode.has("groupname")) {
			LOGGER.error("Property that named groupname must be provided .");

			objectNode.put("message", "Property that named groupname must be provided .");

			return objectNode;
		}
		if (!dataObjectNode.has("desc")) {
			LOGGER.error("Property that named desc must be provided .");

			objectNode.put("message", "Property that named desc must be provided .");

			return objectNode;
		}
		if (!dataObjectNode.has("public")) {
			LOGGER.error("Property that named public must be provided .");

			objectNode.put("message", "Property that named public must be provided .");

			return objectNode;
		}
		if (!dataObjectNode.has("approval")) {
			LOGGER.error("Property that named approval must be provided .");

			objectNode.put("message", "Property that named approval must be provided .");

			return objectNode;
		}
		if (!dataObjectNode.has("owner")) {
			LOGGER.error("Property that named owner must be provided .");

			objectNode.put("message", "Property that named owner must be provided .");

			return objectNode;
		}
		if (!dataObjectNode.has("members") || !dataObjectNode.path("members").isArray()) {
			LOGGER.error("Property that named members must be provided .");

			objectNode.put("message", "Property that named members must be provided .");

			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]);

			objectNode = JerseyUtils.sendRequest(webTarget, dataObjectNode, credential, HTTPMethod.METHOD_POST, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 在群组中添加一个人
	 * 
	 */
	public static ObjectNode addUserToGroup(String chatgroupid, String userName) {

		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		try {

			JerseyWebTarget webTarget = EndPoints.CHATGROUPS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]).path(chatgroupid).path("users")
					.path(userName);

			objectNode = JerseyUtils.sendRequest(webTarget, null, credential, HTTPMethod.METHOD_POST, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 发送消息
	 * 
	 * @param targetType 消息投递者类型：users 用户, chatgroups 群组
	 * @param target 接收者ID 必须是数组,数组元素为用户ID或者群组ID
	 * @param msg 息内容
	 * @param from 发送者
	 * @param ext 扩展字段
	 * 
	 * @return 请求响应
	 */
	public static ObjectNode sendMessages(String targetType, ArrayNode target, ObjectNode msg, String from, ObjectNode ext) {

		// 初始化json对象,用于存放数据
		ObjectNode objectNode = factory.objectNode();

		ObjectNode dataNode = factory.objectNode();

		// 验证appKey完整性
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);

			objectNode.put("message", "Bad format of Appkey");

			return objectNode;
		}

		// check properties that must be provided
		if (!("users".equals(targetType) || "chatgroups".equals(targetType))) {
			LOGGER.error("TargetType must be users or chatgroups .");

			objectNode.put("message", "TargetType must be users or chatgroups .");

			return objectNode;
		}

		try {
			// 构造消息体
			dataNode.put("target_type", targetType);
			dataNode.put("target", target);
			dataNode.put("msg", msg);
			dataNode.put("from", from);
			dataNode.put("ext", ext);

			JerseyWebTarget webTarget = EndPoints.MESSAGES_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]);

			objectNode = JerseyUtils.sendRequest(webTarget, dataNode, credential, HTTPMethod.METHOD_POST, null);

			objectNode = (ObjectNode) objectNode.get("data");
			for (int i = 0; i < target.size(); i++) {
				String resultStr = objectNode.path(target.path(i).asText()).asText();
				if ("success".equals(resultStr)) {
					LOGGER.error(String.format("Message has been send to user[%s] successfully .", target.path(i).asText()));
					System.out.println(target.path(i).asText());
				} else if (!"success".equals(resultStr)) {
					LOGGER.error(String.format("Message has been send to user[%s] failed .", target.path(i).asText()));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return objectNode;
	}

	/**
	 * 注册IM用户[批量生成用户然后注册]
	 * 
	 * 给指定AppKey创建一批用户
	 * 
	 * @param usernamePrefix
	 *            生成用户名的前缀
	 * @param perNumber
	 *            批量注册时一次注册的数量
	 * @param totalNumber
	 *            生成用户注册的用户总数
	 * @return
	 */
	public static ObjectNode createNewIMUserBatchGen(Long perNumber, Long totalNumber, List<AppUser> users) {
		ObjectNode objectNode = factory.objectNode();

		if (totalNumber == 0 || perNumber == 0) {
			return objectNode;
		}

		System.out.println("你即将注册" + totalNumber + "个用户，如果大于" + perNumber + "了,会分批注册,每次注册" + perNumber + "个");

		ArrayNode genericArrayNode = genericArrayNode(HxUtil.HX_HEAD, totalNumber, users);
		if (totalNumber <= perNumber) {
			objectNode = createNewIMUserBatch(genericArrayNode, users);
		} else {
			// 缓存集合和数组对象
			ArrayNode tmpArrayNode = factory.arrayNode();
			List<AppUser> tmpAppUser = new ArrayList<>();
			for (int i = 0; i < genericArrayNode.size(); i++) {
				tmpArrayNode.add(genericArrayNode.get(i));
				tmpAppUser.add(users.get(i));
				// 300 records on one migration
				if ((i + 1) % perNumber == 0) {
					objectNode = createNewIMUserBatch(tmpArrayNode, tmpAppUser);
					tmpArrayNode.removeAll();
					tmpAppUser.clear();
					continue;
				}

				// the rest records that less than the times of 300
				if (i > (genericArrayNode.size() / perNumber * perNumber - 1)) {
					objectNode = createNewIMUserBatch(tmpArrayNode, tmpAppUser);
					tmpArrayNode.removeAll();
					tmpAppUser.clear();
				}
			}
		}

		return objectNode;
	}

	/**
	 * 指定前缀和数量生成用户基本数据
	 * 
	 * @param usernamePrefix
	 * @param number
	 * @return
	 */
	private static ArrayNode genericArrayNode(String usernamePrefix, Long number, List<AppUser> users) {
		ArrayNode arrayNode = factory.arrayNode();
		for (int i = 0; i < users.size(); i++) {
			ObjectNode userNode = factory.objectNode();
			// 以邮箱作为账号
			String account = users.get(i).getName();
			// 截取邮箱后面的后缀
			String[] accountName = account.split(".com");
			userNode.put("username", usernamePrefix + "_" + accountName[0].replace("@", ""));
			userNode.put("password", "111111");
			arrayNode.add(userNode);
		}

		return arrayNode;
	}

	/**
	 * 注册IM用户[批量]
	 * 
	 * 给指定AppKey创建一批用户
	 * 
	 * @param dataArrayNode
	 * @return
	 */
	private static ObjectNode createNewIMUserBatch(ArrayNode dataArrayNode, List<AppUser> users) {
		ObjectNode objectNode = factory.objectNode();

		// check appKey format
		if (!JerseyUtils.match("^(?!-)[0-9a-zA-Z\\-]+#[0-9a-zA-Z]+", APPKEY)) {
			LOGGER.error("Bad format of Appkey: " + APPKEY);
			objectNode.put("message", "Bad format of Appkey");
			return objectNode;
		}

		// check properties that must be provided
		if (dataArrayNode.isArray()) {
			for (JsonNode jsonNode : dataArrayNode) {
				if (null != jsonNode && !jsonNode.has("username")) {
					LOGGER.error("Property that named username must be provided .");

					objectNode.put("message", "Property that named username must be provided .");

					return objectNode;
				}
				if (null != jsonNode && !jsonNode.has("password")) {
					LOGGER.error("Property that named password must be provided .");

					objectNode.put("message", "Property that named password must be provided .");

					return objectNode;
				}
			}
		}

		try {
			JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", APPKEY.split("#")[0]).resolveTemplate("app_name", APPKEY.split("#")[1]);

			objectNode = JerseyUtils.sendRequest(webTarget, dataArrayNode, credential, HTTPMethod.METHOD_POST, null);
			String statusCode = "200";
			String returnCode = objectNode.get("statusCode").toString();
			if (returnCode.equals(statusCode)) {
				System.out.println("reg success");
				IUserService userService = SpringContextHolder.getBean(IUserService.class);
				for (int i = 0; i < users.size(); i++) {
					AppUser user = users.get(i);
					// 以邮箱作为账号
					String account = user.getName();
					// 截取邮箱后面的后缀
					String[] accountName = account.split(".com");
					// set
					user.setHxUserName(HxUtil.HX_HEAD + "_" + accountName[0].replace("@", ""));
					user.setHxPassword("111111");
					userService.updateAppUser(user);
				}
				return objectNode;
			} else {
				System.out.println("reg faild");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return objectNode;
		}
		return objectNode;
	}
	
	/**
	 * 物流宝创建环信用户
	 * @param username
	 * @return  map
	 */
	public static Map<String, String> createHxUser(String username){
		Map<String, String> map = new HashMap<String, String>();
		//设置环信用户账号
		int i=(int)(Math.random()*900)+100;
		map.put("username", HX_HEAD+"_"+username+"_"+i);
		//设置环信用户密码
		map.put("password", "111111");
		ObjectNode objectNode = factory.objectNode();
		//创建请求地址
		JerseyWebTarget webTarget = EndPoints.USERS_TARGET.resolveTemplate("org_name", WLBAPPKEY.split("#")[0]).resolveTemplate("app_name", WLBAPPKEY.split("#")[1]);
        //发送请求
		objectNode = JerseyUtils.sendRequest(webTarget, map, credential, HTTPMethod.METHOD_POST, null);
        //获取状态码
		String returnCode = objectNode.get("statusCode").toString();
		map.put("statusCode", returnCode);
		return map;
	}
	
	/**
	 * 物流宝环信推送消息
	 *  @param targetType 消息投递者类型：users 用户, chatgroups 群组
	 * @param target 接收者ID 必须是数组,数组元素为用户ID或者群组ID
	 * @param msg 息内容
	 * @param from 发送者
	 * @param ext 扩展字段
	 * @return "400" massage结构错误 "401" 未授权[无token、token错误、token过期] 其他:参见"http://docs.easemob.com/start/450errorcode/10restapierrorcode"
	 */
	public static String wlbAPPsendMessages(String targetType, ArrayNode target, ObjectNode msg, String from, ObjectNode ext){
		JerseyWebTarget webTarget = EndPoints.MESSAGES_TARGET.resolveTemplate("org_name", WLBAPPKEY.split("#")[0]).resolveTemplate("app_name", WLBAPPKEY.split("#")[1]);
		ObjectNode objectNode = factory.objectNode();
		ObjectNode dataNode = factory.objectNode();
		// 构造消息体
		dataNode.put("target_type", targetType);
		dataNode.put("target", target);
		dataNode.put("msg", msg);
		dataNode.put("from", from);
		dataNode.put("ext", ext);
		objectNode = JerseyUtils.sendRequest(webTarget, dataNode, credential, HTTPMethod.METHOD_POST, null);
		//获取状态码
		String returnCode = objectNode.get("statusCode").toString();
		return returnCode;
	}
	
}
