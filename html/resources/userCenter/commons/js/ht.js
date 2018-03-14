// JavaScript Document
$(document).ready(function() {
    $(".btnAdd").live("click",function(){
		var obj=$(this).parents(".htItem").clone();
		obj.find("input").val("");
		obj.find(".htNew1").html('<div class="fileUpload"><a href="javascript:void(0);" class="btnUpload btnHt"><input name="" type="file">上传合同</a></div>');
		obj.find(".htNew2").html('<a href="javascript:void(0);" class="btnHt btnAdd">新增</a>');
		$(this).parents(".formIn").append(obj);
		$(this).parents(".htItem").find(".htNew2").html('<a href="javascript:void(0);" class="btnHt btnDel">删除</a>');
	})
	$(".btnDel").live("click",function(){
		if($(this).parents(".htItem").siblings(".htItem").length>0){
			$(this).parents(".htItem").remove();
		}
		$(this).parents(".htItem").siblings(".htItem").find(".htNew2").html('<a href="javascript:void(0);" class="btnHt btnDel">删除</a>');
		if($(".htItem").length<2){
			$(this).parents(".htItem").find(".htNew2").html('<a href="javascript:void(0);" class="btnHt btnAdd">新增</a>');
		}
	})
	$(".closeBtn").live("click",function(){
		$(this).siblings(".textBox").val("");
		$(this).hide();
		$(this).parents(".htItem").find(".btnHt").removeClass("btnUploadNo");
		$(this).parents(".htItem").find(".htNew1").html('<div class="fileUpload"><a href="javascript:void(0);" class="btnUpload btnHt"><input name="" type="file">上传合同</a></div>');
		$(this).parents(".htItem").find(".htNew2").html('<a href="javascript:void(0);" class="btnHt btnDel">删除</a>');
	});
});