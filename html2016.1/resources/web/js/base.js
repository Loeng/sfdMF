// JavaScript Document
$(document).ready(function() {
	var count=$(".firstNav .cur").length;
	if(count==0){
		$(".secondNav,.navList em").hide();
	}
	if(count>0){
		$(".firstNav .cur").parents("li").addClass("show");
		$(".secondNav,.navList em").show();
	}
	/*一级菜单展开*/
	$(".navBtn").live("click",function(){
		$(this).parent("li").siblings("li").find("a").removeClass("cur");
		$(this).addClass("cur");
		$(this).parent().siblings().removeClass("show");
		$(this).parent().addClass("show");
	});	
		
	$(".navList a").live("click",function(){
		$(this).siblings("a").removeClass("cur");
		$(this).addClass("cur");
	});
    $(".navBtn").live("mouseover",function(){
		$(".firstNav li").removeClass("show");
		$(this).parent().addClass("show");
		$(".secondNav,.navList em").hide();
		if($(this).siblings(".navList").length>0){
			$(".secondNav,.navList em").show();
		}
	})
	$(".nav").live("mouseleave",function(){
		if(count==0){
			$(".secondNav,.navList em").hide();
		}
		$(".firstNav li").removeClass("show");
		$(".firstNav .cur").parents("li").addClass("show");
	})
});


//选项卡切换(自己写的)
$.fn.extend({
	qiehuan:function(canshu){
		//默认参数，为了测试所以写的区别于真实应用
		var defaults={
			tab:"tabv",
			cont:"tacont"
		};
		$.extend(defaults,canshu);
		var main=$(this);
		main.find(canshu.tab).hover(function(){
			$(this).siblings().removeClass("cur");
			$(this).addClass("cur");
			var dom=$(this).parent().siblings(canshu.cont);
			var index=$(this).parent().children().index(this);
			dom.hide();
			dom.eq(index).show();
		})
	}
})

//表格字符截取
function cutString(obj){
	$(obj).find("td").each(function() {
		$(this).attr("title",$(this).text().trim());
		if($(this).children("div").length<=0){
			var str=$(this).html();
			$(this).html("<div>"+str+"</div>");
			$(this).find("div").css({
			  "overflow":"hidden",
			  "text-overflow":"ellipsis",
			  "padding":"0 8px"
			});
		}
		else
		{
			$(this).find("div").css({
			  "overflow":"hidden",
			  "text-overflow":"ellipsis",
			  "padding":"0 8px"
			});
		}
	});
}