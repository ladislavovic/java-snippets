<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<body>
	<h1>Services</h1>
	Messages: <br>
	
	<c:forEach items="${messages}" var="msg">
	  <p>MSG: ${msg}</p>
	</c:forEach>
</body>
</html>
