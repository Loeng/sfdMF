$(document).ready(function() {
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
function formSubmit(){
	if($("#card").val()==''){
		alert('请选择开户银行');
		return false;
	}else if($("#cardname").val()==''){
		alert('请填写真实姓名');
		return false;
	}else if($("#cardphone").val()==''){
		alert('请填写银行卡绑定手机号');
		return false;
	}else if($("#cardsfz").val()==''){
		alert('请填写身份证号');
		return false;
	}else if($("#carnumber").val()==''){
		alert('请填写银行卡号');
		return false;
	}else{
		$("#addBankCardForm").ajaxSubmit(
				function(data){
				if( data=="1"){
					econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
				}else{
					econfirm('添加失败',null,null,goBack,[$("#ctxpath").val()]);
				}
			});
	}
}
//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href=contextPath+"/store/account/bank/list";
	}
	window.location.href=contextPath + "/store/account/bank/list";
}