package SpreadsheetProperty;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.UpdateSheetPropertiesRequest;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by TinTin on 6/29/16.
 * Spreadsheet Property such as
 * total row, column and its
 * ID
 */
public class Spreadsheet {

    /*************************************PRIVATE VARIABLE****************************************************************/

    /**
     * Get spreadsheet service
     **/
    private static final SpreadsheetService service = new SpreadsheetService("Attendance");



    /**
     * Spreadsheet ID
     */
    private String spreadsheetID;



    /**
     * Total Rows and columns of a spreadsheet
     **/


    /**
     * URL link
     **/
    private static String url_feed;


    /*****************************************Constructor and Methods************************************************************/

    /**
     * A static variable to get service from spreadsheet
     * to minimize the amount of time invoking getSheetService()
     * from mypackage.SpreadsheetProperty.Authorization class
     */
    static Sheets SHEET_SERVICE;

    static {
        try {
            SHEET_SERVICE = Authorization.getSheetsService();
        } catch (IOException e) {
            System.exit(1);
        }
    }


    /**
     * Constructor that takes
     * int a spreadsheet ID
     *
     * @param spreadsheetID
     */
    Spreadsheet(String spreadsheetID)
            throws ServiceException, URISyntaxException, IOException {
        this.spreadsheetID = spreadsheetID;
        url_feed = "https://spreadsheets.google.com/feeds/worksheets/" + spreadsheetID + "/public/full";

    }


    /**
     * Default constructor, doesn't do anything
     **/
    Spreadsheet() {
        // Empty
    }


    /**
     * Return spreadsheet ID
     */
    String getSpreadsheetID() {
        return spreadsheetID;
    }

    /**
     * Return total rows and columns
     */
    public int getMaxRows() throws ServiceException, IOException, URISyntaxException {
        return totalRow();
    }

    public int getMaxCols() throws ServiceException, IOException, URISyntaxException {
        return totalCol();
    }

    /**
     * Return URL Feed
     */
    String getUrlFeed() {
        return url_feed;
    }





    /**
     * Return the number of row in
     * the spreadsheet to append
     * a new input from user to a
     * new row below the current row
     *
     * @throws IOException
     */
    private int totalRow()
            throws ServiceException, IOException, URISyntaxException {


        // Define URL to request
        URL urlFeed = new URL(url_feed);

        // Get spreadsheet and store them in the List object
        SpreadsheetFeed feed = service.getFeed(urlFeed, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();


        // Count how many row already exist in the spread sheet
        return spreadsheets.get(0).getWorksheets().size();

    }


    /**
     * Return the number of row in
     * the spreadsheet to append
     * a new input from user to a
     * new row below the current row
     *
     * @throws IOException
     */
    private int totalCol()
            throws ServiceException, IOException {

        // Return the total of columns
        return getListEntry().get(0).getCustomElements().getTags().size();
    }


    /**
     * Get the list entry of all the row in the spreadsheet
     */
    List<ListEntry> getListEntry() throws IOException, ServiceException {


        // Define URL to request
        URL urlFeed = new URL(url_feed);

        // Get spreadsheet feed and store them in the List object
        SpreadsheetFeed feed = service.getFeed(urlFeed, SpreadsheetFeed.class);

        List<SpreadsheetEntry> spreadsheets = feed.getEntries();


        // Feed url to a List
        ListFeed listFeed = service.getFeed(spreadsheets.get(0).getWorksheetFeedUrl(), ListFeed.class);

        // Return access to that sheet
        return listFeed.getEntries();

    }

    /**
     * Get the row header of the spreadsheet
     */
    Set<String> getHeaderRow() throws IOException, ServiceException {

        // Return access to that sheet
        return getListEntry().get(0).getCustomElements().getTags();

    }

//    public static void main(String arg[]) throws IOException, ServiceException, URISyntaxException {
//        System.out.println(new Spreadsheet("1xXOeJvmKwgnjU2wB8ViwTMMs0Mqg-hu301gKgy4eBdI").getMaxCols());
//    }

}