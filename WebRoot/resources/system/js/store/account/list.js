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
});

// 返回
function fanhui(){
	var type = $("input[name='type']").val();
	window.location.href = $("#ctxpath").val() + "/system/store/newlist/" + type;
}

//重置
function reset(){
	$("input[type='text']").each(function(){
		$(this).val("");
	});
}

// 分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	$("#searchStoreForm").submit();
}

// 搜索
function searchSubmit(){
	$("#searchStoreForm").submit();
}

// 点击新增按钮，跳转到新增会员页面
function storeAdd(storeId){
	var storeId = $("input[name='storeId']").val();
	var type = $("input[name='type']").val();
	window.location.href = $("#ctxpath").val() + "/system/store/account/add?storeId=" + storeId + "&type=" + type;
}

// 点击修改按钮，根据id进行会员资料查询
function storeModify(id){
	var type = $("input[name='type']").val();
	window.location.href = $("#ctxpath").val() + "/system/store/account/edit/" + id + "/" + type + "/1";
}

// 查询详情
function queryId(id){
	var type = $("input[name='type']").val();
	window.location.href = $("#ctxpath").val() + "/system/store/account/edit/" + id + "/" + type + "/2";
}

function singleDetele(id){
	econfirm('确定要删除此企业吗？', storeDelete, [id], null, null);
}

// 删除会员
function storeDelete(ids){
	var ctxpath = $("#ctxpath").val();
	$.post(ctxpath + "/system/store/account/del", {delId:ids}, function(data){
		if(data){
			ealert("删除成功！");
			var ida = ids.split("-");
			for(var i = 0; i < ida.length; i++){
				$("#del"+ida[i]).remove();
			}
		}else{
			ealert("删除失败！");
		}
	});
}

// 全选和取消全选
function selectAll(){
	var index = 0;
	$("input[type='checkbox']").each(function(){
		if($(this).is(":checked")){
			++index;
		}
	});
	var length = $("input[type='checkbox']").length;
	if(index == length){
		$("input[type='checkbox']").each(function(){
			$(this).attr("checked", false);
		});
	}else{
		$("input[type='checkbox']").each(function(){
			$(this).attr("checked", true);
		});
	}
}

// 删除前验证
function checkSelect(){
	var selected = false;
	var ids = "";
	$("input[type='checkbox']").each(function(){
		if($(this).is(":checked")){
			selected = true;
			return false; //跳出循环
		}
	});
	$("input[type='checkbox']").each(function(){
		if($(this).is(":checked")){
			ids += "-" + $(this).val();
		}
	});
	
	if(selected) {
		ids = ids.substring(1);
		econfirm('是否确认删除？', storeDelete, [ids], null, null);
	}else {
		ealert("请至少选择一条数据！");
	}
}









