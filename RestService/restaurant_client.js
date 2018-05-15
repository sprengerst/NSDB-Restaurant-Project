#!/usr/bin/env nodejs
var http = require('http');
var express = require('express');
var bodyParser = require('body-parser');

const dbName = 'RestaurantDB';
const collectionName = 'Restaurants';
const url = 'mongodb://localhost:27017';

var MongoClient = require('mongodb').MongoClient;
var app = express();
app.use(bodyParser.json());

app.get('/getRestaurants', function (req, res) {
  MongoClient.connect(url, function (err, client) {
    if (!err) {
      // Get DB
      const db = client.db(dbName);

      // Get the documents collection
      const collection = db.collection(collectionName);

      // Find some documents
      collection.find({}).toArray(function (err, docs) {
        if (!err) {
          res.write(JSON.stringify(docs));
          res.end();
        } else {
          res.status(500);
          res.write('Error reading collection Restaurants');
          res.end();
        }
      });
    } else {
      res.status(500);
      res.write('Error connecting to DB');
      res.end();
    }
  });
});

app.post('/createOrUpdateRestaurant', function (req, res) {
  MongoClient.connect(url, function (err, client) {
    if (!err) {
      // Get DB
      const db = client.db(dbName);

      // Get the documents collection
      const collection = db.collection(collectionName);

      var id = req.body.id;
      if (id) {
        collection.find({ id: id }).toArray(function (err, docs) {
          if (!err) {
            if (docs.length == 0) { // new item
              collection.insertOne(req.body, function (err, r) {
                if (!err) {
                  res.write('Record successfully created');
                  res.end();
                } else {
                  res.status(500);
                  res.write('Error creating record');
                  res.end();
                }
              });
            } else { // update 
              collection.deleteOne({ id: id }, function (err, r) {
                if (!err) {
                  collection.insertOne(req.body, function (err, r) {
                    if (!err) {
                      res.write('Record successfully updated');
                      res.end();
                    } else {
                      res.status(500);
                      res.write('Error updating record');
                      res.end();
                    }
                  });
                } else {
                  res.status(500);
                  res.write('Error deleting record');
                  res.end();
                }
              });
            }
          } else {
            res.status(500);
            res.write('Error reading collection Restaurants');
            res.end();
          }
        });
      } else {
        res.status(400);
        res.write('Invalid request. No id specified.');
        res.end();
      }
    } else {
      res.status(500);
      res.write('Error connecting to DB');
      res.end();
    }
  });
});

http.createServer(app).listen(8080, function () {
  console.log('Server started on port 8080');
});