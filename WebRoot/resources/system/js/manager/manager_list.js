$(function(){
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg")
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg")
	});
	
	/*查询条件展开和收起*/
	$(".pro_ss dt span.span_up").click(function(){
		$(this).hide();
		$(this).prev(".span_down").show();
		$(this).parent().nextAll("dd").hide();
		$(this).parent().parent().next(".pro_ss_btn").hide();
	});
	$(".pro_ss dt span.span_down").click(function(){
		$(this).hide();
		$(this).next(".span_up").show();
		$(this).parent().nextAll("dd").show();
		$(this).parent().parent().next(".pro_ss_btn").show();
	});


});	

// 分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchManagerForm").submit();
}

// searchManagerForm的提交
function searchSubmit(){
	document.getElementById("searchManagerForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("status")[0].checked = true;
	document.getElementsByName("name")[0].value = "";
	document.getElementsByName("roleId")[0].value = "";
	document.getElementsByName("mobile")[0].value = "";
	document.getElementsByName("email")[0].value = "";
}

// 跳转到新增页面
function managerAdd(){
	window.location.href = $("#ctxpath").val() + "/system/manager/add";
}

// 跳转到修改页面
function managerModify(id){
	window.location.href = $("#ctxpath").val() + "/system/manager/detail/" + id;
}

// 查询详细信息
function queryId(id){
	var ctx = $("#ctxpath").val();
	$("#apPreview").load(ctx + "/system/manager/query/" + id, function(){ 
		$("#apPreview").show();
		$(".apPreviewBg").show();
	});
}

// 关闭弹窗
function eyeClose(){
	$("#apPreview").hide();
	$(".apPreviewBg").hide();
}



// 全选与全不选
function selectAll(){
	var index = 0;
	$("input[type='checkbox']").each(function(i){
		if(i == 0 && $(this).is(":checked")){
			index = 1;
		}
		if(index == 1){
			$(this).attr("checked", false);
		}else{
			$(this).attr("checked", true);
		}
	});
}

//删除前验证
function checkSelect(){
	var idstr = "";
	$("input[type='checkbox']").each(function(i){
		if($(this).is(":checked")){
			idstr += "," + $(this).val();
		}
	});
	
	if(idstr == ""){
		ealert("请至少选择一条数据！");
	}else {
		idstr = idstr.substring(1);
		econfirm('是否确认删除？', deleteAll, [idstr], null, null);
	}
}

//删除选择的记录
function deleteAll(ids){
	var ctx = $("#ctxpath").val();
	$.post(ctx + "/system/manager/delete", {ids:ids}, function(data){
		if(data == true || data == "true"){
			ealert("删除成功！");
			if(ids.indexOf(",") > 0){
				var idArray = ids.split(",");
				for(var i=0; i<idArray.length; i++){
					var idstr = "#del" + idArray[i];
					$(idstr).remove();
				}
			}else{
				$("#del" + ids).remove();
			}
		}else{
			ealert("删除失败！");
		}
	});
}







