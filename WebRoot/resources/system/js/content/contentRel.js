$(document).ready(function() {
	$(".limitItem").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			var ids = $("#channelId").val();
			var names = $("#channelName").val();
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
	
	var check=$(".limitItem input:checked");
	for(i=0;i<check.length;i++){
		check.eq(i).parents().prev(".limitItem").find("input").attr("checked",true);
		check.eq(i).parents().prev(".limitItem").find(".check").addClass("checked");
	}
});	

function checkF(){
	var xuan=$(".limitItem input:checked");
	var str=""
	xuan.each(function(){
		str+=$(this).parents("label").text().trim()+"  ";
	})
 	$("#checkResult").attr("value",str);
	$("#checkResult").attr("title",str);
}

window.onload=checkF();



////关联频道弹出层
//function channelRel(id){
//	if(id == null || id == ""){
//		$("#channeltc").load($("#ctxpath").val()+"/system/content/channelList",function(){
//		});
//	}else{
//		$("#channeltc").load($("#ctxpath").val()+"/system/content/channelList?id=" + id,function(){
//		});
//	}
//	$("#channelBg").show();
//	$("#channeltc").show();
//}
//	
	function channelSure(){
		$("#cids").val($("#channelId").val());
		apClose();
		checkF();
	}
	
	//关闭弹出层
	function apClose(){
		$("#channelBg").hide();
		$("#channeltc").parents(".del_tc").hide();
	}
	
 //======================================長文本內容分頁END==============================================