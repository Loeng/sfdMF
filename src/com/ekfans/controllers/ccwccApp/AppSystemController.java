package com.ekfans.controllers.ccwccApp;

import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.message.util.MessageUtil;
import com.ekfans.plugin.page.BasicRequest;
import com.ekfans.pub.util.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguoyu on 2017/4/12.
 */
@Controller
public class AppSystemController {

    @ResponseBody
    @RequestMapping(value = "/ccwcc/system/recommendation", method = RequestMethod.POST)
    public Response tsjy(@RequestBody String jsonStr) {
        try {
            String note = "";
            String contentMan = "";
            String mobile = "";
            if (!StringUtil.isEmpty(jsonStr)) {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj.has("note")) {
                    note = jsonObj.get("note").toString().trim();
                }
                if (jsonObj.has("contentMan")) {
                    contentMan = jsonObj.get("contentMan").toString().trim();
                }
                if (jsonObj.has("mobile")) {
                    mobile = jsonObj.get("mobile").toString().trim();
                }
            }

            String mailNote = "联系人：" + contentMan + "；联系方式：" + mobile + "；正文：" + note;
            MessageUtil.sendMail("fengyue@sfdhb.com", "核价宝投诉与建议", mailNote);
            MessageUtil.sendMail("lgy@sfdhb.com", "核价宝投诉与建议", mailNote);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Response().success();
    }

    @ResponseBody
    @RequestMapping(value = "/ccwcc/system/checkupdate", method = RequestMethod.GET)
    public Response checkUpdate(HttpServletRequest request) {
        String version = request.getParameter("version");
        Map<String, Object> paramMap = new HashMap<>();
        try {

            String systemVersion = Cache.getSystemParamConfig("核价宝版本号");
            // 判断是否需要更新
            if (StringUtil.isEmpty(version) || !systemVersion.equals(version)) {
                String desc = Cache.getSystemParamConfig("核价宝更新说明");
                String url = Cache.getSystemParamConfig("核价宝下载地址");
                paramMap.put("desc", desc);
                paramMap.put("url", url);
                paramMap.put("newVersion", systemVersion);
                paramMap.put("status", true);

            } else {
                paramMap.put("status", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(paramMap);
    }

    @ResponseBody
    @RequestMapping(value = "/ccwcc/system/getad", method = RequestMethod.GET)
    public Response getStartAd(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        try {

            BasicRequest br = new BasicRequest(request);
            String img = Cache.getSystemParamConfig("核价宝启动广告图");
            String url = Cache.getSystemParamConfig("核价宝启动广告链接");
            paramMap.put("url", url);
            paramMap.put("img", br.getWebFullUrlPrex() + img);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response().success(paramMap);
    }

    @ResponseBody
    @RequestMapping(value = "/ccwcc/system/areas", method = RequestMethod.GET)
    public Response getProvinceList(HttpServletRequest request) {
        String areaId = request.getParameter("areaId");

        ISystemAreaService areaService = SpringContextHolder.getBean(ISystemAreaService.class);
        // 定义返回数据
        List<Map<String, String>> alist = new LinkedList<Map<String, String>>();

        try {
            List<SystemArea> areaList = areaService.getSystemAreaList(areaId);
            if (areaList != null && areaList.size() > 0) {
                for (SystemArea area : areaList) {
                    if (area == null) {
                        continue;
                    }
                    Map<String, String> aMap = new HashMap<String, String>();
                    aMap.put("areaId", area.getId());
                    aMap.put("name", area.getAreaName());
                    alist.add(aMap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Response().success(alist);
    }

    @RequestMapping(value = "/billing/shar/{id}")
    public String getProvinceList(@PathVariable String id, HttpServletRequest request) {



        return "/ccwcc/billing/billingShar";
    }


}
