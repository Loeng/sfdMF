package com.ekfans.plugin.wftong.base.easemob.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.plugin.wftong.base.easemob.dao.IGroupsDao;
import com.ekfans.plugin.wftong.base.easemob.model.Groups;

/**
 * 
 * ClassName: GroupsService 
 * @Description: TODO
 * @author chengm
 * @date 2015年7月28日
 */
@Service
public class GroupsService implements IGroupsService {
	@Autowired
	private IGroupsDao groupsDao;

	@Override
	public boolean addGroupsInfo(Groups groups) {

		boolean flag = true;
		try {
			groupsDao.addBean(groups);
		} catch (Exception e) {

			e.printStackTrace();
			flag = false;
		}
		return flag;
	}

	@Override
	public Groups findByGroupsId(String groupsId) {
		try {
			return (Groups) groupsDao.get(groupsId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
