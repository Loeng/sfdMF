package com.ekfans.base.content.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.content.dao.IContentRelDao;
import com.ekfans.base.content.model.ContentRel;
import com.ekfans.pub.util.StringUtil;

@Service
public class ContentRelService implements IContentRelService{

	@Autowired
	private IContentRelDao dao;
	
	@Override
	public boolean addRel(String contentId, String[] channelId) {
		// delete语句
		StringBuffer sql1 = new StringBuffer(" delete cr from ContentRel as cr where cr.contentId = :contentId");
		 // 定义参数MAP
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("contentId", contentId);
        Session session = null;
		Transaction tran = null;
		try {
			session = dao.createSession();
			tran = session.beginTransaction();
			// 先清空之前的数据
			dao.delete(sql1.toString(), paramMap);
			// 重新添加
			if(channelId != null && channelId.length > 0){
				for (int i = 0; i < channelId.length; i++) {
					ContentRel contentRel = new ContentRel();
					contentRel.setContentId(contentId);
					contentRel.setChannelId(channelId[i]);
					dao.addBean(contentRel, session);
				}
			}
			tran.commit();
			session.flush();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(session != null && session.isOpen()){
				session.close();
			}
		}
		return false;
	}

	@Override
	public List<ContentRel> getList(String contentId) {
		// TODO Auto-generated method stub
		if(!StringUtil.isEmpty(contentId)){
			Map<String, Object> map = new HashMap<>();
			StringBuffer sql = new StringBuffer("select cr,cc.name from ContentRel as cr,ContentCategory as cc where 1=1");
			sql.append(" and cr.channelId = cc.id");
			sql.append(" and cr.contentId = :contentId");
			map.put("contentId", contentId);
			try {
				List<Object[]> objects = dao.list(sql.toString(), map);
				List<ContentRel> contentRels = new ArrayList<>();
				if(objects != null && objects.size() > 0){
					for (Object[] obj : objects) {
						ContentRel contentRel = (ContentRel) obj[0];
						contentRel.setCname((String)obj[1]);
						contentRels.add(contentRel);
					}
				}
				return contentRels;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	
}
