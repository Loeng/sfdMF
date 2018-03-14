package com.ekfans.base.store.service;

import java.util.List;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreIntel;
import com.ekfans.pub.util.Pager;

/**
 * 资质认证Service接口
 * 
 * @ClassName: IStoreIntelService
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IStoreIntelService {

	/**
	 * （新增和修改）资质认证和资质文件
	 * 
	 * @param silist
	 * @param files
	 * @return 1:没有上传资质文件，2:成功，3:失败
	 */
	public int updateOrSaveIntel(List<StoreIntel> silist, List<String> files);
	
	/**
	 * 查询未认证资质的企业
	 * 
	 * @param pager 分页
	 * @param type 0:采购,1:销售,2:运输
	 * @param storeName 企业名称
	 */
	public List<Store> getUnauthenStoreList(Pager pager, String type, String storeName);
	
	/**
	 * 根据资质类型和企业id获取未认证的资质和对应的证明文件
	 * 
	 * @param storeId 企业id 
	 * @param type 0:采购,1:销售,2:运输
	 */
	public List<StoreIntel> getUnZiZhi(String storeId, String type);
	
	/**
	 * 审核资质操作
	 * 
	 * @param caoid 审核人id
	 * @param ids 资质集合
	 * @return true:成功，false:失败
	 */
	public boolean checkIntel(String caoid, String ids);
}