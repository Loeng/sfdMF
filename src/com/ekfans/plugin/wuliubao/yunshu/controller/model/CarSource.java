package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.order.model.Bargain;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppBargainDao;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 车源响应信息包装类
 * @author pp
 * @Date 2017年7月17日10:31:32
 */
public class CarSource {
	    //车源id
		private String id= "";
		// 企业ID
		private String storeId= "";
		// 详细地址
		private String habitatAddress= "";
		// 车载
		private String wfpTotal= "";
		//商品距离
		private String commodityDistance="";
		//离我距离
		private String distanceMe="";
		// 创建时间
		private String createTime= "";
		// 更新时间
		private String updateTime= "";
		// 有效时间
		private String endTime= "";
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
		private String linkMan= "";
		// 联系电话
		private String phone= "";
		// 描述
		private String content= "";
		// 起始地全路径
		private String[] startList;
		// 目的地全路径
		private String[] endList;
		// 出发地省级id
		private String startProvinceId;
		// 目的地省级id
		private String endProvinceId;
		// 出发地市级id
		private String startCityId;
		// 目的地市级id
		private String endCityId;
		// 出发地区级id
		private String startAreaId;
		// 目的地区级id
		private String endAreaId;
		// 企业名称
		private String storeName= "";
		// 单位 默认为吨
		private String unit = "吨";
	    // 车辆类型
		private String carName= "";
		// 罐体材质
		private String tankMaterialName= "";
		// 危废品运输界定父类ID
		private String[] wfpysParentId;
		//当前用户议价状态(1:已议价,2:未议价)
	    private String yJStatus="";
        //议价信息条数
	    private Integer barCount=0;
	    //经营范围
        private String[] wfpysList;
    	//车长
        private BigDecimal carLength;
        //车宽
        private BigDecimal carWidth;
        //车高
        private BigDecimal carHeight;
        //车辆尺寸单位 
        private String sizeUnit ="米";
		//车牌号
	    private String carNumber= "";
	    //车源状态 (0:下架,1:上架,2:审核中,3:审核失败,4:已完成)
	    private String carSourceStatus;
	    //用户是否被关注(1以被关注 0 未被关注)
	    private String isAttention="";
	    public CarSource() {
			super();
		}
		public CarSource(Wftransport w) {
	    	this.id=w.getId();
			this.storeId=w.getStoreId();
			this.endList=w.getEndFullPath().split(",");
			this.startList=w.getStartFullPath().split(",");
			this.habitatAddress=w.getHabitatAddress();
			this.wfpTotal=w.getWfpTotal().toString();
			this.createTime=w.getCreateTime();
			this.updateTime=w.getUpdateTime();
			this.endTime=w.getEndTime();
			this.status=w.getStatus();
			this.checkTime=w.getCheckTime();
			this.checkMan=w.getCheckMan();
			this.checkNote=w.getCheckNote();
			this.checkStatus=w.getCheckStatus();
			this.linkMan=w.getLinkMan();
			this.phone=w.getPhone();
			this.content=w.getContent();
			this.storeName=w.getStoreName();
			this.unit=w.getUnit();
			this.carName=w.getCarName();
			this.tankMaterialName=w.getTankMaterialName();
			this.carNumber=w.getCarNumber();
			this.wfpysList=w.getWfpysName().split("\\|");
			this.wfpysParentId=w.getWfpysParentId().split(",");
			this.sizeUnit=w.getSizeUnit();
			this.carLength=w.getCarLength();
			this.carHeight=w.getCarHeight();
			this.carWidth=w.getCarWidth();
			this.setIsAttention(w.getIsAttention());
			//设置省市区id
			listToString(w.getEndPlace().split("\\|"),w.getStartPlace().split("\\|"));
			this.setBarCount(w.getBarCount());
			if(this.termOfValidity(w.getEndTime())==false){
				//已完成
				this.carSourceStatus="4";
			}else {
				if(w.getCheckStatus()==1){
					if(w.getStatus()==1){
					  //已上架
					  this.carSourceStatus="1";
					}
					if(w.getStatus()==0){
					  //已下架
					  this.carSourceStatus="0";
					}
				}
				if(w.getCheckStatus()==0){
					//审核中
					this.carSourceStatus="2";
				}
				if(w.getCheckStatus()==2){
					//审核失败
					this.carSourceStatus="3";
				}
			}
			
		}
	    public CarSource(Wftransport w,Pager p) {
	    	this.id=w.getId();
			this.storeId=w.getStoreId();
			this.endList=w.getEndFullPath().split(",");;
			this.startList=w.getStartFullPath().split(",");;
			this.habitatAddress=w.getHabitatAddress();
			this.wfpTotal=w.getWfpTotal().toString();
			this.createTime=w.getCreateTime();
			this.updateTime=w.getUpdateTime();
			this.endTime=w.getEndTime();
			this.status=w.getStatus();
			this.checkTime=w.getCheckTime();
			this.checkMan=w.getCheckMan();
			this.checkNote=w.getCheckNote();
			this.checkStatus=w.getCheckStatus();
			this.linkMan=w.getLinkMan();
			this.phone=w.getPhone();
			this.content=w.getContent();
			this.storeName=w.getStoreName();
			this.unit=w.getUnit();
			this.carName=w.getCarName();
			this.tankMaterialName=w.getTankMaterialName();
			this.carLength=w.getCarLength();
			this.carNumber=w.getCarNumber();
			this.wfpysList=w.getWfpysName().split("\\|");
			this.wfpysParentId=w.getWfpysParentId().split(",");
			//设置省市区id
			listToString(w.getEndPlace().split("\\|"),w.getStartPlace().split("\\|"));
			this.setBarCount(w.getBarCount());
		}
	    
	    
	    public CarSource(Wftransport w,CheListForm cf,HttpServletRequest request) throws Exception {
	    	this.id=w.getId();
			this.storeId=w.getStoreId();
			this.endList=w.getEndFullPath().split(",");;
			this.startList=w.getStartFullPath().split(",");;
			this.habitatAddress=w.getHabitatAddress();
			this.wfpTotal=w.getWfpTotal().toString();
			this.createTime=w.getCreateTime();
			this.updateTime=w.getUpdateTime();
			this.endTime=w.getEndTime();
			this.status=w.getStatus();
			this.checkTime=w.getCheckTime();
			this.checkMan=w.getCheckMan();
			this.checkNote=w.getCheckNote();
			this.checkStatus=w.getCheckStatus();
			this.linkMan=w.getLinkMan();
			this.phone=w.getPhone();
			this.content=w.getContent();
			this.storeName=w.getStoreName();
			this.unit=w.getUnit();
			this.carName=w.getCarName();
			this.tankMaterialName=w.getTankMaterialName();
			this.carLength=w.getCarLength();
			this.carHeight=w.getCarHeight();
			this.carWidth=w.getCarWidth();
			this.sizeUnit=w.getSizeUnit();
			this.carNumber=w.getCarNumber();
			this.wfpysList=w.getWfpysName().split("\\|");;
			this.wfpysParentId=w.getWfpysParentId().split(",");
			this.setBarCount(w.getBarCount());
			String token = request.getHeader("WLB-Token");
			if(!StringUtil.isEmpty(token)){
			 this.setIsAttention(w.getIsAttention());
			}else{
				this.setIsAttention("0");
			}
			//设置省市区id
			listToString(w.getEndPlace().split("\\|"),w.getStartPlace().split("\\|"));
//			
//			 try {
//					this.setCommodityDistance(BaiduMapUtil.getCommodityDistance(w.getStartFullPath(), w.getEndFullPath())+"");
//		            if(cf!=null&&!StringUtil.isEmpty(cf.latitude)&&!StringUtil.isEmpty(cf.longitude)){
//		            this.setDistanceMe(BaiduMapUtil.getDistanceMe(w.getStartFullPath(), cf.latitude, cf.longitude)+"");
//		            }
//							
//		        } catch (UnsupportedEncodingException | JSONException e) {
//					e.printStackTrace();
//				}  
			 if(cf==null||StringUtil.isEmpty(cf.userId)){
				 this.yJStatus="2";
			 }else{
				 IWlbAppBargainDao dao = SpringContextHolder.getBean(IWlbAppBargainDao.class);
				 List<Bargain> b = (List<Bargain>) dao.getBargain(cf.userId, null, w.getId(), null);
				 if(b==null||b.size()==0){
					 this.yJStatus="2";
				 }else{
				 this.yJStatus="1";
				 }
			 }
			
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
		public String getHabitatAddress() {
			return habitatAddress;
		}
		public void setHabitatAddress(String habitatAddress) {
			this.habitatAddress = habitatAddress;
		}
		public String getWfpTotal() {
			return wfpTotal;
		}
		public void setWfpTotal(String wfpTotal) {
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
		public String getTankMaterialName() {
			return tankMaterialName;
		}
		public void setTankMaterialName(String tankMaterialName) {
			this.tankMaterialName = tankMaterialName;
		}
		
		public String getCarNumber() {
			return carNumber;
		}
		public void setCarNumber(String carNumber) {
			this.carNumber = carNumber;
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
		public String getCarSourceStatus() {
			return carSourceStatus;
		}
		public void setCarSourceStatus(String carSourceStatus) {
			this.carSourceStatus = carSourceStatus;
		}
        public String getyJStatus() {
			return yJStatus;
		}
		public void setyJStatus(String yJStatus) {
			this.yJStatus = yJStatus;
		}
		public Integer getBarCount() {
			return barCount;
		}
		public void setBarCount(Integer barCount) {
			this.barCount = barCount;
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
		public void setCarLength(BigDecimal carLength) {
			this.carLength = carLength;
		}
		public BigDecimal getCarLength() {
			return carLength;
		}
		
		public String getSizeUnit() {
			return sizeUnit;
		}
		public void setSizeUnit(String sizeUnit) {
			this.sizeUnit = sizeUnit;
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
		
		public String[] getWfpysParentId() {
			return wfpysParentId;
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
		//有效期判断 返回false:有效期过了   true:有效期未过
		public boolean termOfValidity(String endTime){
			String sysTime=DateUtil.getSysDateTimeString();
			try {
				if(StringUtil.isEmpty(endTime) || endTime.compareTo(sysTime)<0){
					   return false;
				}
			} catch (Exception e) {
				 return false;
			}
			
			return true;
		}
		//设置省市区id
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
