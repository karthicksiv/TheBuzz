# BMW team #

## Phase 1 ##

* Quick summary \
The goal is to start small... a cloud-hosted web system, plus an Android app, so that employees can post
short messages, and other employees can up-vote and down-vote. But you know this is going to get big...
real big... so it would be good to do things right the first time, and anticipate the growing pains that you’re
sure to encounter once everyone realizes that you’ve invented the next big thing. 
* [Manager Report template](https://piazza.com/class_profile/get_resource/k504tsv65th4np/k6fsnfj4iqy4b7)
* [Assignment Detail](https://piazza.com/class_profile/get_resource/k504tsv65th4np/k6dvzpkwrlm5hm)

## Tutorial websites ##

* [1.Managing Projects with Trello](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_trello/index.html)
* [2.Introduction to Git](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_git/index.html)
* [3.Maven and Databases](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_maven_postgres/index.html)
* [4.RESTful Web Services](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_spark/index.html)
* [5.Creating a Web Front-End](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_typescript_jquery/index.html)
* [6.Building an Android App](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_android_volley/index.html)
* [7.Unit Testing](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_junit_espresso_jasmine/index.html)
* [8.Heroku, PaaS, and the 12-Factor App](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_heroku/index.html)
* [9.Understanding CORS](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_cors/index.html)
* [10.Maintainable, Beautiful Web Front-Ends](http://www.cse.lehigh.edu/~spear/cse216_tutorials/tut_bootstrap_handlebars/index.html)

## Assignments 
### The Back End ###

The student who assumes the role of Back-End Developer for this task will use the Java Spark
Framework [2](http://sparkjava.com/) to create a dynamic web server. Based on the design from class, the server will provide
several “routes”, through which users can “POST” new messages by sending JSON, and “like” messages,
again by sending JSON. The server will return a list of messages, along with a count of “likes”, as JSON.
The server must return proper HTTP status codes.
The back-end programmer will be persisting the results of the HTTP requests to a PostgreSQL database.
The server must read its configuration (at least the port on which to listen, and the reference to the
database) from environment variables. Heroku will give you the variables you need for using
PostgreSQL.
The back end developer must also produce unit tests that are appropriate for ensuring the correctness of
the back end. Remember: the back end will change in the next phase: things that shouldn’t break as the
back end is extended should have tests that accompany them.

### The Android App ###

Once the server is online, the Android app can begin interacting with it. The app must show a list of
messages, allow the user to create messages, and allow the user to “like” messages. Your app should be
backward compatible to v21 (Android 5.0 Lollipop, SDK level 21). You are allowed to lock the screen in 
portrait mode. Be sure to use Volley [3](https://developer.android.com/training/volley/request.html#request-json) and RecyclerView [4](http://stackoverflow.com/questions/28392554/should-we-use-recyclerview-to-replace-listview).
The Android developer must also produce Espresso tests for both the logic and the UI of the Android app. 

### The Web Developer ###

Your team’s web developer will create a basic web application that performs GET and POST requests to
the server, in order to show a list of messages, allow the creation of new messages, and allow a user to
“like” messages. Just like the Android app, the web app must be “live”, and interact directly with the back
end. You should implement your program as a “single-page web app”, where the back end is as
uncoupled from the web front end as possible. It is OK to rely on the back end for serving static content,
but the only dynamic content it serves should be JSON, never HTML.
The Web Developer should work with the Project Manager to develop a reasonable workflow for building
the web source (starting from the ideas behind “deploy.sh” in Phase 0). The process by which team
members can deploy the web code must be straightforward and clean. The web front end sources should
not be checked into the back-end’s folder hierarchy.
Note: the web front end should also use Handlebars [5](http://handlebarsjs.com) for templating, and Bootstrap [6](http://getbootstrap.com) for styling. The
Android and Web front-ends should be as similar as possible.
The web developer must also produce Jasmine tests for both the logic and the UI of the web front end. 

### The Admin App ###

If your team has only less than 5 members, the Project Manager will also work as the Admin App
Developer. There is not much work to be done on the admin app, but the work is important. You should
make sure that a standalone Java program is able to drop tables from the database, recreate tables from the
database, and do any other maintenance that needs to be done. And, of course, there should be unit tests.
In addition, the admin app developer should directly assist the other programmers in two ways. First,
through peer programming, the admin app developer can sit with another teammate and provide over-theshoulder advice. Second, while the admin app developer can’t check in code for teammates, the admin
app developer can help teammates to develop a good testing strategy. 

## Questions to be answered 

### The Back End ###
* Describe the REST API endpoints that your team agreed to implement. If this deviates significantly from class discussion, explain why.
* Describe the Data Model that your team agreed to implement. If this deviates significantly from class discussion, explain why.
* Does the back-end match the description above? Why or why not?
* Is the back-end code appropriately organized into files / classes / packages?
* Are variables, functions, classes and packages named well?
* Are the dependencies in the pom.xml file appropriate? Were there any unexpected dependencies added to the program?
* What was the biggest issue that came up in code review of the back-end server?
* What technical debt do you see in the current back-end server implementation?
* Are the unit tests for the back-end server complete/satisfactory? If yes, why do you have confidence in your answer. If not, what's missing?
* What challenges did you observe on account of the relationship between the database and the server? Was mocking used to separate tests? Why or why not?
### The Android App ###
* Are the mechanisms for moving among Activities satisfactory and appropriate?
* Describe any significant deviations from the UI design discussed in class.
* Is the android app appropriately organized into files / classes / packages?
* Are variables, functions, classes and packages named well?
* Are the dependencies in the build.gradle file appropriate? Were there any unexpected dependencies added to the program?
* What was the biggest issue that came up in code review of the Android app?
* What technical debt do you see in the current Android app implementation?
* Are the unit tests for the Android app complete/satisfactory? If yes, why do you have confidence in your answer. If not, what's missing?
### The Web Developer ###
* What is the overall structure of the classes used to implement the web front-end?
* What benefits did you observe on account of the use of Singletons?
* Does the user interface match the interface for the Android app? Why or why not?
* Is the web front-end appropriately organized into files / classes / packages?
* Are variables, functions, classes and packages named well?
* Are the dependencies in the package.json file appropriate? Were there any unexpected dependencies added to the program?
* What was the biggest issue that came up in code review of the web front-end?
* What technical debt do you see in the current web front-end implementation?
* Are the unit tests for the web front-end complete/satisfactory? If yes, why do you have confidence in your answer. If not, what's missing?
### The Admin App ###
* Is the Admin app appropriately organized into files / classes / packages?
* Are variables, functions, classes and packages named well?
* Are the dependencies in the pom.xml file appropriate? Were there any unexpected dependencies added to the program?
* What was the biggest issue that came up in code review of the Admin app?
* What technical debt do you see in the current Admin app implementation?
* Are the unit tests for the Admin app complete/satisfactory? If yes, why do you have confidence in your answer. If not, what's missing?
### Project-Wide ###
* Were any difficulties encountered when generating documentation for the Java and TypeScript code?
* Were there any issues with the use of git for source control?
* Were there any team issues that arose?
* How did the amount of time your teammates spent compare to the amount of time you thought the tasks would take?
* Describe the most significant obstacle or difficulty your team faced.
* What is your biggest concern as you think ahead to the next phase of the project?
* Submit documentation for the user interface for the apps.
* Submit the completed “CSE216 Project Manager Design Thinking Phase 1” form.
