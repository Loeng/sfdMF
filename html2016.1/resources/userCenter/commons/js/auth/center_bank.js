$(document).ready(function(){
	// ====== 法人简历 js ======
    $(".resumeAdd").live("click",function(){
		var obj=$(this).parents(".resume").clone();
		obj.find("input").val("");
		obj.find(".resumeBtn").html('<a href="javascript:void(0);" class="btnBlue resumeRemove">删除</a><a href="javascript:void(0);" class="btnBlue resumeAdd">继续添加</a>');
		obj.removeClass("listFirst");
		
		$.each(obj.find("input"), function(){
			var val = $(this).attr("name");
			var num = val.substring(val.indexOf("[")+1, val.indexOf("]"));
			val = val.replace(num, parseInt(num)+1);
			$(this).attr("name", val);
			
			var onclick = $(this).attr("onclick");
			if(onclick != ""){
				var timel = new Date().getTime();
				if(val.indexOf("startTime") >= 0){
					$(this).attr("id", "ddbegin" + timel);
					var ddend = "ddend" + timel;
					$(this).attr("onclick", "WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\\'" + ddend + "\\')}'})");
				}else if(val.indexOf("endTime") >= 0){
					$(this).attr("id", "ddend" + timel);
					var ddbegin = "ddbegin" + timel;
					$(this).attr("onclick", "WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\\'" + ddbegin + "\\')}'})");
				}
			}
		});
		$(this).parents(".formIn").append(obj);
		$(this).parents(".resume").find(".resumeAdd").remove();
		$(".listFirst").find(".resumeAdd,.resumeRemove").remove();
	});
	$(".resumeRemove").live("click",function(){
		if($(this).siblings(".resumeAdd").length>0){
			$(this).parents(".resume").prev().find(".resumeBtn").append('<a href="javascript:void(0);" class="btnBlue resumeAdd">继续添加</a>');
		}
		$(this).parents(".resume").remove();
		if($(".resume").length<2){
			$(".listFirst .resumeBtn").html('<a href="javascript:void(0);" class="btnBlue resumeAdd">继续添加</a>');
		}
	});
});

function utilSuccess(){
	window.location.reload();
}

//移除
function tempOut(obj, mark) {
	if (mark == 1) {
		$(obj).parent().attr("class", "stepItem");
	}
}

function tempOver(obj) {
	$(obj).parent().attr("class", "stepItem alreadyItem");
}

function bankTwoSubmit(){
	var ctx = $("#ctxpath").val();
	var regexp = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	var legalName = $.trim($("input[name='sl.legalName']").val());
	var legalIdNumber = $.trim($("input[name='sl.legalIdCard']").val());
	
	if(legalName == ""){
		falert("请输入法人姓名");
		return;
	}
	if(legalIdNumber == ""){
		falert("请输入法人身份证号");
		return;
	}
	if(!regexp.test(legalIdNumber)){
		falert("法人身份证号格式错误");
		return;
	}
	
	$("#twoSubmit").ajaxSubmit(function(data){
		if(data == true || data == "true"){
			window.location.href = ctx + "/store/auth/bank_two";
		}else{
			falert("提交失败");
		}
	});
}

function storeBankSubmit(){
	$("#threeSubmit").ajaxSubmit(function(data){
		if(data == true || data == "true"){
			salertWithFunction("提交成功", utilSuccess, null);
		}else{
			falert("提交失败");
		}
	});
}