package com.ekfans.controllers.system.todo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ekfans.base.todo.model.ToDoModel;
import com.ekfans.base.todo.service.IToDoService;
import com.ekfans.pub.util.Pager;
import com.ekfans.pub.util.StringUtil;

/**
 * 三分地后台管理待办事项控制器
 * @author pp
 * @date 2017年9月8日10:28:11
 *
 */
@Controller
public class ToDoController {
	@Autowired
	private IToDoService todoservice;
	/**
	 * 三分地后台管理获取待办事项详情
	 * @return
	 */
	@RequestMapping(value = "/system/todo/tododetails")
	public String getToDo(HttpServletRequest request){
		List<ToDoModel> todolist = new ArrayList<ToDoModel>();
		try {
//			String currentpageStr = request.getParameter("pageNum"); // 页码
//			// 定义分页
//			Pager pager = new Pager();
//			int currentPage = 1;
//			if (!StringUtil.isEmpty(currentpageStr)) {
//				try {
//					currentPage = Integer.parseInt(currentpageStr);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			pager.setCurrentPage(currentPage);
			//获取IToDoService的类类型
			Class<?> todoServiceClass = todoservice.getClass();
			//获取该类的方法数组
			Method[] methods = todoServiceClass.getDeclaredMethods();
			//遍历数组 循环执行该类的方法
			for(Method method : methods){
				//获取该方法的返回参数类名
				String typeName=method.getReturnType().getName();
				//获取该方法的参数数量
				int parameterCount=method.getParameterTypes().length;
				//如果返回参数为 ToDoModel 并且  方法的参数数量为 0 
				if(typeName.equals("com.ekfans.base.todo.model.ToDoModel")&&parameterCount==0){
					//获取该方法的待处理事项
					ToDoModel doModel =(ToDoModel) method.invoke(todoservice);
					//存入集合
					todolist.add(doModel);
				}
			}
			
			request.setAttribute("todolist", todolist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/system/todo/todo_details2";
	}
}
