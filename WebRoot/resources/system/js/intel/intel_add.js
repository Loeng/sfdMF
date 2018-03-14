$(document).ready(function(){
	$(".ht_btn").next().addClass("afterHt");
});

function checkNameFocus(){
	$("input[name='name']").parent().removeAttr("class");
}

function checkNameBlur(){
	var obj = $("input[name='name']");
	var name = $.trim($(obj).val());
	
	if(name == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("资质名称不能为空");
		return false;
	}
	if(name.length < 4 || name.length > 20){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("资质名称4-20字符组成");
		return false;
	}
	return true;
}

// 提交
function formSubmit(){
	if(checkNameBlur()){
    	$("#intelAddFrom").ajaxSubmit(function(data){
    		if(data == 3){
    			ealert("保存成功");
    		}else{
    			ealert("保存失败");
    		}
    	});
    }
}
// 重置
function reset(){
	document.getElementById("intelAddFrom").reset();
}

// 返回列表
function goBack(){
	window.location.href = $("#ctxpath").val() + "/system/intel/list";
}	
