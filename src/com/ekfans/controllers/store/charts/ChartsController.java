package com.ekfans.controllers.store.charts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.service.IOrderWfpService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.JsonUtil;

@Controller
public class ChartsController extends BasicController{
    @Autowired 
    private IOrderWfpService wfpService;
    /**
    * @Title: lineCharts
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * 详细业务流程:测试报表
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param reponse void 返回类型
    * @throws
     */
    @RequestMapping(value="/store/lineCharts", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String lineCharts(HttpServletResponse reponse){
       List<String> list = wfpService.getAll();
       String ListJson = JsonUtil.listToJson(list);
       return ListJson;
    }
}
