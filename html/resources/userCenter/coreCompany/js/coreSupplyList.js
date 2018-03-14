
function searchSubmit(){
	$("#searchSupplyProductList").submit();
}

function reset(){
	document.getElementsByName("supplyProductName")[0].value = "";
	document.getElementsByName("status")[0].value = "";
}

function goPage(num){
	$("input[name='pageNum']").val(num);
	$("#searchSupplyProductList").submit();
}

function loadQuery(id,type,userId){
	$(".apContent").load($("#ctxpath").val()+"/supply/loadSupplyGysHf/"+id+"/"+type+"/"+userId,function(){

	});
	$(".apLayerBg").show();
	$(".apLayer").show();
}

//关闭弹出层
function closeLoad(){
	$("#apLayerBg").hide();
	$("#apLayer").hide();
}
function check(){
	var reg =/^(0|[1-9][0-9]*)$/;
	var re =/^[0-9]+(.[0-9]{2})?$/;
	if($("#sellersNumber").val()==''){
		falert("核定数量不能为空");
		return false;
	}else if($("#FinalPrice").val()==''){
		falert("核定价格不能为空");
		return false;
	}else if($("#updateTime").val()==''){
		falert("议价下单截止时间不能为空");
		return false;
	}else{
		return true;
	}
}
//搜索议价表单提交
function forSubmitInquiry(id){
		var sellersNumber = $("input[name='sellersNumber']").val();
		var FinalPrice = $("input[name='FinalPrice']").val();
		var endTime = $("input[name='updateTime']").val();
		if(check()){
			$.post($("#ctxpath").val()+"/store/supplyPrice/Add",{id:id,sellersNumber:sellersNumber,FinalPrice:FinalPrice,updateTime:endTime},function(result){
				if(result == '1'){
			    	econfirm('议价成功，返回议价列表?',goBack,[$("#ctxpath").val()],null,null);
			    	closeLoad();
			    }else{
			    	econfirm('核定议价失败,是否继续议价?',null,null,goBack,[$("#ctxpath").val()]);
			    }
			  });
		}
  }
function goBack(ctxPath){
	window.location.href=ctxPath+"/store/supply/coreSupplyList";
}
function checkValue (value){
	if(value == null || value == ""){
		return false;
	}else{
		return true;
	}
}