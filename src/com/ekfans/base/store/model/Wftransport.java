package com.ekfans.base.store.model;

import java.math.BigDecimal;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: Wftransport  
 * @Description: TODO(危废运输) 
 * @Copyright: Copyright (c) 2016年3月15日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年3月15日上午11:35:59
 * @version 1.0
 */
public class Wftransport extends BasicBean {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8190127877090118635L;
	// 企业ID
	private String storeId="";
	// 出发地地区id
	private String startPlace="";
	// 目的地地区id
	private String endPlace="";
	// 详细地址
	private String habitatAddress="";
	// 运价
	private BigDecimal price;
	// 类型 0:车源 1:货源
	private int type;
	// 运输名称
	private String name="";
	// 货源名称
	private String supplyName="";
	// 车载/货源总量
	private BigDecimal wfpTotal;
	// 创建时间
	private String createTime="";
	// 更新时间
	private String updateTime="";
	// 截止时间
	private String endTime="";
	// 状态 0:下架 1:上架
	private int status;
	// 审核时间
	private String checkTime = "";
	// 审核人
	private String checkMan = "";
	// 审核备注
	private String checkNote = "";
	// 审核状态 0--未审核 1--已审核 2--审核失败
	private int checkStatus = 0;
	// 联系人
	private String linkMan="";
	// 联系电话
	private String phone="";
	// 描述
	private String content="";
	// 起始地全路径
	private String startFullPath="";
	// 目的地全路径
	private String endFullPath="";
	// 企业名称
	private String storeName="";
	// 单位 默认为吨
	private String unit = "吨";

	// 车辆类型
	private String carName="";
	// 废物代码全称
	private String code = "";
	// 废物代码ID
	private String wfmlId = "";
	// 货物类别名称
	private String categoryName="";
	// 罐体材质
	private String tankMaterialName="";
	// 危废品运输界定父类ID
	private String wfpysParentId="";
	//危废品运输界定 下级id
	private String wfpysId;
	// 危废品运输界定名称
	private String wfpysName="";

	//***物流宝新增字段***
	//车辆尺寸单位 
    private String sizeUnit ="米";
	//车长
    private BigDecimal carLength;
    //车宽
    private BigDecimal carWidth;
    //车高
    private BigDecimal carHeight;
	//货物体积
    private BigDecimal cargoVolume;
	//有效时限
    private String validTime="";
    //车牌号
    private String carNumber="";
    //装货详细地址
    private String startHabitatAddress="";
    //卸货详细地址
    private String endHabitatAddress="";
    //议价信息条数
    private Integer barCount=0;
    //体积单位
    private String volumeUnit="m³";
    //用户是否被关注(1以被关注 0 未被关注)
    private String isAttention="0";

	// ***app field***
	// 收藏状态 0未收藏 1收藏了
	private String collectStatus = "0";
	// 环信用户名
	private String hxUserName = "";
	// 好友关系 0不是好友 1好友 2好友的好友
	private String friendStatus = "0";

	public String getHxUserName() {
		return hxUserName;
	}

	public void setHxUserName(String hxUserName) {
		this.hxUserName = hxUserName;
	}

	public String getFriendStatus() {
		return friendStatus;
	}

	public void setFriendStatus(String friendStatus) {
		this.friendStatus = friendStatus;
	}

	public String getCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(String collectStatus) {
		this.collectStatus = collectStatus;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getWfpTotal() {
		return wfpTotal;
	}

	public void setWfpTotal(BigDecimal wfpTotal) {
		this.wfpTotal = wfpTotal;
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

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckMan() {
		return checkMan;
	}

	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote;
	}

	public int getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public String getEndPlace() {
		return endPlace;
	}

	public void setEndPlace(String endPlace) {
		this.endPlace = endPlace;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHabitatAddress() {
		return habitatAddress;
	}

	public void setHabitatAddress(String habitatAddress) {
		this.habitatAddress = habitatAddress;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStartFullPath() {
		return startFullPath;
	}

	public void setStartFullPath(String startFullPath) {
		this.startFullPath = startFullPath;
	}

	public String getEndFullPath() {
		return endFullPath;
	}

	public void setEndFullPath(String endFullPath) {
		this.endFullPath = endFullPath;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWfmlId() {
		return wfmlId;
	}

	public void setWfmlId(String wfmlId) {
		this.wfmlId = wfmlId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTankMaterialName() {
		return tankMaterialName;
	}

	public void setTankMaterialName(String tankMaterialName) {
		this.tankMaterialName = tankMaterialName;
	}
    public String getWfpysName() {
		return wfpysName;
	}

	public void setWfpysName(String wfpysName) {
		this.wfpysName = wfpysName;
	}

	public String getWfpysParentId() {
		return wfpysParentId;
	}

	public void setWfpysParentId(String wfpysParentId) {
		this.wfpysParentId = wfpysParentId;
	}
    public BigDecimal getCargoVolume() {
		return cargoVolume;
	}

	public void setCargoVolume(BigDecimal cargoVolume) {
		this.cargoVolume = cargoVolume;
	}

	public String getValidTime() {
		return validTime;
	}

	public void setValidTime(String validTime) {
		this.validTime = validTime;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}


	public String getStartHabitatAddress() {
		return startHabitatAddress;
	}

	public void setStartHabitatAddress(String startHabitatAddress) {
		this.startHabitatAddress = startHabitatAddress;
	}

	public String getEndHabitatAddress() {
		return endHabitatAddress;
	}

	public void setEndHabitatAddress(String endHabitatAddress) {
		this.endHabitatAddress = endHabitatAddress;
	}
    public Integer getBarCount() {
		return barCount;
	}

	public void setBarCount(Integer barCount) {
		this.barCount = barCount;
	}

	public String getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}
    public BigDecimal getCarLength() {
		return carLength;
	}

	public void setCarLength(BigDecimal carLength) {
		this.carLength = carLength;
	}

	public BigDecimal getCarWidth() {
		return carWidth;
	}

	public void setCarWidth(BigDecimal carWidth) {
		this.carWidth = carWidth;
	}

	public BigDecimal getCarHeight() {
		return carHeight;
	}

	public void setCarHeight(BigDecimal carHeight) {
		this.carHeight = carHeight;
	}

	public String getSizeUnit() {
		return sizeUnit;
	}
	

	public String getWfpysId() {
		return wfpysId;
	}

	public void setWfpysId(String wfpysId) {
		this.wfpysId = wfpysId;
	}

	public void setSizeUnit(String sizeUnit) {
		this.sizeUnit = sizeUnit;
	}
	public String getIsAttention() {
		return isAttention;
	}

	public void setIsAttention(String isAttention) {
		this.isAttention = isAttention;
	}
	
}
