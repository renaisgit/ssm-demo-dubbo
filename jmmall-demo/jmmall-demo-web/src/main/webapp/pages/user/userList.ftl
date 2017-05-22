<html>
<head>
<@ui.commonMETA/>
<title>用户列表</title>
<@ui.commonCSS/>
</head>
<body>
<a href="${basePath}/user/userList.html">所有用户打开</a>
<a href="${basePath}/user/userPage.html">分页用户打开</a>
	<table>
		<#list result as user>
		<tr>
			<td>
				${user.id}
			</td>
		</tr>
		</#list>
	</table>
</body>
<@ui.commonJQUERY/>
</html>