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
// searchProductAddForm的提交
function addSubmit() {
	var _name = $("#span1").html()+$("#span2").html()
				+$("#span3").html()+$("#span4").html()+$("#span5").html();
	$("#name").val(_name);
	var _id = $("#id").val();
	if (_id.trim() != "") {
		document.getElementById("searchProductAddForm").submit();
	} else {
		ealert("请选择商品分类！");
	}

}
// 重置
function resetForm() {
	document.getElementsByName("name")[0].value = "";

}

$(document).ready(function() {

	/* 树形菜单 */
	$('.fl_title').click(function() {
		$(this).next(".fl_in").slideToggle();
		$(this).toggleClass("ht_fl_zk");
	});
	$('.fl_nav label').click(function() {
		$(".fl_nav label").removeClass("cur");
		$(this).addClass("cur");
		$(this).text("已选择")
	});
	/* 按钮浮动定位 */
	$(".ht_btn").next().addClass("afterHt");

});

// 收索出一级页面
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
				result += '<a href="javaScript:void(0)" id="Two' + productCategorys[i].id
						+ '" onclick="searchTwoName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
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
				result += '<a href="javaScript:void(0)" id="Two' + productCategorys[i].id
						+ '" onclick="searchTwoName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
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
// 收索出二级页面
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
				result += '<a href="javaScript:void(0)" id="three' + productCategorys[i].id
						+ '" onclick="searchThreeName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
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
				result += '<a href="javaScript:void(0)" id="three' + productCategorys[i].id
						+ '" onclick="searchThreeName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox3").html(result);
			$("#jsonBox4").html("");
			$("#jsonBox5").html("");
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
				result += '<a href="javaScript:void(0)" id="four' + productCategorys[i].id
						+ '" onclick="searchFourName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
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
				result += '<a href="javaScript:void(0)" id="four' + productCategorys[i].id
						+ '" onclick="searchFourName(' + '\''
						+ productCategorys[i].id + '\'' + ',' + '\''
						+ productCategorys[i].name + '\'' + ');">'
						+ productCategorys[i].name + '</a>';
			}

			$("#jsonBox4").html(result);
			$("#jsonBox5").html("");
		});
	}
	$(this).next("div.pro_glpp").show();
}
// 收索出四级页面
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
				result += '<a href="javaScript:void(0)" id="five' + productCategorys[i].id
						+ '" onclick="searchFiveName(' + '\''
						+ productCategorys[i].name + '\'' + ');>'
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
				result += '<a href="javaScript:void(0)" id="five' + productCategorys[i].id
						+ '"onclick="searchFiveName(' + '\''
						+ productCategorys[i].name + '\'' + ');>'
						+ productCategorys[i].name + '</a>';
			}
			$("#jsonBox5").html(result);
		});
	}
	$(this).next("div.pro_glpp").show();
}
// 收索出五级页面
function searchFiveName(id, name) {
	var five = "#five" + id;
	$(five).siblings().removeClass("cur");
	$(five).addClass("cur");
	$("#span5").html('> ' + name);
	$("#id").val(id);
}

// 搜索出的分类的选择
function searchName(name, id) {
	$("#span1").html(name);
	$("#id").val(id);
}

// 返回选择
function goBack() {
	var contextPath = $('#ctxpath').val();
	window.location.href = contextPath + "/system/product/productAddClassify";
}