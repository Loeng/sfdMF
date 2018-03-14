$(document).ready(function(){
	var mark = $("#mark").val();
	if(mark == "false"){
		salertWithFunction("请先绑定银行卡", jumpBank, null);
	} 
});

function jumpBank(){
	window.location.href = $("#ctxpath").val() + "/store/manager/ACCOUNT_BANK_LIST";
}

// 银行卡
function checkBank(){
	var bankId = $.trim($("select[name='bankId']").val());
	var obj = $('#bankSpan');
	
	if(bankId == ""){
		$(obj).show();
		$(obj).html("请选择银行卡");
		return false;
	}
	$(obj).hide();
	return true;
}

// 贷款最长等待时间
function checkMaxTime(){
	var max = $.trim($("input[name='loanMaxWaitTime']").val());
	var regexp = /^\d+$/;
	var obj = $('#loanMaxWaitTimeSpan');
	
	if(max == ""){
		$(obj).show();
		$(obj).text("请输入贷款最长等待时间");
		return false;
	}
	if(!regexp.test(max)){
		$(obj).show();
		$(obj).text("请输入正确的贷款最长等待时间");
		return false;
	}
	if(parseFloat(max) <= 0){
		$(obj).show();
		$(obj).text("请输入大于零的贷款最长等待时间");
		return false;
	}
	$(obj).hide();
	return true;
}

// 融资额
function checkMoney(){
	var money = $.trim($("input[name='amount']").val());
	var regexp = /^([1-9][\d]{0,9}|0)(\.[\d]{1,2})?$/;
	var obj = $('#amountSpan');
	
	if(money == ""){
		$(obj).show();
		$(obj).text("请输入融资额");
		return false;
	}
	if(!regexp.test(money)){
		$(obj).show();
		$(obj).text("请输入正确的融资额");
		return false;
	}
	if(parseFloat(money) <= 0){
		$(obj).show();
		$(obj).text("请输入大于零的融资额");
		return false;
	}
	$(obj).hide();
	return true;
}

// 联系人
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

// 手机号
function checkPhone(){
	var money = $.trim($("input[name='contactPhone']").val());
	var regexp = /^(1[0-9][0-9])\d{8}$/;
	var obj = $('#contactPhoneSpan');
	
	if(!regexp.test(money)){
		$(obj).show();
		$(obj).text("请输入正确的联系电话");
		return false;
	}
	$(obj).hide();
	return true;
}

function saveMaInfo(){
	if(checkBank() && checkMaxTime() && checkMoney() && checkContactName() && checkPhone()){
		$("#shenSubmitForm").ajaxSubmit(function(data){
			// 1:成功，2:失败，4:订单合同不能为空
			if(data == 1){
				salertWithFunction("申请提交成功", haosssxxx, null);
			}else if(data == 2){
				falert("申请提交失败");
			}else if(data == 4){
				falert("请上传抵押物文件");
			}
		});
	}else{
		checkBank();
		checkMaxTime();
		checkMoney();
		checkContactName();
		checkPhone();
	}
}

function haosssxxx(){
	var ctx = $("#ctxpath").val();
	window.location.href = ctx + "/store/manager/FINANCE_MANGER_CREDIT";
}

function updateMaInfo(){
	if(checkBank() && checkMaxTime() && checkMoney() && checkContactName() && checkPhone()){
		$("#updateSubmitForm").ajaxSubmit(function(data){
			// 1:成功，2:失败，4:订单合同不能为空
			if(data == 1){
				salertWithFunction("修改成功", haosssxxx, null);
			}else if(data == 2){
				falert("修改失败");
			}else if(data == 4){
				falert("请上传抵押物文件");
			}
		});
	}else{
		checkBank();
		checkMaxTime();
		checkMoney();
		checkContactName();
		checkPhone();
	}
}


