package com.ekfans.base.order.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.dao.IWfpmlDao;
import com.ekfans.base.order.model.Wfpml;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class WfpmlService implements IWfpmlService {
	@Autowired
	private IWfpmlDao wfpmlDao;

	/**
	 * 根据父ID查询目录
	 */
	@Override
	public List<Wfpml> getWfpmlByParentId(Pager pager, String parentId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from Wfpml");
		List<Wfpml> ml = new ArrayList<Wfpml>();
		if (!StringUtil.isEmpty(parentId)) {
			hql.append(" where fullPath like :parentId");
			paramMap.put("parentId", "%" + parentId + "%");
			hql.append(" and no != '' and no != ' ' and no != null and no != 'null'");
			hql.append(" and parentId != '' and parentId != ' ' and parentId != null and parentId != 'null' ");
			hql.append(" order by position asc");
		} else {
			return null;
		}
		try {
			if (pager != null) {
				ml = wfpmlDao.list(pager, hql.toString(), paramMap);
			} else {
				ml = wfpmlDao.list(pager, hql.toString(), paramMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ml;
	}

	@Override
	public List<Wfpml> getWfpMlByName3(Pager pager, String name) {
		StringBuffer hql = new StringBuffer(" from Wfpml wl ");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Wfpml> list = new ArrayList<Wfpml>();
		if (!StringUtil.isEmpty(name)) {
			hql.append(" where wl.parentId in(select id from Wfpml ww where ww.parentId in(select id from Wfpml w where w.name like :mlName and (w.parentId = null or w.parentId is null or w.parentId = 'null' or w.parentId = ' ')))");
			map.put("mlName", "%" + name + "%");
		}
		hql.append(" order by wl.position asc");
		try {
			list = wfpmlDao.list(pager, hql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Wfpml> getWfpMlByName1(String name) {
		StringBuffer hql = new StringBuffer(" from Wfpml w ");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Wfpml> list = new ArrayList<Wfpml>();
		if (!StringUtil.isEmpty(name)) {
			hql.append("  where w.name like :mlName and (w.parentId = null or w.parentId is null or w.parentId = 'null' or w.parentId = ' ')");
			map.put("mlName", "%" + name + "%");
		}
		hql.append(" order by w.position asc");
		try {
			list = wfpmlDao.list(hql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Wfpml> getWfpMlByName2(String name) {
		StringBuffer hql = new StringBuffer(" from Wfpml ww ");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Wfpml> list = new ArrayList<Wfpml>();
		if (!StringUtil.isEmpty(name)) {
			hql.append("  where ww.parentId in(select id from Wfpml w where w.name like :mlName and (w.parentId = null or w.parentId is null or w.parentId = 'null' or w.parentId = ' '))");
			map.put("mlName", "%" + name + "%");
		}
		hql.append(" order by w.position asc");
		try {
			list = wfpmlDao.list(hql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Wfpml> getWfpMlByName3(String name) {
		StringBuffer hql = new StringBuffer(" from Wfpml wl ");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Wfpml> list = new ArrayList<Wfpml>();
		if (!StringUtil.isEmpty(name)) {
			hql.append("where wl.parentId in(select id from Wfpml ww where ww.parentId in(select id from Wfpml w where w.name like :mlName and (w.parentId = null or w.parentId is null or w.parentId = 'null' or w.parentId = ' ')))");
			map.put("mlName", "%" + name + "%");
		}
		hql.append(" order by wl.position asc");
		try {
			list = wfpmlDao.list(hql.toString(), map);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据父ID查询目录
	 */
	@Override
	public List<Wfpml> getWfpmlByParent(String parentId) {

		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from Wfpml");
		List<Wfpml> ml = new ArrayList<Wfpml>();
		if (!StringUtil.isEmpty(parentId)) {
			hql.append(" where parentId=:parentId");
			paramMap.put("parentId", parentId);
		} else {
			hql.append(" where parentId is null");
		}
		hql.append(" order by position asc");
		try {
			ml = wfpmlDao.list(hql.toString(), paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ml;
	}

	/**
	 * 根据名录ID查询名录详细信息
	 */
	@Override
	public List<Wfpml> getWfpMlById(String ids) {
		List<Wfpml> ml = new ArrayList<Wfpml>();
		if (!StringUtil.isEmpty(ids)) {
			if (ids.contains(",")) {
				String[] id = ids.split(",");
				for (int i = 0; i < id.length; i++) {
					try {
						Wfpml m = (Wfpml) wfpmlDao.get(id[i]);
						ml.add(m);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					Wfpml m = (Wfpml) wfpmlDao.get(ids);
					ml.add(m);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ml;
	}

	public String getWFPMLNames(String ids) {
		StringBuffer returnStr = new StringBuffer("");
		if (!StringUtil.isEmpty(ids)) {
			if (ids.contains(",")) {
				String[] id = ids.split(",");
				for (int i = 0; i < id.length; i++) {
					try {
						Wfpml m = (Wfpml) wfpmlDao.get(id[i]);
						if (m != null && !StringUtil.isEmpty(m.getName())) {
							if (i != 0) {
								returnStr.append(m.getName());
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				try {
					Wfpml m = (Wfpml) wfpmlDao.get(ids);
					if (m != null && !StringUtil.isEmpty(m.getName())) {
						returnStr.append(m.getName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return returnStr.toString();
	}

	@Override
	public void addWfpml(Wfpml m, String paraentId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select max(wpl.position) from Wfpml wpl where 1=1");
		if (!StringUtil.isEmpty(paraentId)) {
			hql.append(" and wpl.parentId = :parentId");
			paramMap.put("parentId", paraentId);
		} else {
			hql.append(" and (wpl.parentId = null or wpl.parentId is null or wpl.parentId = 'null' or wpl.parentId = ' ')");
		}
		try {
			List list = wfpmlDao.list(hql.toString(), paramMap);
			int position = 0;
			if (list != null && list.size() > 0 && list.get(0) != null) {
				String posStr = list.get(0).toString();
				if (!StringUtil.isEmpty(posStr)) {
					try {
						position = Integer.parseInt(posStr);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			}
			m.setPosition(position + 1);
			wfpmlDao.addBean(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delWfpml1(String firstId) {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			// 查询到爷爷id为firstid的三级名录
			StringBuffer three = new StringBuffer(" from Wfpml where parentId in (select ww.id from Wfpml ww where ww.parentId =:firstId)");
			paramMap.put("firstId", firstId);

			List list = wfpmlDao.list(three.toString(), paramMap);
			// 删除爷爷id为firstid的三级名录
			for (int i = 0; i < list.size(); i++) {
				wfpmlDao.delete(list.get(i));
			}
			// 查询到爸爸id为一级名录id的二级名录
			StringBuffer two = new StringBuffer(" from Wfpml where parentId=:firstId");
			paramMap.put("firstId", firstId);
			// 删除爸爸id为一级名录id的二级名录
			wfpmlDao.delete(two.toString(), paramMap);
			// 删除一级名录
			wfpmlDao.deleteById(firstId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delWfpml2(String secondId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 查询到父亲id为secondId的三级名录
		StringBuffer three = new StringBuffer(" from Wfpml wl where wl.parentId = :secondId");
		paramMap.put("secondId", secondId);
		try {
			// 删除父亲id为secondId的三级名录
			wfpmlDao.delete(three.toString(), paramMap);
			// 删除二级名录
			wfpmlDao.deleteById(secondId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delWfpml3(String threeId) {
		try {
			wfpmlDao.deleteById(threeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateWfpml1(Wfpml m) {
		if (m != null) {
			try {
				wfpmlDao.updateBean(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<Wfpml> getWfpmlList(String parentId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from Wfpml wl where 1=1");
		if (!StringUtil.isEmpty(parentId)) {
			hql.append(" and wl.parentId=:parentId");
			paramMap.put("parentId", parentId);
		} else {
			hql.append(" and (wl.parentId = 'null' or wl.parentId is null or wl.parentId = ' ' or parentId = null)");
		}
		hql.append(" order by wl.position asc");
		try {
			return wfpmlDao.list(hql.toString(), paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据末级名录ID查询一级名录以及末级名录集合
	 * 
	 * @param lastIds
	 * @return
	 */

	public List<Wfpml> getWfpmlListByLastIds(String[] lastIds) {
		if (lastIds == null || lastIds.length <= 0) {
			return null;
		}

		StringBuffer lastSql = new StringBuffer(" from Wfpml as wf where 1=1");
		lastSql.append(" and wf.id in (");

		for (int i = 0; i < lastIds.length; i++) {
			String id = lastIds[i];
			if (!StringUtil.isEmpty(id)) {
				lastSql.append("'").append(id).append("'");
				if (i + 1 < lastIds.length) {
					lastSql.append(",");
				}
			}
		}
		lastSql.append(")");
		lastSql.append(" order by wf.position asc");

		try {
			List<Wfpml> mlList = wfpmlDao.list(lastSql.toString(), null);
			if (mlList == null || mlList.size() <= 0) {
				return null;
			}

			Map<String, List<Wfpml>> map = new LinkedHashMap<String, List<Wfpml>>();
			for (Wfpml ml : mlList) {
				if (ml == null) {
					continue;
				}

				String firstId = "";
				try {
					String fullPath = ml.getFullPath();
					if (fullPath.indexOf("|") == -1) {
						continue;
					}
					String[] bb = fullPath.split("\\|");
					firstId = bb[1];
					if (StringUtil.isEmpty(firstId)) {
						continue;
					}
				} catch (Exception e) {
					continue;
				}
				List<Wfpml> wfml = null;
				if (map.containsKey(firstId)) {
					wfml = map.get(firstId);

				} else {
					wfml = new LinkedList<Wfpml>();
				}
				wfml.add(ml);
				map.put(firstId, wfml);
			}

			Object[] firstIds = map.keySet().toArray();

			StringBuffer sql = new StringBuffer(" from Wfpml as wf where 1=1");
			sql.append(" and wf.id in (");
			for (int i = 0; i < firstIds.length; i++) {
				String id = (String) firstIds[i];
				if (!StringUtil.isEmpty(id)) {
					sql.append("'").append(id).append("'");
					if (i + 1 < firstIds.length) {
						sql.append(",");
					}
				}
			}
			sql.append(")");
			sql.append(" order by wf.position asc");
			List<Wfpml> list = wfpmlDao.list(sql.toString(), null);
			if (list == null || list.size() <= 0) {
				return null;
			}

			List<Wfpml> returnList = new LinkedList<Wfpml>();
			for (Wfpml ml : list) {
				ml.setChilds(map.get(ml.getId()));
				returnList.add(ml);
			}
			return returnList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}
}
