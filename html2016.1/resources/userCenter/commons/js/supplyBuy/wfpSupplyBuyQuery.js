
//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/store/supplyBuy/wfpList";
	}
	window.location.href=contextPath + "/store/supplyBuy/wfpList";
}
