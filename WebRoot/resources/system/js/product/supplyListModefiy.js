function supplyDetail(ctxpath,id,type){
	window.location.href = ctxpath+"/system/supply/lookSupply/"+id+"/"+type;
}

function formSubmit(){
	$("#addSystemSupplyProductForm").ajaxSubmit(
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
		window.location.href="/system/supply/supplyProductList";
	}
	window.location.href=contextPath + "/system/supply/supplyProductList";
}