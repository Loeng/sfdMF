// JavaScript Document
$(document).ready(function() {
	// 弹出层的显示
    $(".imgList a").click(function(){
    	// 获取图片的地址并将其放入方法区域的地址
    	var picPath = $(this).children("img").eq(0).attr("src");
    	$(".del_tc .imgContent").children("img").eq(0).attr("src",picPath);
		$(".del_tcBg").show();
		$(".del_tc").show();
	})
	
	// 关闭弹出层
	$(".del_tc .tit .apClose").live("click",function(){
		$(".del_tcBg").hide();
		$(".del_tc").hide();
	})
});