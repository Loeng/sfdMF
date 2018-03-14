var area;
(function(){
	var thisCallback=null;
	var _this=null;
	var thisInterval=" ";   //省市之间的 间隔符
	var falg = false;
	var html=" <div class='gg_area'>"+
				    "<div class='area_box'>"+
					    "<div class='zimu'>A-G</div>"+
					    "<ul id='ag'></ul>"+
				    "</div>"+
				    "<div class='area_box'>"+
					    "<div class='zimu'>H-N</div>"+
					    "<ul id='hn'></ul>"+
				    "</div>"+
				    "<div class='area_box'>"+
					    "<div class='zimu'>O-Z</div>"+
					    "<ul id='oz'></ul>"+
					"</div>"+
				"</div>";
	function getArea(element,interval,callback){
		_this=element;
		if(interval){
			thisInterval=interval;
		}
		$(element).next(".gg_area").remove(); //先去掉已添加的html
		$(element).after(html); //给页面添加基本的html
		loadSystemArea(0);
		if(typeof callback == 'function'){
			thisCallback=callback;
		}
	
	}
	area=getArea;
	
	//是否在选择地区的这个div上
    var isOut=true;
	
	$(".gg_area").live('mouseout',function(){
	        isOut=true; //不在div上
    });
	
	$(".gg_area").live('mouseover',function(){
	        isOut=false;//在div上
    });

	document.onmousedown=function(){
	   if(isOut){
			$(".gg_area").hide();
	   }
	};
	
	$(".area_two label").live('click',function(event){
//		if(event.type=='mouseenter'){ 
			$(this).addClass("xz_label").siblings().removeClass("xz_label");
//		}else{
//			//等待加样式　取消该div显示
//		}
		
	});
	$(".area_two label strong").live('click',function(event){
//		if(event.type=='mouseenter'){ 
			var parentAreaID=this.id;
			loadArea(this,parentAreaID);
			
//		}
	});
	
	
	$(".area_three b").live('click',function(){
		$(_this).val("");
		var area=$(this).text();
		var city=$(this).parent("div").prev().text();
		var province=$(this).parent().parent("label").parent("div").prev().text();
		var areaID=this.id;
		var cityID=$(this).parent("div").prev()[0].id;
		var provinceID=$(this).parent().parent("label").parent("div").prev()[0].id;
		$(_this).val(province+thisInterval+city+thisInterval+area);
		
		$(this).parents("div.gg_area").hide();
		$(this).parents("li").removeClass("xz_li");
		
		if(thisCallback!=null){
			thisCallback();
		}
	});
	
	//判断鼠标　hover到的省级地区的  增加样式
	$(".area_box ul li").live('hover',function(event){ 
		if(event.type=='mouseenter'){ 
			$(this).addClass("xz_li");
		}else{ 
			$(this).removeClass("xz_li");	
			$(this).children().children("label").removeClass("xz_label");
		} 
	});
	
	//判断鼠标　hover到的省级地区　获取的值并加载下一级　　和上面的方法配合使用
	$(".area_box ul li span").live('hover',function(event){
		if(event.type=='mouseenter'){ 
			 var parentAreaID=this.id;
		     loadCity(this,parentAreaID);
		}
	});
	
	//省
	function  loadSystemArea(){
		   var ctxpath=$("#ctxpath").val();
		   $.post(
			    ctxpath+"/area/sel/province/000000",
			 	function(data){
					var systemAreas = jQuery.parseJSON(data);
					var agArea ='';
			 		var hnArea ='';
			 		var ozArea ='';
		 			for(var i=0;i<systemAreas.length;i++) {
		 				var area=systemAreas[i];
		 				var temp= '<li><span id="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</span></li>';
		 				if(area.pingYin<="G"){
		 					agArea += temp;
		 				}else if(area.pingYin<="N"){
		 					hnArea += temp;
		 				}else {
		 					ozArea += temp;
		 				}
			 		}
			 		$('#ag').html(agArea);
			 		$('#hn').html(hnArea);
			 		$('#oz').html(ozArea);
			 		
			 		$(".gg_area").show();
			 	}
		   );
	}
	//市
	function loadCity(_this,parentID){
		  var ctxpath=$("#ctxpath").val();
		   $.post(
				ctxpath+"/area/sel/province/"+parentID,
			 	function(data){
					var systemAreas = jQuery.parseJSON(data);
					if(systemAreas.length){
						var tmepData ='';
						tmepData+='<div class="area_two">';
					    
						for(var i=0;i<systemAreas.length;i++) {
							tmepData += '<label><strong id="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</strong></label>';
						}
						tmepData+="</div>";
						$(_this).next(".area_two").remove();//先清楚以前添加的元素
						if(systemAreas.length!=0){
							$(_this).parent().append(tmepData);
						}
					}else{
						$(".area_box ul li span").bind("click",function(){
							var province=$(this).text();
							$("#pca").val(province);
							$(this).parents("div.gg_area").hide();
							$(this).parents("li").removeClass("xz_li");
							if(thisCallback!=null){
								thisCallback();
							}
						});
					}
			 		
				}
			);
	}
	
	//区
	function loadArea(_this,parentID){
		falg = false;
		  var ctxpath=$("#ctxpath").val();
		   $.post(
				ctxpath+"/area/sel/province/"+parentID,
			 	function(data){
					var systemAreas = jQuery.parseJSON(data);
					if(systemAreas.length){
						if(systemAreas != ""){
					 		var tmepData ='';
							tmepData+='<div class="area_three">';
							for(var i=0;i<systemAreas.length;i++) {
								tmepData += '<b id="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</b>';
							}
							tmepData+="</div>";
							$(_this).next(".area_three").remove(); //先清楚以前添加的元素
							
							if(systemAreas.length!=0){
								$(_this).parent().append(tmepData);
							}
								falg = true;
						}else{
							falg = false;
						}
					}else{
						$(".area_box ul li strong").bind("click",function(){
							var city=$(this).text();
							var province=$(this).parent("label").parent("div").prev().text();
							$("#pca").val(province+thisInterval+city);
							$(this).parents("div.gg_area").hide();
							$(this).parents("li").removeClass("xz_li");
							if(thisCallback!=null){
								thisCallback();
							}
						});
					}
						
				}
			);
	}
})();

