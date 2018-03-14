<!doctype html>
<%@page pageEncoding="UTF-8" %>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@include file="../common/jsp/pub.jsp"%>
<html>
<head>
	<meta charset="UTF-8">
	<title>新增管理员</title>
</head>
<body>
	<%
		String fullName = (String)request.getAttribute("fullName");
		String modify = (String)request.getAttribute("modify");
		Map<String, SystemArea> objMap = (Map<String, SystemArea>)request.getAttribute("objMap");
		Map<String, List<String>> childMap = (Map<String, List<String>>)request.getAttribute("childMap");
		Map<String, List<String>> rootMap = (Map<String, List<String>>)request.getAttribute("rootList");
	%>
	
<div class="ht_area">
	<%if("true".equals(modify)){%> <img src="<%=CONTEXTSYSTEM%>/images/dt_tb.jpg"> <%}%>
	<span id="showNames"><%=!StringUtil.isEmpty(fullName)?fullName:""%></span>
	<div class="gg_area" id="areaShow">
		
			<%
				if(rootMap != null && rootMap.size() >0){
					Iterator it = rootMap.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						String key = (String)entry.getKey();
						List<String> rootList = (List<String>)entry.getValue();
						if(!StringUtil.isEmpty(key)){
			%>
						<div class="area_box">
							<%
								if(rootList != null && rootList.size()>0){
							%>
							<div class="zimu"><%=key%></div>
							<ul>
							<%
									for(int i=0;i<rootList.size();i++){
										String rootId = (String) rootList.get(i);
										if(!StringUtil.isEmpty(rootId)){
											List<String> childList = (List<String>)childMap.get(rootId);
											SystemArea rootArea = (SystemArea)objMap.get(rootId);
											if(rootArea != null){
							%>
										<li>
											<span><%=rootArea.getAreaName()%></span>
											<%
												if(childList != null && childList.size()>0){
											%>
											<div class="area_two">	
											<%
													for(int j=0;j<childList.size();j++){
														String childId = (String) childList.get(j);
														if(!StringUtil.isEmpty(childId)){
															List<String> list = (List<String>)childMap.get(childId);
															SystemArea childArea = (SystemArea)objMap.get(childId);
															if(childArea != null){
											%>
																<label id="<%=childArea.getId()%>">
																	<strong><%=childArea.getAreaName()%></strong>
																	<%
																		if(list != null && list.size()>0){
																	%>
																	<div class="area_three">
																	<%
																			for(int k=0;k<list.size();k++){
																				String lastId = (String) list.get(k);
																				if(!StringUtil.isEmpty(lastId)){
																					SystemArea lastArea = (SystemArea)objMap.get(lastId);
																					if(lastArea != null){
																	%>
																						<b id="<%=lastArea.getId()%>"><%=lastArea.getAreaName()%></b>
																	
																	<%
																					}
																				}
																			}
																	%>
																	</div>
																	<%
																		}
																	%>
																</label>
											<%
															}
														}
													}
											%>
											</div>
											<%
												}
											%>
										</li>
							<%
											}
										}
									}
							%>
									</ul>
							<%
								}
							%>
						</div>
			<%
						}
					}
				}
			%>
	<div>
</div>
<input type="hidden" value="<%=(String)request.getAttribute("areaId")%>" name="areaId" id="areaId"/>

<script>

	$(function(){
		var chose = 0;
		$(".ht_area img").click(function(){
			//$(this).next(".gg_area").show();
			$("#areaShow").show();
			chose = 0;
		});
		
		
		$(".area_two label").click(function(){
			$(this).addClass("xz_label").siblings().removeClass("xz_label");
			if(chose == 0 || chose == 1){
				$("#showNames").html($(this).parent().parent().children("span").html() + "  " + $(this).children("strong").html());
				if($(this).children("div").html() == null || $(this).children("div").html() == "undefined" || $(this).children("div").html() == "null"){
					if($(this).attr("id") != ""){
							$("#areaId").val($(this).attr("id"));
					}
					$(this).parents("div.gg_area").hide();
					$(this).parents("li").removeClass("xz_li");
				}
			}
			chose = 1;
			//$(this).nextAll().slideToggle();		
		});
		$(".area_three b").click(function(){
			$(this).parents("div.gg_area").hide();
			$(this).parents("li").removeClass("xz_li");
			$("#showNames").html($("#showNames").html() + "  " + $(this).html());
			$("#areaId").val($(this).attr("id"));
			chose = 2;
		});
		
		$(".area_box ul li").hover(function(){
			$(this).addClass("xz_li");
			},function(){
				$(this).removeClass("xz_li");	
				$(this).children().children("label").removeClass("xz_label");	
		});
	})	
</script>
</body>
</html>