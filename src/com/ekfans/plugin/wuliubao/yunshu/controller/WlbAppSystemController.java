package com.ekfans.plugin.wuliubao.yunshu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ekfans.base.ccwcc.model.CcwccPush;
import com.ekfans.base.ccwcc.service.ICcwccPushService;
import com.ekfans.base.content.model.AdDetail;
import com.ekfans.base.content.model.ShopAd;
import com.ekfans.base.content.service.IShopAdService;
import com.ekfans.base.store.service.IStoreService;
import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.basic.controller.BasicController;
import com.ekfans.controllers.ccwccApp.Response;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppAptitude;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppUserDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.plugin.wuliubao.yunshu.base.model.MessageBack;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Version;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IMessageBackService;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IVersionService;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IWlbAppAptitudeServicel;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddMessageBackForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.WlbUser;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.JGUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 物流宝系统相关控制器
 * @author pp
 * @Date 2017年7月23日19:08:51
 *
 */
@Controller
public class WlbAppSystemController extends BasicController{
	 // 定义错误日志
 	public static Logger log = LoggerFactory.getLogger(WlbAppSystemController.class);
	@Autowired
 	private IShopAdService adService;
	@Resource
	private IStoreService storeService;
	@Autowired
	private IWlbAppAptitudeServicel aptitudservice;
	@Autowired
	private IWlbAppAptitude apti;
	@Autowired
    ICcwccPushService pushService;
	@Autowired
	private IMessageBackService messageService;
	@Autowired
	private IWlbAppUserDao userdao;
	@Autowired
	private IVersionService versionService;
 	/**
 	 * 获取广告信息
 	 * @return
 	 */
	@RequestMapping(value="/wlbApp/getAd",method = RequestMethod.GET)
	@ResponseBody
	public Response getAd(){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			ShopAd ad = adService.getWlbShopAd();
			List<Object> adList = new ArrayList<Object>();
			if(ad==null) {
				map.put("number", 0);
				map.put("adList", adList);
				return new Response().success(map);
			}
			for(AdDetail a : ad.getDetailist()){
				Map<String,Object> m = new HashMap<String,Object>();
				m.put("link", a.getLinkAddress());
				m.put("picturePath", a.getAdPicture());
				adList.add(m);
			}
			map.put("number", ad.getDetailist().size());
			map.put("adList", adList);
			return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 跳转到三分地管理后台物流宝资质认证列表页面
	 */
	@RequestMapping(value = "/system/wlbzzrz/list")
	public String wlbApList(){
		String name = getRequest().getParameter("name");
		String nickName = getRequest().getParameter("nickName");
		String currentPageNum = getRequest().getParameter("pageNum");
		
		// 定义分页
		Pager pager = new Pager();
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentPageNum)) {
			try {
				currentPage = Integer.parseInt(currentPageNum);
			} catch (Exception e) {
				log.error("error：" + e.getMessage());
			}
		}
		pager.setCurrentPage(currentPage);
		
		try {
			getRequest().setAttribute("aplist", aptitudservice.getCheckUser(pager, name, nickName));
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error：" + e.getMessage());
		}
		getRequest().setAttribute("pager", pager);
		getRequest().setAttribute("name", name);
		getRequest().setAttribute("nickName", nickName);
		
