package SpreadsheetProperty;

import com.google.api.services.sheets.v4.Sheets;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by TinTin on 6/29/16.
 * Spreadsheet Property such as
 * total row, column and its
 * ID
 */
public class Spreadsheet {

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

        // Get spreadsheet service
        SpreadsheetService service = new SpreadsheetService("Attendance");

        // Define URL to request
        URL urlFeed = new URL(url_feed);

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
        URL urlFeed = new URL(url_feed);

        // Get spreadsheet feed and store them in the List object
        SpreadsheetFeed feed = service.getFeed(urlFeed, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();



        // Declare Sheet entry to manipulate data
        SpreadsheetEntry sheetEntry = spreadsheets.get(0);


        // Feed url to a List
        ListFeed listFeed = service.getFeed(sheetEntry.getWorksheetFeedUrl(), ListFeed.class);

        // Get access to that list
        ListEntry listEntry = listFeed.getEntries().get(0);


        // Return the total of columns
        return listEntry.getCustomElements().getTags().size();
    }
}