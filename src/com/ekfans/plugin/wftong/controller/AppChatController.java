package com.ekfans.plugin.wftong.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;

import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.number.NoManager;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.plugin.wftong.AppServerController;
import com.ekfans.plugin.wftong.base.easemob.model.Groups;
import com.ekfans.plugin.wftong.base.easemob.service.IGroupsService;
import com.ekfans.plugin.wftong.base.friend.model.FriendRelation;
import com.ekfans.plugin.wftong.base.friend.service.IFriendRelationService;
import com.ekfans.plugin.wftong.controller.model.AppFriend;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.plugin.wftong.util.HxUtil;
import com.ekfans.plugin.wftong.util.MyJSONObject;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;

/**
 * 环信 Controllers
 * 
 * @Title: AppChatController
 * @Description:
 * @Copyright: Copyright (c) 2016
 * @Company: 成都易科远见科技有限公司
 * @author lh
 * @date 2016年4月13日14:41:59
 * @version 1.0
 */
public class AppChatController {
	// 响应
	private MyJSONObject jsonResponse;
	// 请求主体
	private MyJSONObject jsonRequest;
	// request
	private HttpServletRequest request;
	// 当前用户对象
	private AppUser appUser;

	private IUserService userService = SpringContextHolder.getBean(IUserService.class);
	private IFriendRelationService friendService = SpringContextHolder.getBean(IFriendRelationService.class);
	private IGroupsService groupsService = SpringContextHolder.getBean(IGroupsService.class);

	/**
	 * 构造
	 * @param jsonRequest 请求主体，用于获取请求参数
	 * @param jsonResponse 响应主体，用于返回结果数据
	 * @param appUser 当前用户
	 * @param request 
	 */
	public AppChatController(MyJSONObject jsonRequest, MyJSONObject jsonResponse, AppUser appUser, HttpServletRequest request) throws Exception {
		this.jsonRequest = jsonRequest;
		this.jsonResponse = jsonResponse;
		this.appUser = appUser;
		this.request = request;
	}

	/**
	 * 获取朋友列表
	 * @throws Exception
	 */
	public void getFriendList() throws Exception {
		String userId = this.appUser.getUserId();

		// 调用service查询
		List<AppFriend> list = friendService.getFriendList(userId, request);
		if (list != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		}
	}

