package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import com.ekfans.base.order.dao.IInquiryDao;
import com.ekfans.base.order.model.Inquiry;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.BaiduMapUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

public class HuoResponse {
        //货源id
	    private String id="";
	    // 企业ID
		private String storeId="";
		//商品距离
		private String commodityDistance="";
		//离我距离
		private String distanceMe="";
		// 出发地省级id
		private String startProvinceId="";
		// 目的地省级id
		private String endProvinceId="";
		// 出发地市级id
		private String startCityId="";
		// 目的地市级id
		private String endCityId="";
		// 出发地区级id
		private String startAreaId="";
		// 目的地区级id
		private String endAreaId="";
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
		private String[] startList;
		// 目的地全路径
		private String[] endList;
		// 企业名称
		private String storeName="";
		// 单位 默认为吨
		private String unit = "";
		// 车辆类型
		private String carName="";
		// 货物类别名称
		private String categoryName="";
		// 罐体材质
		private String tankMaterialName="";
		// 危废品运输界定名称
		private String[] wfpysList;
		// 危废品运输界定父类ID
	 	private String[] wfpysParentId;
		//车长
		private BigDecimal carLength;
		//车辆尺寸单位 
	    private String sizeUnit ="米";
		//货物体积
	    private BigDecimal cargoVolume;
	   //货物体积单位
	    private String volumeUnit="";
		//有效时限
	    private String validTime="";
	    //装货详细地址
	    private String startHabitatAddress="";
	    //卸货详细地址
	    private String endHabitatAddress="";
	    private String responseStatus="";
	    //当前用户议价状态(1:已议价,2:未议价)
	    private String yJStatus="";
	    //议价信息条数
	    private Integer barCount=0;
	    //用户是否被关注(1以被关注 0 未被关注)
	    private String isAttention="";
	    public HuoResponse() {
			super();
		}
        public HuoResponse(Wftransport w){
			this.setId(w.getId());
			this.setStoreId(w.getStoreId());
			this.setName(w.getName());
			this.setSupplyName(w.getSupplyName());
			this.setWfpTotal(w.getWfpTotal());
			this.setCreateTime(w.getCreateTime());
			this.setUpdateTime(w.getUpdateTime());
			this.setEndTime(w.getEndTime());
			this.setStatus(w.getStatus());
			this.setCheckTime(w.getCheckTime());
			this.setCheckMan(w.getCheckMan());
			this.setCheckNote(w.getCheckNote());
			this.setCheckStatus(w.getCheckStatus());
			this.setLinkMan(w.getLinkMan());
			this.setPhone(w.getPhone());
			this.setContent(w.getContent());
			this.setStartList(w.getStartFullPath().split(","));
			this.setEndList(w.getEndFullPath().split(","));
			this.setStoreName(w.getStoreName());
			this.setUnit(w.getUnit());
			this.setCarName(w.getCarName());
			this.setCategoryName(w.getCategoryName());
			this.setTankMaterialName(w.getTankMaterialName());
			this.setWfpysList(w.getWfpysName().split("\\|"));
			this.setCarLength(w.getCarLength());
			this.setSizeUnit(w.getSizeUnit());
			this.setCargoVolume(w.getCargoVolume());
			this.setVolumeUnit(w.getVolumeUnit());
			this.setValidTime(w.getValidTime());
			this.setStartHabitatAddress(w.getStartHabitatAddress());
			this.setEndHabitatAddress(w.getEndHabitatAddress());
			//设置省市区id
			listToString(w.getEndPlace().split("\\|"),w.getStartPlace().split("\\|"));
			this.setWfpysParentId(w.getWfpysParentId().split(","));
			this.setBarCount(w.getBarCount());
			this.setIsAttention(w.getIsAttention());
		}
		
		public HuoResponse(Wftransport w,Pager p){
			this.setId(w.getId());
			this.setStoreId(w.getStoreId());
			this.setName(w.getName());
			this.setSupplyName(w.getSupplyName());
			this.setWfpTotal(w.getWfpTotal());
			this.setCreateTime(w.getCreateTime());
			this.setUpdateTime(w.getUpdateTime());
			this.setEndTime(w.getEndTime());
			this.setStatus(w.getStatus());
			this.setCheckTime(w.getCheckTime());
			this.setCheckMan(w.getCheckMan());
			this.setCheckNote(w.getCheckNote());
			this.setCheckStatus(w.getCheckStatus());
			this.setLinkMan(w.getLinkMan());
			this.setPhone(w.getPhone());
			this.setContent(w.getContent());
			this.setStartList(w.getStartFullPath().split(","));
			this.setEndList(w.getEndFullPath().split(","));
			this.setStoreName(w.getStoreName());
			this.setUnit(w.getUnit());
			this.setCarName(w.getCarName());
			this.setCategoryName(w.getCategoryName());
			this.setTankMaterialName(w.getTankMaterialName());
			this.setWfpysList(w.getWfpysName().split("\\|"));
			this.setCarLength(w.getCarLength());
			this.setCargoVolume(w.getCargoVolume());
			this.setVolumeUnit(w.getVolumeUnit());
			this.setValidTime(w.getValidTime());
			this.setStartHabitatAddress(w.getStartHabitatAddress());
			this.setEndHabitatAddress(w.getEndHabitatAddress());
			//设置省市区id
			listToString(w.getEndPlace().split("\\|"),w.getStartPlace().split("\\|"));
			this.setWfpysParentId(w.getWfpysParentId().split(","));
			this.setBarCount(w.getBarCount());
		}
		
