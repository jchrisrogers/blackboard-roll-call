<%@ page import="servlet.Professor" %>
<%--
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
    <meta charset="UTF-8">
    <title>Activation Page</title>
    <script src="http://code.jquery.com/jquery-1.10.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <style>
        body {
            text-align: center;
            max-width: 100%;
        }

        #dialog {
            text-align: center;
            float: left;
        }

        #profform {
            display: inline-block;
        }

    </style>
</head>
<body>

<!-- Two button for instruction and passcode generator -->
<button id="instruction">Click for Instruction</button>
<button id="generator">Generate passcode</button>

<div id="profform">
<!-- Copy google spreadsheet ID here and passcode will appear here -->
<form action="${pageContext.request.contextPath}/servlet/Professor" id="submission" method="post">

    <table>
        <tr>
            <th>Name</th>
            <th><input id="name" name="name" placeholder="name" type="text"></th>
            <th><span id="nameMissing"></span> <br></th>
        </tr>
        <tr>
            <th>Class</th>
            <th><input id="class" name="class" placeholder="class" type="text"></th>
            <th><span id="classMissing"></span> </th>
        </tr>
        <tr>
            <th>Section</th>
            <th><input id="section" name="section" placeholder="section" type="text"></th>
            <th><span id="sectionMissing"></span> </th>
        </tr>
    </table>

    <span id="codeMissing"></span> <br>
    <label for="code"></label>
    <input id="code" name="code" placeholder="passcode" type="text"> <br>
    <span id="idMissing"></span> <br>
    <label for="spreadsheetID"></label>
    <input id="spreadsheetID" name="spreadsheetID" placeholder="spreadsheet ID" type="text"> <br>
    <button id="submit" type="submit">Submit</button>
    <button type="reset">Reset</button>
</form>
</div>

<!-- Instruction pop and hide -->
<div id="dialog" title="Instruction">
    <p>1. Create a spreadsheet on google drive</p>
    <p>2. Copy your spreadsheet ID. It should look like: https://docs.google.com/spreadsheets/d/spreadsheetID/edit</p>
    <p>3. Copy and paste the link into the spreadsheet ID box</p>
    <p>4. Generate the passcode to populate the spreadsheet with student attendance</p>
</div>



<!-- some scripting and jquery -->
<script>

    // Visual effect for dialog
    $(function () {
        // Hide on default
        // Automatically hide from user untill clicked
        $("#dialog").dialog({
            autoOpen: false,    // hide on default
            show: {
                effect: "blind",
                duration: 1000,
                responsive: true
            },
            // Explode out of the screen when click close
            hide: {
                effect: "explode",
                duration: 1000
            }
            , position: {
                my: "left",
                at: "left+10",
                of: window,
                collision: "none"
            }
            , create: function (event, ui) {
                $(event.target).parent().css('position', 'fixed');
            }
        });

        // Visual effect for form submission
        // Automatically hide from user until clicked on the button "generate code"
        $(function () {
            $('#submission').dialog({
                autoOpen: false,
                show: {
                    effect: "blind",
                    duration: 1000,
                    responsive: true
                }
                , position: {
                    my: "right",
                    at: "right+10",
                    of: window,
                    collision: "none"
                }
            , create: function (event, ui) {
                $(event.target).parent().css('position', 'fixed');
                }
            });
        });

        // Pop up only when user click on button "instruction"
        $("#instruction").click(function () {
            $("#dialog").dialog("open");

        });
    });



    // Upon clicking the button "generate code"
    $('#generator').click(function () {
        // Input random number into input box with id="code"
        $("#code").val(Math.floor(Math.random() * 10000000000) + "");
        // Simultaneously open the submission form at the same time
        $('#submission').dialog('open').submit(function (event) {
            // Check whether passcode is left empty
            if ($('#code').val() === "") {
                $('#codeMissing').text('code missing').show();
                event.preventDefault();
            } else {
                $('#codeMissing').text("").show();
            }

            // Check whether spreadsheet ID is left empty
            if ($('#spreadsheetID').val() === "") {
                $('#idMissing').text('spreadsheet id missing').show();
                event.preventDefault();
            } else {
                $('#idMissing').text("").show();
            }

            // Check whether name is left empty
            if ($('#name').val() === "") {
                $('#nameMissing').text('name missing').show();
                event.preventDefault();
            } else {
                $('#nameMissing').text("").show();
            }

            // Check whether class field is left empty
            if ($('#class').val() === "") {
                $('#classMissing').text('class missing').show();
                event.preventDefault();
            } else {
                $('#classMissing').text("").show();
            }

            // Check whether section is left empty
            if ($('#section').val() === "") {
                $('#sectionMissing').text('section missing').show();
                event.preventDefault();
            } else {
                $('#sectionMissing').text("").show();
            }
        });
    });


</script>


</body>
</html>
