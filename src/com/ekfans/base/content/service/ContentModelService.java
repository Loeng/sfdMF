package com.ekfans.base.content.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ekfans.base.content.dao.IContentDao;
import com.ekfans.base.content.model.Content;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.ImageUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.content.dao.IContentModelDao;
import com.ekfans.base.content.model.ContentModel;
import com.ekfans.pub.util.StringUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 资讯详细内容业务实现Service
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
public class ContentModelService implements IContentModelService {

    // 定义错误处理日志
    public static Logger log = LoggerFactory.getLogger(ContentModelService.class);

    @Autowired
    private IContentDao contentDao;
    // 定义DAO
    @Autowired
    private IContentModelDao contentModelDao;

    /**
     * 新增咨询详情信息
     */
    @Override
    public boolean addContentModel(String contentId, String[] contentDetails, HttpServletRequest request) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = contentModelDao.createSession();
            transaction = session.beginTransaction();

            String pic1 = "";
            String pic2 = "";
            String pic3 = "";

            int a = 1;
            if (contentDetails != null && contentDetails.length > 0) {
                for (int i = 0; i < contentDetails.length; i++) {
                    ContentModel contentModel = new ContentModel();
                    contentModel.setContentId(contentId);
                    String contentDetail = contentDetails[i];
                    contentModel.setContent(contentDetail);

                    if (a <= 3 && !StringUtil.isEmpty(contentDetail) && contentDetail.indexOf("<img") != -1) {
                        Document doc = Jsoup.parse(contentDetail);
                        Elements pics = doc.select("img[src]");
                        if (pics != null && pics.size() > 0) {
                            for (Element pic : pics) {
                                if (a == 1) {
                                    pic1 = pic.attr("src");
                                } else if (a == 2) {
                                    pic2 = pic.attr("src");
                                } else if (a == 3) {
                                    pic3 = pic.attr("src");
                                }
                                a++;
                            }

                        }
                    }

                    // 设置咨询的页数
                    contentModel.setPageNo(i + 1);
                    contentModelDao.addBean(contentModel, session);
                }
            }

            if (a > 1) {
                Content content = (Content) contentDao.get(contentId);
                content.setAppPic1(pic1);
                content.setAppPic2(pic2);
                content.setAppPic3(pic3);
//                ImageUtil.appContentPictureManager(content, "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/contentIco", request);
                contentDao.updateBean(content, session);
            }

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据咨询id删除咨询详情信息
     */
    @Override
    public boolean deleteContentModel(String id) {
        try {
            // 根据咨询id得到其对应的详情信息
            StringBuffer hql = new StringBuffer("select cm from ContentModel as cm where 1=1");
            Map<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(id)) {
                hql.append(" and cm.contentId=:id");
                params.put("id", id);
            }
            List<ContentModel> contentModels = contentModelDao.list(hql.toString(), params);
            if (contentModels != null) {
                ContentModel contentModel = contentModels.get(0);
                // 删除此咨询详情信息
                contentModelDao.delete(contentModel);
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 根据咨询id获得咨询的详情信息
     */
    @Override
    public List<ContentModel> getContentModelByContentId(String contentId) {
        try {
            // 得来咨询id对应的详情信息
            StringBuffer hql = new StringBuffer("select cm from ContentModel as cm where 1=1");
            Map<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(contentId)) {
                hql.append(" and cm.contentId = :contentId");
                params.put("contentId", contentId);
            }
            // 注意这里的店铺公告按PAGE_NO升序排序,有一个显示的先后顺序
            hql.append(" order by cm.pageNo ASC");
            List<ContentModel> contentModels = contentModelDao.list(hql.toString(), params);
            if (contentModels != null && contentModels.size() > 0) {
                return contentModels;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 根据咨询id获得咨询的详情信息
     */
    @Override
    public List<ContentModel> getContentModel2ByContentId(String contentId, String page) {
        try {
            // 得来咨询id对应的详情信息
            StringBuffer hql = new StringBuffer("select cm from ContentModel as cm where 1=1");

            Map<String, Object> params = new HashMap<String, Object>();
            if (!StringUtil.isEmpty(contentId)) {
                hql.append(" and cm.contentId = :contentId");
                params.put("contentId", contentId);
            }
            if (!StringUtil.isEmpty(page)) {
                hql.append(" and cm.pageNo = :pageNo");
                params.put("pageNo", Integer.valueOf(page));
            }
            // 注意这里的店铺公告按PAGE_NO升序排序,有一个显示的先后顺序
            hql.append(" order by cm.pageNo ASC");
            List<ContentModel> contentModels = contentModelDao.list(hql.toString(), params);
            if (contentModels != null && contentModels.size() > 0) {
                return contentModels;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 更新咨询的详情信息
     */
    @Override
    public boolean updateContentModel(String contentId, String[] contentDetails) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = contentModelDao.createSession();
            transaction = session.beginTransaction();
            if (StringUtil.isEmpty(contentId) || contentDetails == null || contentDetails.length == 0) {
                return false;
            }
            // 删除原有contentId所属的contentDetail
            StringBuffer hql = new StringBuffer("select cm from ContentModel as cm where 1 = 1");
            Map<String, Object> params = new HashMap<String, Object>();
            hql.append(" and cm.contentId = :contentId");
            params.put("contentId", contentId);
            List<ContentModel> oldCMs = contentModelDao.list(hql.toString(), params);
            if (oldCMs != null && oldCMs.size() > 0) {
                for (int oi = 0; oi < oldCMs.size(); oi++) {
                    contentModelDao.delete(oldCMs.get(oi));
                }
            }

            String pic1 = "";
            String pic2 = "";
            String pic3 = "";

            int a = 1;
            // 添加新的
            ContentModel cm = new ContentModel();
            for (int i = 0; i < contentDetails.length; i++) {
                String contentDetail = contentDetails[i];
                cm.setContentId(contentId);
                cm.setContent(contentDetail);
                cm.setPageNo(i + 1);
                if (a <= 3 && !StringUtil.isEmpty(contentDetail) && contentDetail.indexOf("<img") != -1) {
                    Document doc = Jsoup.parse(contentDetail);
                    Elements pics = doc.select("img[src]");
                    if (pics != null && pics.size() > 0) {
                        for (Element pic : pics) {
                            if (a == 1) {
                                pic1 = pic.attr("src");
                            } else if (a == 2) {
                                pic2 = pic.attr("src");
                            } else if (a == 3) {
                                pic3 = pic.attr("src");
                            }
                            a++;
                        }
                    }
                }
                contentModelDao.addBean(cm, session);
            }
            if (a > 1) {
                Content content = (Content) contentDao.get(contentId);
                content.setAppPic1(pic1);
                content.setAppPic2(pic2);
                content.setAppPic3(pic3);
//                ImageUtil.appContentPictureManager(content, "/customerfiles/content/" + DateUtil.getNoSpSysDateString() + "/contentIco", request);
                contentDao.updateBean(content, session);
            }

            session.flush();
            transaction.commit();
            session.close();
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return false;
    }

}