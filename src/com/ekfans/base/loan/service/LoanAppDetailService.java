package com.ekfans.base.loan.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.loan.dao.ILoanAppDetailDao;
import com.ekfans.base.loan.model.LoanAppDetail;
import com.ekfans.base.loan.model.LoanTypeDetail;
import com.ekfans.base.loan.util.LoanTypeUtil;
import com.ekfans.basic.spring.SpringContextHolder;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

/**
 * 贷款申请实现Service
 * 
 * @Description:
 * @Copyright: Copyright (c) 2014
 * @Company: ekfans
 * @author zgm
 * @date 2016-4-25
 * @version 1.0
 */
@Service
public class LoanAppDetailService implements ILoanAppDetailService {
	public static Logger log = LoggerFactory.getLogger(LoanAppDetailService.class);

	@Autowired
	private ILoanAppDetailDao appDetailDao;

	/**
	 * 新增明细
	 * 
	 * @param request
	 * @param loanAppId
	 * @param loanTypeId
	 * @param session
	 */
	public void addDetails(HttpServletRequest request, HttpServletResponse response, String loanAppId, String loanTypeId, Session session) {
		if (StringUtil.isEmpty(loanAppId) || StringUtil.isEmpty(loanTypeId)) {
			return;
		}

		try {
			// 保存明细
			ILoanTypeDetailService typeDetailService = SpringContextHolder.getBean(ILoanTypeDetailService.class);
			List<LoanTypeDetail> detailList = typeDetailService.getDetailsByTypeId(loanTypeId);
			if (detailList != null && detailList.size() > 0) {
				for (int i = 0; i < detailList.size(); i++) {
					LoanTypeDetail typeDetail = detailList.get(i);
					if (typeDetail == null) {
						continue;
					}
					LoanAppDetail appDetail = new LoanAppDetail();
					appDetail.setBankId(typeDetail.getBankId());
					appDetail.setAppId(loanAppId);
					appDetail.setName(typeDetail.getName());
					appDetail.setNameCode(typeDetail.getNameCode());
					appDetail.setNote(typeDetail.getNote());
					appDetail.setPosition(typeDetail.getPosition());
					appDetail.setType(typeDetail.getType());
					appDetail.setTypeId(typeDetail.getTypeId());
					appDetail.setTypeDetailId(request.getParameter(typeDetail.getId() + "TypeDetailId"));

					if (!StringUtil.isEmpty(appDetail.getNameCode())
							&& (LoanTypeUtil.LOAN_DETAIL_CODE_ORDER.equals(appDetail.getNameCode()))) {
						String value = request.getParameter(typeDetail.getId());
						String valueType = request.getParameter(typeDetail.getId() + "ValueType");
						appDetail.setValue(value);
						appDetail.setValueType(valueType);
					} else if (!StringUtil.isEmpty(appDetail.getNameCode())
							&& LoanTypeUtil.LOAN_DETAIL_CODE_CONTRACT.equals(appDetail.getNameCode())) {
						String contractType = request.getParameter("contractType");
						if ("0".equals(contractType)) {
							// 文件
							String typeValue = "/customerfiles/company/loan/" + DateUtil.getNoSpSysDateString() + "/";
							typeValue = FileUploadHelper.uploadFile(typeDetail.getId(), typeValue, request, response);
							appDetail.setValue(typeValue);
							appDetail.setValueType("9");
						} else if("1".equals(contractType)) {
							String value = request.getParameter(typeDetail.getId());
							// 合同id
							String valueType = request.getParameter(typeDetail.getId() + "ValueType");
							appDetail.setValue(value);
							appDetail.setValueType(valueType);
						}
					} else if (!StringUtil.isEmpty(appDetail.getNameCode())
							&& (LoanTypeUtil.LOAN_DETAIL_CODE_WL.equals(appDetail.getNameCode()) || LoanTypeUtil.LOAN_DETAIL_CODE_YW.equals(appDetail.getNameCode()))) {
						String otherStoreId = request.getParameter("orderStoreId");
						if (StringUtil.isEmpty(otherStoreId)) {
							otherStoreId = request.getParameter("contractStoreId");
						}
						appDetail.setValue(otherStoreId);
						appDetail.setValueType("6");
					} else if (LoanTypeUtil.LOAN_DETAIL_TYPE_PIC.equals(appDetail.getType()) || LoanTypeUtil.LOAN_DETAIL_TYPE_EXCEL.equals(appDetail.getType())
							|| LoanTypeUtil.LOAN_DETAIL_TYPE_WORD.equals(appDetail.getType()) || LoanTypeUtil.LOAN_DETAIL_TYPE_PDF.equals(appDetail.getType())) {
						String typeValue = "/customerfiles/company/loan/" + DateUtil.getNoSpSysDateString() + "/";
						typeValue = FileUploadHelper.uploadFile(typeDetail.getId(), typeValue, request, response);
						appDetail.setValue(typeValue);
					} else if (LoanTypeUtil.LOAN_DETAIL_TYPE_CHECKBOX.equals(appDetail.getType())) {
						String[] typeValues = request.getParameterValues(typeDetail.getId());
						String typeValue = "";
						if (typeValues != null && typeValues.length > 0) {
							for (int j = 0; j < typeValues.length; j++) {
								String value = typeValues[j];
								if (!StringUtil.isEmpty(value)) {
									typeValue = typeValue + value + ";";
								}
							}
						}
						appDetail.setValue(typeValue);
					} else {
						String value = request.getParameter(typeDetail.getId());
						appDetail.setValue(value);
					}

					if (session != null) {
						appDetailDao.addBean(appDetail, session);
					} else {
						appDetailDao.addBean(appDetail);
					}

				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}