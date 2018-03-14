$(document).ready(function() {
	$(".ht_btn").next().addClass("afterHt");
	
	$(".limitItem").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
			$main.next(".limitList").find("input").attr("checked",true);
			$main.next(".limitList").find(".check").addClass("checked");
			$main.parents().prev(".limitItem").find("input").attr("checked",true);
			$main.parents().prev(".limitItem").find(".check").addClass("checked");
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
			$main.next(".limitList").find(":checkbox").attr("checked",false);
			$main.next(".limitList").find(".check").removeClass("checked");
		}
	});
	$(".limitAll").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.nextAll(".limitList").find("input").attr("checked",true);
			$main.nextAll(".limitList").find(".check").addClass("checked");
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.nextAll(".limitList").find(":checkbox").attr("checked",false);
			$main.nextAll(".limitList").find(".check").removeClass("checked")
		}
	});
});	

// 返回列表
function goBack(){
	window.location.href = $("#ctxpath").val() + "/system/role/list";
}

function checkNameFocus(){
	$("input[name='name']").parent().removeAttr("class");
}

function checkNameBlur(){
	var obj = $("input[name='name']");
	var name = $.trim($(obj).val());
	
	if(name == ""){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("角色名称不能为空");
		return false;
	}
	if(name.length < 6 || name.length > 20){
		$(obj).parent().attr("class", "text_c_ts");
		$(obj).next().html("角色名称6-20字符组成");
		return false;
	}
	return true;
}


// 提交
function formSubmit() {
	if(checkNameBlur()){
		$("#modifyRoleForm").ajaxSubmit(function(data){
			// 1:角色名称为空，2:角色名称6-20字符组成，3:保存失败，4:保存成功
    		var obj = $("input[name='name']");
    		if(data == 1){
    			$(obj).parent().attr("class", "text_c_ts");
    			$(obj).next().html("角色名称不能为空");
    		}else if(data == 2){
    			$(obj).parent().attr("class", "text_c_ts");
    			$(obj).next().html("角色名称6-20字符组成");
    		}else if(data == 3){
    			ealert("修改失败");
    		}else if(data == 4){
    			ealert("修改成功");
    		}
		});
	}else{
		checkNameBlur();
	}
}