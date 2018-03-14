
$(function(){
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
		var ids=getReplyAllIDS();
		 if(ids.trim().length==0){
			 alert("请先选择");
			 return;
		 }
		$(".replyLayer").hide();
		$(this).parent().children(".replyLayer").show();
	})
	
	/*全选*/
	$(".checkAll").click(function(){
		$(".consultLine").find()
	})
	
})

//提交搜索表单
function submitForm(){
	document.getElementById("consultMessageForm").submit();
}

//清空搜索表单
function resetForm(){
	document.getElementsByName("replyStatus")[0].checked=true;
	$("#userName").val('');
	$("#beginDate").val('');
	$("#endDate").val('');
	$("#endDate").val('');
}

// 咨询回复
function  consultReply(thisObj){
	var contextPath=$("#ctxpath").val();
	var content=$(thisObj).prev().val();
	var consultID = $(thisObj).next().val();
	if(content){
		     $.post(
				contextPath + "/store/appraisal/replyConsult",
				{content:content,consultID:consultID},
			 	function(data){
			 		if(data=="noNull"){
			 			falert("回复不能为空");
			 		}else if(data=="false"){
			 			falert("回复失败,请稍后重试！");
			 		}else{
			 			salertWithFunction("回复成功",submitForm,null);
			 		}
			 	}
			 );
	}else{
		falert("回复内容不能为空");
	}
}

function isChecked(_this){
	if(_this.name=="true"){
		_this.checked=false;
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
			contextPath + "/store/appraisal/replyConsultAll",
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
//全选 全不选
function selectAll(){
	var checked=$('#selectAll')[0].checked;
	$('.consultText input').each(function(){
		    if(checked){
		    	if(this.name=='false'||this.namge==false){
		    		this.checked=true;
		    	}
		    	
		    }else{
		    	this.checked=false;
		    }
	})
}

//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("consultMessageForm").submit();
}