var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var fs = require('fs');

// List of search coordinates
var coordinates = [{ "loc" : "47.8438918,12.9546627", "rad" : "2000"},
 { "loc" : "47.8879258,13.0593995", "rad" : "4000"}, { "loc" : "47.7858241,13.1542137", "rad" : "2000"},
 { "loc" : "47.7457299,13.0588197", "rad" : "2000"}, { "loc" : "47.766874,13.051222", "rad" : "2000"}, 
 { "loc" : "47.6663162,13.006398", "rad" : "5000"}, { "loc" : "47.7207501,12.9060064", "rad" : "5000"},
 { "loc" : "47.8171162,12.867355", "rad" : "5000"}, { "loc" : "47.8008757,13.0309893", "rad" : "1500"},
 { "loc" : "47.7992904,13.0386598", "rad" : "1500"}, { "loc" : "47.7754471,13.0687329", "rad" : "1500"},
 { "loc" : "47.7991585,13.068397", "rad" : "1500"}, { "loc" : "47.808539,12.9489838", "rad" : "1500"},
 { "loc" : "47.7797764,13.0253661", "rad" : "1500"}, { "loc" : "47.7240028,13.0705351", "rad" : "1500"}];

var apiKey = "AIzaSyD93VVlX1hr7X5I7LRMfU7GxzjOmgbS0Fk";
var searchRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
var requestString = "";
var allRestaurants = [];
var continueCrawl = true;

var xmlhttp = new XMLHttpRequest();

xmlhttp.onreadystatechange = function() {
  
  // Check if request was successfull
  if (this.readyState == 4 && this.status == 200) {

    // Retrieve results
    var myArr = JSON.parse(this.responseText);
    var tmpRestaurants = myArr["results"];

    var addRestaurant = true;

    // Go thorugh restaurants in result
    tmpRestaurants.forEach(element => {

      // Checks if restaurant is already in the List
      for (var i = 0; i < allRestaurants.length; i++) {
        if (allRestaurants[i].id == element.id) {
          addRestaurant = false;
        }
      }

        // Delete all unwanted attributes and add restaurant
        if (addRestaurant) {
          delete element.photos;
          delete element.rating;
          delete element.types;
          delete element.scope;
          delete element.reference;
          delete element.place_id;
          delete element.geometry.viewport;
          allRestaurants.push(element);
        }
    });

    // If there is a next page, add the token to the end
    if ("next_page_token" in myArr) {
      // If it's not the initial request only the next_page_token needs
      // to be added
      var i = requestString.indexOf("&pagetoken");
      if (i != -1) {
        requestString = requestString.slice(0,176);
        requestString += myArr["next_page_token"];

      // Replace the pagetoken
      } else {
        requestString += "&pagetoken=" + myArr["next_page_token"];
        
      }
    // If there is no next page end loop
    } else {
      continueCrawl = false;
      console.log("finished crawling");
    }
  }
};

// Crawl over every search coordinate
coordinates.forEach(function(element) {

  continueCrawl = true;

  // Define request String
  var location = element.loc;
  var radius = element.rad; //meters
  // Can be extended in the future
  var type = "restaurant";
  requestString = searchRequest + "location=" + location + "&radius=" + radius + "&type=" + type +"&key=" + apiKey;
  
  // Continue crawling until there is no next page
  while (continueCrawl) {
    xmlhttp.open("GET",requestString, false);
    xmlhttp.send();
    sleep(500);
  }
});

  
var restaurantString = JSON.stringify(allRestaurants);
console.log("Writing json file");
fs.writeFile("test2.json", restaurantString);

/* Next_Page_Token needs a moment to be created, this function blocks
further processing until the set time is reached.
*/
function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}


