$(document).ready(function() {
	if($("#ok").val()=="true"){
		salert("操作成功");
	}else if($("#ok").val()=="false"){
		falert("操作失败");
	}
});