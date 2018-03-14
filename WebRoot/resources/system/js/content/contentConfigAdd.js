

$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
});
$(function(){
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('操作成功',null,null,null,null);
	}else if($("#ok").val()=="false"){
		ealert("操作失败！");
	}
});

//验证上传导航图片的格式
function validateForm(add) {
	//可不上传图片，直接返回true
 	if (add.uploadFile.value == "") {
        return true;
  	}     
 	//截取提交上传文件的扩展名  
  	var ext = add.uploadFile.value.match(/^(.*)(\.)(.{1,8})$/)[3];
  	ext = ext.toLowerCase(); //设置允许上传文件的扩展名           
  	if (ext ==  "jpg" || ext == "gif" || ext=="png") {
        return true;
  	} else {
        alert("只允许上传 .jpg或gif 或png文件，请重新选择需要上传的文件！");
        return false;
 	}
}
// 提交
function formSubmit(){
	document.getElementById("addContentConfigForm").submit();
}

