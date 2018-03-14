$(document).ready(function(){
	$(".ht_btn").next().addClass("afterHt");
});

function checkMailFocus(){
	$("input[name='email']").parent().removeAttr("class");
}

function checkPhoneFocus(){
	$("input[name='contactPhone']").parent().removeAttr("class");
}

function checkNameFocus(){
	$("input[name='orgName']").parent().removeAttr("class");
}
function checkManFocus(){
	$("input[name='contactMan']").parent().removeAttr("class");
}

function checkNameBlur(){
	var obj = $("input[name='orgName']");
	var name = $.trim($(obj).val());
	
	if(name == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("机构名称不能为空");
		return false;
	}
//	if(name.length < 4 || name.length > 20){
//		$(obj).parent().attr("class", "text_c_ts");
//		$(obj).next().html("资质名称4-20字符组成");
//		return false;
//	}
	return true;
}

function checkManBlur(){
	var obj = $("input[name='contactMan']");
	var name = $.trim($(obj).val());
	
	if(name == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("联系人不能为空");
		return false;
	}
	return true;
}

function checkPhoneBlur(){
	var obj = $("input[name='contactPhone']");
	var name = $.trim($(obj).val());
	var reg = /^(1[0-9][0-9])\d{8}$/;
	if(name == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("联系人电话不能为空");
		return false;
	}
	return true;
}
function checkMailBlur(){
	var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	var obj = $("input[name='email']");
	var name = $.trim($(obj).val());
	
	if(name == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("联系人邮箱不能为空");
		return false;
	}else if(!reg.test(name)){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("联系人邮箱格式不正确");
		return false;
	}
	return true;
}

// 提交
function formSubmit(){
	if(checkNameBlur() && checkMailBlur() && checkPhoneBlur() && checkManBlur()){
    	$("#coUpdateForm").ajaxSubmit(function(data){
    		if(data == true){
    			econfirm('修改成功，是否继续修改？', null, null, goBack, null);
    		}else{
    			ealert("保存失败");
    		}
    	});
    }
}
// 重置
function reset(){
	document.getElementById("coAddFrom").reset();
}

// 返回列表
function goBack(){
	window.location.href = $("#ctxpath").val() + "/system/cooperation/list";
}	
