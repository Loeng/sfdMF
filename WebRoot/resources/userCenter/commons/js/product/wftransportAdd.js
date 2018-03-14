$(document).ready(function() {
	var ctx = $("#ctxpath").val();
	/*初始化罐体材质*/
	if($(".carName").find("option:selected").attr("id")!=null&&$("#tankMaterialName").val()!=""&&$("#tankMaterialName").val()!=null){
		$.ajax({
			type: "POST",
			url: ctx + "/store/auth/accredit/getTankMaterial/" + $(".carName").find("option:selected").attr("id"),
			dataType: "json",
			success: function(data){
				var html = "<option  value=''>请选择</option>";
				for(var i=0; i<data.length; i++ ){
					if(data[i].name==$("#tankMaterialName").val()){
						html += "<option value= '"+data[i].name+"' id= '"+data[i].id+"' selected>"+data[i].name+" </option>";
					}else{
						html += "<option value= '"+data[i].name+"' id= '"+data[i].id+"'>"+data[i].name+"</option>";
					}
				}
				 $(".tankMaterialName").html(html);
			}
		});
	}
	
	/*选择弹窗*/
	$(".btnXuanze").live("click",function(){
		var ids = "";
		$("input[name='intelId']").each(function(){
			ids += "," + $(this).val();
		});	
		$(".alterBox").load(ctx + "/web/supplyBuy/jumpIntelPage/load",function(){
			$(".alterBox input[type='checkbox']").each(function(){
				if(ids.indexOf($(this).val()) >= 0){
					$(this).attr("checked", true);
					$(this).parent().parent().addClass("checked");
				}
			});
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
	$(".checkItem").click(function(){
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
	
	var wfpyHtml = "";
	$(".wfpysParentId").each(function(){
		if($(this).find("option:selected").text() == "六类：毒害品和感染性物品"){
			$(".wfpysId").hide();
			$(".code").show();
		} else{
			$(".code").hide();
		}
		// 根据第一级菜单取出第二级数据
		if($(this).find("option:selected").text() != "请选择"){
			$.ajax({
				type: "POST",
				url: $("#ctxpath").val() + "/store/auth/accredit/getWfpys/" + $(this).find("option:selected").val(),
				dataType: "json",
				success: function(data){
					for(var i=0; i<data.length; i++ ){
						wfpyHtml += "<option value= '"+data[i].id+"'>"+data[i].name+"</option>";
					}
					 $("#wfpysId").html(wfpyHtml);
					 $("#wfpysId").show();
				}
			});
		}else{
			$(".wfpysId").hide();
			$(".code").hide();
		}
	});
	
	var childIds = $("#wfDetails").val();
	if(trim(childIds) != null && trim(childIds) != "" && trim(childIds) != "null"){
		var url = $("#ctxpath").val() + "/wfpscrap/wfpml/show";
		$("#mlDiv").load(url,{"mlIds":childIds});
		$("#mlDiv").show();
	}
	
	/*$(".zizhiList:eq(1) li").each(function(){
		var shengluehao=$(this).html();
		var word22=$(this).text();
		var changdu=shengluehao.length;
		console.log(changdu);
		if (changdu<7) {
			$(this).html(shengluehao);
			}
		else{
			var newtext=word22.substr(0, 7);
			$(this).html(newtext+"...认证"+'<a href="javascript:void(0);" class="closeLi"></a>');
		}
	});*/
});

function reset(){
	document.getElementById("addWftransportForm").reset();
}

//返回列表
function goBack(contextPath){
	var type = $("#type").val();
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/store/wftransport/getList/" + type;
	}
	window.location.href=contextPath + "/store/wftransport/getList/" + type;
}


function selectwfpysName(){
	if($(".wfpysParentId").val() == ""){
		$("#wfpysSpan").show();
		$("#wfpysSpan").text("请选择危废品标准");
		return false;
	}else{
		$("#wfpysSpan").hide();
		return true;
	}
}

function checkCarLength(){
	if($(".carLength").val() == ""){
		$("#carLengthSpan").show();
		$("#carLengthSpan").text("请输入车身长度");
		return false;
	}else{
		var regexp = /^[0-9]+(.[0-9]{1,9})?$/;
		if(!regexp.test($(".carLength").val())){
		     $('#carLengthSpan').show();
		     $('#carLengthSpan').text("请输入正确的车身长度");
		     return false;
		}
		$("#carLengthSpan").hide();
		return true;
	}
}

function checkCarWidth(){
	if($(".carWidth").val() == ""){
		$("#carWidthSpan").show();
		$("#carWidthSpan").text("请输入车身宽度");
		return false;
	}else{
		var regexp = /^[0-9]+(.[0-9]{1,9})?$/;
		if(!regexp.test($(".carWidth").val())){
		            $('#carWidthSpan').show();
		            $('#carWidthSpan').text("请输入正确的车身宽度");
		            return false;
		}
		$("#carWidthSpan").hide();
		return true;
	}
}

function checkCarHeight(){
	if($(".carHeight").val() == ""){
		$("#carHeightSpan").show();
		$("#carHeightSpan").text("请输入车身高度");
		return false;
	}else{
		var regexp = /^[0-9]+(.[0-9]{1,9})?$/;
		if(!regexp.test($(".carHeight").val())){
		     $('#carHeightSpan').show();
		     $('#carHeightSpan').text("请输入正确的车身高度");
		     return false;
		}
		$("#carHeightSpan").hide();
		return true;
	}
}

function checkCarNumber(){
	if($(".carNumber").val() == ""){
		$("#carNumberSpan").show();
		$("#carNumberSpan").text("请输入车牌号");
		return false;
	}else{
		$("#carNumberSpan").hide();
		return true;
	}
}

function checkCargoVolume(){
	if($(".cargoVolume").val() == ""){
		$("#cargoVolumeSpan").show();
		$("#cargoVolumeSpan").text("请输入货源体积");
		return false;
	}else{
		var regexp = /^[0-9]+(.[0-9]{1,9})?$/;
		if(!regexp.test($(".cargoVolume").val())){
		     $('#cargoVolumeSpan').show();
		     $('#cargoVolumeSpan').text("请输入正确的货源体积");
		     return false;
		}
		$("#cargoVolumeSpan").hide();
		return true;
	}
}

function checkCarLength1(){
	if($(".carLength1").val() == ""){
		$("#carLengthSpan1").show();
		$("#carLengthSpan1").text("请输入期望车辆长度");
		return false;
	}else{
		var regexp = /^[0-9]+(.[0-9]{1,9})?$/;
		if(!regexp.test($(".carLength1").val())){
		     $('#carLengthSpan1').show();
		     $('#carLengthSpan1').text("请输入正确的车辆长度");
		     return false;
		}
		$("#carLengthSpan1").hide();
		return true;
	}
}

function checkwfpTotalNameBlur(){
	if($(".wfpTotal").val() == ""){
		$("#wfpTotalSpan").show();
		$("#wfpTotalSpan").text("请输入货源重量");
		return false;
	}else{
		var regexp = /^[0-9]+(.[0-9]{1,9})?$/;
		if(!regexp.test($(".wfpTotal").val())){
		     $('#wfpTotalSpan').show();
		     $('#wfpTotalSpan').text("请输入正确的货源重量");
		     return false;
		}
		$("#wfpTotalSpan").hide();
		return true;
	}
}

function checkprice(){
	var regexp = /^[0-9]+(.[0-9]{1,9})?$/;
	if($(".price").val()==""||$(".price").val()==null){
		$("#priceSpan").hide();
		 return true;
	}
	if(!regexp.test($(".price").val())){
	   $('#priceSpan').show();
	   $('#priceSpan').text("请输入正确的价格");
	   return false;
	}
	   $("#priceSpan").hide();
	   return true;
	
}

function checkValidTime(){
	if($(".validTime").val() == ""){
		$("#validTimeSpan").show();
		$("#validTimeSpan").text("请输入有效时间");
		return false;
	}else{
		$("#validTimeSpan").hide();
		return true;
	}
}

function selectcategoryName(){
	if($(".categoryName").val() == ""){
		$("#categoryNameSpan").show();
		$("#categoryNameSpan").text("请选择货物类别");
		return false;
	}else{
		$("#categoryNameSpan").hide();
		return true;
	}
}

function selectcarName(){
	var carName= $(".carName").val();
	if(carName == ""){
		$("#carNameSpan").show();
		$("#carNameSpan").text("请选择车辆类型");
		return false;
	}
	var webroot = $("#ctxpath").val();
	var html = "<option  value=''>请选择</option>";
	$.ajax({
		type: "POST",
		url: webroot + "/store/auth/accredit/getTankMaterial/" + $(".carName").find("option:selected").attr("id"),
		dataType: "json",
		success: function(data){
			for(var i=0; i<data.length; i++ ){
			   html += "<option value= '"+data[i].name+"' id= '"+data[i].id+"'>"+data[i].name+"</option>";
			}
			 $(".tankMaterialName").html(html);
		}
	});
	$("#carNameSpan").hide();
    return true;	
}

//罐体材质
function selecttankMaterialName(){
	var carName= $(".carName").val();
	if(carName==""||carName==null){
		$("#tankMaterialNameSpan").show();
		$("#tankMaterialNameSpan").text("请先选择车辆类型");
	    return false;
	}
	 $("#tankMaterialNameSpan").hide();
	return true;
	
}



function formSubmit(){
	var type = $("#type").val();
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
	$("#wfpysName").val(wfpysParentName + "," + wfpysName);
	// 获取类型
	var type = $("#type").val();
	// 验证表单 车源
	if(type=="0"){
		if(checknameBlur()&
		   checkAreaBlur1()&
		   checkAreaBlur()&
		   checkCarLength()&
		   checkCarWidth()&
		   checkCarHeight()&
		   selectwfpysName()&
		   selectcarName()&
		   checkwfpTotalNameBlur()&
		   checkCarNumber()&
		   checkContentNameBlur()&
		   checkContentPhoneBlur()&
		   checkprice()&
		   checkValidTime()){
			$("#addWftransportForm").ajaxSubmit(
					function(data){
					if(data==true || data=="true"){
						// 页面为添加的时候保存成功清空页面元素
						if($("#method").val() == "add"){
							$(':input','#addWftransportForm')  
							.not(':button, :submit, :reset, :hidden')  
							.val('')  
							.removeAttr('checked')  
							.removeAttr('selected');
							$("#carNameSpan").hide();
							econfirm('操作成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
						}else{
							econfirm('操作成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
						}
					}else{
						falert("操作失败");
					}
				});
		}
	}
	// 验证表单 货源
	if(type=="1"){
		if(checknameBlur()&
		   checkAreaBlur1()&
		   checkAreaBlur()&
		   selectwfpysName()&
		   selectcategoryName()&
		   selectcarName()&
		   checkcarNameBlur(type)&
		   checkwfpTotalNameBlur()&
		   checkCargoVolume()&
		   selectcarName()&
		   checkCarLength1()&
		   checkContentNameBlur()&
		   checkContentPhoneBlur()&
		   checkEndTimeBlur()&
		   checkprice()&
		   checkValidTime()){
			$("#addWftransportForm").ajaxSubmit(
					function(data){
					if(data==true || data=="true"){
						// 页面为添加的时候保存成功清空页面元素
						if($("#method").val() == "add"){
							$(':input','#addWftransportForm')  
							.not(':button, :submit, :reset, :hidden')  
							.val('')  
							.removeAttr('checked')  
							.removeAttr('selected');
							$("#carNameSpan").hide();
							econfirm('操作成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
						}else{
							econfirm('操作成功，是否继续修改？',null,null,goBack,[$("#ctxpath").val()]);
						}
					}else{
						falert("操作失败");
					}
				});
         }
	}	

}
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
function checkcarNameBlur(types){
	if(types == "0"){
		var carName = $.trim($("input[name='carName']").val());
		if(carName == ""){
			$('#carNameSpan').show();
			$('#carNameSpan').text("请输入车辆类型");
			return false;
		}
		$('#carNameSpan').hide();
		return true;
		
	}else{
		var supplyName = $.trim($("input[name='supplyName']").val());
		if(supplyName == ""){
			$('#carNameSpan1').show();
			$('#carNameSpan1').text("请输入货源名称");
			return false;
		}
		$('#carNameSpan1').hide();
		return true;
	}
	
	
}

//标题车载总量
function checkwfpTotalBlur(){
	var wfpTotal = $.trim($("input[name='wfpTotal']").val());
	
	if(wfpTotal == ""){
		$('#wfpTotalSpan').show();
		$('#wfpTotalSpan').text("请输入车载总量");
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
	var regexp2=/^(0\d{2,3})(\d{7,8})(\d{3,})?$/;
	if(contactPhone == ""){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入联系电话");
		return false;
	}
	if(!regexp2.test(contactPhone)&&!regexp.test(contactPhone)){
		$('#contactPhoneSpan').show();
		$('#contactPhoneSpan').text("请输入正确的联系电话");
		return false;
	}
	$('#contactPhoneSpan').hide();
	return true;
}


// 单位
function checkUnit(){
	var unit = $('.wfysdw option:selected') .val();
	if(unit  == ""||unit == "null"){
		$(".unit").html("吨");
		$(".unitPrice").html("元/吨");
	}else{
		$(".unit").html(unit);
		$(".unitPrice").html("元/"+unit);
	}
	
}

//验证是否选中
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
				url: webroot + "/store/auth/accredit/getWfpys/" + $(this).find("option:selected").val(),
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

//搜索表单提交
function searchSubmit(){
	$("#searchProductForm").submit();
}