$(function(){
		$(".ht_list tr").mouseover(function(){
			$(this).addClass("tr_bg")
		});
		$(".ht_list tr").mouseout(function(){
			$(this).removeClass("tr_bg")
		});
		
		/*查询条件展开和收起*/
		$(".pro_ss dt span.span_up").click(function(){
			$(this).hide();
			$(this).prev(".span_down").show();
			$(this).parent().nextAll("dd").hide();
			$(this).parent().parent().next(".pro_ss_btn").hide();
		});
		$(".pro_ss dt span.span_down").click(function(){
			$(this).hide();
			$(this).next(".span_up").show();
			$(this).parent().nextAll("dd").show();
			$(this).parent().parent().next(".pro_ss_btn").show();
		});
	
	
});	

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
			categoryDelete1(items[i].value,contextPath);
		}
	} 
	ealert("删除成功");
}
//用于批量删除频道时的删除方法(避免不断弹出删除成功的提示)
function categoryDelete1(id,contextPath){
	$.post(
			contextPath + "/system/templateCategory/delete/"+id,
			function(data){
				if(data){
					var d="#del"+id;
					$(d).parent().parent().remove();
					var oTable = document.getElementById("categoryTab");
					for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
					}
				}
			}
	);
	 
}
//删除频道
function categoryDelete(id,contextPath){
	$.post(
		contextPath + "/system/templateCategory/delete/"+id,
	 	function(data){
	 		if(data){
	 			ealert("删除成功！");
	 			var d="#del"+id;
	 			$(d).parent().parent().remove();
	 			var oTable = document.getElementById("categoryTab");
				for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
	 		}else{
	 			ealert("删除失败！");
	 		}
	 	}
	);
}

function eyeDetail(id,contextPath){
	$(".apPreview").show();
	$(".apPreviewBg").show();
	
	$("#previewContent").load(contextPath + "/system/templateCategory/information/"+id);
}

function eyeClose(){
	$(".apPreview").hide();
	$(".apPreviewBg").hide();
}

//点击新增按钮，跳转到新增页面
function categoryAdd(contextPath){
	window.location.href = contextPath + "/system/templateCategory/add";
}
//点击修改按钮，根据id进行频道资料查询
function categoryModify(id,contextPath){
	window.location.href = contextPath + "/system/templateCategory/detail/"+id;
}
//searchCategoryForm的提交
function searchSubmit(){
	document.getElementById("searchCategoryForm").submit();
}

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchCategoryForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("name")[0].value="";
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

