package SpreadsheetProperty;



import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

/**
 * Created by TinTin on 6/28/16.
 * The purpose of this class is
 * to check if student's ID and
 * name are on the spreadsheet
 */
public class Authentication extends Spreadsheet {


    /*****************************************************************************************************/

    /**Create an array of student and get the header row of the spreadsheet**/
    private List<ListEntry> listEntry = getListEntry();
    private Set<String> headerRow = getHeaderRow();



    /*****************************************************************************************************/



    /** A default constructor **/
    public Authentication(String spreadsheetID)
            throws IOException, ServiceException, URISyntaxException {
        super(spreadsheetID);
    }


    /** Default constructor, doesn't do anything **/
    public Authentication() throws IOException, ServiceException {
        // Empty
    }

    /**
     * Check if student ID and Name is valid.
     * First check for the ID and the row it is
     * at. Then check for the first and last name
     * at the row.
     *
     * @throws IOException
     * @throws ServiceException
     */
    public int isInputValid(String username, String id)
            throws ServiceException, IOException, URISyntaxException {

        // Index to keep track of where to check "yes" to the corresponding first and last name at the correct row
        int index;


        // Make sure student input first and last name. This block will try to split name variable
        // into first name and last name. Making sure there is blank space separating first and last name
        // Check if student ID and the name input from user is valid. ID will be checked first then user's name
        if ((index = isIDValid(id)) < getRows() && index >= 0 &&
                listEntry.get(index).getPlainTextContent().split(",")[getUsernameIndex()-1].split(":\\s")[1].equals(username)) {

            return index;
        }
        else {
            return -1;  // Indicate out of bound rows
        }
    }



    /**
     * Check for ID if it matches
     * with the student ID and the user input
     * @throws IOException
     * @throws ServiceException
     */

    private int isIDValid(String id)
            throws IOException, ServiceException, URISyntaxException {

        int idFound = 0;     // A student ID found within a particular array. The total numbers of array really depends on how many student we have total

        // Traverse the entry of each array. Since each student belong to an Array
        for (ListEntry list : listEntry) {
            // Since the ID field is "student: id" so we need to split the string to get the "id" part only and not the "student:"
            if (list.getPlainTextContent().split(",")[getiDIndex() - 1].split(":\\s")[1] .equals(id)) {
                return idFound;
            }
            idFound++;
        }

        return -1;
    }


    /**
     * Cell query to get the index of username and student ID
     */
    private int getUsernameIndex() throws IOException, ServiceException {
        int index = 0;
        for (String element : headerRow) {
            if (element.equals("username"))
                break;
            index++;
        }
        return index;
    }


    private int getiDIndex() throws IOException, ServiceException {
        int index = 0;
        for (String element : headerRow) {
            if (element.matches("studentid"))
                break;
            index++;
        }
        return index;
    }

//    public static void main(String argv[]) throws IOException, ServiceException, URISyntaxException {
//        System.out.println(new Authentication("1xXOeJvmKwgnjU2wB8ViwTMMs0Mqg-hu301gKgy4eBdI").isInputValid("sasonbaghdadi", "216722988"));
//    }


}
