package com.ekfans.controllers.sfdbigData;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.store.model.Store;
import com.ekfans.base.store.service.IAccreditService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

@Controller
public class SfdBigDataController {
	@Resource
	private IStoreService storeService;
	@Autowired
	private IAccreditService accreditService;
    /**
	 * 三分地环保大数据平台获取企业数据
	 * @param req
	 * @return
	 */
    @RequestMapping(value ="/company/datasynchro/{pageNum}")
    @ResponseBody
    public List<Company> indexMaill(HttpServletRequest req,@PathVariable String pageNum){
    	// 定义分页
    	Pager pager = new Pager();
    	int currentPage = 1;
    	if (!StringUtil.isEmpty(pageNum)) {
    		try {
    			currentPage = Integer.parseInt(pageNum);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	pager.setCurrentPage(currentPage);
    	List<Store> list = this.storeService.getStoreList(pager,1);
    	return Company.getListCompany(list,pager,accreditService,req);
    }
    /**
	 * 三分地环保大数据平台 同步数据
	 * @param req
	 * @return
	 */
    @RequestMapping(value ="/company/datasynchro/sava/{id}")
    @ResponseBody
    public void datasynchroQuery(HttpServletRequest req,@PathVariable String id){
    	Store store = this.storeService.getStore(id);
    	store.setIsDataSynchro("1");
    	this.storeService.update(store);
    }
}
