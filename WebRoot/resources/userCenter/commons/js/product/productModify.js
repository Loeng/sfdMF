String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
		return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi" : "g")),
				replaceWith);
	} else {
		return this.replace(reallyDo, replaceWith);
	}
}
$(document).ready(
		function() {
			/* 弹层 */
			/* 选择品牌弹层 */
			$(".clickBrand").click(
					function() {
						$(".alertBrand").load(
								$("#ctxpath").val()
										+ "/store/product/brand/plist");
						$(".apLayerBg").show();
						$(".alertBrand").show();
					})

			$(".clickTemp").click(function() {
				$(".apLayerBg").show();
				$(".alertTemp").show();
			})
			var isAdvance = $("#adradio").val();
			if(isAdvance == "1"){
				$("#advanceFont").show();
			}else{
				$("#advanceFont").hide();
			}
			/* 增加条目 */
			$(".btnAddLine").click(function() {
				var obj = $("#addFieldsDemo").html();
				$(this).siblings(".pfPriceList").append(obj);
			})

			/* 删除条目 */
			$(".closeIco").live("click", function() {
				$(this).parents(".listItem").remove();
			})

			// 判定后台是否返回添加成功的信息
			if ($("#ok").val() == "true") {
				econfirm('添加成功，是否继续添加？', null, null, goBack, [ $("#ctxpath")
						.val() ]);
			} else if ($("#ok").val() == "false") {
				falert("添加失败！");
			}

			/* 增加图片 */
			$(".btnAdd").click(function() {
				var radomId = "popPic" + new Date().getTime();
				var obj = $("#picForClone").html();
				obj = obj.replaceAll("picClone", radomId);
				$(this).before(obj);
				var popPicNum = $("#popPicProperties").val();
				popPicNum = popPicNum + radomId + ";";
				$("#popPicProperties").val(popPicNum);
			})
			/* 删除图片 */
			$(".uploadItem .closeIco").live("click", function() {
				$(this).parents(".uploadItem").remove();
				var popPicNum = $("#popPicProperties").val();
				var oldId = $(this).attr("id") + ";";
				popPicNum = popPicNum.replaceAll(oldId, "");
				$("#popPicProperties").val(popPicNum);
			});

			if ($("#proTemplate").val() != "") {
				loadTempfields($("#proTemplate").val());
			}
			
		});

//选择大宗商品时隐藏商城价、市场价 显示批发价和价格区间
function checkRadioD(){
	$("#unitPriceDiv").css("display","none");
	$("#listPriceDiv").css("display","none");
	$("#pfPriceDiv").css("display","block");
	$("#jtjDiv").css("display","block");
}
//选择优选商品时显示商城价、市场价 隐藏批发价和价格区间
function checkRadioY(){
	$("#unitPriceDiv").css("display","block");
	$("#listPriceDiv").css("display","block");
	$("#pfPriceDiv").css("display","none");
	$("#jtjDiv").css("display","none");
}
function searchName(name, id) {
	$("#span1").html(name);
	$("#id").val(id);
}

