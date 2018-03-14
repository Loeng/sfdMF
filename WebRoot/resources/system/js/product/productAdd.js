$(function(){
	
	/*增加条目*/
	$(".btnAddLine").click(function(){
		var obj = $("#addFieldsDemo").html();
		var radomId = "popPic" + new Date().getTime();
		obj = obj.replaceAll("picClone",radomId);
		$(this).siblings(".pfPriceList").append(obj);
		$(".pfPriceList .listItem:last").find(".productPrices").val(radomId);
	})
	
	/*删除条目*/
	$(".btnRemoveLine").live("click",function(){
		$(this).parents(".listItem").remove();
	})
	
	
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
	
	/*全选*/
	$(".checkAll").live("click",function() {
		var obj=$(this).find("input");
		if(obj.is(":checked")){
			$(".colorSizeTable td input[type='checkbox']").attr("checked",true);
		}else{
			$(".colorSizeTable td input[type='checkbox']").attr("checked",false);
		}
	});
	
	/*增加图片*/
	$(".btnAdd").click(function(){
		var radomId = "popPic" + new Date().getTime();
		var obj=$("#picForClone").html();
		obj = obj.replaceAll("picClone",radomId);
		$(this).before(obj);
		var popPicNum = $("#popPicProperties").val();
		popPicNum = popPicNum + radomId + ";";
		$("#popPicProperties").val(popPicNum);
	})
	
	/*删除图片*/
	$(".uploadItem .closeIco").live("click",function(){
		$(this).parents(".uploadItem").remove();
		var popPicNum = $("#popPicProperties").val();
		var oldId = $(this).attr("id") + ";";
		popPicNum = popPicNum.replaceAll(oldId,"");
		$("#popPicProperties").val(popPicNum);
	})
	
	
});



// 返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/product/list";
	}
	window.location.href=contextPath + "/system/product/list";
}




//店铺弹出层的搜索
function searchStoreList(){
	var name = document.getElementById("nameStore").value;
	if(name != null && name != "" && name !=" "){
		$("#storetc").load($("#ctxpath").val()+"/system/store/plistSearch/" +name +"/1");
	}else{
		$("#storetc").load($("#ctxpath").val()+"/system/store/plist");
	}
	$("#storeBg").show();
	$("#storetc").show();
}

function goPageBrand(p){
	var name = document.getElementById("nameBrand").value;
	if(name != null && name != "" && name != " "){
		$("#brandtc").load($("#ctxpath").val()+"/system/product/brand/plistSearch/" +name +"/"+ p);
	}else{
		$("#brandtc").load($("#ctxpath").val()+"/system/product/brand/plistSearch/" +null +"/"+ p);
	}
}

function goPageStore(p){
	var name = document.getElementById("nameStore").value;
	if(name != null && name != "" && name !=" "){
		$("#storetc").load($("#ctxpath").val()+"/system/store/plistSearch/" +name +"/"+ p);
	}else{
		$("#storetc").load($("#ctxpath").val()+"/system/store/plistSearch/" +null +"/"+ p);
	}
}
//品牌弹出层的搜索
function searchBrandList(){
	var name = document.getElementById("nameBrand").value;
	if(name != null && name != "" && name != " "){
		$("#brandtc").load($("#ctxpath").val()+"/system/product/brand/plistSearch/" + name+"/1");
	}else{
		$("#brandtc").load($("#ctxpath").val()+"/system/product/brand/plist");
	}
	$("#brandBg").show();
	$("#brandtc").show();
}
//查询出店铺详细信息
function loadStore(){
	$("#storetc").load($("#ctxpath").val()+"/system/store/plist",function(){
		
	});
	$("#storeBg").show();
	$("#storetc").show();
}

//查询出品牌详细信息
function loadBrand(){
	$("#brandtc").load($("#ctxpath").val()+"/system/product/brand/plist",function(){
		
	});
	$("#brandBg").show();
	$("#brandtc").show();
}

//查询出模板详细信息
function loadTemplate(){
	$("#templatetc").load($("#ctxpath").val()+"/system/template/plist",function(){
		
	});
	$("#templateBg").show();
	$("#templatetc").show();
}

//选择店铺
function sreachStore(storeName,storeId){
	$('#storeName').val(storeName);
	$('#storeId').val(storeId);
	checkStoreIdBlur();
	$("#storeBg").hide();
	$("#storetc").hide();
}
//选择品牌
function sreachBrand(brandName,brandId){
	$('#brandName').val(brandName);
	$('#brand').val(brandId);
	$("#brandBg").hide();
	$("#brandtc").hide();
}
//选择模板
function sreachTemplate(name){
	$('#tempLate').val(name);
	$("#templateBg").hide();
	$("#templatetc").hide();
}

