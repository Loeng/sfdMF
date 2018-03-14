/* 文本框高度自适应 */
(function($){
	$.fn.autoTextarea = function(options) {
		var defaults={
			maxHeight:360, // 默认最大高度
			minHeight:$(this).height() // 默认最小高度，也就是文本框最初的高度，当内容高度小于这个高度的时候，文本以这个高度显示
		};
		var opts = $.extend({},defaults,options);
		return $(this).each(function() {
			$(this).bind("paste cut keydown keyup focus blur",function(){
				var height,style=this.style;
				this.style.height =  opts.minHeight + 'px';
				if (this.scrollHeight > opts.minHeight) {
					if (this.scrollHeight > opts.maxHeight) {
						height = opts.maxHeight;
						style.overflowY = 'scroll';
					} else {
						height = this.scrollHeight;
						style.overflowY = 'hidden';
					}
					style.height = height  + 'px';
				}
			});
		});
	};
})(jQuery);

$(document).ready(function(){
	
	
	$("input.i_bg").focus(function (){ 
			$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 
	/* 文本框自适应高度 */
	$("textarea").autoTextarea({maxHeight:360});
	/* 按钮浮动定位 */
	$(".ht_btn").next().addClass("afterHt");
});

// 提交通过提示
function formSubmit() {
	econfirm("是否确认审核", submit, null, null, null);
}

function submit(){
	$("#checkStatus").val("2");
	document.getElementById("modifyProductForm").submit();
}

//提交不通过提示
function formSubmitFalse() {
	econfirm("是否确认拒绝", submitFalse, null, null, null);
}

function submitFalse(){
	$("#checkStatus").val("3");
	document.getElementById("modifyProductForm").submit();
}

// 返回列表
function goBack(){
	window.location.href = $("#ctxpath").val() + "/system/wfpSb/list";
}



