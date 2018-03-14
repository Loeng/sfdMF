package com.ekfans.controllers.system.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.system.model.SystemParamConfig;
import com.ekfans.base.system.service.ISystemParamConfigService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.cache.service.ISystemConfigCacheService;
import com.ekfans.plugin.cache.service.SystemConfigCacheService;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemConfigController extends BasicController {
    // 注入Service
    @Autowired
    private ISystemParamConfigService systemParamConfigService;

    /**
     * @param request
     * @return String 返回类型
     * @throws
     * @Title: index
     * @Description: TODO(跳转到系统参数配置页面(默认展示商城配置参数)) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/system/config/paramConfig/index")
    public String index(HttpServletRequest request) {
        // 取到商户配置列表返回
        List<SystemParamConfig> configs = systemParamConfigService.getConfigList("storeConfig");
        request.setAttribute("configs", configs);
        return "/system/systemConfigs/systemParamConfig";
    }

    /**
     * @param request
     * @return String 返回类型
     * @throws
     * @Title: list
     * @Description: TODO(根据配置类型查询配置列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/system/config/paramConfig/list/{id}")
    public String list(@PathVariable String id, HttpServletRequest request) {
        // 取到商户配置列表返回
        List<SystemParamConfig> configs = systemParamConfigService.getConfigList(id);
        request.setAttribute("configs", configs);
        return "/system/systemConfigs/paramConfigList";
    }

    /**
     * @param request
     * @return String 返回类型
     * @throws
     * @Title: save
     * @Description: TODO(保存参数列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    @RequestMapping(value = "/system/config/paramConfig/save")
    @ResponseBody
    public Object save(HttpServletRequest request, HttpServletResponse response) {
        // 得到id数组
        String[] ids = request.getParameterValues("id");

        boolean flag = false;
        if (ids.length > 0 && ids != null) {
            // 循环遍历
            for (int i = 0; i < ids.length; i++) {
                // 得到id
                String id = ids[i];
                if (!StringUtil.isEmpty(id)) {
                    // 根据id得到参数对象
                    SystemParamConfig systemParamConfig = systemParamConfigService.getConfigById(id);
                    if (systemParamConfig != null) {
                        // 得到note值
                        String note = systemParamConfig.getNote();

                        // 得到参数对象相应的值
                        String value = request.getParameter("value" + id);

                        // 如果note要求输入数字而得到的值不是数字返回false
                        if ("请输入数字".equals(note) && !StringUtil.isNumber(value)) {
                            return false;
                        }
                        if ("img".equals(systemParamConfig.getValueType())) {
                            // 设置图标保存路径
                            String currentPath = "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/paramConfig/";
                            // 调用方法获取分类图标，返回图标路径
                            String picturePath = FileUploadHelper.uploadFile("value" + systemParamConfig.getId(), currentPath, request, response);
                            value = picturePath;
                        }
                        // 重新设置属性
                        systemParamConfig.setValue(value);
                        // 保存到数据库
                        flag = systemParamConfigService.updateConfig(systemParamConfig);
                        // 出现更新失败时跳出循环
                        if (!flag) {
                            break;
                        }
                    }
                }

            }
        }

        ISystemConfigCacheService cacheService = new SystemConfigCacheService();
        cacheService.refreshParamConfig();

        // 从缓存中获取网站名
        getSession().setAttribute("webName", Cache.getSystemContentConfig("网站名称"));
        // 从缓存中获取网站logo
        getSession().setAttribute("webLogo", Cache.getSystemContentConfig("网站Logo"));
        // 从缓存中获取网站logo
        // getSession().setAttribute("yxLogo",
        // Cache.getSystemContentConfig("优选商城LOGO"));
        // getSession().setAttribute("companyLogo",
        // Cache.getSystemContentConfig("企业中心LOGO"));
        // 从缓存中获取网站的底部版权信息
        getSession().setAttribute("webCopyright", Cache.getSystemContentConfig("底部版权信息"));
        // 从缓存获取网站底部联系信息
        getSession().setAttribute("webContactInformation", Cache.getSystemContentConfig("底部联系信息"));
        // 从缓存获取网站底部联系信息
        getSession().setAttribute("servieRule", Cache.getSystemContentConfig("服务条款"));

        // 从缓存获取购物指南并将其放入servletContext
        getSession().setAttribute("shoppingGuide", Cache.getContentsByCategoryName("采购流程"));
        // 从缓存获取配送方式并将其放入servletContext
        getSession().setAttribute("shippingMethod", Cache.getContentsByCategoryName("服务与支持"));
        // 从缓存获支付方式并将其放入servletContext
        getSession().setAttribute("myMethod", Cache.getContentsByCategoryName("我的账户"));
        // 从缓存获售后服务并将其放入servletContext
        getSession().setAttribute("aboutUs", Cache.getContentsByCategoryName("关于我们"));
        return flag;
    }
}
