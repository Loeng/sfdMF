// 重置
function reset(){
	document.getElementById("modifyProductForm").reset();
}

//验证商品名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("商品名为1-20位字符组成");
		$("#nameCheck").val("false");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("商品名为1-20位字符组成");
		$("#nameCheck").val("true");
		return true;
	}
}

//验证商品名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 1 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("商品名为1-20位字符组成");
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

//验证品牌格式
function checkBrandBlur(){
	var nameStr = document.getElementsByName('brand')[0].value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("brandDd").className="text_c_ts";
		$("#nameSpan").html("请选择关联品牌");
		return false;
	}else{
		document.getElementById("brandDd").className="";
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

//验证模板格式
function checkTemlateBlur(){
	var nameStr = document.getElementsByName('templateId')[0].value;
	if(nameStr == null || nameStr == "" || nameStr ==" "){
		document.getElementById("templateIdDd").className="text_c_ts";
		$("#nameSpan").html("请选择关联模板");
		return false;
	}else{
		document.getElementById("templateIdDd").className="";
		return true;
	}
}
// 验证上传图片格式
function validateForm(modify) {
	// 可不上传图标，直接返回true
	if (modify.bigPicture.value == ""){
		return true;
	}     
	//截取提交上传文件的扩展名  
	var ext = modify.bigPicture.value.match(/^(.*)(\.)(.{1,8})$/)[3];
	ext = ext.toLowerCase(); //设置允许上传文件的扩展名           
	if (ext ==  "jpg" || ext == "gif" || ext=="png") {
		return true;
	} else {
		alert("只允许上传 .jpg或gif 或png文件，请重新选择需要上传的文件！");
		return false;
	}
}
// 返回列表
function goBack(contextPath){
	if(contextPath==null){
		window.location.href="/store/product/sales";
	}
	window.location.href=contextPath + "/store/product/sales";
}
//仓库中的列表的返回
function Back(contextPath){
	if(contextPath==null){
		window.location.href="/store/product/stocks";
	}
	window.location.href=contextPath + "/store/product/stocks";
}



//提交
function formSubmit() {
	if(checkNameBlur() && checkStoreIdBlur() && checkTemlateBlur() && checkBrandBlur() ){
		document.getElementById("modifyProductForm").submit();
	}else{
		checkNameBlur();
		checkStoreIdBlur();
		checkTemlateBlur();
		checkBrandBlur();
	}
}


var flag = false;
// 判定后台是否返回添加成功的信息
	
$(function(){
	if($("#modifyOk").val()=="true"){
		econfirm('修改成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
	}else if(!$("#modifyOk").val()=="false"){
		ealert("修改失败！");
	}
	
	$("#brandSpan").click(function(){
		var _contextPath = $("#ctxpath").val();
		alert(_contextPath);
		// 判定ctxpath是否为空
		if(_contextPath == null || _contextPath == ''){
			$.post(
					"/store/product/brand/plist",
				 	function(data){
				 		var brand = jQuery.parseJSON(data);
				 		var result = '';
				 		for(var i=0;i<brand.length;i++) {
				 			result += '<span><input type="radio" name="productBrand" value="'+brand[i].name+'" onclick="addBrand('+'\''+brand[i].name+'\''+');"/>'+brand[i].name+'</span>';
				 		}
				 		document.getElementById("jsonBrand").innerHTML = result;
				 	}
				 );
		}else{
			$.post(
					_contextPath + "/store/product/brand/plist",
				 	function(data){
				 		var brand = jQuery.parseJSON(data);
				 		var result = '';
				 		for(var i=0;i<brand.length;i++) {
				 			result += '<span><input type="radio" name="productBrand" value="'+brand[i].name+'" onclick="addBrand('+'\''+brand[i].name+'\''+');"/>'+brand[i].name+'</span>';
				 		}
				 		document.getElementById("jsonBrand").innerHTML = result;
				 	}
				 );
		}
		$(this).next("div.pro_glpp").show();
	});
	
	// 点击关联店铺，查询店铺列表
	$("#storeSpan").click(function(){
		var _contextPath = $("#ctxpath").val();
		// 判定ctxpath是否为空
		if(_contextPath ==null || _contextPath == ""){
			$.post(
				"/store/store/plist",
			 	function(data){
			 		var store = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<store.length;i++) {
			 			result += '<span><input type="radio" name="store" value="'+store[i].storeName+'" onclick="addStore('+'\''+store[i].storeName+'\''+');"/>'+store[i].storeName+'</span>';
			 		}
			 		document.getElementById("jsonStore").innerHTML = result;
			 	}
			 );
		}else{
			$.post(
					_contextPath + "/store/store/plist",
				 	function(data){
				 		var store = jQuery.parseJSON(data);
				 		var result = '';
				 		for(var i=0;i<store.length;i++) {
				 			result += '<span><input type="radio" name="store" value="'+store[i].storeName+'" onclick="addStore('+'\''+store[i].storeName+'\''+');"/>'+store[i].storeName+'</span>';
				 		}
				 		document.getElementById("jsonStore").innerHTML = result;
				 	}
				 );
		}
		$(this).next("div.pro_glpp").show();
	});
	
	//点击关联模板
	$("#tempLateSpan").click(function(){
		var _contextPath = $("#ctxpath").val();
		// 判定ctxpath是否为空
		if(_contextPath == null || _contextPath == ""){
			$.post(
					"/store/template/plist",
				 	function(data){
				 		var temp = jQuery.parseJSON(data);
				 		var result = '';
				 		for(var i=0;i<temp.length;i++) {
				 			result += '<span><input type="radio" name="temp" value="'+temp[i].name+'" onclick="addTempLate('+'\''+temp[i].name+'\''+');"/>'+temp[i].name+'</span>';
				 		}
				 		document.getElementById("jsonTempLate").innerHTML = result;
				 	}
				 );
		}else{
			$.post(
					_contextPath + "/store/template/plist",
				 	function(data){
				 		var temp = jQuery.parseJSON(data);
				 		var result = '';
				 		for(var i=0;i<temp.length;i++) {
				 			result += '<span><input type="radio" name="temp" value="'+temp[i].name+'" onclick="addTempLate('+'\''+temp[i].name+'\''+');"/>'+temp[i].name+'</span>';
				 		}
				 		document.getElementById("jsonTempLate").innerHTML = result;
				 	}
				 );
		}
		$(this).next("div.pro_glpp").show();
	});
	// 点击关闭按钮，将选中的品牌或店铺填写到表单
	$(".pro_ppList div span").click(function(){
		var items = document.getElementsByTagName("input");  
	    for(var i=0;i<items.length;i++){  
	        if(items[i].type=="radio"&&items[i].name=="productBrand"&&items[i].checked){
	            $("#brand").val(items[i].value);
	        }
	        if(items[i].type=="radio"&&items[i].name=="store"&&items[i].checked){
	            $("#store").val(items[i].value);
	        }
	        if(items[i].type=="radio"&&items[i].name=="temp"&&items[i].checked){
	            $("#tempLate").val(items[i].value);
	        }
	    }  
		$(this).parent().parent().parent("div.pro_glpp").hide();
	});
	
	
	$("input.i_bg").focus(function (){ 
			$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	}); 
});	

function addStore(name){
	document.getElementById("store").value= name;
}
function addBrand(name){
	document.getElementById("brand").value= name;
}
function addTempLate(name){
	document.getElementById("tempLate").value= name;
}





//收索出一级页面
function searchFirstName(id,name){
	var _contextPath = $("#ctxpath").val();
	document.getElementById("span1").innerHTML = name;
	document.getElementById("span2").innerHTML = "";
	document.getElementById("span3").innerHTML = ""; 
	document.getElementById("span4").innerHTML = "";
	document.getElementById("span5").innerHTML = "";
	document.getElementById("pcId").value = id;
	document.getElementById("mainCategory").value = name;
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/store/productCategory/plist"+id,
			 	function(data){
			 		var productCategorys = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<productCategorys.length;i++) {
			 			result += '<a href="#" onclick="searchTwoName('+'\''+productCategorys[i].id+'\''+','+'\''+productCategorys[i].name+'\''+');">'+'>'+productCategorys[i].name+'</a>';
			 		}
			 		document.getElementById("jsonBox2").innerHTML = result;
			 		document.getElementById("jsonBox3").innerHTML = "";
			 		document.getElementById("jsonBox4").innerHTML = "";
			 		document.getElementById("jsonBox5").innerHTML = "";
			 	}
			 );
	}else{
		$.post(
				_contextPath + "/store/productCategory/plist"+id,
			 	function(data){
			 		var productCategorys = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<brand.length;i++) {
			 			result += '<a href="#" onclick="searchTwoName('+'\''+productCategorys[i].id+'\''+','+'\''+productCategorys[i].name+'\''+');">'+'>'+productCategorys[i].name+'</a>';
			 		}
			 		document.getElementById("jsonBox2").innerHTML = result;
			 		document.getElementById("jsonBox3").innerHTML = "";
			 		document.getElementById("jsonBox4").innerHTML = "";
			 		document.getElementById("jsonBox5").innerHTML = "";
			 	}
			 );
	}
	$(this).next("div.pro_glpp").show();	
}
//收索出二级页面
function searchTwoName(id,name){
	var _contextPath = $("#ctxpath").val();
	document.getElementById("span2").innerHTML ='>'+ name;
	document.getElementById("span3").innerHTML = ""; 
	document.getElementById("span4").innerHTML = "";
	document.getElementById("span5").innerHTML = "";
	document.getElementById("pcId").value = id;
	document.getElementById("mainCategory").value = name;
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/store/productCategory/plist"+id,
			 	function(data){
			 		var productCategorys = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<productCategorys.length;i++) {
			 			result += '<a href="#" onclick="searchThreeName('+'\''+productCategorys[i].id+'\''+','+'\''+productCategorys[i].name+'\''+');">'+'>'+productCategorys[i].name+'</a>';
			 		}
			 		document.getElementById("jsonBox3").innerHTML = result;
			 		document.getElementById("jsonBox4").innerHTML = "";
			 		document.getElementById("jsonBox5").innerHTML = "";
			 	}
			 );
	}else{
		$.post(
				_contextPath + "/store/productCategory/plist"+id,
			 	function(data){
			 		var productCategorys = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<brand.length;i++) {
			 			result += '<a href="#" onclick="searchThreeName('+'\''+productCategorys[i].id+'\''+','+'\''+productCategorys[i].name+'\''+');">'+'>'+productCategorys[i].name+'</a>';
			 		}
			 		document.getElementById("jsonBox3").innerHTML = result;
			 		document.getElementById("jsonBox4").innerHTML = "";
			 		document.getElementById("jsonBox5").innerHTML = "";
			 	}
			 );
	}
	$(this).next("div.pro_glpp").show();	
}
//收索出三级页面
function searchThreeName(id,name){
	var _contextPath = $("#ctxpath").val();
	document.getElementById("span3").innerHTML = '>'+ name;
	document.getElementById("span4").innerHTML = "";
	document.getElementById("span5").innerHTML = "";
	document.getElementById("pcId").value = id;
	document.getElementById("mainCategory").value = name;
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/store/productCategory/plist"+id,
			 	function(data){
			 		var productCategorys = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<productCategorys.length;i++) {
			 			result += '<a href="#" onclick="searchFourName('+'\''+productCategorys[i].id+'\''+','+'\''+productCategorys[i].name+'\''+');">'+'>'+productCategorys[i].name+'</a>';
			 		}
			 		document.getElementById("jsonBox4").innerHTML = result;
			 		document.getElementById("jsonBox5").innerHTML = "";
			 	}
			 );
	}else{
		$.post(
				_contextPath + "/store/productCategory/plist"+id,
			 	function(data){
			 		var productCategorys = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<brand.length;i++) {
			 			result += '<a href="#" onclick="searchFourName('+'\''+productCategorys[i].id+'\''+','+'\''+productCategorys[i].name+'\''+');">'+'>'+productCategorys[i].name+'</a>';
			 		}
			 		document.getElementById("jsonBox4").innerHTML = result;
			 		document.getElementById("jsonBox5").innerHTML = "";
			 	}
			 );
	}
	$(this).next("div.pro_glpp").show();	
}
//收索出四级页面
function searchFourName(id,name){
	var _contextPath = $("#ctxpath").val();
	document.getElementById("span4").innerHTML = '>'+ name;
	document.getElementById("span5").innerHTML = "";
	document.getElementById("pcId").value = id;
	document.getElementById("mainCategory").value = name;
	// 判定ctxpath是否为空
	if(_contextPath == null || _contextPath == ''){
		$.post(
				"/store/productCategory/plist"+id,
			 	function(data){
			 		var productCategorys = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<productCategorys.length;i++) {
			 			result += '<a href="#" onclick="searchFiveName('+'\''+productCategorys[i].name+'\''+');>'+'>'+productCategorys[i].name+'</a>';
			 		}
			 		document.getElementById("jsonBox5").innerHTML = result;
			 		
			 	}
			 );
	}else{
		$.post(
				_contextPath + "/store/productCategory/plist"+id,
			 	function(data){
			 		var productCategorys = jQuery.parseJSON(data);
			 		var result = '';
			 		for(var i=0;i<brand.length;i++) {
			 			result += '<a href="#" onclick="searchFiveName('+'\''+productCategorys[i].name+'\''+');>'+'>'+productCategorys[i].name+'</a>';
			 		}
			 		document.getElementById("jsonBox5").innerHTML = result;
			 		
			 	}
			 );
	}
	$(this).next("div.pro_glpp").show();	
}
//收索出五级页面
function searchFiveName(id,name){
	document.getElementById("span5").innerHTML = '>'+ name;
	document.getElementById("pcId").value = id;
	document.getElementById("mainCategory").value = name;
}