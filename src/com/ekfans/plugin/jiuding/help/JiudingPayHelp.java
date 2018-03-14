package com.ekfans.plugin.jiuding.help;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.service.IWfOrderJiudingPayRelService;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.base.wfOrder.util.WfOrderHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.jiuding.utils.RSASignUtil;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

import static com.ekfans.basic.spring.SpringContextHolder.getBean;

public class JiudingPayHelp {
    //B2B支持银行对应简称
    public static Map<String, String> B2BBanks = new HashMap<>();
    //B2C支持银行对应简称
    public static Map<String, String> B2CBanks = new HashMap<>();
    //C2B借记卡支持银行对应简称
    public static Map<String, String> C2BJJBanks = new HashMap<>();
    //C2B贷记卡支持银行对应简称
    public static Map<String, String> C2BDJBanks = new HashMap<>();

    static {
        B2BBanks.put("工商银行", "ICBC");
        B2BBanks.put("中国农业银行", "ABC");
        B2BBanks.put("建设银行", "CCB");
        B2BBanks.put("交通银行", "BOCOM");
        B2BBanks.put("招商银行", "CMB");
        B2BBanks.put("中信银行", "CNCB");
        B2BBanks.put("中国民生银行", "CMBC");
        B2BBanks.put("华夏银行", "HXB");
        B2BBanks.put("中国光大银行", "CEB");
        B2BBanks.put("中国银行", "BOC");
        B2BBanks.put("上海浦东发展银行", "SPDB");
        B2BBanks.put("平安银行", "PAB");
        B2BBanks.put("东亚银行（中国）", "BEA");
        B2BBanks.put("广发银行", "GDB");

        B2CBanks.put("中国银行", "BOC");
        B2CBanks.put("建设银行", "CCB");
        B2CBanks.put("光大银行", "CEB");
        B2CBanks.put("广发银行", "GDB");
        B2CBanks.put("平安银行", "PABC");
        B2CBanks.put("工商银行", "ICBC");
        B2CBanks.put("民生银行", "CMBC");
        B2CBanks.put("交通银行", "BCOM");
        B2CBanks.put("邮储银行", "PSBC");
        B2CBanks.put("招商银行", "CMB");
        B2CBanks.put("北京银行", "BOB");
        B2CBanks.put("上海银行", "BOS");
        B2CBanks.put("农业银行", "ABC");
        B2CBanks.put("华夏银行", "HXB");

        C2BJJBanks.put("中国银行", "BOC");
        C2BJJBanks.put("建设银行", "CCB");
        C2BJJBanks.put("光大银行", "CEB");
        C2BJJBanks.put("广发银行", "GDB");
        C2BJJBanks.put("平安银行", "PABC");
        C2BJJBanks.put("工商银行", "ICBC");
        C2BJJBanks.put("民生银行", "CMBC");
        C2BJJBanks.put("交通银行", "BCOM");
        C2BJJBanks.put("邮储银行", "PSBC");
        C2BJJBanks.put("招商银行", "CMB");
        C2BJJBanks.put("北京银行", "BOB");
        C2BJJBanks.put("上海银行", "BOS");
        C2BJJBanks.put("农业银行", "ABC");
        C2BJJBanks.put("华夏银行", "HXB");

        C2BDJBanks.put("农业银行", "ABC");
        C2BDJBanks.put("中国银行", "BOC");
        C2BDJBanks.put("建设银行", "CCB");
        C2BDJBanks.put("光大银行", "CEB");
        C2BDJBanks.put("工商银行", "ICBC");
        C2BDJBanks.put("交通银行", "BCOM");
        C2BDJBanks.put("邮储银行", "PSBC");
        C2BDJBanks.put("北京银行", "BOB");
        C2BDJBanks.put("上海银行", "BOS");


    }

