package com.ekfans.base.product.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ekfans.base.store.model.Store;
import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 商品--实体类
 * 
 * @ClassName: Product
 * @Description:
 * @author ekfans
 * @date 2014-6-25 下午10:38:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Product extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	// 商品名称
	private String name = "";
	// 商品名称缩写
	private String sortName = "";
	// 店铺ID -- 对应店铺表(Store)主键
	private String storeId = "";
	// 商城价
	private BigDecimal unitPrice = new BigDecimal("0.00");
	// 市场价
	private BigDecimal listPrice = new BigDecimal("0.00");
	// 批发价
	private BigDecimal pfPrice = new BigDecimal("0.00");
	// 数量单位
	private String unit = "";
	// 库存数量
	private int quantity = 0;
	// 库存预警数量
	private int quantityWarning = 0;
	// 所属品牌
	private String brand = "";
	// 商品原图
	private String picture = "";
	// 商品大图
	private String bigPicture = "";
	// 商品中图
	private String centerPicture = "";
	// 商品小图
	private String smallPicture = "";
	// 商品描述
	private String description = "";
	// 商品备注
	private String note = "";
	// 商品类型(1:优选商城，2：大宗采购)
	private String type = "";
	// 商品模版ID -- 对应商品模版表(PRODUCT_TEMPLATE)主键
	private String templateId = "";
	// 商品状态
	private boolean status = false;
	// 审核状态
	private int checkStatus = 0;
	// 审核人
	private String checkMan = "";
	// 审核时间
	private String checkTime = "";
	// 审核说明
	private String checkNote = "";
	// 搜索关键字
	private String searchKey = "";
	// 商品查看次数
	private int visitCount = 0;
	// 商品购买次数
	private int buyCount = 0;
	// 商品主分类
	private String mainCategory = "";
	// 商品产地
	private String habitat = "";
	// 商品产地详细地址
	private String habitatAddress = "";
	// 商品编号
	private String productNumber = "";
	// 所属仓库ID
	private String wareHouseId = "";
	// 推荐图片1
	private String recommendPicture1 = "";
	// 推荐图片2
	private String recommendPicture2 = "";
	// 推荐图片3
	private String recommendPicture3 = "";
	// 推荐图片4
	private String recommendPicture4 = "";
	// 推荐图片5
	private String recommendPicture5 = "";
	// 商品邮费
	private BigDecimal fare = new BigDecimal("0.00");
	// 检测机构名称
	private String jcjg = "";
	// 检测报告
	private String jcFile = "";
	// 成交量
	private int cjl = 0;
	// 质量等级
	private String productModel = "";
	// 交货地址
	private String deliceAddress = "";
	// 交货地址ID
	private String deliceAddressId = "";
	// 交货方式
	private String deliceType = "";
	// 交货日
	private String deliceData = "";
	// 是否支持预定(0:否：1：支持)
	private String isAdvance = "";
	// 预定单价
	private BigDecimal advancePrice = new BigDecimal("0.00");
	// 起订数量
	private int advanceNum = 1;
	// 付款方式
	private String payType = "";
	// 商品关联大区
	private String productAreaId = "";
	// 是否选择计价单计价
	private boolean productJjdStatus = false;
	// 商品扩展字段集合
	private List<ProductInfo> infoList = null;
	// 商品扩展字段详情集合
	private List<ProductInfoDetail> infoDetailList = null;

	// 商品价格增减记录
	private double riseDrop = 0.00;
	// =================== 临时数据 ================
	// 商品总价【购买数量*商城价格】
	private BigDecimal tempProductTotalPrice = new BigDecimal("0.00");

	// 作为绿色商品时商品主分类ID（eg：微晶板材 设备器材 车辆租售 周边产品）
	private String rootCatgId;

	// 店铺名称
	private String storeName = "";
	// 企业简称
	private String storeRefer = "";
	// 店铺所在省
	private String province = "";

	// 城市
	private String area = "";

	// 品牌名称
	private String brandName = "";

	// 模板名称
	private String templateName = "";

	// 扩展字段名称
	private String fieldName = "";

	// 扩展字段值
	private String fieldValue = "";

	// 分类名称
	private String categoryName = "";

	// 商品的短路径 - 临时字段，不操作数据库
	private String linkUrl = "";

	// 商品联系人
	private String linkPerson = "";
	private String linkTel = "";
	// 该商品是否已回复议价
	private int isInquiry = 0;
	private Store store; // 企业
	private List<ProductValuation> productValuation = new ArrayList<ProductValuation>();// 计价

	// 创建时间
	private String createTime;
	// 更新时间
	private String updateTime;
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getStoreRefer() {
		return storeRefer;
	}

	public void setStoreRefer(String storeRefer) {
		this.storeRefer = storeRefer;
	}

	public int getIsInquiry() {
		return isInquiry;
	}

	public String getRootCatgId() {
		return rootCatgId;
	}

	public void setRootCatgId(String rootCatgId) {
		this.rootCatgId = rootCatgId;
	}

	public void setIsInquiry(int isInquiry) {
		this.isInquiry = isInquiry;
	}

	public String getLinkPerson() {
		return linkPerson;
	}

	public void setLinkPerson(String linkPerson) {
		this.linkPerson = linkPerson;
	}

	public String getLinkTel() {
		return linkTel;
	}

	public void setLinkTel(String linkTel) {
		this.linkTel = linkTel;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public boolean getProductJjdStatus() {
		return productJjdStatus;
	}

	public void setProductJjdStatus(boolean productJjdStatus) {
		this.productJjdStatus = productJjdStatus;
	}

	private List<ProductInfoDetail> productInfoDetails = new ArrayList<ProductInfoDetail>();

	public BigDecimal getPfPrice() {
		return pfPrice;
	}

	public void setPfPrice(BigDecimal pfPrice) {
		this.pfPrice = pfPrice;
	}

	public String getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getRecommendPicture1() {
		return recommendPicture1;
	}

	public void setRecommendPicture1(String recommendPicture1) {
		this.recommendPicture1 = recommendPicture1;
	}

	public String getRecommendPicture2() {
		return recommendPicture2;
	}

	public void setRecommendPicture2(String recommendPicture2) {
		this.recommendPicture2 = recommendPicture2;
	}

	public String getRecommendPicture3() {
		return recommendPicture3;
	}

	public void setRecommendPicture3(String recommendPicture3) {
		this.recommendPicture3 = recommendPicture3;
	}

	public String getRecommendPicture4() {
		return recommendPicture4;
	}

	public void setRecommendPicture4(String recommendPicture4) {
		this.recommendPicture4 = recommendPicture4;
	}

	public String getRecommendPicture5() {
		return recommendPicture5;
	}

	public void setRecommendPicture5(String recommendPicture5) {
		this.recommendPicture5 = recommendPicture5;
	}

	public List<ProductInfoDetail> getProductInfoDetails() {
		return productInfoDetails;
	}

	public void setProductInfoDetails(List<ProductInfoDetail> productInfoDetails) {
		this.productInfoDetails = productInfoDetails;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getBigPicture() {
		return bigPicture;
	}

	public void setBigPicture(String bigPicture) {
		this.bigPicture = bigPicture;
	}

	public String getCenterPicture() {
		return centerPicture;
	}

	public void setCenterPicture(String centerPicture) {
		this.centerPicture = centerPicture;
	}

	public String getSmallPicture() {
		return smallPicture;
	}

	public void setSmallPicture(String smallPicture) {
		this.smallPicture = smallPicture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public int getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(int visitCount) {
		this.visitCount = visitCount;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public String getMainCategory() {
		return mainCategory;
	}

	public void setMainCategory(String mainCategory) {
		this.mainCategory = mainCategory;
	}

	public String getProductNumber() {
		return productNumber;
	}

	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}

	public List<ProductInfo> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<ProductInfo> infoList) {
		this.infoList = infoList;
	}

	public List<ProductInfoDetail> getInfoDetailList() {
		return infoDetailList;
	}

	public void setInfoDetailList(List<ProductInfoDetail> infoDetailList) {
		this.infoDetailList = infoDetailList;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getQuantityWarning() {
		return quantityWarning;
	}

	public void setQuantityWarning(int quantityWarning) {
		this.quantityWarning = quantityWarning;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getListPrice() {
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public BigDecimal getTempProductTotalPrice() {
		return tempProductTotalPrice;
	}

	public void setTempProductTotalPrice(BigDecimal tempProductTotalPrice) {
		this.tempProductTotalPrice = tempProductTotalPrice;
	}

	public String getHabitat() {
		return habitat;
	}

	public void setHabitat(String habitat) {
		this.habitat = habitat;
	}

	public String getHabitatAddress() {
		return habitatAddress;
	}

	public void setHabitatAddress(String habitatAddress) {
		this.habitatAddress = habitatAddress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJcjg() {
		return jcjg;
	}

	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}

	public String getJcFile() {
		return jcFile;
	}

	public void setJcFile(String jcFile) {
		this.jcFile = jcFile;
	}

	public int getCjl() {
		return cjl;
	}

	public void setCjl(int cjl) {
		this.cjl = cjl;
	}

	public String getProductModel() {
		return productModel;
	}

	public void setProductModel(String productModel) {
		this.productModel = productModel;
	}

	public String getDeliceAddress() {
		return deliceAddress;
	}

	public void setDeliceAddress(String deliceAddress) {
		this.deliceAddress = deliceAddress;
	}

	public String getDeliceAddressId() {
		return deliceAddressId;
	}

	public void setDeliceAddressId(String deliceAddressId) {
		this.deliceAddressId = deliceAddressId;
	}
	
	public String getDeliceType() {
		return deliceType;
	}

	public void setDeliceType(String deliceType) {
		this.deliceType = deliceType;
	}

	public String getDeliceData() {
		return deliceData;
	}

	public void setDeliceData(String deliceData) {
		this.deliceData = deliceData;
	}

	public String getIsAdvance() {
		return isAdvance;
	}

	public void setIsAdvance(String isAdvance) {
		this.isAdvance = isAdvance;
	}

	public BigDecimal getAdvancePrice() {
		return advancePrice;
	}

	public void setAdvancePrice(BigDecimal advancePrice) {
		this.advancePrice = advancePrice;
	}

	public int getAdvanceNum() {
		return advanceNum;
	}

	public void setAdvanceNum(int advanceNum) {
		this.advanceNum = advanceNum;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getProductAreaId() {
		return productAreaId;
	}

	public void setProductAreaId(String productAreaId) {
		this.productAreaId = productAreaId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<ProductValuation> getProductValuation() {
		return productValuation;
	}

	public void setProductValuation(List<ProductValuation> productValuation) {
		this.productValuation = productValuation;
	}

	public double getRiseDrop() {
		return riseDrop;
	}

	public void setRiseDrop(double riseDrop) {
		this.riseDrop = riseDrop;
	}

}