$(document).ready(function() {
	var ctx = $("#ctxpath").val();
	var type = $("#jugg").val();
	
	/*选择弹窗*/
	$(".btnXuanze").live("click",function(){
		var ids = "";
		$("input[name='intelId']").each(function(){
			ids += "," + $(this).val();
		});	
		$(".alterBox").load(ctx + "/web/supplyBuy/jumpIntelPage/load",function(){
			$(".alterBox input[type='checkbox']").each(function(){
				if(ids.indexOf($(this).val()) >= 0){
					$(this).attr("checked", true);
					$(this).parent().parent().addClass("checked");
				}
			});
			$(".alertLayerBg").show();
			$(".alertLayerBg").next(".alterBox").show();
		});
	});
	
	/*关闭弹窗*/
	$(".layerClose").live("click",function(){
		$(this).parent(".alterBox").hide();
		$(this).parent(".alterBox").prev(".alertLayerBg").hide();
	});
	
	/*确定弹窗*/
	$(".altSure").live("click",function(){
		var idname = "";
		$("input[type='checkbox']").each(function(){
			if($(this).is(":checked")){
				var names = $(this).parent().parent().next().html();
				idname += '<li title="' + names + '">'+ names + '<a href="javascript:void(0);" class="closeLi"></a>';
				idname += '<input type="hidden" name="intelId" value="' + $(this).val() + '" /></li>';
			}
		});
		$(".zizhiList").empty();
		$(".zizhiList").html(idname);
		
		$(this).parents(".alterBox").hide();
		$(this).parents(".alterBox").prev(".alertLayerBg").hide();
	});
	
	/*取消弹窗*/
	$(".altCancel").live("click",function(){
		$(this).parents(".alterBox").hide();
		$(this).parents(".alterBox").prev(".alertLayerBg").hide();
	});
	
	/*删除当前资质*/
	$(".closeLi").live("click",function(){
		if($(".zizhiList li").length<2){
			$(this).parent("li").attr("title", "选择你要认证的资质名称");
			$(this).parent("li").html('选择你要认证的资质名称');
		}else{
			$(this).parent("li").remove();
		}
	});
	
	/*当前复选框*/
	$(".checkItem").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			//选中
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
		}else{
			//取消选中
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
		}
	});
	
	/*$(".zizhiList:eq(1) li").each(function(){
		var shengluehao=$(this).html();
		var word22=$(this).text();
		var changdu=shengluehao.length;
		console.log(changdu);
		if (changdu<7) {
			$(this).html(shengluehao);
			}
		else{
			var newtext=word22.substr(0, 7);
			$(this).html(newtext+"...认证"+'<a href="javascript:void(0);" class="closeLi"></a>');
		}
	});*/
});
function formSubmit(){
	if(checkTitleBlur()&&checkContentNameBlur()&&checkContentPhoneBlur()){
		$("#updateSupplyBuyForm").ajaxSubmit(
				function(data){
				if(data==true || data=="true"){
					econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
				}else{
					falert("修改失败");
				}
			});
	}else{
		checkTitleBlur();
		checkContentNameBlur();
		checkContentPhoneBlur();
	}
}
function reset(){
	document.getElementById("updateSupplyBuyForm").reset();
}
//返回列表
function goBack(contextPath){
	var productType = $("#productType").val();
	var type = $("#type").val();
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/store/supplyBuy/ylxqList/"+productType+"/"+type;
	}
	window.location.href=contextPath + "/store/supplyBuy/ylxqList/"+productType+"/"+type;
}

function checkTitleBlur(){
	var title = $.trim($("input[name='title']").val());
	if(title == ""){
		$('#titleSpan').show();
		$('#titleSpan').text("请输入供求标题");
		return false;
	}
	$('#titleSpan').hide();
	return true;
}
function checkContentNameBlur(){
	var contactName = $.trim($("input[name='contactName']").val());
	
	if(contactName == ""){
		$('#contactNameSpan').show();
		$('#contactNameSpan').text("请输入联系人");
		return false;
	}
	$('#contactNameSpan').hide();
	return true;
}
function checkContentPhoneBlur(){
	var contactPhone = $.trim($("input[name='contactPhone']").val());
	var regexp = /^(1[0-9][0-9])\d{8}$/;
	
	if(contactPhone == ""){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入联系电话");
		return false;
	}
	if(!regexp.test(contactPhone)){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入正确的联系电话");
		return false;
	}
	$('#contactPhoneSpan').hide();
	return true;
}