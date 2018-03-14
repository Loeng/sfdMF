// JavaScript Document

$(document).ready(function() {
    $(".btnOrange").click(function(){
		$(this).addClass("btnGray");
		$(this).html("<em>90</em>秒后重新获取");
	})
	function abc(){
		var a=parseInt($(".btnGray em").text());
		if(a>1){
		a=a-1
		$(".btnGray em").text(a);
		}
		else{
			$(".btnOrange").removeClass("btnGray");
			$(".btnOrange").html("发送验证码");
		}
	}
	setInterval(abc,1000);
});