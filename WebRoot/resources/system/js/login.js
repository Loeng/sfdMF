
//点击图片更换验证码
function refresh() {
	var ctx = $("#ctxpath").val();
	$("#vfc").attr("src", ctx + "/security/image?" + Math.random());
}

function keyLogin() {
	if (event.keyCode==13){
		sysUserLogin();
	}
}

function checkName(){
	var obj = $("#name");
	var name = $.trim($(obj).val());
	if(name == ""){
		errorMess(obj,"请输入用户名称");
		return false;
	}
	errorMess(obj,"");
	return true;
}

function checkPassword(){
	var obj = $("#password");
	var password = $.trim($(obj).val());
	if(password == ""){
		errorMess(obj,"请输入密码");
		return false;
	}
	if(password.length < 6 || password.length > 32){
		errorMess(obj,"密码长度6-32位");
		return false;
	}
	errorMess(obj,"");
	return true;
}

function checkVerifyCode(){
	var obj = $("input[name='verifyCode']");
	var verifyCode = $.trim($(obj).val());
	if(verifyCode == ""){
		errorMess("","请输入验证码");
		return false;
	}
	errorMess("","");
	return true;
}
function errorMess(obj,mess){
	if(null!=mess&&""!=mess&&" "!=mess){
		$("#error").html("<em>!</em>"+mess);
		$(obj).parent().parent().addClass("errorItem");
	}else{
		$("#error").html(mess);
		$(obj).parent().parent().removeClass("errorItem");
	}
}


function sysUserLogin(){
	
	if(checkName() && checkPassword() && checkVerifyCode()){
		//待加密字符串
	    var name = $.trim($("#name").val());
	    var password = $.trim($("#password").val());
	    
	    //第一个参数必须；第二个、第三个参数可选
	    var key1 = $("#key1").val();  
	    var key2 = $("#key2").val(); 
	    var key3 = $("#key3").val();
	    
	    name = strEnc(name, key1, key2, key3);
	    password = strEnc(password, key1, key2, key3);
	    
	    $("input[name='name']").val(name);
	    $("input[name='password']").val(password)
	    
	    $("#loginSubmit").ajaxSubmit(function(data){
	    	
	    	// 1:用户名不能为空，2:密码不能为空，3:密码长度6-32位，4:验证码不能为空，5:验证码错误，6:用户不存在
	    	// 7:密码错误，8:登陆失败，9:登陆成功
	    	if(data == 1){
	    		refresh();
	    		errorMess($("#name"),"请输入用户名");
	    	}else if(data == 2){
	    		refresh();
	    		errorMess($("#password"),"请输入密码");
	    	}else if(data == 3){
	    		refresh();
	    		errorMess($("#password"),"密码长度6-32位");
	    	}else if(data == 4){
	    		refresh();
	    		errorMess("","请输入验证码");
	    	}else if(data == 5){
	    		refresh();
	    		errorMess("","输入验证码错误");
	    	}else if(data == 6){
	    		refresh();
	    		errorMess($("#name"),"用户名不存在");
	    	}else if(data == 7){
	    		refresh();
	    		errorMess($("#password"),"输入密码错误");
	    	}else if(data == 8){
	    		refresh();
	    		errorMess("","登陆失败");
	    	}else if(data == 9){
	    		window.location.href = $("#ctxpath").val() + "/system";
	    	}
	    });
	}else{
		checkName();
		checkPassword();
		checkVerifyCode();
	}
}
