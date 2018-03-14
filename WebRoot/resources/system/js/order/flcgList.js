$(document).ready(function() {
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg");
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg");
	});
});	

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchFlcgForm").submit();
}

//搜索
function searchSubmit(){
	document.getElementById("searchFlcgForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("productNo")[0].value="";
	document.getElementsByName("beginDate")[0].value="";
	document.getElementsByName("endDate")[0].value="";
	document.getElementsByName("companyName")[0].value="";
}