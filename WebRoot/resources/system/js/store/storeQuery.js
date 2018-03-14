
// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/store/list";
	}
	window.location.href=contextPath + "/system/store/list";
}
$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});