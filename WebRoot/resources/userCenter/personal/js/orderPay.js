// JavaScript Document

$(document).ready(function() {
	/*展开和收起*/
    $(".layerToggle").click(function(){
		$(this).toggleClass("layerToggleDown");
		$(this).parent().next(".toggles").slideToggle(100);
		$(this).parent().next().next(".toggles").slideToggle(100);
		$(".layerToggle").text("收起");
		$(".layerToggleDown").text("展开");
	})
})