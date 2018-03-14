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
	});

	$(".limitItem").live("click",function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.find("input").attr("checked",true);
			$main.next(".limitList").find("input").attr("checked",true);
			$main.next(".limitList").find(".check").addClass("checked");
			$main.parents().prev(".limitItem").find("input").attr("checked",true);
			$main.parents().prev(".limitItem").find(".check").addClass("checked");
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.find("input").attr("checked",false);
			$main.next(".limitList").find(":checkbox").attr("checked",false);
			$main.next(".limitList").find(".check").removeClass("checked");
		}
	});
	$(".limitAll").live("click",function(){
		$main = $(this);
		if($main.find("input").is(":checked")){
			// 下级都被选中
			$main.find(".check").addClass("checked");
			$main.nextAll(".limitList").find("input").attr("checked",true);
			$main.nextAll(".limitList").find(".check").addClass("checked");
		}else{
			// 下级都取消选中
			$main.find(".check").removeClass("checked");
			$main.nextAll(".limitList").find(":checkbox").attr("checked",false);
			$main.nextAll(".limitList").find(".check").removeClass("checked")
		}
	});
})


var flag = false;
// 显示下级菜单
function showChilds(id){
	var d = "#"+id;
	var p = "#p"+id;
	$(p).next("div").slideToggle();
	$(p).toggleClass("ht_fl_zk");
	$(d).load($("#ctxpath").val()+"/system/finacne/demarts/child/"+id);
}

// 显示部门详情
function showDepartment(id){
	// 将部门id放入隐藏域，便于添加子部门引用id
	$("#departmentId").val(id);
	var l = "#l"+id;
	$(".fl_nav label").removeClass("cur");
	$(l).addClass("cur");
	$(".fl_nav label").text("选择");
	$(l).text("已选");
	var result =  "<a href='javascript:modifySubmit();' class='btn'>保存</a>"+
        "<a href='javascript:modifyReset();' class='btn'>重置</a>"+
        "<a href='javascript:deleteConfirm();' class='btn'>删除</a>";
	document.getElementById("buttonDiv").innerHTML = result;
	$("#dataArea").load($("#ctxpath").val()+"/system/finacne/demarts/detail/"+id,function() {
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

// 新增页面的提交
function addSubmit(){
	// 得到部门id
	var id = $("#departmentId").val();
	if(checkNameBlur()){
		$("#addForm").ajaxSubmit(
		 		function(data){
		 			if(data==true){
		 				if(id == "" || id == " " || id == null || id == "null"){
		 					alert('添加成功！');
		 					window.location.reload();
		 				}else{
			 				econfirm('添加成功，是否继续添加？',addReset,null,null,null);
			 				// 得到父部门span的id
							var p = "#p" + id;
							var c = $(p).attr("class");
							// 展开时先闭合部门再进行ajax查询；闭合时直接ajax查询
							if(c=="fl_title ht_fl_zk"){
								$(p).next("div").slideToggle();
								$(p).toggleClass("ht_fl_zk");
				 				showChilds(id);
							}else{
								showChilds(id);
							}
						}
		 			}else{
		 				falert("添加失败");
		 			}
		 		});
	}
	
}

//添加页面的重置
function addReset(){
	document.getElementById("addForm").reset();
}

// 修改页面的提交
function modifySubmit(){
	if(checkNameBlur() && checkPositionBlur()){
		$("#modifyDepartmentForm").ajaxSubmit(
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
	document.getElementById("modifyDepartmentForm").reset();
}

//删除
function deleteConfirm(){
	var id = $("#departmentId").val();
	// 得到部门id
	$.post(
		$("#ctxpath").val() + "/system/finacne/demarts/checkCanRemove/"+id,
		function(data){
			if("1" == data || 1 == data){
				econfirm('确认删除此部门？',deleteDepartment,[id],null,null);
			}else{
				falert("无法删除该部门，请确认部门下没有子部门或者监管账号！");
			}
		}
	);
}

function deleteDepartment(id){
	// 得到父部门id
	var pId = $("#parentId").val();
	$("#dataArea").load($("#ctxpath").val()+"/system/finacne/demarts/delete/"+id,function(data) {
		document.getElementById("dataArea").innerHTML = "";
		if(data==true||data=="true"){
				falert("删除成功");
				// 得到父部门span的id
				var p = "#p" + pId;
				var c = $(p).attr("class");
				// 展开时先闭合部门再进行ajax查询；闭合时直接ajax查询
				if(c=="fl_title ht_fl_zk"){
					$(p).next("div").slideToggle();
					$(p).toggleClass("ht_fl_zk");
	 				showChilds(pId);
				}else{
					showChilds(pId);
				}
				
			}else{
				falert("删除失败");
			}
	});
}
//验证部门名称格式
function checkName(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_ts";
		$("#nameSpan").html("部门名称为2-20位字符组成");
		return false;
	}else{
		document.getElementById("nameDd").className="text_d_ts";
		$("#nameSpan").html("部门名称为2-20位字符组成");
		return true;
	}
}

//验证部门名称格式
function checkNameBlur(){
	var nameStr = document.getElementsByName('name')[0].value;
	if(nameStr.length < 2 || nameStr.length >20 || nameStr ==" "){
		document.getElementById("nameDd").className="text_c_ts";
		$("#nameSpan").html("部门名称为2-20位字符组成");
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
//添加跟部门
function addDepartmentRoot(){
	
		var result = "<a href='javascript:addSubmit();' class='btn' >保存</a>"+
					 "<a href='javascript:addReset();' class='btn' >重置</a>";
		document.getElementById("buttonDiv").innerHTML = result;
		$("#dataArea").load($("#ctxpath").val()+"/system/finacne/demarts/add",{bankId:$("#rootBankId").val()},function() {
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
		});
	
}


// 添加子部门
function addDepartment(){
	// 得到部门id
	var id = $("#departmentId").val();
	var bankId = $("#rootBankId").val();
	if(id==null || id=="" || id==" "){
		falert("请选择部门");
	}else{
		var result = "<a href='javascript:addSubmit();' class='btn' >保存</a>"+
					 "<a href='javascript:addReset();' class='btn' >重置</a>";
		document.getElementById("buttonDiv").innerHTML = result;
		$("#dataArea").load($("#ctxpath").val()+"/system/finacne/demarts/add",{bankId:bankId,parentId:id},function() {
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



function goRigth(){
	alert("right");
}

function goLeft(){
	alert("left");
}