$(document).ready(function() {
		$("#productNum").blur(function(){
			$.post($("#ctxpath").val()+"/store/supply/checkSupplyNum",{productNum:$("#productNum").val()},function(data){
				if(data >= 1){
					$("#productNum").after("<span id=\"productid\" class=\"errorText\">该商品编号已存在,请重新填写!</span>");
				}else{
					if($("#productid")){
						$("#productid").remove();
					}
				}
			});
		});
		$("#productNum").focus(function(){
			$("#productid").remove();
		});

		
		
});
function formSubmit(){
	$("#addSupplyProductForm").ajaxSubmit(
			function(data){
			if(data==true || data=="true"){
				econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
			}else{
				falert("添加失败");
			}
		});
}
function reset(){
	document.getElementById("addSupplyProductForm").reset();
}
//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/store/supply/supplyProductList";
	}
	window.location.href=contextPath + "/store/supply/supplyProductList";
}