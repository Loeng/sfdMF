/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function(config) {
	// Define changes to default configuration here. For example:
	//config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.shiftEnterMode = CKEDITOR.ENTER_P;//去掉自动加p标签
	config.enterMode = CKEDITOR.ENTER_P;
	config.fullPage= true;
	config.allowedContent= true;

	var pathName = getContextPath();
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	config.filebrowserImageUploadUrl = projectName+"/ckeditor/upload";
	config.filebrowserUploadUrl=projectName+"/ckeditor/upload";
	
	// 保留word粘贴样式
	config.pasteFromWordIgnoreFontFace = true; //默认为忽略格式
	config.pasteFromWordRemoveFontStyles = false;
	config.pasteFromWordRemoveStyles = false;
	// 是否使用等标签修饰或者代替从word文档中粘贴过来的内容。plugins/pastefromword/plugin.js
	//	CKEDITOR.config.pasteFromWordKeepsStructure = true;
	//	CKEDITOR.config.plugins
	//	CKEDITOR.config.protectedSource
	
	//文本框高度自动增加插件
	//	config.extraPlugins = 'autogrow';
	
	//文本框高度
	config.height = '500';
	
	//文字大小默认为14px
	config.fontSize_defaultLabel = '14';
};

function getContextPath() {
	var contextPath = document.location.pathname;
	var index =contextPath.substr(1).indexOf("/"); 
	contextPath = contextPath.substr(0,index+1); 
	delete index; 
	if(contextPath == "/system" || contextPath == "/store" || contextPath == "/user"){
		contextPath = "";	
	}
	return contextPath; 
}