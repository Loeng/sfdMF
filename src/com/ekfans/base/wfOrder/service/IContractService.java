package com.ekfans.base.wfOrder.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ekfans.base.store.model.CarInfo;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.wfOrder.model.Contract;
import com.ekfans.base.wfOrder.model.ContractCars;
import com.ekfans.pub.util.Pager;

/**
 * 合同接口
 * 
 * @ClassName: IContractService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Ekfans_zhangJin
 * @date 2015-1-12 上午10:54:35
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IContractService {

	/**
	 * @Title: getContract
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据两个ID查询运输合同
	 *               (详细描述此方法相关的业务处理流程)
	 * @param firstId
	 * @param secondeId
	 * @return Contract 返回类型
	 * @throws
	 */
	public Contract getContract(String firstId, String secondeId);

	/**
	 * @Title: addContract
	 * @Description: TODO(合同保存方法) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param con
	 *            void 返回类型
	 * @throws
	 */
	public boolean addContract(Contract con);

	/**
	 * @Title: updateContract
	 * @Description: TODO(更新合同信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param con
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean updateContract(Contract con);

	/**
	 * @Title: isContractNo
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:查询合同编号是否重复 (详细描述此方法相关的业务处理流程)
	 * @param contractNo
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean isContractNo(String contractNo);

	/**
	 * 查询合同根据登陆人
	 * 
	 * @Title: getContractAllList
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param pager
	 * @return List<Contract> 返回类型
	 * @throws
	 */
	public List<Contract> getContractAllList(Pager pager, String stroeid);

	/**
	 * 
	 * @Title: getContractById
	 * @Description: TODO(根据ID查询合同信息) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param contractId
	 * @return Contract 返回类型
	 * @throws
	 */
	public Contract getContractById(String contractId);

	/**
	 * @Title: deleteContractById
	 * @Description: TODO(这里用一句话描述这个方法的作用) 详细业务流程:根据ID删除合同 (详细描述此方法相关的业务处理流程)
	 * @param contractId
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean deleteContractById(String contractId);

	/**
	 * @Title: getContractByStoreId
	 * @Description: TODO(查询与登陆相关的合同) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param storeId
	 * @return List<Contract> 返回类型
	 * @throws
	 */
	public List<Contract> getContractByStoreId(Pager pager, Store store, String id, String contractName);

	/**
	 * @Title: getContractByParams
	 * @Description: TODO(根据查询条件搜索企业列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param firstName
	 * @param secondName
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return List<Contract> 返回类型
	 * @throws
	 */
	public List<Contract> getContractByParams(Pager pager, String storeId, String contractName, String firstName, String secondName, String startTime, String endTime, String type, String contractNo, String viewType);

	/**
	 * @Title: orderChoseContracts
	 * @Description: TODO(根据查询条件搜索企业列表) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param firstName
	 * @param secondName
	 * @param startTime
	 * @param endTime
	 * @param type
	 * @return List<Contract> 返回类型
	 * @throws
	 */
	public List<Contract> orderChoseContracts(Pager pager, String storeId, String contractName, String storeName, String startTime, String endTime, String type, String contractNo, String viewType);

	/**
	 * 根据企业id和合同编号查询合同
	 * 
	 * @param storeId
	 *            企业id
	 * @param contractNo
	 *            合同编号
	 */
	public Contract getContractByContractNo(String storeId, String contractNo);

	/**
	 * 保存合同
	 * 
	 * @param request
	 * @param response
	 * @param contract
	 * @return
	 */
	public boolean saveContract(HttpServletRequest request, HttpServletResponse response, Contract contract);

	public boolean updateContract(HttpServletRequest request, HttpServletResponse response, Contract contract);

	public Contract getContractByScrapId(String scrapId);

	public List<ContractCars> getCarsByContract(String contractId);

	public List<Contract> getYsContractsByContractId(String contractId);

	public List<Contract> getContracts(Pager pager, String storeId, String contractName, String firstName, String secondName, String startTime, String endTime, String type, String checkStatus, String contractNo, String viewType);

	public boolean updateYsContract(String conId, HttpServletRequest request);

	public List<CarInfo> getCarUsersByContract(String contractId, String storeId);

	/**
	 * 校验合同所关联的运输合同是否需要公里数
	 * 
	 * @param contractId
	 * @return
	 */
	public boolean checkContractNeedMileage(String contractId);
}
