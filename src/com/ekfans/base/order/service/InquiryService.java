package com.ekfans.base.order.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IInquiryDao;
import com.ekfans.base.order.dao.IInquiryDetailDao;
import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.order.model.InquiryDetail;
import com.ekfans.base.product.dao.ISupplyProductDao;
import com.ekfans.base.product.model.SupplyProduct;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 询价Service接口实现
 * 
 * @ClassName: InquiryService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
@SuppressWarnings("unchecked")
public class InquiryService implements IInquiryService {

	private Logger log = LoggerFactory.getLogger(InquiryService.class);
	@Resource
	private IInquiryDao inquiryDao;
	@Resource
	private IInquiryDetailDao inquiryDetailDao;
	@Resource
	private ISupplyProductDao supplyProductDao;
	
	@Autowired
	private IStoreService storeService;
	
	@Override
	public Boolean saveBuyerInq(Inquiry inq) {
		// 定义Session
		Session session = null;
		// 定义事物处理
		Transaction transaction = null;
		try {
			// 创建Session
			session = inquiryDao.createSession();
			// 开启事物处理
			transaction = session.beginTransaction();
			// 保存议价信息
			inquiryDao.addBean(inq);
			InquiryDetail inquiryDetail = new InquiryDetail();
			inquiryDetail.setBuyersId(inq.getBuyersId());
			inquiryDetail.setSellersId(inq.getSellersId());
			inquiryDetail.setBuyersNote(inq.getNote());
			inquiryDetail.setInquiryId(inq.getId());
			// 保存议价详情
			inquiryDetailDao.addBean(inquiryDetail);
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				// 保存失败,回滚事务
				transaction.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}
			log.error(e.getMessage());
		} finally {
			// 执行session关闭。
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return false;
	}

