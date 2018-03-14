$(document).ready(function() {
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
});

$(function(){
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		ealert("添加成功！")
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


var flag = false;







function checkProductIdBlur(){
	var nameStr = $('#productId').val();
	if($.trim(nameStr) == ""){
		$("#productIdDd").addClass("text_c_ts");
		return false;
	}
	document.getElementById("productIdDd").className="";
	return true;
}

function checkWarehouseIdBlur(){
	var nameStr = $('#warehouseId').val();
	if($.trim(nameStr) == ""){
		$("#warehouseIdDd").addClass("text_c_ts");
		return false;
	}
	document.getElementById("warehouseIdDd").className="";
	return true;
}



//返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href=contextPath + "/system/warehouse/list";
	}
	window.location.href=contextPath + "/system/warehouse/list";
}

//提交
function formSubmit(){
    if(checkProductIdBlur()&&checkWarehouseIdBlur()){
    	document.getElementById("addGoWarehouseForm").submit();
    }else{
    	checkProductIdBlur();
    	checkWarehouseIdBlur();
    	
    }
}


//会员等级弹出层的搜索
function loadProduct(){
	$("#productTc").load($("#ctxpath").val()+"/system/product/plistSearch",function(){
		
	});
	$("#productBg").show();
	$("#productTc").show();
}	

//选择会员等级
function sreachP(name,id){
	$('#product').val(name);
	$('#productId').val(id);
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}
function sreachW(name,id){
	$('#warehouse').val(name);
	$('#warehouseId').val(id);
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}
//关闭弹出层
function apClose(){
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}

//会员等级弹出层的搜索
function loadWareHouse(){
	$("#warehouseTc").load($("#ctxpath").val()+"/system/warehouse/plistSearch",function(){
		
	});
	$("#warehouseBg").show();
	$("#warehouseTc").show();
}	

//选择会员等级
function sreachStore(name,id){
	$('#levelName').val(name);
	$('#levelId').val(id);
	$("#userLevelBg").hide();
	$("#userLeveltc").hide();
}
//关闭弹出层
function apClose(){
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}