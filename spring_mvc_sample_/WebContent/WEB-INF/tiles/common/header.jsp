<%@ page language="java" pageEncoding="utf-8"%>


<h1>在庫管理システム</h1>
<div class="header-menu">
	<a href="<c:url value='/' />">Home</a>&nbsp;&nbsp;
	<form action="<c:url value='/logout'/>" method="post" style="display: inline">
		<input type="submit" value="ログアウト" />
		<input type="hidden"  name="${_csrf.parameterName}" value="${_csrf.token}"/>
	</form>

</div>
