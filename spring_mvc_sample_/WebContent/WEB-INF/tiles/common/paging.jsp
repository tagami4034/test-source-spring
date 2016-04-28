<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"  %>
<% /*
	【tilesパラメタ(putAttribute)】
	paging ...ページングオブジェクト
	actionName ・・・遷移先のActionパス名
	modelAttribute ...formのmodelAttribute属性に設定する値。
*/
%><tiles:importAttribute name="paging" /><%
%><tiles:importAttribute name="actionName"/><% 
%><tiles:importAttribute name="modelAttribute"/>
<table style="padding:0pt; margin: 0pt;">
<tr>
<td>	
<form:form action="${actionName}" method="POST" modelAttribute="${modelAttribute}" cssStyle="display: inline;">
	<input type="hidden" name="paging.searchKeys" value="${fn:escapeXml(paging.prev.searchKeys)}" />
	<input type="submit" value="前へ">
</form:form>
</td> 


<td>
<form:form action="${actionName}" method="POST" modelAttribute="${modelAttribute}" cssStyle="display: inline;">
	<input name="paging.specifiedPageNum" value="${paging.page}"  style="width:50pt;"/>/${paging.maxPage}
	<input type="hidden" name="paging.searchKeys" value="${fn:escapeXml(paging.pageSpecify.searchKeys)}" />
	<input type="submit" value="ジャンプ" />
</form:form>
</td>

<td>
<form:form action="${actionName}" method="POST" modelAttribute="${modelAttribute}" cssStyle="display: inline;">
	<input type="hidden" name="paging.searchKeys" value="${fn:escapeXml(paging.next.searchKeys)}" />
	<input type="submit" value="次へ" />
</form:form>
</td>

</tr>
</table>