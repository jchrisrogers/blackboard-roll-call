<%@ page import="mypackage.Professor" %><%--
  Created by IntelliJ IDEA.
  User: TinTin
  Date: 6/27/16
  Time: 8:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Confirmation</title>
</head>
<body>
<%
    String passcode = request.getParameter("code");
    String spreadsheetID = request.getParameter("spreadsheetID");

    Professor.importSpreadsheetIDandPasscode(spreadsheetID, passcode);

%>

</body>
</html>
