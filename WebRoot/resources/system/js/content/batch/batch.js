// JavaScript Document
// 显示下级菜单
function showChilds(id){
	var d = "#"+id;
	var p = "#p"+id;
	$(p).next("div").slideToggle();
	$(p).toggleClass("ht_fl_zk");
	$(d).load($("#ctxpath").val()+"/system/content/batch/child/"+id);
}

function selectCategory(id){
	var l = "#l"+id;
	$(".fl_nav label").removeClass("cur");
	$(l).addClass("cur");
	$(".fl_nav label").text("选择");
	$(l).text("已选");
}
//搜索分类下的资讯
function showContent(id){
	// 将分类id放入隐藏域，便于添加子分类引用id
	$("#downId").val(id);
	var l = "#l"+id;
	$(".fl_nav label").removeClass("cur");
	$(l).addClass("cur");
	$(".fl_nav label").text("选择");
	$(l).text("已选");
	if(null != id && "" != id){
		$("#contents1").load($("#ctxpath").val()+"/system/content/batch/getContentsByCategoryId/"+id+"/1",
				function(){
					$("#countTop").text($("#totalRowTop").val());
			});
	}
}
/**
 * 搜索框表单重置
 * @param id 表单ID
 */
function formReset(){
	$("#contentsForm")[0].reset();
	$("#categoryId").val("");
}

/**
 * 搜索框表单提交
 * @param id 表单ID
 * @param tableId tbodyId
 */
function formSubmit(){
	var _categoryId = $("#downId").val();//获取左侧分类树选中的分类ID
	if(null == _categoryId || "" == _categoryId){
		ealert("请选择资讯分类");
		return;
	}
	var _ctxpath = $("#ctxpath").val();
	$("#contents2").load(_ctxpath+"/system/content/batch/searchContents",
		$("#contentsForm").serialize(),
		function(){
			$("#countBot").text($("#totalRowBot").val());
	});
}

// 上部全选
function selectAllTop(){
	var items = document.getElementsByName("checkbox1");
	// 定义是否已经全选
	var seleced = true;
	// 遍历选择框，看是否已经全选
	for(var i=0;i<items.length;i++){  
    	if(!items[i].checked){
    		seleced = false;
    		break;
    	}
	}
	// 如果已经全选，则全取消，否则全选
	if(seleced){
		for(var i=0;i<items.length;i++){  
	        items[i].checked = false;
	    }
	}else{
	    for(var i=0;i<items.length;i++){  
	        items[i].checked = true;
	    }
	}
}

// 下部分全选
function selectAllBot(){
	var items = document.getElementsByName("checkbox2");
	// 定义是否已经全选
	var seleced = true;
	// 遍历选择框，看是否已经全选
	for(var i=0;i<items.length;i++){  
    	if(!items[i].checked){
    		seleced = false;
    		break;
    	}
	}
	// 如果已经全选，则全取消，否则全选
	if(seleced){
		for(var i=0;i<items.length;i++){  
	        items[i].checked = false;
	    }
	}else{
	    for(var i=0;i<items.length;i++){  
	        items[i].checked = true;
	    }
	}
}
/**
 * 资讯下移
 */
function moveDown(){
	var _ctxpath = $("#ctxpath").val();
	var checkboxs = $("input[name='checkbox1']:checked");//获取已选择的复选框集合
	var length = checkboxs.length;
	if(0 == length){
		ealert("请选择要移动的资讯");
		return;
	}
	var categoryId = $("#downId").val();//获取左侧分类树选中的分类ID
	if(null == categoryId || "" == categoryId){
		ealert("请选择资讯分类");
		return;
	}
	for(var i=0;i<length;i++){
		var checkbox =  checkboxs[i];
//		$("#"+checkbox.value).remove();//删除本来节点元素
//		$(checkbox).attr("name","checkbox2");//设置该checkbox name值为下方的name值
//		//将该行资讯数据添加至下方列表tbody中
//		$("#contents2").append("<tr id=\""+checkbox.value+"\">"+$(checkbox).parent().parent().html()+"</tr>");//$(checkbox).parent().parent().html()获取到td节点元素
		//更新到服务器端
		$.post(
			    _ctxpath+"/system/content/batch/moveContent/"+categoryId+"/"+checkbox.value+"/down",
			    function(result){
			    	if(result == 'fail'){
			    		ealert("操作失败");
			    		return;
			    	}else{
			    		// 上下两个列表重新load
			    		$("#contents1").load(_ctxpath+"/system/content/batch/getContentsByCategoryId/"+categoryId+"/1",
			    				function(){
			    					$("#countTop").text($("#totalRowTop").val());
			    			});
			    		$("#contents2").load(_ctxpath+"/system/content/batch/searchContents",
			    				$("#contentsForm").serialize(),
			    				function(){
			    					$("#countBot").text($("#totalRowBot").val());
			    			});
			    	}
			    }
			);
	}
}

