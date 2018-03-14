// 重置
function reset(){
	document.getElementById("modifyOrderForm").reset();
}

// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/order/list";
	}
	window.location.href=contextPath + "/system/order/list";
}
// 提交
function formSubmit(){
	if(checkUserNameBlur() && checkStoreNameBlur()){
	document.getElementById("modifyOrderForm").submit();
	}else{
		checkUserNameBlur();
		checkStoreNameBlur();
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


//验证店铺名格式
function checkStoreName(){
	var nameStr = document.getElementsByName('storeId')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("storeDd").className="text_ts";
		$("#nameSpan").html("店铺名为6-20位字符组成");
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("storeDd").className="text_d_ts";
		$("#nameSpan").html("店铺名为6-20位字符组成");
		$("#nameCheck").val("true");
		return true;
	}
}

// 验证店铺名格式
function checkStoreNameBlur(){
	var nameStr = $("#storeName").val();
	if(nameStr.length < 6 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("storeDd").className="text_c_ts";
		$("#storeSpan").html("店铺名为6-20位字符组成");
		$("#nameCheck").val("false");
		return false;
	}else{
		    getStoreIdByName();//执行验证操作
			document.getElementById("storeDd").className="";
			$("#nameCheck").val("true");
			return true;
	}
}
// 验证店铺名是否存在  如果存在根据店铺名查询出店铺对应的Id
function getStoreIdByName(){
	var ctxpath = $("#ctxpath").val();
	var storeName= $("#storeName").val();
	$.post(ctxpath+"/system/order/getStoreId/"+storeName,
			function(storeId){
		if(storeId != null && storeId.length > 0){
			$("#storeId").val(storeId);
		}else{
			document.getElementById("storeDd").className="text_c_ts";
			$("#storeSpan").html("店铺名不存在");
		}
	});
}


//验证用户名格式
function checkUserName(){
	var nameStr = document.getElementsByName('userId')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("用户名为1-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("用户名为1-20位字符组成");
		return true;
	}
}

//验证用户名格式
function checkUserNameBlur(){
	var nameStr = $("#userName").val();
	if(nameStr.length < 6 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("用户名为6-20位字符组成");
		return false;
	}else{
		    getUserIdByName();
			document.getElementById("nameDd").className="";
			$("#nameCheck").val("true");
			return true;
	}
}

//根据用户名查询出用户id
function getUserIdByName(){
	var ctxpath = $("#ctxpath").val();
	var userName= $("#userName").val();
	$.post(ctxpath+"/system/order/getUserId/"+userName,
			function(userId){
		if(userId != null && userId.length > 0){
			$("#userId").val(userId);
		}else{
			document.getElementById("nameDd").className="text_c_ts";
			$("#nameSpan").html("该用户不存在");
		}
	});
}