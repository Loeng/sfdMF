package com.ekfans.plugin.jiuding.sdk;

import com.ekfans.plugin.jiuding.utils.Md5SignUtil;
import com.ekfans.plugin.jiuding.utils.MerchantUtil;
import com.ekfans.plugin.jiuding.utils.StringMapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.*;

/**
 * SDK相关业务demo
 * Created by zhuzs on 2017/7/3.
 */
public class SDKDemo {
//
//    private static Logger logger = LoggerFactory.getLogger(SDKDemo.class);
//
//    //private static final String host = "127.0.0.1:9090";                    // 本地
//
//    private static final String host = "100.73.11.172:8080";                  // dev
//
//    private static final String merchantId = "800010000020003";
//
//    public static void main(String[] args) {
//        // 查询用户可选的支付方式
//        // queryPayChannels();
//
//        // 支付协议查询接
//       // payAgreementUrlQuery();
//
//        // 绑卡通道查询
//        //queryBindingCardChannel();
//
//        // 四要素鉴权接口
//         //authAndBindingCard();
//
//        // 微信或者支付宝支付
////        wxPayOrAlPay();
//    }
//
//
//
//
//    /**
//     * 查询用户可选的支付方式
//     */
//    @Test
//    public void queryPayChannels() {
//        String url = "http://" + host + "/sdk/h5/queryPayChannels"; // 联调时替换host
//
//        Map<String, String> paramMap = new HashMap<>();
//        // 设置通用参数
//        initCommonParam(paramMap);
//        // ------------------------------------华丽的分割线，以上为通用参数------------------------------------
//        // 业务参数
//
//        // 商户用户标示
//        paramMap.put("merchantUserToken", "ABC562123asda789912345qeqwrqr1");
//        paramMap.put("mercOrderId", "38279388683931902938");
//
//        // MD5 加签结果
//        String sign = Md5SignUtil.md5Sign(paramMap, "", "GBK", null);
//        paramMap.put("sign", sign);
//
//        String param = StringMapUtil.changeMapToString(paramMap);
//        logger.info("param={}", param);
//
//        String res = "";
//        try {
//            res = MerchantUtil.sendAndRecv(url, param, "00");
//            logger.info("返回报文:{}", res);
//        } catch (IOException e) {
//            logger.error("", e);
//        }
//
//        // 对返回结果进行验签
//        Map<String, Map<String,Object>> resultMap = JSON.parseObject(res,Map.class);
//        logger.info("resultMap:{}", resultMap);
//        //data域参与验签
//        Map dataMap = resultMap.get("data");
//        //将数组转成jsonStr处理特殊数据
//        String payItemList =JSON.toJSONString(dataMap.get("payItemList"));
//        dataMap.put("payItemList", payItemList);
//        Map errorMap =resultMap.get("error");
//        String resultSign =(String) dataMap.get("sign");
//        dataMap.remove("sign");
//        logger.info("removeResultMap:{}", resultMap);
//        if (Md5SignUtil.md5Sign(dataMap, "", "GB18030", null).equals(resultSign)) {
//            logger.info("验签成功");
//        } else {
//            logger.info("验签失败");
//            return;
//        }
//
//       //String payItemList =  (String) dataMap.get("payItemList");
//        if (StringUtils.isBlank(payItemList)) {
//            return;
//        }
//        List<Map<String, String>> mapList = (List<Map<String, String>>) JSON.parse(payItemList);
//        logger.info("业务参数：{}", mapList);
//
//    }
//
//    /**
//     * 绑卡通道查询
//     */
//    @Test
//    public void queryBindingCardChannel() {
//        String url = "http://" + host + "/sdk/h5/queryBindingCardChannel"; // 联调时替换host
//        Map<String, String> paramMap = new HashMap<>();
//        initCommonParam(paramMap);
//
//        // 商户用户标示
//        paramMap.put("merchantUserToken", "ABC562123asda789912345qeqwrqr1");
//        // 卡号，des加密
//        paramMap.put("cardNo", "6f7fb49bd3dc5088eb3eb54b7b208c1d906ad91d4dbb3063");
//
//        // MD5 加签结果
//        String sign = Md5SignUtil.md5Sign(paramMap, "", "GBK", null);
//        paramMap.put("sign", sign);
//
//        String param = StringMapUtil.changeMapToString(paramMap);
//        logger.info("param={}", param);
//
//        String res = "";
//        try {
//            res = MerchantUtil.sendAndRecv(url, param, "00");
//            logger.info("返回报文:{}", res);
//        } catch (IOException e) {
//            logger.error("", e);
//        }
//
//        // 对返回结果进行验签
//        Map<String, Map<String,Object>> resultMap = JSON.parseObject(res,Map.class);
//        logger.info("resultMap={}", resultMap);
//        Map  dataMap = resultMap.get("data");
//        String resultSign =(String)dataMap.get("sign");
//        dataMap.remove("sign");
//        logger.info("removeResultMap={}", resultMap);
//        if (Md5SignUtil.md5Sign(dataMap, "", "GB18030", null).equals(resultSign)) {
//            logger.info("验签成功");
//        } else {
//            logger.info("验签失败");
//        }
//
//    }
//
//    /**
//     * 支付协议查询接
//     */
//    @Test
//    public void payAgreementUrlQuery() {
//        String url = "http://" + host + "/paygateway/sdk/h5/queryPayAgreementUrl"; // 联调时替换host
//        //String url ="http://127.0.0.1:7777/paygateway-web/sdk/h5/queryPayAgreementUrl";
//        Map<String, String> paramMap = new HashMap<>();
//        initCommonParam(paramMap);
//
//        //协议id
//        paramMap.put("protocolId","201706050000017593");
//
//
//        // MD5 加签结果
//        String sign = Md5SignUtil.md5Sign(paramMap, "", "GBK", null);
//        paramMap.put("sign", sign);
//
//        String param = StringMapUtil.changeMapToString(paramMap);
//        logger.info("param=" + param);
//
//        String res = "";
//        try {
//            res = MerchantUtil.sendAndRecv(url, param, "00");
//            logger.info("返回报文:" + res);
//        } catch (IOException e) {
//            logger.error("返回报文", e);
//        }
//
//        // 对返回结果进行验签
//        Map<String, Map<String,Object>> resultMap = JSON.parseObject(res,Map.class,Feature.InitStringFieldAsEmpty);
//        logger.info("resultMap=" + resultMap);
//        Map  dataMap = resultMap.get("data");
//        String resultSign = (String)dataMap.get("sign");
//        dataMap.remove("sign");
//        logger.info("removeResultMap=" + dataMap);
//        if (Md5SignUtil.md5Sign(dataMap, "", "GB18030", null).equals(resultSign)) {
//            logger.info("验签成功");
//        } else {
//            logger.info("验签失败");
//        }
//    }
//
//
//    /**
//     * 微信支付或支付宝支付
//     */
//    @Test
//    public void wxPayOrAlPay() {
//        String url = "http://" + host + "/sdk/h5/weChatPayOrAliPay"; // 联调时替换为对应地址
//        Map<String, String> paramMap = new HashMap<>();
//        initCommonParam(paramMap);
//
//        // TODO ------测试的时候，需要改变的字段------
//        // 商户用户号
//        String merchantUserToken = "ABC562123asda789912345qeqwrqr1";
//        // 商户订单号
//        // payOrderId 170707160220439257 merchantOrderId cf25d4a09b754f98961738ada0a00fb0
//        String mercOrderId = "cf25d4a09b754f98961738ada0a00fb0";
//        // 短信验证码
//        String checkCode = "111111";
//        // TODO ------测试的时候，需要改变的字段------
//
//        paramMap.put("merchantId", merchantId);
//        //商户用户表识
//        paramMap.put("memberId", merchantUserToken);
//        //商户订单号
//        paramMap.put("mercOrderId", mercOrderId);
//        //短信校验码
//        paramMap.put("checkCode", checkCode);
//        //通道类型SDKP：支付sdk支付
//        paramMap.put("payType", "SDKP");
//        //交易金额.以分为单位
//        paramMap.put("amount", "100");
//        //交易币种默认CNY, 即人民币
//        paramMap.put("currency", "CNY");
//        //交易时间 格式YYYYMMDDHHmmss
//        SimpleFastDateFormat sfdf = SimpleFastDateFormat.getInstance("yyyyMMddHHmmss");
//        paramMap.put("orderTime", sfdf.format(new Date()));
//        //客户端IP商户生成的用户ID
//        paramMap.put("clientIP", UUID.randomUUID().toString());
//        //订单有效期单 只能取以下枚举值 00-分01-小时
//        paramMap.put("validUnit", "00");
//        //订单有效期数量
//        paramMap.put("validNum", "30");
//        //异步通知url 交易结果通过后台通知到这个
//        paramMap.put("offlineNotifyUrl", "www.baidu.com");
//        //支付渠道代码微信WXP 支付宝 ALP
//        paramMap.put("payChannelType", "WXP");
//
//        // MD5 加签结果
//        String sign = Md5SignUtil.md5Sign(paramMap, "", "GBK", null);
//        paramMap.put("sign", sign);
//
//        String param = StringMapUtil.changeMapToString(paramMap);
//        logger.info("param=" + param);
//
//        String res = "";
//        try {
//            res = MerchantUtil.sendAndRecv(url, param, "00");
//            logger.info("返回报文:" + res);
//        } catch (IOException e) {
//            logger.error("", e);
//        }
//
//        // 对返回结果进行验签
//        Map<String, Map<String,Object>> resultMap = JSON.parseObject(res,Map.class);
//        logger.info("resultMap=" + resultMap);
//        Map dataMap =resultMap.get("data");
//        String resultSign = (String)dataMap.get("sign");
//        dataMap.remove("sign");
//        logger.info("removeResultMap=" + dataMap);
//        if (Md5SignUtil.md5Sign(dataMap, "", "GB18030", null).equals(resultSign)) {
//            logger.info("验签成功");
//        } else {
//            logger.info("验签失败");
//        }
//
//    }
//
//    /**
//     * 四要素鉴权接口
//     */
//    @Test
//    public void authAndBindingCard() {
//        String url = "http://" + host + "/sdk/h5/authAndBindingCard"; // 联调时替换host
//        Map<String, String> paramMap = new HashMap<>();
//        initCommonParam(paramMap);
//        paramMap.put("merchantUserToken", "ABC562123asda789912345qeqwrqr1"); // 商户生成的用户为唯一标示
//        paramMap.put("idType", "00"); // 证件类型
//        paramMap.put("idNo", "e85451b65df7f40fb666b31db73a949db1acd4b88c47904a"); // 用户填写的证件号码(DES加密)
//        paramMap.put("userName", "贾利娟"); // 姓名
//        paramMap.put("phone", "13552550781"); // 手机号码
//        paramMap.put("cardNo", "a1a0d5f025215770b0bc0d654fa64934906ad91d4dbb3063"); // 银行卡号
//        paramMap.put("cardType", "0"); // 卡类型 0.储蓄卡，1.信用卡
////        paramMap.put("expireDate","0715"); // 有效期,4位定长，2015年7月为0715【贷记卡用】
////        paramMap.put("cvn2","1234"); // 有效期,4位定长，2015年7月为0715【贷记卡用】
//        paramMap.put("mercOrderId","2017080121514521");
//        // MD5 加签结果
//        String sign = Md5SignUtil.md5Sign(paramMap, "", "GBK", null);
//        paramMap.put("sign", sign);
//
//        String param = StringMapUtil.changeMapToString(paramMap);
//        logger.info("param={}", param);
//
//        String res = "";
//        try {
//            res = MerchantUtil.sendAndRecv(url, param, "00");
//            logger.info("返回报文:{}", res);
//        } catch (IOException e) {
//            logger.error("", e);
//        }
//
//        // 对返回结果进行验签
//        Map<String, Map<String,Object>> resultMap = JSON.parseObject(res,Map.class);
//        logger.info("resultMap={}", resultMap);
//
//        Map dataMap =resultMap.get("data");
//        String resultSign = (String)dataMap.get("sign");
//        dataMap.remove("sign");
//        logger.info("removeResultMap={}",dataMap);
//        if (Md5SignUtil.md5Sign(dataMap, "", "GB18030", null).equals(resultSign)) {
//
//            logger.info("验签成功");
//        } else {
//            logger.info("验签失败");
//        }
//    }
//
//    private static void initCommonParam(Map<String, String> paramMap) {
//        // 字符集 默认00， 表示GB18030
//        paramMap.put("charset", "00");
//        // 版本号 默认为：1.0，如有版本变更，会在对应接口中声明
//        paramMap.put("version", "1.0");
//        // 商户号 商户在九派注册的商户编
//        paramMap.put("merchantId", merchantId);
//        // 请求时间 20160505120101
//        paramMap.put("requestTime", "20170703120101");
//        // 请求编号 当日唯一
//        paramMap.put("requestId", "2017070312010101511");
//        // 请求类型 各接口自定义
//        paramMap.put("service", "serviceName");
//        // 签名类型 默认RSA256 也支持RSA（算法SHA-1）
//        paramMap.put("signType", "MD5");
//        // 扩展字段 扩展字段，格式：key1=value&key2=value2的base64编码
//        paramMap.put("ext", new BASE64Encoder().encode("a=1&b=2".getBytes()));
//        //  支付 token 支付token，商户Server调用支付预下单接口返回的凭证，有时效性限制
//        paramMap.put("payToken", "111111");
//    }
}
