// JavaScript Document
$(document).ready(function() {
	/*弹层*/
    $(".clickBrand").click(function(){
		$(".apLayerBg").show();
		$(".alertBrand").show();
	})
	
	$(".clickTemp").click(function(){
		$(".apLayerBg").show();
		$(".alertTemp").show();
	})
	
	
	/*增加图片*/
	$(".btnAdd").click(function(){
		var obj=$(".uploadItem").eq(0).clone();
		obj.append("<a href=\"javascript:void(0);\" class=\"closeIco\"></a>")
		$(this).before(obj);
	})
	/*删除图片*/
	$(".uploadItem .closeIco").live("click",function(){
		$(this).parents(".uploadItem").remove();
	})
});