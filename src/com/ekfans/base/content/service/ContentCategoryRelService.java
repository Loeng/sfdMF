package com.ekfans.base.content.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.content.dao.IContentCategoryDao;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.pub.util.StringUtil;

/**
 * 内容和分类关系业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings("unchecked")
public class ContentCategoryRelService implements IContentCategoryRelService {

	public static Logger log = LoggerFactory.getLogger(ContentCategoryRelService.class);

	// 定义DAO
	@Autowired
	private IContentCategoryDao contentCategoryRelDao;

	public List<ContentCategoryRel> getContentCategoryRelById(String categoryId) {
		StringBuffer sql = new StringBuffer("select ccr from ContentCategoryRel as ccr where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(categoryId)) {
			sql.append(" and ccr.categoryId = :categoryId");
			paramMap.put("categoryId", categoryId);
		}
		try {
			List<ContentCategoryRel> list = contentCategoryRelDao.list(sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public boolean remove(String ccId, String cId) {
		try {
			// sql
			StringBuffer hql = new StringBuffer("delete from ContentCategoryRel c where 1=1");
			// 参数map
			Map<String, Object> map = new HashMap<String, Object>();
			// 如果资讯id不为空，添加查询条件
			if (!StringUtil.isEmpty(cId)) {
				hql.append(" and c.contentId = :cId");
				map.put("cId", cId);
			}
			// 如果分类id不为空，添加查询条件
			if (!StringUtil.isEmpty(cId)) {
				hql.append(" and c.categoryId = :ccId");
				map.put("ccId", ccId);
			}
			contentCategoryRelDao.delete(hql.toString(), map);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean add(ContentCategoryRel ccr) {
		try {
			if (ccr == null) {
				return false;
			}
			contentCategoryRelDao.addBean(ccr);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

    @Override
    public List<ContentCategoryRel> getContentCategoryRelByContentId(String contentId) {
        StringBuffer sql = new StringBuffer("select ccr from ContentCategoryRel as ccr where 1=1");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(contentId)) {
            sql.append(" and ccr.contentId = :contentId");
            paramMap.put("contentId", contentId);
        }
        try {
            List<ContentCategoryRel> list = contentCategoryRelDao.list(sql.toString(), paramMap);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
