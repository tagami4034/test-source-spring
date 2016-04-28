<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<form:form action="list.html" method="POST" modelAttribute="form">
<form:hidden path="paging.page"/>

<div class="prog-err">
<form:errors htmlEscape="true"/>
</div>

<table class="data">
	<tr>
		<th><spring:message code="model.memberSearchKeys.id" /><br></th>
		<td><form:input path="memberSearchKeys.id" htmlEscape="true" /><br><form:errors path="memberSearchKeys.nameBW" cssStyle="color:red"/></td>
	</tr>
	<tr>
		<th><spring:message code="model.memberSearchKeys.nameBW"/><br></th>
		<td><form:input path="memberSearchKeys.nameBW" maxlength="20" htmlEscape="true"/><br><form:errors path="memberSearchKeys.nameBW" cssStyle="color:red"/>
		</td>
	</tr>
	<tr>
		<th><spring:message code="model.memberSearchKeys.loginId"/><br></th>
		<td><form:input path="memberSearchKeys.loginId" maxlength="20" htmlEscape="true"/><br><form:errors path="memberSearchKeys.loginId" cssStyle="color:red"/>
		</td>
	</tr>
</table>
<br>
	<input type="submit" value="検索" />
</form:form>

