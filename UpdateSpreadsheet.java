package mypackage;

import com.google.api.client.json.Json;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.api.services.sheets.v4.Sheets;
import java.util.Date;
import java.util.List;
import org.json.*;



import java.io.IOException;

/** Update empty sheet **/
public class UpdateSpreadsheet extends SheetsQuickstart {


    public static void update(String name, String email, String id) throws IOException, JSONException {
        Sheets service = SheetsQuickstart.getSheetsService();
        String spreadsheetId = "19UE63tTGZ9w5Tid00IFKwWwvxM0G8Zn2HFKjhqlFm6g";
        String range = "Sheet1!A2:D";


        // Get data into an array of Data set
        List <ValueRange> valueRangeList =;



        // Update value
        service.spreadsheets().values().batchUpdate(
                spreadsheetId, new BatchUpdateValuesRequest()
                                .setValueInputOption("RAW")
                                .setData(valueRangeList));



    }

    public static void main(String[] argv) throws IOException {
        // empty
    }


}
