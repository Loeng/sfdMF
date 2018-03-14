// JavaScript Document

$(document).ready(function() {
	 /*编辑头像*/
	 $(".userPhoto").hover(
	 	function(){
			$(this).children(".photoSet").toggle();
		}
	 )
	 
	 /*推荐商品悬停信息*/
	 $(".bottomPro li").hover(
	 	function(){
			$(this).children("span").slideToggle(200);
		}
	 )
});