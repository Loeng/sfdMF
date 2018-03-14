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

// 分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchSupplyBuyForm").submit();
}

// searchStoreForm的提交
function searchSubmit(){
	document.getElementById("searchSupplyBuyForm").submit();
}

// 点击新增按钮，跳转到新增会员页面
function storeAdd(ind){
	var productType = $("#productType").val();
	var type = $("#type").val();
	window.location.href = $("#ctxpath").val() + "/system/supplyBuy/Add/"+productType+"/"+type;
}

// 重置
function resetForm(){
//	document.getElementsByName("type")[0].checked = true;
	document.getElementsByName("status")[0].checked = true;
	document.getElementsByName("title")[0].value = "";
	document.getElementsByName("beginDate")[0].value = "";
	document.getElementsByName("endDate")[0].value = "";
}

// 点击修改按钮，根据id进行会员资料查询
function storeModify(id){
	window.location.href = $("#ctxpath").val() + "/system/supplyBuy/query/" + id + "/1";
}

function singleDetele(id){
	var ctxpath = $("#ctxpath").val();
	econfirm('确定要关闭此供需吗？', storeDelete, [id], null, null);
}

// 删除会员
function storeDelete(ids){
	var ctxpath = $("#ctxpath").val();
	$.post(ctxpath + "/system/supplyBuy/delete", {ids:ids}, function(data){
		if(data){
			ealert("关闭成功！");
			location.reload();
		}else{
			ealert("关闭失败！");
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
			ids += "," + $(this).val();
		}
	});
	
	if(selected) {
		ids = ids.substring(1);
		econfirm('是否确认关闭？', storeDelete, [ids], null, null);
	}else {
		ealert("请至少选择一条数据！");
	}
}

// 查询详情
function queryId(id){
	window.location.href = $("#ctxpath").val() + "/system/supplyBuy/query/" + id + "/0";
}







