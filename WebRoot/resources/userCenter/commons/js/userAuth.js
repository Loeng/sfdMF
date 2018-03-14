// JavaScript Document
$(document).ready(function() {
    $(".resumeAdd").live("click",function(){
		var obj=$(this).parents(".resume").clone();
		obj.find("input").val("");
		obj.find(".resumeBtn").html('<a href="javascript:void(0);" class="btnBase resumeRemove">删除</a><a href="javascript:void(0);" class="btnBase resumeAdd">继续添加</a>');
		obj.removeClass("listFirst");
		$(this).parents(".formIn").append(obj);
		$(this).parents(".resume").find(".resumeAdd").remove();
		$(".listFirst").find(".resumeAdd,.resumeRemove").remove();
	})
	$(".resumeRemove").live("click",function(){
		if($(this).siblings(".resumeAdd").length>0){
			$(this).parents(".resume").prev().find(".resumeBtn").append('<a href="javascript:void(0);" class="btnBase resumeAdd">继续添加</a>');
		}
		$(this).parents(".resume").remove();
		if($(".resume").length<2){
			$(".listFirst .resumeBtn").html('<a href="javascript:void(0);" class="btnBase resumeAdd">继续添加</a>');
		}
	})
});