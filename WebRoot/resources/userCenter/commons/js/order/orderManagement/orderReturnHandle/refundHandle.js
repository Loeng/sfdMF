// 同意退换货处理模块的操作
function agreeRefundHandle(ctxpath,refundId){
	var i = confirm("确定同意退/换货?");
	if(!i){
		return;
	}
	$.post(
		ctxpath+"/store/order/refundHandle/"+refundId+"/"+"2",
		function(data){
			if(data){
				window.location.href = ctxpath+"/store/order/returnProduct";
			}else{
				alert("操作失败，稍后重试！");
			}
	});
}

// 不同意退换货处理模块的操作
function disagreeRefundHandle(){
	// 显示出提示框，要求填入理由
	$(".decline").show();
}

// 理由和不同意想法的提交
function submit(ctxpath,refundId){
	
	var $reason = $.trim($("#refundReason").val());
	if($reason==null || $reason==""){
		alert("请输入拒绝理由!");
		return;
	}
	var i = confirm("确定不同意退/换货?");
	if(!i){
		return;
	}
	$.post(
		ctxpath+"/store/order/refundHandle/"+refundId+"/"+"1",
		{"reason":$reason},
		function(data){
			if(data){
				window.location.href = ctxpath+"/store/order/returnProduct";
			}else{
				alert("操作失败，稍后重试！");
			}
	});
}

// 理由的重置,取消按钮
function reset(){
	var tObj = document.getElementById("refundReason");
	tObj.value = "";
	$(".decline").hide();
}

// 返回列表
function goBack(){
	window.history.go(-1);
}