$(function() {
	if ($("#modifyOk").val() == "true") {
		econfirm('修改成功，是否继续修改？', null, null, goList, [ $("#ctxpath").val() ]);
	} else if (!$("#modifyOk").val() == "false") {
		falert("修改失败！");
	}
	$("input.i_bg").focus(function() {
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function() {
		$(this).parent().removeClass("text_ts");
	});
});

function goPageBrand(p) {
	var name = document.getElementById("nameBrand").value;
	if (name != null && name != "" && name != " ") {
		$("#brandtc").load(
				$("#ctxpath").val() + "/store/product/brand/plistSearch/"
						+ name + "/" + p);
	} else {
		$("#brandtc").load(
				$("#ctxpath").val() + "/store/product/brand/plistSearch/"
						+ null + "/" + p);
	}
}

// 品牌弹出层的搜索
function searchBrandList() {
	var name = document.getElementById("nameBrand").value;
	if (name != null && name != "" && name != " ") {
		$("#brandtc").load(
				$("#ctxpath").val() + "/store/product/brand/plistSearch/"
						+ name + "/1");
	} else {
		$("#brandtc").load($("#ctxpath").val() + "/store/product/brand/plist");
	}
	$("#brandBg").show();
	$("#brandtc").show();
}
// 重置品牌搜索
function resetBrand() {
	document.getElementById("nameBrand").value = "";
}

function reset() {
	location.reload();
}

// 查询出品牌详细信息
function loadBrand() {
	$("#brandtc").load($("#ctxpath").val() + "/store/product/brand/plist",
			function() {
				
			});
	$("#brandBg").show();
	$("#brandtc").show();
}
// 选择品牌
function sreachBrand(brandName, brandId) {
	$('#brandName').val(brandName);
	$('#brand').val(brandId);
	$(".apLayerBg").hide();
	$(".alertBrand").hide();
}

function showTemp() {
	$("#templateBg").show();
	$("#templatetc").show();
	
}

function closeTemp() {
	$("#templateBg").hide();
	$("#templatetc").hide();
}
// 弹出层的搜索条件重置
function resetForm() {
	document.getElementById("name").value = "";
}
// 关闭弹出层
function apClose() {
	$("#brandBg").hide();
	$("#brandtc").hide();
}

// 验证商品名格式
function checkName() {
	var nameStr = document.getElementsByName('name')[0].value;
	if (nameStr.length < 1 || nameStr.length > 128 || nameStr == " ") {
		$("#nameSpan").html("商品名为1-128位字符组成");
		$("#nameSpan").show();
		return false;
	} else {
		$("#nameSpan").html("商品名为1-128位字符组成");
		$("#nameSpan").hide();
		return true;
	}
}

// 验证商品名格式
function checkNameBlur() {
	var nameStr = document.getElementsByName('name')[0].value;
	if (nameStr.length < 1 || nameStr.length > 128 || nameStr == " ") {
		$("#nameSpan").html("商品名为1-128位字符组成");
		$("#nameSpan").show();
		return false;
	} else {
		$("#nameSpan").hide();
		return true;
	}
}

// 验证商品编号是否存在
function checkIdBlur(productNumber) {
	var contextPath = $('#ctxpath').val();
	flag = false;
	if (productNumber == null || productNumber == "" || productNumber == " "
			|| productNumber.length < 6 || productNumber.length > 16) {
		$("#idSpan").html("商品编号为6-16位字符");
		$("#idSpan").show();
		$("#idCheck").val(false);
		return false;
	} else {
		$.post(contextPath + "/store/product/checkId/" + productNumber + "/"
				+ $("#productId").val(), function(data) {
			if (!data) {
				$("#idSpan").html("商品编号已存在");
				$("#idSpan").show();
				$("#idCheck").val(false);
				return false;
			} else {
				$("#idSpan").hide();
				$("#idCheck").val(true);
				return true;
			}
		});
	}
}


// 返回列表
function goList(contextPath) {
	var returnType = $("#returnType").val();
	var type = $("#pType").val();
    var jumpStr = "PRODUCT_STOCKS";
    var saleJumpStr = "";
    if (type == "0") {
        jumpStr = "PRODUCT_STOCKS";
        saleJumpStr = "PRODUCT_SALES";
    } else if (type == "1") {
        jumpStr = "GREEN_PRODUCT_STOCKS";
        saleJumpStr = "GREEN_PRODUCT_SALES";
    } else {
        jumpStr = "GREEN_PRODUCT_STOCKS";
        saleJumpStr = "GREEN_PRODUCT_SALES";
    }
    choseMenu($('#ctxpath').val(),jumpStr);
	if ("check" == returnType) {
		//window.location.href= contextPath + "/store/product/stocks/"+type;
		choseMenu(contextPath,jumpStr);

	} else {
		//window.location.href= contextPath + "/store/product/sales/"+type;
		choseMenu(contextPath,saleJumpStr);
	}
}

// 提交
function formSubmit() {
	var _id = document.getElementsByName("productNumber")[0].value;
	//验证商品名
	if (!checkNameBlur() || !$("#idCheck").val()) {
		return;
	}
	// 验证库存
	if ($("#quantity").val() <= 0) {
		$("#quantitySpan").html("库存量最小大于1");
		$("#quantitySpan").show();
		return;
	} else {
		$("#quantitySpan").hide();
	}

	// 验证数量单位
	if ($("#unit").val() == "" || $("#unit").val() == " ") {
		$("#unitSpan").html("请输入数量的单位。如:吨");
		$("#unitSpan").show();
		return;
	} else {
		$("#unitSpan").hide();
	}

	//验证产地
	var habitatInfo=$("#habitat").val();
	if(habitatInfo == ""||habitatInfo=="null"){
		document.getElementById("habitat").className="text_c_ts";
		$('#habitatSpan').show();
		$('#habitatSpan').text("请选择产地");
		return false;
	}else{
	$('#habitatSpan').hide();
	}
	
	//验证交货地
	var deliceAddressInfo=$("#deliceAddress").val();
	if(deliceAddressInfo == ""||deliceAddressInfo=="null"){
		document.getElementById("deliceAddress").className="text_c_ts";
		$('#deliceAddressSpan').show();
		$('#deliceAddressSpan').text("请选择交货地");
		return false;
	}else{
	$('#deliceAddressSpan').hide();
	}
	
	
	//验证质量等级
	var productModel=$("#productModel").val();
	if(productModel == ""||productModel=="null"){
		document.getElementById("productModel").className="text_c_ts";
		$('#productModelSpan').show();
		$('#productModelSpan').text("请填写质量等级");
		return false;
	}else{
		$('#productModelSpan').hide();
	}
	
	
	
	var editorStr = CKEDITOR.instances.description.getData();
	$("#description").val(editorStr);
	$("#modifyProductForm").ajaxSubmit(
			function(data) {
				if (data == true) {
					econfirm('修改成功，是否继续修改？', null, null, goList,[ $("#ctxpath").val() ]);
				} else {
					falert("修改失败！");
				}
			});
}

function searchCategory() {
	$("#id").val("");
	$("#name").val("");
	$("#span1").html("");
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	var val = $("#categorySearch").val();
	if (val.trim() != "" && val != null) {
		$("#font2").load("/store/product/productCategory/search/" + val);
	} else {
		$("#font2").load("/store/product/productCategory/search/" + null);
	}
	$("#font1").hide();
	$("#font2").show();
}

function goBack() {
	$("#id").val("");
	$("#name").val("");
	$("#span1").html("");
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	$('#font2').hide();
	$('#font1').show();
}

// 确定选择的分类
function changeTemp() {
	var _name = $("#span1").html() + $("#span2").html() + $("#span3").html()
			+ $("#span4").html();
	$("#fullName").html(_name);
	$("#templateBg").hide();
	$("#templatetc").hide();
	$("#modifyLoad").load(
			$("#ctxpath").val() + "/store/product/editLoad/"
					+ $("#productId").val() + "/" + $("#id").val(), function() {
				CKEDITOR.replace('description');
				$("#mainCategoryId").val($("#id").val());
				/* 增加图片 */
				$(".btnAdd").click(function() {
					var radomId = "popPic" + new Date().getTime();
					var obj = $("#picForClone").html();
					obj = obj.replaceAll("picClone", radomId);
					$(this).before(obj);
					var popPicNum = $("#popPicProperties").val();
					popPicNum = popPicNum + radomId + ";";
					$("#popPicProperties").val(popPicNum);
				})

				if ($("#proTemplate").val() != "") {
					loadTempfields($("#proTemplate").val());
				}
			});
}
// 收索出一级页面
function searchFirstName(id, name) {
	var _contextPath = $("#ctxpath").val();
	var firstName = "#firstName" + id;
	$("#span1").html(name);
	$("#span2").html("");
	$("#span3").html("");
	$("#span4").html("");
	$(firstName).siblings().removeClass("cur");
	$(firstName).addClass("cur");
	$("#id").val(id);

	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/store/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for (var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="Two'
						+ productCategorys[i].id + '" onclick="searchTwoName('
						+ '\'' + productCategorys[i].id + '\'' + ',' + '\''
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
			for (var i = 0; i < brand.length; i++) {
				result += '<a href="javaScript:void(0)" id="Two'
						+ productCategorys[i].id + '" onclick="searchTwoName('
						+ '\'' + productCategorys[i].id + '\'' + ',' + '\''
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
	$("#span2").html('> ' + name);
	$("#span3").html("");
	$("#span4").html("");
	$("#id").val(id);

	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/store/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for (var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="three'
						+ productCategorys[i].id
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
			for (var i = 0; i < brand.length; i++) {
				result += '<a href="javaScript:void(0)" id="three'
						+ productCategorys[i].id
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
	$("#span3").html('> ' + name);
	$("#span4").html("");
	$("#id").val(id);
	// 判定ctxpath是否为空
	if (_contextPath == null || _contextPath == '') {
		$.post("/store/productCategory/plist/" + id, function(data) {
			var productCategorys = jQuery.parseJSON(data);
			var result = '';
			for (var i = 0; i < productCategorys.length; i++) {
				result += '<a href="javaScript:void(0)" id="four'
						+ productCategorys[i].id + '" onclick="searchFourName('
						+ '\'' + productCategorys[i].id + '\'' + ',' + '\''
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
			for (var i = 0; i < brand.length; i++) {
				result += '<a href="javaScript:void(0)" id="four'
						+ productCategorys[i].id + '" onclick="searchFourName('
						+ '\'' + productCategorys[i].id + '\'' + ',' + '\''
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
	$("#span4").html('> ' + name);
	$("#id").val(id);
}
// load出可选字段的扩展字段和扩展字段的值
function loadTempfields(value) {
	if (value == "") {
		$('#tempFieldsFont').html('');
	}
	$("#tempFieldsFont").load(
			$("#ctxpath").val() + "/store/product/tempfieldsLoad/" + value
					+ "/" + $("#productId").val(), function() {
				var itemChecks = $(".itemCheck");
				itemChecks.each(function(index) {
					if ($(this).is(":checked")) {
						$(this).siblings(".imgForbid").hide();
						$(this).siblings(".imgUpload").show();
					} else {
						$(this).siblings(".imgUpload").hide();
						$(this).siblings(".imgForbid").show();
					}
				});
				showModelDetail();
			});
}

/* 模板规格型号类选中效果 */
function selectModel(obj) {
	if ($(obj).is(":checked")) {
		$(obj).siblings(".imgForbid").hide();
		$(obj).siblings(".imgUpload").show();
	} else {
		$(obj).siblings(".imgUpload").hide();
		$(obj).siblings(".imgForbid").show();
	}
	showModelDetail();
}

function showModelDetail() {
	var items = $(".colorList");
	var titles = new Array();
	var detail1 = new Array();
	var detail2 = new Array();
	var detail3 = new Array();
	var detail4 = new Array();
	items.each(function(index) {
		var boxs = $(this).find(".colorItem input:checked");
		if (boxs != null && boxs.length > 0) {
			var titleStr = $(this).children("dt").html();
			titles[titles.length] = titleStr;
			boxs.each(function(a) {
				if (index == 0) {
					detail1[a] = $(this).val();
				} else if (index == 1) {
					detail2[a] = $(this).val();
				} else if (index == 2) {
					detail3[a] = $(this).val();
				} else {
					detail4[a] = $(this).val();
				}
			});
		}
	});
	if (titles.length <= 0) {
		$("#modelDetailLine").hide();
		$(".colorSizeTable tbody").html("");
		return;
	}

	var templateFields = document.getElementsByName("tempFieldsValue");

	var htmlStr = "<tr><th><span class='checkAll'><label><input type=\"checkbox\">启用</label></span></th>";
	for (i = 0; i < titles.length; i++) {
		htmlStr = htmlStr + "<th>" + titles[i] + "</th>";
	}
	htmlStr += "<th>价格</th><th>库存</th><th>预警库存</th></tr>";

	if (titles.length == 1) {
		var array = new Array();
		if (detail1.length > 0) {
			array = detail1;
		} else if (detail2.length > 0) {
			array = detail2;
		} else if (detail3.length > 0) {
			array = detail3;
		} else if (detail4.length > 0) {
			array = detail4;
		}

		for (i = 0; i < array.length; i++) {
			var proId = "";
			for (a = 0; a < templateFields.length; a++) {
				if (templateFields[a].value == $("#tempValue" + array[i]).val()) {
					proId = templateFields[a].title;
				}
			}

			if (proId != null && proId != "") {
				htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\""
						+ array[i] + "||" + proId + "\" checked></td>";
				htmlStr += "<td>" + $("#tempValue" + array[i]).val() + "</td>";
				htmlStr += "<td><input type=\"text\" name=\""
						+ array[i]
						+ "Price\" class=\"i_bg itemBox2\" value=\""
						+ $("#" + proId + "Price").val()
						+ "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr += "<td><input type=\"text\" name=\""
						+ array[i]
						+ "Stock\" class=\"i_bg itemBox2\" value=\""
						+ $("#" + proId + "Quantity").val()
						+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr += "<td><input type=\"text\" name=\""
						+ array[i]
						+ "Warn\" class=\"i_bg itemBox2\" value=\""
						+ $("#" + proId + "QuantityWarning").val()
						+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
				htmlStr += "<td><input type=\"hidden\" name=\"" + array[i]
						+ "Title\" value=\"" + titles[0] + "\" />";
			} else {
				htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\""
						+ array[i] + "\" ></td>";
				htmlStr += "<td>" + $("#tempValue" + array[i]).val() + "</td>";
				htmlStr += "<td><input type=\"text\" name=\""
						+ array[i]
						+ "Price\" class=\"i_bg itemBox2\" value=\""
						+ $("#unitPrice").val()
						+ "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr += "<td><input type=\"text\" name=\""
						+ array[i]
						+ "Stock\" class=\"i_bg itemBox2\" value=\""
						+ $("#quantity").val()
						+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
				htmlStr += "<td><input type=\"text\" name=\""
						+ array[i]
						+ "Warn\" class=\"i_bg itemBox2\" value=\""
						+ 0
						+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
				htmlStr += "<td><input type=\"hidden\" name=\"" + array[i]
						+ "Title\" value=\"" + titles[0] + "\" />";
			}
		}

	} else if (titles.length == 2) {
		var array0 = new Array();
		var array1 = new Array();
		if (detail1.length > 0) {
			array0 = detail1;
		}
		if (detail2.length > 0) {
			if (array0.length <= 0) {
				array0 = detail2;
			} else {
				array1 = detail2;
			}
		}
		if (detail3.length > 0) {
			if (array0.length <= 0) {
				array0 = detail3;
			} else {
				array1 = detail3;
			}
		}
		if (detail4.length > 0) {
			if (array0.length <= 0) {
				array0 = detail4;
			} else {
				array1 = detail4;
			}
		}

		for (i = 0; i < array0.length; i++) {
			for (j = 0; j < array1.length; j++) {
				var proId = "";
				for (a = 0; a < templateFields.length; a++) {
					if (templateFields[a].value == ($("#tempValue" + array0[i])
							.val() + $("#tempValue" + array1[j]).val())) {
						proId = templateFields[a].title;
					}
				}

				if (proId != null && proId != "") {
					htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\""
							+ array0[i]
							+ "--"
							+ array1[j]
							+ "||"
							+ proId
							+ "\" checked></td>";
					htmlStr += "<td>" + $("#tempValue" + array0[i]).val()
							+ "</td>";
					htmlStr += "<td>" + $("#tempValue" + array1[j]).val()
							+ "</td>";
					htmlStr += "<td><input type=\"text\" name=\""
							+ array0[i]
							+ "--"
							+ array1[j]
							+ "Price\" class=\"i_bg itemBox2\" value=\""
							+ $("#" + proId + "Price").val()
							+ "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr += "<td><input type=\"text\" name=\""
							+ array0[i]
							+ "--"
							+ array1[j]
							+ "Stock\" class=\"i_bg itemBox2\" value=\""
							+ $("#" + proId + "Quantity").val()
							+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr += "<td><input type=\"text\" name=\""
							+ array0[i]
							+ "--"
							+ array1[j]
							+ "Warn\" class=\"i_bg itemBox2\" value=\""
							+ $("#" + proId + "QuantityWarning").val()
							+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
					htmlStr += "<td><input type=\"hidden\" name=\"" + array0[i]
							+ "Title\" value=\"" + titles[0] + "\" />";
					htmlStr += "<td><input type=\"hidden\" name=\"" + array1[j]
							+ "Title\" value=\"" + titles[1] + "\" />";
				} else {
					htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" value=\""
							+ array0[i] + "--" + array1[j] + "\" ></td>";
					htmlStr += "<td>" + $("#tempValue" + array0[i]).val()
							+ "</td>";
					htmlStr += "<td>" + $("#tempValue" + array1[j]).val()
							+ "</td>";
					htmlStr += "<td><input type=\"text\" name=\""
							+ array0[i]
							+ "--"
							+ array1[j]
							+ "Price\" class=\"i_bg itemBox2\" value=\""
							+ $("#unitPrice").val()
							+ "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr += "<td><input type=\"text\" name=\""
							+ array0[i]
							+ "--"
							+ array1[j]
							+ "Stock\" class=\"i_bg itemBox2\" value=\""
							+ $("#quantity").val()
							+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
					htmlStr += "<td><input type=\"text\" name=\""
							+ array0[i]
							+ "--"
							+ array1[j]
							+ "Warn\" class=\"i_bg itemBox2\" value=\""
							+ 0
							+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
					htmlStr += "<td><input type=\"hidden\" name=\"" + array0[i]
							+ "Title\" value=\"" + titles[0] + "\" />";
					htmlStr += "<td><input type=\"hidden\" name=\"" + array1[j]
							+ "Title\" value=\"" + titles[1] + "\" />";
				}
			}
		}
	} else if (titles.length == 3) {
		var array0 = new Array();
		var array1 = new Array();
		var array2 = new Array();
		if (detail1.length > 0) {
			array0 = detail1;
		}
		if (detail2.length > 0) {
			if (array0.length <= 0) {
				array0 = detail2;
			} else {
				array1 = detail2;
			}
		}
		if (detail3.length > 0) {
			if (array0.length <= 0) {
				array0 = detail3;
			} else if (array1.length <= 0) {
				array1 = detail3;
			} else {
				array2 = detail3;
			}
		}
		if (detail4.length > 0) {
			if (array0.length <= 0) {
				array0 = detail4;
			} else if (array1.length <= 0) {
				array1 = detail4;
			} else {
				array2 = detail4;
			}
		}

		for (i = 0; i < array0.length; i++) {
			for (j = 0; j < array1.length; j++) {
				for (k = 0; k < array2.length; k++) {
					var proId = "";
					for (a = 0; a < templateFields.length; a++) {
						if (templateFields[a].value == ($(
								"#tempValue" + array0[i]).val()
								+ $("#tempValue" + array1[j]).val() + $(
								"#tempValue" + array2[k]).val())) {
							proId = templateFields[a].title;
						}
					}

					if (proId != null && proId != "") {
						htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" checked  value=\""
								+ array0[i]
								+ "--"
								+ array1[j]
								+ "--"
								+ array2[k] + "||" + proId + "\"></td>";
						htmlStr += "<td>" + $("#tempValue" + array0[i]).val()
								+ "</td>";
						htmlStr += "<td>" + $("#tempValue" + array1[j]).val()
								+ "</td>";
						htmlStr += "<td>" + $("#tempValue" + array2[k]).val()
								+ "</td>";
						htmlStr += "<td><input type=\"text\" name=\""
								+ array0[i]
								+ "--"
								+ array1[j]
								+ "--"
								+ array2[k]
								+ "Price\" class=\"i_bg itemBox2\" value=\""
								+ $("#" + proId + "Price").val()
								+ "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
						htmlStr += "<td><input type=\"text\" name=\""
								+ array0[i]
								+ "--"
								+ array1[j]
								+ "--"
								+ array2[k]
								+ "Stock\" class=\"i_bg itemBox2\" value=\""
								+ $("#" + proId + "Quantity").val()
								+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
						htmlStr += "<td><input type=\"text\" name=\""
								+ array0[i]
								+ "--"
								+ array1[j]
								+ "--"
								+ array2[k]
								+ "Warn\" class=\"i_bg itemBox2\" value=\""
								+ $("#" + proId + "QuantityWarning").val()
								+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
						htmlStr += "<td><input type=\"hidden\" name=\""
								+ array0[i] + "Title\" value=\"" + titles[0]
								+ "\" />";
						htmlStr += "<td><input type=\"hidden\" name=\""
								+ array1[j] + "Title\" value=\"" + titles[1]
								+ "\" />";
						htmlStr += "<td><input type=\"hidden\" name=\""
								+ array2[k] + "Title\" value=\"" + titles[2]
								+ "\" />";
					} else {
						var price = $("#unitPrice").val();
						var stock = $("#quantity").val();
						var warn = $("#quantityWarning").val();
						htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" checked  value=\""
								+ array0[i]
								+ "--"
								+ array1[j]
								+ "--"
								+ array2[k] + "\"></td>";
						htmlStr += "<td>" + $("#tempValue" + array0[i]).val()
								+ "</td>";
						htmlStr += "<td>" + $("#tempValue" + array1[j]).val()
								+ "</td>";
						htmlStr += "<td>" + $("#tempValue" + array2[k]).val()
								+ "</td>";
						htmlStr += "<td><input type=\"text\" name=\""
								+ array0[i]
								+ "--"
								+ array1[j]
								+ "--"
								+ array2[k]
								+ "Price\" class=\"i_bg itemBox2\" value=\""
								+ price
								+ "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
						htmlStr += "<td><input type=\"text\" name=\""
								+ array0[i]
								+ "--"
								+ array1[j]
								+ "--"
								+ array2[k]
								+ "Stock\" class=\"i_bg itemBox2\" value=\""
								+ stock
								+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
						htmlStr += "<td><input type=\"text\" name=\""
								+ array0[i]
								+ "--"
								+ array1[j]
								+ "--"
								+ array2[k]
								+ "Warn\" class=\"i_bg itemBox2\" value=\""
								+ 0
								+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
						htmlStr += "<td><input type=\"hidden\" name=\""
								+ array0[i] + "Title\" value=\"" + titles[0]
								+ "\" />";
						htmlStr += "<td><input type=\"hidden\" name=\""
								+ array1[j] + "Title\" value=\"" + titles[1]
								+ "\" />";
						htmlStr += "<td><input type=\"hidden\" name=\""
								+ array2[k] + "Title\" value=\"" + titles[2]
								+ "\" />";
					}
				}
			}
		}
	} else if (titles.length == 4) {
		var array0 = new Array();
		var array1 = new Array();
		var array2 = new Array();
		var array3 = new Array();
		if (detail1.length > 0) {
			array0 = detail1;
		}
		if (detail2.length > 0) {
			if (array0.length <= 0) {
				array0 = detail2;
			} else {
				array1 = detail2;
			}
		}
		if (detail3.length > 0) {
			if (array0.length <= 0) {
				array0 = detail3;
			} else if (array1.length <= 0) {
				array1 = detail3;
			} else {
				array2 = detail3;
			}
		}
		if (detail4.length > 0) {
			if (array0.length <= 0) {
				array0 = detail4;
			} else if (array1.length <= 0) {
				array1 = detail4;
			} else if (array2.length <= 0) {
				array2 = detail4;
			} else {
				array3 = detail4;
			}
		}

		for (i = 0; i < array0.length; i++) {
			for (j = 0; j < array1.length; j++) {
				for (k = 0; k < array2.length; k++) {
					for (l = 0; l < array3.length; l++) {
						var proId = "";
						for (a = 0; a < templateFields.length; a++) {
							if (templateFields[a].value == ($(
									"#tempValue" + array0[i]).val()
									+ $("#tempValue" + array1[j]).val()
									+ $("#tempValue" + array2[k]).val() + $(
									"#tempValue" + array3[l]).val())) {
								proId = templateFields[a].title;
							}
						}

						if (proId != null && proId != "") {
							htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" checked value=\""
									+ array0[i]
									+ "--"
									+ array1[j]
									+ "--"
									+ array2[k]
									+ "--"
									+ array3[l]
									+ "||"
									+ proId + "\"></td>";
							htmlStr += "<td>"
									+ $("#tempValue" + array0[i]).val()
									+ "</td>";
							htmlStr += "<td>"
									+ $("#tempValue" + array1[j]).val()
									+ "</td>";
							htmlStr += "<td>"
									+ $("#tempValue" + array2[k]).val()
									+ "</td>";
							htmlStr += "<td>"
									+ $("#tempValue" + array3[l]).val()
									+ "</td>";
							htmlStr += "<td><input type=\"text\" name=\""
									+ array0[i]
									+ "--"
									+ array1[j]
									+ "--"
									+ array2[k]
									+ "--"
									+ array3[l]
									+ "Price\" class=\"i_bg itemBox2\" value=\""
									+ $("#" + proId + "Price").val()
									+ "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
							htmlStr += "<td><input type=\"text\" name=\""
									+ array0[i]
									+ "--"
									+ array1[j]
									+ "--"
									+ array2[k]
									+ "--"
									+ array3[l]
									+ "Stock\" class=\"i_bg itemBox2\" value=\""
									+ $("#" + proId + "Quantity").val()
									+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
							htmlStr += "<td><input type=\"text\" name=\""
									+ array0[i]
									+ "--"
									+ array1[j]
									+ "--"
									+ array2[k]
									+ "--"
									+ array3[l]
									+ "Warn\" class=\"i_bg itemBox2\" value=\""
									+ $("#" + proId + "QuantityWarning").val()
									+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
							htmlStr += "<td><input type=\"hidden\" name=\""
									+ array0[i] + "Title\" value=\""
									+ titles[0] + "\" />";
							htmlStr += "<td><input type=\"hidden\" name=\""
									+ array1[j] + "Title\" value=\""
									+ titles[1] + "\" />";
							htmlStr += "<td><input type=\"hidden\" name=\""
									+ array2[k] + "Title\" value=\""
									+ titles[2] + "\" />";
							htmlStr += "<td><input type=\"hidden\" name=\""
									+ array3[l] + "Title\" value=\""
									+ titles[3] + "\" />";
						} else {
							var detailId = "";
							var price = $("#unitPrice").val();
							var stock = $("#quantity").val();
							var warn = $("#quantityWarning").val();

							htmlStr += "<tr><td><input type=\"checkbox\" name=\"modelDetailCheck\" checked value=\""
									+ array0[i]
									+ "--"
									+ array1[j]
									+ "--"
									+ array2[k] + "--" + array3[l] + "\"></td>";
							htmlStr += "<td>"
									+ $("#tempValue" + array0[i]).val()
									+ "</td>";
							htmlStr += "<td>"
									+ $("#tempValue" + array1[j]).val()
									+ "</td>";
							htmlStr += "<td>"
									+ $("#tempValue" + array2[k]).val()
									+ "</td>";
							htmlStr += "<td>"
									+ $("#tempValue" + array3[l]).val()
									+ "</td>";
							htmlStr += "<td><input type=\"text\" name=\""
									+ array0[i]
									+ "--"
									+ array1[j]
									+ "--"
									+ array2[k]
									+ "--"
									+ array3[l]
									+ "Price\" class=\"i_bg itemBox2\" value=\""
									+ price
									+ "\"  onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
							htmlStr += "<td><input type=\"text\" name=\""
									+ array0[i]
									+ "--"
									+ array1[j]
									+ "--"
									+ array2[k]
									+ "--"
									+ array3[l]
									+ "Stock\" class=\"i_bg itemBox2\" value=\""
									+ stock
									+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td>";
							htmlStr += "<td><input type=\"text\" name=\""
									+ array0[i]
									+ "--"
									+ array1[j]
									+ "--"
									+ array2[k]
									+ "--"
									+ array3[l]
									+ "Warn\" class=\"i_bg itemBox2\" value=\""
									+ 0
									+ "\" onpaste=\"return false\" onblur=\"javascript:if(this.value==''){this.value=0;}\" onkeyup=\"javascript:clearNoNum(this)\"></td></tr>";
							htmlStr += "<td><input type=\"hidden\" name=\""
									+ array0[i] + "Title\" value=\""
									+ titles[0] + "\" />";
							htmlStr += "<td><input type=\"hidden\" name=\""
									+ array1[j] + "Title\" value=\""
									+ titles[1] + "\" />";
							htmlStr += "<td><input type=\"hidden\" name=\""
									+ array2[k] + "Title\" value=\""
									+ titles[2] + "\" />";
							htmlStr += "<td><input type=\"hidden\" name=\""
									+ array3[l] + "Title\" value=\""
									+ titles[3] + "\" />";
						}
					}
				}
			}
		}
	}

	$("#modelDetailLine").show();
	$(".colorSizeTable tbody").html(htmlStr);

}

function changeFields(tempId, tempFieldName, category, position, valueStr) {
	$("#" + tempId).val(
			tempId + "**" + tempFieldName + "**" + category + "**" + position
					+ "**" + valueStr);
}

//选择大宗商品时隐藏商城价、市场价 显示批发价和价格区间
function checkRadioD(){
	$("#advanceFont").hide();
}
//选择优选商品时显示商城价、市场价 隐藏批发价和价格区间
function checkRadioY(){
	$("#advanceFont").show();



}