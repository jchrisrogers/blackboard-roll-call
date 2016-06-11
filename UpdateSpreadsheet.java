package mypackage;







import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.List;

/** Update empty sheet **/
public class UpdateSpreadsheet  {


    /** Application name. */
    private static final String APPLICATION_NAME =
            "Google Sheets API Java Quickstart";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart.json");



    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/sheets.googleapis.com-java-quickstart.json
     */






    /** Create client id and secret **/
    private static final String clientID = "532786746523-n7r8fv173dc96kfmlo59bfd8cn5mmh0c.apps.googleusercontent.com";
    public static final String GOOGLE_ACCOUNT_USERNAME = "tle97@mail.ccsf.edu"; // Fill in google account username
    public static final String GOOGLE_ACCOUNT_PASSWORD = "Iloveyouforever!"; // Fill in google account password
    public static final String SPREADSHEET_URL = "https://docs.google.com/spreadsheets/d/1b4hMNU_H2CTot3s39643bw1OXtAXPZWdX7bC53d4n4A/edit";


    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */

    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */


    public static void update(String name, String email, String id) throws IOException, ServiceException, GeneralSecurityException {



        String p12Password = "notasecret";
        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/worksheets/19UE63tTGZ9w5Tid00IFKwWwvxM0G8Zn2HFKjhqlFm6g/public/full");;


        File p12 = new File("key.p12");

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream keyFileStream = ClassLoader.getSystemResourceAsStream("key.p12");

        keystore.load(keyFileStream, p12Password.toCharArray());
        PrivateKey key = (PrivateKey)keystore.getKey("privatekey", p12Password.toCharArray());

        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String[] SCOPESArray = {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/spreadsheets/private/full", "https://docs.google.com/feeds"};
        final List SCOPES = Arrays.asList(SCOPESArray);
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId("532786746523-n7r8fv173dc96kfmlo59bfd8cn5mmh0c.apps.googleusercontent.com")
                .setServiceAccountScopes(SCOPES)
                .setServiceAccountPrivateKey(key)
                .build();

        SpreadsheetService service = new SpreadsheetService("Test");

        service.setOAuth2Credentials(credential);
        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        if (spreadsheets.size() == 0) {
            System.out.println("No spreadsheets found.");
        }

        SpreadsheetEntry spreadsheet = null;
        for (int i = 0; i < spreadsheets.size(); i++) {
            if (spreadsheets.get(i).getTitle().getPlainText().startsWith("ListOfSandboxes")) {
                spreadsheet = spreadsheets.get(i);
                System.out.println("Name of editing spreadsheet: " + spreadsheets.get(i).getTitle().getPlainText());
                System.out.println("ID of SpreadSheet: " + i);
            }
        }



     

        // Update value
        /*service.spreadsheets().values().batchUpdate(spreadsheetId,
                new BatchUpdateValuesRequest()
                        .setValueInputOption("RAW")
                        .set("Name", "tuyen")).execute();*/



    }

    public static void main(String[] argv) throws IOException, ServiceException, GeneralSecurityException {

        UpdateSpreadsheet.update("lsdkfj","lsdkfj","foweijfw");
        /*
        List<Request> requests = new ArrayList<>();
        Sheets service = getSheetsService();
        String spreadsheetId = "19UE63tTGZ9w5Tid00IFKwWwvxM0G8Zn2HFKjhqlFm6g";
        requests.add(new Request()
                .setCopyPaste(new CopyPasteRequest()
                        .setSource(new GridRange()
                                .setSheetId(0)
                                .setStartRowIndex(0)
                                .setEndRowIndex(1)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(3))
                        .setDestination(new GridRange()
                                .setSheetId(0)
                                .setStartRowIndex(1)
                                .setEndRowIndex(6)
                                .setStartColumnIndex(0)
                                .setEndColumnIndex(3))
                        .setPasteType("PASTE_FORMAT")));

        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        service.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest)
                .execute();*/
    }


}
