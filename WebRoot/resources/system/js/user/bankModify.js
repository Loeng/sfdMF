//查询
function formSubmit(){
	document.getElementById("searchBankForm").submit();
}
function bankSubmit(ctx,userid){
	econfirm('是否确认审核该银行卡?',submit,[ctx,userid],null,null);
}
function submit(ctx,userid){
	$("#BankCardForm").ajaxSubmit(
			function(data){
			if( data=="1"){
				econfirm('审核成功',goBack,[ctx,userid],goBack,[ctx,userid]);
			}else if(data=="0"){
				econfirm('审核失败',goBack,[ctx,userid],goBack,[ctx,userid]);
			}
		});
}

//返回列表
function goBack(contextPath,userid){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href=contextPath+"/system/user/bankCardList/"+userid;
	}
	window.location.href=contextPath + "/system/user/bankCardList/"+userid;
}