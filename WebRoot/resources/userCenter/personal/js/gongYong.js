$(document).ready(function() {
	var ctxpath = $('#ctxpath').val();
	$.post(ctxpath + "/user/center/top",function(data){
		$("#shopCartSum").empty();
		$("#shopCartSum").html("购物车(" + data + ")");
	});
})