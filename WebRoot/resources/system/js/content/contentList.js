$(function(){
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg");
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg");
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
//自动生成序号
window.onload = function(){
	var oTable = document.getElementById("contentTab");
	for(var i=1;i<oTable.rows.length;i++){
		oTable.rows[i].cells[1].innerHTML = (i);
	}
};
//searchContentForm的提交
function searchSubmit(){
	document.getElementById("searchContentForm").submit();	
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
		if(items[i].type=="checkbox" && items[i].checked){
			contentDelete1(items[i].value,contextPath);
    	}
	} 
	ealert("删除成功");
}
//用于批量删除资讯时的删除方法(避免不断弹出删除成功的提示)
function contentDelete1(id,contextPath){
	 $.post(
		contextPath + "/system/content/delete/"+id,
	 	function(data){
	 		if(data){
	 			var d="#del"+id;
	 			$(d).parent().parent().remove();
	 			var oTable = document.getElementById("contentTab");
	 			for(var i=1;i<oTable.rows.length;i++){
	 				oTable.rows[i].cells[1].innerHTML = (i);
	 			}
	 		}
	 	}
	 );
	 
}
//删除资讯
function contentDelete(contextPath,id){
	$.post(
		contextPath + "/system/content/delete/"+id,
	 	function(data){
	 		if(data){
				ealert("删除成功！");
				var d="#del"+id;
				$(d).parent().parent().remove();
				var oTable = document.getElementById("contentTab");
				for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
	 		}else{
	 			ealert("删除失败！");
	 			}
	 		}
		);
}

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchContentForm").submit();
}

// 点击新增按钮，跳转到新增资讯页面
function contentAdd(contextPath){
	window.location.href=contextPath + "/system/content/add";
}
// 点击修改按钮，根据id进行资讯资料查询
function contentModify(id,contextPath){
	window.location.href=contextPath + "/system/content/detail/"+id;
}
	       
// searchContentForm的提交
function searchSubmit(){
	document.getElementById("searchContentForm").submit();
}
// 重置
function resetForm(){
	document.getElementsByName("name")[0].value="";
	document.getElementsByName("bigenDate")[0].value="";
	document.getElementsByName("endDate")[0].value="";
	document.getElementsByName("checkStatus")[0].checked=true;
	$("select").each(function(i){
		if(i == 0){
			this.options[i].selected = true;
		}else{
			$(this).remove();
		}
	});
}
//删除前验证
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
		$.post(ctxpath + "/system/content/add/categoryChild", {"id":categoryId},function(data){
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
			ctxpath + "/system/content/add/categoryChild",
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