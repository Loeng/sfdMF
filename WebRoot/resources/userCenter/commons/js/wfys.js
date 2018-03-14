function checkFocus(obj) {
	if($(obj).val()!=""){
		$(obj).siblings(".errorText").html("");
	}
}

function checkQuantity(){
	var flag=true;
	var quantity = $("input[name='quantity']").val();
	if(quantity == "" || quantity == null || quantity == " "){
		$('#quantitySpan').show();
		$('#quantitySpan').text("请输入数量");
		flag = false;
	}
	return flag;
}
function checkPrice(){
	var flag=true;
	var price = $("input[name='price']").val();
	if(price == "" || price == null || price == " "){
		$('#priceSpan').show();
		$('#priceSpan').text("请输入价格");
		flag = false;
	}
	return flag;
}

function validInfo(obj,tit){
	var flag=true;
	if($(obj).val()!=null&&$(obj).val()!=" "&&$(obj).val()!=""){
		$(obj).next().html("");
		$(obj).next().hide();
	}else{
		$(obj).next().html(tit);
		$(obj).next().show();
		flag = false;
	}
	return flag;
}

function checkContactPhone(){
	var flag=true;
	var contactPhone = $("input[name='linkTel']").val();
	if(contactPhone == "" || contactPhone == null || contactPhone == " "){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入联系电话");
		flag = false;
	}
	return flag;
}


// ---------------- 临时发布验证 ----------------------
// 标题验证
function checknameBlur(){
	var name = $.trim($("input[name='name']").val());
	if(name == ""){
		$('#nameSpan').show();
		$('#nameSpan').text("请输入标题");
		return false;
	}
	$('#nameSpan').hide();
	return true;
}

//标题车类型
//function checkcarNameBlur(){
//	var supplyName = $.trim($("#supplyName").val());
//	if(supplyName == ""){
//		$('#supplyNameSpan').show();
//		$('#supplyNameSpan').text("请输入货源名称");
//		$('#carNameSpan').hide();
//		$('#carNameSpan').text("");
//		return false;
//	}
//	$('#carNameSpan').hide();
//	return true;
//}

//标题车载总量
function checkwfpTotalBlur(){
	var wfpTotal = $.trim($("input[name='wfpTotal']").val());
	
	if(wfpTotal == ""){
		$('.wfpTotalSpan').show();
		$('.wfpTotalSpan').text("请输入总量");
		return false;
	}
	$('#wfpTotalSpan').hide();
	return true;
}

function checkContentNameBlur(){
	var linkMan = $.trim($("input[name='linkMan']").val());
	
	if(linkMan == ""){
		$('#contactNameSpan').show();
		$('#contactNameSpan').text("请输入联系人");
		return false;
	}
	$('#contactNameSpan').hide();
	return true;
}
function checkContentPhoneBlur(){
	var contactPhone = $.trim($("input[name='phone']").val());
	var regexp = /^(1[0-9][0-9])\d{8}$/;
	
	if(contactPhone == ""){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入联系电话");
		return false;
	}
	if(!regexp.test(contactPhone)){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入正确的联系电话");
		return false;
	}
	$('#contactPhoneSpan').hide();
	return true;
}

// 验证是否选中
function checkAreaBlur(){
	var areaInfo = $("#startPlace").val();
	if(areaInfo == ""||areaInfo == "null"){
		$('#startPlaceSpan').show();
		$('#startPlaceSpan').text("请选择起始地");
		return false;
	}
	$('#startPlaceSpan').hide();
	return true;
}
// 获取选中地区
$(".startPlaceArea").live("click",function(){
	checkAreaBlur();
});
$(".endPlaceArea").live("click",function(){
	checkAreaBlur1();
});

function checkAreaBlur1(){
	var areaInfo = $("#endPlace").val();
	if(areaInfo == ""||areaInfo == "null"){
		$('#endPlaceSpan').show();
		$('#endPlaceSpan').text("请选择目的地");
		return false;
	}
	$('#endPlaceSpan').hide();
	return true;
}

function checkUnit(){
	var unit = $.trim($("input[name='unit']").val());
	if(unit  == ""||unit == "null"){
		$(".unit").html("吨");
	}else{
		$(".unit").html(unit);
	}
	
}

function selectwfpysName(){
	if($("#wfpysParentId").val() == ""){
		//$("#wfpysSpan").show();
		//$("#wfpysSpan").text("请选择危废品标准");
		//return false;
	}else{
		$("#wfpysSpan").hide();
		return true;
	}
}

function selectcategoryName(){
	if($("#categoryName").val() == ""){
		$("#categoryNameSpan").show();
		$("#categoryNameSpan").text("请选择货物类别");
		return false;
	}else{
		$("#categoryNameSpan").hide();
		return true;
	}
}

function selectcarName(){
	if($("#carName").val() == ""){
		$("#carNameSpan").show();
		$("#carNameSpan").text("请选择车辆类型");
		return false;
	}else{
		$("#carNameSpan").hide();
		return true;
	}
}

function selecttankMaterialName(){
	var types =  $('input:radio:checked').val();
	if(types == "0"){
		if($("#tankMaterialName").val() == ""){
			$("#tankMaterialNameSpan").show();
			$("#tankMaterialNameSpan").text("请选择罐体材质");
			return false;
		}else{
			$("#tankMaterialNameSpan").hide();
			return true;
		}
	}else{
		return true;
	}
}


