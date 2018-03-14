function targetLoad(){
	$("#loadTarget").load($("#ctxpath").val()+"/user/target/list");
}

// 单条评价的提交
function evaluateOrder(ctxpath){
	//econfirm('是否确认删除？',deleteAll,[$("#ctxpath").val()],null,null);
	econfirm("确定要提交评价?", newEvaluateOrder, null, null, null);
	/*
	var i = econfirm("确定要提交评价?");
	if(!i){
		return;
	}
// 多条记录的评价方法
	$("#evaluateToUserId").submit();
	*/
}
function newEvaluateOrder(){
	$("#evaluateToUserId").submit();
}


// 好评、中评、差评
function setEvaluate(value){
	var temp="";
	$("input[type='radio']").each(function(){
		if(this.checked){
			temp += ","+$(this).val();
		}
	});
	$("#etypeindex").val(temp.substring(1));
}
// JavaScript Document
$(document).ready(function() {
	/*选项卡切换*/
    $(".tabs a").click(function() {
        $(this).siblings().removeClass("nowItem");
		$(this).addClass("nowItem");
    });
	$(".tabs a").eq(0).click(function(){
		$(".toMe").hide();
		$(".toOther").show();
	})
	$(".tabs a").eq(1).click(function(){
		$(".toMe").show();
		$(".toOther").hide();
	})
	
	/*点击回复弹出*/
    $(".replyLink").click(function(){
		$(".replyLayer").hide();
		$(this).parents(".consultLine").next(".replyLayer").show();
	})
	
	/*取消关闭*/
	$(".canselBtn").click(function(){
		$(this).parents(".replyLayer").hide();
	})
	
	/*批量回复*/
	$(".replyMore").click(function(){
		$(".replyLayer").hide();
		$(this).parent().children(".replyLayer").show();
	})
	
	/*全选*/
	$(".checkAll input[type='checkbox']").click(function(){
		a = $(this);
		if(a.is(":checked")){
			$(".consultLine input[type='checkbox']").attr("checked",true);
		}
		else{
			$(".consultLine input[type='checkbox']").attr("checked",false);
		}
	})
	
});

//提交搜索表单

function submitForm(){
	document.getElementById("appraiseForm").submit();
}

//清空搜索表单
function resetForm(){
	document.getElementsByName("replyStatus")[0].checked=true;
	$("#productName").val('');
	$("#beginDate").val('');
	$("#endDate").val('');
}

//回复
function  reply(_this,consultID){
	
	var contextPath=$("#ctxpath").val();
	var content=$(_this).prev().val();
	if(content){
		     $.post(
				contextPath + "/store/appraisal/replyAppraise",
				{content:content,consultID:consultID},
			 	function(data){
			 		if(data=="noNull"){
			 			alert("回复不能为空");
			 		}else if(data=="false"){
			 			alert("回复失败,请稍后重试！");
			 		}else{
			 			submitForm();
			 			alert("回复成功");
			 		}
			 	}
			 );
	}else{
		alert("回复内容不能为空");
	}
}


function getReplyAllIDS(){
	var tempIDS="";
	 $('.consultText input').each(function(){
	    if(this.checked){
	    	tempIDS+=this.value+"@";
	    }
   })
   var ids=tempIDS.replace(/@$/g, "");
	 
   return ids
}
//回复 多人
function  replyAll(){
	
	var ids=getReplyAllIDS();
	 if(ids.trim().length==0){
		 alert("请先选择");
		 return;
	 }
	var contextPath=$("#ctxpath").val();
	var content=$("#saveReplyAll").val();
	if(content){
		 
	     $.post(
			contextPath + "/store/appraisal/replyAppraiseAll",
			{content:content,consultIDS:ids},
		 	function(data){
		 		if(data=="noNull"){
		 			alert("回复不能为空");
		 		}else if(data=="false"){
		 			alert("回复失败,请稍后重试！");
		 		}else{
		 			submitForm();
		 			alert("回复成功");
		 		}
		 	}
		 );
	}else{
		alert("回复内容不能为空");
	}
}


//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	try{
		document.getElementById("evaluateToUserIdPage").submit();
	}catch(err){
		var pages = $("input[name='pageNum']").val();
		var htmls = $("a[class='nowItem']").html();
		if(htmls=="给别人的评价"){
			$("#toOther11New").load($("#ctxpath").val()+"/user/appraisal/appraisalListContentLoad?judg=1&pageNum="+pages,function(){
				$("#toOther11New").show();
			});
		}else{
			$("#toOther11New").load($("#ctxpath").val()+"/user/appraisal/appraisalListContentLoad?judg=2&pageNum="+pages,function(){
				$("#toOther11New").show();
			});
		}
	}
}

function appraiseListLoad(contextPath,obj,judg){
	$("a[errors='nowItem111']").each(function(){
		if(this==obj){
			$(this).attr("class","nowItem");
		}else{
			$(this).removeAttr("class");
		}
	});
	
	$("#toOther11New").load($("#ctxpath").val()+"/user/appraisal/appraisalListContentLoad?judg="+judg+"&pageNum=1",function(){
		$("#toOther11New").show();
	});
}


