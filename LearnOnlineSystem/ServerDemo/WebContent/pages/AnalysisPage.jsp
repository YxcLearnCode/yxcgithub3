<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计分析</title>
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
			<h2>统计分析</h2>
		</div>
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>&nbsp;</td>
				<td>ID</td>
				<td>科目</td>
				<td>错题占比</td>
			</tr>
			<tr>			
			</tr>
		</table>

        <p>    
           <canvas id="canvas_circle" width="290" height="200"> </canvas>    
        </p>  
	</div>
  
	
	
	<script>
		ajaxfun("geterrortopic", null,getcourse);
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
						ajaxfun("geterrortopic", null,getcourse);
					}
				}
			});
		}
		
		
		//获取科目
		var errortopicdata
    	function getcourse(data){
			console.log(data)
			errortopicdata = data
			ajaxfun("getcourse", data, add)
    	}

		
		//增加行方法
		var coursedata
		function add(data) {
			coursedata=data
			
			var arr_data = []
			var arr_text = []
		
			$("tr:not(tr:first,tr:last)").remove(); //每次增加行前删除前面的行，否则会重复增加
			for (x in data) { //遍历JSON格式的数组取元素, x代表下标
				
				//计算占比 proportion
				//每个科目错题占比计算
				var proportion = 0
				var count = 0
				var total = 0
				for(y in errortopicdata){
					if(errortopicdata[y].course_id == coursedata[x].course_id){
						count = errortopicdata[y].count;
					}
					total = total + errortopicdata[y].count
				}		
				proportion = total==0?0:count *100 / total
				
				arr_data.push(proportion/100)
				arr_text.push(data[x].course_name)

						
				$(
						"<tr index='" + data[x].user_id +"'>"
								+ "<td>"
								+ (Number(x) + 1)
								+ "</td>"
								+ "<td>"
								+ data[x].course_id
								+ "</td>"
								+ "<td>"
								+ data[x].course_name
								+ "</td> "	
								+ "<td>"
								+ Math.round(proportion)
								+ "</td> "	
								+ "</tr>").insertBefore("tr:last");
			}

			init(arr_text,arr_data)
		}
		
		
		//用户考试信息
		$('table').on('click', '.btn-learn', function() {			
			var index = Number($(this).parent().parent().attr("index"));
			if (null != index && "" != index) {					
				//var str = JSON.stringify(coursedata[index])
				window.location.href="trunto?page=LearnPage&data=" + index; 
			}
		})
		
		
		//用户考试信息
		$('table').on('click', '.btn-exam', function() {			
			var index = Number($(this).parent().parent().attr("index"));
			if (null != index && "" != index) {					
				//var str = JSON.stringify(coursedata[index])
				window.location.href="trunto?page=ExamResultPage&data=" + index; 
			}
		})
		
		
		//用户计划信息
		$('table').on('click', '.btn-plan', function() {			
			var index = Number($(this).parent().parent().attr("index"));
			if (null != index && "" != index) {					
				//var str = JSON.stringify(coursedata[index])
				window.location.href="trunto?page=PlanPage&data=" + index; 
			}
		})
		
		
		//更改用户信息
		$('table').on('click', '.btn-update', function() {			
			var index = Number($(this).parent().parent().attr("index"));
			if (null != index && "" != index) {					
				//var str = JSON.stringify(coursedata[index])
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
		
		init()
		//初始化饼图
	    function init(text_arr,data_arr) {    
            //绘制饼图    
            //比例数据和颜色    
            var color_arr = ["BLACK","Sienna","PURPLE","BLUE",'RED','YELLOW','GREEN','YELLOW',"Silver","Peru","Tan","Thistle","Plum","Lavender","LightCoral","Tomato","Gold","Violet","Plum"];     
    
            drawCircle("canvas_circle", data_arr, color_arr, text_arr);    
        } 
		
		
		//绘制饼图    
        function drawCircle(canvasId, data_arr, color_arr, text_arr)    
        {    
            var c = document.getElementById(canvasId);    
            var ctx = c.getContext("2d");   
    
            var radius = c.height / 2 - 20; //半径    
            var ox = radius + 20, oy = radius + 20; //圆心    
    
            var width = 30, height = 10; //图例宽和高    
            var posX = ox * 2 + 20, posY = 30;   //    
            var textX = posX + width + 5, textY = posY + 10;    
    
            var startAngle = 0; //起始弧度    
            var endAngle = 0;   //结束弧度    
            for (var i = 0; i < data_arr.length; i++)    
            {    
                //绘制饼图    
                endAngle = endAngle + data_arr[i] * Math.PI * 2; //结束弧度    
                ctx.fillStyle = color_arr[i];    
                ctx.beginPath();    
                ctx.moveTo(ox, oy); //移动到到圆心    
                ctx.arc(ox, oy, radius, startAngle, endAngle, false);    
                ctx.closePath();    
                ctx.fill();    
                startAngle = endAngle; //设置起始弧度    
    
                //绘制比例图及文字    
                ctx.fillStyle = color_arr[i];    
                ctx.fillRect(posX, posY + 20 * i, width, height);    
                ctx.moveTo(posX, posY + 20 * i);    
                ctx.font = 'bold 12px 微软雅黑';    //斜体 30像素 微软雅黑字体    
                 ctx.fillStyle = color_arr[i]; //"#000000";    
                    var percent = text_arr[i] + "：" + 100 * data_arr[i] + "%";    
                    ctx.fillText(percent, textX, textY + 20 * i);    
                }    
            }
		
	</script>

</body>
</html>
