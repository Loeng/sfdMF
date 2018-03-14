package com.ekfans.controllers.user.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.IntegralLog;
import com.ekfans.base.user.model.User;
import com.ekfans.base.user.service.IUserService;

@Controller
public class checkMyScoresController {
    @Autowired
    private IUserService userService;
    
    /**
     * 
    * @Title: checkScores
    * @Description: TODO(获取当前用户的积分信息)
    * 详细业务流程:
    * (详细描述此方法相关的业务处理流程)
    * @param @param session
    * @param @param request
    * @param @return    设定文件
    * @return String    返回类型
    * @throws
     */
    @RequestMapping(value="/user/services/scores/getMyScores")
    public String checkScores(HttpSession session,HttpServletRequest request){
	//  获取完整的登录的User对象
	User user =(User)session.getAttribute(SystemConst.USER);
	if(user == null){
	    return "/user/login/";
	}
	//  获取用户的id
	String userId = user.getId();
	//根据userId查询出用户的积分信息
	List<IntegralLog> integralLogs = userService.getIntegralByUserId(userId);
	if(integralLogs.size() > 0){
		//取出总积分
		int myscore= 0;
		for (IntegralLog integralLog : integralLogs) {
			if("1".equals(integralLog.getType())){
				myscore += integralLog.getIntegral();
			}
			if(integralLog.getNote().length()>20){
				integralLog.setNote(integralLog.getNote().substring(0, 17)+"...");
			}
		}	
		//取出积分有效时间
//		String scoreTime = integralLogs.get(integralLogs.size()-1).getInvalidTime();
//		request.setAttribute("scoreTime",scoreTime);
		request.setAttribute("purviewId", "scores");
		request.setAttribute("myscore",myscore);
		request.setAttribute("integralLogs",integralLogs);
		request.setAttribute("cur","service");
		request.setAttribute("integration", userService.getUser(user.getId()).getIntegration());
		return "/userCenter/customer/service/scores/myScores";
	}
	return "/userCenter/customer/service/scores/myScores";
    }
}
