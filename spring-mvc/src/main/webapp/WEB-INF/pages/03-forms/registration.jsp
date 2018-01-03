<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration</title>
</head>
<body>
    <div align="center">
        <form:form action="register" method="post" commandName="userForm">
            <form:errors path="*" element="div"></form:errors>
            <table border="0">
                <tr>
                    <td colspan="2" align="center"><h2>Spring MVC Form Demo - Registration</h2></td>
                </tr>
                <tr>
                    <td>User Name:</td>
                    <td><form:input path="username" /></td>
                    <td><form:errors path="username"></form:errors></td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><form:password path="password" /></td>
                    <td><form:errors path="password"></form:errors></td>
                </tr>
                <tr>
                    <td>E-mail:</td>
                    <td><form:input path="email" /></td>
                    <td><form:errors path="email"/></td>
                </tr>
                <tr>
                    <td>Birthday (mm/dd/yyyy):</td>
                    <td><form:input path="birthDate" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Profession:</td>
                    <td><form:select path="profession" items="${professionList}" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Select</td>
                    <td><form:select path="select" items="${objectItems}" itemLabel="label" itemValue="id" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td>Multiple Select</td>
                    <td><form:select multiple="true" path="multipleSelect" items="${objectItems}" itemLabel="label" itemValue="id" /></td>
                    <td></td>
                </tr>
                <tr>
                    <td colspan="3" align="center"><input type="submit" value="Register" /></td>
                </tr>
            </table>
        </form:form>
    </div>
</body>
</html>