function utilSuccess(){
	window.location.reload();
}

function storeBasicZhiLiao(){
	$("#saveBasisInfoSubmit").ajaxSubmit(function(data){
		// 1：成功，2：失败，3：上传企业LOGO，4：上传营业执照，5：企业名称不能为空
		if(data == 1){
			salertWithFunction("提交成功", utilSuccess, null);
		}else if(data == 2){
			falert("提交失败");
		}else if(data == 3){
			falert("请上传企业LOGO");
		}else if(data == 4){
			falert("请上传营业执照");
		}else if(data == 5){
			falert("请输入企业名称");
		}else if(data == 6){
			falert("请上传信用机构代码证");
		}else if(data == 7){
			falert("请上传公司章程");
		}else if(data == 8){
			falert("请上传企业简介(WORD版)");
		}
	});
}

function storeBasisSubmit(){
	var ctx = $("#ctxpath").val();
	var old = $.trim($("#tempStoreName").val());
	var nez = $.trim($("input[name='storeName']").val());
	if(nez == ""){
		falert("请输入企业名称");
		return;
	}
	if(checkzipCode() && checkcontactPhone()){
		if(old != nez){
			$.post(ctx + "/store/auth/checkCenterStoreName",{old:old,nez:nez},function(data){
				if(data == true || data == "true"){
					falert("企业名称已存在");
				}else{
					storeBasicZhiLiao();
				}
			});
		}else{
			storeBasicZhiLiao();
		}
	}
}

function checkzipCode(){
	var regexp = /^[1-9][0-9]{5}$/;
	var val = $.trim($("input[name='zipCode']").val());
	
	if(val != "" && !regexp.test(val)){
		falert("请输入正确的邮政编码");
		return false;
	}
	return true;
}

function checkcontactPhone(){
	var regexp = /^1\d{10}$/;
	var val = $.trim($("input[name='contactPhone']").val());
	
	if(val != "" && !regexp.test(val)){
		falert("请输入正确的联系电话");
		return false;
	}
	return true;
}
