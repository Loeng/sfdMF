window.onload = function(){
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
}
String.prototype.replaceAll = function (str1,str2){
	  var str = this;     
	  var result = str.replace(eval("/"+str1+"/gi"),str2);
	  return result;
	}
	
	/*文本框只能输入数字和小数点 - 小数点仅一位   引用方法：onkeyup="javascript:clearNoNum(this);"*/
function clearNoNum(obj){
	if(obj.value == null || obj.value == ""){
		//obj.value=0;
		//return;	
	}
	
	//先把非数字的都替换掉，除了数字和.
	obj.value = obj.value.replace(/[^\d.]/g,"");
	//必须保证第一个为数字而不是.
	obj.value = obj.value.replace(/^\./g,"");
	//保证只有出现一个.而没有多个.
	obj.value = obj.value.replace(/\.{2,}/g,".");
	//保证.只出现一次，而不能出现两次以上
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	//保证.小数点后只能有两位
	obj.value = obj.value.replace(/([0-9]+\.[0-9]{2})[0-9]*/,"$1");
	
}

	

function getContextPath() {
		var contextPath = document.location.pathname;
		var index =contextPath.substr(1).indexOf("/");
		contextPath = contextPath.substr(0,index+1);
		delete index;
		if(contextPath == "/system" || contextPath == "/company" || contextPath == "/user"){
			contextPath = "";	
		}
		return contextPath;
	}
	
	/**后台弹出提示框JS**/
	function ealert(textStr){  
		var div = document.createElement("div");  
		div.className = "del_tcBg";
		div.setAttribute("id","ekfansDiv"); 
		var myDialog = document.createElement("div");   
		myDialog.setAttribute("id","ekfansAlert");   
		myDialog.className = "del_tc hintApLayer";  
		
		var head = document.createElement("div"); 
		head.setAttribute("id","alertHead");
		head.className = "tit";
		var headElementHtmlContent = document.createTextNode("提示信息");
		head.appendChild(headElementHtmlContent);
		var content=document.createElement("div");
		content.className = "aplayerContent";
		var p=document.createElement("p");
		var pElementHtmlContent = document.createTextNode(textStr);
		p.appendChild(pElementHtmlContent);
		var buttonElement = document.createElement("div");
		buttonElement.className = "btn";
		var qdElement = document.createElement("a");
		qdElement.className = "btn";
		qdElement.href = "javascript:closeDialog();";
		var qdElementHtmlContent = document.createTextNode("确定");
		qdElement.appendChild(qdElementHtmlContent);
		buttonElement.appendChild(qdElement);
		myDialog.appendChild(head);
		content.appendChild(p);
		content.appendChild(buttonElement);
		myDialog.appendChild(content);
		document.body.appendChild(div);
		document.body.appendChild(myDialog);
	}  
	
	/**后台弹出提示框JS**/
	function ekalert(titleStr,textStr){  
		var div = document.createElement("div"); 
		div.setAttribute("id","ekfansDiv");
		div.className = "del_tcBg";
		var myDialog = document.createElement("div");   
		myDialog.setAttribute("id","ekfansAlert");   
		myDialog.className = "del_tc hintApLayer";  
		
		var head = document.createElement("div");  
		head.className = "tit";
		/**
		var headSpan = document.createElement("span");  
		var headImg = document.createElement("img"); 
		headImg.src="/system/images/del3.jpg";
		headSpan.appendChild(headImg);
		head.appendChild(headSpan);
		**/
		var headElementHtmlContent = document.createTextNode(titleStr);
		head.appendChild(headElementHtmlContent);
		var content=document.createElement("div");
		content.className = "aplayerContent";
		var p=document.createElement("p");
		var pElementHtmlContent = document.createTextNode(textStr);
		p.appendChild(pElementHtmlContent);
		var buttonElement = document.createElement("div");
		buttonElement.className = "btn";
		var qdElement = document.createElement("a");
		qdElement.className = "btn";
		qdElement.href = "javascript:closeDialog();";
		var qdElementHtmlContent = document.createTextNode("确定");
		qdElement.appendChild(qdElementHtmlContent);
		buttonElement.appendChild(qdElement);
		myDialog.appendChild(head);
		content.appendChild(p);
		content.appendChild(buttonElement);
		myDialog.appendChild(content);
		document.body.appendChild(myDialog);
	}  
	
