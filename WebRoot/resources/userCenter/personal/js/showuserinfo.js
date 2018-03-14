// JavaScript Document

$(document).ready(function() {
	$(".formSite input").focus(function(){
		$(this).next(".gg_area").show();
	})
	
	/*地区选择*/
	$(function(){
		$(".gg_dp_logo img").click(function(){
			$(this).parent().parent().next("div.cz_tc").show();
		});
		$(".cz_tc a.qx").click(function(){
			$(this).parent("div.cz_tc").hide();
		});
		$(".ht_area img").click(function(){
			$(this).next(".gg_area").show();
		});
		$(".area_two label").click(function(){
			$(this).addClass("xz_label").siblings().removeClass("xz_label");
			//$(this).nextAll().slideToggle();		
		});
		$(".area_three b").click(function(){
			$(this).parents("div.gg_area").hide();
			$(this).parents("li").removeClass("xz_li");
		});
		$(".area_box ul li").hover(function(){
			$(this).addClass("xz_li");
			},function(){
				$(this).removeClass("xz_li");	
				$(this).children().children("label").removeClass("xz_label");	
		});
	})
})