package com.ekfans.base.loan.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ekfans.base.loan.model.LoanTypeDetail;
import com.ekfans.pub.util.StringUtil;

public class LoanTypeHelper {
	public static List<LoanTypeDetail> initLoanType() {
		List<LoanTypeDetail> details = new LinkedList<LoanTypeDetail>();

		LoanTypeDetail fDetail = new LoanTypeDetail();
		fDetail.setName("申请融资的业务订单");
		fDetail.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_ORDER);
		fDetail.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_ORDER));
		fDetail.setNote("融资申请客户与其下游交易对手签订的有效订单");
		fDetail.setCommentType(true);
		details.add(fDetail);

		LoanTypeDetail sDetail = new LoanTypeDetail();
		sDetail.setName("申请融资的业务发票");
		sDetail.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_RECEIPT);
		sDetail.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_RECEIPT));
		sDetail.setNote("融资申请客户与其下游签订的有效合同所开立的发票（扫描件）");
		sDetail.setCommentType(true);
		details.add(sDetail);

		LoanTypeDetail fDetail2 = new LoanTypeDetail();
		fDetail2.setName("申请融资的业务合同");
		fDetail2.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_CONTRACT);
		fDetail2.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CONTRACT));
		fDetail2.setNote("融资申请客户与其下游交易对手签订的有效合同（扫描件）");
		fDetail2.setCommentType(true);
		details.add(fDetail2);

		LoanTypeDetail detail = new LoanTypeDetail();
		detail.setName("跟下游企业之间的往来发票扫描件");
		detail.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_WLFP);
		detail.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_WLFP));
		detail.setNote("跟下游企业之间的往来发票扫描件,不超过十张的，全部提供，超过十张的，提供最近十张");
		details.add(detail);

		LoanTypeDetail detail2 = new LoanTypeDetail();
		detail2.setName("在三分地平台历史业务数据");
		detail2.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_YW);
		detail2.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_YW));
		detail2.setNote("融资申请客户在三分地平台的历史业务数据");
		detail2.setCommentType(true);
		details.add(detail2);

		LoanTypeDetail detail3 = new LoanTypeDetail();
		detail3.setName("近三年度经审计的财务报表扫描件");
		detail3.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_YEAR);
		detail3.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_YEAR));
		detail3.setNote("融资申请客户的近三年度的财务报表（最好是经审计）（扫描件）");
		detail3.setCommentType(true);
		details.add(detail3);

		LoanTypeDetail detail4 = new LoanTypeDetail();
		detail4.setName("近三个月的财务报表");
		detail4.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_MONTH);
		detail4.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_MONTH));
		detail4.setNote("融资申请客户的近三个月的财务报表（EXCEL）");
		detail4.setCommentType(true);
		details.add(detail4);

		LoanTypeDetail detail5 = new LoanTypeDetail();
		detail5.setName("公司简介");
		detail5.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_JJ);
		detail5.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_JJ));
		detail5.setNote("融资申请客户的简介(WORD)");
		detail5.setCommentType(true);
		details.add(detail5);

		LoanTypeDetail detail6 = new LoanTypeDetail();
		detail6.setName("营业执照");
		detail6.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_YYZZ);
		detail6.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_YYZZ));
		detail6.setNote("融资申请客户的营业执照(加盖公司公章的扫描件)");
		detail6.setCommentType(true);
		details.add(detail6);

		LoanTypeDetail detail7 = new LoanTypeDetail();
		detail7.setName("信用代码证");
		detail7.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_DMZ);
		detail7.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_DMZ));
		detail7.setNote("融资申请客户的机构信用代码证(加盖公司公章的扫描件)");
		detail7.setCommentType(true);
		details.add(detail7);

		LoanTypeDetail detail8 = new LoanTypeDetail();
		detail8.setName("公司章程");
		detail8.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_ZC);
		detail8.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_ZC));
		detail8.setNote("融资申请客户的公司章程(加盖公司公章的扫描件所形成的PDF)");
		detail8.setCommentType(true);
		details.add(detail8);

		return details;
	}

	public static Map<String, List<LoanTypeDetail>> initLoanType(List<LoanTypeDetail> detailList) {
		List<LoanTypeDetail> details = new LinkedList<LoanTypeDetail>();
		List<LoanTypeDetail> sDetails = new LinkedList<LoanTypeDetail>();
		Map<String, LoanTypeDetail> cMap = new HashMap<String, LoanTypeDetail>();
		for (LoanTypeDetail detail : detailList) {
			if (detail == null) {
				continue;
			}
			if (!StringUtil.isEmpty(detail.getNameCode())) {
				cMap.put(detail.getNameCode(), detail);
			} else {
				sDetails.add(detail);
			}
		}

		LoanTypeDetail fDetail = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_ORDER)) {
			fDetail = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_ORDER);
			fDetail.setChecked(true);
		} else {
			fDetail = new LoanTypeDetail();
			fDetail.setName("申请融资的业务订单");
			fDetail.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_ORDER);
			fDetail.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_ORDER));
			fDetail.setNote("融资申请客户与其下游交易对手签订的有效订单");
			fDetail.setCommentType(true);
		}
		details.add(fDetail);

		LoanTypeDetail sDetail = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_RECEIPT)) {
			sDetail = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_RECEIPT);
			sDetail.setChecked(true);
		} else {
			sDetail = new LoanTypeDetail();
			sDetail.setName("申请融资的业务发票");
			sDetail.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_RECEIPT);
			sDetail.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_RECEIPT));
			sDetail.setNote("融资申请客户与其下游签订的有效合同所开立的发票（扫描件）");
			sDetail.setCommentType(true);
		}
		details.add(sDetail);

		LoanTypeDetail fDetail2 = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_CONTRACT)) {
			fDetail2 = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CONTRACT);
			fDetail2.setChecked(true);
		} else {
			fDetail2 = new LoanTypeDetail();
			fDetail2.setName("申请融资的业务合同");
			fDetail2.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_CONTRACT);
			fDetail2.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CONTRACT));
			fDetail2.setNote("融资申请客户与其下游交易对手签订的有效合同（扫描件）");
			fDetail2.setCommentType(true);
		}
		details.add(fDetail2);

		LoanTypeDetail detail = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_WL)) {
			detail = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_WL);
			detail.setChecked(true);
		} else {
			detail = new LoanTypeDetail();
			detail.setName("跟下游企业之间的往来发票扫描件");
			detail.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_WLFP);
			detail.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_WLFP));
			detail.setNote("跟下游企业之间的往来发票扫描件,不超过十张的，全部提供，超过十张的，提供最近十张");
			details.add(detail);
		}
		details.add(detail);

		LoanTypeDetail detail2 = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_YW)) {
			detail2 = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_YW);
			detail2.setChecked(true);
		} else {
			detail2 = new LoanTypeDetail();
			detail2.setName("在三分地平台历史业务数据");
			detail2.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_YW);
			detail2.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_YW));
			detail2.setNote("融资申请客户在三分地平台的历史业务数据");
			detail2.setCommentType(true);
		}
		details.add(detail2);

		LoanTypeDetail detail3 = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_YEAR)) {
			detail3 = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_YEAR);
			detail3.setChecked(true);
		} else {
			detail3 = new LoanTypeDetail();
			detail3.setName("近三年度经审计的财务报表扫描件");
			detail3.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_YEAR);
			detail3.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_YEAR));
			detail3.setNote("融资申请客户的近三年度的财务报表（最好是经审计）（扫描件）");
			detail3.setCommentType(true);
		}
		details.add(detail3);

		LoanTypeDetail detail4 = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_MONTH)) {
			detail4 = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_MONTH);
			detail4.setChecked(true);
		} else {
			detail4 = new LoanTypeDetail();
			detail4.setName("近三个月的财务报表");
			detail4.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_MONTH);
			detail4.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_CWBB_MONTH));
			detail4.setNote("融资申请客户的近三个月的财务报表（EXCEL）");
			detail4.setCommentType(true);
		}
		details.add(detail4);

		LoanTypeDetail detail5 = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_JJ)) {
			detail5 = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_JJ);
			detail5.setChecked(true);
		} else {
			detail5 = new LoanTypeDetail();
			detail5.setName("公司简介");
			detail5.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_JJ);
			detail5.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_JJ));
			detail5.setNote("融资申请客户的简介(WORD)");
			detail5.setCommentType(true);
		}
		details.add(detail5);

		LoanTypeDetail detail6 = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_YYZZ)) {
			detail6 = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_YYZZ);
			detail6.setChecked(true);
		} else {
			detail6 = new LoanTypeDetail();
			detail6.setName("营业执照");
			detail6.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_YYZZ);
			detail6.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_YYZZ));
			detail6.setNote("融资申请客户的营业执照(加盖公司公章的扫描件)");
			detail6.setCommentType(true);
		}
		details.add(detail6);

		LoanTypeDetail detail7 = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_DMZ)) {
			detail7 = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_DMZ);
			detail7.setChecked(true);
		} else {
			detail7 = new LoanTypeDetail();
			detail7.setName("信用代码证");
			detail7.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_DMZ);
			detail7.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_DMZ));
			detail7.setNote("融资申请客户的机构信用代码证(加盖公司公章的扫描件)");
			detail7.setCommentType(true);
		}
		details.add(detail7);

		LoanTypeDetail detail8 = null;
		if (cMap.containsKey(LoanTypeUtil.LOAN_DETAIL_CODE_ZC)) {
			detail8 = cMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_ZC);
			detail8.setChecked(true);
		} else {
			detail8 = new LoanTypeDetail();
			detail8.setName("公司章程");
			detail8.setNameCode(LoanTypeUtil.LOAN_DETAIL_CODE_ZC);
			detail8.setType(LoanTypeUtil.typeCodeNameMap.get(LoanTypeUtil.LOAN_DETAIL_CODE_ZC));
			detail8.setNote("融资申请客户的公司章程(加盖公司公章的扫描件所形成的PDF)");
			detail8.setCommentType(true);
		}
		details.add(detail8);

		Map<String, List<LoanTypeDetail>> rMap = new HashMap<String, List<LoanTypeDetail>>();
		rMap.put("firstList", details);
		rMap.put("secondList", sDetails);
		return rMap;
	}
}
