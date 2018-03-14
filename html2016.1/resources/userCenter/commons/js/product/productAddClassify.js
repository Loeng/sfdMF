//searchProductCategoryForm的提交
function searchCategory() {
	$("#id").val("");
	$("#name").val("");
	$("#jd1").hide();
	$("#jd2").hide();
	$("#jd3").hide();
	$("#span1").html("");
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	var val = $("#categorySearch").val();
	var type = $("#type").val();
	if($.trim(val)!=""){
		$(".searchClass").load($("#ctxpath").val() + "/store/product/productCategory/search/" + $.trim(val)+"/"+type);
		$(".proClassList").hide();
		$(".searchList").show();
	}else{
		falert("请输入您需要搜索的商品分类名称");
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
	if ($.trim(_id) != "") {
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
function searchFirstName(id, name) {
	var _contextPath = $("#ctxpath").val();
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

	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/store/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="Two' + productCategorys[i].id
						+ '" onclick="searchTwoName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox2").html(result);
			$("#jsonBox3").html("");
			$("#jsonBox4").html("");
		});
	} else {
		$.post(_contextPath + "/store/productCategory/plist/" + id, function(
				data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for ( var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="Two' + productCategorys[i].id
						+ '" onclick="searchTwoName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
						+ productCategorys[i].name + '</a>';
			}

			$("#jsonBox2").html(result);
			$("#jsonBox3").html("");
			$("#jsonBox4").html("");
		});
	}
	$(this).next("div.pro_glpp").show();
}
// 收索出二级页面
function searchTwoName(id, name) {
	var _contextPath = $("#ctxpath").val();
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
		$.post(_contextPath + "/store/productCategory/plist/" + id, function(
				data) {
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