	/**
	 * 保存核心企业向供应商发起得议价信息
	 */
	@Override
	public Boolean saveInq(Inquiry inq, SupplyProduct product) {
		// 定义Session
		Session session = null;
		// 定义事物处理
		Transaction transaction = null;
		try {
			// 创建Session
			session = inquiryDao.createSession();
			// 开启事物处理
			transaction = session.beginTransaction();
			// 保存议价信息
			inquiryDao.addBean(inq);
			InquiryDetail inquiryDetail = new InquiryDetail();
			inquiryDetail.setBuyersId(inq.getBuyersId());
			inquiryDetail.setSellersId(product.getUserId());
			inquiryDetail.setBuyersNote(inq.getNote());
			inquiryDetail.setInquiryId(inq.getId());
			// 保存议价详情
			inquiryDetailDao.addBean(inquiryDetail);
			// 更改发布商品状态
			product.setStatus(1);
			supplyProductDao.updateBean(product);
		} catch (Exception e) {
			if (transaction != null) {
				// 保存失败,回滚事务
				transaction.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}
			log.error(e.getMessage());
		} finally {
			// 执行session关闭。
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return true;
	}

	/**
	 * 保存采购师向核心企业发起得议价信息
	 */
	@Override
	public Boolean saveSupplyInq(Inquiry inq) {
		// 定义Session
		Session session = null;
		// 定义事物处理
		Transaction transaction = null;
		try {
			// 创建Session
			session = inquiryDao.createSession();
			// 开启事物处理
			transaction = session.beginTransaction();
			// 保存议价信息
			inquiryDao.addBean(inq);
			InquiryDetail inquiryDetail = new InquiryDetail();
			inquiryDetail.setBuyersId(inq.getBuyersId());
			inquiryDetail.setSellersId(inq.getSellersId());
			inquiryDetail.setBuyersNote(inq.getNote());
			inquiryDetail.setInquiryId(inq.getId());
			// 保存议价详情
			inquiryDetailDao.addBean(inquiryDetail);
			// 更改发布商品状态(暂时)

		} catch (Exception e) {
			if (transaction != null) {
				// 保存失败,回滚事务
				transaction.rollback();
			}
			if (session != null && session.isOpen()) {
				session.close();
			}
			log.error(e.getMessage());
		} finally {
			// 执行session关闭。
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return true;
	}

	// 核心企业查询供应商议价列表
	@Override
	public List<Inquiry> getsupplyList(Pager pager, String proName, String userId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select i.id,p.supplyProductName,i.status,i.number,i.createTime,p.unit,i.price,i.FinalPrice,i.sellersNumber,i.sellersId,i.buyersId,i.endTime from Inquiry as i,SupplyProduct as p where 1 = 1 ");
		sql.append(" and p.id = i.productId");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(proName)) {
			sql.append(" and p.supplyProductName like :name");
			paramMap.put("name", "%" + proName + "%");
		}
		// 如果买方不为空，添加查询条件
		if (!StringUtil.isEmpty(userId)) {
			// 表示为核心企业查询采购商议价信息
			sql.append(" and i.buyersId = :buyersId");
			paramMap.put("buyersId", userId);
		}
		sql.append(" order by i.updateTime desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = inquiryDao.list(pager, sql.toString(), paramMap);
			List<Inquiry> inquirys = new ArrayList<Inquiry>();
			for (Object[] object : list) {
				Inquiry i = new Inquiry();
				i.setId((String) object[0]);
				i.setProductName((String) object[1]);
				i.setStatus((String) object[2]);
				i.setNumber((Integer) object[3]);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse((String) object[4]);
				i.setCreateTime(format.format(date));
				i.setUnit((String) object[5]);
				i.setPrice((BigDecimal) object[6]);
				i.setFinalPrice((BigDecimal) object[7]);
				i.setSellersNumber((Integer) object[8]);
				i.setSellersId((String)object[9]);
				i.setBuyersId((String)object[10]);
				i.setEndTime((String)object[11]);
				if (!StringUtil.isEmpty((String) object[11])) {
                    // 获取核价订单时间
                    Date endTime = format.parse((String) object[11]);
                    if ((new Date()).getTime() > endTime.getTime()) {
                        i.setCheckTime("true");
                    }
                }
				inquirys.add(i);
			}
			return inquirys;
		} catch (Exception e) {
		    log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Inquiry> getList(Pager pager, String proName, String minPrice, String maxPrice, String userId, int type) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select i.id,p.recommendPicture1,p.name,i.status,i.number,i.createTime,p.unit,i.price,i.FinalPrice,i.buyersId,i.sellersId,i.sellersNumber,i.endTime from Inquiry as i,Product as p where 1 = 1 ");
		sql.append(" and p.id = i.productId");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(proName)) {
			sql.append(" and p.name like :name");
			paramMap.put("name", "%" + proName + "%");
		}
		// 查询采购商的议价信息 buyersId买家、sellersId卖家
		if (type == 1) {
			// 查询供应商的议价
			sql.append(" and i.sellersId = :sellersId");
			paramMap.put("sellersId", userId);
		} else {
			// 表示为核心企业查询采购商议价信息
			sql.append(" and i.buyersId = :sellersId");
			paramMap.put("sellersId", userId);
		}
		// 价格区间的查找
		if (!StringUtil.isEmpty(minPrice) && !StringUtil.isEmpty(maxPrice)) {
			sql.append(" and i.price  BETWEEN :minUnitPrice AND :maxUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(minPrice));
			paramMap.put("maxUnitPrice", new BigDecimal(maxPrice));
		} else if (!StringUtil.isEmpty(minPrice) && StringUtil.isEmpty(maxPrice)) {
			sql.append(" and i.price > :minUnitPrice");
			paramMap.put("minUnitPrice", new BigDecimal(minPrice));
		} else if (StringUtil.isEmpty(minPrice) && !StringUtil.isEmpty(maxPrice)) {
			sql.append(" and i.price < :maxUnitPrice");
			paramMap.put("maxUnitPrice", new BigDecimal(maxPrice));
		}
		sql.append(" order by i.updateTime desc");
		List<Inquiry> inquirys = new ArrayList<Inquiry>();
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = inquiryDao.list(pager, sql.toString(), paramMap);
			for (Object[] object : list) {
				Inquiry i = new Inquiry();
				i.setId((String) object[0]);
				i.setPic((String) object[1]);
				i.setProductName((String) object[2]);
				i.setStatus((String) object[3]);
				i.setNumber((Integer) object[4]);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse((String) object[5]);
				i.setCreateTime(format.format(date));
				i.setUnit((String) object[6]);
				i.setPrice((BigDecimal) object[7]);
				i.setFinalPrice((BigDecimal) object[8]);
				i.setBuyersId((String) object[9]);
				i.setSellersId((String) object[10]);
				i.setSellersNumber((Integer) object[11]);
				i.setEndTime((String) object[12]);
				if (!StringUtil.isEmpty((String) object[12])) {
				    format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    String nowTime = format.format(new Date());
					// 获取核价订单时间 如果str1<str2则返回false
					if (!DateUtil.compareTimeString((String) object[12], nowTime)) {
						i.setCheckTime("true");
					}
				}
				inquirys.add(i);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return inquirys;
	}

	@Override
	public Inquiry getIdByInquiry(String id) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select i.id,p.recommendPicture3,p.name,i.status,p.unit,i.number,i.price,i.linkPerson,i.linkTel,id.buyersNote,i.sellersNumber,i.FinalPrice,p.id,i.endTime,i.buyersId,i.sellersId from Inquiry as i,Product as p,InquiryDetail as id where 1 = 1 ");
		sql.append(" and p.id = i.productId");
		sql.append(" and i.id = id.inquiryId");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(id)) {
			sql.append(" and i.id = :id");
			paramMap.put("id", id);
		} else {
			return null;
		}
		sql.append(" order by i.updateTime desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = inquiryDao.list(sql.toString(), paramMap);
			List<Inquiry> inquirys = new ArrayList<Inquiry>();
			for (Object[] object : list) {
				Inquiry i = new Inquiry();
				i.setId((String) object[0]);
				i.setPic((String) object[1]);
				i.setProductName((String) object[2]);
				i.setStatus((String) object[3]);
				i.setUnit((String) object[4]);
				i.setNumber((Integer) object[5]);
				i.setPrice((BigDecimal) object[6]);
				i.setLinkPerson((String) object[7]);
				i.setLinkTel((String) object[8]);
				i.setNote((String) object[9]);
				i.setSellersNumber((Integer) object[10]);
				i.setFinalPrice((BigDecimal) object[11]);
				i.setProductId((String) object[12]);
				i.setEndTime((String) object[13]);
				i.setBuyersId((String) object[14]);
				i.setSellersId((String) object[15]);
				inquirys.add(i);
			}
			if (inquirys != null && inquirys.size() > 0) {
				return inquirys.get(0);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	// 根据商品ID查询该议价
	public Inquiry getInquiryByProductId(String productId) {
		StringBuffer buffer = new StringBuffer();
		if (!StringUtil.isEmpty(productId)) {
			buffer.append("from Inquiry where productId='" + productId + "'");
			try {
				List<Inquiry> inqList = inquiryDao.list(buffer.toString(), null);
				if (inqList != null && inqList.size() > 0) {
					// 根据议价ID查询议价备注
					Inquiry in = inqList.get(0);
					StringBuffer buf = new StringBuffer();
					buf.append(" from InquiryDetail where inquiryId='" + in.getId() + "'");
					List<InquiryDetail> inqDetail = inquiryDetailDao.list(buf.toString(), null);
					if (inqDetail != null && inqDetail.size() > 0) {
						InquiryDetail inquiryD = inqDetail.get(0);
						in.setNote(inquiryD.getBuyersNote());
					}
					
					return in;
				}
			} catch (Exception e) {
			    log.error(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 根据ID查询议价信息
	 */
	@Override
	public Inquiry getById(String id) {
		try {
			Inquiry in = (Inquiry) inquiryDao.get(id);
			StringBuffer buffer = new StringBuffer("from InquiryDetail where inquiryId ='" + in.getId() + "'");
			List<InquiryDetail> inqd = inquiryDetailDao.list(null, buffer.toString(), null);
			if (inqd != null && inqd.size() > 0) {
				InquiryDetail inq = inqd.get(0);
				in.setNote(inq.getBuyersNote());
			}
			StringBuffer productBuffer = new StringBuffer("from SupplyProduct where id='" + in.getProductId() + "'");
			List<SupplyProduct> sp = supplyProductDao.list(productBuffer.toString(), null);
			if (sp != null && sp.size() > 0) {
				SupplyProduct s = sp.get(0);
				in.setProductName(s.getSupplyProductName());
				in.setUnit(s.getUnit());
			}
			return in;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Title: getId
	 * @Description: TODO(根据ID获取议价信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @return Inquiry 返回类型
	 * @throws
	 */
	public Inquiry getId(String id) {
		try {
			return (Inquiry) inquiryDao.get(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateInquiry(Inquiry i) {
		try {
			inquiryDao.updateBean(i);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();

			return false;
		}
	}
	
	@Override
    public boolean updateInquiry(Inquiry i,Session session) {
        try {
            inquiryDao.updateBean(i,session);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

	/**
	 * @Title: getSupplyList
	 * @Description: TODO 查询供应商议价信息 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param name
	 * @return Inquiry 返回类型
	 * @throws
	 */
	@Override
	public List<Inquiry> getSupplyList(Pager pager, String status, String name, String userid) {
		try {
			StringBuffer inq_buffer = new StringBuffer("select i.id,p.supplyProductName,i.status,i.number,i.createTime,i.price,p.unit,i.FinalPrice,i.sellersNumber,i.productId,i.price,i.endTime,i.sellersId from Inquiry i,SupplyProduct p where p.id = i.productId and i.sellersId='" + userid + "'");
			if (!StringUtil.isEmpty(name)) {
				inq_buffer.append(" and p.supplyProductName like '%" + name + "%'");
			}
			if (!StringUtil.isEmpty(status)) {
				if (status.equals("未核价")) {
					status = "0";
				} else {
					status = "1";
				}
				inq_buffer.append(" and i.status ='" + status + "'");
			}
			inq_buffer.append(" order by i.updateTime desc");
			List<Object[]> in = inquiryDao.list(pager, inq_buffer.toString(), null);
			List<Inquiry> inqList = new ArrayList<Inquiry>();
			if (in != null && in.size() > 0) {
				for (Object[] obj : in) {
					Inquiry inq = new Inquiry();
					inq.setId((String) obj[0]);
					inq.setProductName((String) obj[1]);
					inq.setStatus((String) obj[2]);
					inq.setNumber(Integer.parseInt(obj[3].toString()));
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date date = format.parse((String) obj[4]);
					inq.setCreateTime(format.format(date));
					inq.setPrice((BigDecimal) obj[5]);
					inq.setUnit((String) obj[6]);
					inq.setFinalPrice((BigDecimal) obj[7]);
					if (obj[8] != null) {
						inq.setSellersNumber(Integer.parseInt(obj[8].toString()));
					}
					inq.setProductId((String) obj[9]);
					inq.setPrice((BigDecimal)obj[10]);
					inq.setEndTime((String)obj[11]);
					inq.setSellersId((String)obj[12]);
					if (!StringUtil.isEmpty((String) obj[11])) {
	                    // 获取核价订单时间
	                    Date endTime = format.parse((String) obj[11]);
	                    if ((new Date()).getTime() > endTime.getTime()) {
	                        inq.setCheckTime("true");
	                    }
	                }
					StringBuffer buffer = new StringBuffer("from InquiryDetail where inquiryId ='" + inq.getId() + "'");
					List<InquiryDetail> inqd = inquiryDetailDao.list(null, buffer.toString(), null);
					if (inqd != null && inqd.size() > 0) {
						InquiryDetail inqde = inqd.get(0);
						inq.setNote(inqde.getBuyersNote());
					}
					StringBuffer productBuffer = new StringBuffer("from SupplyProduct where id='" + inq.getProductId() + "'");
					List<SupplyProduct> sp = supplyProductDao.list(productBuffer.toString(), null);
					if (sp != null && sp.size() > 0) {
						SupplyProduct s = sp.get(0);
						inq.setProductName(s.getSupplyProductName());
						inq.setUnit(s.getUnit());
					}
					inqList.add(inq);
				}
			}
			return inqList;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Inquiry> getSystemInquiryList(Pager pager, String status, String name, String userId, String beginDate, String endDate) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select i.id,p.name,i.status,i.number,u.name,i.createTime,p.unit,i.price,i.FinalPrice from Inquiry as i,Product as p,User as u where 1 = 1 ");
		sql.append(" and p.id = i.productId");
		// sql.append(" and i.sellersId = u.id");
		sql.append(" and i.buyersId = u.id");
		sql.append(" and u.type = 2");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and p.name like :name");
			paramMap.put("name", "%" + name + "%");
		}
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and i.status = :status");
			paramMap.put("status", status);
		}
		// 如果买方不为空，添加查询条件
		if (!StringUtil.isEmpty(userId)) {
			// 表示为核心企业查询采购商议价信息
			sql.append(" and u.name = :userId");
			paramMap.put("userId", userId);
		}
		if (!StringUtil.isEmpty(beginDate)) {
		    sql.append(" and i.createTime>=:beginDate");
            paramMap.put("beginDate", beginDate);
        }
        if (!StringUtil.isEmpty(endDate)) {
            sql.append(" and i.createTime<=:endDate");
            paramMap.put("endDate", endDate);
        }
		sql.append(" order by i.updateTime desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = inquiryDao.list(pager, sql.toString(), paramMap);
			List<Inquiry> inquirys = new ArrayList<Inquiry>();
			for (Object[] object : list) {
				Inquiry i = new Inquiry();
				i.setId((String) object[0]);
				i.setProductName((String) object[1]);
				i.setStatus((String) object[2]);
				i.setNumber((Integer) object[3]);
				i.setBuyersId((String) object[4]);
				i.setCreateTime((String) object[5]);
				i.setUnit((String) object[6]);
				i.setPrice((BigDecimal) object[7]);
				i.setFinalPrice((BigDecimal) object[8]);
				inquirys.add(i);
			}
			return inquirys;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Inquiry> getSystemGyInquiryList(Pager pager, String status, String name, String userId, String beginDate, String endDate) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select i.id,sp.supplyProductName,i.status,i.number,u.name,i.createTime,sp.unit,i.price,i.FinalPrice from Inquiry as i,SupplyProduct as sp,User as u where 1 = 1 ");
		sql.append(" and sp.id = i.productId");
		// sql.append(" and i.sellersId = u.id");
		sql.append(" and i.buyersId = u.id");
		sql.append(" and u.type = 3");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 当品牌的id和product品跑对应
		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and sp.supplyProductName like :name");
			paramMap.put("name", "%" + name + "%");
		}
		if (!StringUtil.isEmpty(status)) {
			sql.append(" and i.status = :status");
			paramMap.put("status", status);
		}
		// 如果买方不为空，添加查询条件
		if (!StringUtil.isEmpty(userId)) {
			// 表示为核心企业查询采购商议价信息
			sql.append(" and u.name = :userId");
			paramMap.put("userId", userId);
		}
		if (!StringUtil.isEmpty(beginDate)) {
			sql.append(" and i.createTime>=:beginDate");
			paramMap.put("beginDate", beginDate);
		}
		if (!StringUtil.isEmpty(endDate)) {
			sql.append(" and i.createTime<=:endDate");
			paramMap.put("endDate", endDate);
		}
		sql.append(" order by i.updateTime desc");
		try {
			// 调用DAO方法查找商品
			List<Object[]> list = inquiryDao.list(pager, sql.toString(), paramMap);
			List<Inquiry> inquirys = new ArrayList<Inquiry>();
			for (Object[] object : list) {
				Inquiry i = new Inquiry();
				i.setId((String) object[0]);
				i.setProductName((String) object[1]);
				i.setStatus((String) object[2]);
				i.setNumber((Integer) object[3]);
				i.setBuyersId((String) object[4]);
				i.setCreateTime((String) object[5]);
				i.setUnit((String) object[6]);
				i.setPrice((BigDecimal) object[7]);
				i.setFinalPrice((BigDecimal) object[8]);
				inquirys.add(i);
			}
			return inquirys;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
	/**
	* @Title: closeSupply
	* @Description: TODO(采购商关闭议价单)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @return boolean 返回类型
	* @throws
	 */
	public  boolean closeSupply(String id){
	    try {
            Inquiry in = (Inquiry) inquiryDao.get(id);
            //设置为关闭
            in.setStatus("3");
            inquiryDao.updateBean(in);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	    return true;
	}

	@Override
	public List<Inquiry> getInquiryList(Pager pager, String userId, String status, String supplyType, String productType, String sourceType, String linkPerson,String storeName) {
		// 定义查询SQL
		StringBuffer sql=new StringBuffer("");
		Map<String,Object> params = new HashMap<String,Object>();
		if(!StringUtil.isEmpty(sourceType) && "1".equals(sourceType)){//供应询价
				sql.append("select i, sb.title from Inquiry as i, SupplyBuy as sb, User as user where 1 = 1 ");
				sql.append(" and i.productId = sb.id");
				
				
				if(!StringUtil.isEmpty(supplyType)){
					sql.append(" and sb.type = :supplyType");
					params.put("supplyType", supplyType);
				}
				if(!StringUtil.isEmpty(productType)){
					sql.append(" and sb.productType = :productType");
					params.put("productType", productType);
				}
				
				
		}else if(!StringUtil.isEmpty(sourceType) && "2".equals(sourceType)){//货源询价
			sql.append("select i,wft.name from Inquiry as i, Wftransport as wft, User as user where 1 = 1 ");
			if(!StringUtil.isEmpty(userId)){
				sql.append(" and i.productId = wft.id");
				sql.append(" and wft.storeId = user.id");
			}
		}
		if(!StringUtil.isEmpty(userId)){
			sql.append(" and (i.buyersId=:userId or i.sellersId=:userId)");
			params.put("userId", userId);
		}
		if(!StringUtil.isEmpty(status)){
			sql.append(" and i.status = :status");
			params.put("status", status);
		}
		if(!StringUtil.isEmpty(linkPerson)){
			sql.append(" and i.linkPerson like :linkPerson");
			params.put("linkPerson", "%"+linkPerson+"%");
		}
		if(!StringUtil.isEmpty(storeName)){
			sql.append(" and i.storeName like :storeName");
			params.put("storeName", "%"+storeName+"%");
		}
		sql.append(" group by i.id order by i.createTime desc");
		try {
			// 调用DAO方法查找询价列表
			List<Object[]> list = inquiryDao.list(pager, sql.toString(), params);
			List<Inquiry> inquirys = new ArrayList<Inquiry>();
			if(list!=null&&list.size()>0){
				for (Object[] object : list) {
					Inquiry i = new Inquiry();
					i = (Inquiry) object[0];
					String tilte = (String) object[1];
//					String unit = (String) object[2];
//					i.setUnit(unit);
					i.setTitle(tilte);
					inquirys.add(i);
				}
			}
			return inquirys;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Inquiry> getSystemInquiryList(Pager pager, String storeName, String status, String supplyType, String productType, String sourceType, String linkPerson) {
		// 定义查询SQL
		StringBuffer sql=new StringBuffer("");
		Map<String,Object> params = new HashMap<String,Object>();
		if(!StringUtil.isEmpty(sourceType) && "1".equals(sourceType)){//供应询价
				sql.append("select i, sb.title from Inquiry as i, SupplyBuy as sb where 1 = 1 ");
				sql.append(" and i.productId = sb.id");
				if(!StringUtil.isEmpty(supplyType)){
					sql.append(" and sb.type = :supplyType");
					params.put("supplyType", supplyType);
				}
				if(!StringUtil.isEmpty(productType)){
					sql.append(" and sb.productType = :productType");
					params.put("productType", productType);
				}
				
				
		}else if(!StringUtil.isEmpty(sourceType) && "2".equals(sourceType)){//货源询价
			sql.append("select i,wft.name from Inquiry as i, Wftransport as wft where 1 = 1 ");
			sql.append(" and i.productId = wft.id");
		}
		if(!StringUtil.isEmpty(status)){
			sql.append(" and i.status = :status");
			params.put("status", status);
		}
		if(!StringUtil.isEmpty(linkPerson)){
			sql.append(" and i.linkPerson like :linkPerson");
			params.put("linkPerson", "%"+linkPerson+"%");
		}
		if(!StringUtil.isEmpty(storeName)){
			sql.append(" and i.storeName like :storeName");
			params.put("storeName", "%"+storeName+"%");
		}
		sql.append(" group by i.id order by i.createTime desc");
		try {
			// 调用DAO方法查找询价列表
			List<Object[]> list = inquiryDao.list(pager, sql.toString(), params);
			List<Inquiry> inquirys = new ArrayList<Inquiry>();
			if(list!=null&&list.size()>0){
				for (Object[] object : list) {
					Inquiry i = new Inquiry();
					i = (Inquiry) object[0];
					String tilte = (String) object[1];
//					String unit = (String) object[2];
//					i.setUnit(unit);
					i.setTitle(tilte);
					inquirys.add(i);
				}
			}
			return inquirys;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
