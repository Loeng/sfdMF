// JavaScript Document
$(document).ready(function() {
    $(".checkAll input[type='checkbox']").click(function(){
		a = $(this);
		if(a.is(":checked")){
			$(".tdCheck input[type='checkbox']").attr("checked",true);
		}
		else{
			$(".tdCheck input[type='checkbox']").attr("checked",false);
		}
	})
});