<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>进度管理</title>
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
			<h2>用户  ${msg} 进度管理</h2>
		</div>
		<table class="table table-bordered table-hover">
			<tr class="info">
				<td>&nbsp;</td>
				<td>ID</td>
				<td>科目</td>
				<td>状态</td>
				<td>进度</td>
			</tr>
			<tr>				
			</tr>
		</table>
	</div>
	<script>
	    ajaxfun("getuserstudy", '${msg}', getstudy)
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
						ajaxfun("getuserstudy", '${msg}', getstudy);
					}
				}
			});
		}

		
		//获取学习状态
		var studydata
    	function getstudy(data){
			console.log(data)
			studydata = data
			ajaxfun("getcourse", null , add)
    	}
				
		
		//增加行方法
		var coursedata
		function add(data) {
			coursedata = data
			$("tr:not(tr:first,tr:last)").remove(); //每次增加行前删除前面的行，否则会重复增加
			for (x in coursedata) { //遍历JSON格式的数组取元素, x代表下标
						
				var isfinish = "未完成"
				var percent = 0
				for(y in studydata){
					if(studydata[y].course_id == coursedata[x].course_id){
						percent = studydata[y].percent
						if(studydata[y].percent == 100){
							isfinish = "已完成"
						}
					}					
				}
				
					
				//输出列表
				$(
						"<tr index='" + Number(x) +"'>"
								+ "<td>"
								+ (Number(x) + 1)
								+ "</td>"
								+ "<td>"
								+ coursedata[x].course_id
								+ "</td>"
								+ "<td>"
								+ coursedata[x].course_name
								+ "</td> "
								+ "<td>"
								+ isfinish
								+ "</td> "
								+ "<td>"
								+ percent
								+ "%</td> "						
								+ "</tr>").insertBefore("tr:last");
			}
		}
		
		//提醒按钮
		$('table').on('click', '.btn-read', function() {			
			var index = Number($(this).parent().parent().attr("index"));	
			if (null != index) {
				//ajaxfun("trunto", {"page":"ContentDetailPage","data":JSON.stringify(coursedata[id])}, null)			
				//window.location.href="trunto?page=ContentDetailPage&data="+ index
				if("提醒" == $(this).text()){
		    		$(this).text("已提醒")
			    	$(this).attr("disabled", true);		    		
		    		
		    		//科目名字
		    		var msg = {}  //提醒消息
		    		msg.user_id =  coursedata[Number(index)].user_id;
				    for(a in coursedata){
				    	if(Number(coursedata[a].course_id) == coursedata[Number(index)].course_id){
				    		msg.message = "是时候学习" + coursedata[a].course_name + "了";
				    	}
				    }
				    
		    		ajaxfun("addmessage", JSON.stringify(msg), success)
				}else{
					alert($(this).text())
				}
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
		
		
		//操作完成提示
		function success(data){
			alert("操作已完成")
		}
	</script>

</body>
</html>
