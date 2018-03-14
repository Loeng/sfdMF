$(function(){
	$(".ht_list tr").mouseover(function(){
		$(this).addClass("tr_bg")
	});
	$(".ht_list tr").mouseout(function(){
		$(this).removeClass("tr_bg")
	});
	$(".pro_ss dt span.span_down").click(function(){
		$(this).hide();
		$(this).next(".span_up").show();
		$(this).parent().nextAll("dd").hide();
		$(this).parent().parent().next(".pro_ss_btn").hide();
	});
	$(".pro_ss dt span.span_up").click(function(){
		$(this).hide();
		$(this).prev(".span_down").show();
		$(this).parent().nextAll("dd").show();
		$(this).parent().parent().next(".pro_ss_btn").show();
	});
});	
//自动生成序号
window.onload = function(){
	var oTable = document.getElementById("adTab");
	for(var i=1;i<oTable.rows.length;i++){
		oTable.rows[i].cells[1].innerHTML = (i);
		
	}
};
// 全选与全不选
function selectAll(){
	var items = document.getElementsByTagName("input");
	// 定义是否已经全选
	var seleced = true;
	// 遍历选择框，看是否已经全选
    for(var i=0;i<items.length;i++){  
        if(items[i].type=="checkbox"){
        	if(!items[i].checked){
    seleced = false;
    break;
        	}
        }
    }
    // 如果已经全选，则全取消，否则全选
    if(seleced){
    	for(var i=0;i<items.length;i++){  
	        if(items[i].type=="checkbox"){
	        	items[i].checked = false;
	        }
    	}
    }else{
    	for(var i=0;i<items.length;i++){  
	        if(items[i].type=="checkbox"){
	        	items[i].checked = true;
	        }
    	}
    }
    
}
//删除选择的记录
function deleteAll(contextPath){
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){  
		if(items[i].type=="checkbox"&&items[i].checked){
			adDelete1(items[i].value,contextPath);
	    }
	} 
	ealert("删除成功");
}
//用于批量删除广告时的删除方法(避免不断弹出删除成功的提示)
function adDelete1(id,contextPath){
	$.post(
		contextPath + "/system/advert/delete/"+id,
	 	function(data){
	 		if(data){
				 var d="#del"+id;
				 $(d).parent().parent().remove();
				 var oTable = document.getElementById("adTab");
				 for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
	 		}
	 	}
	 );
	 
}
//删除广告
function adDelete(id,contextPath){
	$.post(
		contextPath + "/system/advert/delete/"+id,
	 	function(data){
	 		if(data){
				 ealert("删除成功！");
				 var d="#del"+id;
				 $(d).parent().parent().remove();
				 var oTable = document.getElementById("adTab");
					for(var i=1;i<oTable.rows.length;i++){
						oTable.rows[i].cells[1].innerHTML = (i);
					}
	 		}else{
	 			ealert("删除失败！");
	 		}
	 	}
	);
}
//点击新增按钮，跳转到新增页面
function adAdd(contextPath){
	window.location.href= contextPath + "/system/advert/add";
}
//点击修改按钮，根据id进行广告资料查询
function adModify(id,contextPath){
	window.location.href= contextPath + "/system/advert/detail/"+id;
}

// 预览
function adQuery(id,contextPath){
	$("#apPreview").load(contextPath + "/system/advert/query/"+id,function(data){
		$(".apPreview").show();
		$(".apPreviewBg").show();
	$("body").css("overflow-y","hidden");
		/*横排广告*/
		var imgHeight=$(".hengAdv").height()-2;
		$(".hengAdv img").css("height",imgHeight-30+"px");
		var inWidth=$(".hengAdv img").length*($(".hengAdv img").width()+12);
		$(".hengAdv ul").css("width",inWidth+"px");
		
		/*竖排广告*/
		$(".shuAdv li img").css("width",$(".shuAdv").width()-2+"px");
		
		/*轮播li宽度*/
		$(".bannerPic li").each(function() {
	        var liWidth=$(this).parents(".bannerIn").width();
			$(this).css("width",liWidth+"px");
	    });
		
		var textArray=new Array();
		$(".num li").each(function(index) {
	        textArray[index]=$(this).html();
	    });
		$(".bannerIn").slide({ titCell:".num" , mainCell:".bannerPic" , effect:"fold", autoPlay:true, delayTime:1200 , autoPage:true });
		$(".num li").each(function(index) {
	        $(this).html(textArray[index]);
	    });
		
		
		
		/*图文切换2*/
		var unitHeight=$(".changePic2").height()*0.7-3;
		var titleHeight=$(".changePic2").height()*0.3/$(".advDownList").length;
		$(".advDownList dd").css("height",unitHeight-titleHeight+"px");
		$(".advDownList dt").css("height",titleHeight+"px");
		$(".advDownList dt").css("line-height",titleHeight+"px");
		$(".advDownList dd").eq(0).css("display","block");
		 $(".advDownList dt").hover(function(){
			 $(".advDownList dd").stop(true,true).slideUp(100);
			$(this).next("dd").stop(true,true).slideDown(100);
		})
		

	   var count=$(".text_nav li").length;
	   var kuan=$(".bannerIn").width()-count;
	   $(".text_nav li").css("width",kuan/count+"px");
	});
}

function eyeClose(){
	$(".apPreview").hide();
	$(".apPreviewBg").hide();
	$("body").css("overflow-y","auto");
}

//searchAdForm的提交
function searchSubmit(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchAdForm").submit();
}
// 重置
function resetForm(){
	document.getElementsByName("name")[0].value="";
}
//删除前验证
function checkSelect(){
	var selected = false;
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){
		if(items[i].type=="checkbox"&&items[i].checked){
			selected = true;
		}
	} 
	
	if(selected) {
		econfirm('是否确认删除？',deleteAll,[$("#ctxpath").val()],null,null);
	}else {
		ealert("请至少选择一条数据！");
	}
}

function goPage(num){
	$("#pageNum").val(num);
	$("#searchAdForm").submit();
}