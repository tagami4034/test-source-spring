<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript">
//<!--
//-->
</script>

<title>ログイン</title>
</head>
<body>

<h1>在庫管理システム</h1>
<div class="explanation">
<c:if test="${param.error eq 'true'}">
		<font color="red"> 入力されたユーザID、パスワードは不正です。<br>
		</font>
</c:if> <br>
</div>


  <form id="f" action="<c:url value='j_spring_security_check'/>" method="post"
  >
	<table class="login">
	<tr>
		<th>ログインID</th>
		<td><input type="text" name="j_username" value="<c:out value="${SPRING_SECURITY_LAST_USERNAME}"/>"></td>
	</tr>
	<tr>
		<th>ログインPW</th>
		<td><input type="text" id="passwd" name="j_password" ></td>
	</tr>
	</table>

<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
<input type="submit" name="login" value="ログイン" >
</form>

</body>
</html>