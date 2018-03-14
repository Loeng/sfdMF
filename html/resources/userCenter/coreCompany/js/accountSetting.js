$(document).ready(function() {
	
	$(window).ready(function() {
       load($("#userPassword").val());
      
       var email = $("#userEmail").val();
       var phone = $("#userPhone").val();
       var card = $("#userisAssociatedBank").val();
       if(email!="" && phone!=""){
    	   $("#all").html("<em class=\"intensionLev3\"></em>");
    	   $("#jibie").html("<dt>您的账号当前安全级别为<span class=\"orange\">高级</span>");
       }else if(email!="" || phone!=""){
    	   $("#all").html("<em class=\"intensionLev2\"></em>");
    	   $("#jibie").html("<dt>您的账号当前安全级别为<span class=\"orange\">中级</span>，您可以通过以下方式提高等级</dt>");
       }else{
    	   $("#id").html("<em class=\"intensionLev1\"></em>");
    	   $("#jibie").html("<dt>您的账号当前安全级别为<span class=\"orange\">低级</span>，您可以通过以下方式提高等级</dt>");
       }
       if(email!=""){
    	   $("#yx").html(" <span class=\"intensionLev3\"></span><span class=\"intensionText\">已开通</span>");
       }else{
    	   $("#yx").html(" <span class=\"intensionLev2\"></span><span class=\"intensionText\">未开通</span>");
       }
       if(phone!=""){
    	   $("#sj").html(" <span class=\"intensionLev3\"></span><span class=\"intensionText\">已开通</span>");
       }else{
    	   $("#sj").html(" <span class=\"intensionLev2\"></span><span class=\"intensionText\">未开通</span>");
       }
       if(card ==  true){
    	   $("#yh").html(" <span class=\"intensionLev3\"></span><span class=\"intensionText\">已开通</span>");
       }else{
    	   $("#yh").html(" <span class=\"intensionLev2\"></span><span class=\"intensionText\">未开通</span>");
       }
       
       
    });
	/*验证密码************************************************/
	/*验证原密码*/
	$("#oldPwd").blur(function(){
		if($("#oldPwd").val().replace(/\s/g,"")=='' || $("#oldPwd").val().replace(/\s/g,"")== null){
			$("#oldPwd").after("<div id =\"ypassword\" class=\"errorText\">原登录密码不能为空</div>");
		}
	});
	$("#oldPwd").focus(function(){
		$("#ypassword").remove();
	});
	/*验证是否输入新密码*/
	$("#newPwd").blur(function(){
		if($("#newPwd").val().replace(/\s/g,"")=='' || $("#newPwd").val().replace(/\s/g,"")== null){
			$("#newPwd").after(" <span id =\"xpassword\" class=\"errorText\">新登录密码不能为空</span>");
		}else if($("#newPwd").val().length < 6){
			$("#xpassword").remove();
			$("#newPwd").after(" <span id =\"xpassword\" class=\"errorText\">新登录密码不能少于6位</span>");
		}
	});
	$("#newPwd").focus(function(){
		$("#xpassword").remove();
	});
	/*验证第二次输入密码是否匹配*/
	$("#newPwdTwo").blur(function(){
		if($("#newPwdTwo").val().replace(/\s/g,"")=='' || $("#newPwdTwo").val().replace(/\s/g,"")== null){
			$("#newPwdTwo").after(" <span id =\"yzpassword\" class=\"errorText\">再次密码不能为空</span>");
		}else {
			$("#yzpassword").remove();
			if($("#newPwd").val() != $("#newPwdTwo").val())
			$("#newPwdTwo").after(" <span id =\"xpassword\" class=\"errorText\">再次密码不一致</span>");
		}
	});
	$("#newPwdTwo").focus(function(){
		$("#yzpassword").remove();
	});
	/*验证邮箱************************************************/
	$("#oldEmail").blur(function(){
		if($("#oldEmail").val().replace(/\s/g,"")=="" || $("#oldEmail").val().replace(/\s/g,"")== undefined ){
			
		}else{
			var reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			if(!reg.test($("#oldEmail").val())){
				$("#oldEmail").after("<span id =\"oldemail\" class=\"errorText\">邮箱格式不正确,请重新输入!</span>");
			}
		}
	});
	$("#oldEmail").focus(function(){
		$("#emailkong").remove();
		$("#oldemail").remove();
	});
	$("#newEmail").blur(function(){
		if($("#newEmail").val()=='' && $("#newEmail").val()== null){
			$("#newEmail").after("<span id =\"newemail\" class=\"errorText\">邮箱不能为空,请重新输入!</span>");
		}else{
			$("#newemail").remove();
			var reg =/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			if(!reg.test($("#newEmail").val())){
				$("#newEmail").after("<span id =\"newemail\" class=\"errorText\">邮箱格式不正确,请重新输入!</span>");
			}
		}
	});
	$("#newEmail").focus(function(){
		$("#newemail").remove();
	});
	
	/*验证开户人姓名*/
	$("#cardname").blur(function(){
		if($("#cardname").val()=='' || $("#cardname").val()== null){
			$("#cardname").after("<span id=\"cardnameSpan\" class=\"errorText\">姓名不能为空</span>");
		}
	});
	$("#cardname").focus(function(){
		$("#cardnameSpan").remove();
	});
	/*验证手机号码*/
	$("#cardphone").blur(function(){
		if($("#cardphone").val()=='' || $("#cardphone").val()== null){
			$("#phoneSpan").show();
			$("#phoneSpan").html("手机号不能为空");
		}else{
			var reg=/^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/;
			if(!reg.test($("#cardphone").val())){
				$("#phoneSpan").show();
				$("#phoneSpan").html("手机号格式不正确");
			}
		}
	});
	$("#cardphone").focus(function(){
		$("#phoneSpan").hide();
	});
	
	/*验证身份证号码*/
	$("#cardsfz").blur(function(){
		if($("#cardsfz").val()=='' || $("#cardsfz").val()== null){
			$("#cardsfz").after("<span id=\"cardsfzSpan\" class=\"errorText\">身份证不能为空</span>");
		}else{
			var reg=/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
			if(!reg.test($("#cardsfz").val())){
				$("#cardsfz").after("<span id=\"cardsfzSpan\" class=\"errorText\">身份证号格式不正确</span>");
			}
		}
	});
	$("#cardsfz").focus(function(){
		$("#cardsfzSpan").remove();
	});
	/*验证银行卡号*/
	$("#carnumber").blur(function(){
		if($("#carnumber").val()=='' || $("#carnumber").val()== null){
			$("#carnumber").after("<span id=\"carnumberSpan\" class=\"errorText\">银行卡不能为空</span>");
		}
	});
	$("#carnumber").focus(function(){
		$("#carnumberSpan").remove();
	});
});

