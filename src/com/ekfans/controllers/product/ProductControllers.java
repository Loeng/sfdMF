package com.ekfans.controllers.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.product.service.ProductService;

@Controller
public class ProductControllers {
    @Autowired
    private ProductService productService;
    //点击首页选购跳转到优选商城
    @RequestMapping(value ="/product/indexmaill")
    public String indexMaill(){
        
        return "/web/storeweb/storeIndex";
    }
}
