$(function(){
	var ctx = $("#ctxpath").val();
	var type = $("#jugg").val();
	
	/*选择弹窗*/
	$(".btnXuanze").live("click",function(){
		$(".alterBox").load(ctx + "/web/supplyBuy/jumpIntelPage/load",function(){
			$(".alertLayerBg").show();
			$(".alertLayerBg").next(".alterBox").show();
		});
	});
	
	/*关闭弹窗*/
	$(".layerClose").live("click",function(){
		$(this).parent(".alterBox").hide();
		$(this).parent(".alterBox").prev(".alertLayerBg").hide();
	});
	
	/*确定弹窗*/
	$(".altSure").live("click",function(){
		var idname = "";
		$("input[type='checkbox']").each(function(){
			if($(this).is(":checked")){
				var names = $(this).parent().parent().next().html();
				idname += '<li title="' + names + '">'+ names + '<a href="javascript:void(0);" class="closeLi"></a>';
				idname += '<input type="hidden" name="intelId" value="' + $(this).val() + '" /></li>';
			}
		});
		$(".zizhiList").empty();
		$(".zizhiList").html(idname);
		
		$(this).parents(".alterBox").hide();
		$(this).parents(".alterBox").prev(".alertLayerBg").hide();
	});
	
	/*取消弹窗*/
	$(".altCancel").live("click",function(){
		$(this).parents(".alterBox").hide();
		$(this).parents(".alterBox").prev(".alertLayerBg").hide();
	});
	
	/*删除当前资质*/
	$(".closeLi").live("click",function(){
		if($(".zizhiList li").length<2){
			$(this).parent("li").attr("title", "选择你要认证的资质名称");
			$(this).parent("li").html('选择你要认证的资质名称');
		}else{
			$(this).parent("li").remove();
		}
	});
	
	/*当前复选框*/
	$(".checkItem").live("click",function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			//选中
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
		}else{
			//取消选中
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
		}
	});
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}

});



// 返回列表
function goBack(contextPath){
	var productType = $("#productType").val();
	var type = $("#type").val();
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/supplyBuy/list/"+productType+"/"+type;
	}
	window.location.href=contextPath + "/system/supplyBuy/list/"+productType+"/"+type;
}




//店铺弹出层的搜索
function searchStoreList(){
	var name = document.getElementById("nameStore").value;
	if(name != null && name != "" && name !=" "){
		$("#storetc").load($("#ctxpath").val()+"/system/store/plistSearch/1",{"name":name});
	}else{
		$("#storetc").load($("#ctxpath").val()+"/system/store/plist");
	}
	$("#storeBg").show();
	$("#storetc").show();
}

function goPageStore(p){
	var name = document.getElementById("nameStore").value;
	$("#storetc").load($("#ctxpath").val()+"/system/store/plist",{"name":name,"pageNum":p});
//	if(name != null && name != "" && name !=" "){
//		$("#storetc").load($("#ctxpath").val()+"/system/store/plistSearch/" +name +"/"+ p);
//	}else{
//		$("#storetc").load($("#ctxpath").val()+"/system/store/plistSearch/" +null +"/"+ p);
//	}
}

//查询出店铺详细信息
function loadStore(){
	$("#storetc").load($("#ctxpath").val()+"/system/store/plist",function(){
	});
	$("#storeBg").show();
	$("#storetc").show();
}



//选择店铺
function sreachStore(storeName,storeId){
	$("#s_storeName").val(storeName);
	$("#storeName").val(storeName);
	$('#storeId').val(storeId);
	checkStoreIdBlur();
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}


//验证店铺格式
function checkStoreIdBlur(){
	var nameStr = document.getElementsByName('storeId')[0].value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("storeIdDd").className="text_c_ts";
		$("#nameSpan").html("请选择关联店铺");
		return false;
	}else{
		document.getElementById("storeIdDd").className="";
		return true;
	}
}

//弹出层的搜索条件重置
function resetStore(){
	document.getElementById("nameStore").value="";
}

//关闭弹出层
function apClose(){
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}



function checkAreaBlur(){
	var areaInfo=$("#areaId").val();
	if(areaInfo == ""||areaInfo=="null"){
		document.getElementById("areaDd").className="text_c_ts";
		$('#areaSpan').show();
		$('#areaSpan').text("请选择交货地");
		return false;
	}
	document.getElementById("areaDd").className="";
	$('#areaSpan').hide();
	return true;
}

function checkTitleBlur(){
	var title = $.trim($("input[name='title']").val());
	if(title == ""||title == " "){
		document.getElementById("titleDd").className="text_c_ts";
		$('#titleSpan').show();
		$('#titleSpan').text("请输入标题");
		return false;
	}
	document.getElementById("titleDd").className="";
	$('#titleSpan').hide();
	return true;
}

