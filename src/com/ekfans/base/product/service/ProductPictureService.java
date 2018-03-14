package com.ekfans.base.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.product.dao.IProductPictureDao;
import com.ekfans.base.product.model.ProductPicture;
import com.ekfans.plugin.image.Im4Java;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class ProductPictureService implements IProductPictureService{

    @Autowired
    private IProductPictureDao productPictureDao;
    
  //定义错误日志
    public static Logger log = LoggerFactory.getLogger(ProductPictureService.class);
    /**
     * 
     * 添加
     */
    public boolean addProductPicture(ProductPicture pP,String picturePath) {
        if (pP == null) {
            return false;
        }
        try {
            
            if(!StringUtil.isEmpty(picturePath)){
                //  保存原图
                pP.setPicture(picturePath);
                
                // 得到后缀名
                String suffix = picturePath.substring(picturePath.length()-4, picturePath.length());
                
                // 得到文件路径(无后缀名)
                String prefix = picturePath.substring(0, picturePath.length()-4);
                
                // 大图路径
                String bigPicturePath = prefix + "big" + suffix;
                // 压缩为大图保存
                Im4Java.cutImage(400, 400, picturePath, bigPicturePath);
                pP.setBigPicture(bigPicturePath);
                
                // 中图路径
                String midPicturePath = prefix + "middle" + suffix;
                // 压缩为中图保存
                Im4Java.cutImage(200, 200, picturePath, midPicturePath);
                pP.setMidPicture(midPicturePath);
                  
                // 小图路径
                String smallPicturePath = prefix + "small" + suffix;
                // 压缩为小图保存
                Im4Java.cutImage(100, 100, picturePath, smallPicturePath);
                pP.setSmallPicture(smallPicturePath);
            }
            productPictureDao.addBean(pP);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据商品id查出多图片视图
     */
    public List<ProductPicture> getList(String productId) {
        StringBuffer sql = new StringBuffer("select pP from ProductPicture as pP where 1=1");
        
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(productId)) {
            sql.append(" and pP.productId = :productId");
            map.put("productId", productId);
        }
            sql.append(" order by pP.position asc");
        try {
            List<ProductPicture> list = productPictureDao.list(sql.toString(), map);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据productId删除多角度视图
     */
    public boolean getProductIdDelete(String productId) {
     // 定义查询SQL
        StringBuffer sql = new StringBuffer(" delete pp from ProductPicture as pp where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        //如果roleId不为空
        if (!StringUtil.isEmpty(productId)) {
            // 添加查询条件
            sql.append(" and pp.productId = :productId");
            paramMap.put("productId", productId);
        }
        try {
            productPictureDao.delete(sql.toString(), paramMap);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
}
