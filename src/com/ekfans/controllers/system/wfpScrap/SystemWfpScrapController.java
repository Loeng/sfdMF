package com.ekfans.controllers.system.wfpScrap;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Accredit;
import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccreditService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.wfOrder.model.ScrapWfp;
import com.ekfans.base.wfOrder.model.ScrapWfpTrans;
import com.ekfans.base.wfOrder.service.IScrapWfpService;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SystemWfpScrapController extends BasicController {
	
	@Autowired
	private IScrapWfpService swService;
	@Autowired
	private IScrapWfpService wfpService;
	@Autowired
	private IStoreService storeService;
	@Autowired
	private IAccreditService accreditService;
	
	@RequestMapping(value = "/system/order/shenBao/listSytem")
	public String getSBListSystem(String contractName,
			String contractNo,String wfName,String checkStatus,
			HttpServletRequest request,HttpServletResponse response){
		Pager pager = new Pager();
		// 从页面获取页码
        String currentpageStr = request.getParameter("pageNum");
        // 将从页面获取的分页数据转化成int型
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentpageStr)) {
            currentPage = Integer.parseInt(currentpageStr);
        }
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        pager.setRowsPerPage(10);
        
        List<ScrapWfp> list=new ArrayList<ScrapWfp>();
        list=swService.getWfpList(null, contractName, contractNo, wfName, checkStatus, null, null, pager);
		
        List<Store> buyList=new ArrayList<Store>();
        for(int i=0;i<list.size();i++){
        	Store s1=storeService.getStore(list.get(i).getBuyId());
        	Store s2=storeService.getStore(list.get(i).getSalId());
        	list.get(i).setBuyName(s1.getStoreName());
        	list.get(i).setSalName(s2.getStoreName());
        }
        request.setAttribute("sbList", list);
        request.setAttribute("buyList", buyList);
		request.setAttribute("pager", pager);
        request.setAttribute("currentpageStr", currentpageStr);
		request.setAttribute("contractName", contractName);
		request.setAttribute("contractNo", contractNo);
		request.setAttribute("wfName", wfName);
		request.setAttribute("checkStatus", checkStatus);
		
		return "/system/order/shenBao/sbList";
	}
	
	@RequestMapping(value = "/system/order/shenBao/getSBById/{type}")
	public String getSBById(@PathVariable String type,String sbId,
			HttpServletRequest request){
		if(StringUtil.isEmpty(sbId)){
			return null;
		}
		ScrapWfp sb=swService.getScrapWfpById(sbId);
		List<ScrapWfpTrans> childs = wfpService.getTransByWfpId(sb.getId());
		List<ScrapWfpTrans> trans = new ArrayList<ScrapWfpTrans>();
		for (int i = 0; i < childs.size(); i++) {
			ScrapWfpTrans scratTrans = childs.get(i);
			if (scratTrans != null) {
				Store transStore = storeService.getStore(scratTrans.getTransId());
				Accredit transAcc = accreditService.getAccreditByStoreAndType(transStore.getId(), "2");
				transStore.setTransAccredit(transAcc);
				scratTrans.setTransStore(transStore);
				trans.add(scratTrans);
			}
		}

		Store buyStore = storeService.getStore(sb.getBuyId());
		Accredit buyAcc = accreditService.getAccreditByStoreAndType(buyStore.getId(), "0");
		buyStore.setBuyerAccredit(buyAcc);
		Store salStore = storeService.getStore(sb.getSalId());
		sb.setChildList(trans);
		request.setAttribute("salStore", salStore);
		request.setAttribute("buyStore", buyStore);
		request.setAttribute("scrapWfp", sb);
		
		
		if(type.equals("1")){
			request.setAttribute("type", "1");
		}else{
			request.setAttribute("type", "2");
		}
		return "/system/order/shenBao/sbShowAndCheck";
	}
	@RequestMapping(value = "/system/order/shenBao/checkSB")
	@ResponseBody
	public String checkSB(String id,HttpServletRequest request, HttpServletResponse response){
		if(StringUtil.isEmpty(id)){
			return "0";
		}
		if(swService.Check(id, request, response)){
			return "1";
		}
		return "0";
	}
}
