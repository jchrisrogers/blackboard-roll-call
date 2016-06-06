/**
 * Created by tuyen on 6/4/16.
 */



// Variable to hold request
var request;


// Bind to the submit event of our form
$("#require").submit(function (event) {

  // Get element from html
  var name = document.getElementById('name').value;
  var id = document.getElementById('id').value;
  var email = document.getElementById('email').value;



  // Make sure all fields are valid input
  if (name == "" || id == "" || email == "") {
    alert("Required fields can't be empty");
  }
  else if (!name.match(/[0-9]/) && !id.match(/[a-zA-Z]/)) {

    // Abort any pending request
    if (request) {
      request.abort();
    }
    // setup some local variables
    var $form = $(this);

    // Let's select and cache all the fields
    var $inputs = $form.find("input, select, button, textarea");


    // Serialize the data in the form
    var serializedData = $form.serialize();
    serializedData = serializedData + "&myIP=" + userip;
   
    // Let's disable the inputs for the duration of the Ajax request.
    // Disabled form elements will not be serialized.
    $inputs.prop("disabled", true);

    // Fire off the request to google sheet
    request = $.ajax({
      url: "https://script.google.com/macros/s/AKfycbyKC3gLIxUy8rD1ZxsAdwZRgZ3rzvPrfHv6IfaLz0kLYALehpc/exec",
      type: "post",
      data: serializedData
    });


    //Prevent default posting of form
    event.preventDefault();
  }
  else {
    alert("Field Name should not contain numeric value and ID field should not contain letter");
  }


});

