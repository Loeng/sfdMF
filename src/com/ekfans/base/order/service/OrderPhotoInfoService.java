package com.ekfans.base.order.service;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IOrderPhotoInfoDao;
import com.ekfans.base.order.model.OrderPhotoInfo;

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
public class OrderPhotoInfoService implements IOrderPhotoInfoService {
	// 定义错误日志
	private Logger log = LoggerFactory.getLogger(OrderPhotoInfoService.class);
	// 定义DAO
	@Autowired
	private IOrderPhotoInfoDao orderPhotoinfoDao;

	@Override
	public boolean add(OrderPhotoInfo info) {
		if (info == null) {
			return false;
		}
		try {
			orderPhotoinfoDao.addBean(info);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean add(OrderPhotoInfo info, Session session) {
		if (info == null) {
			return false;
		}
		try {
			orderPhotoinfoDao.addBean(info, session);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

}
