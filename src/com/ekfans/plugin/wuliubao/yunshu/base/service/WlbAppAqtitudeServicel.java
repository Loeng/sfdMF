package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ekfans.base.system.model.Manager;
import com.ekfans.base.system.util.SystemConst;
import com.ekfans.base.user.model.User;
import com.ekfans.plugin.cache.base.Cache;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppAptitude;
import com.ekfans.plugin.wuliubao.yunshu.base.dao.IWlbAppUserDao;
import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AptitudeForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.WlbUser;
import com.ekfans.plugin.wuliubao.yunshu.controller.util.JGUtil;
import com.ekfans.pub.util.DateUtil;
import com.ekfans.pub.util.FileUtil;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;
import com.ekfans.pub.util.transaction.Purview;
import com.ekfans.pub.util.transaction.WebSocketUtil;

@Service
public class WlbAppAqtitudeServicel implements IWlbAppAptitudeServicel{
  @Autowired
  private IWlbAppAptitude apti;
  @Autowired
  private IWlbAppUserDao userdao;
@Override
public String saveAptitude(AptitudeForm apt,HttpServletRequest request) throws Exception {
	if(StringUtil.isEmpty(apt.type)||!apt.type.equals("1")&&!apt.type.equals("2")&&!apt.type.equals("3")){
		return "0";
	}
	//获取用户token
	String token = request.getHeader("WLB-Token");
	//通过token获取用户id
	String userid = token.split("_")[0];
	//通过token获取用户信息
	WlbUser u = (WlbUser) Cache.engine.get(token);
	Aptitude ap = apti.getAptitude(userid);
	ap.setName(u.getName());
	ap.setNickName(u.getNickName());
	ap.setType(apt.type);
	ap.setAuthenticatorId(userid);
	ap.setState("1");
	if(apt.scope!=null){
		ap.setManagement(StringUtil.arrayToString(apt.scope,"|"));
	}
	ap.setUpdateTime(DateUtil.getSysDateTimeString());
	//如果用户认证是司机
	if(apt.type.equals("1")){
		ap.setManagementNumber(apt.dINumber);
		if(!StringUtil.isEmpty(apt.dIBD)){
			ap.setManagementPath(FileUtil.baseStringToImage(request,apt.dIBD, "/customerfiles/avatar/"));
		}
		ap.setDangerTransportNumber(apt.dTQNumber);
		if(!StringUtil.isEmpty(apt.dTQBD)){
			ap.setDangerTransportPath(FileUtil.baseStringToImage(request,apt.dTQBD, "/customerfiles/avatar/"));
		}
		ap.setTransportNumber(apt.dsINumber);
		if(!StringUtil.isEmpty(apt.dsIBD)){
			ap.setTransportPath(FileUtil.baseStringToImage(request,apt.dsIBD, "/customerfiles/avatar/"));
		}
	}
	//如果用户认证是运输企业
	if(apt.type.equals("2")){
		ap.setManagementNumber(apt.bINumber);
		if(!StringUtil.isEmpty(apt.bIBD)){
			ap.setManagementPath(FileUtil.baseStringToImage(request,apt.bIBD, "/customerfiles/avatar/"));
		}
		ap.setDangerTransportNumber(apt.dTMNumber);
		if(!StringUtil.isEmpty(apt.dTMBD)){
			ap.setDangerTransportPath(FileUtil.baseStringToImage(request,apt.dTMBD, "/customerfiles/avatar/"));
		}
		ap.setTransportNumber(apt.rTQNumber);
		if(!StringUtil.isEmpty(apt.rTQBD)){
			ap.setTransportPath(FileUtil.baseStringToImage(request,apt.rTQBD, "/customerfiles/avatar/"));
		}
	}
	//如果用户认证是产生企业
	if(apt.type.equals("3")){
		ap.setManagementNumber(apt.bINumber);
		if(!StringUtil.isEmpty(apt.bIBD)){
			ap.setManagementPath(FileUtil.baseStringToImage(request,apt.bIBD, "/customerfiles/avatar/"));
		}
		ap.setSewagePermitNumber(apt.sPNumber);
		if(!StringUtil.isEmpty(apt.sPBD)){
			ap.setSewagePermitPath(FileUtil.baseStringToImage(request,apt.sPBD, "/customerfiles/avatar/"));
		}
		if(!StringUtil.isEmpty(apt.sPEBD)){
			ap.setSewagePermitEnclosurePath(FileUtil.baseStringToImage(request,apt.sPEBD, "/customerfiles/avatar/"));
		}
	}
	//更新资质认证
	apti.updateBean(ap);
	//更新用户信息
	WlbUser user = (WlbUser) Cache.engine.get(token);
	user.PackingWlbUser(ap);
	//更新缓存
	Cache.engine.remove(token);
	Cache.engine.add(token, user);
	//发送三分地后台管理提示通知
	WebSocketUtil.sendSystemMessage(Purview.WLB_ZZRZ, "有一条物流宝资质认证信息需要审核!");
	return "1";
}

@Override
public List<Aptitude> getCheckUser(Pager pager,String nickName,String name) throws Exception {
	return apti.getCheckAptitude(pager,nickName,name);
}

@Override
public String check(String id, String status, String remarks,HttpServletRequest request) throws Exception {
	Aptitude ap = (Aptitude) apti.get(id);
	//审核成功
	if(status.equals("1")){
		ap.setState("2");
	}
    if(status.equals("2")){
    	ap.setState("3");
	}
    if(!StringUtil.isEmpty(remarks)){
    	ap.setRemarks(remarks);
    }
    Manager manager =(Manager) request.getSession().getAttribute(SystemConst.MANAGER);
    ap.setAuditTime(DateUtil.getSysDateTimeString());
    ap.setAuditorId(manager.getRoleId());
    apti.updateBean(ap);
    //获取用户
    User user = (User) userdao.get(ap.getAuthenticatorId());
    if(user!=null&&!StringUtil.isEmpty(user.getRegistrationID())){
    	//发送极光通通知
        Map<String,String> dataNode = new HashMap<String,String>();
    	//messageType:"2",为资质认证审核信息
    	dataNode.put("messageType", "2");
    	//审核状态 1:通过 2:未通过
    	dataNode.put("checkType", status);
    	//用户ID
    	dataNode.put("userID", user.getId());
    	//审核备注
    	if(!StringUtil.isEmpty(remarks)){
    	dataNode.put("remarks", remarks);
    	}else if(status.equals("2")&&StringUtil.isEmpty(remarks)){
    		dataNode.put("remarks", "您的资质认证审核未通过!");
    	}else if(status.equals("1")){
    		dataNode.put("remarks", "您的资质认证审核已通过!");
    	}
    	JGUtil.wlbAPPsendMessages(user.getRegistrationID(),dataNode,"您有一条资质认证审核信息!");
    }
    return "1";
}

@Override
public Aptitude getAptitude(String authenticatorId) throws Exception {
	if(!StringUtil.isEmpty(authenticatorId)){
	   Aptitude aptitude = apti.getAptitude(authenticatorId);
	   return aptitude;
	}
	   return null;
}


}
