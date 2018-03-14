$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});

$(function(){
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		ealert("出库成功！")
	}else if($("#ok").val()=="false"){
		ealert("出库失败！");
	}
	
	$("input.i_bg").focus(function ()//得到焦点时触发的事件
	{ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function () //失去焦点时触发的事件
		{ 
		$(this).parent().removeClass("text_ts");
	}); 
});	

//load出出库信息
function loadWareOrder(){
	 var wareOrderId = $('#wareOrderId').val();
	 
	 if($.trim(wareOrderId) == ""){ 
		 $("#wareOrderDd").parent().addClass("text_c_ts");
		 return false;
	 }else{
		 $("#wareOrderDd").parent().removeClass("text_c_ts");
		 $("#wareHouseOrderLoad").load($("#ctxpath").val()+"/system/warehouseOrder/search",{id:wareOrderId});
		 return true;
	 }
	
}

//提交
function formSubmit(){
	var productId = $('#productId').val();
	if($.trim(productId) == ""){
		ealert("请填写提货单！");
	}else if(loadWareOrder()){
    	document.getElementById("addGoWarehouseForm").submit();
    }else{
    	loadWareOrder();
    }
}


