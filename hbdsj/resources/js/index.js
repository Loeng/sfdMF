/**
 * Created by ekfans on 2017/7/7.
 */
$(".searchLayer").hide();
$(".searchLayer .close").live("click",function(){
    $(".searchLayer").slideUp();
    $(".headerBg").fadeOut(500);
})
$(document).mouseup(function(e){
    if($(e.target).parents(".searchLayer").length==0){
        $(".searchLayer").slideUp();
        $(".headerBg").fadeOut(500);
    }
})

$(".moreSearch").live("click",function(){
    $(".searchLayer").slideDown();
    $(".headerBg").fadeIn(500);
})
$(".search ul li").click(function(){
    $(this).addClass("cur").siblings().removeClass("cur");
})

//多选框
$(".check").live("click",function(){
    var obj=$(this);
    var bool=obj.find("input").is(":checked");
    if(bool){
        obj.addClass("checked");
    }
    else{
        obj.removeClass("checked");
    }
})

//二次弹层
$(".addSite").click(function () {
    $(".siteLayer,.layerBg").fadeIn(200);
})
$(".addHy").click(function () {
    $(".hyLayer,.layerBg").fadeIn(200);
})
$(".addWr").click(function () {
    $(".wrLayer,.layerBg").fadeIn(200);
})
$(".addCode").click(function () {
    $(".threeLayer,.layerBg").fadeIn(200);
})
$(".addJyfw").click(function () {
    $(".jyfwLayer,.layerBg").fadeIn(200);
})
$(".addDouble").click(function () {
    $(".doubleLayer,.layerBg").fadeIn(200);
})
$(".addGtcz").click(function () {
    $(".gtczLayer,.layerBg").fadeIn(200);
})
$(".addJslx").click(function () {
    $(".jslxLayer,.layerBg").fadeIn(200);
})

//关闭弹层
$(".closeLayer,.upLayer.selectLayer li,.wfCodeList tr").live("click",function () {
    $(".upLayer,.layerBg,.hyType,.wfCodeList").fadeOut(200);
})

$(document).mouseup(function(e){
    if($(e.target).parents(".upLayer").length==0){
        $(".upLayer,.layerBg").hide();
    }
})


//选项卡
$.fn.extend({
    qiehuan:function(canshu){
        //默认参数，为了测试所以写的区别于真实应用
        var defaults={
            tab:".tabNav li",
            cont:".tabContent",
            onStyle:"hover"
        };

        $.extend(defaults,canshu);
        var main=$(this);

        main.find(defaults.tab).on(defaults.onStyle,function(){
            $(this).siblings().removeClass("cur");
            $(this).addClass("cur");
            var dom=$(this).parent().siblings(defaults.cont);
            var index=$(this).parent().children().index(this);
            dom.eq(index).show().siblings(defaults.cont).hide();
            $("#tabIndex").val(index);
        })
    }
})
$(".layerContent").qiehuan({tab:".leftTab li",cont:".tabContent",onStyle:"hover"});


//选择项
$(".siteLayer").on("click","li",function () {
    $(this).addClass("cur").siblings().removeClass("cur");
})

//逐级选择
$(".threeLayer").on("click",".hyCode li",function () {
    $(this).addClass("cur").siblings().removeClass("cur");
    $(".hyType").fadeIn(200);
})
$(".threeLayer").on("click",".hyType li",function () {
    $(this).addClass("cur").siblings().removeClass("cur");
    $(".threeLayer .wfCodeList").fadeIn(200);
})

$(".doubleLayer").on("click",".hyCode li",function () {
    $(this).addClass("cur").siblings().removeClass("cur");
    $(".doubleLayer .wfCodeList").fadeIn(200);
})


//确定项
$(".siteLayer").on("click",".btnBlue2",function () {
    var index=$("#tabIndex").val();
    var ssq="";
    $(".siteLayer").find(".cur").each(function () {
        ssq+=$(this).text();
    })
    $(".addSite").eq(index).before('<li>'+ssq+'<span class="deleteIco"></span></li>');
})
$(".hyLayer").on("click","li",function () {
    var index=$("#tabIndex").val();
    var hy=$(this).text();
    $(".addHy").eq(index).before('<li>'+hy+'<span class="deleteIco"></span></li>');
})
$(".wrLayer").on("click","li",function () {
    var index=$("#tabIndex").val();
    var wr=$(this).text();
    $(".addWr").eq(index).before('<li>'+wr+'<span class="deleteIco"></span></li>');
})

$(".threeLayer .wfCodeList").on("click","tbody tr",function () {
    var index=$("#tabIndex").val();
    var code=$(this).children("td").eq(0).text();
    $(".addCode").before('<li>'+code+'<span class="deleteIco"></span></li>');
})
$(".doubleLayer .wfCodeList").on("click","tbody tr",function () {
    var index=$("#tabIndex").val();
    var code=$(this).children("td").eq(0).text();
    $(".addDouble").before('<li>'+code+'<span class="deleteIco"></span></li>');
})
$(".jyfwLayer").on("click","li",function () {
    var jyfw=$(this).text();
    $(".addJyfw").before('<li>'+jyfw+'<span class="deleteIco"></span></li>');
})
$(".cllxLayer").on("click","li",function () {
    var cllx=$(this).text();
    $(".addCllx").before('<li>'+cllx+'<span class="deleteIco"></span></li>');
})
$(".gtczLayer").on("click","li",function () {
    var gtcz=$(this).text();
    $(".addGtcz").before('<li>'+gtcz+'<span class="deleteIco"></span></li>');
})
$(".jslxLayer").on("click","li",function () {
    var jslx=$(this).text();
    $(".addJslx").before('<li>'+jslx+'<span class="deleteIco"></span></li>');
})

//删除项
$(".formItem").on("click",".deleteIco",function () {
    $(this).parents("li").remove();
})

