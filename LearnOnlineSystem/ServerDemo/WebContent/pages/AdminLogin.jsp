<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>  
<title>Login Form</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/login.css" rel='stylesheet' type='text/css' />

</head>
<body>
	<div class="main">
		<div class="login">
			<h1>在线学习管理系统</h1>
			<div class="inset">
				<!--start-main-->
				<form action="adminlogin">
			         <div>
			         	<h2>管理员登录</h2>
						<span><label>用户名</label></span>
						<span><input name="id" type="text" class="id" ></span>
					 </div>
					 <div>
						<span><label>密码</label></span>
					    <span><input name="password" type="password" class="password"></span>
					 </div>
					<div style="text-align:center;"><p style="color:#f00; font-size:12px;">${msg}</p></div>
					<div class="sign">
                        <input type="submit" value="登录" class="submit" />
					</div>
					</form>
				</div>
			</div>
		<!--//end-main-->
		</div>

</body>  
</html>  