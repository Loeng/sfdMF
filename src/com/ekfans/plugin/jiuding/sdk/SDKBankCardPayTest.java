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
 * 银行卡支付 绑卡接口
 *
 */
public class SDKBankCardPayTest {
    private static final Logger log = LoggerFactory.getLogger(SDKBankCardPayTest.class);

    protected String addSignEncoding = "GB18030";

    //private static final String host = "127.0.0.1:9090";
    private static final String host = "100.73.11.172:8080";

    protected String callURL = "http://" + host + "/sdk/h5/bankCardPay";

    protected String merchantId = "800010000020003";

    public static void main(String[] arg) throws Exception {
        log.info("开始测试");
        SDKBankCardPayTest test = new SDKBankCardPayTest();
        test.testBankCardPay();
    }

    private void testBankCardPay() {
        // TODO ------测试的时候，需要改变的字段------
        // token
        String payToken = "111111";
        // 商户用户号
        String merchantUserToken = "ABC562123asda789912345qeqwrqr1";
        // 商户订单号
        String mercOrderId = "eb92705b125a43b5b65ad43f9e713590";
        // 支付协议号
        String paymentContractId = "201707110000018213";
        // 短信验证码
        String checkCode = "111111";
        // TODO ------测试的时候，需要改变的字段------


        // 构造请求参数
        Map<String, String> paramMap = MixUtil.initHeader("BankCardPay", payToken, merchantId);
        // 商户用户号
        paramMap.put("merchantUserToken", merchantUserToken);
        // 商户订单号
        paramMap.put("mercOrderId", mercOrderId);
        // 支付方式的协议ID
        paramMap.put("paymentContractId", paymentContractId);
        // 短信校验码
        paramMap.put("checkCode", checkCode);
        // 交易金额 单位:分  可空-如果为空，就以预下单的为准；如果不为空，就校验前端传过来的和预下单的是否一致。
        paramMap.put("amount", "");


        callPaygateway(paramMap);
    }

    protected void callPaygateway(Map<String, String> paramMap) {
        List<String> signFields = new ArrayList<>();
        signFields.addAll(paramMap.keySet());
        String md5Sign = Md5SignUtil.md5Sign(paramMap, "", addSignEncoding, null);
        paramMap.put("sign", md5Sign);
        log.info("md5Sign={}", md5Sign);
        try {
            String responseStr = MerchantUtil.sendAndRecv(callURL, StringMapUtil.changeMapToString(paramMap), "00");
            log.info("调用:service={} 参数={} 响应报文={}", paramMap.get("service"), StringMapUtil.changeMapToString(paramMap), responseStr);
            log.info("响应报文={}", responseStr);
            Map<String, String> resultMap = StringMapUtil.stringToErrorFieldMap(responseStr);
            log.info("【返回值】rspCode={}, rspMessage={}", resultMap.get("rspCode"), resultMap.get("rspMessage"));
            if(resultMap.get("returnCode")!=null) {
                if (resultMap.get("returnCode").contains("00000")) {
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
