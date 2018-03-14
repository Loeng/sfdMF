$(document).ready(function() {
	$("input.i_bg").focus(function (){ 
		$(this).parent().addClass("text_ts");
	});
	$("input.i_bg").blur(function (){ 
		$(this).parent().removeClass("text_ts");
	});
	
	/*按钮浮动定位*/
	$(".ht_btn").next().addClass("afterHt");
	
	$(".pro_ppList ul li").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
		}
	})
	$(".pro_ppList_title").click(function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
			$main.next("ul").find("input").attr("checked",true);
			$main.next("ul").find(".check").addClass("checked");
		}
		else{
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
			$main.next("ul").find(":checkbox").attr("checked",false);
			$main.next("ul").find(".check").removeClass("checked");
		}
	})
})


var flag = false;
// 显示下级菜单
function showChilds(id){
	var d = "#"+id;
	var p = "#p"+id;
	$(p).next("div").slideToggle();
	$(p).toggleClass("ht_fl_zk");
	$(d).load($("#ctxpath").val()+"/system/product/productCategory/child/"+id);
}

// 显示分类详情
function showCategory(id){
	// 将分类id放入隐藏域，便于添加子分类引用id
	$("#categoryId").val(id);
	var l = "#l"+id;
	$(".fl_nav label").removeClass("cur");
	$(l).addClass("cur");
	$(".fl_nav label").text("选择");
	$(l).text("已选");
	var result =  "<a href='javascript:modifySubmit();' class='btn'>保存</a>"+
        "<a href='javascript:modifyReset();' class='btn'>重置</a>"+
        "<a href='javascript:deleteConfirm();' class='btn'>删除</a>";
	document.getElementById("buttonDiv").innerHTML = result;
	$("#dataArea").load($("#ctxpath").val()+"/system/product/productCategory/detail/"+id,function() {
		 $(".pro_ppList ul li").click(function(){
				$main = $(this);
				if($main.find("input").is(":checked")){
					$main.find(".check").addClass("checked");
					$main.find("input").attr("checked",true);
				}
				else{
					$main.find(".check").removeClass("checked");
					$main.find("input").attr("checked",false);
				}
			})
			$(".pro_ppList_title").click(function(){
				$main = $(this);
				if($main.find("input").is(":checked")){
					$main.find(".check").addClass("checked");
					$main.find("input").attr("checked",true);
					$main.next("ul").find("input").attr("checked",true);
					$main.next("ul").find(".check").addClass("checked");
				}
				else{
					$main.find(".check").removeClass("checked");
					$main.find("input").attr("checked",false);
					$main.next("ul").find(":checkbox").attr("checked",false);
					$main.next("ul").find(".check").removeClass("checked");
				}
			});
	});
}

// 添加子分类
function addCategory(){
	// 得到分类id
	var id = $("#categoryId").val();
	if(id==null || id=="" || id==" "){
		ealert("请选择分类");
	}else{
		var result = "<a href='javascript:addSubmit();' class='btn' >保存</a>"+
					 "<a href='javascript:addReset();' class='btn' >重置</a>";
		document.getElementById("buttonDiv").innerHTML = result;
		$("#dataArea").load($("#ctxpath").val()+"/system/product/productCategory/add",function() {
			 $(".pro_ppList ul li").click(function(){
					$main = $(this);
					if($main.find("input").is(":checked")){
						$main.find(".check").addClass("checked");
						$main.find("input").attr("checked",true);
					}
					else{
						$main.find(".check").removeClass("checked");
						$main.find("input").attr("checked",false);
					}
				});
				$(".pro_ppList_title").click(function(){
					$main = $(this);
					if($main.find("input").is(":checked")){
						$main.find(".check").addClass("checked");
						$main.find("input").attr("checked",true);
						$main.next("ul").find("input").attr("checked",true);
						$main.next("ul").find(".check").addClass("checked");
					}
					else{
						$main.find(".check").removeClass("checked");
						$main.find("input").attr("checked",false);
						$main.next("ul").find(":checkbox").attr("checked",false);
						$main.next("ul").find(".check").removeClass("checked");
					}
				})
			$("#parentId").val(id);
		});
	}
	
}

// 新增页面的提交
function addSubmit(){
	// 得到分类id
	var id = $("#categoryId").val();
	if(checkNameBlur() && checkPositionBlur()){
		$("#addCategoryForm").ajaxSubmit(
		 		function(data){
		 			if(data==true){
		 				econfirm('添加成功，是否继续添加？',addReset,null,null,null);
		 				// 得到父分类span的id
						var p = "#p" + id;
						var c = $(p).attr("class");
						// 展开时先闭合分类再进行ajax查询；闭合时直接ajax查询
						if(c=="fl_title ht_fl_zk"){
							$(p).next("div").slideToggle();
							$(p).toggleClass("ht_fl_zk");
			 				showChilds(id);
						}else{
							showChilds(id);
						}
		 			}else{
		 				ealert("添加失败");
		 			}
		 		});
	}
	
}

