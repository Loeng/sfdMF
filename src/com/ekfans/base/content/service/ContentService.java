package com.ekfans.base.content.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.content.dao.IContentCategoryDao;
import com.ekfans.base.content.dao.IContentCategoryRelDao;
import com.ekfans.base.content.dao.IContentDao;
import com.ekfans.base.content.model.Content;
import com.ekfans.base.content.model.ContentCategory;
import com.ekfans.base.content.model.ContentCategoryRel;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 资讯业务实现Service
 *
 * @author Lgy
 * @version 1.0
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @date 2014-1-6
 */
@Service
@SuppressWarnings("unchecked")
public class ContentService implements IContentService {

    // 定义错误日志
    public static Logger log = LoggerFactory.getLogger(ContentService.class);

    // 定义DAO
    @Autowired
    private IContentDao contentDao;

    @Autowired
    private IContentCategoryRelDao contentCategoryRelDao;

    @Autowired
    private IContentCategoryDao contentCategoryDao;

    @Autowired
    private IContentRelService relService;

    /**
     * 根据id删除资讯
     */
    public boolean deleteContent(String id) {
        // 如果传进来的id为空，则返回false
        if (StringUtil.isEmpty(id)) {
            return false;
        }
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" delete ccr from ContentCategoryRel as ccr where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        sql.append(" and ccr.contentId = :contentId");
        paramMap.put("contentId", id);
        try {
            // 删除导航图片
            Content content = (Content) contentDao.get(id);
            if (!StringUtil.isEmpty(content.getNavigationImage())) {
                File file = new File(content.getNavigationImage());
                if (file.exists()) {
                    file.delete();
                }
            }
            // 调用DAO方法删除资讯
            contentDao.deleteById(id);
            // 调用DAO方法删除资讯与分类关系
            contentCategoryRelDao.delete(sql.toString(), paramMap);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据分类id获取资讯列表
     */
    public List<Content> listContent(Pager pager, String categoryId, String name) {
        // 定义查询SQL
        StringBuffer sql = new StringBuffer(" select c from Content as c,ContentCategoryRel as ccr where 1=1");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 建立资讯表与关系表的关系
        sql.append(" and c.id = ccr.contentId");

        // 添加分类ID
        if (!StringUtil.isEmpty(categoryId)) {
            sql.append(" and ccr.categoryId = :categoryId");
            paramMap.put("categoryId", categoryId);
        }
        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(name)) {
            sql.append(" and c.contentName = :name");
            paramMap.put("name", name);
        }

        sql.append(" order by c.updateTime desc, c.position desc");
        try {
            // 调用DAO方法查找资讯
            List<Content> list = contentDao.list(pager, sql.toString(), paramMap);
            return list;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加资讯
     */
    public boolean addContent(Content content, File uploadFile, String uploadFileContentType) {
        try {

            // 设置创建时间为当前系统时间
            content.setCreateTime(DateUtil.getSysDateTimeString());
            // 新增时跟新时间设置为当前系统时间
            content.setUpdateTime(DateUtil.getSysDateTimeString());
            // 设置审核时间为当前系统时间
            content.setCheckTime(DateUtil.getSysDateTimeString());
            // 将资讯保存到数据库
            contentDao.addBean(content);
            // 转换数组
            String[] ids = null;
            if (!StringUtil.isEmpty(content.getIds())) {
                // 转换数组
                ids = content.getIds().split(",");
            }
            relService.addRel(content.getId(), ids);

            // 返回true
            return true;
        } catch (Exception e) {
            // 若出现异常，返回false
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据id得到资讯
     */
    public Content getContent(String id) {
        try {
            return (Content) contentDao.get(id);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 修改资讯
     */
    public boolean modifyContent(Content content, File uploadFile, String uploadFileContentType) {
        // 如果传进来的资讯对象为空，则返回false
        if (content == null) {
            return false;
        }
        try {
            // 删除旧导航图片
            if (!StringUtil.isEmpty(content.getNavigationImage())) {
                File file = new File(content.getNavigationImage());
                if (file.exists()) {
                    file.delete();
                }
            }
            // 设置更新时间为当前系统时间
            content.setUpdateTime(DateUtil.getSysDateTimeString());
            // 调用DAO的方法修改资讯
            contentDao.updateBean(content);
            String[] ids = null;
            if (!StringUtil.isEmpty(content.getIds())) {
                // 转换数组
                ids = content.getIds().split(",");
            }
            relService.addRel(content.getId(), ids);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取资讯列表
     */
    @Override
    public List<Content> listContent(Pager pager, String name, String bigenDate, String endDate, String checkStatus, String categoryId) {
        // 定义查询SQL
        StringBuffer hql = new StringBuffer(" select c from Content as c , ContentCategoryRel r where r.contentId = c.id ");
        // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();

        // 如果查询条件输入了name，添加查询条件
        if (!StringUtil.isEmpty(name)) {
            hql.append(" and c.contentName like :name");
            paramMap.put("name", "%" + name + "%");
        }
        if (!StringUtil.isEmpty(bigenDate)) {
            hql.append(" and c.createTime >= :bigenDate");
            paramMap.put("bigenDate", bigenDate);
        }
        if (!StringUtil.isEmpty(endDate)) {
            hql.append(" and c.createTime <= :endDate");
            paramMap.put("endDate", endDate);
        }
        if (!StringUtil.isEmpty(checkStatus)) {
            hql.append(" and c.checkStatus = :checkStatus");
            paramMap.put("checkStatus", Boolean.parseBoolean(checkStatus));
        }
        if (!StringUtil.isEmpty(categoryId)) {
            hql.append(" and r.categoryId in(select id from ContentCategory where fullPath like :category)");
            paramMap.put("category", "%" + categoryId + "%");
        }
        hql.append(" order by c.updateTime DESC, c.position DESC");
        try {
            // 调用DAO方法查找资讯
            List<Content> list = contentDao.list(pager, hql.toString(), paramMap);
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 资讯详情页查询最新资讯方法
     */
    @Override
    public List<Content> listContent(String id) {
        Session session = this.contentDao.createSession();
        // 定义查询SQL
        StringBuffer hql = new StringBuffer("from Content where id <> ? order by createTime DESC");

        Query query = session.createQuery(hql.toString());
        query.setString(0, id);
        query.setFirstResult(0);
        query.setMaxResults(10);
        try {
            List<Content> list = query.list();
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return null;
    }

    /**
     * 检查资讯id是否存在
     */
    public boolean checkId(String id) {
        try {
            if (contentDao.get(id) != null) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据name查找资讯列表
     */
    public Content getContentByName(String contentName) {
        StringBuffer sql = new StringBuffer("select c from Content as c where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(contentName)) {
            sql.append(" and c.contentName = :contentName");
            map.put("contentName", contentName);
        }
        try {
            List<Content> list = contentDao.list(sql.toString(), map);
            return list.get(0);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据全路径取得资讯列表 WSJ
     * 需要的数据:Content：crateTime，id，contentName，navigationText，navigationImage
     * ，introduction
     */
    public List<Content> getContentByFullPath(Pager pager, String fullPath) {
        StringBuffer sql = new StringBuffer("select c.id,c.createTime,c.contentName,c.navigationText,c.navigationImage");
        sql.append(" from Content as c, ContentCategoryRel as ccr,ContentCategory as cc where 1=1");
        Map<String, Object> map = new HashMap<String, Object>();
        // 定义关联表
        sql.append(" and cc.id = ccr.categoryId");// 分类表的ID和分类关系表的分类ID对应
        sql.append(" and ccr.contentId = c.id");// 分类关系表的contentId和资讯表对应
        if (!StringUtil.isEmpty(fullPath)) {
            sql.append(" and cc.fullPath like :fullPath");
            map.put("fullPath", "%" + fullPath + "%");
        }
        sql.append(" order by c.updateTime desc, c.position desc");
        try {
            List<Object[]> list = contentDao.list(pager, sql.toString(), map);
            List<Content> contents = new ArrayList<Content>();
            for (Object[] objects : list) {
                Content content = new Content();
                content.setId((String) objects[0]);
                content.setCreateTime((String) objects[1]);
                content.setContentName((String) objects[2]);
                content.setNavigationText((String) objects[3]);
                content.setNavigationImage((String) objects[4]);
                contents.add(content);
            }
            return contents;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 新增资讯时,关联资讯的分类
     */
    @Override
    public boolean addContentCategory(String contentId, String contentCategoryId) {
        try {
            if (StringUtil.isEmpty(contentId) || StringUtil.isEmpty(contentCategoryId)) {
                return false;
            }
            ContentCategoryRel ccr = new ContentCategoryRel();
            ccr.setContentId(contentId);
            ccr.setCategoryId(contentCategoryId);
            contentCategoryRelDao.addBean(ccr);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 修改资讯的关联分类
     */
    @Override
    public boolean updateContentCategory(String contentId, String contentCategoryId) {
        try {
            if (StringUtil.isEmpty(contentId) || StringUtil.isEmpty(contentCategoryId)) {
                return false;
            }
            StringBuffer hql = new StringBuffer("select ccr from ContentCategoryRel as ccr where 1=1");
            Map<String, Object> params = new HashMap<String, Object>();
            hql.append(" and ccr.contentId = :contentId");
            params.put("contentId", contentId);
            List<ContentCategoryRel> ccrs = contentCategoryRelDao.list(hql.toString(), params);
            if (ccrs != null && ccrs.size() == 1) {
                ContentCategoryRel ccr = ccrs.get(0);
                ccr.setCategoryId(contentCategoryId);
                // 执行修改
                contentCategoryRelDao.updateBean(ccr);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    // =============================================查询咨询分类START==========================================

    /**
     * 获得资讯分类
     */
    @Override
    public ContentCategoryRel getContentCategoryRelByContentId(String contentId) {
        try {
            if (StringUtil.isEmpty(contentId)) {
                return null;
            }
            StringBuffer hql = new StringBuffer("select ccr");
            hql.append(" from ContentCategoryRel as ccr where 1=1");
            Map<String, Object> params = new HashMap<String, Object>();
            hql.append(" and ccr.contentId = :contentId");
            params.put("contentId", contentId);
            List<ContentCategoryRel> list = contentCategoryRelDao.list(hql.toString(), params);
            if (list != null && list.size() == 1) {
                ContentCategoryRel ccr = list.get(0);
                // 查询出分类的全名
                String contentCategoryName = "";
                ContentService.getContentCategoryById(contentCategoryName, ccr.getCategoryId(), contentCategoryDao, log);
                if (StringUtil.isEmpty(contentCategoryName)) {
                    ccr.setContentCategoryName(contentCategoryName);
                }
                return ccr;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * @param @param  contentCategoryId
     * @param @param  myDao
     * @param @param  mylog
     * @param @return 设定文件
     * @return ContentCategory 返回类型
     * @throws
     * @Title: getContentCategoryById
     * @Description: TODO(查询上一级contentCategory) 详细业务流程: (详细描述此方法相关的业务处理流程)
     */
    private static void getContentCategoryById(String contentName, String contentCategoryId, IContentCategoryDao myDao, Logger mylog) {
        try {
            if (StringUtil.isEmpty(contentCategoryId)) {
                return;
            }
            StringBuffer hql = new StringBuffer("select cc from ContentCategory as cc where 1=1");
            Map<String, Object> params = new HashMap<String, Object>();
            hql.append(" and cc.id = :contentCategoryId");
            params.put("contentCategoryId", contentCategoryId);
            List<ContentCategory> ccs = myDao.list(hql.toString(), params);
            if (ccs != null && ccs.size() == 1) {
                ContentCategory cc = ccs.get(0);
                contentName = cc.getName();
                if (cc.getParentId() != null && cc.getParentId().length() > 0) {
                    ContentService.getContentCategoryById(contentName, cc.getParentId(), myDao, mylog);
                }
            }
        } catch (Exception e) {
            mylog.error(e.getMessage());
        }
        return;
    }

    // ========================================查询咨询分类END==============================================

    /**
     * 根据资讯分类ID获取该分类所属的资讯
     *
     * @param @param  categoryId
     * @param @return 设定文件
     * @return List<Content> 返回类型
     * @throws
     * @Title: getContentsByCategory
     */
    public List<Content> getContentsByCategory(String categoryId, int showNumber, Pager pager) {
        // 如果传进来的分类ID为空，则返回null
        if (StringUtil.isEmpty(categoryId)) {
            return null;
        }

//		// 定义参数MAP
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		// 定义查询SQL，关联咨询表、资讯与分类关系关联表、资讯分类表
//		StringBuffer sql = new StringBuffer(" select DISTINCT content from Content as content,ContentCategory as cc,ContentCategoryRel as ccr,ContentRel as cr where 1=1");
//		// 关联频道ID
//		sql.append(" and ((cr.contentId = content.id and cr.channelId = :channelId) or (ccr.contentId = content.id and cc.fullPath like :categoryId"
//				+ " and cc.status = :categoryStatus and cc.id = ccr.categoryId and content.checkStatus = :contentStatus))");
//		paramMap.put("channelId", categoryId);
//		// 关联分类ID
//		paramMap.put("categoryId", "%" + categoryId + "%");
//
//		// 分类的状态为正常
//		paramMap.put("categoryStatus", true);
//
//		// 关联分类表 与分类与资讯关联表
//		sql.append(" and cc.id = ccr.categoryId");
//
//		// 资讯的审核状态为已审核
//		paramMap.put("contentStatus", true);
//
//		// 排序
//		sql.append(" order by content.updateTime DESC, content.position DESC");

        // 资讯没有关联频道时查询sql
        Map<String, Object> paramMap1 = new HashMap<String, Object>();
        // 定义查询SQL，关联咨询表、资讯与分类关系关联表、资讯分类表
        StringBuffer sql1 = new StringBuffer(" select content from Content as content,ContentCategory as cc,ContentCategoryRel as ccr where 1=1");
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
        sql1.append(" order by content.updateTime desc, content.position desc");

        try {
            // 定义返回的集合
            List<Content> contents = null;

            // 如果传进来的数量大于0，则使用分页，否则，不适用分页
            if (showNumber > 0) {
                pager.setCurrentPage(1);
                pager.setRowsPerPage(showNumber);
//				contents = contentDao.list(pager, sql.toString(), paramMap);
//				if (contents == null || contents.size() < 1) {
                contents = contentDao.list(pager, sql1.toString(), paramMap1);
//				}
                System.out.println();
            } else {
//				contents = contentDao.list(sql.toString(), paramMap);
//				if (contents == null || contents.size() < 1) {
                contents = contentDao.list(sql1.toString(), paramMap1);
//				}
            }
            return contents;

        } catch (Exception e) {
            // TODO: handle exception
        }

        return null;
    }

    @Override
    public List<Content> queryContentsByParams(Pager pager, Map<String, Object> params) {
        // 定义查询hql
        StringBuffer hql = new StringBuffer();

        hql.append("select c from Content c,ContentCategoryRel ccr where c.id = ccr.contentId");
        // 如果查询条件map不为空
        if (null != params) {

            // 获取商品名
            String pName = (String) params.get("cName");
            // 分类id
            List<String> ids = (List<String>) params.get("ids");
            if (StringUtils.isNotEmpty(pName)) {
                // 拼装hql
                hql.append(" and c.contentName like :cName");
            }
            if (ids != null && ids.size() > 0) {
                // 拼装hql
                hql.append(" and ccr.categoryId in (");
                for (int i = 0; i < ids.size(); i++) {
                    hql.append("'").append(ids.get(i)).append("'");
                    if (i < ids.size() - 1) {
                        hql.append(",");
                    }
                }
                hql.append(")");
            }
            // 拼装hql
            hql.append(" and ccr.contentId not in (select cr.contentId from ContentCategoryRel cr where 1=1");
            hql.append(" and cr.categoryId = :categoryId");
            hql.append(")");
        }

        hql.append(" order by c.updateTime desc, c.position desc");

        try {

            // 调用dao查询
            List<Content> list = contentDao.list(pager, hql.toString(), params);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Content> getCCList(Pager pager, List<ContentCategory> cclist) {
        if (cclist == null || cclist.size() <= 0) {
            return null;
        }
        StringBuffer ids = new StringBuffer("");
        for (int i = 0; i < cclist.size(); i++) {
            ids.append((i == 0 ? "'" + cclist.get(i).getId() : ",'" + cclist.get(i).getId()) + "'");
        }

        try {
            String hql = "select c from Content,ContentCategoryRel cc where cc.categoryId in (" + ids + ") and c.id=cc.contentId";

            hql = hql + " order by c.updateTime desc, c.position desc";
            return contentDao.list(hql, null);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
