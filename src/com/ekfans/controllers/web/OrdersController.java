package com.ekfans.controllers.web;

import com.ekfans.base.order.service.IOrderService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.ccb.CcbPayUtil;
import com.ekfans.plugin.ccb.MD5;
import com.ekfans.plugin.jiuding.help.JiudingPayHelp;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrdersController extends BasicController {

    private Logger log = LoggerFactory.getLogger(OrdersController.class);
    @Resource
    private IOrderService orderService;
    @Resource
    private IUserService userService;
    @Resource
    private IStoreService storeService;

    /**
     * 大宗采购首页，订单展示
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/web/dazong/getOrdersList")
    public String getOrdersList(HttpServletRequest req) {
        Pager pager = new Pager();
        pager.setCurrentPage(1);
        pager.setRowsPerPage(12);

        // 页面显示需要的数据
        req.setAttribute("olist", this.orderService.getDaZongOrderList(pager));

        return "/web/purchase/channel/index/orders";
    }

    /**
     * 首页成交记录显示
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "/web/indexOrder/indexOrderListByNum")
    public String indexOrderListByNum(HttpServletRequest req) {
        Pager pager = new Pager();
        pager.setCurrentPage(1);
        pager.setRowsPerPage(Integer.parseInt(req.getParameter("num")));

        // 页面显示需要的数据
        req.setAttribute("olist", this.orderService.getDaZongOrderList(pager));

        return "/web/channel/index/orderRecords";
    }


    /**
     * 获取个人用户注册数量、企业用户注册数量、多少企业收到款
     */
    @RequestMapping(value = "/web/dazong/getStaticSource")
    public void getStaticSource(HttpServletRequest req, HttpServletResponse resp) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonStr = mapper.writeValueAsString(this.userService.getRegCustomerNumber());

            backWrite(resp, jsonStr);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/web/renzheng/getAutoCompany")
    public void getAutoCompany(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Store> slist = this.storeService.getAutoCompany();
        map.put("number", slist == null || slist.size() <= 0 ? 0 : slist.get(0).getAutoNumber());
        map.put("slist", slist);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(map);

            backWrite(resp, jsonStr);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void backWrite(HttpServletResponse resp, String jsonStr) {
        resp.reset();
        resp.setContentType("text/html;charset=UTF-8");
        resp.setHeader("Cache-control", "no-cache");
        resp.setCharacterEncoding("UTF-8");

        ServletOutputStream out = null;
        try {
            out = resp.getOutputStream();
            out.write(jsonStr.getBytes("UTF-8"));
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/web/order/ccbpay/{orderId}")
    public String orderCcbPay(@PathVariable String orderId, HttpServletRequest req, HttpServletResponse resp) {
        String data = CcbPayUtil.getOrderPayInfo(req, orderId);
        String apiUrl = Cache.getResource("ccb.pay.url");
        String ThirdSysID = Cache.getResource("ccb.pay.ThirdSysID");
        String TxCode = "MALL10001";
        String RequestType = "1";

        String auth = MD5.getMD5(ThirdSysID + TxCode + RequestType + data + Cache.getResource("ccb.pay.md5key"), "UTF-8");

        req.setAttribute("ThirdSysID", ThirdSysID);
        req.setAttribute("TxCode", TxCode);
        req.setAttribute("RequestType", RequestType);
        req.setAttribute("Data", data);
        req.setAttribute("Auth", auth);
        req.setAttribute("apiUrl", apiUrl);
        return "/web/ccbPay/send";
    }

    @RequestMapping(value = "/web/ccbpay/notify")
    @ResponseBody
    public String orderCcbPayNotify(HttpServletRequest request, HttpServletResponse resp) {
        String msg = CcbPayUtil.comfirmPaySuccess(request);
        return msg;
    }

    @RequestMapping(value = "/web/ccbpay/callback")
    public String orderCcbPayCallBack(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("notify!!! " + DateUtil.getSysDateTimeString());
        return "/web/ccbPay/callback";
    }

    @RequestMapping(value = "/web/ccbpay/failed")
    public String orderCcbPayFailed(HttpServletRequest request, HttpServletResponse resp) {
        return "/web/ccbPay/failed";
    }

    @RequestMapping(value = "/web/order/jiudingpay/{orderId}/{ofPayType}/{carType}/{bankAbbr}")
    public String orderJiuDingPay(@PathVariable String orderId, @PathVariable String ofPayType, @PathVariable String carType, @PathVariable String bankAbbr, HttpServletRequest req, HttpServletResponse resp) {
        Map<String, Object> map = JiudingPayHelp.getJiudingPayInfo(orderId, bankAbbr, carType, ofPayType, req);
        System.out.println("九鼎支付（数据）：请求服务器rpmBankPayment接口，参数：" + map);
        req.setAttribute("paramMap", map);
        req.setAttribute("url", Cache.getResource("jiuding.sdk.url"));
        return "/web/jiudingPay/send";
    }

    @RequestMapping(value = "/web/jiudingpay/notify")
    @ResponseBody
    public void orderJiuDingNotify(HttpServletRequest req, HttpServletResponse resp) {
        Boolean b = JiudingPayHelp.payCallBack(req);
        String msg = b ? "result=SUCCESS" : "result=FAILED";
        backWriteStr(resp, msg);
    }

    @RequestMapping(value = "/web/jiudingpay/callback")
    public String orderJiuDingCallBack(HttpServletRequest req, HttpServletResponse resp) {
        return "/web/jiudingPay/success";
    }
}
