// JavaScript Document

$(document).ready(function() {
	/*模拟下拉菜单*/
    $(".selList input").focus(function() {
		$(".selList ul").hide();
		$(".selList").css("z-index","1");
        $(this).parents(".selList").children("ul").show();
		$(this).parents(".selList").css("z-index","2");
    });
	$(".selList .selPoint").click(function() {
		$(".selList ul").hide();
		$(".selList").css("z-index","1");
        $(this).parents(".selList").children("ul").show();
		$(this).parents(".selList").css("z-index","2");
    });
	$(".selList ul li").click(function() {
		var str=$(this).text();
		$(this).parents(".selList").find("input").val(str);
		$(this).parent().hide();
	})
	
	/*点击外部收起下拉菜单*/
	$(".selList").click(function(e){
		e?e.stopPropagation():event.cancelBubble = true;
	});
	$(document).click(function(e){
		$(".selList ul").hide();
	})
})