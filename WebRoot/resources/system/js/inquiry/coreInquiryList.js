$(document).ready(function() {
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg");
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg");
	});
	
	// 显示
	$(".pro_ss dt span.span_down").click(function(){
		$(this).hide();
		$(this).next(".span_up").show();
		$(this).parent().nextAll("dd").hide();
		$(this).parent().parent().next(".pro_ss_btn").hide();
	});
	// 隐藏
	$(".pro_ss dt span.span_up").click(function(){
		$(this).hide();
		$(this).prev(".span_down").show();
		$(this).parent().nextAll("dd").show();
		$(this).parent().parent().next(".pro_ss_btn").show();
	});
	
});	

;
//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchInquiryForm").submit();
}



//搜索
function searchSubmit(){
	document.getElementById("searchInquiryForm").submit();
}


// 重置
function resetForm(){
	document.getElementsByName("status")[0].checked=true;
	document.getElementsByName("name")[0].value="";
	document.getElementsByName("beginDate")[0].value="";
	document.getElementsByName("endDate")[0].value="";
	document.getElementsByName("userId")[0].value="";
}




//------------------------------议价详情信息-----------------------------
//跳转到详情
function goDetail(id,contextPath){
	window.location.href = contextPath + "/system/coreInquiry/detail/"+id;
}