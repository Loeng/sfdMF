//保存编辑的频道配置
function saveConfig(channelId,ctxpath){
	var htmlStr = $("#configBody").html();
	alert(htmlStr);
	$("#htmlStr").val(htmlStr);
	$("#configSaveForm").submit();
}

//取消编辑频道配置
function cancelConfig(){
	window.close();   
}
