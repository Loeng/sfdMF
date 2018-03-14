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
	$(".checkAll").live("click",function(){	
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
	$(".checkItem").live("click",function(){
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

	//随屏滚动js
	var bodyHeight=$(document).height();
	var navHeight=$(".leftNav").outerHeight();
	var offBottom=bodyHeight-navHeight-103;
	var footerHeight=$(".bottomMenu").innerHeight()+$(".footer").outerHeight();
	var scrollH;
	$(window).scroll(function(){
		scrollH = $(document).scrollTop();
		if(scrollH >= 103){
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

	/*左侧菜单展开收起*/
	$(".leftList dd").hide();
	$(".leftList .cur").parents("dd").show();
	 $(".leftList dt").click(function(){
		$(this).parent().siblings().find("dd").slideUp(200);
		$(this).parent().siblings().find("dt").removeClass("listShow");
		$(this).next("dd").slideDown(200);
		$(this).addClass("listShow");
	})
	
	/*弹窗关闭*/
	$(".apClose").live("click",function(){
		$(this).parents(".apLayer").hide();
		$(".apLayerBg").hide();
	})
	
	/*议价弹窗*/
	var count=$(".apBargain .stair li").length;
	for(i=4;i<count;i++){
		$(".apBargain .stair li").eq(i).hide();
	}
	$(".readAll").toggle(function(){
		$(".apBargain .stair li").show(200);
		$(this).addClass("allDown");
	},function(){
		for(i=4;i<count;i++){
			$(".apBargain .stair li").eq(i).hide(200);
		}
		$(this).removeClass("allDown");
	})
	
	$(".helpIcon").live("click",function(){
		$(".helpBg").show();
		$(".helpBox").show();
	})
	$(".helpClose").live("click",function(){
		$(".helpBg").hide();
		$(".helpBox").hide();
	})
});

function clearNoNum(obj){
	obj.value = obj.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符 
	obj.value = obj.value.replace(/^\./g,"");  //验证第一个字符是数字而不是. 
	obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的.  
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
}

function choseMenu(webroot,choseId){
	location.href=webroot + "/store/manager/" + choseId;	
}

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
			  "padding":"0 8px",
			  "white-space":"nowrap"
			});
		}
		else
		{
			$(this).find("div").css({
			  "overflow":"hidden",
			  "text-overflow":"ellipsis",
			  "padding":"0 8px",
			  "white-space":"nowrap"
			});
		}
	});
}