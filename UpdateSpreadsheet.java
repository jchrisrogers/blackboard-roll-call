package mypackage;

import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateSpreadsheet extends EmptySheet {

    /**
     * Return the number of row in
     * the spreadsheet to append
     * a new input from user to a
     * new row below the current row
     *
     * @throws IOException
     */

    public static int totalRow()
            throws ServiceException, IOException {

        // Get spreadsheet service
        SpreadsheetService service = new SpreadsheetService("Attendance");

        // Define URL to request
        URL urlFeed = new URL("https://spreadsheets.google.com/feeds/worksheets/19UE63tTGZ9w5Tid00IFKwWwvxM0G8Zn2HFKjhqlFm6g/public/full");

        // Get spreadsheet and store them in the List object
        SpreadsheetFeed feed = service.getFeed(urlFeed, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        // Declare Sheet entry to manipulate data
        SpreadsheetEntry sheetEntry = spreadsheets.get(1);


        // Fetch the data from google spreadsheet
        List<WorksheetEntry> worksheetEntry = sheetEntry.getWorksheets();

        // Count how many row already exist in the spread sheet

        return worksheetEntry.size();

    }


    /**
     * Update google spreadsheet
     * @param name
     * @param email
     * @param id
     * @throws IOException
     * @throws ServiceException
     */
    public static boolean updateSheet(String name, String id, String email) throws IOException, ServiceException {

        boolean validInput = false;


        // Check for user input. Make sure user input first and last name with their correct ID number
        if (isInputValid(name, id)) {

            // Build a new authorized API client service.
            Sheets service = getSheetsService();

            SpreadsheetService spreadsheetService = new SpreadsheetService("Attendance");


            // Prints the names and majors of students in a sample spreadsheet:
            // https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit

            List<Request> requests = new ArrayList<>();

            // Change the name of sheet ID '0' (the default first sheet on every spreadsheet
            requests.add(new Request()
                    .setUpdateSheetProperties(new UpdateSheetPropertiesRequest()
                            .setProperties(new SheetProperties()
                                    .setSheetId(0)
                                    .setTitle("Attandance"))
                            .setFields("title")));


            // update spreadsheet by appending the new information below the current row
            List<CellData> values = new ArrayList<>();

            // Add Name, ID and Email in a horizontal position
            values.add(new CellData()
                    .setUserEnteredValue(new ExtendedValue()
                            .setStringValue(new java.util.Date().toString())));
            values.add(new CellData()
                    .setUserEnteredValue(new ExtendedValue()
                            .setStringValue(name)));
            values.add(new CellData()
                    .setUserEnteredValue(new ExtendedValue()
                            .setStringValue(id)));
            values.add(new CellData()
                    .setUserEnteredValue(new ExtendedValue()
                            .setStringValue(email)));
            requests.add(new Request()
                    .setUpdateCells(new UpdateCellsRequest()
                            .setStart(new GridCoordinate()
                                    .setSheetId(0)
                                    .setRowIndex(totalRow() + 1)
                                    .setColumnIndex(0))
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
        else {
            System.out.println("Input is invalid. Make sure to enter " +
                    "first and last name with correct student ID");    // Let user know id or name can't be found. Input is invalid
        }
        return validInput;
    }


    /**
     * Check if student ID and Name is valid to
     * prevent fraud and improve security
     * @throws IOException
     * @throws ServiceException
     */
    static boolean isInputValid (String name, String id)
            throws ServiceException, IOException {

        // Declare first and last name
        boolean validInput = false;           // default value. Assume user input only is valid for name and id. Otherwise true
        String firstName;
        String lastName;


        // Check if student ID and the name input from user is valid. ID will be checked first then user's name
        if (isIDValid(id) && correctNameFormat(name)) {

            // Split name into first and last name
            lastName = name.split("\\s")[1];
            firstName = name.split("\\s")[0];

            // Store into List object
            List<List<Object>> valueRangeList = getValueRange().getValues();

            // Look for valid name
            for (List list : valueRangeList) {
                if (list.get(0).equals(lastName) && list.get(1).equals(firstName)) {
                    validInput  = true;
                    break;
                }
            }
        }

        return validInput;
    }

    /**
     * Check if user enter first and
     * last name. Otherwise prompt
     * user to input again
     * @throws IOException
     * @throws ServiceException
     */

    static boolean correctNameFormat(String name)
            throws IOException, ServiceException {

        return name.split("\\s").length == 2;
    }


    /**
     * Check for ID if it matches
     * with the student ID and the user input
     * @throws IOException
     * @throws ServiceException
     */

    static boolean isIDValid(String id) throws IOException, ServiceException {


        // Store into List object
        List<List<Object>> valueRangeList = getValueRange().getValues();

        // Look for valid ID
        for (List list : valueRangeList) {
            if (list.get(3).equals(id)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Get range of spreadsheet
     * @throws IOException
     */
    static ValueRange getValueRange() throws IOException{
        // Get spreadsheet service
        Sheets service = getSheetsService();

        String range = "Form Responses 1!A2:D";

        // Get range
        return  service.spreadsheets().values().get(getSpreadsheetID(), range).execute();
    }


    /** Main */
    public static void main(String[] args) throws IOException, ServiceException {

        updateSheet("Tuyen Le", "218694867", "tuyen_le92@rocketmail.com");

    }


}
