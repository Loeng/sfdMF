/**
 * 
 */
package com.ekfans.controllers.system.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.product.model.ProductCategoryRel;
import com.ekfans.base.product.service.IProductCategoryRelService;
import com.ekfans.base.product.service.IProductCategoryService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.pub.util.JsonUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 
* @ClassName: SystemProductBatchController
* @Description: 商品批量操作控制器
* @author ekfans
* @date 2014年7月8日 上午11:16:25
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
@RequestMapping("/system/product/batch")
public class SystemProductBatchController {
	
	/**
	 * 商品分类业务接口
	 */
	@Autowired
	private IProductCategoryService productCategoryService;
	
	/**
	 * 商品业务接口
	 */
	@Autowired
	private IProductService productService;
	
	/**
	 * 商品分类关联业务接口
	 */
	@Autowired
	private IProductCategoryRelService productCategoryRelService;
	
  /**
	* 
	* @Title: toView
	* @Description: 跳转视图
	* @param request HttpServletRequest
	* @return String 返回类型
	*/
	@RequestMapping(value="/move")
	public String toView(HttpServletRequest request){
	    // 得到通用分类的子分类
        List<ProductCategory> categories = productCategoryService.getChildCategories(Cache.getResource("product.category.rootcategory"));
        // 得到频道分类
        ProductCategory category = productCategoryService.getCategoryById("CHANNEL");
		//将商品分类数据集设置在requestScope
        List<ProductCategory> cates = new ArrayList<ProductCategory>();
        cates.add(category);
        //将商品分类数据集设置在requestScope
        request.setAttribute("categories", cates);
        request.setAttribute("generalCategoryChild", categories);
		return "/system/product/batch/move";	
	}
	
	/**
     * 
    * @Title: child
    * @Description: TODO 查找子分类
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @param request
    * @return String 返回类型
    * @throws
     */
    @RequestMapping(value = "/child/{id}")
    public String child(@PathVariable String id, HttpServletRequest request) {
        // 查询出分类列表返回到页面
        List<ProductCategory> categories = productCategoryService.getChildCategories(id);
        request.setAttribute("categories", categories);
        return "/system/product/batch/categoryTree";
    }
    
  /**
	* @Title: getProductsByCategoryId
	* @Description: 通过分类ID获取商品列表
	* @param id 分类id
	* @return String 返回类型 JSON
	*/
	@RequestMapping(value="/getProductsByCategoryId/{id}/{pageNum}")
	public String getProductsByCategoryId(@PathVariable("id")String id,@PathVariable("pageNum")String pageNum,HttpServletRequest request){
		if(StringUtils.isEmpty(id)){
			return null;
		}
		// 定义分页
        Pager pager = new Pager();

        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(pageNum)) {
            try {
                currentPage = Integer.parseInt(pageNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pager.setCurrentPage(currentPage);
		//通过分类ID获取商品列表
		List<Product> list = productService.queryProductsByCategoryId(pager,id);
		request.setAttribute("products", list);
		request.setAttribute("pager", pager);
		request.setAttribute("totalRow", pager.getTotalRow());

		return "/system/product/batch/productListTop";
	}
	
	/**
	* @Title: searchProducts
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param request
	* @return String 返回类型
	 */
	@RequestMapping(value="/searchProducts")
	public String searchProducts(HttpServletRequest request){
	    // 定义分页
        Pager pager = new Pager();
        
        String pageNum = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(pageNum)) {
            try {
                currentPage = Integer.parseInt(pageNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pager.setCurrentPage(currentPage);
        
		Map<String,Object> map = new HashMap<String,Object>();
		//获取商品名称
		map.put("pName", "%"+request.getParameter("searchName")+"%");
		//得到所有子分类id
		List<String> ids = productCategoryService.getAllCategoryIdsById(request.getParameter("categoryId"));
		//设置分类id
        map.put("ids", ids);
		//获取下移的频道分类编号
		map.put("categoryId", request.getParameter("downId"));
		//查询频道分类下的临时商品集合
		List<Product> products = productService.queryProductsByParams(pager,map);
		request.setAttribute("products", products);
		request.setAttribute("pager", pager);
        request.setAttribute("totalRow", pager.getTotalRow());

        return "/system/product/batch/productListBot";
	}
	
	/**
	 * 
	* @Title: getProductChildCategory
	* @Description: TODO 批量移动的分类选择得到子分类
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param request
	* @param response void 返回类型
	* @throws
	 */
	@RequestMapping(value = "/categoryChild", method = RequestMethod.POST)
    public void getProductChildCategory(HttpServletRequest request, HttpServletResponse response) {
        // 获取当前的分类Id
        String id = request.getParameter("id");
        // 查询出子分类,并将其转为JSON字符串返回
        List<ProductCategory> categories = productCategoryService.getChildCategories(id);
        String JsonStr = JsonUtil.listToJson(categories);
        try {
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(JsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/**
	 * 
	* @Title: screenProducts
	* @Description:商品集合比较去重
	* @param ps1 List<Product> 通用分类下的商品集合
	* @param ps2 List<Product> 频道分类下的商品集合
	 */
	public void screenProducts(List<Product> ps1,List<Product> ps2){
		if(null != ps1 && null != ps2){
			//获取迭代器
			Iterator<Product> it = ps1.iterator();
			//遍历通用分类下的商品集合
			while(it.hasNext()){
				Product pro = it.next();
				//遍历频道分类下的商品集合
				for (Product product : ps2) {
					//如果通用分类下的商品集合中含有频道分类下商品集合中的商品，则删除该元素
					if(pro.getId().equals(product.getId())){
						it.remove();
					}
				}
			}
		}
	}

	
	/**
	 * 
	* @Title: move
	* @Description: 将商品移动至选择的分类中
	* @param cid 分类ID
	* @param pid 商品ID
	* @param isUp 是否为像上移动
	* @return String 返回类型
	 */
	@RequestMapping(value="/moveProduct/{cid}/{pid}/{isUp}",method=RequestMethod.POST)
	@ResponseBody
	public String move(@PathVariable("cid")String cid,@PathVariable("pid")String pid,@PathVariable("isUp")String isUp){
		//封装PO
		ProductCategoryRel pcRl = new ProductCategoryRel();
		pcRl.setCategoryId(cid);
		pcRl.setProductId(pid);
		pcRl.setMain(false);
		pcRl.setPosition(0);
		//返回结果
		boolean result = false;
		//如果为向上移动，则删除商品分类关联表中的关联
		if(isUp.equals("up")){
			result = productCategoryRelService.remove(pcRl);	
		}else{
			//否则新增关联至商品关联表中
			result = productCategoryRelService.add(pcRl);
		}
		if(result){
			return "success";
		}else{
			return "fail";
		}
	}
	
}
