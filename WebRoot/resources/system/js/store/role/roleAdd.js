$(document).ready(function(){
		$(".limitItem").click(function(){
			$main = $(this);
			if($main.find("input").is(":checked")){
				//下级都被选中
				$main.find(".check").addClass("checked");
				$main.find("input").attr("checked",true);
				$main.next(".limitList").find("input").attr("checked",true);
				$main.next(".limitList").find(".check").addClass("checked");
				$main.parents().prev(".limitItem").find("input").attr("checked",true);
				$main.parents().prev(".limitItem").find(".check").addClass("checked");
			}else{
				//下级都取消选中
				$main.find(".check").removeClass("checked");
				$main.find("input").attr("checked",false);
				$main.next(".limitList").find(":checkbox").attr("checked",false);
				$main.next(".limitList").find(".check").removeClass("checked");
			}
		});
		$(".limitAll").click(function(){
			$main = $(this);
			if($main.find("input").is(":checked")){
				//下级都被选中
				$main.find(".check").addClass("checked");
				$main.nextAll(".limitList").find("input").attr("checked",true);
				$main.nextAll(".limitList").find(".check").addClass("checked");
			}else{
				//下级都取消选中
				$main.find(".check").removeClass("checked");
				$main.nextAll(".limitList").find(":checkbox").attr("checked",false);
				$main.nextAll(".limitList").find(".check").removeClass("checked")
			}
		});
});	

$(function(){
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
	
	$("input.i_bg").focus(function ()//得到焦点时触发的事件
	{ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function () //失去焦点时触发的事件
		{ 
		$(this).parent().removeClass("text_ts");
	}); 
});	

//验证角色名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 3 || nameStr.length >20){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("角色名为3-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("角色名为3-20位字符组成");
		return true;
	}
}

//验证角色名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 3 || nameStr.length >20){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("角色名为3-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}

// 提交
function formSubmit(){
	if(checkName()){
	    document.getElementById("addRoleForm").submit();
	}else{
		checkNameBlur(); //调用鼠标移除的js显示出警告信息
	}
}
// 重置
function reset(){
	document.getElementById("addRoleForm").reset();
}

// 返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/store/role/list";
	}
	window.location.href=contextPath + "/system/store/role/list";
}	
$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});