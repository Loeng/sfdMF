// JavaScript Document
$(document).ready(function() {
	
	/*��½�����޸ĵ�����ʾ*/
	$(".jsLoginPw").click(function(){
		$(".alertLayerBg").show();
		$(".alertLayerBg").next(".alterBox").show();
	})
	$(".layerClose").click(function(){
		$(this).parents(".alterBox").hide();
		$(".alertLayerBg").hide();
	})
	
		/*�����޸ĵ�����ʾ*/
	$(".jsTradePw").click(function(){
		$(".alertLayerBg").show();
		$(".alterBoxTrade").show();
	})
	$(".layerClose").click(function(){
		$(this).parents(".alterBoxTrade").hide();
		$(".alertLayerBg").hide();
	})
	
		/*�ֻ����޸ĵ�����ʾ*/
	$(".jsQuestionPw").click(function(){
		$(".alertLayerBg").show();
		$(".alterBoxQuestion").show();
	})
	$(".layerClose").click(function(){
		$(this).parents(".alterBoxQuestion").hide();
		$(".alertLayerBg").hide();
	})
	
		/*���п��޸ĵ�����ʾ*/
	$(".jsBank").click(function(){
		$(".alertLayerBg").show();
		$(".alterBoxBank").show();
	})
	$(".layerClose").click(function(){
		$(this).parents(".alterBoxBank").hide();
		$(".alertLayerBg").hide();
	})
});
