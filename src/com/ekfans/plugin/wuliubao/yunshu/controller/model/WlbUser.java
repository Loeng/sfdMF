package com.ekfans.plugin.wuliubao.yunshu.controller.model;

import com.ekfans.base.user.model.User;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;

/**
 * 物流宝数据包装类 只用于数据传输 不作为持久化实体类
 * @author pp
 * @Date 2017年7月5日17:38:41
 */
public class WlbUser implements java.io.Serializable{

    //用户id
	private String userId ="";
	//用户token
	private String token="";
	// 用户名
	private String name = "";
	// 昵称
	private String nickName = "";
	// 密码强度(1:弱,2:中,3:强)
	private String passwordStrong = "";
	//用户类型(1:司机,2:运输企业,3:产生企业)
	private String type = "";
	// 用户头像
	private String headPortrait = "";
	// 手机号（找回账号密码）
	private String mobile = "";
	// 编辑资料手机号
	private String safeMobile = "";
	// 上次登陆时间
	private String lastLoginTime = "";
	//认证状态  '0' 未认证   '1' 认证中   '2' 已认证  '3' 认证失败   默认 '0'
	private String state = "0";
	//经营范围
	private String[] Scope;
	//营业执照号
	private String bINumber="";
	//营业执照图片保存路径
	private String bIPath="";
	//行驶证号 
	private String dINumber="";
	//行驶证图片保存路径
	private String dIPath="";
	//道路运输资质号
	private String rTQNumber="";
	//道路运输资质图片保存路径
	private String rTQPath="";
	//驾驶证号 
	private String dsINumber="";
	//驾驶证图片保存路径
	private String dsIPath="";
	//危险品道路运输经营许可证号 
	private String dTMNumber="";
	//危险品道路运输经营许可证图片保存路径
	private String dTMPath="";
	//危险品运输资格证号
	private String dTQNumber="";
	//表示危险品运输资格证保存路径
	private String dTQPath="";
	//排污许可证号
	private String sPNumber=""; 
	//排污许可证图片保存路径
	private String sPPath=""; 
	//排污许可证附件图片保存路径
	private String sPEPath=""; 
	//审核人ID
	private String auditorId="";
	//审核时间
	private String auditTime="";
	//审核备注
	private String remarks="";
	//极光推送的设备唯一性标识
	private String registrationID="";
	
	//包装数据
    public WlbUser(User user, Aptitude ap, String token) {
    	   this.setToken(token);
	       this.setUserId(user.getId());
	       this.setName(user.getName());
	       this.setPasswordStrong(user.getPasswordStrong());
	       this.setHeadPortrait(user.getHeadPortrait());
	       this.setMobile(user.getMobile());
	       this.setLastLoginTime(user.getLastLoginTime());
	       this.setRegistrationID(user.getRegistrationID());
	       this.setAuditorId(ap.getAuditorId());
	       this.setAuditTime(ap.getAuditTime());
	       this.setRemarks(ap.getRemarks());
	       this.setType(ap.getType());
    	   this.setState(ap.getState());
    	   this.setNickName(user.getNickName());
    	   this.setSafeMobile(user.getSafeMobile());
    	   this.setScope(ap.getManagement().split("\\|"));
	       //如果认证人是司机
	       if(ap.getType().equals("1")){
	    	   this.setdINumber(ap.getManagementNumber());
	    	   this.setdIPath(ap.getManagementPath());
	    	   this.setDsINumber(ap.getTransportNumber());
	    	   this.setDsIPath(ap.getTransportPath());
	    	   this.setdTQNumber(ap.getDangerTransportNumber());
	    	   this.setdTQPath(ap.getDangerTransportPath());
	       }
	       //如果认证人是企业
	       if(ap.getType().equals("2")){
	    	   this.setbINumber(ap.getManagementNumber());
	    	   this.setbIPath(ap.getManagementPath());
	    	   this.setrTQNumber(ap.getTransportNumber());
	    	   this.setrTQPath(ap.getTransportPath());
	    	   this.setdTMNumber(ap.getDangerTransportNumber());
	    	   this.setdTMPath(ap.getDangerTransportPath());
	       }
	       //如果认证人是产生企业
	       if(ap.getType().equals("3")){
	    	   this.setbINumber(ap.getManagementNumber());
	    	   this.setbIPath(ap.getManagementPath());
	    	   this.setsPNumber(ap.getSewagePermitNumber());
	    	   this.setsPPath(ap.getSewagePermitPath());
	    	   this.setsPEPath(ap.getSewagePermitEnclosurePath());
	       }
	}
    
  
    
