package com.ekfans.plugin.wuliubao.yunshu.controller.util;

import java.util.Map;
import org.glassfish.jersey.client.JerseyWebTarget;
import com.ekfans.plugin.wftong.base.easemob.model.EndPoints;
import com.ekfans.plugin.wftong.base.easemob.model.HTTPMethod;
import com.ekfans.plugin.wftong.base.easemob.util.JerseyUtils;
import com.ekfans.plugin.wftong.util.HxUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * 极光推送工具类
 * @author pp
 * @date 2017年8月2日15:36:47
 *
 */
public class JGUtil {
	 //运输端AppKey
	 private static String YS_APPKEY = "4f1bef3c5c112ea9a41231ea";
	 //运输端Master Secret
	 private static String YS_MASTER_SECRET = "b61e513784fa35ac34f2b11f";
	 
	 //产生端AppKey
	 private static String CS_APPKEY = "1a1ca260143c788ee0dc1973";
	 //产生端Master Secret
	 private static String CS_MASTER_SECRET = "0fc2f49ecefed010fee9df9c";
	 
	 //快捷地构建推送对象：所有平台，所有设备，内容为 message 的通知和消息  附属参数为map。
	 public static PushPayload[] buildPushObject_all_all_alertOrMessage(Map<String,String> map,String message) throws Exception {
		 PushPayload[] p = new PushPayload[2];
		
		//推送ios
			p[0]=PushPayload.newBuilder()
		                .setPlatform(Platform.ios()).setOptions(Options.newBuilder().setApnsProduction(true).build())
		                .setAudience(Audience.all())
		                .setNotification(Notification.newBuilder()
		                        .addPlatformNotification(IosNotification.newBuilder()
		                                .setAlert(message)
		                                .setBadge(5)
		                                .setSound("happy")
		                                .addExtras(map)
		                                .build())
		                        .build())
		                .setMessage(Message.newBuilder()
		                        .setMsgContent(message)
		                        .addExtras(map)
		                        .build())
		                .build();
				//推送android
				p[1]=PushPayload.newBuilder()
		                .setPlatform(Platform.android())
		                .setAudience(Audience.all())
		                .setNotification(Notification.newBuilder()
		                        .addPlatformNotification(AndroidNotification.newBuilder()
		                                .setAlert(message)
		                                .addExtras(map)
		                                .build())
		                        .build())
		                .setMessage(Message.newBuilder()
		                        .setMsgContent(message)
		                        .addExtras(map)
		                        .build())
		                .build();
			return p;
	 }
	 //构建推送对象：所有平台，推送目标是系统标识号为 "registrationID" 的通知和消息  附属参数为map。
	public static PushPayload[] buildPushObject_all_registrationId_alertOrMessage(String registrationID,Map<String,String> map,String message) {
		   
		PushPayload[] p = new PushPayload[2];
		//推送ios
		p[0]=PushPayload.newBuilder()
	                .setPlatform(Platform.ios()).setOptions(Options.newBuilder().setApnsProduction(true).build())
	                .setAudience(Audience.registrationId(registrationID))
	                .setNotification(Notification.newBuilder()
	                        .addPlatformNotification(IosNotification.newBuilder()
	                                .setAlert(message)
	                                .setBadge(5)
	                                .setSound("happy")
	                                .addExtras(map)
	                                .build())
	                        .build())
	                .setMessage(Message.newBuilder()
	                        .setMsgContent(message)
	                        .addExtras(map)
	                        .build())
	                .build();
			//推送android
			p[1]=PushPayload.newBuilder()
	                .setPlatform(Platform.android())
	                .setAudience(Audience.registrationId(registrationID))
	                .setNotification(Notification.newBuilder()
	                        .addPlatformNotification(AndroidNotification.newBuilder()
	                                .setAlert(message)
	                                .addExtras(map)
	                                .build())
	                        .build())
	                .setMessage(Message.newBuilder()
	                        .setMsgContent(message)
	                        .addExtras(map)
	                        .build())
	                .build();
		
		return p;
	 }
	
	
	    
	    /**
	     * 物流宝极光消息推送
	     * @param registrationID 接收设备的唯一标识
	     * @param message 消息内容
	     * @return
	     */
		public static void wlbAPPsendMessages(String registrationID,Map<String,String> map,String message){
			JerseyWebTarget webTarget = EndPoints.JG_SEND_MESSAGES;
			ObjectNode objectNode = HxUtil.factory.objectNode();
			// 构造消息体
			PushPayload[] p = buildPushObject_all_registrationId_alertOrMessage(registrationID,map,message);
			for(PushPayload push : p){
				objectNode = JerseyUtils.jGSendRequest(webTarget, push.toString(), YS_APPKEY+":"+YS_MASTER_SECRET, HTTPMethod.METHOD_POST, null);
				System.out.println("ys返回参数:"+objectNode.toString());
				objectNode = JerseyUtils.jGSendRequest(webTarget, push.toString(), CS_APPKEY+":"+CS_MASTER_SECRET, HTTPMethod.METHOD_POST, null);
				System.out.println("cs返回参数:"+objectNode.toString());
			}
			
		}
	    
		/**
	     * 物流宝极光消息推送
	     * @param message 消息内容
	     * @return
		 * @throws Exception 
	     */
		public static void wlbSendMessages(Map<String,String> map,String message) throws Exception{
			JerseyWebTarget webTarget = EndPoints.JG_SEND_MESSAGES;
			ObjectNode objectNode = HxUtil.factory.objectNode();
			// 构造消息体
			PushPayload[] p = buildPushObject_all_all_alertOrMessage(map,message);
			for(PushPayload pu : p){
				objectNode = JerseyUtils.jGSendRequest(webTarget, pu.toString(), YS_APPKEY+":"+YS_MASTER_SECRET, HTTPMethod.METHOD_POST, null);
				System.out.println("ys返回参数:"+objectNode.toString());
				objectNode = JerseyUtils.jGSendRequest(webTarget, pu.toString(), CS_APPKEY+":"+CS_MASTER_SECRET, HTTPMethod.METHOD_POST, null);
				System.out.println("cs返回参数:"+objectNode.toString());
			}
		}
		
}
