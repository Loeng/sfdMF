$(document).ready(function(){
	var mark = $("#mark").val();
	if(mark == "false"){
		salertWithFunction("请先绑定银行卡", jumpBank, null);
	} 
});

function jumpBank(){
	window.location.href = $("#ctxpath").val() + "/store/manager/ACCOUNT_BANK_LIST";
}

// 查找
function selectOrder(){
	var orderId = $.trim($("#contractId").val());
	if(orderId == ""){
		falert("请输入合同编号");
		return ;
	}
	
	$("#orderDetail").load($("#ctxpath").val() + "/store/finance/getContractLoad",{contractNo:orderId},function(){
		var val = $.trim($("input[name='contractId']").val());
		if(val == ""){
			falert("合同编号输入有误");
		}
	});
}

function checkOrderId(){
	var orderId = $.trim($("#contractId").val());
	var val = $.trim($("input[name='contractId']").val());
	
	if(orderId == ""){
		falert("请输入合同编号");
		return false;
	}
	if(orderId != "" && val == ""){
		falert("请输入有效的合同编号");
		return false;
	}
	return true;
}

function checkBank(){
	var bankId = $.trim($("select[name='bankId']").val());
	var obj = $('#bankSpan');
	
	if(bankId == ""){
		$(obj).show();
		$(obj).text("请选择借贷银行");
		return false;
	}
	$(obj).hide();
	return true;
}

function checkMoney(){
	var money = $.trim($("input[name='amount']").val());
	var regexp = /^([1-9][\d]{0,9}|0)(\.[\d]{1,2})?$/;
	var obj = $('#amountSpan');
	
	if(money == ""){
		$(obj).show();
		$(obj).text("请输入借款金额");
		return false;
	}
	if(!regexp.test(money)){
		$(obj).show();
		$(obj).text("请输入正确的借款金额");
		return false;
	}
	if(parseFloat(money) <= 0){
		$(obj).show();
		$(obj).text("请输入大于零的借款金额");
		return false;
	}
	$(obj).hide();
	return true;
}

function checkNumber(){
	var manumber = $.trim($("input[name='manumber']").val());
	var regexp = /^\d+$/;
	var obj = $('#manumberSpan');
	
	if(manumber == ""){
		$(obj).show();
		$(obj).text("请输入借款期数");
		return false;
	}
	if(!regexp.test(manumber)){
		$(obj).show();
		$(obj).text("请输入正确的借款期数");
		return false;
	}
	if(parseFloat(manumber) <= 0){
		$(obj).show();
		$(obj).text("请输入大于零的借款期数");
		return false;
	}
	$(obj).hide();
	return true;
}

function checkContactName(){
	var money = $.trim($("input[name='contactName']").val());
	var obj = $('#contactNameSpan');
	
	if(money == ""){
		$(obj).show();
		$(obj).text("请输入联系人");
		return false;
	}
	$(obj).hide();
	return true;
}

function checkPhone(){
	var money = $.trim($("input[name='contactPhone']").val());
	var regexp = /^(1[0-9][0-9])\d{8}$/;
	var obj = $('#contactPhoneSpan');
	
	if(money == ""){
		$(obj).show();
		$(obj).text("请输入联系电话");
		return false;
	}
	if(!regexp.test(money)){
		$(obj).show();
		$(obj).text("请输入正确的联系电话");
		return false;
	}
	$(obj).hide();
	return true;
}

function saveMaInfo(){
	if(checkOrderId() && checkBank() && checkMoney() && checkNumber() && checkContactName() && checkPhone()){
		$("#shenSubmitForm").ajaxSubmit(function(data){
			// 1:成功，2:失败，3:订单号不能空，4:订单合同不能为空
			if(data == 1){
				salertWithFunction("申请提交成功", haosssxxx, null);
			}else if(data == 2){
				falert("申请提交失败");
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
	window.location.href = $("#ctxpath").val() + "/store/manager/FINANCE_MANGER_QY_LIST";
}

function updateMaInfo(){
	if(checkOrderId() && checkBank() && checkMoney() && checkNumber() && checkContactName() && checkPhone()){
		$("#updateSubmitForm").ajaxSubmit(function(data){
			// 1:成功，2:失败，3:订单号不能空，4:订单合同不能为空
			if(data == 1){
				salertWithFunction("修改成功", haosssxxx, null);
			}else if(data == 2){
				falert("修改失败");
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

