<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传试题</title>
<link rel="stylesheet" href="css/upload.css" type="text/css">
<script type="text/javascript" src="js/json2.js"></script>

</head>
<body>

	<%@ include file="nav.html"%>
	
	<div style="width: 800px; margin: 0 auto;">

		<div class="report-file">
		    
			<span>上传文件</span><input class="file-prew" type="file"
				onchange="processFiles(this.files)">
							
		</div>
		
		<div id="examcontent" style="overflow: auto"></div>

		<div class="report-file" id="submitexam" style="margin-bottom: 20px">
		<select class="filter" onclick="choosecourse(this)">
				<option value="new" disabled selected>科目列表</option>
		</select>
			<span onclick="submitexamdata()">确认上传</span>
		</div>
		
		
		<script type="text/javascript">
			$('#submitexam').hide();

			//判断浏览器是否支持FileReader接口 
			if (typeof FileReader == 'undefined') {
				alert("你的浏览器不支持FileReader接口！")
				//使选择控件不可操作 
				file.setAttribute("disabled", "disabled");
			}

			var data;//试卷对象			
			function processFiles(files) {
				var file = files[0];
				var reader = new FileReader();
				reader.onload = function(e) {
					// 这个事件发生，意为着数据准备好了
					// 把它复制到页面的<div>元素中
					var arr = e.target.result.trim().split('question:')
					data = {}
					data.exam_name = arr[0].split('title:')[1]
					var exam_content = []
					for (var i = 1; i < arr.length; i++) {
						var item = arr[i].split('\n')
						var topic = {}
						topic.question = item[0].trim()

						for (var j = 1; j < item.length; j++) {
							if (item[j].indexOf("answer:") != -1) {
								item[j] = item[j].split('answer:')[1]
								topic.answer = j-1
							}
							var topicoption = item[j].trim()
							if("" != topicoption){
								if(1 == j){
									topic.option_a = topicoption
								}else if(2 == j){
									topic.option_b = topicoption
								}else if(3 == j){
									topic.option_c = topicoption
								}else if(4 == j){
									topic.option_d = topicoption
								}								
							}							
						}
						if(null == topic.option_b){
							topic.type = 0             //填空题
						}else if(null != topic.option_d){
							topic.type = 1              //选择题
						}else if(null != topic.option_c){
							topic.type = 2              //简答题
						}else{
							topic.type = 3              //判断题
						}						
						exam_content.push(topic)
					}
					data.exam_content = exam_content
					console.log(exam_content);
					if ("" != data.title) {
						$('#submitexam').show()
						showexam()
					} else {
						alert('读取试题出错,请确认格式')
						$('#submitexam').css("display", "none");
					}
				};
				reader.readAsText(file, "gb2312");
			}

			function showexam() {
				$('#examcontent').empty()
				$('#examcontent').append(
						"<h2 style='text-align:center'>" + data.exam_name
								+ "</h2><br>")
                var obj = data.exam_content
				for (x in obj) {
					var html = "<br><h3>" + (Number(x) + 1) + "、"
							+ obj[x].question + "</h3><br>" //题目
					if (null != obj[x].option_a) {
						html += "<p>A、" + obj[x].option_a + "<p>"
					}
					if (null != obj[x].option_b) {
						html += "<p>B、" + obj[x].option_b + "<p>"
					}
					if (null != obj[x].option_c) {
						html += "<p>C、" + obj[x].option_c + "<p>"
					}
					if (null != obj[x].option_d) {
						html += "<p>D、" + obj[x].option_d + "<p>"
					}
					
					if(obj[x].type == 2){
						html += "<p style='color:#f00'>关键字:"
						html += obj[x].option_a+"&nbsp;"
						html += obj[x].option_b+"&nbsp;"
						html += obj[x].option_c+"&nbsp;" + "</p>"	
					}else{
						html += "<p style='color:#f00'>正确答案:"
						if(0 == obj[x].answer){
							html += "A</p>"	
						}else if(1 == obj[x].answer){
							html += "B</p>"	
						}else if(2 == obj[x].answer){
							html += "C</p>"	
						}else if(3 == obj[x].answer){
							html += "D</p>"	
						}										
					}
					
					$('#examcontent').append(html)
				}
			}

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
						if(null != func){
							func(data)
						}
					}
				});
			}

			//科目选择
			var index = "0";
			function choosecourse(courseselect) {
				var selectedOption = courseselect.options[courseselect.selectedIndex];
				if (selectedOption.value != index && selectedOption.value != "new") {					
					if("new" != selectedOption.value){
						index = selectedOption.value;						
					}
				}
			}

			//增加科目列表item方法
			function showcourse(data) {
				$("option:not(option:first)").remove(); //每次增加行前删除前面的行，否则会重复增加
				for (x in data) { //遍历JSON格式的数组取元素, x代表下标
					var html = '<option value="'+data[x].course_id+'">'
							+ data[x].course_name + '</option>';
					$(".filter").append(html);
				}
			}
			
			//提交
			function submitexamdata(){
				data.course_id = index;
				if(null != data.course_id && 0 != data.course_id){
					for(x in data.exam_content){
						data.exam_content[x].course_id = data.course_id
					}
			    	ajaxfun("addtopic", JSON.stringify(data.exam_content), null);	
			    	alert("操作成功");
			    	$('#examcontent').empty()
			    	$('#submitexam').hide()			    	
				}else{
					alert("请选择科目");
				}
			}
			
		</script>
	</div>
</body>
</html>