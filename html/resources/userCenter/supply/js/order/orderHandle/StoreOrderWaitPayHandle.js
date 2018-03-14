function checkPriceJudgFocus(judg){
	if(judg==1){
		$("#qpPriceError").css("display","none");
	}else if(judg==2){
		$("#qpFareError").css("display","none");
	}
}

function checkPriceJudgBlur(obj,judg){
	var tempval=$("#"+obj).val();
	var reg=/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/;
	if(reg.test(tempval)){
		return true;
	}else{
		if(judg==1){
			$("#qpPriceError").css("display","block");
		}else if(judg==2){
			$("#qpFareError").css("display","block");
		}
		return false;
	}
}

// 提交修改价格
function modifyPrice(ctxpath,orderId,inputId,judg){
	if(checkPriceJudgBlur(inputId,judg)){
		//获取修改前的金额
		var oldTotalPrice=$("#qpPrice").html().replace("￥","");
		//获取修改前的金额
		var oldFare=$("#qpFare").html().replace("￥","");
		if(judg==1){
			//获取修改后的金额
			var newTotalPrice=$("#"+inputId).val();
			if(newTotalPrice!=oldTotalPrice){
//				$.post(ctxpath+"/store/order/modifyOrderPrice/"+orderId,{ordersTotalPrice:newTotalPrice},function(data){
//					if(data){
						$("#qpPrice").html("￥"+new Number(newTotalPrice).toFixed(2));
						var sumPrice=parseFloat(newTotalPrice)+parseFloat(oldFare);
						$("#qpPriceYunTotal").html("￥"+new Number(sumPrice).toFixed(2));
						
						$("#"+inputId).val("");
						$("#qpModel").css("display","none");
						$("#newProductPriceTotal").val(newTotalPrice);
//					}
//				});
			}
		}else if(judg==2){
			//获取修改后的运费金额
			var newFare=$("#"+inputId).val();
			if(oldFare!=newFare){
//				$.post(ctxpath+"/store/order/modifyOrderPrice/"+orderId,{totalFreight:newFare},function(data){
//					if(data){
						$("#qpFare").html("￥"+new Number(newFare).toFixed(2));
						var sumPrice=parseFloat(oldTotalPrice)+parseFloat(newFare);
						$("#qpPriceYunTotal").html("￥"+new Number(sumPrice).toFixed(2));
						
						$("#"+inputId).val("");
						$("#qpModelFare").css("display","none");
						$("#newFaress").val(newFare);
//					}
//				});
			}
		}
	}
	/*
	var paid = $("#modifyPrice").val();
	// 提交的时候也涉及验证
	if(!checkPrice(paid) && $("#qh").val()=="show"){
		return;
	}
	*/
	/*
	var totalFreight=$("#totalFreight").val();
	var ordersTotalPrice=$("#ordersTotalPrice").val();
	$.post(ctxpath+"/store/order/modifyOrderPrice/"+orderId,{totalFreight:totalFreight, ordersTotalPrice:ordersTotalPrice},
		function(modifyOk){
			if(modifyOk){
				window.location.href = ctxpath+"/store/order/sure";
			}else{
				alert("修改价格失败!");
			}
	});
	*/
}

function updateSubmitPrice(contextPath,orderId){
	var newProductPriceTotal=$("#newProductPriceTotal").val();
	var newFaress=$("#newFaress").val();
	
	var judg=1;
	if(newProductPriceTotal.trim()!="" || newFaress.trim()!=""){
		judg=2;
	}
	
	if(judg!=1){
		econfirm("确定要修改订单价格吗?",updateSubmitPriceNews,[contextPath, orderId, newProductPriceTotal, newFaress],null,null);
	}
}

function updateSubmitPriceNews(contextPath,orderId,newProductPriceTotal,newFaress){
	
	$.post(contextPath+"/store/order/modifyOrderPrice/"+orderId,{ordersTotalPrice:newProductPriceTotal, totalFreight:newFaress},function(modifyOk){
		if(modifyOk){
			econfirm("修改成功是否返回列表?",goList,[contextPath],null,null);
		}else{
			falert("修改价格失败!");
		}
	});
}

function goList(contextPath){
	window.location.href=contextPath+"/store/gongXiaoOrder/list";
}
// 修改金额块的显示与隐藏切换效果
function qiehuan(){
	var qh = $("#qh").val();
	if(qh=="hide"){
		$(".editMoney").show();
		$("#hjcP").html("确认金额");
		$("#qh").val("show");
	}else if(qh=="show"){
		if(checkPrice($("#modifyPrice").val())){
			$(".editMoney").hide();
			$("#hjcP").html("修改金额");
			$("#qh").val("hide");
			$("#oldPrice").html($("#modifyPrice").val());
		}
	}
}

// 对修改金额的验证
function checkPrice(modifyPrice){
	var regx = /^[0-9]*$/;
	if(regx.test(modifyPrice) && modifyPrice.length>=1){
		$("#priceMsg").removeClass("errorText errorRed");
		$("#priceMs").html("").removeClass("errorMain");
		return true;
	}else{
		$("#priceMsg").addClass("errorText errorRed");
		$("#priceMs").html("请输入正确价格！").addClass("errorMain");
		return false;
	}
}

// 返回列表的方法
function goBack(contextPath){
	window.location.href=contextPath+"/store/gongXiaoOrder/list";
}
