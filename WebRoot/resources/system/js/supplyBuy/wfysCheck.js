$(function(){

	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		ealert("审核成功!")
	}else if($("#ok").val()=="false"){
		ealert("审核失败！");
	}

});



// 返回列表
function goBack(contextPath){
	var type = $("#type").val();
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/sysytem/wftransport/getListToSystem/"+type;
	}
	window.location.href=contextPath + "/sysytem/wftransport/getListToSystem/"+type;
}



//提交通过提示
function formSubmit() {
	$("#checkStatus").val("1");
	econfirm("是否确认审核", submit, null, null, null);
}
function submit(){
	$("#checkStatus").val("1");
	document.getElementById("checkForm").submit();
//	var contextPath = $("#ctxpath").val();
//	var type = $("#type").val();
//	// 判定contextPath是否为空
//	if(contextPath==null||contextPath==""){
//		window.location.href="/sysytem/wftransport/getListToSystem/"+type;
//	}
//	window.location.href=contextPath + "/sysytem/wftransport/getListToSystem/"+type;
}

function submitFalse(){
	$("#checkStatus").val("2");
	document.getElementById("checkForm").submit();
}
//提交不通过提示
function formSubmitFalse() {
	econfirm("是否确认拒绝", submitFalse, null, null, null);
}



