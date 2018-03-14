package com.ekfans.plugin.wuliubao.yunshu.base.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ekfans.plugin.wuliubao.yunshu.base.model.Aptitude;
import com.ekfans.plugin.wuliubao.yunshu.controller.model.AptitudeForm;
import com.ekfans.pub.util.Pager;

/**
 * 资质认证相关业务处理
 * @author pp
 * @Date 2017年7月13日15:26:45
 */
public interface IWlbAppAptitudeServicel {
   /**
    * 保存资质认证
    * @param apt 资质认证信息
    * @return
    * @throws Exception 
    */
	String saveAptitude(AptitudeForm apt,HttpServletRequest request) throws Exception;
	
	/**
	 * 获取未认证的用户
	 * @return
	 * @throws Exception
	 */
	List<Aptitude> getCheckUser(Pager pager,String nickname,String username) throws Exception;

    /**
     * 物流宝资质认证人工审核
     * @param id 
     * @param status(1:通过,2:失败)
     * @param remarks 审核备注
     * @return
     * @throws Exception
     */
    String check(String id,String status,String remarks,HttpServletRequest request) throws Exception;
    
    /**
     * 得到资质信息
     * @param authenticatorId
     * @return
     * @throws Exception
     */
    public Aptitude getAptitude(String authenticatorId) throws Exception;
}
