package com.ekfans.base.store.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IWfpmlDao;
import com.ekfans.base.order.model.Wfpml;
import com.ekfans.base.store.dao.IWfpysDao;
import com.ekfans.base.store.dao.IWfpysRelDao;
import com.ekfans.base.store.model.Wfpys;
import com.ekfans.base.store.model.WfpysRel;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class WfpysService implements IWfpysService {
	@Autowired
	private IWfpysDao wfpysDao;
	
	@Autowired
	private IWfpysRelDao wfpysRelDao;

	@Override
	public Wfpys getWfpys(String id) {
		// TODO Auto-generated method stub
		try {
			return (Wfpys) wfpysDao.get(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Wfpys> getList(boolean first, String parentId,String name) {
		// TODO Auto-generated method stub
		List<Wfpys> wfpys=new ArrayList<>();
		StringBuffer hql=new StringBuffer("select w from Wfpys w where 1=1");
		Map<String, Object> map=new HashMap<>();
		if(first){
			hql.append(" and (w.parentId is null or w.parentId='')");
		}else{
			if(!StringUtil.isEmpty(parentId)){
				hql.append(" and w.parentId=:parentId");
				map.put("parentId", parentId);
			}
		}
		if(!StringUtil.isEmpty(name)){
			hql.append(" and w.name like :name");
			map.put("name", "%"+name+"%");
		}
		hql.append(" order by w.position asc");
		try {
			List<Wfpys> list=wfpysDao.list(hql.toString(), map);
			if(!first&&(null==list||list.size()==0)){
				wfpys.add((Wfpys) wfpysDao.get(parentId));
				return wfpys;
			}else{
				return list;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Wfpys> getList(Set<String> set) {
		// TODO Auto-generated method stub
		if (set == null || set.size()<= 0) {
			return null;
		}
		List<String> list=new ArrayList<>(set);
		StringBuffer lastSql = new StringBuffer(" from Wfpys as wf where 1=1");
		lastSql.append(" and wf.id in (");

		for (int i = 0; i <list.size(); i++) {
			String id =list.get(i);
			if (!StringUtil.isEmpty(id)) {
				lastSql.append("'").append(id).append("'");
				if (i + 1 < list.size()) {
					lastSql.append(",");
				}
			}
		}
		lastSql.append(")");
		lastSql.append(" order by wf.position asc");

		try {
			List<Wfpys> mlList = wfpysDao.list(lastSql.toString(), null);
			if (mlList == null || mlList.size() <= 0) {
				return null;
			}
			List<Wfpys> parent=new ArrayList<Wfpys>();
			List<Wfpys> child=new ArrayList<>();
			for(Wfpys w:mlList){
				if(StringUtil.isEmpty(w.getParentId())){
					parent.add(w);//父级
				}else{
					child.add(w);//子级
				}
			}
			
			if(null!=child&&child.size()>0){
				StringBuffer childSql = new StringBuffer(" from Wfpys as wf where 1=1");
				childSql.append(" and wf.id in (");

				for (int i = 0; i <child.size(); i++) {
					String id =child.get(i).getParentId();
					if (!StringUtil.isEmpty(id)) {
						childSql.append("'").append(id).append("'");
						if (i + 1 < child.size()) {
							childSql.append(",");
						}
					}
				}
				childSql.append(")");
				childSql.append(" order by wf.position asc");
				List<Wfpys> child_parent = wfpysDao.list(childSql.toString(), null);
				for(Wfpys w:child_parent){
					for(int i=0;i<child.size();i++){
						if(!StringUtil.isEmpty(child.get(i).getParentId())&&w.getId().equals(child.get(i).getParentId())){
							w.getChilds().add(child.get(i));
						}
					}
					parent.add(w);
				}
			}
			//处理list集合根据position排序
			Collections.sort(parent, new Comparator<Wfpys>() {
	            public int compare(Wfpys o1, Wfpys o2) {
	                // 升序
	                return (Integer.valueOf(o1.getPosition())).compareTo(Integer.valueOf(o2.getPosition()));
	            }
	        });
			return parent;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
	
	@Override
	public String getChildIdsByAccId(String accId) {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer("select ar.wfpysId from WfpysRel as ar");
			sql.append(" where ar.accreditId = :accreditId");
			paramMap.put("accreditId", accId);
			sql.append(" order by ar.position asc");
			List<String> acList = wfpysRelDao.list(sql.toString(), paramMap);
			if (acList == null || acList.size() <= 0) {
				return null;
			}
			StringBuffer returnStr = new StringBuffer();
			for (String detailId : acList) {
				if (!StringUtil.isEmpty(detailId)) {
					returnStr.append(detailId).append(";");
				}
			}
			return returnStr.toString();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return null;
	}

}
