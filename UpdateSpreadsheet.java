package mypackage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.common.collect.Lists;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.ServiceException;
import com.google.api.services.content.ShoppingContent;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;


/** Update empty sheet **/
public class UpdateSpreadsheet  {


    /** Application name. */
    private static final String APPLICATION_NAME =
            "Google Sheets API Java Quickstart";

    /** SpreadSheet ID */
    private  static final String SPREADSHEET_ID = "1IX_9DMEecQAK9_1_EYA4Gs0JfeOBSwww0vSLu2Xq3NY";

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
            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

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
    private static Credential authorize() throws IOException {
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



    /**
     * Build and return an authorized Sheets API client service.
     * @return an authorized Sheets API client service
     * @throws IOException
     */

    private static Sheets getSheetsService() throws IOException {
        Credential credential = authorize();
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }


    /** Authorization for updating spreadsheet
     * @return an authorized Sheets API client service
     * @throws IOException
     * @throws ServiceException
     * @throws GeneralSecurityException
     * **/
    private static Credential authorizeForUpdateSheet() throws IOException, ServiceException, GeneralSecurityException {

        String p12Password = "notasecret";
        URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/worksheets/" + SPREADSHEET_ID + "/public/full");



        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream keyFileStream = ClassLoader.getSystemResourceAsStream("key.p12");

        keystore.load(keyFileStream, p12Password.toCharArray());
        PrivateKey key = (PrivateKey)keystore.getKey("privatekey", p12Password.toCharArray());

        HttpTransport httpTransport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String[] SCOPESArray = {"https://spreadsheets.google.com/feeds", "https://spreadsheets.google.com/feeds/spreadsheets/private/full", "https://docs.google.com/feeds"};
        final List SCOPES = Arrays.asList(SCOPESArray);

        // Build Spreadsheet
        /*SpreadsheetService service = new SpreadsheetService("Test");

        service.setOAuth2Credentials(credential);
        SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();*/

        return new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(jsonFactory)
                .setServiceAccountId("hallowed-trail-133723@appspot.gserviceaccount.com")
                .setServiceAccountScopes(SCOPES)
                .setServiceAccountPrivateKey(key)
                .build();

    }

    /** Get service for updating spreadsheet
     * @return Sheets
     * @throws ServiceException
     * @throws GeneralSecurityException
     * @throws IOException
     * **/
    private static Sheets updateSheetService() throws ServiceException, GeneralSecurityException, IOException {
        Credential credential = authorizeForUpdateSheet();
        SpreadsheetService service = new SpreadsheetService("Test");
        service.setOAuth2Credentials(credential);

        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /** Update Spreadsheet **/
    /*public static void updateSpreadsheet(String name, String id, String email) throws IOException, GeneralSecurityException, ServiceException {
        Sheets service = updateSheetService();

        // Update value
        service.spreadsheets().values().batchUpdate(SPREADSHEET_ID,
                new BatchUpdateValuesRequest()
                        .setValueInputOption("RAW")
                        .set("Name", "tuyen")).execute();
    }*/



    private static void setUp() throws GeneralSecurityException, IOException {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        // Go to the Google Developers Console, open your application's
        // credentials page, and copy the client ID and client secret.
        // Then paste them into the following code.
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
        details.setClientId("532786746523-n7r8fv173dc96kfmlo59bfd8cn5mmh0c.apps.googleusercontent.com");
        details.setClientSecret("yu3ZgNxt7grMItEx-_LZWuAB");

        // Or your redirect URL for web based applications.
        String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
        String scope = "https://www.googleapis.com/auth/content";

        GoogleClientSecrets clientSecrets = new GoogleClientSecrets();
        clientSecrets.setInstalled(details);

        GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow
                .Builder(httpTransport, jsonFactory, clientSecrets, newArrayList(scope))
                .setAccessType("offline").build();

        String authorizeUrl =
                authorizationFlow.newAuthorizationUrl().setRedirectUri(redirectUrl).build();
        System.out.println("Paste this url in your browser: \n" + authorizeUrl + '\n');

        // Wait for the authorization code.
        System.out.println("Type the code you received here: ");
        String authorizationCode = new BufferedReader(new InputStreamReader(System.in)).readLine();

        // Authorize the OAuth2 token.
        GoogleAuthorizationCodeTokenRequest tokenRequest =
                authorizationFlow.newTokenRequest(authorizationCode);
        tokenRequest.setRedirectUri(redirectUrl);
        GoogleTokenResponse tokenResponse = tokenRequest.execute();

        // Create the OAuth2 credential.
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(new NetHttpTransport())
                .setJsonFactory(new JacksonFactory())
                .setClientSecrets(clientSecrets)
                .build();

        // Set authorized credentials.
        credential.setFromTokenResponse(tokenResponse);

        ShoppingContent service = new ShoppingContent
                .Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("YOUR_APPLICATION_NAME")
                .build();
    }


    public static void main(String[] argv) throws IOException, ServiceException, GeneralSecurityException {
        setUp();
        /*Sheets service = updateSheetService();
       service.spreadsheets().values().batchUpdate(SPREADSHEET_ID,
                new BatchUpdateValuesRequest()
                        .setValueInputOption("RAW").setData()*/


        /*List<Request> requests = new ArrayList<>();
        Sheets service = updateSheetService();
        String spreadsheetId = SPREADSHEET_ID;
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
                .execute();
       */
    }


}
