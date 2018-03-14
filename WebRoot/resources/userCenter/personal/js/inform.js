// JavaScript Document

$(document).ready(function() {
    $(".readLink a").toggle(function(){
		$(this).parent().parent().addClass("reading");
		$(this).text("收起");
	},function(){
		$(this).parent().parent().removeClass("reading");
		$(this).text("查看");
	})
});