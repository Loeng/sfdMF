$(document).ready(function() {
	$(".limitItem").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			var ids = $("#channelId").val();
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
			$main.next(".limitList").find("input").attr("checked",true);
			$main.next(".limitList").find(".check").addClass("checked");
			$main.parents().prev(".limitItem").find("input").attr("checked",true);
			$main.parents().prev(".limitItem").find(".check").addClass("checked");
			
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
			$main.next(".limitList").find(":checkbox").attr("checked",false);
			$main.next(".limitList").find(".check").removeClass("checked");
		}
		
		var obj=$(".limitItem input:checked");
		ids="";
		for(i=0;i<obj.length;i++){
			if(obj.eq(i).parents(".limitItem").next(".limitList").find("input").length<1)
			ids+=obj.eq(i).attr("value") + ",";
		}
		//alert(obj.length);
		$("#channelId").val(ids);
	});
});	

// 添加成功后  询问是否还需要添加
// 使用MODERNIZR之后,jQuery的加载会延迟一小段时间,故会出现'$'未定义的
$(function(){
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',goOnAdd,[$("#ctxpath").val()],goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
	
	$("input.i_bg").focus(function()  //得到焦点时触发的事件
    {
	   $(this).parent().addClass("text_ts");
	});
	
	$("input.i_bg").blur(function () //失去焦点时触发的事件
	{
		$(this).parent().removeClass("text_ts");
	}); 
	
})

//验证资讯名格式
function checkName(){
	//var nameStr = document.getElementsByName('contentName')[0].value;
	//if(nameStr.length < 3 || nameStr.length > 32){
	//	document.getElementById("nameDd").className="text_ts";
	//	$("#nameSpan").html("3-20位字符组成");
	//	return false;
	//}else{
		document.getElementById("nameDd").className="text_d_ts";
		//$("#nameSpan").html("3-20位字符组成");
		return true;
//	}
}

//验证资讯名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('contentName')[0].value;
	//if(nameStr.length < 3 || nameStr.length >32){
		//document.getElementById("nameDd").className="text_c_ts";
		//$("#nameSpan").html("3-20位字符组成");
		//return false;
//	}else{
		document.getElementById("nameDd").className="";
		return true;
	//}
}

//验证资讯ID是否存在
/**
var flag = false;
function checkId(id,contextPath){
	flag = false;
	if(id == ""){
		document.getElementById("idDd").className="text_c_ts";
		$("#idSpan").html("咨讯id不能为空");
	}else{
	$.post(
		contextPath + "/system/content/checkId/"+id,
	 	function(data){
	 		if(!data){
	 			ealert("资讯ID已存在!");
	 			return false;
	 		}else{
	 			document.getElementById("idDd").className=""
	 			flag = true;
	 		}
	 	}
	 );
	}
}
**/
//===========================================图片上传区域JS-START===========================================
//验证上传导航图片的格式
function validateForm(add) {
	//可不上传图片，直接返回true
 	if (add.uploadFile.value == "") {
        return true;
  	}     
 	//截取提交上传文件的扩展名  
  	var ext = add.uploadFile.value.match(/^(.*)(\.)(.{1,8})$/)[3];
  	ext = ext.toLowerCase(); //设置允许上传文件的扩展名           
  	if (ext ==  "jpg" || ext == "gif" || ext=="png") {
        return true;
  	} else {
        alert("只允许上传 .jpg或gif 或png文件，请重新选择需要上传的文件！");
        return false;
 	}
}
//===========================================图片上传区域JS-END=============================================
// 提交
function formSubmit(){
	if(checkCategory() && checkNameBlur()){
		document.getElementById("addContentForm").submit();
	}else{
		var id = document.getElementsByName('id')[0].value;
		checkId(id,'${webroot}');
		checkNameBlur();
	}
}

function checkCategory(){
	var text = $("#categoryId").val();
	if(text!=""){
		return true;
	}else{
		document.getElementById("cDd").className="text_c_ts";
		$("#categorySpan").html("请选择分类");
		return false;
	}
}
// 重置
function reset(){
    document.getElementById("addContentForm").reset();
}

// 返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath == null || contextPath == ""){
		window.location.href="/system/content/list";
	}
	window.location.href=contextPath + "/system/content/list";
}

// 继续添加
function goOnAdd(ctxpath){
	window.location.href = ctxpath+"/system/content/add";
}

//==================================关联店铺START==============================================
// 此功能已失效
// 查询出店铺详细信息
function loadStore(){
	$("#storetc").load($("#ctxpath").val()+"/system/store/plist",function(){
		
	});
	$("#storeBg").show();
	$("#storetc").show();
}
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

// 选择店铺
function sreachStore(name,id){
	$('#storeName').val(name);
	$("#storeId").val(id);
	$("#storeBg").hide();
	$("#storetc").hide();
}
// 重置店铺的搜索条件
function resetStore(){
	document.getElementById("nameStore").value="";
}
//关闭弹出层
function apClose(){
	$(".del_tcBg").hide();
	$(".del_tc").hide();
}

$(document).ready(function() {
	/*单选框*/
    $("input[type='radio']").change(function() {
        $(this).parent().parent().siblings(".zt").removeClass("radioSel");
        $(this).parent().parent().addClass("radioSel");
    });
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
})

//========================================关联店铺END==================================================

