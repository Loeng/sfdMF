function updateUserLogo(){
	$("#updateUserLogo").ajaxSubmit(function(data){
		if(data == true || data == "true"){
			window.location.reload();
		}else{
			salert("修改头像失败");
		}
	});
}