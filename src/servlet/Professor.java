package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TinTin on 6/28/16.
 * Read in professor's input
 */
@WebServlet(name = "Professor", urlPatterns = {"/servlet/Professor"})
public class Professor extends HttpServlet {

    /**
     * Private List data that store passcode and
     * spreadsheet ID
     */
    private static List<List<String>> professorData = new ArrayList<>();


    /**
     * Accept professor's input when they
     * submit passcode and googlespread ID
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> dataIn = new ArrayList<>();

        // Add data to dataIn
        dataIn.add(request.getParameter("name"));
        dataIn.add(request.getParameter("class") + ", section " + request.getParameter("section"));
        dataIn.add(request.getParameter("code"));
        dataIn.add(request.getParameter("spreadsheetID"));



        // Store an array into List<List<String>> class
        professorData.add(dataIn);


        // Print out a successful submission page
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Reading request";
        String docType = "<!DOCTYPE HTML PUBLIC \"-//W3c??DTD HTML 4.0 " + "Transitional//EN\">\n";
        out.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                "<H1 ALIGN=\"CENTER\">" + title + "</H1>\n" +
                "<UL>\n" +
                "   <LI><B>name</B>: "
                + dataIn.get(0) + "\n" +
                "   <LI><B>class and section #</B>: "
                + dataIn.get(1) + "\n" +
                "   <LI><B>passcode</B>: "
                + dataIn.get(2) + "\n" +
                "   <LI><B>spreadsheetID</B>: "
                + dataIn.get(3) + "\n" +
                "</UL>\n" +
                "</BODY></HTML>");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    /**
     * Check if a certain passcode exist within
     * Professors's database
     */
    public static String passcodeExist(String passcode) {

        for (List list : professorData) {
            if (list.get(2).equals(passcode))
                return (String) list.get(3);        // Cast to string since list.get(3) returns an object
        }
        return "";      // Return an empty string if no passcode exist
    }

    /**
     * Get the course and its section number
     */
    public static String getCourseTitle(String passcode) {
        for (List list : professorData) {
            if (list.get(2).equals(passcode)) {
                return (String) list.get(1);    // Cast to string since list.get(1) returns an object
            }
        }
        return "";
    }
}
