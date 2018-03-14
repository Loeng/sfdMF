
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

function loadQuery(id,type){
	$("#apContent").load($("#ctxpath").val()+"/supply/loadGys/"+id+"/"+type,function(){
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

function commitSupply(){
	var number = $("input[name='number']").val();
	var price = $("input[name='price']").val();
	var linkPerson = $("input[name='linkPerson']").val();
	var linkTel = $("input[name='linkTel']").val();
	var productid = $("input[name='productId']").val();
	var note = $("textarea[name='note']").val();
	var ctxpath = $('#ctxpath').val();
	if(!(checkValue(number)&&checkValue(price)&&checkValue(linkPerson)&&checkValue(note))){
		alert('请填写必填项');
		return false;
	}else{
	  $.post(ctxpath+"/store/storeInquiry/Add",
			{number:number,price:price,linkPerson:linkPerson,linkTel:linkTel,productid:productid,note:note},
			function(data){
		    if(data=='1'){
		    	alert('议价发布成功');
		    }else{
		    	alert('议价发布失败,请重新提交议价');
		    }
	  });	
	}
}

function checkValue (value){
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
			salertWithFunction('关闭订单成功!',callback,null);
		}else{
			salertWithFunction('关闭订单失败!',callback,null);
		}
		
		$("#apLayerBg").hide();
		$("#apLayer").hide();
	});
}

function callback(){
	window.location.reload();
}
//关闭弹出层
function closeLoad(){
	$("#apLayerBg").hide();
	$("#apLayer").hide();
}

