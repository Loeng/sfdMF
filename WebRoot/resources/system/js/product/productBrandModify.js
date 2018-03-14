// 重置
function reset(){
	document.getElementById("modifyProductBrandForm").reset();
}


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
	if(nameStr.length < 1 || nameStr.length >20){
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
	if(nameStr.length < 1 || nameStr.length >20){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("品牌名为1-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}

// 验证上传图片格式
function validateForm(modify) {
	// 可不上传图标，直接返回true
	if (modify.bigPicture.value == ""){
		return true;
	}     
	//截取提交上传文件的扩展名  
	var ext = modify.bigPicture.value.match(/^(.*)(\.)(.{1,8})$/)[3];
	ext = ext.toLowerCase(); //设置允许上传文件的扩展名           
	if (ext ==  "jpg" || ext == "gif" || ext=="png") {
		return true;
	} else {
		alert("只允许上传 .jpg或gif 或png文件，请重新选择需要上传的文件！");
		return false;
	}
}
// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/system/product/brand/list";
	}
	window.location.href=contextPath + "/system/product/brand/list";
}
//提交
function formSubmit() {
	if(checkNameBlur()){
		document.getElementById("modifyProductBrandForm").submit();
	}else{
		checkNameBlurc();
	}
}

$(function(){
	if($("#modifyOk").val()=="true"){
		econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
	}else if(!$("#modifyOk").val()=="false"){
		ealert("修改失败！");
	}
	$("input.i_bg").focus(function (){ 
			$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 
});	