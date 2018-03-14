function myreset(){
	$("input[name='orderId']").val("");
}

function sreachSubmit(){
	$("#thordersubmit").submit();
}

function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	$("#thordersubmit").submit();
}

function chaDetail(id){
	window.location.href = $("#ctxpath").val() + "/store/thorder/returnProductDetail/" + id;
}
