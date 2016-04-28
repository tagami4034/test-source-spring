<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>


<form:form action="list.html" method="POST" modelAttribute="form">
<form:hidden path="paging.page"/>

<div class="prog-err">
<form:errors htmlEscape="true"/>
</div>
<table class="data">
	<tr>
		<th><spring:message code="model.productSearchKeys.procd" /><br></th>
		<td><form:input path="productSearchKeys.procd" htmlEscape="true" /><br><form:errors path="productSearchKeys.nameBW" cssStyle="color:red"/></td>
	</tr>
	<tr>
		<th><spring:message code="model.productSearchKeys.proClass" /><br></th>
		<td><form:input path="productSearchKeys.proClass" htmlEscape="true" /><br><form:errors path="productSearchKeys.proClass" cssStyle="color:red"/></td>
	</tr>
	<tr>
		<th><spring:message code="model.productSearchKeys.nameBW"/><br></th>
		<td><form:input path="productSearchKeys.nameBW" maxlength="20" htmlEscape="true"/><br><form:errors path="productSearchKeys.nameBW" cssStyle="color:red"/>
		</td>
	</tr>
	<tr>
		<th><spring:message code="model.productSearchKeys.tanka"/><br></th>
		<td><form:input path="productSearchKeys.tanka" maxlength="20" htmlEscape="true"/><br><form:errors path="productSearchKeys.tanka" cssStyle="color:red"/>
		</td>
	</tr>
</table>

<br>
	<input type="submit" value="検索" />
</form:form>


