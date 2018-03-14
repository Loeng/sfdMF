
// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/user/list";
	}
	window.location.href=contextPath + "/system/user/list";
}
$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});