$(document).ready(function(){
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg")
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg")
	});
	$(".pro_ss dt span.span_down").click(function(){
		$(this).hide();
		$(this).next(".span_up").show();
		$(this).parent().nextAll("dd").hide();
		$(this).parent().parent().next(".pro_ss_btn").hide();
	});
	$(".pro_ss dt span.span_up").click(function(){
		$(this).hide();
		$(this).prev(".span_down").show();
		$(this).parent().nextAll("dd").show();
		$(this).parent().parent().next(".pro_ss_btn").show();
	});
});	

//搜索
function searchSubmit(){
	var rexE = /^([1-9][\d]{0,12}|0)(\.[\d]{1,2})?$/;
	var _min = $.trim($("input[name='minUnitPrice']").val());
	var _max = $.trim($("input[name='maxUnitPrice']").val());
	
	if(_min != "" && !rexE.test(_min)){
		ealert("融资金额填写有误");
		$("input[name='minUnitPrice']").val("");
		return;
	}
	if(_max != "" && !rexE.test(_max)){
		ealert("融资金额填写有误");
		$("input[name='maxUnitPrice']").val("");
		return;
	}
	if(parseFloat(_min) > parseFloat(_max)){
		ealert("价格区间填写有误");
		return;
	}
	
	document.getElementById("searchProductForm").submit();	
}

//重置
function resetForm(){
	document.getElementsByName("status")[0].checked = true;
	document.getElementsByName("type")[0].checked = true;
	document.getElementsByName("name")[0].value = "";
	document.getElementsByName("storeName")[0].value = "";
	document.getElementsByName("minUnitPrice")[0].value = "";
	document.getElementsByName("maxUnitPrice")[0].value = "";
	document.getElementsByName("productNumber")[0].value = "";
	$("select").each(function(i){
		if(i == 0){
			this.options[i].selected = true;
		}else{
			$(this).remove();
		}
	});
}

// 全选与全不选
function selectAll(){
	var items = document.getElementsByTagName("input");
	// 定义是否已经全选
	var seleced = true;
	// 遍历选择框，看是否已经全选
	for(var i=0;i<items.length;i++){  
		if(items[i].type=="checkbox"){
			if(!items[i].checked){
				seleced = false;
				break;
			}
		}
	}
	// 如果已经全选，则全取消，否则全选
	if(seleced){
		for(var i=0;i<items.length;i++){  
			if(items[i].type=="checkbox"){
				items[i].checked = false;
			}
		}
	}else{
		for(var i=0;i<items.length;i++){  
			if(items[i].type=="checkbox"){
				items[i].checked = true;
			}
		}
	}
}
//删除选择的记录
function deleteAll(contextPath){
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){
		if(items[i].type=="checkbox"&&items[i].checked){
			productDelete1(items[i].value,contextPath);
		}
	} 
	ealert("删除成功");
}
//用于批量删除商品时的删除方法(避免不断弹出删除成功的提示)
function productDelete1(id,contextPath){
	$.post(
		contextPath + "/contract/delContractById/"+id,
		function(data){
			if(data){
				var d="#del"+id;
				$(d).parent().parent().remove();
				var oTable = document.getElementById("productTab");
				for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
			}
		}
	);				
}
//删除商品
function productDelete(id,contextPath){
	$.post(
		contextPath + "/contract/delContractById/"+id,
		function(data){
			if(data){
				ealert("删除成功！");
				var d="#del"+id;
				$(d).parent().parent().remove();
				var oTable = document.getElementById("productTab");
				for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
			}else{
				ealert("删除失败！");
			}
		}
	);
}

//点击新增按钮，跳转到新增页面
function productAdd(contextPath){
	window.location.href= contextPath + "/system/product/productAddClassify";
}


// 返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/contractList";
	}
	window.location.href=contextPath + "/system/contractList";
}

//点击查看详情
function listProduct(id,contextPath){
	window.location.href= contextPath + "/system/productDetails/"+id;

}

//点击修改按钮，根据id进行商品资料查询
function contractModify(id,contextPath){
	window.location.href= contextPath + "/system/product/edit/"+id;
}


//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	$("#contractFormList").submit();
}


// 删除前验证
function checkSelect(){
	var selected = false;
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){
		if(items[i].type=="checkbox"&&items[i].checked){
			selected = true;
		}
	} 
	
	if(selected) {
		econfirm('是否确认删除？',deleteAll,[$("#ctxpath").val()],null,null);
	}else {
		ealert("请至少选择一条数据！");
	}
}


//根据id查询出详细信息
function loaddiv(id,contextPath){
	$('#loadDiv').load($("#ctxpath").val()+"/system/productDetails/"+id,function(){
		
	});
	$(".apPreview").show();
	$(".apPreviewBg").show();
}
function apClose(){
	$(".apPreview").hide();
	$(".apPreviewBg").hide();
}

$(document).ready(function(){
	// 点击一级
	$("#select1Span select").live("change",function(){
		//清除必填提示样式
		document.getElementById("cDd").className="";
		$("#categorySpan").html("");
		// 事先清除二級三級的內容
		$("#select2Span").html("");
		$("#select3Span").html("");
		var categoryId = $(this).children("option:selected").val();
		$("#categoryId").val(categoryId);
		var level = $(this).parent().find("input").val();
		// 如果该分类下面还有子分类,还需查询出子分类
		var ctxpath = $("#ctxpath").val();
		$.post(ctxpath + "/system/product/add/categoryChild", {"id":categoryId},function(data){
			if(data != null && data != ""){
				var JSONObj = eval("(" + data + ")");
				if(JSONObj != null && JSONObj.length > 0){
					// 新建一个select节点,并将该节点添加到原来原来父级select容器内
					var selectObj = "<select><option value=''>==请选择==</option>";
					// 将查询出来的结果放入该select对象内
					for(var i = 0;i<JSONObj.length;i++){
						var nowCategory = JSONObj[i];
						selectObj += "<option value ='"+nowCategory.id+"'>"+nowCategory.name+"</option>";
					}
					selectObj += "<input type='hidden' value='ERJIS'></select>";
					$("#select2Span").html(selectObj);
				}
			}
			});
	});
	
	// 点击二级	
	$("#select2Span select").live("change",function(){
		// 事先清除三級內容
		$("#select3Span").html("");
		var categoryId = $(this).children("option:selected").val();
		$("#categoryId").val(categoryId);
		var level = $(this).parent().find("input").val();
		// 如果该分类下面还有子分类,还需查询出子分类
		var ctxpath = $("#ctxpath").val();
		$.post(
			ctxpath + "/system/product/add/categoryChild",
			{"id":categoryId},
			function(data){
			if(data != null && data != ""){
				var JSONObj = eval("(" + data + ")");
				if(JSONObj != null && JSONObj.length > 0){
					// 新建一个select节点,并将该节点添加到原来原来父级select容器内
					var selectObj = "<select><option value=''>==请选择==</option>";
					// 将查询出来的结果放入该select对象内
					for(var i = 0;i<JSONObj.length;i++){
						var nowCategory = JSONObj[i];
						selectObj += "<option value ='"+nowCategory.id+"'>"+nowCategory.name+"</option>";
					}
					selectObj += "<input type='hidden' value='SANJIS'></select>";
					$("#select3Span").html(selectObj);
				}
			}
			});
	});	
	
	// 點擊三級
	$("#select3Span select").live("change",function(){
		// 點擊三級進行賦值更新即可
		var categoryId = $(this).children("option:selected").val();
		$("#categoryId").val(categoryId);
	});
	
 })