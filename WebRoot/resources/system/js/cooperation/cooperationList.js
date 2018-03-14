$(function(){
	$(".ht_list tr").mouseover(function() {
		$(this).addClass("tr_bg")
	});
	$(".ht_list tr").mouseout(function() {
		$(this).removeClass("tr_bg")
	});

	/* 查询条件展开和收起 */
	$(".pro_ss dt span.span_up").click(function() {
		$(this).hide();
		$(this).prev(".span_down").show();
		$(this).parent().nextAll("dd").hide();
		$(this).parent().parent().next(".pro_ss_btn").hide();
	});
	$(".pro_ss dt span.span_down").click(function() {
		$(this).hide();
		$(this).next(".span_up").show();
		$(this).parent().nextAll("dd").show();
		$(this).parent().parent().next(".pro_ss_btn").show();
	});
});

// 搜索
function searchSubmit(){
	document.getElementById("searchForm").submit();
}

// 重置
function resetForm(){
	$("input[name='orgName']").val("");
	$("input[name='contactMan']").val("");
	$("input[name='contactPhone']").val("");
	document.getElementsByName("type")[0].checked = true;
}

// 新增
function coAdd(){
	window.location.href = $("#ctxpath").val() + "/system/cooperation/add";
}

// 分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchRoleForm").submit();
}

// 修改
function coModify(id){
	window.location.href = $("#ctxpath").val() + "/system/cooperation/jumpToModify/" + id ;
}

// 查看
function queryId(id){
	window.location.href = $("#ctxpath").val() + "/system/cooperation/query/" + id ;
}

function deleteEconfirm(id){
	econfirm('确定要删除此机构吗？', coDelete, [id], null, null);
}

function coDelete(id){
	var ctx = $("#ctxpath").val();
	$.post(ctx + "/system/cooperation/delete", {ids:id}, function(data){
		if(data == true || data == "true"){
			ealert("删除成功！");
			if(id.indexOf(",") > 0){
				var idArray = id.split(",");
				for(var i=0; i<idArray.length; i++){
					var idstr = "#del" + idArray[i];
					$(idstr).remove();
				}
			}else{
				var idstr = "#del" + id;
				$(idstr).remove();
			}
		}else{
			ealert("删除失败！");
		}
	});
}

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

function checkSelect(){
	var idstr = "";
	$("input[type='checkbox']").each(function(i){
		if($(this).is(":checked")){
			idstr += "," + $(this).val();
		}
	});
	
	if(idstr == ""){
		ealert("请至少选择一条数据！");
	}else{
		idstr = idstr.substring(1);
		econfirm('是否确认删除？', coDelete, [idstr], null, null);
	}
}
