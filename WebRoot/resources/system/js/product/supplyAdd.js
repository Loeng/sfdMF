$(document).ready(function() {
	$("#productNum").blur(function(){
		if($("#productNum").val()==''){
			$("#productNum").after("<span id=\"productid\" class=\"errorText\">商品编号不能为空</span>");
		}else{
			$.post($("#ctxpath").val()+"/store/supply/checkSupplyNum",{productNum:$("#productNum").val()},function(data){		
				if(data >= 1){
					$("#productNum").after("<span id=\"productid\" class=\"errorText\">该商品编号已存在,请重新填写!</span>");
				}else{
					if($("#productid")){
						$("#productid").remove();
					}
				}
			});
		}
	});
	$("#productNum").focus(function(){
		$("#productid").remove();
	});	
	/*验证商品名称*/
	$("#productName").blur(function(){
		if($("#productName").val()==''){
			$("#nameSpan").show();
		}else{
			$("#nameSpan").hide();
		}
	});
	$("#unit").focus(function(){
		$("#unitSpan").remove();
	});	
	/*验证商品名称*/
	$("#unit").blur(function(){
		if($("#unit").val()==''){
			$("#unitSpan").show();
		}else{
			$("#unitSpan").hide();
		}
	});
	
	/*验证数量*/
	$("#quantity").blur(function(){
		var  reg = /^\d+$/;
		if($("#quantity").val()==''){
			$("#quantitySpan").show();
		}else{
			if(!reg.test($("#quantity").val())){
				$("#quantitySpan").show();
			}
		}
	});
	$("#quantity").focus(function(){
		$("#quantitySpan").hide();
	});
	/*验证批发价格*/
	$("#jiage").blur(function(){
		var reg=/^\d+(\.\d{0,2})?$/;
		if($("#jiage").val()==''||$("#jiage").val()=='0.00'){
			$("#jiageSpan").show();
		}else{
			if(!reg.test($("#jiage").val())){
				$("#jiageSpan").show();
			}
		}
	});
	$("#jiage").focus(function(){
		$("#jiageSpan").hide();
	});
	$("#unit").blur(function(){
		if($("#unit").val()==''){
			$("#unitySpan").show();
		}else{
			$("#unitySpan").hide();
		}
	});
	/*验证联系人*/
	$("#linkPerson").blur(function(){
		if($("#linkPerson").val()==''){
			$("#linkPersonSpan").show();
		}
	});
	$("#linkPerson").focus(function(){
		$("#linkPersonSpan").hide();
	});
	/*验证联系方式*/
	$("#linkTel").blur(function(){
		if($("#linkTel").val()==''){
			$("#linkTelSpan").show();
		}else{
			var reg=/^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/;
			if(!reg.test($("#linkTel").val())){
				$("#linkTelSpan").show();
			}
		}
	});
	$("#linkTel").focus(function(){
		$("#linkTelSpan").hide();
	});
});
//验证商品名称
function productName(){
		if($("#productName").val()==''){
			$("#nameSpan").show();
			return false;
		}else{
			return true;
		}

}
function productquantity(){
   /*验证数量*/
	var  reg =  /^\d+(.d+)?$/;
	if($("#quantity").val()==''){
		$("#quantitySpan").show();
		return false;
	}else{
		if(!reg.test($("#quantity").val())){
			$("#quantitySpan").show();
			return false;
		}else{
			return true;
		}
	}
}
function productNum(){
	$("#productid").remove();
	if($("#productNum").val()==''){
		$("#productNum").after("<span id=\"productid\" class=\"errorText\">商品编号不能为空</span>");
		return false;
	}
	$.post($("#ctxpath").val()+"/system/supply/checkSupplyNum",{productNum:$("#productNum").val()},function(data){		
		if(data >= 1){
			$("#productNum").after("<span id=\"productid\" class=\"errorText\">该商品编号已存在,请重新填写!</span>");
		}
	});
	
	return true;
}

function unitcheck(){
	var val = $.trim($("#unit").val());
	if(val == ""){
		$("#unitySpan").show();
		return false;
	}else{
		return true;
	}
}
function pfprice(){
		var reg=/^\d+(\.\d{0,2})?$/;
		if($("#jiage").val()==''||$("#jiage").val()=='0.00'){
			$("#jiageSpan").show();
			return false;
		}else{
			if(!reg.test($("#jiage").val())){
				$("#jiageSpan").show();
				return false;
			}else{
				return true;
			}
		}
}
function linkperson(){
		if($("#linkPerson").val()==''){
			$("#linkPersonSpan").show();
			return false;
		}else{
			return true;
		}
}
function  linktel(){
	if($("#linkTel").val()==''){
		$("#linkTelSpan").show();
		return false;
	}else{
		var reg=/^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/;
		if(!reg.test($("#linkTel").val())){
			$("#linkTelSpan").show();
			return false;
		}else{
			return true;
		}
	}
}
function linkname(){
	if($("#userName").val()==''){
		$("#userNameSpan").show();
		return false;
	}else{
		return true;
	}
}
//验证
function formSubmit(){
	if(productName()&&productquantity()&&productNum()&&unitcheck()&&unitcheck()&&pfprice()&&linkperson()&&linktel()&&linkname()){
		$.post($("#ctxpath").val()+"/system/supply/checkSupplyNum",{productNum:$("#productNum").val()},function(data){		
			if(data >= 1){
				$("#productNum").after("<span id=\"productid\" class=\"errorText\">该商品编号已存在,请重新填写!</span>");
			}else{
				$("#addSystemSupplyProductForm").ajaxSubmit(
						function(data){
							if(data==true || data=="true"){
								econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
							}else{
								falert("添加失败");
							}
					});
			}
		});
	}
}

function reset(){
	document.getElementById("supplyProductName").value="";
	document.getElementById("productNumsearch").value="";
}

//重新选择分类
function  restCatory(contextPath){
	window.location.href= contextPath + "/system/product/supplyProductAddClassify";
}
function searchSubmit(){
	document.getElementById("searchProductForm").submit();
}
//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/supply/supplyProductList";
	}
	window.location.href=contextPath + "/system/supply/supplyProductList";
}
function supplyDetail(ctxpath,id,type){
	window.location.href = ctxpath+"/system/supply/lookSupply/"+id+"/"+type;
}