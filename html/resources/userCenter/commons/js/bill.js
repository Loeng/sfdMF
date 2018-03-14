// JavaScript Document

$(document).ready(function() {
    $(".billTab").click(function(){
		$(this).parent().children(".billTab").removeClass("cur");
		$(this).addClass("cur");
	})
	
	$(".tit .month").click(function(){
		$(".searchForm select").hide();
		$(".searchForm .month").show();
	})
	
	$(".tit .quarter").click(function(){
		$(".searchForm select").hide();
		$(".searchForm .quarter").show();
	})
});