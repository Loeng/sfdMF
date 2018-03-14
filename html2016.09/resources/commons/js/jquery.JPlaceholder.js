//@charset "utf-8";
/**
 * jquery版本要求：1.3 ~ 1.8，HTML声明请遵循W3C标准
 * 用来模拟HTML5 input标签的placeholder属性的插件
 * 兼容IE6、7、8、9浏览器
 * @author wangzhiangtony@qq.com
 * @version 1.0
 * @date 2013-4-15 11:16:59
 */
$(function($) {
    //判断浏览器是否支持placeholder属性
    function isPlaceholder() {
        var input = document.createElement('input');
        return 'placeholder' in input;
    }
 
    function changeToOriginalColor(self) {
        var index = $(self).index();
        var color = originalColor[$(self).index()];
        $(self).css('color', color);
    }
 
    if(!isPlaceholder()) {
        var texts = $(':text');
        var len = texts.length;
        var originalColor = [];
        for(var i = 0; i < len; i++) {
            var self = texts[i];
            var placeholder = $(self).attr('placeholder');
            if($(self).val() == "" && placeholder != null) {
                $(self).val(placeholder);
                originalColor[i] = $(self).css('color');
                $(self).css('color', '#9B9B9B');
            }
        }
        texts.focus(function() {
            if($(this).attr('placeholder') != null && $(this).val() == $(this).attr('placeholder')) {
                $(this).val('');
                changeToOriginalColor(this);
            }
        }).blur(function() {
            if($(this).attr('placeholder') != null && ($(this).val() == '' || $(this).val() == $(this).attr('placeholder'))) {
                $(this).val($(this).attr('placeholder'));
                $(this).css('color', '#9B9B9B');
            }
        });
    }
});


/*$(function(){
if(!placeholderSupport()){   // 判断浏览器是否支持 placeholder
    $('[placeholder]').focus(function() {
        var input = $(this);
        if (input.val() == input.attr('placeholder')) {
            input.val('');
            input.removeClass('placeholder');
        }
    }).blur(function() {
        var input = $(this);
        if (input.val() == '' || input.val() == input.attr('placeholder')) {
            input.addClass('placeholder');
            input.val(input.attr('placeholder'));
        }
    }).blur();
};
})
function placeholderSupport() {
    return 'placeholder' in document.createElement('input');
}*/
