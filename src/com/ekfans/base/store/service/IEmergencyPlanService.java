package com.ekfans.base.store.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ekfans.base.store.model.EmergencyPlan;
import com.ekfans.pub.util.Pager;

public interface IEmergencyPlanService {
	
	public boolean save(EmergencyPlan qua, HttpServletRequest request,
			HttpServletResponse response);
	
	public List<EmergencyPlan> getQuaByStoreId(Pager page,String startTime,String endTime, HttpServletRequest request,
			HttpServletResponse response);
	
	public boolean delete(String[] quaIds);
	
	public boolean deleteById(String quaId);
	
	public EmergencyPlan getQuaById(String quaId);
	
	public boolean updateBean(EmergencyPlan qua);
	
	public boolean startUse(String id,HttpServletRequest request, HttpServletResponse response);
	
	public List<EmergencyPlan> getQuaByStoreId(String storeId);
}
