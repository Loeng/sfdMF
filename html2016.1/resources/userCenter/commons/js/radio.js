// JavaScript Document

$(document).ready(function() {
	/*模拟单选按钮*/
	$(".radioCheked").find("input").attr("checked","checked");
	$(".radio").click(function(){
		var radio=$(this).children("input");
		$(this).addClass("radioCheked");
		radio.attr("checked","checked");
		$(this).parent().siblings().find("input").removeAttr("checked");
		$(this).parent().siblings().children(".radio").removeClass("radioCheked");
	})


})