// JavaScript Document
$(document).ready(function() {
    //下拉登录和注册样式
	$(".downList").live("mouseover",function(){
		$(this).addClass("downListShow");
	})
	$(".downList").live("mouseout",function(){
		$(this).removeClass("downListShow");
	})
	
	
	
	/*点击外部收起下拉菜单*/
	$(".nowData").click(function(e) {
		e ? e.stopPropagation() : event.cancelBubble = true;
	});
	$(document).click(function(e) {
		$(".qxt").hide();
	});
	
	//随屏滚动js
	var bodyHeight=$(document).height();
	var navHeight=$(".leftLinks").outerHeight();
	var offBottom=bodyHeight-navHeight-235;
	var footerHeight=$(".bottomMenu").innerHeight()+$(".footer").outerHeight();
	var scrollH;
	$(window).scroll(function(){
		scrollH = $(document).scrollTop();
		if(scrollH >= 535){
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
	
	
	/*模拟下拉菜单*/
	$(".selList input").focus(function() {
		$(".selList ul").hide();
		$(".selList").css("z-index", "1");
		$(this).parents(".selList").children("ul").show();
		$(this).parents(".selList").css("z-index", "2");
	});
	$(".selList .iconDown").click(function(){
			$(".selList ul").hide();
			$(".selList").css("z-index", "1");
			$(this).parents(".selList").children("ul").show();
			$(this).parents(".selList").css("z-index", "2");
	});
	$(".selList ul li").click(function(){
		var str = $(this).text();
		$(this).parents(".selList").find("input").val(str);
		$(this).parent().hide();
	})

	/*点击外部收起下拉菜单*/
	$(".selList").click(function(e) {
		e ? e.stopPropagation() : event.cancelBubble = true;
	});
	$(document).click(function(e) {
		$(".selList ul").hide();
	})
});



function setCookie(c_name, value, expiredays){
	var exdate=new Date(); 
	exdate.setDate(exdate.getDate() + expiredays); 
	document.cookie=c_name+ "=" + escape(value) + ((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
}