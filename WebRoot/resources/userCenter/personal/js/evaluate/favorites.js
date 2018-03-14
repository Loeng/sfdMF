// JavaScript Document

$(document).ready(function() {
	/*选项卡切换*/
	$(".saveHeader a").click(
		function(){
			$(this).parent().children("a").removeClass("nowItem");
			$(this).addClass("nowItem");
		}
	)
	
	/*列表行变色*/
	$(".saveList").hover(
		function(){
			$(this).toggleClass("listHover");
		}
	)
	
	/*店铺收藏名称隐现*/
	$(".shopInfo").hover(
		function(){
			$(this).children("span").slideToggle(200);
		}
	)
});

