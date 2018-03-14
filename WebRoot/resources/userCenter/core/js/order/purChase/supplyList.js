
//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchForm").submit();
}

//searchUserForm的提交
function searchSubmit(){
	$("#searchForm").submit();
}
  // 重置
function resetForm(){
	document.getElementsByName("proName")[0].value="";
}

function loadQuery(id){
	$("#apContent").load($("#ctxpath").val()+"/supple/load/"+id,function(){
		var count=$(".apBargain .stair li").length;
		for(i=4;i<count;i++){
			$(".apBargain .stair li").eq(i).hide();
		}
		$(".readAll").toggle(function(){
			$(".apBargain .stair li").show(200);
			$(this).addClass("allDown");
		},function(){
			for(i=4;i<count;i++){
				$(".apBargain .stair li").eq(i).hide(200);
			}
			$(this).removeClass("allDown");
		})
	});
	$("#apLayerBg").show();
	$("#apLayer").show();
}

//关闭弹出层
function closeLoad(){
	$("#apLayerBg").hide();
	$("#apLayer").hide();
}

