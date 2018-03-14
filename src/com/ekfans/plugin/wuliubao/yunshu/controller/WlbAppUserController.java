package com.ekfans.plugin.wuliubao.yunshu.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ekfans.base.order.dao.IInquiryDao;
import com.ekfans.base.store.model.Wftransport;
import com.ekfans.base.store.service.IWftransportService;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;
import com.ekfans.controllers.ccwccApp.Response;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.message.util.MessageUtil;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppUserDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Version;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IVersionService;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IWlbAppAptitudeServicel;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IWlbAppBargainService;
import com.ekfans.plugin.wuliubao.yunshu.base.service.IWlbAppUserService;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddBargainRemarksForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AptitudeForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.BargainingForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.CarSource;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.EditUserForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.FileUploadForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.GetBargainForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.HuoResponse;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.LogModifyPWForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.LoginForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.ModifyPWForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.RegisterForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.UserBargain;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.WlbUser;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.BaiduMapUtil;
import com.ekfans.pub.tools.security.SecurityCode;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 *  物流宝APP用户信息相关控制接口
 * @author pp
 * @Date 2017年6月30日11:04:27
 */
@Controller
public class WlbAppUserController {
    @Autowired
    private IWlbAppUserService userservice;
    @Autowired
    private IWlbAppAptitudeServicel aptitudservice;
    @Autowired
    private IUserService uservice;
    @Autowired
    private IWlbAppBargainService bargainService;
    @Autowired
	private IInquiryDao inquiryDao;
    @Autowired
	private IWlbAppUserDao userdao;
    @Autowired
	private IWftransportService wftransprotService;
    @Autowired
	private IVersionService versionService;
    // 定义错误日志
 	public static Logger log = LoggerFactory.getLogger(WlbAppUserController.class);
	/**
	 * 用户注册
	 * @return
	 */
	@RequestMapping(value = "/wlbApp/user/register", produces = "application/json;charset=UTF-8",method=RequestMethod.POST)
	public @ResponseBody Response register(@RequestBody byte[] te){
	    String statusCode=null;
	    try {
	    RegisterForm reg = (RegisterForm) new RegisterForm().getObject(te);
	    statusCode = userservice.register(reg.password,reg.SecurityCode,reg.mobile,reg.type);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
		if(statusCode.equals("0")){
			return new Response().failure();
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("statusCode", statusCode);
		return new Response().success(map);
	}
	/**
	 * 通过短信方式获取验证码
	 * @param mobile 手机号
	 * @param type  获取类型(1:其他,2:用户注册,3:找回密码)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/getCode/{mobile}/{type}",method = RequestMethod.GET)
	public Response getCode(@PathVariable String mobile,@PathVariable String type){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
		 //用户注册
		 if(type.equals("2")){
			//获取用户
	        User user = userdao.getUser(mobile);
			if(user!=null){
				//手机号已注册
				map.put("statusCode", "2");
				map.put("Code", "");
				return  new Response().success(map);
			}
		
		 }
		 //找回密码
		 if(type.equals("3")){
			//获取用户
	        User user = userdao.getUser(mobile);
			if(user==null){
				//手机号未注册
				map.put("statusCode", "3");
				map.put("Code", "");
				return  new Response().success(map);
			}
		 }
		} catch (Exception e) {}
		String code = SecurityCode.getSecurityCode();
		boolean bo = MessageUtil.sendPhoneMessage(mobile, code+"，来自三分地环保 物流宝的验证码。【三分地环保提醒：注意账号安全，请确认此操作系本人】");
		if(bo){
			Cache.engine.remove(mobile);
			Cache.engine.add(mobile, code);
			map.put("statusCode", "1");
			map.put("Code", code);
			return  new Response().success(map);
		}
		map.put("statusCode", "0");
		map.put("Code", "");
		return  new Response().success(map);
	}
	/**
	 * 用户登录
	 * @param type  登录类型(0:账号密码登录,1:短信验证登录)
	 * @param password   
	 * @param SecurityCode
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/login",method = RequestMethod.POST)
	public Response login(@RequestBody byte[] te){
		System.out.println("-------login-----");
		String statusCode=null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
		LoginForm login = (LoginForm) new LoginForm().getObject(te);;
		statusCode = userservice.login(login.type, login.password, login.SecurityCode, login.mobile,login.registrationID,login.userType);
		if(statusCode.length()>12){
			WlbUser wlbUser = (WlbUser) Cache.engine.get(statusCode);
			map.put("statusCode", "1");
			map.put("user", wlbUser);
			return new Response().success(map);
		}
		map.put("statusCode", statusCode);
		return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	/**
	 * 用户修改密码
	 * @param newpassword
	 * @param SecurityCode
	 * @param mobile
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/modifyPW",method = RequestMethod.POST)
	public Response modifyPW(@RequestBody byte[] te){
		String statusCode=null;
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			ModifyPWForm mo = (ModifyPWForm) new ModifyPWForm().getObject(te);
			statusCode = userservice.modifyPW(mo.newpassword,mo.SecurityCode,mo.mobile);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
		map.put("statusCode", statusCode);
		return new Response().success(map);
	}
	/**
	 * 用户退出登录
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/logOut",method = RequestMethod.GET)
	public Response logOut(HttpServletRequest request){
		//获取token
		String token = request.getHeader("WLB-Token");
		//删除缓存中用户信息
		Cache.engine.remove(token);
		//删除数据库中用户token信息
		try {
			User user = (User) userdao.get(token.split("_")[0]);
			if(user!=null){
				user.setWlbToken("");
				userdao.updateBean(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
		return new Response().success();
	} 
	/**
	 * 登录中修改密码
	 * @param newpassword
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/logModifyPW",method = RequestMethod.POST)
	public Response logModifyPW(@RequestBody byte[] te,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			LogModifyPWForm lo = (LogModifyPWForm) new LogModifyPWForm().getObject(te);
			String statusCode = userservice.logModifyPW(lo.newpassword,lo.password, request);
			map.put("statusCode", statusCode);
			return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	/**
	 * 用户资质认证
	 * @param te
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/aptitude",method = RequestMethod.POST)
	public Response userAptitude(@RequestBody byte[] te,HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			AptitudeForm ap = (AptitudeForm) new AptitudeForm().getObject(te);
			String statusCode = aptitudservice.saveAptitude(ap, request);
			map.put("statusCode", statusCode);
			map.put("user", Cache.engine.get(request.getHeader("WLB-Token")));
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
		return new Response().success(map);
	}
	

	/**
	 * 编辑用户资料(名称和联系电话)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/editUser",method = RequestMethod.POST)
	public Response editUser(HttpServletRequest request,@RequestBody byte[] te){
		try {
			EditUserForm ef=(EditUserForm) new EditUserForm().getObject(te);
			Map<String,Object> map=userservice.editUser(request, ef.safeMobile, ef.nickName);
			return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 上传图片
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/fileUpload",method = RequestMethod.POST)
	public Response FileUpload(HttpServletRequest request,@RequestBody byte[] te){
		try {
			FileUploadForm ff=(FileUploadForm) new FileUploadForm().getObject(te);
		    Map<String,Object> map=userservice.FileUpload(ff.stream, request);
			return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	/**
	 * 发布用户议价
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/bargaining",method = RequestMethod.POST)
	public Response bargaining(HttpServletRequest request,@RequestBody byte[] te){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			BargainingForm b = (BargainingForm) new BargainingForm().getObject(te);
			String statusCode=bargainService.sendBargain(b);
			map.put("statusCode", statusCode);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[price]必须为数字!");
		}catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
		return new Response().success(map);
	}
	/**
	 * 删除用户议价
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/deleteBargain/{bargainID}",method = RequestMethod.GET)
	public Response deletebargain(@PathVariable String bargainID){
		try {
			bargainService.deleteBargain(bargainID);
			inquiryDao.deleteById(bargainID);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
		return new Response().success();
	}

	/**
	 * 获取用户议价信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/getBargain",method = RequestMethod.POST)
	public Response getUserbargain(HttpServletRequest request,@RequestBody byte[] te){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			GetBargainForm form = (GetBargainForm) new GetBargainForm().getObject(te);
			Pager page = new Pager();
			int current = 1;
			if (!StringUtil.isEmpty(form.currentPage)) {
				current = Integer.parseInt(form.currentPage);
			}
			page.setCurrentPage(current);
			List<UserBargain> userBargainList = bargainService.getUser(request, form.userType, form.bargainType,form.sellerContactState,page,form.latitude,form.longitude);
			if(userBargainList==null){
				map.put("statusCode","0");
				map.put("Pager", page);
				map.put("userBargainList", new ArrayList<>());
				return new Response().success(userBargainList);
			}
			map.put("statusCode","1");
			map.put("Pager", page);
			map.put("userBargainList", userBargainList);
			return new Response().success(map);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[currentPage]必须为数字!");
		}catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 获取用户商品议价信息
	 * @param request
	 * @param wftransportIds 商品id
	 * @param bargainType  议价类型 (1:我的议价,2:对我议价)
	 * @param sellerContactState 卖家是否已联系 (0:未联系,1:已联系)
	 * @param currentPage  分页
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/getUserBargain",method = RequestMethod.POST)
	public Response getBargain(HttpServletRequest request,@RequestBody byte[] te){
		try {
			GetBargainForm form = (GetBargainForm) new GetBargainForm().getObject(te);
			Pager page = new Pager();
			int current = 1;
			if (!StringUtil.isEmpty(form.currentPage)) {
				current = Integer.parseInt(form.currentPage);
			}
			page.setCurrentPage(current);
			Map<String,Object> map = bargainService.getBargain(request, form.wftransportIds, form.bargainType,form.sellerContactState,page,form.latitude,form.longitude);
			return new Response().success(map);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("字段[currentPage]必须为数字!");
		}catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 添加更新用户备注
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/addBargainRemarks",method = RequestMethod.POST)
	public Response updatebargain(HttpServletRequest request,@RequestBody byte[] te){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取用户token
			String token = request.getHeader("WLB-Token");
			//通过token获取用户id
			String userid = token.split("_")[0];
			AddBargainRemarksForm remarks = (AddBargainRemarksForm) new AddBargainRemarksForm().getObject(te);
			String statusCode = bargainService.addUserBargainRemarks(remarks,userid);
			map.put("statusCode",statusCode);
			return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 添加卖家用户是否已联系
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/sellerContactState/{bargainId}",method = RequestMethod.GET)
	public Response addSellerContactState(HttpServletRequest request,@PathVariable String bargainId){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取用户token
			String token = request.getHeader("WLB-Token");
			//通过token获取用户id
			String userid = token.split("_")[0];
		    String statusCode = bargainService.addSellerContactState(bargainId, userid);
			map.put("statusCode",statusCode);
			return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure();
		}
	}
	
	/**
	 * 用户更新:获取最新的用户信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/user/refreshUser")
	public Response refreshUser(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		String token = request.getHeader("WLB-Token");
		//通过token获取用户id
		String[] userid = token.split("_");
	    User user = uservice.getUser(userid[0]);
	    //根据id获取资质信息
	    try {
			Aptitude aptitude = aptitudservice.getAptitude(userid[0]);
			WlbUser wu=new WlbUser(user, aptitude, token);
			map.put("WlbUser",wu);
			return new Response().success(map);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response().failure("未获取到资质信息");
		}
	}
	
	/**
	 * 用户分享商品信息(车源or货源)
	 * @param request
	 * @param wftransportIds 
	 * @return
	 */
	@RequestMapping(value="/wlbApp/user/share/{wftransportIds}")
	public String share(HttpServletRequest request,@PathVariable String wftransportIds){
	    try {
	    	//获取要分享的车货源
	    	Wftransport w = (Wftransport) wftransprotService.getWftransportById(wftransportIds);
	    	User user = (User) userdao.get(w.getStoreId());
	    	request.setAttribute("user", user);
	    	//设置截止时间
	    	if(!StringUtil.isEmpty(w.getEndTime())){
	    		request.setAttribute("endTime", w.getEndTime().substring(0,10).replace("-","/"));
	    	}else{
	    		request.setAttribute("endTime", "");
	    	}
	    	//设置物流宝最新的下载链接
	    	
	    	if(w.getType()==0){
	    		CarSource car = new CarSource(w);
	    		request.setAttribute("car", car);
	    		return "/wlb/share_ys_q";
		    }
	    	if(w.getType()==1){
	    		//设置有效时间
	    		if(!StringUtil.isEmpty(w.getValidTime())){
		    		request.setAttribute("validTime", w.getValidTime().substring(0,w.getValidTime().indexOf(" ")).replace("-","/"));
		    	}else{
		    		request.setAttribute("validTime", "");
		    	}
	    		//获取商品距离
		    	Double distance = BaiduMapUtil.getCommodityDistance(w.getStartFullPath(), w.getEndFullPath());
		    	DecimalFormat df = new DecimalFormat("#.##");
		    	String a=df.format(distance);
		    	request.setAttribute("distance", a);
	    		HuoResponse huo = new HuoResponse(w);
	    		request.setAttribute("huo", huo);
	    		return "/wlb/share_cs_q";
		    }
	    } catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 跳转到物流宝下载页面
	 * @param request
	 * @param type (1:车主端Android,3:货主端Android)
	 * @return
	 */
	@RequestMapping(value="/wlbApp/download/{type}")
	public String download(HttpServletRequest request,@PathVariable String type){
	    try {
	    	Version version = versionService.getnewVersion(type);
	    	request.setAttribute("type", type);
	    	request.setAttribute("version", version);
	    } catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
		}
		
	    return "/wlb/download";
	}
	
	/**
	 * 物流宝被议价的车源货源是否接受询价
	 * @param type 商品类型(0:车源,1:货源)
	 * @param bargainId 询价ID
	 * @param states  是否接受(0:不接受,1:接受)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/wlbApp/bespeak/{type}/{bargainId}/{states}")
	public Response bespeak(@PathVariable String bargainId,@PathVariable String type,@PathVariable String states,HttpServletRequest request) {
		String statusCode;
		Map<String,String> map = new HashMap<String,String>();
		try {
			statusCode = bargainService.ifAccept(states, bargainId, type, request);
			map.put("statusCode",statusCode);
		} catch (Exception e) {
			e.printStackTrace();
			// 记日志
			log.error(e.getMessage());
			return new Response().failure("系统异常");
		}
		return new Response().success(map);
	}
}















