//返回列表
function goBack(ctxpath){
	window.location.href = ctxpath+"/system/product/brand/list";
}

$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
});