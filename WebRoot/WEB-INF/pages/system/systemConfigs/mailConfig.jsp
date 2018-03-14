<!doctype html>
<%@ page language="java" import="java.util.*,com.ekfans.pub.util.*,com.ekfans.base.system.util.MailConst" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@include file="../common/jsp/pub.jsp"%>
<%@ taglib uri="http://ckeditor.com" prefix="ckeditor" %>
<html>
<head>
<meta charset="utf-8">
<title>邮件模板</title>
<link href="<%=CONTEXTSYSTEM%>/css/mailConfig.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=CONTEXTSYSTEM%>/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="<%=CONTEXT%>/ckeditor/ckeditor.js"></script>

<script>
	function submitForm () {
		var password = document.getElementsByName("password")[0].value;
		var repassword = document.getElementsByName("repassword")[0].value;
		if(password != repassword){
			alert("两次输入的密码不一致，请重新输入！")
			return;	
		}
		document.getElementById("mailConfig").submit();
	}	
	
</script>
</head>
<body >
	<s:form name="mailConfig" id="mailConfig" action="/system/mail/save">
		<div class="topButton">
			<input onclick="javascript:submitForm()" type="button" align="absmiddle" value="保存" class="input1">&nbsp;
			<input  onclick="javascript:formReset()" type="button" value="清空" align="absmiddle" class="input1">
		</div>
		<div class="mailTop" >
			<ul>
				<%
					MailConfig mailConfig = (MailConfig)request.getAttribute("mailConfig");
					if(mailConfig == null){
						mailConfig = new MailConfig();
					}
				%>
				<li>发件服务器：<input type="text" name="host" value="<%=mailConfig.getHost()%>"></li>
				<li>用户帐号：<input type="text" name="userName" value="<%=mailConfig.getUserName()%>"></li>
				<li>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：<input type="password" name="password" value="<%=mailConfig.getPassword()%>"></li>
				<li>重复密码：<input type="password" name="repassword" value="<%=mailConfig.getPassword()%>"></li>
			</ul>	
		</div>
		
		<div>
			<table>
				
			<%
				CKEditorConfig settings = new CKEditorConfig();
				settings.addConfigValue("width","900");
				settings.addConfigValue("height","250");
				Map<String, String[]> contentMap = mailConfig.getContentMap();
				if(contentMap == null){
					contentMap = new HashMap<String, String[]>();
				}
				
				Map<String,String> mailConfigs = MailConst.MAIL_CONFIG_NAMES;
				System.out.println(mailConfigs.size());
				if(mailConfigs != null && mailConfigs.size() > 0){
					Iterator<Map.Entry<String, String>> it = mailConfigs.entrySet().iterator();
				  while (it.hasNext()) {
				  Map.Entry<String, String> entry = it.next();
					String constKey = entry.getKey();
					String constName = entry.getValue();
					String[] constValues = contentMap.get(constKey);
					if(constValues == null || constValues.length<=0){
						constValues = new String[2];
					}
					
					String titleValue = StringUtil.isEmpty(constValues[0]) ? "":constValues[0];
					String contentValue = StringUtil.isEmpty(constValues[1]) ? "":constValues[1];
			%>
				<tr>
					<td><%=constName%>主题</td>
					<td><input type="text" name="<%=constKey%>title" value="<%=titleValue%>" style="width:280px;"/></td>
				</tr>
				<tr>
					<td><%=constName%>模板</td>
					<td>
						<ckeditor:editor  basePath="/ckeditor/" editor="<%=constKey%>" config="<%=settings%>" value="<%=contentValue%>" />
					</td>
				</tr>
				
				<tr>
					<td colspan="2">&nbsp;</td>
				</tr>
			<%}}%>
			</table>
		</div>
	</s:form>
</body>
</html>