function checkNumberBlur(){
	var number = $.trim($("input[name='number']").val());
	var regexp = /^[1-9]d*.d*|0.d*[1-9]d*$/;
	if(number == ""){
		document.getElementById("numberDd").className="text_c_ts";
		$('#numberSpan').show();
		$('#numberSpan').text("请输入预估数量");
		return false;
	}
	if(!regexp.test(number)){
		document.getElementById("numberDd").className="text_c_ts";
		$('#numberSpan').show();
		$('#numberSpan').text("请输入正确的预估数量");
		return false;
	}
	document.getElementById("numberDd").className="";
	$('#numberSpan').hide();
	return true;
}
function unitBlur(){
	var unit = $.trim($("input[name='unit']").val());
	if(unit == ""){
		document.getElementById("unitDd").className="text_c_ts";
		$('#unitSpan').show();
		$('#unitSpan').text("请输入计价单位");
		return false;
	}
	document.getElementById("unitDd").className="";
	$('#unitShow').html("&nbsp单位:"+ "<em class='red'>" +unit +"</em>");
	$('#unitSpan').hide();
	return true;
}
function futurePricesBlur(){
	var futurePrices = $.trim($("input[name='futurePrices']").val());
	var regexp = /^[1-9]d*.d*|0.d*[1-9]d*$/;
	if(futurePrices == ""){
		
		return true;
	}
	if(!regexp.test(futurePrices)){
		document.getElementById("futurePricesDd").className="text_c_ts";
		$('#futurePricesSpan').show();
		$('#futurePricesSpan').text("请输入正确的预估价格");
		return false;
	}
	document.getElementById("futurePricesDd").className="";
	$('#futurePricesSpan').hide();
	return true;
	
}

function categoryBlur(){
	var categoryId = $("select[name='categoryId']").val(); 
	if(categoryId == ""){
		document.getElementById("categoryIdDd").className="text_c_ts";
		$('#categorySpan').show();
		$('#categorySpan').text("请选择分类");
		return false;
	}
	document.getElementById("categoryIdDd").className="";
	$('#categorySpan').hide();
	return true;
}
function storeNameBlur(){
	var storeName = $("input[name='storeName']").val(); 
	if(storeName == ""){
		document.getElementById("storeNameDd").className="text_c_ts";
		$('#storeNameSpan').show();
		$('#storeNameSpan').text("请选择企业");
		return false;
	}
	document.getElementById("categoryIdDd").className="";
	$('#categorySpan').hide();
	return true;
}

function checkContactNameBlur(){
	var contactName = $.trim($("input[name='contactName']").val());
	if(contactName == ""){
		document.getElementById("contactNameDd").className="text_c_ts";
		$('#contactNameSpan').show();
		$('#contactNameSpan').text("请输入联系人");
		return false;
	}
	document.getElementById("contactNameDd").className="";
	$('#contactNameSpan').hide();
	return true;
}
function qualityLevelBlur(){
	var qualityLevel = $.trim($("input[name='qualityLevel']").val());
	if(qualityLevel == ""){
		document.getElementById("qualityLevelDd").className="text_c_ts";
		$('#qualityLevelSpan').show();
		$('#qualityLevelSpan').text("请输入质量等级");
		return false;
	}
	document.getElementById("qualityLevelDd").className="";
	$('#qualityLevelSpan').hide();
	return true;
}
function endTimeBlur(){
	var endTime = $.trim($("input[name='endTime']").val());
	if(endTime == ""){
		document.getElementById("endTimeDd").className="text_c_ts";
		$('#endTimeSpan').show();
		$('#endTimeSpan').text("请选择有效时间");
		return false;
	}
	document.getElementById("endTimeDd").className="";
	$('#endTimeSpan').hide();
	return true;
}
function checkContactPhoneBlur(){
	var contactPhone = $.trim($("input[name='contactPhone']").val());
	var regexp = /^(1[0-9][0-9])\d{8}$/;
	if(contactPhone == ""){
		document.getElementById("contactPhoneDd").className="text_c_ts";
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入联系电话");
		return false;
	}
	if(!regexp.test(contactPhone)){
		document.getElementById("contactPhoneDd").className="text_c_ts";
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入正确的联系电话");
		return false;
	}
	document.getElementById("contactPhoneDd").className="";
	$('#contactPhoneSpan').hide();
	return true;
}



function formSubmit() {
	var areaInfo="";
	$(".areaIdArea").each(function(){
		areaInfo+=$(this).find("option:selected").text()+",";
	});
	$("#destination").val(areaInfo);
	//如果通过则提交 不同则显示未通过的项目
	if(endTimeBlur()&&checkTitleBlur()&&checkContactNameBlur()&&checkContactPhoneBlur()&&categoryBlur()&&checkNumberBlur()&&futurePricesBlur()&&unitBlur()&&qualityLevelBlur()&&checkAreaBlur()){
		document.getElementById("addSupplyBuyForm").submit();
	}else{
		checkTitleBlur();
		checkContactNameBlur();
		checkContactPhoneBlur();
		categoryBlur();
		checkNumberBlur();
		futurePricesBlur();
		unitBlur();
		qualityLevelBlur();
		checkAreaBlur();
		endTimeBlur();
	}
}


