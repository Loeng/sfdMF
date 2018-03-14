// 重置
function reset(){
	$("input[type='text']").each(function(){
		$(this).val("");
	});
}

// 搜索
function searchSubmit(){
	$("#mainSearchForm").submit();
}

// 新增
function addShen(type){
	window.location.href = $("#ctxpath").val() + "/store/finance/jumpDaiPape/" + type;
}

// 编辑
function loanEdit(id){
	window.location.href = $("#ctxpath").val() + "/store/finance/jumpDaiDetailPape/3?id=" + id;
}

// 详情
function orderDetail(id){
	window.location.href = $("#ctxpath").val() + "/store/finance/jumpDaiDetailPape/2?id=" + id;
}

// 分页
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	$("#mainSearchForm").submit();
}
