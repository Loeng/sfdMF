package com.ekfans.base.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IBargainDao;
import com.ekfans.base.order.model.Bargain;
import com.ekfans.plugin.number.NoManager;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * @ClassName BargainService
 * @Description TODO
 * @author ekfans
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company 成都易科远见科技有限公司 www.ekfans.com
 * @date 2016年3月16日
 */
@Service
@SuppressWarnings("unchecked")
public class BargainService implements IBargainService{
	private Logger log = LoggerFactory.getLogger(BargainService.class);
	
	@Autowired
	private IBargainDao bargainDao;

	@Override
	public Bargain getById(String id) {
		try{
			Bargain bargain = (Bargain) bargainDao.get(id);
			return bargain;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Bargain> getBargainList(Pager pager, String productName,
			String contactMan, String contactPhone, String userId, String type) {
		// 参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql语句
		StringBuffer hql = new StringBuffer("select b,p.name,p.unitPrice,p.smallPicture, buy.storeName, sale.storeName, p.productModel "
				+ "from Bargain as b,Product as p,Store buy,Store sale "
				+ "where p.id = b.sourceId and b.buyerId = buy.id and b.sellerId = sale.id");
		if(!StringUtil.isEmpty(userId)){
			// 议价类型
			if(("1").equals(type) ){ // 收到的议价 
				hql.append(" and  b.sellerId = :userId  ");
				params.put("userId", userId);
			}else{	// 发布的议价（默认）
				hql.append(" and  b.buyerId = :userId ");
				params.put("userId", userId);
			}
		}
		// 搜索条件
		if (!StringUtil.isEmpty(productName)) {
			hql.append(" and p.name like :productName");
			params.put("productName", "%" +productName+ "%");
		}
		if (!StringUtil.isEmpty(contactMan)) {
			hql.append(" and b.contactMan like :contactMan");
			params.put("contactMan", "%" +contactMan+ "%");
		}
		if (!StringUtil.isEmpty(contactPhone)) {
			hql.append(" and b.contactPhone like :contactPhone");
			params.put("contactPhone", "%" +contactPhone+ "%");
		}
		// 排序
		hql.append(" order by b.createTime desc");
		
		try{
			List<Bargain> bargains=null;
			Bargain bargain=null;
			// 查询列表
			List<Object[]> objs = bargainDao.list(pager, hql.toString(), params);
			if(null!=objs&&objs.size()>0){
				bargains=new ArrayList<>();
				for (Object [] obj:objs) {
					bargain=(Bargain) obj[0];
					bargain.setProductName((String) obj[1]);
					bargain.setUnitPrice((BigDecimal) obj[2]);
					bargain.setSmallPicture((String) obj[3]);
					bargain.setBuyerName((String) obj[4]);
					bargain.setSellerName((String) obj[5]);
					bargain.setProductModel((String) obj[6]);
					bargains.add(bargain);
				}
			}
			return bargains;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Bargain getDetailById(String id) {
		// TODO Auto-generated method stub
		// 参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		StringBuffer hql = new StringBuffer(" select b,p.name,p.unitPrice,p.smallPicture, buy.storeName, sale.storeName, p.productModel, p.unit "
				+ "from Bargain as b,Product as p,Store buy,Store sale "
				+ "where p.id = b.sourceId and b.buyerId = buy.id and b.sellerId = sale.id and b.id = :id");
		params.put("id", id);
		try{
			Bargain ba = null;
			List<Object[]> objs = bargainDao.list(hql.toString(), params);
			Object[] obj = objs.get(0);
			ba = (Bargain) obj[0];
			ba.setProductName((String) obj[1]);
			ba.setUnitPrice((BigDecimal) obj[2]);
			ba.setSmallPicture((String) obj[3]);
			ba.setBuyerName((String) obj[4]);
			ba.setSellerName((String) obj[5]);
			ba.setProductModel((String) obj[6]);
			ba.setUnit((String) obj[7]);
			return ba;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}

	
	@Override
	public Bargain getWfDetailById(String id) {
		// TODO Auto-generated method stub
		// 参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		StringBuffer hql = new StringBuffer(" select b,w.name,w.startFullPath,w.endFullPath, buy.storeName, sale.storeName,w.wfpTotal "
				+ "from Bargain as b,Wftransport as w,Store buy,Store sale "
				+ "where w.id = b.sourceId and b.buyerId = buy.id and b.sellerId = sale.id and b.id = :id");
		params.put("id", id);
		try{
			Bargain ba = null;
			List<Object[]> objs = bargainDao.list(hql.toString(), params);
			Object[] obj = objs.get(0);
			ba = (Bargain) obj[0];
			ba.setProductName((String) obj[1]);
			ba.setStart(obj[2].toString());
			ba.setEnd((String) obj[3]);
			ba.setBuyerName((String) obj[4]);
			ba.setSellerName((String) obj[5]);
			ba.setWfpTotal((BigDecimal) obj[6]);
			return ba;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}
	@Override
	public Bargain getWfDetailByIdFromYouKe(String id) {
		// TODO Auto-generated method stub
		// 参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		StringBuffer hql = new StringBuffer(" select b,w.name,w.startFullPath,w.endFullPath, buy.storeName, w.wfpTotal "
				+ "from Bargain as b,Wftransport as w,Store buy "
				+ "where w.id = b.sourceId and b.buyerId = buy.id  and b.id = :id");
		params.put("id", id);
		try{
			Bargain ba = null;
			List<Object[]> objs = bargainDao.list(hql.toString(), params);
			Object[] obj = objs.get(0);
			ba = (Bargain) obj[0];
			ba.setProductName((String) obj[1]);
			ba.setStart(obj[2].toString());
			ba.setEnd((String) obj[3]);
			ba.setBuyerName((String) obj[4]);
			ba.setWfpTotal((BigDecimal) obj[5]);
			return ba;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}
	
	@Override
	public boolean updateBargain(Bargain b){
		try{
			bargainDao.updateBean(b);
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return false;
	}
	
	@Override
	public boolean saveAuditBargain(Bargain b) {
		try{
			//修改议价状态
			b.setStatus("1");
			bargainDao.updateBean(b);
			return true;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean deleteById(String id) {
		// TODO Auto-generated method stub
		try {
			bargainDao.deleteById(id);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public boolean addBargain(Bargain b) {
		// TODO Auto-generated method stub
		try {
			b.setId(new NoManager().createBargainId());
			b.setOrderStatus("0");
			bargainDao.addBean(b);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Bargain> getBargainTransList(Pager pager, String carName,
			String contactMan, String contactPhone, String userId, String type, String userType) {
		// 参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql语句
		
		StringBuffer hql = null;
		if(!StringUtil.isEmpty(userId)){
			// 议价类型
			if(("1").equals(type) ){ // 收到的议价 
				hql = new StringBuffer("select b,w.name, buy.storeName, sale.storeName "
						+ "from Bargain as b,Wftransport as w,Store buy,Store sale "
						+ "where w.id = b.sourceId and b.buyerId = buy.id and b.sellerId = sale.id");
					hql.append(" and  b.sellerId = :userId  ");
					params.put("userId", userId);
				
			}else{	// 发布的议价（默认）
				if(("0").equals(userType)||StringUtil.isEmpty(userType)){
					hql = new StringBuffer("select b,w.name, buy.storeName, sale.storeName "
							+ "from Bargain as b,Wftransport as w,Store buy,Store sale "
							+ "where w.id = b.sourceId and b.buyerId = buy.id and b.sellerId = sale.id");
					hql.append(" and  b.buyerId = :userId ");
					params.put("userId", userId);
				}else{
					hql = new StringBuffer("select b,w.name, buy.storeName "
							+ "from Bargain as b,Wftransport as w,Store buy "
							+ "where w.id = b.sourceId and b.buyerId = buy.id and (b.sellerId = '' or b.sellerId is null)");
					hql.append(" and  b.buyerId = :userId ");
					params.put("userId", userId);
					
				}
			}
		}
		
		// 搜索条件
		if (!StringUtil.isEmpty(carName)) {
			hql.append(" and w.name like :carName");
			params.put("carName", "%" +carName+ "%");
		}
		if (!StringUtil.isEmpty(contactMan)) {
			hql.append(" and b.contactMan like :contactMan");
			params.put("contactMan", "%" +contactMan+ "%");
		}
		if (!StringUtil.isEmpty(contactPhone)) {
			hql.append(" and b.contactPhone like :contactPhone");
			params.put("contactPhone", "%" +contactPhone+ "%");
		}
		// 排序
		hql.append(" order by b.createTime desc");
		
		try{
			List<Bargain> bargains=null;
			Bargain bargain=null;
			// 查询列表
			List<Object[]> objs = bargainDao.list(pager, hql.toString(), params);
			if(null!=objs&&objs.size()>0){
				bargains=new ArrayList<>();
				for (Object [] obj:objs) {
					bargain=(Bargain) obj[0];
					bargain.setProductName((String) obj[1]);
					bargain.setBuyerName(obj[2].toString());
					if("1".equals(type)){ //收到的议价
						bargain.setSellerName((String) obj[3]);
					}else{// 发布的议价
						if("0".equals(userType)||StringUtil.isEmpty(userType)){ //议价对象为企业
							bargain.setSellerName((String) obj[3]);
						}else{
							bargain.setSellerName("游客"); //议价对象为游客
						}
					}
					bargains.add(bargain);
				}
			}
			return bargains;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}
	@Override
	public List<Bargain> getSysBargainTransList(Pager pager, String carName,  String fbType) {
		// 参数map
		Map<String, Object> params = new HashMap<String, Object>();
		// hql语句
		
		StringBuffer hql = null;
		if(  StringUtil.isEmpty(fbType) || ("0").equals(fbType)){
			hql = new StringBuffer("select b,w.name, buy.storeName, sale.storeName "
					+ "from Bargain as b,Wftransport as w,Store buy,Store sale "
					+ "where w.id = b.sourceId and b.buyerId = buy.id and b.sellerId = sale.id ");
		}else{
			hql = new StringBuffer("select b,w.name, buy.storeName "
					+ "from Bargain as b,Wftransport as w,Store buy "
					+ "where w.id = b.sourceId and b.buyerId = buy.id and (b.sellerId = '' or b.sellerId is null)");
		}
		// 搜索条件
		if (!StringUtil.isEmpty(carName)) {
			hql.append(" and w.name like :carName");
			params.put("carName", "%" +carName+ "%");
		}
		// 排序
		hql.append(" order by b.createTime desc");
		
		try{
			List<Bargain> bargains=null;
			Bargain bargain=null;
			// 查询列表
			List<Object[]> objs = bargainDao.list(pager, hql.toString(), params);
			if(null!=objs&&objs.size()>0){
				bargains=new ArrayList<>();
				for (Object [] obj:objs) {
					bargain=(Bargain) obj[0];
					bargain.setProductName((String) obj[1]);
					bargain.setBuyerName(obj[2].toString());
					if("0".equals(fbType)||StringUtil.isEmpty(fbType)){ //议价对象为企业
						bargain.setSellerName((String) obj[3]);
					}else{
						bargain.setSellerName("游客"); //议价对象为游客
					}
					bargains.add(bargain);
				}
			}
			return bargains;
		}catch(Exception e){
			log.error(e.getMessage());
		}
		return null;
	}

}
