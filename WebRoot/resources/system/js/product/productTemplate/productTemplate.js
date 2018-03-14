
//searchProductForm的提交
function searchSubmit(){
	document.getElementById("searchProductForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("name")[0].value="";
}

//点击新增按钮，跳转到新增页面
function productTemplateAdd(contextPath){
	window.location.href= contextPath + "/system/productTemplate/add";
}


//自动生成序号
window.onload = function(){
	var oTable = document.getElementById("productTab");
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

// 删除前验证
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

//用于批量删除商品时的删除方法(避免不断弹出删除成功的提示)
function productDelete1(id,contextPath){
	$.post(
		contextPath + "/system/productTemplate/delete/"+id,
		function(data){
			if(data){
				var d="#del"+id;
				$(d).parent().parent().remove();
				var oTable = document.getElementById("productTab");
				for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
			}
		}
	);				
}
//删除模板
function templateDelete(id,contextPath){
	$.post(
		contextPath + "/system/productTemplate/delete/"+id,
		function(data){
			if(data){
				ealert("删除成功！");
				var d="#del"+id;
				$(d).parent().parent().remove();
				var oTable = document.getElementById("productTab");
				for(var i=1;i<oTable.rows.length;i++){
					oTable.rows[i].cells[1].innerHTML = (i);
				}
			}else{
				ealert("删除失败！");
			}
		}
	);
}

//删除选择的记录
function deleteAll(contextPath){
	var items = document.getElementsByTagName("input");  
	for(var i=0;i<items.length;i++){
		if(items[i].type=="checkbox"&&items[i].checked){
			productDelete1(items[i].value,contextPath);
		}
	} 
	ealert("删除成功");
}


//点击修改按钮，根据id进行商品资料查询
function templateModify(id,contextPath){
	window.location.href= contextPath + "/system/productTemplate/detail/"+id;
}


//根据id load查看详情页面
function loaddiv(id,contextPath){
	$("#loadDiv").load($("#ctxpath").val()+"/system/productTemplateDetails/"+id,function(){
		
	});
	$(".apPreview").show();
	$(".apPreviewBg").show();
}
function apClose(){
	$(".apPreview").hide();
	$(".apPreviewBg").hide();

}