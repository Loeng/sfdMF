package com.ekfans.base.product.service.web.productList;

import java.util.List;

import com.ekfans.base.product.model.ProductBrand;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.controllers.web.vo.ProListTemplateVO;
import com.ekfans.pub.util.Pager;

/**
 * 
* @ClassName: IProductListService
* @Description: TODO(前台商品列表)
* @author 成都易科远见科技有限公司
* @date 2014-5-19 上午09:47:21
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IProductListService {
    
    /**
     * 
    * @Title: getProductByConditions
    * @Description: TODO(商品列表里按条件查询与排序<排序使用单一排序>)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param mainCategory
    * @param @param brand
    * @param @param salesVolumeType
    * @param @param popularityType
    * @param @param newestType
    * @param @param priceType
    * @param @param ProductArea
    * @param @param pager
    * @param @return    设定文件
    * @return List<Product>    返回类型
    * @throws
     */
    public Object[] getProductByConditions(
                                                // 先决条件
                                                String productName,
                                                // 搜索条件
                                                String categoryId,
                                                String brand,
                                                // proinfoDetail
                                                String infoNameAndValue1,
                                                String infoNameAndValue2,
                                                String infoNameAndValue3,
                                                String infoNameAndValue4,
						// 排序条件
						String sortNameAndType,
                                                // 商品地址(卖家店铺的地址)
                                                String ProductArea,
                                                // 分页
                                                Pager pager);
    
    /**
     * 
    * @Title: findThisProductCatagory
    * @Description: TODO(查询出匹配商品的所属分类,
    *                    分两种情况,一种情况是点首页的分类表进到商品列表,这是传过来了
    *                    一个categoryId,这时根据categoryId查询出插叙出起对应的子分类,
    *                    如果没有子分类,就查询出起同级分类;如果根据商品名字搜索商品进入
    *                    商品列表,就查询出满足商品的所属父级分类)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param productName
    * @param @return    设定文件
    * @return List<ProductCategory>    返回类型
    * @throws
     */
    public List<ProductCategory> findThisProductCatagory(String categoryId,String productName);
    
    /**
     * 
    * @Title: findThisProductBrand
    * @Description: TODO(查询出匹配商品所属品牌)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param productName
    * @param @return    设定文件
    * @return List<ProductBrand>    返回类型
    * @throws
     */
    public List<ProductBrand> findThisProductBrand(List<String> categoryIds,String productName);
    
    /**
     * 
    * @Title: findProductTemplateFields
    * @Description: TODO(根据productName获得商品所属模板详细字段名和详细字段值)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param templateId
    * @param @return    设定文件
    * @return List<TemplateFields>    返回类型
    * @throws
     */
    public List<ProListTemplateVO> findProductTemplateFields(List<String> categoryIds,String productName);
    
    /**
     * 
    * @Title: getDefaultProCategory
    * @Description: TODO(如果点击商品分类进入商品列表
    *                    查询出默认分类的信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param categoryId
    * @param @return    设定文件
    * @return ProductCategory    返回类型
    * @throws
     */
    public ProductCategory getDefaultProCategory(String categoryId);
}
