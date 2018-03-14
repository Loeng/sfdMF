// JavaScript Document

$(document).ready(function() {
	 
   windowMin();
});

/*响应式*/
function windowMin(){
	var gao=$(window).height();
	console.log(gao);
	if(gao>667){
		$("head").append('<link href="../../../resources/web/css/xys.css" rel="stylesheet" type="text/css">');
	}
	else{
		var cc=$("link:last").attr("href");
		console.log(cc);
		var xys="../../../resources/web/css/xys.css"
		console.log(xys);
		if(cc==xys){
		$("link:last").remove();}
		else{}
	}
}

window.onresize=windowMin;