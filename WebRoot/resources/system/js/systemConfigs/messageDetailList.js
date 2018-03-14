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

//自动生成序号
window.onload = function(){
	var oTable = document.getElementById("configDetailTab");
	for(var i=1;i<oTable.rows.length;i++){
		oTable.rows[i].cells[0].innerHTML = (i);
	}
};

//点击修改按钮，根据id进行频道资料查询
function configDetailModify(id,contextPath){
	window.location.href = contextPath + "/system/message/detail/"+id;
}

//searchDetailForm的提交
function searchSubmit(){
	document.getElementById("searchDetailForm").submit();
}

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchDetailForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("name")[0].value="";
	document.getElementsByName("id")[0].value="";
}


function eyeClose(){
	$(".apPreview").hide();
	$(".apPreviewBg").hide();
}

function eyeDetail(id,contextPath){
	 $(".apPreview").load(contextPath + "/system/message/query/"+id, function(){ 
		    $(".apPreview").show();
			$(".apPreviewBg").show();
			
	 });
}

