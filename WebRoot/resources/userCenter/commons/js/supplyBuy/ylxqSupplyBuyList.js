// 搜索表单提交
function formSubmit(){
	$("#searchSupplyBuy").submit();
}

// 搜索表单重置
function formReset(){
	$("#title").val("");
	$("#status").val("");
	$("#beginDate").val("");
	$("#endDate").val("");
}

//关闭
function deleteSupplyBuy(id,contextPath){
	$.post(
			contextPath + "/store/supplyBuy/ylxqDetele/"+id,
			function(data){
				if(data){
					salertWithFunction("关闭成功！",toList,null)
				}else{
					falert("关闭失败！");
				}
			}
		);
}

//删除
function removeSupplyBuy(id,contextPath){
    $.post(
        contextPath + "/store/supplyBuy/ylxqRemove/"+id,
        function(data){
            if(data){
                salertWithFunction("删除成功！",toList,null)
            }else{
                falert("删除失败！");
            }
        }
    );
}
// 完成
function finishSupplyBuy(id,contextPath){
	$.post(
			contextPath + "/store/supplyBuy/ylxqFinish/"+id,
			function(data){
				if(data){
					salertWithFunction("操作成功！",toList,null)
				}else{
					falert("操作失败！");
				}
			}
	);
}

function toList(){
	var contextPath = $("#contextPath").val();
	var productType = $("#productType").val();
	var type = $("#type").val();
	window.location.href= contextPath + "/store/supplyBuy/ylxqList/"+productType+"/"+type;
}
//分页跳转
function goPage(pageNum){
	$("input[name='pageNum']").val(pageNum);
	document.getElementById("searchSupplyBuy").submit();
}

