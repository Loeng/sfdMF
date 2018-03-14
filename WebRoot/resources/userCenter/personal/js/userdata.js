// JavaScript Document

$(document).ready(function() {
	/*地区选择*/
	$(function(){
		$(".area_two label").click(function(){
			$(this).addClass("xz_label").siblings().removeClass("xz_label");
			//$(this).nextAll().slideToggle();		
		});
		$(".area_three b").click(function(){
			$(this).parents("div.gg_area").hide();
			$(this).parents("li").removeClass("xz_li");
			var county=$(this).text();
			var city=$(this).parents("label").children("strong").text();
			var province=$(this).parents("li").children("span").text();
			$(this).parents(".formRight").children(".siteBox").attr("value",province+" "+city+" "+county);
			
		});
		$(".area_box ul li").hover(function(){
			$(this).addClass("xz_li");
			},function(){
				$(this).removeClass("xz_li");	
				$(this).children().children("label").removeClass("xz_label");	
		});
	})
	
	/*地区选择展开隐藏*/
	$(".siteBox").focus(function(){
		$(this).next(".gg_area").slideDown(100);
	})
});