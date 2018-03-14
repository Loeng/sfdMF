$(document).ready(function() {
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
	
	$(".AddItem").click(function(){
		var ctxpath = $("#ctxpath").val();
		var obj="<dl class='formBox'>" +
				"<dt>子分类名</dt><dd>" +
				"<input type='hidden' class='i_bg' name='childId' >" +
				"<input type='text' class='i_bg' name='childName'><a href='javascript:void(0);' class='btnRemove'><img src='"+ctxpath+"/resources/system/images/del2.jpg' width='16' height='16'></a>" +
				"</dd>" +
				"</dl>";
		$(".boxContent").append(obj);
	})
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
	$(".btnRemove").live("click",function(){
		var id = $(this).prev().prev().val();
		var contextPath = $("#ctxpath").val();
		if(id!=null && id != ''){
			$.post(
				contextPath + "/system/templateCategory/deleteChild/"+id,
			 	function(data){
			 		if(data){
			 			$(this).parents("dl").remove();
			 		}else{
			 			ealert("删除失败！");
			 		}
			 	}
			);
		}
		$(this).parents("dl").remove();
	})
});	

// 重置
function reset(){
	document.getElementById("modifyCategoryForm").reset();
}

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

// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/templateCategory/list";
	}
	window.location.href=contextPath + "/system/templateCategory/list";
}
//提交
function formSubmit() {
	if(checkNameBlur()){
		document.getElementById("modifyCategoryForm").submit();
	}else{
		checkNameBlur();
	}
}