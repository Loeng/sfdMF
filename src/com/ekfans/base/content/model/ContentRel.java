package com.ekfans.base.content.model;

import com.ekfans.basic.hibernate.model.BasicBean;

/**
 * @ClassName: ContentRel  
 * @Description: TODO(关联频道分类) 
 * @Copyright: Copyright (c) 2016年7月28日
 * @Company: 成都易科远见科技有限公司
 * @author 成都易科远见科技有限公司
 * @date 2016年7月28日上午11:16:37
 * @version 1.0
 */
public class ContentRel extends BasicBean{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3154774702357386351L;
	// 频道ID
	private String channelId;
	// 资讯ID
	private String contentId;
	// 分类名称
	private String cname;
	
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
}
