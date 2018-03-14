// 搜索表单提交
function formSubmit(){
	$("#searchSupplyBuy").submit();
}

// 搜索表单重置
function formReset(){
	$("#title").val("");
	$("#storeName").val("");
	$("#beginDate").val("");
	$("#endDate").val("");
}


//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchSupplyBuy").submit();
}

