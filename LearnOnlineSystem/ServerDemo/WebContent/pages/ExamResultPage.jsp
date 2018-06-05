<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>考试题目管理</title>
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
			<h2>用户${msg}考试内容管理</h2>
		</div>
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>&nbsp;</td>
				<td>课程id</td>
				<td>试题</td>
				<td>成绩</td>
				<td>操作</td>
			</tr>
			<tr>				
			</tr>
		</table>
		
		<div style="margin:30px">
	    	<div id="examcontent"></div>
		</div>
	</div>
	<script>
		ajaxfun("getexamresult", '${msg}', add);
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
						ajaxfun("getexamresult", '${msg}', add);
					}
				}
			});
		}


		//计算成绩
		var examdata;
		var a = 2;   //填空题计分
		var b = 2;   //选择题计分
		var c = 30;  //简答题计分
		function calculatescore(data){
			
			examdata = data
			for(x in examdata){
				var score = 0
				var topics = examdata[x].topics	
				var answers = examdata[x].answers
				
				for(y in topics){
					if(answers.length > y){
					if(0 == topics[y].type){        //填空题判断 
						if(topics[y].option_a == answers[y].answer){
							score += a;
						}
					}else if(1 == topics[y].type){  //选择题判断
						if(topics[y].answer == answers[y].answer){
							score += b;
						}
					}else if(2 == topics[y].type){  //简答题判断 
						if(answers[y].answer != null){
						if(answers[y].answer.indexOf(topics[y].option_a) >= 0){
							score += c/3;
						}
						if(answers[y].answer.indexOf(topics[y].option_b) >= 0){
							score += c/3;
						}
						if(answers[y].answer.indexOf(topics[y].option_c) >= 0){
							score += c/3;
						}
						}
					}
					}
				}				
				examdata[x].score = score
				//topics.length != 0?Math.round(score*100/topics.length):0
			}
		}
		
		
		//增加行方法
		function add(data) {
			console.log(data)
			if(data.length == 0){
				alert("无数据！")
				return
			}
			calculatescore(data)
			$("tr:not(tr:first,tr:last)").remove(); //每次增加行前删除前面的行，否则会重复增加
			for (x in examdata) { //遍历JSON格式的数组取元素, x代表下标
				
				$(
						"<tr index='" + examdata[x].exam_id +"'>"
								+ "<td>"
								+ (Number(x) + 1)
								+ "</td>"
								+ "<td>"
								+ examdata[x].course_id
								+ "</td>"
								+ "<td>"
								+ examdata[x].exam_id
								+ "</td> "
								+ "<td>"
								+ examdata[x].score
								+ "分</td>"
								+ "<td><button class='btn btn-read btn-exam btn-sm'>查看试题</button>"
								+ "&nbsp;&nbsp;<button class='btn btn-read btn-error btn-sm'>查看错题</button></td>"
								+ "</tr>").insertBefore("tr:last");
			}
		}
		
		//查看内容
		$('table').on('click', '.btn-exam', function() {			
			var index = $(this).parent().parent().attr("index");
			var exam;
			for(y in examdata){
				if(examdata[y].exam_id == index){
					exam = examdata[y]
				}
			}

			$('#examcontent').empty()
			$('#examcontent').append(
					"<h2 style='text-align:center'>试题  " + exam.exam_id
							+ " 题目</h2><br>")
			var topicsArray = exam.topics
			var answerArray = exam.answers
			
			for (x in topicsArray) {
					var html = "<br><h3>" + (Number(x) + 1) + "、"
							+ topicsArray[x].question + "</h3><br>" //题目
					if (null != topicsArray[x].option_a) {
						html += "<p>A、" + topicsArray[x].option_a + "<p>"
					}
					if (null != topicsArray[x].option_b) {
						html += "<p>B、" + topicsArray[x].option_b + "<p>"
					}
					if (null != topicsArray[x].option_c) {
						html += "<p>C、" + topicsArray[x].option_c + "<p>"
					}
					if (null != topicsArray[x].option_d) {
						html += "<p>D、" + topicsArray[x].option_d + "<p>"
					}
					
					var answer = answerArray.length>x?answerArray[x].answer:""
					if(topicsArray[x].type == 2){
						html += "<p style='color:#f00'>关键字:"
						html += topicsArray[x].option_a+"&nbsp;"
						html += topicsArray[x].option_b+"&nbsp;"
						html += topicsArray[x].option_c+"&nbsp;" 
						html += "<br>用户答案:" + answer
						html += "<br></p>"
						html += "</p>"	
					}else{
						//正确答案
						html += "<p style='color:#f00'>正确答案:"
						if(0 == topicsArray[x].answer){
							html += "A<br>"	
						}else if(1 == topicsArray[x].answer){
							html += "B<br>"	
						}else if(2 == topicsArray[x].answer){
							html += "C<br>"	
						}else if(3 == topicsArray[x].answer){
							html += "D<br>"	
						}
						
						if(topicsArray[x].type == 1){
						//用户提交的答案						
						if(0 == answerArray[x].answer){
							html += "<br>用户答案:A<br>"	
						}else if(1 == answerArray[x].answer){
							html += "<br>用户答案:B<br>"	
						}else if(2 == answerArray[x].answer){
							html += "<br>用户答案:C<br>"	
						}else if(3 == answerArray[x].answer){
							html += "<br>用户答案:D<br>"	
						}
						}else if(topicsArray[x].type == 0){
							html += "<br>用户答案:"+ answer +"<br>"
						}
						html += "</p>" 
					}					
					$('#examcontent').append(html)	
			}
		})
		
		
				//查看内容
		$('table').on('click', '.btn-error', function() {			
			var index = $(this).parent().parent().attr("index");
			var exam;
			for(y in examdata){
				if(examdata[y].exam_id == index){
					exam = examdata[y]
				}
			}
			
			
			$('#examcontent').empty()
			$('#examcontent').append(
					"<h2 style='text-align:center'>试题" + exam.exam_id
							+ " 错题</h2><br>")
							
			var topicsArray = exam.topics
			var answerArray = exam.answers
			
			for (x in topicsArray) {
				if(answerArray[x].iserror == 1){  //错题
					var html = "<br><h3>" + (Number(x) + 1) + "、"
							+ topicsArray[x].question + "</h3><br>" //题目
					if (null != topicsArray[x].option_a) {
						html += "<p>A、" + topicsArray[x].option_a + "<p>"
					}
					if (null != topicsArray[x].option_b) {
						html += "<p>B、" + topicsArray[x].option_b + "<p>"
					}
					if (null != topicsArray[x].option_c) {
						html += "<p>C、" + topicsArray[x].option_c + "<p>"
					}
					if (null != topicsArray[x].option_d) {
						html += "<p>D、" + topicsArray[x].option_d + "<p>"
					}
					
					if(topicsArray[x].type == 2){
						html += "<p style='color:#f00'>关键字:"
						html += topicsArray[x].option_a+"&nbsp;"
						html += topicsArray[x].option_b+"&nbsp;"
						html += topicsArray[x].option_c+"&nbsp;" 
						html += "<br>用户答案:" + answerArray[x].answer
						html += "<br></p>"							
					}else{
						//正确答案
						html += "<p style='color:#f00'>正确答案:"
						if(0 == topicsArray[x].answer){
							html += "A<br>"	
						}else if(1 == topicsArray[x].answer){
							html += "B<br>"	
						}else if(2 == topicsArray[x].answer){
							html += "C<br>"	
						}else if(3 == topicsArray[x].answer){
							html += "D<br>"	
						}
						
						//用户提交的答案						
						if(topicsArray[x].type == 1){
							//用户提交的答案						
							if(0 == answerArray[x].answer){
								html += "<br>用户答案:A<br>"	
							}else if(1 == answerArray[x].answer){
								html += "<br>用户答案:B<br>"	
							}else if(2 == answerArray[x].answer){
								html += "<br>用户答案:C<br>"	
							}else if(3 == answerArray[x].answer){
								html += "<br>用户答案:D<br>"	
							}
							}else if(topicsArray[x].type == 0){
								html += "<br>用户答案:"+ answerArray[x].answer +"<br>"
							}
							html += "</p>"
					}					
					$('#examcontent').append(html)	
			}
			}
		})

	</script>

</body>
</html>
