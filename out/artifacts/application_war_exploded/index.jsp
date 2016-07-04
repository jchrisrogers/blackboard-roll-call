<%--
  Created by IntelliJ IDEA.
  User: Chris Rogers
  Date: 7/3/16
  Time: 7:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Attendance Service</title>
    <meta charset="UTF-8">
    <style>
        div {
            width: 100%;
            height: 100%;
            margin: auto;

        }
        ul {
            list-style-type: none;
            diplay: flex;
        }
        .nav {
            width: 75%;
            height: auto;
            margin: 15%;
        }
        .link1 {
            display: inline;
            margin-top: 5px;
            margin-bottom: 5px;
            padding: 5px;
            border-radius: 15px;
            background-color: #043927;
            color: #FFFFFF;
            text-align: center;
            font-size: 5vw;
            line-height: 10vw;
            width: 40%;
            height: auto;
            margin-left: 0;
            float: left;
        }
        .link2 {
            display: inline;
            margin-top: 5px;
            margin-bottom: 5px;
            padding: 5px;
            border-radius: 15px;
            background-color: #C4B581;
            color: #FFFFFF;
            text-align: center;
            font-size: 5vw;
            line-height: 10vw;
            width: 40%;
            height: auto;
            margin-left: 0;
            float: left;
        }
        a {
            text-decoration: none;
        }
        a:link {
            color: #DAD490;
        }

        a:visited {
            color: #008453;
        }
        a:active {
            color: #E6B711;
        }
    </style>
</head>
<body>

<!-- Buttons for links to Student and Professor pages -->
    <div>
        <ul class="nav">
            <li class="link1" onclick="location.href ='Student.jsp'"><a href="Student.jsp" target="_blank">Students</a></li>
            <li class="link2" onclick="location.href ='Professor.jsp'"><a href="Professor.jsp" target="_blank">Professor</a></li>
        </ul>
    </div>
</body>
</html>
