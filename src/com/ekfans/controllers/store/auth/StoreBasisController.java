package com.ekfans.controllers.store.auth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.model.StoreFinancialData;
import com.ekfans.base.store.model.StoreLegal;
import com.ekfans.base.store.model.StoreLegalResume;
import com.ekfans.base.store.service.IStoreFinancialDataService;
import com.ekfans.base.store.service.IStoreLegalResumeService;
import com.ekfans.base.store.service.IStoreLegalService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.service.IAreaService;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.store.auth.VO.AttributeVO;
import com.ekfans.controllers.tools.util.FileUploadHelper;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.StringUtil;

@Controller
@Scope("prototype")
public class StoreBasisController extends BasicController {

	@Resource
	private IStoreService storeService;
	@Resource
	private IStoreLegalService storeLegalService;
	@Resource
	private IStoreLegalResumeService storeLegalResumeService;
	@Resource
	private IStoreFinancialDataService storeFinancialDataService;
	@Resource
	private IAreaService areaService;

	/**
	 * 验证企业名称时候重复
	 * 
	 * @return true：有该用户，false：没有
	 */
	@RequestMapping(value = "/store/auth/checkCenterStoreName")
	@ResponseBody
	public Boolean checkCenterStoreName(HttpServletRequest request) {
		String old = request.getParameter("old");
		String nez = request.getParameter("nez");

		return this.storeService.checkStoreNameUpdate(old, nez);
	}

	/**
	 * 跳转到企业基础认证页面
	 */
	@RequestMapping(value = "/store/auth/basis")
	public String jumpStoreBasisInfoPage() {
		String storeId = ((Store) getSession().getAttribute(SystemConst.STORE)).getId();

		// 根据企业id查询企业基础信息，并把企业基础信息传递给页面
		getRequest().setAttribute("tstore", storeService.getStore(storeId));
		// 获取中国全部大区名称
		getRequest().setAttribute("alist", areaService.getAllArea());

		return "/userCenter/store/authenticate/store_basis";
	}

	/**
	 * 更新或保存企业基础信息
	 * 
	 * @return 1：成功，2：失败，3：上传企业LOGO，4：上传营业执照，5：企业名称不能为空
	 */
	@RequestMapping(value = "/store/auth/saveBasisInfo")
	@ResponseBody
	public int updateStoreBasisInfo(@ModelAttribute Store store, HttpServletRequest request, HttpServletResponse response) {
		Store dataStore = storeService.getStore(store.getId());
		// 企业LOGO
		String logo = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		logo = FileUploadHelper.uploadFile("storeLogo", logo, getRequest(), response);
		// 营业执照
		String blc = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		blc = FileUploadHelper.uploadFile("businessLicense", blc, getRequest(), response);
		// 信用代码证
		String creditCodeCard = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		creditCodeCard = FileUploadHelper.uploadFile("creditCodeCard", creditCodeCard, getRequest(), response);
		// 公司章程
		String articles = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		articles = FileUploadHelper.uploadFile("articles", articles, getRequest(), response);
		// 公司简介(WORD版)
		String synopsis = "/customerfiles/company/logo/" + DateUtil.getNoSpSysDateString() + "/";
		synopsis = FileUploadHelper.uploadFile("synopsis", synopsis, getRequest(), response);

		dataStore.setArticles(articles);
		dataStore.setSynopsis(synopsis);
		dataStore.setCreditCodeCard(creditCodeCard);
		dataStore.setStoreLogo(logo);
		dataStore.setBusinessLicense(blc);
		dataStore.setStoreRefer(store.getStoreRefer());
		dataStore.setAreaId(store.getAreaId());
		dataStore.setRegTime(store.getRegTime());
		dataStore.setMailingAddress(store.getMailingAddress());
		dataStore.setUnitType(store.getUnitType());
		dataStore.setZipCode(store.getZipCode());
		dataStore.setAreaNumber(store.getAreaNumber());
		dataStore.setContactName(store.getContactName());
		dataStore.setContactPhone(store.getContactPhone());
		dataStore.setContactFax(store.getContactFax());
		dataStore.setRegCapital(store.getRegCapital());
		dataStore.setNumberEmployees(store.getNumberEmployees());
		dataStore.setBureau(store.getBureau());
		dataStore.setBusinessLicenseNumber(store.getBusinessLicenseNumber());
		dataStore.setBank(store.getBank());
		dataStore.setOpeningTime(store.getOpeningTime());
		dataStore.setOrgId(store.getOrgId());
		dataStore.setReditCard(store.getReditCard());
		dataStore.setBusinessScope(store.getBusinessScope());
		dataStore.setPlanning(store.getPlanning());
		dataStore.setCommonStatus("1");
		dataStore.setBureauTime(store.getBureauTime());
		dataStore.setUpdateTime(DateUtil.getSysDateTimeString());
		int i = storeService.updateStore(dataStore);
		if (i == 1) {
			getSession().removeAttribute(SystemConst.STORE);
			getSession().setAttribute(SystemConst.STORE, storeService.getStore(store.getId()));
		}
		return i;
	}

