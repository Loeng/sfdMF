$(document).ready(function() {
	// 判定后台是否返回添加成功的信息
	if($("#ok").val()=="true"){
		econfirm('添加成功，是否继续添加？',null,null,goBack,[$("#ctxpath").val()]);
	}else if($("#ok").val()=="false"){
		ealert("添加失败！");
	}
});

//验证广告名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >32){
		document.getElementById("nameDd").className="text_ts";
		return false;
	}else{
		return true;
	}
}

//验证广告名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if($.trim(nameStr).length < 2 || $.trim(nameStr).length >32){
		document.getElementById("nameDd").className="text_c_ts";
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}

// 选择类型后的load
function typeLoad(value,contextPath){
	if(value==""){
		$("#typeLoad").text("");
	}else{
		$("#typeLoad").load(contextPath + "/system/advert/typeLoad/"+value,function(){
			if(value=="2"){
				CKEDITOR.replace("ckeditor");
			}
			$("input.i_bg").focus(function ()//得到焦点时触发的事件
					{ 
						$(this).parent().addClass("text_ts");
					});
					$("input.i_bg").blur(function () //失去焦点时触发的事件
						{ 
						$(this).parent().removeClass("text_ts");
					}); 
			 /*选项卡切换*/
			$(".tabs li").live("click",function(){
				$(this).siblings().removeClass("cur");
				$(this).addClass("cur");
				var content=$(this).parent().next(".advStyleContent").children(".styleContent");
				var index=$(this).parent().children("li").index(this);
				content.removeClass("cur");
				content.eq(index).addClass("cur");
			})			
			/*关闭广告形式选项卡面板*/
			$(".closeIco").click(function(){
				$(this).parents(".tabStyle").hide();
			})
			
			/*打开广告形式选项卡面板*/
			$(".btnBlue").click(function(){
				$(this).parents("dl").next(".tabStyle").show();
			})
			
		});
	}
}

//选择数量后的load
function numLoad(value,contextPath){
	var _type = $("#typeSelect").val();
	if(value==""||value=="0"){
		document.getElementById("numDd").className="text_c_ts";
		$("#numLoad").text("");
	}else if(value>20){
		ealert("广告数量最多20个");
	}else{
		document.getElementById("numDd").className="";
		$("#numLoad").load(contextPath + "/system/advert/numLoad/"+_type+"/"+value,function(){
			/*设文字颜色*/
			$(".color").colorpicker({
			fillcolor:true,
			success:function(o,color){
				$(o).children("em").css("background",color);
				$(o).children("em").next("input").val(color);
				$(o).siblings(".bigBox").css("color",color);
				$(o).css("color","#333");
				///////////
			}})
		});
	}
}

// 验证是否选择单图广告
function checkShow(value){
	if(value=="dantu"){
		$("#number").val(1);
		$("#number").blur();
		$("#number").attr({ readonly: 'true' });
	}else{
		$("#number").val(1);
		$("#number").blur();
		$("#number").removeAttr("readonly");
	}
}

//提交
function formSubmit(){
	var _typeValue = $("#typeSelect").val();
	var _showValue = $("#showSelect").val();
	var _numValue = $("#number").val();
	if(_typeValue=="" || _typeValue=="请选择"){
		ealert("请选择广告类型");
	}else if(_showValue=="" || _showValue=="请选择"){
		ealert("请选择广告风格");
	}else if(_numValue=="" || _numValue==" "){
		document.getElementById("numDd").className="text_c_ts";
	}else{
		if(checkNameBlur()){
			document.getElementById("addAdForm").submit();
		}
	}
	
}

// 重置
function reset(){
    document.getElementById("addAdForm").reset();
}

// 返回列表
function goBack(contextPath){
	// 判定contextPath是否为空
	if(contextPath==null||contextPath==""){
		window.location.href="/system/advert/list";
	}
	window.location.href=contextPath + "/system/advert/list";
}