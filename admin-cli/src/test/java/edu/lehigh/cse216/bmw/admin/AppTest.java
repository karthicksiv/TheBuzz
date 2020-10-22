package edu.lehigh.cse216.bmw.admin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.*;
//import java.util.ArrayList;
import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    private Database db = Database.getDatabase(
            "postgres://ozslfyfuvohvfc:c34ae7a7c99d96a87b96b0f800fb4ab15a37196ffedf7064f38bf6ef60dd0c2d@ec2-184-72-235-80.compute-1.amazonaws.com:5432/d1ebcj9clt9npg");
    // private RowData rd = new RowData(0, null, null, 0);

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    public void testGetString() {
        String test = "test";
        Reader inputString = new StringReader(test);
        BufferedReader in = new BufferedReader(inputString);
        assertEquals("test", App.getString(in, "Message Input"));
        assertTrue(true);
    }

    public void testPrompt() {
        String prompt = "T";
        Reader inputString = new StringReader(prompt);
        BufferedReader in = new BufferedReader(inputString);
        assertEquals("T", prompt);
    }

    public void testGetInt() {
        int integer = 2;
        Reader inputString = new StringReader(Integer.toString(integer));
        BufferedReader in = new BufferedReader(inputString);
        assertEquals(2, App.getInt(in, "Message Input"));
        assertTrue(true);
    }

    public void testGetDatabase() {
        assertNotNull(db);
    }

    /*
     * public void testCreateTable(){ db.createTable(); assertEquals(1,
     * db.uInsertRow("test", "test bio")); assertEquals(1, db.mInsertRow("test",
     * "test message", 1, "attachment", "mLink")); assertEquals(1,
     * db.cInsertRow("test", 1, 1)); assertEquals(1, db.vInsertRow(1, 1, 1));
     * db.dropTable(); }
     */
    public void testInsertRows() {
        // test prep
        db.createTable();

        // test execution
        assertEquals(1, db.uInsertRow("testinsert", "test bio"));
        assertEquals(1, db.mInsertRow("subject: insert", "this is a test insert", 1, "attachmenttest", "link"));
        assertEquals(1, db.cInsertRow("test comment. this is testing", 1, 1));
        assertEquals(1, db.vInsertRow(1, 1, 1));

        // test cleanup
        db.dropTable();
    }

    public void testSelectAll() {
        // test prep
        db.createTable();
        db.uInsertRow("testinsert", "test bio");
        db.mInsertRow("subject: insert", "this is a test insert", 1, "attachment", "link");
        db.cInsertRow("test comment. this is testing", 1, 1);
        db.vInsertRow(1, 1, 1);

        // Actual test
        assertEquals(1, db.uSelectAll().size());
        assertEquals(1, db.mSelectAll().size());
        assertEquals(1, db.cSelectAll().size());
        assertEquals(1, db.vSelectAll().size());

        // test cleanup
        db.dropTable();
    }

    public void testSelectOne() {
        // test prep
        db.createTable();
        db.uInsertRow("testinsert", "test bio");
        db.mInsertRow("subject: insert", "this is a test insert", 1, "attahcment test", "link");
        db.cInsertRow("test comment. this is testing", 1, 1);
        db.vInsertRow(1, 1, 1);

        // test execution
        // assertEquals(3, db.uSelectOne(1).numElements); Be sure to test this with
        // uncommented drop table and creation table. I comment the table creation and
        // drop table so it won't work now
        // assertEquals(5, db.mSelectOne(1).numElements);
        assertEquals(1, db.cSelectOne(1).size());
        assertEquals(1, db.vSelectOne(1).size());
        // assertEquals(3, db.vSelectOne(1).numElements);

        // test cleanup
        db.dropTable();
    }

    public void testDeleteRow() {
        // test prep
        db.createTable();
        db.uInsertRow("test", "test bio");
        db.mInsertRow("test", "test message", 1, "attachment", "mlink");
        db.cInsertRow("test", 1, 1);
        db.vInsertRow(1, 1, 1);

        // test execution
        assertEquals(1, db.vDeleteRow(1));
        assertEquals(1, db.cDeleteRow(1));
        assertEquals(1, db.mDeleteRow(1));
        assertEquals(1, db.uDeleteRow(1));

        // test cleanup
        db.dropTable();
    }

    /* TESTS BELOW ARE NEW TO PHASE 4 */

    /**
     * Tests search title function. There is one entry that matches the user's
     * search keyword, so ids.size() should be 1
     */
    public void testSearchTitle() {
        db.createTable();
        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        ArrayList<Integer> ids = App.searchTitle("Subject", db);
        int length = ids.size();
        assertEquals(1, length);

        db.dropTable();
    }

    /**
     * Tests search title function. There are two entries that match the search
     * keyword, so ids.size() should be 2
     */
    public void testSearchTitle2() {
        db.createTable();
        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        db.uInsertRow("Name Again", "Test bio");
        db.mInsertRow("My Subject the Second", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment2", 2, 1);
        db.vInsertRow(1, 2, 1);

        ArrayList<Integer> ids = App.searchTitle("Subject", db);
        int length = ids.size();
        assertEquals(2, length);

        db.dropTable();
    }

    /**
     * Tests searchTitle() function. There are three entries, but only two match the
     * search keyword. ids.size() should be 3
     */
    public void testSearchTitle3() {
        db.createTable();
        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        db.uInsertRow("Name Again", "Test bio");
        db.mInsertRow("My Subject the Second", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment2", 2, 1);
        db.vInsertRow(1, 2, 1);

        db.uInsertRow("Name Again", "Test bio");
        db.mInsertRow("No Keywords", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment2", 3, 1);
        db.vInsertRow(1, 3, 1);

        ArrayList<Integer> ids = App.searchTitle("Subject", db);
        int length = ids.size();
        assertEquals(2, length);

        db.dropTable();
    }

    /**
     * Tests searchMsg() function to see if there is one entry that matches search
     * keyword
     */
    public void testSearchMsg() {
        db.createTable();
        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        ArrayList<Integer> ids = App.searchMsg("Message", db);
        int length = ids.size();
        assertEquals(1, length);

        db.dropTable();
    }

    /**
     * Tests searchMsg() function to see if there are two entries that match search
     * keyword
     */
    public void testSearchMsg2() {
        db.createTable();
        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject again", "My Message a second time", 1, "attachment", "link");
        db.cInsertRow("Test comment", 2, 1);
        db.vInsertRow(1, 2, 1);

        ArrayList<Integer> ids = App.searchMsg("Message", db);
        int length = ids.size();
        assertEquals(2, length);

        db.dropTable();
    }

    /**
     * Tests searchMsg() function to see if there are only two out of the three entries that match the search keyword
     * keyword
     */
    public void testSearchMsg3() {
        db.createTable();
        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject again", "My Message a second time", 1, "attachment", "link");
        db.cInsertRow("Test comment", 2, 1);
        db.vInsertRow(1, 2, 1);

        db.uInsertRow("Name Again", "Test bio");
        db.mInsertRow("No Keywords", "No keyword", 1, "attachment", "link");
        db.cInsertRow("Test comment2", 3, 1);
        db.vInsertRow(1, 3, 1);

        ArrayList<Integer> ids = App.searchMsg("Message", db);
        int length = ids.size();
        assertEquals(2, length);

        db.dropTable();
    }

    /**
     * Tests searchAuthor to see if there is a post for an author with name "Name"
     */
    public void testSearchAuthor() {
        db.createTable();
        db.uInsertRow("My Name", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        ArrayList<Integer> ids = App.searchUser("Name", db);
        int length = ids.size();
        assertEquals(1, length);

        db.dropTable();
    }

    /**
     * Tests to see if there are two authors with Bob in their name - Bob and Bobby. ids.size() should return 2
     */
    public void testSearchAuthor2() {
        db.createTable();
        db.uInsertRow("Bob", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        db.uInsertRow("Bobby", "Test bio");
        db.mInsertRow("My Subject", "My Message", 2, "attachment", "link");
        db.cInsertRow("Test comment", 2, 1);
        db.vInsertRow(1, 2, 1);

        ArrayList<Integer> ids = App.searchUser("Bob", db);
        int length = ids.size();
        assertEquals(2, length);

        db.dropTable();
    }

    /**
     * Tests to ensure that searchAuthor() is not accounting for authors without keyword in name
     */
    public void testSearchAuthor3() {
        db.createTable();
        db.uInsertRow("Bob", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        db.uInsertRow("Bobby", "Test bio");
        db.mInsertRow("My Subject", "My Message", 2, "attachment", "link");
        db.cInsertRow("Test comment", 2, 1);
        db.vInsertRow(1, 2, 1);

        db.uInsertRow("Jim", "Test bio");
        db.mInsertRow("My Subject", "My Message", 3, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 3, 1);

        ArrayList<Integer> ids = App.searchUser("Bob", db);
        int length = ids.size();
        assertEquals(2, length);

        db.dropTable();
    }

    /**
     * Tests searchAll() function to find all posts with keyword in any part of them. Bob is found in author, title, msg of 3 
     * different posts so ids.size() should be 3
     */
    public void testSearchAll(){
        db.createTable();
        db.uInsertRow("Bob", "Test bio");
        db.mInsertRow("My Subject", "My Message", 1, "attachment", "link");
        db.cInsertRow("Test comment", 1, 1);
        db.vInsertRow(1, 1, 1);

        db.uInsertRow("Jim", "Test bio");
        db.mInsertRow("My Subject is Bob", "My Message", 2, "attachment", "link");
        db.cInsertRow("Test comment", 2, 1);
        db.vInsertRow(1, 2, 1);
        
        db.uInsertRow("Jim", "Test bio");
        db.mInsertRow("My Subject", "My Message is Bobby", 3, "attachment", "link");
        db.cInsertRow("Test comment", 3, 1);
        db.vInsertRow(1, 3, 1);

        ArrayList<Integer> ids = App.searchAll("Bob", db);
        int length = ids.size();
        assertEquals(3, length);

        db.dropTable();
    }

    public void testDisconnect() {
        assertEquals(true, db.disconnect());
    }

}
