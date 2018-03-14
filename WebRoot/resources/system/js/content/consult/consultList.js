// 留言审核列表JS
function searchConsult(){
	if(!checkTime()){
		return;
	}
	var ctxpath = $("#ctxpath").val();
	var consultType = $("input[name='consultType']:checked").val();
	if(consultType == undefined){
		consultType = '';
	}
	var beginDate = $("input[name='beginDate']").val();
	var endDate = $("input[name='endDate']").val();
	var content = $("input[name='content']").val();
	var person = $("input[name='person']").val();
	document.write("<form action='"+ctxpath+"/system/consultlist' method='post' name='consultForm' style='display:none;'>");
	document.write("<input name='consultType' value='"+consultType+"'>");
	document.write("<input name='beginDate' value='"+beginDate+"'>");
	document.write("<input name='endDate' value='"+endDate+"'>");
	document.write("<input name='content' value='"+content+"'>");
    document.write("<input name='person' value='"+person+"'>");
	document.write("</form>");
	$("[name='consultForm']").submit();
}


//咨询审核列表JS
function searchAdvisory(){
	if(!checkTime()){
		return;
	}
	var ctxpath = $("#ctxpath").val();
	var consultType = $("input[name='consultType']:checked").val();
	if(consultType == undefined){
		consultType = '';
	}
	var beginDate = $("input[name='beginDate']").val();
	var endDate = $("input[name='endDate']").val();
	var content = $("input[name='content']").val();
	var person = $("input[name='person']").val();
	document.write("<form action='"+ctxpath+"/system/advisorylist' method='post' name='consultForm' style='display:none;'>");
	document.write("<input name='consultType' value='"+consultType+"'>");
	document.write("<input name='beginDate' value='"+beginDate+"'>");
	document.write("<input name='endDate' value='"+endDate+"'>");
	document.write("<input name='content' value='"+content+"'>");
    document.write("<input name='person' value='"+person+"'>");
	document.write("</form>");
	$("[name='consultForm']").submit();
}


// 搜索条件重置
function resetConsult(){
	$("input[name ='consultType']:checked").attr("checked",false);
	$("input[name ='beginDate']")[0].value = '';
	$("input[name ='endDate']")[0].value = '';
	$("input[name ='content']")[0].value = '';
	$("input[name ='person']")[0].value = '';
}
// 分页跳转
function goPage(pageNum){
	var ctxpath = $("#ctxpath").val();
	var consultType = $("input[name='consultType']:checked").val();
	if(consultType == undefined){
		consultType = '';
	}
	var beginDate = $("input[name='beginDate']").val();
	var endDate = $("input[name='endDate']").val();
	var content = $("input[name='content']").val();
	var person = $("input[name='person']").val();
	document.write("<form action='"+ctxpath+"/system/consultlist' method='post' name='consultForm' style='display:none;'>");
	document.write("<input name='consultType' value='"+consultType+"'>");
	document.write("<input name='beginDate' value='"+beginDate+"'>");
	document.write("<input name='endDate' value='"+endDate+"'>");
	document.write("<input name='content' value='"+content+"'>");
    document.write("<input name='person' value='"+person+"'>");
    document.write("<input name='pageNum' value='"+pageNum+"'>");
	document.write("</form>");
	$("[name='consultForm']").submit();
}

// 内容审核
function goToConsultCheck(consultId){
	var ctxpath = $("#ctxpath").val();
	document.write("<form action='"+ctxpath+"/system/goToconsultCheck' method='post' name='consultForm' style='display:none;'>");
	document.write("<input type='hidden' name='consultId' value='"+consultId+"'>");
	document.write("</form>");
	$("[name='consultForm']").submit();
}

// 查看详情
function showDetail(consultId){
	var ctxpath = $("#ctxpath").val();
	$("#apPreview").load(
	ctxpath+"/system/showConsultDetail",
	{'consultId':consultId},
	function(){
		
	});
}

// 时间验证
function checkTime(){
	var beginDate = $("input[name='beginDate']").val();
	var endDate = $("input[name='endDate']").val();
	if(beginDate > endDate){
		ealert("开始时间不能大于结束时间！");
		return false;
	}else{
		return true;
	}
}