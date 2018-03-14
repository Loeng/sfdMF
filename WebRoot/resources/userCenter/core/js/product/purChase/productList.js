
/*验证电话号码*/
function linkTelBlur(){
	var tel = $("#linkTel").val().trim();
	if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(tel)) || tel==''){
		falert("联系方式不正确!");
		return false;
	}
	return true;
}
function linkelFoucs(){
	$("#linkTelSpan").remove();
}
/*验证购买数量*/
function numberBlur(){
	var num = $("#number").val().trim();
	var reg = new RegExp("^[0-9]*$");  
	if(!reg.test(num) || num ==''){
		falert('请正确填写数量');
		return false;
	}else{
		return true;
	}
}
function numberFocus(){
	$("#numberError").hide();
}
/*验证购买价格*/
function priceBlur(){
	var num = $("#price").val().trim();
	var reg = new RegExp("^[0-9]+\.[0-9]{1,2}|[0-9]+$");
	if(!reg.test(num) || num ==''){
		falert('请正确填写价格');
		return false;
	}else{
		return true;
	}
}
function priceFocus(){
	$("#priceError").hide();
}
function loadQuery(id,type){
	$("#apContent").load($("#ctxpath").val()+"/productList/Supple/load/"+id,function(){
	});
	$("#apLayerBg").show();
	$("#apLayer").show();
}
//清空前台议价
function clearSupply(){
	$("#number").val("");
	$("#price").val("");
	$("#linkPerson").val("");
	$("#linkTel").val("");
	$("textarea[name='note']").val("");
}

//前台大宗采购发起议价提交
function  productSupplySubmit(){
	var number = $("input[name='number']").val();
	var price = $("input[name='price']").val();
	var linkPerson = $("input[name='linkPerson']").val();
	var linkTel = $("input[name='linkTel']").val();
	var productid = $("input[name='productId']").val();
	var sellersId = $("input[name='sellersId']").val();
	var note = $("textarea[name='note']").val();
	var ctxpath = $('#ctxpath').val();
	if(!numberBlur()){
		return false;
	}else if(!priceBlur()){
		return false;
	}else if(!checkValue(linkPerson,'联系人')){
		return false;
	}else if(!linkTelBlur()){
		return false;
	}else{
	  $.post(ctxpath+"/store/storeInquiry/supplyAdd",
			{number:number,price:price,linkPerson:linkPerson,linkTel:linkTel,productId:productid,note:note,sellersId:sellersId},
			function(data){
		    if(data=='1'){
		    	salert('议价发布成功');
		    	$("#apContent").hide();
		    	$("#apClose").parents(".apLayer").hide();
				$(".apBg").hide();
		    }else{
		    	salert('议价发布失败!');
		    }
		    //清空
		    clearSupply();
	  });	
	}
}
function sub(){
	$("#apLayer,.apLayerBg").hide();
}

function checkValue (value,type){
	if(value == null || value == ''){
		salert(type+'不能为空!');
		return false;
	}else{
		return true;
	}
}