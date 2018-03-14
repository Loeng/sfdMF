// JavaScript Document
$(document).ready(function() {
	/*打开模板选择列表*/
	$(".homeDiy").next().css("margin-top","160px");
	$(".navHoverCur").css("top","191px");
	$(".diyOpen").click(function(){
		$(".homeDiy").toggle();
		if($(".homeDiy").css("display")=="block"){
			$(".homeDiy").next().css("margin-top","160px");
			$(".navHoverCur").css("top","191px");
		}
		else{
			$(".homeDiy").next().css("margin-top","0");
			$(".navHoverCur").css("top","31px");
		}
	})
    
	$(".canselBtn").click(function(){
		$(this).parents(".homeDiy").hide();
		$(".homeDiy").next().css("margin-top","0");
		$(".navHoverCur").css("top","31px");
	})
	
    $(".layerControl").parent().css("position","relative");
	
	/*选项卡切换*/
	$(".upTab li").click(function(){
		$(this).addClass("cur");
		$(this).siblings().removeClass("cur");
		var index=$(".upTab li").index(this);
		$(".upTabContent").hide();
		$(".upTabContent").eq(index).show();
		if(index==1){
			$(".upTabContent").eq(1).parents(".homeDiy").next().css("margin-top","195px");
		}
		if(index==0){
			$(".upTabContent").eq(1).parents(".homeDiy").next().css("margin-top","385px");
		}
	})
	
	/*弹出下拉*/
	$(".controlList dt").click(function(){
		$(this).next("dd").addClass("show");
	})
	
	/*触发loading*/
	$(".controlList dd span").click(function(){
		$(this).parents(".layerControl").addClass("layerLoading");
		$(this).parent("dd").removeClass("show");
	})
	
	/*点击外部消失*/
	$(".controlList").click(function(e){
		e?e.stopPropagation():event.cancelBubble = true;
	});
	$(document).click(function(e){
		var a=$(".controlList dd");
		a.removeClass("show");
	})
	
	/*树形菜单*/
	$('.fl_title').click(function(){
		
		$(this).next(".fl_in").slideToggle();
		$(this).toggleClass("ht_fl_zk");
	});
	$('.fl_nav label').click(function(){
		$(".fl_nav label").removeClass("cur");
		$(this).addClass("cur");
		$(this).text("已选")
	});
	
	/*选择广告*/
	$(".ht_list tr").click(function(){
		$(this).find("input[type='radio']").attr("checked",true);
	})
	
	/*二级弹窗*/
	$(".secondAlert").click(function() {
		var obj=$(this).parents(".apLayer").next(".inapLayer");
        obj.show();
		obj.find(".apBg").show();
		obj.find(".apLayer").show();
    });
	
	/*关闭弹窗*/
	$(".apClose").click(function() {
        $(this).parents(".apLayer").hide();
		if($(this).parents(".inapLayer").length>0){
			$(this).parents(".apLayer").prev(".apBg").hide();
			$(this).parents(".inapLayer").hide();
		}
		else{
			$(".apBg").hide();
		}
		
		
    });
});

/*移除配置的JS （清空按钮）*/
function removeConfig(obj,ctxPath,channelId,configType,position){
	var postUrl = ctxPath + "/system/channel/configRemove/" + channelId + "/" + configType + "/" + position;
	$.ajax({ 
		type: "post", 
		url: postUrl, 
		cache:false, 
		async:false, 
		dataType: "text", 
		success: function(returnObj){ 
			if(returnObj == "true"){
				alert('配置清空成功');
				location.reload();
			}else{
				alert('配置清空失败');
			}
		}
	});
}

/*打开配置的弹窗*/
function editConfig(ctxPath,channelId,configType,position){
	var postUrl = ctxPath + "/system/channel/configAlone/" + channelId + "/" + configType + "/" + position;
	$("#showConfig").load(postUrl,function(){
		/*二级弹窗*/
		$(".secondAlert").click(function() {
			$(this).parents(".apLayer").show();
			$(this).parents(".apLayer").next(".inapLayer").show();
			$(this).parents(".apLayer").next(".inapLayer").find(".apBg").show();
			$(this).parents(".apLayer").next(".inapLayer").find(".apLayer").show();
		});
		
			/*关闭弹窗*/
		$(".apClose").click(function() {
			closeWin(this);
		});
		
		$("#windClose").click(function() {
			closeWin(this);
		});
		
	});	
}