/**
 * 资讯上移
 */
function moveUp(){
	var _ctxpath = $("#ctxpath").val();
	var checkboxs = $("input[name='checkbox2']:checked");//获取已选择的复选框集合
	var length = checkboxs.length;
	if(0 == length){
		ealert("请选择要移动的资讯");
		return;
	}
	var categoryId = $("#downId").val();//获取左侧分类树选中的分类ID
	if(null == categoryId || "" == categoryId){
		ealert("请选择资讯分类");
		return;
	}
	for(var i=0;i<length;i++){
		var checkbox =  checkboxs[i];
//		$("#"+checkbox.value).remove();//删除本来节点元素
//		$(checkbox).attr("name","checkbox1");//设置该checkbox name值为上方的name值
//		$("#tempMsg").remove();
//		//将该行资讯数据添加至上方列表tbody中
//		$("#contents1").append("<tr id=\""+checkbox.value+"\">"+$(checkbox).parent().parent().html()+"</tr>");
		//更新至服务器
		$.post(
			    _ctxpath+"/system/content/batch/moveContent/"+categoryId+"/"+checkbox.value+"/up",
			    function(result){
			    	if(result == 'fail'){
			    		ealert("操作失败");
			    		return;
			    	}else{
			    		// 上下两个列表重新load
			    		$("#contents1").load(_ctxpath+"/system/content/batch/getContentsByCategoryId/"+categoryId+"/1",
			    				function(){
			    					$("#countTop").text($("#totalRowTop").val());
			    			});
			    		$("#contents2").load(_ctxpath+"/system/content/batch/searchContents",
			    				$("#contentsForm").serialize(),
			    				function(){
			    					$("#countBot").text($("#totalRowBot").val());
			    			});
			    	}
			    }
			);
	}
	
}

//第一级分类选择
function selectCategory1(){
	//$("#upId").val(obj.value);//设置搜索框中分类ID至页面 隐藏表单域
	var _ctxpath = $("#ctxpath").val();
	var _id = $("#category1").val();
	$("#select2Span").html("");
	$("#select3Span").html("");
	$("#categoryId").val(_id);
	//从服务器获取选中分类的资讯数据
	$.post(
		_ctxpath+"/system/content/batch/categoryChild",
		{id:_id},
		function(data){
			if(data != null && data != ""){
				var JSONObj = $.parseJSON(data);
				if(JSONObj != null && JSONObj.length > 0){
					// 新建一个select节点,并将该节点添加到原来原来父级select容器内
					var selectObj = "<select id='category2' class='i_bg' onchange='selectCategory2();' style='width:140px'><option>请选择分类</option>";
					// 将查询出来的结果放入该select对象内
					for(var i = 0;i<JSONObj.length;i++){
						var nowCategory = JSONObj[i];
						selectObj += "<option value ='"+nowCategory.id+"'>"+nowCategory.name+"</option>";
					}
					
					$("#select2Span").html(selectObj);
				}
			}
		}
	);
}
//第二级分类选择
function selectCategory2(){
	//$("#upId").val(obj.value);//设置搜索框中分类ID至页面 隐藏表单域
	var _ctxpath = $("#ctxpath").val();
	var _id = $("#category2").val();
	$("#select3Span").html("");
	$("#categoryId").val(_id);
	//从服务器获取选中分类的资讯数据
	$.post(
		_ctxpath+"/system/content/batch/categoryChild",
		{id:_id},
		function(data){
			if(data != null && data != ""){
				var JSONObj = $.parseJSON(data);
				if(JSONObj != null && JSONObj.length > 0){
					// 新建一个select节点,并将该节点添加到原来原来父级select容器内
					var selectObj = "<select id='category3' class='i_bg' onchange='selectCategory3();' style='width:140px'><option>请选择分类</option>";
					// 将查询出来的结果放入该select对象内
					for(var i = 0;i<JSONObj.length;i++){
						var nowCategory = JSONObj[i];
						selectObj += "<option value ='"+nowCategory.id+"'>"+nowCategory.name+"</option>";
					}
					
					$("#select3Span").html(selectObj);
				}
			}
		}
	);
}
// 第三级分类选择
function selectCategory3(){
	var _id = $("#category3").val();
	$("#categoryId").val(_id);
}


//分页跳转
function goPage(pageNum){
	$("#contents1").load($("#ctxpath").val()+"/system/content/batch/getContentsByCategoryId/"+$("#downId").val()+"/"+pageNum,
			function(){
		$("#countTop").text($("#totalRowTop").val());
	});
}

//分页跳转
function goPageNew(pageNum){
	$("#pageNum").val(pageNum);
	$("#contents2").load($("#ctxpath").val()+"/system/content/batch/searchContents",
		$("#contentsForm").serialize(),
		function(){
			$("#countBot").text($("#totalRowBot").val());
		}
	);
}
