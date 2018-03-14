// JavaScript Document
$(document).ready(function() {
	/*树形菜单*/
	$('.fl_title').click(function(){
		$(this).nextAll(".fl_in").slideToggle();
		$(this).toggleClass("ht_fl_zk");
	});
	$('.fl_nav label').click(function(){
		$(".fl_title label").removeClass("cur");
		$(".fl_title label").text("选择");
		$(this).addClass("cur");
		$(this).text("已选");
	})
	/*复选框*/
	$("input[type='checkbox']").change(function(){
		if($(this).attr("checked")){
			$(this).parent().parent().addClass("checked");
		}
		else{
			$(this).parent().parent().removeClass("checked");
		}
	})
	
	/*单选框*/
	$("input[type='radio']").change(function() {
		$("span.zt").removeClass("radioSel");
        $(this).parents(".zt").addClass("radioSel");
    });
	
	/*关闭弹窗*/
	$(".del_list").click(function(){
		$(".del_tc").show();
		$(".del_tcBg").show();
	});
	$(".del_tc .tit span").click(function(){
		$(".del_tc").hide();
		$(".del_tcBg").hide();
	});
	
	/*文本框自适应高度*/
	$("textarea").autoTextarea({maxHeight:360});
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
	/*编辑器分页*/
	$(".addEditor").click(function(){
		var obj=$(this).parents(".editorPage").prev(".formBoxLine").children("dd").eq(0).clone();
		obj.html("<textarea></textarea>");
		var obj2=$(this).parents(".editorPage").prev(".formBoxLine").children(".clear")
		obj2.before(obj);
		$(this).parents(".editorPage").prev(".formBoxLine").children("dd").hide();
		obj2.show()
		obj.show();
		var index=$(this).parents(".editorPage").prev(".formBoxLine").children("dd").index(obj);
		var size=$(this).next(".pageSize").children("li").eq(0).clone();
		size.children(".btn").text(index+1);
		$(this).next(".pageSize").append(size);
		$(this).next(".pageSize").children("li").removeClass("cur");
		$(this).next(".pageSize").children("li").eq(index).addClass("cur");
	})
	$(".removePage").live("click",function(){
		var index=$(this).parents(".pageSize").children("li").index($(this).parent());
		var a=$(this).parents(".editorPage").prev(".formBoxLine").children("dd").eq(index);
		var b=$(this).parent().nextAll("li").find(".btn");
		b.each(function() {
            var eq=parseInt($(this).text());
			eq--;
			$(this).text(eq);
        });
		if($(this).parent().attr("class")=="cur"){
			a.prev().show();
			$(this).parent().prev().addClass("cur");
		}
		a.remove();
		$(this).parent("li").remove();
		
		
	})
	
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
