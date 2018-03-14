// JavaScript Document
$(document).ready(function() {
		/*可编辑*/
	$(".btnEdit").click(function() {
		$(".radio").children().find("input").removeAttr("disabled","disabled");
		$("input").css("background","#FFF");
		$(".checkBox").children().find("input").removeAttr("disabled","disabled");
		$("input").removeAttr("disabled","disabled");
		$("input").removeAttr("readonly","readonly");
		$(".iconDown").removeClass("readOnly");
		$(".selList").children().find("input").attr("readonly","readonly");
		$(".ddh").attr("readonly","readonly");
	});
	
	
	/*展开和收起*/
    $(".layerToggle").click(function(){
		$(this).toggleClass("layerToggleDown");
		$(this).parent().next(".toggles").slideToggle(100);
		$(this).parent().next().next(".toggles").slideToggle(100);
		$(".layerToggle").text("收起");
		$(".layerToggleDown").text("展开");
	})
	
	/*选项卡切换*/
	$(".orderLogTab").click(function(){
		$(".orderLog").show();
		$(".orderInfo").hide();
		$(".orderInfoTab").removeClass("cur")
		$(this).addClass("cur");
		$(this).addClass("borderBottomColor");
		$(".orderInfo").removeClass("toggles");
		$(".orderLog").addClass("toggles");
	})
	
	$(".orderInfoTab").click(function(){
		$(".orderLog").hide();
		$(".orderInfo").show();
		$(".orderLogTab").removeClass("cur")
		$(this).addClass("cur");
		$(".orderLog").removeClass("toggles");
		$(".orderInfo").addClass("toggles");
	})
	
	/*修改金额*/
	$(".money .btnChange").click(function(){
		$(this).hide();
		$(this).next(".editMoney").show();
	})
	
	/*保存*/
	$(".btnSave").click(function(){
		var price="￥"+$(this).siblings(".editBox").find("input").val();
		$(this).parents(".money").find(".price").text(price);
        $(this).parents(".editMoney").hide();
		$(this).parents(".money").find(".btnChange").show();
    });

		/*拒绝申请*/
	$(".btnRefuse").click(function(){
		$(".decline").show();
	})
	$(".btnCansel").click(function(){
		$(".decline").hide();
	})
});
