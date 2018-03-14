package com.ekfans.plugin.wuliubao.yunshu.base.dao;

import java.util.List;

import com.ekfans.basic.hibernate.dao.IGenericDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.pub.util.Pager;
/**
 * 
 * @author pp
 * @Date 2017年7月13日14:36:20
 */
public interface IWlbAppAptitude  extends IGenericDao{
	/**
	 * 获取未审核的资质的数量
	 * @return
	 * @throws Exception
	 */
	int getAptitudeNum() throws Exception;
	
	/**
	 * 通过认证人id获取资质认证
	 * @param authenticatorId
	 * @return
	 * @throws Exception 
	 */
	Aptitude getAptitude(String authenticatorId) throws Exception;
	
	/**
	 * 通过用户账号丶用户昵称获取未认证的用户资质认证集合
	 * @param pager
	 * @param nickname
	 * @param username
	 * @return
	 * @throws Exception 
	 */
	List<Aptitude> getCheckAptitude(Pager pager,String nickName,String name) throws Exception;
}
