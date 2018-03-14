$(document).ready(function() {
	
	if($("#modifyOk").val()=="true"){
		econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
	}else if(!$("#modifyOk").val()=="false"){
		ealert("修改失败！");
	}

	$("input.i_bg").focus(function (){ 
		//得到焦点时触发的事件
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		//失去焦点时触发的事件
		$(this).parent().removeClass("text_ts");
	}); 
			
	/*复选框*/
	$(".check").click(function(){
		$(this).toggleClass("checked")
	});
	
	// 点击是否为可选字段改变select的选中值
	$(".changeSel").live("click",function(){
		var check=$(this);
		if(check.is(":checked")){
			var sel=check.parents("tr").find("select");
			sel.val($("#selectValue").val());
		}
	})
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
	/*选项卡切换*/
	$(".contentItem").eq(0).show();
	var kuan=$(".tabItem li").length*120;
	$(".tabItem ul").css("width",kuan+"px");
	$(".tabItem li").each(function() {
		$(this).click(function(){
			$(this).siblings().removeClass("cur");
			$(this).addClass("cur");
			var dom=$(".contentItem");
			var index=$(this).parent().children().index(this);
			dom.hide();
			dom.eq(index).show();
		})
    });
	
	var count=kuan/600;
	$(".next").click(function(){
		var kuan=$(".tabItem li").length*120;
		var left=parseInt($(".tabItem ul").css("left"));
		if(kuan==600){
			return false;
		}
		if (left==0){
			$(".tabItem ul").animate({left:"-600px"});
		}
		if(Math.abs(left)<kuan){
			$(".tabItem ul").animate({left:left+(-600)+"px"},"fast");
		}
		else{
			$(".tabItem ul").animate({left:"0px"});
		}
	})
	$(".prev").click(function(){
		var kuan=$(".tabItem li").length*120;
		var left=parseInt($(".tabItem ul").css("left"));
		if (left==0){
			return false;
		}
		if(Math.abs(left)<kuan){
			$(".tabItem ul").animate({left:left+600+"px"},"fast");
		}
		else{
			return false;
		}
	})
	
	/*增加条目*/
	$(".btnAddLine").click(function(){
		var ctxpath = $("#ctxpath").val();
		var cId = $(this).next("div").next("input").val();
		$("#addFieldsDemo").find("#categoryId").val(cId);
		var obj = $("#addFieldsDemo").html();
		obj = obj.replace("<table>","");
		obj = obj.replace("<tbody>","");
		obj = obj.replace("</tbody>","");
		obj = obj.replace("</table>","");
		$(this).next("div").next("input").next(".boxTable").find("tbody").append(obj);
	})
	
	/*删除条目*/
	$(".btnRemoveLine").live("click",function(){
		var id = $(this).next("input").val();
		var contextPath = $("#ctxpath").val();
		if(id!=null && id != ''){
			$.post(
				contextPath + "/system/productTemplate/deleteField/"+id,
			 	function(data){
			 		if(data){
			 			$(this).parents("dl").remove();
			 		}else{
			 			ealert("删除失败！");
			 		}
			 	}
			);
		}
		$(this).parents("tr").remove();
	})
	
});

// 重置
function reset(){
	document.getElementById("modifyTemplateForm").reset();
}

//提交
function formSubmit() {
	if(checkNameBlur()){
		checkCheckbox();
		document.getElementById("modifyTemplateForm").submit();
	}else{
		checkNameBlur();
	}
}

//给checkbox赋值，若已选，赋值为true；未选赋值false并选中(便于后台取值)
function checkCheckbox(){
	var items = document.getElementsByTagName("input");
	// 遍历选择框，看是否已经全选
    for(var i=0;i<items.length;i++){  
        if(items[i].type=="checkbox"){
        	if(items[i].checked){
        		items[i].value = "true";
        	}else{
        		items[i].value = "false";
        		items[i].checked = true;
        	}
        }
    }
}

//验证模板名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("模板名为1-20位字符组成");
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("模板名为1-20位字符组成");
		$("#nameCheck").val("true");
		return true;
	}
}

//验证模板名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("模板名为1-20位字符组成");
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		$("#nameCheck").val("true");
		return true;
	}
}

// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/product/template";
	}
	window.location.href=contextPath + "/system/product/template";
}
