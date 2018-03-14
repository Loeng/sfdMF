$(document).ready(function() {
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
	/*
	$("#storeSpan").click(function(){
		$("#storetc").show();
		$("#storeBg").show();
	});
	
	$("#storetc #storetit span").click(function(){
		$("#storetc").hide();
		$("#storeBg").hide();
	});
	*/
	/*打开弹窗*/
	$("#brandSpan").click(function(){
		$("#brandtc").show();
		$("#brandBg").show();
	});
	$("#brandtc #brandtit span").click(function(){
		$("#brandtc").hide();
		$("#brandBg").hide();
	});
	
	/*打开弹窗*/
	$("#tempLateSpan").click(function(){
		$("#templatetc").show();
		$("#templateBg").show();
	});
	$("#templatetc #templatetit span").click(function(){
		$("#templatetc").hide();
		$("#templateBg").hide();
	});
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");

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
	
	if($("#proTemplate").val() != ""){
		loadTempfields($("#proTemplate").val());	
	}
	
});

function searchName(name, id) {
	$("#span1").html(name);
	$("#id").val(id);
}



$(function(){
	if($("#modifyOk").val()=="true"){
		econfirm('修改成功，是否继续修改？',null,null,goList,[$("#ctxpath").val()]);
	}else if(!$("#modifyOk").val()=="false"){
		ealert("修改失败！");
	}
	$("input.i_bg").focus(function (){ 
			$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 
});
//店铺弹出层的搜索
function searchStoreList(){
	var name = document.getElementById("nameStore").value;
	if(name != null && name != "" && name !=" "){
	$("#storetc").load($("#ctxpath").val()+"/system/store/plistSearch/" +name);
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
// 重置品牌搜索
function resetBrand(){
	document.getElementById("nameBrand").value="";
}


//收索出市
function searchFirstName(id){
	var _contextPath = $("#ctxpath").val();
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/system/systemArea/plist/"+id,
			 	function(data){
			 		var systemAreas = jQuery.parseJSON(data);
			 		if(systemAreas.length == 0 || systemAreas==null){
			 			$("#jsonBox2").get(0).innerHTML ="";
			 			$("#jsonBox3").get(0).innerHTML ="";
			 		}else{
				 		var result = '<option value="==请选择==" >==请选择==</option>';
				 		for(var i=0;i<systemAreas.length;i++) {
				 			result += '<option value="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</option>';
				 		}
				 		$('#jsonBox2').get(0).innerHTML = result;
				 		$('#jsonBox3').get(0).innerHTML = "";
			 		}
			 	}
			 );
	}else{
		$.post(
				_contextPath + "/system/systemArea/plist/"+id,
			 	function(data){
					var systemAreas = jQuery.parseJSON(data);
			 		var result = '<option value="==请选择==" >==请选择==</option>';
			 		for(var i=0;i<systemAreas.length;i++) {
			 			result += '<option value="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</option>';
			 		}
			 		$('#jsonBox2').get(0).innerHTML = result;
			 		$('#jsonBox3').get(0).innerHTML = "";
			 	}
			 );
	}
	$("#habitat").val(id);
	$(this).next("div.pro_glpp").show();
}
function searchThreeName(id){
	$("#habitat").val(id);
}

//收索区
function searchTwoName(id){
	var _contextPath = $("#ctxpath").val();
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/system/systemArea/plist/"+id,
			 	function(data){
			 		var systemAreas = jQuery.parseJSON(data);
			 		if(systemAreas.length == 0 || systemAreas==null){
			 			$("#jsonBox3").get(0).innerHTML ="";

			 		}else{
			 		var result = '<option value="==请选择==" >==请选择==</option>';
			 		for(var i=0;i<systemAreas.length;i++) {
			 			result += '<option value="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</option>';
			 		}
			 		$('#jsonBox3').get(0).innerHTML = result;
			 		}
			 	}
			 );
	}else{
		$.post(
				_contextPath + "/system/systemArea/plist/"+id,
			 	function(data){
					var systemAreas = jQuery.parseJSON(data);
			 		var result = '<option value="==请选择==" >==请选择==</option>';
			 		for(var i=0;i<systemAreas.length;i++) {
			 			result += '<option value="'+systemAreas[i].id+'">'+systemAreas[i].areaName+'</option>';
			 		}
			 		$('#jsonBox3').get(0).innerHTML = result;
			 	}
			 );
	}
	$("#habitat").val(id);
	$(this).next("div.pro_glpp").show();	
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

function showTemp(){
	$("#templateBg").show();
	$("#templatetc").show();
}

function closeTemp(){
	$("#templateBg").hide();
	$("#templatetc").hide();
}
//弹出层的搜索条件重置
function resetForm(){
	document.getElementById("name").value="";
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
		$("#nameSpan").html("商品名为1-128位字符组成");
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("商品名为1-128位字符组成");
		$("#nameCheck").val("true");
		return true;
	}
}

