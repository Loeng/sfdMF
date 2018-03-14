// JavaScript Document
$(document).ready(function() {
	
	/*验证码获取倒计时*/
	var wait=120;
	function time(o) {
			if (wait == 0) {
				o.removeAttribute("disabled");
				o.setAttribute("class","btnSend");         
				o.value="获取验证码";
				wait = 120;
			} else {
				o.setAttribute("disabled", true);
				o.setAttribute("class","btnSend btnForbid");
				o.value=wait + "后重新发送";
				wait--;
				setTimeout(function() {
					time(o)
				},
				1000)
			}
		}
	document.getElementById("btnSend").onclick=function(){
		time(this);
	}
});