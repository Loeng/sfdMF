// JavaScript Document

$(document).ready(function() {
	/*表格划过变色*/
	$(function(){
		$(".ht_list tr").mouseover(function(){
			$(this).addClass("tr_bg")
		});
		$(".ht_list tr").mouseout(function(){
			$(this).removeClass("tr_bg")
		});
		
		/*查询条件展开和收起*/
		$(".pro_ss dt span.span_up").click(function(){
			$(this).hide();
			$(this).prev(".span_down").show();
			$(this).parent().nextAll("dd").hide();
			$(this).parent().parent().next(".pro_ss_btn").hide();
		});
		$(".pro_ss dt span.span_down").click(function(){
			$(this).hide();
			$(this).next(".span_up").show();
			$(this).parent().nextAll("dd").show();
			$(this).parent().parent().next(".pro_ss_btn").show();
		});
	})	
	
	/*查看详情弹层*/
	$(".eyeIco").click(function(){
		var str=$(this).attr("class");
		var id=str.replace(/eyeIco /,"");
		$("#"+id).show();
		$(".apPreviewBg").show();
	})
	
	$(".apPreview .apClose").click(function(){
		$(".apPreview").hide();
		$(".apPreviewBg").hide();
	})
	
	/*横排广告*/
	var imgHeight=$(".hengAdv").height()-2;
	$(".hengAdv img").css("height",imgHeight-30+"px");
	var inWidth=$(".hengAdv img").length*($(".hengAdv img").width()+12);
	$(".hengAdv ul").css("width",inWidth+"px");
	
	/*竖排广告*/
	$(".shuAdv li img").css("width",$(".shuAdv").width()-2+"px");
	
	/*轮播li宽度*/
	$(".box_163css ul li").each(function() {
        var liWidth=$(this).parents(".scroll_box").width();
		$(this).css("width",liWidth+"px");
    });
	
	$(function(){
		var option = {
			prev:".prev", //上一张按钮，默认为null
			next:".next",//下一张按钮，默认为null
			nav:'.scroll_nav',//轮播导航，默认为null,当为null时，自动生成导航。
			auto:true//是否自动轮显，默认为true
		};
		$(".scroll_box").baisonSlider(option);
		//banner轮显按钮特效
		$(".prev,.next").find('img').hover(
			function(){
				$(this).stop(false,true).fadeTo(800,0);	
			},
			function(){
				$(this).stop(false,true).fadeTo(500,1);
			}
		);
	})
	
	/*图文切换2*/
	var unitHeight=$(".changePic2").height()*0.7-3;
	var titleHeight=$(".changePic2").height()*0.3/$(".advDownList").length;
	$(".advDownList dd").css("height",unitHeight-titleHeight+"px");
	$(".advDownList dt").css("height",titleHeight+"px");
	$(".advDownList dt").css("line-height",titleHeight+"px");
	$(".advDownList dd").eq(0).css("display","block");
	 $(".advDownList dt").hover(function(){
		 $(".advDownList dd").stop(true,true).slideUp(100);
		$(this).next("dd").stop(true,true).slideDown(100);
	})
});