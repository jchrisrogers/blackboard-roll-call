<%--
  Created by IntelliJ IDEA.
  User: TinTin
  Date: 6/28/16
  Time: 1:59 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta content="IE=edge" http-equiv="X-UA-Compatible">
    <meta content="width=device-width, initial-scale=1" name="viewport">
    <script src="https://code.jquery.com/jquery-1.10.2.js"></script>
    <title>Student Login</title>

    <style>
        body {
            background: -webkit-linear-gradient(grey, black); /* For Safari 5.1 to 6.0 */
            background: -o-linear-gradient(grey, black); /* For Opera 11.1 to 12.0 */
            background: -moz-linear-gradient(grey, black); /* For Firefox 3.6 to 15 */
            background: linear-gradient(grey, black); /* Standard syntax */
            height: 700px;
            margin-top: 8.2pc;
        }
        img {
            height: 315px;
            position: absolute;
            left: 0;
            top: 110px;
            width: 1450px;
            z-index: -1;
        }

        #idMissing, #nameMissing, #passcodeMissing {
            color: red;
            font-size: small;
        }

        .signin,
        .signin h2,
        .signin label,
        .signin input {
            border: 0;
            margin: 0;
            outline: 0;
            padding: 0;
        }
        .signin {
            background-color: #141517;
            border-radius: 5px;
            box-shadow: 5px 5px 1px 0px rgba(255, 255, 255, .2), inset 0px 1px 1px 0px rgb(0, 0, 0);
            cursor: default;
            height: 250px;
            margin: auto;
            -moz-border-radius: 5px;
            -moz-box-shadow: 5px 5px 1px 0px rgba(255, 255, 255, .2), inset 0px 1px 1px 0px rgb(0, 0, 0);
            position: relative;
            padding: 20px 25px 0px 25px;
            width: 200px;
            -webkit-border-radius: 5px;
            -webkit-box-shadow: 5px 15px 1px 0px rgba(255, 255, 255, .2), inset 0px 1px 1px 0px rgb(0, 0, 0);
        }
        .signin:before {
            border-bottom: 10px solid #141517;
            border-right: 10px solid #141517;
            border-top: 10px solid transparent;
            border-left: 10px solid transparent;
            content: '';
            height: 0px;
            left: 10px;
            position: absolute;
            top: -12px;
            width: 0px;
        }
        .signin h1 {
            color: #e4e4e4;
            font-family: 'Myriad Pro', sans-serif;
            font-size: 22px;
            font-weight: normal;
            line-height: 40px;
        }
        .signin input[type=text],
        .signin input[type=tel],
        .signin input[type=email],
        .signin button {
            border: 0;
            border-radius: 26px;
            font-family: Helvetica, sans-serif;
            font-size: 12px;
            font-weight: bold;
            line-height: 14px;
            -moz-border-radius: 26px;
            -moz-transition: all .15s ease-in-out;
            -o-transition: all .15s ease-in-out;
            outline: none;
            padding: 6px 15px;
            text-shadow: 0px 1px 1px rgba(255, 255, 255, .2);
            transition: all .15s ease-in-out;
            -webkit-border-radius: 26px;
            -webkit-transition: all .15s ease-in-out;
        }
        .signin input[type=text],
        .signin input[type=tel],
        .signin input[type=email] {
            box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .6);
            background: #989898;
            background: -moz-linear-gradient(top, #ffffff 0%, #989898 100%);
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #ffffff), color-stop(100%, #989898));
            background: -webkit-linear-gradient(top, #ffffff 0%, #989898 100%);
            background: -o-linear-gradient(top, #ffffff 0%, #989898 100%);
            background: -ms-linear-gradient(top, #ffffff 0%, #989898 100%);
            background: linear-gradient(top, #ffffff 0%, #989898 100%);
            color: #686868;
            width: 170px;
            -moz-box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .6);
            -webkit-box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .6);
        }
        .signin input::-webkit-input-placeholder {
            color: orangered;
        }
        .signin input[type=text]:hover,
        .signin input[type=tel]:hover,
        .signin input[type=email]:hover {
            box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .6), 0px 0px 5px rgba(255, 255, 255, .5);
            -webkit-box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .6), 0px 0px 5px rgba(255, 255, 255, .5);
            -moz-box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .6), 0px 0px 5px rgba(255, 255, 255, .5);
        }
        .signin input[type=text]:focus,
        .signin input[type=tel]:focus,
        .signin input[type=email]:focus {
            background: #e1e1e1;
            background: -moz-linear-gradient(top, #ffffff 0%, #e1e1e1 100%);
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #ffffff), color-stop(100%, #e1e1e1));
            background: -webkit-linear-gradient(top, #ffffff 0%, #e1e1e1 100%);
            background: -o-linear-gradient(top, #ffffff 0%, #e1e1e1 100%);
            background: -ms-linear-gradient(top, #ffffff 0%, #e1e1e1 100%);
            background: linear-gradient(top, #ffffff 0%, #e1e1e1 100%);
        }
        .signin button {
            box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .45), 0px 1px 1px 0px rgba(0, 0, 0, .3);
            cursor: pointer;
            color: #445b0f;
            float: right;
            -moz-box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .45), 0px 1px 1px 0px rgba(0, 0, 0, .3);
            -webkit-box-shadow: inset 1px 1px 1px 0px rgba(255, 255, 255, .45), 0px 1px 1px 0px rgba(0, 0, 0, .3);
        }
        .signin button:hover {
            box-shadow: inset 1px 1px 3px 0px rgba(255, 255, 255, .8), 0px 1px 1px 0px rgba(0, 0, 0, .6);
            -webkit-box-shadow: inset 1px 1px 3px 0px rgba(255, 255, 255, .8), 0px 1px 1px 0px rgba(0, 0, 0, .6);
            -moz-box-shadow: inset 1px 1px 3px 0px rgba(255, 255, 255, .8), 0px 1px 1px 0px rgba(0, 0, 0, .6);
        }
        .signin button:active {
            box-shadow: none;
            -webkit-box-shadow: none;
            -moz-box-shadow: none;
        }
        .signin button {
            background: #a5cd4e;
            background: -moz-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #a5cd4e), color-stop(100%, #6b8f1a));
            background: -webkit-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
            background: -o-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
            background: -ms-linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
            background: linear-gradient(top, #a5cd4e 0%, #6b8f1a 100%);
        }
    </style>


</head>

<body>
<img src="newyork.jpg" style="width:100%">
<div class="signin">
    <h1>Login Form</h1>
    <form action="${pageContext.request.contextPath}/servlet/Student" id="require" method="post">
        <span id="nameMissing"></span>
        <p>
            <label for="username"></label>
            <input id="username" name="username" placeholder="username" onfocus="this.placeholder=''"  onblur="this.placeholder='username'" type="text">
        </p>
        <span id ="idMissing"></span>
        <p>
            <label for="studentId"></label>
            <input id="studentId" name="studentId" placeholder="studentID" onfocus="this.placeholder=''" onblur="this.placeholder='studentID'" type="tel">
        </p>
        <span id="passcodeMissing"></span>
        <p>
            <label for="passcode"></label>
            <input id="passcode" name="passcode" placeholder="passcode" onfocus="this.placeholder=''" onblur="this.placeholder='passcode'"  type="tel">
        </p>

        <button id="submit" name="submit" type="submit">Submit</button>
    </form>

</div>

<!--Check to see if any fields are empty. If empty display error message-->
<!-- Otherwise, process to SubmissionServlet.jsp-->
<script>
    $(document).ready(function () {
        $("form").submit(function (event) {
            if ($('#username').val() === "") {
                $('#nameMissing').text('Missing name field').show().fadeOut(10000);
                event.preventDefault();
            }
            if ($('#studentId').val() === "") {
                $('#idMissing').text('Missing id field').show().fadeOut(10000);
                event.preventDefault();
            }
            if ($('#passcode').val() === "") {
                $('#passcodeMissing').text('Missing passcode field').show().fadeOut(10000);
                event.preventDefault();
            }
        })
    })

</script>

</body>
</html>
