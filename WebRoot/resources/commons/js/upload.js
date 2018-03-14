/**
 * EKFansUpload上传
 * @author chenzhiheng
 * 
 * 依赖css：
 * /resources/commons/css/upload/upload.css
 * 
 * 依赖js：  
 * /resources/commons/js/jquery-1.8.3.min.js //jquery核心文件
 * ###EKFansUpload依赖###
 * /resources/plugin/fileupload/js/vendor/jquery.ui.widget.js //jqueryUI的widget依赖
 * /resources/plugin/fileupload/js/jquery.fileupload.js //jquery文件上传插件基础依赖
 * /resources/commons/js/upload/upload.js'
 */
 
//获取访问路径
function getContextPath(){
	var contextPath = window.location.protocol + "//" + window.location.host;
	var content = document.location.pathname;
	if(isStartWith(content,"/store") || isStartWith(content,"/system") || isStartWith(content,"/user")){
			content = "";
	}else{
		var index =content.substr(1).indexOf("/");
		content = content.substr(0,index+1);
		delete index;
	}
	if(content != null && content != '' && content != ' ' && content.indexOf("store") && content.indexOf("/bg")){
		if(isStartWith(content,"/")){
			contextPath = contextPath + content;	
		}else{
			contextPath = contextPath +"/"+ content;	
		}
	}
	return contextPath;
}
var contentPath = getContextPath();
var ajaxChosePic = function(inputName,widthStr,heightStr,fileSize){
	console.log(inputName+"<<<");

	var oldPicPath = document.getElementsByName(inputName+"OldRealPath")[0].value;
	var picwh = document.getElementsByName(inputName+"wh")[0].value;
	var isEdit = document.getElementsByName(inputName+"editUrl")[0].value;
	var isRemove = $("#" + inputName + "remove").val();
	if((oldPicPath != '' && oldPicPath != ' ' && oldPicPath != null && oldPicPath != 'null' && "true" == isEdit) && isRemove == "false"){
		var img = new Image();//构造JS的Image对象
		img.src = oldPicPath;//将本地图片赋给image对象
		// 加载完成执行
		img.onload = function(){
			// 打印
			initPic(inputName,oldPicPath,widthStr,heightStr,img.width,img.height,fileSize);
		};
	}

	new AjaxUpload(inputName+"InputFile",{
		action:contentPath+"/upload/picUpload/"+inputName,  
		autoSubmit:true,
		responseType:"json",
		name:inputName+"InputFile",
		//提交图片时的操作
		onSubmit:function(file, extension){
			if (extension && /^(pdf|jpg|png|jpeg|gif|PDF|JPG|PNG|JPEG|GIF)$/.test(extension)){
				showWait(inputName);
				hideButton(inputName);
			} else {
				ealert("你所选择的文件不受系统支持");
				hideWait(inputName);
				showButton(inputName,widthStr,heightStr,fileSize);
				return false;
			}
		},
		
		//图片提交完成后返回的操作
		onComplete: function(file, response){
			var dataobj=response;
			var errorMessage = dataobj.errorMessage;
			if(errorMessage != '' &&  errorMessage != ' '){
				hideWait(inputName);
				showButton(inputName,widthStr,heightStr,fileSize);
				alert("文件上传失败,文件大小最大可传"+fileSize + "M");
				document.getElementById(inputName+"Preview").innerHTML="";
				document.getElementById(inputName+"Preview").style.background = "url("+contentPath + "/images/fileUpload/zwtp.png)  center center no-repeat";
				document.getElementsByName(inputName+"FileUrl")[0].value="";
				document.getElementById(inputName+"Preview").setAttribute("onmouseover","");
				document.getElementById(inputName+"Preview").setAttribute("onmouseout","");
			}else{
				var fileUrl = dataobj.fileUrl;
				var realPath = dataobj.realPath;
				var img = new Image();//构造JS的Image对象
				img.src = fileUrl;//将本地图片赋给image对象
				// 加载完成执行
				var imgWidth = 0;
				var imgHeight = 0;
				img.onload = function(){
					// 打印
					initPic(inputName,fileUrl,widthStr,heightStr,img.width,img.height,fileSize);
					document.getElementsByName(inputName+"editUrl")[0].value="false";
					document.getElementsByName(inputName+"FileUrl")[0].value=realPath;
					hideWait(inputName);
					hideButton(inputName);
				};
			}
		}
	});
};

