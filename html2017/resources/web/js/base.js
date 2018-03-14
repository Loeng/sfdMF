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
		else{
			$(".secondNav,.navList em").show();
		}
		$(".firstNav li").removeClass("show");
		$(".firstNav .cur").parents("li").addClass("show");
	})
	
	//浏览器版本
	if($.browser.msie) { 
		var count=parseInt($.browser.version);
		if(count<=9){
			$(".vertical").addClass("ieLow");
		}
	}
});
//main最低高度
function mainH(){
	var screenH=window.innerHeight;
	var otherH=$(".header").outerHeight(true)+$(".nav").outerHeight(true)+$(".footer").outerHeight(true);
	var inH=$(".main").outerHeight(true)-$(".main").height();
	$(".main").css("min-height",screenH-otherH-inH+"px");
}
window.onresize=function(){
	mainH();
}
window.onload=function(){
    mainH();
}

//选项卡切换(自己写的)
$.fn.extend({
	qiehuan:function(canshu){
		//默认参数
		var defaults={
			tab:".tabNav li",
			cont:".tabContent",
			onStyle:"hover"
		};
		
		$.extend(defaults,canshu);
		var main=$(this);
		
		main.find(defaults.tab).on(defaults.onStyle,function(){
			$(this).siblings().removeClass("cur");
			$(this).addClass("cur");
			var dom=$(this).parent().siblings(defaults.cont);
			var index=$(this).parent().children().index(this);
			dom.hide();
			dom.eq(index).show();
		})
	}
})
//滚动加载
$.fn.extend({
	autoScroll:function(parms){
		//默认参数
		var defaults={
			data:"",
			delay:2000,
			runTime:800,
			outerBox:".slideIn",
			lineBox:"ul",
			line:"li"
		};
		
		$.extend(defaults,parms);
		var main=$(this);
		if(defaults.data==""||defaults.data==undefined){
			alert("未设定行数据");
		   	return false;
	   	}
		function scrollNews(){
			var $self = main.find(defaults.lineBox);
			var gao=main.find(defaults.outerBox).height();
			var itemH=$self.find(defaults.line).first().outerHeight(true);
			var shu=$self.find(defaults.line).length;
			var gaoAll=shu*itemH;
			if(gaoAll-itemH<=gao-itemH){
			   return false;
			}
			else{
			   $self.animate({ "marginTop" : -itemH +"px" },defaults.runTime, function(){
				   //模拟josn加载
				   $self.append(defaults.data);
				   $self.css("margin-top","0");
				   $self.find(defaults.line).first().remove();
			   });
			}
		}
		run = setInterval(scrollNews, defaults.delay);
		main.hover(function(){
			clearInterval(run);
		},function(){
            run = setInterval(scrollNews, defaults.delay);
		})
	}
})
	

//循环滚动


