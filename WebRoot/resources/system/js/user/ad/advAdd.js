// JavaScript Document

$(document).ready(function() {
    /*选项卡切换*/
	$(".tabs li").live("click",function(){
		$(this).siblings().removeClass("cur");
		$(this).addClass("cur");
		var content=$(this).parent().next(".advStyleContent").children(".styleContent");
		var index=$(this).parent().children("li").index(this);
		content.removeClass("cur");
		content.eq(index).addClass("cur");
	})
	
	/*广告类型选择*/ 
	$(".advStyleCheck").change(function() {
        var option=$(this).find("option:selected").attr("class");
		switch(option){
			case "normalStyle":
				$(".advStyleList").hide();
				$("div.normalStyle").show();
				break;
			case "changeStyle":
				$(".advStyleList").hide();
				$("div.changeStyle").show();
				break;
			case "diyStyle":
				$(".advStyleList").hide();
				$("div.diyStyle").show();
				break;
			case "fontStyle":
				$(".advStyleList").hide();
				$("div.fontStyle").show();
				break;
		}
	})
})