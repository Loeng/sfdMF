

// 返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/product/list";
	}
	window.location.href=contextPath + "/system/product/list";
}

$(document).ready(function() {
	
	/*树形菜单*/
	$('.fl_title').click(function(){
		$(this).next(".fl_in").slideToggle();
		$(this).toggleClass("ht_fl_zk");
	});
	$('.fl_nav label').click(function(){
		$(".fl_nav label").removeClass("cur");
		$(this).addClass("cur");
		$(this).text("已选择")
	});
	
	/*复选框*/
	$(".check").click(function(){
		$(this).toggleClass("checked");
	})
	
	/*单选框*/
	$("input[type='radio']").change(function() {
        $(this).parent().parent().addClass("radioSel")
    });
	
	/*关闭弹窗*/
	$(".del_list").click(function(){
		$(".del_tc").show();
		$(".del_tcBg").show();
	});
	$(".del_tc .tit span").click(function(){
		$(".del_tc").hide();
		$(".del_tcBg").hide();
	});
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
	
});