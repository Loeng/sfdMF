package com.ekfans.base.ccwcc.util;


import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.NativeHttpClient;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosAlert;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.ekfans.base.ccwcc.model.CcwccPush;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuguoyu on 2017/4/19.
 */
public class CcwccPushHelp {
    private static final String appKey = "9442a1699320c4060e52231e";
    private static final String masterSecret = "36d855d7a2a75de1b8033c79";
    public static final String TAG = "ccwcc_push";
    public static final String TITLE = "三分地核价宝消息通知";
    public static final String ALERT = "三分地核价宝消息通知";


    public static void push(CcwccPush ccwccPush, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
//        map.put("content", ccwccPush.getContent());
        map.put("type", ccwccPush.getType());
        map.put("sourceId", ccwccPush.getSourceId());
        BasicRequest br = new BasicRequest(request);
        map.put("linkUrl", !StringUtil.isEmpty(ccwccPush.getLinkUrl()) ? (br.getWebFullUrlPrex() + ccwccPush.getLinkUrl()) : "");
        map.put("sharUrl", !StringUtil.isEmpty(ccwccPush.getSharUrl()) ? (br.getWebFullUrlPrex() + ccwccPush.getSharUrl()) : "");
//        String content = JsonUtil.convertToJsonString(map);

        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(masterSecret, appKey, null, clientConfig);
        String authCode = ServiceHelper.getBasicAuthorization(appKey, masterSecret);
        NativeHttpClient httpClient = new NativeHttpClient(authCode, null, clientConfig);
        jpushClient.getPushClient().setHttpClient(httpClient);
        try {
            IosAlert alert = IosAlert.newBuilder()
                    .setTitleAndBody(ccwccPush.getTitle(), null, ccwccPush.getContent())
                    .build();
//        PushPayload payload = buildPushObject_all_all_alert(ccwccPush.getContent());
            PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.android_ios())
                    .setAudience(Audience.all()).setNotification(Notification.newBuilder()
//                            .setAlert(ccwccPush.getContent())
                            .addPlatformNotification(AndroidNotification.newBuilder()
                                    .setTitle(ccwccPush.getTitle())
                                    .setAlert(ccwccPush.getContent())
                                    .addExtras(map)
                                    .build())
                            .addPlatformNotification(IosNotification.newBuilder().setAlert(alert)
                                    .incrBadge(1).addExtras(map).build())
                            .build())
                    .build();

            PushResult result = jpushClient.sendPush(payload);
            jpushClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
