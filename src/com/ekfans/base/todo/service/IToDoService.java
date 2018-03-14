package com.ekfans.base.todo.service;

import com.ekfans.base.todo.model.ToDoModel;

/**
 * 待办事项:获取三分地后台所需的待办事项接口
 * @author pp
 * @date 2017年9月8日10:48:37
 *
 */

public interface IToDoService {
     /**
      * 获取车源审核的待办事项
      * @return
      */
	 ToDoModel getCheCheck();
	 
	 /**
	  * 获取货源审核的待办事项
	  * @return
	  */
	 ToDoModel getHuoCheck();
	 
	 /**
	  * 获取物流宝资质认证的待办事项
	  * @return
	  * @throws Exception 
	  */
	 ToDoModel getWLBZZRZCheck() throws Exception;
	 /**
	  * 获取成品审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getCPCheck()throws Exception;
	 /**
	  * 获取成品管理-求购审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getCPQGCheck()throws Exception;
	 /**
	  * 获取供求管理-求购审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getGQQGCheck()throws Exception;
	 /**
	  * 获取供应审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getGQCheck()throws Exception;
	 /**
	  * 获取供需审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getGXCheck()throws Exception;
	 /**
	  * 获取商品审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getSPCheck()throws Exception;
	 /**
	  * 获取危废报废审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getWFBFCheck()throws Exception;
	 /**
	  * 获取基础信息认证的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getJCCheck()throws Exception;
	 /**
	  * 获取银行认证的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getYHCheck()throws Exception;
	 /**
	  * 获取处置资质认证的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getCZCheck()throws Exception;
	 /**
	  * 获取产生资质认证的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getCSCheck()throws Exception;
	 /**
	  * 获取运输资质认证的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getYSCheck()throws Exception;
	 /**
	  * 获取运输车辆审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getYSCarCheck()throws Exception;
	 /**
	  * 获取驾驶员审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getJSYCheck()throws Exception;
	 /**
	  * 获取押运员审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getYYYCheck()throws Exception;
	 /**
	  * 获取账户绑定审核的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getAccountCheck()throws Exception;
	 /**
	  * 获取物流宝留言反馈的待办事项
	  * @return
	  * @throws Exception
	  */
	 ToDoModel getWLBlyfk()throws Exception;
	 
}