//验证商品名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >128 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("商品名为1-128位字符组成");
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		$("#nameCheck").val("true");
		return true;
	}
}


//验证店铺格式
function checkStoreId(){
	var nameStr = document.getElementsByName('storeId')[0].value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("storeIdDd").className="text_ts";
		$("#nameSpan").html("请选择关联店铺");
		return false;
	}else{
		document.getElementById("storeIdDd").className="text_d_ts";
		$("#nameSpan").html("请选择关联店铺");
		return true;
	}
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

//验证品牌格式
function checkBrand(){
	var nameStr = document.getElementsByName('brand')[0].value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("brandDd").className="text_ts";
		$("#nameSpan").html("请选择关联品牌");
		return false;
	}else{
		document.getElementById("brandDd").className="text_d_ts";
		$("#nameSpan").html("请选择关联品牌");
		return true;
	}
}
//验证模板格式
function checkTemplate(){
	var nameStr = document.getElementsByName('templateId')[0].value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("templateIdDd").className="text_ts";
		$("#nameSpan").html("请选择关联模板");
		return false;
	}else{
		document.getElementById("templateIdDd").className="text_d_ts";
		$("#nameSpan").html("请选择关联模板");
		return true;
	}
}

//验证质量等级
function checkProductModelBlur(){
	var productModelStr = document.getElementsByName('productModel')[0].value;
	if(productModelStr.length < 1 || productModelStr ==" "){
		document.getElementById("productModelDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("productModelDd").className="";
		return true;
	}
}

//验证交货地
function checkDeliceAddressBlur(){
	var deliceAddressInfo=$("#deliceAddress").val();
	if(deliceAddressInfo == ""||deliceAddressInfo=="null"){
		document.getElementById("deliceAddressDd").className="text_c_ts";
		$('#deliceAddressSpan').show();
		$('#deliceAddressSpan').text("请选择交货地");
		return false;
	}
	document.getElementById("deliceAddress").className="";
	$('#deliceAddressSpan').hide();
	return true;
}

//验证产地
function checkhabitatBlur(){
	var habitatInfo=$("#habitat").val();
	if(habitatInfo == ""||habitatInfo=="null"){
		document.getElementById("habitatDd").className="text_c_ts";
		$('#habitatSpan').show();
		$('#habitatSpan').text("请选择产地");
		return false;
	}
	document.getElementById("habitat").className="";
	$('#habitatSpan').hide();
	return true;
}

//验证模板格式
//function checkTemlateBlur(){
//	var nameStr = document.getElementsByName('templateId')[0].value;
//	if(nameStr == null || nameStr == "" || nameStr ==" "){
//		document.getElementById("templateIdDd").className="text_c_ts";
//		$("#nameSpan").html("请选择关联模板");
//		return false;
//	}else{
//		document.getElementById("templateIdDd").className="";
//		return true;
//	}
//}
// 验证商品编号是否存在
function checkId(productNumber,contextPath){
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
	 			
	 			document.getElementById("idDd").className="text_d_ts"
		 		return true;
	 		}
	 	}
	 );
	}
}

