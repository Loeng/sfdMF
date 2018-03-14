
$(document).ready(function(){
	//$("input[name='name']").focus();

	var formStatus=$(".loginform").Validform({
		btnSubmit:".btnlogin", 
		tiptype:3, 
		callback:function(data){
			return false;
		}
	});

	$(".btnlogin").click(function(){
		$(".inputBox").blur();
		// 验证为true 则提交
		if(formStatus.check()){
			submitform();
		}
	});
	
	$(".Validform_checktip").live("click",function(){
		$(this).prev(".inputBox").focus();
		$(this).removeClass("Validform_wrong");
		$(this).text("");
	});
	
	$(".inputBox").live("focus",function(){
		//$(this).removeClass("Validform_error");
		$(this).next(".Validform_checktip").removeClass("Validform_wrong").text("");
	});
	
	$(".inputBox").live("blur",function(){

	});
	
	$(".inputBox").change(function(){
		$(this).next(".Validform_checktip").removeClass("Validform_wrong");
		$(this).next(".Validform_checktip").text("");
	})

});
	
	
//按Enter键登陆操作
function keyLogin() {
	if (event.keyCode==13){
		$(".btnlogin").trigger("click");
	}
}


function submitform(){
	var name=$("input[name='name']").val();
	var password=$("input[name='password']").val();
	
	$(".loginform").ajaxSubmit(function(data){
		// 0:禁用，1:启用，2:删除，3:用户名不存在，4:密码错误，5:验证码错误，6:账号未激活, 7:帐号信息需要完善
		if(data == 1){
			window.location.href = $("#ctxpath").val() + "/web/util/jumpZhongPage";
		} else if (data == 7) {
			// 跳转到资料完善页面
			window.location.href = $("#ctxpath").val() + "/web/util/jumpSupplementPage";
		} else{
			errorManager(data);
		}  
	});
	
}

// 换一个验证码操作
function huang(){
	document.getElementById("yzimg").src = $("#ctxpath").val() + "/web/security/image?" + Math.random();
}

function messageError(inObj, meObj, mess) {
	$(inObj).parent().addClass("errorBorder");
	$(meObj).addClass("error");
	$(meObj).html(mess);
}


// 验证验证码
function yanzhengBlur(){
	var inObj = $("input[name='yanzheng']");
	var meObj = $("#yanzheng");
	
	var val = $.trim($(inObj).val());
	if("" == val){
		messageError(inObj, meObj, "请输入验证码");
		return false;
	}
	return true;
}

//提示信息
function errorManager(mark){
	// 0:禁用，1:启用，2:删除，3:用户名不存在，4:密码错误，5:验证码错误，6:账号未激活
	if(mark == 0){
		$("input[name='name']").addClass("Validform_error");
		$("input[name='name']").next(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='name']").next(".Validform_checktip").text("用户名被禁用!");
	}else if(mark == 2){
		$("input[name='name']").addClass("Validform_error");
		$("input[name='name']").next(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='name']").next(".Validform_checktip").text("用户名被删除!");
	}else if(mark == 3){
		$("input[name='name']").addClass("Validform_error");
		$("input[name='name']").next(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='name']").next(".Validform_checktip").text("用户名不存在!");
	}else if(mark == 4){
		$("input[name='password']").addClass("Validform_error");
		$("input[name='password']").next(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='password']").next(".Validform_checktip").text("密码错误!");
	}else if(mark == 5){
		$("input[name='code']").addClass("Validform_error");
		$("input[name='code']").next(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='code']").next(".Validform_checktip").text("验证码错误!");
	}else if(mark == 6){
		$("input[name='name']").addClass("Validform_error");
		$("input[name='name']").next(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='name']").next(".Validform_checktip").text("账号未激活!");
	}
	huang();// 换验证码
}



