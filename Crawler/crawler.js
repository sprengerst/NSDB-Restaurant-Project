var XMLHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var fs = require('fs');

var apiKey = "AIzaSyD93VVlX1hr7X5I7LRMfU7GxzjOmgbS0Fk";
var searchRequest = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=47.8081806,13.0378785&radius=3000&type=restaurant";

var requestString = searchRequest + "&key=" + apiKey;

var allRestaurants = [];
var continueCrawl = true;

var xmlhttp = new XMLHttpRequest();

var counter = 0;


xmlhttp.onreadystatechange = function() {

  
  if (this.readyState == 4 && this.status == 200) {

    var myArr = JSON.parse(this.responseText);

    var tmpRestaurants = myArr["results"];

    tmpRestaurants.forEach(element => {
      delete element.photos;
      delete element.rating;
      delete element.types;
      delete element.scope;
      delete element.reference;
      delete element.place_id;
      delete element.geometry.viewport;
      delete element.weekday_text;
      allRestaurants.push(element);
    });
    if ("next_page_token" in myArr) {
      var i = requestString.indexOf("&pagetoken");
      if (i != -1) {
        requestString = requestString.slice(0,176);
        requestString += myArr["next_page_token"];

      } else {
        requestString += "&pagetoken=" + myArr["next_page_token"];
        
      }
    } else {
      continueCrawl = false;
      console.log("finished crawling");
    }
  }
  
};

while (continueCrawl) {
  xmlhttp.open("GET",requestString, false);
  xmlhttp.send();
  sleep(500);
}

var restaurantString = JSON.stringify(allRestaurants);
console.log("Writing json file");
console.log(allRestaurants.length);
fs.writeFile("test.json", restaurantString);

function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}
