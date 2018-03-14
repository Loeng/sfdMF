$(function(){
	// 判定后台是否返回添加成功的信息
	var returnValue=$("#retrunType").val()
	if(returnValue=="success"){
		econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
	}else if(returnValue=="failure"){
		ealert("修改失败！");
	}else if(returnValue=="noFind"){
		ealert("未找到对应的用户名！");
	}
	
	$("input.i_bg").focus(function ()//得到焦点时触发的事件
	{ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function () //失去焦点时触发的事件
		{ 
		$(this).parent().removeClass("text_ts");
	}); 
	
})

function checkUserNameFocus(){
	$("#userIntegralName").parent().removeClass("text_c_ts");
}

function getIntegralByName(){
	var ctx = $("#ctxpath").val();
	var val = $.trim($("#userIntegralName").val());
	
	if(val == ""){
		$("#userIntegralName").parent().addClass("text_c_ts");
		$("#nameSpan").html("请输入用户名");
		return false;
	}
	$.post(ctx + "/system/user/integral/getUserNameIntegral", {name:val}, function(data){
		if(data != "noFind"){
			$("#integration").val(data);
			$("#userIntegralName").parent().removeClass("text_c_ts");
			$("#userIntegralName").next().html();
		}else{
			$("#integration").val(0);
			$("#userIntegralName").parent().addClass("text_c_ts");
			$("#nameSpan").html("请输入正确的用户名");
		}
	});
	return true;
}

function checkIsNumber(){
	var value=$("#integral").val();
	var integration=$("#integration").val();  //目前的积分
	var type=$('input:radio:checked').val();  //1增分    0减分
	
	if(type==0&&(integration-value<0)){
		$("#integral").parent().addClass("text_c_ts");
		$("#integral").val(0);
		return false;
	}
	
	var isNumber=/^\d+$/.test(value);
	$("#integral").parent().removeClass("text_c_ts");
	if(!isNumber){
		$("#integral").parent().addClass("text_c_ts");
		return false;
	}
	return true;
}



function formSubmit(){
	var userName=$("#userIntegralName").val();
	if(!userName){
		$("#userIntegralName").parent().addClass("text_c_ts");
		return false;
	}
	if(checkIsNumber()){
		document.getElementById("addUserLeveIntegralForm").submit();
	}
}

function reset(){
	document.getElementById("addUserLeveIntegralForm").reset();
}

function goBack(){
	var contextPath=$("#ctxpath").val();
	window.location.href=contextPath + "/system/user/integral/integralLogList";
	
}
$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});