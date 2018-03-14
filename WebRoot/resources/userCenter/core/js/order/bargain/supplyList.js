
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
	}else if((reg.test(minPrice)&&reg.test(maxPrice))||(reg.test(minPrice)&&$.trim(maxPrice) == "") || (reg.test(maxPrice)&&$.trim(minPrice) == "")){
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
function check(){
	var reg =/^(0|[1-9][0-9]*)$/;
	var re =/^[0-9]+(.[0-9]{2})?$/;
	if($("#sellersNumber").val()==''){
		falert("核定数量不能为空");
		return false;
	}else if($("#FinalPrice").val()==''){
		falert("核定价格不能为空");
		return false;
	}else if($("#updateTime").val()==''){
		falert("议价下单截止时间不能为空");
		return false;
	}else if(!reg.test($("#sellersNumber").val())){
		falert("请填写正确核定数量");
		return false;
	}else if(!re.test($("#FinalPrice").val())){
		falert("请填写正确核定价格");
		return false;
	}else{
		return true;
	}
}
function loadQuery(id,type){
	
	$("#apContent").load($("#ctxpath").val()+"/supple/load/"+id+"/"+type+"/"+$("#userId").val(),function(){
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
//搜索议价表单提交
function forSubmitInquiry(id){
		var sellersNumber = $("input[name='sellersNumber']").val();
		var FinalPrice = $("input[name='FinalPrice']").val();
		var updateTime = $("input[name='updateTime']").val();
		if(check()){
			$.post($("#ctxpath").val()+"/store/supplyPrice/Add",{id:$("#id").val(),sellersNumber:sellersNumber,FinalPrice:FinalPrice,updateTime:updateTime},function(result){
			    if(result == '1'){
			    	econfirm('议价成功，返回议价列表?',goBack,[$("#ctxpath").val()],null,null);
			    	closeLoad();
			    }else{
			    	econfirm('核定议价失败,是否继续议价?',null,null,goBack,[$("#ctxpath").val()]);
			    }
			  });
		}
  }

function goBack(ctxPath){
	window.location.href=ctxPath+"/store/purchase/pricelist";
}
function checkValue (value){
	value = $.trim(value);
	if(value == null || value == ""){
		return false;
	}else{
		return true;
	}
}

function closeSupply(ctx,id){
	econfirm('确定关闭订单?',goCloseSupply,[$("#ctxpath").val(),id],null,null);
}
function goCloseSupply(ctx,id){
	$.post(ctx+"/store/supply/closeSupply/"+id,{id:$("#id").val()},function(result){
		if(result == true || result=='true'){
			salert('关闭订单成功!');
		}else{
			salert('关闭订单失败!');
		}
		$("#apLayerBg").hide();
		$("#apLayer").hide();
	});
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