// JavaScript Document

$(document).ready(function() {
	/*点击回复弹出*/
    $(".replyLink").click(function(){
		$(".replyLayer").hide();
		$(this).parents(".consultLine").next(".replyLayer").show();
	})
	
	/*取消关闭*/
	$(".canselBtn").click(function(){
		$(this).parents(".replyLayer").hide();
	})
	
	/*批量回复*/
	$(".replyMore").click(function(){
		$(".replyLayer").hide();
		$(this).parent().children(".replyLayer").show();
	})
	
	/*全选*/
	$(".checkAll").click(function(){
		$(".consultLine").find()
	})
});