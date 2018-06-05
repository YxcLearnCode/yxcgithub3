<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>创建科目</title>
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
	<div style="width: 300px; margin: 0 auto;">
		<div style="text-align: center;">
			<h2 id="title">新增科目</h2>
			<br><br> <input id="name" name="name" type="text" class="form-control" placeholder="输入科目名">
			<br><br> <input id="teacher" name="teacher" type="text" class="form-control" placeholder="输入教师">
			<br><br> <textarea id="introduce" class="form-control" name="introduce" rows="20"
				cols="80"
				onpropertychange="if(this.scrollHeight>80) this.style.posHeight=this.scrollHeight+5">输入简介</textarea>
			<br><br>
			 <span class="btn btn-primary btn-sm">确认</span>
		</div>
	</div>

	<script type="text/javascript">
	    
	    $("#title").html("创建科目")
	    
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

		
		//点击增加按钮事件
		$('.btn-primary').click(function() {
			var newdata = {}
			newdata.course_name = $("#name").val();
			newdata.course_teacher = $("#teacher").val();
			newdata.course_introduction = $("#introduce").val();
			
			ajaxfun("addcourse", JSON.stringify(newdata), success);
			   
		})

		
		function success(data){
			alert("操作成功");
		}
		
	</script>

</body>
</html>
