
function menuClick(tagTitle, uuid, linkedUrl, contextPath) {
	var tagStatus = createTag(uuid, tagTitle, contextPath);
	if (tagStatus) {
		createContent(uuid, linkedUrl);
	}
	tabs();
	autoTab();
}
function menuClick1(tagTitle, uuid, linkedUrl, contextPath) {
	var u = linkedUrl.split('%2F').join('/');
    var test1 = decodeURI(linkedUrl);
	var tagStatus = createTag(uuid, tagTitle, contextPath);
	if (tagStatus) {
		createContent(uuid, u);
	}
	tabs();
	autoTab();
}

function createTag(uuid, tagTitle, contextPath) {
	var tagElement = document.getElementById(uuid + "Tag");
	var oItem = document.getElementById("tags").getElementsByTagName("li");
	
	if (tagElement == null || oItem == "null") {
		for ( var i = 0; i < oItem.length; i++) {
			var x = oItem[i];
			x.className = "";
		}
		var tagStr = "<li id=\"" + uuid + "Tag\" class=\"cur\" onClick=\"javascript:showTag('"
				   + uuid + "');\" title=\"" + tagTitle + "\">" + tagTitle + "<span><img src=\""
				   + contextPath + "/resources/system/images/bt_del.png\" onclick=\"javascript:removeTags('"
				   + uuid + "');\"></span></li>";
		$("#tags").append(tagStr);
	} else {
		for ( var i = 0; i < oItem.length; i++) {
			var x = oItem[i];
			x.className = "";
		}
		tagElement.className = "cur";
	}
	return true;
}

function createContent(uuid, linkedUrl) {
	var contentElement = document.getElementById(uuid + "Content");
	var oItem = document.getElementById("mainContent").getElementsByTagName(
			"div");
	if (contentElement == null || oItem == "null") {
		for ( var i = 0; i < oItem.length; i++) {
			var x = oItem[i];
			x.style.display = "none";
		}
		var contentStr = "<div id=\"" + uuid + "Content\">";
		contentStr = contentStr + "<iframe src=\"" + linkedUrl + "\" name=\"" + uuid + "Frame\" id=\""
				   + uuid
				   + "Frame\" class='contentIframe' frameborder=\"0\" scrolling=\"auto\" width=\"100%\" height="
				   + (parseInt($(document).height()) - 51) + "></iframe>";
		contentStr = contentStr + "</div>";
		$("#mainContent").append(contentStr);
	} else {
		for ( var i = 0; i < oItem.length; i++) {
			var x = oItem[i];
			x.style.display = "none";
		}
		contentElement.style.display = "";
		window.frames[uuid + "Frame"].location.reload();
	}
}

function showTag(uuid) {
	if(uuid=="SYSTEM_CONTENT_MANAGER_MSG"){
		window.frames[uuid + "Frame"].location.reload();
	}
		var tagElement = document.getElementById(uuid + "Tag");
		var oItem = document.getElementById("tags").getElementsByTagName("li");
		if (tagElement != null && tagElement != "null") {
			for ( var i = 0; i < oItem.length; i++) {
				var x = oItem[i];
				x.className = "";
			}
			tagElement.className = "cur";
		}
		showContent(uuid);
	
}

function removeTags(uuid) {
	var tagElement = document.getElementById(uuid + "Tag");
	var contentElement = document.getElementById(uuid + "Content");
	if (contentElement != null) {
		contentElement.parentNode.removeChild(contentElement);
	}
	if (tagElement != null) {
		tagElement.parentNode.removeChild(tagElement);
		tabs();
	}
	var tagOItem = document.getElementById("tags").getElementsByTagName("li");
	for ( var i = 0; i < tagOItem.length; i++) {
		var x = tagOItem[i];
		x.className = "";
	}
	var tagE = tagOItem[tagOItem.length - 1];
	tagE.className = "cur";
	var uuid = tagE.id;
	if (uuid != null) {
		uuid = uuid.substr(0, uuid.length - 3);
		showContent(uuid);
	}
}

function showContent(uuid) {
	var contentElement = document.getElementById(uuid + "Content");
	var contentOItem = document.getElementById("mainContent").getElementsByTagName("div");
	if (contentOItem != "null" && contentElement != null) {
		for ( var i = 0; i < contentOItem.length; i++) {
			var x = contentOItem[i];
			x.style.display = "none";
		}
		contentElement.style.display = "";
	}
}

$(function() {
	// 详情页左侧商品分类 显示、隐藏
	$(".ht_menu li span").click(function() {
		$(this).parent().addClass("li_show").siblings().removeClass("li_show");
	});
	$(".ht_menu li div dl dt").click(function() {
		$(this).parent().siblings().removeClass("dl_show");
		$(this).parents("dl").siblings().children("dd").slideUp();
		$(this).parent().toggleClass("dl_show");
		$(this).nextAll().slideToggle();
	});
})

/* 标签页切换 */
function tabs() {
	var outerWidth = 0;
	var ulWidth = $(".ht_bt").width() - 50;
	for (i = 0; i < $("#tags li").length; i++) {
		outerWidth += $("#tags li").eq(i).outerWidth() + 1;
	}
	if (outerWidth < ulWidth) {
		$("#tags").css("width", ulWidth + "px");
	} else {
		$("#tags").css("width", outerWidth + "px");
	}
	$(".ht_bt .tabList").css("width", ulWidth + "px");
}

var newLeft = 0;
var outerWidth = parseInt($("#tags").css("width"));
function autoTab() {
	var outerWidth = parseInt($("#tags").css("width"));
	var ulWidth = $(".ht_bt").width() - 50;

	var ofLeft = $("#tags li.cur").position().left + $("#tags li.cur").outerWidth() + 1;
	if (ofLeft > ulWidth) {
		var theCount = ofLeft - ulWidth;
		$("#tags").animate({
			left : "-" + theCount + "px"
		});
	}
	if (ofLeft < ulWidth) {
		$("#tags").animate({
			left : "0px"
		});
	}
}

window.onresize = function() {
	tabs();
	autoTab();
	$(".contentIframe").attr("height", window.innerHeight - 51);
}

$(document).ready(function() {
	tabs();
	$(".nextTabs").click(function() {
		var outerWidth = parseInt($("#tags").css("width"));
		var ulWidth = $(".ht_bt").width() - 50;
		var left = parseInt($("#tags").css("left"));
		if (outerWidth <= ulWidth) {
			return false;
		}
		if (left == 0) {
			$("#tags").animate({
				left : "-" + ulWidth + "px"
			});
		}
		if (Math.abs(left - ulWidth) < outerWidth) {
			$("#tags").animate({
				left : (left - ulWidth) + "px"
			}, "fast");
		} else {
			$("#tags").animate({
				left : "0px"
			});
		}
	})
	$(".prevTabs").click(function() {
		var outerWidth = parseInt($("#tags").css("width"));
		var ulWidth = $(".ht_bt").width() - 50;
		var left = parseInt($("#tags").css("left"));
		if (left == 0) {
			return false;
		}
		if (Math.abs(left) <= ulWidth) {
			$("#tags").animate({
				left : "0px"
			}, "fast");
		} else {
			$("#tags").animate({
				left : (left + ulWidth) + "px"
			}, "fast");
		}
	})
});