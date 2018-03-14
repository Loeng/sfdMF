

//审核通过
function formSubmit(id,contextPath) {
   econfirm("确认此店铺申请通过？",submit,[id,contextPath],null,null);
}
//通过
function submit(id,contextPath){
	window.location.href = contextPath + "/system/storeApply/checkModify/"+id;
}


