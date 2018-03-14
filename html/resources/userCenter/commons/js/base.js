// JavaScript Document
$(document).ready(function() {
	/*模拟下拉菜单*/
	$(".selList input").live("focus",function() {
		$(".selList ul").hide();
		$(".selList").css("z-index", "1");
		$(this).parents(".selList").children("ul").show();
		$(this).parents(".selList").css("z-index", "2");
	});
	$(".selList .iconDown").live("click",function() {
			$(".selList ul").hide();
			$(".selList").css("z-index", "1");
			$(this).parents(".selList").children("ul").show();
			$(this).parents(".selList").css("z-index", "2");
	});
	$(".selList ul li").live("click",function() {
		var str = $(this).text();
		$(this).parents(".selList").find("input").val(str);
		$(this).parent().hide();
	})

	/*点击外部收起下拉菜单*/
	$(".selList").live("click",function(e) {
		e ? e.stopPropagation() : event.cancelBubble = true;
	});
	$(document).live("click",function(e) {
		$(".selList ul").hide();
	})
	 
	 
	/*全部复选框*/
	$(".checkAll").click(function(){	
	$main = $(this);
	if($main.find("input").is(":checked")){
		//下级都被选中
		$main.find(".check").addClass("checked");
		$main.parents(".tabList").find("input").attr("checked",true);
		$main.parents(".tabList").find(".check").addClass("checked");
	}else{
		//下级都取消选中
		$main.find(".check").removeClass("checked");
		$main.parents(".tabList").find(":checkbox").attr("checked",false);
		$main.parents(".tabList").find(".check").removeClass("checked")
	}
	});
	
	/*当前复选框*/
	$(".checkItem").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			//选中
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
		}else{
			//取消选中
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
		}
	});
	
	/*左侧菜单展开收起*/
	$(".leftList dd").hide();
	$(".leftList .cur").parents("dd").show();
	 $(".leftList dt").click(function(){
		 	$(this).parent().siblings().find("dd").slideUp(200);
			$(this).next("dd").slideDown(200);
			$(this).addClass("listShow");
	})
	
	/*左侧菜单默认展开*/
	 /*$(".leftList dt").click(
	 	function(){
			$(this).next("dd").slideToggle(200);
			$(this).toggleClass("listShow");
		}
	 )*/
	
	/*弹窗关闭*/
	$(".apClose,.btnCancel").click(function(){
		$(this).parents(".apLayer").hide();
		$(".apLayerBg").hide();
	})

	//随屏滚动js
	var bodyHeight=$(document).height();
	var navHeight=$(".leftNav").outerHeight();
	var offBottom=bodyHeight-navHeight-147;
	var footerHeight=$(".bottomMenu").innerHeight()+$(".footer").outerHeight();
	var scrollH;
	$(window).scroll(function(){
		scrollH = $(document).scrollTop();
		if(scrollH >= 147){
			$(".budong").addClass("fixed");
			if(offBottom-scrollH<footerHeight){
				var top=offBottom-scrollH-footerHeight;
				$(".budong").css("top",top+"px");
			}
			else{
				$(".budong").css("top","0");
			}
		}
		else{
			$(".budong").removeClass("fixed");
		}
	});
});