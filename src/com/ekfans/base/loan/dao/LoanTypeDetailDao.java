package com.ekfans.base.loan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ekfans.base.loan.model.LoanType;
import com.ekfans.base.loan.model.LoanTypeDetail;
import com.ekfans.basic.hibernate.dao.GenericDao;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款类型明细DAO接口的实现
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2016-1-3
 * @version 1.0
 */
@Repository
@SuppressWarnings("unchecked")
public class LoanTypeDetailDao extends GenericDao implements ILoanTypeDetailDao {
	public LoanTypeDetailDao() throws HibernateException {
		super();
		this.beanClass = "com.ekfans.base.loan.model.LoanTypeDetail";
	}

	public static Logger log = LoggerFactory.getLogger(LoanTypeDetailDao.class);

	/**
	 * 根据类型ID删除明细
	 * 
	 * @param typeId
	 */
	@Override
	public void deleteByTypeId(String typeId, Session session) {
		if (StringUtil.isEmpty(typeId)) {
			return;
		}

		// 定义SQL参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer(" from LoanTypeDetail as detail where 1=1");
		sql.append(" and detail.typeId = :typeId");
		paramMap.put("typeId", typeId);

		try {
			if (session != null) {
				super.delete(sql.toString(), paramMap, session);
			} else {
				super.delete(sql.toString(), paramMap);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 新增
	 * 
	 * @param loanType
	 * @param request
	 * @param session
	 */
	@Override
	public void addDetails(LoanType loanType, HttpServletRequest request, Session session) {
		// 如果传过来的类型为空则返回
		if (loanType == null) {
			return;
		}
		// 从页面获取前缀
		String[] detailPrexs = request.getParameterValues("detailPrex");
		// 如果前缀为空，或者长度小于或等于0，则返回
		if (detailPrexs == null || detailPrexs.length <= 0) {
			return;
		}
		try {

			for (int i = 0; i < detailPrexs.length; i++) {
				// 获取循环中的前缀
				String detailPrex = detailPrexs[i];
				// 如果前缀为空，则进入下个循环
				if (StringUtil.isEmpty(detailPrex)) {
					continue;
				}
				// 定义新的对象
				LoanTypeDetail detail = new LoanTypeDetail();
				// 设置常规数据 - 银行ID
				detail.setBankId(loanType.getBankId());
				// 设置常规数据 - 类型ID
				detail.setTypeId(loanType.getId());
				// 设置名称
				detail.setName(request.getParameter(detailPrex + "name"));
				// 设置名称编号
				detail.setNameCode(request.getParameter(detailPrex + "nameCode"));
				// 设置字段类型
				detail.setType(request.getParameter(detailPrex + "type"));
				// 设置字段类型
				detail.setTypeValue(request.getParameter(detailPrex + "value"));
				// 设置字段类型
				String commType = request.getParameter(detailPrex + "commentType");
				Boolean type = false;
				if (!StringUtil.isEmpty(commType)) {
					try {
						type = Boolean.valueOf(commType);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				detail.setCommentType(type);
				// 设置字段类型
				detail.setNote(request.getParameter(detailPrex + "note"));
				detail.setPosition(i);
				if (session != null) {
					super.addBean(detail, session);
				} else {
					super.addBean(detail);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}

	}

	/**
	 * 根据类型ID获取明细
	 * 
	 * @param typeId
	 * @return
	 */
	@Override
	public List<LoanTypeDetail> getDetailsByTypeId(String typeId) {
		if (StringUtil.isEmpty(typeId)) {
			return null;
		}

		// 定义SQL参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer(" from LoanTypeDetail as detail where 1=1");
		sql.append(" and detail.typeId = :typeId");
		paramMap.put("typeId", typeId);
		sql.append(" order by detail.position asc");

		try {
			List<LoanTypeDetail> detailList = super.list(sql.toString(), paramMap);
			return detailList;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
