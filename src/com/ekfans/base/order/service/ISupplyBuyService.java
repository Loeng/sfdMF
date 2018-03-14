package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.SupplyBuy;
import com.ekfans.base.product.model.Product;
import com.ekfans.pub.util.Pager;

import javax.servlet.http.HttpServletRequest;

/**
 * 供求关系实现Service接口
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2014-1-6
 * @version 1.0
 */
public interface ISupplyBuyService {

	/**
	 * 添加
	 */
	public boolean add(SupplyBuy sb);

	/**
	 * 删除
	 */
	public boolean remove(String id);

	/**
	 * 列表
	 */
	public List<SupplyBuy> listSupplyBuy(Pager pager, String title, String beginDate, String endDate, String storeId, String type, String status, String productType, String checkStatus,
			String checkType, String storeName);

	/**
	 * 详细
	 */
	public SupplyBuy getSupplyBuyById(String id);

	/**
	 * 修改
	 */
	public boolean update(SupplyBuy sb);

	/**
	 * 前台展现供求信息的列表
	 */
	public List<SupplyBuy> webListSupplyBuy(Pager pager, String checkStatus, String productType, String type, String status,String catgId);

	/**
	 * 获得供求展示信息
	 * 
	 * @param type 供求类型
	 * @return
	 */
	public List<SupplyBuy> webListSupplyShow(Pager pager, String type);
	
	/**
	 * 获得供求列表
	 * 
	 * @param type 供求类型
	 * @return
	 */
	public List<SupplyBuy> webListSupplyTable(Pager pager, String type ,String title);
	/**
	 * 
	 * @Title: 频道页获得供求列表
	 * @Description: TODO(listSupplyTable)
	 * 详细业务流程:
	 * (详细描述此方法相关的业务处理流程)
	 * @param 
	 * @return 
	 * @throws
	 */
	public List<SupplyBuy> listSupplyTable(Pager pager,String storeId, String title, String type, String productType, String destination, String categoryId, String lowPrice, String topPrice);
	
	/**
	 * @Title: App获取列表
	 * @param guoqi 是否过期 
	 * @return
	 */
	public List<SupplyBuy> listSupplyApp(Pager pager,String type, String storeId, String title, String categoryId, String destination, String checkStatus, boolean guoqi);
	/**
	 * 获取最新行情
	 */
	public List<SupplyBuy> getNewSupplyList(String productType,String type);
	
	/**
	 * 用于展示首页的供求信息
	 * 
	 * @param pager
	 * @param categoryId
	 * @return
	 */
	public List<SupplyBuy> getIndexSupplyBuy(Pager pager,String storeId, String title, String type, String productType, String destination, String categoryId, String lowPrice, String topPrice);

	/**
	 * 用于展示首页的供求信息
	 *
	 * @param pager
	 * @param categoryId
	 * @return
	 */
	public List<SupplyBuy> appSupplyShow(Pager pager, String type, String productType, String destination, String categoryId, HttpServletRequest request);

   /**
    * 后台查数量
    * @param productType(商品类型 0现货 1原料 2危废品) type(类型0供应，1求购)
    * @return
    */
	public int getSupplyBuyCheckNum(String productType,int type);
}