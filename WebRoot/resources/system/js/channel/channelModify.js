$(function(){
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
});	

// 重置
function reset(){
	document.getElementById("modifyChannelForm").reset();
}

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

//验证频道ID是否存在
function checkId(id,contextPath){
	$.post(
		contextPath + "/system/channel/checkId/"+id,
	 	function(data){
	 		if(!data){
	 			ealert("频道ID已存在!");
	 			return false;
	 		}
	 	}
	 );
}

// 验证上传图片格式
function validateForm(modify) {
	// 可不上传图标，直接返回true
	if (modify.bigPicture.value == ""){
		return true;
	}     
	//截取提交上传文件的扩展名  
	var ext = modify.bigPicture.value.match(/^(.*)(\.)(.{1,8})$/)[3];
	ext = ext.toLowerCase(); //设置允许上传文件的扩展名           
	if (ext ==  "jpg" || ext == "gif" || ext=="png") {
		return true;
	} else {
		alert("只允许上传 .jpg或gif 或png文件，请重新选择需要上传的文件！");
		return false;
	}
}
// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/channel/list";
	}
	window.location.href=contextPath + "/system/channel/list";
}
//提交
function formSubmit() {
	if(checkNameBlur()){
		document.getElementById("modifyChannelForm").submit();
	}else{
		checkNameBlur();
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
		 $("#viewPath").html("连接url不能为空");
		 return false;
	 }else{
		 document.getElementById("viewPathDd").className="";
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