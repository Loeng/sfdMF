$(document).ready(function() {
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg");
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg");
	});
	
	// 显示
	$(".pro_ss dt span.span_down").click(function(){
		$(this).hide();
		$(this).next(".span_up").show();
		$(this).parent().nextAll("dd").hide();
		$(this).parent().parent().next(".pro_ss_btn").hide();
	});
	// 隐藏
	$(".pro_ss dt span.span_up").click(function(){
		$(this).hide();
		$(this).prev(".span_down").show();
		$(this).parent().nextAll("dd").show();
		$(this).parent().parent().next(".pro_ss_btn").show();
	});
	
	//关闭弹窗
	$(".apPreview .tit .apClose").click(function(){
		$(".apPreviewBg").hide();
		$(".apPreview").hide();
	});
});	

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchOrderForm").submit();
}

//跳转到商品列表页面
function add(){
	window.location.href = "/system/order/toAdd";
}

//搜索
function searchSubmit(){
	var regexp = /^\d+$/;
	var beginPrice = $.trim($("input[name='beginPrice']").val());
	var endPrice = $.trim($("input[name='endPrice']").val());
	
	if(beginPrice != "" && !regexp.test(beginPrice)){
		ealert("订单金额输入错误！");
		return;
	}
	if(endPrice != "" && !regexp.test(endPrice)){
		ealert("订单金额输入错误！");
		return;
	}
	if(parseFloat(beginPrice) > parseFloat(endPrice)){
		ealert("订单金额区间输入错误！");
		return;
	}
	document.getElementById("searchOrderForm").submit();
}


// 重置
function resetForm(){
	document.getElementsByName("shippingStatus")[0].checked=true;
	document.getElementsByName("orderNum")[0].value="";
	document.getElementsByName("beginDate")[0].value="";
	document.getElementsByName("endDate")[0].value="";
	document.getElementsByName("beginPrice")[0].value="";
	document.getElementsByName("endPrice")[0].value="";
}



//跳转到修改页面
function orderModify(orderId,contextpath){
	window.location.href = contextpath+"/system/order/toUpdate/"+orderId;
}
//删除选择的记录
function deleteAll(contextPath){
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){  
		if(items[i].type=="checkbox"&&items[i].checked){
			orderDelete1(items[i].value,contextPath);
		}
	} 
	ealert("删除成功");
}
//用于批量删除订单时的删除方法(避免不断弹出删除成功的提示)
function orderDelete1(id,contextPath){
	$.post(
			contextPath + "/system/order/delete/"+id,
			function(data){
				if(data){
					var d="#del"+id;
					$(d).parent().parent().remove();
					var oTable = document.getElementById("orderTab");
					for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
					}
				}
			}
	);
	 
}
//删除订单
function orderDelete(id,contextPath){
	var i = confirm("你确定要删除此订单?");
	if(i != true){
		return;
	}
	$.post(
		contextPath + "/system/order/delete/"+id,
	 	function(data){
	 		if(data){
	 			ealert("删除成功！");
	 			var d="#del"+id;
	 			$(d).parent().parent().remove();
	 			var oTable = document.getElementById("orderTab");
				for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
	 		}else{
	 			ealert("删除失败！");
	 		}
	 	}
	);
}

//全选与全不选
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

//------------------------------订单详情信息-----------------------------
//显示订单详情
function showOrderDetail(orderId){
	$(".apPreviewBg").show();
	$(".apPreview").show();
	$("#orderDetail").load($("#ctxpath").val()+"/system/order/detail/"+orderId);
		
}