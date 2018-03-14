package com.ekfans.controllers.sfdbigData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccreditService;
import com.ekfans.base.store.util.AccreditHelper;
import com.ekfans.basic.hibernate.model.BasicBean;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.WebUtil;

/**
 * 企业信息--实体类
 *
 * @author 成都易科远见科技有限公司
 * @ClassName: Manager
 * @Copyright: Copyright (c) Chengdu Ekfans Technology Co.,Ltd
 * @Company:成都易科远见科技有限公司 www.ekfans.com
 * @date 2017年10月18日15:26:49
 */
public class Company extends BasicBean {
    private static final long serialVersionUID = 1L;
    // 企业名称
    private String name = "";
    //企业LOGO
    private String logo = "";
    //信用编码
    private String creditCode = "";
    //企业网站
    private String doman = "";
    //企业类型
    private String companyType = "";
    //联系人
    private String contentMan = "";
    //联系电话
    private String contentMobile = "";
    //联系邮箱
    private String contentEmail = "";
    //企业法人
    private String legalPerson = "";
    //企业注册所属区域ID
    private String legalArea = "";
    //企业注册地
    private String legalAddress = "";
    //企业注册资金
    private BigDecimal legalFund = new BigDecimal(0.00);
    //注册资金单位
    private String monetaryUnit= "";
    //经营范围
    private String manageScope = "";
    //经营区域
    private String area = "";
    //经营地址
    private String address = "";
    // 危废处置认证状态
    private String czAuthStatus = "0";
    // 处置许可证
    private String czPic = "";
    // 危废运输认证状态
    private String ysAuthStatus = "0";
    // 运输许可证
    private String ysPic = "";
    // 排污认证状态
    private String pwAuthStatus = "0";
    // 排污许可证
    private String pwPic = "";
    // 排污详细
    private String pwDetail = "";
    //环保行业
    private String industryName = "";
    //仓库数
    private int hourse = 0;
    //车辆数
    private int cars = 0;
    //主营业务
    private String mainNote = "";
    //企业简介
    private String note = "";
    //融资状态
    private String financStatus = "";
    //总交易额
    private BigDecimal totalTurnover = new BigDecimal(0);
    //资金流向
    private String moneyFlow = "";
    //信用评级
    private String creditRating = "";
    //来源
    private String source = "三分地环保产业链平台";
    //来源ID
    private String sourceId = "";
    //创建时间
    private String createTime = "";
    //状态 0:冻结  1:正常
    private String status = "1";
    // 经营地区fullNmae
    private String areaFullName = "";
    
    // ----------------虚拟数据------------------
    // 危废名录
    private String wfmlChose = "";
    // 危废运输
    private String wfysChose = "";
    //总页数
    private int totalPage = 0;
    //总条数
    private int totalRow = 0;

    
    public static List<Company> getListCompany(List<Store> storeList,Pager pager,IAccreditService accreditService,HttpServletRequest request){
    	List<Company> companyList = new ArrayList<Company>();
    	for(Store store : storeList) {
    		Company company = new Company();
    		company.setId(store.getId());
    		company.setName(store.getStoreName());
    		company.setCreateTime(store.getCreateTime());
    		company.setDoman(store.getDomain());
    		if(store.getUnitType()!=null) {
    			if(store.getUnitType()==1) {
        			company.setCompanyType("个人独资企业");
        		}else if(store.getUnitType()==2){
        			company.setCompanyType("合伙企业");
        		}	
    		}
    		company.setAddress(store.getMailingAddress());
    		company.setContentMan(store.getContactName());
    		company.setContentMobile(store.getContactPhone());
    		company.setLogo(WebUtil.getContextAddress(request)+store.getStoreLogo());
    		company.setNote(store.getNotes());
    		company.setLegalFund(new BigDecimal(store.getRegCapital()));
    		company.setMonetaryUnit("万元(人民币)");
    		company.setManageScope(store.getBusinessScope());
    		if(store.getBuyerStatus().equals("3")) {
    			company.setCzAuthStatus("1");
    			// 资质类型 - 危险废物经营许可
    			Accredit wfjy = accreditService.getAccreditByStoreAndType(store.getId(), AccreditHelper.ACC_RZTYPE_WF);
    			company.setCzPic(WebUtil.getContextAddress(request)+wfjy.getLicenseFile());
    			company.setWfmlChose(wfjy.getChildIds());
    			company.setArea(wfjy.getAreaId());
    			// 资质类型 - 排放污染物许可
    			Accredit wfpw = accreditService.getAccreditByStoreAndType(store.getId(), AccreditHelper.ACC_RZTYPE_PW);
    			if(wfpw.isCheckStatus()) {
    			   company.setPwAuthStatus("1");
    			   company.setPwPic(WebUtil.getContextAddress(request)+wfpw.getLicenseFile());
    			}
    		
    		}
    		if(store.getTransStatus().equals("3")) {
    			company.setYsAuthStatus("1");
    			// 资质类型 - 道路运输经营许可
    			Accredit wfys = accreditService.getAccreditByStoreAndType(store.getId(), AccreditHelper.ACC_RZTYPE_YS);
    			company.setYsPic(WebUtil.getContextAddress(request)+wfys.getLicenseFile());
    			company.setWfysChose(wfys.getChildIds());
    		}
    		company.setTotalPage(pager.getTotalPage());
    		company.setTotalRow(pager.getTotalRow());
    		companyList.add(company);
    	}
    	return companyList;
    	
    }
    public static Company getCompany(Store store){
    	Company company = new Company();
        company.setId(store.getId());
    	company.setName(store.getStoreName());
    	company.setCreateTime(store.getCreateTime());
    	return company;
    }
    
