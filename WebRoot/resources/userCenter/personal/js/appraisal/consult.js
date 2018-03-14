//提交搜索表单
function submitForm(){
	document.getElementById("searchConsult").submit();
}

//清空搜索表单
function resetForm(){
	document.getElementsByName("replyStatus")[0].checked=true;
	$("#storeName").val('');
	$("#beginDate").val('');
	$("#endDate").val('');
	$("#endDate").val('');
}