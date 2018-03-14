function supplyDetail(ctxpath,id,type){
	window.location.href = ctxpath+"/store/supply/lookSupply/"+id+"/"+type;
}

function formSubmit(){
	$("#editSupplyProductForm").ajaxSubmit(
			function(data){
			if(data==true || data=="true"){
				econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
			}else{
				falert("修改失败!");
			}
		});
}
//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/store/supply/supplyProductList";
	}
	window.location.href=contextPath + "/store/supply/supplyProductList";
}

function searchSubmit(){
	$("#supplyFormSubmit").submit();
}

function myreset(){
	$("input[name='productName']").val("");
	$("input[name='productNum']").val("");
}

function goPage(num){
	$("input[name='pageNum']").val(num);
	$("#supplyFormSubmit").submit();
}