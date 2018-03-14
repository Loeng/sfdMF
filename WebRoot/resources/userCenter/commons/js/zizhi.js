// JavaScript Document
$(document).ready(function() {
	
	/*选择弹窗*/
	$(".btnXuanze").live("click",function(){
		$(".alertLayerBg").show();
		$(".alertLayerBg").next(".alterBox").show();
	});
	
	/*关闭弹窗*/
	$(".layerClose").live("click",function(){
		$(this).parent(".alterBox").hide();
		$(this).parent(".alterBox").prev(".alertLayerBg").hide();
	});
	
	/*确定弹窗*/
	$(".altSure").live("click",function(){
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
		if($(".zizhiList li").length<3){
			$(this).parent("li").html('选择你要认证的资质名称');
		}
		else{
			$(this).parent("li").remove();
		}
	});
	
	/*清空证明*/
	$(".closeUp").live("click",function(){
		$(this).prev(".textBox").val("");
		$(this).hide();
	});
	
	/*删除证明*/
	$(".btnDelZm").live("click",function(){
		if($(this).parents(".zhengmingLtem").siblings(".zhengmingLtem").length>1){
			$(this).parents(".zhengmingLtem").prev(".zhengmingLtem").find(".new2").html('<div class="btnArea"><a href="javascript:void(0);"  class="btnDelZm">删除</a><a href="javascript:void(0);"  class="btnUpZm">添加文件</a></div>');
			$(this).parents(".zhengmingLtem").remove();
		}
			if($(this).parents(".zhengmingLtem").siblings(".zhengmingLtem").length==1){
			$(this).parents(".zhengmingLtem").prev(".zhengmingLtem").find(".new2").html('<div class="btnArea"><a href="javascript:void(0);"  class="btnDelZm">删除</a><a href="javascript:void(0);"  class="btnUpZm">添加文件</a></div>');
			$(this).parents(".zhengmingLtem").remove();
			$(".zhengmingLtem").find(".new2").html('<div class="btnArea"><a href="javascript:void(0);"  class="btnDelZm">删除</a><a href="javascript:void(0);"  class="btnUpZm">添加文件</a></div>');
		}
		if($(this).parents(".zhengmingLtem").siblings(".zhengmingLtem").length<1){
			$(this).parents(".zhengmingLtem").find(".inputCont").html('<input name="" type="text" class="textBox box280" readonly>');
			$(this).parents(".zhengmingLtem").find(".new2").html('<div class="btnArea"><a href="javascript:void(0);"  class="btnDelZm">删除</a><a href="javascript:void(0);"  class="btnUpZm">添加文件</a></div>');
		}
		else{}
	});
	
	/*增加证明*/
	$(".btnUpZm").live("click",function(){
		var obj=$(this).parents(".zhengmingLtem").clone();
		obj.find("input").val("");
		obj.find(".inputCont").html('<input name="" type="text" class="textBox box280" readonly>');
		$(this).parents(".zhengming").append(obj);
		$(this).parents(".zhengmingLtem").find(".new2").html('');
		$(this).parents(".zhengmingLtem").next(".zhengmingLtem").html('<dt>证明文件</dt><dd class="formRight"><div class="inputCont"><input name="" type="text" class="textBox box280" readonly></div><div class="fileUpload"><a href="javascript:void(0);" class="btnUpload btnSc"><input name="" type="file">请选择</a></div><div class="clear"></div></dd><dd class="clear"></dd><dd class="new2"><div class="btnArea"><a href="javascript:void(0);"  class="btnDelZm">删除</a><a href="javascript:void(0);"  class="btnUpZm">添加文件</a></div></dd><div class="clear"></div>');
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