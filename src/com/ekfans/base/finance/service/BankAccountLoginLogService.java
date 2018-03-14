package com.ekfans.base.finance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.finance.dao.IBankAccountDao;

/**
 * 企业权限功能菜单Service接口实现
 * 
 * @ClassName: StorePurviewService
 * @author Lgy
 * @date 2014-03-21 上午11:35:38 
 * @version v1.0 Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 *          Company:成都易科远见科技有限公司 www.ekfans.com
 */
@Service
public class BankAccountLoginLogService implements IBankAccountLoginLogService {

	@Autowired
	IBankAccountDao accountDao;
}
