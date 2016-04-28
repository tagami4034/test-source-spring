<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>トップ</title>
</head>
<body style="font-size: 14px;">
【テストSpring3.2】<br>
<br>
●実戦的サンプル<br>
<!-- <a href="cust/member/edit/input.html?member.id=1">Spring ユーザ画面テストへ</a><br> -->
<!-- <a href="admin/member/srch/input.html">Spring 管理者画面テストへ</a><br> -->
<a href="zaiko/product/srch/input.html">Spring在庫管理</a><br>
<form action="<c:url value='/logout'/>" method="post" style="display: inline">
	<input type="submit" value="ログアウト" />
	<input type="hidden"  name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<!--
<br><br>
●動作を確認するためのリンク<br>
　エラーのときの動作を見ることでどのような設計をしているか感じることができると思います。<br>
　お試しください！出力されるログも確認ください！<br>
<br>
<a href="cust/err.html">存在しないURLにアクセスした場合</a><br>
　⇒何もしないとtomcatのバージョンが見える危険なエラー画面が表示される<br>
<br>
<a href="test/err.html">権限のない画面にアクセスした場合</a><br>
　⇒SpringSecurityにより403のエラーが出される<br>
<br>
<a href="cust/member/edit/input.html?member.id=8">パラメタ改ざんし、他人のID=8を見ようとした場合</a><br>
　⇒ちゃんと処置しないと他人の情報を見れてしまう。ここでは処置しているのでエラーになる。<br>
<br>
 -->
</body>
</html>