package com.ekfans.base.user.service;

import java.util.List;

import com.ekfans.base.user.model.BankBinding;
import com.ekfans.base.user.model.User;
import com.ekfans.pub.util.Pager;

/**
 * 银行卡绑定Service接口
 * 
 * @ClassName: IBankBindingService
 * @Description:
 * @author ek_lq
 * @date 2014-9-20 上午11:55:14
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
public interface IBankBindingService {

	/**
	 * 根据企业id获取关联的银行卡集合
	 * 
	 * @param storeId
	 *            企业id
	 * @param type
	 *            银行卡绑定状态（null:绑定的全部银行卡，false:未认证银行卡，true:认证通过银行卡）
	 * @return List<BankBinding>
	 */
	public List<BankBinding> getBankBindingByStoreId(String storeId, String type);

	/**
	 * @Title: saveBankBindIng
	 * @Description: TODO(绑定银行卡) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param bank
	 * @return boolean 返回类型
	 * @throws
	 */
	public boolean CheckBankBindIng(BankBinding bank);

	/**
	 * @Title: deletBank
	 * @Description: TODO(根据银行卡ID,和银行卡数量删除银行卡) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @param id
	 * @param count
	 * @return String 返回类型
	 * @throws
	 */
	public String deletBank(String id, int count, User user);

	// 新增银行卡
	public String addBank(BankBinding bank);

	// 更改银行卡
	public String updateBank(BankBinding bank);

	/**
	 * @Title: allBank
	 * @Description: TODO(后台获取银行卡) 详细业务流程: (详细描述此方法相关的业务处理流程)
	 * @return List<BankBinding> 返回类型
	 * @throws
	 */
	public List<BankBinding> allBank(Pager pager, String id, String name, String banknum);

	/**
	 * 根据ID获取对象
	 * 
	 * @param bindingId
	 * @return
	 */
	public BankBinding getBankBindingById(String bindingId);
}
