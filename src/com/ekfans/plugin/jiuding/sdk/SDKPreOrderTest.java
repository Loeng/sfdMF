package com.ekfans.plugin.jiuding.sdk;

import com.ekfans.plugin.jiuding.utils.MerchantUtil;
import com.ekfans.plugin.jiuding.utils.RSASignUtil;
import com.ekfans.plugin.jiuding.utils.StringMapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by caoshusheng on 17/7/3.
 * 【商户证书认证的方式调用】
 *
 *  预下单
 *
 */
public class SDKPreOrderTest {

    private static final Logger log = LoggerFactory.getLogger(SDKPreOrderTest.class);

    private static  String addSignEncoding = "GB18030";

    //private static final String host = "127.0.0.1:9090";
//    private static final String host = "100.73.11.172:8080";
//    private static final String host = "100.73.49.207/paygateway"; // test
    private static final String host = "43.227.141.72/paygateway";

    private static  String preorderURL = "http://"+ host + "/sdk/secapi/preorder";

    private static  String merchantId = "800010000020003";

    private static String relativePath;
    static {
        relativePath = SDKPreOrderTest.class.getClassLoader().getResource("").getFile();
        relativePath = relativePath.substring(0, relativePath.lastIndexOf("/"));
        relativePath = relativePath.substring(0, relativePath.lastIndexOf("/"));
        relativePath = relativePath.substring(0, relativePath.lastIndexOf("/"));
    }

    private static String profileBase = relativePath + "/src/main/webapp/WEB-INF/cert/";

    private static String merchantCertPass = "CI02yf";

    private static String merchantCertPath = profileBase + merchantId + ".p12";
    private static String rootCertPath = profileBase + "rootca.cer";

    public static void main(String[] arg) throws Exception {
        SDKPreOrderTest test = new SDKPreOrderTest();
        test.testSDKPreOrder();
    }

    private void testSDKPreOrder() throws Exception {
        Map<String, String> paramMap = initPreOrderRequestPara("preorder");
        // 商户用户号
        paramMap.put("merchantUserToken", "AB52123asda789912345qeqwrqr1");
        String mercOrderId = UUID.randomUUID().toString().replace("-", "");
        // 商户订单号
        paramMap.put("mercOrderId", mercOrderId);
        log.info("商户订单号={}", mercOrderId);
        // 支付类型
        paramMap.put("payType", "SDKP");
        // 交易金额 单位:分
        paramMap.put("amount", "100");
        // 交易币种
        paramMap.put("currency", "CNY");
        // 订单时间  YYYYMMDDHHmmss
        paramMap.put("orderTime", org.apache.commons.lang3.time.DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        // 客户IP
        paramMap.put("clientIP", "127.0.0.1");
        // 订单有效期单位 00-分 01-小时 02-日 03-月
        paramMap.put("validUnit", "01");
        // 订单有效期数量
        paramMap.put("validNum", "24");
        // 商品名称
        paramMap.put("goodsName", "aa");
        // 商品描述
        paramMap.put("goodsDesc", "aaaa");
        // 异步通知URL
        paramMap.put("offlineNotifyUrl", "httpL://127.0.0.1/test");


        callPayGateWayPreOrder(paramMap);
    }

    protected void callPayGateWayPreOrder(Map<String, String> paramMap) throws Exception {
        try {

            Map requestMap = new HashMap();
            requestMap.putAll(paramMap);
            Set set = paramMap.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if ((paramMap.get(key) == null) || paramMap.get(key).toString().trim().length() == 0) {
                    requestMap.remove(key);
                }
            }
            RSASignUtil util = new RSASignUtil(merchantCertPath, merchantCertPass);
            String reqData = RSASignUtil.coverMap2String(requestMap);
            String merchantSign = util.sign(reqData, "GB18030");
            String merchantCert = util.getCertInfo();
            String buf = reqData + "&merchantSign=" + merchantSign + "&merchantCert=" + merchantCert;

            String responseStr = MerchantUtil.sendAndRecv(preorderURL, buf, "00");
            log.info("返回报文={}", responseStr);
            Map<String, String> retMap = StringMapUtil.changeStringToMap(responseStr);
            log.info("【返回值】rspCode={}, rspMessage={}", retMap.get("rspCode"), retMap.get("rspMessage"));
            if (retMap.get("rspCode").contains("00000")) {
                log.info("结果-成功");
            } else {
                log.warn("结果-失败");
            }
            Map<String, String> responseMap = new HashMap<>();
            responseMap.putAll(retMap);
            Set set1 = retMap.keySet();
            Iterator iterator1 = set1.iterator();
            while (iterator1.hasNext()) {
                String key0 = (String) iterator1.next();
                String tmp = retMap.get(key0);
                if (StringUtils.equals(tmp, "null") || StringUtils.isBlank(tmp)) {
                    responseMap.remove(key0);
                }

            }
            String sf = RSASignUtil.coverMap2String(responseMap);
            // -- 验证签名
            boolean flag;
            RSASignUtil rsautil = new RSASignUtil(rootCertPath);
            flag = rsautil.verify(sf, util.getValue(responseStr, "serverSign"), util.getValue(responseStr, "serverCert"), "GB18030");
            if (!flag) {
                log.error("验签-失败");
                return;
            } else {
                log.info("验签-成功");
            }
        } catch (Exception ex) {
            log.error("异常-调用网关出现异常", ex);
        }
    }

    protected Map<String, String> initPreOrderRequestPara(String service) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("charset", "00");
        paramMap.put("version", "1.0");
        paramMap.put("merchantId", merchantId);
        paramMap.put("requestTime", org.apache.commons.lang3.time.DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
        paramMap.put("requestId", String.valueOf(System.currentTimeMillis()));
        paramMap.put("service", service);
        paramMap.put("signType", "RSA256");
        return paramMap;
    }
}