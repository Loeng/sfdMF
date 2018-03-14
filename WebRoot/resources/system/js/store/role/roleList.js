$(document).ready(function() {
	$(".ht_list tr").mouseover(function() {
		$(this).addClass("tr_bg");
	});
	$(".ht_list tr").mouseout(function() {
		$(this).removeClass("tr_bg");
	});
	$(".pro_ss dt span.span_down").click(function() {
		$(this).hide();
		$(this).next(".span_up").show();
		$(this).parent().nextAll("dd").hide();
		$(this).parent().parent().next(".pro_ss_btn").hide();
	});
	$(".pro_ss dt span.span_up").click(function() {
		$(this).hide();
		$(this).prev(".span_down").show();
		$(this).parent().nextAll("dd").show();
		$(this).parent().parent().next(".pro_ss_btn").show();
	});
});

// 全选与全不选
function selectAll() {
	var items = document.getElementsByTagName("input");
	// 定义是否已经全选
	var seleced = true;
	// 遍历选择框，看是否已经全选
	for ( var i = 0; i < items.length; i++) {
		if (items[i].type == "checkbox") {
			if (!items[i].checked) {
				seleced = false;
				break;
			}
		}
	}
	// 如果已经全选，则全取消，否则全选
	if (seleced) {
		for ( var i = 0; i < items.length; i++) {
			if (items[i].type == "checkbox") {
				items[i].checked = false;
			}
		}
	} else {
		for ( var i = 0; i < items.length; i++) {
			if (items[i].type == "checkbox") {
				items[i].checked = true;
			}
		}
	}

}
// 删除选择的记录
function deleteAll(contextPath) {
	var items = document.getElementsByTagName("input");
	for ( var i = 0; i < items.length; i++) {
		if (items[i].type == "checkbox" && items[i].checked) {
			roleDelete1(items[i].value, contextPath);
		}
	}
	ealert("删除成功");
}
// 用于批量删除用户时的删除方法(避免不断弹出删除成功的提示)
function roleDelete1(id, contextPath) {
	$.post(contextPath + "/system/store/role/delete/" + id, function(data) {
		if (data) {
			var d = "#del" + id;
			$(d).parent().parent().remove();
			var oTable = document.getElementById("roleTab");
			for ( var i = 1; i < oTable.rows.length; i++) {
				oTable.rows[i].cells[1].innerHTML = (i);
			}
		}
	});

}
// 删除用户
function roleDelete(id, contextPath) {
	$.post(contextPath + "/system/store/role/delete/" + id, function(data) {
		if (data) {
			ealert("删除成功！");
			var d = "#del" + id;
			$(d).parent().parent().remove();
			var oTable = document.getElementById("roleTab");
			for ( var i = 1; i < oTable.rows.length; i++) {
				oTable.rows[i].cells[1].innerHTML = (i);
			}
		} else {
			ealert("删除失败！");
		}
	});
}
// 点击新增按钮，跳转到新增页面
function roleAdd(contextPath) {
	window.location.href = contextPath + "/system/store/role/add";
}
// 点击修改按钮，根据id进行用户资料查询
function roleModify(id, contextPath) {
	window.location.href = contextPath + "/system/store/role/detail/" + id;
}
// searchRoleForm的提交
function searchSubmit() {
	document.getElementById("searchRoleForm").submit();
}

// 分页跳转
function goPage(pageNum) {
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchRoleForm").submit();
}

// 重置
function resetForm() {
	document.getElementsByName("name")[0].value = "";
}

// 删除前验证
function checkSelect() {
	var selected = false;
	var items = document.getElementsByTagName("input");
	for ( var i = 0; i < items.length; i++) {
		if (items[i].type == "checkbox" && items[i].checked) {
			selected = true;
		}
	}

	if (selected) {
		econfirm('是否确认删除？', deleteAll, [ $("#ctxpath").val() ], null, null);
	} else {
		ealert("请至少选择一条数据！");
	}
}

// 根据id查询出详细信息
function queryId(id, contextPath) {
	$('#div1').load($("#ctxpath").val() + "/system/store/role/query/" + id, function() {
		
	});
	$(".apPreview").show();
	$(".apPreviewBg").show();
}

function apClose() {
	$(".apPreview").hide();
	$(".apPreviewBg").hide();
}

// 弹层拖动
function drag(a, b) { // a为要拖动的层的ID，b为要点击相应拖动的层的ID
	var oWin = document.getElementById(a);
	var oH2 = document.getElementById(b);
	var bDrag = false;
	var disX = disY = 0;
	oH2.onmousedown = function(event) {
		var event = event || window.event;
		bDrag = true;
		disX = event.clientX - oWin.offsetLeft;
		disY = event.clientY - oWin.offsetTop;
		this.setCapture && this.setCapture();
		return false
	};
	document.onmousemove = function(event) {
		if (!bDrag)
			return;
		var event = event || window.event;
		var iL = event.clientX - disX;
		var iT = event.clientY - disY;
		var maxL = document.documentElement.clientWidth - oWin.offsetWidth;
		var maxT = document.documentElement.clientHeight - oWin.offsetHeight;
		iL = iL < 0 ? 0 : iL;
		iL = iL > maxL ? maxL : iL;
		iT = iT < 0 ? 0 : iT;
		iT = iT > maxT ? maxT : iT;

		oWin.style.marginTop = oWin.style.marginLeft = 0;
		oWin.style.left = iL + "px";
		oWin.style.top = iT + "px";
		return false
	};
	document.onmouseup = window.onblur = oH2.onlosecapture = function() {
		bDrag = false;
		oH2.releaseCapture && oH2.releaseCapture();
	};
}

