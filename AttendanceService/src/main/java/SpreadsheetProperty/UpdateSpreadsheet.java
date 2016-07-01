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

/**
 * Created by TinTin on 6/29/16.
 * A class that will update the
 * spreadsheet
 */


public class UpdateSpreadsheet extends Authentication {


    /*****************************************************************************************************/


    /**
     * Keep track of how many
     * row and column. Row will be constant
     * throughout. However, column will
     * change overtime since we are adding
     * data in a horizontal position.
     */
    private static int attendanceColumn;


    /**
     * Current Date
     **/
    private static String CURRENT_DATE = new java.util.Date().toString();


    /**
     * Request service for google spreadsheet
     */
    private final List<Request> requests = getRequest(); // Define requests for google API to update the spreadsheet. The getRequest() method is inherited from Spreadsheet Class


    /*****************************************************************************************************/


    /**
     * Constructor to update the
     * current column, row and
     * date in the spreadsheet
     */
    public UpdateSpreadsheet()
            throws IOException, ServiceException, URISyntaxException {

        attendanceColumn = getMaxCols();       // get the current column of the spreadsheet. Inherited from Spreadsheet Property
    }


    /**
     * Update google spreadsheet
     * Puting a checkmark "yes" within
     * a particular row that matches
     * the student's username and id
     * indicating their presence
     *
     * @param username
     * @throws IOException
     * @throws ServiceException
     */
    public void updateAttendance(String username, String id)
            throws IOException, ServiceException, URISyntaxException {

        // The row that we want to update.  insertRow cannot exceed maximum row
        // If input is valid then return the row we want to update. Otherwise return -1
        // isInputValid() will check if there is a particular username or id within that row
        int updateRow = isInputValid(username, id);


        /**
         * Look for the column with the name "Last Access" or "Access"
         * If it is already created or not. If not then insert it into the spreadsheet.
         * Otherwise dont do anything. A value accessColumn = -1 means there is no such
         * column with the name "Last Access" or "Access"
         * **/
        int accessColumn = getAccessIndex();



        /** An empty column is the column that missing a value in a specific cell. Otherwise, the column is filled **/

        // If there is no "Attendance" and "Last Access" header column
        if (!emptyColumn() && accessColumn < 0) {


            // If both "Last Access" and "Attendance" header are missing
            // Insert "Last Access" and "Attendance" column header to the right most position of the spreadsheet
            insertAccessAndAttendanceHeader(attendanceColumn);

            updateAccessColumn(updateRow, attendanceColumn);

            // After the "Last Access" was inserted and updated. Increment the column by one to update the "Attendance" header column
            attendanceColumn++;

        }
        // If there is no "Attendance" header but there is a "Last Access" header
        else if (!emptyColumn() && accessColumn >= 0) {


            // If only "Attendance" header is missing but there is already a "Last Access" column
            insertAttendanceHeader(attendanceColumn);

            // Update the last time student login to the system
            updateAccessColumn(updateRow, accessColumn);

        }
        // If there exist both "Attendance" and "Last Access" header
        else {


            // If the new header is added,
            // current will be incremented by one so
            // We need to minus one so that it will stay in the
            // proper column throughout the entire process
            // of filling up the column until itsn't empty anymore
            attendanceColumn = attendanceColumn - 1;

            // Update the last time student login to the system
            updateAccessColumn(updateRow, accessColumn);

        }


        /** update spreadsheet by putting a checkmark "yes" to an appropriate row and column of a particular student **/
        List<CellData> values = new ArrayList<>();

        // Add checkmark yes to the header column "Attendance"
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue("yes")));


        // Add request for check mark "yes"
        requests.add(new Request()
                .setUpdateCells(new UpdateCellsRequest()
                        .setStart(new GridCoordinate()
                                .setSheetId(0)
                                .setRowIndex(updateRow + 1)
                                .setColumnIndex(attendanceColumn))
                        .setRows(Arrays.asList(
                                new RowData().setValues(values)))
                        .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));


        // Final call to publish the updated sheet to google drive
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        SHEET_SERVICE.spreadsheets().batchUpdate(getSpreadsheetID(), batchUpdateRequest)
                .execute();

        // }

    }


    /**
     * Continuously update the "Last Access" column
     * corresponded to the right row whenever a student login
     *
     * @param updateRow
     * @param accessColumn
     * @throws IOException
     */

    private void updateAccessColumn(int updateRow, int accessColumn)
            throws IOException {



        List<CellData> values = new ArrayList<>();

        // Update the time last access by student
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue(CURRENT_DATE)));


        // Add request for Access time
        requests.add(new Request()
                .setUpdateCells(new UpdateCellsRequest()
                        .setStart(new GridCoordinate()
                                .setSheetId(0)
                                .setRowIndex(updateRow + 1)
                                .setColumnIndex(accessColumn))
                        .setRows(Arrays.asList(
                                new RowData().setValues(values)))
                        .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

        // Final call to publish the updated sheet to google drive
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        SHEET_SERVICE.spreadsheets().batchUpdate(getSpreadsheetID(), batchUpdateRequest)
                .execute();
    }


    /**
     * Insert attendance header everytime the
     * entire column has been fill and the maximum
     * row has been reach. Simply insert a new "Attendance"
     * header to the right most column
     *
     * @param column
     * @throws IOException
     */

    private void insertAttendanceHeader(int column)
            throws IOException {



        List<CellData> values = new ArrayList<>();


        // Add "Attendance" header column
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue("Attendance on: " + CURRENT_DATE)));


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
        SHEET_SERVICE.spreadsheets().batchUpdate(getSpreadsheetID(), batchUpdateRequest)
                .execute();


    }


    /**
     * Insert an "Attendance" and "Last Access"
     * header column if there exist none in the Spreadsheet
     *
     * @throws IOException
     * @throws ServiceException
     */

    private void insertAccessAndAttendanceHeader(int column)
            throws IOException, ServiceException {


        // Update spreadsheet by appending the new information below the current row
        List<CellData> values = new ArrayList<>();

        // Add new "Last Access" header
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue("Last Access")));


        // Add new "Attendance" header
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue("Attendance " + CURRENT_DATE)));


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
        SHEET_SERVICE.spreadsheets().batchUpdate(getSpreadsheetID(), batchUpdateRequest)
                .execute();

    }


    /**
     * Check to see whether the entire column
     * is empty or not. In order to do that
     * we have to check to see whether each cell
     * within that column is empty.
     */
    private boolean emptyColumn()
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
    private boolean isEmptyField()
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



