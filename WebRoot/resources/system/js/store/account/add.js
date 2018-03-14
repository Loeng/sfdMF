$(document).ready(function(){
	$("input[type='text']").each(function(){
		$(this).focus(function(){
			$(this).parent().removeAttr("class");
		});
	});
	$("input[type='password']").each(function(){
		$(this).focus(function(){
			$(this).parent().removeAttr("class");
		});
	});
	
	$(".limitItem").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
			$main.next(".limitList").find("input").attr("checked",true);
			$main.next(".limitList").find(".check").addClass("checked");
			$main.parents().prev(".limitItem").find("input").attr("checked",true);
			$main.parents().prev(".limitItem").find(".check").addClass("checked");
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
			$main.next(".limitList").find(":checkbox").attr("checked",false);
			$main.next(".limitList").find(".check").removeClass("checked");
		}
	});
	$(".limitAll").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.nextAll(".limitList").find("input").attr("checked",true);
			$main.nextAll(".limitList").find(".check").addClass("checked");
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.nextAll(".limitList").find(":checkbox").attr("checked",false);
			$main.nextAll(".limitList").find(".check").removeClass("checked")
		}
	});
});

// 清除
function reset(){
	window.location.reload();
}

// 返回列表
function goBack(){
	var storeId = $("input[name='storeId']").val();
	var type = $("input[name='type']").val();
	
	window.location.href = $("#ctxpath").val() + "/system/store/account/list?storeId=" + storeId + "&type=" + type;
}

// 判断邮箱
function checkNameBlur(){
	var obj = $("input[name='name']");
	var regexp = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	var name = $.trim($(obj).val());
	
	if(name == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("请输入邮箱");
		return false;
	}
	if(!regexp.test(name)){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("邮箱格式输入错误");
		return false;
	}
	return true;
}

// 判断密码
function checkPasswordBlur(){
	var obj = $("input[name='password']");
	var password = $.trim($(obj).val());
	
	if(password == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("请输入密码");
		return false;
	}
	if(password.length < 6 || password.length > 32){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("密码为6-32位长度组成！");
		return false;
	}
	return true;
}

// 判断管理员
function checkContactNameBlur(){
	var obj = $("input[name='contactName']");
	var contactName = $.trim($(obj).val());
	
	if(contactName == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("请输入管理员");
		return false;
	}
	return true;
}

// 保存
function formSubmit(){
	var ctx = $("#ctxpath").val();
	var name = $.trim($("input[name='name']").val());
	
	if(checkNameBlur() && checkPasswordBlur() && checkContactNameBlur()){
		$.post(ctx + "/system/store/account/checkName",{id:"",name:name},function(data){
			if(data == true || data == "true"){
				$("#addRoleForm").ajaxSubmit(function(data1){
					if(data1 == true || data1 == "true"){
						ealert("保存成功");
					}else{
						ealert("保存失败");
					}
				});
			}else{
				ealert("此账户已存在！");
			}
		});
	}
}

// 修改
function updateformSubmit(){
	var ctx = $("#ctxpath").val();
	var id = $.trim($("input[name='id']").val());
	var name = $.trim($("input[name='name']").val());
	
	if(checkNameBlur() && checkPasswordBlur() && checkContactNameBlur()){
		$.post(ctx + "/system/store/account/checkName",{id:id,name:name},function(data){
			if(data == true || data == "true"){
				$("#addRoleForm").ajaxSubmit(function(data1){
					if(data1 == true || data1 == "true"){
						ealert("修改成功");
					}else{
						ealert("修改失败");
					}
				});
			}else{
				ealert("此账户已存在！");
			}
		});
	}
}



