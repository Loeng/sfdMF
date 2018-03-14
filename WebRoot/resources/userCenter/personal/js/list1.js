// JavaScript Document

$(document).ready(function() {
	
	/*选项卡切换*/
	$(".orderHeader a").click(
		function(){
			$(this).parent().children("a").removeClass("nowItem");
			$(this).addClass("nowItem");
		}
	)
	
	/*列表行变色*/
	$(".orderList").hover(
		function(){
			$(this).toggleClass("listHover");
		}
	)
});

