<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>内容管理</title>
<link rel="stylesheet" href="css/course.css">
<link rel="stylesheet" href="css/upload.css" type="text/css">
<style media="screen">
body {
	width: 100%;
}

.form-control {
	display: block;
	width: 100%;
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 1.428571429;
	color: #555555;
	vertical-align: middle;
	background-color: #ffffff;
	border: 1px solid #cccccc;
	border-radius: 4px;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
	-webkit-transition: border-color ease-in-out .15s, box-shadow
		ease-in-out .15s;
	transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
}

.form-control:focus {
	border-color: #66afe9;
	outline: 0;
	-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px
		rgba(102, 175, 233, 0.6);
	box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px
		rgba(102, 175, 233, 0.6);
}

.text {
	line-height: 1.5em;
	font-family: "Times New Roman", Times, serif;
	font-size: 14px;
}
</style>

</head>
<body>
	<%@ include file="nav.html"%>
	<div style="width: 800px; margin: 0 auto;">
		<div style="text-align: center;">
			<h2>内容管理</h2>
			<br> <input id="title" type="text" class="form-control">
			<br> <br>
			<textarea id="content" class="form-control" name="content" rows="20"
				cols="80"
				onpropertychange="if(this.scrollHeight>80) this.style.posHeight=this.scrollHeight+5">输入几个回车试试</textarea>

			<div class="report-file" id="submitexam" onclick="update()" style="margin-bottom: 20px">
				</select> <span >确认上传</span>
			</div>
		</div>
	</div>

	<script type="text/javascript">
		ajaxfun("getcontent", '${msg}', showContent);
		function ajaxfun(url, data, func) {
			$.ajax({
				url : url, //获取数据列 
				type : 'POST',
				dataType : "json",
				data : {
					"data" : data
				},
				success : function(data) {
					if (null != func) {
						func(data);
					}
				}
			});
		}

		//显示内容信息
		var contentdata;
		function showContent(data) {
			contentdata = data;
			if (null != data) {
				$("#title").val(data.content_name);
				$("#content").val(data.text);
			}
		}
		
		function success(data){
			alert("操作成功");
		}
		
		function update(){
			contentdata.content_name = $("#title").val();
			contentdata.text = $("#content").val();
			if(null != contentdata.content_name && null != contentdata.text){
				ajaxfun("modifycontent", JSON.stringify(contentdata), success);				
			}
		}
	</script>

</body>
</html>
