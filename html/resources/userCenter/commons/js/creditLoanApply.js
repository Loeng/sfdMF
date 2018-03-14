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

// 银行卡
function checkBank(){
	var bankId = $.trim($("select[name='bankId']").val());
	if(bankId == ""){
		$('#bankSpan').show();
		return false;
	}
	$('#bankSpan').hide();
	return true;
}

// 贷款最长等待时间
function checkMaxTime(){
	var max = $.trim($("input[name='loanMaxWaitTime']").val());
	var regexp = /^\d+$/;
	
	if(max == ""){
		$('#loanMaxWaitTimeSpan').show();
		$('#loanMaxWaitTimeSpan').text("请输入贷款最长等待时间");
		return false;
	}
	if(!regexp.test(max)){
		$('#loanMaxWaitTimeSpan').show();
		$('#loanMaxWaitTimeSpan').text("请输入正确的贷款最长等待时间");
		return false;
	}
	if(parseFloat(max) <= 0){
		$('#loanMaxWaitTimeSpan').show();
		$('#loanMaxWaitTimeSpan').text("请输入大于零的贷款最长等待时间");
		return false;
	}
	$('#loanMaxWaitTimeSpan').hide();
	return true;
}

// 融资额
function checkMoney(){
	var money = $.trim($("input[name='amount']").val());
	var regexp = /^([1-9][\d]{0,9}|0)(\.[\d]{1,2})?$/;
	
	if(money == ""){
		$('#amountSpan').show();
		$('#amountSpan').text("请输入融资额");
		return false;
	}
	if(!regexp.test(money)){
		$('#amountSpan').show();
		$('#amountSpan').text("请输入正确的融资额");
		return false;
	}
	if(parseFloat(money) <= 0){
		$('#amountSpan').show();
		$('#amountSpan').text("请输入大于零的融资额");
		return false;
	}
	$('#amountSpan').hide();
	return true;
}

// 联系人
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

// 手机号
function checkPhone(){
	var money = $.trim($("input[name='contactPhone']").val());
	var regexp = /^(1[0-9][0-9])\d{8}$/;
	if(!regexp.test(money)){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入正确的联系电话");
		return false;
	}
	$('#contactPhoneSpan').hide();
	return true;
}

function saveMaInfo(type, mark){
	if(checkBank() && checkMaxTime() && checkMoney() && checkContactName() && checkPhone()){
		$("#shenSubmitForm").ajaxSubmit(function(data){
			// 1:成功，2:失败，4:订单合同不能为空
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

function jumpLoadApply(ctx, type){
	window.location.href = ctx + "/store/finance/jumpOrdersLoanListPage/" + type;
}

