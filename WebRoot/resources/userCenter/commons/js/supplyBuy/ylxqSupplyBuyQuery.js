
//返回列表
function goBack(contextPath){
	var productType = $("#productType").val();
	var type = $("#type").val();
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/store/supplyBuy/ylxqList/"+productType+"/"+type;
	}
	window.location.href=contextPath + "/store/supplyBuy/ylxqList/"+productType+"/"+type;
}
