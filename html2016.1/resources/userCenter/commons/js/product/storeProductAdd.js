String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
    } else {
        return this.replace(reallyDo, replaceWith);
    }
}
// JavaScript Document
$(document).ready(function() {
	/*弹层*/
	/*选择品牌弹层*/
	$(".clickBrand").click(function(){
		$(".alertBrand").load($("#ctxpath").val() + "/store/product/brand/plist");
		$(".apLayerBg").show();
		$(".alertBrand").show();
	})
	
	$(".clickTemp").click(function(){
		$(".apLayerBg").show();
		$(".alertTemp").show();
	})
	
	if($("#proTemplate").val() != ""){
		loadTempfields($("#proTemplate").val());	
	}
	
	/*增加条目*/
	$(".btnAddLine").click(function(){
		var obj = $("#addFieldsDemo").html();
		$(this).siblings(".pfPriceList").append(obj);
	})
	
	/*删除条目*/
	$(".closeIco").live("click",function(){
		$(this).parents(".listItem").remove();
	})
	
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',goAdd,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
	
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
	});
});

//品牌弹出层的搜索
function searchBrandList(){
	var name = document.getElementById("nameBrand").value;
	if(name != null && name != "" && name != " "){
		$(".alertBrand").load($("#ctxpath").val()+"/store/product/brand/plistSearch/" + name+"/1");
	}else{
		$(".alertBrand").load($("#ctxpath").val()+"/store/product/brand/plist");
	}
	$(".apLayerBg").show();
	$(".alertBrand").show();
}
function goAdd(){
	location.href= $("#ctxpath").val() +  "/store/product/storeProductAddClassify?jugg=" + $("#type").val();
	
}
// 返回列表
function goBack(contextPath){
	var type = $("#type").val();
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/store/product/sales/"+type;
	}
	window.location.href=contextPath + "/store/product/sales/"+type;
}


function goPageBrand(p){
	var name = document.getElementById("nameBrand").value;
	if(name != null && name != "" && name != " "){
		$("#brandtc").load($("#ctxpath").val()+"/store/product/brand/plistSearch/" +name +"/"+ p);
	}else{
		$("#brandtc").load($("#ctxpath").val()+"/store/product/brand/plistSearch/" +null +"/"+ p);
	}
}

//选择品牌
function sreachBrand(brandName,brandId){
	$('#brandName').val(brandName);
	$('#brand').val(brandId);
	$(".apLayerBg").hide();
	$(".alertBrand").hide();
}

function resetBrand(){
	document.getElementById("nameBrand").value="";
}

function reset(){
	location.reload();
}

//关闭弹出层
function apClose(){
	$("#brandBg").hide();
	$("#brandtc").hide();
}

//验证商品名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >128 || nameStr ==" "){
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
		$("#nameSpan").html("请输入正确的商品名称");
		$("#nameSpan").show();
		return false;
	}else{
		$("#nameSpan").hide();
		return true;
	}
}

function formSubmit() {
	var editorStr = CKEDITOR.instances.description.getData();
	$("#description").val(editorStr);
	var id = document.getElementsByName('productNumber')[0].value;
	if(!checkNameBlur() || !$("#idCheck").val()){
		return;
	}
	//验证库存
	if($("#quantity").val() <= 0){
		$("#quantitySpan").html("库存量最小大于1");
		$("#quantitySpan").show();
		return;
	}else{
		$("#quantitySpan").hide();
	}
	
	
	//验证数量单位
	if($("#unit").val() == "" || $("#unit").val() == " "){
		$("#unitSpan").html("请输入数量的单位。如:吨");
		$("#unitSpan").show();
		return;
	}else{
		$("#unitSpan").hide();
	}
	
	$("#addProductForm").ajaxSubmit(
		function(data){
		if(data==true || data=="true"){
			econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
		}else{
			falert("添加失败");
		}
	});
}
var flag = false;
// 验证商品编号是否存在
function checkId(productNumber){
	var contextPath = $('#ctxpath').val();
	flag = false;
	if(productNumber == null || productNumber == "" || productNumber == " " || productNumber.length<6 || productNumber.length>16){
		$("#idSpan").html("商品编号为6-16位字符");
		$("#idSpan").show();
		 $("#idCheck").val(false);
		return false;
	}else{
		$.post(
			contextPath + "/store/product/checkId/" + productNumber + "/null",
		 	function(data){
		 		if(!data){
					$("#idSpan").html("商品编号已存在");
					$("#idSpan").show();
		 			$("#idCheck").val(false);
		 			return false;
		 		}else{
		 			$("#idSpan").html("商品编号为6-16位字符");
		 			$("#idSpan").hide();
		 			$("#idCheck").val(true);
			 		return true;
		 		}
		 	}
		 );
	}
}

function checkIdBlur (productNumber) {
	var contextPath = $('#ctxpath').val();
	if(productNumber == null){
		$("#idSpan").html("商品编号为6-16位字符");
		$("#idSpan").show();
		$("#idCheck").val(false);
		flag = false;
	}else{
		var regexp = /^[A-Za-z0-9]+$/;
		productNumber = $.trim(productNumber);
		
		if(productNumber == "" || productNumber.length < 6 || productNumber.length > 16){
			$("#idSpan").html("商品编号为6-16位字符");
			$("#idSpan").show();
			$("#idCheck").val(false);
			flag = false;
		}else if(!regexp.test(productNumber)){
			$("#idSpan").html("商品编号数字或字母组成");
			$("#idSpan").show();
			$("#idCheck").val(false);
			flag = false;
		}else{
			$.post(contextPath + "/store/product/checkId/" + productNumber + "/null", function(data){
		 		if(!data){
					$("#idSpan").html("商品编号已存在");
					$("#idSpan").show();
		 			$("#idCheck").val(false);
		 			return false;
		 		}else{
		 			$("#idSpan").html("商品编号为6-16位字符");
		 			$("#idSpan").hide();
		 			$("#idCheck").val(true);
			 		return true;
		 		}
			});
		}
	}
}

//load出可选字段的扩展字段和扩展字段的值
function loadTempfields(value){
	if(value == ""){
		$('#tempFieldsFont').html('');
	}
	$("#tempFieldsFont").load($("#ctxpath").val()+"/store/product/tempfieldsLoad/" + value + "/null",function() {
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
			htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Warn\" class=\"i_bg itemBox2\" value=\"" + 0 + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
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
				htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Warn\" class=\"i_bg itemBox2\" value=\"" + 0 + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
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
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "Warn\" class=\"i_bg itemBox2\" value=\"" + 0 + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
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
						htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "Warn\" class=\"i_bg itemBox2\" value=\"" + 0 + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
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

