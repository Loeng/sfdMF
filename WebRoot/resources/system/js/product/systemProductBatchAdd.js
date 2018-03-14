$(document).ready(function(){
	var error = $.trim($("#errorResult").val());
	var status = $("#resultStatus").val()

	if(status =="ok" && error==""){
		ealert('上传成功');
	}else{
		$("#errorSpan").html(error);
		$("#errorDiv").css("display","block");
	}
});

function submit(){
	var excel = $("#productexcelPurviewA").html();
	var storeId = $("#storeId").val();
	var zip = $("#picpathPurviewA").html();
	if(storeId==""){
		ealert("请选择核心企业");
		return false;
	}else{
		if(excel=="" || excel=="请选择需要上传的EXCEL文档!"){
			ealert("请选择要上传的excel");
			return false;
		}else{
			if(zip=="" || zip =="请选择需要上传的压缩包!"){
				ealert("请选择要上传的zip包");
				return false;
			}else{
				document.getElementById("saveProductBatchForm").submit();
			}
		}
		
	}
}