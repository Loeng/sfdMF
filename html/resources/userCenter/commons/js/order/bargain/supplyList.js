
//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchForm").submit();
}
function checkNum(){
	var minPrice = $('#minPrice').val();
	var maxPrice = $('#maxPrice').val();
	var reg=/^([1-9][\d]{0,9}|0)(\.[\d]{1,2})?$/;
	if($.trim(minPrice) == "" && $.trim(maxPrice) == ""){
		return true;
	}else if(reg.test(minPrice)&&reg.test(maxPrice)){
		return true;
	}else{		
		return false;
	}
}
//searchUserForm的提交
function searchSubmit(){
	if(checkNum()){
		document.getElementById("searchForm").submit();
	}
}
  // 重置
function resetForm(){
	document.getElementsByName("proName")[0].value="";
	document.getElementsByName("minPrice")[0].value="";
	document.getElementsByName("maxPrice")[0].value="";
}

function loadQuery(id,type){
	alert(type);
	/*
	$("#apContent").load($("#ctxpath").val()+"/supple/load/"+id+"/"+type,function(){
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
	*/
}

//关闭弹出层
function closeLoad(){
	$("#apLayerBg").hide();
	$("#apLayer").hide();
}

//提交保存
function submit(){
	var sellersNumber = $('#sellersNumber').val();
	var FinalPrice = $('#FinalPrice').val();
	var id = $('#id').val();
	var ctxpath = $('#ctxpath').val();
	// 发送请求
	document.write("<form action='"+ctxpath+"/store/supply/save' method='post' name='formx1' style='display:none'>");
	document.write("<input type='hidden' name='sellersNumber' value='"+sellersNumber+"' />");
	document.write("<input type='hidden' name='FinalPrice' value='"+FinalPrice+"' />");
	document.write("<input type='hidden' name='id' value='"+id+"' />");
	document.write("</form>");
	document.formx1.submit();
}