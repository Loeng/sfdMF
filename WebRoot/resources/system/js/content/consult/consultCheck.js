// 审核提交
function checkConsultSubmit(checkStatus,consultType){
	if(checkStatus == '-1'){
		if(!checkNoteForDisagree()){
			return;
		}
	}
	var ctxpath = $("#ctxpath").val();
	var consultId = $("#consultId").val();
	var checkNote = $("#checkNote").val();
	if(consultType == "0"){
		document.write("<form action='"+ctxpath+"/system/advisoryCheck' method='post' name='consultCheckForm' style='display:none;'>");		
	}else{
		document.write("<form action='"+ctxpath+"/system/Check' method='post' name='consultCheckForm' style='display:none;'>");
	}
	document.write("<input name='consultId' value='"+consultId+"'>");
	document.write("<input name='checkStatus' value='"+checkStatus+"'>");
	document.write("<input name='checkNote' value='"+checkNote+"'>");
	document.write("</form>");
	$("form[name='consultCheckForm']").submit();
}


// 必填项验证
function checkNoteForDisagree(){
	var checkNote = $("#checkNote").val();
	if(checkNote.length <= 0){
		ealert("请输入拒绝理由！");
		return false;
	}else{
		return true;
	}
}