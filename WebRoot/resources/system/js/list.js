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
		$(".pro_ss dt span.span_up").live("click",function(){
			$(this).hide();
			$(this).prev(".span_down").show();
			$(this).parent().nextAll("dd").hide();
			$(this).parent().parent().next(".pro_ss_btn").hide();
		});
		$(".pro_ss dt span.span_down").live("click",function(){
			$(this).hide();
			$(this).next(".span_up").show();
			$(this).parent().nextAll("dd").show();
			$(this).parent().parent().next(".pro_ss_btn").show();
		});
	});
	/*
	// 查看详情弹层
	$(".eyeIco").live("click",function(){
		$(".apPreview").show();
		$(".apPreviewBg").show();
	});
	$(".apPreview .apClose").live("click",function(){
		$(".apPreview").hide();
		$(".apPreviewBg").hide();
	});*/
})