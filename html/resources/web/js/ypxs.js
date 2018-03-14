// JavaScript Document

$(document).ready(function() {
   windowMin();
});

/*响应式*/
function windowMin(){
	var gao=$(window).height();
	console.log(gao);
	if(gao<760){
		$("head").append('<link href="../../../resources/web/css/indexMin.css" rel="stylesheet" type="text/css">');
	}
	else{
		//$("link:eq(1)").remove();
	}
}

window.onresize=windowMin;