//验证是否选择截止时间
function checkEndTimeBlur(){
	var endTime = $.trim($("input[name='endTime']").val());
	if(endTime == ""){
		$('#endTimeSpan').show();
		$('#endTimeSpan').text("请选择截止时间");
		return false;
	}
	$('#endTimeSpan').hide();
	return true;
}

// ---------------- 临时发布验证 end ------------------

// 发布弹出层
	function release(){
		$("#fbHtml").load($("#ctxpath").val() + "/web/wftransport/release",function(){
			// 发布弹出层
			layer.open({
				type: 1,
				title: '信息发布',
				skin: 'layer-class',
				area: ['1200px'], //宽高
				content: $("#fbHtml"),
				btn:['确认','取消'],
				yes:function(index){
					// 获取起始地选中的城市全路径
					var startFullPath="";
					$(".startPlaceArea").each(function(){
						if($(this).find("option:selected").text() != "请选择:"){
							startFullPath+=$(this).find("option:selected").text()+",";
						}
					});
					$("#startFullPath").val(startFullPath)
					// 获取目的地选中的城市全路径
					var endFullPath="";
					$(".endPlaceArea").each(function(){
						if($(this).find("option:selected").text() != "请选择:"){
							endFullPath+=$(this).find("option:selected").text()+",";
						}else{
							$(this).find("option:selected").text();
						}
					});
					$("#endFullPath").val(endFullPath)
					
					
					// 获取选中分类名称
					var wfpysParentName = "";
					var wfpysName = "";
					// 获取一级菜单选中名称
					$(".wfpysParentId").each(function(){
						if($(this).find("option:selected").text() != "请选择:"){
							wfpysParentName = $(this).find("option:selected").text();
						}
					});
					// 获取二级菜单选中名称
					$(".wfpysId").each(function(){
						if($(this).find("option:selected").text() != "请选择:"){
							wfpysName = $(this).find("option:selected").text();
						}
					});
					$("#wfpysName").val(wfpysParentName + wfpysName);
					
					//点击确定执行的语句
					if(selectwfpysName()&&selectcategoryName()&&checknameBlur()
							&&checkwfpTotalBlur()&&checkContentNameBlur()&&checkContentPhoneBlur()
							&&checkAreaBlur()&&checkAreaBlur1()&&selectcarName()&&selecttankMaterialName()&&checkEndTimeBlur()){
						$("#addWftransportForm").ajaxSubmit(function(data){
				    		if(data == true || data=="true"){
				    			layer.close(index);
				    			$("#updateHtml").hide();
								$("#layui-layer1,#layui-layer-shade1").hide();
								layer.alert("发布成功",{time: 4000 },function(){window.location.reload()});
				    		}else{
				    			layer.alert("发布失败!");
				    			$("#updateHtml").hide();
								$("#layui-layer1,#layui-layer-shade1").hide();
								layer.alert("发布失败",{time: 4000 },function(){window.location.reload()});
				    		}
				    	});
					}else{
						checknameBlur();
						checkwfpTotalBlur();
						checkContentNameBlur();
						checkContentPhoneBlur();
						checkAreaBlur();
						checkAreaBlur1();
						selectwfpysName();
						selectcategoryName();
						selectcarName();
						selecttankMaterialName();
						checkEndTimeBlur();
					}
				},
				cancel:function(){
					//点击取消执行的语句
					$("#updateHtml").hide();
					$("#layui-layer1,#layui-layer-shade1").hide();
					window.location.reload();
				}
			});
		});
	}
	
	
	function showCode(){
		var webroot = $("#ctxpath").val();
		var html = "";
		$(".wfpysParentId").each(function(){
			// 选择第六项 出现危废代码选项
			if($(this).find("option:selected").text() == "六类：毒害品和感染性物品"){
				$(".wfpysId").hide();
				$(".code").show();
				$("#mlDiv").show();
			} else{
				$(".code").hide();
				$("#mlDiv").hide();
			}
			// 根据第一级菜单取出第二级数据
			if($(this).find("option:selected").text() != "请选择"){
				$.ajax({
					type: "POST",
					url: webroot + "/web/auth/accredit/getWfpysToWeb/" + $(this).find("option:selected").val(),
					dataType: "json",
					success: function(data){
						for(var i=0; i<data.length; i++ ){
						   html += "<option value= '"+data[i].id+"'>"+data[i].name+"</option>";
						}
						 $("#wfpysId").html(html);
						 $("#wfpysId").show();
					}
				});
				$("#mlDiv").hide();
			}else{
				$(".wfpysId").hide();
				$(".code").hide();
			}
		});
	}

	//load出契约列表
	function loadDirectory(){
		 $("#Directory").load($("#ctxpath").val() + "/wfpscrap/wfpml",function(){
		 	$("#wfmlChose").val($("#wfDetails").val())
		 });
		 $(".apLayerBg").show();
		 $(".alertBrand").show();
	}

	function choseWfMls(){
		var choses = $("#wfmlChose").val();
		var url = $("#ctxpath").val() + "/wfpscrap/wfpml/show";
		$("#mlDiv").load(url,{"mlIds":choses});
		$("#mlDiv").show();
		$("#wfDetails").val(choses);
		
		$(".apLayerBg").hide();
		 $(".alertBrand").hide();
	}