//弹出层的搜索条件重置
function resetStore(){
	document.getElementById("nameStore").value="";
}
function resetTemplate(){
	document.getElementById("nameTemplate").value="";
}
function resetBrand(){
	document.getElementById("nameBrand").value="";
}
//关闭弹出层
function apClose(){
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}

//验证商品名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >128 || nameStr ==" "){
		document.getElementById("nameDd").className="text_ts";
		$("#nameCheck").val("false");
		return false;
	}else{
		$("#nameCheck").val("true");
		return true;
	}
}

//验证商品名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >128 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		$("#nameCheck").val("true");
		return true;
	}
}

//重新选择分类
function  restCatory(contextPath){
	var type = $("#type").val();
	window.location.href= contextPath + "/system/product/productAddClassify?jugg="+type;
}
var subFlag = false;
function formSubmit() {
	if(!subFlag){
		subFlag=true;
		var editorStr = CKEDITOR.instances.description.getData();
		$("#description").val(editorStr);
		if(checkNameBlur() && checkStoreIdBlur()){
			$("#loadingLayer").show();
			$("#addProductForm").ajaxSubmit(
			 		function(data){
			 			if(data==true){
			 				$("#loadingLayer").hide();
			 				econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
			 				subFlag=false;
			 			}else{
			 				$("#loadingLayer").hide();
			 				ealert("添加失败");
			 				subFlag=false;
			 			}
			 		});
		}else{
			checkNameBlur();
			checkStoreIdBlur();
			//checkBrandBlur();
			var id = document.getElementsByName('productNumber')[0].value;
			checkIdBlur (id);
			subFlag=false;
		}
	}else{
		ealert("保存中，请勿重复提交");
	}
	
}
var flag = false;

//验证店铺格式
function checkStoreIdBlur(){
	var nameStr = document.getElementById('storeName').value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("storeIdDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("storeIdDd").className="";
		return true;
	}
}

//验证品牌格式
function checkBrand(){
	var nameStr = document.getElementsByName('brand')[0].value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("brandDd").className="text_ts";
		return false;
	}else{
		document.getElementById("brandDd").className="text_d_ts";
		return true;
	}
}

//验证品牌格式
function checkBrandBlur(){
	var nameStr = document.getElementsByName('brand')[0].value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("brandDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("brandDd").className="";
		return true;
	}
}


// 验证商品编号是否存在
function checkId(productNumber){
	var contextPath = $('#ctxpath').val();
	flag = false;
	if(productNumber == null || productNumber == "" || productNumber == " " || productNumber.length<6 || productNumber.length>16){
		document.getElementById("idDd").className="text_c_ts";
		$("#idSpan").html("商品编号为6-16位字符");
		return false;
	}else{
		
	
	$.post(
		contextPath + "/system/product/checkId/"+productNumber,
	 	function(data){
	 		if(!data){
	 			document.getElementById("idDd").className="text_c_ts";
				$("#idSpan").html("商品编号已存在");
	 			return false;
	 		}else{
	 			
	 			document.getElementById("idDd").className=""
		 		return true;
	 		}
	 	}
	 );
	}
}

function checkIdBlur (productNumber) {
	var contextPath = $('#ctxpath').val();
	if(productNumber == null || productNumber == "" || productNumber == " " || productNumber.length<6 || productNumber.length>16){
		document.getElementById("idDd").className="text_c_ts";
		flag = false;
	}else{
		$.post(
				contextPath + "/system/product/checkId/"+productNumber,
			 	function(data){
			 		if(!data){
			 			document.getElementById("idDd").className="text_c_ts";
						$("#idSpan").html("商品编号已存在");
			 			return false;
			 		}else{
			 			document.getElementById("idDd").className=""
				 		return true;
			 		}
			 	}
			 );
	}
}
//跳转到继续添加得到页面
function goAdd(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/product/productAddClassify";
	}
	window.location.href=contextPath + "/system/product/productAddClassify";
}

//load出可选字段的扩展字段和扩展字段的值
function loadTempfields(value){
	if(value == ""){
		$('#tempFieldsFont').html('');
	}
	$("#tempFieldsFont").load($("#ctxpath").val()+"/system/product/tempfieldsLoad/" + value + "/null",function() {
		var itemChecks = $(".itemCheck");		
		itemChecks.each(function(index) {
			if($(this).is(":checked")){
				$(this).siblings(".imgForbid").hide();
				$(this).siblings(".imgUpload").show();
			}else{
				$(this).siblings(".imgUpload").hide();
				$(this).siblings(".imgForbid").show();
			}
		});
	});
}

function selectModel(obj){
	if($(obj).is(":checked")){
		$(obj).siblings(".imgForbid").hide();
		$(obj).siblings(".imgUpload").show();
	}else{
		$(obj).siblings(".imgUpload").hide();
		$(obj).siblings(".imgForbid").show();
	}
	showModelDetail();
}

