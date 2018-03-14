// JavaScript Document
$(document).ready(function() {
	/*议价弹窗*/
	$(".eyeIco").click(function(){
		$(".apRead").show();
		$(".apLayerBg").show();
	})
	
	$(".editIco").click(function(){
		$(".apBargain").show();
		$(".apRead").hide();
		$(".apLayerBg").show();
	})
	
	$(".btnReset").click(function(){
		$(".writeForm input").val("");
	})
	
	$(".apBargain").each(function(index, element) {
        var count=$(this).find(".stair li").length;
		for(i=4;i<count;i++){
			$(this).find(".stair li").eq(i).hide();
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
});