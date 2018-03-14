$(document).ready(function() {
	if($("#modifyOk").val()=="true"){
		
	}else if(!$("#modifyOk").val()=="false"){
		ealert("修改失败！");
	}
	
	$("input.i_bg").focus(function (){ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	});
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
	$(".tabs a").click(function(){
		$(".tabs li").removeClass("cur");
		$(this).parent().addClass("cur");
	})
})

// form的ajax提交
function formSubmit(){
	var flag = true;
	// 得到dd标签对象数组
	var dd = $(".add_pro dd");
	for(var i=0;i<dd.size();i++){
		// 得到dd对象的class
		var ddClass = $(dd[i]).attr("class");
		// 如果dd的class为错误样式，返回false
		if(ddClass==="text_c_ts"){
			flag = false;
			break;
		}
	}
	if(flag==true){
		$("#paramConfigForm").ajaxSubmit(
				function(data){
				if(data==true){
					ealert("修改成功！");
	 			}else{
	 				ealert("修改失败");
	 			}
	 		});
	}
}

// 表单重置
function formReset(){
	document.getElementById("paramConfigForm").reset();
}

// 动态加载配置列表
function showConfigs(id){
	$("#dataArea").load($("#ctxpath").val()+"/system/config/paramConfig/list/"+id);
}

//判断数据类型属否为Number
function isNumber(num){
	var reNum=/^\d*$/;
	return(reNum.test(num));
}

// 检验是否需要输入数字
function checkNum(id,note){
	if(note=="请输入数字"){
		var val = document.getElementsByName('value'+id)[0].value;
		var ddId = "dd" + id;
		if(!isNumber(val)){
			document.getElementById(ddId).className="text_c_ts";
		}else{
			document.getElementById(ddId).className="text_ts";
		}
	}
}

//给出输入数字的提示
function checkNumBlur(id,note){
	if(note=="请输入数字"){
		var val = document.getElementsByName('value'+id)[0].value;
		var ddId = "dd" + id;
		if(!isNumber(val)){
			document.getElementById(ddId).className="text_c_ts";
		}else{
			document.getElementById(ddId).className="";
		}
	}
}