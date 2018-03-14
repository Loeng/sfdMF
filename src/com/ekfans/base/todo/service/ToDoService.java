package com.ekfans.base.todo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.order.service.ISupplyBuyService;
import com.ekfans.base.product.service.IProductService;
import com.ekfans.base.store.service.IAccountBankService;
import com.ekfans.base.store.service.ICarInfoService;
import com.ekfans.base.store.service.ICarUserService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.store.service.IWftransportService;
import com.ekfans.base.todo.model.ToDoModel;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppAptitude;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IMessageBackService;
import com.ekfans.pub.util.transaction.Purview;
@Service
public class ToDoService implements IToDoService{
	@Autowired
	private IWlbAppAptitude apti;
	@Autowired
	private IWftransportService wftransporService;
	@Autowired
	private IProductService productService;
	@Autowired
	private ISupplyBuyService supplyBuyService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private ICarInfoService carInfoService;
	@Autowired
	private ICarUserService userService;
	@Autowired
	private IAccountBankService accountBankService;
	@Autowired
	private IMessageBackService messageService;
	@Override
	public ToDoModel getCheCheck() {
		ToDoModel todo = new ToDoModel();
		todo.setNum(wftransporService.getSysList(null, null, null, null, "0", "", "0", null).size());
		todo.setExplain("危废车源审核");
		todo.setPurview(Purview.CYSH);
		return todo;
	}

	@Override
	public ToDoModel getHuoCheck() {
		ToDoModel todo = new ToDoModel();
		todo.setNum(wftransporService.getSysList(null, null, null, null, "1", "", "0", null).size());
		todo.setExplain("危废货源审核");
		todo.setPurview(Purview.HYSH);
		return todo;
	}

	@Override
	public ToDoModel getWLBZZRZCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(apti.getAptitudeNum());
		todo.setExplain("物流宝资质认证审核");
		todo.setPurview(Purview.WLB_ZZRZ);
		return todo;
	}

	/**
	  * 获取成品审核的待办事项
	  * @return
	  * @throws Exception
	  */
	public ToDoModel getCPCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(productService.listUncheckProduct(null, null, null, null, null, null, null, null, Cache.getResource("cengpinProduct")).size());
		todo.setExplain("成品审核");
		todo.setPurview(Purview.CPSH);
		return todo;
	}

	 /**
	  * 获取成品管理-求购审核的待办事项
	  * @return
	  * @throws Exception
	  */
	public ToDoModel getCPQGCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		//todo.setNum(supplyBuyService.getSupplyBuyCheckNum("0",1));
		todo.setNum(supplyBuyService.listSupplyBuy(null, null, null, null, null, "1", "1", "0", "1", "0", null).size());
		todo.setExplain("成品管理-求购审核");
		todo.setPurview(Purview.CP_QGSH);
		return todo;
	}

	/**
	  * 获取供应管理-求购审核的待办事项
	  * @return
	  * @throws Exception
	  */
	public ToDoModel getGQQGCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(supplyBuyService.listSupplyBuy(null, null, null, null, null, "1", "1", "1", "1", "0", null).size());
		todo.setExplain("供求管理-求购审核");
		todo.setPurview(Purview.GQ_QGSH);
		return todo;
	}

	@Override
	public ToDoModel getGQCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(supplyBuyService.listSupplyBuy(null, null, null, null, null, "1", "0", "1", "1", "0", null).size());
		todo.setExplain("供求管理-供应审核");
		todo.setPurview(Purview.GYSH);
		return todo;
	}

	//后台暂时没有 没写
	public ToDoModel getGXCheck() throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ToDoModel getSPCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(productService.listUncheckProduct(null, null, null, null, null, null, null, null, Cache.getResource("cengpinProduct")).size());
		todo.setExplain("商品审核");
		todo.setPurview(Purview.SPSH);
		return todo;
	}

	//后台暂时没有 没写
	public ToDoModel getWFBFCheck() throws Exception {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ToDoModel getJCCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(storeService.getCheckStore(null, null, 1).size());
		todo.setExplain("基础信息认证");
		todo.setPurview(Purview.JCXXRZ);
		return todo;
	}

	@Override
	public ToDoModel getYHCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(storeService.getCheckStore(null, null, 2).size());
		todo.setExplain("银行认证");
		todo.setPurview(Purview.YHRZ);
		return todo;
	}

	@Override
	public ToDoModel getCZCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(storeService.getStoreUnCheckList("cz", null, null, null).size());
		todo.setExplain("处置资质认证");
		todo.setPurview(Purview.CZZZRZ);
		return todo;
	}

	@Override
	public ToDoModel getCSCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(storeService.getStoreUnCheckList("cs", null, null, null).size());
		todo.setExplain("产生资质认证");
		todo.setPurview(Purview.CSZZRZ);
		return todo;
	}

	@Override
	public ToDoModel getYSCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(storeService.getStoreUnCheckList("ys", null, null, null).size());
		todo.setExplain("运输资质认证");
		todo.setPurview(Purview.YSZZRZ);
		return todo;
	}

	@Override
	public ToDoModel getYSCarCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(carInfoService.getCarInfoSystem(null, null,null, null).size());
		todo.setExplain("运输车辆审核");
		todo.setPurview(Purview.YSCLSH);
		return todo;
	}

	@Override
	public ToDoModel getJSYCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(userService.getCarUserByPageSystem("0", null, null,null, null).size());
		todo.setExplain("驾驶员审核");
		todo.setPurview(Purview.JSYSH);
		return todo;
	}

	@Override
	public ToDoModel getYYYCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(userService.getCarUserByPageSystem("1", null, null,null, null).size());
		todo.setExplain("押运员审核");
		todo.setPurview(Purview.YSYSH);
		return todo;
	}

	@Override
	public ToDoModel getAccountCheck() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(accountBankService.getCheckList(null, null).size());
		todo.setExplain("账户绑定审核");
		todo.setPurview(Purview.ZHBDSH);
		return todo;
	}

	@Override
	public ToDoModel getWLBlyfk() throws Exception {
		ToDoModel todo = new ToDoModel();
		todo.setNum(messageService.getMessageBackList(null,null,null).size());
		todo.setExplain("反馈管理");
		todo.setPurview(Purview.WLB_LYFK);
		return todo;
	}

}
