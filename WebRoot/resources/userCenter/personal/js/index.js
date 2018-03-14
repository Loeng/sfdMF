

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