/*打开配置广告的选择广告弹窗*/
function configChoseAd(ctxPath){
	var adName = "";
	var typeStr = "";
	if($("#adName").length > 0){
		adName = $("#adName").val();
	}
	if($("#type").length > 0){
		typeStr = $("#type").val();
	}
	
	if(adName == "" || adName == " "){
		adName = null;	
	}
	if(typeStr == "" || typeStr == " "){
		typeStr = null;	
	}
	var postUrl = ctxPath + "/system/channel/config/adChose/" + adName + "/" + typeStr + "/null";
	$("#configChose").load(postUrl,function(){
		/*关闭弹窗*/
		$(".apClose").click(function() {
			closeWin(this);
		});
		
		$("#windClose").click(function() {
			closeWin(this);
		});
		
		/*选择广告*/
		$(".choseAd").click(function(){
			var adId = $(this).children("#choseAdId").val();
			var adName = $(this).children("#choseAdName").val();
			$("#objectId").val(adId);
			$("#objectName").html(adName);
			closeWin(this);
		})
	});	
}

/*选择广告列表页的重置按钮*/
function configChoseAdClear(){
	if($("#adName").length > 0){
		$("#adName").val("");
	}
	if($("#type").length > 0){
		$("#type").val("");
	}
}

/*打开配置商品分类的选择商品分类弹窗*/
function configChoseProductCategory(ctxPath){
	var postUrl = ctxPath + "/system/channel/config/pcChose";
	$("#configChose").load(postUrl,function(){
		/*关闭弹窗*/
		$(".apClose").click(function() {
			closeWin(this);
		});
		
		$("#windClose").click(function() {
			closeWin(this);
		});
		
		/*树形菜单*/
		$('.fl_title').click(function(){
		
			$(this).next(".fl_in").slideToggle();
			$(this).toggleClass("ht_fl_zk");
		});
	
		$('.fl_nav label').click(function(){
			var pcId = $(this).children("#chosePcId").val();
			var pcName = $(this).children("#chosePcName").val();
			$("#objectId").val(pcId);
			$("#objectName").html(pcName);
			closeWin(this);
		});
	});	
}

/*打开配置资讯分类的选择资讯分类弹窗*/
function configChoseContentCategory(ctxPath){
	var postUrl = ctxPath + "/system/channel/config/ccChose";
	$("#configChose").load(postUrl,function(){
		/*关闭弹窗*/
		$(".apClose").click(function() {
			closeWin(this);
		});
		
		$("#windClose").click(function() {
			closeWin(this);
		});
		
		/*树形菜单*/
		$('.fl_title').click(function(){
		
			$(this).next(".fl_in").slideToggle();
			$(this).toggleClass("ht_fl_zk");
		});
	
		$('.fl_nav label').click(function(){
			var ccId = $(this).children("#choseCcId").val();
			var ccName = $(this).children("#choseCcName").val();
			$("#objectId").val(ccId);
			$("#objectName").html(ccName);
			closeWin(this);
		});
	});	
}

/*配置的保存JS*/
function submitConfig(){
	if($("#objectId").val().trim() == "" || $("#objectId").val() == null){
		if($("#objectType").val()=="pc"){
			falert("请选择商品分类！");
		}else if($("#objectType").val()=="ad"){
			falert("请选择广告！");
		}
	}else{
		$("#configForm").ajaxSubmit(
			function(data){
				if(data==true){
					if(!confirm('配置保存成功,是否继续修改？')){
						closeWin($("#configForm"));
						location.reload();
					}
				}else{
					falert("配置保存失败");
				}
		});	
	}
}


/*关闭弹窗JS*/
function closeWin(obj){
	$(obj).parents(".apLayer").hide();
	if($(obj).parents(".inapLayer").length>0){
		$(obj).parents(".apLayer").prev(".apBg").hide();
		$(obj).parents(".inapLayer").hide();
	}else{
		$(".apBg").hide();
	}
}

//保存编辑的频道配置
function saveConfig(channelId,ctxpath){
	var submitUrl = ctxpath + "/system/channel/configSave/" + channelId;
	$.ajax({
		type: "post", 
		url: submitUrl, 
		cache:false, 
		async:false, 
		dataType: "text", 
		success: function(returnObj){ 
			if(returnObj == "true"){
				alert('频道配置保存成功');
			}else{
				alert('频道配置保存失败');
			}
		}
	});
}

//取消编辑频道配置
function cancelConfig(){
	window.close();   
}