//	/**后台弹出提示框JS**/
//	function ekalert(titleStr,textStr){  
//		var div = document.createElement("div");  
//		div.className = "del_tcBg";
//		var myDialog = document.createElement("div");   
//		myDialog.setAttribute("id","ekfansAlert");   
//		myDialog.className = "del_tc";  
//		
//		var head = document.createElement("div");  
//		head.className = "tit";
//		var headElementHtmlContent = document.createTextNode(titleStr);
//		head.appendChild(headElementHtmlContent);
//		var p=document.createElement("p");
//		var pElementHtmlContent = document.createTextNode(textStr);
//		p.appendChild(pElementHtmlContent);
//		var buttonElement = document.createElement("div");
//		buttonElement.className = "btn";
//		var qdElement = document.createElement("a");
//		qdElement.href = "javascript:closeDialog();";
//		var qdElementHtmlContent = document.createTextNode("确定");
//		qdElement.appendChild(qdElementHtmlContent);
//		buttonElement.appendChild(qdElement);
//		myDialog.appendChild(head);
//		myDialog.appendChild(p);
//		myDialog.appendChild(buttonElement);
//		document.body.appendChild(div);
//		document.body.appendChild(myDialog);
//	}
	
	function econfirm(textStr,callback,params,cancelCallback,cancelParams){
		var div = document.createElement("div");  
		div.className = "del_tcBg";    
		var myDialog = document.createElement("div");   
		myDialog.setAttribute("id","ekfansConfirm");   
		myDialog.className = "del_tc hintApLayer";  
		
		var head = document.createElement("div"); 
		head.setAttribute("id","confirmHead");
		head.className = "tit";
		var headSpan = document.createElement("span"); 
		headSpan.className = "apClose";
		var headImg = document.createElement("img"); 
		headImg.src=getContextPath() + "/resources/system/images/del3.jpg";
		//headImg.src = "/resources/system/images/del3.jpg";
		headImg.setAttribute("onclick","javascript:closeFirm();"); 
		headSpan.appendChild(headImg);
		head.appendChild(headSpan);
		var headElementHtmlContent = document.createTextNode("系统提示");
		head.appendChild(headElementHtmlContent);
		var content=document.createElement("div");
		content.className = "aplayerContent";
		var p=document.createElement("p");
		var pElementHtmlContent = document.createTextNode(textStr);
		p.appendChild(pElementHtmlContent);
		var buttonElement = document.createElement("div");
		buttonElement.className = "btn";
		
		var fdElement = document.createElement("a");
		fdElement.setAttribute("id","quxiao");
		fdElement.className = "btn";
		fdElement.href = "javascript:cancel();";
		var fdElementHtmlContent = document.createTextNode("取消");
		fdElement.appendChild(fdElementHtmlContent);
		buttonElement.appendChild(fdElement);
		var qdElement = document.createElement("a");
		qdElement.setAttribute("id","queding");
		qdElement.className = "btn";
		qdElement.href = "javascript:ok();";
		var qdElementHtmlContent = document.createTextNode("确定");
		qdElement.appendChild(qdElementHtmlContent);
		buttonElement.appendChild(qdElement);
		
		myDialog.appendChild(head);
		content.appendChild(p);
		content.appendChild(buttonElement);
		myDialog.appendChild(content);
		document.body.appendChild(div);
		document.body.appendChild(myDialog);
		this.ok = function () {
			document.body.removeChild(div);
			document.body.removeChild(document.getElementById('ekfansConfirm'));
			if(callback != null && callback != ""){
				if(params == null || params.length<=0){
					callback(params);
				}else{
					callback.apply(this,params);
				}
			}
			$("body").css("overflow-y","auto");
		}
		this.cancel = function () {
			document.body.removeChild(div);
			document.body.removeChild(document.getElementById('ekfansConfirm'));
			if(cancelCallback != null && cancelCallback != ""){
				if(cancelParams == null || cancelParams.length<=0){
					cancelCallback(cancelParams);
				}else{
					cancelCallback.apply(this,cancelParams);
				}
			}
			$("body").css("overflow-y","auto");
		}
		
		this.closeFirm = function () {
			document.body.removeChild(div);
			document.body.removeChild(document.getElementById('ekfansConfirm'));
			$("body").css("overflow-y","auto");
		}
	}

	function closeDialog(){ 
		document.body.removeChild(document.getElementById('ekfansDiv'));
		document.body.removeChild(document.getElementById('ekfansAlert')); 
		$("body").css("overflow-y","auto"); 
	}  
	
	//CharMode函数 
	//测试某个字符是属于哪一类. 
	function CharMode(iN){
		if (iN>=48 && iN <=57) //数字 
			return 1; 
		if (iN>=65 && iN <=90) //大写字母 
			return 2; 
		if (iN>=97 && iN <=122) //小写 
			return 4; 
		else 
			return 8; //特殊字符 
	} 
	//bitTotal函数 
	//计算出当前密码当中一共有多少种模式 
	function bitTotal(num){ 
		modes=0; 
		for (i=0;i<4;i++){ 
		if (num & 1) modes++; 
			num>>>=1; 
		} 
		return modes; 
	} 
	//checkStrong函数 
	//返回密码的强度级别 
	function checkStrong(sPW){
		if (sPW.length<=4) 
		return 0; //密码太短 
		Modes=0; 
		for (i=0;i<sPW.length;i++){ 
			//测试每一个字符的类别并统计一共有多少种模式. 
			Modes|=CharMode(sPW.charCodeAt(i)); 
		} 
		return bitTotal(Modes); 
	}
	//去除输入框前后空格
	function trim(str){
		if($.trim(str)==""){
			return false;
		}else{
			return true;
		}
	}
	//验证手机号码
	function checkMobile(str){
		var myreg = /1[0-9]{10}/;
		if(!myreg.test(str)){
			return false;
		}else{
			return true;
		}
	}
	
	/*提交表单末尾清除浮动*/
	$(document).ready(function(e) {
        $("form").append("<div class=\"clear\"></div>");
    });