function showModelDetail () {
	var items = $(".colorList");
	var titles = new Array();
	var detail1 = new Array();
	var detail2 = new Array();
	var detail3 = new Array();
	var detail4 = new Array();
	items.each(function(index){
		var boxs = $(this).find(".colorItem input:checked");
		if(boxs != null && boxs.length > 0){
			var titleStr = $(this).children("dt").html();
			titles[titles.length] = titleStr;
			boxs.each(function(a){
				if(index == 0){
					detail1[a] = $(this).val();
				}else if(index == 1){
					detail2[a] = $(this).val();
				}else if(index == 2){
					detail3[a] = $(this).val();
				}else{
					detail4[a] = $(this).val();
				}
			});
		}
	});
	if(titles.length <= 0){
		$("#modelDetailLine").hide();
		$(".colorSizeTable tbody").html("");
		return;
	}
	
	var htmlStr = "<tr><th><span class='checkAll'><label><input type=\"checkbox\">启用</label></span></th>";
	for(i=0;i<titles.length;i++){
		htmlStr = htmlStr + "<th>" + titles[i] + "</th>";
	}
	htmlStr += "<th>价格</th><th>库存</th><th>预警库存</th></tr>";
	
	if(titles.length == 1){
		var array = new Array();
		if(detail1.length > 0 ){
			array = detail1;
		}else if(detail2.length > 0 ){
			array = detail2;
		}else if(detail3.length > 0 ){
			array = detail3;
		}else if(detail4.length > 0 ){
			array = detail4;
		}
		
		for(i=0;i<array.length;i++){
			htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\"" + array[i] + "\"></td>";
			htmlStr+="<td>"+ $("#tempValue" + array[i]).val() +"</td>";
			htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Price\" class=\"i_bg itemBox2\" value=\"" + $("#unitPrice").val() + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
			htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Stock\" class=\"i_bg itemBox2\" value=\"" + $("#quantity").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
			htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Warn\" class=\"i_bg itemBox2\" value=\"" + $("#quantityWarning").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
			htmlStr+="<td><input type=\"hidden\" name=\"" + array[i] + "Title\" value=\"" + titles[0] + "\" />";
		}
		
	} else if(titles.length == 2){
		var array0 = new Array();
		var array1 = new Array();
		if(detail1.length > 0){
			array0 = detail1;
		}	
		if(detail2.length > 0){
			if(array0.length <= 0){
				array0 = detail2;
			}else{
				array1 = detail2;
			}
		}	
		if(detail3.length > 0){
			if(array0.length <= 0){
				array0 = detail3;
			}else{
				array1 = detail3;
			}
		}	
		if(detail4.length > 0){
			if(array0.length <= 0){
				array0 = detail4;
			}else{
				array1 = detail4;
			}
		}	
		
		for(i=0;i<array0.length;i++){
			for(j=0;j<array1.length;j++){
				htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\"" + array0[i] + "--" + array1[j] + "\"></td>";
				htmlStr+="<td>"+ $("#tempValue" + array0[i]).val() +"</td>";
				htmlStr+="<td>"+ $("#tempValue" + array1[j]).val() +"</td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Price\" class=\"i_bg itemBox2\" value=\"" + $("#unitPrice").val() + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Stock\" class=\"i_bg itemBox2\" value=\"" + $("#quantity").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Warn\" class=\"i_bg itemBox2\" value=\"" + $("#quantityWarning").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
				htmlStr+="<td><input type=\"hidden\" name=\"" + array0[i] + "Title\" value=\"" + titles[0] + "\" />";
				htmlStr+="<td><input type=\"hidden\" name=\"" + array1[j] + "Title\" value=\"" + titles[1] + "\" />";
			}
		}
	}else if(titles.length == 3){
		var array0 = new Array();
		var array1 = new Array();
		var array2 = new Array();
		if(detail1.length > 0){
			array0 = detail1;
		}	
		if(detail2.length > 0){
			if(array0.length <= 0){
				array0 = detail2;
			}else{
				array1 = detail2;
			}
		}	
		if(detail3.length > 0){
			if(array0.length <= 0){
				array0 = detail3;
			}else if(array1.length <= 0){
				array1 = detail3;
			}else{
				array2 = detail3;
			}
		}	
		if(detail4.length > 0){
			if(array0.length <= 0){
				array0 = detail4;
			}else if(array1.length <= 0){
				array1 = detail4;
			}else{
				array2 = detail4;
			}
		}	
		
		for(i=0;i<array0.length;i++){
			for(j=0;j<array1.length;j++){
				for(k=0;k<array2.length;k++){
					
					var detailId = "";
					var price = $("#unitPrice").val();
					var stock = $("#quantity").val();
					var warn = $("#quantityWarning").val();
					htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\"  value=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "\"></td>";
					htmlStr+="<td>"+ $("#tempValue" + array0[i]).val() +"</td>";
					htmlStr+="<td>"+ $("#tempValue" + array1[j]).val() +"</td>";
					htmlStr+="<td>"+ $("#tempValue" + array2[k]).val() +"</td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "Price\" class=\"i_bg itemBox2\" value=\"" + price + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "Stock\" class=\"i_bg itemBox2\" value=\"" + stock + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "Warn\" class=\"i_bg itemBox2\" value=\"" + warn + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
					htmlStr+="<td><input type=\"hidden\" name=\"" + array0[i] + "Title\" value=\"" + titles[0] + "\" />";
					htmlStr+="<td><input type=\"hidden\" name=\"" + array1[j] + "Title\" value=\"" + titles[1] + "\" />";
					htmlStr+="<td><input type=\"hidden\" name=\"" + array2[k] + "Title\" value=\"" + titles[2] + "\" />";
				}
			}
		}
	}else if(titles.length == 4){
		var array0 = new Array();
		var array1 = new Array();
		var array2 = new Array();
		var array3 = new Array();
		if(detail1.length > 0){
			array0 = detail1;
		}	
		if(detail2.length > 0){
			if(array0.length <= 0){
				array0 = detail2;
			}else{
				array1 = detail2;
			}
		}	
		if(detail3.length > 0){
			if(array0.length <= 0){
				array0 = detail3;
			}else if(array1.length <= 0){
				array1 = detail3;
			}else{
				array2 = detail3;
			}
		}	
		if(detail4.length > 0){
			if(array0.length <= 0){
				array0 = detail4;
			}else if(array1.length <= 0){
				array1 = detail4;
			}else if(array2.length <= 0){
				array2 = detail4;
			}else{
				array3 = detail4;
			}
		}	
		
		for(i=0;i<array0.length;i++){
			for(j=0;j<array1.length;j++){
				for(k=0;k<array2.length;k++){
					for(l=0;l<array3.length;l++){
				
						var detailId = "";
						var price = $("#unitPrice").val();
						var stock = $("#quantity").val();
						var warn = $("#quantityWarning").val();
						
						htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\"  value=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "\"></td>";
						htmlStr+="<td>"+ $("#tempValue" + array0[i]).val() +"</td>";
						htmlStr+="<td>"+ $("#tempValue" + array1[j]).val() +"</td>";
						htmlStr+="<td>"+ $("#tempValue" + array2[k]).val() +"</td>";
						htmlStr+="<td>"+ $("#tempValue" + array3[l]).val() +"</td>";
						htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "Price\" class=\"i_bg itemBox2\" value=\"" + price + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
						htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "Stock\" class=\"i_bg itemBox2\" value=\"" + stock + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
						htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "Warn\" class=\"i_bg itemBox2\" value=\"" + warn + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
						htmlStr+="<td><input type=\"hidden\" name=\"" + array0[i] + "Title\" value=\"" + titles[0] + "\" />";
						htmlStr+="<td><input type=\"hidden\" name=\"" + array1[j] + "Title\" value=\"" + titles[1] + "\" />";
						htmlStr+="<td><input type=\"hidden\" name=\"" + array2[k] + "Title\" value=\"" + titles[2] + "\" />";
						htmlStr+="<td><input type=\"hidden\" name=\"" + array3[l] + "Title\" value=\"" + titles[3] + "\" />";
					}
				}
			}
		}
	}
	
	$("#modelDetailLine").show();
	$(".colorSizeTable tbody").html(htmlStr);	
	
}


function changeFields(tempId,tempFieldName,category,position,valueStr){
	$("#" + tempId).val(tempId + "**" + tempFieldName + "**" + category + "**" + position + "**" + valueStr);
}

function changeFields2(tempId,tempFieldName,category,position,idStr){
	var valueStr = "";
	
	var values = document.getElementsByName(idStr);
	for(i=0;i<values.length;i++){
		var valObj = 	values[i];
		if(valObj.checked){
			if(valueStr == ""){
				valueStr = valObj.value;
			}else{
				valueStr = valueStr + ";" + 	valObj.value;
			}	
		}
	}
	alert(valueStr)
	
	
	
	$("#" + tempId).val(tempId + "**" + tempFieldName + "**" + category + "**" + position + "**" + valueStr);
}
//选择大宗商品时隐藏商城价、市场价 显示批发价和价格区间
function checkRadioD(){
	$("#advanceFont").hide();
}
//选择优选商品时显示商城价、市场价 隐藏批发价和价格区间
function checkRadioY(){
	$("#advanceFont").show();

}