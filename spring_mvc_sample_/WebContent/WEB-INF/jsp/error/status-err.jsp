<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><% 
	int status = response.getStatus();
	String msg = "";

	switch(status){
	case 400:
		msg = "Bad Request (タイプミス等、リクエストにエラーがあります)";
		break;
	case 401:
		msg = "Unauthorixed (認証に失敗しました)";
		break;
	case 402:
		msg = "Payment Required";
		break;
	case 403:
		msg = "Forbidden (アクセス権がありません)";
		break;
	case 404:
		msg = "Not Found (該当アドレスのページはありません、またはアクセス権がありません)";
		break;
	case 405:
		msg = "Method Not Allowed (許可されていないメソッドタイプのリクエストを受けました)";
		break;
	case 406:
		msg = "Not Acceptable Accept (Acceptが受け入れられない内容でした)";
		break;
	case 407:
		msg = "Proxy Authentication Required (Proxy認証が必要です)";
		break;
	case 408:
		msg = "Request Timeout (リクエストがタイムアウトしました)";
		break;
	case 409:
		msg = "Conflict (そのリクエストは現在の状態のリソースと矛盾するため完了できませんでした)";
		break;
	case 410:
		msg = "Gone (そのリクエストは永続的に利用でません)";
		break;
	case 411:
		msg = "Length Required (Content-Lengthの無いリクエストを拒否しました)";
		break;
	case 412:
		msg = "Precondition Failed (前提条件で不正であると判断しました)";
		break;
	case 413:
		msg = "Request Entity Too Large (処理可能量より大きいリクエストのため拒否しました)";
		break;
	case 414:
		msg = "Request-URI Too Large (Request-URIが長すぎるため拒否しました)";
		break;
	case 415:
		msg = "Unsupported Media Type (サポートしていないメディアタイプを拒否しました)";
		break;
	case 416:
		msg = "Requested range not satisfiable (サイズなどの大きさが範囲内にありません)";
		break;
	case 417:
		msg = "Expectation Failed Expect (Expect拡張が受け入れられられませんでした)";
		break;
		
	case 500:
		msg = "Internal Server Error CGI (スクリプトなどでエラーが発生しました)";
		break; 
	case 501:
		msg = "Not Implemented (リクエストを実行するための必要な機能をサポートしていません)";
		break;
	case 502:
		msg = "Bad Gateway (不正なゲートウェイのリクエストを受け取り、拒否しました)";
		break;
	case 503:
		msg = "いずれかのエラーが発生しています。<br>Service Unavailable、Mapping Server Error、Too many users、Method Not Allowed";
		break;
	case 504:
		msg = "Gateway Timeout (ゲートウェイがタイムアウトしました)";
		break;
	case 505:
		msg = "HTTP Version not supported (サポートされていないHTTPプロトコルバージョンを受けました)";
		break;
	}
%><!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>エラー</title>
</head>
<body>
エラー(<%=status%>)<br>
<%=msg %><br>
<br>
<input type="button" value="戻る" onclick="javascript: history.back();">　
<form action="<c:url value='/top.html'/>" method="get" style="display: inline;">
<input type="submit" value="トップ" >　
</form>


</body>
</html>