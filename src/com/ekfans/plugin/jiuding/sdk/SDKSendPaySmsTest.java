package com.ekfans.plugin.jiuding.sdk;

import com.ekfans.plugin.jiuding.utils.Md5SignUtil;
import com.ekfans.plugin.jiuding.utils.MerchantUtil;
import com.ekfans.plugin.jiuding.utils.MixUtil;
import com.ekfans.plugin.jiuding.utils.StringMapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by caoshusheng on 17/7/3.
 * 【MD5认证的方式调用】
 *
 * 发送短信验证码
 *
 */
public class SDKSendPaySmsTest {
    private static final Logger log = LoggerFactory.getLogger(SDKSendPaySmsTest.class);

    private String addSignEncoding = "GB18030";

    //private static final String host = "127.0.0.1:9090";
    private static final String host = "100.73.11.172:8080";

    private String callURL = "http://" + host + "/sdk/h5/sendPaySms";

    private String merchantId = "800010000020003";

    public static void main(String[] arg) throws Exception {
        SDKSendPaySmsTest test = new SDKSendPaySmsTest();
        test.testSendPaySms();
    }

    private void testSendPaySms() {
        // TODO ------测试的时候，需要改变的字段------
        // token
        String payToken = "111111";
        // 商户订单号
        String mercOrderId = "3fdbff8862be497caf379f7f799d0234";
        // 商户用户标识
        String merchantUserToken = "123456789";
        // 支付方式协议号
        String paymentContractId = "201706280000018004";

        // 构造请求参数
        Map<String, String> paramMap = MixUtil.initHeader("SendPaySms", payToken, merchantId);
        // 商户订单号
        paramMap.put("mercOrderId", mercOrderId);
        // 商户用户标识
        paramMap.put("merchantUserToken", merchantUserToken);
        // 支付方式协议号
        paramMap.put("paymentContractId", paymentContractId);

        callPaygatewaySendPaySms(paramMap);
    }

    protected void callPaygatewaySendPaySms(Map<String, String> paramMap) {
        List<String> signFields = new ArrayList<>();
        signFields.addAll(paramMap.keySet());
        String md5Sign = Md5SignUtil.md5Sign(paramMap, "", addSignEncoding, null);
        paramMap.put("sign", md5Sign);
        log.info("md5Sign={}", md5Sign);
        try {
            String responseStr = MerchantUtil.sendAndRecv(callURL, StringMapUtil.changeMapToString(paramMap), "00");
            log.info("调用:service={} 参数={} 响应报文={}", paramMap.get("service"), StringMapUtil.changeMapToString(paramMap), responseStr);
            Map<String, String> resultMap = StringMapUtil.stringToDataFieldMap(responseStr);
            log.info("【返回值】rspCode={}, rspMessage={}", resultMap.get("rspCode"), resultMap.get("rspMessage"));
            if(resultMap.get("rspCode")!=null) {
                if (resultMap.get("rspCode").contains("00000")) {
                    log.info("结果-成功");
                }
            }else {
                log.error("结果-失败");
            }
            log.info("resultMap={}", resultMap);
            String resultSign = resultMap.get("sign");
            resultMap.remove("sign");
            log.info("removeResultMap={}", resultMap);
            if (Md5SignUtil.md5Sign(resultMap, "", "GB18030", null).equals(resultSign)) {
                log.info("验签-成功");
            } else {
                log.error("验签-失败");
            }
        } catch (Exception ex) {
            log.error("异常-调用网关出现异常", ex);
        }
    }


}
