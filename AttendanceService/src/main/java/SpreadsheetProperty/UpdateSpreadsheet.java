package SpreadsheetProperty;


import com.google.api.services.sheets.v4.model.*;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class UpdateSpreadsheet extends Authentication {



    /**
     * Constructor to update the
     * current column, row and
     * date in the spreadsheet
     */
    public UpdateSpreadsheet(String spreadsheetID)
            throws IOException, ServiceException, URISyntaxException {
        super(spreadsheetID);

        UpdateSpreadsheet.spreadsheetID = spreadsheetID;
        insertColumn = getCols();
    }



    /**
     * Constant variable Spreadsheet ID
     */
    private static String spreadsheetID;




    /**
     * Keep track of how many
     * row and column. Row will be constant
     * throughout. However, column will
     * change overtime since we are adding
     * data in a horizontal position.
     */
    private static int insertColumn;


    /**
     * Current Date
     */
    private static String CURRENT_DATE = new java.util.Date().toString();








    /**
     * Update google spreadsheet
     * @param username
     * @throws IOException
     * @throws ServiceException
     */
    public void updateSheet(String username, String id, String courseTitle)
            throws IOException, ServiceException, URISyntaxException {

        // The new row we want to insert.  insertRow cannot exceed maximum row
        // If input is valid then return the desired row. Otherwise return -1
        int insertRow = isInputValid(username, id);


        // An empty column is the column that missing a value in a specific cell.
        // Otherwise, the column is completely filled
        if (!emptyColumn()) {
            insertHeader(insertColumn);
        }
        else {
            // If the new header is added,
            // CURRENT_COLUMN will be incremented by one so
            // We need to minus one so that it will stay in the
            // proper column throughout the entire process
            // of filling up the column field until
            // it is not empty anymore
            insertColumn = insertColumn - 1;
        }


        // Check for user input. Make sure user input first and last name with their correct ID number
        // Make sure to insert to newColumn only. If all cells are fill then totalCol() == newColumn()
        // insertRow cannot exceed the total row in the spreadsheet.
        // If insertRow is less than 0, that means we have an error

        if (insertRow < getRows() && insertRow >= 0) {
            // Define requests for google API to update the spreadsheet
            List<Request> requests = new ArrayList<>();

            // Call to google API for request and update
            requests.add(new Request()
                    .setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
                            .setProperties(new SheetProperties()
                                    .setSheetId(0)
                                    .setTitle("Attendance: " + courseTitle))
                            .setFields("title")));


            // update spreadsheet by appending the new information below the current row
            List<CellData> values = new ArrayList<>();

            // Add checkmark yes to the cell
            values.add(new CellData()
                    .setUserEnteredValue(new ExtendedValue()
                            .setStringValue("yes")));
            requests.add(new Request()
                    .setUpdateCells(new UpdateCellsRequest()
                            .setStart(new GridCoordinate()
                                    .setSheetId(0)
                                    .setRowIndex(insertRow + 1)
                                    .setColumnIndex(insertColumn))
                            .setRows(Arrays.asList(
                                    new RowData().setValues(values)))
                            .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

            // Final call to publish the updated sheet to google drive
            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                    .setRequests(requests);
            SHEET_SERVICE.spreadsheets().batchUpdate(spreadsheetID, batchUpdateRequest)
                    .execute();
        }

    }


    /**
     * Insert header name
     * to Spreadsheet
     * @throws IOException
     * @throws ServiceException
     */

    public static void insertHeader(int column)
            throws IOException, ServiceException {


        // Define requests for google API to update the spreadsheet
        List<Request> requests = new ArrayList<>();

        // Call to google API for request and update
//        requests.add(new Request()
//                .setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
//                        .setProperties(new SheetProperties()
//                                .setSheetId(0)
//                                .setTitle("Attendance"))
//                        .setFields("title")));


        // Update spreadsheet by appending the new information below the current row
        List<CellData> values = new ArrayList<>();

        // Add new header
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue(CURRENT_DATE)));
        requests.add(new Request()
                .setUpdateCells(new UpdateCellsRequest()
                        .setStart(new GridCoordinate()
                                .setSheetId(0)
                                .setRowIndex(0)
                                .setColumnIndex(column))
                        .setRows(Arrays.asList(
                                new RowData().setValues(values)))
                        .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

        // Final call to publish the updated sheet to google drive
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        SHEET_SERVICE.spreadsheets().batchUpdate(spreadsheetID, batchUpdateRequest)
                .execute();

    }





    /**
     * Check to see whether the entire column
     * is empty or not. In order to do that
     * we have to check to see whether each cell
     * within that column is empty.
     */
    static boolean emptyColumn()
            throws ServiceException, IOException, URISyntaxException {
        return isEmptyField();  // Helper function checking each field
    }



    /**
     * This is a helper function that will
     * traverse each row and check whether
     * there is an empty field within that
     * row. Return false if
     * one of the field is null indicating
     * an empty field. Otherwise
     * the entire row has been filled
     */
    private static boolean isEmptyField()
            throws IOException, ServiceException, URISyntaxException {

        SpreadsheetService spreadsheetService = new SpreadsheetService("Attendance");

        URL urlSpreadsheet = new URL(getUrlFeed());
        SpreadsheetFeed spreadsheetFeed = spreadsheetService.getFeed(urlSpreadsheet, SpreadsheetFeed.class);
        SpreadsheetEntry spreadsheetEntry = spreadsheetFeed.getEntries().get(0);



        ListFeed listFeed = spreadsheetService.getFeed(spreadsheetEntry.getWorksheetFeedUrl(), ListFeed.class);
        List<ListEntry> listEntries = listFeed.getEntries();

        for (ListEntry r : listEntries) {
            for (String tag : r.getCustomElements().getTags()) {
                if (r.getCustomElements().getValue(tag) == null)
                    return true;
            }
        }

        return false;
    }

}


