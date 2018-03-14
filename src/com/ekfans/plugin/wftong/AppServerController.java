package com.ekfans.plugin.wftong;

import java.net.ConnectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wftong.base.log.model.WftLog;
import com.ekfans.plugin.wftong.base.log.service.IWftLogService;
import com.ekfans.plugin.wftong.controller.AppChatController;
import com.ekfans.plugin.wftong.controller.AppCommonController;
import com.ekfans.plugin.wftong.controller.AppSupplyController;
import com.ekfans.plugin.wftong.controller.AppTransportController;
import com.ekfans.plugin.wftong.controller.AppUserController;
import com.ekfans.plugin.wftong.controller.AppWfOrderController;
import com.ekfans.plugin.wftong.controller.model.AppUser;
import com.ekfans.plugin.wftong.util.MyJSONObject;
import com.ekfans.pub.util.DateUtil;

/**
 * 危废通APP接口控制
 * @Title: AppServerController
 * @Company: 成都易科远见科技有限公司
 * @Description: 返回值为一个包装好的map转json对象
 * @author lh
 * @date 2016年3月31日
 * @version 1.0
 */
@Controller
public class AppServerController extends BasicController {
	@Autowired
	private IWftLogService wftLogService;

	// -----------传输主体参数-----------
	// requestJson名
	public static final String REQ_BODY = "reqBody";

	// -----------接收公共参数-----------
	// 用户令牌:用户ID+登录日期
	public static final String TOKEN_GET = "token";
	// 数据包名
	public static final String SOURCE_GET = "source";

	// -----------返回公共参数-----------
	// 0失败 1成功
	public static final String STATUS_RETURN = "resultStatus";
	// 公共参数名
	public static final String MSG_RETURN = "resultMsg";

