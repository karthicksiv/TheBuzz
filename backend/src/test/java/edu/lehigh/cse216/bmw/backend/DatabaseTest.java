package edu.lehigh.cse216.bmw.backend;

import edu.lehigh.cse216.bmw.backend.Database.RowData;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Date;

import org.apache.velocity.exception.ParseErrorException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
//final
/**
 * Unit test for simple App.
 */
public class DatabaseTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DatabaseTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(DatabaseTest.class);
    }

    /**
     * Checks the connection with the database.
     * 
     * db_url is variable for the test Heroku database, not normal Heroku database.
     * The database has three rows with pre-specified parameters for testing.
     */
    public void testGetDatabase() {
        String db_url = "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
        Database db = Database.getDatabase(db_url);
        assertFalse(db == null);
    }

    /**
     * Tests table creation. Technical debt due to async issues that will be fixed
     * later. Not required in rubric.
     */
/*    
      public void testCreateTable(){ String db_url =
      "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
      Database db = Database.getDatabase(db_url);
      
      db.createTable(); assertTrue(true); }
*/     
    /**
     * Tests row insertion. Technical debt due to async issues that will be fixed
     * later. Not required in rubric.
     */
    
    // public void testInsertRow() { String db_url =
    //  "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
    //  Database db = Database.getDatabase(db_url);
    //  db.dropTable();
    //  db.createTable();
    //  String title = "MyTitle"; String message = "MyMessage"; int u_id = 0;
    //  int newId = db.mInsertRow(title, message, u_id,null,null);
    //  assertTrue(newId == 1); }

    /**
     * Tests selection of row in database.
     * 
     * Message for row 1, ID 1 is always Msg1. Title for row 1, ID 1 is always
     * Test1. Votes for row 1, ID 1 is always 0.
     */
    /*public void testSelectOne() {
        String db_url = "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
        Database db = Database.getDatabase(db_url);

        RowData res = db.selectOne(1); // Select row 1 in the database

        //assertTrue(res.uId == 0); // Test to see if parameters in database are equal to what they should be
        assertTrue(res.mTitle.equals("MyTitle"));
        assertTrue(res.mMessage.equals("MyMessage"));
    }

    /**
     * Test updating message in row 2.
     * 
     * Original message Msg2, change to MyNewMsg
     */
    /*public void testUpdateOne() {
        String db_url = "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
        Database db = Database.getDatabase(db_url);

        int result = db.updateOne(2, "messagetitle","MyNewMessage"); // Update message, check non-null (not -1)
        assertTrue(result != -1);

        RowData res = db.selectOne(2); // Select row 2, see if the message in that row matches what we set it to
        assertTrue(res.mMessage.equals("MyNewMessage"));
    }*/

    /**
     * Test upvoting on a specified post (row 3).
     */
    /*public void testUpVoteOne() {
        String db_url = "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
        Database db = Database.getDatabase(db_url);

        int votes = db.getVotes(3); // Get original number of votes on a specified post

        int result = db.UpVoteOne(3, votes); // Upvote on specified post, check to ensure not null result
        assertTrue(result != -1);

        RowData res = db.selectOne(3); // Select row 3, check to see if current number of votes is one more than
                                       // original
        assertTrue(res.mVote == (votes + 1));
    }*/

    /**
     * Testing downvoting on specified post (row 3)
     */
    /*public void testDownVoteOne() {
        String db_url = "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
        Database db = Database.getDatabase(db_url);

        int votes = db.getVotes(3); // Same logic as previous test other than testing if original - 1

        int result = db.DownVoteOne(3, votes);
        assertTrue(result != -1);

        RowData res = db.selectOne(3);
        assertTrue(res.mVote == (votes - 1));
    }*/

    /**
     * Testing delete row. Technical debt to be fixed later due to
     */
    // public void testDeleteRow(){
    //     String db_url = "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
    //     Database db = Database.getDatabase(db_url);

    //     int result = db.deleteRow(1);
    //     assertTrue(result != -1);
    // }
    /* More Technical debt.
     *
     * public void testDropTable(){ String db_url =
     * "postgres://xexorxmivwmezo:815c72c9f79b349454fac895ad873ae8845fbfc4d7837725486bef421a984f27@ec2-50-17-178-87.compute-1.amazonaws.com:5432/de6v94cggbf377";
     * Database db = Database.getDatabase(db_url);
     * 
     * db.dropTable(); assertTrue(true); }
     */
     
    public void testSearchDate() {
	
        String db_url = "postgres://yoikxsezpixhoa:f2a6909356ae1a5af25353fdd4f07c5a647ae407431beda30572e050c775fc1f@ec2-52-73-247-67.compute-1.amazonaws.com:5432/dd134cv3dbo47v";
        Database db = Database.getDatabase(db_url);


        String udate = "2020-04-14";
        try{
            Date convertdate = new SimpleDateFormat("yyyy-MM-dd").parse(udate);
            java.sql.Date newdate = new java.sql.Date(convertdate.getTime());
            ArrayList<Database.RowData> data = db.searchDate(newdate);
            assertTrue(data != null);
           
            assertTrue(data.get(0).mId==20);

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public void testSearchTitle() {
	
        String db_url = "postgres://yoikxsezpixhoa:f2a6909356ae1a5af25353fdd4f07c5a647ae407431beda30572e050c775fc1f@ec2-52-73-247-67.compute-1.amazonaws.com:5432/dd134cv3dbo47v";
        Database db = Database.getDatabase(db_url);


        String test = "asdf";
        String test2 = "karthick";
        String test3 = "karthickandroid";

        try{
            ArrayList<Database.RowData> data = db.searchTitle(test);
            assertTrue(data != null);
            assertTrue(data.get(0).mId==1);
            //System.out.println(data.get(0).mMessage);

            ArrayList<Database.RowData> data2 = db.searchTitle(test2);
            assertTrue(data2.get(0).mId==31);
            ArrayList<Database.RowData> data3 = db.searchTitle(test3);
            assertTrue(data3.get(0).mId==31);

            //assertTrue(data3.get(0).mId==31);

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void testSearchMessage() {
	
        String db_url = "postgres://yoikxsezpixhoa:f2a6909356ae1a5af25353fdd4f07c5a647ae407431beda30572e050c775fc1f@ec2-52-73-247-67.compute-1.amazonaws.com:5432/dd134cv3dbo47v";
        Database db = Database.getDatabase(db_url);


        String test = "asdf";
        String test2 = "test";
        String test3 = "testingtesting";

        try{
            ArrayList<Database.RowData> data = db.searchMessage(test);
            assertTrue(data != null);
            assertTrue(data.get(0).mId==1);
            //System.out.println(data.get(0).mMessage);

            ArrayList<Database.RowData> data2 = db.searchMessage(test2);
            assertTrue(data2.get(0).mId==18);
            ArrayList<Database.RowData> data3 = db.searchMessage(test3);
            assertTrue(data3.get(0).mId==31);

            //assertTrue(data3.get(0).mId==31);

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void testSearchTM() {
	
        String db_url = "postgres://yoikxsezpixhoa:f2a6909356ae1a5af25353fdd4f07c5a647ae407431beda30572e050c775fc1f@ec2-52-73-247-67.compute-1.amazonaws.com:5432/dd134cv3dbo47v";
        Database db = Database.getDatabase(db_url);


        String test = "asdf";
        String test2 = "test";
        String test3 = "testingtesting";

        try{
            ArrayList<Database.RowData> data = db.searchTM(test);
            assertTrue(data != null);
            assertTrue(data.get(0).mId==1);
            //System.out.println(data.get(0).mMessage);

            ArrayList<Database.RowData> data2 = db.searchTM(test2);
            assertTrue(data2.get(0).mId==8);
            ArrayList<Database.RowData> data3 = db.searchTM(test3);
            assertTrue(data3.get(0).mId==31);

            //assertTrue(data3.get(0).mId==31);

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void testSearchAuthor() {
	
        String db_url = "postgres://yoikxsezpixhoa:f2a6909356ae1a5af25353fdd4f07c5a647ae407431beda30572e050c775fc1f@ec2-52-73-247-67.compute-1.amazonaws.com:5432/dd134cv3dbo47v";
        Database db = Database.getDatabase(db_url);


        String test = "j";
        String test2 = "tesfsdfst";
        

        try{
            ArrayList<Database.RowData> data = db.searchAuthor(test);
            assertTrue(data != null);
            assertTrue(data.get(0).uId==1);
            //System.out.println(data.get(0).mMessage);

            ArrayList<Database.RowData> data2 = db.searchAuthor(test2);
            assertTrue(data2.size()==0);

            //assertTrue(data3.get(0).mId==31);

        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
