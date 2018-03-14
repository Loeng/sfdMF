$(document).ready(function() {
	/* 全选*/
	$(".allCheck").click(function(){   
		if(this.checked){   
			$("input[name='checkBtn']").each(function(){this.checked=true;});
			 
		}else{   
			$("input[name='checkBtn']").each(function(){this.checked=false;});   
		}   
	}); 
	/* 标记信息为已读*/
	$(".btnRead").click(function(){   
		$("input[name='checkBtn']").each(function(){     
			if($(this).attr("checked")){
				$(this).parent().siblings().children(".newMsg").css("background-position","0px -13px");
            }else{
			
			}
         }) 
	}); 
	/* 读取信息*/
	/* $(".newMsg").click(function(){
		alert("我是消息!");
		$(this).css("background-position","0px -13px");
	});*/
})