package com.ekfans.plugin.ccb;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.order.util.OrderConst;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.base.wfOrder.model.WfOrder;
import com.ekfans.base.wfOrder.service.IWfOrderJiudingPayRelService;
import com.ekfans.base.wfOrder.service.IWfOrderService;
import com.ekfans.base.wfOrder.util.WfOrderHelper;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ekfans.basic.spring.SpringContextHolder.getBean;

/**
 * 建行支付帮助类
 * <p>
 * 本地调试需要注意的问题：
 * 1. 因为IP白名单的原因，请求受到限制，要在白名单服务器配置一个nginx正向代理，用于通过服务器来转发请求，
 * ```
 * server {
 * resolver 59.51.78.211; #指定DNS服务器IP地址
 * listen 8880;
 * location / {
 * proxy_pass http://124.127.94.39:8880; #设定代理服务器的协议和地址
 * }
 * }
 * ```
 * 然后还要在本机调用的时候注意域名和地址，要用代理的地址，同时由于api端还限制了域名，所以还要注意不能用ip访问，也就是说本地还需要配置host,
 * ```
 * http://emall.ccb.com:8880/alliance/thirdPartAPI.php //正确
 * http://emall.ccb.com:8880/alliance/thirdPartAPI.php  //错误，由于使用了代理，则不能用文档中的
 * ```
 * 最后的最后还需要注意，由于使用代理，host配置中的ip不能是原文档中的，要改成代理服务器的，
 * ```
 * 175.6.250.68 emall.ccb.com
 * 175.6.250.68 login.ccb.com
 * 175.6.250.68 epay.ccb.com
 * 175.6.250.68 img.mall.ccb.com
 * ```
 * 2. 因为回调IP必须为固定IP和我本地调试的需求，需要搭建一个内网穿透环境，用了github上的frp实现。
 */
public class CcbPayUtil {
    private static IOrderService orderService = getBean(IOrderService.class);
    private static IWfOrderService wfOrderService = getBean(IWfOrderService.class);
    private static IUserService userService = getBean(IUserService.class);
    private static IStoreService storeService = getBean(IStoreService.class);
    private static IWfOrderJiudingPayRelService payRelService = getBean(IWfOrderJiudingPayRelService.class);