    //包装数据
    public String getsPNumber() {
		return sPNumber;
	}
	public void setsPNumber(String sPNumber) {
		this.sPNumber = sPNumber;
	}
	public String getsPPath() {
		return sPPath;
	}
	public void setsPPath(String sPPath) {
		this.sPPath = sPPath;
	}
	public void PackingWlbUser(Aptitude ap) {
		   this.setState(ap.getState());
		   //如果认证人是司机
	       if(ap.getType().equals("1")){
	    	   this.setdINumber(ap.getManagementNumber());
	    	   this.setdIPath(ap.getManagementPath());
	    	   this.setDsINumber(ap.getTransportNumber());
	    	   this.setDsIPath(ap.getTransportPath());
	    	   this.setdTQNumber(ap.getDangerTransportNumber());
	    	   this.setdTQPath(ap.getDangerTransportPath());
	       }
	       //如果认证人是运输企业
	       if(ap.getType().equals("2")){
	    	   this.setScope(ap.getManagement().split("\\|"));
	    	   this.setbINumber(ap.getManagementNumber());
	    	   this.setbIPath(ap.getManagementPath());
	    	   this.setrTQNumber(ap.getTransportNumber());
	    	   this.setrTQPath(ap.getTransportPath());
	    	   this.setdTMNumber(ap.getDangerTransportNumber());
	    	   this.setdTMPath(ap.getDangerTransportPath());
	       }
	       //如果认证人是产生企业
	       if(ap.getType().equals("3")){
	    	   this.setScope(ap.getManagement().split("\\|"));
	    	   this.setbINumber(ap.getManagementNumber());
	    	   this.setbIPath(ap.getManagementPath());
	    	   this.setsPNumber(ap.getSewagePermitNumber());
	    	   this.setsPPath(ap.getSewagePermitPath());
	    	   this.setsPEPath(ap.getSewagePermitEnclosurePath());
	       }
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPasswordStrong() {
		return passwordStrong;
	}
	public void setPasswordStrong(String passwordStrong) {
		this.passwordStrong = passwordStrong;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHeadPortrait() {
		return headPortrait;
	}
	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String[] getScope() {
		return Scope;
	}
	public void setScope(String[] scope) {
		Scope = scope;
	}
	public String getbINumber() {
		return bINumber;
	}
	public void setbINumber(String bINumber) {
		this.bINumber = bINumber;
	}
	public String getbIPath() {
		return bIPath;
	}
	public void setbIPath(String bIPath) {
		this.bIPath = bIPath;
	}
	public String getdINumber() {
		return dINumber;
	}
	public void setdINumber(String dINumber) {
		this.dINumber = dINumber;
	}
	public String getdIPath() {
		return dIPath;
	}
	public void setdIPath(String dIPath) {
		this.dIPath = dIPath;
	}
	public String getrTQNumber() {
		return rTQNumber;
	}
	public void setrTQNumber(String rTQNumber) {
		this.rTQNumber = rTQNumber;
	}
	public String getrTQPath() {
		return rTQPath;
	}
	public void setrTQPath(String rTQPath) {
		this.rTQPath = rTQPath;
	}
	public String getDsINumber() {
		return dsINumber;
	}
	public void setDsINumber(String dsINumber) {
		this.dsINumber = dsINumber;
	}
	public String getDsIPath() {
		return dsIPath;
	}
	public void setDsIPath(String dsIPath) {
		this.dsIPath = dsIPath;
	}
	public String getdTMNumber() {
		return dTMNumber;
	}
	public void setdTMNumber(String dTMNumber) {
		this.dTMNumber = dTMNumber;
	}
	public String getdTMPath() {
		return dTMPath;
	}
	public void setdTMPath(String dTMPath) {
		this.dTMPath = dTMPath;
	}
	public String getdTQNumber() {
		return dTQNumber;
	}
	public void setdTQNumber(String dTQNumber) {
		this.dTQNumber = dTQNumber;
	}
	public String getdTQPath() {
		return dTQPath;
	}
	public void setdTQPath(String dTQPath) {
		this.dTQPath = dTQPath;
	}
	public String getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSafeMobile() {
		return safeMobile;
	}
	public void setSafeMobile(String safeMobile) {
		this.safeMobile = safeMobile;
	}
	public String getsPEPath() {
		return sPEPath;
	}
	public void setsPEPath(String sPEPath) {
		this.sPEPath = sPEPath;
	}
	public String getRegistrationID() {
		return registrationID;
	}
	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}

	
	
}
