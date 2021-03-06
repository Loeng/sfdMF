// 搜索表单提交
function formSubmit(){
	$("#searchStoreEvaluateOrder").submit();
}

// 搜索表单重置
function formReset(){
	$("#orderId").val("");
	$("#userName").val("");
	$("#beginDate").val("");
	$("#endDate").val("");
}

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchStoreEvaluateOrder").submit();
}

// 跳转到订单评价页面
function evaluateOrder(ctxpath,orderId){
	window.location.href = ctxpath+"/store/order/orderEvaluateHandel/"+orderId;
}

//全选与全不选
function selectAll(){
	// 按钮上的全选与全部不选
	var sbtn = document.getElementById("sbut").checked;
	// 获得所有的input对象数组
	var items = document.getElementsByTagName("input");
	// 如果已经全选，则全取消，否则全选 (这里操作的是name为rcBox的复选框)
	if(sbtn){
		for(var i=0;i<items.length;i++){  
			if(items[i].type=="checkbox"){
				items[i].checked = true;
			}
		}
	}else{
		for(var i=0;i<items.length;i++){  
			if(items[i].type=="checkbox"){
				items[i].checked = false;
			}
		}
	}
}