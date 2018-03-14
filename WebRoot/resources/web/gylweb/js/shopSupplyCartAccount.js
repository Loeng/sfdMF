$(document).ready(function() {
	
	/*load出的确认地址页面*/
	$("#inMain").load($("#ctxpath").val()+"/purchase/shopCart/supplyLoad",function() {
	
	/*促销*/
	$(".promotion").click(function(){
		$(this).toggleClass("promotionClick");
	})
	$(".promotion dd span").click(function(){
		var text=$(this).text();
		$(this).parents(".promotion").children("dt").html(text+"<em class=\"promotionIco\"></em>");
	})
	
	 /*新增/编辑收货地址*/
	 $(".cancel,.closeAddress").click(function(){
		 $(this).parents(".address").hide();
	 })
	 $(".btnNew").click(function(){
		 $(".address").show();
	 })
	
	/*优惠券及发票*/
	var a=$(".invoice .blue,.coupon .blue")
	a.click(function(){
			$(".orderDown").hide();
			$(this).next(".orderDown").show();
		}
	)
	
	$(".layerClose").click(
		function(){
			$(this).parent().hide();
		}
	)
	
		
		/*地区选择*/
		$(".area_two label").click(function(){
			$(this).addClass("xz_label").siblings().removeClass("xz_label");
			//$(this).nextAll().slideToggle();		
		});
		$(".area_three b").click(function(){
			$(this).parents("div.gg_area").hide();
			$(this).parents("li").removeClass("xz_li");
			var county=$(this).text();
			var city=$(this).parents("label").children("strong").text();
			var province=$(this).parents("li").children("span").text();
			$(this).parents(".formRight").children(".siteBox").attr("value",province+" "+city+" "+county);
		});
		$(".area_box ul li").hover(function(){
			$(this).addClass("xz_li");
			},function(){
				$(this).removeClass("xz_li");	
				$(this).children().children("label").removeClass("xz_label");	
		});
		/*地区选择展开隐藏*/
		$(".siteBox").focus(function(){
			$(this).next(".gg_area").slideDown(100);
		})
		
		 /*新增/编辑收货地址*/
		 $(".cancel").click(function(){
			 $(this).parents("#addressAddDiv").hide();
			 
			 
			 var temvals=$("#pca").val();
	 			    		for(var i=0;i<temvals.length;i++){
	 			    			if(i==0){
	 			    				$("#uaProvincial").val(temvals[i]);
	 			    			}else if(i==2){
	 			    				$("#uaCity").val(temvals[i]);
	 			    			}else if(i==3){
	 			    				$("#uaArea").val(temvals[i]);
	 			    			}
	 			    		}
	 			    		$("#uaAddress").val($("#address").val());
	 			    		$("#uaZipcode").val($("#zipcode").val());
	 			    		$("#uaMobile").val($("#mobile").val());
	 			    		$("#uaName").val($("#name").val());
			 
			 
			 
			 $('#name').val("");
				$('#zipcode').val("");
				$('#mobile').val("");
				$('#email').val("");
				$('#address').val("");
				$('#pca').val("");
				document.getElementById("nameMsgSpan").className = "";
				document.getElementById("nameMsg").className = "";
				document.getElementById("nameMsg").innerHTML = "";
				document.getElementById("zipcodeMsgSpan").className = "";
				document.getElementById("zipcodeMsg").className = "";
				document.getElementById("zipcodeMsg").innerHTML = "";
				document.getElementById("mobileMsgSpan").className = "";
				document.getElementById("mobileMsg").className = "";
				document.getElementById("mobileMsg").innerHTML = "";
				document.getElementById("emailMsgSpan").className = "";
				document.getElementById("emailMsg").className = "";
				document.getElementById("emailMsg").innerHTML = "";
				document.getElementById("addressMsgSpan").className = "";
				document.getElementById("addressMsg").className = "";
				document.getElementById("addressMsg").innerHTML = "";
		 })
		 $(".btnNew").click(function(){
		 		$("#addressAddDiv").hide();
		 		$("#addressUpdateDiv").hide();
			 $("#addressAddDiv").show();
		 })
		
		/*优惠券及发票*/
		var a=$(".invoice .blue,.coupon .blue")
		a.click(function(){
			var judg=$("#invoiceInfoContent").css("display");
			if(judg=="none"){
				var obj=document.getElementById("RadioGroup1_0");
				$(obj).attr("checked",true);
			}
			$(".orderDown").hide();
			$(this).next(".orderDown").show();
		}
		)
		
		$(".layerClose").click(
			function(){
				$(this).parent().hide();
			}
		)
	});
	
	
});
//搜索商品或店铺
function searchProduct(ctxpath){
	var productFuzzyName = $("#productFuzzyName").val();
	if(productFuzzyName.trim()==""){
		$("#productFuzzyName").val("请输入条件");
		return;
	}
	var searchType = document.getElementById("searchType").innerHTML;
	if(productFuzzyName != "请输入条件"){
	if(searchType=='商品'){
		document.write("<form action='"+ctxpath+"/web/product/getProduct' method='post' name='formhjc' style='display:none'>");
		document.write("<input type='hidden' name='productName' value='"+productFuzzyName+"' />");
		document.write("</form>");
		document.formhjc.submit();
	}else if(searchType=='店铺'){
		document.write("<form action='"+ctxpath+"/web/store/getMateStore' method='post' name='formhjc' style='display:none'>");
		document.write("<input type='hidden' name='storeName' value='"+productFuzzyName+"' />");
		document.write("</form>");
		document.formhjc.submit();
	}
	}
}

