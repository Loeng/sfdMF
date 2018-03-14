var flag = false;
$(document).ready(function() {
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
	
	$(".AddItem").click(function(){
		var ctxpath = $("#ctxpath").val();
		var obj="<dl class='formBox'>" +
				"<dt>子分类名</dt><dd>" +
				"<input type='text' class='i_bg' name='name'><a href='javascript:void(0);' class='btnRemove'><img src='"+ctxpath+"/resources/system/images/del2.jpg' width='16' height='16'></a>" +
				"</dd>" +
				"</dl>";
		$(".boxContent").append(obj);
	})
	
	$(".btnRemove").live("click",function(){
		$(this).parents("dl").remove();
	})
});	

//验证分类名格式
function checkName(){
	var nameStr = document.getElementsByName('categoryName')[0].value;
	if(nameStr.length < 2 || nameStr.length >20){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("分类名为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("分类名为2-20位字符组成");
		return true;
	}
}

//验证分类名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('categoryName')[0].value;
	if(nameStr.length < 2 || nameStr.length >20){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("分类名为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}


//验证所有信息是否正确
function checkData(){
	if(checkNameBlur()==false){
		return false;
	}else if(checkName()==false){
		return false;
	}else{
		return true;
	}
}

// 提交
function formSubmit(){
	 if(checkNameBlur()){
	    	document.getElementById("addCategoryForm").submit();
	    }else{
	    	checkNameBlur();
	    }
}

// 重置
function reset(){
    document.getElementById("addCategoryForm").reset();
}

//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/templateCategory/list";
	}
	window.location.href=contextPath + "/system/templateCategory/list";
}
