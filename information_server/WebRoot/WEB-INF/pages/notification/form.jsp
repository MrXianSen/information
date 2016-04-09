<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>管理页</title>
<meta name="menu" content="notification" />
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

	<h1>发布推送消息</h1>

	<%--<div style="background:#eee; margin:20px 0px; padding:20px; width:500px; border:solid 1px #999;">--%>
	<div style="margin:20px 0px;">
		<form action="/notification.do?action=send" method="post"
			style="margin: 0px;">
			<table cellpadding="4" cellspacing="0" border="0">
				<tr>
					<td width="20%">To:</td>
					<td width="60%"><input type="radio" name="broadcast" value="0"
						checked="checked" /> 广播所有用户 <input type="radio" name="broadcast"
						value="1" /> 广播指定用户</td>
				</tr>
				<tr id="trUsername" style="display:none;">
					<td>Username:</td>
					<td><input type="text" id="username" name="username"
						style="width:380px;" /></td>
					<td><label id="userNameError" class="error"></label></td>
				</tr>
				<!-- 当没有传进来的消息时 -->
				<c:if test="${information != null }">
					<tr>
						<td>标题:</td>
						<td><input type="text" id="title" name="title"
							style="width:380px;" value="${information.getTitle() }" /></td>
						<td><label id="titleError" class="error"></label></td>
					</tr>
					<tr>
						<td>类型:</td>
						<td><select name="type">
								<option><c:out value="${information.getType() }"></c:out></option>
								<option>宣讲会</option>
								<option>竞赛信息</option>
								<option>讲座</option>
								<option>其他</option>
						</select></td>
					</tr>
					<tr>
						<td>日期:</td>
						<td><input type="text" name="date" id="date"
							readonly="readonly" value="${information.getDate() }" /></td>
						<td><label id="dateError" class="error"></label></td>
					</tr>
					<tr>
						<td>时间:</td>
						<th><select name="hour">
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
						</select> <label>分</label></th>
					</tr>
					<tr>
						<td>消息内容：</td>
						<td><textarea id="message" name="message" id="content"
								onblur="return checkArea(this.value)"
								style="width:380px; height:80px;"
								onclick="return check(this.value)"><c:out
									value="${information.getContent() }"></c:out></textarea></td>
						<td><label id="contentError" class="error"></label></td>
					</tr>
					<tr>
						<td>URI:</td>
						<td><input type="text" id="uri" name="uri" value=""
							style="width:380px;" /> <br /> <span style="font-size:0.8em">ex)
								http://www.dokdocorea.com, geo:37.24,131.86, tel:111-222-3333</span></td>
					</tr>
					<tr>
					<td>&nbsp;</td>
					<td><input type="submit" value="推送" onclick="return check()"/> 
					<input type="reset" value="取消" onclick="cancle()"/></td>
				</tr>
				</c:if>
				<c:if test="${information eq null }">
					<tr>
						<td>标题:</td>
						<td><input type="text" id="title" name="title"
							style="width:380px;" /></td>
						<td><label id="titleError" class="error"></label></td>
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
						<td>日期:</td>
						<td><input type="text" name="date" id="date"
							readonly="readonly" /></td>
						<td><label id="dateError" class="error"></label></td>
					</tr>
					<tr>
						<td>时间:</td>
						<th><select name="hour">
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
						</select> <label>分</label></th>
					</tr>
					<tr>
						<td>消息内容：</td>
						<td><textarea id="message" name="message" id="content"
								onblur="return checkArea(this.value)"
								style="width:380px; height:80px;"></textarea></td>
						<td><label id="contentError" class="error"></label></td>
					</tr>
					<tr>
						<td>URI:</td>
						<td><input type="text" id="uri" name="uri" value=""
							style="width:380px;" /> <br /> <span style="font-size:0.8em">ex)
								http://www.dokdocorea.com, geo:37.24,131.86, tel:111-222-3333</span></td>
					</tr>
					<tr>
					<td>&nbsp;</td>
					<td><input type="submit" value="推送" onclick="return check()"/> 
					<input type="reset" value="取消"/></td>
				</tr>
				</c:if>
			</table>
		</form>
	</div>

	<script type="text/javascript">
		//<![CDATA[
		area = false ;
		function checkArea(value){
			if(value == "" || value == null)
			{
				area = true ;
			}else{
				area = false ;
			}
		}
		//用户点击取消按钮时
		function cancle() {
			history.back();
		}
		//验证输入的内容是否为空
		function check() {
			isNull = false;
			$(".error").text("");
			if(!$(":text[name=username]").val()){
				$("#userNameError").text("用户名不能为空") ;
				isNull = true ;
			}
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
		$(function() {
			$("#date").datepick({
				dateFormat : "yy-mm-dd"
			});
		});
		$(function() {
			$('input[name=broadcast]').click(function() {
				if ($('input[name=broadcast]')[0].checked) {
					$('#trUsername').hide();
				} else {
					$('#trUsername').show();
				}
			});

			if ($('input[name=broadcast]')[0].checked) {
				$('#trUsername').hide();
			} else {
				$('#trUsername').show();
			}
		});

		//]]>
	</script>

</body>
</html>
