
function myreset(){
	document.getElementById("addUserAddressForm").reset();
}

function setDefaultAddress(id){
	econfirm('确定要设置此地址为默认？', newSetDefaultAddress, [id], null, null);
}

function myupreset(){
	window.location.href = $("#ctxpath").val() + "/store/address/deliveryAddress";
}

function newSetDefaultAddress(id){
	var ctxpath = $("#ctxpath").val();
	var udstatus = $("#udstatus").val();
	
	$.post(ctxpath + "/store/address/setDefaultAddress/" + id, {udstatus:udstatus}, function(data){
		if(data == true || data == "true"){
			salertWithFunction("设置成功!", myupreset, null);
		}else{
			falert("设置失败!");
		}		
	});
}

function deleteUserAddress(id){
	var udstatus = $("#udstatus").val();
	if(id == udstatus){
		falert("不能删除默认地址!");
	}else{
		econfirm('是否确认删除？', newDeleteUserAddress, [id], null, null);
	}
}

function newDeleteUserAddress(id){
	var ctxpath = $("#ctxpath").val();
	var udstatus = $("#udstatus").val();
	
	$.post(ctxpath + "/store/address/delete/" + id, {udstatus:udstatus}, function(data){
		// 1:成功，2:失败，3:不能删除默认地址
		if(data == 1){
			salertWithFunction("删除成功!", myupreset, null);
		}else if(data == 2){
			falert("删除失败!");
		}else if(data == 3){
			falert("不能删除默认地址!");
		}	
	});
}

function updateUserAddress(){
	if(checknameBlur() && checkzipcodeBlur() && checkmobileBlur() && checkaddressBlur()){
		var area = $.trim($("input[name='area']").val());
		var obj = $("#areaid");
		
		if(area == ""){
			$(obj).css("display", "block");
			$(obj).html("请选择省市区");
			return;
		}
		$("#addUserAddressForm").ajaxSubmit(function(data){
			if(data == true || data == "true"){
				econfirm("修改成功，是否继续修改？", null, null, myupreset, null);
			}else{
				falert("添加失败");
			}
		});
	}
}

// ==============================================
function checkFocusUtil(obj){
	$(obj).next().css("display", "none");
}

function checknameBlur(){
	var name = $.trim($("input[name='name']").val());
	var obj = $("#name");
	
	if(name == ""){
		$(obj).css("display", "block");
		$(obj).html("请输入收货人姓名");
		return false;
	}
	return true;
}

function checkzipcodeBlur(){
	var regexp = /^[1-9][0-9]{5}$/;
	var zipcode = $.trim($("input[name='zipcode']").val());
	var obj = $("#zipcode");
	
	if(zipcode == ""){
		$(obj).css("display", "block");
		$(obj).html("请输入邮政编码");
		return false;
	}
	if(!regexp.test(zipcode)){
		$(obj).css("display", "block");
		$(obj).html("请输入正确邮政编码");
		return false;
	}
	return true;
}

function checkaddressBlur(){
	var address = $.trim($("textarea[name='address']").val());
	var obj = $("#address");
	
	if(address == ""){
		$(obj).css("display", "block");
		$(obj).html("请输入街道地址");
		return false;
	}
	return true;
}

function checkmobileBlur(){
	var regexp1 = /^\d+$/;
	var phone1 = $.trim($("input[name='phone1']").val());
	var phone2 = $.trim($("input[name='phone2']").val());
	var phone3 = $.trim($("input[name='phone3']").val());
	var phone = $.trim(phone1 + phone2 + phone3);
	
	var regexp = /^1\d{10}$/;
	var mobile = $.trim($("input[name='mobile']").val());
	var obj = $("#mobile");
	
	if(mobile == "" && phone == ""){
		$(obj).css("display", "block");
		$(obj).html("手机号码和电话至少填写一项！");
		return false;
	}
	if(mobile != "" && phone == ""){
		if(!regexp.test(mobile)){
			$(obj).css("display", "block");
			$(obj).html("输入有误");
			return false;
		}
	}
	if(mobile == "" && phone != ""){
		if(!regexp1.test(phone)){
			$(obj).css("display", "block");
			$(obj).html("输入有误");
			return false;
		}
	}
	if(mobile != "" && phone != ""){
		if(!regexp1.test(phone) || !regexp.test(mobile)){
			$(obj).css("display", "block");
			$(obj).html("输入有误");
			return false;
		}
	}
	return true;
}


function addUserAddress(){
	if(checknameBlur() && checkzipcodeBlur() && checkmobileBlur() && checkaddressBlur()){
		var area = $.trim($("input[name='area']").val());
		var obj = $("#areaid");
		
		if(area == ""){
			$(obj).css("display", "block");
			$(obj).html("请选择省市区");
			return;
		}
		$("#addUserAddressForm").ajaxSubmit(function(data){
			if(data == true || data == "true"){
				salertWithFunction("添加成功!", myupreset, null);
			}else{
				falert("添加失败");
			}
		});
	}
}

