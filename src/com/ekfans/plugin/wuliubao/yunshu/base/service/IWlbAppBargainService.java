package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;

import com.ekfans.plugin.wuliubao.yunshu.controller.model.AddBargainRemarksForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.BargainingForm;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.UserBargain;
import com.ekfans.pub.util.Pager;

/**
 * 物流宝用户询价议价业务处理
 * @author pp
 * @Date 2017年7月18日15:29:32
 */
public interface IWlbAppBargainService {
     
	/**
	 * 发送议价信息
	 * @param bar
	 * @return 0:商品ID为空,1:议价成功,2:联系人为空,3:手机号为空,4:环信推送失败,5:商品类型不正确,
	 * 6:货物名为空,7:价格为空,8:被议价人ID为空,9:议价人id为空,10:议价的商品不存在,11:商品已议价,12:不能对自己议价 ,13:被议价人用户不存在
	 * 14.价格单位不能为空
	 * @throws Exception 
	 */
   	 String sendBargain(BargainingForm bar) throws Exception;
   	 
   	 /**
   	  * 删除议价信息
   	  * @param bargainID
   	  * @throws Exception
   	  */
   	 void deleteBargain(String bargainID) throws Exception;
   	 
   	 /**
   	  * 获取用户议价信息
   	  * @param request
   	  * @param userType
   	  * @param bargainType
   	  * @return
   	  * @throws Exception
   	  */
   	 List<UserBargain> getUser(HttpServletRequest request,String userType,String bargainType,String sellerContactState,Pager page,String latitude,String longitude) throws Exception;
   	 
   	/**
   	 * 获取用户商品议价信息
   	 * @param request
   	 * @param wftransportIds 商品id
   	 * @param bargainType 议价类型 (1:我的议价,2:对我议价)
   	 * @param sellerContactState
   	 * @return
   	 * @throws Exception
   	 */
   	 Map<String,Object> getBargain(HttpServletRequest request,String wftransportIds,String bargainType,String sellerContactState,Pager page,String latitude,String longitude) throws Exception;
   	 
   	 /**
   	  * 添加,更新用户备注
   	  * @param remarks
   	  * @return 1:添加成功,2:议价类型不正确,3:议价id错误
   	 * @throws Exception 
   	  */
   	 String addUserBargainRemarks(AddBargainRemarksForm remarks,String userId) throws Exception;
   	 
   	 /**
   	  * 添加卖家用户是否已联系
   	  * @param bargainId
   	  * @param userId
   	  * @return 0:添加失败 1:添加成功,2:用户不是被议价人无法添加,3:议价id错误
   	  * @throws Exception
   	  */
   	 String addSellerContactState(String bargainId,String userId) throws Exception;
   	 
   	/**
   	 * 物流宝用户是否接受询价or议价结果
   	 * @param states 1:接受 2:不接受
   	 * @param bargainId 询价or议价 ID
   	 * @param type 商品类型 0:车源,1:货源
   	 * @param request
   	 * @return 0:states错误;1:成功;2:(type)商品类型错误;3:询价or议价信息不存在;4:该用户不是被议价or询价人;5:用户已接受询价议价
   	 * @throws Exception
   	 */
   	 String ifAccept(String states,String bargainId,String type,HttpServletRequest request) throws Exception;
   	 
}
