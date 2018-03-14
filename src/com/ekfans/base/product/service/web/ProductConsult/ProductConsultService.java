package com.ekfans.base.product.service.web.ProductConsult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.store.dao.IStoreConsultDao;
import com.ekfans.base.store.model.Consult;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
public class ProductConsultService implements IProductConsultService {

	private Logger log = LoggerFactory.getLogger(ProductConsultService.class);
	@Autowired
	private IStoreConsultDao consultDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Consult> getProConsultByCondition(String proId, String consultContent, String type, Pager pager) {
		if (StringUtil.isEmpty(proId)) {
			return null;
		}
		// param data
		Map<String, Object> params = new HashMap<String, Object>();
		// hql
		StringBuffer hql = new StringBuffer("from Consult c where c.productId=:proId");
		hql.append(" and c.status='1' and c.checkStatus=1 and c.consultType='0'");
		// setting data
		params.put("proId", proId);
		if (!StringUtil.isEmpty(consultContent)) {
			hql.append(" and c.questionAnswer like :consultContent");
			params.put("consultContent", "%" + consultContent + "%");
		}
		// 咨询的类型
		if (StringUtil.isEmpty(type)) {
			type = "0";// 如果没有咨询,则默认查询出商品咨询
		}
		hql.append(" and c.type=:type");
		params.put("type", type);
		// 添加排序
		hql.append(" order by c.createTime DESC");
		
		try {
			if (pager == null) {
				pager = new Pager();
				pager.setCurrentPage(1);
				pager.setRowsPerPage(6);
			}
			List<Consult> consults = consultDao.list(pager, hql.toString(), params);
			if (consults != null && consults.size() > 0) {
				for (int i = 0; i < consults.size(); i++) {
					List<Consult> cs = ProductConsultService.getReplyByConsultId(consults.get(i).getId(), consultDao, log);
					String userName = ProductConsultService.getUserNameById(consults.get(i).getCreator(), consultDao, log);
					consults.get(i).setChildList(cs);
					consults.get(i).setCreatorName(userName);
				}
				return consults;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
		
		/*
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			// 查询出
			StringBuffer hql = new StringBuffer("select c from Consult as c where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			// 先决条件
			hql.append(" and c.productId = :proId");
			// 咨询记录的基本条件
			hql.append(" and c.status = '1' and c.checkStatus = 1 and c.replyStatus = true and c.consultType = '0'");
			// 搜索条 对于商品咨询这块才有搜索功能
			params.put("proId", proId);
			if (!StringUtil.isEmpty(consultContent)) {
				hql.append(" and c.questionAnswer like :consultContent");
				params.put("consultContent", "%" + consultContent + "%");
			}
			// 咨询的类型
			if (StringUtil.isEmpty(type)) {
				// 如果没有咨询,则默认查询出商品咨询
				type = "0";
			}
			hql.append(" and c.type = :type");
			params.put("type", type);
			// 添加排序
			hql.append(" order by c.createTime DESC");

			// 对于是否有分页的处理,没有分页则默认显示最新的六条数据
			if (pager == null) {
				pager = new Pager();
				pager.setCurrentPage(1);
				pager.setRowsPerPage(6);
			}
			List<Consult> consults = consultDao.list(pager, hql.toString(), params);
			if (consults != null && consults.size() > 0) {
				for (int i = 0; i < consults.size(); i++) {
					List<Consult> cs = ProductConsultService.getReplyByConsultId(consults.get(i).getId(), consultDao, log);
					String userName = ProductConsultService.getUserNameById(consults.get(i).getCreator(), consultDao, log);
					consults.get(i).setChildList(cs);
					consults.get(i).setCreatorName(userName);
				}
				return consults;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
		*/
	}

	/**
	 * 
	 * @Title: getReplyByConsultId
	 * @Description: TODO(根据consult查询对应问题的回复) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param consultId
	 * @param @return 设定文件
	 * @return List<Consult> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private static List<Consult> getReplyByConsultId(String consultId, IStoreConsultDao mydao, Logger mylog) {
		try {
			if (StringUtil.isEmpty(consultId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer(
					"select c from Consult as c where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and c.parentId = :consultId");
			params.put("consultId", consultId);
			List<Consult> cs = mydao.list(hql.toString(), params);
			return cs;
		} catch (Exception e) {
			mylog.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @Title: getUserNameById
	 * @Description: TODO(获取用户名) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param userId
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private static String getUserNameById(String userId, IStoreConsultDao mydao, Logger mylog) {
		try {
			if (StringUtil.isEmpty(userId)) {
				return "";
			}
			StringBuffer hql = new StringBuffer(
					"select u.name,u.nickName from User as u where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and u.id = :userId");
			params.put("userId", userId);
			List<Object[]> userNames = mydao.list(hql.toString(), params);
			if (userNames != null) {
				for(Object[] object:userNames){
				    if(!StringUtil.isEmpty((String)object[1])){
				        return (String)object[1];
				    }else{
				        return (String)object[0];
				    }
				}
			}
		} catch (Exception e) {
			mylog.error(e.getMessage());
		}
		return "";
	}

	/**
	 * 新增一条咨询
	 */

	@Override
	public boolean addProductConsult(String consultType, String consultContent, String proId, String userId) {
		try {
			if (StringUtil.isEmpty(consultType) || StringUtil.isEmpty(consultContent) || StringUtil.isEmpty(proId) || StringUtil.isEmpty(userId)) {
				return false;
			}
			// 获得关联的条件
			Object[] conditions = ProductConsultService.getConditionByProId(
					proId, consultDao, log);
			String storeId = (String) conditions[0];
			String storeName = (String) conditions[1];
			String proName = (String) conditions[2];
			// 封装一个Consult对象
			Consult consult = new Consult();
			consult.setType(consultType);
			consult.setQuestionAnswer(consultContent);
			consult.setProductId(proId);
			consult.setStoreId(storeId);
			consult.setCreator(userId);
			consult.setCreateTime(DateUtil.getSysDateTimeString());
			consult.setStatus("1");
			consult.setCheckStatus(0);
			consult.setReplyStatus(false);
			consult.setConsultType("0");
			consult.setStoreName(storeName);
			consult.setProductName(proName);

			consultDao.addBean(consult);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 
	 * @Title: getConditionByProId
	 * @Description: TODO(新增咨询时获得相应的关联条件) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param proId
	 * @param @return 设定文件
	 * @return Object[] 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	private static Object[] getConditionByProId(String proId, IStoreConsultDao mydao, Logger mylog) {
		try {
			if (StringUtil.isEmpty(proId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer(
					"select s.id,s.storeName,p.name from Product as p,Store as s where 1=1");
			hql.append(" and p.storeId = s.id");
			Map<String, Object> params = new HashMap<String, Object>();
			hql.append(" and p.id = :proId");
			params.put("proId", proId);
			List<Object[]> conditions = mydao.list(hql.toString(), params);
			if (conditions != null && conditions.size() > 0) {
				return conditions.get(0);
			}
		} catch (Exception e) {
			mylog.error(e.getMessage());
		}
		return null;
	}
}
