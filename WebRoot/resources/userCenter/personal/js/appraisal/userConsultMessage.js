
//提交搜索表单
function submitForm(){
	document.getElementById("consultMessageForm").submit();
}

//清空搜索表单
function resetForm(){
	document.getElementsByName("replyStatus")[0].checked=true;
	$("#productName").val('');
	$("#userName").val('');
	$("#beginDate").val('');
	$("#endDate").val('');
	$("#endDate").val('');
}



//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("consultMessageForm").submit();
}