//=====================================关联咨询分类START===============================================
$(document).ready(function(){
	// 点击一级
	$("#select1Span select").live("change",function(){
		//清除必填提示样式
		document.getElementById("cDd").className="";
		$("#categorySpan").html("");
		// 事先清除二級三級的內容
		$("#select2Span").html("");
		$("#select3Span").html("");
		var categoryId = $(this).children("option:selected").val();
		$("#categoryId").val(categoryId);
		var level = $(this).parent().find("input").val();
		// 如果该分类下面还有子分类,还需查询出子分类
		var ctxpath = $("#ctxpath").val();
		$.post(
			ctxpath + "/system/content/add/categoryChild",
			{"id":categoryId},
			function(data){
				dataType:"text";
			if(data != null && data != ""){
				var JSONObj = $.parseJSON(data);
				if(JSONObj != null && JSONObj.length > 0){
					// 新建一个select节点,并将该节点添加到原来原来父级select容器内
					var selectObj = "<select><option>==请选择==</option>";
					// 将查询出来的结果放入该select对象内
					for(var i = 0;i<JSONObj.length;i++){
						var nowCategory = JSONObj[i];
						selectObj += "<option value ='"+nowCategory.id+"'>"+nowCategory.name+"</option>";
					}
					selectObj += "<input type='hidden' value='ERJIS'></select>";
					$("#select2Span").html(selectObj);
				}
			}
			});
	});
	
	// 点击二级
	$("#select2Span select").live("change",function(){
		// 事先清除三級內容
		$("#select3Span").html("");
		var categoryId = $(this).children("option:selected").val();
		$("#categoryId").val(categoryId);
		var level = $(this).parent().find("input").val();
		// 如果该分类下面还有子分类,还需查询出子分类
		var ctxpath = $("#ctxpath").val();
		$.post(
			ctxpath + "/system/content/add/categoryChild",
			{"id":categoryId},
			function(data){
			if(data != null && data != ""){
				var JSONObj = $.parseJSON(data);
				if(JSONObj != null && JSONObj.length > 0){
					// 新建一个select节点,并将该节点添加到原来原来父级select容器内
					var selectObj = "<select><option>==请选择==</option>";
					// 将查询出来的结果放入该select对象内
					for(var i = 0;i<JSONObj.length;i++){
						var nowCategory = JSONObj[i];
						selectObj += "<option value ='"+nowCategory.id+"'>"+nowCategory.name+"</option>";
					}
					selectObj += "<input type='hidden' value='SANJIS'></select>";
					$("#select3Span").html(selectObj);
				}
			}
			});
	});	
	
	// 點擊三級
	$("#select3Span select").live("change",function(){
		// 點擊三級進行賦值更新即可
		var categoryId = $(this).children("option:selected").val();
		$("#categoryId").val(categoryId);
	});
	
 })
//======================================关联咨询分类END===============================================
 
 //====================================長文本內容的分頁START==========================================
 $(document).ready(function(){
		/*编辑器分页*/
	    // 新增一页
	    var m = 2; // 用于contentId的生成,此Id是为了方便操作CKEDITOR
		$(".addEditor").click(function(){
			var obj=$(this).parents(".editorPage").prev(".formBoxLine").children("dd").eq(0).clone();
			var contentId = "content" + m;
			m++;
			obj.html("<textarea name='contentDetail' id='"+contentId+"' class='ckeditor'></textarea>");
			var obj2=$(this).parents(".editorPage").prev(".formBoxLine").children(".clear")
			// 把克隆的节点放到obj2之前
			obj2.before(obj);
			// 隐藏所有的children为<dd>
			$(this).parents(".editorPage").prev(".formBoxLine").children("dd").hide();
			obj2.show();
			obj.show();
			CKEDITOR.replace(contentId);
			var index=$(this).parents(".editorPage").prev(".formBoxLine").children("dd").index(obj);
			var size=$(this).next(".pageSize").children("li").eq(0).clone();
			size.children(".btn").text(index+1);
			$(this).next(".pageSize").append(size);
			$(this).next(".pageSize").children("li").removeClass("cur");
			$(this).next(".pageSize").children("li").eq(index).addClass("cur");
		});
		
		// 删除一页
		$(".removePage").live("click",function(){
			var index=$(this).parents(".pageSize").children("li").index($(this).parent());
			if(index == 0){
				ealert("你不能删除正在操作的分页.");
				return;
			}
			var a=$(this).parents(".editorPage").prev(".formBoxLine").children("dd").eq(index);
			var b=$(this).parent().nextAll("li").find(".btn");
			b.each(function() {
	            var eq=parseInt($(this).text());
				eq--;
				$(this).text(eq);
	        });
			if($(this).parent().attr("class")=="cur"){
				a.prev().show();
				$(this).parent().prev().addClass("cur");
			}
			a.remove();
			$(this).parent("li").remove();
			m--;
		})
		
	    // 文本编辑器的分页切换
		$(".pageSize li a.btn").live("click",function(){
			// 更换按钮样式
			$(this).parents(".pageSize").children("li.cur").removeClass("cur");
			$(this).parent().addClass("cur");
			// 显示当前的CKEDITOR
			    // 需要显示哪一个CKEDITOR
			var n = parseInt($(this).text());
			var nowContentId = "content" + n;
			
			// 隐藏所有的
			var dds = $(".formBoxLine dd");
			dds.each(function(){
				$(this).hide();
			});
			// 显示当前的
			$("#"+nowContentId).parent().show();
		})
 });

	// 关联频道弹出层
	function channelRel(){
		$("#channeltc").load($("#ctxpath").val()+"/system/content/channelList",function(){
			
		});
		$("#channelBg").show();
		$("#channeltc").parents(".del_tc").show();
	}
	
	function channelSure(){
		$("#cids").val($("#channelId").val());
		alert("关联成功");
		$("#channelBg").hide;
		$("#channeltc").parents(".del_tc").hide();
	}
 //======================================長文本內容分頁END==============================================

	function changeCtype(act){
			if(act == "0"){
				$("#cLabelDL").show();
			}else{
				$("#cLabelDL").hide();
                $("#contentLabelSelect").val("");
			}
	}