function initPic(inputName,picPath,widthStr,heightStr,picWidth,picHeight,fileSize){
	var attrName = "";
	var attrValue="";
	if(parseInt(picWidth) == parseInt(picHeight)){
		widthStr = ((parseInt(widthStr) * 0.86) - 2);
	}
	//heightStr = ((parseInt(heightStr) * 0.86) - 3);
	if(parseInt(picWidth) >= parseInt(picHeight) && parseInt(picWidth) <= parseInt(widthStr)){
		attrName = "width";
		attrValue = picWidth + "px";
	}else if(parseInt(picWidth) >= parseInt(picHeight) && parseInt(picWidth) >= parseInt(widthStr)){
		attrName = "width";
		attrValue = widthStr + "px";
	}else if(parseInt(picHeight) >= parseInt(picWidth) && parseInt(picHeight) <= parseInt(heightStr)){
		attrName = "height";
		attrValue = picHeight + "px";
	}else if(parseInt(picHeight) >= parseInt(picWidth) && parseInt(picHeight) >= parseInt(heightStr)){
		attrName = "height";
		attrValue = heightStr + "px";
	}
	
	$("#" + inputName + "Img").attr("width","");
	$("#" + inputName + "Img").attr("height","");
	$("#" + inputName + "Img").attr("src",picPath);
	$("#" + inputName + "Img").attr(attrName,attrValue);
	showRemove(inputName,widthStr,heightStr,fileSize);
	
}


function removePic(inputName,widthStr,heightStr,fileSize){
	//获取传进来的原图路径
	var oldPicPath = document.getElementsByName(inputName+"OldRealPath")[0].value;
	//获取是否编辑
	var isEdit = document.getElementsByName(inputName+"editUrl")[0].value;
	if("true" == isEdit){
		$("#" + inputName + "Img").attr("height",heightStr);
		$("#" + inputName + "Img").attr("src", contentPath + "/resources/commons/images/upload/imgdefault.jpg");
		$("#" + inputName + "remove").val("true");
		hideRemove(inputName);
		showButton(inputName,widthStr,heightStr,fileSize);
	}else{
		var picPath = document.getElementsByName(inputName+"FileUrl")[0].value;
		var url = contentPath+"/upload/fileRemove";
		$.getJSON(url ,{filePath:picPath,ranNum:Math.random()} ,function(data){
			var result = data ;
			if(result.remove == "true" || result.remove == true){
				if('' != oldPicPath && "false" == document.getElementsByName(inputName + "remove")[0].value){
					var img = new Image();//构造JS的Image对象
					img.src = oldPicPath;//将本地图片赋给image对象
					initPic(inputName,oldPicPath,widthStr,heightStr,img.width,img.height,fileSize);
					document.getElementsByName(inputName + "editUrl")[0].value="true";
					document.getElementsByName(inputName + "FileUrl")[0].value="";
					showButton(inputName,widthStr,heightStr,fileSize);
				}else{
					$("#" + inputName + "Img").attr("height",heightStr);
					$("#" + inputName + "Img").attr("src",contentPath + "/resources/commons/images/upload/imgdefault.jpg");
					hideRemove(inputName);
					showButton(inputName,widthStr,heightStr,fileSize);
				}
				document.getElementsByName(inputName+"FileUrl")[0].value="";
			}
		}) ;
	}	
}

/*
 *显示上传等待的框
 *
 */
function showWait (inputName) {
	var purviewElement = $("#"+ inputName + "Purview");
	var appendStr = "<span class=\"uploadingBg\" id=\"" + inputName + "wait\"></span>";
	purviewElement.append(appendStr);
}

/*
 *移除上传等待的框
 *
 */
function hideWait (inputName) {
	var waitElement = document.getElementById(inputName + "wait");
	if(waitElement!=null){
		waitElement.parentNode.removeChild(waitElement);
	}
}


/*
 *放开按钮
 *
 */