//根据分类搜索商品
function searchProductHuangrong(ctxpath,categoryId){
	document.write("<form action='"+ctxpath+"/web/product/getProduct' method='post' name='formhjc' style='display:none'>");
	document.write("<input type='hidden' name='categoryId' value='"+categoryId+"' />");
	document.write("</form>");
	document.formhjc.submit();
}

function checkInvoiceTitleFocus(){
	$("#invoiceTitlePromtMessage").hide();
}

function checkInvoiceTitleBlur(obj){
	var vals=$(obj).val().trim();
	if(vals==""){
		$("#invoiceTitlePromtMessage").show();
		return false;
	}else{
		$("#invoiceTitlePromtMessage").hide();
		return true;
	}
}

//设置默认收货地址
function setDefaultAddress(id,ctxpath,provincial,city,area,address,zipcode,mobile,name){
	$.post(
			ctxpath+"/purchase/setDefaultAddress/"+id,
			function(date){
		    if(date){
		    	salert("设置成功");
		    	$("#adressUl").load($("#ctxpath").val()+"/purchase/adress/shopCartload",function() {
 					/*选择地址*/
 			        $(".siteList label").click(
 			    		function(){
 			    		$(".siteList li").removeClass("siteNow");
 			    		$(this).parent().addClass("siteNow");
 			    		}
 			    	)
 			    	
 			    	/*地区选择*/
 			    	$(function(){
 			    		$(".area_two label").click(function(){
 			    			$(this).addClass("xz_label").siblings().removeClass("xz_label");
 			    			//$(this).nextAll().slideToggle();		
 			    		});
 			    		$(".area_three b").click(function(){
 			    			$(this).parents("div.gg_area").hide();
 			    			$(this).parents("li").removeClass("xz_li");
 			    			var county=$(this).text();
 			    			var city=$(this).parents("label").children("strong").text();
 			    			var province=$(this).parents("li").children("span").text();
 			    			$(this).parents(".formRight").children(".siteBox").attr("value",province+" "+city+" "+county);
 			    		});
 			    		$(".area_box ul li").hover(function(){
 			    			$(this).addClass("xz_li");
 			    			},function(){
 			    				$(this).removeClass("xz_li");	
 			    				$(this).children().children("label").removeClass("xz_label");	
 			    		});
 			    	})
 			    	
 			    	/*地区选择展开隐藏*/
 			    	$(".siteBox").focus(function(){
 			    		$(this).next(".gg_area").slideDown(100);
 			    	})
 			    	
 			    	 /*新增/编辑收货地址*/
 			    	 $(".cancel").click(function(){
 			    		 $(this).parents("#addressAddDiv").hide();
 			    		 $('#name').val("");
 							$('#zipcode').val("");
 							$('#mobile').val("");
 							$('#email').val("");
 							$('#address').val("");
 							$('#pca').val("");
 							document.getElementById("nameMsgSpan").className = "";
 							document.getElementById("nameMsg").className = "";
 							document.getElementById("nameMsg").innerHTML = "";
 							document.getElementById("zipcodeMsgSpan").className = "";
 							document.getElementById("zipcodeMsg").className = "";
 							document.getElementById("zipcodeMsg").innerHTML = "";
 							document.getElementById("mobileMsgSpan").className = "";
 							document.getElementById("mobileMsg").className = "";
 							document.getElementById("mobileMsg").innerHTML = "";
 							document.getElementById("emailMsgSpan").className = "";
 							document.getElementById("emailMsg").className = "";
 							document.getElementById("emailMsg").innerHTML = "";
 							document.getElementById("addressMsgSpan").className = "";
 							document.getElementById("addressMsg").className = "";
 							document.getElementById("addressMsg").innerHTML = "";
 			    	 })
 			    	 $(".btnNew").click(function(){
 			    		 $("#addressAddDiv").show();
 			    	 })
 			    	
 			    	/*优惠券及发票*/
 			    	var a=$(".invoice .blue,.coupon .blue")
 			    	a.click(function(){
 			    			$(".orderDown").hide();
 			    			$(this).next(".orderDown").show();
 			    		}
 			    	)
 			    	
 			    	$(".layerClose").click(
 			    		function(){
 			    			$(this).parent().hide();
 			    		}
 			    	)
 				});
		    	$('#uaProvincial').val(provincial);
		    	$('#uaCity').val(city);
		    	$('#uaArea').val(area);
		    	$('#uaAddress').val(address);
		    	$('#uaZipcode').val(zipcode);
		    	$('#uaMobile').val(mobile);
		    	$('#uaName').val(name);
		    }else{
		    	falert("设置失败");
		    }
	});
}