//var area;
//(function(){
//	var thisCallback=null;
//	var _this=null;
//	var thisInterval=" ";   //省市之间的 间隔符
//	var html=" <div class='gg_area'>"+
//				    "<div class='area_box'>"+
//					    "<div class='zimu'>A-G</div>"+
//					    "<ul id='ag'></ul>"+
//				    "</div>"+
//				    "<div class='area_box'>"+
//					    "<div class='zimu'>H-N</div>"+
//					    "<ul id='hn'></ul>"+
//				    "</div>"+
//				    "<div class='area_box'>"+
//					    "<div class='zimu'>O-Z</div>"+
//					    "<ul id='oz'></ul>"+
//					"</div>"+
//				"</div>";
//	function getArea(element,interval,callback){
//		_this=element;
//		if(interval){
//			thisInterval=interval;
//		}
//		$(element).next(".gg_area").remove(); //先去掉已添加的html
//		$(element).after(html); //给页面添加基本的html
//		loadSystemArea(0);
//		if(typeof callback == 'function'){
//			thisCallback=callback;
//		}
//	
//	}
//	area=getArea;
//	
//	//是否在选择地区的这个div上
//    var isOut=true;
//	
//	$(".gg_area").live('mouseout',function(){
//	        isOut=true; //不在div上
//    });
//	
//	$(".gg_area").live('mouseover',function(){
//	        isOut=false;//在div上
//    });
//
//	document.onmousedown=function(){
//	   if(isOut){
//			$(".gg_area").hide();
//	   }
//	}
//	
//	$(".area_two label").live('hover',function(event){
//		if(event.type=='mouseenter'){ 
//			$(this).addClass("xz_label").siblings().removeClass("xz_label");
//		}else{
//			//等待加样式　取消该div显示
//		}
//		
//	});
//	$(".area_two label strong").live('hover',function(event){
//		if(event.type=='mouseenter'){ 
//			var parentAreaID=this.id;
//			loadArea(this,parentAreaID);
//		}
//	});
//	
//	
//	$(".area_three b").live('click',function(){
//		$(_this).val("");
//		var area=$(this).text();
//		var city=$(this).parent("div").prev().text();
//		var province=$(this).parent().parent("label").parent("div").prev().text();
//		
//		var areaID=this.id;
//		var cityID=$(this).parent("div").prev()[0].id;
//		var provinceID=$(this).parent().parent("label").parent("div").prev()[0].id;
//		
//		$(_this).val(province+thisInterval+city+thisInterval+area);
//		
//		$(this).parents("div.gg_area").hide();
//		$(this).parents("li").removeClass("xz_li");
//		
//		if(thisCallback!=null){
//			thisCallback();
//		}
//		
//		
//	});
//	
//	//判断鼠标　hover到的省级地区的
//	$(".area_box ul li").live('hover',function(event){ 
//		if(event.type=='mouseenter'){ 
//			$(this).addClass("xz_li");
//		}else{ 
//			$(this).removeClass("xz_li");	
//			$(this).children().children("label").removeClass("xz_label");
//		} 
//	}) 
//	
//	//判断鼠标　hover到的省级地区　获取的值并加载下一级　　和上面的方法配合使用
//	$(".area_box ul li span").live('hover',function(event){
//		if(event.type=='mouseenter'){ 
//			 var parentAreaID=this.id;
//		     loadCity(this,parentAreaID);
//		}
//	});
//	
//	//省
//	function  loadSystemArea(){
//		   var ctxpath=$("#ctxpath").val();
//		   $.post(
//			    ctxpath+"/area/sel/province/0",
//			 	function(data){
//					var systemAreas = jQuery.parseJSON(data);
//			 		var agArea ='';
//			 		var hnArea ='';
//			 		var ozArea ='';
//		 			for(var i=0;i<systemAreas.length;i++) {
//		 				var area=systemAreas[i];
//		 				var temp= '<li><span id="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</span></li>';
//		 				if(area.pingYin<="G"){
//		 					agArea += temp;
//		 				}else if(area.pingYin<="N"){
//		 					hnArea += temp;
//		 				}else {
//		 					ozArea += temp;
//		 				}
//			 		}
//			 		$('#ag').html(agArea);
//			 		$('#hn').html(hnArea);
//			 		$('#oz').html(ozArea);
//			 		
//			 		$(".gg_area").show();
//			 	}
//		   );
//	}
//	//市
//	function loadCity(_this,parentID){
//		
//		  var ctxpath=$("#ctxpath").val();
//		   $.post(
//				ctxpath+"/area/sel/province/"+parentID,
//			 	function(data){
//					var systemAreas = jQuery.parseJSON(data);
//			 		var tmepData ='';
//					tmepData+='<div class="area_two">';
//				    
//					for(var i=0;i<systemAreas.length;i++) {
//						tmepData += '<label><strong id="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</strong></label>';
//					}
//					tmepData+="</div>";
//					$(_this).next(".area_two").remove();//先清楚以前添加的元素
//					if(systemAreas.length!=0){
//						$(_this).parent().append(tmepData);
//					}
//					
//					
//				}
//			)
//	}
//
//	//区
//	function loadArea(_this,parentID){
//		
//		  var ctxpath=$("#ctxpath").val();
//		   $.post(
//				ctxpath+"/area/sel/province/"+parentID,
//			 	function(data){
//					var systemAreas = jQuery.parseJSON(data);
//			 		var tmepData ='';
//					tmepData+='<div class="area_three">';
//					for(var i=0;i<systemAreas.length;i++) {
//						tmepData += '<b id="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</b>';
//					}
//					tmepData+="</div>";
//					$(_this).next(".area_three").remove(); //先清楚以前添加的元素
//					if(systemAreas.length!=0){
//						$(_this).parent().append(tmepData);
//					}
//					
//				}
//			)
//	}
//	
//	
//})()