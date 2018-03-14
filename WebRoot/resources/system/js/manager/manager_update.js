$(function(){
	$(".ht_btn").next().addClass("afterHt");
});	

// 重置
function reset(id){
	window.location.href = $("#ctxpath").val() + "/system/manager/detail/" + id;
}

// 返回列表
function goBack(){
	window.location.href = $("#ctxpath").val() + "/system/manager/list";
}

function checkFocus(obj) {
	$(obj).parent().removeAttr("class");
	$(obj).next().html("");
}

function errorMes(iobj, mess) {
	$(iobj).parent().attr("class", "text_c_ts");
	$(iobj).next().html(mess);
}

// 验证管理员名格式
function checkNameBlur(){
	var iobj = $("input[name='name']");
	var ival = $.trim($(iobj).val());
	
	if(ival == ""){
		errorMes(iobj, "请输入管理员名");
		return false;
	}
	if(ival.length < 4 || ival.length > 20){
		errorMes(iobj, "管理员名为4-20位字符组成");
		return false;
	}
	return true;
}

// 验证密码格式
function checkPasswordBlur(){
	var iobj = $("input[name='password']");
	var ival = $.trim($(iobj).val());
	
	if(ival == ""){
		errorMes(iobj, "请输入密码");
		return false;
	}
	if(ival.length < 6 || ival.length >32){
		errorMes(iobj, "密码为6-32位字符组成");
		return false;
	}
	return true;
}

// 验证密码是否一致
function checkPwdSameBlur(){
	var iobj = $("#cpwd");
	var ival = $.trim($(iobj).val());
	var pwd = $.trim($("input[name='password']").val());
	
	if(ival == ""){
		errorMes(iobj, "请输入确认密码");
		return false;
	}
	if(pwd != ival){
		errorMes(iobj, "两次密码输入不一致");
		return false;
	}
	return true;
}

// 验证管理员角色是否添加
function checkRole(){
	var iobj = $("select[name='roleId']");
	var ival = $.trim($(iobj).val());
	
	if(ival == ""){
		errorMes(iobj, "请选择管理员角色");
		return false;
	}else{
		$(iobj).parent().removeAttr("class");
		$(iobj).next().html("");
	}
	return true;
}

// 验证手机号格式
function checkMobileBlur(){
	var iobj = $("input[name='mobile']");
	var ival = $.trim($(iobj).val());
	var regexp = /^1\d{10}$/;
	
	if(!regexp.test(ival)){
		errorMes(iobj, "请输入正确的手机号");
		return false;
	}
	return true;
}

// 验证邮箱格式
function checkEmailBlur(){
	var iobj = $("input[name='email']");
	var ival = $.trim($(iobj).val());
	var regexp = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	
	if(!regexp.test(ival)){
		errorMes(iobj, "请输入正确的email");
		return false;
	}
	return true;
}

// 提交
function formSubmit(){
    if(checkNameBlur() && checkPasswordBlur() && checkPwdSameBlur() && checkRole() && checkMobileBlur() && checkEmailBlur()){
    	$("#modifyManagerForm").ajaxSubmit(function(data){
    		if(data == 1){
    			ealert("修改成功");
    		}else{
    			ealert("修改失败");
    		}
    	});
    }
}
