// JavaScript Document
$(document).ready(function() {
    /*选项卡切换*/
	$(".regContent").eq(0).show();
	$(".regTab li a").click(
	function(){
		$(this).parent().siblings().children("a").removeClass("cur");
		$(this).addClass("cur");
		var index=$(".regTab li a").index(this);
		$(".regContent").hide();
		$(".regContent").eq(index).show();
	})
	
	$(".Retype").live("click",function(){
		$(this).addClass("cur");
	});
	
});
function regFormSubmit(){
	var ctx = $("#ctxpath").val();
	var name = $("input[name='name']").val();
	var type = $(".cur").val();
	var mail = $("input[name='mail']").val();
	var phone = $("input[name='phone']").val();
	var password = $("input[name='password']").val();
	$.post(ctx + "/web/storeReg",{name:name,type:type,mail:mail,phone:phone,password:password,},function(data){
		if(data == true || data == "true"){
			salert("注册成功!");
		}else{
			falert("注册失败");
		}
	});
}