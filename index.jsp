<%--
  Created by IntelliJ IDEA.
  User: tuyen
  Date: 6/9/16
  Time: 6:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="mypackage.UpdateSpreadsheet" %>
<%@ page import="com.google.gdata.util.ServiceException" %>
<%@ page import="java.net.URISyntaxException" %>
<%@ page import="mypackage.Student" %>

<html>
<head>

    <title>Submission Form</title>


</head>
<body>

<%


    String username = request.getParameter("username");
    String id = request.getParameter("id");
    String passcode = request.getParameter("passcode");


    try {
        try {

            // Indicate username and id are valid.
            // If username and id are valid then
            // send an email receipt to student
            if (new UpdateSpreadsheet().updateSheet(username, id, passcode) < UpdateSpreadsheet.getRow()) {
                new Student().storeStudentInfo(username, id);
                Student.sendMail(username);    // send email receipt to student
            }

        } catch (ServiceException e) {
            System.exit(1);
        }
    } catch (URISyntaxException e) {
        System.exit(1);
    }


%>

</body>
