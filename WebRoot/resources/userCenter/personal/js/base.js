// JavaScript Document
$(document).ready(function() {
	
	/*顶部展开和收起*/
	$(".downList").hover(function(){
		$(this).addClass("downListShow");
	},function(){
		$(this).removeClass("downListShow");
	})
	
	/*模拟下拉菜单*/
	$(".select_box").click(function(event){   
		event.stopPropagation();
		$(this).find(".option").toggle();
		$(this).parent().siblings().find(".option").hide();
	});
	$(document).click(function(event){
		var eo=$(event.target);
		if($(".select_box").is(":visible") && eo.attr("class")!="option" && !eo.parent(".option").length)
		$('.option').hide();									  
	});
	$(".option a").click(function(){
		var value=$(this).text();
		$(this).parent().siblings(".select_txt").text(value);
		$("#select_value").val(value)
	 })
	 
	 /*商品分类展开收起*/
	 $(".proAll").hover(function(){
			$(this).children(".proList").stop(true,true).toggle(200);
			$(this).toggleClass("proAllShow");
		}
	 )
	 
	 /*左侧菜单展开收起*/
	
	 $(".leftList .cur").parents("dd").show();
	 $(".leftList dt").click(function(){
		 	$(this).parent().siblings().find("dd").slideUp(200);
			$(this).next("dd").slideDown(200);
			$(this).addClass("listShow");
	})
});