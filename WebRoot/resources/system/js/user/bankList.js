//查询
function searchSubmit(){
	document.getElementById("searchBankForm").submit();
}
//重置
function reset(){
	document.getElementById("name").value ='';
	document.getElementById("banknum").value ='';
}
//查询详细信息
function bankModify(id,ctx,userid){
  window.location.href=ctx+"/system/usesr/bankModify/"+id+"/"+userid;	
}

