var verification={};
//var greenClass="text_d_ts";
var greenClass="";
var redClass="text_c_ts";
//验证手机电话
verification.checkMobile=function(id){
	var domNode=document.getElementById(id);
	var value=domNode.value;          //文本的值
	$(domNode).parent().removeClass(greenClass+" "+redClass);
	if(/^1[\d]{10}$/g.test(value)){
		$(domNode).parent().addClass(greenClass);//更改文本的样式
		return true;
	}else{
		$(domNode).parent().addClass(redClass);
		return false;
	}
}

//验证邮箱
verification.checkEmail=function(id){
	var domNode=document.getElementById(id);
	var value=domNode.value;//文本的值
	$(domNode).parent().removeClass(greenClass+" "+redClass);
	if(/(\S)+[@]{1}(\S)+[.]{1}(\w)+/.test(value)){
		$(domNode).parent().addClass(greenClass);//更改文本的样式
		return true;
	}else{
		$(domNode).parent().addClass(redClass);
		return false;
	}
}

