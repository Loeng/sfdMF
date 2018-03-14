
$(document).ready(function(){
	//加载头部和尾部
	$("#channelTop").load($("#ctxpath").val() + "/web/channel/commons/channel-top.jetx",{"channelId":null,"channelType":null,"returnUrl":window.location.href});
	$("#channelFoot").load($("#ctxpath").val() + "/web/channel/commons/channel-footer.jetx");
	
	//实例化验证插件
	var formStatus=$(".regform").Validform({
		btnSubmit:".btnReg", 
		tiptype:3,
		showAllError:true,
		callback:function(data){
			return false;
		}
	});
	

	$(".btnReg").click(function(){
//		$(".inputBox").blur();
		// 验证为true 则提交
		if(formStatus.check()){
			findOnePhone();
		}
	});
	
	$(".Validform_checktip").live("click",function(){
		$(this).prev(".inputBox").focus();
		$(this).removeClass("Validform_wrong");
		$(this).text("");
	});
	
	$(".inputBox").change(function(){
		$(this).next(".Validform_checktip").removeClass("Validform_wrong");
		$(this).next(".Validform_checktip").text("");
	})

	/*验证码获取倒计时*/
	var wait=120;
	function time(o) {
			if (wait == 0) {
				o.removeAttribute("disabled");
				o.setAttribute("class","btnOrange");         
				o.value="获取验证码";
				wait = 120;
			} else {
				o.setAttribute("disabled", true);
				o.setAttribute("class","btnOrange btnForbid");
				o.value=wait + "后重新发送";
				wait--;
				setTimeout(function() {
					time(o)
				},
				1000)
			}
		}
	
	document.getElementById("btnSend").onclick=function(){
		sendYanze();
	}
	
	//发送手机验证码
	function sendYanze(){
		var phone = $.trim($("input[name='mobile']").val());
		// 判断是否已输入手机号码
		if(phone != "" ){
			$.post($("#ctxpath").val() + "/web/findpassword/zhaosendPhoneYan", {phone:phone}, function(data){
				if(data == 1){
					time();
				}
			});
		}else{
			$("input[name='mobile']").addClass("Validform_error");
			$("input[name='mobile']").siblings(".Validform_checktip").addClass("Validform_wrong").show();
			$("input[name='mobile']").siblings(".Validform_checktip").text("请输入手机号码!");
		}
	}
});

	
//按Enter键登陆操作
function keyLogin() {
	if (event.keyCode==13){
		$(".btnReg").trigger("click");
	}
}

function findOnePhone(){
	var name = $.trim($("input[name='mobile']").val());
	$("#phoneSubmitOne").ajaxSubmit(function(data){
		// 1:验证码错误，2:不存在， 3:禁用，4:启用，5:删除
		if(data == 4){
			window.location.href = $("#ctxpath").val() + "/web/findpassword/jumpPasswordPageTwo?name=" + name;
		}else{
//			errorMessage(data, 2);
			errorMsg(data);
		}
	});
}

function errorMsg(data){
	if(data == 2){
		$("input[name='mobile']").addClass("Validform_error");
		$("input[name='mobile']").siblings(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='mobile']").siblings(".Validform_checktip").text("用户名不存在!");
	}else if(data == 3){
		$("input[name='mobile']").addClass("Validform_error");
		$("input[name='mobile']").siblings(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='mobile']").siblings(".Validform_checktip").text("用户名被禁用!");
	}else if(data == 5){
		$("input[name='mobile']").addClass("Validform_error");
		$("input[name='mobile']").siblings(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='mobile']").siblings(".Validform_checktip").text("用户名已被删除!");
	}else if(data == 1){
		$("input[name='verifyCode']").addClass("Validform_error");
		$("input[name='verifyCode']").siblings(".Validform_checktip").addClass("Validform_wrong").show();
		$("input[name='verifyCode']").siblings(".Validform_checktip").text("验证码错误!");
	}
}

function errorMessage(mark, jud){
	var inObj = null;
	var meObj = null;
	if(jud == 1){
		inObj = $("input[name='name']");
		meObj = $("#name");
	}else if(jud == 2){
		inObj = $("input[name='mobile']");
		meObj = $("#mobile");
	}
	
	// 1:验证码错误，2:不存在， 3:禁用，4:启用，5:删除
	if(mark == 2){
		messageError(inObj, meObj, "用户名不存在");
	}else if(mark == 3){
		messageError(inObj, meObj, "用户名禁用");
	}else if(mark == 5){
		messageError(inObj, meObj, "用户名删除");
	}else if(mark == 1){
		var inObj1 = $("input[name='verifyCode']");
		var meObj1 = $("#verifyCode");
		messageError(inObj1, meObj1, "验证码错误");
	}
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




