$(document).ready(function() {
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
	// 关联分类
	$(".pro_pp .pp").click(function(){
		$("#categoryDiv").load($("#ctxpath").val()+"/system/productTemplate/listCategory",function() {
			$(".del_tc").show();
			$(".del_tcBg").show();
			$(".tit .apClose").click(function(){
				$(".del_tc").hide();
				$(".del_tcBg").hide();
			});
		});
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
	
});

//提交
function formSubmit() {
	if(checkNameBlur()){
		checkCheckbox();
		document.getElementById("addTemplateForm").submit();
	}else{
		checkNameBlur();
	}
}

// 重置
function reset(){
	document.getElementById("addTemplateForm").reset();
}

// 模板分类的翻页
function goPage(pageNum){
	var _name = $("#categoryName").val();
	$("#categoryDiv").load($("#ctxpath").val()+"/system/productTemplate/listCategory",
			{name:_name,page:pageNum},function() {
		$(".del_tc").show();
		$(".del_tcBg").show();
	});
}

// 选择分类
function getCategory(id,name){
	// 关闭弹窗
	$(".del_tc").hide();
	$(".del_tcBg").hide();
	// 获取分类名
	$("#cName").val(name);
	// 加载子分类
	$(".Tab").load($("#ctxpath").val()+"/system/productTemplate/listChildCategory",
			{pId:id},function() {
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
					$(this).parents("tr").remove();
				})
		
	});
}

// 给checkbox赋值，若已选，赋值为true；未选赋值false并选中(便于后台取值)
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
// 返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/product/template";
	}
	window.location.href=contextPath + "/system/product/template";
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