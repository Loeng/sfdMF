// JavaScript Document
$(document).ready(function() {

	
	/*添加自定义功能*/
	var diy="<div class=\"diy\"></div>";
	var img="<a href=\"javascript:void(0)\" class=\"btnImg\"></a>";
	var background="<a href=\"javascript:void(0)\" class=\"btnBackground\"><em></em></a>";
	var remove="<a href=\"javascript:void(0)\" class=\"btnDelete\"></a>";
	var fontColor="<a href=\"javascript:void(0)\" class=\"btnFontColor\"></a>";
	var add="<a href=\"javascript:void(0)\" class=\"btnAdd\">增加菜单</a>";
	var addText="<a href=\"javascript:void(0)\" class=\"btnAppend\">增加文字</a>";
	var addImg="<a href=\"javascript:void(0)\" class=\"btnAddImg\">增加图片</a>";
	var fontSize="<select name='' class='sizeSel'>";
	fontSize+="<option>12px</option>";
    fontSize+="<option>14px</option>";
    fontSize+="<option>16px</option>";
    fontSize+="<option>18px</option>";
    fontSize+="<option>20px</option>";
    fontSize+="<option>22px</option>";
    fontSize+="<option>24px</option>";
	fontSize+="</select><span>字号：</span>";
	var move="<div class=\"handler\"></div>";
	$(".apDiv").append(diy);
	$(".resizeDiv").append(move);
	/*全部设定*/
	$(".diyAll .diy").append(remove+background+fontColor+add+img);
	/*无增加*/
	$(".diyNoAdd .diy").append(remove+background+fontColor+img);
	/*无图片*/
	$(".diyNoImg .diy").append(remove+background+fontColor+add);
	/*无图片和增加*/
	$(".diySimple .diy").append(remove+background+fontColor);

	/*选择title模板*/
	$(".tempTitle li").each(function() {
		$(this).click(function(){
			$(this).siblings().removeClass("cur");
			$(this).addClass("cur");
			var index=$(".tempTitle li").index(this);
			$(".diyTitle").html($(".titleLayerList").eq(index).html());
			$(".diyTitle .diy").html(remove+background+fontColor+img);
			setColor();
		})
    });
	
	/*打开模板选择列表*/
	$(".diyOpen").click(function(){
		$(".homeDiy").toggle();
		$(this).toggleClass("downListShow");
		$(".nullLayer").toggle();
		var gao=$(".homeDiy").height();
		$(".nullLayer").css("height",gao+"px");
	})
	
	/*选择导航条模板*/
	$(".tempNav li").click(function(){
		$(this).siblings().removeClass("cur");
		$(this).addClass("cur");
	})
	$(".tempNav .temp1").click(function(){
		$(".diySideNav").hide();
	})
	$(".tempNav .temp2").click(function(){
		$(".diySideNav").show();
		$(".diySideNav").removeClass("sideRightList");
		$(".diySideNav").addClass("sideLeftList");
	})
	$(".tempNav .temp3").click(function(){
		$(".diySideNav").show();
		$(".diySideNav").removeClass("sideLeftList");
		$(".diySideNav").addClass("sideRightList");
	})
	
	
	/*选择banner模板*/
	$(".tempBanner li").each(function() {
		$(this).click(function(){
			$(this).siblings().removeClass("cur");
			$(this).addClass("cur");
			var index=$(".tempBanner li").index(this);
			$(".diyBanner").html($(".bannerLayerList").eq(index).html());
			$(".diyBanner .diy").html(remove+background+fontColor+img);
			setColor();
			resize();
		})
    });
	
	/*选择选项卡模板*/
	$(".tempTab li").each(function() {
		$(this).click(function(){
			$(this).siblings().removeClass("cur");
			$(this).addClass("cur");
			var index=$(".tempTab li").index(this);
			$(".footer").before($(".tabsLayerList").eq(index).html());
			$(".tabItem .diy").html(remove+background+fontColor+add);
			$(".tabContentItem .diy").html(remove+background+fontColor+img+addText+addImg+fontSize);
			tabShow();
			setColor();
			resize();
			sel();
		})
    });
	
	/*选择自定义层*/
	$(".tempDiy li").each(function() {
		$(this).click(function(){
			$(this).siblings().removeClass("cur");
			$(this).addClass("cur");
			var index=$(".tempDiy li").index(this);
			$(".pageIn").append($(".diyLayerList").eq(index).html());
			$(".diyLayerItem .diy").html(remove+background+fontColor+img+addText+addImg+fontSize);
			setColor();
			resize();
			sel();
		})
   });
	
	
	/*选择频道链接*/
	$(".diyNav .btnAdd").live("click",function(){
		$(".linkCheckLayer *").show();
	})
	
	/*添加导航菜单项*/
	$(".channelChoseRadio").live("click",function(){
		var items = $(".diyItem");
		var count=items.length;
		if(count<7){
			var channelStr = $(this).val();
			var channeName = "";
			var channelLinkUrl = "";
			
			if(channelStr != null && channelStr.length>0){
					var str = channelStr.split(";");
					if(str != null && str.length >0){
						channeName = str[0];
						channelLinkUrl = str[1];
					}
			}
			
			var flag = true;
			items.each(function(){
				var str=$(this).attr("href")
				if(str==channelLinkUrl){
					flag=false;
				}
			})
			
			if(flag){
				var a=$(".diyItem").eq(0).clone();
				a.children("span").text(channeName);
				a.attr("href",channelLinkUrl);
				$(".diyItem:last").after(a);
				$(".linkCheckLayer *").hide();
				$(this).attr("disabled","disabled")
			}else{
				$(this).attr("disabled","disabled")
				ealert("该频道已添加，请选择其他频道");	
				$(".linkCheckLayer *").hide();
			}
		}else{
			ealert("最多只能添加6个频道");	
			return;
		}
	});
	
	/*添加选项卡菜单项*/
	$(".tabItem .btnAdd").live("click",function(){
		var count=$(this).parents(".apDiv").find(".diyItem2").length;
		if(count<5){
			var a=$(".diyItem2").eq(0).clone();
			var b=$(".tabContentItem").eq(1).clone();
			a.html("<span>制定菜单名</span><em class='editItem'></em><em class='diy'><em href=\"javascript:void(0)\" class=\"btnDelete\"></em></em>");
			$(this).parents(".apDiv").append(a);
			$(this).parents(".tabItem").next(".tabContent").append(b);
		}
		else{
			alert("选项卡菜单上限为5个");
		}
	});
	
	/*编辑选项卡*/
	$(".tabItem .editItem").live("click",function(){
		var value=$(this).parent().text();
		$(this).siblings().remove();
		$(this).parent().append("<input type='text' value="+value+" class='boxInput'><span></span>");
		$(this).siblings(".boxInput").focus();
	})
	
	/*添加文本*/
	$(".btnAppend").live("click",function(){
		var a="<div class='textLine resizeDiv'><textarea cols='' rows='' class='boxInput'></textarea><span></span></div>"
		$(this).parents(".apDiv").append(a);
		$(".boxInput").focus();
	})
	
	
	/*编辑文本域*/
	$(".tabContentItem .editItem,.diyLayerItem .editItem").live("click",function(){
		var value=$(this).parent().text();
		$(this).siblings().remove();
		$(this).parent().append("<textarea cols='' rows='' class='boxInput'>"+value+"</textarea><span></span>");
		$(this).siblings(".boxInput").focus();
	})
	
	/*获取输入的文本内容*/
	$(".boxInput").live("focusout",function(){
		$(this).siblings("span").text($(this).val());
		$(this).parent().append("<em class='editItem'></em><em class='diy'><em href=\"javascript:void(0)\" class=\"btnDelete\"></em></em>");
		$(this).remove();
	})
	/*$("textarea").live("mousedown",function(){
		event.stopPropagation();
	})*/
	
	/*添加图片*/
	$(".btnAddImg").live("click",function(){
		$(this).parent().before("<div class='apDiv resizeDiv'><div class='diy'></div></div>");
		$(this).parent().siblings(".apDiv").children(".diy").html(remove+img);
		$(this).parent().siblings(".apDiv").append(move);
		resize();
	})
	
	
	/*模板列表选项卡*/
	var leftTab=$(".leftTab li")
	var rightTemp=$(".templateList ul");
	leftTab.each(function() {
		$(this).click(function(){
			leftTab.removeClass("cur");
			$(this).addClass("cur");
			var index=leftTab.index(this);
			rightTemp.hide();
			rightTemp.eq(index).show();
		}) 
    });
	
	/*删除层*/
	$(".btnDelete").live("click",function(){
		$(this).parent().parent().remove();
	})

	/*设文字和背景颜色*/
	setColor();
	
	/*下拉选择框激活*/
	sel();
	
	/*缩放层*/
	resize();
	
	
	/*设文字大小*/
	$(".sizeSel").live("change",function() {
        var option=$(this).find("option:selected").text();
		$(this).parent().parent().css("font-size",option);
    });
	
	/*拖动层级排列*/
	$(".resizeDiv").live("mousedown",function(){
		$(".resizeDiv").css("z-index","10");
		$(this).css("z-index","20");
	})
	
	/*选择广告*/
	$(".btnImg").live("click",function(){
		$(".imgCheckLayer *").show();
	})
	
	$(".imgList li").click(function(){
		$(".imgList li").removeClass("cur");
		$(this).addClass("cur");
		alert($(this).find("img").attr("src"));
		$("#imgvalue").val($(this).find("img").attr("src"));
		
	})
	$(".layerClose").click(function(){
		$(this).parents(".alertLayer").hide();
		$(".alertLayerBg").hide();
	})
	
	
	/*选项卡切换*/
	$(".diyItem2").live("click",function(){
		$(this).siblings().removeClass("cur");
		$(this).addClass("cur");
		var content=$(this).parent().next(".tabContent").children(".tabContentItem");
		var index=$(this).parent().children(".diyItem2").index(this);
		content.hide();
		content.eq(index).show();
	})
	
	/*保存DIY*/
	$("#diySave").click(function(){
		$(".diy").remove();
		$(".resizeDiv").removeClass("resizeDiv");
		$(".editItem").remove();
	})
});