    public static Map<String, Object> getJiudingPayInfo(String orderId, String bankAbbr, String cardType, String ofPayType, HttpServletRequest request) {
        String storeId = null;
        String userId = null;
        String createTime = null;
        BigDecimal totalPrice = null;

        try {
            // 由于危废订单和普通订单的id创建规则有明确区分。故这里可以通过单表查找的数据是否为空为依据来区分不同的订单类型
            IOrderService orderService = getBean(IOrderService.class);
            IWfOrderService wfOrderService = getBean(IWfOrderService.class);

            Order order = orderService.getOrderByOrderId(orderId);
            WfOrder wfOrder = wfOrderService.getWfOrderById(orderId);

            if (order != null) {
                if (OrderConst.ORDER_STATUS_FINISH.equals(order.getStatus()) || OrderConst.ORDER_STATUS_WAIT_SEND.equals(order.getStatus())) {
                    return null;
                }
                storeId = order.getStoreId();
                userId = order.getUserId();
                createTime = order.getCreateTime();
                totalPrice = order.getTotalPrice().abs();
            } else if (wfOrder != null) {
                if (WfOrderHelper.WFORDER_STATUS_WAIT_FH.equals(wfOrder.getStatus()) || WfOrderHelper.WFORDER_STATUS_WAIT_FINISH.equals(wfOrder.getStatus())) {
                    return null;
                }
                storeId = wfOrder.getSaleId();
                userId = wfOrder.getBuyId();
                createTime = wfOrder.getCreateTime();
                // 查询危费订单类型
                String payType = wfOrderService.getPayTypeById(orderId);
                // 危费订单根据支付类型的不同，会有不同的支付金额
                totalPrice = wfOrderService.getPayPrice(orderId, payType);

                // 虚拟一个orderId：危费订单拥有多次支付情况，并且支付平台限制了orderId只能使用一次。
                // 解决方法是：先创建一个关联的虚拟orderId与真实的orderId相关联，回调时再反向取回原wfOrderId;
                orderId = getBean(IWfOrderJiudingPayRelService.class).create(orderId).getId();
            }

            if ("1".equalsIgnoreCase(ofPayType)) {
                ofPayType = "B2B";
            } else {
                ofPayType = "B2C";
            }

            Map requestMap = new HashMap();
            BasicRequest br = new BasicRequest(request);
            String pageReturnUrl = br.getWebFullUrlPrex() + "/web/jiudingpay/callback";
            String notifyUrl = br.getWebFullUrlPrex() + "/web/jiudingpay/notify";

            String service = "rpmBankPayment";

            String merchantCertPass = Cache.getResource("jiuding.sdk.merchantCertPass");
            String path = JiudingPayHelp.class.getClassLoader().getResource("/").getPath();
            if (path.endsWith("/") || path.endsWith("\\")) {
                path = path + "jiudingPay/";
            } else {
                path = path + "/jiudingPay/";
            }
            String merchantCertPath = path + Cache.getResource("jiuding.sdk.merchantCertPath");

            String requestId = String.valueOf(System.currentTimeMillis());
            IStoreService storeService = getBean(IStoreService.class);
            Store store = storeService.getStore(storeId);

            Map<String, Object> dataMap = new LinkedHashMap<String, Object>();

            //字符集  charset String(10) 默认00，表示GB18030，02 表示UTF-8 可空
            dataMap.put("charset", "02");
            //版本号 version String(10) 默认为:1.0，如有版本变 更，会在对应接口中声明 不可空
            dataMap.put("version", "1.0");
            //商户号 merchantId String(15) 商户在九派注册的商户编号  不可空
            dataMap.put("merchantId", Cache.getResource("jiuding.sdk.merchantId"));
            //请求时间 requestTime String(14) 如:20160505120101 不可空
            dataMap.put("requestTime", DateUtil.getFullSysDateTimeStringNew());
            //请求编号 requestId String(50) 当日唯一 不可空
            dataMap.put("requestId", requestId);
            //请求类型 service String(32) 各接口定义字段，如:网 银支付(rpmBankPayment) 不可空
            dataMap.put("service", service);
            //签名类型 signType String(3) 默认RSA256也支持RSA(算法SHA-1)推荐使用RSA256 不可空
            dataMap.put("signType", "RSA256");
            //二级商户 subMerchantId String(15) 二级商户id(无二级商户 不需要传)


            //页面返回地址 pageReturnUrl String(100) 页面重定向URL，支付结果 同步返回到该url，该url由 商户提供 不可空
            dataMap.put("pageReturnUrl", pageReturnUrl);
            //后台通知地址 notifyUrl String(100) 后台异步通知URL，交易结 果通过后台通知到此url， 为确保安全该字段支持 (DES加密，ECB模式) 不可空
            dataMap.put("notifyUrl", notifyUrl);
            //商户展示名称  merchantName String(32) 不可空
            dataMap.put("merchantName", store != null ? store.getStoreName() : storeId);
            //买家用户标识 memberId String(32) 商户生成的用户ID 可空
            dataMap.put("memberId", userId);
            //订单提交日期 orderTime String(14) 不可空
            String orderTime = createTime.replaceAll("-", "");
            orderTime = orderTime.replaceAll(" ", "");
            orderTime = orderTime.replaceAll(":", "");
            dataMap.put("orderTime", orderTime);
            //商户订单号 orderId String(32) 不可空
            dataMap.put("orderId", orderId);
            //订单金额 totalAmount Number(20) 以分为单位，只能传整数 不可空
            dataMap.put("totalAmount", String.valueOf((totalPrice.multiply(new BigDecimal(100)).intValue())));
            //币种 currency String(10) CNY:人民币 不可空
            dataMap.put("currency", "CNY");
            //银行简称 bankAbbr String(20) 如ICBC等 不可空
            dataMap.put("bankAbbr", bankAbbr);
            //卡类型 cardType String(2) 0:借记卡 1:贷记卡 不可空
            dataMap.put("cardType", cardType);
            //支付类型 payType String(4) B2C/B2B 不可空
            dataMap.put("payType", ofPayType);
            //有效期单位 validUnit String(2) 00:分 01:小时 02:日 03:月 不可空
            dataMap.put("validUnit", "02");
            //有效期数量 validNum String(3) 不可空
            dataMap.put("validNum", "1");
            //商品展示地址 showUrl String(400) 可空
            dataMap.put("showUrl", "");
            //商品名称 goodsName String(20) 不可空
            dataMap.put("goodsName", "三分地环保产业链在线");
            //商品编号 goodsId String(80) 可空
            dataMap.put("goodsId", "");
            //商品描述 goodsDesc String(680) 可空
            dataMap.put("goodsDesc", "");


            requestMap.putAll(dataMap);
            Set set = dataMap.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if ((dataMap.get(key) == null) || dataMap.get(key).toString().trim().length() == 0) {
                    requestMap.remove(key);
                }

            }
            RSASignUtil util = new RSASignUtil(merchantCertPath, merchantCertPass);

            String reqData = util.coverMap2String(requestMap);
            util.setService(service);
            String merchantSign = util.sign(reqData, "UTF-8");
            String merchantCert = util.getCertInfo();
            //请求报文
            String buf = reqData + "&merchantSign=" + merchantSign + "&merchantCert=" + merchantCert;

            //签名信息 merchantSign String 使用商户证书对报文签名后的值 不可空
            requestMap.put("merchantSign", merchantSign);
            //商户证书 merchantCert String DER编码X.509，不参与签名 不可空
            requestMap.put("merchantCert", merchantCert);
            return requestMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Boolean payCallBack(HttpServletRequest request) {
//        MerchantConfig.getConfig().loadPropertiesFromSrc();
//        String charset = MerchantConfig.getConfig().getCharset();
//        String rootCertPath = request.getSession().getServletContext().getRealPath("/") + MerchantConfig.getConfig().getRootCertPath();
        String path = JiudingPayHelp.class.getClassLoader().getResource("/").getPath();
        if (path.endsWith("/") || path.endsWith("\\")) {
            path = path + "jiudingPay/";
        } else {
            path = path + "/jiudingPay/";
        }
        String rootCertPath = path + Cache.getResource("jiuding.sdk.rootCertPath");
        //编码设置
        String encoding = "UTF-8";
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            String orderId = request.getParameter("orderId");
            String orderSts = request.getParameter("orderSts");
            String amount = request.getParameter("amount");
            Map<String, String> dataMap = new LinkedHashMap();
            dataMap.put("orderId", orderId);
            dataMap.put("orderSts", orderSts);
            dataMap.put("amount", amount);
            dataMap.put("charset", request.getParameter("charset"));
            dataMap.put("version", request.getParameter("version"));
            dataMap.put("acDate", request.getParameter("acDate"));
            dataMap.put("bankAbbr", request.getParameter("bankAbbr"));
            dataMap.put("fee", request.getParameter("fee"));
            dataMap.put("memberId", request.getParameter("memberId"));
            dataMap.put("merchantId", request.getParameter("merchantId"));
            dataMap.put("orderTime", request.getParameter("orderTime"));
            dataMap.put("payTime", request.getParameter("payTime"));
            dataMap.put("payType", request.getParameter("payType"));
            dataMap.put("signType", request.getParameter("signType"));

            Map requestMap = new HashMap();
            requestMap.putAll(dataMap);
            Set set = dataMap.keySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String tmp = dataMap.get(key);

                if (StringUtil.isEmpty(tmp) || StringUtils.isBlank(tmp)) {
                    requestMap.remove(key);
                    System.out.println("key:" + key + " ==> value:" + tmp + "<br/>");
                }
            }

            //数据签名，hmac为签名后的消息摘要
            RSASignUtil rsaUtil = new RSASignUtil(rootCertPath);
            String returnData = rsaUtil.coverMap2String(requestMap);
            System.out.println("九鼎支付（数据）：收到服务器rpmAsyncNotify接口，数据：" + returnData);
            boolean flag = rsaUtil.verify(returnData, request.getParameter("serverSign"), request.getParameter("serverCert"), encoding);
            if (flag) {
                //验签成功,商户的业务逻辑处理...
                IOrderService orderService = getBean(IOrderService.class);
                IWfOrderService wfOrderService = getBean(IWfOrderService.class);
                Boolean status = false;
                if ("PD".equals(orderSts)) {
                    System.out.println("九鼎支付：九鼎回调处理开始...");
                    /*
                     * 由于危废订单和普通订单的id创建规则有明确区分
                     * 故这里可以通过单表查找的数据是否为空为依据来区分不同的订单类型
                     */
                    Order order = getBean(IOrderService.class).getOrderByOrderId(orderId);

                    //取回真实orderId
                    String wfOrderIdRel = getBean(IWfOrderJiudingPayRelService.class).getWfOrderIdByRelId(orderId);
                    WfOrder wfOrder = getBean(IWfOrderService.class).getWfOrderById(wfOrderIdRel);

                    System.out.println(order);
                    System.out.println(wfOrder);
                    if (order != null) {
                        status = orderService.orderOLJiudingPay(order.getId(), order.getId(), request);
                    } else if (wfOrder != null) {
                        BigDecimal payPrice = new BigDecimal(amount).divide(new BigDecimal("100"));
                        status = wfOrderService.wfOrderOLJiudingPay(wfOrder.getId(), payPrice);
                    }
                }
                System.out.println("九鼎支付：九鼎回调处理结果为" + status);
                return status;
            } else {
                //验签失败,商户的业务逻辑处理...
                System.out.println("九鼎支付：九鼎回调请求验证失败");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("九鼎支付：九鼎回调业务处理失败...");
        }
        return false;
    }
}
