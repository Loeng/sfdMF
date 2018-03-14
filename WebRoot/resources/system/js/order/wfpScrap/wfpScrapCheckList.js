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

//重置
function resetForm(){
	document.getElementsByName("scarpName")[0].value = "";
	document.getElementsByName("storeName")[0].value = "";
	document.getElementsByName("startTime")[0].value = "";
	document.getElementsByName("endTime")[0].value = "";
}

// 搜索
function searchSubmit(){
	document.getElementById("searchProductForm").submit();
}

//分页
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchProductForm").submit();
}

// 审核
function productCheck(id, contextPath){
	window.location.href = contextPath + "/system/wfpSp/toCheck/" + id ;
}


