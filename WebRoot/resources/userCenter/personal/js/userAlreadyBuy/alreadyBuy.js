// JavaScript Document

$(document).ready(function() {
    $(".orderTab").click(function(){
		$(".orderTab").removeClass("cur");
		$(this).addClass("cur");
	})
});
// load页面
function loadAlready(status,shippingStatus,userApp){
	if(status=="" && shippingStatus=="" && userApp==""){
		$("#loadDiv").load($("#ctxpath").val()+"/loaduser/alreadyBuyforload",{"status":status,"shippingStatus":shippingStatus,"userApp":userApp},function(){
			$(".btnOrange").each(function(){
				var tempClick=$(this).attr("onclick");
				if(tempClick!=null && tempClick.indexOf("postok('")!=-1){
					tempClick=tempClick.replace("postok('","");
					var temps=tempClick.substring(0,tempClick.indexOf("','"+$("#ctxpath").val()+"',2);"));
					$(this).attr("onclick","postok('"+temps+"','"+$("#ctxpath").val()+"',1)");
				}
			});
		});
	}else{
		//alert();
		$("#loadDiv").load($("#ctxpath").val()+"/loaduser/alreadyBuyforload",{"status":status,"shippingStatus":shippingStatus,"userApp":userApp});
	}
}

//确认收货
function postok(id,contextPath,judg){
	$.post(contextPath + "/user/alreadyBuy/confirm/"+id,function(data){
		if(data){
			//salert("收货成功");
	 		if(judg==1){
	 			window.location.href=contextPath+"/user/alreadyBuy";
	 		}else if(judg==2){
	 			var status=3;
	 			var shippingStatus=true;
	 			var userApp="";
	 			$("#loadDiv").load(contextPath+"/loaduser/alreadyBuyforload",{"status":status,"shippingStatus":shippingStatus,"userApp":userApp});
	 		}
	 	}else{
	 		falert("收货失败");
	 	}
	});
}
//取消订单
function cancel(id,contextPath){
	$.post(
		contextPath + "/user/alreadyBuy/cancel/"+id,	
		function(data){
			if(data){
				salertWithFunction("取消成功",reload,null);
			}else{
				falert("取消失败");
			}
		}
	);
}


function reload(){
	window.location.reload();
}
// 分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	
	var status=$("#tempstatus").val();
	var shippingStatus=$("#tempshippingStatus").val();
	var userApp=$("#tempuserApp").val();
	if((null==status || status=="") && (null==shippingStatus || shippingStatus=="") && (null==userApp || userApp=="")){
		tempGoPage(1);
	}else if(status==2 && (null==shippingStatus || shippingStatus=="") && (null==userApp || userApp=="")){
		tempGoPage(2);
	}else if(status==3 && shippingStatus=="true" && (null==userApp || userApp=="")){
		tempGoPage(3);
	}else{
		tempGoPage(4);
	}
	
	$("#loadDiv").load($("#ctxpath").val()+"/loaduser/alreadyBuyforload",
			{"status":status,"shippingStatus":shippingStatus,"userApp":userApp,pageNum:pageNum}
	);
	//document.getElementById("searchStoreOrder").submit();
}

function tempGoPage(num){
	for(var i=1;i<=4;i++){
		var tempId = "#tempAllCheck"+i;
		if(i==num){
			$(tempId).attr("class","orderTab cur");
		}else{
			$(tempId).attr("class","orderTab");
		}
	}
}

// 立即付款
function userBuyNow(ctxpath,orderId){
	document.write("<form action='"+ctxpath+"/buyNowUser/order/userByNow' id='buyNowFrom' method='post' style='display:none;'>");
	document.write("<input type='hidden' name='orderId' value='"+orderId+"'>");
	document.write("</form>");
	$("#buyNowFrom").submit();
}


