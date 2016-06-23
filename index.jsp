<%--
  Created by IntelliJ IDEA.
  User: tuyen
  Date: 6/9/16
  Time: 6:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mypackage.UpdateSpreadsheet" %>

<html>
<head>
  <title>Sign In Form</title>
</head>
<body>
<!--Get name, email and id when student submit his/her name from signin.html form--%>
    <%
        String name = request.getParameter("name");
        String id = request.getParameter("id");
        String email = request.getParameter("email");
        UpdateSpreadsheet.updateSheet(name, id, email);
    %>
</body>
