$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	$("input[type='text']").each(function(){
		$(this).focus(function(){
			$(this).parent().removeAttr("class");
		});
	});
	$("input[type='password']").each(function(){
		$(this).focus(function(){
			$(this).parent().removeAttr("class");
		});
	});
	
	$(".limitItem").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
			$main.next(".limitList").find("input").attr("checked",true);
			$main.next(".limitList").find(".check").addClass("checked");
			$main.parents().prev(".limitItem").find("input").attr("checked",true);
			$main.parents().prev(".limitItem").find(".check").addClass("checked");
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
			$main.next(".limitList").find(":checkbox").attr("checked",false);
			$main.next(".limitList").find(".check").removeClass("checked");
		}
	});
	$(".limitAll").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.nextAll(".limitList").find("input").attr("checked",true);
			$main.nextAll(".limitList").find(".check").addClass("checked");
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.nextAll(".limitList").find(":checkbox").attr("checked",false);
			$main.nextAll(".limitList").find(".check").removeClass("checked")
		}
	});
});

$(function(){
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
	
	$("input.i_bg").focus(function ()//得到焦点时触发的事件
	{ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function () //失去焦点时触发的事件
		{ 
		$(this).parent().removeClass("text_ts");
	}); 
});	


var flag = false;







function checkNameBlur(){
	var nameStr = $('#class_name').val();
	if(nameStr.length < 1 || nameStr.length >32 || nameStr ==" "){
		$("#NameDd").addClass("text_c_ts");
		return false;
	}else{
		document.getElementById("NameDd").className="";
		return true;
	}
}
function checkFullName(){
	if($("#fullName").val() == '' || $("#fullName").val() == null || $("#fullName").val() == ' '){
		$("#fullNameDd").addClass("text_c_ts");
		return false;
	}else{
		document.getElementById("fullNameDd").className="";
		return true;
	}
}
function checkAddressBlur(){
	if($("#fulladdress").val() == '' || $("#fulladdress").val() == null || $("#fulladdress").val() == ' '){
		$("#addrDd").addClass("text_c_ts");
		return false;
	}else{
		document.getElementById("addrDd").className="";
		return true;
	}
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

function checkAccountName(){
	if($("#accountName").val() == '' || $("#accountName").val() == null || $("#accountName").val() == ' '){
		$("#accountNameSpan").html("请输入账号");
		$("#accountNameDd").addClass("text_c_ts");
		return false;
	}else{
		var _contextPath = $("#ctxpath").val();
		$.post(_contextPath + '/system/bank/checkaccount',{accountName:$("#accountName").val(),accountId:$("#bankAccountId").val()},function (data){
			if(data==1 || data=="1"){
				$("#accountNameSpan").html("账号已被使用");
				$("#accountNameDd").addClass("text_c_ts");
				$("#canCommit").val("0");
				return false;
			}else{
				document.getElementById("accountNameDd").className="";
				return true;
			}
		});
	}
	
}
function checkAccountPassword(){
	if($("#password").val() == '' || $("#password").val() == null || $("#password").val() == ' '){
		$("#passwordDd").addClass("text_c_ts");
		return false;
	}else{
		document.getElementById("passwordDd").className="";
		return true;
	}
}
function checkRealName(){
	if($("#realName").val() == '' || $("#realName").val() == null || $("#realName").val() == ' '){
		$("#realNameDd").addClass("text_c_ts");
		return false;
	}else{
		document.getElementById("realNameDd").className="";
		return true;
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



//重置
function reset(){
	document.getElementById("addBankForm").reset();
}

//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href=contextPath + "/system/bank/list";
	}
	contextPath = $("#ctxpath").val();
	window.location.href=contextPath + "/system/bank/list";
}

//返回列表
function goAdd(){
	contextPath = $("#ctxpath").val();
	window.location.href=contextPath + "/system/finacne/bankAdd";
}

//提交
function formSubmit(){
	checkNameBlur();
	checkAddressBlur();
	checkFullName();
	checkAccountName();
	checkAccountPassword();
	checkRealName();
	if($(".text_c_ts").length>0){
		return;
	}else{
		$("#modifyBankForm").ajaxSubmit(function(data){
			if(data == "true" || data == true){
				if(confirm("银行信息保存成功，是否继续编辑？")){
					//goAdd(null);
				}else{
					goBack(null);
				}
				//econfirm("银行信息保存成功，是否继续新增？",goAdd,null,goBack,null);
			}else{
				alert("银行信息保存失败，请稍后再试！");
			}
		});
	}
}
