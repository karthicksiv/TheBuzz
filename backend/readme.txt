Developer Manual

Welcome to the Buzz Backend.


1) Compilation and Setting up

Make sure you are in the right directory. Check if pom.xml is in the directory that you are in.

Once you are in the correct directory, please type:

mvn compile

Doing so will compile the backend.

2) Testing the Buzz

Once the source files are compiled, please type:

mvn test

This will run the tests in the test directory.

note: some of the tests were omitted such as createTable() to prevent the tests from removing the current database setup. Simply remove // or /* */ to run these tests as well.

3) Deploying to local directory

in the directory with pom.xml, please type:

mvn exec:java

this will host the program in the port that you have set to. You can check if the directories are working using curl command from the command line or you can also use the web browser of your choice to connect to the localhost.


