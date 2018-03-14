package com.ekfans.base.content.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * 广告实体类
 * 
 * @Title: ShopAd.java
 * @Description:
 * @Copyright: Copyright (c) 2013
 * @Company: ekfans
 * @author zgm
 * @date 2013-12-25
 * @version 1.0
 */
@Entity
public class ShopAd extends BasicBean {

	// 序列化
	private static final long serialVersionUID = 1L;
	
	// 广告名称
	private String name = "";
	
	// 广告类型(0:普通，1:切换，2:自定义，3:文字)
	private String type = "";
	
	// 显示类型
    private String showType = "";
	
	// 广告说明
	private String note = "";
	
	// 创建时间
	private String createTime = "";
	
	// 宽
	private double width = 0.00;
	
	// 高
	private double high = 0.00;
	
	private String html = "";
	//物流宝广告配置  "1" 为以配置上线  其他数值表示未配置  
	private String isConfigure= "";
	
	private List<AdDetail> detailist = new ArrayList<AdDetail>();

	public List<AdDetail> getDetailist() {
		return detailist;
	}

	public void setDetailist(List<AdDetail> detailist) {
		this.detailist = detailist;
	}

	public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

	public String getIsConfigure() {
		return isConfigure;
	}

	public void setIsConfigure(String isConfigure) {
		this.isConfigure = isConfigure;
	}
}