$(function(){
	if($("#modifyOk").val()=="true"||$("#modifyOk").val()==true){
		econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#modifyOk").val()=="false"){
		ealert("修改失败！");
	}
	$("input.i_bg").focus(function (){ 
			$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 
});	

// 重置
function reset(){
	document.getElementById("modifyUserForm").reset();
}

//验证会员名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 6 || nameStr.length >20){
		document.getElementById("nameDd").className="text_ts";
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		return true;
	}
}
var flag = true;
//验证用户名是否存在
function checkNameBlur(name,contextPath){
	var _oldName = $("#oldName").val();
	if(name == null || name == "" || name == " " || name.trim().length<6 || name.trim().length>16){
		$("#nameSpan").html("会员名为6-16位字符组成");
		document.getElementById("nameDd").className="text_c_ts";
		return false;
	}else{
		if(name != _oldName){
			$.post(
					contextPath + "/system/user/checkName",{name:name},
				 	function(data){
				 		if(!data){
				 			flag = false;
				 			$("#nameSpan").html("会员名已存在");
				 			document.getElementById("nameDd").className="text_c_ts";
				 		}else{
				 			$("#nameSpan").html("会员名为6-16位字符组成");
				 			document.getElementById("nameDd").className="";
				 			flag = true;
				 		}
				 	}
				 );
		}else{
			flag = true;
		}
	}
}

//验证会员名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.trim().length < 6 || nameStr.trim().length >32){
		$("#nameSpan").html("会员名为6-16位字符组成");
		document.getElementById("nameDd").className="text_c_ts";
		return false;
	}else{
		$("#nameSpan").html("会员名为6-16位字符组成");
		document.getElementById("nameDd").className="";
		return true;
	}
}

//验证密码格式
function checkPassword(){
	var psdStr = document.getElementsByName('password')[0].value;
	var strong = checkStrong(psdStr.trim());
	if(strong <= 1){
		document.getElementById("pswCheck").className="psdWeak";
		$("#storng").val("1");
	}else if(strong == 2){
		document.getElementById("pswCheck").className="psdMedium";
		$("#storng").val("2");
	}else{
		document.getElementById("pswCheck").className="psdForced";
		$("#storng").val("3");
	}
	if(psdStr.length < 6 || psdStr.length >32){
		document.getElementById("pwdDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("pwdDd").className="";
		return true;
	}

}

//验证密码格式
function checkPasswordBlur(){
	var psdStr = document.getElementsByName('password')[0].value;
	if(psdStr.trim().length < 6 || psdStr.trim().length >32){
		document.getElementById("pwdDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("pwdDd").className="";
		return true;
	}
}
//验证密码是否一致
function checkPwdSame(){
	var pwd = document.getElementById("pwd").value;
	var psdStr = document.getElementsByName('password')[0].value;
	if(pwd!=psdStr){
		document.getElementById("pwdsDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("pwdsDd").className="";
		return true;
	}

}

//验证密码是否一致
function checkPwdSameBlur(){
	var pwd = document.getElementById("pwd").value;
	var psdStr = document.getElementsByName('password')[0].value;
	if(pwd!=psdStr){
		document.getElementById("pwdsDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("pwdsDd").className="";
		return true;
	}
}

//提交
function formSubmit(){
	if(flag && checkName() && checkPasswordBlur() && checkPwdSameBlur()&&checkCardNumberBlur()&&checkEmailBlur()&&checkMobileBlur()){
		document.getElementById("modifyUserForm").submit();
	}else{
		checkPasswordBlur();
		checkPwdSameBlur();
		checkCardNumberBlur();
		checkEmailBlur();
		checkMobileBlur();
	}
}

// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/user/list";
	}
	window.location.href=contextPath + "/system/user/list";
}

//验证手机号格式
function checkMobile(){
	var n = $('#mobile').val();
	if(!/^[0-9]{11}$/.test(n)){
		document.getElementById("mobileDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("mobileDd").className="text_d_ts";
		return true;
	}
}
//验证手机号格式
function checkMobileBlur(){
	var n = $('#mobile').val();
	if(!/^[0-9]{11}$/.test(n)){
		document.getElementById("mobileDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("mobileDd").className="";
		return true;
	}
}
//验证邮箱格式
function checkEmail(){
	var n = $('#email').val();
	if(!/(\S)+[@]{1}(\S)+[.]{1}(\w)+/.test(n)){
		document.getElementById("emailDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("emailDd").className="text_d_ts";
		return true;
	}
}
//验证邮箱格式
function checkEmailBlur(){
	var n = $('#email').val();
	if(!/(\S)+[@]{1}(\S)+[.]{1}(\w)+/.test(n)){
		document.getElementById("emailDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("emailDd").className="";
		return true;
	}
}
//验证身份证格式
function checkCardNumber(){
	var n = $('#cardNumber').val();
	if(!/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/.test(n)&&!/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/.test(n)){
		document.getElementById("cardNumberDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("cardNumberDd").className="text_d_ts";
		return true;
	}
}
//验证身份证格式
function checkCardNumberBlur(){
	var n = $('#cardNumber').val();
	if(!/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/.test(n)&&!/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/.test(n)){
		document.getElementById("cardNumberDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("cardNumberDd").className="";
		return true;
	}
}
$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});
//会员等级弹出层的搜索
function loadUserLevel(){
	$("#userLeveltc").load($("#ctxpath").val()+"/system/userLevel/plistSearch",function(){
		
	});
	$("#userLevelBg").show();
	$("#userLeveltc").show();
}	

//选择会员等级
function sreachStore(name,id){
	$('#levelName').val(name);
	$("#userLevelBg").hide();
	$("#userLeveltc").hide();
}
//关闭弹出层
function apClose(){
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}