//选择收货地址
function optOrderAdress(provincial,city,area,address,zipcode,mobile,name){
	$('#uaProvincial').val(provincial);
	$('#uaCity').val(city);
	$('#uaArea').val(area);
	$('#uaAddress').val(address);
	$('#uaZipcode').val(zipcode);
	$('#uaMobile').val(mobile);
	$('#uaName').val(name);
}

function submit(){
	document.getElementById("addshopCartPayForm").submit();
}

//添加地址表单的提交
function formSubmit(){
	if(checkNameBlur() && checkAddressBlur() && checkMobileBlurTempNew()){
		var zipcode=$("#zipcode").val().trim();
		if(zipcode!=""){
			if(!checkZipcodeBlur()){
				return;
			}
		}
		
		/*if(checkZipcodeBlur()){
			
		}*/
		
		$("#addUserAddressForm").ajaxSubmit(
		 		function(data){
		 			if(data==true){
		 				var temp=0;
		 				$(".selectFindHome").each(function(){
		 					$(this).find("option[value='']").attr("selected",true);
		 					++temp;
		 					if(temp>1){
		 						$(this).remove();
		 					}	
		 				});
		 				salert("添加成功");
		 				$("#tempInfoAddress").html("");
		 				
		 				var temvals=$("#pca").val().split(" ");
		 			 	for(var i=0;i<temvals.length;i++){
		 			  	if(i==0){
		 			    	$("#uaProvincial").val(temvals[i]);
		 			    			}else if(i==1){
		 			    				$("#uaCity").val(temvals[i]);
		 			    			}else if(i==2){
		 			    				$("#uaArea").val(temvals[i]);
		 			    			}
		 			    		}
		 			    		$("#uaAddress").val($("#address").val());
		 			    		$("#uaZipcode").val($("#zipcode").val());
		 			    		$("#uaMobile").val($("#mobile").val());
		 			    		$("#uaName").val($("#name").val());

		 				$("#adressUl").load($("#ctxpath").val()+"/purchase/adress/shopCartload",function() {
		 					/*选择地址*/
		 			        $(".siteList label").click(
		 			    		function(){
		 			    		$(".siteList li").removeClass("siteNow");
		 			    		$(this).parent().addClass("siteNow");
		 			    		}
		 			    	)
		 			    	
		 			    	/*地区选择*/
		 			    	$(function(){
		 			    		$(".area_two label").click(function(){
		 			    			$(this).addClass("xz_label").siblings().removeClass("xz_label");
		 			    			//$(this).nextAll().slideToggle();		
		 			    		});
		 			    		$(".area_three b").click(function(){
		 			    			$(this).parents("div.gg_area").hide();
		 			    			$(this).parents("li").removeClass("xz_li");
		 			    			var county=$(this).text();
		 			    			var city=$(this).parents("label").children("strong").text();
		 			    			var province=$(this).parents("li").children("span").text();
		 			    			$(this).parents(".formRight").children(".siteBox").attr("value",province+" "+city+" "+county);
		 			    		});
		 			    		$(".area_box ul li").hover(function(){
		 			    			$(this).addClass("xz_li");
		 			    			},function(){
		 			    				$(this).removeClass("xz_li");	
		 			    				$(this).children().children("label").removeClass("xz_label");	
		 			    		});
		 			    	})
		 			    	
		 			    	/*地区选择展开隐藏*/
		 			    	$(".siteBox").focus(function(){
		 			    		$(this).next(".gg_area").slideDown(100);
		 			    	})
		 			    	
		 			    	 /*新增/编辑收货地址*/
		 			    	 $(".cancel").click(function(){
		 			    		 $(this).parents("#addressAddDiv").hide();
		 			    		 
		 			    		 var temvals=$("#pca").val();
		 			    		for(var i=0;i<temvals.length;i++){
		 			    			if(i==0){
		 			    				$("#uaProvincial").val(temvals[i]);
		 			    			}else if(i==2){
		 			    				$("#uaCity").val(temvals[i]);
		 			    			}else if(i==3){
		 			    				$("#uaArea").val(temvals[i]);
		 			    			}
		 			    		}
		 			    		$("#uaAddress").val($("#address").val());
		 			    		$("#uaZipcode").val($("#zipcode").val());
		 			    		$("#uaMobile").val($("#mobile").val());
		 			    		$("#uaName").val($("#name").val());
		 			    		 
		 			    		 
		 			    		 $('#name').val("");
		 							$('#zipcode').val("");
		 							$('#mobile').val("");
		 							$('#email').val("");
		 							$('#address').val("");
		 							$('#pca').val("");
		 							document.getElementById("nameMsgSpan").className = "";
		 							document.getElementById("nameMsg").className = "";
		 							document.getElementById("nameMsg").innerHTML = "";
		 							document.getElementById("zipcodeMsgSpan").className = "";
		 							document.getElementById("zipcodeMsg").className = "";
		 							document.getElementById("zipcodeMsg").innerHTML = "";
		 							document.getElementById("mobileMsgSpan").className = "";
		 							document.getElementById("mobileMsg").className = "";
		 							document.getElementById("mobileMsg").innerHTML = "";
		 							document.getElementById("emailMsgSpan").className = "";
		 							document.getElementById("emailMsg").className = "";
		 							document.getElementById("emailMsg").innerHTML = "";
		 							document.getElementById("addressMsgSpan").className = "";
		 							document.getElementById("addressMsg").className = "";
		 							document.getElementById("addressMsg").innerHTML = "";
		 			    	 })
		 			    	 $(".btnNew").click(function(){
		 			    		 $("#addressAddDiv").show();
		 			    	 })
		 			    	
		 			    	/*优惠券及发票*/
		 			    	var a=$(".invoice .blue,.coupon .blue")
		 			    	a.click(function(){
		 			    			$(".orderDown").hide();
		 			    			$(this).next(".orderDown").show();
		 			    		}
		 			    	)
		 			    	
		 			    	$(".layerClose").click(
		 			    		function(){
		 			    			$(this).parent().hide();
		 			    		}
		 			    	)
		 				});
		 				$(".address").hide();
		 				$('#name').val("");
		 				$('#zipcode').val("");
		 				$('#mobile').val("");
		 				$('#email').val("");
		 				$('#address').val("");
		 				$('#pca').val("");
		 			}else{
		 				falert("添加失败");
		 			}
		 		});
	    	
	}else{
		checkNameBlur();
		//checkZipcodeBlur();
		//checkMobileBlur();
		//checkEmailBlur();
		checkAddressBlur();
		//checkpcaBlur()
		checkMobileBlurTempNew();
	}
}