//    public static void main(String agv[]) throws IOException, ServiceException, URISyntaxException {
//
//
//        if (new Authentication("1wXIN0kQK1p3_Zff-xYQs_LQkz8reDo11yg3b6TAkYDg").isInputValid("sasonbaghdadi", "216722988") >= 0) {
//
//            Spreadsheet spreadsheet = new Spreadsheet("1wXIN0kQK1p3_Zff-xYQs_LQkz8reDo11yg3b6TAkYDg");
//            spreadsheet.setCourseTitle("CSC 130 section 101");
//            UpdateSpreadsheet updateSpreadsheet = new UpdateSpreadsheet();
//
//            updateSpreadsheet.updateSheet("sasonbaghdadi", "216722988");
//
//
//        }
//
//        if (new Authentication("1xXOeJvmKwgnjU2wB8ViwTMMs0Mqg-hu301gKgy4eBdI").isInputValid("tuyenle", "218694867") >= 0) {
//
//            Spreadsheet spreadsheet1 = new Spreadsheet("1xXOeJvmKwgnjU2wB8ViwTMMs0Mqg-hu301gKgy4eBdI");
//            spreadsheet1.setCourseTitle("CSC 130 section 200");
//            UpdateSpreadsheet updateSpreadsheet1 = new UpdateSpreadsheet();
//            updateSpreadsheet1.updateSheet("tuyenle", "218694867");
//        }
//
//
//        if (new Authentication("1wXIN0kQK1p3_Zff-xYQs_LQkz8reDo11yg3b6TAkYDg").isInputValid("dallasberry", "218676836") >= 0) {
//
//            Spreadsheet spreadsheet = new Spreadsheet("1wXIN0kQK1p3_Zff-xYQs_LQkz8reDo11yg3b6TAkYDg");
//            spreadsheet.setCourseTitle("CSC 130 section 101");
//            UpdateSpreadsheet updateSpreadsheet = new UpdateSpreadsheet();
//            updateSpreadsheet.updateSheet("dallasberry", "218676836");
//        }
//
//
//
//        if (new Authentication("1xXOeJvmKwgnjU2wB8ViwTMMs0Mqg-hu301gKgy4eBdI").isInputValid("ezaki", "210216160") >= 0) {
//
//            Spreadsheet spreadsheet1 = new Spreadsheet("1xXOeJvmKwgnjU2wB8ViwTMMs0Mqg-hu301gKgy4eBdI");
//            spreadsheet1.setCourseTitle("CSC 130 section 200");
//            UpdateSpreadsheet updateSpreadsheet1 = new UpdateSpreadsheet();
//            updateSpreadsheet1.updateSheet("ezaki", "210216160");
//        }
//
//    }


}