function checkIdBlur (productNumber) {
	if(productNumber == null || productNumber == "" || productNumber == " " || productNumber.length<6 || productNumber.length>16){
		document.getElementById("idDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("idDd").className="";
		return true;
	}
}
// 返回列表
function goList(contextPath){
	var type = $("#type").val();
	if(contextPath==null){
		window.location.href="/system/product/list/"+type;
	}
	window.location.href=contextPath + "/system/product/list/"+type;
}
//提交
function formSubmit() {
	var _id = document.getElementsByName("productNumber")[0].value;
	if(checkNameBlur() && checkStoreIdBlur()  && checkIdBlur(_id) && checkhabitatBlur() && checkDeliceAddressBlur() && checkProductModelBlur()){
		var editorStr = CKEDITOR.instances.description.getData();
		$("#description").val(editorStr);
		$("#loadingLayer").show();
		$("#modifyProductForm").ajaxSubmit(
		 		function(data){
		 			if(data==true){
		 				$("#loadingLayer").hide();
		 				econfirm('修改成功，是否继续修改？',null,null,goList,[$("#ctxpath").val()]);
		 			}else{
		 				$("#loadingLayer").hide();
		 				ealert("修改失败！");
		 			}
		 		});
	}else{
		checkNameBlur();
		checkStoreIdBlur();
		checkBrandBlur();
		checkProductModelBlur();
		checkhabitatBlur();
		checkDeliceAddressBlur();
		var id = document.getElementsByName('productNumber')[0].value;
		checkIdBlur (id);
	}
}



//收索出一级页面
function searchFirstName(id, name) {
	var _contextPath = $("#ctxpath").val();
	var firstName = "#firstName" + id;
	$("#span1").html(name);
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	$("#span5").html("");
	$(firstName).siblings().removeClass("cur");
	$(firstName).addClass("cur");
	$("#id").val(id);
	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/system/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="#" id="Two' + productCategorys[i].id
						+ '" onclick="searchTwoName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">' + '>'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox2").html(result);
			$("#jsonBox3").html("");
			$("#jsonBox4").html("");
			$("#jsonBox5").html("");
		});
	} else {
		$.post(_contextPath + "/system/productCategory/plist/" + id, function(
				data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="#" id="Two' + productCategorys[i].id
						+ '" onclick="searchTwoName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">' + '>'
						+ productCategorys[i].name + '</a>';
			}

			$("#jsonBox2").html(result);
			$("#jsonBox3").html("");
			$("#jsonBox4").html("");
			$("#jsonBox5").html("");
		});
	}
	$(this).next("div.pro_glpp").show();
}
//收索出二级页面
function searchTwoName(id, name) {
	var _contextPath = $("#ctxpath").val();
	var Two = "#Two" + id;
	$(Two).siblings().removeClass("cur");
	$(Two).addClass("cur");
	$("#span2").html('> ' + name);
	$("#span3").html("");
	$("#span4").html("");
	$("#span5").html("");
	$("#id").val(id);

	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/system/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="#" id="three' + productCategorys[i].id
						+ '" onclick="searchThreeName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">' + '>'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox3").html(result);
			$("#jsonBox4").html("");
			$("#jsonBox5").html("");
		});
	} else {
		$.post(_contextPath + "/system/productCategory/plist/" + id, function(
				data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="#" id="three' + productCategorys[i].id
						+ '" onclick="searchThreeName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">' + '>'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox3").html(result);
			$("#jsonBox4").html("");
			$("#jsonBox5").html("");
		});
	}
	$(this).next("div.pro_glpp").show();
}
//收索出三级页面
function searchThreeName(id, name) {
	var _contextPath = $("#ctxpath").val();
	var three = "#three" + id;
	$(three).siblings().removeClass("cur");
	$(three).addClass("cur");
	$("#span3").html('> ' + name);
	$("#span4").html("");
	$("#span5").html("");
	$("#id").val(id);
	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/system/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="#" id="four' + productCategorys[i].id
						+ '" onclick="searchFourName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">' + '>'
						+ productCategorys[i].name + '</a>';
			}

			$("#jsonBox4").html(result);
			$("#jsonBox5").html("");
		});
	} else {
		$.post(_contextPath + "/system/productCategory/plist/" + id, function(
				data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="#" id="four' + productCategorys[i].id
						+ '" onclick="searchFourName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">' + '>'
						+ productCategorys[i].name + '</a>';
			}

			$("#jsonBox4").html(result);
			$("#jsonBox5").html("");
		});
	}
	$(this).next("div.pro_glpp").show();
}
//收索出四级页面
function searchFourName(id, name) {
	var _contextPath = $("#ctxpath").val();
	var four = "#four" + id;
	$(four).siblings().removeClass("cur");
	$(four).addClass("cur");
	$("#span4").html('> ' + name);
	$("#span5").html("");
	$("#id").val(id);

	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/system/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="#" id="five' + productCategorys[i].id
						+ '" onclick="searchFiveName(' + '\''
						+ productCategorys[i].name + '\'' + ');>' + '>'
						+ productCategorys[i].name + '</a>';
			}

			$("#jsonBox5").html(result);
		});
	} else {
		$.post(_contextPath + "/system/productCategory/plist/" + id, function(
				data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="#" id="five' + productCategorys[i].id
						+ '"onclick="searchFiveName(' + '\''
						+ productCategorys[i].name + '\'' + ');>' + '>'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox5").html(result);
		});
	}
	$(this).next("div.pro_glpp").show();
}
//收索出五级页面
function searchFiveName(id, name) {
	var five = "#five" + id;
	$(five).siblings().removeClass("cur");
	$(five).addClass("cur");
	$("#span5").html('> ' + name);
	$("#id").val(id);
}

