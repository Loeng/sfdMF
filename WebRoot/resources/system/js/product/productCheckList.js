$(document).ready(function(){
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg")
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg")
	});
	$(".pro_ss dt span.span_down").click(function(){
		$(this).hide();
		$(this).next(".span_up").show();
		$(this).parent().nextAll("dd").hide();
		$(this).parent().parent().next(".pro_ss_btn").hide();
	});
	$(".pro_ss dt span.span_up").click(function(){
		$(this).hide();
		$(this).prev(".span_down").show();
		$(this).parent().nextAll("dd").show();
		$(this).parent().parent().next(".pro_ss_btn").show();
	});
});

// 重置
function resetForm(){
	document.getElementsByName("status")[0].checked = true;
	document.getElementsByName("name")[0].value = "";
	document.getElementsByName("storeId")[0].value = "";
	document.getElementsByName("minUnitPrice")[0].value = "";
	document.getElementsByName("maxUnitPrice")[0].value = "";
	document.getElementsByName("productNumber")[0].value = "";
}

// 搜索
function searchSubmit(){
	var rexE = /^([1-9][\d]{0,12}|0)(\.[\d]{1,2})?$/;
	var _min = $.trim($("input[name='minUnitPrice']").val());
	var _max = $.trim($("input[name='maxUnitPrice']").val());
	
	if(_min != "" && !rexE.test(_min)){
		ealert("价格填写有误");
		$("input[name='minUnitPrice']").val("");
		return;
	}
	if(_max != "" && !rexE.test(_max)){
		ealert("价格填写有误");
		$("input[name='maxUnitPrice']").val("");
		return;
	}
	if(parseFloat(_min) > parseFloat(_max)){
		ealert("价格区间填写有误");
		return;
	}
	document.getElementById("searchProductForm").submit();
}

//分页
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchProductForm").submit();
}

// 审核
function productCheck(id, contextPath){
	var type = $("#type").val();
	window.location.href = contextPath + "/system/product/toCheck/" + id + "/" + type;
}


