<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<div class="body-description">
在庫マスタ編集できます！！
</div>

<form:form action="zaikoconfirm.html" method="POST" modelAttribute="form">
<form:hidden path="product.procd"/>
<form:hidden path="product.upDate"/>
<form:hidden path="paging.searchKeys"/>

<div class="prog-err">
<form:errors htmlEscape="true"/>
</div>

<table class="data">
	<tr>
		<th><spring:message code="model.product.procd" /><br></th>
		<td><spring:bind path="product.procd" htmlEscape="true">${status.value}</spring:bind><br></td>
	</tr>
	<tr>
		<th><spring:message code="model.product.name"/><br></th>
		<td><spring:bind path="product.name" htmlEscape="true">${status.value}</spring:bind><br>
		</td>
	</tr>
	<tr>
		<th><spring:message code="model.product.kazu"/><br></th>
		<td><form:input path="product.kazu" maxlength="7" htmlEscape="true"/><br><form:errors path="product.kazu" cssStyle="color:red"/>
		</td>
	</tr>
	<c:if test="${form.admin}">

		<tr>
			<th><spring:message code="model.product.upDate"/><br></th>
			<td><spring:bind path="product.upDate" htmlEscape="true">${status.value}</spring:bind>
				<br>
			</td>
		</tr>
	</c:if>
</table>
<br>
	<input type="submit" value="確認" />
</form:form>

