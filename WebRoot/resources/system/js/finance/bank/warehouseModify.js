$(function(){
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('修改成功，是否返回列表？',goBack,[$("#ctxpath").val()],null,null);
	}else if($("#ok").val()=="false"){
		ealert("修改失败！");
	}
	
	/*增加图片*/
	$(".btnAdd").click(function(){
		var radomId = "popPic" + new Date().getTime();
		var obj=$("#picForClone").html();
		obj = obj.replaceAll("picClone",radomId);
		$(this).before(obj);
		var popPicNum = $("#popPicProperties").val();
		popPicNum = popPicNum + radomId + ";";
		$("#popPicProperties").val(popPicNum);
	})
	
	/*删除图片*/
	$(".uploadItem .closeIco").live("click",function(){
		$(this).parents(".uploadItem").remove();
		var popPicNum = $("#popPicProperties").val();
		var oldId = $(this).attr("id") + ";";
		popPicNum = popPicNum.replaceAll(oldId,"");
		$("#popPicProperties").val(popPicNum);
	})
});
//选择物业名称
function sreachCommunity(name,id){
	$('#managementName').val(name);
	$('#managementId').val(id);
	$(".del_tcBg").hide();
	$(".del_tc").hide();
	document.getElementById("managementNameDd").className="";
}
//关闭弹出层
function apClose(){
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}
// 返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/community/communityList";
	}
	window.location.href=contextPath + "/system/community/communityList";
}
//会员等级弹出层的搜索
function loadCommunityManageMent(){
	$("#communitytc").load($("#ctxpath").val()+"/system/community/plistSearch");
	$("#communityBg").show();
	$("#communitytc").show();
}	


//验证会员名格式
function checkUserName(){
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
function checkUserNameBlur(name,contextPath){
	var _oldName = $("#oldName").val();
	if(name == null || name == "" || name == " " || !/^[1][0-9]{10}$/.test(name)){
		$("#nameSpan").html("用户名为手机号格式");
		document.getElementById("nameDd").className="text_c_ts";
		return false;
	}else{
		if(name != _oldName){
			$.post(
					contextPath + "/system/user/checkName/"+name,
				 	function(data){
				 		if(!data){
				 			flag = false;
				 			$("#nameSpan").html("用户名已存在");
				 			document.getElementById("nameDd").className="text_c_ts";
				 		}else{
				 			$("#nameSpan").html("用户名为手机号格式");
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
function checkUserName(){
	var nameStr = document.getElementsByName('userName')[0].value;
	if(!/^[1][0-9]{10}$/.test(nameStr)){
		$("#nameSpan").html("用户名为手机号格式");
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
	var strong = checkStrong($.trim(psdStr));
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
	var psdStr = $.trim(document.getElementsByName('password')[0].value);
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


function formSubmit(){
	if(flag && checkUserName() && checkPasswordBlur() && checkPwdSameBlur()&&checkNameBlur()&&checkAddressBlur()&&checkpca()&&checkManagementNameBlur()&&checkTelBlur()){
		document.getElementById("communityModifyForm").submit();
	}else{
		checkNameBlur();
		checkAddressBlur();
		checkpca();
		checkManagementNameBlur();
		checkTelBlur();
		checkPasswordBlur();
		checkPwdSameBlur();
		checkUserName();
	}
}

function checkNameBlur(){
	if($("#class_name").val() == '' || $("#class_name").val() == null || $("#class_name").val() == ' ' ){
		$("#NameDd").addClass("text_c_ts");
		return false;
	}
	document.getElementById("NameDd").className="";
	return true;
}
function checkAddressBlur(){
	if($("#fulladdress").val() == '' || $("#fulladdress").val() == null || $("#fulladdress").val() == ' '){
		$("#addrDd").addClass("text_c_ts");
		return false;
	}
	document.getElementById("addrDd").className="";
	return true;
}
function checkManagementNameBlur(){
	if($("#managementName").val() == '' || $("#managementName").val() == null || $("#managementName").val() == ' '){
		$("#managementNameDd").addClass("text_c_ts");
		return false;
	}
	document.getElementById("managementNameDd").className="";
	return true;
}
function checkTelBlur(){
	if($("#serviceTel").val() == '' || $("#serviceTel").val() == null || $("#serviceTel").val() == ' '){
		$("#serviceTelDd").addClass("text_c_ts");
		return false;
	}
	document.getElementById("serviceTelDd").className="";
	return true;
}

//收索出市
function searchFirstName(id){
	var _contextPath = $("#ctxpath").val();
	$("#address_id").val(id);
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
			 		var result = '<option value="" >==请选择==</option>';
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
	checkpca();
}

function checkpca(){
	if($.trim($("#address_id").val()) == '' ){
		document.getElementById("addressDd").className="formSite text_c_ts";
		return false;
	}else{
		document.getElementById("addressDd").className="formSite";
		return true;
	}
}

//搜索区
function searchTwoName(id){
	var _contextPath = $("#ctxpath").val();
	$("#address_id").val(id);
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/system/systemArea/plist/"+id,
			 	function(data){
			 		var systemAreas = jQuery.parseJSON(data);
			 		if(systemAreas.length == 0 || systemAreas==null){
			 			$("#jsonBox3").get(0).innerHTML ="";

			 		}else{
			 		var result = '<option value="" >==请选择==</option>';
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

function searchThreeName(id){
	$("#address_id").val(id);
}