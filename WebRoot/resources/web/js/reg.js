// JavaScript Document
var formStatus;
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
		$(this).siblings().removeClass("cur");
		$("#type").val($(this).val());
	});
	$("#type").val($(".Retype").eq(0).val());
	
	formStatus=$("#regform").Validform({
		tiptype:function(msg,o,cssctl){
			//msg：提示信息;
			//o:{obj:*,type:*,curform:*}, obj指向的是当前验证的表单元素（或表单对象），type指示提示的状态，值为1、2、3、4， 1：正在检测/提交数据，2：通过验证，3：验证失败，4：提示ignore状态, curform为当前form对象;
			//cssctl:内置的提示信息样式控制函数，该函数需传入两个参数：显示提示信息的对象 和 当前提示的状态（既形参o中的type）;
			if(!o.obj.is("form")){//验证表单元素时o.obj为该表单元素，全部验证通过提交表单时o.obj为该表单对象;
				var objtip=o.obj.siblings(".Validform_checktip");
				cssctl(objtip,o.type);
				objtip.text(msg);
			}
		}
	});
	
	$(".textBox").live("focus",function(){
		//$(this).removeClass("Validform_error");
		$(this).siblings(".Validform_checktip").removeClass("Validform_wrong").text("");
	});
	
	$(".Validform_checktip").live("click",function(){
		$(this).siblings(".textBox").focus();
		$(this).removeClass("Validform_wrong");
		$(this).text("");
	});
});

var flag = false;
var flag1 = false;
//验证企业名是否存在
function checkStoreName(){
	var ctx = $("#ctxpath").val();
	var companyName = $("input[name='companyName']").val();
	$.post(ctx + "/web/checkStoreName",{companyName:companyName},function(data){
		if(data){
			$(".companyName").find("input").addClass("Validform_error");
			$(".companyName").find(".Validform_checktip").addClass("Validform_wrong").show();
			$(".companyName").find(".Validform_checktip").text("企业名称已存在!");
			flag1= false;
		}else{
			flag1= true;
		}
	});
}

//验证用户名是否存在(手机号就是用户名)
function checkUserName(){
	var ctx = $("#ctxpath").val();
	var name = $("input[name='phone']").val();
	$.post(ctx + "/web/checkUserName",{name:name},function(data){
		if(data){
			$(".mobile").find("input").addClass("Validform_error");
			$(".mobile").find(".Validform_checktip").addClass("Validform_wrong").show();
			$(".mobile").find(".Validform_checktip").text("手机号已注册!");
			flag = false;
		}else{
			flag =  true;
		}	
	});
}
function regFormSubmit(){
	//获取页面参数
	var ctx = $("#ctxpath").val();
	var companyName = $("input[name='companyName']").val();
	var type = $("#type").val();
	var mail = $("input[name='mail']").val();
	var phone = $("input[name='phone']").val();
	var password = $("input[name='password']").val();
	var checkNo = $("input[name='checkNo']").val();
	//验证成功后提交
	if(formStatus.check() && $('#xyBox').is(':checked') && flag && flag1){
		$.ajax({
            type: "POST",
            url: ctx + "/web/storeReg",
            data: {companyName:companyName,type:type,mail:mail,phone:phone,password:password,checkNo:checkNo},
            dataType: "json",
            success: function(data){
            	if(data){
//            		salert("注册成功!");
            		salertWithFunction("注册成功!",function(){
            			 setTimeout(function() {
            				 window.location.href=ctx+"/web/reg/storeReg/"+phone;
     					 },500);    
            		});
    			}else{
    				falert("注册失败");
    			}
            }
        });
	}
}
//		$.post(ctx + "/web/storeReg",{companyName:companyName,type:type,mail:mail,phone:phone,password:password},function(data){
//			alert(data);
//			if(data){
//				alert("注册成功!");
////				window.location.href=ctx+"/web/reg/"+type+"/storeReg";
//			}else{
//				alert("注册失败");
//			}
//		});
//	} else {
//		alert(3);
//	}
//	else{
//		$(".regform").abort();
//	}
