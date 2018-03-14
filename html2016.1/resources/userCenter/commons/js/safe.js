// JavaScript Document
$(document).ready(function() {

	/* 登陆密码修改弹窗提示 */
	$(".jsLoginPw").click(function() {
		$(".alertLayerBg").show();
		$(".alertLayerBg").next(".alterBox").show();
	});
	$(".layerClose").click(function() {
		$(this).parents(".alterBox").hide();
		$(".alertLayerBg").hide();
		$("#oldPwd").val("");
		$("#newPwd").val("");
		$("#newPwdTwo").val("");
	});

	/* 邮箱修改弹窗提示 */
	$(".jsTradePw").click(function() {
		$(".alertLayerBg").show();
		$(".alterBoxTrade").show();
	});
	$(".layerClose").click(function() {
		$(this).parents(".alterBoxTrade").hide();
		$(".alertLayerBg").hide();
		$("#oldEmail").val("");
		$("#newEmail").val("");
	});

	/* 手机号修改弹窗提示 */
	$(".jsQuestionPw").click(function() {
		$(".alertLayerBg").show();
		$(".alterBoxQuestion").show();
	});
	$(".layerClose").click(function() {
		$(this).parents(".alterBoxQuestion").hide();
		$(".alertLayerBg").hide();
		$("#oldPhoneSpan").val("");
		$("#newPhone").val("");
		$("#yanzhengma").val("");
	});

	
});
