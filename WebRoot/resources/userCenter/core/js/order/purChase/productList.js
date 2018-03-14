// JavaScript Document
$(document).ready(function() {
	/*议价弹窗*/
	$(".eyeIco").click(function(){
		$(".apBargain").show();
		$(".apLayerBg").show();
	})
	var count=$(".apBargain .stair li").length;
	for(i=4;i<count;i++){
		$(".apBargain .stair li").eq(i).hide();
	}
	$(".readAll").toggle(function(){
		$(".apBargain .stair li").show(200);
		$(this).addClass("allDown");
	},function(){
		for(i=4;i<count;i++){
			$(".apBargain .stair li").eq(i).hide(200);
		}
		$(this).removeClass("allDown");
	})
	
	
});
/*验证电话号码*/
function linkTelBlur(){
	var tel = $("#linkTel").val();
	if(!(/^1[3|4|5|8][0-9]\d{4,8}$/.test(tel))){
		$("#linkTel").after("<span id=\"linkTelSpan\" class=\"errorText\">手机号码不正确,请重新填写!</span>");
	}
}
function linkelFoucs(){
	$("#linkTelSpan").remove();
}
/*验证购买数量*/
function numberBlur(){
	var num = $("#number").val();
	var reg = new RegExp("^[0-9]*$");  
	if(!reg.test(num)){
		
		$("#numberError").css("display","inline-block");
		$("#numberError").css("padding-right","5px");
		$("#numberError").show();
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
	var num = $("#price").val();
	var reg = new RegExp("^[0-9]*$");  
	if(!reg.test(num)){
		$("#priceError").css("padding-right","5px");
		$("#priceError").css("display","inline-block");
		$("#priceError").show();
		$("#price").val("");
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
//搜索
function  formSubmit(){
	$("#searchStoreProductList").submit();
}

//搜索表单重置
function formReset(){
	$("#storeName").val("");
	$("#gName").val("");
}

function reset(){
	$("input[name='number']").val("");
	$("input[name='price']").val("");
	$("input[name='linkPerson']").val("");
	$("input[name='linkTel']").val("");
	$("input[name='productId']").val("");
	$("textarea[name='note']").val("");
}

function commitSupply(){
	var number = $("input[name='number']").val();
	var price = $("input[name='price']").val();
	var linkPerson = $("input[name='linkPerson']").val();
	var linkTel = $("input[name='linkTel']").val();
	var productid = $("input[name='productId']").val();
	var note = $("textarea[name='note']").val();
	var ctxpath = $('#ctxpath').val();
	if(!(numberBlur()&&priceBlur()&&checkValue(linkPerson)&&checkValue(note)&&checkValue(number)&&checkValue(price))){
		alert('请填写必填项或正确填写!');
		return false;
	}else{
	  $.post(ctxpath+"/store/storeInquiry/Add",
			{number:number,price:price,linkPerson:linkPerson,linkTel:linkTel,productid:productid,note:note},
			function(data){
		    if(data=='1'){
		    	salert('议价发布成功');
		    	$("#apContent").hide();
		    	$("#apLayerBg").hide();
		    	$("#apLayer").hide();
		    }else{
		    	salert('议价发布失败,请重新提交议价');
		    }
	  });	
	}
}
function sub(){
	$("#apLayer,.apLayerBg").hide();
}

function checkValue (value){
	if(value == null || value == ""){
		return false;
	}else{
		return true;
	}
}

//分页
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchStoreProductList").submit();
}