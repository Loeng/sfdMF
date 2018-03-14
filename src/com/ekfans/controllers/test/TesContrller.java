package com.ekfans.controllers.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.store.model.AccountBank;
import com.ekfans.base.store.service.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.content.dao.IContentDao;
import com.ekfans.base.content.model.Content;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.store.dao.IAccountBankLogDao;
import com.ekfans.base.store.dao.IAccountLogDao;
import com.ekfans.base.store.model.AccountBankLog;
import com.ekfans.base.store.model.AccountLog;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.webService.bcs.BCSClientService;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.JsonObject;
import com.ekfans.pub.util.JsonUtil;

@Controller
public class TesContrller extends BasicController {
    @Autowired
    private IStoreService storeService;
    @Autowired
    private IAccountBankLogService bankLogService;
    @Autowired
    private IAccountLogService logService;
    @Autowired
    private IBankInfService bankInfService;
    @Autowired
    private IProductCategoryService iProductCategoryService;
    @Autowired
    private IAccountBankService accountBankService;

    /**
     * 测试入口
     *
     * @return
     */
    @RequestMapping(value = "/test/bcs/test")
    public String tests(HttpServletRequest request) {
        return "/web/test/bcstest";
    }

    /**
     * 测试入口
     *
     * @return
     */
    @RequestMapping(value = "/test/getjson")
    @ResponseBody
    public void getJson(HttpServletRequest request, HttpServletResponse resp) {
        String jsonStr = request.getParameter("name");
        StringBuffer str = new StringBuffer("");
        InputStream stream = null;
        try {
            stream = request.getInputStream();
            if (stream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        if (str != null) {
            jsonStr = str.toString();
        }
        try {
            JsonObject json = new JsonObject(jsonStr);
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put("name", json.getString("name") + "现在是" + DateUtil.getSysDateString());
            jsonMap.put("value", "阿斯顿阿斯顿");
            backWriteStr(resp, JsonUtil.convertToJsonString(jsonMap));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put("name", "你好啊");
        System.out.println(JsonUtil.convertToJsonString(jsonMap));

    }

    /**
     * 查询可用余额和总余额
     *
     * @return
     */
    @RequestMapping(value = "/test/bcs/getprices/{storeId}")
    public String getPrices(@PathVariable String storeId, HttpServletRequest request) {
        BigDecimal[] prices = BCSClientService.getAvlBal(storeId);
        request.setAttribute("totalAval", prices[0]);
        request.setAttribute("aval", prices[1]);
        request.setAttribute("type", "1");
        return "/web/test/bcstest";
    }

    /**
     * 查询可用余额和总余额
     *
     * @return
     */
    @RequestMapping(value = "/test/content/addPosition")
    public String addPosition(HttpServletRequest request) {
        IContentDao contentDao = SpringContextHolder.getBean(IContentDao.class);
        StringBuffer sql = new StringBuffer(" from Content as content order by content.updateTime desc");
        try {
            List<Content> contents = contentDao.list(sql.toString(), null);
            if (contents != null) {
                int a = contents.size();
                for (Content content : contents) {
                    if (content == null) {
                        continue;
                    }
                    content.setPosition(a);
                    contentDao.updateBean(content);
                    System.out.println(a);
                    a = a - 1;
                }
            }
            System.out.println("---------OKOKOKOKOK");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/web/test/bcstest";
    }

    /**
     * 入金
     *
     * @return
     */
    @RequestMapping(value = "/test/bcs/addprice")
    public String addPrice(HttpServletRequest request) {
        String storeId = "000000004d74fbb6014d799b7d93006c";

        Boolean status = logService.sufficient(storeId, new BigDecimal(50));
        request.setAttribute("status", status);
        request.setAttribute("type", "2");
        return "/web/test/bcstest";
    }

    /**
     * 交易
     *
     * @return
     */
    @RequestMapping(value = "/test/bcs/orderpay")
    public String orderPay(HttpServletRequest request) {
        String storeId = "000000004d74fbb6014d799b7d93006c";
        String receiveId = "1";

        Boolean status = logService.payForOrder(storeId, receiveId, "402880f84f43c19c014f43dd707b001a", "1", new BigDecimal(1000));
        request.setAttribute("status", status);
        request.setAttribute("type", "3");
        return "/web/test/bcstest";
    }

    @RequestMapping(value = "/test/bcs/orderstatus")
    public String getOrderStatus(HttpServletRequest request) {

        IAccountLogDao accountLogDao = SpringContextHolder.getBean(IAccountLogDao.class);
        IAccountBankLogDao bankLogDao = SpringContextHolder.getBean(IAccountBankLogDao.class);

        AccountLog accountLog = new AccountLog();
        try {
            accountLog.setUserId("000000004d74fbb6014d799b7d93006c");
            accountLog.setReceiveId("1");
            accountLog.setCreateTime(DateUtil.getSysDateTimeString());
            accountLog.setOrderId("8a8086c350cc714b0150cc7350d0000a");
            accountLog.setStatus("3");
            accountLogDao.addBean(accountLog);
        } catch (Exception e) {
            // TODO: handle exception
        }

        AccountBankLog bankLog = new AccountBankLog();
        bankLog.setUserId("000000004d74fbb6014d799b7d93006c");
        Map<String, Object> rMap = BCSClientService.getPayInfo("8a8086c350cc714b0150cc7350d0000a", "2", bankLog, accountLog);
        request.setAttribute("rMap", rMap);
        Session session = null;
        Transaction transaction = null;
        try {
            session = accountLogDao.createSession();
            transaction = session.beginTransaction();

            accountLogDao.updateBean(accountLog, session);
            bankLogDao.addBean(bankLog, session);

            session.flush();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        request.setAttribute("type", "4");
        return "/web/test/bcstest";
    }

    @RequestMapping(value = "/test/bcs/uinfo/{storeId}")
    public String getUserInfo(@PathVariable String storeId, HttpServletRequest request) {

        IAccountLogDao accountLogDao = SpringContextHolder.getBean(IAccountLogDao.class);
        IAccountBankLogDao bankLogDao = SpringContextHolder.getBean(IAccountBankLogDao.class);

        AccountLog accountLog = new AccountLog();
        try {
            accountLog.setUserId(storeId);
            accountLog.setCreateTime(DateUtil.getSysDateTimeString());
            accountLogDao.addBean(accountLog);
        } catch (Exception e) {
            // TODO: handle exception
        }

        AccountBankLog bankLog = new AccountBankLog();
        bankLog.setUserId(storeId);
        Map<String, Object> rMap = BCSClientService.getUserInfo(storeId, "20151101", "20151103", "1", "", "20", bankLog, accountLog);
        request.setAttribute("rMap", rMap);
        Session session = null;
        Transaction transaction = null;
        try {
            session = accountLogDao.createSession();
            transaction = session.beginTransaction();

            accountLogDao.updateBean(accountLog, session);
            bankLogDao.addBean(bankLog, session);

            session.flush();
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        request.setAttribute("type", "5");
        return "/web/test/bcstest";
    }

    /**
     * 入金
     *
     * @return
     */
    @RequestMapping(value = "/test/bcs/takeMoney/{storeId}")
    public String takeMoney(@PathVariable String storeId, HttpServletRequest request) {

        Boolean status = logService.takeMoney(storeId, new BigDecimal(20), new BigDecimal(0));
        request.setAttribute("status", status);
        request.setAttribute("type", "6");
        return "/web/test/bcstest";
    }

    /**
     * 导入银行信息
     *
     * @return
     */
    @RequestMapping(value = "/test/bcs/addBanks")
    public String addBanks(HttpServletRequest request) {

        Boolean status = bankInfService.setBankInfs(Cache.getBCSResource("bank.inf.path"));
        request.setAttribute("status", status);
        request.setAttribute("type", "7");
        return "/web/test/bcstest";
    }

    /**
     * 导入银行信息
     *
     * @return
     */
    @RequestMapping(value = "/test/pro/getcatg")
    public String getProCatg(HttpServletRequest request) {
        List<ProductCategory> proCategories = Cache.getProductCategorys();
        System.out.println(proCategories);
        return "";
    }

    @RequestMapping(value = "/test/bcs/unregist/{storeId}")
    public String unRegist(@PathVariable String storeId, HttpServletRequest request) {
        AccountBank bank = accountBankService.getBanksByUserId(storeId);
        Boolean status = accountBankService.unRegidst(bank, request);
        request.setAttribute("status", status);
        request.setAttribute("type", "99");
        return "/web/test/bcstest";
    }
}
