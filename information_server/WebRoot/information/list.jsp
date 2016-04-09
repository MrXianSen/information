<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>管理页</title>
<meta name="menu" content="information" />
<link rel="stylesheet" type="text/css"
	href="<c:url value='/styles/tablesorter/style.css'/>" />
<script type="text/javascript"
	src="<c:url value='/scripts/jquery.tablesorter.js'/>"></script>
</head>

<body>
	<h1>所有消息</h1>
	<table id="tableList" class="tablesorter" cellspacing="1">
		<thead>
			<tr>
				<th>标题</th>
				<th>日期</th>
				<th>类型</th>
				<th>内容</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="information" items="${informationList }">
				<tr>
				<c:choose>
					<c:when test="${information ne null }">
						<td><c:out value="${information.title }"></c:out></td>
						<td align="center"><fmt:formatDate
							pattern="yyyy-MM-dd HH:mm:ss" value="${information.date}" /></td>
						<td><c:out value="${information.type }"></c:out></td>
						<td><c:out value="${information.content }"></c:out></td>
					</c:when>
				</c:choose>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<script type="text/javascript">
		//<![CDATA[
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
