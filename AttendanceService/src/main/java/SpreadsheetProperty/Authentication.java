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



    /** A defined constructor **/
    public Authentication(String spreadsheetID)
            throws IOException, ServiceException, URISyntaxException {
        super(spreadsheetID);
    }


    /** Default constructor, doesn't do anything **/
    Authentication() throws IOException, ServiceException {
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



        // Check if student ID and the name input from user is valid. ID will be checked first then user's name
        // isIDValid() will check for valid id first. If it's found within the spreadsheet continue to validate for user's input username
        // If username is valid then return the row index. Otherwise, return -1 since there is no exist row with that username
        /* Row that we want to update */
        int updateRow;
        if ((updateRow = isIDValid(id)) < getMaxRows() && updateRow >= 0 &&
                listEntry.get(updateRow).getCustomElements().getValue("username").equals(username)) {

            return updateRow;
        }
        else {

            return -1;  // Indicate out of bound rows
        }
    }




    /**
     * Check the ID from user input whether
     * it matches with the one from the
     * spreadsheet. If matches, return the
     * row that matches and used it for
     * updating within that particular row.
     * Otherwise, return -1 indicating an error
     * @throws IOException
     * @throws ServiceException
     */

    private int isIDValid(String id)
            throws IOException, ServiceException, URISyntaxException {



        int idFound = 0;     // A student ID found within a particular array. The total numbers of array really depends on how many student we have total


        // Traverse the entry of each array. Since each student belong to an Array
        while (true) {
            // Within that entry find a "studentid" header with the value that matches the one student input
            if (listEntry.get(idFound).getCustomElements().getValue("studentid").equals(id)) {
                break;
            }
            idFound++;
        }


        return idFound >= 0 ? idFound : -1;  // If student ID can't be found, return -1
    }


    /**
     * Look for the column with the name "Access"
     * that indicate the last time student logged in
     * @return
     */

    public int getAccessHeader()
    throws IOException, ServiceException {

        int index = 0;
        for (String element : headerRow) {
            if (element.matches("lastaccess"))   // Need to have the pattern access or Access in it. Otherwise, the program can't detect it so it will assume that column is missing
                break;
            index++;
        }

        // Compare the index to the maximum column of the spreadsheet. If it exceeds the maximum, means that it could not find the desire column
        return index;

    }




}