//收货人姓名验证
function checkNameFocus(){
	//显示提示信息
    //var nameSpan = document.getElementById("nameMsgSpan");
    //nameSpan.className = "errorText";
    //var nameMsg = document.getElementById("nameMsg");
   // nameMsg.className = "errorMain";
   // nameMsg.innerHTML = "2-20字符";
   $("#nameMsgSpan").css("display","none");
}

function checkNameBlur(){
	var name = $("#name").val();
	if(name.trim() == ""){
		//document.getElementById("nameMsgSpan").className = "errorText errorRed";
		//document.getElementById("nameMsg").className = "errorMain";
		//document.getElementById("nameMsg").innerHTML = "2-20字符";
		$("#nameMsgSpan").attr("class","formError");
		$("#nameMsgSpan").css("display","inline");
		$("#nameMsgSpan").html("请您填写收货人姓名");
		return false;
	}else if(name.trim().length<2 || name.trim().length>20){
		$("#nameMsgSpan").attr("class","formError");
		$("#nameMsgSpan").css("display","inline");
		$("#nameMsgSpan").html("收货人姓名必须在2-20字符之间");
		return false;
	}else{
		//document.getElementById("nameMsgSpan").className = "";
		//document.getElementById("nameMsg").className = "";
		//document.getElementById("nameMsg").innerHTML = "";
		$("#nameMsgSpan").css("display","none");
		return true;
	}
}

function checkpcaFocus(){
	$("#areaMsg").css("display","block");
	document.getElementById("areaMsg").className = "errorText";
}

function checkpcaBlur(){
	var pca = $("#pca").val().trim();
	if(pca.length<=0){
		$("#areaMsg").css("display","block");
		document.getElementById("areaMsg").className = "errorText errorRed";
		return false;
	}else{
		$("#areaMsg").css("display","none");
		return true;
	}
	
	//$("#area").css("background","#f00");
}


// 邮政编码验证
function checkZipcodeFocus(){
	//显示提示信息
    //document.getElementById("zipcodeMsgSpan").className = "errorText";
    //document.getElementById("zipcodeMsg").className = "errorMain";
    //document.getElementById("zipcodeMsg").innerHTML = "输入正确的邮箱";
	$("#zipcodeMsg").css("display","none");
}

function checkZipcodeBlur(){
	var zipcode = $("#zipcode").val();
	//只能输入六位数字的验证
	var regx = /^[1-9][0-9]{5}$/;
	if(zipcode == "" || !regx.test(zipcode)){
		//document.getElementById("zipcodeMsgSpan").className = "errorText errorRed";
		//document.getElementById("zipcodeMsg").className = "errorMain";
		//document.getElementById("zipcodeMsg").innerHTML = "邮箱格式错误";
		$("#zipcodeMsg").css("display","inline");
		return false;
	}else{
		//document.getElementById("zipcodeMsgSpan").className = "";
		//document.getElementById("zipcodeMsg").className = "";
		//document.getElementById("zipcodeMsg").innerHTML = "";
		$("#zipcodeMsg").css("display","none");
		return true;
	}
}

