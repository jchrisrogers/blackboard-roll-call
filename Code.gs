//  sheet name where data is to be written below
var SHEET_NAME = "Sheet1";
         

var SCRIPT_PROP = PropertiesService.getScriptProperties(); // new property service
 
// GET method
function doGet(e){
  return handleResponse(e); 
}

// POST method 
function doPost(e){
  return handleResponse(e);
}
 

function handleResponse(e) {

  // we want a public lock, one that locks for all invocations
  var lock = LockService.getPublicLock();
  lock.waitLock(30000);  // wait 30 seconds 
   
  
  
  
  try {
    // next set to write data, could write to multiple/alternate destinations
    var doc = SpreadsheetApp.openById(SCRIPT_PROP.getProperty("key"));
    var sheet = doc.getSheetByName(SHEET_NAME);
    
    // assume header is in row 1 but can override with header_row in GET/POST data
    var headRow = e.parameter.header_row || 1;
    var headers = sheet.getRange(1, 1, 1, sheet.getLastColumn()).getValues()[0];
    var nextRow = sheet.getLastRow()+1; // get next row
    var row = []; 
    
    // loop through the header columns
    for (i in headers){
      if (headers[i] == "Timestamp"){ // special case if you include a 'Timestamp' column
        row.push(new Date());
      } else { // else use header name to get data
        row.push(e.parameter[headers[i]]);
      }
    }
   
    
    // more efficient to set values as [][] array than individually
    sheet.getRange(nextRow, 1, 1, row.length).setValues([row]);
    
    

    // return json success results
    return ContentService
          .createTextOutput(JSON.stringify({"resulsdfsdfsdt":"success", "rozdfadadgaaragaegaegawfffgagegggew": nextRow}))
          .setMimeType(ContentService.MimeType.JSON);
  } catch(e) {
    // if error return this
    return ContentService
          .createTextOutput(JSON.stringify({"result":"error", "error": e}))
          .setMimeType(ContentService.MimeType.JSON);
  } finally { //release lock
    lock.releaseLock();
  }
   
}


function setup() {
    var doc = SpreadsheetApp.getActiveSpreadsheet();
    SCRIPT_PROP.setProperty("key", doc.getId());
}


// Get data 
function numberOfStd() {
  var doc = SpreadsheetApp.getActiveSpreadsheet();
  var sheet = doc.getSheetByName(SHEET_NAME);
  var lastPos = sheet.getLastRow();
  var studentName = [];
  var timeStamp = [];
  
  // Get student Name
  for (var i = 2; i <= lastPos; i++) {
    studentName.push(sheet.getRange(i,2,lastPos,2).getValue());
    timeStamp.push(sheet.getRange(i,1,lastPos,1).getValue());
  }
 
  Logger.log(studentName);
  Logger.log(timeStamp);
  
}
