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
	$(".checkAll input[type='checkbox']").click(function(){
		a = $(this);
		if(a.is(":checked")){
			$(".consultLine input[type='checkbox']").attr("checked",true);
		}
		else{
			$(".consultLine input[type='checkbox']").attr("checked",false);
		}
	})
	
	/*评价类型*/
	$(".listSelect dt").click(function() {
        $(this).next("dd").toggle();
    });
	$(".listSelect dd li").click(function(){
		var text=$(this).text();
		$(this).parents(".listSelect").find("dt").text(text);
		$(this).parent().hide();
	})
	
	/*点击外部消失*/
	$(".listSelect dt").click(function(e){
		e?e.stopPropagation():event.cancelBubble = true;
	});
	$(document).click(function(e){
		var a=$(".listSelect dd");
		a.hide();
	})
});