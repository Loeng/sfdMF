// 重置
function reset(){
	document.getElementById("modifyStoreForm").reset();
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


//验证会员名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 6 || nameStr.length >20){
		document.getElementById("nameDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}

//验证密码格式
function checkPassword(){
	var psdStr = document.getElementsByName('password')[0].value;
	var strong = checkStrong(psdStr);
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
		document.getElementById("pwdDd").className="text_d_ts";
		return true;
	}

}

//验证密码格式
function checkPasswordBlur(){
	var psdStr = document.getElementsByName('password')[0].value;
	if(psdStr.length < 6 || psdStr.length >32){
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
		document.getElementById("pwdsDd").className="text_d_ts";
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


// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/store/list";
	}
	window.location.href=contextPath + "/system/store/list";
}
//提交
function formSubmit() {
	if(checkNameBlur() && checkPasswordBlur() && checkPwdSameBlur() && checkStoreNameBlur() &&checkMobileBlur()&&checkEmailBlur()&&checkCardNumberBlur()){
		document.getElementById("modifyStoreForm").submit();
	}else{
		checkNameBlur();
		checkPasswordBlur();
		checkPwdSameBlur();
		checkStoreNameBlur();
		checkMobileBlur();
		checkEmailBlur();
		checkCardNumberBlur();
	}
}

//鼠标移除文本框的时候 验证店铺名不能为空
function checkStoreNameBlur(){
	if(document.getElementById("storeName").value == ""){
		document.getElementById("storeNameDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("storeNameDd").className="";
		return true;
	}
}	

$(function(){
	if($("#modifyOk").val()=="true"){
		econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
	}else if(!$("#modifyOk").val()=="false"){
		ealert("修改失败！");
	}
	$("input.i_bg").focus(function (){ 
			$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 
});	

//收索出市
function searchFirstName(id){
	var _contextPath = $("#ctxpath").val();
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/system/systemArea/plist/"+id,
			 	function(data){
			 		var systemAreas = jQuery.parseJSON(data);
			 		if(systemAreas.length == 0 || systemAreas==null){
			 			$("#jsonBox2").get(0).innerHTML ="";
			 			$("#jsonBox3").get(0).innerHTML ="";
			 		}else{
			 		var result = '<option value="==请选择==" >==请选择==</option>';
			 		for(var i=0;i<systemAreas.length;i++) {
			 			result += '<option value="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</option>';
			 		}
			 		$('#jsonBox2').get(0).innerHTML = result;
			 		$('#jsonBox3').get(0).innerHTML = "";
			 		}
			 	}
			 );
	}else{
		$.post(
				_contextPath + "/system/systemArea/plist/"+id,
			 	function(data){
					var systemAreas = jQuery.parseJSON(data);
			 		var result = '<option value="==请选择==" >==请选择==</option>';
			 		for(var i=0;i<systemAreas.length;i++) {
			 			result += '<option value="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</option>';
			 		}
			 		$('#jsonBox2').get(0).innerHTML = result;
			 		$('#jsonBox3').get(0).innerHTML = "";
			 	}
			 );
	}
	$(this).next("div.pro_glpp").show();	
}

//收索区
function searchTwoName(id){
	var _contextPath = $("#ctxpath").val();
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/system/systemArea/plist/"+id,
			 	function(data){
			 		var systemAreas = jQuery.parseJSON(data);
			 		if(systemAreas.length == 0 || systemAreas==null){
			 			$("#jsonBox3").get(0).innerHTML ="";

			 		}else{
			 		var result = '<option value="==请选择==" >==请选择==</option>';
			 		for(var i=0;i<systemAreas.length;i++) {
			 			result += '<option value="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</option>';
			 		}
			 		$('#jsonBox3').get(0).innerHTML = result;
			 		}
			 	}
			 );
	}else{
		$.post(
				_contextPath + "/system/systemArea/plist/"+id,
			 	function(data){
					var systemAreas = jQuery.parseJSON(data);
			 		var result = '<option value="==请选择==" >==请选择==</option>';
			 		for(var i=0;i<systemAreas.length;i++) {
			 			result += '<option value="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</option>';
			 		}
			 		$('#jsonBox3').get(0).innerHTML = result;
			 	}
			 );
	}
	$(this).next("div.pro_glpp").show();	
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
//验证店铺角色是否添加
function checkRole(){
	if(document.getElementById("roleId").value==""){
		document.getElementById("roleIdDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("roleIdDd").className="";
		return true;
	}
}

$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	$(function(){
		$(".gg_dp_logo img").click(function(){
			$(this).parent().parent().next("div.cz_tc").show();
		});
		$(".cz_tc a.qx").click(function(){
			$(this).parent("div.cz_tc").hide();
		});
		$(".ht_area img").click(function(){
			$(this).next(".gg_area").show();
		});
		$(".area_two label").click(function(){
			$(this).addClass("xz_label").siblings().removeClass("xz_label");
			//$(this).nextAll().slideToggle();		
		});
		$(".area_three b").click(function(){
			$(this).parents("div.gg_area").hide();
			$(this).parents("li").removeClass("xz_li");
		});
		$(".area_box ul li").hover(function(){
			$(this).addClass("xz_li");
			},function(){
				$(this).removeClass("xz_li");	
				$(this).children().children("label").removeClass("xz_label");	
		});
	})
});

//会员等级弹出层的搜索
function loadUserLevel(){
	$("#userLeveltc").load($("#ctxpath").val()+"/system/storeLevel/plistSearch",function(){
		
	});
	$("#userLevelBg").show();
	$("#userLeveltc").show();
}	

//选择会员等级
function sreachStore(name,id){
	$('#levelName').val(name);
	$('#levelId').val(id);
	$("#userLevelBg").hide();
	$("#userLeveltc").hide();
}
//关闭弹出层
function apClose(){
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}