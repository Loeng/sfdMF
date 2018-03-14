$(document).ready(function(){
	var ctx = $("#ctxpath").val();
	var roleId = $("#roleId").val();
	$("#topRoleName").load(ctx + "/system/role/getRoleNameById/" + roleId);
});

function logout(){
	window.parent.location = $("#ctxpath").val() + "/system/manager/logout";	
}	

function showLeftPurview(obj, topId){
	var ctx = $("#ctxpath").val();
	if(obj != null){
		$(obj).siblings().removeClass("cur");
		$(obj).addClass("cur");
	}
	$("#indexMenu", window.parent.document).attr("src", ctx + "/system/left/" + topId);
}

function qHc(contextPath){
	$.post(
		contextPath + "/system/index/qhc",
		function(data){
			if(data){
				alert("缓存清除成功")
			}else{
				alert("缓存清除缓存")
			}
		}
	);
}	