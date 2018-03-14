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

function reone(){
	var ctx = $("#ctxpath").val();
	window.location.href = ctx + "/store/auth/jumpAuthOnePage";
}

function retwo(){
	var ctx = $("#ctxpath").val();
	window.location.href = ctx + "/store/auth/jumpAutoTwoPage";
}

// 移除
function tempOut(obj, mark) {
	if (mark == 1) {
		$(obj).parent().attr("class", "stepItem");
	}
}

function tempOver(obj) {
	$(obj).parent().attr("class", "stepItem alreadyItem");
}

function checkStoreName(){
	var old = $.trim($("#tempStoreName").val());
	var nez = $.trim($("input[name='store.storeName']").val());
	if(nez == ""){
		falert("请输入企业名称");
		return false;
	}
	return true;
}

// one
function one(){
	var ctx = $("#ctxpath").val();
	var old = $.trim($("#tempStoreName").val());
	var nez = $.trim($("input[name='store.storeName']").val());
	
	if(nez == ""){
		falert("请输入企业名称");
		return;
	}
	if(old != nez){
		$.post(ctx + "/store/auth/checkCenterStoreName",{old:old,nez:nez},function(data){
			if(data == true || data == "true"){
				falert("企业名称已存在");
			}else{
				$("#oneSubmit").ajaxSubmit(function(data){
					if(data == 1){
						window.location.href = ctx + "/store/auth/jumpAutoTwoPage";
					}else if(data == 2){
						falert("提交失败");
					}else if(data == 3){
						falert("请上传企业LOGO");
					}else if(data == 4){
						falert("请上传营业执照");
					}
				});
			}
		});
	}else{
		$("#oneSubmit").ajaxSubmit(function(data){
			if(data == 1){
				window.location.href = ctx + "/store/auth/jumpAutoTwoPage";
			}else if(data == 2){
				falert("提交失败");
			}else if(data == 3){
				falert("请上传企业LOGO");
			}else if(data == 4){
				falert("请上传营业执照");
			}
		});
	}
}

// two
function two(){
	var ctx = $("#ctxpath").val();
	var regexp = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	var legalName = $.trim($("input[name='si.legalName']").val());
	var legalIdNumber = $.trim($("input[name='si.legalIdNumber']").val());
	
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
			window.location.href = ctx + "/store/auth/jumpAutoThreePage";
		}else{
			falert("提交失败");
		}
	});
}

// three
function three(){
	var ctx = $("#ctxpath").val();
	$("#threeSubmit").ajaxSubmit(function(data){
		if(data == true || data == "true"){
			salertWithFunction("资料完成提交", myDocReload, null);
		}else{
			falert("提交失败");
		}
	});
}

function myDocReload(){
	window.location.reload();
}
