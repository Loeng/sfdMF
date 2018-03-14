//后台该阶段计划不需要在后台新增订单  该功能暂时关闭
var flag = false;
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

// 提交
function formSubmit(){
	if(checkUserNameBlur() && flag && checkStoreNameBlur()){
	document.getElementById("addOrderForm").submit();
	}else{
		checkUserNameBlur();
		checkStoreNameBlur();
		var id = document.getElementsByName('id')[0].value;
		checkIdBlur (id);
	}
}

// 重置
function reset(){
	document.getElementById("addOrderForm").reset();
}

//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href = "/system/order/list";
	}
	window.location.href=contextPath + "/system/order/list";
}

// 验证订单编号是否存在
function checkId(id,contextPath){
	if(id == null || id == "" || id == " " || id.length<6 || id.length>16){
		document.getElementById("idDd").className="text_c_ts";
		$("#idSpan").html("订单编号为6-16位字符");
		$("#idCheck").val("false");
		return false;
	}else{
		document.getElementById("idDd").className="text_d_ts";
		$("#idSpan").html("订单编号为6-16位字符");
	}
	
	$.post(
		contextPath + "/system/order/checkId/"+id,
	 	function(data){
	 		if(!data){
	 			document.getElementById("idDd").className="text_c_ts";
				$("#idSpan").html("订单编号已存在");
	 			$("#idCheck").val("false");
	 			return false;
	 		}else{
	 			document.getElementById("idDd").className="text_d_ts";
	 			$("#idCheck").val("true");
	 		}
	 	}
	 );
}

function checkIdBlur (id) {
	flag = false;
	if(id == null || id == "" || id == " " || id.length<6 || id.length>16){
		document.getElementById("idDd").className="text_c_ts";
	}else{
		document.getElementById("idDd").className="";
		flag = true;
		return false;
	}
}

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

//验证店铺名格式
function checkStoreNameBlur(){
	var nameStr = document.getElementsByName('storeId')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("storeDd").className="text_c_ts";
		$("#nameSpan").html("店铺名为6-20位字符组成");
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("storeDd").className="";
		$("#nameCheck").val("true");
		return true;
	}
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
	var nameStr = document.getElementsByName('userId')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("用户名为1-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}


//-----------------------------------------页面样式空间------------------------------------------
$(document).ready(function() {
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