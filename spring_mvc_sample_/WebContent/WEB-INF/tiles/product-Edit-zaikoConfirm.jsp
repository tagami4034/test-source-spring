<%@ page language="java" pageEncoding="utf-8" %>


<form:form action="zaikotransactfinish.html" method="POST" modelAttribute="form">
<form:hidden path="product.procd" htmlEscape="true"/>
<form:hidden path="product.upDate" />
<form:hidden path="paging.searchKeys"/>
<table class="data">
	<tr>
		<th><spring:message code="model.product.procd" /><br></th>
		<td><spring:bind path="product.procd" htmlEscape="true">${status.value}</spring:bind></td>
	</tr>
	<tr>
		<th><spring:message code="model.product.name"/><br></th>
		<td><spring:bind path="product.name" htmlEscape="true">${status.value}</spring:bind>
		<form:hidden path="product.name"/> </td>
	</tr>
	<tr>
		<th><spring:message code="model.product.kazu"/><br></th>
		<td><spring:bind path="product.kazu" htmlEscape="true">${status.value}</spring:bind>
		<form:hidden path="product.kazu"/> </td>
	</tr>
	<tr>
		<th><spring:message code="model.product.upDate"/><br></th>
		<td><spring:bind path="product.upDate" htmlEscape="true">${status.value}</spring:bind>
		<form:hidden path="product.upDate" /></td>
	</tr>
</table>

<input type="submit" value="完了" onclick="javascript: disableObject(this);"/>
</form:form>

