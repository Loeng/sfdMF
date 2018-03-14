// 分页跳转
function goPage(pageNum){
	var ctxpath = $("#ctxpath").val();
	var orderId = $("#orderId").val();
	window.location.href = ctxpath+"/store/order/orderEvaluateHandel/"+orderId+"/"+pageNum;
}




// 单条评价的提交
function evaluateOrder(){
	econfirm("确定要提交评价?",evaluateOrderStore,null,null,null);
}

function evaluateOrderStore(){
	$("#evaluateToUserId").submit();
}

// 好评、中评、差评
function setEvaluate(value){
	$("#hjcetype").val(value);
}


function checkJudgAppr(){
	var vals="";
	$("input[type='radio']").each(function(){
		if(this.checked){
			vals += ","+$(this).val();
		}
	});
	$("#judgType").val(vals.substring(1));
}