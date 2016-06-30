<%--
  Created by IntelliJ IDEA.
  User: TinTin
  Date: 6/28/16
  Time: 10:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<%@ page import="SpreadsheetProperty.UpdateSpreadsheet"%>
<%@ page import="com.google.gdata.util.ServiceException" %>
<%@ page import="java.net.URISyntaxException" %>
<%@ page import="SpreadsheetProperty.SendMail" %>


<html>
<head>
    <title>Successful Submission</title>
    <style>
        body {
            background-color: darkgray;
            text-align: center;
        }
    </style>
</head>
<body>

Thank you for your submission. An email confirmation has been sent to <%=(String)request.getAttribute("username") + "@csus.edu"%>
<%

    String username = (String)request.getAttribute("username");
    String studentID =  (String)request.getAttribute("studentID");
    String spreadsheetID =  (String)request.getAttribute("spreadsheetID");
    String courseTitle = (String)request.getAttribute("courseTitle");


    try {
        try {
            new UpdateSpreadsheet(spreadsheetID).updateSheet(username, studentID, courseTitle);
            new SendMail(username, courseTitle);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
%>
</body>
</html>
