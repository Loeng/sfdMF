// 重置
function reset(){
	document.getElementById("modifyUserLevelForm").reset();
}

//验证会员等级名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 3 || nameStr.length >20){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("会员等级名为3-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("会员等级名为3-20位字符组成");
		return true;
	}
}

//验证会员等级名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	//document.getElementsByName('name')[0].value
	if(nameStr.length < 3 || nameStr.length >20){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("会员等级名为3-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}

// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/user/level/list";
	}
	window.location.href=contextPath + "/system/user/level/list";
}
//提交
function formSubmit() {
	if(checkName()){
		document.getElementById("modifyUserLevelForm").submit();
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

//只能输入数字
function IsNum(e) {
    var k = window.event ? e.keyCode : e.which;
    if (((k >= 48) && (k <= 57)||(k == 46))) {//小数点k==46
    } else {
        if (window.event) {
            window.event.returnValue = false;
        }
        else {
            e.preventDefault(); //for firefox 
        }
    }
} 
$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});