<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>



<div class="body-description">
検索結果を表示しています。編集対象のユーザを編集してください。<br>
編集完了後はこの画面に戻ってきます。
</div>

<div class="prog-err">
<form:errors htmlEscape="true" />
</div>
<c:set var="actionUrl"><c:url value="/admin/member/edit"/></c:set>

<c:set var="searchCondition"><c:if test="${!empty form.memberSearchKeys.id}">
<spring:message code="model.memberSearchKeys.id" />=${form.memberSearchKeys.id }、
</c:if>
<c:if test="${!empty form.memberSearchKeys.nameBW}">
<spring:message code="model.memberSearchKeys.nameBW" />=${form.memberSearchKeys.nameBW}、
</c:if>
<c:if test="${!empty form.memberSearchKeys.loginId}">
<spring:message code="model.memberSearchKeys.loginId" />=${form.memberSearchKeys.loginId}、
</c:if>
</c:set>
<c:set var="searchCondition" value="${searchCondition.replaceFirst('、$', '')}"></c:set> 
<div class="body-srch-cond">
検索条件：　${searchCondition}<br>
<form:form action="input.html" method="post" modelAttribute="form" cssStyle="display:inline;">
	<form:hidden path="paging.searchKeys" />
	<input type="submit" value="検索条件入力へ">
</form:form>
</div>


<tiles:insertTemplate template="/WEB-INF/tiles/common/paging.jsp">
	<tiles:putAttribute name="paging" value="${form.paging}" />
	<tiles:putAttribute name="actionName" value="list.html" />
	<tiles:putAttribute name="modelAttribute" value="form" />
</tiles:insertTemplate>


<table class="data">
	<tr>
		<th><spring:message code="model.member.id" /><br></th>
		<th><spring:message code="model.member.name" /><br></th>
		<th><spring:message code="model.member.loginId" /><br></th>
		<th><spring:message code="title.transact" /><br></th>
	</tr>
	<c:forEach items="${form.memberList}"  var="member">
		<tr>
			<td>${member.id}<br></td>
			<td>${fn:escapeXml(member.name)}<br></td>
			<td>${fn:escapeXml(member.loginId)}<br></td>
			<td><form:form action="${actionUrl}/input.html" method="post" modelAttribute="form" cssStyle="display: inline; ">
					<input type="hidden" name="member.id" value="${member.id}"/>
					<form:hidden path="paging.searchKeys" htmlEscape="true"/>
					<input type="submit" value="編集" />
				</form:form>
			</td> 
		</tr>
	</c:forEach>
</table>

<tiles:insertTemplate template="/WEB-INF/tiles/common/paging.jsp">
	<tiles:putAttribute name="paging" value="${form.paging}" />
	<tiles:putAttribute name="actionName" value="list.html" />
	<tiles:putAttribute name="modelAttribute" value="form" />
</tiles:insertTemplate>

