package com.ekfans.base.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderValuationDao;
import com.ekfans.base.order.model.OrderValuation;

@Service
public class OrderValuationService implements  IOrderValuationService{
    @Autowired
    private IOrderValuationDao orderValuationDao;
    @Override
    public boolean addOrderValuation(OrderValuation ov) {
        // TODO 添加
        try {
            orderValuationDao.addBean(ov);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 根据报废ID查询含量
     */
    @Override
    public List<OrderValuation> getOrderValuationById(String id) {
        StringBuffer buffer = new StringBuffer("from OrderValuation o where o.orderId='"+id+"'");
        try {
           return orderValuationDao.list(buffer.toString(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
