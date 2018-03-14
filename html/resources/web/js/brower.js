// JavaScript Document

$(document).ready(function() {
	findDimensions(); // 调用函数
});

/*浏览器大小判断*/
var winWidth = 0;
var winHeight = 0;
function findDimensions() //函数：获取尺寸
{
	// 获取窗口宽度
	if (window.innerWidth)
		winWidth = window.innerWidth;
	else if ((document.body) && (document.body.clientWidth))
		winWidth = document.body.clientWidth;
	// 获取窗口高度
	if (window.innerHeight)
		winHeight = window.innerHeight;
	else if ((document.body) && (document.body.clientHeight))
		winHeight = document.body.clientHeight;
	// 通过深入Document内部对body进行检测，获取窗口大小
	if (document.documentElement  && document.documentElement.clientHeight && document.documentElement.clientWidth)
	{winHeight = document.documentElement.clientHeight;
	winWidth = document.documentElement.clientWidth;}
	/*$("body").css("height",winHeight+"px");*/
	var ksgao=$(window).height(); /*浏览器可视高度*/
	var kskuan=$(window).width(); /*浏览器可视宽度*/
	var sjgao=$(document).height() < $('body').height() ? $(document).height() : $('body').height();/*内容实际高度*/
	var sjkuan=$(document).width() < $('body').width() ? $(document).width() : $('body').width();/*内容实际宽度*/
	console.log("实际高"+sjgao+"px;可视高"+ksgao+"px;可视宽"+kskuan+"px;实际宽"+sjkuan+"px;");
}
window.onresize=findDimensions;