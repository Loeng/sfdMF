// 分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchStoreForm").submit();
}

// searchUserForm的提交
function searchSubmit(){
	document.getElementById("searchStoreForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("status")[0].checked = true;
	document.getElementsByName("storeName")[0].value = "";
}

// 点击修改按钮，根据id进行会员资料查询
function queryId(id, type){
	var ctxpath = $("#ctxpath").val();
	window.location.href = ctxpath + "/system/store/queryCreditInfo/" + id + "/" + type;
}



