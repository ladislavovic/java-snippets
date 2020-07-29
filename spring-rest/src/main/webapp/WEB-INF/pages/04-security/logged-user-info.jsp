<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<body>
	<h1>Logged user info</h1>
	username: ${username} <br>
	<c:forEach items="${authorities}" var="var">
	  authority: ${var} <br>
	</c:forEach>
</body>
</html>
