// JavaScript Document

$(document).ready(function() {
function SuCaiJiaYuan(){
	this.init();
}

SuCaiJiaYuan.prototype = {
	constructor: SuCaiJiaYuan,
	init: function(){		
		this._initBackTop();
	},	
	_initBackTop: function(){
		var $backTop = this.$backTop = $('<div class="cbbfixed">'+'<a class="gotop cbbtn" title="返回顶部">'+'<span class="up-icon"></span>'+'</a>'+'</div>');
		$('body').append($backTop);
		$backTop.click(function(){
			$("html, body").animate({scrollTop:0}, 120);
		});

		var timmer = null;
		$(window).bind("scroll",function() {
            var d = $(document).scrollTop(),
            e = $(window).height();
            0 < d ? $backTop.css("bottom", "90px") : $backTop.css("bottom", "-101px");
			clearTimeout(timmer);
			timmer = setTimeout(function() {
                clearTimeout(timmer)
            },100);
	   });
	}
	
}
var SuCaiJiaYuan = new SuCaiJiaYuan();

});
