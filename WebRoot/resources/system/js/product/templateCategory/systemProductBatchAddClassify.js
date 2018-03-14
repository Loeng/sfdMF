//searchProductCategoryForm的提交
function searchSubmit() {
	$("#id").val("");
	$("#name").val("");
	$("#span1").html("");
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	$("#span5").html("");
	var val = $("#categorySearch").val();
	$('#font2').show();
	$('#font1').hide();
	if(val.trim()!="" && val!=null){
		$("#font2").load("/system/product/productCategory/search/" + val);
	}else{
		$("#font2").load("/system/product/productCategory/search/" + null);
	}
}

function unsearchCategory(){
	$("#id").val("");
	$("#name").val("");
	$("#jd1").hide();
	$("#jd2").hide();
	$("#jd3").hide();
	$("#span1").html("");
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	$(".proClassList").show();
	$(".searchList").hide();
}

function searchName(name, id) {
	$("#span1").html(name);
	$("#id").val(id);
}
// searchProductAddForm的提交
function addSubmit() {
	var _name = $("#span1").html()+$("#span2").html()
				+$("#span3").html()+$("#span4").html();
	$("#name").val(_name);
	var _id = $("#id").val();
	if (_id.trim() != "") {
		document.getElementById("searchProductAddForm").submit();
	} else {
		falert("请选择商品分类！");
	}

}
//重置
function resetForm(){
	document.getElementsByName("name")[0].value="";
	
}

//返回选择
function goBack() {
	var contextPath = $('#ctxpath').val();
	window.location.href = contextPath + "/store/product/storeProductAddClassify";
}

//收索出一级页面
function searchFirstName() {
	var _contextPath = $("#ctxpath").val();
	var  num = $("#productCategoryNum").val();
	num =num.split(",");
	var id = num[0];
	var name = num[1];
	var firstName = "#firstName" + id;
	$("#span1").html(name);
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	$("#jd1").hide();
	$("#jd2").hide();
	$("#jd3").hide();
	$(firstName).siblings().removeClass("cur");
	$(firstName).addClass("cur");
	$("#id").val(id);
	$("#productId").val(num);

	var result ='<select id="select" onchange="searchTwoName();" class="i_bg"  style="width:157px;float:left;"><option selected >请选择商品分类</option>';
	$("#selectOne").html("");
	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post(_contextPath + "/system/productCategory/plist/" + id, function(data) {
			
			var productCategorys = jQuery.parseJSON(data);
			for ( var i = 0; i < productCategorys.length; i++) {
				 result +='<option value ="'+productCategorys[i].id+'","'+productCategorys[i].name+'">'+productCategorys[i].name+'</option>';
			}
			$("#selectOne").html(result+'</select>');
			$("#jsonBox3").html("");
			$("#jsonBox4").html("");
			$(this).next("div.pro_glpp").show();
		});
	} else {
		$.post(_contextPath + "/system/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			if(data!=""){
				for ( var i = 0; i < productCategorys.length; i++) {
					 result +='<option value="'+productCategorys[i].id+','+productCategorys[i].name+'">'+productCategorys[i].name+'</option>';
				}
				$("#selectOne").html(result+'</select>');
				$("#jsonBox3").html("");
				$("#jsonBox4").html("");
				$(this).next("div.pro_glpp").show();
			}
			
		});
	}
}
// 收索出二级页面
function searchTwoName() {
	var _contextPath = $("#ctxpath").val();
	var  num = $("#select").val().split(",");
	var id = num[0];
	var name = num[1];
	var Two = "#Two" + id;
	$(Two).siblings().removeClass("cur");
	$(Two).addClass("cur");
	$("#span2").html(name);
	$("#jd1").show();
	$("#jd2").hide();
	$("#jd3").hide();
	$("#span3").html("");
	$("#span4").html("");
	$("#id").val(id);
	$("#productId").val(num);
	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/store/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="three' + productCategorys[i].id
						+ '" onclick="searchThreeName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox3").html(result);
			$("#jsonBox4").html("");
		});
	} else {
		$.post(_contextPath + "/store/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="three' + productCategorys[i].id
						+ '" onclick="searchThreeName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox3").html(result);
			$("#jsonBox4").html("");
		});
	}
	$(this).next("div.pro_glpp").show();
}
// 收索出三级页面
function searchThreeName(id, name) {
	var _contextPath = $("#ctxpath").val();
	var three = "#three" + id;
	$(three).siblings().removeClass("cur");
	$(three).addClass("cur");
	$("#span3").html(name);
	$("#jd2").show();
	$("#jd3").hide();
	$("#span4").html("");
	$("#id").val(id);
	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/store/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="four' + productCategorys[i].id
						+ '" onclick="searchFourName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
						+ productCategorys[i].name + '</a>';
			}

			$("#jsonBox4").html(result);
		});
	} else {
		$.post(_contextPath + "/store/productCategory/plist/" + id, function(
				data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="four' + productCategorys[i].id
						+ '" onclick="searchFourName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
						+ productCategorys[i].name + '</a>';
			}

			$("#jsonBox4").html(result);
		});
	}
	$(this).next("div.pro_glpp").show();
}
// 收索出四级页面
function searchFourName(id, name) {
	var four = "#four" + id;
	$(four).siblings().removeClass("cur");
	$(four).addClass("cur");
	$("#span4").html(name);
	$("#jd3").show();
	$("#id").val(id);
}

function Category(){
	var id = $("#CategoryNum").val();
	if(id!=""){
		$("#categoryId").val(id);
	}
}
function downLoad(){
	if($("#productId").val()==""){
		ealert("请选择商品分类");
		return fasle;
	}
	if($("#categoryId").val()==""){
		ealert("请选择模板分类");
		return false;
	}
	document.getElementById("excelDownLoadForm").submit();
}

