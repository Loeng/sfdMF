package com.ekfans.controllers.store.product;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.service.InquiryService;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.ProductTemplate;
import com.ekfans.base.product.model.SupplyProduct;
import com.ekfans.base.product.model.TemplateFields;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductTemplateService;
import com.ekfans.base.product.service.ISupplyProductService;
import com.ekfans.base.product.service.ITemplateFieldsService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.BankBinding;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IBankBindingService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class SupplyProductController extends BasicController{

    @Resource
    private ISupplyProductService supplyProductService;
    @Autowired
    private IProductCategoryService productCategoryService;
    @Autowired
    private IProductTemplateService productTemplateService;
    @Autowired
    private ITemplateFieldsService templateFieldsService;
    @Autowired
    private InquiryService inquiryService;
    @Autowired
    private IBankBindingService bankdingService;
    /**
    * @Title: saveSupplyProduct
    * @Description: TODO 保存供应商商品议价
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param supplyProduct
    * @return Object 返回类型
    * @throws
     */
    @RequestMapping(value="/store/product/savesupply")
    @ResponseBody
    public Object saveSupplyProduct(HttpServletRequest request,SupplyProduct supplyProduct){
        User user = (User) getSession().getAttribute(SystemConst.USER);
        //创建时间
        supplyProduct.setCreateTime(DateUtil.getSysDateTimeString());
        supplyProduct.setUserId(user.getId());
        if(supplyProduct!=null){
            supplyProductService.saveSupplyProduct(supplyProduct);
        }else{
            return false;
        }
        return true;
    }
    /**
     * 
    * @Title: addClassifyByGys
    * @Description: TODO(供应商发布商品,选择商品分类)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping (value= "/store/product/supplyProductAddClassify")
    public  String addClassifyByGys(HttpServletRequest request) {
        // 获取页面的name属性
        String name = request.getParameter("name");
        // 查询出分类列表返回到页面
        List<ProductCategory> pcs = productCategoryService.getCategories(name,null);
        request.setAttribute("pcs", pcs);
        return "/userCenter/store/product/supplyProductAddClassify";
    }
    /**
    * @Title: editSupplyProduct
    * @Description: TODO(这里用一句话描述这个方法的作用)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @param sp
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/store/supply/supplyEdit")
    @ResponseBody
    public Object editSupplyProduct(HttpServletRequest request,SupplyProduct sp){
    try {
    	   sp.setCreateTime(DateUtil.getSysDateTimeString());
           return  this.supplyProductService.update(sp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "true";
    }
    
    /**
    * @Title: getSupplyList
    * @Description: TODO(获取供应商商品列表)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value ="/store/supply/supplyProductList")
    public String getSupplyList(HttpServletRequest request){
    	String productName = request.getParameter("productName"); // 商品名称
    	String productNum = request.getParameter("productNum"); // 商品编号
    	String currentpageStr = request.getParameter("pageNum"); // 从页面获取页码
        // 定义分页
        Pager pager = new Pager();
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
        	currentPage = Integer.parseInt(currentpageStr);
        }
        pager.setCurrentPage(currentPage);
        pager.setRowsPerPage(10);
        List<SupplyProduct> supply = supplyProductService.getListByGys(productName, productNum, pager);
        request.setAttribute("supply", supply);
        request.setAttribute("pager", pager);
        request.setAttribute("productName", productName);
        request.setAttribute("productNum", productNum);
        
        return "/userCenter/store/product/supplyList";
    }
    /**
    * @Title: lookSupplyProductById
    * @Description: TODO(查看商品的详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @param type
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value="/store/supply/lookSupply/{id}/{type}")
    public String lookSupplyProductById(@PathVariable String id, @PathVariable String type, HttpServletRequest request){
    	SupplyProduct sp = null;
    	try {
			sp = this.supplyProductService.getSupplyProductById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 
    	request.setAttribute("sp", sp);
        request.setAttribute("type", type);
        
        return "/userCenter/store/product/supplyListModefiy";
    }
    
    /**
    * @Title: checkSupplyNum
    * @Description: TODO(页面搜索)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param request
    * @return int 返回类型
    * @throws
     */
    @RequestMapping(value ="/store/supply/checkSupplyNum")
    @ResponseBody
    public int checkSupplyNum(HttpServletRequest request){
        //获取页面商品编号信息
        String productNum = request.getParameter("productNum");
        return supplyProductService.getCheckProductNum(productNum);
    }
    /**
     * @Title: supplyadd
     * @Description: TODO(跳转供应商发布商品页面)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param request
     * @return String 返回类型
     * @throws
      */
     @RequestMapping(value="/store/supply/product/add")
     public String supplyadd(HttpServletRequest request){
      // 得到页面的商品分类Id
         String id = request.getParameter("pcId");
         // 从ProductCategory取得模板Id
         String templateId = productCategoryService.getTemplateByCategoryId(id);
         String pcName = productCategoryService.getCategoryFullNameByCategoryId(id, " > ");
         // 根据模板Id取得模板对象
         ProductTemplate productTemplate = productTemplateService.getProductTemplateByTempLateId(templateId);
         // 根据模板Id取得模板扩展字段集合
         List<TemplateFields> templateFields = templateFieldsService.getProductTempalteFieldsByTemplateId(templateId);
         request.setAttribute("pcName", pcName);
         request.setAttribute("pcId", id);
         request.setAttribute("productTemplate", productTemplate);
         request.setAttribute("templateFields", templateFields);
         return "/userCenter/store/product/supplyProductAdd";
     }
     /**
     * @Title: getSupplyList
     * @Description: TODO(供应查询核心企业议价信息)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @return String 返回类型
     * @throws
      */
     @RequestMapping(value ="/store/supply/coreSupplyList")
    public String getSupplyList(HttpServletRequest request,HttpSession session){
         User user = (User) session.getAttribute(SystemConst.USER); //获取缓存中登陆用户
         String supplyProductName = request.getParameter("supplyProductName"); // 商品名称
         String status = request.getParameter("status"); // 议价状态
         String currentpageStr = request.getParameter("pageNum"); // 从页面获取页码
         
         // 定义分页
         Pager pager = new Pager();
         int currentPage = 1;
         if (!StringUtil.isEmpty(currentpageStr)) {
         	currentPage = Integer.parseInt(currentpageStr);
         }
         pager.setCurrentPage(currentPage);
         
         List<Inquiry> ilist = inquiryService.getSupplyList(pager, status, supplyProductName, user.getId());
         
         request.setAttribute("pager", pager);
         request.setAttribute("inquiry", ilist);
         request.setAttribute("userId", user.getId());
         request.setAttribute("supplyProductName", supplyProductName);
         request.setAttribute("status", status);
         
        return "/userCenter/store/coreCompany/coreSupplyList";
	}
     @RequestMapping(value = "/supply/loadSupplyGysHf/{id}/{type}/{userId}")
     public String loadByIdGysHf(@PathVariable String id,@PathVariable String type,@PathVariable String userId){
         // 得到议价信息
         Inquiry i = inquiryService.getById(id);
         //根据议价ID查询议价详情
         getRequest().setAttribute("inquiry", i);
         getRequest().setAttribute("type", type);
         getRequest().setAttribute("userId", userId);
         return "/userCenter/store/coreCompany/loadSupplyGysHf";
     }
     
     @RequestMapping(value="/store/supplyPrice/Add", method=RequestMethod.POST)
     @ResponseBody
     public String GysHfSupply(HttpServletRequest request ){
         //获取议价ID
         //获取ID
         String id = request.getParameter("id");
         Inquiry in = inquiryService.getId(id);
         if(in!=null){
             //获取核定价格
             String number = request.getParameter("sellersNumber");
             //获取核定数量
             String FinalPrice = request.getParameter("FinalPrice");
             in.setSellersNumber(Integer.parseInt(number));
             in.setFinalPrice(new BigDecimal(FinalPrice));
             in.setUpdateTime(DateUtil.getSysDateTimeString());
             in.setEndTime(request.getParameter("updateTime"));
             //设置已回复
             in.setStatus("1");
             inquiryService.updateInquiry(in);
             return "1";
         }
         return "0";
     }
     /**
     * @Title: bankList
     * @Description: TODO(根据ID查询企业绑定的银行卡)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param request
     * @param session
     * @return String 返回类型
     * @throws
      */
     @RequestMapping(value="/store/account/bank/list")
     public String bankList(HttpServletRequest request,HttpSession session){
         //从session中获取登录企业ID
         Store store = (Store)session.getAttribute(SystemConst.STORE);
         List<BankBinding> list =  bankdingService.getBankBindingByStoreId(store.getId(), null);
         int count = 0 ;
         if(list!=null && list.size()>0){
             for(int i = 0 ;i<list.size();i++){
                 BankBinding b = list.get(i);
                 if(b.getStatus()==true){
                     count ++;
                 }
             }
         }
         request.setAttribute("count", count);
         request.setAttribute("banklist", list);
         return "/userCenter/store/coreCompany/bankCardList";
     }
     /**
     * @Title: closeSupply
     * @Description: TODO(根据议价单ID关闭议价单)
     * 详细业务流程:
     * (详细描述此方法相关的业务处理流程)
     * @param id
     * @return Object 返回类型
     * @throws
      */
     @RequestMapping(value="/store/supply/closeSupply/{id}")
     @ResponseBody
     public Object closeSupply(@PathVariable String id){
         
        return  inquiryService.closeSupply(id);
     }
}
