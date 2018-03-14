/**
 * @license Copyright (c) 2003-2014, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function(config) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	
	
	var pathName = getContextPath();
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	config.filebrowserImageUploadUrl = projectName+"/ckeditor/upload";
	config.filebrowserUploadUrl=projectName+"/ckeditor/upload";
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