function emailcheck(){
	if($("#newEmail").val()=='' && $("#newEmail").val()== null){
		$("#newEmail").after("<span id =\"newemail\" class=\"errorText\">邮箱不能为空,请重新输入!</span>");
		return false;
	}else{
		$("#newemail").remove();
		var reg =/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if(!reg.test($("#newEmail").val())){
			$("#newEmail").after("<span id =\"newemail\" class=\"errorText\">邮箱格式不正确,请重新输入!</span>");
			return false;
		}else{
			return true;
		}
	}
}


function password(){
	if($("#oldPwd").val().replace(/\s/g,"")=='' || $("#oldPwd").val().replace(/\s/g,"")== null){
		$("#ypassword").remove();
		$("#oldPwd").after("<div id =\"ypassword\" class=\"errorText\">原登录密码不能为空</div>");
		return false;
	}else if($("#newPwd").val().replace(/\s/g,"")=='' || $("#newPwd").val().replace(/\s/g,"")== null){
		$("#xpassword").remove();
		$("#newPwd").after(" <span id =\"xpassword\" class=\"errorText\">新登录密码不能为空</span>");
		return false;
	}else if($("#newPwd").val().length < 6){
		$("#xpassword").remove();
		$("#newPwd").after(" <span id =\"xpassword\" class=\"errorText\">新登录密码不能少于6位</span>");
		return false;
	}else if($("#newPwdTwo").val().replace(/\s/g,"")=='' || $("#newPwdTwo").val().replace(/\s/g,"")== null){
		$("#yzpassword").remove();
		$("#newPwdTwo").after(" <span id =\"yzpassword\" class=\"errorText\">再次密码不能为空</span>");
		return false;
	}else if($("#newPwdTwo").val()!=$("#newPwd").val()){
		$("#yzpassword").remove();
		$("#xpassword").remove();
		if($("#newPwd").val() != $("#newPwdTwo").val())
		$("#newPwdTwo").after(" <span id =\"xpassword\" class=\"errorText\">再次密码不一致</span>");
		return false;
	}else{
		return true;
	}
}
/*修改邮箱*/
function emailSubmit(){
	if(emailcheck()){
		$.post($("#ctxpath").val() + "/store/account/checkEmail",{email:$("#oldEmail").val(),newEmail:$("#newEmail").val()},function(data){	
		   if(data == '0'){
				   $("#oldEmail").after("<div id =\"ypassword\" class=\"errorText\">原邮箱不正确,请重新输入!</div>");
		   }else if(data == '2'){
				   falert("更新邮箱失败,请重新更新!");
		   }else{
			   $("#oldEmail").val("");
			   $("#newEmail").val("");
			   $(".alterBoxTrade").hide();
			   $(".alterBox").hide();
			   $(".alertLayerBg").hide();
			   salert("更新成功,请到邮箱激活!");
		   }
		});
	}
}
/*修改密码*/
function passwordSubmit(){
	if(password()){
		$.post($("#ctxpath").val() + "/store/account/checkpassword",{password:$("#oldPwd").val(),newPassword:$("#newPwd").val()},function(data){	
			   if(data == '0'){
					   $("#oldPwd").after("<div id =\"ypassword\" class=\"errorText\">原登录密码不正确,请重新输入!</div>");
			   }else if(data == '2'){
					   falert("更新密码失败,请重新更新!");
			   }else{
				   $("#oldPwd").val("");
				   $("#newPwd").val("");
				   $("#newPwdTwo").val("");
				   $(".alterBox").hide();
//				   $(".alertLayerBg").hide();
				   salertWithFunction("更新成功!",back,null);
			   }
			});
	}
}
function back(){
	window.location.reload();
}
/*绑定手机号码*/
function cardSubmit(){
	if(phone()){
		$.post($("#ctxpath").val() + "/store/account/phone",
				{oldphone:$("#oldphone").val(),newphone:$("#newphone").val()},
				function(data){	
				   if(data == '0'){
						   falert("绑定手机号码失败,请重新绑定!");
				   }else{
					   $("#oldEmail").val("");
					   $("#newEmail").val("");
					   $(".alterBoxBank").hide();
					   $(".alterBox").hide();
					   $(".alertLayerBg").hide();
					   salert("绑定手机号码成功!");
				   }
	    	});
	}else{
		
	}
}
function update(ctx){
	window.location.href=ctx+"/store/manager/ACCOUNT_BANK_LIST";
}
function newphone(){
	var reg=/^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/;
	var newhone = $("#newPhone").val();
	if($.trim(newhone) == ""){
		$("#newPhoneSpan").html("新手机号码不能为空");
		$("#newPhoneSpan").show();
		return false;
	}else{
		if(!reg.test($.trim(newhone))){
			$("#newPhoneSpan").html("手机号码格式不正确");
			$("#newPhoneSpan").show();
			return false;
		}else{
			$("#newPhoneSpan").hide();
			return true;
		}	
	}
}
function onNewPhone(){
	$("#newPhoneSpan").hide();
}

