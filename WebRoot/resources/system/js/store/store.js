$(document).ready(function(){
	$("input[type='checkbox']").each(function(){
		$(this).click(function(){
			var ids = "";
			$("input[type='checkbox']").each(function(){
				if($(this).is(":checked")){
					ids += "," + $(this).val();
				}
			});
			$("input[name='store.areaId']").val(ids == "" ? "" : ids.substring(1));
		});
	});
});

// 清除
function reset(){
	window.location.reload();
}

// 返回列表
function goBack(type){
	window.location.href = $("#ctxpath").val() + "/system/store/newlist/" + type;
}

// 用户名
function checkUtils1(){
	var regexp1 = /^(1[0-9][0-9])\d{8}$/;
	//var regexp2 = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	var regexp3=/^[A-Za-zd]+([-_.][A-Za-zd]+)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/; 
	var vals = $.trim($("input[name='user.name']").val());
	
	if(vals == ""){
		ealert("请输入帐号名称");
		return false;
	}
	if(regexp1.test(vals) ||regexp3.test(vals)){
		return true;
	}else{
		ealert("帐号名称输入错误，必须是手机号或邮箱号");
		return false;
	}
}

// 密码格式
function checkUtils2(){
	var vals = $("input[name='user.password']").val();
	
	if(vals == ""){
		ealert("请输入密码");
		return false;
	}
	if(vals.length < 6 || vals.length > 32){
		ealert("密码为6-32位长度组成！");
		return false;
	}
	return true;
}

// 密码是否一致
function checkUtils3(){
	var val = $("input[name='user.password']").val();
	var vals = $("#pwd").val();
	
	if(vals == ""){
		ealert("请输入确认密码！");
		return false;
	}
	if(val != vals){
		ealert("两次输入不一致！");
		return false;
	}
	return true;
}

// 验证企业名称
function checkUtils4(){
	var val = $("input[name='store.storeName']").val();
	
	if($.trim(val) == ""){
		ealert("请输入企业名称！");
		return false;
	}
	return true;
}

// 保存企业
function save(){
	var notes1= CKEDITOR.instances.notes1.getData();
	$("#notes").val(notes1);
	
	
	var ctx = $("#ctxpath").val();
	var name = $.trim($("input[name='user.name']").val());
	var storeName = $.trim($("input[name='store.storeName']").val());
	
	if(checkUtils1() && checkUtils2() && checkUtils3() && checkUtils4()){
		$.post(ctx + "/system/user/newCheckUserName", {name:name}, function(data){
			if(data == true || data == "true"){
				ealert("此账户已存在！");
			} else {
				$.post(ctx + "/system/store/checkStoreName", {storeName:storeName}, function(datas){
					if(datas == true || datas == "true"){
						ealert("此企业名称已存在！");
					} else {
						$("#addStoreForm").ajaxSubmit(function(data){
							if(data == 1){
								ealert("保存成功");
							}else if(data == 2){
								ealert("保存失败");
							}else if(data == 3){
								ealert("请上传企业LOGO");
							}else if(data == 4){
								ealert("请上传营业执照");
							}
						});
					}
				});
			}
		});
	}
}

//修改企业
function update(){
	var notes1= CKEDITOR.instances.notes1.getData();
	$("#notes").val(notes1);
	
	
	var ctx = $("#ctxpath").val();
	// old
	var oldName = $.trim($("#oldUserName").val());
	var oldStoreName = $.trim($("#oldStoreName").val());
	// new
	var newName = $.trim($("input[name='user.name']").val());
	var newStoreName = $.trim($("input[name='store.storeName']").val());
	
	if(checkUtils1() && checkUtils2() && checkUtils3() && checkUtils4()){
		$.post(ctx + "/system/user/checkUserNameUpdate", {oldName:oldName, newName:newName}, function(data){
			if(data == true || data == "true"){
				ealert("此账户已存在！");
			} else {
				$.post(ctx + "/system/store/checkStoreNameUpdate", {oldStoreName:oldStoreName, newStoreName:newStoreName}, function(datas){
					if(datas == true || datas == "true"){
						ealert("此企业名称已存在！");
					} else {
						$("#addStoreForm").ajaxSubmit(function(data){
							if(data == 1){
								ealert("保存成功");
							}else if(data == 2){
								ealert("保存失败");
							}else if(data == 3){
								ealert("请上传企业LOGO");
							}else if(data == 4){
								ealert("请上传营业执照");
							}
						});
					}
				});
			}
		});
	}
}



