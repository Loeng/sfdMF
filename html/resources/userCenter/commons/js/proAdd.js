// JavaScript Document
$(document).ready(function() {
	/*弹层*/
    $(".clickBrand").click(function(){
		$(".apLayerBg").show();
		$(".alertBrand").show();
	})
	
	$(".clickTemp").click(function(){
		$(".apLayerBg").show();
		$(".alertTemp").show();
	})
	
	
	/*增加图片*/
	$(".btnAdd").click(function(){
		var obj=$(".uploadItem").eq(0).clone();
		obj.append("<a href=\"javascript:void(0);\" class=\"closeIco\"></a>")
		$(this).before(obj);
	})
	/*删除图片*/
	$(".uploadItem .closeIco").live("click",function(){
		$(this).parents(".uploadItem").remove();
	})
});






$(function(){
	
	/*增加条目*/
	$(".btnAddLine").click(function(){
		var obj = $("#addFieldsDemo").html();
		var radomId = "popPic" + new Date().getTime();
		obj = obj.replaceAll("picClone",radomId);
		$(this).siblings(".pfPriceList").append(obj);
		$(".pfPriceList .listItem:last").find(".productPrices").val(radomId);
	})
	
	/*删除条目*/
	$(".btnRemoveLine").live("click",function(){
		$(this).parents(".listItem").remove();
	})
	
	
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
	
	/*全选*/
	$(".checkAll").live("click",function() {
		var obj=$(this).find("input");
		if(obj.is(":checked")){
			$(".colorSizeTable td input[type='checkbox']").attr("checked",true);
		}else{
			$(".colorSizeTable td input[type='checkbox']").attr("checked",false);
		}
	});
	
	/*增加图片*/
	$(".btnAdd").click(function(){
		var radomId = "popPic" + new Date().getTime();
		var obj=$("#picForClone").html();
		obj = obj.replaceAll("picClone",radomId);
		$(this).before(obj);
		var popPicNum = $("#popPicProperties").val();
		popPicNum = popPicNum + radomId + ";";
		$("#popPicProperties").val(popPicNum);
	})
	
	/*删除图片*/
	$(".uploadItem .closeIco").live("click",function(){
		$(this).parents(".uploadItem").remove();
		var popPicNum = $("#popPicProperties").val();
		var oldId = $(this).attr("id") + ";";
		popPicNum = popPicNum.replaceAll(oldId,"");
		$("#popPicProperties").val(popPicNum);
	})
});