
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
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
});	

//验证频道名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >20){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("频道名为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("频道名为2-20位字符组成");
		return true;
	}
}

//验证频道名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >20){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("频道名为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}
//验证url是否为空
function checkViewPathBlur(){
	var view = $.trim($("input[name='viewPath']").val());
	var parentId = $.trim($("input[name='parentId']").val())
	 if(view == "" && parentId != ""){
		 document.getElementById("viewPathDd").className="text_c_ts";
		 $("#viewPath").html("连接URL不能为空");
		 return false;
	 }else{
		 document.getElementById("viewPathDd").className="";
		 return true;
	 }
}
//验证url是否为空
function checkViewPath(){
	var view = $.trim($("input[name='viewPath']").val());
	 if(view==""){
		 document.getElementById("viewPathDd").className="text_ts";
		 $("#viewPathSpan").html("模板路径不能为空");
		 return false;
	 }else{
		 document.getElementById("viewPathDd").className="";
		 return true;
	 }
}

//验证url是否为空
function checkLinkUrl(){
	var view = $.trim($("input[name='linkUrl']").val());
	 if(view==""){
		 document.getElementById("linkUrlDd").className="text_ts";
		 $("#linkUrlSpan").html("连接路径不能为空");
		 return false;
	 }else{
		 document.getElementById("linkUrlDd").className="";
		 return true;
	 }
}


//验证频道ID是否存在
function checkId(){
	var contextPath = $("#ctxpath").val();
	var idStr = $("#idStr").val();
	if(idStr == ""){
		document.getElementById("idDd").className="text_c_ts";
		$("#idSpan").html("频道id不能为空");
		return false;
	}else{
	$.post(
		contextPath + "/system/channel/checkId/"+idStr,
	 	function(data){
	 		if(!data){
	 			ealert("频道ID已存在!");
	 			return false;
	 		}else{
	 			document.getElementById("idDd").className="";
	 			return true;
	 		}
	 	});
	}
}


//验证所有信息是否正确
function checkData(){
	if(checkId()==false){
		return false;
	}else if(checkNameBlur()==false){
		return false;
	}else if(checkName()==false){
		return false;
	}else{
		return true;
	}
}




// 提交
function formSubmit(){
	if(!checkData()){
		return;
	}
	document.getElementById("addChannelForm").submit();
}

// 重置
function reset(){
    document.getElementById("addChannelForm").reset();
}

//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/channel/list";
	}
	window.location.href=contextPath + "/system/channel/list";
}

function changeType(valueStr){
	if(valueStr == "0"){
		$("#viewPathDl").show();
		$("#linkDl").hide();
		$("#desDl").hide();
	}else if(valueStr == "1"){
		$("#viewPathDl").hide();
		$("#linkDl").show();
		$("#desDl").hide();
	}else if(valueStr == "2"){
		$("#viewPathDl").hide();
		$("#linkDl").hide();
		$("#desDl").show();
	}
}