		return "/system/store/auth/wlb_aptitude_list";
	}
	/**
	 * 显示用户认证详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/system/wlbzzrz/detail/{id}")
	public String wlbApDetails(@PathVariable String id,HttpServletRequest request){
		Aptitude ap =null;
		try {
			 Manager manager =(Manager) request.getSession().getAttribute(SystemConst.MANAGER);
			 request.getSession().setAttribute(SystemConst.MANAGER, manager);
			 ap = (Aptitude) apti.get(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("error：" + e.getMessage());
		}
		getRequest().setAttribute("ap", ap);
		return "/system/store/auth/wlb_aptitude_check";
	}
	/**
	 * 物流宝资质认证人工审核
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/system/wlbzzrz/check")
	public String wlbAppCheck(String id,String status,String remarks,HttpServletRequest request){
		try {
			aptitudservice.check(id, status, remarks,request);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return "0";
		}
		return "1";
	}
	
	/**
	 * 物流宝新建消息推送
	 * @return
	 */
	@RequestMapping(value = "/system/wlbpush/add")
    public String wlbGoAdd() {
        return "/system/wlb/wlbPushAdd";
    }
	
	/**
	 * 物流宝消息推送列表
	 * @return
	 */
	@RequestMapping(value = "/system/wlbpush/list")
    public String wlbList(HttpServletRequest request) {
	    String currentPageNo = request.getParameter("pageNum");
        String content = request.getParameter("content");
        String type = request.getParameter("type");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String pushStartTime = request.getParameter("pushStartTime");
        String pushEndTime = request.getParameter("pushEndTime");
        String status = request.getParameter("status");

        Pager pager = new Pager();
        int currentPage = 1;
        if (!StringUtil.isEmpty(currentPageNo)) {
            try {
                currentPage = Integer.parseInt(currentPageNo);
            } catch (Exception e) {
            }
        }

        List<CcwccPush> pushList = pushService.getCcwccPushList(pager, content, status, "0", startTime, endTime, pushStartTime, pushEndTime);
        // 设置要查询的页码
        pager.setCurrentPage(currentPage);
        request.setAttribute("content", content);
        request.setAttribute("type", type);
        request.setAttribute("startTime", startTime);
        request.setAttribute("endTime", endTime);
        request.setAttribute("pushStartTime", pushStartTime);
        request.setAttribute("pushEndTime", pushEndTime);
        request.setAttribute("pager", pager);
        request.setAttribute("pushList", pushList);
        request.setAttribute("status", status);
        return "/system/wlb/wlbPushList";
    }

     /**
     * 显示物流宝消息推送详情
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/system/wlbpush/edit/{id}")
    public String goEdit(@PathVariable String id, HttpServletRequest request) {
        CcwccPush push = pushService.getById(id);
        request.setAttribute("ccwccPush", push);
        return "/system/wlb/wlbPushEdit";
    }

    /**
     * 物流宝消息推送
     * @param ccwccPush
     * @param request
     * @param response
     */
    @RequestMapping(value = "/system/wlbpush/save")
    @ResponseBody
    public void saveOrUpdate(CcwccPush ccwccPush, HttpServletRequest request, HttpServletResponse response) {
        String pushNow = request.getParameter("pushNow");
        if(!ccwccPush.getType().equals("0")){
        	backWriteStr(response, "1");
        	return;
        }
        Boolean status = pushService.wlbSaveOrUpdate(ccwccPush, (!StringUtil.isEmpty(pushNow) && "1".equals(pushNow)) ? true : false,request);
        backWriteStr(response, status + "");
    }

    
	/**
	 * 添加留言反馈
	 * @param by
	 * @return
	 */
	@RequestMapping(value="/wlbApp/addMessageBack", produces = "application/json;charset=UTF-8",method=RequestMethod.POST)
	public @ResponseBody Response addMessageBack(@RequestBody byte[] by,HttpServletRequest request){
		
		try {
			AddMessageBackForm mf=(AddMessageBackForm) new AddMessageBackForm().getObject(by);
			MessageBack m=new MessageBack();
			m.setCreateTime(DateUtil.getSysDateTimeString());
			m.setContent(mf.content);
			//获取用户token
			String token = request.getHeader("WLB-Token");
			if(!StringUtil.isEmpty(token)){
				//通过token获取用户信息
				WlbUser user = (WlbUser) Cache.engine.get(token);
				m.setUserId(user.getUserId());
				m.setUserName(user.getName());
				m.setNickName(user.getNickName());
			}
			boolean b=messageService.addMessageBack(m);
			if(b){
				return new Response().success("添加成功!");
			}
			return new Response().failure("添加失败!");
			
		} catch (Exception e) {
			e.printStackTrace();
			return new Response().failure();
		} 

	}
	/**
	 * 后台管理进入物流宝留言反馈界面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/system/wlblyfk/list")
	public String getMessageBackList(HttpServletRequest request){
		String type = request.getParameter("type");
		String name = request.getParameter("name");
		Pager page = new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);
		try {
			List<MessageBack> list = messageService.getMessageBackList(type,name,page);
			request.setAttribute("pager", page);
			request.setAttribute("list", list);
			request.setAttribute("name", name);
			request.setAttribute("type", type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/system/wlb/lyfkList";
	}
	/**
	 * 三分地后台管理系统进入反馈详情
	 * @param request
	 * @param id 反馈ID
	 * @return
	 */
	@RequestMapping(value="/system/wlblyfk/feedback/{id}")
	public String feedback(HttpServletRequest request,@PathVariable String id){
	    try {
			MessageBack messageBack = messageService.getMessageBack(id);
			request.setAttribute("mess", messageBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/system/wlb/lyfk_feedback";
	}
	
	/**
	 * 三分地后台管理系统留言反馈信息致用户
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/system/wlblyfk/feedback")
	public String feedback(HttpServletRequest request){
	    try {
	    	String id = request.getParameter("id");
	    	String feedbackContent = request.getParameter("feedbackContent");
	    	Manager manager =(Manager) request.getSession().getAttribute(SystemConst.MANAGER);
			MessageBack messageBack = messageService.getMessageBack(id);
			messageBack.setFeedbackContent(feedbackContent);
			messageBack.setFeedbackID(manager.getRoleId());
			messageBack.setFeedbackMan(manager.getName());
			messageBack.setFeedbackTime(DateUtil.getSysDateTimeString());
			messageBack.setType("1");
			//获取用户
		    User user = (User) userdao.get(messageBack.getUserId());
		    if(user!=null&&!StringUtil.isEmpty(user.getRegistrationID())){
		    	//通过极光推送议价消息
	        	Map<String,String> dataNode = new HashMap<String,String>();
	        	//messageType:"4",为系统推送信息
	        	dataNode.put("messageType", "4");
	        	dataNode.put("id", messageBack.getId());
	            dataNode.put("content", messageBack.getFeedbackContent());
	        	dataNode.put("pushTime", DateUtil.getSysDateTimeString());
	        	//使用极光推送系统消息 
	        	JGUtil.wlbAPPsendMessages(user.getRegistrationID(), dataNode, "您有一条系统消息!");
	        	messageService.updateMessageBack(messageBack);
	        	return "1";
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "2";
	}
	/**
	 * 三分地后台管理系统物流宝版本信息列表
	 * @return
	 */
	@RequestMapping(value="/system/wlbversion/list")
	public String wlbVersionList(HttpServletRequest request){
		String type=request.getParameter("type");
		String num=request.getParameter("num");
		String newVersion=request.getParameter("newVersion");
		Pager page=new Pager();
		// 从页面获取页码
		String currentpageStr = request.getParameter("pageNum");
		// 将从页面获取的分页数据转化成int型
		int currentPage = 1;
		if (!StringUtil.isEmpty(currentpageStr)) {
			currentPage = Integer.parseInt(currentpageStr);
		}
		// 设置要查询的页码
		page.setCurrentPage(currentPage);
		page.setRowsPerPage(10);
		List<Version> versionList = versionService.getVersionList(type, num, newVersion, page);
		request.setAttribute("versionList", versionList);
		request.setAttribute("pager", page);
		request.setAttribute("type", type);
		request.setAttribute("newVersion", newVersion);
		request.setAttribute("num", num);
		return "/system/wlb/wlbVersionList";
	}
	/**
	 * 跳转到三分地后台管理系统物流宝新增版本
	 * @return
	 */
	@RequestMapping(value="/system/wlbversion/add")
	public String wlbVersionAdd(HttpServletRequest request){
		return "/system/wlb/wlbVersionAdd";
	}
	/**
	 * 三分地后台管理系统物流宝新增版本
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value="/system/wlbversion/save")
	public String wlbVersionSave(HttpServletRequest request,Version version){
		try {
			versionService.saveVersion(version);
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";
	}
	/**
	 * 三分地后台管理系统物流宝删除版本信息  可批量删除
	 * @param request
	 * @param version
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/system/wlbversion/delete")
	public boolean wlbVersionDelete(HttpServletRequest request,Version version){
		try {
			String[] versionIDs = request.getParameter("ids").split(",");
			versionService.deleteVersion(versionIDs);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 三分地后台管理系统物流宝进入版本详情页面
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/system/wlbversion/edit/{id}")
	public String wlbVersionEdit(HttpServletRequest request,@PathVariable String id){
		try {
			Version version = versionService.getVersion(id);
			request.setAttribute("version", version);
			return "/system/wlb/wlbVersionEdit";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/system/wlb/wlbVersionEdit";
	}
	
}
