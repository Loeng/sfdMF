
	/*文本框只能输入数字和小数点 - 小数点仅一位   引用方法：onkeyup="javascript:clearNoNum(this);"*/
	function clearNoNum(obj){
		if(obj.value == null || obj.value == ""){
			//obj.value=0;
			//return;	
		}
		
		//先把非数字的都替换掉，除了数字和.
		obj.value = obj.value.replace(/[^\d.-]/g,"");
		//必须保证第一个为数字而不是.
		obj.value = obj.value.replace(/^\./g,"");
		//保证只有出现一个.而没有多个.
		obj.value = obj.value.replace(/\.{2,}/g,".");
		//保证.只出现一次，而不能出现两次以上
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		//保证.小数点后只能有两位
		obj.value = obj.value.replace(/([0-9]+\.[0-9]{2})[0-9]*/,"$1");
		
	}
	
	/** 前台成功操作弹出提示框JS* */
	function salert(textStr){  
		// 背景遮挡
		var div = document.createElement("div");  
		div.setAttribute("id","ekfansDiv"); 
		div.className = "alertLayerBg";
		
		// 提示框
		var myDialog = document.createElement("div");   
		myDialog.setAttribute("id","ekfansAlert");   
		myDialog.className = "alertLayer";  
		
		// 头部
		var head = document.createElement("div"); 
		var span = document.createElement("span");
		head.setAttribute("id","alertHead");
		span.setAttribute("id","apClose");
		head.className = "tit";
		span.className = "apClose";
		head.appendChild(span);
		
		var headElementHtmlContent = document.createTextNode("系统提示");
		head.appendChild(headElementHtmlContent);
		
		// 提示信息区域
		var content = document.createElement("div");
		content.className = "alertContent sucFailLayer";
		
		// 图标
		var em = document.createElement("em");
		em.className = "sucIco";
		var span1 = document.createElement("span");
		
		// 提示信息
		span1.innerHTML = textStr;
		
		//按钮
		var btnHtml = document.createElement("div");
		btnHtml.className = "btn";
		btnHtml.innerHTML = '<a class="btnSure" href="javascript:cok();">确定</a>';
		
		content.appendChild(em);
		content.appendChild(span1);
		
		
		
		myDialog.appendChild(head);
		myDialog.appendChild(content);
		myDialog.appendChild(btnHtml);
		
		
		document.body.appendChild(div);
		document.body.appendChild(myDialog);
		// 居中
		var kuan=$(".alertLayer").outerWidth();
		var gao=$(".alertLayer").outerHeight();
		var top=(window.innerHeight-gao)/2;
		$(".alertLayer").css("margin-left",-kuan/2+"px");
		$(".alertLayer").css("top",top+"px");
		
		
	}  
	
	function salertWithFunction(textStr,callback,params){
		// 背景遮挡
		var div = document.createElement("div");  
		div.setAttribute("id","ekfansDiv"); 
		div.className = "alertLayerBg";
		
		// 提示框
		var myDialog = document.createElement("div");   
		myDialog.setAttribute("id","ekfansAlert");   
		myDialog.className = "alertLayer";  
		
		// 头部
		var head = document.createElement("div"); 
		var span = document.createElement("span");
		head.setAttribute("id","alertHead");
		span.setAttribute("id","alertClose");
		head.className = "tit";
		span.className = "apClose";
		head.appendChild(span);
		
		var headElementHtmlContent = document.createTextNode("系统提示");
		head.appendChild(headElementHtmlContent);
		
		// 提示信息区域
		var content = document.createElement("div");
		content.className = "alertContent sucFailLayer";
		
		// 图标
		var em = document.createElement("em");
		em.className = "sucIco";
		var span1 = document.createElement("span");
		
		//按钮
		var btnHtml = document.createElement("div");
		btnHtml.className = "btn";
		btnHtml.innerHTML = '<a class="btnSure" href="javascript:ok();">确定</a>';
		
		
		// 提示信息
		span1.innerHTML = textStr;
		content.appendChild(em);
		content.appendChild(span1);
		
		myDialog.appendChild(head);
		myDialog.appendChild(content);
		myDialog.appendChild(btnHtml);
		
		document.body.appendChild(div);
		document.body.appendChild(myDialog);
		// 居中
		var kuan=$(".alertLayer").outerWidth();
		var gao=$(".alertLayer").outerHeight();
		var top=(window.innerHeight-gao)/2;
		$(".alertLayer").css("margin-left",-kuan/2+"px");
		$(".alertLayer").css("top",top+"px");
		
		$('#alertClose').click(function(){
			document.body.removeChild(div);
			document.body.removeChild(document.getElementById('ekfansAlert'));
			if(callback != null && callback != ""){
				if(params == null || params.length<=0){
					callback(params);
				}else{
					callback.apply(this,params);
				}
			}
		})
		
		this.ok = function(){
			document.body.removeChild(div);
			document.body.removeChild(document.getElementById('ekfansAlert'));
			if(callback != null && callback != ""){
				if(params == null || params.length<=0){
					callback(params);
				}else{
					callback.apply(this,params);
				}
			}
		}
	}
	
	$(function(){
		$('#apClose').live("click",function(){
			closeDialog();
			})
		$('#apClose2').live("click",function(){
			document.body.removeChild(document.getElementById('ekfansDiv'));
			document.body.removeChild(document.getElementById('ekfansConfirm'));
			})	
		});
		
	/** 前台失败操作弹出提示框JS* */
	function falert(textStr){  
		// 背景遮挡
		var div = document.createElement("div");  
		div.setAttribute("id","ekfansDiv"); 
		div.className = "alertLayerBg";
		
		// 提示框
		var myDialog = document.createElement("div");  		 
		myDialog.setAttribute("id","ekfansAlert");   
		myDialog.className = "alertLayer";  
		
		// 头部
		var head = document.createElement("div"); 
		var span = document.createElement("span");
		head.setAttribute("id","alertHead");
		span.setAttribute("id","apClose");
		head.className = "tit";
		span.className = "apClose";
		head.appendChild(span);
		
		var headElementHtmlContent = document.createTextNode("系统提示");
		head.appendChild(headElementHtmlContent);
		
		// 提示信息区域
		var content = document.createElement("div");
		content.className = "alertContent sucFailLayer";
		
		// 图标
		var em = document.createElement("em");
		em.className = "failIco";
		var span1 = document.createElement("span");
		
		// 提示信息
		span1.innerHTML = textStr;
		
		//按钮
		var btnHtml = document.createElement("div");
		btnHtml.className = "btn";
		btnHtml.innerHTML = '<a class="btnSure" href="javascript:cok();">确定</a>';
		
		content.appendChild(em);
		content.appendChild(span1);
		
		// 提示信息
		
		myDialog.appendChild(head);
		myDialog.appendChild(content);
		myDialog.appendChild(btnHtml);
		
		document.body.appendChild(div);
		document.body.appendChild(myDialog);
		
		// 居中
		var kuan=$(".alertLayer").outerWidth();
		var gao=$(".alertLayer").outerHeight();
		var top=(window.innerHeight-gao)/2;
		$(".alertLayer").css("margin-left",-kuan/2+"px");
		$(".alertLayer").css("top",top+"px");
		
		
	}  
	
	function falertWithFunction(textStr,callback,params){
		// 背景遮挡
		var div = document.createElement("div");  
		div.setAttribute("id","ekfansDiv"); 
		div.className = "alertLayerBg";
		
		// 提示框
		var myDialog = document.createElement("div");   
		myDialog.setAttribute("id","ekfansAlert");   
		myDialog.className = "alertLayer";  
		
		// 头部
		var head = document.createElement("div"); 
		var span = document.createElement("span");
		head.setAttribute("id","alertHead");
		span.setAttribute("id","alertClose");
		head.className = "tit";
		span.className = "apClose";
		head.appendChild(span);
		
		var headElementHtmlContent = document.createTextNode("系统提示");
		head.appendChild(headElementHtmlContent);
		
		// 提示信息区域
		var content = document.createElement("div");
		content.className = "alertContent sucFailLayer";
		
		// 图标
		var em = document.createElement("em");
		em.className = "failIco";
		var span1 = document.createElement("span");
		
		//按钮
		var btnHtml = document.createElement("div");
		btnHtml.className = "btn";
		btnHtml.innerHTML = '<a class="btnSure" href="javascript:ok();">确定</a>';
		
		
		// 提示信息
		span1.innerHTML = textStr;
		content.appendChild(em);
		content.appendChild(span1);
		
		myDialog.appendChild(head);
		myDialog.appendChild(content);
		myDialog.appendChild(btnHtml);
		
		document.body.appendChild(div);
		document.body.appendChild(myDialog);
		// 居中
		var kuan=$(".alertLayer").outerWidth();
		var gao=$(".alertLayer").outerHeight();
		var top=(window.innerHeight-gao)/2;
		$(".alertLayer").css("margin-left",-kuan/2+"px");
		$(".alertLayer").css("top",top+"px");
		
		$('#alertClose').click(function(){
			document.body.removeChild(div);
			document.body.removeChild(document.getElementById('ekfansAlert'));
			if(callback != null && callback != ""){
				if(params == null || params.length<=0){
					callback(params);
				}else{
					callback.apply(this,params);
				}
			}
		})
		
		this.ok = function(){
			document.body.removeChild(div);
			document.body.removeChild(document.getElementById('ekfansAlert'));
			if(callback != null && callback != ""){
				if(params == null || params.length<=0){
					callback(params);
				}else{
					callback.apply(this,params);
				}
			}
		}
	}
	
	function econfirm(textStr,callback,params,cancelCallback,cancelParams){
		var div = document.createElement("div");  
		div.className = "confirmBg";  
		div.setAttribute("id","ekfansDiv");  
		var myDialog = document.createElement("div");   
		myDialog.setAttribute("id","ekfansConfirm");   
		myDialog.className = "confirm";  
		
		var head = document.createElement("div"); 
		var span = document.createElement("span");
		head.setAttribute("id","confirmHead");
		span.setAttribute("id","apClose2");
		head.className = "tit";
		span.className = "apClose";
		head.appendChild(span);
		
		var headElementHtmlContent = document.createTextNode("系统提示");
		head.appendChild(headElementHtmlContent);
		var content=document.createElement("div");
		content.className = "confirmContent";
		var elementHtmlContent = document.createTextNode(textStr);
		content.appendChild(elementHtmlContent);
		var buttonElement = document.createElement("div");
		buttonElement.className = "btns";
		
		var fdElement = document.createElement("a");
		fdElement.setAttribute("id","quxiao");
		fdElement.className = "btn";
		fdElement.href = "javascript:cancel();";
		var fdElementHtmlContent = document.createTextNode("取消");
		fdElement.appendChild(fdElementHtmlContent);
		
		var qdElement = document.createElement("a");
		qdElement.setAttribute("id","queding");
		qdElement.className = "btn";
		qdElement.href = "javascript:ok();";
		var qdElementHtmlContent = document.createTextNode("确定");
		qdElement.appendChild(qdElementHtmlContent);
		buttonElement.appendChild(qdElement);
		buttonElement.appendChild(fdElement);
		
		
		myDialog.appendChild(head);
		myDialog.appendChild(content);
		myDialog.appendChild(buttonElement);
		document.body.appendChild(div);
		document.body.appendChild(myDialog);
		
		var kuan2=$(".confirm").outerWidth();
		var gao2=$(".confirm").outerHeight();
		var top2=(window.innerHeight-gao2)/2;
		$(".confirm").css("margin-left",-kuan2/2+"px");
		$(".confirm").css("top",top2+"px");
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
		}
		
	}

	function closeDialog(){ 
		document.body.removeChild(document.getElementById('ekfansDiv'));
		document.body.removeChild(document.getElementById('ekfansAlert'));  
	} 
	
	function cok(){ 
		closeDialog();
	}
	
	// CharMode函数
	// 测试某个字符是属于哪一类.
	function CharMode(iN){
		if (iN>=48 && iN <=57) // 数字
			return 1; 
		if (iN>=65 && iN <=90) // 大写字母
			return 2; 
		if (iN>=97 && iN <=122) // 小写
			return 4; 
		else 
			return 8; // 特殊字符
	} 
	// bitTotal函数
	// 计算出当前密码当中一共有多少种模式
	function bitTotal(num){ 
		modes=0; 
		for (i=0;i<4;i++){ 
		if (num & 1) modes++; 
			num>>>=1; 
		} 
		return modes; 
	} 
	// checkStrong函数
	// 返回密码的强度级别
	function checkStrong(sPW){
		if (sPW.length<=4) 
		return 0; // 密码太短
		Modes=0; 
		for (i=0;i<sPW.length;i++){ 
			// 测试每一个字符的类别并统计一共有多少种模式.
			Modes|=CharMode(sPW.charCodeAt(i)); 
		} 
		return bitTotal(Modes); 
	}
	// 去除输入框前后空格
	function trim(str){
		if(str.trim()==""){
			return false;
		}else{
			return true;
		}
	}
	
	function trimStr(str){ //删除左右两端的空格
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}
	
	// 验证手机号码
	function checkMobile(str){
		var myreg = /1[0-9]{10}/;
		if(!myreg.test(str)){
			return false;
		}else{
			return true;
		}
	}	
	
	function goLogin(channelType,webroot){
		if("0" == channelType){
			location.href= webroot + "/web/login/0";
		}else{
			location.href=webroot + "/web/login/2";
		}
		
	}

	//判断输入密码的类型  
	function CharMode(iN){  
		if (iN>=48 && iN <=57) //数字  
	return 1;  
		if (iN>=65 && iN <=90) //大写  
	return 2;  
		if (iN>=97 && iN <=122) //小写  
	return 4;  
		else  
	return 8;   
	}  
	//bitTotal函数  
	//计算密码模式  
	function bitTotal(num){  
		modes=0;  
	for (i=0;i<4;i++){  
		if (num & 1) modes++;  
			num>>>=1;  
		}  
	return modes;  
	}  
	//返回强度级别  
	function checkStrong(sPW){  
	if (sPW.length<=4)  
		return 0; //密码太短  
		Modes=0;  
		for (i=0;i<sPW.length;i++){  
			//密码模式  
			Modes|=CharMode(sPW.charCodeAt(i));  
		}  
		return bitTotal(Modes);  
	}  
	  
	//加载验证密码强度
	function pwdStrength(pwd){  
		S_level=checkStrong(pwd);  
		switch(S_level) {  
		case 0:  
			return 1;
		case 1:  
			return 2;
			break;  
		case 2:  
			return 3;
			break;  
	     }  
	}  
	
	