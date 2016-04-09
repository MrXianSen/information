<%@page import="org.androidpn.server.model.Information"%>
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
				<th>时间</th>
				<th>类型</th>
				<th>内容</th>
				<th></th>				
			</tr>
		</thead>
		<tbody>
			<c:forEach var="information" items="${informationList }">
				<tr>
				<c:choose>
					<c:when test="${information ne null }">
						<td><c:out value="${information.title }"></c:out></td>
						<td align="center"><c:out value="${information.date }"></c:out></td>
						<td><c:out value="${information.getTime() }"></c:out></td>
						<td><c:out value="${information.type }"></c:out></td>
						<td>
							<c:out value="${information.shows }"></c:out>
						</td>
						<td>
							<a href="/information.do?action=edit&id=${information.getID() }">编辑</a> 
							<a href="/notification.do?action=push&id=${information.getID() }">推送</a> 
							<a href="/information.do?action=delete&id=${information.getID() }" 
							onclick="javascript:return confirm('是否要删除该信息？')">删除</a>
							
						</td>
					</c:when>
				</c:choose>
				</tr>
			</c:forEach>
		</tbody>
		
	</table>
	<c:choose>
		<c:when test="${pageBean.getTp() <= 5 }">
			<c:set var="begin" value = "1"></c:set>
			<c:set var="end" value = "${pageBean.getTp() }"></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="begin" value="${pageBean.getPc()-2 }"></c:set>
			<c:set var="end" value="${pageBean.getPc()+2 }"></c:set>
			<c:if test="${begin < 1 }">
				<c:set var="begin" value="1"></c:set>
				<c:set var="end" value="5"></c:set>
			</c:if>
			<c:if test="${end > pageBean.getTp() }">
				<c:set var="begin" value="${pageBean.getTp() - 4 }"></c:set>
				<c:set var="end" value="${pageBean.getTp() }"></c:set>
			</c:if>
		</c:otherwise>
	</c:choose>
	<center>
		第<c:out value="${pageBean.getPc() }"></c:out>页 
		<c:if test="${pageBean.getPc() == 1 }">
			<label>首页</label>
			<label>上一页</label>
		</c:if>
		<c:if test="${pageBean.getPc() > 1 }">
			<a href="/information.do?action=list&currPage=1">首页</a>
			<a href="/information.do?action=list&currPage=${pageBean.getPc() - 1 }">上一页</a>
		</c:if>
		<c:forEach var="i" begin="${begin }" end="${end }">
			<c:choose>
				<c:when test="${i eq pageBean.getPc() }">
					[${i }]
				</c:when>
				<c:otherwise>
					[<a href="/information.do?action=list&currPage=${i }">${i }</a>]
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${pageBean.getPc() == pageBean.getTp() }">
			<label>下一页</label>
			<label>尾页</label>
		</c:if>
		<c:if test="${pageBean.getPc() < pageBean.getTp() }">
			<a href="/information.do?action=list&currPage=${pageBean.getPc() + 1 }">下一页</a>
			<a href="/information.do?action=list&currPage=${pageBean.getTp() }">尾页</a>
		</c:if>
		共<c:out value="${pageBean.getTp() }"></c:out>页
	</center>
	
	<script type="text/javascript">
		//<![CDATA[
		$(function() {
			$('#tableList').tablesorter();
			//$('#tableList').tablesorter( {sortList: [[0,0], [1,0]]} );
			//$('table tr:nth-child(odd)').addClass('odd');
			///information.do?action=delete&id=${information.getID() }
			$('table tr:nth-child(even)').addClass('even');
		});
		
		function check(){
			new E.ui.Dialog("确认",	{
				title: "确认",
				content: "真的要删除本条信息吗？",
				yesFn: function(){
					return "/information.do?action=delete&id=${information.getID() }" ;
				},
				noFn: function(){
					return "" ;
				}
			});
		}
		//]]>
	</script>
</body>
</html>
