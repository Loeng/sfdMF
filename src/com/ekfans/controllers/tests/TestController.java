package com.ekfans.controllers.tests;

import com.ekfans.base.store.model.AccountBank;
import com.ekfans.base.store.service.IAccountBankService;
import com.ekfans.basic.controller.BasicController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuguoyu on 2017/3/28.
 */
public class TestController extends BasicController {
    @Autowired
    IAccountBankService accountBankService;

    @RequestMapping(value = "/test/unregist/{storeId}")
    public String unRegist(@PathVariable String storeId, HttpServletRequest request) {
        AccountBank bank = accountBankService.getBanksByUserId(storeId);
        Boolean status = false;
        if (bank != null) {
            status = accountBankService.unRegidst(bank, request);
        }
        request.setAttribute("unRegStatus", status);
        return "/test/unregist";
    }

    @RequestMapping(value = "/test/testPage")
    public String tests(HttpServletRequest request) {

        return "/test/unregist";
    }
}
