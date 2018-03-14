package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.plugin.wuliubao.yunshu.base.dao.IVersionDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Version;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings("unchecked")
public class VersionServiceImpl implements IVersionService{

	@Autowired
	private IVersionDao dao;

	@Override
	public Version getnewVersion(String type) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from Version v where v.newVersion=1");
		// 类型
		if(!StringUtil.isEmpty(type)){
			sql.append(" and v.type = :type");
			map.put("type", type);
		}
		try {
			List<Version> list = dao.list(sql.toString(), map);
			if (null != list && list.size() > 0) {
				return (Version)list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Version> getVersionList(String type, String num, String newVersion,Pager page) {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("from Version v where 1=1");
		// 类型
		if(!StringUtil.isEmpty(type)){
			sql.append(" and v.type = :type");
			map.put("type", type);
		}
		// 版本号
		if(!StringUtil.isEmpty(num)){
			sql.append(" and v.num = :num");
			map.put("num", num);
		}
		// 是否最新版本  "1"为最新  "0"为旧版
		if(!StringUtil.isEmpty(newVersion)){
			sql.append(" and v.newVersion = :newVersion");
			map.put("newVersion", newVersion);
		}
		try {
		 if(page==null){
			List<Version> list = dao.list(sql.toString(), map);
			return list;
		 }
		 List<Version> list = dao.list(page,sql.toString(), map);
		 return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Version getVersion(String versionID) throws Exception {
		Version ver = (Version) dao.get(versionID);
		return ver;
	}

	@Override
	public void deleteVersion(String[] versionIDs) throws Exception {
		dao.deleteByIds(versionIDs);
	}

	@Override
	public void saveVersion(Version version) throws Exception {
		//如果设置该版本为最新版本
		if(version.getNewVersion().equals("1")){
			List<Version> list = this.getVersionList(version.getType(),"","1",null);
			for(Version ver : list){
				ver.setNewVersion("0");
				dao.updateBean(ver);
			}
		}
		version.setUpdateTime(DateUtil.getSysDateTimeString());
		dao.saveOrUpdateBean(version);
	}

	@Override
	public void updateVersion(Version version) throws Exception {
		dao.updateBean(version);
	}
	



}
