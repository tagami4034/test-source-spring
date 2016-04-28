<%@ page language="java" pageEncoding="utf-8" %>


<form:form action="transactfinish.html" method="POST" modelAttribute="form">
<form:hidden path="member.id" htmlEscape="true"/>
<form:hidden path="member.version" />
<form:hidden path="paging.searchKeys"/>
<table class="data">
	<tr>
		<th><spring:message code="model.member.loginId" /><br></th>
		<td><spring:bind path="member.loginId" htmlEscape="true">${status.value}</spring:bind></td>
	</tr>
	<tr>
		<th><spring:message code="model.member.name"/><br></th>
		<td><spring:bind path="member.name" htmlEscape="true">${status.value}</spring:bind>
		<form:hidden path="member.name"/> </td>
	</tr>
	<tr>
		<th><spring:message code="model.member.age"/><br></th>
		<td><spring:bind path="member.age" htmlEscape="true">${status.value}</spring:bind>
		<form:hidden path="member.age"/> </td>
	</tr>
	<c:if test="${form.admin}">
		<tr>
			<th><spring:message code="model.member.role"/><br></th>
			<td><spring:bind path="member.role" htmlEscape="true">${status.value}</spring:bind>
			<form:hidden path="member.role"/> </td>
		</tr>
	</c:if>
	<tr>
		<th><spring:message code="model.member.upDate"/><br></th>
		<td><spring:bind path="member.upDate" htmlEscape="true">${status.value}</spring:bind>
		<form:hidden path="member.upDate" /></td>
	</tr>
</table>

<input type="submit" value="完了" onclick="javascript: disableObject(this);"/>
</form:form>

