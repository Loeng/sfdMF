package com.ekfans.base.metal.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ekfans.controllers.ccwccApp.AppPreciousBean;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.metal.dao.IPreciousMetalDao;
import com.ekfans.base.metal.dao.IPreciousMetalDetailDao;
import com.ekfans.base.metal.model.PreciousMetal;
import com.ekfans.base.metal.model.PreciousMetalDetail;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 品目Service接口实现
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
public class PreciousMetalService implements IPreciousMetalService {

	private Logger log = LoggerFactory.getLogger(PreciousMetalService.class);

	@Autowired
	private IPreciousMetalDao metalDao;
	@Autowired
	private IPreciousMetalDetailDao detailDao;

	@Override
	public PreciousMetal getMetalById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			return (PreciousMetal) metalDao.get(id);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<PreciousMetal> getMetalsByCategoryId(String categoryId) {
		StringBuffer sql = new StringBuffer(" from PreciousMetal as metal where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(categoryId)) {
			sql.append(" and metal.categoryId = :categoryId");
			paramMap.put("categoryId", categoryId);
		}
		sql.append(" order by metal.createTime asc");

		try {
			return metalDao.list(sql.toString(), paramMap);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean saveOrUpdate(PreciousMetal metal) {
		if (metal == null) {
			return false;
		}
		if (StringUtil.isEmpty(metal.getCreateTime())) {
			metal.setCreateTime(DateUtil.getSysDateTimeString());
		}

		try {
			if (StringUtil.isEmpty(metal.getId())) {
				metalDao.addBean(metal);
			} else {
				metalDao.updateBean(metal);
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return false;
	}

	@Override
	public boolean deleteById(String id) {
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		try {
			metalDao.deleteById(id);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	public boolean deleteByIds(String[] ids) {
		if (ids == null || ids.length <= 0) {
			return false;
		}
		try {
			metalDao.deleteByIds(ids);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<PreciousMetal> getNewPreciousMetalByCategory(String categoryId, Pager pager) {
		// List<PreciousMetal> metals = getMetalsByCategoryId(categoryId);
		// if (metals == null || metals.size() <= 0) {
		// return null;
		// }
		String hql = "select d.dateTime from PreciousMetalDetail as d,PreciousMetal as metal where metal.id = d.metalId and metal.categoryId = '" + categoryId
				+ "' order by d.dateTime desc";

		String dateTime = DateUtil.getSysDateString();
		try {
			Pager newPage = new Pager(1);
			newPage.setRowsPerPage(1);
			List<Object> details = detailDao.list(newPage, hql, null);
			if (details != null && details.size() > 0) {
				dateTime = details.get(0).toString();
			}
		} catch (Exception e) {
		}

		StringBuffer sql = new StringBuffer("select metal,detail from PreciousMetalDetail as detail,PreciousMetal as metal where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" and metal.categoryId = :categoryId");
		paramMap.put("categoryId", categoryId);
		sql.append(" and metal.id = detail.metalId");

		sql.append(" and detail.dateTime = :dateTime");
		paramMap.put("dateTime", dateTime);

		sql.append(" order by metal.createTime asc");
		List<PreciousMetal> rList = new LinkedList<PreciousMetal>();
		try {
			List<Object[]> objList = null;
			if (pager != null) {
				objList = detailDao.list(pager, sql.toString(), paramMap);
			} else {
				objList = detailDao.list(sql.toString(), paramMap);
			}
			if (objList == null || objList.size() <= 0) {
				return rList;
			}

			// Map<String, PreciousMetalDetail> rMap = new HashMap<String,
			// PreciousMetalDetail>();
			// for (PreciousMetalDetail detail : details) {
			// if (detail == null) {
			// continue;
			// }
			// rMap.put(detail.getMetalId(), detail);
			// }
			//
			// for (PreciousMetal metal : metals) {
			// PreciousMetalDetail detail = rMap.get(metal.getId());
			// if (detail != null) {
			// metal.setDetail(detail);
			// rList.add(metal);
			// }
			// }
			for (Object[] obj : objList) {
				if (obj == null || obj.length < 2) {
					continue;
				}
				PreciousMetal metal = (PreciousMetal) obj[0];
				if (metal == null) {
					continue;
				}
				PreciousMetalDetail detail = (PreciousMetalDetail) obj[1];
				if (detail != null) {
					metal.setDetail(detail);
				}
				rList.add(metal);
			}

			return rList;
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return null;
	}

	@Override
	public List<PreciousMetal> runUps(int num, boolean flag) {
		// 判断list是否为空
		// 定义Session
		Session session = null;
		try {
			// 创建Session
			session = metalDao.createSession();
			/*
			 * CONCAT() 连接字符串 ROUND(AVG(xx), 2) 四舍五入保留两位小数 CEIL() 进一取整 ABS()
			 * 取绝对值 SUBSTRING() 截取字符串，与java有些区别
			 */
			StringBuffer hql = new StringBuffer("");
			hql.append("select MAX(CONCAT('%',SUBSTRING(md.createTime,1,10),'%')) from PreciousMetalDetail md order by md.dateTime DESC");
			Query query = session.createQuery(hql.toString());
			List<String> timeList = query.list();
			String dataTime = timeList.get(0); // 得到最近一天

			hql = new StringBuffer("");
			hql.append(" select m, (CONCAT(CEIL(ROUND(ABS(md.riseAndDrop/md.price),2)*100), '%')) as a, md.riseAndDrop as b");
			hql.append(" from PreciousMetalDetail as md, PreciousMetal as m");
			hql.append(" where md.metalId = m.id");
			if (flag) {
				hql.append(" and md.riseAndDrop/md.price > 0");
			} else {
				hql.append(" and md.riseAndDrop/md.price < 0");
			}
			hql.append(" and md.createTime like ?");
			hql.append(" GROUP BY md.metalId");
			hql.append(" order by ABS(md.riseAndDrop/md.price) DESC");

			query = session.createQuery(hql.toString());
			query.setString(0, dataTime);
			query.setFirstResult(0);
			query.setMaxResults(num);
			List<Object[]> objects = query.list();
			List<PreciousMetal> reList = new ArrayList<>();
			for (Object[] object : objects) {
				PreciousMetal metal = (PreciousMetal) object[0];
				metal.setRunUpPer(object[1] + "");
				metal.setRiseAndDrop(Double.valueOf(object[2] + ""));
				reList.add(metal);
			}
			return reList;
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			session.flush();
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return null;
	}

	/**
	 * 获取APP最新行情
	 *
	 * @param categoryId
	 * @return
	 */
	public List<AppPreciousBean> getNewPreciousMetalByCategoryWithApp(String categoryId, Pager pager){
		// List<PreciousMetal> metals = getMetalsByCategoryId(categoryId);
		// if (metals == null || metals.size() <= 0) {
		// return null;
		// }
		String hql = "select d.dateTime from PreciousMetalDetail as d,PreciousMetal as metal where metal.id = d.metalId and metal.categoryId = '" + categoryId
				+ "' order by d.dateTime desc";

		String dateTime = DateUtil.getSysDateString();
		try {
			Pager newPage = new Pager(1);
			newPage.setRowsPerPage(1);
			List<Object> details = detailDao.list(newPage, hql, null);
			if (details != null && details.size() > 0) {
				dateTime = details.get(0).toString();
			}
		} catch (Exception e) {
		}

		StringBuffer sql = new StringBuffer("select metal,detail from PreciousMetalDetail as detail,PreciousMetal as metal where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();

		sql.append(" and metal.categoryId = :categoryId");
		paramMap.put("categoryId", categoryId);
		sql.append(" and metal.id = detail.metalId");

		sql.append(" and detail.dateTime = :dateTime");
		paramMap.put("dateTime", dateTime);

		sql.append(" order by metal.createTime asc");

		List<AppPreciousBean> beanList = new ArrayList<>();
		try {
			List<Object[]> objList = null;
			if (pager != null) {
				objList = detailDao.list(pager, sql.toString(), paramMap);
			} else {
				objList = detailDao.list(sql.toString(), paramMap);
			}
			if (objList == null || objList.size() <= 0) {
				return beanList;
			}

			for (Object[] obj : objList) {
				if (obj == null || obj.length < 2) {
					continue;
				}
				PreciousMetal metal = (PreciousMetal) obj[0];
				if (metal == null) {
					continue;
				}
				PreciousMetalDetail detail = (PreciousMetalDetail) obj[1];

				AppPreciousBean bean = new AppPreciousBean();
				bean.setId(metal.getId());
				bean.setCategoryId(metal.getCategoryId());
				bean.setName(metal.getName());
				bean.setSpec(metal.getSpec());
				bean.setUnit(metal.getUnit());
				bean.setPrice(detail.getPrice());
				bean.setStartPrice(detail.getStartPrice());
				bean.setEndPrice(detail.getEndPrice());
				bean.setDateTime(detail.getDateTime());
				bean.setRiseAndDrop(detail.getRiseAndDrop());

				beanList.add(bean);
			}

			return beanList;
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return null;
	}
}
