package mypackage;

import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;



import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UpdateSpreadsheet extends Authorization {
    /**
     * Pre-defined error value
     * -1 indicates invalid id
     * -2 indicates ID is correct but does not match with the right student
     * -3 indicates name is not in correct format. (First name, last name)
     * -4 indicates name is never found
     */
    private static final int INVALID_ID = -1;
    private static final int ID_Not_Match_With_Name = -2;
    private static final int INCORRECT_NAME_FORMAT= -3;

    /**
     * Keep track of how many
     * row has been filled already
     */
    private static int currentRow;


    /**
     * URL link
     */
    private static final String URL_FEED = "https://spreadsheets.google.com/feeds/worksheets/1wXIN0kQK1p3_Zff-xYQs_LQkz8reDo11yg3b6TAkYDg/public/full";

    /**
     * Current Date
     */
    private static final String currentDate = new java.util.Date().toString();


    /**
     * Return the number of row in
     * the spreadsheet to append
     * a new input from user to a
     * new row below the current row
     * @throws IOException
     */
    private static int totalRow()
            throws ServiceException, IOException,  URISyntaxException  {

        // Get spreadsheet service
        SpreadsheetService service = new SpreadsheetService("Attendance");

        // Define URL to request
        URL urlFeed = new URL(URL_FEED);

        // Get spreadsheet and store them in the List object
        SpreadsheetFeed feed = service.getFeed(urlFeed, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();



        // Declare Sheet entry to manipulate data
        SpreadsheetEntry sheetEntry = spreadsheets.get(0);


        // Fetch the data from google spreadsheet
        List<WorksheetEntry> worksheetEntry = sheetEntry.getWorksheets();

        // Count how many row already exist in the spread sheet




        return worksheetEntry.size();

    }

    /**
     * Return the number of row in
     * the spreadsheet to append
     * a new input from user to a
     * new row below the current row
     * @throws IOException
     */
    private static int totalCol()
        throws ServiceException, IOException {

        /// Get spreadsheet service
        SpreadsheetService service = new SpreadsheetService("Attendance");

        // Define URL to request
        URL urlFeed = new URL(URL_FEED);

        // Get spreadsheet feed and store them in the List object
        SpreadsheetFeed feed = service.getFeed(urlFeed, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();



        // Declare Sheet entry to manipulate data
        SpreadsheetEntry sheetEntry = spreadsheets.get(0);


        // Feed url to a List
        ListFeed listFeed = service.getFeed(sheetEntry.getWorksheetFeedUrl(), ListFeed.class);

        // Get access to that list
        ListEntry listEntry = listFeed.getEntries().get(0);

        System.out.println(listEntry.getCustomElements().getTags());
        // Return the total of columns
        return listEntry.getCustomElements().getTags().size();
    }


    /**
     * Get a specific cell value
     * within a row and col from the
     * spreadsheet. Return the
     * value within the cell if exist,
     * Otherwise return Null
     */
    private static void getCellValue()
            throws IOException, ServiceException, URISyntaxException {
        SpreadsheetService spreadsheetService = new SpreadsheetService("Attendance");

        URL urlSpreadsheet = new URL(URL_FEED);
        SpreadsheetFeed spreadsheetFeed = spreadsheetService.getFeed(urlSpreadsheet, SpreadsheetFeed.class);
        SpreadsheetEntry spreadsheetEntry = spreadsheetFeed.getEntries().get(0);

        WorksheetFeed worksheetFeed = spreadsheetService.getFeed(spreadsheetEntry.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheetEntries = worksheetFeed.getEntries();
        WorksheetEntry worksheetEntry = worksheetEntries.get(0);

        //

        ListFeed listFeed = spreadsheetService.getFeed(spreadsheetEntry.getWorksheetFeedUrl(), ListFeed.class);
        List<ListEntry> listEntries = listFeed.getEntries();

        for (ListEntry r : listEntries) {
            for (String tag : r.getCustomElements().getTags()) {
                System.out.println(r.getCustomElements().getValue(tag));
            }
            System.out.println("");
        }



    }


    /**
     * Update google spreadsheet
     * @param name
     * @param email
     * @param id
     * @throws IOException
     * @throws ServiceException
     */
    public static boolean updateSheet(String name, String id, String email)
            throws IOException, ServiceException, URISyntaxException {

        boolean validInput = false;
        int row = totalRow();
        int insertRow;



        // Check for user input. Make sure user input first and last name with their correct ID number
        // Make sure to insert to newColumn only. If all cells are fill then totalCol() == newColumn()
        if ((insertRow = isInputValid(name, id, row)) < row && insertRow >= 0) {


            // Build a new authorized API client service.
            Sheets service = getSheetsService();


            // Define requests for google API to update the spreadsheet
            List<Request> requests = new ArrayList<>();

            // Call to google API for request and update
            requests.add(new Request()
                    .setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
                            .setProperties(new SheetProperties()
                                    .setSheetId(0)
                                    .setTitle("Attendance"))
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
                                    .setColumnIndex(totalCol()))
                            .setRows(Arrays.asList(
                                    new RowData().setValues(values)))
                            .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

            // Final call to publish the updated sheet to google drive
            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                    .setRequests(requests);
            service.spreadsheets().batchUpdate(getSpreadsheetID(), batchUpdateRequest)
                    .execute();

            // Reset to true if user input is correct. Otherwise false
            validInput = true;
        }
        else if (insertRow == INVALID_ID) {
            System.out.println("Student ID can't be found");    // error handling
        }
        else if (insertRow == ID_Not_Match_With_Name){
            System.out.println("Student ID does not match with student's first and last name"); // error handling
        }
        else if (insertRow == INCORRECT_NAME_FORMAT) {
            System.out.println("Name can't be found or is not in a correct format");    // error handling
        }



        return validInput;
    }


    /**
     * Insert header name
     * to Spreadsheet
     * @throws IOException
     * @throws ServiceException
     */

    public static void insertHeader()
    throws IOException, ServiceException {

        // Build a new authorized API client service.
        Sheets service = getSheetsService();


        // Define requests for google API to update the spreadsheet
        List<Request> requests = new ArrayList<>();

        // Call to google API for request and update
        requests.add(new Request()
                .setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
                        .setProperties(new SheetProperties()
                                .setSheetId(0)
                                .setTitle("Attendance"))
                        .setFields("title")));


        // Update spreadsheet by appending the new information below the current row
        List<CellData> values = new ArrayList<>();

        // Add new header
        values.add(new CellData()
                .setUserEnteredValue(new ExtendedValue()
                        .setStringValue(new java.util.Date().toString())));
        requests.add(new Request()
                .setUpdateCells(new UpdateCellsRequest()
                        .setStart(new GridCoordinate()
                                .setSheetId(0)
                                .setRowIndex(0)
                                .setColumnIndex(totalCol()))
                        .setRows(Arrays.asList(
                                new RowData().setValues(values)))
                        .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

        // Final call to publish the updated sheet to google drive
        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        service.spreadsheets().batchUpdate(getSpreadsheetID(), batchUpdateRequest)
                .execute();

    }




    /**
     * Check if student ID and Name is valid.
     * First check for the ID and the row it is
     * at. Then check for the first and last name
     * at the row.
     *
     * The value -1 indicates that student ID can't be found
     * The value -2 indicates that student ID does not match with student's first and last name
     * The value -3indicates that name can't be found or is not entered correctly by student
     *
     * @throws IOException
     * @throws ServiceException
     */
    private static int isInputValid (String name, String id, int row)
            throws ServiceException, IOException, URISyntaxException {

        // Index to keep track of where to check "yes" to the corresponding first and last name at the correct row
        int index;
        boolean validName = true;


        // Make sure student input first and last name. This block will try to split name variable
        // into first name and last name. Making sure there is blank space separating first and last name
        try {

            // Split name into first and last name
            String firstName = name.split("\\s")[0];
            String lastName = name.split("\\s")[1];


            // Get all the first, last name and email then store into List of Array
            List<List<Object>> valueRangeList = getValueRange().getValues();

            // Check if student ID and the name input from user is valid. ID will be checked first then user's name
            if ((index = isIDValid(id)) < row &&
                    (validName = valueRangeList.get(index).toArray()[0].equals(lastName)) &&
                    (validName = valueRangeList.get(index).toArray()[1].equals(firstName))) {
            }
            else if (index > row) {
                index = INVALID_ID; // error for invalid ID
            }
            else if (!validName) {
                index = ID_Not_Match_With_Name; // error for invalid name
            }
        }catch(ArrayIndexOutOfBoundsException e) {
            index = INCORRECT_NAME_FORMAT;      // error for incorrect name format
        }



        return index;
    }



    /**
     * Check for ID if it matches
     * with the student ID and the user input
     * @throws IOException
     * @throws ServiceException
     */

    static int isIDValid(String id)
            throws IOException, ServiceException, URISyntaxException {


        // Store into List object
        List<List<Object>> valueRangeList = getValueRange().getValues();
        int index = 0;

        // Look for valid ID
        for (List list : valueRangeList) {
            if (list.get(3).equals(id)) {
                break;
            }
            index++;
        }

        return index;
    }


    /**
     * Get range of spreadsheet
     * @throws IOException
     */
    static ValueRange getValueRange() throws IOException{
        // Get spreadsheet service
        Sheets service = getSheetsService();

        String range = "Attendance!A2:D";

        // Get range
        return  service.spreadsheets().values().get(getSpreadsheetID(), range).execute();
    }


    /** Main */
    public static void main(String[] args) throws IOException, ServiceException, URISyntaxException {

        updateSheet("Tuyen Le", "2186948643345347", "tuyen_le92@rocketmail.com");

        System.out.println(totalCol());
    }


}


