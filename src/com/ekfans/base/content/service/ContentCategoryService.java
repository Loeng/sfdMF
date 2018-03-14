package com.ekfans.base.content.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.content.dao.IContentCategoryDao;
import com.ekfans.base.content.dao.IContentDao;
import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.util.ContentCategoryHelper;
import com.ekfans.plugin.number.NoManager;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 内容分类业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-6
 * @version 1.0
 */
@Service
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class ContentCategoryService implements IContentCategoryService {
	// 定义Log
	public static Logger log = LoggerFactory.getLogger(ContentCategoryService.class);

	// 定义DAO
	@Autowired
	private IContentCategoryDao contentCategoryDao;

	@Autowired
	private IContentCategoryDao contentCategoryRelDao;
	
	@Autowired
	private IContentDao contentDao;

	/**
	 * 获取一级菜单
	 * 
	 * @return
	 */
	public List<ContentCategory> getCategories() {

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select cc from ContentCategory as cc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 权限的状态是放开
		sql.append(" and cc.status = '1'");

		// 找出一级菜单
		sql.append(" and (cc.parentId is null or cc.parentId = '' or cc.parentId = '')");

		// 根据位置进行排序
		sql.append(" order by cc.position asc");

		try {
			// 调用DAO执行SQL获取返回LIST
			List<ContentCategory> contentCategoryList = contentCategoryDao.list(sql.toString(), paramMap);

			// 如果数据库查出的数据为空，则返回空
			if (contentCategoryList == null || contentCategoryList.size() <= 0) {
				return null;
			}
			// // 递归设置子分类
			 setChilds(contentCategoryList);

			return contentCategoryList;
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}

		return null;
	}

	/**
	 * 
	 * @Title: setChilds
	 * @Description: TODO 递归设置子列表 详细业务流程: 传入一级分类，递归设置子列表
	 * @param @param categories 一级分类
	 * @return void
	 * @throws
	 */
	public void setChilds(List<ContentCategory> categories) {
		// 遍历分类列表
		for (int i = 0; i < categories.size(); i++) {
			// 获取分类对象
			ContentCategory category = (ContentCategory) categories.get(i);
			// 如果获取的对象为空，则跳过
			if (category == null) {
				continue;
			}
			// 设置子列表
			category.setChildList(getChildCategories(category.getId()));
			// 递归调用设置子列表的子列表
			setChilds(category.getChildList());
		}
	}

	/**
	 * 根据上级菜单获取下级菜单:带显示条件
	 * 
	 * @param parentId
	 * @return
	 */
	public List<ContentCategory> getChildCategories(String parentId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select cc from ContentCategory as cc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果传进来的父ID不为空，则加上父ID条件;若为空，则返回null
		if (!StringUtil.isEmpty(parentId)) {
			sql.append(" and cc.parentId = :parentId");
			paramMap.put("parentId", parentId);
		} else {
			return null;
		}
		// 权限的状态是放开
		sql.append(" and cc.status = '1'");

		// 根据位置进行排序
		sql.append(" order by cc.position asc");

		try {
			// 调用DAO执行SQL获取返回LIST
			List contentCategoryList = contentCategoryDao.list(sql.toString(), paramMap);

			return contentCategoryList;

		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}

		return null;
	}

	/**
	 * 根据上级菜单获取下级菜单:不带显示条件
	 * 
	 * @param parentId
	 * @return
	 */
	@Override
	public List<ContentCategory> getAllChildCategories(String parentId) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select cc from ContentCategory as cc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果传进来的父ID不为空，则加上父ID条件;若为空，则返回null
		if (!StringUtil.isEmpty(parentId)) {
			sql.append(" and cc.parentId = :parentId");
			paramMap.put("parentId", parentId);
		} else {
			return null;
		}

		// 根据位置进行排序
		sql.append(" order by cc.position asc");

		try {
			// 调用DAO执行SQL获取返回LIST
			List contentCategoryList = contentCategoryDao.list(sql.toString(), paramMap);

			return contentCategoryList;

		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}

		return null;
	}

	/**
	 * 添加内容分类
	 */
	public boolean addCategory(ContentCategory category, String picturePath) {
		// 如果分类为空，返回false
		if (category == null) {
			return false;
		}
		try {

			// 设置ID
			String categoryId = new NoManager().createContentCategoryId();
			category.setId(categoryId);
			// 如果上传了图标，设置属性
			if (!StringUtil.isEmpty(picturePath)) {
				category.setIcon(picturePath);
			}
			// 设置创建时间
			category.setCreateTime(DateUtil.getSysDateTimeString());

			// 得到父分类
			ContentCategory parentCategory = (ContentCategory) contentCategoryDao.get(category.getParentId());
			// 设置分类全路径(父分类全路径+分类id)
			// 第一次添加父分类的全路径
			category.setFullPath(parentCategory.getFullPath() + "_" + category.getId());
			// 调用DAO方法添加分类
			contentCategoryDao.addBean(category);
			return true;
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 删除内容分类
	 */
	public boolean deleteCategory(String id) {
		// 如果id为空，返回false
		if (StringUtil.isEmpty(id)) {
			return false;
		}
		try {
			// 删除旧图标
			ContentCategory category = (ContentCategory) contentCategoryDao.get(id);
			if (!StringUtil.isEmpty(category.getIcon())) {
				File file = new File(category.getIcon());
				if (file.exists()) {
					file.delete();
				}
			}
			// 调用DAO方法删除分类
			contentCategoryDao.deleteById(id);
			// TODO 删除子分类
			return true;
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 修改内容分类
	 */
	public boolean modifyCategory(ContentCategory category, String picturePath) {
		// 如果分类为空，返回false
		if (category == null) {
			return false;
		}
		try {
			// 如果上传了图标，替换旧图标
			if (!StringUtil.isEmpty(picturePath)) {
				category.setIcon(picturePath);
			}
			// 调用DAO方法修改分类
			contentCategoryDao.updateBean(category);
			return true;
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 根据id查询内容分类
	 */
	public ContentCategory getCategoryById(String id) {
		// 如果id为空，返回null
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		try {
			// 调用DAO方法查找分类
			return (ContentCategory) contentCategoryDao.get(id);
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}
		return null;
	}

	public boolean checkId(String id) {
		try {
			// 如果id存在返回true
			if (contentCategoryDao.get(id) != null) {
				return true;
			}
		} catch (Exception e) {
			// 将报错的信息打印到日志表
			log.error(e.getMessage());
		}
		return false;
	}

	// 根据name获得对象
	public ContentCategory getContentCategoryByName(String name,String parentId) {
		StringBuffer sql = new StringBuffer("select cc from ContentCategory as cc where 1=1");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and cc.name = :name");
			paramMap.put("name", name);
		}
		if (!StringUtil.isEmpty(parentId)) {
			sql.append(" and cc.parentId = :parentId");
			paramMap.put("parentId", parentId);
		}
		try {
			List<ContentCategory> list = contentCategoryDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得ContentCategory的二级菜单
	 */
	public List<ContentCategory> getContentCategoryByFullPath(Pager pager, String parentId, String id) {
		StringBuffer sql = new StringBuffer("select cc.id,cc.name,c.contentName," + "c.createTime,c.navigationText,c.navigationImage,cc.description,c.id" + " from ContentCategory as cc,Content as c,ContentCategoryRel as ccr where 1=1");
		sql.append(" and cc.id = ccr.categoryId");
		sql.append(" and ccr.contentId = c.id");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(parentId)) {
			sql.append(" and cc.parentId = :id");
			map.put("id", parentId);
		}
		if (!StringUtil.isEmpty(id)) {
			sql.append(" and c.id = :id");
			map.put("id", id);
		}
		// if(!StringUtil.isEmpty(id)){
		// sql.append(" and cc.id = :id");
		// map.put("id",id);
		// }
		sql.append(" order by c.createTime DESC ");
		try {
			List<Object[]> list = contentCategoryDao.list(pager, sql.toString(), map);
			List<ContentCategory> contentCategorys = new ArrayList<ContentCategory>();
			for (Object[] objects : list) {
				ContentCategory contentCategory = new ContentCategory();
				contentCategory.setId((String) objects[0]);
				contentCategory.setName((String) objects[1]);
				contentCategory.setContentName((String) objects[2]);
				contentCategory.setCreatTime((String) objects[3]);
				contentCategory.setNavigationText((String) objects[4]);
				contentCategory.setNavigationImage((String) objects[5]);
				contentCategory.setDescription((String) objects[6]);
				contentCategory.setCId((String) objects[7]);
				contentCategorys.add(contentCategory);

			}
			return contentCategorys;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据获取的parentId获取ContentCategory集合 wsj
	 * 需要的数据：ContentCategory：id，fullPath,name
	 * ContentCategoryRel：contentId，categoryId
	 * Content：crateTime，id，contentName，navigationText
	 * ，navigationImage，introduction
	 */
	@Override
	public List<ContentCategory> getCategoryByPrarentId(Pager pager, String parentId, String fullPath) {
		StringBuffer sql = new StringBuffer("select ccr.categoryId,ccr.contentId,cc.id,c.contentName,c.createTime,c.navigationText,");
		sql.append("c.navigationImage,cc.description,cc.fullPath,cc.name from ContentCategoryRel as ccr,ContentCategory as cc,Content as c where 1=1");
		// 定义关联表
		sql.append(" and cc.id = ccr.categoryId");// 分类表的ID和分类关系表的分类ID对应
		sql.append(" and ccr.contentId = c.id");// 分类关系表的contentId和资讯表对应
		Map<String, Object> map = new HashMap<String, Object>();
		if (!StringUtil.isEmpty(parentId)) {
			sql.append(" and cc.parentId = :parentId");
			map.put("parentId", parentId);
		}
		if (!StringUtil.isEmpty(fullPath)) {
			sql.append(" and cc.fullPath like :fullPath");
			map.put("fullPath", "%" + fullPath + "%");
		}
 		sql.append(" group by c.id");
		try {
			List<Object[]> list = contentCategoryDao.list(pager, sql.toString(), map);
			List<ContentCategory> contentCategorys = new ArrayList<ContentCategory>();
			for (Object[] objects : list) {
				ContentCategory contentCategory = new ContentCategory();
				contentCategory.setCategoryId((String) objects[0]);
				contentCategory.setContentId((String) objects[1]);
				contentCategory.setId((String) objects[2]);
				contentCategory.setContentName((String) objects[3]);
				contentCategory.setCreatTime((String) objects[4]);
				contentCategory.setNavigationText((String) objects[5]);
				contentCategory.setNavigationImage((String) objects[6]);
				contentCategory.setDescription((String) objects[7]);
				contentCategory.setFullPath((String) objects[8]);
				contentCategory.setName((String) objects[9]);
				contentCategorys.add(contentCategory);
			}
			return contentCategorys;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 查询通用分类下的二级子分类
	 * 
	 * @param pager
	 * @return
	 */
	@Override
	public List<ContentCategory> getContentCategoryBypId(Pager pager) {
		try {
			StringBuffer hql = new StringBuffer("from ContentCategory where parentId = '00000001' order by createTime DESC");
			List<ContentCategory> list = contentCategoryDao.list(pager, hql.toString(), null);
			return list;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据咨询名,获取咨询下属的内容集合
	 */
	@Override
	public List<Content> getContentsByCategoryId(String categoryId) {
		try {
			if (StringUtil.isEmpty(categoryId)) {
				return null;
			}
			StringBuffer hql = new StringBuffer("select c from Content as c,ContentCategoryRel as ccr,ContentCategory as cc where 1=1");
			Map<String, Object> params = new HashMap<String, Object>();
			// 添加关联关系
			hql.append(" and c.status = true");
			hql.append(" and cc.id = ccr.categoryId");
			hql.append(" and ccr.contentId = c.id");
			// 添加条件
			hql.append(" and cc.id = :id");
			params.put("id", categoryId);
	        hql.append(" order by c.position asc,c.updateTime desc");
			List<Content> contents = contentCategoryDao.list(hql.toString(), params);
			if (contents != null && contents.size() > 0) {
				return contents;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 根据父ID查询子分类集合
	 * 
	 * @Title: getContentCategorysByParentId
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param parentId
	 * @param @param showNumber
	 * @param @return 设定文件
	 * @return List<ContentCategory> 返回类型
	 * @throws
	 */
	public List<ContentCategory> getContentCategorysByParentId(String parentId, boolean canParentNull, int showNumber) {

		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select cc from ContentCategory as cc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 权限的状态是放开
		sql.append(" and cc.status = '1'");
		sql.append(" and cc.fullPath like :categoryId");
		paramMap.put("categoryId", "%" + parentId + "%");
		// 根据位置进行排序
		sql.append(" order by cc.position asc");
		// 定义查询SQL
		StringBuffer sql2 = new StringBuffer(" select cc from ContentCategory as cc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap2 = new HashMap<String, Object>();
		// 如果传进来的父ID不为空，则加上父ID条件;若为空，则返回null
		if (!StringUtil.isEmpty(parentId)) {
			sql2.append(" and cc.parentId = :parentId");
			paramMap2.put("parentId", parentId);
		} else {
			if (canParentNull) {
				sql2.append(" and (cc.parentId = null or cc.parentId = '' or cc.parentId = ' ')");
			} else {
				return null;
			}
		}
		// 权限的状态是放开
		sql2.append(" and cc.status = '1'");
		// 根据位置进行排序
		sql2.append(" order by cc.position asc");
		try {
			// 调用DAO查询SQL
			List<ContentCategory> contentCategoryList = contentCategoryDao.list(sql.toString(), paramMap);
			// 调用DAO查询SQL
			List<ContentCategory> categoryList = null;
			if (showNumber > 0) {
				Pager page = new Pager(1);
				page.setRowsPerPage(showNumber);
				categoryList = contentCategoryDao.list(page, sql2.toString(), paramMap2);
			} else {
				categoryList = contentCategoryDao.list(sql2.toString(), paramMap2);
			}
			ContentCategoryHelper.recoverCategoryList(contentCategoryList, categoryList);
			return categoryList;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据咨询ID查询咨询全分类集合
	 */
	@Override
	public List<List<ContentCategory>> getContentCategoryBycontentId(String contentId) {
		List<List<ContentCategory>> list=new ArrayList<>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("select ccr.categoryId from ContentCategoryRel as ccr where 1=1");
		if (!StringUtil.isEmpty(contentId)) {
			sql.append(" and ccr.contentId = :contentId");
			paramMap.put("contentId", contentId);
		}
		String categoryId;
		try {
			categoryId = (String) contentCategoryRelDao.list(sql.toString(), paramMap).get(0);
			ContentCategory contentCategory =getCategoryById(categoryId);
			//调用递归方法处理数据
			findParent(list, contentCategory);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			Map<String, Object> listMap = new HashMap<>();
//			StringBuffer sql = new StringBuffer("select ccr.categoryId from ContentCategoryRel as ccr where 1=1");
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			if (!StringUtil.isEmpty(contentId)) {
//				sql.append(" and ccr.contentId = :contentId");
//				paramMap.put("contentId", contentId);
//			}
//			String categoryId = (String) contentCategoryRelDao.list(sql.toString(), paramMap).get(0);
//			ContentCategory contentCategory = null;
//			if (!StringUtil.isEmpty(categoryId)) {
//				contentCategory = getCategoryById(categoryId);
//				if (contentCategory != null) {
//					listMap.put("fristName",contentCategory.getName());
//					listMap.put("fristId",contentCategory.getId());
//				}
//			}
//			if (contentCategory != null && contentCategory.getParentId() != null || !StringUtil.isEmpty(contentCategory.getParentId())) {
//				ContentCategory category = (ContentCategory) contentCategoryDao.get(contentCategory.getParentId());
//				if (category != null) {
//					if(!StringUtil.isEmpty(category.getParentId()) || category.getParentId() != null){
//						listMap.put("secondName",category.getName());
//						listMap.put("secondId",category.getId());
//						ContentCategory c = (ContentCategory) contentCategoryDao.get(category.getParentId());
//						if (c != null && !StringUtil.isEmpty(c.getName())) {
//							listMap.put("thirdName",c.getName());
//							listMap.put("thirdId",c.getId());
//						}
//					}
//				}
//			}
////			Collections.reverse(listMap);
//			return listMap;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		return null;
	}
	
	
	// 递归分类
	public void findParent(List<List<ContentCategory>> list,ContentCategory category){
		List<ContentCategory> list2=null;
		ContentCategory category2=null;
		if(category != null){
			if(!StringUtil.isEmpty(category.getParentId())){
				category2=getCategoryById(category.getParentId());
				//再次调用此方法
				findParent(list,category2);
				list2=getAllChildCategories(category.getParentId());
			}else{
				list2=getCategories();
			}
			for(int i=0;i<list2.size();i++){
				if(list2.get(i).getId().equals(category.getId())){
					list2.get(i).setChecked(true);
				}
			}
		}else{
			list2=getCategories();
		}
		list.add(list2);
	}

	/**
	 * 获取分类的全称
	 * 
	 * @Title: getCategoryFullName
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param categoryId 资讯分类ID
	 * @param @param splitStr 资讯分类名称每级之间的分隔符
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getCategoryFullNameByCategoryId(String categoryId, String splitStr) {

		// 如果传进来的商品分类ID为空，则返回null
		if (StringUtil.isEmpty(categoryId)) {
			return null;
		}

		try {
			// 先获取该分类的实体类
			ContentCategory category = (ContentCategory) contentCategoryDao.get(categoryId);
			// 如果查出的实体为空，则返回null
			if (category == null) {
				return null;
			}

			// 如果该分类没有父分类，则表示该分类是跟分类，直接返回该分类的名称
			if (StringUtil.isEmpty(category.getParentId()) || StringUtil.isEmpty(category.getFullPath())) {
				return category.getName();
			}

			String[] categoryIds = category.getFullPath().split("_");
			// 如果截取的fullPath的集合为空或者长度等于0，则返回该分类的名称
			if (categoryIds == null || categoryIds.length <= 0) {
				return category.getName();
			}

			// 拼写SQL语句，使用in的方法，(100万条数据以下不存在性能问题)
			StringBuffer sql = new StringBuffer("select category.id,category.name from ContentCategory as category where 1=1");
			sql.append(" and category.id in (");
			for (int i = 0; i < categoryIds.length; i++) {
				sql.append("'").append(categoryIds[i]).append("'");
				if (i < categoryIds.length - 1) {
					sql.append(",");
				}
			}
			sql.append(")");

			// 调用DAO方法查询SQL
			List cList = contentCategoryDao.list(sql.toString(), null);

			// 如果查出的集合为空，或者长度小于或等于0，则返回该分类的名称
			if (cList == null || cList.size() <= 0) {
				return category.getName();
			}

			Map<String, String> nameMap = new HashMap<String, String>();
			// 便利查出的集合
			for (int i = 0; i < cList.size(); i++) {
				Object[] obj = (Object[]) cList.get(i);
				if (obj != null && obj.length > 0) {
					nameMap.put((String) obj[0], (String) obj[1]);
				}
			}

			StringBuffer returnName = new StringBuffer("");

			for (int i = 0; i < categoryIds.length; i++) {
				if (StringUtil.isEmpty(categoryIds[i])) {
					continue;
				}
				returnName.append(nameMap.get(categoryIds[i]));
				if (i < categoryIds.length - 1) {
					returnName.append(splitStr);
				}
			}

			return returnName.toString();

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@Override
	public List<String> getAllCategoryIdsById(String id) {
		// 如果传进来的ID为空，则返回null
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select cc.id from ContentCategory as cc where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 权限的状态是放开
		sql.append(" and cc.status = '1'");

		sql.append(" and cc.fullPath like :categoryId");
		paramMap.put("categoryId", "%" + id + "%");

		// 根据位置进行排序
		sql.append(" order by cc.position asc");
		try {
			// 调用DAO查询SQL
			List<String> ids = contentCategoryDao.list(sql.toString(), paramMap);

			return ids;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<ContentCategory> getChlichAll(String id) {
		String hql = "from ContentCategory where fullPath like '%" + id + "%' and status=true";
		
		try {
			List<ContentCategory> categories = contentCategoryDao.list(hql, null);
			return categories;
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}


	@Override
	public List<Content> getWebContentByFullPath(Pager pager,String categoryId) {
		// 如果传进来的分类ID为空，则返回null
        if (StringUtil.isEmpty(categoryId)) {
            return null;
        }

//        // 定义参数MAP
//        Map<String, Object> paramMap = new HashMap<String, Object>();
//        // 定义查询SQL，关联咨询表、资讯与分类关系关联表、资讯分类表
//        StringBuffer sql = new StringBuffer(
//                " select DISTINCT content from Content as content,ContentCategory as cc,ContentCategoryRel as ccr,ContentRel as cr where 1=1");
//        // 关联频道ID
//        sql.append(" and ((cr.contentId = content.id and cr.channelId = :channelId) or (ccr.contentId = content.id and cc.fullPath like :categoryId"
//        		+ " and cc.status = :categoryStatus and cc.id = ccr.categoryId and content.checkStatus = :contentStatus))");
//        paramMap.put("channelId", categoryId);
//        // 关联分类ID
//        paramMap.put("categoryId", "%" + categoryId + "%");
//
//        // 分类的状态为正常
//        paramMap.put("categoryStatus", true);
//
//        // 关联分类表 与分类与资讯关联表
//        sql.append(" and cc.id = ccr.categoryId");
//
//        // 资讯的审核状态为已审核
//        paramMap.put("contentStatus", true);
//
//        // 排序
//        sql.append(" order by content.updateTime DESC, content.position DESC");
        
        
        // 资讯没有关联频道时查询sql
        Map<String, Object> paramMap1 = new HashMap<String, Object>();
        // 定义查询SQL，关联咨询表、资讯与分类关系关联表、资讯分类表
        StringBuffer sql1 = new StringBuffer(
                " select content from Content as content,ContentCategory as cc,ContentCategoryRel as ccr where 1=1");
        // 关联分类ID
        sql1.append(" and cc.fullPath like :categoryId");
        paramMap1.put("categoryId", "%" + categoryId + "%");

        // 分类的状态为正常
        sql1.append(" and cc.status = :categoryStatus");
        paramMap1.put("categoryStatus", true);

        // 关联分类表 与分类与资讯关联表
        sql1.append(" and cc.id = ccr.categoryId");

        // 关联资讯表 与 分类与资讯关联表
        sql1.append(" and ccr.contentId = content.id");

        // 资讯的审核状态为已审核
        sql1.append(" and content.checkStatus = :contentStatus");
        paramMap1.put("contentStatus", true);
        // 排序
        sql1.append(" order by content.position desc,content.updateTime desc");
        
        
        try {
            // 定义返回的集合
            List<Content> contents = null;

//            contents = contentDao.list(pager, sql.toString(), paramMap);
//            if(contents == null || contents.size() < 1){
            	contents = contentDao.list(pager, sql1.toString(), paramMap1);
//            }

            return contents;

        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
	}
}