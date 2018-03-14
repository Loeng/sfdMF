package com.ekfans.base.store.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.model.Order;
import com.ekfans.base.order.model.OrderWfp;
import com.ekfans.base.store.dao.IMortgageApplicationDao;
import com.ekfans.base.store.model.MortgageApplication;
import com.ekfans.base.wfOrder.dao.IContractDao;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款申请Service接口实现
 * 
 * @ClassName: MortgageApplicationService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class MortgageApplicationService implements IMortgageApplicationService {

	private Logger log = LoggerFactory.getLogger(MortgageApplicationService.class);
	@Resource
	private IMortgageApplicationDao mortgageApplicationDao;
	@Resource
	private IContractDao contractDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MortgageApplication> getMAList(Pager pager, Integer type, String storeId, String startTime, String endTime, String orderId, String minMoney, String maxMoney, String contractNo, String status) {
		// 定义参数Map
		Map<String, Object> map = new HashMap<String, Object>();
		// hql 
		StringBuffer hql = null;
		if(type == 1){
			hql = new StringBuffer("select ma,o from MortgageApplication ma,Order o where ma.orderId=o.id and");
		}else if(type == 2){
			hql = new StringBuffer("from MortgageApplication ma where");
		}else if(type == 3){
			hql = new StringBuffer("select ma,c from MortgageApplication ma,Contract c where ma.contractId=c.id and");
		}else if(type == 4){
			hql = new StringBuffer("select ma,ow from MortgageApplication ma,OrderWfp ow where ow.orderNumber=ma.orderId and");
		}
		hql.append(" ma.storeId=:storeId and ma.type=:type");
		
		map.put("storeId", storeId);
		map.put("type", type);
		if(!StringUtil.isEmpty(status)){
			hql.append(" and ma.status=:status");
			if("提交中".equals(status)){
				map.put("status", 1);
			}else if("审核中".equals(status)){
				map.put("status", 2);
			}else if("审核成功".equals(status)){
				map.put("status", 3);
			}
		}
		if(!StringUtil.isEmpty(startTime)){
			hql.append(" and ma.createTime>=:startTime");
			map.put("startTime", startTime.trim() + " 00:00:00");
		}
		if(!StringUtil.isEmpty(endTime)){
			hql.append(" and ma.createTime<=:endTime");
			map.put("endTime", endTime.trim() + " 23:59:59");
		}
		if(!StringUtil.isEmpty(orderId)){
			hql.append(" and ma.orderId like :orderId");
			map.put("orderId", "%" + orderId.trim() + "%");
		}
		if(!StringUtil.isEmpty(minMoney)){
			hql.append(" and ma.amount>=:minMoney");
			map.put("minMoney", new BigDecimal(minMoney));
		}
		if(!StringUtil.isEmpty(maxMoney)){
			hql.append(" and ma.amount<=:maxMoney");
			map.put("maxMoney", new BigDecimal(maxMoney));
		}
		if(!StringUtil.isEmpty(contractNo)){
			hql.append(" and c.contractNo like :contractNo");
			map.put("contractNo", "%" + contractNo + "%");
		}
		hql.append(" order by ma.createTime desc,ma.amount desc");
		
		try {
			if(type == 2){
				return mortgageApplicationDao.list(pager, hql.toString(), map);
			}else{
				List<Object[]> list = mortgageApplicationDao.list(pager, hql.toString(), map);
				if(list != null && list.size() > 0){
					List<MortgageApplication> malist = new ArrayList<MortgageApplication>();
					for (Object[] obj : list) {
						MortgageApplication ma = (MortgageApplication)obj[0];
						if (type == 1) {
							ma.setOrder((Order) obj[1]);
						}else if (type == 3) {
							ma.setContract((Contract) obj[1]);
						}else if(type == 4){
							ma.setOrderWfp((OrderWfp)obj[1]);
						}
						malist.add(ma);
					}
					return malist;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public int saveMa(MortgageApplication ma) {
		// 1:成功，2:失败，3:订单号不能空，4:订单合同或抵押物文件不能为空
		if(ma == null){
			return 2;
		}
		
		try {
			if((ma.getType() == 1 || ma.getType() == 4) && StringUtil.isEmpty(ma.getOrderId())) {
				return 3;
			}
			if(ma.getType() == 1 && StringUtil.isEmpty(ma.getOrderPdfFile())){
				return 4;
			}
			if(ma.getType() == 2 && StringUtil.isEmpty(ma.getPawnFile())){
				return 4;
			}
			ma.setOrderId((ma.getType() == 1 || ma.getType() == 4) ? ma.getOrderId().trim() : "");
			ma.setCreateTime(DateUtil.getSysDateTimeString());
			
			this.mortgageApplicationDao.addBean(ma);
			
			return 1;
		} catch (Exception e) {
			log.error(e.getMessage());
			return 2;
		}
	}

	@Override
	public MortgageApplication getMaById(String id) {
		if(StringUtil.isEmpty(id)){
			return null;
		}
		
		try {
			MortgageApplication ma = (MortgageApplication)mortgageApplicationDao.get(id);
			if(ma.getType() == 3){
				ma.setContract((Contract)contractDao.get(ma.getContractId()));
			}
			return ma;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			
			return null;
		}
	}

	@Override
	public int updateMa(MortgageApplication ma) {
		// 1:成功，2:失败，3:订单号不能空，4:订单合同或抵押物文件不能为空
		if(ma == null){
			return 2;
		}
		
		try {
			if((ma.getType() == 1 || ma.getType() == 4) && StringUtil.isEmpty(ma.getOrderId())) {
				return 3;
			}
			if(ma.getType() == 1 && StringUtil.isEmpty(ma.getOrderPdfFile())){
				return 4;
			}
			if(ma.getType() == 2 && StringUtil.isEmpty(ma.getPawnFile())){
				return 4;
			}
			ma.setOrderId((ma.getType() == 1 || ma.getType() == 4) ? ma.getOrderId().trim() : "");
			mortgageApplicationDao.updateBean(ma);
			
			return 1;
		} catch (Exception e) {
			log.error(e.getMessage());
			return 2;
		}
	}

    @SuppressWarnings("unchecked")
	@Override
    public String getMASum(String storeId) {
    	if(StringUtil.isEmpty(storeId)){
    		return "0";
    	}
    	
    	// 定义存储查询条件的Map
        Map<String, Object> params = new HashMap<String, Object>();
    	// hql
        String hql = "select ma.id from MortgageApplication ma where ma.storeId =:storeId";      
        // setting data
        params.put("storeId", storeId);
        try {
            List<Object> list = mortgageApplicationDao.list(hql.toString(), params);
            if(list != null && list.size() > 0){
            	return list.size()+"";
            }
            return "0";
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return "0";
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<MortgageApplication> getList(String storeId) {
    	if(StringUtil.isEmpty(storeId)){
    		return null;
    	}
    	
    	// 定义存储查询条件的Map
        Map<String, Object> params = new HashMap<String, Object>();
        // hql
        String hql = "from MortgageApplication ma where ma.storeId =:storeId";      
        // setting
        params.put("storeId", storeId);
        
        Pager pager = new Pager();
        pager.setCurrentPage(1);
        pager.setRowsPerPage(6);
        
        try {
            return this.mortgageApplicationDao.list(pager, hql, params);
        } catch (Exception e) {
        	log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

	@SuppressWarnings("unchecked")
	@Override
	public List<MortgageApplication> getSysMaList(Pager pager, String type, int matype, String orderId, String storeName, String status) {
		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql 
		StringBuffer hql = null;
		if(matype == 1){
			hql = new StringBuffer("select ma,s.storeName,b.bankName,o from MortgageApplication ma,User u,Store s,Bank b,Order o where ma.storeId=u.id and ma.storeId=s.id and ma.bankId=b.id and ma.orderId=o.id and ma.type=:matype");
		} else if(matype == 2){
			hql = new StringBuffer("select ma,s.storeName,b.bankName from MortgageApplication ma,User u,Store s,Bank b where ma.storeId=u.id and ma.storeId=s.id and ma.bankId=b.id and ma.type=:matype");
		} else if(matype == 3){
			hql = new StringBuffer("select ma,s.storeName,b.bankName,c.contractNo from MortgageApplication ma,Store s,Bank b,Contract c,User u where u.id=ma.storeId and c.id=ma.contractId and ma.storeId=s.id and ma.bankId=b.id and ma.type=:matype");
		}else if(matype == 4){
			hql = new StringBuffer("select ma,s.storeName,b.bankName,ow from MortgageApplication ma,Store s,Bank b,OrderWfp ow where ma.storeId=s.id and ma.bankId=b.id and ma.type=:matype and ow.orderNumber=ma.orderId");
		}
		// setting data
		params.put("matype", matype);
		if(!StringUtil.isEmpty(type)){
			hql.append(" and u.type=:type");
			params.put("type", type);
		}
		if(!StringUtil.isEmpty(orderId)){
			hql.append(" and ma.orderId=:orderId");
			params.put("orderId", orderId);
		}
		if(!StringUtil.isEmpty(storeName)){
			hql.append(" and s.storeName like :storeName");
			params.put("storeName", "%" + storeName + "%");
		}
		if(!StringUtil.isEmpty(status)){
			hql.append(" and ma.status=:status");
			params.put("status", Integer.valueOf(status));
		}
		hql.append(" order by ma.createTime desc");
		
		try {
			List<Object[]> list = this.mortgageApplicationDao.list(pager, hql.toString(), params);
			if(list != null && list.size() > 0){
				List<MortgageApplication> malist = new ArrayList<MortgageApplication>();
				for (Object[] obj : list) {
					MortgageApplication ma = (MortgageApplication) obj[0];
					ma.setStoreName(obj[1].toString());
					ma.setBankName(obj[2].toString());
					ma.setOrder(matype == 1 ? (Order)obj[3] : null);
					ma.setContractId(matype == 3 ? obj[3].toString() : "");
					ma.setOrderWfp(matype == 4 ? (OrderWfp)obj[3] : null);
					malist.add(ma);
				}
				return malist;
			}
			
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}