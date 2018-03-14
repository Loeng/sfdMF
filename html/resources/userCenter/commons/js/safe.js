// JavaScript Document
$(document).ready(function() {
	
	/*µÇÂ½ÃÜÂëÐÞ¸Äµ¯´°ÌáÊ¾*/
	$(".jsLoginPw").click(function(){
		$(".alertLayerBg").show();
		$(".alertLayerBg").next(".alterBox").show();
	})
	$(".layerClose").click(function(){
		$(this).parents(".alterBox").hide();
		$(".alertLayerBg").hide();
	})
	
		/*ÓÊÏäÐÞ¸Äµ¯´°ÌáÊ¾*/
	$(".jsTradePw").click(function(){
		$(".alertLayerBg").show();
		$(".alterBoxTrade").show();
	})
	$(".layerClose").click(function(){
		$(this).parents(".alterBoxTrade").hide();
		$(".alertLayerBg").hide();
	})
	
		/*ÊÖ»úºÅÐÞ¸Äµ¯´°ÌáÊ¾*/
	$(".jsQuestionPw").click(function(){
		$(".alertLayerBg").show();
		$(".alterBoxQuestion").show();
	})
	$(".layerClose").click(function(){
		$(this).parents(".alterBoxQuestion").hide();
		$(".alertLayerBg").hide();
	})
	
		/*ÒøÐÐ¿¨ÐÞ¸Äµ¯´°ÌáÊ¾*/
	$(".jsBank").click(function(){
		$(".alertLayerBg").show();
		$(".alterBoxBank").show();
	})
	$(".layerClose").click(function(){
		$(this).parents(".alterBoxBank").hide();
		$(".alertLayerBg").hide();
	})
});
