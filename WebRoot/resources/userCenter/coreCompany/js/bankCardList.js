
//删除银行卡
function deleteCard(ctx,id,type){
	if(type=='true' || type== true){
		econfirm('该卡已通过银行审核,是否确认删除?如确认删除,可能会出现资金风险,请慎重选择!',deleteBankCard,[ctx,id],null,null);
	}else{
		econfirm('该卡未通过银行审核,是否确认删除?',deleteBankCard,[ctx,id],null,null);
	}
}
//调用后台方法删除
function  deleteBankCard(ctx,id){
	window.location.href=ctx+"/store/account/deleteBnak/"+id+"/"+$("#bankcount").val();
}

//跳转到添加银行卡页面
function add(ctx){
	window.location.href=ctx+"/store/account/bank/add";
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

function update(ctxpath,id){
	window.location.href=ctxpath+"/store/account/bank/update/"+id;
}

function checkValue (value){
	if(value == null || value == ""){
		return false;
	}else{
		return true;
	}
}

//关闭弹出层
function closeLoad(){
	$("#apLayerBg").hide();
	$("#apLayer").hide();
}

