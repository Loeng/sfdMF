function checkRefundType(){
	var rt = $("select[name='refundType']").val();
	if($.trim(rt) == ""){
		falert("请选择退换货理由");
		return false;
	}
	return true;
}

function checkNote(){
	var note = $.trim($("textarea[name='note']").val());
	if(note == ""){
		falert("请输入拒绝理由");
		return false;
	}
	return true;
}

function formSubmit() {
	if(checkRefundType() && checkNote()){
		$("#addRefundForm").ajaxSubmit(function(data){
			if(data == true || data == "true"){
				window.location.href = $("#ctxpath").val() + "/user/alreadyBuy";
			}else{
				falert("申请失败");
			}
		});
	}
}

// 搜索
function submitRefund(){
	$("#orderReturnProduct").submit();
}

// 分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("orderReturnProduct").submit();
}

// 重置
function formReset(){
	$("#orderId").val("");
	$("#userName").val("");
	$("#beginDate").val("");
	$("#endDate").val("");
}

function refundDetail(ctx, id){
	window.location.href = ctx + "/user/order/returnProductDetail/" + id;
}