// 手机号码验证
function checkMobileFocus(){
	//显示提示信息
    document.getElementById("mobileMsgSpan").className = "errorText";
    document.getElementById("mobileMsg").className = "errorMain";
    document.getElementById("mobileMsg").innerHTML = "请输入正确的手机号码";
}

function checkMobileBlur(){
	var mobile = $("#mobile").val();
	var regx = /^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8}$/;
	if(mobile == "" || !regx.test(mobile)){
		document.getElementById("mobileMsgSpan").className = "errorText errorRed";
		document.getElementById("mobileMsg").className = "errorMain";
		document.getElementById("mobileMsg").innerHTML = "手机号码格式有误";
		return false;
	}else{
		document.getElementById("mobileMsgSpan").className = "";
		document.getElementById("mobileMsg").className = "";
		document.getElementById("mobileMsg").innerHTML = "";
		return true;
	}
}
function checkMobileFocusTempNew(){
	$("#UtilsError").css("display","none");
}
function checkMobileBlurTempNew(){
	
	//手机号码
	var phone=$("#mobile").val();
	//固定电话号码
	var mobile=$("#phone").val();
	if(phone.length==11 || (mobile!="" && mobile != " ")){
		$("#UtilsError").css("display","none");
		return true;
	}else{
		$("#UtilsError").css("display","inline");
		return false;
	}
}






//邮箱验证
function checkEmailFocus(){
	//显示提示信息
    document.getElementById("emailMsgSpan").className = "errorText";
    document.getElementById("emailMsg").className = "errorMain";
    document.getElementById("emailMsg").innerHTML = "请输入正确的邮箱地址";
}

function checkEmailBlur(){
	var email = $("#email").val();
	var regx = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
	if(email == "" || !regx.test(email)){
		document.getElementById("emailMsgSpan").className = "errorText errorRed";
		document.getElementById("emailMsg").className = "errorMain";
		document.getElementById("emailMsg").innerHTML = "邮箱地址格式有误";
		return false;
	}else{
		document.getElementById("emailMsgSpan").className = "";
		document.getElementById("emailMsg").className = "";
		document.getElementById("emailMsg").innerHTML = "";
		return true;
	}
}

//街道地址验证
function checkAddressFocus(){
	//显示提示信息
    //document.getElementById("addressMsgSpan").className = "errorText";
    //document.getElementById("addressMsg").className = "errorMain";
    //document.getElementById("addressMsg").innerHTML = "请输入详细地址";
	$("#addressMsgSpan").css("display","none");
}

function checkAddressBlur(){
	var address = $("#address").val();
	var city = $("#tempInfoAddress").html().trim();
	if(city==""){
		$("#addressMsgSpan").css("display","inline");
		$("#addressMsgSpan").html("请选择省市区");
		return false;
	}else if(address.trim() == "" ){
		//document.getElementById("addressMsgSpan").className = "errorText errorRed";
		//document.getElementById("addressMsg").className = "errorMain";
		//document.getElementById("addressMsg").innerHTML = "请输入详细地址";
		$("#addressMsgSpan").css("display","inline");
		$("#addressMsgSpan").html("请您填写收货人详细地址");
		return false;
	}else{
		//document.getElementById("addressMsgSpan").className = "";
		//document.getElementById("addressMsg").className = "";
		//document.getElementById("addressMsg").innerHTML = "";
		
		
		var lengths=0;
		$(".selectFindHome").each(function(){
			if($(this).val().trim()==""){
				lengths=1;
			}
		});
		if(lengths==1){
			$("#addressMsgSpan").css("display","inline");
			$("#addressMsgSpan").html("请选择省市区");
			return false;
		}
		$("#addressMsgSpan").css("display","none");
		return true;
	}
}

//确定需要发票信息
function invoiceConfirm(id){
	//var companyName = "#companyName"+id;
	//var invoiceTitle = "#invoiceTitle"+id;
	//var com = "#comp"+id;
	//var radio = "#Radio"+id;
	//$(companyName).val($(com).val());
	//$(invoiceTitle).val($(radio).val());
	var invoiceTitle=$("input:text[name='invoiceTitle']").val();
	if(invoiceTitle.trim()==""){
		$("#invoiceTitlePromtMessage").show();
	}else{
		var temp="";
		$("input:radio[name='invoiceType']").each(function(){
			if(this.checked){
				if($(this).val()=="false"){
					temp += "发票信息：<span>个人</span>";
				}else{
					temp += "发票信息：<span>单位</span>";
				}
			}
		});
		$("input:radio[name='invoiceContent']").each(function(){
			if(this.checked){
				temp += "<span>"+$(this).val()+"</span>";
			}
		});
		$("input:text[name='invoiceTitle']").each(function(){
			temp += "<span>"+$(this).val()+"</span>";
		});
		$("#invoiceInfoContent").html(temp);
		$("#invoiceInfoContent").show();
		$(".invoiceDown").hide();
	}
	
}

