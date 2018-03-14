package com.ekfans.base.store.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import com.ekfans.base.order.model.Wfpml;
import com.ekfans.base.store.dao.IAccreditDao;
import com.ekfans.base.store.dao.IAccreditRelDao;
import com.ekfans.base.store.dao.IWfpysDao;
import com.ekfans.base.store.dao.IWfpysRelDao;
import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.model.AccreditRel;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.WfpysRel;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

public class AccreditHelper {
	// 资质类型 - 危险废物经营许可
	public static final String ACC_RZTYPE_WF = "0";
	// 资质类型 - 排放污染物许可
	public static final String ACC_RZTYPE_PW = "1";
	// 资质类型 - 道路运输经营许可
	public static final String ACC_RZTYPE_YS = "2";

	/**
	 * 从页面获取危废处置的对象并保存
	 * 
	 * @param request
	 * @return
	 */
	public static void saveOrUpdateWfAccredit(HttpServletRequest request, HttpServletResponse response, IAccreditDao accDao, IAccreditRelDao relDao, Store store, Session session) {
		String id = request.getParameter("wfId");
		String licenseNo = request.getParameter("wflicenseNo");
		String linceseAuthor = request.getParameter("wflinceseAuthor");
		String startTime = request.getParameter("wfstartTime");
		String endTime = request.getParameter("wfendTime");
		String scale = request.getParameter("wfscale");
		String areaId = request.getParameter("wfareaId");

		String address = request.getParameter("wfaddress");
		// wflicenseFile wflicenseTwoFile
		String wfDetails = request.getParameter("wfDetails");

		Accredit accredit = new Accredit();
		accredit.setId(id);
		accredit.setLicenseNo(licenseNo);
		accredit.setLinceseAuthor(linceseAuthor);
		accredit.setStartTime(startTime);
		accredit.setEndTime(endTime);
		accredit.setScale(scale);
		accredit.setAreaId(areaId);
		accredit.setAddress(address);
		String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
		// 调用方法获取分类图标，返回图标路径
		String licensePath = FileUploadHelper.uploadFile("wflicenseFile", currentPath, request, response);
		String licenseTwoPath = FileUploadHelper.uploadFile("wflicenseTwoFile", currentPath, request, response);
		accredit.setLicenseFile(licensePath);
		accredit.setLicenseTwoFile(licenseTwoPath);
		accredit.setStoreId(store.getId());
		accredit.setStoreName(store.getStoreName());
		accredit.setDetailModel(Wfpml.class.getName());
		accredit.setRzType(ACC_RZTYPE_WF);
		accredit.setCheckStatus(false);
		if (StringUtil.isEmpty(id)) {
			accredit.setCreateTime(DateUtil.getSysDateTimeString());
		}
		accredit.setUpdateTime(DateUtil.getSysDateTimeString());

		try {

			if (session != null) {
				if (StringUtil.isEmpty(id)) {
					accDao.addBean(accredit, session);
				} else {
					accDao.updateBean(accredit, session);
				}
			} else {
				if (StringUtil.isEmpty(id)) {
					accDao.addBean(accredit);
				} else {
					accDao.updateBean(accredit);
				}
			}

			if (!StringUtil.isEmpty(wfDetails)) {
				String[] detailIds = wfDetails.split(";");
				String sql = " from AccreditRel as ar where 1=1 and ar.accreditId = :accreditId";
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("accreditId", accredit.getId());
				if (session != null) {
					relDao.delete(sql, paramMap, session);
				} else {
					relDao.delete(sql, paramMap);
				}

				for (int i = 0; i < detailIds.length; i++) {
					String detailId = detailIds[i];
					if (StringUtil.isEmpty(detailId)) {
						continue;
					}
					AccreditRel rel = new AccreditRel();
					rel.setAccreditId(accredit.getId());
					rel.setDetailId(detailId);
					rel.setPosition(i);
					if (session != null) {
						relDao.addBean(rel, session);
					} else {
						relDao.addBean(rel);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 从页面获取排放污染物的对象并保存
	 * 
	 * @param request
	 * @return
	 */
	public static void saveOrUpdatePwAccredit(HttpServletRequest request, HttpServletResponse response, IAccreditDao accDao, Store store, Session session) {
		String id = request.getParameter("pwId");
		String licenseNo = request.getParameter("pwlicenseNo");
		String linceseAuthor = request.getParameter("pwlinceseAuthor");
		String startTime = request.getParameter("pwstartTime");
		String endTime = request.getParameter("pwendTime");
		String scale = request.getParameter("pwscale");
		String areaId = request.getParameter("pwareaId");
		String address = request.getParameter("pwaddress");

		Accredit accredit = new Accredit();
		accredit.setId(id);
		accredit.setLicenseNo(licenseNo);
		accredit.setLinceseAuthor(linceseAuthor);
		accredit.setStartTime(startTime);
		accredit.setEndTime(endTime);
		accredit.setScale(scale);
		accredit.setAreaId(areaId);
		accredit.setAddress(address);
		String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
		// 调用方法获取分类图标，返回图标路径
		String licensePath = FileUploadHelper.uploadFile("pwlicenseFile", currentPath, request, response);
		String licenseTwoPath = FileUploadHelper.uploadFile("pwlicenseTwoFile", currentPath, request, response);
		accredit.setLicenseFile(licensePath);
		accredit.setLicenseTwoFile(licenseTwoPath);
		accredit.setStoreId(store.getId());
		accredit.setStoreName(store.getStoreName());
		accredit.setRzType(ACC_RZTYPE_PW);
		accredit.setCheckStatus(false);
		if (StringUtil.isEmpty(id)) {
			accredit.setCreateTime(DateUtil.getSysDateTimeString());
		}
		accredit.setUpdateTime(DateUtil.getSysDateTimeString());

		try {

			if (session != null) {
				if (StringUtil.isEmpty(id)) {
					accDao.addBean(accredit, session);
				} else {
					accDao.updateBean(accredit, session);
				}
			} else {
				if (StringUtil.isEmpty(id)) {
					accDao.addBean(accredit);
				} else {
					accDao.updateBean(accredit);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 从页面获取运输资质的对象并保存
	 * 
	 * @param request
	 * @return
	 */
	public static void saveOrUpdateYSAccredit(HttpServletRequest request, HttpServletResponse response, IAccreditDao accDao,IWfpysRelDao relDao, Store store, Session session) {
		String id = request.getParameter("ysId");
		String licenseNo = request.getParameter("yslicenseNo");
		String linceseAuthor = request.getParameter("yslinceseAuthor");
		String startTime = request.getParameter("ysstartTime");
		String endTime = request.getParameter("ysendTime");
		String scale = request.getParameter("ysscale");
		String areaId = request.getParameter("ysareaId");
		String address = request.getParameter("ysaddress");
		String wfysDetails = request.getParameter("wfysDetails");

		Accredit accredit = new Accredit();
		accredit.setId(id);
		accredit.setLicenseNo(licenseNo);
		accredit.setLinceseAuthor(linceseAuthor);
		accredit.setStartTime(startTime);
		accredit.setEndTime(endTime);
		accredit.setScale(scale);
		accredit.setAreaId(areaId);
		accredit.setAddress(address);
		String currentPath = "/customerfiles/store/" + store.getId() + "/" + DateUtil.getNoSpSysDateString();
		// 调用方法获取分类图标，返回图标路径
		String licensePath = FileUploadHelper.uploadFile("yslicenseFile", currentPath, request, response);
		accredit.setLicenseFile(licensePath);
		accredit.setStoreId(store.getId());
		accredit.setStoreName(store.getStoreName());
		accredit.setRzType(ACC_RZTYPE_YS);
		accredit.setCheckStatus(false);
		if (StringUtil.isEmpty(id)) {
			accredit.setCreateTime(DateUtil.getSysDateTimeString());
		}
		accredit.setUpdateTime(DateUtil.getSysDateTimeString());
		try {

			if (session != null) {
				if (StringUtil.isEmpty(id)) {
					accDao.addBean(accredit, session);
				} else {
					accDao.updateBean(accredit, session);
				}
			} else {
				if (StringUtil.isEmpty(id)) {
					accDao.addBean(accredit);
				} else {
					accDao.updateBean(accredit);
				}
			}
			if (!StringUtil.isEmpty(wfysDetails)) {
				String[] detailIds = wfysDetails.split(";");
				String sql = " from WfpysRel as ar where 1=1 and ar.accreditId = :accreditId";
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("accreditId", accredit.getId());
				if (session != null) {
					relDao.delete(sql, paramMap, session);
				} else {
					relDao.delete(sql, paramMap);
				}

				for (int i = 0; i < detailIds.length; i++) {
					String detailId = detailIds[i];
					if (StringUtil.isEmpty(detailId)) {
						continue;
					}
					WfpysRel rel = new WfpysRel();
					rel.setAccreditId(accredit.getId());
					rel.setWfpysId(detailId);
					rel.setPosition(i);
					if (session != null) {
						relDao.addBean(rel, session);
					} else {
						relDao.addBean(rel);
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
}
