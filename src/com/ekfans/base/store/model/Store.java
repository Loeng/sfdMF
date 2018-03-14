package com.ekfans.base.store.model;

import java.math.BigDecimal;
import java.util.List;

import com.ekfans.base.order.model.OrderDetail;
import com.ekfans.base.product.model.Product;
import com.ekfans.base.product.model.ProductCategory;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 企业基础信息--实体类
 * 
 * @ClassName: Store
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public class Store extends BasicBean {
	// ID头：避免ID与第三方冲突;
	public static final String SINGLE_MARK = "MS";

	private static final long serialVersionUID = 1L;
	// 企业角色id
	private String roleId = "0";
	// 企业名称
	private String storeName = "";
	// 企业LOGO
	private String storeLogo = "";
	// 营业执照文件
	private String businessLicense = "";
	// 企业简介
	private String notes = "";
	// 注册时间
	private String regTime = "";
	// 通讯地址
	private String mailingAddress = "";
	// 单位类型（1:个人独资企业，2:合伙企业）
	private Integer unitType = -1;
	// 邮政编码
	private Integer zipCode = 610000;
	// 占地面积(单位：平方米)
	private Double areaNumber = 0.00;
	// 联系人姓名
	private String contactName = "";
	// 联系人电话
	private String contactPhone = "";
	// 联系人传真
	private String contactFax = "";
	// 注册资金（单位：万元）
	private Double regCapital = 0.00;
	// 职工人数
	private Integer numberEmployees = 0;
	// 工商登记机关
	private String bureau = "";
	// 工商登记机关（登记时间）
	private String bureauTime = "";
	// 营业执照号
	private String businessLicenseNumber = "";
	// 开户银行
	private String bank = "";
	// 开户时间
	private String openingTime = "";
	// 贷款卡号
	private String reditCard = "";
	// 组织机构代码
	private String orgId = "";
	// 基础认证（ 0：未认证，1：认证中，2：认证失败，3：认证成功）
	private String commonStatus = "";
	// 银行认证（ 0：未认证，1：认证中，2：认证失败，3：认证成功）
	private String bankStatus = "";

	// 处置资质认证（ 0：未认证，1：认证中，2：认证失败，3：认证成功）
	private String buyerStatus = "";
	// 产生资质认证（ 0：未认证，1：认证中，2：认证失败，3：认证成功）
	private String salerStatus = "";
	// 运输资质认证（ 0：未认证，1：认证中，2：认证失败，3：认证成功）
	private String transStatus = "";
	// 企业官网
	private String domain = "";
	// 经营范围
	private String businessScope = "";
	// 未来事业 发展方向
	private String planning = "";
	// 创建时间
	private String createTime = "";
	// 修改时间
	private String updateTime = "";
	// 集团企业（false:不是，true:是）
	private Boolean storeGroup = false;
	// 区域id
	private String areaId = "";
	// 企业简称
	private String storeRefer = "";
	// 道路紧急预案ID
	private String emergencyPlanId = "";
	// 道路紧急预案附件
	private String emergencyPlanFile = "";
	// 是否已开通虚拟账户
	private Boolean accountStatus = false;
	// 长沙银行虚拟账户户号
	private String accountNo = "";
	// 支付密码
	private String payPassword = "";
	// 是否已签约（长沙银行）
	private Boolean accountSuccess = false;
	// 虚拟账户余额
	private BigDecimal account = new BigDecimal(0.00);
	// 客户签约日期
	private String accountDate = "";
	// 是否被第三方使用
	private boolean useData = false;
	// 机构信用代码证
	private String creditCodeCard = "";
	// 公司章程
	private String articles = "";
	// 公司简介（WORD版）
	private String synopsis = "";
	// 企业数据是否被同步到环保大数据平台  0:未同步   1:已同步 
	private String isDataSynchro;
	// ======================= 临时数据 =======================
	private String storeUserName = ""; // 用做显示的卖家名字
	private List<ProductCategory> productCategoryList = null; // 用做显示的商品分类
	private List<Product> productPictureList = null; // 用做显示的商品图片
	private List<OrderDetail> orderDetailList = null; // 需要靠订单的数量来排序
	private User userEntity; // 用户类
	private Integer autoNumber = 0; // 企业完成认证的数量
	private String areaName = "";// 区域名称
	private Integer userType = 0;
	// 临时数据 - 处置资质对象
	private Accredit buyerAccredit = null;
	// 临时数据 - 产生资质对象
	private Accredit salerAccredit = null;
	// 临时数据 - 运输资质对象
	private Accredit transAccredit = null;

	// =============== get set ===============

	public String getRoleId() {
		return roleId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}

	public Integer getZipCode() {
		return zipCode;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}

	public Double getAreaNumber() {
		return areaNumber;
	}

	public void setAreaNumber(Double areaNumber) {
		this.areaNumber = areaNumber;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}

	public Double getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(Double regCapital) {
		this.regCapital = regCapital;
	}

	public Integer getNumberEmployees() {
		return numberEmployees;
	}

	public void setNumberEmployees(Integer numberEmployees) {
		this.numberEmployees = numberEmployees;
	}

	public String getBureau() {
		return bureau;
	}

	public void setBureau(String bureau) {
		this.bureau = bureau;
	}

	public String getBureauTime() {
		return bureauTime;
	}

	public void setBureauTime(String bureauTime) {
		this.bureauTime = bureauTime;
	}

	public String getBusinessLicenseNumber() {
		return businessLicenseNumber;
	}

	public void setBusinessLicenseNumber(String businessLicenseNumber) {
		this.businessLicenseNumber = businessLicenseNumber;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}

	public String getReditCard() {
		return reditCard;
	}

	public void setReditCard(String reditCard) {
		this.reditCard = reditCard;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}

	public String getPlanning() {
		return planning;
	}

	public void setPlanning(String planning) {
		this.planning = planning;
	}

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

	public String getStoreUserName() {
		return storeUserName;
	}

	public void setStoreUserName(String storeUserName) {
		this.storeUserName = storeUserName;
	}

	public List<ProductCategory> getProductCategoryList() {
		return productCategoryList;
	}

	public void setProductCategoryList(List<ProductCategory> productCategoryList) {
		this.productCategoryList = productCategoryList;
	}

	public List<Product> getProductPictureList() {
		return productPictureList;
	}

	public void setProductPictureList(List<Product> productPictureList) {
		this.productPictureList = productPictureList;
	}

	public List<OrderDetail> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<OrderDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}

	public User getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(User userEntity) {
		this.userEntity = userEntity;
	}

	public Integer getAutoNumber() {
		return autoNumber;
	}

	public Boolean getStoreGroup() {
		return storeGroup;
	}

	public void setStoreGroup(Boolean storeGroup) {
		this.storeGroup = storeGroup;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setAutoNumber(Integer autoNumber) {
		this.autoNumber = autoNumber;
	}

	public String getStoreRefer() {
		return storeRefer;
	}

	public void setStoreRefer(String storeRefer) {
		this.storeRefer = storeRefer;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getEmergencyPlanId() {
		return emergencyPlanId;
	}

	public void setEmergencyPlanId(String emergencyPlanId) {
		this.emergencyPlanId = emergencyPlanId;
	}

	public String getEmergencyPlanFile() {
		return emergencyPlanFile;
	}

	public void setEmergencyPlanFile(String emergencyPlanFile) {
		this.emergencyPlanFile = emergencyPlanFile;
	}

	public String getCommonStatus() {
		return commonStatus;
	}

	public void setCommonStatus(String commonStatus) {
		this.commonStatus = commonStatus;
	}

	public String getBankStatus() {
		return bankStatus;
	}

	public void setBankStatus(String bankStatus) {
		this.bankStatus = bankStatus;
	}

	public String getBuyerStatus() {
		return buyerStatus;
	}

	public void setBuyerStatus(String buyerStatus) {
		this.buyerStatus = buyerStatus;
	}

	public String getSalerStatus() {
		return salerStatus;
	}

	public void setSalerStatus(String salerStatus) {
		this.salerStatus = salerStatus;
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}

	public Accredit getBuyerAccredit() {
		return buyerAccredit;
	}

	public void setBuyerAccredit(Accredit buyerAccredit) {
		this.buyerAccredit = buyerAccredit;
	}

	public Accredit getSalerAccredit() {
		return salerAccredit;
	}

	public void setSalerAccredit(Accredit salerAccredit) {
		this.salerAccredit = salerAccredit;
	}

	public Accredit getTransAccredit() {
		return transAccredit;
	}

	public void setTransAccredit(Accredit transAccredit) {
		this.transAccredit = transAccredit;
	}

	public Boolean getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Boolean accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public Boolean getAccountSuccess() {
		return accountSuccess;
	}

	public void setAccountSuccess(Boolean accountSuccess) {
		this.accountSuccess = accountSuccess;
	}

	public BigDecimal getAccount() {
		return account;
	}

	public void setAccount(BigDecimal account) {
		this.account = account;
	}

	public String getAccountDate() {
		return accountDate;
	}

	public void setAccountDate(String accountDate) {
		this.accountDate = accountDate;
	}

	public boolean isUseData() {
		return useData;
	}

	public void setUseData(boolean useData) {
		this.useData = useData;
	}

	public String getCreditCodeCard() {
		return creditCodeCard;
	}

	public void setCreditCodeCard(String creditCodeCard) {
		this.creditCodeCard = creditCodeCard;
	}

	public String getArticles() {
		return articles;
	}

	public void setArticles(String articles) {
		this.articles = articles;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getIsDataSynchro() {
		return isDataSynchro;
	}

	public void setIsDataSynchro(String isDataSynchro) {
		this.isDataSynchro = isDataSynchro;
	}
	

}