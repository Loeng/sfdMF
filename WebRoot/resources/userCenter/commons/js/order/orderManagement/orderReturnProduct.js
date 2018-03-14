// 退换货订单查询操作的提交
function formSubmit(){
	document.getElementById("orderReturnProduct").submit();
}

// 退换货搜索表单重置
function formReset(){
	$("#orderId").val("");
	$("#refundId").val("");
}

// 退换或订单查看详情
function refundDetail(ctxpath,refundId){
	window.location.href = ctxpath+"/store/order/returnProductDetail/"+refundId;
}

// 全选与全不选
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

// 批量同意退货、换货
function aggreeRefunds(ctxpath){
	var i = confirm("确定同意退货/换货?");
	if(!i) return;
	// 得到选中记录的refundId,并将其封装成一个JSON字符串
	var refundIds = getCheckedRefundId();
	// if(refundIds==null || refundIds=="") return;
	// 解析成JSON对象
	alert(refundIds);
	// var str = JSON.stringify(refundIds);
	// var $jsonRefundIds = $.parseJSON(str);
	/** eval 函数可能会存在问题,目前这里暂时这样处理 */
	var strObj = eval("("+refundIds+")");
	alert(strObj);
	$.post(
			ctxpath+"/store/order/aggreeRefunds",
			strObj,
			function(data){
				if(data){
				    alert("操作成功!");
				    window.location.href = ctxpath+"/store/order/returnProduct";
				}else{
					alert("操作失败!");
				}
			});
}

// 获取选中记录的refundId
function getCheckedRefundId(){
	var items = document.getElementsByTagName("input");
	if(items==null || items.length<=0){
		return;
	}
	var refundIdJSON = "{";
	var n = 0;
	for(var i = 0;i < items.length;i++){
		if(items[i].type=="checkbox" && items[i].name=="rcBox"){
			if(items[i].checked){
				n++;
				var refundId = $(items[i]).parent().next().html();
				refundIdJSON += '"refundId'+n+'":"'+refundId+'",';
			}
		}
	}
	refundIdJSON += '"size":"'+n+'",';
	refundIdJSON = refundIdJSON.substring(0, refundIdJSON.length-1);
	refundIdJSON += "}";
	return refundIdJSON;
}