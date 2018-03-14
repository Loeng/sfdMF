
//关闭弹出层
function closeLoad(){
	$("#apLayerBg").hide();
	$("#apLayer").hide();
}

//搜索议价表单提交
function forSubmitInquiry(id){
		var sellersNumber = $("input[name='sellersNumber']").val();
		var FinalPrice = $("input[name='FinalPrice']").val();
		if(!(checkValue(number) && checkValue(price)){
			alert("请填写必填项");
			return false;
		}else{
			
			$.post("'"+ctxpath+"/store/supplyPrice/Add",{id:id,sellersNumber:sellersNumber,FinalPrice:FinalPrice},function(result){
			    if(result == '1'){
			    	alert('核定议价成功!');
			    }else{
			    	alert('核定议价失败!');
			    }
			  });
		
		}
    }
function checkValue (value){
	if(value == null || value == ""){
		return false;
	}else{
		return true;
	}
}
