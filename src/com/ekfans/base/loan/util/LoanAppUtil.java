package com.ekfans.base.loan.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoanAppUtil {

	// 申请的状态:0：待处理，1：融资立项,2:实地考察，3:信审会审批，4:落实担保条件，5：贷款发放，6：贷后资料收集，7：还款，8：完成

	// 融资申请状态 待处理
	public final static String APP_STATUS_APPLY = "00";

	// 融资申请状态 融资立项
	public final static String APP_STATUS_LX = "01";

	// 融资申请状态 实地考察
	public final static String APP_STATUS_SDKC = "02";

	// 融资申请状态 信审会审批
	public final static String APP_STATUS_XSH = "03";

	// 融资申请状态 落实担保条件
	public final static String APP_STATUS_LSDBTJ = "04";

	// 融资申请状态 贷款发放
	public final static String APP_STATUS_DKFF = "05";

	// 融资申请状态 贷后资料收集
	public final static String APP_STATUS_ZLSJ = "06";

	// 融资申请状态 还款
	public final static String APP_STATUS_HK = "07";

	// 融资申请状态 完成
	public final static String APP_STATUS_FINISH = "08";

	// 融资申请状态 拒绝
	public final static String APP_STATUS_JUJUE = "09";

	// 融资申请状态 打回
	public final static String APP_STATUS_BACK = "10";

	// 融资申请状态 取消申请
	public final static String APP_STATUS_CANCEL = "99";

	public static Map<String, String> appStatusMap = new LinkedHashMap<String, String>();
	static {
		appStatusMap.put(APP_STATUS_APPLY, "待处理");
		appStatusMap.put(APP_STATUS_LX, "银行立项");
		appStatusMap.put(APP_STATUS_SDKC, "实地考察");
		appStatusMap.put(APP_STATUS_XSH, "信审会审批");
		appStatusMap.put(APP_STATUS_LSDBTJ, "落实担保条件");
		appStatusMap.put(APP_STATUS_DKFF, "融资发放");
		appStatusMap.put(APP_STATUS_ZLSJ, "融资后资料收集");
		appStatusMap.put(APP_STATUS_HK, "还款");
		appStatusMap.put(APP_STATUS_FINISH, "完成");
		appStatusMap.put(APP_STATUS_JUJUE, "拒绝融资");
		appStatusMap.put(APP_STATUS_BACK, "驳回重新提交");
		appStatusMap.put(APP_STATUS_CANCEL, "取消申请");
	}

}