function yz(){
	var yzm = $("#yanzhengma").val();
	if(yzm !=""){
		if(yzm.length <= 5){
			$("#phoneyanzheng").html("验证码不能小于6位");
			$("#phoneyanzheng").show();
			return false;
		}else{
			return true;
		}
	}else{
		$("#phoneyanzheng").html("验证码不能为空");
		$("#phoneyanzheng").show();
		return false;
	}
}
function onfyz(){
	$("#phoneyanzheng").hide();
}


function postphone(ctx){
	$.post(ctx+"/store/account/phone",
			  {oldphone:$("#oldPhone").val(),newphone:$("#newPhone").val(),yz:$("#yanzhengma").val()},
            function(data){
				  if(data=='0'){
					  salert('修改手机号码成功');
					   $("#oldphone").val("");
					   $("#newphone").val("");   
					   $("#yanzheng").val("");
					   $(".alterBoxQuestion").hide();
					   $("#alterBox").hide();
					   $(".alertLayerBg").hide();
				  }else if(data=='1'){
					  falert('原手机号错误');
				  }else if(data=='2'){
					  falert('验证码错误请重新输入验证码');
				  }else{
					  falert('修改手机号码失败');
				  }
				   
			});
}
function phone(ctx){
	var reg=/^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/;
	old = $("#oldPhone").val();
	if($.trim(old) != ''){
		if(!reg.test(old)){
			$("#oldPhoneSpan").val("手机格式不正确");
			return false;
		}else{
			if(newphone()){
				//判断验证码不能为空
				if(yz()){
					postphone(ctx);
				}
			}
		}
	}else{
		if(newphone()){
			//判断验证码不能为空
			if(yz()){
				postphone(ctx);
			}
		}
	}
}

function load(pwd){
	if(pwd=='1'){
		$("#password").html("<span class=\"intensionLev1\"></span><span class=\"intensionText\">强度:弱</span>"); 
	}else if(pwd=='2'){
		$("#password").html("<span class=\"intensionLev2\"></span><span class=\"intensionText\">强度:中</span>"); 
	}else if(pwd=='3'){
		$("#password").html("<span class=\"intensionLev3\"></span><span class=\"intensionText\">强度:强</span>"); 
	}
}