//searchProductCategoryForm的提交
function searchSubmit() {
	$("#id").val("");
	$("#name").val("");
	$("#span1").html("");
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	$("#span5").html("");
	$("#jsonBox2").html("");
	$("#jsonBox3").html("");
	$("#jsonBox4").html("");
	$("#jsonBox5").html("");
	var val = $("#categorySearch").val();
	$('#font2').show();
	$('#font1').hide();
	if(val.trim()!="" && val!=null){
		$("#font2").load("/system/product/productCategory/search/" + val);
	}else{
		$("#font2").load("/system/product/productCategory/search/" + null);
	}
}

function goBack(){
	$("#id").val("");
	$("#name").val("");
	$("#span1").html("");
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	$("#span5").html("");
	$('#font2').hide();
	$('#font1').show();
}

//确定选择的分类
function changeTemp(){
	
	if($("#productId").val()!="" && $("#productId").val() != null && $("#id").val()!=""){
		var _name = $("#span1").html()+$("#span2").html()
		+$("#span3").html()+$("#span4").html();
		$("#fullName").html(_name);
		$("#templateBg").hide();
		$("#templatetc").hide();
		location.href=$("#ctxpath").val()+"/system/product/editLoad/"+$("#productId").val()+"/"+$("#id").val();
		CKEDITOR.replace('description');
		$("#mainCategoryId").val($("#id").val());
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
		
		if($("#proTemplate").val() != ""){
			loadTempfields($("#proTemplate").val());	
		}
	}else{
		ealert('请选择分类!');
	}
	
}

//load出可选字段的扩展字段和扩展字段的值
function loadTempfields(value){
	if(value == ""){
		$('#tempFieldsFont').html('');
	}
	$("#tempFieldsFont").load($("#ctxpath").val()+"/system/product/tempfieldsLoad/"+value+"/"+$("#productId").val(),function() {
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
		showModelDetail();
	});
}

/*模板规格型号类选中效果*/
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

