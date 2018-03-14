package com.ekfans.base.product.service;

import java.util.List;

import com.ekfans.base.product.model.ProductPicture;

/**
 * 
* @ClassName: IProductPictureService
* @Description: TODO(多角度视图的借口)
* @author ekfans
* @date 2014-6-16 下午04:40:08
* @version v1.0
* Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
* Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IProductPictureService {
    
    /**
     * 
    * @Title: addProductPicture
    * @Description: TODO(多角度视图的添加操作)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param pP
    * @return boolean 返回类型
    * @throws
     */
    public boolean addProductPicture(ProductPicture pP,String picturePath);
    
    /**
     * 
    * @Title: getList
    * @Description: TODO(根据商品id查出全部数据)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @return List<ProductPicture> 返回类型
    * @throws
     */
    public List<ProductPicture> getList(String productId);
    
    /**
     * 
    * @Title: getProductIdDelete
    * @Description: TODO(根据商品id删除对应的多角度视图)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param productId
    * @return boolean 返回类型
    * @throws
     */
    public boolean getProductIdDelete(String productId);
}