	/**
	 * Wftong App服务主体
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/app/service", produces = "application/json;charset=UTF-8")
	public @ResponseBody
	String serviceGet(HttpServletRequest request, HttpServletResponse response) {
		// 请求主体
		MyJSONObject jsonRequest = null;
		// 响应主体:jsonObject比存map再转json快很多倍
		MyJSONObject jsonResponse = null;
		String source = null;
		String token = null;

		try {
			// 请求body
			String reqBody = request.getParameter(AppServerController.REQ_BODY);

			// 初始化传输主体
			jsonRequest = new MyJSONObject(reqBody);
			jsonResponse = new MyJSONObject();
			jsonResponse.put(AppServerController.STATUS_RETURN, "0");
			jsonResponse.put(AppServerController.MSG_RETURN, "");

			// 必要参数
			source = jsonRequest.getString(AppServerController.SOURCE_GET);
			token = jsonRequest.getString(AppServerController.TOKEN_GET);

			// 当前用户
			AppUser appUser = Cache.getAppUserByToken(token, request);

			// 验证：不用验证登录
			if (!"001".equals(source) && !"008".equals(source) && !"009".equals(source) && !"011".equals(source)) {
				if (appUser == null) {
					jsonResponse.put(AppServerController.MSG_RETURN, Cache.getAppResource("app.login.nologin"));
					// 未登陆
					jsonResponse.put(AppServerController.STATUS_RETURN, "2");
					return jsonResponse.toString();
				}
			}

			// 记录日志
			WftLog log = new WftLog();
			log.setContent(reqBody);
			log.setCreateTime(DateUtil.getSysDateTimeString());
			log.setSource(source);
			log.setUserId(appUser != null ? appUser.getUserId() : "");
			log.setUserName(appUser != null ? appUser.getPhone() : "");
			wftLogService.save(log);

			// 根据编码做出处理
			switch (source) {
			case "001":
				// 登录
				new AppUserController(jsonRequest, jsonResponse, appUser, request).login();
				break;
			case "002":
				// 获取资质
				new AppUserController(jsonRequest, jsonResponse, appUser, request).getZz();
				break;
			case "003":
				// 服务热线
				new AppUserController(jsonRequest, jsonResponse, appUser, request).getServerPhone();
				break;
			case "004":
				// 获取用户信息
				new AppUserController(jsonRequest, jsonResponse, appUser, request).getUserInfo();
				break;
			case "005":
				// 修改企业信息
				new AppUserController(jsonRequest, jsonResponse, appUser, request).updateStoreInfo();
				break;
			case "006":
				// 上传图片
				new AppCommonController(jsonRequest, jsonResponse, appUser, request).picUpload();
				break;
			case "007":
				// 财务认证信息
				new AppUserController(jsonRequest, jsonResponse, appUser, request).getStoreBankInfo();
				break;
			case "008":
				// 发送验证码
				new AppUserController(jsonRequest, jsonResponse, appUser, request).sendPhoneYan();
				break;
			case "009":
				// 更换密码
				new AppUserController(jsonRequest, jsonResponse, appUser, request).unsetPwd();
				break;
			case "010":
				// 更换密码
				new AppUserController(jsonRequest, jsonResponse, appUser, request).updatePwd();
				break;
			case "011":
				// app注册
				new AppUserController(jsonRequest, jsonResponse, appUser, request).appUserReg();
				break;
			case "300":
				// 查找用户
				new AppChatController(jsonRequest, jsonResponse, appUser, request).searchUserList();
				break;
			case "301":
				// 申请/添加/拒绝好友
				new AppChatController(jsonRequest, jsonResponse, appUser, request).operateFriend();
				break;
			case "302":
				// 删除好友
				new AppChatController(jsonRequest, jsonResponse, appUser, request).deleteFriend();
				break;
			case "303":
				// 根据环信ID查询用户
				new AppUserController(jsonRequest, jsonResponse, appUser, request).getUserInfo();
				break;
			case "304":
				// 我的好友列表
				new AppChatController(jsonRequest, jsonResponse, appUser, request).getFriendList();
				break;
			case "305":
				// 我的二度人脉列表
				new AppChatController(jsonRequest, jsonResponse, appUser, request).getSecondFriendList();
				break;
			case "306":
				// 刷新用户信息重新获取数据（如头像）
				new AppChatController(jsonRequest, jsonResponse, appUser, request).getFlashInfo();
				break;
			case "310":
				// 创建群组
				new AppChatController(jsonRequest, jsonResponse, appUser, request).addGroups();
				break;
			case "311":
				// 申请、同意加群
				new AppChatController(jsonRequest, jsonResponse, appUser, request).applyJoinGroup();
				break;
			case "312":
				// 加/减/T/解散
				new AppChatController(jsonRequest, jsonResponse, appUser, request).operateGroup();
				break;
			case "313":
				// 添加好友
				new AppChatController(jsonRequest, jsonResponse, appUser, request).addFriend();
				break;
			case "401":
				// 发布供应
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).addGy();
				break;
			case "402":
				// 查看供应
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).getGyList();
				break;
			case "403":
				// 我的供应
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).getMyGyList();
				break;
			case "404":
				// 供应详情
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).getGyDetails();
				break;
			case "405":
				// 原料分类列表
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).getGyCategory();
				break;
			case "451":
				// 发布需求
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).addXq();
				break;
			case "452":
				// 查看需求
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).getXqList();
				break;
			case "453":
				// 我的需求
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).getMyXqList();
				break;
			case "454":
				// 需求详情
				new AppSupplyController(jsonRequest, jsonResponse, appUser, request).getXqDetails();
				break;

			case "501":
				// 发布车源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).addCar();
				break;
			case "502":
				// 查看车源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).carList();
				break;
			case "503":
				// 我的车源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).myCarList();
				break;
			case "505":
				// 车源详情
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).carDetails();
				break;

			case "551":
				// 查找所有车辆类型
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).getCarTypeList();
				break;
			case "552":
				// 查找货物类别
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).getGoodsCatgList();
				break;
			case "553":
				// 危废品分类标准
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).getWfTypeList();
				break;
			case "555":
				// 查找罐体材质
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).getTankList();
				break;

			case "601":
				// 发布货源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).addGoods();
				break;
			case "602":
				// 查看货源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).goodsList();
				break;
			case "603":
				// 我的货源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).myGoodsList();
				break;
			case "605":
				// 货源详情
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).goodsDetails();
				break;

			case "611":
				// 收藏车源、货源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).collectTransport();
				break;
			case "612":
				// 转发车源、货源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).forwardingTransport();
				break;
			case "613":
				// 举报车源、货源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).reportTransport();
				break;
			case "614":
				// 车源、货源收藏列表
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).collectTransportList();
				break;
			case "615":
				// 上下架车源、货源
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).changeTransportStatus();
				break;
			case "616":
				// 车源议价
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).yiJia();
				break;
			case "617":
				// 转发列表
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).forwardingList();
				break;
			case "618":
				// 货源议价
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).baoJia();
				break;

			case "701":
				// 车辆报备
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).addCarInfo();
				break;
			case "702":
				// 车辆信息修改
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).udpCarInfo();
				break;
			case "703":
				// 车辆详情
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).carInfoDetails();
				break;
			case "704":
				// 获取车辆管理列表
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).carInfoList();
				break;
			case "705":
				// 删除车辆
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).delInfo();
				break;
			case "711":
				// 请求设备信息列表(注：设备相关接口的数据来自监控平台)
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).getDeviceList();
				break;
			case "712":
				// 绑定设备
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).updateDeviceToUse();
				break;
			case "713":
				// 获设备数据
				new AppTransportController(jsonRequest, jsonResponse, appUser, request).getDeviceById();
				break;

			case "800":
				// 获取处置,再循环,运输订单列表
				new AppWfOrderController(jsonRequest, jsonResponse, appUser, request).wfOrderList();
				break;
			case "802":
				// 获取处置,再循环,运输订单详情
				new AppWfOrderController(jsonRequest, jsonResponse, appUser, request).wfOrderDetails();
				break;
			case "804":
				// 发货
				new AppWfOrderController(jsonRequest, jsonResponse, appUser, request).wfOrderFh();
				break;
			case "805":
				// 收货
				new AppWfOrderController(jsonRequest, jsonResponse, appUser, request).wfOrderSh();
				break;
			case "806":
				// 取消订单
				new AppWfOrderController(jsonRequest, jsonResponse, appUser, request).wfOrderQx();
				break;

			case "996":
				// 设置个人隐私(电话显示 添加好友验证等隐私设置)
				new AppCommonController(jsonRequest, jsonResponse, appUser, request).privatySet();
				break;
			case "998":
				// 选择省市区
				new AppCommonController(jsonRequest, jsonResponse, appUser, request).getChildSysArea();
				break;
			case "999":
				// 检查更新
				new AppCommonController(jsonRequest, jsonResponse, appUser, request).checkUpdate();
				break;
			default:
				jsonResponse.put(AppServerController.MSG_RETURN, "Not define port:" + source);
				break;
			}
		} catch (JSONException e) {
			try {
				jsonResponse.put(AppServerController.MSG_RETURN, "JSONException");
			} catch (JSONException e1) {
			}
		} catch (ConnectException e) {
			try {
				jsonResponse.put(AppServerController.MSG_RETURN, "ConnectException");
			} catch (JSONException e1) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String resultJson = jsonResponse.toString();
		return resultJson;
	}
}
