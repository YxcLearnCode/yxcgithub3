<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>用户登录成功</title>  
</head>  
<body>
<%@ include file="nav.html"%>
<h1>操作成功了</h1>  
后台返回的信息：${msg.getUser_name()}  
</body>  
</html>  