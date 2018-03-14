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
	
	/*选项卡切换*/
	$(".orderLogTab").click(function(){
		$(".orderLog").show();
		$(".orderInfo").hide();
		$(".orderInfoTab").removeClass("cur")
		$(this).addClass("cur");
		$(".orderInfo").removeClass("toggles");
		$(".orderLog").addClass("toggles");
	})
	
	$(".orderInfoTab").click(function(){
		$(".orderLog").hide();
		$(".orderInfo").show();
		$(".orderLogTab").removeClass("cur")
		$(this).addClass("cur");
		$(".orderLog").removeClass("toggles");
		$(".orderInfo").addClass("toggles");
	})
	
	/*修改金额*/
	$(".money .btnOrange").click(function(){
		$(this).next(".editMoney").toggle();
	})
});