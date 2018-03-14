$(document).ready(function(){
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg");
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg");
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

// 分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchForm").submit();
}

// 搜索
function searchSubmit(){
	document.getElementById("searchForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("storeName")[0].value = "";
}

// 跳转到认证页面
function storeCheck(id){
	window.location.href = $("#ctxpath").val() + "/system/store/bank/checkone/" + id;
}

//企业认证通过或拒绝
function checkPass(storeId, mark, status) {
	var ctxpath = $("#ctxpath").val();
	var checknote = $.trim($("textarea[name='checkNote']").val());
	
	if(status == 2 && checknote == ""){
		ealert("请输入拒绝理由！");
		return;
	}
	$.post(ctxpath + "/system/store/checkStoreBasisInfo", {storeId:storeId,mark:mark,status:status}, function(data){
		if(data == true || data == "true"){
			ealert("审核成功！");
			$("#dis"+storeId).css("display", "none");
		}else{
			ealert("操作失败！");
		}
	});
}