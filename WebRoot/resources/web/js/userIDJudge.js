//判断用户购买商品时是否登录或者是否为采购商类型


function validUser(proId,type,channel,storeId){


	var userId=$("#userId").val();
    if(userId == storeId){
        layer.alert("不能购买本公司发布的产品!");
        return;
    }

	$.post($("#ctxpath").val()+"/web/gouWu/userShopFiler",function (data) {
		if(data==0){
			location.href=$("#ctxpath").val()+"/web/login";
		}
		if(data==2){
			layer.alert("只有采购商才能进行此操作！");
		}
		if(data==1){
			if(channel=="detail"){
				var count = $("#buyCount").val();
				if(type=="0"){//下单
					location.href=$("#ctxpath").val()+"/web/gouWu/order/"+proId+"?quantity="+count;
				}else{//订购
					location.href=$("#ctxpath").val()+"/web/dingGou/order/"+proId+"?quantity="+count;
				}
			}else{
				if(type=="0"){
					location.href=$("#ctxpath").val()+"/web/gouWu/order/"+proId+"?quantity="+count;
					//window.open($("#ctxpath").val()+"/web/gouWu/order/"+proId);
				}else{
					location.href=$("#ctxpath").val()+"/web/dingGou/order/"+proId+"?quantity="+count;
					//window.open($("#ctxpath").val()+"/web/dingGou/order/"+proId);
				}
			}
		}
	});
}