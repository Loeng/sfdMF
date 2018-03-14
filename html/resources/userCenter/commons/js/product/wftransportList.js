
$(document).ready(function() {
    $(".checkAll input[type='checkbox']").click(function(){
		a = $(this);
		if(a.is(":checked")){
			$(".tdCheck input[type='checkbox']").attr("checked",true);
		}
		else{
			$(".tdCheck input[type='checkbox']").attr("checked",false);
		}
	})
});

//删除选择的记录
function deleteAll(contextPath){
	var items = document.getElementsByName("delProducts");  
	for(var i=0;i<items.length;i++){
		if(items[i].checked){
			productDelete1(items[i].value,contextPath);
		}
	}
	salert("删除成功");
}

//用于批量删除商品时的删除方法(避免不断弹出删除成功的提示)
function productDelete1(id,contextPath){
	$.post(
		contextPath + "/store/wftransport/deleteById/"+id,
		function(data){
			if(data){
				var d="#del"+id;
				$(d).parent().parent().remove();
				//var oTable = document.getElementById("proTable");
				//for(var i=1;i<oTable.rows.length;i++){
				//	oTable.rows[i].cells[1].innerHTML = (i);
				//}
			}
		}
	);				
}
//删除商品
function productDelete(id,contextPath){
	$.post(
		contextPath + "/store/wftransport/deleteById/"+id,
		function(data){
			if(data){
				salert("删除成功！");
				var d="#del"+id;
				$(d).parent().parent().remove();
				//var oTable = document.getElementById("proTable");
				//for(var i=1;i<oTable.rows.length;i++){
				//	oTable.rows[i].cells[1].innerHTML = (i);
				//}
			}else{
				falert("删除失败！");
			}
		}
	);
}

function deleteP(id,contextPath){
	econfirm('是否确认删除该商品？',productDelete,[id,contextPath],null,null);	
}


//点击修改按钮，根据id进行商品资料查询
function productModify(id,contextPath,type){
	alert(type);
	window.location.href= contextPath + "/store/wftransport/getWftransport/" + id + "/" + type;
}

//searchProductForm的提交
function searchSubmit(){
	document.getElementById("searchProductForm").submit();
}

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchProductForm").submit();
}

// 重置
function resetForm(){
	document.getElementsByName("name")[0].value="";
	document.getElementsByName("startPlace")[0].value="";
	document.getElementsByName("endPlace")[0].value="";
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
		falert("至少选择一条");
	}
}
