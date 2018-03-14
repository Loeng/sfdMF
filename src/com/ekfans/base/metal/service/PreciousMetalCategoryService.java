package com.ekfans.base.metal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.metal.dao.IPreciousMetalCategoryDao;
import com.ekfans.base.metal.model.PreciousMetalCategory;
import com.ekfans.pub.util.DateUtil;
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
public class PreciousMetalCategoryService implements IPreciousMetalCategoryService {
	private Logger log = LoggerFactory.getLogger(PreciousMetalCategoryService.class);

	@Autowired
	private IPreciousMetalCategoryDao categoryDao;

	@Override
	public List<PreciousMetalCategory> getCategorys() {
		StringBuffer sql = new StringBuffer(" from PreciousMetalCategory as category where 1=1");
		 Map<String, Object> paramMap = new HashMap<String, Object>();

		 //临时加上，后期去掉！！！
		sql.append(" and category.id != :categoryId");
		paramMap.put("categoryId","ff8080815397d464015397df2a4d00b0");


		sql.append(" order by category.createTime asc");
		try {
			List<PreciousMetalCategory> categorys = categoryDao.list(sql.toString(), paramMap);
			return categorys;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public PreciousMetalCategory getCategoryById(String categoryId) {
		if (StringUtil.isEmpty(categoryId)) {
			return null;
		}
		try {
			return (PreciousMetalCategory) categoryDao.get(categoryId);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean deleteById(String categoryId) {
		if (StringUtil.isEmpty(categoryId)) {
			return true;
		}
		try {
			categoryDao.deleteById(categoryId);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean addOrUpdate(PreciousMetalCategory category) {
		if (category == null) {
			return false;
		}
		if (StringUtil.isEmpty(category.getCreateTime())) {
			category.setCreateTime(DateUtil.getSysDateTimeString());
		}
		try {
			if (StringUtil.isEmpty(category.getId())) {
				categoryDao.addBean(category);
			} else {
				categoryDao.updateBean(category);
			}
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return false;
	}

}
