// JavaScript Document
$(document).ready(function() {

	/*议价弹窗*/
	/*$(".eyeIco").click(function(){
		$(".apBargain").show();
		$(".apLayerBg").show();
	})*/
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
});