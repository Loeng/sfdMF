	
$(document).ready(function(){
	// 判定后台是否返回添加成功的信息
	if($("#message").val()=="success"){
		ealert('修改成功');
	}else if($("#message").val()=="fail"){
		ealert("修改失败！");
	}
	
	$(".ht_btn").next().addClass("afterHt");
});
	
	function formSubmit () {
		var password = $("#password").val();
		var rePassword = $("#rePassword").val();
		
		if(password != rePassword){
			ealert("两次邮件密码不一致，请重新输入!")
			return;	
		}
		
		var mobilePsw = $("#mobilePsw").val();
		var reMobilePsw = $("#reMobilePsw").val();
		
		if(mobilePsw != reMobilePsw){
			ealert("两次短信密码不一致，请重新输入!")
			return;	
		}
		document.getElementById("messageConfigForm").submit();
	}
