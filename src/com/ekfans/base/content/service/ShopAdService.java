package com.ekfans.base.content.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.content.dao.IAdDetailDao;
import com.ekfans.base.content.dao.IShopAdDao;
import com.ekfans.base.content.model.AdDetail;
import com.ekfans.base.content.model.ShopAd;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 广告业务实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
@Service("shopAdServiceImpl")
public class ShopAdService implements IShopAdService {
	// 定义DAO
	@Autowired
	private IShopAdDao shopAdDao;
	@Autowired
	private IAdDetailDao adDetailDao;
	/**
	 * 添加广告
	 */
	public boolean addAdvert(ShopAd ad) {
		if (ad == null) {
			return false;
		}
		try {
			// 设置创建时间为当前系统时间
			ad.setCreateTime(DateUtil.getSysDateTimeString());
			// 调用DAO方法添加广告
			shopAdDao.addBean(ad);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据id查找广告
	 */
	public ShopAd getAdvertById(String id) {
		if (id == null) {
			return null;
		}
		try {
			// 调用DAO方法查找广告
			return (ShopAd) shopAdDao.get(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据id查找广告名称
	 * 
	 * @param id
	 * @return
	 */
	public String getAdNameById(String id) {
		// 如果传过来的广告ID为空，则返回null;
		if (StringUtil.isEmpty(id)) {
			return null;
		}

		// 定义参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer("select ad.name from ShopAd as ad where 1=1");
		sql.append(" and ad.id = :adId");
		paramMap.put("adId", id);
		try {
			List list = shopAdDao.list(sql.toString(), paramMap);
			if (list != null && list.size() > 0) {
				return (String) list.get(0);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	/**
	 * 根据id删除广告
	 */
	public boolean deleteAdvert(String id) {
		if (id == null) {
			return false;
		}
		try {
			// 调用DAO方法查找广告
			shopAdDao.deleteById(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 根据id修改广告
	 */
	public boolean modifyAdvert(ShopAd ad) {
		try {
			// 调用DAO方法修改广告
			shopAdDao.updateBean(ad);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查找广告列表
	 */
	public List<ShopAd> listAdvert(Pager pager, String name, String type) {
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select ad from ShopAd as ad where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 如果查询条件输入了name，添加查询条件
		if (!StringUtil.isEmpty(name)) {
			sql.append(" and ad.name   like :name");
			paramMap.put("name", "%" + name + "%");
		}
		if(!StringUtil.isEmpty(type)){
		    sql.append(" and ad.type=:type");
		    paramMap.put("type", type);
		}
		sql.append(" order by ad.createTime desc");
		try {
			// 调用DAO方法查找广告
			List<ShopAd> list = shopAdDao.list(pager, sql.toString(), paramMap);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getAdvertIdByName(String name) {
		if (StringUtil.isEmpty(name)) {
			return null;
		}
		// 定义查询SQL
		StringBuffer sql = new StringBuffer(" select ad.id from ShopAd as ad where 1=1");
		// 定义参数MAP
		Map<String, Object> paramMap = new HashMap<String, Object>();

		// 关联广告名
		sql.append(" and ad.name = :name");
		paramMap.put("name", name);

		try {
			// 调用DAO方法查找广告
			List list = shopAdDao.list(sql.toString(), paramMap);
			return list.get(0).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据条件查询广告
	 * 
	 * @Title: getShopAds
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param @param adName
	 * @param @param type
	 * @param @param showType
	 * @param @param page
	 * @param @return 设定文件
	 * @return List<ShopAd> 返回类型
	 * @throws
	 */
	public List<ShopAd> getShopAds(String adName, String type, String showType, Pager page) {
		// 定义SQL参数Map
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// 定义SQL
		StringBuffer sql = new StringBuffer("select ad.id,ad.name,ad.showType from ShopAd as ad where 1=1");

		// 关联广告名称查询条件
		if (!StringUtil.isEmpty(adName)) {
			sql.append(" and ad.name like :adName");
			paramMap.put("adName", "%" + adName + "%");
		}

		// 关联广告类型
		if (!StringUtil.isEmpty(type)) {
			sql.append(" and ad.type = :type");
			paramMap.put("type", type);
		}

		// 关联显示类型
		if (!StringUtil.isEmpty(showType)) {
			sql.append(" and ad.showType = :showType");
			paramMap.put("showType", showType);
		}

		// 按照创建时间倒序排序
		sql.append(" order by ad.createTime desc");

		try {
			// 调用DAO查询SQL，如果传进来的分页参数不为空，则使用带分页的查询，否则，不适用带分页的查询
			List list = null;
			if (page != null) {
				list = shopAdDao.list(page, sql.toString(), paramMap);
			} else {
				list = shopAdDao.list(sql.toString(), paramMap);
			}

			// 定义返回的集合
			List<ShopAd> adList = new ArrayList<ShopAd>();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Object[] obj = (Object[]) list.get(i);
					if (obj != null && obj.length > 0) {
						ShopAd ad = new ShopAd();
						ad.setId((String) obj[0]);
						ad.setName((String) obj[1]);
						ad.setShowType((String) obj[2]);
						adList.add(ad);
					}
				}
			}

			return adList;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

	@Override
	public String wlbConfigureAd(String shopAdid) throws Exception {
		List<ShopAd> ads = shopAdDao.getConfiguredList();
		if(ads!=null){
			for(ShopAd s : ads){
				s.setIsConfigure("0");
				shopAdDao.updateBean(s);
			}
		}
		ShopAd ad = (ShopAd) shopAdDao.get(shopAdid);
		if(ad==null){
			return "2";
		}
		if(!ad.getType().equals("1")){
			return "3";
		}
		ad.setIsConfigure("1");
		shopAdDao.updateBean(ad);
		return "1";
	}

	@Override
	public String wlbCancelConfigureAd(String shopAdid) throws Exception {
		ShopAd ad = (ShopAd) shopAdDao.get(shopAdid);
		if(ad==null){
			return "2";
		}
		ad.setIsConfigure("0");
		shopAdDao.updateBean(ad);
		return "1";
	}

	@Override
	public ShopAd getWlbShopAd() throws Exception {
		List<ShopAd> list = shopAdDao.getConfiguredList();
		if(list==null||list.size()==0) {
			return null;
		}
		ShopAd ads = list.get(0);
		List<AdDetail> listAdDetail= adDetailDao.getListAdDetail(ads.getId());
		ads.setDetailist(listAdDetail);
		return ads;
	}
}
