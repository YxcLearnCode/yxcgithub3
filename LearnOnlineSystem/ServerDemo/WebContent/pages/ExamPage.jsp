<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>试题管理</title>
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
			<h2>试题管理</h2>
		</div>
		<div class="filter-box">
			<select id="course_select" class="filter" onclick="choosecourse(this)">				
			</select>
			
			<select id="type_select" class="filter" onclick="choosetype(this)">
				<option value="new" disabled selected>题目类型</option>
				<option value="0">填空题</option>
				<option value="1">选择题</option>
				<option value="2">简答题</option>
			</select>
		</div>
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>&nbsp;</td>
				<td>ID</td>
				<td>选项</td>
				<td>答案</td>
				<td>删除试题</td>
			</tr>
			<tr>
				<td colspan="5" class="danger"><button
						class="btn btn-primary btn-sm">上传试题</button></td>
			</tr>
		</table>
		<div style="margin:30px">
	    	<div id="examcontent"></div>
		</div>
	</div>

	<script>
		ajaxfun("getcourse", null, showcourse);
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
					} else {
						ajaxfun("getcourse", null, showcourse);
					}
				}
			});
		}

		//科目选择
		var courseindex = "0";
		var typeindex = "-1";
		var topicdata = {}
		function choosecourse(courseselect) {
			var selectedOption = courseselect.options[courseselect.selectedIndex];
			if (selectedOption.value != courseindex && selectedOption.value != "new") {
				courseindex = selectedOption.value;
				if("-1" != typeindex){
					topicdata.type = typeindex
					topicdata.course_id = courseindex
			    	ajaxfun("gettopicbytype", JSON.stringify(topicdata), add);		
				}
			}

		}
		
		function choosetype(typeselect) {
			var selectedOption = typeselect.options[typeselect.selectedIndex];
			if (selectedOption.value != typeindex && selectedOption.value != "new") {
				typeindex = selectedOption.value;				
				if("-1" != courseindex){
					topicdata.type = typeindex
					topicdata.course_id = courseindex
			    	ajaxfun("gettopicbytype", JSON.stringify(topicdata), add);
				}
			}

		}

		//增加科目列表item方法
		function showcourse(data) {
			$("#course_select").empty();     //每次增加行前删除前面的行，否则会重复增加				
			var html = '<option value="new" disabled selected>科目列表</option>'
			$("#course_select").append(html);
			for (x in data) { //遍历JSON格式的数组取元素, x代表下标				
				html = '<option value="'+data[x].course_id+'">'
						+ data[x].course_name + '</option>';
				$("#course_select").append(html);
			}
		}

		//增加行方法
		var examdata;
		function add(data) {
			examdata = data;
			console.log(data);
			$("tr:not(tr:first,tr:last)").remove(); //每次增加行前删除前面的行，否则会重复增加
			for (x in data) { //遍历JSON格式的数组取元素, x代表下标
				var options;
				var answer;
				if(data[x].type != 2){
			    	options = data[x].option_a!=null?"A、"+ data[x].option_a:"";
			    	options += data[x].option_b!=null?"<br>B、"+ data[x].option_b:"";
			    	options += data[x].option_c!=null?"<br>C、"+ data[x].option_c:"";
		     		options += data[x].option_d!=null?"<br>D、"+ data[x].option_d:"";
				
		    		if(data[x].answer == 0){
		    			answer = "A"
		    		}else if(data[x].answer == 1){
			    		answer = "B"
			    	}else if(data[x].answer == 2){
		    			answer = "C"
		    		}else if(data[x].answer == 3){
		    			answer = "D"
		    		}
				}else{
					options=""
					answer = "关键词:"+ data[x].option_a + "&nbsp;"+data[x].option_b + "&nbsp;"+data[x].option_c + "&nbsp;";				
				}
				
				$(
						"<tr index='" + data[x].id +"'>"
								+ "<td>"
								+ (Number(x) + 1)
								+ "</td>"
								+ "<td>"
								+ data[x].question
								+ "</td>"
								+ "<td>"
								+ options
								+ "</td> "
								+ "<td>"
								+ answer
								+ "</td> "
								+ "<td> <button class='btn btn-danger btn-sm'>删除</button></td>"
								+ "</tr>").insertBefore("tr:last");
			}
		}

		//点击增加按钮事件
		$('.btn-primary').click(function() {
			window.location.href = "trunto?page=UploadPage";
		})

		//删除行方法，事件委派，根据当前点击的按钮的行的索引值
		$('table').on('click', '.btn-danger', function() {
			var index = $(this).parent().parent().attr("index");
			$(this).parent().parent().remove();
			ajaxfun("deltopic", index, null);			
		})
	</script>

</body>
</html>
