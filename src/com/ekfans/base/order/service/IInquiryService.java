package com.ekfans.base.order.service;

import java.util.List;

import org.hibernate.Session;

import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.product.model.SupplyProduct;
import com.ekfans.pub.util.Pager;

/**
 * 询价Service接口
 * 
 * @ClassName: IInquiryService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IInquiryService {

	/**
	 * 保存买家议价信息
	 * 
	 * @param inq
	 * @return true:成功，false:失败
	 */
	public Boolean saveBuyerInq(Inquiry inq);
	
	
	//核心企业查询供应商议价列表
	public List<Inquiry> getsupplyList(Pager pager, String proName, String userId);
	public Boolean saveInq(Inquiry inq,SupplyProduct supplyProduct);
	/**
	 * 
	 * @Title: getList
	 * @Description: TODO(供应议价列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @param proName
	 * @param minPrice
	 * @param maxPrice
	 * @return List<Inquiry> 返回类型
	 * @throws
	 */
	public List<Inquiry> getList(Pager pager, String proName, String minPrice, String maxPrice, String userId, int type);

	/**
	 * 根据id查看详情
	 */
	public Inquiry getIdByInquiry(String id);

	/**
	 * 根据id获得对象
	 */
	public Inquiry getById(String id);

	/**
	 * 修改
	 */
	public boolean updateInquiry(Inquiry i);
	
	/**
     * 修改
     */
    public boolean updateInquiry(Inquiry i,Session session);
	/**
	* @Title: getSupplyList
	* @Description: TODO(查询供应商的议价信息)
	* 详细业务流程:
	* (详细描述此方法相关的业务处理流程)
	* @param pager 分页
	* @param name 前台搜索商品名称
	* @param userid 卖家id
	* @return Inquiry 返回类型
	* @throws
	 */
    public List<Inquiry>  getSupplyList(Pager pager,String status ,String name,String userid);
    
    /**
    * @Title: getId
    * @Description: TODO根据ID获取议价信息
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param id
    * @return Inquiry 返回类型
    * @throws
     */
    public Inquiry getId(String id);
    
    /**
    * @Title: saveSupplyInq
    * @Description: TODO(保存采购商发起议价信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param inq
    * @return Boolean 返回类型
    * @throws
     */
    public Boolean saveSupplyInq(Inquiry inq);
    
    /**
     * 后台核心企业议价列表
     */
    public List<Inquiry> getSystemInquiryList(Pager pager,String status ,String name,String userId,String beginDate,String endDate);
    
    /**
     * 后台供应商议价列表
     */
    public List<Inquiry> getSystemGyInquiryList(Pager pager,String status ,String name,String userId,String beginDate,String endDate);
    
    
    /**
     * 企业中心询价列表
     */
    public List<Inquiry> getInquiryList(Pager pager , String userId, String status , String supplyType, String productType,String sourceType, String linkPerson,String storeName);
    /**
     * 后台获取询价列表
     */
    public List<Inquiry> getSystemInquiryList(Pager pager ,String StoreName, String status , String supplyType, String productType,String sourceType, String linkPerson);
}