//添加页面的重置
function addReset(){
	document.getElementById("addCategoryForm").reset();
}

// 修改页面的提交
function modifySubmit(){
	if(checkNameBlur() && checkPositionBlur()){
		$("#modifyCategoryForm").ajaxSubmit(
		 		function(data){
		 			if(data==true){
		 				var pId = $("#parentId").val();
		 				
		 				if(pId == ""){
		 					salertWithFunction("修改成功", myConReload, null);
		 				}else{
		 					var p = "#p" + pId;
							var c = $(p).attr("class");
							if(c == "fl_title ht_fl_zk"){
								$(p).next("div").slideToggle();
								$(p).toggleClass("ht_fl_zk");
				 				showChilds(pId);
							}else{
								showChilds(pId);
							}
							
							salert("修改成功");
		 				}
		 			}else{
		 				falert("修改失败");
		 			}
		 		});
	}
}

function myConReload(){
	window.location.reload();
}

//修改页面的重置
function modifyReset(){
	document.getElementById("modifyCategoryForm").reset();
}

//删除
function deleteConfirm(){
	// 得到分类id
	var id = $("#categoryId").val();
	econfirm('确认删除此分类？',deleteCategory,[id],null,null);
	
}

function deleteCategory(id){
	// 得到父分类id
	var pId = $("#parentId").val();
	$("#dataArea").load($("#ctxpath").val()+"/system/product/productCategory/delete/"+id,function(data) {
		document.getElementById("dataArea").innerHTML = "";
		if(data==true||data=="true"){
				ealert("删除成功");
				// 得到父分类span的id
				var p = "#p" + pId;
				var c = $(p).attr("class");
				// 展开时先闭合分类再进行ajax查询；闭合时直接ajax查询
				if(c=="fl_title ht_fl_zk"){
					$(p).next("div").slideToggle();
					$(p).toggleClass("ht_fl_zk");
	 				showChilds(pId);
				}else{
					showChilds(pId);
				}
				
			}else{
				ealert("删除失败");
			}
	});
}
//验证分类名格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("分类名为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("分类名为2-20位字符组成");
		return true;
	}
}

//验证分类名格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("分类名为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="";
		return true;
	}
}





//只能输入数字
function IsNum(e) {
    var k = window.event ? e.keyCode : e.which;
    if (((k >= 48) && (k <= 57))) {//小数点k==46
    } else {
        if (window.event) {
            window.event.returnValue = false;
        }
        else {
            e.preventDefault(); //for firefox 
        }
    }
} 
// 验证是否输入排序位置
function checkPositionBlur(){
	var positionStr = document.getElementsByName('position')[0].value;
	if(positionStr==""||positionStr==null){
		document.getElementById("positionDd").className="text_c_ts";
		$("#positionSpan").html("请输入排序位置");
		return false;
	}else{
		document.getElementById("positionDd").className="";
		return true;
	}
}

//验证是否输入排序位置
function checkPosition(){
	var positionStr = document.getElementsByName('position')[0].value;
	if(positionStr==""||positionStr==null){
		document.getElementById("positionDd").className="text_c_ts";
		$("#positionSpan").html("请输入排序位置");
		return false;
	}else{
		document.getElementById("positionDd").className="text_d_ts";
		return true;
	}
}
//添加跟分类
function addCategoryRoot(){
	
		var result = "<a href='javascript:addSubmit();' class='btn' >保存</a>"+
					 "<a href='javascript:addReset();' class='btn' >重置</a>";
		document.getElementById("buttonDiv").innerHTML = result;
		$("#dataArea").load($("#ctxpath").val()+"/system/product/productCategoryRoot/add",function() {
			 $(".pro_ppList ul li").click(function(){
					$main = $(this);
					if($main.find("input").is(":checked")){
						$main.find(".check").addClass("checked");
						$main.find("input").attr("checked",true);
					}
					else{
						$main.find(".check").removeClass("checked");
						$main.find("input").attr("checked",false);
					}
				});
				$(".pro_ppList_title").click(function(){
					$main = $(this);
					if($main.find("input").is(":checked")){
						$main.find(".check").addClass("checked");
						$main.find("input").attr("checked",true);
						$main.next("ul").find("input").attr("checked",true);
						$main.next("ul").find(".check").addClass("checked");
					}
					else{
						$main.find(".check").removeClass("checked");
						$main.find("input").attr("checked",false);
						$main.next("ul").find(":checkbox").attr("checked",false);
						$main.next("ul").find(".check").removeClass("checked");
					}
				})
			$("#parentId").val(id);
		});
	
}


function goRigth(){
	alert("right");
}

function goLeft(){
	alert("left");
}