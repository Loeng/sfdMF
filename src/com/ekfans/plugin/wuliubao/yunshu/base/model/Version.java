package com.ekfans.plugin.wuliubao.yunshu.base.model;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;
/**
 * 物流宝版本实体
 * @author Administrator
 *
 */
@Entity
public class Version extends BasicBean{
	
	private static final long serialVersionUID = -4437978758093909272L;
	  //类型(0:运输IOS;1:运输Android;2:产生IOS;3:产生Android)
	  private String type="";
	  //版本号
	  private String num="";
	  //版本说明
	  private String description="";
	  //下载链接
	  private String url="";
	  //更新时间
	  private String updateTime="";
	  //最新版本   "1"为最新  "0"为旧版
	  private String newVersion="";
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
      
}
