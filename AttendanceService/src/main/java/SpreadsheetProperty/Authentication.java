package SpreadsheetProperty;


import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by TinTin on 6/28/16.
 * The purpose of this class is
 * to check if student's ID and
 * name are on the spreadsheet
 */
public class Authentication extends Spreadsheet {

    /**
     * Constructor that takes
     * int a spreadsheet ID
     *
     * @param spreadsheetID
     */
    public Authentication(String spreadsheetID)
            throws IOException, ServiceException, URISyntaxException {
        super(spreadsheetID);
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


        // Get all the first, last name and email then store into List of Array
        List<List<Object>> valueRangeList = getValueRange().getValues();

        // Check if student ID and the name input from user is valid. ID will be checked first then user's name
        if ((index = isIDValid(id)) < getRows() &&
                (valueRangeList.get(index).toArray()[0].equals(username))) {
            return index;
        } else {
            return -1;  // Indicate out of bound rows
        }
    }



    /**
     * Check for ID if it matches
     * with the student ID and the user input
     *
     * @throws IOException
     * @throws ServiceException
     */

    private int isIDValid(String id)
            throws IOException, ServiceException, URISyntaxException {


        // Store into List object
        List<List<Object>> valueRangeList = getValueRange().getValues();
        int index = 0;

        // Look for valid ID
        for (List list : valueRangeList) {
            if (list.get(1).equals(id)) {
                break;
            }
            index++;
        }

        return index;
    }


    /**
     * Get range of spreadsheet
     *
     * @throws IOException
     */
    private ValueRange getValueRange()
            throws IOException {

        // Get range
        return SHEET_SERVICE.spreadsheets().values().get(super.getSpreadsheetID(), "Attendance!C2:D").execute();
    }

}
