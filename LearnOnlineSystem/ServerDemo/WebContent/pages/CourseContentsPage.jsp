<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>科目管理</title>
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
			<h2>科目内容管理</h2>
		</div>
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>&nbsp;</td>
				<td>ID</td>
				<td>内容名字</td>
				<td>操作</td>
			</tr>
			<tr>
				<td colspan="5" class="danger"><button
						class="btn btn-primary btn-sm">增加</button></td>
			</tr>
		</table>
	</div>
	<script>
		ajaxfun("getcontents", '${msg}', add);
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
						ajaxfun("getcontents", '${msg}', add);
					}
				}
			});
		}

		//增加行方法
		var contentsdata;
		function add(data) {
			contentsdata = data;
			$("tr:not(tr:first,tr:last)").remove(); //每次增加行前删除前面的行，否则会重复增加
			for (x in data) { //遍历JSON格式的数组取元素, x代表下标				 		
				$(
						"<tr index='" + data[x].id +"'>"
								+ "<td>"
								+ (Number(x) + 1)
								+ "</td>"
								+ "<td>"
								+ data[x].id
								+ "</td>"
								+ "<td>"
								+ data[x].content_name
								+ "</td> "
								+ "<td> <button class='btn btn-danger btn-sm'>删除</button>&nbsp;&nbsp;"
								+ "<button class='btn btn-read btn-sm'>查看</button></td>"
								+ "</tr>").insertBefore("tr:last");
			}
		}
		
		//查看内容
		$('table').on('click', '.btn-read', function() {			
			var index = Number($(this).parent().parent().attr("index"));	
			if (null != index) {
				//ajaxfun("trunto", {"page":"ContentDetailPage","data":JSON.stringify(contentsdata[id])}, null);
				window.location.href="trunto?page=ContentDetailPage&data="+ index;
			}			
		})
 
		//点击增加按钮事件
		$('.btn-primary').click(function() {
			var name = prompt("输入内容标题");
			if (null != name && "" != name) {
				var obj = {};
				obj.course_id = '${msg}'
				obj.content_name = name
				ajaxfun("addcontent", JSON.stringify(obj),null);				
			}
		})

		//删除行方法，事件委派，根据当前点击的按钮的行的索引值
		$('table').on('click', '.btn-danger', function() {
			var index = Number($(this).parent().parent().attr("index"));
			ajaxfun("delcontent", index, null);
		})
	</script>

</body>
</html>
