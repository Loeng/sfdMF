package com.ekfans.base.product.service.web.ProductDetail;

import java.util.List;

import com.ekfans.base.order.model.Appraise;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.product.model.ProductInfoDetail;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.base.product.service.web.ProductDetail.vo.StoreServiceVo;
import com.ekfans.pub.util.Pager;


/**
 * 
* @ClassName: IProductDetailService
* @Description: TODO(这里用一句话描述这个类的作用)
* @author 成都易科远见科技有限公司
* @date 2014-5-21 下午03:45:13
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IProductDetailService {
    /**
     * 
    * @Title: getProDetailByProId
    * @Description: TODO(根据商品Id获得商品详情)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param proId
    * @param @return    设定文件
    * @return Object[]    返回类型
    * @throws
     */
    public Object[] getProDetailByProId(String proId);
    

    /**
     * 
    * @Title: getAppraiseByProId
    * @Description: TODO(查询出当前商品id所对应的评价)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param proId
    * @param @return    设定文件
    * @return List<Appraise>    返回类型
    * @throws
     */
    public List<Appraise> getAppraiseByProId(String proId,String apType,Pager pager);
    
    /**
     * 
    * @Title: getAppraiseSum
    * @Description: TODO(获得该件商品的总评价数)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param proId
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    public String getAppraiseSum(String proId);
    
    /**
     * 
    * @Title: getProductDealRecord
    * @Description: TODO(对该件商品成交记录的统计)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param proId
    * @param @return    设定文件
    * @return List<OrderDetail>    返回类型
    * @throws
     */
    public List<OrderDetail> getProductDealRecord(String proId,Pager pager);
    
    /**
     * 
    * @Title: getProductDealRecordSum
    * @Description: TODO(获得当前商品当前月的总成交数量)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param proId
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    public String getProductDealRecordSum(String proId);
    
    /**
     * 
    * @Title: getRepertory
    * @Description: TODO(跳转到商品详情查询总库存)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    public String getRepertory(String proId);
    
    /**
     * 
    * @Title: addShoppingCart
    * @Description: TODO(加入购物车)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean addShoppingCart(String proId,String userId);
    
    /**
     * 
    * @Title: buyNow
    * @Description: TODO(立即结算该商品、将该商品加入购物车并将
    * 该商品的isChecked状态设置为true、购物车的其他上平isChecked状态设置为false)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param userId
    * @param @param proId
    * @param @return    设定文件
    * @return boolean    返回类型
    * @throws
     */
    public boolean buyNow(String userId,String proId);
    
    /**
     * 
    * @Title: getProInfoDetailByCondition
    * @Description: TODO(商品分类选择ProInfo可选条件的时候,获得满足该条件该商品的infoDetail)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param proId
    * @param @param info1NameAndValue
    * @param @param info2NameAndValue
    * @param @return    设定文件
    * @return ProductInfoDetail    返回类型
    * @throws
     */
    public ProductInfoDetail getProInfoDetailByCondition(String proId,String info1NameAndValue,String info2NameAndValue,String info3NameAndValue,String info4NameAndValue);

    /**
     * 
    * @Title: getProPicture
    * @Description: TODO(获取商品的多角度视图)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param proId
    * @param @return    设定文件
    * @return List<String>    返回类型
    * @throws
     */
    public List<ProductPicture> getProPicture(String proId);
    
    /**
     * 
    * @Title: getServiceByServiceName
    * @Description: TODO(s获取店铺的服务集合)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param serviceName
    * @param @return    设定文件
    * @return    返回类型
    * @throws
     */
    public List<StoreServiceVo> getServiceByServiceName(String storeId);
}
