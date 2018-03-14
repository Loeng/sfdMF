$(document).ready(function() {
	/*选项卡切换*/
	$(".saveHeader a").click(
		function(){
			$(this).parent().children("a").removeClass("nowItem");
			$(this).addClass("nowItem");
		}
	)
	
	/*列表行变色*/
	$(".saveList").hover(
		function(){
			$(this).toggleClass("listHover");
		}
	)
	
	/*店铺收藏名称隐现*/
	$(".shopInfo").hover(
		function(){
			$(this).children("span").slideToggle(200);
		}
	)
})

//全选与全不选
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
function deleteProductCollectAll(contextPath){
 	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){  
		if(items[i].type=="checkbox"&&items[i].checked){
	    	productCollectDelete1(items[i].value,contextPath);
	    }
	} 
	salert("删除成功");
}
//用于批量删除会员时的删除方法(避免不断弹出删除成功的提示)
function productCollectDelete1(id,contextPath){
	$.post(
		contextPath + "/user/productCollect/delete/"+id,
	 	function(data){
	 		if(data){
	 		var d="#del"+id;
		 		$(d).parent().parent().remove();
		 		var div = "#div"+id;
		 		$(div).remove();	
	 		}
	 	}
	);
}
//删除会员
function productCollectDelete(id,contextPath){
	
	$.post(
		contextPath + "/user/productCollect/delete/"+id,
	 	function(data){
	 		if(data){
	 			salert("删除成功");
	 			var d="#del"+id;	 			
	 			$(d).parent().parent().remove();
	 			var div = "#div"+id;
		 		$(div).remove();
	 		}else{
	 			falert("删除失败");
	 		}
	 	}
	);
}

//删除前验证
function checkProductCollectSelect(){
	var selected = false;
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){
		if(items[i].type=="checkbox"&&items[i].checked){
			selected = true;
		}
	} 
	
	if(selected) {
		econfirm('是否确认删除？',deleteProductCollectAll,[$("#ctxpath").val()],null,null);
	}else {
		falert("至少一条");
	}
}

//load店铺收藏页面
function loadStoreCollect(){
	
	$("#userSave").load($("#ctxpath").val()+"/user/storeCollect/load",function(){
		/*选项卡切换*/
    	$(".saveHeader a").click(
    		function(){
    			$(this).parent().children("a").removeClass("nowItem");
    			$(this).addClass("nowItem");
    		}
    	)
    	
    	/*列表行变色*/
    	$(".saveList").hover(
    		function(){
    			$(this).toggleClass("listHover");
    		}
    	)
    	
    	/*店铺收藏名称隐现*/
    	$(".shopInfo").hover(
    		function(){
    			$(this).children("span").slideToggle(200);
    		}
    	)
	});
}
	
//load商品收藏页面
function loadProductCollect(){
	
	$("#userSave").load($("#ctxpath").val()+"/user/productCollect/load",function(){
		/*选项卡切换*/
    	$(".saveHeader a").click(
    		function(){
    			$(this).parent().children("a").removeClass("nowItem");
    			$(this).addClass("nowItem");
    		}
    	)
    	
    	/*列表行变色*/
    	$(".saveList").hover(
    		function(){
    			$(this).toggleClass("listHover");
    		}
    	)
    	
    	/*店铺收藏名称隐现*/
    	$(".shopInfo").hover(
    		function(){
    			$(this).children("span").slideToggle(200);
    		}
    	)
    
	});
}

//删除选择的记录
function deleteAllStoreCollect(contextPath){
 	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){  
		if(items[i].type=="checkbox"&&items[i].checked){
	    	storeCollectDelete1(items[i].value,contextPath);
	    }
	} 
	salert("删除成功");
}
//用于批量删除会员时的删除方法(避免不断弹出删除成功的提示)
function storeCollectDelete1(id,contextPath){
	$.post(
		contextPath + "/user/storeCollect/delete/"+id,
	 	function(data){
	 		if(data){
	 		var d="#del"+id;
		 		$(d).parent().parent().remove();
	 		}
	 	}
	);
}
//删除会员
function storeCollectDelete(id,contextPath){
	
	$.post(
		contextPath + "/user/storeCollect/delete/"+id,
	 	function(data){
	 		if(data){
	 			salert("删除成功");
	 			var d="#del"+id;
	 			$(d).parent().parent().remove();	 			
	 		}else{
	 			falert("删除失败");
	 		}
	 	}
	);
}

//删除前验证
function checkStoreCollectSelect(){
	var selected = false;
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){
		if(items[i].type=="checkbox"&&items[i].checked){
			selected = true;
		}
	} 
	
	if(selected) {
		econfirm('是否确认删除？',deleteAllStoreCollect,[$("#ctxpath").val()],null,null);
	}else {
		salert("至少一条");
	}
}

//购买
function productPay(pId,contextPath){
	window.location.href= contextPath + "/web/proDetail/"+pId;
}
//批量加入购物车前的验证
function checkAddShopCartSelect(){
	var selected = false;
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){
		if(items[i].type=="checkbox"&&items[i].checked){
			selected = true;
		}
	} 
	
	if(selected) {
		econfirm('是否加入购物车？',addShopCartAll,[$("#ctxpath").val()],null,null);
	}else {
		falert("至少选择一条");
	}
}

//批量加入购物车
function addShopCartAll(contextPath){
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){  
		if(items[i].type=="checkbox"&&items[i].checked){
	    	addShopCart(items[i].value,contextPath);
	    }
	} 
	salert("加入成功");
}
//单个商品加入购物车
function addShopCart(id,contextPath){
	$.post(
			contextPath + "/user/shopCart/add/"+id+"/"+1,
		 	function(data){
		 		if(data){
		 		
		 		}
		 	}
		);
}










	