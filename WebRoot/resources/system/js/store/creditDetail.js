$(document).ready(function() {
	// 按钮浮动定位
	$(".ht_btn").next().addClass("afterHt");
	
	$("input.i_bg").focus(function (){ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 
});

// 审核通过
function formSubmit() {
	var note = $.trim($("#creditResult").val());
	if(note == ""){
		ealert("请填写测算结果！");
		return;
	}
	econfirm("是否确认测算?", submit, null, null, null);
}
// 通过
function submit(){
	var ctx = $("#ctxpath").val();
	var id = $("#ceId").val();
	var note = $.trim($("#creditResult").val());
	
	$.post(ctx + "/system/store/auCreditInfo", {ceId:id, note:note}, function(data){
		if(data == true || data == "true"){
			ealert("测算完成！");
		}else{
			ealert("测算失败，系统异常！");
		}
	});
}




