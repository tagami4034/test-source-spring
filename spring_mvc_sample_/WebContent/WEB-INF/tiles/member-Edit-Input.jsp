<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<div class="body-description">
このJSPファイルは管理者用の画面とユーザ用の画面で共有しています。<br>
同じファイルですが、色や表示項目・変更できる項目に差異があります。<br>
どうやっているか確認してみてください。
</div>

<form:form action="confirm.html" method="POST" modelAttribute="form">
<form:hidden path="member.id"/>
<form:hidden path="member.upDate"/>
<form:hidden path="paging.searchKeys"/>

<div class="prog-err">
<form:errors htmlEscape="true"/>
</div>

<table class="data">
	<tr>
		<th><spring:message code="model.member.loginId" /><br></th>
		<td><spring:bind path="member.loginId" htmlEscape="true">${status.value}</spring:bind><br></td>
	</tr>
	<tr>
		<th><spring:message code="model.member.name"/><br></th>
		<td><form:input path="member.name" maxlength="20" htmlEscape="true"/><br><form:errors path="member.name" cssStyle="color:red"/>
		</td>
	</tr>
	<tr>
		<th><spring:message code="model.member.age"/><br></th>
		<td><form:input path="member.age" maxlength="3" htmlEscape="true"/><br><form:errors path="member.age" cssStyle="color:red"/>
		</td>
	</tr>
	<c:if test="${form.admin}">
		<tr>
			<th><spring:message code="model.member.role"/><br></th>
			<td>
				<form:select path="member.role" multiple="false">
					<form:options items="${form.roleList}" />
				</form:select>
				<br><form:errors path="member.role" cssStyle="color:red"/>
			</td>
		</tr>
		<tr>
			<th><spring:message code="model.member.upDate"/><br></th>
			<td>
				<spring:bind path="member.upDate" htmlEscape="true">${status.value}</spring:bind>
				<br>
			</td>
		</tr>
	</c:if>
</table>
<br>
	<input type="submit" value="確認" />
</form:form>