function tabShow(){
	$(".tabContent").each(function() {
        $(this).children(".tabContentItem").eq(0).show();
    });
}

function setColor(){
	/*设背景颜色*/
	$(".btnBackground").colorpicker({
	fillcolor:true,
	success:function(o,color){
		$(o).children("em").css("background",color);
		$(o).parent().parent().css("background",color);
	}})
	
	/*设文字颜色*/
	$(".btnFontColor").colorpicker({
	fillcolor:true,
	success:function(o,color){
		$(o).children("em").css("background",color);
		$(o).parent().parent().css("color",color);
	}})
}

/*缩放层*/
function resize(){
	$('.resizeDiv').resizable({
		handler: '.handler',
		min: { width:100, height: 100},
		max: { width:window.innerWidth, height:8000},
	});
	
	$(".handler").mousedown(function() {
		event.stopPropagation();
	});
}

/*下拉选择框激活*/
function sel(){
	$(".sizeSel").mousedown(function() {
		event.stopPropagation();
	});
}

var ie=document.all;
var nn6=document.getElementById&&!document.all;
var isdrag=false;
var y,x;
var oDragObj;

function moveMouse(e) {
if (isdrag) {
oDragObj.style.top = (nn6 ? nTY + e.clientY - y : nTY + event.clientY - y)+"px";
oDragObj.style.left = (nn6 ? nTX + e.clientX - x : nTX + event.clientX - x)+"px";
return false;
}
}

function initDrag(e) {
var oDragHandle = nn6 ? e.target : event.srcElement;
var topElement = "HTML";
while (oDragHandle.tagName != topElement && oDragHandle.className.indexOf("resizeDiv")<0) {
oDragHandle = nn6 ? oDragHandle.parentNode : oDragHandle.parentElement;
}
if (oDragHandle.className.indexOf("resizeDiv")>0) {
isdrag = true;
oDragObj = oDragHandle;
nTY = parseInt(oDragObj.style.top+0);
y = nn6 ? e.clientY : event.clientY;
nTX = parseInt(oDragObj.style.left+0);
x = nn6 ? e.clientX : event.clientX;
document.onmousemove=moveMouse;
return false;
}
}
document.onmousedown=initDrag;
document.onmouseup=new Function("isdrag=false");