<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>管理页</title>
<meta name="menu" content="addInformation" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jquery/jquery.datepick.css'/>" />
<script type="text/javascript"
	src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/jquery/jquery.datepick.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/jquery/jquery.datepick-zh-CN.js'/>"></script>
<style type="text/css">
.error {
	color: red;
}
</style>
</head>

<body>
	<h1 align="center">添加信息</h1>
	<div style="margin:20px 0px;">
		<form action="/add.do?action=addInformation" method="post">
			<table cellpadding="4"
				cellspacing="0" border="0">
				<thead>
					<tr>
						<td width="20%">字段</td>
						<td width="60%">内容</td>
					</tr>
				</thead>
				<tbody>
					<c:if test="${information != null }">

						<tr>
							<td>标题:</td>
							<td><input type="hidden" name="id"
								value="${information.getID() }" /> <input type="text"
								name="title" value="${information.getTitle() }" id="title" /></td>
							<td><label id="titleError" class="error"></label></td>
						</tr>
						<tr>
							<td>日期:</td>
							<td><input type="text" name="date" id="date"
								readonly="readonly" value="${information.getDate() }" /></td>
							<td><label id="dateError" class="error"></label></td>
						</tr>
						<tr>
							<td>时间:</td>
							<td><select name="hour">
									<option><c:out value="${information.getHour() }"></c:out></option>
									<%
										for (int i = 0; i < 24; i++) {
									%>
									<option><%=i%></option>
									<%
										}
									%>
							</select> <label>时</label> <select name="minute">
									<option><c:out value="${information.getMinute() }"></c:out></option>
									<%
										for (int i = 0; i < 60; i++) {
									%>
									<option><%=i%></option>
									<%
										}
									%>
							</select> <label>分</label></td>
						</tr>
						<tr>
							<td>类型:</td>
							<td><select name="type" id="type">
									<option><c:out value="${information.getType() }"></c:out></option>
									<option>宣讲会</option>
									<option>竞赛信息</option>
									<option>讲座</option>
									<option>其他</option>
							</select></td>
						</tr>
						<tr>
							<td>内容:</td>
							<td><textarea id="content" name="content" onblur="return checkArea(this.value)"
									style="width:380px; height:80px;"><c:out
										value="${information.getContent() }"></c:out></textarea></td>
							<td><label id="contentError" class="error"></label></td>
						</tr>
						<tr>
							<td></td>
							<td><input type="submit" value="提交" onclick="return check()" />
								<input type="reset" value="取消" onclick="cancle()" /></td>
						</tr>
					</c:if>
					<c:if test="${information eq null }">
						<tr>
							<td>标题:</td>
							<td><input type="hidden" name="id" value="-1" /> <input
								type="text" name="title" id="title" /></td>
							<td><label id="titleError" class="error"></label></td>
						</tr>
						<tr>
							<td>日期:</td>
							<td><input type="text" name="date" id="date"
								readonly="readonly" /></td>
							<td><label id="dateError" class="error"></label></td>
						</tr>
						<tr>
							<td>时间:</td>
							<td><select name="hour">
									<%
										for (int i = 0; i < 24; i++) {
									%>
									<option><%=i%></option>
									<%
										}
									%>
							</select> <label>时</label> <select name="minute">
									<%
										for (int i = 0; i < 60; i++) {
									%>
									<option><%=i%></option>
									<%
										}
									%>
							</select> <label>分</label></td>
						</tr>
						<tr>
							<td>类型:</td>
							<td><select name="type">
									<option>宣讲会</option>
									<option>竞赛信息</option>
									<option>讲座</option>
									<option>其他</option>
							</select></td>
						</tr>
						<tr>
							<td>内容:</td>
							<td><textarea id="content" name="content" onblur="return checkArea(this.value)"
									style="width:380px; height:80px;"></textarea></td>
							<td><label id="contentError" class="error"></label></td>
						</tr>
						<tr>
							<td></td>
							<td><input type="submit" value="提交" onclick="return check()" />
								<input type="reset" value="取消" /></td>
						</tr>
					</c:if>

				</tbody>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		area = false ;
		//用户点击取消按钮时
		function cancle() {
			history.back();
		}
		
		function checkArea(value){
			if(value == "" || value == null)
			{
				area = true ;
			}else{
				area = false ;
			}
		}
		//验证输入的内容是否为空
		function check(value) {
			isNull = false;
			$(".error").text("");
			if (!$(":text[name=title]").val()) {
				$("#titleError").text("标题不能为空");
				isNull = true;
			}
			if (!$(":text[name=date]").val()) {
				$("#dateError").text("日期不能为空");
				isNull = true;
			}
			if(area){
				$("#contentError").text("内容不能为空") ;
				isNull = true ;
			}else{
				isNull = false ;
			}
			if (isNull) {
				return false;
			}
		}
		//<![CDATA[
		$(function() {
			$("#date").datepick({
				dateFormat : "yy-mm-dd"
			});
		});
		$(function() {
			$('#tableList').tablesorter();
			//$('#tableList').tablesorter( {sortList: [[0,0], [1,0]]} );
			//$('table tr:nth-child(odd)').addClass('odd');
			$('table tr:nth-child(even)').addClass('even');
		});
		//]]>
	</script>
</body>
</html>