function showButton (inputName,widthStr,heightStr,fileSize) {
	var buttonElement = $("#"+ inputName + "A");
	buttonElement.removeClass();
	buttonElement.addClass("btnUpload");
	var appendStr = "<input name=\"" + inputName + "InputFile\" id=\"" + inputName + "InputFile\" type=\"file\">上传图片";
	$("#"+ inputName + "A").html(appendStr);
	ajaxChosePic(inputName,widthStr,heightStr,fileSize);
}

/*
 *按钮变灰
 *
 */
function hideButton (inputName) {
	var buttonElement = $("#"+ inputName + "A");
	buttonElement.removeClass();
	buttonElement.addClass("btnUploadNo");
	var inputElement = document.getElementById(inputName + "InputFile");
	if(inputElement!=null){
		inputElement.parentNode.removeChild(inputElement);
	}
}

/*
 *显示移除的栏目
 *
 */
function showRemove (inputName,wightStr,heightStr,fileSize) {
	var purviewElement = $("#"+ inputName + "Purview");
	var appendStr = "<span class=\"bottomBg\" id=\"" + inputName + "RemoveSpan\"></span>";
	appendStr = appendStr + "<a class=\"imgRemove\" href=\"javascript:removePic('" + inputName +"','" + wightStr + "','" + heightStr + "','" + fileSize + "')\" id=\"" + inputName + "RemoveA\"></a>";
	purviewElement.append(appendStr);
	hoverImgBind(inputName);
}

/*
 *去掉移除的栏目
 *
 */
function hideRemove (inputName) {
	var spanElement = document.getElementById(inputName + "RemoveSpan");
	var aElement = document.getElementById(inputName + "RemoveA");
	if(spanElement!=null){
		spanElement.parentNode.removeChild(spanElement);
	}
	if(aElement!=null){
		aElement.parentNode.removeChild(aElement);
	}
}


/**
 * 绑定已上传图片悬停事件
 */
var hoverImgBind = function(inputName) {
    $(".imgWindow").hover(
		function(){
			$(this).children(".bottomBg").show();
			$(this).children(".imgRemove").show();
		},function(){
			$(this).children(".bottomBg").hide();
			$(this).children(".imgRemove").hide();
		}
	);
};



function isStartWith(act,act2){
	if(act == '' || act2== '' || !act || !act2){
		return false;
	}
	var firstStr=act.substr(0,act2.length);
	if(firstStr == act2){
		return true;
	}
	return false;
}

function hasChoseFile(act){
	var actValue = "";
	var oldPath = "";
	
	if(document.getElementById(act+"OldUrlPath")){
		oldPath = document.getElementsByName(act+"OldUrlPath")[0].value;
	}

	if(document.getElementById(act+'FileUrl')){
		actValue = document.getElementsByName(act+'FileUrl')[0].value;
	}
  
	if(actValue != '' && actValue != ' ' && actValue != null && actValue != "null"){
			return true;
	}else{
		var isRemove =  "";
		if(document.getElementById(act + "remove")){
			isRemove = document.getElementsByName(act + "remove")[0].value;
		}
		if(oldPath != '' && oldPath != ' ' && oldPath != null && oldPath != "null" && "true" != isRemove){
			return true;
		}else{
			return false;
		}
	}
	return false;
}


/***********************************************
*
*文件上传开始
*
***********************************************/

/*
 *放开按钮
 *
 */
function showFileButton (inputName,fileType) {
	var buttonElement = $("#"+ inputName + "A");
	buttonElement.removeClass();
	buttonElement.addClass("btnUpload");
	var appendStr = "<input name=\"" + inputName + "InputFile\" id=\"" + inputName + "InputFile\" type=\"file\">上传文件";
	$("#"+ inputName + "A").html(appendStr);
	ajaxChoseFile(inputName,fileType);
}
/*
 *按钮变灰
 *
 */
function hideFileButton (inputName) {
	var buttonElement = $("#"+ inputName + "A");
	buttonElement.removeClass();
	buttonElement.addClass("btnUploadNo");
	var inputElement = document.getElementById(inputName + "InputFile");
	if(inputElement!=null){
		inputElement.parentNode.removeChild(inputElement);
	}
}

/*
 *显示上传等待的框
 *
 */
