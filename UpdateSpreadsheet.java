package mypackage;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.dublincore.Date;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateSpreadsheet {
    /**
     * Id of spreadsheet
     **/
    private static String SPREAD_SHEET_ID = "19UE63tTGZ9w5Tid00IFKwWwvxM0G8Zn2HFKjhqlFm6g";


    /**
     * Current active row (last row) of the spreadsheet
     */
    private static int ROW;


    /**
     * Application name.
     */
    private static final String APPLICATION_NAME =
            "Google Sheets API Java Quickstart";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials//sheets.googleapis.com-java-quickstart.json");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart.json
     */
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = new FileInputStream("/home/tuyen/IdeaProjects/application/src/main/resources/client_secret.json");
        // SheetsQuickstart.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     *
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    public static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


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
    public static void updateSheet(String name, String id, String email) throws IOException, ServiceException {

        if (isInputValid(name, id)) {
            // Get total row so the function will add new information below the current row
            ROW = totalRow();


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
                                    .setRowIndex(++ROW)
                                    .setColumnIndex(0))
                            .setRows(Arrays.asList(
                                    new RowData().setValues(values)))
                            .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

            // Final call to publish the updated sheet to google drive
            BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                    .setRequests(requests);
            service.spreadsheets().batchUpdate(SPREAD_SHEET_ID, batchUpdateRequest)
                    .execute();
        }
        else {
            System.out.println("Input is invalid. Make sure to enter " +
                    "first and last name with correct student ID");    // Let user know id or name can't be found. Input is invalid
        }

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
        return  service.spreadsheets().values().get(SPREAD_SHEET_ID, range).execute();
    }

    public static void main(String[] args) throws IOException, ServiceException {

        updateSheet("Tuyen Le", "218694867", "tuyen_le92@rocketmail.com");

    }


}
