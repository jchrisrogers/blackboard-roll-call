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
<%@ page import="SpreadsheetProperty.Spreadsheet" %>


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

Thank you for your submission. An email confirmation has been sent to <%=request.getAttribute("username") + "@csus.edu"%>
<%=request.getAttribute("accessHeader")%>

<%

    int updateRow =  (Integer) request.getAttribute("updateRow");

    String courseTitle = (String)request.getAttribute("courseTitle");
    String spreadsheetID = (String) request.getAttribute("spreadsheetID");


    try {
        try {
            // Create a new spreadsheet with properties such as row, column and set the title for that spreadsheet

            // Update the spreadsheet
            UpdateSpreadsheet updateSpreadsheet = new UpdateSpreadsheet(spreadsheetID);
            updateSpreadsheet.updateAttendance(updateRow);

            // Send email notification
           // new SendMail(username, courseTitle);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
%>
</body>
</html>
