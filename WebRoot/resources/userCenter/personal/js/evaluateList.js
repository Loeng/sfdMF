// JavaScript Document
$(document).ready(function() {
	/*选项卡切换*/
    $(".tabs a").click(function() {
        $(this).siblings().removeClass("nowItem");
		$(this).addClass("nowItem");
    });
	$(".tabs a").eq(0).click(function(){
		$(".toMe").hide();
		$(".toOther").show();
	})
	$(".tabs a").eq(1).click(function(){
		$(".toMe").show();
		$(".toOther").hide();
	})
	
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
	
});