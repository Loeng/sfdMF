package com.ekfans.plugin.wuliubao.yunshu.base.model;

import javax.persistence.Entity;

import com.ekfans.base.user.model.User;
import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 资质认证实体类
 * @author pp
 * @Date 2017年6月29日14:23:28
 */
@Entity
public class Aptitude extends BasicBean{

	private static final long serialVersionUID = 813389659044273258L;
	
	//认证类型   '1' 司机   '2' 运输公司  '3' 生产企业
	private String type="";
	//认证状态  '0' 未认证   '1' 认证中   '2' 已认证  '3' 认证失败   默认 '0'
	private String state = "0";
	//认证人ID
	private String authenticatorId="";
	//认证人昵称
	private String nickName="";
	//认证人账号
	private String name="";
	//认证类型是 都表示 经营范围 
	private String management="";
	//认证类型是 '3' 或 '2' 表示 营业执照号   '1' 表示行驶证号码
	private String managementNumber="";
	//认证类型是 '3' 或 '2' 表示 营业执照图片保存路径  '1' 表示行驶证图片保存路径
	private String managementPath="";
	//认证类型是 '2'  表示 道路运输资质号   '1' 表示驾驶证号码  '3' 无特殊意思
	private String transportNumber="";
	//认证类型是 '2'  表示 道路运输资质图片保存路径   '1' 表示驾驶证图片保存路径  '3' 无特殊意思
	private String transportPath="";
	//认证类型是 '2'  表示 危险品道路运输经营许可证号   '1' 表示危险品运输资格证号  '3' 无特殊意思
	private String dangerTransportNumber="";
	//认证类型是 '2'  表示 危险品道路运输经营许可证图片保存路径   '1' 表示危险品运输资格证保存路径  '3' 无特殊意思
	private String dangerTransportPath="";
	//排污许可证号
	private String sewagePermitNumber=""; 
	//排污许可证图片保存路径
	private String sewagePermitPath=""; 
	//排污许可证附件图片保存路径
	private String sewagePermitEnclosurePath=""; 
	// 创建时间
	private String createTime="";
	// 更新时间
	private String updateTime="";
	//审核人ID
	private String auditorId="";
	//审核时间
	private String auditTime="";
	//审核备注
	private String remarks="";
	// ======================= 临时数据 =======================
	//用户信息
	private User user;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAuthenticatorId() {
		return authenticatorId;
	}
	public void setAuthenticatorId(String authenticatorId) {
		this.authenticatorId = authenticatorId;
	}
	public String getManagement() {
		return management;
	}
	public void setManagement(String management) {
		this.management = management;
	}
	public String getManagementNumber() {
		return managementNumber;
	}
	public void setManagementNumber(String managementNumber) {
		this.managementNumber = managementNumber;
	}
	public String getManagementPath() {
		return managementPath;
	}
	public void setManagementPath(String managementPath) {
		this.managementPath = managementPath;
	}
	public String getTransportNumber() {
		return transportNumber;
	}
	public void setTransportNumber(String transportNumber) {
		this.transportNumber = transportNumber;
	}
	public String getTransportPath() {
		return transportPath;
	}
	public void setTransportPath(String transportPath) {
		this.transportPath = transportPath;
	}
	public String getDangerTransportNumber() {
		return dangerTransportNumber;
	}
	public void setDangerTransportNumber(String dangerTransportNumber) {
		this.dangerTransportNumber = dangerTransportNumber;
	}
	public String getDangerTransportPath() {
		return dangerTransportPath;
	}
	public void setDangerTransportPath(String dangerTransportPath) {
		this.dangerTransportPath = dangerTransportPath;
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
	public String getSewagePermitNumber() {
		return sewagePermitNumber;
	}
	public void setSewagePermitNumber(String sewagePermitNumber) {
		this.sewagePermitNumber = sewagePermitNumber;
	}
	public String getSewagePermitPath() {
		return sewagePermitPath;
	}
	public void setSewagePermitPath(String sewagePermitPath) {
		this.sewagePermitPath = sewagePermitPath;
	}
	public String getSewagePermitEnclosurePath() {
		return sewagePermitEnclosurePath;
	}
	public void setSewagePermitEnclosurePath(String sewagePermitEnclosurePath) {
		this.sewagePermitEnclosurePath = sewagePermitEnclosurePath;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
}
