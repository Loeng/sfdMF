package com.ekfans.base.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderPhotoDao;
import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.order.model.OrderPhoto;
import com.ekfans.base.order.model.OrderPhotoInfo;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductInfo;
import com.ekfans.base.product.service.IProductInfoService;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 订单快照业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2014-1-6
 * @version 1.0
 */
@Service
public class OrderPhotoService implements IOrderPhotoService {
	// 定义错误日志
	private Logger log = LoggerFactory.getLogger(OrderPhotoService.class);
	// 定义DAO
	@Autowired
	private IOrderPhotoDao orderPhotoDao;

	@Override
	public boolean add(OrderPhoto op) {
		try {
			orderPhotoDao.addBean(op);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 生成订单快照
	 * 
	 * @param detail
	 * @return
	 */
	public boolean createPhoto(OrderDetail detail, Product product, HttpServletRequest request, Session session) {
		if (detail == null) {
			return false;
		}
		try {
			// 反射定义商品扩展信息Service
			IProductInfoService productInfoService = SpringContextHolder.getBean(IProductInfoService.class);
			// 反射定义订单快照扩展属性DAO
			IOrderPhotoInfoService orderPhotoInfoService = SpringContextHolder.getBean(IOrderPhotoInfoService.class);

			// 定义一个心的商品快照对象
			OrderPhoto photo = new OrderPhoto();

			List<ProductInfo> infoList = null;
			if (product != null) {
				infoList = productInfoService.getProductInfoByProductId(product.getId(), session);
			} else {
				return false;
			}
			ServletContext servletContext = request.getSession().getServletContext();

			// 复制商品原图
			if (!StringUtil.isEmpty(product.getPicture())) {
				String oldPic = product.getPicture();
				String picture = "/customerfiles/store/" + detail.getStoreId() + "/productCopy/" + java.util.UUID.randomUUID().toString() + oldPic.substring(oldPic.lastIndexOf("."));
				FileUtil.copyFile(servletContext.getRealPath(oldPic), servletContext.getRealPath(picture));
				photo.setPicture(picture);
			}

			// 复制商品大图
			if (!StringUtil.isEmpty(product.getBigPicture())) {
				String oldPic = product.getBigPicture();
				String picture = "/customerfiles/store/" + detail.getStoreId() + "/productCopy/Big" + java.util.UUID.randomUUID().toString() + oldPic.substring(oldPic.lastIndexOf("."));
				FileUtil.copyFile(servletContext.getRealPath(oldPic), servletContext.getRealPath(picture));
				photo.setBigPicture(picture);
			}

			// 复制商品中图
			if (!StringUtil.isEmpty(product.getCenterPicture())) {
				String oldPic = product.getCenterPicture();
				String picture = "/customerfiles/store/" + detail.getStoreId() + "/productCopy/Center" + java.util.UUID.randomUUID().toString() + oldPic.substring(oldPic.lastIndexOf("."));
				FileUtil.copyFile(servletContext.getRealPath(oldPic), servletContext.getRealPath(picture));
				photo.setCenterPicture(picture);
			}

			// 复制商品小图
			if (!StringUtil.isEmpty(product.getSmallPicture())) {
				String oldPic = product.getSmallPicture();
				String picture = "/customerfiles/store/" + detail.getStoreId() + "/productCopy/Small" + java.util.UUID.randomUUID().toString() + oldPic.substring(oldPic.lastIndexOf("."));
				FileUtil.copyFile(servletContext.getRealPath(oldPic), servletContext.getRealPath(picture));
				photo.setSmallPicture(picture);
			}

			// 复制订单ID
			photo.setOrderId(detail.getOrderId());
			// 复制订单明细ID
			photo.setOrderDetailId(detail.getId());
			// 复制商品ID
			photo.setProductId(detail.getProductId());
			// 复制商品名称
			photo.setProductName(detail.getProductName());
			// 复制店铺ID
			photo.setStoreId(detail.getStoreId());
			// 复制商城价
			photo.setUnitPrice(product.getUnitPrice());
			// 复制市场价
			photo.setListPrice(product.getListPrice());
			// 复制最终成交价
			photo.setLastPrice(detail.getPrice());
			// 复制单位
			photo.setUnit(product.getUnit());
			// 复制品牌
			photo.setBrand(product.getBrandName());
			// 复制商品详细描述
			photo.setDescription(product.getDescription());
			// 复制商品描述
			photo.setNote(product.getNote());
			// 复制商品类型
			photo.setType(product.getType());
			// 复制模板ID
			photo.setTemplateId(product.getTemplateId());
			// 复制商品主分类
			photo.setMainCategory(product.getMainCategory());
			// 复制地址
			photo.setHabitat(product.getHabitat());
			// 复制详细地址
			photo.setHabitatAddress(product.getHabitatAddress());
			// 复制仓库ID
			photo.setWareHouseId(product.getWareHouseId());
			// 复制运费
			photo.setFare(product.getFare());
			// 复制商品编号
			photo.setProductNumber(product.getProductNumber());
			// 调用DAO添加订单商品快照
			orderPhotoDao.addBean(photo, session);

			// 循环查出来的商品扩展字段属性
			if (infoList != null && infoList.size() > 0) {
				// 获取到商品扩张字段
				for (ProductInfo info : infoList) {
					// 定义一个新的订单快照扩展信息明细
					OrderPhotoInfo photoInfo = new OrderPhotoInfo();
					// 复制分类ID
					photoInfo.setCategoryId(info.getCategoryId());
					// 复制分类名称
					photoInfo.setCategoryName(info.getCategoryName());
					// 复制扩展信息名称
					photoInfo.setInfoName(info.getInfoName());
					// 复制扩展信息类型
					photoInfo.setInfoType(info.getInfoType());
					// 复制扩展信息值
					photoInfo.setInfoValue(info.getInfoValue());
					// 设置快照ID
					photoInfo.setPhotoId(photo.getId());
					// 复制排序
					photoInfo.setPosition(info.getPosition());
					// 复制模板ID
					photoInfo.setTemplateFieldId(info.getTemplateFieldId());
					// 调用DAO使用事物处理插入数据库
					orderPhotoInfoService.add(photoInfo, session);
				}
			}
			return true;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OrderPhoto getListByOdId(String odId) {
		// 定义参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义hql
		StringBuffer hql = new StringBuffer("select op,pc.name from OrderPhoto as op,ProductCategory as pc where 1=1 ");
		// 设置参数
		hql.append(" and op.mainCategory = pc.id");
		if (!StringUtil.isEmpty(odId)) {
			hql.append(" and op.orderDetailId =:odId");
			paramMap.put("odId", odId);
		} else {
			return null;
		}

		try {
			List<Object[]> list = orderPhotoDao.list(hql.toString(), paramMap);

			if (list != null && list.size() > 0) {
				Object[] obj = list.get(0);
				OrderPhoto op = (OrderPhoto) obj[0];
				op.setMainCategory((String) obj[1]);
				return op;
			}
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
