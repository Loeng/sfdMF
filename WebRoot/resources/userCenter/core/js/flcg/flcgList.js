// 搜索表单提交
function formSubmit(){
	$("#searchPurchase").submit();
}

// 搜索表单重置
function formReset(){
	$("#productNo").val("");
	$("#companyName").val("");
	$("#beginDate").val("");
	$("#endDate").val("");
}

// 订单列表的JS操作
function query(ctxpath,orderId,status,shippingStatus,userApp){
	
}

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchPurchase").submit();
}