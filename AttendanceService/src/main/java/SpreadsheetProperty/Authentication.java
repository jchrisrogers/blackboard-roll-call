package SpreadsheetProperty;



import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
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

    /** Row that we want to update **/
    private static int updateRow;

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
        if ((updateRow = isIDValid(id)) < getMaxRows() && updateRow >= 0 &&
                listEntry.get(updateRow).getPlainTextContent().split(",")[getUsernameHeader()-1].split(":\\s")[1].equals(username)) {

            return updateRow;
        }
        else {
            return -1;  // Indicate out of bound rows
        }
    }


    int getUpdateRow() {
        return updateRow;
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
        int idIndex = getidIndex();
        // Traverse the entry of each array. Since each student belong to an Array
        for (ListEntry list : listEntry) {
            // Since the ID field is "student: id" so we need to split the string to get the "id" part only and not the "student:"
            if (list.getPlainTextContent().split(",")[idIndex - 1].split(":\\s")[1] .equals(id)) {
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

    int getAccessHeader()
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


    /**
     * Cell query to get the index of username column. Assume user input Username column in a random order
     */
    private int getUsernameHeader() throws IOException, ServiceException {
        int index = 0;
        for (String element : headerRow) {
            if (element.equals("username"))
                break;
            index++;
        }
        return index;
    }

    /**
     * Cell query to get the index of student id column. Assume user input ID column in a random order
     */
    private int getidIndex() throws IOException, ServiceException {
        int index = 0;
        for (String element : headerRow) {
            if (element.matches(".*[id]$"))   // Need to have the pattern id or ID in it. Otherwise, the program can't detect an id column so it will assume that column is missing
                break;
            index++;
        }

        return index;
    }




}