	/**
	 * 按条件获取所有用户
	 * @throws Exception
	 */
	public void searchUserList() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "searchSign");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();
		String searchSign = jsonRequest.getString("searchSign");

		// 调用service查询
		List<AppFriend> list = friendService.getAllFriendList(userId, searchSign, request);
		if (list != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		}
	}

	/**
	 * 申请/添加/拒绝好友
	 * @throws Exception
	 */
	public void operateFriend() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "friendId", "type");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();
		// 被操作人Id
		String friendId = jsonRequest.getString("friendId");
		// 0申请 1添加 2拒绝好友
		String type = jsonRequest.getString("type");
		// 通知消息
		String text = jsonRequest.getString("text");
		String msgId = jsonRequest.getString("msgId");

		// 查询操作人、被操作人、好友关系
		AppUser user = userService.getAppUserById(userId);
		AppUser friend = userService.getAppUserById(friendId);
		FriendRelation isExists = friendService.chekdFriend(userId, friendId, null);

		// 验证
		if (user == null || friend == null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "0");
			jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.errror.systemError"));
			return;
		}

		switch (type) {
		// 申请操作
		case "0":
			// 没有申请过
			if (isExists == null) {
				// 初始化好友关系
				FriendRelation friendRelation = new FriendRelation();
				friendRelation.setUserId(userId);
				friendRelation.setFriendId(friendId);
				// 保存好友关系
				friendService.addFriend(friendRelation);

				// 无需验证，直接加为好友
				if (!friend.getFriendValidSwitch()) {
					friendRelation.setStatus("1");
					friendRelation.setCreateTime(DateUtil.getSysDateTimeString());
					// 更新数据库
					friendService.update(friendRelation);
				}
			} else if (isExists != null) {
				if ("0".equals(isExists.getStatus())) {
					// 检查验证，直接加为好友
					if (!friend.getFriendValidSwitch()) {
						isExists.setStatus("1");
						isExists.setCreateTime(DateUtil.getSysDateTimeString());
						// 更新数据库
						friendService.update(isExists);
					}
				} else if ("1".equals(isExists.getStatus())) {
					jsonResponse.put(AppServerController.MSG_RETURN, "已是好友关系");
					return;
				}
			}
			break;
		// 同意操作
		case "1":
			// 有且只有申请中
			if (isExists != null && "0".equals(isExists.getStatus())) {
				isExists.setStatus("1");
				// 更新数据库
				friendService.update(isExists);
			}
			break;
		// 拒绝操作
		case "2":
			// 有且只有申请中
			if (isExists != null && "0".equals(isExists.getStatus())) {
				// 删除数据库信息
				friendService.delete(isExists);
				// 直接返回
				jsonResponse.put(AppServerController.STATUS_RETURN, "1");
				return;
			}
			break;
		default:
			break;
		}

		// ======================根据结果发送消息============================
		FriendRelation friendRelation = friendService.chekdFriend(userId, friendId, null);

		// ---------hx---------
		// 拼接图片url用
		BasicRequest br = new BasicRequest(request);
		// 发送用户类型 users为用户,groups为组
		String targetTypeus = "users";
		// 发送者
		String from = "";
		// 接收者
		ArrayNode recs = HxUtil.factory.arrayNode();
		// 消息内容
		ObjectNode sendMsg = HxUtil.factory.objectNode();
		// 扩展字段
		ObjectNode extParam = HxUtil.factory.objectNode();

		// 初始化消息内容
		// 发送的信息内容
		sendMsg.put("msg", text);
		// 内容格式
		sendMsg.put("type", "cmd");

		// 初始化扩展字段
		// type friend:添加好友
		extParam.put("type", "friend");
		// 状态:0申请,1同意,2拒绝
		extParam.put("status", type);
		extParam.put("userId", user.getUserId());
		extParam.put("avatar", user.getAvatar());
		extParam.put("nickName", user.getNickName());

		ObjectNode sendTxtMessageusernode = null;

		if (friendRelation != null) {
			// 申请时
			if ("0".equals(type)) {
				// 申请成功
				if ("0".equals(friendRelation.getStatus())) {
					// 被操作人收到申请
					from = user.getHxUserName();
					recs.add(friend.getHxUserName());
					// extParam.put("status", "1");
					sendTxtMessageusernode = HxUtil.sendMessages(targetTypeus, recs, sendMsg, from, extParam);
				}
				// 添加好友成功
				else if ("1".equals(friendRelation.getStatus())) {
					// 被操作人收到某某添加你成功
					from = user.getHxUserName();
					recs.add(friend.getHxUserName());
					// extParam.put("status", "1");
					sendTxtMessageusernode = HxUtil.sendMessages(targetTypeus, recs, sendMsg, from, extParam);
				}
			}
			// 加好友时
			else if ("1".equals(type)) {
				// 添加好友成功
				if ("1".equals(friendRelation.getStatus())) {
					// 被操作人收到同意你了
					from = user.getHxUserName();
					recs.add(friend.getHxUserName());
					// extParam.put("status", "1");
					sendTxtMessageusernode = HxUtil.sendMessages(targetTypeus, recs, sendMsg, from, extParam);
				}
			}
		}

		if (sendTxtMessageusernode != null) {
			String returnMsg = sendTxtMessageusernode.get(friend.getHxUserName()).toString();
			String success = "\"success\"";
			// 判断接口返回数据
			if (success.equals(returnMsg)) {
				// 返回成功状态
				jsonResponse.putBean(friend);
				jsonResponse.put(AppServerController.STATUS_RETURN, "1");
				jsonResponse.put("msgId", msgId == null ? "" : msgId);
			}
		}

	}

	
	/**
	 * 添加好友
	 * @throws Exception
	 */
	public void addFriend() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "friendId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}
		// 操作人Id
		String userId = this.appUser.getUserId();
		// 被操作人Id
		String friendId = jsonRequest.getString("friendId");

		FriendRelation isExists = new FriendRelation();
		isExists.setUserId(userId);
		isExists.setFriendId(friendId);
		isExists.setCreateTime(DateUtil.getSysDateTimeString());
		isExists.setStatus("1");
		boolean flag = friendService.addFriendDirectly(isExists);
		if (flag){
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");	
		}
	}
	
	
	
	
	
	
	
	/**
	 * 删除好友
	 */
	public void deleteFriend() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "friendId");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();
		String friendId = jsonRequest.getString("friendId");

		// 查询好友列表
		FriendRelation friendRelation = friendService.chekdFriend(userId, friendId, null);
		// 好友关系存在关系状态为1
		if (friendRelation != null) {
			// 解除好友关系,删除数据库信息
			boolean flag = friendService.delete(friendRelation);
			if (flag) {
				// 返回成功状态
				jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			}
		}
	}

	/**
	 * 根据环信ID查询用户
	 */
	// public void getUserInfo() throws Exception {
	// String userId = this.appUser.getUserId();
	// String searchId = jsonRequest.getString("searchId");
	//
	// AppUser appUser = userService.getAppUserByHxId(searchId, request);
	// if (appUser != null) {
	// // 查询是否为好友关系
	// FriendRelation friendRelation = friendService.chekdFriend(userId,
	// searchId, "1");
	// // 当存在好友关系显示好友
	// if (friendRelation != null &&
	// (!friendRelation.getFriendId().equals(userId) ||
	// !friendRelation.getUserId().equals(userId))) {
	// appUser.setRealetionShap("好友");
	// } else {
	// // 当不存在好友关系时,查询是否为好友的好友(二度人脉)
	// List<String> ids = friendService.getAppSecondFriendIds(userId, request);
	// for (String string : ids) {
	// // 供需发布者ID匹配二度好友
	// if (searchId.equals(string)) {
	// appUser.setRealetionShap("好友的好友");
	// break;
	// }
	// }
	// }
	//
	// jsonResponse.put(AppMainController.STATUS_RETURN, "1");
	// jsonResponse.putBean(appUser);
	// }
	// }

	/**
	 * 我的二度人脉列表
	 */
	public void getSecondFriendList() throws Exception {
		String userId = this.appUser.getUserId();

		List<AppFriend> list = friendService.getAppSecondFriendList(userId, request);
		if (list != null) {
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		}
	}

	/**
	 * 刷新用户信息
	 */
	public void getFlashInfo() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "memberIds");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		JSONArray memberIds = jsonRequest.getJSONArray("memberIds");

		List<AppFriend> list = new ArrayList<AppFriend>();
		// 遍历memberId数组
		if (memberIds != null) {
			for (int i = 0; i < memberIds.length(); i++) {
				AppFriend appFriend = friendService.getAppFriendById(memberIds.getString(i), request);
				if (appFriend != null) {
					list.add(appFriend);
				}
			}
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			jsonResponse.put("details", JsonUtil.convertToJsonArray(list));
		}
	}

	/**
	 * 创建群组
	 */
	public void addGroups() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "groupsName", "groupsDesc");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();
		// 群组名称, 此属性为必须的
		String groupsName = jsonRequest.getString("groupsName");
		// 群组描述, 此属性为必须的
		String groupsDesc = jsonRequest.getString("groupsDesc");
		// 群组成员最大数(包括群主), 值为数值类型,默认值200,此属性为可选的
		String maxusers = jsonRequest.getString("maxusers");
		// 加入公开群是否需要批准, 默认值是false（加群不需要群主批准）, 此属性为可选的,只作用于公开群
		boolean approval = jsonRequest.getBoolean("approval");
		// 是否是公开群, 此属性为必须的,为false时为私有群
		boolean publicType = jsonRequest.getBoolean("publicType");
		// 群组成员,此属性为可选的,但是如果加了此项,数组元素至少一个
		JSONArray members = jsonRequest.getJSONArray("memberIds");

		// 获取登陆用户信息
		AppUser user = userService.getAppUserById(userId);
		// 创建ObjectNode 对象
		ObjectNode dataObjectNode = JsonNodeFactory.instance.objectNode();
		/**
		 *  添加群组属性
		 */
		dataObjectNode.put("groupname", groupsName);
		dataObjectNode.put("desc", groupsDesc);
		dataObjectNode.put("approval", approval);
		dataObjectNode.put("public", publicType);
		dataObjectNode.put("maxusers", maxusers);
		dataObjectNode.put("owner", user.getHxUserName());

		// 接受消息用户数组
		ArrayNode targetusers = HxUtil.factory.arrayNode();

		// 拼装字符串
		StringBuffer str = new StringBuffer("邀请");

		// 添加组员信息
		ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();
		if (members != null && members.length() > 0) {
			for (int i = 0; i < members.length(); i++) {
				// 循环查询用户
				User member = userService.getUser(members.getString(i));
				// 添加用户到群组列表
				arrayNode.add(member.getHxUserName());
				// 拼装消息体:XX、XX加入群组
				str.append(member.getNickName());
				// 判断是否为最后一个用户,否则在末尾添加一个“、”
				if (i < members.length() && members.length() > 1) {
					str.append("、");
				}
			}
		}
		// 判断组员数组是否存在多个.
		dataObjectNode.put("members", arrayNode);
		// 调用环信创建群组接口
		ObjectNode creatChatGroupNode = HxUtil.creatChatGroups(dataObjectNode);

		// 将群组ID信息存入数据库
		Groups groups = new Groups();
		// 设置群组ID
		groups.setGroupsId(creatChatGroupNode.get("data").get("groupid").toString().replace("\"", ""));
		// 设置群主ID
		groups.setUserId(userId);
		// 设置群名
		groups.setGroupName(groupsName);
		// 创建时间
		groups.setCreateTime(DateUtil.getSysDateTimeString().toString());
		// 主键ID
		groups.setId(new NoManager().createGroupsId());
		// 调用service保存群组信息
		boolean flag = groupsService.addGroupsInfo(groups);
		// 初始成功状态码
		String success = "200";
		/**
		 * 给群组发送一条消息
		 */
		// 表示这个消息是谁发出来的, 可以没有这个属性, 那么就会显示是admin, 如果有的话, 则会显示是这个用户发出的
		String from = user.getHxUserName();
		// users 给用户发消息, chatgroups 给群发消息
		String targetTypeus = "chatgroups";
		// 添加群组ID，发送给群组
		targetusers.add(groups.getGroupsId());
		// 扩展字段
		ObjectNode ext = HxUtil.factory.objectNode();
		ext.put("type", "many");
		// 消息体
		ObjectNode txtmsg = HxUtil.factory.objectNode();
		// 消息内容
		txtmsg.put("msg", str.append("加入群组").toString());
		// 消息格式
		txtmsg.put("type", "txt");

		// 获取创建群组的回调状态码
		String statusCode = creatChatGroupNode.get("statusCode").toString();
		// 为200 且调用service返回状态为true则创建成功,给群组发送消息
		if (success.equals(statusCode) && flag) {
			// 给组员发送群消息
			HxUtil.sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
			// 返回成功信息
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
		}
	}

	/**
	 * 申请、同意加群
	 */
	public void applyJoinGroup() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "groupId", "type");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		String userId = this.appUser.getUserId();
		String groupId = jsonRequest.getString("groupId");
		// 状态:0申请,1同意
		String type = jsonRequest.getString("type");
		// 消息内容
		String text = jsonRequest.getString("text");
		String msgId = jsonRequest.getString("msgId");

		Groups groups = groupsService.findByGroupsId(groupId);

		// 查询userID的用户
		AppUser user = userService.getAppUserById(userId);
		// 获取user用户的昵称,用于发送消息
		String from = user.getHxUserName();
		// 发送用户类型 users为用户,groups为组
		String targetTypeus = "users";
		// 扩展字段
		ObjectNode ext = HxUtil.factory.objectNode();
		// type group:申请入群
		ext.put("type", "group");
		// 状态:0申请,1同意
		ext.put("status", type);
		// userId
		ext.put("userId", user.getUserId());
		ext.put("groupId", groupId);
		ext.put("groupName", groups != null ? groups.getGroupName() : "");
		// 头像
		ext.put("avatar", user.getAvatar());
		ext.put("groupId", groupId);
		ext.put("nick", user.getNickName());
		//
		AppUser groupUser = userService.getAppUserByGroupId(groupId);
		// 根据群组ID获取群主用户的ID,用于接收消息
		ArrayNode targetusers = HxUtil.factory.arrayNode();
		// 接受消息用户环信ID
		targetusers.add(groupUser.getHxUserName());
		// 信息内容
		ObjectNode txtmsg = HxUtil.factory.objectNode();
		// 发送的信息内容
		txtmsg.put("msg", text);
		// 内容格式
		txtmsg.put("type", "txt");

		ObjectNode sendTxtMessageusernode = null;
		String success = "\"success\"";
		switch (type) {
		// 申请操作
		case "0":
			// 调用环信接口,给用户发送一条申请加群组消息
			sendTxtMessageusernode = HxUtil.sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
			// 接受返回状态信息
			String returnMsg1 = sendTxtMessageusernode.get(groupUser.getHxUserName()).toString();
			// 当状态为"success",则返回成功信息
			if (success.equals(returnMsg1)) {
				jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			}
			break;
		// 同意操作
		case "1":
			// 调用环信接口,给用户发送一条同意加入群组消息
			sendTxtMessageusernode = HxUtil.sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
			String returnMsg2 = sendTxtMessageusernode.get(user.getHxUserName()).toString();
			// 发送消息成功则添加用户到群组
			if (success.equals(returnMsg2)) {
				// 调用环信接口,群组加1人
				ObjectNode addUserToGroupNode = HxUtil.addUserToGroup(groupId, user != null ? user.getHxUserName() : "");
				System.out.println(addUserToGroupNode);
				jsonResponse.put(AppServerController.STATUS_RETURN, "1");
				jsonResponse.put("msgId", msgId);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 加/减/T/解散
	 */
	public void operateGroup() throws Exception {
		// 非空验证
		String verifyNotNull = JsonUtil.verifyParamNotNullApp(jsonRequest, "groupId", "type");
		if (!StringUtil.isEmpty(verifyNotNull)) {
			jsonResponse.put(AppServerController.MSG_RETURN, verifyNotNull);
			return;
		}

		// id
		String userId = this.appUser.getUserId();
		// 群组id数组
		String groupId = jsonRequest.getString("groupId");
		// 踢人0，退出1，加入2，解散3
		String type = jsonRequest.getString("type");
		// 群组成员,此属性为可选的,但是如果加了此项,数组元素至少一个
		JSONArray membersId = jsonRequest.getJSONArray("memberIds");

		// 当前用户的环信ID
		AppUser user = userService.getAppUserById(userId);
		// 接口返回信息
		ObjectNode returnMsg = null;
		// 踢人
		switch (type) {
		case "0":
			// 被踢除的用户环信ID
			String toRemoveUsername1 = userService.getUser(membersId.getString(0)).getHxUserName();
			// 调用环信接口，踢出组员
			returnMsg = HxUtil.deleteUserFromGroup(groupId, toRemoveUsername1);
			break;
		// 退出
		case "1":
			String toRemoveUsername2 = user.getHxUserName();
			// 环信接口.踢出组员
			returnMsg = HxUtil.deleteUserFromGroup(groupId, toRemoveUsername2);
			break;
		// 群主加人,可多人
		case "2":
			/**
			 * 给群组发送一条消息
			 */
			// 表示这个消息是谁发出来的, 可以没有这个属性, 那么就会显示是admin, 如果有的话, 则会显示是这个用户发出的
			String from = user.getHxUserName();
			// users 给用户发消息, chatgroups 给群发消息
			String targetTypeus = "chatgroups";
			// 接受消息用户数组
			ArrayNode targetusers = HxUtil.factory.arrayNode();
			// 拼装字符串
			StringBuffer str = new StringBuffer("邀请");
			// 添加群组ID，发送给群组
			targetusers.add(groupId);
			// 扩展字段
			ObjectNode ext = HxUtil.factory.objectNode();
			ext.put("type", "many");
			ext.put("hxId", user.getHxUserName());
			// 消息体
			ObjectNode txtmsg = HxUtil.factory.objectNode();

			ArrayNode usernames = JsonNodeFactory.instance.arrayNode();
			// 遍历memberId
			if (membersId.length() > 0 && membersId != null) {
				for (int i = 0; i < membersId.length(); i++) {
					// 循环查询用户
					AppUser members = userService.getAppUserById(membersId.getString(i));
					// 添加用户到列表
					usernames.add(members.getHxUserName());
					// 拼装消息体:XX、XX加入群组
					str.append(members.getNickName());
					// 判断是否为最后一个用户,否则在末尾添加一个“、”
					if (i < membersId.length() && membersId.length() > 1) {
						str.append("、");
					}
				}
				// 创建组员
				ObjectNode usernamesNode = JsonNodeFactory.instance.objectNode();
				// 添加组员信息
				usernamesNode.put("usernames", usernames);
				// 批量添加群组
				returnMsg = HxUtil.addUsersToGroupBatch(groupId, usernamesNode);
			}

			// 消息内容
			txtmsg.put("msg", str.append("加入群组").toString());
			// 消息格式
			txtmsg.put("type", "txt");
			// 给组员发送群消息
			ObjectNode returnxx = HxUtil.sendMessages(targetTypeus, targetusers, txtmsg, from, ext);
			System.out.println(returnxx);
			break;
		// 解散群组
		case "3":
			returnMsg = HxUtil.deleteChatGroups(groupId);
			break;

		default:
			break;
		}

		String success = "200";
		// 接受返回状态码
		String statusCode = returnMsg.get("statusCode").toString();
		if (success.equals(statusCode)) {
			// 返回成功信息
			jsonResponse.put(AppServerController.STATUS_RETURN, "1");
			// 操作码
			jsonResponse.put("exId", type);
		}
	}

}
