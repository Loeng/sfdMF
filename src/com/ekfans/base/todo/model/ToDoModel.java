package com.ekfans.base.todo.model;


import com.ekfans.pub.util.transaction.Purview;

/**
 * 待办事项信息模型
 * @author pp
 * @date 2017年9月8日10:34:33
 */
public class ToDoModel {
	//待办事项的数量
    private int num;
    //待办事项的详细信息(待办事项的访问地址,待办事项的权限ID,待办事项的名称)
	private Purview purview;
	//待办事项的文字提示说明
	private String explain;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Purview getPurview() {
		return purview;
	}
	public void setPurview(Purview purview) {
		this.purview = purview;
	}
	public String getExplain() {
		return explain;
	}
	public void setExplain(String explain) {
		this.explain = explain;
	}
	
}
