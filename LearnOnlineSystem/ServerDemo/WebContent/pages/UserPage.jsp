<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" href="css/course.css">
<style media="screen">
body {
	width: 100%;
}

.table {
	text-align: center;
}
</style>

</head>
<body>
	<%@ include file="nav.html"%>
	<div style="width: 800px; margin: 0 auto;">
		<div style="text-align: center;">
			<h2>用户管理</h2>
		</div>
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>&nbsp;</td>
				<td>ID</td>
				<td>名字</td>
				<td>操作</td>
			</tr>
			<tr>
				<td colspan="4" class="danger"><button
						class="btn btn-primary btn-sm">增加</button></td>
			</tr>
		</table>
	</div>
	<script>
		ajaxfun("getusers", null,add);
		function ajaxfun(url, data, func) {
			$.ajax({
				url : url, //获取数据列 
				type : 'POST',
				dataType : "json",
				data : {
					"data" : data
				},
				success : function(data) {
					if(null != func){
						func(data);
					}else{
						ajaxfun("getusers", null,add);
					}
				}
			});
		}
		
		
		//获取学习状态
		var studydata
    	function getstudy(data){
			console.log(data)
			studydata = data
			ajaxfun("getcourse", userid , getcourse)
    	}
		
		//获取科目
		var coursedata
    	function getcourse(data){
			console.log(data)
			coursedata = data
			ajaxfun("getusers", data, add)
    	}

		//增加行方法
		var userdata
		function add(data) {
			userdata=data
			
			$("tr:not(tr:first,tr:last)").remove(); //每次增加行前删除前面的行，否则会重复增加
			for (x in data) { //遍历JSON格式的数组取元素, x代表下标
						
				$(
						"<tr index='" + data[x].user_id +"'>"
								+ "<td>"
								+ (Number(x) + 1)
								+ "</td>"
								+ "<td>"
								+ data[x].user_id
								+ "</td>"
								+ "<td>"
								+ data[x].user_name
								+ "</td> "
								+ "<td><button class='btn btn-read btn-learn btn-sm'>学习进度查看</button>"
								+ "&nbsp;&nbsp;<button class='btn btn-read btn-plan btn-sm'>学习计划查看 </button>"
								+ "&nbsp;&nbsp;<button class='btn btn-read btn-exam btn-sm'>考试管理</button>"
								+ "&nbsp;&nbsp;<button class='btn btn-read btn-update btn-sm'>更改</button>"
								+ "&nbsp;&nbsp;<button class='btn btn-danger btn-sm'>删除</button></td>"
								+ "</tr>").insertBefore("tr:last");
			}
		}
		
		
		//用户学习进度信息
		$('table').on('click', '.btn-learn', function() {			
			var index = Number($(this).parent().parent().attr("index"));
			if (null != index && "" != index) {					
				//var str = JSON.stringify(userdata[index])
				window.location.href="trunto?page=LearnPage&data=" + index; 
			}
		})
		
		
		//用户考试信息
		$('table').on('click', '.btn-exam', function() {			
			var index = Number($(this).parent().parent().attr("index"));
			if (null != index && "" != index) {					
				//var str = JSON.stringify(userdata[index])
				window.location.href="trunto?page=ExamResultPage&data=" + index; 
			}
		})
		
		//用户计划信息
		$('table').on('click', '.btn-plan', function() {			
			var index = Number($(this).parent().parent().attr("index"));
			if (null != index && "" != index) {					
				//var str = JSON.stringify(userdata[index])
				window.location.href="trunto?page=PlanPage&data=" + index; 
			}
		})
		
		//更改用户信息
		$('table').on('click', '.btn-update', function() {			
			var index = Number($(this).parent().parent().attr("index"));
			if (null != index && "" != index) {					
				//var str = JSON.stringify(userdata[index])
				window.location.href="trunto?page=UserCorUPage&data=" + index; 
			}
		})
 
		//点击增加按钮事件
		$('.btn-primary').click(function() {
			window.location.href="trunto?page=UserCorUPage";
		})

		//删除行方法，事件委派，根据当前点击的按钮的行的索引值
		$('table').on('click', '.btn-danger', function() {
			var index = Number($(this).parent().parent().attr("index"));
			ajaxfun("deluser", index, null);			
		})
		
	</script>

</body>
</html>
