package com.ekfans.controllers.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.order.model.OrderPhoto;
import com.ekfans.base.order.service.IOrderPhotoService;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.service.web.ProductDetail.IProductDetailService;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.system.service.ISystemAreaService;
import com.ekfans.basic.controller.BasicController;

/**
 * 交易快照Controller
 * 
 * @ClassName: ProductDetailController
 * @Description:
 * @author lgy
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Controller
public class orderPhotoController extends BasicController {


	@Autowired
	private IOrderPhotoService orderPhotoService;
	@Autowired
	private ISystemAreaService systemAreaService;
	@Autowired
    private IProductDetailService productDetailService;
	/**
	 * 
	 * @Title: loadAppraise
	 * @Description: TODO(根据分页查询去当前商品的评价) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param request
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@RequestMapping(value = "/web/orderPhoto/detail/{odId}")
	public String loadAppraise(@PathVariable String odId,HttpServletRequest request) {
	    //得到对应的快照
	    OrderPhoto op =  orderPhotoService.getListByOdId(odId);
	    //调用方法得到产地的
	    op.setHabitat(systemAreaService.getAreaFullNameByAreaIdAndDelimiter(op.getHabitat(), ""));
        //得到对应的商品详情信息
	    Object[] detailArray = productDetailService.getProDetailByProId(op.getProductId());
        Product product = null;
        if (detailArray != null) {
            product = (Product) detailArray[1];
        }
        request.setAttribute("product", product);
	    request.setAttribute("op", op);
		return "/web/orderPhoto/orderPhotoDetail";
	}

	
}
