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
	document.getElementById("searchStoreForm").submit();
}

// 搜索
function searchSubmit(){
	document.getElementById("searchStoreForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("storeName")[0].value = "";
}

// 跳转到认证页面
function storeCheck(id, type){
	window.location.href = $("#ctxpath").val() + "/system/store/zizhi/jumpZiZhiPage/" + type + "/" + id;
}

// 企业认证通过或拒绝
function checkPass(rzType,storeId) {
	var ctxpath = $("#ctxpath").val();
	$.post(ctxpath + "/system/store/zizhi/checkZiZhi", {"rzType":rzType,"storeId":storeId}, function(data){
		if(data == true || data == "true"){
			econfirm('审核成功，是否返回列表？',goback,[rzType],null,null);
		}else{
			ealert("操作失败！");
		}
	});
}

// 返回列表
function goback(rzType){
	//var type = $("#type").val();
	window.location.href = $("#ctxpath").val() + "/system/store/zizhi/jumplist/"+rzType;
}