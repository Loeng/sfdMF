
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
	var oTable = document.getElementById("userLevelTab");
	for(var i=1;i<oTable.rows.length;i++){
		oTable.rows[i].cells[1].innerHTML = (i);
		
	}
};
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
	    	userLevelDelete1(items[i].value,contextPath);
	    }
	} 
	ealert("删除成功");
}
//用于批量删除会员等级时的删除方法(避免不断弹出删除成功的提示)
function userLevelDelete1(id,contextPath){
	$.post(
		contextPath + "/system/user/level/delete/"+id,
	 	function(data){
	 		if(data){
	 		var d="#del"+id;
		 		$(d).parent().parent().remove();
		 		var oTable = document.getElementById("userLevelTab");
				for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
	 		}
	 	}
	);
}
//删除会员等级
function userLevelDelete(id,contextPath){
	$.post(
		contextPath + "/system/user/level/delete/"+id,
	 	function(data){
	 		if(data){
	 			ealert("删除成功！");
	 			var d="#del"+id;
	 			$(d).parent().parent().remove();
	 			var oTable = document.getElementById("userLevelTab");
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
	document.getElementById("searchUserLevelForm").submit();
}
//点击新增按钮，跳转到新增会员等级页面
function userLevelAdd(contextPath){
	window.location.href = contextPath + "/system/user/level/add";
}
//点击修改按钮，根据id进行会员等级资料查询
function userLevelModify(id,contextPath){
	window.location.href = contextPath + "/system/user/level/detail/"+id;
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

//根据id查询出详细信息
function queryId(id,contextPath){
	$('#div1').load($("#ctxpath").val()+"/system/user/level/query/"+id,function(){
		
	});
	$(".apPreview").show();
	$(".apPreviewBg").show();
}
function apClose(){
	$(".apPreview").hide();
	$(".apPreviewBg").hide();
}