    public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getDoman() {
        return doman;
    }

    public void setDoman(String doman) {
        this.doman = doman;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getContentMan() {
        return contentMan;
    }

    public void setContentMan(String contentMan) {
        this.contentMan = contentMan;
    }

    public String getContentMobile() {
        return contentMobile;
    }

    public void setContentMobile(String contentMobile) {
        this.contentMobile = contentMobile;
    }

    public String getContentEmail() {
        return contentEmail;
    }

    public void setContentEmail(String contentEmail) {
        this.contentEmail = contentEmail;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getLegalArea() {
        return legalArea;
    }

    public void setLegalArea(String legalArea) {
        this.legalArea = legalArea;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public BigDecimal getLegalFund() {
        return legalFund;
    }

    public String getManageScope() {
        return manageScope;
    }

    public void setManageScope(String manageScope) {
        this.manageScope = manageScope;
    }

    public void setLegalFund(BigDecimal legalFund) {
        this.legalFund = legalFund;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getHourse() {
        return hourse;
    }

    public void setHourse(int hourse) {
        this.hourse = hourse;
    }

    public int getCars() {
        return cars;
    }

    public void setCars(int cars) {
        this.cars = cars;
    }

    public String getMainNote() {
        return mainNote;
    }

    public void setMainNote(String mainNote) {
        this.mainNote = mainNote;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFinancStatus() {
        return financStatus;
    }

    public void setFinancStatus(String financStatus) {
        this.financStatus = financStatus;
    }

    public BigDecimal getTotalTurnover() {
        return totalTurnover;
    }

    public void setTotalTurnover(BigDecimal totalTurnover) {
        this.totalTurnover = totalTurnover;
    }

    public String getMoneyFlow() {
        return moneyFlow;
    }

    public void setMoneyFlow(String moneyFlow) {
        this.moneyFlow = moneyFlow;
    }

    public String getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	public String getCzAuthStatus() {
		return czAuthStatus;
	}

	public void setCzAuthStatus(String czAuthStatus) {
		this.czAuthStatus = czAuthStatus;
	}

	public String getYsAuthStatus() {
		return ysAuthStatus;
	}

	public void setYsAuthStatus(String ysAuthStatus) {
		this.ysAuthStatus = ysAuthStatus;
	}

	public String getPwAuthStatus() {
		return pwAuthStatus;
	}

	public void setPwAuthStatus(String pwAuthStatus) {
		this.pwAuthStatus = pwAuthStatus;
	}

	public String getPwDetail() {
		return pwDetail;
	}

	public void setPwDetail(String pwDetail) {
		this.pwDetail = pwDetail;
	}

	public String getCzPic() {
		return czPic;
	}

	public void setCzPic(String czPic) {
		this.czPic = czPic;
	}

	public String getYsPic() {
		return ysPic;
	}

	public void setYsPic(String ysPic) {
		this.ysPic = ysPic;
	}

	public String getPwPic() {
		return pwPic;
	}

	public void setPwPic(String pwPic) {
		this.pwPic = pwPic;
	}

	public String getWfmlChose() {
		return wfmlChose;
	}

	public void setWfmlChose(String wfmlChose) {
		this.wfmlChose = wfmlChose;
	}

	public String getWfysChose() {
		return wfysChose;
	}

	public void setWfysChose(String wfysChose) {
		this.wfysChose = wfysChose;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getAreaFullName() {
		return areaFullName;
	}

	public void setAreaFullName(String areaFullName) {
		this.areaFullName = areaFullName;
	}

	public String getMonetaryUnit() {
		return monetaryUnit;
	}

	public void setMonetaryUnit(String monetaryUnit) {
		this.monetaryUnit = monetaryUnit;
	}
	
}