$(document).ready(function(){
	var mark = $("#biaomark").val();
	if(mark == "false"){
		salertWithFunction("请先绑定银行卡", jumpBank, null);
	} 
});

function jumpBank(){
	var ctx = $("#ctxpath").val();
	window.location.href = ctx + "/store/manager/ACCOUNT_SECURITY";
}

// 查找
function selectOrder(){
	var ctx = $.trim($("#ctxpath").val());
	var orderId = $.trim($("#tempOrderId").val());	
	$("#orderDetail").load(ctx + "/store/finance/getOrderDetailByOrderId",{orderId:orderId},function(){
		var val = $.trim($("#orderId").val());
		if(orderId == ""){
			falert("请输入订单号");
		}else if(val == ""){
			falert("订单号输入有误");
		}
	});
}

function checkOrderId(){
	var orderId = $.trim($("#tempOrderId").val());
	var val = $.trim($("#orderId").val());
	
	if(orderId == ""){
		falert("请输入订单号");
		return false;
	}
	if(orderId != "" && val == ""){
		falert("请输入有效的订单号");
		return false;
	}
	return true;
}

function checkBank(){
	var bankId = $.trim($("select[name='bankId']").val());
	
	if(bankId == ""){
		$('#bankSpan').show();
		$('#bankSpan').text("请选择借贷银行");
		return false;
	}
	$('#bankSpan').hide();
	return true;
}

function checkMoney(){
	var money = $.trim($("input[name='amount']").val());
	var regexp = /^([1-9][\d]{0,9}|0)(\.[\d]{1,2})?$/;
	var countsum = $.trim($("#countsum").val());
	if(money == ""){
		$('#amountSpan').show();
		$('#amountSpan').text("请输入借款金额");
		return false;
	}
	if(!regexp.test(money)){
		$('#amountSpan').show();
		$('#amountSpan').text("请输入正确的借款金额");
		return false;
	}
	if(parseFloat(money) <= 0){
		$('#amountSpan').show();
		$('#amountSpan').text("请输入大于零的借款金额");
		return false;
	}
	if(parseFloat(countsum) < parseFloat(money)){
		$('#amountSpan').show();
		$('#amountSpan').text("借款金额必须小于等于订单金额");
		return false;
	}
	$('#amountSpan').hide();
	return true;
}

function checkNumber(){
	var manumber = $.trim($("input[name='manumber']").val());
	var regexp = /^\d+$/;
	
	if(manumber == ""){
		$('#manumberSpan').show();
		$('#manumberSpan').text("请输入借款期数");
		return false;
	}
	if(!regexp.test(manumber)){
		$('#manumberSpan').show();
		$('#manumberSpan').text("请输入正确的借款期数");
		return false;
	}
	if(parseFloat(manumber) <= 0){
		$('#manumberSpan').show();
		$('#manumberSpan').text("请输入大于零的借款期数");
		return false;
	}
	$('#manumberSpan').hide();
	return true;
}

function checkContactName(){
	var money = $.trim($("input[name='contactName']").val());
	
	if(money == ""){
		$('#contactNameSpan').show();
		$('#contactNameSpan').text("请输入联系人");
		return false;
	}
	$('#contactNameSpan').hide();
	return true;
}

function checkPhone(){
	var money = $.trim($("input[name='contactPhone']").val());
	var regexp = /^(1[0-9][0-9])\d{8}$/;
	
	if(money == ""){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入联系电话");
		return false;
	}
	if(!regexp.test(money)){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入正确的联系电话");
		return false;
	}
	$('#contactPhoneSpan').hide();
	return true;
}

function saveMaInfo(type, mark){
	var ctx = $.trim($("#ctxpath").val());
	if(checkOrderId() && checkBank() && checkMoney() && checkNumber() && checkContactName() && checkPhone()){
		$("#shenSubmitForm").ajaxSubmit(function(data){
			// 1:成功，2:失败，3:订单号不能空，4:订单合同不能为空
			if(data == 1){
				var mess = "";
				if(mark == 1){
					mess = "申请提交成功";
				}else if(mark == 3){
					mess = "修改申请成功"
				}
				salertWithFunction(mess, haosssxxx, null);
			}else if(data == 2){
				var mess = "";
				if(mark == 1){
					mess = "申请提交失败";
				}else if(mark == 3){
					mess = "修改申请失败"
				}
				falert(mess);
			}else if(data == 3){
				falert("订单号不能空");
			}else if(data == 4){
				falert("请上传订单合同");
			}
		});
	}else{
		checkBank();
		checkMoney();
		checkNumber();
		checkContactName();
		checkPhone();
	}
}

function haosssxxx(){
	var ctx = $("#ctxpath").val();
	window.location.href = ctx + "/store/manager/FINANCE_MANGER_ORDER";
}

function jumpLoadApply(ctx, type){
	window.location.href = ctx + "/store/finance/jumpOrdersLoanListPage/" + type;
}

