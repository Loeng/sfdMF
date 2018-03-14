package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.plugin.wuliubao.yunshu.base.dao.IAttentionDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Address;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Attention;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
public class AttentionService implements IAttentionService {
	
	@Autowired
	private IAttentionDao dao;

	@Override
	@SuppressWarnings("unchecked")
	public List<Attention> getAllAttention(Pager page,HttpServletRequest request) {
		 //获取用户token
        String token = request.getHeader("WLB-Token");
        //通过token获取用户id
        String[] userid = token.split("_");
		
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from Attention a where 1=1");
		// 类型
		if(!StringUtil.isEmpty(userid[0])){
			sql.append(" and a.userId = :userId");
			map.put("userId", userid[0]);
		}
		try {
			List<Attention> list = dao.list(page, sql.toString(), map);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean addAttention(String friendId,HttpServletRequest request) {
		Attention attention=new Attention();
		 //获取用户token
        String token = request.getHeader("WLB-Token");
        //通过token获取用户id
        String[] userid = token.split("_");
        if(!StringUtil.isEmpty(friendId)){
        	attention.setFriendId(friendId);
        }
        if(!StringUtil.isEmpty(userid[0])){
        	attention.setUserId(userid[0]);
        }
        attention.setCreateTime(DateUtil.getSysDateTimeString());
        try {
			dao.addBean(attention);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteAttention(String friendId,HttpServletRequest request) {
		 //获取用户token
        String token = request.getHeader("WLB-Token");
        //通过token获取用户id
        String[] userid = token.split("_");
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("delete a from Attention as a where 1=1");
		if(!StringUtil.isEmpty(friendId)){
			sql.append(" and a.friendId = :friendId");
			map.put("friendId", friendId);
		}
		if(!StringUtil.isEmpty(userid[0])){
			sql.append(" and a.userId = :userId");
			map.put("userId", userid[0]);
		}
		
		try {
			dao.delete(sql.toString(), map);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getUserId(String friendId){
		
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from Attention a where 1=1");
		// 类型
		if(!StringUtil.isEmpty(friendId)){
			sql.append(" and a.friendId = :friendId");
			map.put("friendId", friendId);
		}
		try {
			List<Attention> attentions = dao.list(null, sql.toString(), map);
			if(attentions==null||attentions.size()<=0){
				return null;
			}
			List<String> list=new ArrayList<String>();
			for (Attention a : attentions) {
				list.add(a.getUserId());
			}
			
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean isAttention(String friendId) {
		
		
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from Attention a where 1=1");
		// 类型
		if(!StringUtil.isEmpty(friendId)){
			sql.append(" and a.friendId = :friendId");
			map.put("friendId", friendId);
		}
		try {
			List<Attention> list = dao.list(null, sql.toString(), map);
			 if(list!=null&&list.size()>0){
				 return true;
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
