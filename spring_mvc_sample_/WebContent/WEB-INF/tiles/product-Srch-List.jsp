<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>



<div class="body-description">
検索結果を表示しています。編集対象のユーザを編集してください。<br>
編集完了後はこの画面に戻ってきます。
</div>

<div class="prog-err">
<form:errors htmlEscape="true" />
</div>
<c:set var="actionUrl"><c:url value="/zaiko/product/edit"/></c:set>

<c:set var="searchCondition">
<c:if test="${!empty form.productSearchKeys.procd}">
<spring:message code="model.productSearchKeys.procd" />=${form.productSearchKeys.procd }、
</c:if>
<c:if test="${!empty form.productSearchKeys.proClass}">
<spring:message code="model.productSearchKeys.proClass" />=${form.productSearchKeys.proClass }、
</c:if>
<c:if test="${!empty form.productSearchKeys.nameBW}">
<spring:message code="model.productSearchKeys.nameBW" />=${form.productSearchKeys.nameBW}、
</c:if>
<c:if test="${!empty form.productSearchKeys.tanka}">
<spring:message code="model.productSearchKeys.tanka" />=${form.productSearchKeys.tanka}、
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
		<th><spring:message code="model.product.procd" /><br></th>
		<th><spring:message code="model.product.proClass" /></th>
		<th><spring:message code="model.product.name" /><br></th>
		<th><spring:message code="model.product.tanka" /><br></th>
		<th><spring:message code="model.product.zaiko"/><br></th>
		<th><spring:message code="title.producttransact" /><br></th>
		<th><spring:message code="title.zaikotransact" /><br></th>
	</tr>
	<c:forEach items="${form.productList}"  var="product">
		<tr>
			<td>${product.procd}<br></td>
			<td>${product.proClass}<br></td>
			<td>${fn:escapeXml(product.name)}<br></td>
			<td>${fn:escapeXml(product.tanka)}<br></td>
			<td>${product.kazu}</td>
			<td><form:form action="${actionUrl}/input.html" method="post" modelAttribute="form" cssStyle="display: inline; ">
					<input type="hidden" name="product.procd" value="${product.procd}"/>
					<form:hidden path="paging.searchKeys" htmlEscape="true"/>
					<input type="submit" value="商品編集" />
				</form:form>
			</td>
			<td><form:form action="${actionUrl}/zaikoinput.html" method="POST" modelAttribute="form" cssStyle="display: inlire; ">
					<input type="hidden" name="product.procd" value="${product.procd}"/>
					<form:hidden path="paging.searchKeys" htmlEscape="true"/>
					<input type="submit" value="在庫編集" />
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

