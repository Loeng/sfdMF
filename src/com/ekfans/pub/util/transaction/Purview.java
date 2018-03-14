package com.ekfans.pub.util.transaction;
/**
 * 三分地后台管理系统待处理事项枚举
 * 1:用于记录三分地后台管理系统需要处理的事项的ID,事项名称以及访问路径,
 *   (1)ID为数据库表'shop_purview' 的 'ID'字段,
 *   (2)事项名称为数据库表'shop_purview' 的 'CLASS_NAME'字段,
 *   (3)访问路径为数据库表'shop_purview' 的 'FULL_PATH'字段;
 * 2:Purview作为方法参数用于发送后台待处理事项;
 * 3:Purview作为方法参数用于获取后台待处理事项;
 * @author pp
 * @date 2017年9月11日14:40:54
 *
 */
public enum Purview {
  	CYSH("危废车源审核","WFYS_CLGL","/sysytem/wftransport/getListToSystem/0"),
  	HYSH("危废货源审核","WFYS_HYGL","/sysytem/wftransport/getListToSystem/1"),
  	WLB_ZZRZ("物流宝资质认证审核","WLB_ZZRZ_LIST","/system/wlbzzrz/list"),
  	CPSH("成品审核","PRODUCT_CP_CHECK","/system/product/checkList/0"),
  	CP_QGSH("成品管理-求购审核","PRODUCT_QG_AUDIT","/system/supplyBuy/checkList/0/1/1"),
  	GQ_QGSH("供求管理-求购审核","PRODUCT_GX_QG_CHECK","/system/supplyBuy/checkList/1/1/1"),
  	GYSH("供应审核","PRODUCT_GX_GY_CHECK","/system/supplyBuy/checkList/1/0/1"),
  	GXSH("供需审核","PRODUCT_GX_WFP_CHECK","/system/supplyBuy/checkList/2"),
  	SPSH("商品审核","GREEN_MALL_GL_SH","/system/product/checkList/2"),
  	BFSH("危废报废审核","ORDER_WFP_PF_CHECKLIST","/system/wfpSb/list"),
  	JCXXRZ("基础信息认证","STORE_RZ_QY_JC","/system/store/basic/jumplist"),
  	YHRZ("银行认证","STORE_RZ_QY_BANK","/system/store/bank/jumplist"),
  	CZZZRZ("处置资质认证","STORE_RZ_QY_CG","/system/store/zizhi/jumplist/cz"),
  	CSZZRZ("产生资质认证","STORE_RZ_QY_CS","/system/store/zizhi/jumplist/cs"),
  	YSZZRZ("运输资质认证","STORE_RZ_QY_YS","/system/store/zizhi/jumplist/ys"),
  	YSCLSH("运输车辆审核","STORE_RZ_YS_LIST","/system/store/transport/car/listSytem"),
  	JSYSH("驾驶员审核","STORE_RZ_DRIVER_LIST","/system/store/driver/listSytem/0"),
  	YSYSH("押运员审核","STORE_RZ_YY_LIST","/system/store/driver/listSytem/1"),
  	ZHBDSH("账户绑定审核","FINANCE_ACCOUNT_XN_BD","/system/account/checklist"),
  	WLB_LYFK("物流宝留言反馈","WLB_LYFK_LIST","/system/wlblyfk/list"),;
  // 权限名称
  private String purviewName; 
  // 权限ID
  private String purviewID;
  // 访问地址
  private String fullPath;
  private Purview(String purviewName,String purviewID,String fullPath) {
	 this.purviewName=purviewName;
	 this.purviewID=purviewID;
	 this.fullPath=fullPath;
  }
  public String getPurviewName() {
	return purviewName;
  }
  public void setPurviewName(String purviewName) {
	this.purviewName = purviewName;
  }
  public String getPurviewID() {
	return purviewID;
  }
  public void setPurviewID(String purviewID) {
	this.purviewID = purviewID;
  }
  public String getFullPath() {
	return fullPath;
  }
  public void setFullPath(String fullPath) {
	this.fullPath = fullPath;
  }
}
