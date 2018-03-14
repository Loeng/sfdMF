package com.ekfans.base.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import com.ekfans.base.order.model.Inquiry;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Repository
public class InquiryDetailDao extends GenericDao implements IInquiryDetailDao {

	public InquiryDetailDao() throws HibernateException {
		this.beanClass = "com.ekfans.base.order.model.InquiryDetail";
	}

	
}
