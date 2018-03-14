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
	document.getElementById("searchForm").submit();	
}

//重置
function resetForm(){
	document.getElementsByName("productName")[0].value = "";
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

//点击查看详情
function query(id){
	window.location.href= $("#ctxpath").val() + "/system/bargain/detail/"+id;

}

function queryTrans(id,fbType){
	if(fbType=="" || fbType==" "){
		fbType = 2;
	}
	window.location.href= $("#ctxpath").val() + "/system/bargain/transDetail/"+id+"/"+fbType;
}



//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchProductForm").submit();
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
			dataType:"text";
			//alert(typeof data);
			//alert( data) 
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