		public HuoResponse(Wftransport w,HuoListForm hf,HttpServletRequest request) throws Exception{
			this.setId(w.getId());
			this.setStoreId(w.getStoreId());
			this.setName(w.getName());
			this.setSupplyName(w.getSupplyName());
			this.setWfpTotal(w.getWfpTotal());
			this.setCreateTime(w.getCreateTime());
			this.setUpdateTime(w.getUpdateTime());
			this.setEndTime(w.getEndTime());
			this.setStatus(w.getStatus());
			this.setCheckTime(w.getCheckTime());
			this.setCheckMan(w.getCheckMan());
			this.setCheckNote(w.getCheckNote());
			this.setCheckStatus(w.getCheckStatus());
			this.setLinkMan(w.getLinkMan());
			this.setPhone(w.getPhone());
			this.setContent(w.getContent());
			this.setStartList(w.getStartFullPath().split(","));
			this.setEndList(w.getEndFullPath().split(","));
			this.setStoreName(w.getStoreName());
			this.setUnit(w.getUnit());
			this.setCarName(w.getCarName());
			this.setCategoryName(w.getCategoryName());
			this.setTankMaterialName(w.getTankMaterialName());
			this.setWfpysList(w.getWfpysName().split("\\|"));
			this.setCarLength(w.getCarLength());
			this.setSizeUnit(w.getSizeUnit());
			this.setCargoVolume(w.getCargoVolume());
			this.setVolumeUnit(w.getVolumeUnit());
			this.setValidTime(w.getValidTime());
			this.setStartHabitatAddress(w.getStartHabitatAddress());
			this.setEndHabitatAddress(w.getEndHabitatAddress());
			//设置省市区id
			listToString(w.getEndPlace().split("\\|"),w.getStartPlace().split("\\|"));
			this.setWfpysParentId(w.getWfpysParentId().split(","));
			this.setBarCount(w.getBarCount());
			String token = request.getHeader("WLB-Token");
			if(!StringUtil.isEmpty(token)){
			 this.setIsAttention(w.getIsAttention());
			}else{
				this.setIsAttention("0");
			}
			if(StringUtil.isEmpty(hf.userId)){
				 this.yJStatus="2";
			 }else{
				 IInquiryDao dao = SpringContextHolder.getBean(IInquiryDao.class);
				 List<Inquiry> b = (List<Inquiry>) dao.getBargain(hf.userId, null, w.getId(), null);
				 if(b==null||b.size()==0){
					 this.yJStatus="2";
				 }else{
				 this.yJStatus="1";
				 }
			 }
	        try {
				this.setCommodityDistance(BaiduMapUtil.getCommodityDistance(w.getStartFullPath(), w.getEndFullPath())+"");
	            if(hf!=null&&!StringUtil.isEmpty(hf.latitude)&&!StringUtil.isEmpty(hf.longitude)){
	            this.setDistanceMe(BaiduMapUtil.getDistanceMe(w.getStartFullPath(), hf.latitude, hf.longitude)+"");
	            }
						
	        } catch (UnsupportedEncodingException | JSONException e) {
				e.printStackTrace();
			}   	
		}
		
		public HuoResponse(Wftransport w,String responseStatus){
			this.setId(w.getId());
			this.setStoreId(w.getStoreId());
			this.setName(w.getName());
			this.setSupplyName(w.getSupplyName());
			this.setWfpTotal(w.getWfpTotal());
			this.setCreateTime(w.getCreateTime());
			this.setUpdateTime(w.getUpdateTime());
			this.setEndTime(w.getEndTime());
			this.setStatus(w.getStatus());
			this.setCheckTime(w.getCheckTime());
			this.setCheckMan(w.getCheckMan());
			this.setCheckNote(w.getCheckNote());
			this.setCheckStatus(w.getCheckStatus());
			this.setLinkMan(w.getLinkMan());
			this.setPhone(w.getPhone());
			this.setContent(w.getContent());
			this.setStartList(w.getStartFullPath().split(","));
			this.setEndList(w.getEndFullPath().split(","));
			this.setStoreName(w.getStoreName());
			this.setUnit(w.getUnit());
			this.setCarName(w.getCarName());
			this.setCategoryName(w.getCategoryName());
			this.setTankMaterialName(w.getTankMaterialName());
			this.setWfpysList(w.getWfpysName().split("\\|"));
			this.setCarLength(w.getCarLength());
			this.setSizeUnit(w.getSizeUnit());
			this.setCargoVolume(w.getCargoVolume());
			this.setVolumeUnit(w.getVolumeUnit());
			this.setValidTime(w.getValidTime());
			this.setStartHabitatAddress(w.getStartHabitatAddress());
			this.setEndHabitatAddress(w.getEndHabitatAddress());
			//设置省市区id
			listToString(w.getEndPlace().split("\\|"),w.getStartPlace().split("\\|"));
			this.setWfpysParentId(w.getWfpysParentId().split(","));
			this.setResponseStatus(responseStatus);
			this.setBarCount(w.getBarCount());
		}
       
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getStoreId() {
			return storeId;
		}

