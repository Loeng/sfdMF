// 重置
function loanReset(){
	$("input[type='text']").each(function(){
		$(this).val("");
	});
}

// 搜索
function loanSubmit(){
	$("#maSubmitForm").submit();
}

// 新增
function addShen(type){
	var ctx = $("#ctxpath").val();
	window.location.href = ctx + "/store/finance/jumpDaiPape/" + type;
}

// 编辑
function loanEdit(id){
	var ctx = $("#ctxpath").val();
	window.location.href = ctx + "/store/finance/jumpDaiDetailPape/3?id=" + id;
}

// 详情
function orderDetail(id){
	var ctx = $("#ctxpath").val();
	window.location.href = ctx + "/store/finance/jumpDaiDetailPape/2?id=" + id;
}

// 分页
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("maSubmitForm").submit();
}