//取消索要发票
function invoiceCancel(id){
	//var com = "#comp"+id;
	//var radio = "#Radio"+id;
	//var companyName = "#companyName"+id;
	//var invoiceTitle = "#invoiceTitle"+id;
	//$(com).val("");
	//$(radio).val("");
	//$(companyName).val("");
	//$(invoiceTitle).val("");
	$("input:radio[name='invoiceType']").attr("checked",false);
	$("input:radio[name='invoiceContent']").attr("checked",false);
	$("input:text[name='invoiceTitle']").val("");
	$("#invoiceInfoContent").hide();
	$(".invoiceDown").hide();
}

//验证 省市区是否为空
function checkPCAIsNull(){
	//延迟200ms取值。因为用公共 省市区点击时候。会误判断onblur事件
		var pcaValue=$("#pca").val();
		if(pcaValue){
			$("#checkPCA").hide();
			return true;
		}else{
			$("#checkPCA").show();
			return false;
		}
	
}

function updateAdress(id){
	$("#addressAddDiv").hide();
	$("#addressUpdateDiv").load($("#ctxpath").val()+"/purchase/addressUpdate/load/"+id,function(){
		$("#addressUpdateDiv").show();
	});
	//$("#gengArress").load(contextPath+"/user/addressUpdate/load/"+id+"?judgment="+judgment);
	
}

//-------------------------------------修改地址-----------------------------------
//重置
function resetForm(){
	//重置操作、保留原有值
	$("#name2").val($("#oldName").val());
	$("#pca2").val($("#oldPca").val());
	$("#zipcode2").val($("#oldZipcode").val());
	$("#phone2").val($("#oldPhone").val());
	$("#mobile2").val($("#oldMobile").val());
	$("#address2").val($("#oldAddress").val());
}

//修改地址表单的提交
function formSubmitUpdate(){
	alert(checkMobileBlurTempNew2())
	if(checkNameBlur2() && checkAddressBlur2() && checkMobileBlurTempNew2()){
		var zipcodeTwo = $("#zipcode2").val().trim();
		if(zipcodeTwo!=""){
			if(!checkZipcodeBlur2()){
				return;
			}
		}
		$("#modifyUserAddressForm").ajaxSubmit(
			function(data){
		 		if(data==true){
		 			salert("修改成功");
		 			$("#adressUl").load($("#ctxpath").val()+"/purchase/adress/shopCartload",function() {
		 				/*选择地址*/
		 				/*
		 			 	$(".siteList label").click(
		 			    		function(){
		 			    		$(".siteList li").removeClass("siteNow");
		 			    		$(this).parent().addClass("siteNow");
		 			    		}
		 			    	)
		 			  */  	
		 			    	/*地区选择*/
		 			    	/*
		 			    	$(function(){
		 			    		$(".area_two label").click(function(){
		 			    			$(this).addClass("xz_label").siblings().removeClass("xz_label");
		 			    			//$(this).nextAll().slideToggle();		
		 			    		});
		 			    		$(".area_three b").click(function(){
		 			    			$(this).parents("div.gg_area").hide();
		 			    			$(this).parents("li").removeClass("xz_li");
		 			    			var county=$(this).text();
		 			    			var city=$(this).parents("label").children("strong").text();
		 			    			var province=$(this).parents("li").children("span").text();
		 			    			$(this).parents(".formRight").children(".siteBox").attr("value",province+" "+city+" "+county);
		 			    		});
		 			    		$(".area_box ul li").hover(function(){
		 			    			$(this).addClass("xz_li");
		 			    			},function(){
		 			    				$(this).removeClass("xz_li");	
		 			    				$(this).children().children("label").removeClass("xz_label");	
		 			    		});
		 			    	})
		 			    	*/
		 			    	/*地区选择展开隐藏*/
		 			    	/*
		 			    	$(".siteBox").focus(function(){
		 			    		$(this).next(".gg_area").slideDown(100);
		 			    	})
		 			    	*/
		 			    	 /*新增/编辑收货地址*/
		 			    	 $(".cancel").click(function(){
		 			    		 $(this).parents("#addressAddDiv").hide();
		 			    		 
		 			    		var temvals=$("#pca").val();
		 			    		for(var i=0;i<temvals.length;i++){
		 			    			if(i==0){
		 			    				$("#uaProvincial").val(temvals[i]);
		 			    			}else if(i==2){
		 			    				$("#uaCity").val(temvals[i]);
		 			    			}else if(i==3){
		 			    				$("#uaArea").val(temvals[i]);
		 			    			}
		 			    		}
		 			    		$("#uaAddress").val($("#address").val());
		 			    		$("#uaZipcode").val($("#zipcode").val());
		 			    		$("#uaMobile").val($("#mobile").val());
		 			    		$("#uaName").val($("#name").val());
		 			    		 
		 			    		$('#name').val("");
		 							$('#zipcode').val("");
		 							$('#mobile').val("");
		 							$('#email').val("");
		 							$('#address').val("");
		 							$('#pca').val("");
		 							document.getElementById("nameMsgSpan").className = "";
		 							document.getElementById("nameMsg").className = "";
		 							document.getElementById("nameMsg").innerHTML = "";
		 							document.getElementById("zipcodeMsgSpan").className = "";
		 							document.getElementById("zipcodeMsg").className = "";
		 							document.getElementById("zipcodeMsg").innerHTML = "";
		 							document.getElementById("mobileMsgSpan").className = "";
		 							document.getElementById("mobileMsg").className = "";
		 							document.getElementById("mobileMsg").innerHTML = "";
		 							document.getElementById("emailMsgSpan").className = "";
		 							document.getElementById("emailMsg").className = "";
		 							document.getElementById("emailMsg").innerHTML = "";
		 							document.getElementById("addressMsgSpan").className = "";
		 							document.getElementById("addressMsg").className = "";
		 							document.getElementById("addressMsg").innerHTML = "";
		 			    	 })
		 			    	 $(".btnNew").click(function(){
		 			    	 	$("#nameMsgSpan").attr("class","formError");
		 			    		 $("#addressAddDiv").show();
		 			    	 })
		 			    	
		 			    	/*优惠券及发票*/
		 			    	var a=$(".invoice .blue,.coupon .blue")
		 			    	a.click(function(){
		 			    		
		 			    			$(".orderDown").hide();
		 			    			$(this).next(".orderDown").show();
		 			    		}
		 			    	)
		 			    	
		 			    	$(".layerClose").click(
		 			    		function(){
		 			    			$(this).parent().hide();
		 			    		}
		 			    	)
		 				});
		 				$("#addressUpdateDiv").hide();
		 			}else{
		 				ealert("修改失败");
		 			}
			});
	}else{
		//checkNameBlur2();
		//checkZipcodeBlur2(); 
		//checkMobileBlur2();
		//checkAddressBlur2();
		
		checkNameBlur2();
		checkAddressBlur2();
		checkMobileBlurTempNew2();
	}
}

