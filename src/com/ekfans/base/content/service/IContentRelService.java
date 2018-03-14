package com.ekfans.base.content.service;

import java.util.List;

import com.ekfans.base.content.model.ContentRel;

public interface IContentRelService {

	/**
	 * @Title addRel
	 * @Description TODO(添加关联频道) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param contentId 资讯ID
	 * @param channelId 频道ID
	 * @return boolean
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016上午11:34:53
	 */
	public boolean addRel(String contentId,String [] channelId);
	
	/**
	 * @Title getList
	 * @Description TODO(查看选中频道) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param contentId
	 * @return List<ContentRel>
	 * @Auth：成都易科远见科技有限公司
	 * @date：2016下午2:35:12
	 */
	public List<ContentRel> getList(String contentId); 
}