/*模板展示*/
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
	
	var templateFields = document.getElementsByName("tempFieldsValue");
	
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
			var proId = "";
			for(a=0;a<templateFields.length;a++){
				if(templateFields[a].value == $("#tempValue" + array[i]).val()){
					proId = templateFields[a].title;
				}
			}
			
			if(proId != null && proId != ""){
				htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\"" + array[i] + "||" + proId + "\" checked></td>";
				htmlStr+="<td>"+ $("#tempValue" + array[i]).val() +"</td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Price\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "Price").val() + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Stock\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "Quantity").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Warn\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "QuantityWarning").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
				htmlStr+="<td><input type=\"hidden\" name=\"" + array[i] + "Title\" value=\"" + titles[0] + "\" />";
			}else{
				htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\"" + array[i] + "\" ></td>";
				htmlStr+="<td>"+ $("#tempValue" + array[i]).val() +"</td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Price\" class=\"i_bg itemBox2\" value=\"" + $("#unitPrice").val() + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Stock\" class=\"i_bg itemBox2\" value=\"" + $("#quantity").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr+="<td><input type=\"text\" name=\"" + array[i] + "Warn\" class=\"i_bg itemBox2\" value=\"" + $("#quantityWarning").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
				htmlStr+="<td><input type=\"hidden\" name=\"" + array[i] + "Title\" value=\"" + titles[0] + "\" />";
			}
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
				var proId = "";
				for(a=0;a<templateFields.length;a++){
					if(templateFields[a].value == ($("#tempValue" + array0[i]).val()+$("#tempValue" + array1[j]).val())){
						proId = templateFields[a].title;
					}
				}
				
				if(proId != null && proId != ""){
					htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\"" + array0[i] + "--" + array1[j] + "||" + proId + "\" checked></td>";
					htmlStr+="<td>"+ $("#tempValue" + array0[i]).val() +"</td>";
					htmlStr+="<td>"+ $("#tempValue" + array1[j]).val() +"</td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Price\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "Price").val() + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Stock\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "Quantity").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Warn\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "QuantityWarning").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
					htmlStr+="<td><input type=\"hidden\" name=\"" + array0[i] + "Title\" value=\"" + titles[0] + "\" />";
					htmlStr+="<td><input type=\"hidden\" name=\"" + array1[j] + "Title\" value=\"" + titles[1] + "\" />";
				}else{
					htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\"" + array0[i] + "--" + array1[j] + "\" ></td>";
					htmlStr+="<td>"+ $("#tempValue" + array0[i]).val() +"</td>";
					htmlStr+="<td>"+ $("#tempValue" + array1[j]).val() +"</td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Price\" class=\"i_bg itemBox2\" value=\"" + $("#unitPrice").val() + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Stock\" class=\"i_bg itemBox2\" value=\"" + $("#quantity").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "Warn\" class=\"i_bg itemBox2\" value=\"" + $("#quantityWarning").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
					htmlStr+="<td><input type=\"hidden\" name=\"" + array0[i] + "Title\" value=\"" + titles[0] + "\" />";
					htmlStr+="<td><input type=\"hidden\" name=\"" + array1[j] + "Title\" value=\"" + titles[1] + "\" />";
				}
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
					var proId = "";
					for(a=0;a<templateFields.length;a++){
						if(templateFields[a].value == ($("#tempValue" + array0[i]).val()+$("#tempValue" + array1[j]).val() + $("#tempValue" + array2[k]).val())){
							proId = templateFields[a].title;
						}
					}
					
					if(proId != null && proId != ""){
						htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" checked  value=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "||" + proId + "\"></td>";
						htmlStr+="<td>"+ $("#tempValue" + array0[i]).val() +"</td>";
						htmlStr+="<td>"+ $("#tempValue" + array1[j]).val() +"</td>";
						htmlStr+="<td>"+ $("#tempValue" + array2[k]).val() +"</td>";
						htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "Price\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "Price").val() + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
						htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "Stock\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "Quantity").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
						htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "Warn\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "QuantityWarning").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
						htmlStr+="<td><input type=\"hidden\" name=\"" + array0[i] + "Title\" value=\"" + titles[0] + "\" />";
						htmlStr+="<td><input type=\"hidden\" name=\"" + array1[j] + "Title\" value=\"" + titles[1] + "\" />";
						htmlStr+="<td><input type=\"hidden\" name=\"" + array2[k] + "Title\" value=\"" + titles[2] + "\" />";
					}else{
						var price = $("#unitPrice").val();
						var stock = $("#quantity").val();
						var warn = $("#quantityWarning").val();
						htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" checked  value=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "\"></td>";
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
						var proId = "";
						for(a=0;a<templateFields.length;a++){
							if(templateFields[a].value == ($("#tempValue" + array0[i]).val()+$("#tempValue" + array1[j]).val() + $("#tempValue" + array2[k]).val() + $("#tempValue" + array3[l]).val())){
								proId = templateFields[a].title;
							}
						}
						
						if(proId != null && proId != ""){
							htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" checked value=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "||" + proId + "\"></td>";
							htmlStr+="<td>"+ $("#tempValue" + array0[i]).val() +"</td>";
							htmlStr+="<td>"+ $("#tempValue" + array1[j]).val() +"</td>";
							htmlStr+="<td>"+ $("#tempValue" + array2[k]).val() +"</td>";
							htmlStr+="<td>"+ $("#tempValue" + array3[l]).val() +"</td>";
							htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "Price\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "Price").val() + "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
							htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "Stock\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "Quantity").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
							htmlStr+="<td><input type=\"text\" name=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "Warn\" class=\"i_bg itemBox2\" value=\"" + $("#" + proId + "QuantityWarning").val() + "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
							htmlStr+="<td><input type=\"hidden\" name=\"" + array0[i] + "Title\" value=\"" + titles[0] + "\" />";
							htmlStr+="<td><input type=\"hidden\" name=\"" + array1[j] + "Title\" value=\"" + titles[1] + "\" />";
							htmlStr+="<td><input type=\"hidden\" name=\"" + array2[k] + "Title\" value=\"" + titles[2] + "\" />";
							htmlStr+="<td><input type=\"hidden\" name=\"" + array3[l] + "Title\" value=\"" + titles[3] + "\" />";
						}else{
							var detailId = "";
							var price = $("#unitPrice").val();
							var stock = $("#quantity").val();
							var warn = $("#quantityWarning").val();
							
							htmlStr+="<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" checked value=\"" + array0[i] + "--" + array1[j] + "--" + array2[k] + "--" + array3[l] + "\"></td>";
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
	}
	
	$("#modelDetailLine").show();
	$(".colorSizeTable tbody").html(htmlStr);	
	
}



function changeFields(tempId,tempFieldName,category,position,valueStr){
	$("#" + tempId).val(tempId + "**" + tempFieldName + "**" + category + "**" + position + "**" + valueStr);
}