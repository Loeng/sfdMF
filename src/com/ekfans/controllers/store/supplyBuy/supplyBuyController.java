package com.ekfans.controllers.store.supplyBuy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.store.model.Intel;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IIntelService;
import com.ekfans.base.system.model.SystemArea;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class supplyBuyController extends BasicController {
    @Autowired
    private ISupplyBuyService supplyBuyService;
    @Autowired
    private IProductCategoryService categoryService;
    @Autowired
    private ISystemAreaService systemAreaService;
    @Resource
    private IIntelService intelService;

    /**
     * 跳转到源流需求添加页面
     */
    @RequestMapping(value = "/store/supplyBuy/ylxqAdd/{productType}/{type}")
    public String safeAdd(@PathVariable String productType, @PathVariable String type, HttpServletRequest request, HttpSession session) {
        //根据商品类型查询分类(0成品 1原料 2危废品)
        List<ProductCategory> categories = categoryService.getCategoriesByType(productType);
        List<SystemArea> provinces = systemAreaService.getProvinceList();
        request.setAttribute("categories", categories);
        request.setAttribute("provinces", provinces);
        request.setAttribute("productType", productType);
        request.setAttribute("type", type);
        return "/userCenter/store/supplyBuy/ylxqAdd";
    }

    /**
     * 跳至供求发布弹层
     *
     * @param productType
     * @param type
     * @param request
     * @return
     */
    @RequestMapping(value = "/web/supplyBuy/loadQg/{productType}/{type}")
    public String loadRequire(@PathVariable String productType, @PathVariable String type, HttpServletRequest request) {
        List<SystemArea> provinces = systemAreaService.getProvinceList();
        List<ProductCategory> categories = categoryService.getCategoriesByType(productType);
        request.setAttribute("categories", categories);
        request.setAttribute("provinces", provinces);
        request.setAttribute("productType", productType);
        request.setAttribute("type", type);
        return "/web/channel/xhjy/loadQg";
    }

    /**
     * 发布求购弹层保存(发布弹层不能用store/system)
     *
     * @param sb
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/web/supplyBuy/ylxqSave", method = RequestMethod.POST)
    @ResponseBody
    public boolean saveQg(@ModelAttribute SupplyBuy sb, HttpServletRequest request, HttpServletResponse response) {
        Object obj = getSession().getAttribute(SystemConst.STORE);
        //拼接交货地
        String[] destinationStrs = null;
        String destination = "";
        if (!StringUtil.isEmpty(sb.getDestination())) {
            destinationStrs = sb.getDestination().split(",");
        }
        if (destinationStrs != null) {
            destination += destinationStrs[0];
            if (!"县".contains(destinationStrs[1])) {
                destination += "" + destinationStrs[1];
                sb.setDestination(destination);
            }
        }
        String[] intelId = getRequest().getParameterValues("intelId"); // 资质ID集合
        String intelligenceIds = "";
        if (intelId != null && intelId.length > 0) {
            for (int i = 0; i < intelId.length; i++) {
                if (i == 0) {
                    intelligenceIds = intelligenceIds + intelId[i];
                } else {
                    intelligenceIds = intelligenceIds + "-" + intelId[i];
                }
            }
        }
        if (obj != null) {
            Store store = (Store) obj;
            //设置企业id
            sb.setStoreId(store.getId());
            sb.setStoreName(store.getStoreName());
        } else {
            sb.setStoreName("游客");
        }
        sb.setIntelligenceIds(intelligenceIds);
        //设置企业名字
        //设置状态正常
        sb.setStatus("1");
        sb.setCheckStatus(0);
        //设置创建时间
        sb.setCreateTime(DateUtil.getSysDateTimeString());
        return supplyBuyService.add(sb);

    }

    /**
     * 保存源流需求
     */
    @RequestMapping(value = "/store/supplyBuy/ylxqSave", method = RequestMethod.POST)
    @ResponseBody
    public boolean saveShen(@ModelAttribute SupplyBuy sb, HttpServletRequest request, HttpServletResponse response) {
        Object obj = getSession().getAttribute(SystemConst.STORE);
        //拼接交货地
        String[] destinationStrs = null;
        String destination = "";
        if (!StringUtil.isEmpty(sb.getDestination())) {
            destinationStrs = sb.getDestination().split(",");
        }
        if (destinationStrs != null) {
            for (String string : destinationStrs) {
                if (!"县".equals(string) && !string.contains("请选择")) {
                    destination += string;

                }
            }
            sb.setDestination(destination);
        }
        String[] intelId = getRequest().getParameterValues("intelId"); // 资质ID集合
        String intelligenceIds = "";
        if (intelId != null && intelId.length > 0) {
            for (int i = 0; i < intelId.length; i++) {
                if (i == 0) {
                    intelligenceIds = intelligenceIds + intelId[i];
                } else {
                    intelligenceIds = intelligenceIds + "-" + intelId[i];
                }
            }
        }
        if (obj != null) {
            Store store = (Store) obj;
            //设置企业id
            sb.setStoreId(store.getId());
            sb.setStoreName(store.getStoreName());
        } else {
            sb.setStoreName("游客");
        }
        //设置状态正常
        sb.setStatus("1");
        sb.setIntelligenceIds(intelligenceIds);
        sb.setCheckStatus(0);
        //设置创建时间
        sb.setCreateTime(DateUtil.getSysDateTimeString());
        return supplyBuyService.add(sb);

    }


    /**
     * 跳转源流需求到列表
     */
    @RequestMapping(value = "/store/supplyBuy/ylxqList/{productType}/{type}")
    public String supplyList(@PathVariable String productType, @PathVariable String type, HttpServletRequest request, HttpSession session) {
        Store store = (Store) getSession().getAttribute(SystemConst.STORE);
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            currentPage = Integer.parseInt(currentpageStr);
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        //页面上传来的参数
        //标题
        String title = request.getParameter("title");
        //创建时间
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        //状态
        String status = request.getParameter("status");
        if ("关闭".equals(status)) {
            status = "0";
        } else if ("正常".equals(status)) {
            status = "1";
        } else if ("完成".equals(status)) {
            status = "2";
        } else {
            status = "";
        }
        List<SupplyBuy> supplyBuys = supplyBuyService.listSupplyBuy(pager, title, beginDate, endDate, store.getId(), type, status, productType, null, null, null);
        //绑定会页面
        request.setAttribute("supplyBuys", supplyBuys);
        request.setAttribute("title", title);
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("status", status);
        request.setAttribute("pager", pager);
        request.setAttribute("productType", productType);
        request.setAttribute("type", type);
        request.setAttribute("currentpageStr", currentPage);
        return "/userCenter/store/supplyBuy/ylxqList";
    }

    @RequestMapping(value = "/store/supplyBuy/ylxqFinish/{id}")
    public
    @ResponseBody
    boolean supplyFinish(@PathVariable String id, HttpServletRequest request) {
        SupplyBuy sb = supplyBuyService.getSupplyBuyById(id);
        sb.setStatus("2");
        boolean flag = supplyBuyService.update(sb);
        return flag;
    }

    /**
     * 详情源流需求信息
     */
    @RequestMapping(value = "/store/supplyBuy/ylxqQuery/{id}/{type}/{productType}")
    public String supplyQuery(@PathVariable String id, @PathVariable String type, @PathVariable String productType, HttpServletRequest request, HttpSession session) {
        SupplyBuy sb = supplyBuyService.getSupplyBuyById(id);
        List<ProductCategory> categories = categoryService.getCategoriesByType(productType);
        //得到所有的资质集合
        String intelligenceIds = sb.getIntelligenceIds();
        //返回页面的纸质集合
        List<Intel> ilist = new ArrayList<Intel>();
        if (!StringUtil.isEmpty(intelligenceIds)) {
            String[] intelligenceIdsList = intelligenceIds.split("-");
            for (int i = 0; i < intelligenceIdsList.length; i++) {
                ilist.add(intelService.getIntelById(intelligenceIdsList[i]));
            }
        }

        request.setAttribute("categories", categories);
        request.setAttribute("ilist", ilist);
        request.setAttribute("sb", sb);

        if ("1".equals(type)) {
            return "/userCenter/store/supplyBuy/ylxqUpdate";
        } else if ("2".equals(type)) {
            return "/userCenter/store/supplyBuy/ylxqOK";
        } else {
            return "/userCenter/store/supplyBuy/ylxqQuery";
        }

    }

    /**
     * 保存源流需求
     */
    @RequestMapping(value = "/store/supplyBuy/ylxqUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Object supplyBuyUpdate(@ModelAttribute SupplyBuy sb, HttpServletResponse response) {
        //拼接交货地
        String[] destinationStrs = null;
        String destination = "";
        if (!StringUtil.isEmpty(sb.getDestination())) {
            destinationStrs = sb.getDestination().split(",");
        }
        if (destinationStrs != null) {
            destination += destinationStrs[0];
            if (!"县".contains(destinationStrs[1])) {
                destination += "" + destinationStrs[1];
                sb.setDestination(destination);
            }
        }

        String[] intelId = getRequest().getParameterValues("intelId"); // 资质ID集合
        String intelligenceIds = "";
        if (intelId != null && intelId.length > 0) {
            for (int i = 0; i < intelId.length; i++) {
                if (i == 0) {
                    intelligenceIds = intelligenceIds + intelId[i];
                } else {
                    intelligenceIds = intelligenceIds + "-" + intelId[i];
                }
            }
        }
        sb.setIntelligenceIds(intelligenceIds);
        sb.setUpdateTime(DateUtil.getSysDateTimeString());
        sb.setCheckStatus(0);
        return supplyBuyService.update(sb);

    }

    /**
     * 关闭
     */
    @RequestMapping(value = "/store/supplyBuy/ylxqDetele/{id}")
    @ResponseBody
    public boolean supplyBuyUpdate(@PathVariable String id, HttpServletResponse response) {
        //得到对应信息
        SupplyBuy sb = supplyBuyService.getSupplyBuyById(id);
        sb.setStatus("0");
        if (supplyBuyService.update(sb)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/store/supplyBuy/ylxqRemove/{id}")
    @ResponseBody
    public boolean supplyBuyRemove(@PathVariable String id, HttpServletResponse response) {
        if (supplyBuyService.remove(id)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * load得出所有资质
     */
    @RequestMapping(value = "/web/supplyBuy/jumpIntelPage/load")
    public String jumpIntelLoadPage() {
        List<Intel> alllist = intelService.getIntelAll();

        getRequest().setAttribute("ilist", alllist);

        return "/userCenter/store/supplyBuy/zizhi_load";
    }

    /**
     * load资质
     *
     * @return
     */
    @RequestMapping(value = "/web/supplyBuy/loadZz")
    public String IntelLoadPage() {
        List<Intel> alllist = intelService.getIntelAll();
        getRequest().setAttribute("ilist", alllist);
        return "/web/channel/xhjy/loadZizhi";
    }

    /**
     * 跳转信息交互到列表
     */
    @RequestMapping(value = "/store/supplyBuy/wfpList")
    public String wfpList(HttpServletRequest request, HttpSession session) {
        Store store = (Store) getSession().getAttribute(SystemConst.STORE);
        // 定义分页
        Pager pager = new Pager();
        // 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            currentPage = Integer.parseInt(currentpageStr);
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        //页面上传来的参数
        //标题
        String title = request.getParameter("title");
        //创建时间
        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        //状态
        String storeName = request.getParameter("storeName");
        String type = request.getParameter("type");

        List<SupplyBuy> supplyBuys = supplyBuyService.listSupplyBuy(pager, title, beginDate, endDate, null, type, "1", "2", "1", "1", storeName);
        //绑定会页面
        request.setAttribute("supplyBuys", supplyBuys);
        request.setAttribute("title", title);
        request.setAttribute("beginDate", beginDate);
        request.setAttribute("endDate", endDate);
        request.setAttribute("storeName", storeName);
        request.setAttribute("pager", pager);
        request.setAttribute("currentpageStr", currentPage);
        return "/userCenter/store/supplyBuy/wfpList";
    }

    /**
     * 危废品供求详情
     */
    @RequestMapping(value = "/store/supplyBuy/wfpQuery/{id}")
    public String wfpQuery(@PathVariable String id, HttpServletRequest request, HttpSession session) {
        SupplyBuy sb = supplyBuyService.getSupplyBuyById(id);
        //得到所有的资质集合
        String intelligenceIds = sb.getIntelligenceIds();
        //返回页面的纸质集合
        List<Intel> ilist = new ArrayList<Intel>();
        if (!StringUtil.isEmpty(intelligenceIds)) {
            String[] intelligenceIdsList = intelligenceIds.split("-");
            for (int i = 0; i < intelligenceIdsList.length; i++) {
                ilist.add(intelService.getIntelById(intelligenceIdsList[i]));
            }
        }

        request.setAttribute("ilist", ilist);
        request.setAttribute("sb", sb);
        return "/userCenter/store/supplyBuy/wfpQuery";

    }
}
