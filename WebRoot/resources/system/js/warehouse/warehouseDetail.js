$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");

	/*全选*/
	$(".checkAll").live("click",function() {
		var obj=$(this).find("input");
		if(obj.is(":checked")){
			$(".colorSizeTable td input[type='checkbox']").attr("checked",true);
		}else{
			$(".colorSizeTable td input[type='checkbox']").attr("checked",false);
		}
	});
	$("input.i_bg").focus(function (){ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 

	
});




//返回列表
function goList(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/warehouse/list";
	}
	window.location.href=contextPath + "/system/warehouse/list";
}

//重置
function reset(){
	document.getElementById("modifyWarehouseForm").reset();
}
//验证项目名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >32 || nameStr ==" "){
		document.getElementById("nameDd").className="text_ts";
		$("#nameCheck").val("false");
		return false;
	}else{
		$("#nameCheck").val("true");
		return true;
	}
}

//验证项目名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >32 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		$("#nameCheck").val("true");
		return true;
	}
}



//验证详细地址是否为空
function checkAddressBlur(){
	if($.trim($("#fulladdress").val()) == '' || $("#fulladdress").val() == null){
		$("#addrDd").addClass("text_c_ts");
		return false;
	}
	document.getElementById("addrDd").className="";
	return true;
}

//提交
function formSubmit() {
	
	if(checkNameBlur() && checkAddressBlur()){
		$("#modifyWarehouseForm").ajaxSubmit(
		 		function(data){
		 			if(data==true || data=="true"){
		 				econfirm('修改成功，是否返回列表？',goList,[$("#ctxpath").val()],null,null);
		 			}else{
		 				ealert("修改失败！");
		 			}
		 		});
	}else{
		checkNameBlur();
		checkAddressBlur();
	}
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

