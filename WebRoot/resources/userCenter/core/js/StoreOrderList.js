// 搜索表单提交
function formSubmit(){
	$("#searchStoreOrder").submit();
}

// 搜索表单重置
function formReset(){
	$("#orderId").val("");
	$("#userName").val("");
	$("#beginDate").val("");
	$("#endDate").val("");
}

// 订单列表的JS操作
function xxx(ctxpath,orderId,status,shippingStatus,userApp){
	// 发送请求
	document.write("<form action='"+ctxpath+"/store/order/orderListHandel' method='post' name='formx1' style='display:none'>");
	document.write("<input type='hidden' name='orderId' value='"+orderId+"' />");
	document.write("<input type='hidden' name='status' value='"+status+"' />");
	document.write("<input type='hidden' name='shippingStatus' value='"+shippingStatus+"' />");
	document.write("<input type='hidden' name='userApp' value='"+userApp+"' />");
	document.write("</form>");
	document.formx1.submit();
}

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchStoreRefundOrder").submit();
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