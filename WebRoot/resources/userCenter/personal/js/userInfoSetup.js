
function updateUserInfo(){
	if(checkRealNameBlur() && checkCardNumberBlur() && checkMobileBlur() && checkAddressBlur()){
		var areaid = $("input[name='areaId']").val();
		if(areaid == ""){
			var obj = $("#areaid");
			$(obj).css("visibility", "visible");
			$(obj).html("请选择省市区");
			return;
		}
		$("#userInfo").ajaxSubmit(function(data){
			if(data == true || data == "true"){
				salertWithFunction("修改成功！",reload,null);
			}else{
				salert("修改个人资料失败");
			}
		});
	}
}
function reload(){
	window.location.reload();
}
function checkRealNameFocus(obj){
	$(obj).next().css("visibility", "hidden");
}

function checkRealNameBlur(){
	var realName = $.trim($("input[name='realName']").val());
	var obj = $("#realName");
	
	if(realName == ""){
		$(obj).css("visibility", "visible");
		$(obj).html("请输入真实姓名");
		return false;
	}
	return true;
}

function checkCardNumberFocus(obj){
	$(obj).next().css("visibility", "hidden");
}

function checkCardNumberBlur(){
	var regexp = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	var cardNumber = $.trim($("input[name='cardNumber']").val());
	var obj = $("#cardNumber");
	
	if(cardNumber == ""){
		$(obj).css("visibility", "visible");
		$(obj).html("请输入身份证号");
		return false;
	}
	if(!regexp.test(cardNumber)){
		$(obj).css("visibility", "visible");
		$(obj).html("请输入正确身份证号");
		return false;
	}
	return true;
}

function checkMobileFocus(obj){
	$(obj).next().css("visibility", "hidden");
}

function checkMobileBlur(){
	var regexp = /^1\d{10}$/;
	var mobile = $.trim($("input[name='mobile']").val());
	var obj = $("#mobile");
	
	if(mobile == ""){
		$(obj).css("visibility", "visible");
		$(obj).html("请输入手机号码");
		return false;
	}
	if(!regexp.test(mobile)){
		$(obj).css("visibility", "visible");
		$(obj).html("请输入正确手机号码");
		return false;
	}
	return true;
}

function checkAddressFocus(obj){
	$(obj).next().css("visibility", "hidden");
}

function checkAddressBlur(){
	var realName = $.trim($("input[name='address']").val());
	var obj = $("#address");
	
	if(realName == ""){
		$(obj).css("visibility", "visible");
		$(obj).html("请输入详细地址");
		return false;
	}
	return true;
}