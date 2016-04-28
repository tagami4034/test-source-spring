<%@ page language="java" pageEncoding="utf-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"> 
	<c:set var="titleNameKey">screen.title.<tiles:getAsString name='titleName' /></c:set>
	<c:set var="titleFuncNameKey">screen.func.<tiles:getAsString name='titleFuncName' /></c:set>
	<c:set var="titleSubName"><tiles:getAsString name='titleSubName' /></c:set>
	<c:set var="titleSubNameKey" value="screen.sub.${titleSubName}" />
	<c:set var="titleName"><spring:message code="${titleNameKey}" text="見つかりません"/><spring:message code="${titleFuncNameKey}" text="見つかりません"/>　<spring:message code="${titleSubNameKey}" text="${titleSubNameKey}"/></c:set>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<title>${titleName}</title>
	<link href="<c:url value='/include/prog.css'/>" rel="stylesheet"  type="text/css" />
	<link href="<c:url value='/include/default.css'/>" rel="stylesheet"  type="text/css" />
	<script type="text/javascript" src="<c:url value='/include/common.js'/>" ></script>
	<script type="text/javascript" src="<c:url value='/include/jquery-1.11.2.js'/>" ></script>
</head>

<body>

<div class="sec">
	
	
	<div class="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div class="body">
		<div class="body-header">
			${titleName}
		</div>
		<div class="body-content">
			<tiles:insertAttribute name="body" />
		</div>
	</div>
		
	<div class="footer">
		<tiles:insertAttribute name="footer" />
	</div>
	
</div>
</body>


</html>

