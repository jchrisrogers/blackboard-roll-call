package SpreadsheetProperty;

import com.google.api.services.sheets.v4.Sheets;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * Created by TinTin on 6/29/16.
 * Spreadsheet Property such as
 * total row, column and its
 * ID
 */
public class Spreadsheet {

    // Get spreadsheet service
    private static final SpreadsheetService service = new SpreadsheetService("Attendance");

    /**
     * Spreadsheet ID
     */
    private String spreadsheetID;


    /**
     * Total Rows and columns of a spreadsheet
     */
    private int rows;
    private int cols;

    /**
     * URL link
     */
    private static String url_feed;


    /**
     * A static variable to get service from spreadsheet
     * to minimize the amount of time invoking getSheetService()
     * from mypackage.SpreadsheetProperty.Authorization class
     */
    protected static Sheets SHEET_SERVICE;
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
     * @param spreadsheetID
     */
    public Spreadsheet(String spreadsheetID)
            throws ServiceException, URISyntaxException, IOException {
        this.spreadsheetID = spreadsheetID;
        url_feed = "https://spreadsheets.google.com/feeds/worksheets/" + spreadsheetID + "/public/full";
        rows = totalRow();
        cols = totalCol();
    }



    /**
     * Return spreadsheet ID
     */
    protected  String getSpreadsheetID() {
        return spreadsheetID;
    }

    /**
     * Return total rows and columns
     */
    protected int getRows() {
        return rows;
    }

    protected int getCols() {
        return cols;
    }

    /**
     * Return URL Feed
     */
    protected static String getUrlFeed() {
        return url_feed;
    }


    /**
     * Return the number of row in
     * the spreadsheet to append
     * a new input from user to a
     * new row below the current row
     * @throws IOException
     */
    private static int totalRow()
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
     * @throws IOException
     */
    private static int totalCol()
            throws ServiceException, IOException {

        // Return the total of columns
        return getListEntry().get(0).getCustomElements().getTags().size();
    }


    /**
     * Get the list entry of all the row in the spreadsheet
     */
    protected static List<ListEntry> getListEntry() throws IOException, ServiceException {


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
    protected static Set<String> getHeaderRow() throws IOException, ServiceException {

        // Return access to that sheet
        return getListEntry().get(0).getCustomElements().getTags();

    }
}