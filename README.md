To run this app, open it in any IDE, I prefer InteliJ and then just run main class UTestApplication 

Endpoints, which can be invoke in Postman:

* GET http://localhost:9999/api/countries/unique - fetch unique countries
* GET http://localhost:9999/api/devices/unique   - fetch unique devices

Fetch ranking for testers - sample queries
* GET http://localhost:9999/api/testers/ranking?countries=JP&phones=iPhone 5&phones=iPhone 4
* GET http://localhost:9999/api/testers/ranking?countries=ALL&phones=iPhone 5&phones=iPhone 4
* GET http://localhost:9999/api/testers/ranking?countries=JP&phones=ALL

