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

	formStatus=$("#supplementForm").Validform({
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

function supplementFormSubmit(){
	//获取页面参数
	var ctx = $("#ctxpath").val();
	var companyName = $("input[name='companyName']").val();
	var type = $("#type").val();
	var mail = $("input[name='mail']").val();
	//验证成功后提交
	if(formStatus.check() && $('#xyBox').is(':checked') && flag1){
		$.ajax({
            type: "POST",
            url: ctx + "/web/supplementUser",
            data: {companyName:companyName,type:type,mail:mail},
            dataType: "json",
            success: function(data){
            	if(data){
					window.location.href = $("#ctxpath").val() + "/web/util/jumpZhongPageBySup";
    			}else{
    				falert("操作失败");
    			}
            }
        });
	}
}
