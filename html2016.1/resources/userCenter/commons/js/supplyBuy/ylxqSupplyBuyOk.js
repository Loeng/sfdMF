
function formSubmit(){
	if(checkTitleBlur()&&checkContentNameBlur()&&checkContentNameBlur()){
		$("#updateSupplyBuyForm").ajaxSubmit(
				function(data){
				if(data==true || data=="true"){
					salert("提交成功")
				}else{
					falert("修改失败");
				}
			});
	}else{
		checkTitleBlur();
		checkContentNameBlur();
		checkContentNameBlur();
	}
}
function reset(){
	document.getElementById("updateSupplyBuyForm").reset();
}
//返回列表
function goBack(contextPath){
	var productType = $("#productType").val();
	var type = $("#type").val();
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/store/supplyBuy/ylxqList/"+productType+"/"+type;
	}
	window.location.href=contextPath + "/store/supplyBuy/ylxqList/"+productType+"/"+type;
}

function checkTitleBlur(){
	var title = $.trim($("input[name='title']").val());
	if(title == ""){
		$('#titleSpan').show();
		$('#titleSpan').text("请输入供求标题");
		return false;
	}
	$('#titleSpan').hide();
	return true;
}
function checkContentNameBlur(){
	var contactName = $.trim($("input[name='contactName']").val());
	
	if(contactName == ""){
		$('#contactNameSpan').show();
		$('#contactNameSpan').text("请输入联系人");
		return false;
	}
	$('#contactNameSpan').hide();
	return true;
}
function checkContentPhoneBlur(){
	var contactPhone = $.trim($("input[name='contactPhone']").val());
	var regexp = /^(1[0-9][0-9])\d{8}$/;
	
	if(contactPhone == ""){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入联系电话");
		return false;
	}
	if(!regexp.test(contactPhone)){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入正确的联系电话");
		return false;
	}
	$('#contactPhoneSpan').hide();
	return true;
}