	/**
	 * 跳转到银行认证第一个页面
	 */
	@RequestMapping(value = "/store/auth/bank_one")
	public String jumpBankOnePage() {
		Store store = (Store) getSession().getAttribute(SystemConst.STORE);

		StoreLegal sl = storeLegalService.getStoreLegalById(store.getId());
		List<StoreLegalResume> slrlist = storeLegalResumeService.getStoreLegalResumeByStoreId(store.getId());

		getRequest().setAttribute("sl", sl == null ? new StoreLegal() : sl);
		getRequest().setAttribute("slrlist", slrlist);
		getRequest().setAttribute("tstore", storeService.getStore(store.getId()));

		return "/userCenter/store/authenticate/bank_one";
	}

	/**
	 * 更新或保存企业法人信息和法人简历信息
	 */
	@RequestMapping(value = "/store/auth/bank_one_save")
	@ResponseBody
	public boolean saveStoreLegalInfo(@ModelAttribute AttributeVO vo) {
		boolean status = storeLegalService.updateOrSaveStoreLegal(vo.getSl(), vo.getSlrlist());
		if (status) {
			getSession().removeAttribute(SystemConst.STORE);
			getSession().setAttribute(SystemConst.STORE, storeService.getStore(vo.getSl().getId()));
		}

		return status;
	}

	/**
	 * 跳转到银行认证第二个页面
	 */
	@RequestMapping(value = "/store/auth/bank_two")
	public String jumpBankTwoPage() {
		Store store = (Store) getSession().getAttribute(SystemConst.STORE);

		getRequest().setAttribute("sfdlist", storeFinancialDataService.getStoreFinancialDataByStoreId(store.getId()));
		getRequest().setAttribute("tstore", storeService.getStore(store.getId()));

		return "/userCenter/store/authenticate/bank_two";
	}

	/**
	 * 更新或保存财务信息
	 */
	@RequestMapping(value = "/store/auth/bank_two_save")
	@ResponseBody
	public boolean saveCaiWuInfo(@ModelAttribute AttributeVO vo) {
		SimpleDateFormat sdf = null;
		String storeId = getRequest().getParameter("id"); // 企业id
		String[] dType = getRequest().getParameterValues("dType"); // 类型
		String[] dsMoney = getRequest().getParameterValues("dType"); // 上年末
		String[] deMoney = getRequest().getParameterValues("deMoney"); // 本年上月末

		List<StoreFinancialData> list = new ArrayList<StoreFinancialData>();
		if (dType != null && dType.length > 0) {
			sdf = new SimpleDateFormat("yyyy");
			int year = Integer.valueOf(sdf.format(new Date())) - 1;

			for (int i = 0; i < dType.length; i++) {
				StoreFinancialData sfd = new StoreFinancialData();
				sfd.setStoreId(storeId);
				sfd.setDataTime(year + "");
				sfd.setDataType(dType[i]);
				try {
					sfd.setMoney(StringUtil.isEmpty(dsMoney[i]) ? 0.00 : Double.valueOf(dsMoney[i]));
				} catch (Exception e) {
					sfd.setMoney(0.00);
				}
				list.add(sfd);
			}

			sdf = new SimpleDateFormat("yyyy-MM");
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.MONTH, -1);
			for (int i = 0; i < dType.length; i++) {
				StoreFinancialData fd = new StoreFinancialData();
				fd.setStoreId(storeId);
				fd.setDataTime(sdf.format(ca.getTime()));
				fd.setDataType(dType[i]);
				try {
					fd.setMoney(StringUtil.isEmpty(deMoney[i]) ? 0.00 : Double.valueOf(deMoney[i]));
				} catch (Exception e) {
					fd.setMoney(0.00);
				}
				list.add(fd);
			}
		}

		boolean status = storeFinancialDataService.updateOrSaveStoreFinancialData(list);
		if (status) {
			getSession().removeAttribute(SystemConst.STORE);
			getSession().setAttribute(SystemConst.STORE, storeService.getStore(storeId));
		}

		return status;
	}
}
