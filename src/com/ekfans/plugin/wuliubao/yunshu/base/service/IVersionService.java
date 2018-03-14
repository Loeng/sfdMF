package com.ekfans.plugin.wuliubao.yunshu.base.service;


import java.util.List;

import com.ekfans.plugin.wuliubao.yunshu.base.model.Version;
import com.ekfans.pub.util.Pager;


/**
 * 版本Service接口
 * @author Administrator
 *
 */
public interface IVersionService {

	/**
	 * 得到当前最新版本的信息
	 * @return
	 */
	public Version getnewVersion(String type);
	
	/**
	 * 得到版本列表
	 * @param type(0:运输IOS;1:运输Android;2:产生IOS;3:产生Android)
	 * @param num 版本号
	 * @param newVersion 是否为最新版(1:最新版,0:旧版本)
	 * @return
	 */
	public List<Version>  getVersionList(String type,String num,String newVersion,Pager page);
	
	/**
	 * 通过版本id得到版本信息
	 * @param versionID 版本ID
	 * @return
	 * @throws Exception 
	 */
	public Version getVersion(String versionID) throws Exception;
	
	/**
	 * 通过版本id删除版本
	 * @param versionID 版本ID
	 * @throws Exception 
	 */
	public void deleteVersion(String[] versionIDs) throws Exception;
	
	/**
	 * 保存版本信息
	 * @param version
	 * @throws Exception
	 */
	public void saveVersion(Version version) throws Exception;
	/**
	 * 更新版本信息
	 * @param version
	 * @throws Exception
	 */
	public void updateVersion(Version version) throws Exception;
	
}

















