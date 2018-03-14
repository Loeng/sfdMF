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

// 重置
function reset(){
	document.getElementById("modifyRoleForm").reset();
}

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

// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/store/role/list";
	}
	window.location.href=contextPath + "/system/store/role/list";
}
//提交
function formSubmit() {
	if(checkNameBlur()){
		document.getElementById("modifyRoleForm").submit();	
	}else{
		checkNameBlur();
	}
}

$(function(){
	if($("#modifyOk").val()=="true"){
		econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
	}else if(!$("#modifyOk").val()=="false"){
		ealert("修改失败！");
	}
	$("input.i_bg").focus(function (){ 
			$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 
});	

$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});