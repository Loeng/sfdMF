$(function(){
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
	
	$("input.i_bg").focus(function ()//得到焦点时触发的事件
	{ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function () //失去焦点时触发的事件
		{ 
		$(this).parent().removeClass("text_ts");
	}); 
});	


$(document).ready(function() {
	/*单选框*/
	$("input[type='radio']").change(function() {
        $(this).parent().parent().addClass("radioSel")
    });
	/*文本框自适应高度*/
	$("textarea").autoTextarea({maxHeight:360});
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});

/*文本框高度自适应*/
(function($){
	$.fn.autoTextarea = function(options) {
		var defaults={
			maxHeight:360, //默认最大高度
			minHeight:$(this).height() //默认最小高度，也就是文本框最初的高度，当内容高度小于这个高度的时候，文本以这个高度显示
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



//验证品牌名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("品牌名为1-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("品牌名为1-20位字符组成");
		return true;
	}
}

//验证品牌名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("品牌名为1-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}

// 验证上传图标格式
function validateForm(add) {
	// 可不上传图标，直接返回true
 	if (add.uploadFile.value == ""){
 			return true;
  	} 
 	// 截取提交上传文件的扩展名  
  	var ext = add.uploadFile.value.match(/^(.*)(\.)(.{1,8})$/)[3];
  	ext = ext.toLowerCase(); // 设置允许上传文件的扩展名   
  	if (ext ==  "jpg" || ext == "gif" || ext=="png") {
  		return true;
  	} else {
  		alert("只允许上传 .jpg或gif 或png文件，请重新选择需要上传的文件！");
  		return false;
  	}
}
// 提交
function formSubmit(){
	if(checkNameBlur()){
		document.getElementById("addProductBrandForm").submit();
	}
}
// 重置
function reset(){
	document.getElementById("addProductBrandForm").reset();
}
		
// 返回列表
function goBack(contextPath){// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/product/brand/list";
	}
	window.location.href=contextPath + "/system/product/brand/list";
}