//收货人姓名验证
function checkNameFocus2(){
	//显示提示信息
	$("#nameMsgSpan2").css("display","none");
}

function checkNameBlur2(){
	var $name = $.trim($("#name2").val());
	if($name == ""){
		$("#nameMsgSpan2").html("请您填写收货人姓名");
		$("#nameMsgSpan2").css("display","inline");
		return false;
	}else if($name.length < 2 || $name.length>20){
		$("#nameMsgSpan2").html("收货人姓名必须在2-20字符之间");
		$("#nameMsgSpan2").css("display","inline");
		return false;
	}else{
		$("#nameMsgSpan2").css("display","none");
		return true;
	}
}

// 邮政编码验证
function checkZipcodeFocus2(){
	//显示提示信息
	$("#zipcodeMsgSpan2").css("display","none");
}

function checkZipcodeBlur2(){
	var $zipcode = $.trim($("#zipcode2").val());
	//只能输入六位数字的验证
	var regx = /^[1-9][0-9]{5}$/;
	if($zipcode == "" || !regx.test($zipcode)){
		$("#zipcodeMsgSpan2").addClass("errorRed").show();
		return false;
	}else{
		$("#zipcodeMsgSpan2").css("display","none");
		return true;
	}
}

function checkMobileFocusTempNew2(){
	$("#UtilsError2").css("display","none");
}
function checkMobileBlurTempNew2(){
	var reg=/^0?(13[0-9]|15[012356789]|18[0236789]|17[0236789]|14[57])[0-9]{8}$/;
	var rega=/^0[1-9]{2,3}-[1-9]\d{5,7}$/;
	//手机号码
	var phone=$("#mobile2").val();
	//固定电话号码
	var mobile=$("#phone2").val();
	if(reg.test(phone) || rega.test(mobile)){
		$("#UtilsError2").css("display","none");
		return true;
	}
}

// 手机号码验证
function checkMobileFocus2(){
	$("#mobileMsgSpan2").removeClass("errorRed").show();
}

function checkMobileBlur2(){
	var $mobile = $.trim($("#mobile2").val());
	var regx = /^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\d{8}$/;
	if($mobile == "" || !regx.test($mobile)){
		$("#mobileMsgSpan2").addClass("errorRed").show();
		return false;
	}else{
		$("#mobileMsgSpan2").hide();
		return true;
	}
}

// 电话号码验证
function checkPhoneNumFocus2(){
	// 显示提示信息
    $("#phoneMsgSpan2").removeClass("errorRed").show();
}

function checkPhoneNumBlur2(){
	var $phone = $.trim($("#phone2").val());
	var regx = /^([\d-+]*)$/;
	if($phone.length != ""){
		if(!regx.test($phone)){
			$("#phoneMsgSpan2").addClass("errorRed").show();
		}else{
			$("#phoneMsgSpan2").hide();
			$("#phone2").val($phone);
		}
	}else{
		$("#phoneMsgSpan2").hide();
	}
}

