$(document).ready(function(){
  $(".ht_btn").next().addClass("afterHt");
})
// 重置
function reset(){
	document.getElementById("modifyDetailForm").reset();
}

//验证模板名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >20){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("模板名为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("模板名为2-20位字符组成");
		return true;
	}
}

//验证模板名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >20){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("模板名为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}

// 只能输入数字
function IsNum(e) {
    var k = window.event ? e.keyCode : e.which;
    if (((k >= 48) && (k <= 57))) {//小数点k==46
    } else {
        if (window.event) {
            window.event.returnValue = false;
        }
        else {
            e.preventDefault(); //for firefox 
        }
    }
} 
// 验证是否输入排序位置
function checkIndexBlur(){
	var indexStr = document.getElementsByName('index')[0].value;
	if(indexStr==""||indexStr==null){
		document.getElementById("indexDd").className="text_c_ts";
		$("#indexSpan").html("请输入排序位置");
		return false;
	}else{
		document.getElementById("indexDd").className="";
		return true;
	}
}

//验证是否输入排序位置
function checkIndex(){
	var indexStr = document.getElementsByName('index')[0].value;
	if(indexStr==""||indexStr==null){
		document.getElementById("indexDd").className="text_c_ts";
		$("#indexSpan").html("请输入排序位置");
		return false;
	}else{
		document.getElementById("indexDd").className="text_d_ts";
		return true;
	}
}
// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/message/detailList";
	}
	window.location.href=contextPath + "/system/message/detailList";
}
//提交
function formSubmit() {
	if(checkNameBlur()&&checkIndexBlur()){
		document.getElementById("modifyDetailForm").submit();
	}else{
		
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