		public void setStoreId(String storeId) {
			this.storeId = storeId;
		}



		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSupplyName() {
			return supplyName;
		}

		public void setSupplyName(String supplyName) {
			this.supplyName = supplyName;
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

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
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

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
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

		public String getCarName() {
			return carName;
		}
        public void setCarName(String carName) {
			this.carName = carName;
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
        public BigDecimal getCarLength() {
			return carLength;
		}
        public void setCarLength(BigDecimal carLength) {
			this.carLength = carLength;
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
        public String getStartProvinceId() {
			return startProvinceId;
		}
        public void setStartProvinceId(String startProvinceId) {
			this.startProvinceId = startProvinceId;
		}

		public String getEndProvinceId() {
			return endProvinceId;
		}

		public void setEndProvinceId(String endProvinceId) {
			this.endProvinceId = endProvinceId;
		}

		public String getStartCityId() {
			return startCityId;
		}

		public void setStartCityId(String startCityId) {
			this.startCityId = startCityId;
		}

		public String getEndCityId() {
			return endCityId;
		}

		public void setEndCityId(String endCityId) {
			this.endCityId = endCityId;
		}

		public String getStartAreaId() {
			return startAreaId;
		}

		public void setStartAreaId(String startAreaId) {
			this.startAreaId = startAreaId;
		}

		public String getEndAreaId() {
			return endAreaId;
		}
        public void setEndAreaId(String endAreaId) {
			this.endAreaId = endAreaId;
		}
        public String getCommodityDistance() {
			return commodityDistance;
		}
        public void setCommodityDistance(String commodityDistance) {
			this.commodityDistance = commodityDistance;
		}
        public String getDistanceMe() {
			return distanceMe;
		}
        public void setDistanceMe(String distanceMe) {
			this.distanceMe = distanceMe;
		}
        public String getResponseStatus() {
			return responseStatus;
		}
        public void setResponseStatus(String responseStatus) {
			this.responseStatus = responseStatus;
		}
        public Integer getBarCount() {
			return barCount;
		}
        public void setBarCount(Integer barCount) {
			this.barCount = barCount;
		}
        public String getyJStatus() {
			return yJStatus;
		}
        public void setyJStatus(String yJStatus) {
			this.yJStatus = yJStatus;
		}
        public String getVolumeUnit() {
			return volumeUnit;
		}
        public void setVolumeUnit(String volumeUnit) {
			this.volumeUnit = volumeUnit;
		}
		public String getSizeUnit() {
			return sizeUnit;
		}
		public void setSizeUnit(String sizeUnit) {
			this.sizeUnit = sizeUnit;
		}
		public String[] getWfpysParentId() {
			return wfpysParentId;
		}
		public String[] getStartList() {
			return startList;
		}
		public void setStartList(String[] startList) {
			this.startList = startList;
		}
		public String[] getEndList() {
			return endList;
		}
		public void setEndList(String[] endList) {
			this.endList = endList;
		}
		public String[] getWfpysList() {
			return wfpysList;
		}
		public void setWfpysList(String[] wfpysList) {
			this.wfpysList = wfpysList;
		}
		public void setWfpysParentId(String[] wfpysParentId) {
			this.wfpysParentId = wfpysParentId;
		}
		
		public String getIsAttention() {
			return isAttention;
		}
		public void setIsAttention(String isAttention) {
			this.isAttention = isAttention;
		}
		public void listToString(String[] end,String[] start){
			if(end.length==1){
				this.setEndProvinceId(end[0]);
			}
			if(end.length==2){
				this.setEndCityId(end[1]);
			}
			if(end.length==3){
				this.setEndAreaId(end[2]);
			}
			if(start.length==1){
				this.setStartProvinceId(start[0]);
			if(start.length==2){
				this.setStartCityId(start[1]);
			}
			if(start.length==3){
				this.setStartAreaId(start[2]);
			}
		}
     }
 }





















