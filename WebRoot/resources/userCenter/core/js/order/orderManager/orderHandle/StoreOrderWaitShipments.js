// 返回列表的方法
function goBack(){
	window.history.go(-1);
}




//订单发货验证
function checkTextValue(tempId,judgment){
	var tem = "#"+tempId;
	if(judgment==1){
		$(tem).css("display", "none");
	}else if(judgment==2){
		var temVal = "#"+tempId.replace("Error","");
		var values = $(temVal).val().trim();
		if(values.length<=0){
			$(tem).css("display", "block");
			return false;
		}
		return true;
	}else if(judgment==22){
		var temVal = "#"+tempId.replace("Error","");
		var values = $(temVal).val().trim();
		var reg = /^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
		if(!reg.test(values)){
			$(tem).css("display", "block");
			return false;
		}
		return true;
	}
}

// 订单发货
function delivery(ctxpath,orderId){
	var temId="logisticsNameError,logisticsNoError,actualShippingError".split(",");
	var judgment=0;
	for(var i=0;i<temId.length;i++){
		if(i<=1){
			if(checkTextValue(temId[i],2)){
				judgment=judgment+1;
			}else{
				judgment=judgment+2;
			}
		}else{
			if(checkTextValue(temId[i],22)){
				judgment=judgment+1;
			}else{
				judgment=judgment+2;
			}
		}
	}
	
	if(judgment==3){
		// 获取物流公司名字
		var logisticsName=$("#logisticsName").val().trim();
		// 获取物流单号
		var logisticsNo=$("#logisticsNo").val().trim();
		// 获取运费
		var actualShipping=$("#actualShipping").val().trim();
		
		econfirm("确定要发货?",newdeliveryUt,[ctxpath, orderId, logisticsName, logisticsNo, actualShipping],null,null);
	}
	
	/*
	
	// 获取物流公司名字
	var logisticsName = $("#logisticsName").val().trim();
	// 获取物流单号
	var logisticsNo = $("#logisticsNo").val().trim();
	
	if(logisticsName.length<=0){
		falert("请输入物流公司名字！");
		return;
	}else if(logisticsNo.length<=0){
		falert("请输入物流单号！");
		return;
	}else{
		econfirm("确定要发货?",s,[ctxpath, orderId, logisticsName, logisticsNo],null,null);
	}
	
	*/
}


function newdeliveryUt(ctxpath,orderId,logisticsName,logisticsNo,actualShipping){
	$.post(ctxpath+"/store/order/deliveryStoreOrder",{orderId:orderId, logisticsName:logisticsName, logisticsNo:logisticsNo, actualShipping:actualShipping},function(deliveryOk){
		if(deliveryOk){
			window.location.href = ctxpath+"/store/order/list";
		}else{
			falert("发货失败，稍后重试。");
		}
	});
}

function confirmSh(id){
	econfirm("确定要收货?",function(){
		$.post($("#ctxpath").val()+"/store/order/confirmSh",{"id":id},function(data){
			if(data=="1"){
				salertWithFunction("操作成功！",function(){
					if($("#ps").val()==2){
						choseMenu($("#ctxpath").val(),'ORDER_PURCHASE_LSSC');
					}
					if($("#ps").val()==0){
						choseMenu($("#ctxpath").val(),'ORDER_PURCHASE_CP');
					}
				},null);
			}else{
				falert("确认收货失败，稍后重试。");
			}
		});
	},null,null,null);
}

function choseMenu(webroot,choseId){
	location.href=webroot + "/store/manager/" + choseId;	
}