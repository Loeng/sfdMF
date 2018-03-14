// JavaScript Document

$(document).ready(function() {
	/*选项卡切换*/
	$(".saveHeader a").click(
		function(){
			$(this).siblings().removeClass("nowItem");
			$(this).addClass("nowItem");
			var index=$(".saveHeader a").index(this);
			$(".saveList").hide();
			$(".saveList").eq(index).show();
		}
	)
	
	/*列表行变色*/
	$(".saveList").hover(
		function(){
			$(this).toggleClass("listHover");
		}
	)
	
	/*店铺收藏名称隐现*/
	$(".shopInfo").hover(
		function(){
			$(this).children("span").slideToggle(200);
		}
	)
	
	/*全选*/
	$(".listCheck input,.checkAll input").click(function(e) {
       var obj=$(this);
	   if(obj.is(":checked")){
		   $(this).parents(".saveList").find("input[type='checkBox']").attr("checked",true);
	   }
	   else{
		   $(this).parents(".saveList").find("input[type='checkBox']").attr("checked",false);
	   }
    });
});

