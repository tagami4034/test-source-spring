<%@ page language="java" pageEncoding="utf-8" %>
						   

完了しました。 <br>
この画面でリロードしないでください。<br>
<br>

<c:url var="actionUrl" value="/" />
<form:form action="${actionUrl}" cssStyle="display:inline; padding-right: 10px;"><input type="submit" value="トップへ"></form:form>

<c:if test="${form.admin}">
<c:choose>
	<c:when test="${!empty searchKeys}">
	<form:form action="../srch/list.html" cssStyle="display: inline;"> 
		<input type="hidden" name="paging.searchKeys" value="${fn:escapeXml(searchKeys)}"  />
		<input type="submit" value="検索結果一覧へ">
	</form:form>
	</c:when>
	<c:otherwise>
	<form:form action="../srch/input.html" method="get" cssStyle="display: inline;"> 
		<input type="submit" value="検索条件入力へ">※リロードされたため検索条件がクリアされました
	</form:form>
	</c:otherwise>
</c:choose>
</c:if>
