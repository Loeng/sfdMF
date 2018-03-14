package com.ekfans.base.order.service;

import java.util.List;

import com.ekfans.base.order.model.Wfpml;
import com.ekfans.pub.util.Pager;

public interface IWfpmlService {

	/**
	 * @Title: getWfpml
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据父ID查询名录 (详细描述此方法相关的业务处理流程)
	 * @param parentId
	 * @return List<Wfpml> 返回类型
	 * @throws
	 */
	public List<Wfpml> getWfpmlByParentId(Pager pager, String parentId);

	public List<Wfpml> getWfpmlByParent(String parentId);

	/**
	 * @Title: getWfpMlById
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据ID查询名录 (详细描述此方法相关的业务处理流程)
	 * @param ids
	 * @return List<Wfpml> 返回类型
	 * @throws
	 */
	public List<Wfpml> getWfpMlById(String ids);

	/**
	 * 模糊查询（根据名录名称查询）第一级
	 * 
	 * @param name
	 *            名录名称
	 * @return 名录集合
	 */
	public List<Wfpml> getWfpMlByName1(String name);

	/**
	 * 模糊查询（根据名录名称查询）第2级
	 * 
	 * @param name
	 *            名录名称
	 * @return 名录集合
	 */
	public List<Wfpml> getWfpMlByName2(String name);

	/**
	 * 模糊查询（根据名录名称查询）第3级
	 * 
	 * @param name
	 *            名录名称
	 * @return 名录集合
	 */
	public List<Wfpml> getWfpMlByName3(String name);

	/**
	 * 分页模糊查询
	 * 
	 * @param pager
	 *            分页对象
	 * @param name
	 *            名称
	 * @return
	 */
	public List<Wfpml> getWfpMlByName3(Pager pager, String name);

	/**
	 * 根据ID集合查询名称
	 * 
	 * @param ids
	 * @return
	 */
	public String getWFPMLNames(String ids);

	/**
	 * 新增名录
	 * 
	 * @param m
	 */
	public void addWfpml(Wfpml m, String paraentId);

	/**
	 * 修改一级名录
	 * 
	 * @param m
	 */
	public void updateWfpml1(Wfpml m);

	/**
	 * 删除一级名录
	 * 
	 * @param firstId
	 */
	public void delWfpml1(String firstId);

	/**
	 * 删除二级名录
	 * 
	 * @param secondId
	 */
	public void delWfpml2(String secondId);

	/**
	 * 删除三级名录
	 * 
	 * @param threeId
	 */
	public void delWfpml3(String threeId);

	/**
	 * /** 根据上级名录查询下级名录
	 * 
	 * @param firstId
	 *            一级名录id
	 * @return 返回二级名录集合
	 */
	public List<Wfpml> getWfpmlList(String parentId);

	/**
	 * 根据末级名录ID查询一级名录以及末级名录集合
	 * 
	 * @param lastIds
	 * @return
	 */
	public List<Wfpml> getWfpmlListByLastIds(String[] lastIds);
}
