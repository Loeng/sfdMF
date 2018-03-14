package com.ekfans.base.content.service;

import java.util.List;

import com.ekfans.base.content.model.ShopAd;
import com.ekfans.pub.util.Pager;

/**
 * 广告实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author Lgy
 * @date 2014-1-3
 * @version 1.0
 */
public interface IShopAdService {
	/**
	 * 添加广告
	 * 
	 * @param ad
	 * @return
	 */
	public boolean addAdvert(ShopAd ad);

	/**
	 * 根据id查找广告
	 * 
	 * @param id
	 * @return
	 */
	public ShopAd getAdvertById(String id);

	/**
	 * 根据id查找广告名称
	 * 
	 * @param id
	 * @return
	 */
	public String getAdNameById(String id);

	/**
	 * 
	 * @Title: getAdvertIdByName
	 * @Description: TODO(根据广告名查询广告id) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param name
	 * @return ShopAd 返回类型
	 * @throws
	 */
	public String getAdvertIdByName(String name);

	/**
	 * 根据id删除广告
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteAdvert(String id);

	/**
	 * 修改广告
	 * 
	 * @param ad
	 */
	public boolean modifyAdvert(ShopAd ad);

	/**
	 * 查找广告列表
	 * 
	 * @param pager
	 *            分页
	 * @param name
	 *            广告名
	 * @return
	 */
	public List<ShopAd> listAdvert(Pager pager, String name, String type);

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
	public List<ShopAd> getShopAds(String adName, String type, String showType, Pager page);
	
	/**
	 * 物流宝配置广告
	 * @param shopAdid
	 * @return 1:成功
	 * @throws Exception 
	 */
	public String wlbConfigureAd(String shopAdid) throws Exception;
	
	/**
	 * 物流宝取消配置广告
	 * @param shopAdid
	 * @return
	 * @throws Exception
	 */
	public String wlbCancelConfigureAd(String shopAdid) throws Exception;
    
	/**
	 * 获取物流宝广告配置
	 * @return
	 * @throws Exception
	 */
    public ShopAd getWlbShopAd() throws Exception;
    
}
