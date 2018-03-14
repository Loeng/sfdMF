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
	$('#textArea').on("keyup",function(){
		$('#textNum').text($('#textArea').val().length);//这句是在键盘按下时，实时的显示字数
		if($('#textArea').val().length > 300){
		$('#textNum').text(300);//长度大于100时0处显示的也只是100
		$('#textArea').val($('#textArea').val().substring(0,300));//长度大于100时截取钱100个字符
		}
		})
		$('#textNum').text($('#textArea').val().length);//这句是在刷新的时候仍然显示字数

});	


// 物流宝留言反馈
function feedback(id) {
	var ctxpath = $("#ctxpath").val();
	var checknote = $.trim($("textarea[name='feedbackContent']").val());
	
	if(checknote == ""){
		ealert("请输入反馈信息！");
		return;
	}
	$.post(ctxpath + "/system/wlblyfk/feedback", {id:id,"feedbackContent":checknote}, function(data){
		if(data == "1"){
			ealert("反馈成功！");
			$("#dis"+storeId).css("display", "none");
		}else if(data == "2"){
			ealert("用户的极光id为空[RegistrationID],无法反馈！");
		}else{
			ealert("反馈失败！");
		}
		
	});
}
