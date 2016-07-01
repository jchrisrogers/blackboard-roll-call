package servlet;

import SpreadsheetProperty.Authentication;
import com.google.gdata.util.ServiceException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;


/**
 * Created by TinTin on 6/28/16.
 * A Student class that accept student's
 * input such as their name, email and
 * the passcode that they entered
 */
@WebServlet(name = "Student",  urlPatterns = {"/servlet/Student"})
public class Student extends HttpServlet {


    /**
     * A method that accept
     * student input such as
     * their username, studentID and
     * the passcode that they entere.
     * Then store them into a database
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get request from user input
        String username = request.getParameter("username");
        String passcode = request.getParameter("passcode");
        String studentId = request.getParameter("studentId");






        /**
         * Check whether student's input
         * passcode is valid and match with
         * the one professors generated. If student's
         * input passcode is valid, prompt the Professor
         * class to return the corresponding spreadsheetID
         */
        String spreadsheetID = "";      // Spreadsheet ID from professor
        if ((spreadsheetID = Professor.passcodeExist(passcode)).equals("")) {
            displayError(response, request);

        } else {

            try {
                try {
                    /**
                     * If the passcode is valid then check for student name and id to
                     * see whether it is in the spreadsheet. If they are already in the
                     * spreadsheet then update their attendance. Otherwise, prompt an error page (Error.jsp).
                     * isInputValid() will check if there is a particular username or id within that row
                     * It will return any value that is return than or equal 0 indicating that there exist
                     * a username and student id in the spreadsheet. Otherwise, it will return -1 indicating an error
                     */
                    if (new Authentication(spreadsheetID).isInputValid(username, studentId) >= 0) {
                        String courseTitle = Professor.getCourseTitle(passcode);
                        displayConfirmation(response, request, username, spreadsheetID, studentId, courseTitle);
                    } else {
                        RequestDispatcher rd = getServletContext().getRequestDispatcher("/Error.jsp");  // No username or id exist within any particular row, so prompt an error page. Declaring a service request
                        rd.forward(request, response);      // forward a request to that page
                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }



        }



    }



    private void displayConfirmation(HttpServletResponse response, HttpServletRequest request,
                                            String username, String spreadsheetID, String studentID, String courseTitle)
    throws IOException, ServletException {
        // Update spreadsheet with the corresponding passcode input by student

        RequestDispatcher rd;
        // Set attribute to deploy them to another page, which is the Success.jsp file
        request.setAttribute("username", username);
        request.setAttribute("spreadsheetID", spreadsheetID);
        request.setAttribute("studentID", studentID);
        request.setAttribute("courseTitle", courseTitle);

        rd = getServletContext().getRequestDispatcher("/Success.jsp");  // Prompt a service request
        rd.forward(request, response);  // Forward that request

    }




    /**
     * Display error if student input passcode
     * cannot be found
     */
    private static void displayError(HttpServletResponse response, HttpServletRequest request)
    throws IOException {
        PrintWriter out = response.getWriter();
        String docType = "<!DOCTYPE HTML PUBLIC \"-//W3c??DTD HTML 4.0 " + "Transitional//EN\">\n";
        out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>" + "Error" + "</TITLE></HEAD>\n" +
                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                "<H1 ALIGN=\"CENTER\">" + "Error" + "</H1>\n" +
                "<P>Invalid passcode: " + request.getParameter("passcode") + " cannot be found" + "</P>\n" +
                "</BODY></HTML>");
    }


}
