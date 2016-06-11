package mypackage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.util.ServiceException;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

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
    private static final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);

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
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                SheetsQuickstart.class.getResourceAsStream("/client_secret.json");
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


    public static void update(String name, String email, String id) throws IOException, ServiceException, GeneralSecurityException {



        String p12Password = "notasecret";
        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/worksheets/19UE63tTGZ9w5Tid00IFKwWwvxM0G8Zn2HFKjhqlFm6g/public/full");;



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







        // Update value
        service.spreadsheets().values().batchUpdate(spreadsheetId,
                new BatchUpdateValuesRequest()
                        .setValueInputOption("RAW")
                        .set("Name", "tuyen")).execute();



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