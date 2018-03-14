package com.ekfans.base.order.model.vo;

import com.ekfans.base.order.model.Appraise;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductInfoDetail;

public class OderDetailProduct {

    private OrderDetail orderDetail;

    private Product product;

    private ProductInfoDetail productInfoDetail;
    
    private Appraise appraise;

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductInfoDetail getProductInfoDetail() {
        return productInfoDetail;
    }

    public void setProductInfoDetail(ProductInfoDetail productInfoDetail) {
        this.productInfoDetail = productInfoDetail;
    }

    public Appraise getAppraise() {
        return appraise;
    }

    public void setAppraise(Appraise appraise) {
        this.appraise = appraise;
    }

}