    public static String getOrderPayInfo(HttpServletRequest request, String orderId) {

        /*准备数据容器*/
        Map<String, Object> payOrderMap = new HashMap<>();
        Map<String, Object> orderInfosMap = new HashMap<>();
        List<Map<String, Object>> orderInfos = new ArrayList<>();
        List<Map<String, Object>> orderProducts = new ArrayList<>();

        /*获取部分数据*/
        Order order = orderService.getOrderByOrderId(orderId);
        WfOrder wfOrder = wfOrderService.getWfOrderById(orderId);

        //支付总金额
        BigDecimal totalPrice = null;
        //创建时间
        String createTime = null;
        //买家
        User buyer = null;
        //卖家
        User saler = null;
        //卖家商户
        Store buyerStore = null;

        if (order != null) {
            if (OrderConst.ORDER_STATUS_FINISH.equals(order.getStatus()) || OrderConst.ORDER_STATUS_WAIT_SEND.equals(order.getStatus())) {
                return null;
            }
            buyer = userService.getUser(order.getUserId());
            saler = userService.getUser(order.getStoreId());
            buyerStore = storeService.getStore(order.getUserId());
            createTime = order.getCreateTime().replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
            totalPrice = order.getTotalPrice();
        } else if (wfOrder != null) {
            if (WfOrderHelper.WFORDER_STATUS_WAIT_FH.equals(wfOrder.getStatus()) || WfOrderHelper.WFORDER_STATUS_WAIT_FINISH.equals(wfOrder.getStatus())) {
                return null;
            }
            buyer = userService.getUser(wfOrder.getBuyId());
            saler = userService.getUser(wfOrder.getSaleId());
            buyerStore = storeService.getStore(wfOrder.getSaleId());
            createTime = wfOrder.getCreateTime().replaceAll(":", "").replaceAll("-", "").replaceAll(" ", "");
            // 查询危费订单类型
            String payType = wfOrderService.getPayTypeById(orderId);
            // 危费订单根据支付类型的不同，会有不同的支付金额
            totalPrice = wfOrderService.getPayPrice(orderId, payType);

            // 虚拟一个orderId：危费订单拥有多次支付情况，并且支付平台限制了orderId只能使用一次。
            // 解决方法是：先创建一个关联的虚拟orderId与真实的orderId相关联，回调时再反向取回原wfOrderId;
            orderId = payRelService.create(orderId).getId();
        }
        //处理金额格式 四舍五入保留两位小数
        totalPrice = totalPrice.abs().setScale(2,BigDecimal.ROUND_HALF_UP);

        /*交易基本信息*/
        //TxCode  交易码 Char(8)  Yes 填写MALL10001
        payOrderMap.put("TxCode", "MALL10001");
        //TransID 流水号 Char(30) Yes 由第三方系统生成，作为此次交易的唯一标识 （流水号统一使用ThirdSysID+yyyymmddhhmiss+5位顺序号，由发送方生成。一个流水号只能使用一次（含发送/返回两次跳转），重复请求无效。）
        payOrderMap.put("TransID", CcbPayUtil.createTransID());
        //GoPayType 保存订单之后，是否立即发起支付 Char(2) Yes 1-不发起支付 2-发起短信支付 3-发起商城账户支付（若传1，则该接口仅仅保存订单，不发起支付操作。若传2，则该接口保存订单后，自动发送收款短信。 若传3，则该接口保存订单后，自动跳转商城账户支付）
        payOrderMap.put("GoPayType", "3");
        //OrderIniter 订单的创建方 Char(2) Yes 0-作为买方发起 1-作为卖方发起
        payOrderMap.put("OrderIniter", "0");
        //BuyerUserID_ThirdSys 订单买方在第三方系统中的ID Char(20) Yes 买家在第三方系统中的用户ID信息
        payOrderMap.put("BuyerUserID_ThirdSys", buyer.getCcbUserId());
        //BuyerUserName_ThirdSys 订单买方在第三方系统中的名 Char(20) Yes 买家在第三方系统中的用户名，买家在第三方系统中的用户名，仅支持英文或者数字
        payOrderMap.put("BuyerUserName_ThirdSys", buyer.getName());
        //SellerUserID_ThirdSys 订单卖方在第三方系统中的ID Char(20) Yes 订单卖方在第三方系统中的ID
        payOrderMap.put("SellerUserID_ThirdSys", saler.getCcbUserId());
        //BuyerUserType_ThirdSys 买方会员在第三方系统中的会员类型 Char(20) Yes 1-企业会员 （该字段用户控制绑定善融会员名，如果传了该字段，则根据该字段类型分别绑定善融上对应类型的会员。）
        payOrderMap.put("BuyerUserType_ThirdSys", "1");
        //BuyerTrueName_ThirdSys 买方的真实姓名 Char(20) Yes 买方的真实姓名
        payOrderMap.put("BuyerTrueName_ThirdSys", buyer.getNickName());
        //BuyerCompany_ThirdSys 买方的公司名 Char(100) Yes 买方的公司名(若买方是个人，该字段与BuyerTrueName_ThirdSys保持一致即可)
        payOrderMap.put("BuyerCompany_ThirdSys", buyerStore != null ? (buyerStore.getStoreName()) : buyer.getNickName());
        //BuyerPhoneNum_ThirdSys 买方的手机号码 Char(11) Yes 买方的手机号码
        payOrderMap.put("BuyerPhoneNum_ThirdSys", "".equals(buyer.getMobile()) ? "00000000000" : buyer.getMobile());
        //BuyerAddress_ThirdSys 买方在第三方系统中的地址 Char(100) Yes 买方在第三方系统中的地址
        payOrderMap.put("BuyerAddress_ThirdSys", buyerStore != null ? buyerStore.getMailingAddress() : "");
        //BuyerCertType_ThirdSys 买方的证件类型 Char(50) No 买方的证件类型 1-中国护照 2-港澳台同胞回乡证 3-户口簿 4-军官证 5-公司卡 7-武警士兵证 8-武警文职干部证 9-(外国)护照 10-台通行证 11-士兵证 12-文职干部证 13-警官证 15-学生证 16-营业执照 17-身份证
        payOrderMap.put("BuyerCertType_ThirdSys", "16");
        //BuyerCertValue_ThirdSys 买方证件号码 Char(50) No 买方的证件号码
        payOrderMap.put("BuyerCertValue_ThirdSys", "0");
        //Expand1 扩展信息 Char(128) No 商户自定义扩展信息 善融平台不作处理，原样返回第三方系统。
        payOrderMap.put("Expand1", "EP1");

        /*订单信息*/
        //Order_No 第三方系统的订单号 Char(30) Yes 第三方系统的订单号
        orderInfosMap.put("Order_No", orderId);
        //HaveProducts 是否有订单详情信息 Char(2) Yes 1-有 2-没有此字段必填1
        orderInfosMap.put("HaveProducts", "1");
        //Order_Money 订单支付金额 Char(20) Yes 当前订单的支付金额
        orderInfosMap.put("Order_Money", totalPrice);
        //Order_Time 订单的创建时间 Char(20) Yes 订单在第三方系统中的创建时间，规则例如 20151211172354(YYYYMMDDhhmiss)
        orderInfosMap.put("Order_Time", createTime);
        //Order_Tile 订单的标题 Char(50) Yes 订单的标题,用户支付的时候看到的本次支付内容
        orderInfosMap.put("Order_Tile", "三分地环保产业链订单支付");
        //Order_BuyerPhone 订单买家的手机号码 Char(11) Yes 收货方的手机号码
        orderInfosMap.put("Order_BuyerPhone", "".equals(buyer.getMobile()) ? "00000000000" : buyer.getMobile());
        //ReceiverTrueName_ThirdSys 收货方的真实姓名 Char(20) Yes 收货方的真实姓名
        orderInfosMap.put("ReceiverTrueName_ThirdSys", buyer.getNickName());
        //ReceiverCompany_ThirdSys 收货方的公司名 Char(100) Yes 收货方的公司名(若买方是个人，该字段与ReceiverTrueName_ThirdSys保持一致即可)
        orderInfosMap.put("ReceiverCompany_ThirdSys", buyerStore != null ? (buyerStore.getStoreName()) : buyer.getNickName());
        //ReceiverAddress_ThirdSys 收货方在第三方系统中的收获地址 Char(100) Yes 收货方的收货地址
        orderInfosMap.put("ReceiverAddress_ThirdSys", buyerStore != null ? buyerStore.getMailingAddress() : "");
        //BuyerCertType_ThirdSys 买方的证件类型 Char(50) No 买方的证件类型 1-中国护照 2-港澳台同胞回乡证 3-户口簿 4-军官证 5-公司卡 7-武警士兵证 8-武警文职干部证 9-(外国)护照 10-台通行证 11-士兵证 12-文职干部证 13-警官证 15-学生证 16-营业执照 17-身份证
        orderInfosMap.put("BuyerCertType_ThirdSys", "16");
        //BuyerCertValue_ThirdSys 买方证件号码 Char(50) No 买方的证件号码
        orderInfosMap.put("BuyerCertValue_ThirdSys", "0");

        /*商品详情*/
        Map<String, Object> orderProductsMap = new HashMap<>();
        //ProductID 商品ID Char(30) Yes
        orderProductsMap.put("ProductID", "none001");
        //ProductTitle 商品标题 Char(30) Yes
        orderProductsMap.put("ProductTitle", "直付订单，暂无商品");
        //ProductCode 商品编码 Char(30) Yes
        orderProductsMap.put("ProductCode", "none001");
        //ProductModel 商品型号 Char(30) Yes
        orderProductsMap.put("ProductModel", "直付订单，暂无商品");
        //ProductPrice 商品价格 Char(20) Yes
        orderProductsMap.put("ProductPrice", totalPrice);
        //ProductAmount 商品数量 Char(20) Yes
        orderProductsMap.put("ProductAmount", 1);
        //ProductUnit 商品单位 Char(10) Yes
        orderProductsMap.put("ProductUnit", "批");
        //ProductDesc 商品描述 Char(50) No
        orderProductsMap.put("ProductDesc", "-");
        orderProducts.add(orderProductsMap);

        /*拼装map*/
        //Order_Products 订单对应的商品详情 Dataset - 如果HaveProducts=1，则必填 否则为非必填 详见下面[dataset]定义
        orderInfosMap.put("Order_Products", orderProducts);
        // orderInfos是一个集合
        orderInfos.add(orderInfosMap);
        //OrderInfos 第三方系统的子订单详情，若为多个订单，则传多行 dataset Yes  详见下面[dataset]定义，最多不能超过10条
        payOrderMap.put("OrderInfos", orderInfos);

        /*加密*/
        String KeyStr = Cache.getResource("ccb.pay.keystr");
        String enStr = null;
        try {
            System.out.println("---");
            System.out.println(JsonUtil.convertToJsonString(payOrderMap));
            System.out.println("---");
            enStr = DESUtil.EncryptUTF8(KeyStr, JsonUtil.convertToJsonString(payOrderMap));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return enStr;
    }

    public static String comfirmPaySuccess(HttpServletRequest request) {
        System.out.println("notify!!! " + DateUtil.getSysDateTimeString());
        String Data = request.getParameter("Data");
        String Auth = request.getParameter("Auth");

        String desKeyStr = Cache.getResource("ccb.pay.keystr");
        String md5KeyStr = Cache.getResource("ccb.pay.md5key");
        String thirdSysID = Cache.getResource("ccb.pay.ThirdSysID");

        //String initData = ThirdSysID+TxCode+RequestType+Data+md5KeyStr;
        String initData = thirdSysID + "MALL20001" + "2" + Data + md5KeyStr;
        String md5Auth = MD5.getMD5(initData, "UTF-8");

        //先做md5校验，如果校验不成功有可能数据被篡改了
        String decryptData = "";
        if (Auth.equals(md5Auth)) {
            try {
                decryptData = DESUtil.DecryptUFT8(desKeyStr, Data);

                String orderId = "";
                String orderNoCCB = "";
                String payStatus = "";
                String orderMoney = "";
                String payTime = "";

                /*获取数据*/
                if (!StringUtil.isEmpty(decryptData)) {
                    JSONObject jsonObject = new JSONObject(decryptData);
                    if (jsonObject != null && jsonObject.has("OrderInfos")) {
                        JSONArray array = (JSONArray) jsonObject.get("OrderInfos");
                        if (array != null && array.length() > 0) {
                            JSONObject obj = (JSONObject) array.get(0);
                            if (obj != null) {
                                if (obj.has("Order_No")) {
                                    orderId = obj.getString("Order_No");
                                }
                                if (obj.has("Order_No_CCB")) {
                                    orderNoCCB = obj.getString("Order_No_CCB");
                                }
                                if (obj.has("PayStatus")) {
                                    payStatus = obj.getString("PayStatus");
                                }
                                if (obj.has("Order_Money")) {
                                    orderMoney = obj.getString("Order_Money");
                                }
                                if (obj.has("PayTime")) {
                                    payTime = obj.getString("PayTime");
                                }
                            }
                        }
                    }
                }

                boolean success = false;
                /*支付成功*/
                if ("1".equals(payStatus)) {

                    //验签成功,商户的业务逻辑处理...
                    /*
                     * 由于危废订单和普通订单的id创建规则有明确区分
                     * 故这里可以通过单表查找的数据是否为空为依据来区分不同的订单类型
                     */
                    Order order = orderService.getOrderByOrderId(orderId);

                    //取回真实orderId
                    orderId = payRelService.getWfOrderIdByRelId(orderId);
                    WfOrder wfOrder = wfOrderService.getWfOrderById(orderId);

                    if (order != null) {
                        success = orderService.orderOLCCBPay(orderId, orderId);
                    } else if (wfOrder != null) {
                        success = wfOrderService.wfOrderCCBPay(orderId, new BigDecimal(orderMoney));
                    }
                }

                return success ? thirdSysID + "_success" : "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //数据被篡改了
        }
        return "";
    }

    /**
     * 创建ccbid，采用当前时间"yyyyMMddHHmmssSSS"
     *
     * @return
     */
    public static String createCcbPayId() throws InterruptedException {
        //防止循环调用时重复
        Thread.sleep(1);
        return DateUtil.getFullSysDateTimeString();
    }

    /**
     * 创建流水号
     *
     * @return
     */
    private static String createTransID() {
        String thirdSysID = Cache.getResource("ccb.pay.ThirdSysID");
        String timeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String num = "00" + new SimpleDateFormat("SSS").format(new Date());
        //ThirdSysID+yyyymmddhhmiss+5位顺序号
        return thirdSysID + timeStr + num;
    }
}