//验证 省市区是否为空--qxh
function checkPCAIsNull2(){
	//延迟200ms取值。因为用公共 省市区点击时候。会误判断onblur事件
		var pcaValue=$("#pca2").val();
		if(pcaValue){
			$("#areaMsg2").hide().show();
			return true;
		}else{
			$("#areaMsg2").addClass("errorRed").show();
			return false;
		}
}



//街道地址验证
function checkAddressFocus2(){
	//var address = $("#address2").val();
	//$("#adressMsg2").html("请输入正确的街道地址");
	//$("#addressMsgSpan2").removeClass("errorRed").show();
	$("#addressMsgSpan2").css("display","none");
}

function checkAddressBlur2(){
	var address = $("#address2").val();
	var city = $("#tempInfoAddressField").html().trim();
	if(city == ""){
		$("#addressMsgSpan2").html("请选择省市区");
		$("#addressMsgSpan2").css("display","inline");
		return false;
	}else if(address.trim() == ""){	
		$("#addressMsgSpan2").html("请您填写收货人详细地址");
		$("#addressMsgSpan2").css("display","inline");
		return false;
	}else{
		var lengths=0;
		$(".kongNew").each(function(){
			if($(this).val().trim()==""){
				lengths=1;
			}
		});
		if(lengths==1){
			$("#addressMsgSpan2").css("display","inline");
			$("#addressMsgSpan2").html("请选择省市区");
			return false;
		}
		$("#addressMsgSpan2").css("display","none");
		return true;
	}
}

function updateCannel(){
	$("#addressUpdateDiv").hide();
}




//////////////////////////////////////////////////////////////////////////////////////////////

//新增收货地址
function addNewAddress(){
	$("#addressAddDiv").show();
}

//新增地址取消事件
function canelNewAddress(){
	$("#addressAddDiv").hide();
	$("#nameMsgSpan").css("display","none");
}


function findUtils(obj){
	//var classname="."+tempName;
	//var val=$(obj).val().trim();
	var contextPath=$("#ctxpath").val().trim();
	var judg=1;
	$(".selectFindHome").each(function(){
		if(judg==2){
			$(this).remove();	
		}
		if(this==obj){
			judg=2;
		}	
	});
	
	var tempJudg=$(".selectFindHome").length;

	if(tempJudg<3){
		$.post(contextPath+"/purchase/shopCartAdress/change/utils?parentId="+$(obj).val().trim(),function(data){
			if(data!="11"){
				var temp = '<select onchange="findUtils(this)" class="selectFindHome">';
				var array=data.substring(1).split(";");
				temp += '<option value="">请选择</option>';
				for(var i=0;i<array.length;i++){
					var areaname = array[i].split(",")[0];
					temp += '<option value="'+array[i]+'">'+areaname+'</option>';
				}
				temp += '</select>';
				$(obj).after(temp);
			}
		});
	}
	
	var tempVal="";
	var tempValNew="";
	$(".selectFindHome").each(function(){
		tempVal += " "+$(this).val().split(",")[0];
		tempValNew += $(this).val().split(",")[0]
	});
	
	$("#pca").val(tempVal.substring(1));
	$("#tempInfoAddress").html(tempValNew);
}


function findUtilsField(obj){
	//var classname="."+tempName;
	//var val=$(obj).val().trim();
	var contextPath=$("#ctxpath").val().trim();
	var judg=1;
	$(".kongNew").each(function(){
		if(judg==2){
			$(this).remove();	
		}
		if(this==obj){
			judg=2;
		}	
	});
	
	var tempJudg=$(".kongNew").length;

	if(tempJudg<3){
		$.post(contextPath+"/purchase/shopCartAdress/change/utils?parentId="+$(obj).val().trim(),function(data){
			if(data!="11"){
				var temp = '<select onchange="findUtilsField(this)" class="kongNew">';
				var array=data.substring(1).split(";");
				temp += '<option value="">请选择</option>';
				for(var i=0;i<array.length;i++){
					var areaname = array[i].split(",")[0];
					temp += '<option value="'+array[i]+'">'+areaname+'</option>';
				}
				temp += '</select>';
				$(obj).after(temp);
			}
		});
	}
	
	var tempVal="";
	var tempValNew="";
	$(".kongNew").each(function(){
		tempVal += " "+$(this).val().split(",")[0];
		tempValNew += $(this).val().split(",")[0]
	});
	$("#pca2").val(tempVal.substring(1));
	$("#tempInfoAddressField").html(tempValNew);
}


function submitNewOrders(){
	var address=$("#uaProvincial").val();
	var uaName=$("#uaName").val();
	if(address.trim()=="" && uaName.trim()==""){
		falert("请选择收货地址");
	}else{
		$("#addshopCartPayForm").submit();
	}
}