function showFileWait (inputName) {
	$("#"+ inputName + "Loading").show();
}
/*
 *显示上传等待的框
 *
 */
function hideFileWait (inputName) {
	$("#"+ inputName + "Loading").hide();
}
/**
 * 初始化名称
 */
function initFileName(inputName,fileNameStr,filePurviewUrl){
	var purviewElement = $("#"+ inputName + "PurviewA");
	purviewElement.attr("href",filePurviewUrl);
	purviewElement.html(fileNameStr);
}

function showFileDel(inputName){
	$("#"+ inputName + "PurviewDel").show();
}

function hideFileDel(inputName){
	$("#"+ inputName + "PurviewDel").hide();
}

var ajaxChoseFile = function(inputName,fileType){
	var isRemove = $("#" + inputName + "remove").val();
	new AjaxUpload(inputName+"InputFile",{
		action:contentPath+"/upload/fileUpload/"+inputName,  
		autoSubmit:true,
		responseType:"json",
		name:inputName+"InputFile",
		//提交文件时的操作
		onSubmit:function(file, extension){
			var typeError = true;
			if("pdf" == fileType && extension && !/^(pdf|PDF)$/.test(extension)){
					typeError = false;
			}
			if("zip" == fileType && extension && !/^(zip|ZIP)$/.test(extension)){
					typeError = false;
			}
			if("excel" == fileType && extension && !/^(xls|xlsx|XLS|XLSX)$/.test(extension)){
					typeError = false;
			}
			if("word" == fileType && extension && !/^(doc|docx|DOC|DOCX)$/.test(extension)){
					typeError = false;
			}
			if("ppt" == fileType && extension && !/^(ppt|PPT|pptx|PPTX)$/.test(extension)){
					typeError = false;
			}
			if(typeError){
				showFileWait(inputName);
				hideFileButton(inputName);
			}else{
				alert("你所选择的文件不受系统支持");
				hideFileWait(inputName);
				showFileButton(inputName,fileType);
				return false;	
			}
		},
		
		//文件提交完成后返回的操作
		onComplete: function(file, response){
			var dataobj=response;
			var errorMessage = dataobj.errorMessage;
			if(errorMessage != '' &&  errorMessage != ' '){
				hideFileWait(inputName);
				showFileButton(inputName,fileType);
				alert(errorMessage);
				document.getElementsByName(inputName+"FileUrl")[0].value="";
			}else{
				var fileUrl = dataobj.fileUrl;
				var fileNameStr = dataobj.fileName;
				var purviewPath = dataobj.purviewPath;
					// 打印
					initFileName(inputName,fileNameStr,purviewPath);
					document.getElementsByName(inputName+"FileUrl")[0].value=fileUrl;
					hideFileWait(inputName);
					hideFileButton(inputName);
					showFileDel(inputName);
			}
		}
	});
};

function removeFile(inputName,fileType){
	var filePath = document.getElementsByName(inputName+"FileUrl")[0].value;
	if(filePath == "" || filePath.length <= 0){
		removeInit(inputName,fileType);
		return;
	}
	var url = contentPath+"/upload/fileRemove";
	$.getJSON(url ,{filePath:filePath,ranNum:Math.random()} ,function(data){
		var result = data ;
		if(result.remove == "true" || result.remove == true){
			removeInit(inputName,fileType);
		}
	}) ;
}

function removeInit(inputName,fileType){
	$("#" + inputName + "remove").val(true);
	document.getElementsByName(inputName+"FileUrl")[0].value="";
	hideFileWait(inputName);
	hideFileDel(inputName);
	showFileButton(inputName,fileType);
	
	var typeName = "请选择需要上传的文件！";
	if("pdf" == fileType){
		typeName = "请选择需要上传的PDF文档！";
	}
	if("zip" == fileType){
		typeName = "请选择需要上传的压缩包！";
	}
	if("excel" == fileType){
		typeName = "请选择需要上传的EXCEL文档！";
	}
	if("word" == fileType){
		typeName = "请选择需要上传的WORD文档！";
	}
	if("ppt" == fileType){
		typeName = "请选择需要上传的PDF文档！";
	}
	
	initFileName(inputName,typeName,"javascript:void(0);");	
}
