package edu.lehigh.cse216.bmw.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.ArrayList;

//import com.sun.org.apache.regexp.internal.RE;

public class Database {
    /**
     * The connection to the database. When there is no connection, it should be
     * null. Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    private PreparedStatement cSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    private PreparedStatement uSelectOne;

    private PreparedStatement cSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;

    private PreparedStatement uInsertOne;

    private PreparedStatement cInsertOne;

    private PreparedStatement vInsertOne;

    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    private PreparedStatement cUpdateOne;

    private PreparedStatement vUpdateOne;

    /**
     * A prepared statement for getting the number of votes in a row
     */
    private PreparedStatement mgetVotes; 

    /**
     * A prepared statement for upvoting on a message in the database
     */
    private PreparedStatement mUpVoteOne;

    /**
     * A prepared statement for downvoting on a message in the database
     */
    private PreparedStatement mDownVoteOne;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateTable;

    private PreparedStatement uCreateTable;

    private PreparedStatement cCreateTable;

    private PreparedStatement vCreateTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropTable;

    private PreparedStatement uDropTable;

    private PreparedStatement cDropTable;

    private PreparedStatement vDropTable;

    private PreparedStatement searchTitle;

    private PreparedStatement searchMessage;

    private PreparedStatement searchAuthor;

    private PreparedStatement searchDate;
    private PreparedStatement searchTM;

    /**
     * RowData is like a struct in C: we use it to hold data, and we allow direct
     * access to its fields. In the context of this Database, RowData represents the
     * data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want to
     * encourage users to think of RowData as being anything other than an abstract
     * representation of a row of the database. RowData and the Database are tightly
     * coupled: if one changes, the other should too.
     */
    public static class RowData {
        /**
         * message id
         */
        int mId;
        /**
         * message title
         */
        String mTitle;
        
        /**
         * message content
         */
        String mMessage;
        
        String attachment;
        String mLink;

        // user id
        int uId;

        
        // user bio
        String uBio;

        // user name
        String uName;

        // comment id
        int cId;

        // comment content
        String cMessage;       
        
        // vote
        int vote;

        //totalVotes
        int mVote;

        Date date_created;

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int m_id, String subject, String message, int u_id, String attach, String link, int votes) {
            uId = u_id;
            mId = m_id;
            mTitle = subject;
            mMessage = message;
            attachment = attach;
            mLink = link;
            mVote = votes;
        }

        public RowData(int m_id, String subject, String message, int u_id, String attach, String link, Date creationdate, int votes) {
            uId = u_id;
            mId = m_id;
            mTitle = subject;
            mMessage = message;
            attachment = attach;
            mLink = link;
            mVote = votes;
            date_created = creationdate;
        }


        public RowData(int m_id, String subject, String message, int u_id, int votes) {
            uId = u_id;
            mId = m_id;
            mTitle = subject;
            mMessage = message;
            mVote = votes;
        }

        /**
         * For the users table
         */
        public RowData(int u_id, String u_name, String u_bio){
            uId = u_id;
            uName = u_name;
            uBio = u_bio;
        }

        /**
         * For the comments table
         */
        // for select all
        public RowData(int c_id, String comment, int m_id, int u_id){
            cId = c_id;
            cMessage = comment;
            mId = m_id;
            uId = u_id;
        }
        // for select one
        public RowData(int c_id, String comment, int m_id, int u_id, String name){
            cId = c_id;
            cMessage = comment;
            mId = m_id;
            uId = u_id;
            uName = name;
        }

        /**
         * For the votes table
         */
        public RowData(int m_id, int u_id, int vVote){
            uId = u_id;
            mId = m_id;
            vote = vVote;
        }
    }

    /**
     * The Database constructor is private: we only create Database objects through
     * the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }

        // Attempt to create all of our prepared statements. If any of these
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            // SQL incorrectly. We really should have things like "tblData"
            // as constants, and then build the strings for the statements
            // from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table
            // creation/deletion, so multiple executions will cause an exception
            db.mCreateTable = db.mConnection
                    .prepareStatement("CREATE TABLE messages (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
                            + "NOT NULL, message VARCHAR(500) NOT NULL, attachment VARCHAR(100), mLink VARCHAR(100) , date_created DATE, u_id Integer REFERENCES users(id) ON DELETE CASCADE);");
                            
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE messages CASCADE");

            db.uCreateTable = db.mConnection.prepareStatement("CREATE TABLE users (id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, bio VARCHAR(500) NOT NULL);");

            db.uDropTable = db.mConnection.prepareStatement("DROP TABLE users CASCADE");
            /**
             * SQL for comments table
             */
            db.cCreateTable = db.mConnection.prepareStatement("CREATE TABLE comments (id SERIAL PRIMARY KEY, comment VARCHAR(500) "
                                            + "NOT NULL, m_id INTEGER REFERENCES messages(id) ON DELETE CASCADE, u_id INTEGER REFERENCES users(id) ON DELETE CASCADE);");

            db.cDropTable = db.mConnection.prepareStatement("DROP TABLE comments CASCADE");

            /**
             * SQL for votes table
             */
            db.vCreateTable = db.mConnection.prepareStatement("CREATE TABLE votes (u_id INTEGER REFERENCES users(id), m_id INTEGER REFERENCES messages(id), "
                                            + "vote SMALLINT, PRIMARY KEY (u_id, m_id));");

            db.vDropTable = db.mConnection.prepareStatement("DROP TABLE votes CASCADE");

            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM messages WHERE id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO messages VALUES (DEFAULT, ?, ?, ?, ? ,?, ?)");
            // db.mSelectAll = db.mConnection.prepareStatement("SELECT * FROM messages");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT " +
                                                                "m.id, "+
                                                                "m.subject, "+
                                                                "m.message, "+
                                                                "m.u_id, "+
                                                                "m.attachment, "+
                                                                "m.mLink, "+
                                                                "m.date_created, "+
                                                                "SUM (v.vote) as tot_votes "+
                                                            "FROM "+
                                                                "messages as m "+
                                                            "LEFT JOIN "+
                                                                "votes as v on v.m_id = m.id "+
                                                            "GROUP BY m.id");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT " +
                                                                "m.id, "+
                                                                "m.subject, "+
                                                                "m.message, "+
                                                                "m.u_id, "+
                                                                "m.attachment, "+
                                                                "m.mLink, "+
                                                                "m.date_created, "+
                                                                "SUM (v.vote) as tot_votes "+
                                                            "FROM "+
                                                                "messages as m "+
                                                            "LEFT JOIN "+
                                                                "votes as v on v.m_id = m.id "+
                                                            "WHERE m.id = ? "+
                                                            "GROUP BY m.id");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE messages SET subject = ?,  message = ? WHERE id = ?");
            db.mUpVoteOne = db.mConnection.prepareStatement("UPDATE messages SET vote = ? WHERE id = ?");     //added prepared statements for up/downvoting
            db.mDownVoteOne = db.mConnection.prepareStatement("UPDATE messages SET vote = ? WHERE id = ?");
            db.uInsertOne = db.mConnection.prepareStatement("INSERT INTO users VALUES (?, ?);");
            db.uSelectOne = db.mConnection.prepareStatement("SELECT * FROM users WHERE id = ?");
            db.cSelectAll = db.mConnection.prepareStatement("SELECT * FROM comments");
            db.cInsertOne = db.mConnection.prepareStatement("INSERT INTO comments VALUES (default, ?, ?, ?)");
            db.cUpdateOne = db.mConnection.prepareStatement("UPDATE comments SET comment = ? WHERE id = ? AND u_id = ?");
            db.vUpdateOne = db.mConnection.prepareStatement("UPDATE votes SET vote = ? WHERE m_id = ? AND u_id = ?");
            db.vInsertOne = db.mConnection.prepareStatement("INSERT INTO votes VALUES (?, ?, ?)");

            db.searchTitle = db.mConnection.prepareStatement("SELECT " +
                                                            "m.id, "+
                                                            "m.subject, "+
                                                            "m.message, "+
                                                            "m.u_id, "+
                                                            "m.attachment, "+
                                                            "m.mLink, "+
                                                            "m.date_created, "+
                                                            "SUM (v.vote) as tot_votes "+
                                                        "FROM "+
                                                            "messages as m "+
                                                        "LEFT JOIN "+
                                                            "votes as v on v.m_id = m.id "+
                                                            "WHERE REPLACE(LOWER(m.subject),' ','') LIKE ? " +
                                                        "GROUP BY m.id");

            db.searchMessage = db.mConnection.prepareStatement("SELECT " +
                                                            "m.id, "+
                                                            "m.subject, "+
                                                            "m.message, "+
                                                            "m.u_id, "+
                                                            "m.attachment, "+
                                                            "m.mLink, "+
                                                            "m.date_created, "+
                                                            "SUM (v.vote) as tot_votes "+
                                                        "FROM "+
                                                            "messages as m "+
                                                        "LEFT JOIN "+
                                                            "votes as v on v.m_id = m.id "+
                                                            "WHERE REPLACE(LOWER(m.message),' ','') LIKE ? " +
                                                        "GROUP BY m.id");

            db.searchAuthor = db.mConnection.prepareStatement("SELECT * FROM users WHERE name = ?");
            
            db.searchDate = db.mConnection.prepareStatement("SELECT " +
                                                            "m.id, "+
                                                            "m.subject, "+
                                                            "m.message, "+
                                                            "m.u_id, "+
                                                            "m.attachment, "+
                                                            "m.mLink, "+
                                                            "m.date_created, "+
                                                            "SUM (v.vote) as tot_votes "+
                                                        "FROM "+
                                                            "messages as m "+
                                                        "LEFT JOIN "+
                                                            "votes as v on v.m_id = m.id "+
                                                        "WHERE date_created = ? " +
                                                        "GROUP BY m.id");

            db.searchTM = db.mConnection.prepareStatement("SELECT " +
                "m.id, "+
                "m.subject, "+
                "m.message, "+
                "m.u_id, "+
                "m.attachment, "+
                "m.mLink, "+
                "m.date_created, "+
                "SUM (v.vote) as tot_votes "+
            "FROM "+
                "messages as m "+
            "LEFT JOIN "+
                "votes as v on v.m_id = m.id "+
                "WHERE ((REPLACE(LOWER(m.subject),' ','') LIKE ?) " +
                "OR (REPLACE(LOWER(m.message),' ','') LIKE ?)) " +
            "GROUP BY m.id");


        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an error
     * occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }

    /**
     * Insert a row into the database
     * 
     * @param subject The subject for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    
    int mInsertRow(String subject, String message, int u_id, String attachment, String mLink) {
        int count = 0;
        try {
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            mInsertOne.setString(3, attachment);
            mInsertOne.setString(4, mLink);
            
			java.util.Date date=new java.util.Date();
			java.sql.Date sqlDate=new java.sql.Date(date.getTime());
			//java.sql.Timestamp sqlTime=new java.sql.Timestamp(date.getTime());
			
            mInsertOne.setDate(5, sqlDate);
            mInsertOne.setInt(6, u_id);
	        
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int addUser(int u_id, String u_name){
        int res = -1;
        try {
            uInsertOne.setInt(1, u_id);
            uInsertOne.setString(2, u_name);
            res = u_id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int CInsertRow(String comment, int m_id, int u_id) {
        int count = 0;
        try {
            cInsertOne.setString(1, comment);
            cInsertOne.setInt(2, m_id);
            cInsertOne.setInt(3, u_id);
            count += cInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int VInsertRow(int u_id, int m_id, int vote){
        int count = 0;
        try {
            vInsertOne.setInt(1, u_id);
            vInsertOne.setInt(2, m_id);
            vInsertOne.setInt(3, vote);
            count += vInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    ArrayList<RowData> CSelectAll(int m_id) {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = cSelectAll.executeQuery();
            while (rs.next()) {
                int ID = rs.getInt("m_id");
                if (ID == m_id){
                int idx = rs.getInt("u_id");
                RowData data = USelectOne(idx);
                res.add(data);
                //res.add(new RowData(rs.getInt("id"), rs.getString("comment"), rs.getInt("m_id"), rs.getInt("u_id"), rs.getString("name")));
                res.add(new RowData(rs.getInt("id"), rs.getString("comment"), rs.getInt("m_id"), rs.getInt("u_id")));
                }
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Query the database for a list of all subjects and their IDs and the number of votes they have
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"), rs.getString("attachment"), rs.getString("mLink"), rs.getDate("date_created"), rs.getInt("tot_votes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 
     * Query the database for a list of all subjects and their IDs and the number of votes they have
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> UselectAll(int UID, RowData data) {
        ArrayList<RowData> res = new ArrayList<RowData>();
        //res.add(data);
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                int ID = rs.getInt("u_id");
                if (ID == UID){
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"), rs.getString("attachment"), rs.getString("mLink"), rs.getDate("date_created"), rs.getInt("tot_votes")));
            }
        }
            rs.close();
            res.add(data);
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"), rs.getString("attachment"), rs.getString("mLink"), rs.getDate("date_created"), rs.getInt("tot_votes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ArrayList<RowData> searchTitle(String title) {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            String mod = title.replaceAll("\\s", "");
            searchTitle.setString(1, "%" + mod + "%");

            ResultSet rs = searchTitle.executeQuery();
            
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"), rs.getString("attachment"), rs.getString("mLink"), rs.getDate("date_created"), rs.getInt("tot_votes")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ArrayList<RowData> searchMessage(String message) {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            //System.out.println(message);
            String mod=message.replaceAll("\\s", "");
           //System.out.println("mod: " + mod);
            searchMessage.setString(1,  "%" + mod + "%");

            ResultSet rs = searchMessage.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"), rs.getString("attachment"), rs.getString("mLink"), rs.getDate("date_created"), rs.getInt("tot_votes")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ArrayList<RowData> searchTM(String key) {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            String mod=key.replaceAll("\\s", "");
            searchTM.setString(1,  "%" + mod + "%");
            searchTM.setString(2,  "%" + mod + "%");
            //System.out.println(searchTM);
            ResultSet rs = searchTM.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"), rs.getString("attachment"), rs.getString("mLink"), rs.getDate("date_created"), rs.getInt("tot_votes")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ArrayList<RowData> searchAuthor(String name) {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            searchAuthor.setString(1, name);
            ResultSet rs = searchAuthor.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("name"), rs.getString("bio")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ArrayList<RowData> searchDate(Date date) {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            searchDate.setDate(1,date);
            ResultSet rs = searchDate.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"), rs.getString("attachment"), rs.getString("mLink"), rs.getDate("date_created"), rs.getInt("tot_votes")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }


    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    // Select one user's information
    RowData USelectOne(int id) {
        RowData res = null;
        try {
            uSelectOne.setInt(1, id);
            ResultSet rs = uSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), rs.getString("name"), rs.getString("bio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

/** 
    // votes select one
    ArrayList<RowData> vSelectOne(int id, int u_id){
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            vSelectOne.setInt(1, id);
            ResultSet rs = vSelectOne.executeQuery();
            while(rs.next()) {
                res.add(new RowData(rs.getInt("m_id"), rs.getInt("u_id"), rs.getInt("vote")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
*/

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id      The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int updateOne(int id, String subject, String message) {
        int res = -1;
        try {
            mUpdateOne.setInt(3, id);
            mUpdateOne.setString(1, subject);
            mUpdateOne.setString(2, message);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    // update single comment from c_id.  Do not update message id or user id.
    int CUpdateOne(int id, int u_id, String comment) {
        int res = -1;
        try {
            cUpdateOne.setInt(2, id);
            cUpdateOne.setInt(3, u_id);
            cUpdateOne.setString(1, comment);
            res = cUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    // update vote for m_id and u_id provided to new value provided
    int VUpdateOne(int u_id, int m_id, int vote) {
        int res = -1;
        try {
            vUpdateOne.setInt(2, m_id);
            vUpdateOne.setInt(3, u_id);
            vUpdateOne.setInt(1, vote);
            res = vUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Get current number of votes for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    int getVotes(int id) {
        int votes = 0;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                votes = rs.getInt("vote");      //save number of votes at specified ID into votes, return votes. Used by other methods
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return votes;
    }
    
    /**
     * Add one to the vote total for a row in the database
     * 
     * @param id      The id of the row to update
     * @param vote    Current number of votes. Add one for upvote
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int UpVoteOne(int id, int vote){
        int res = -1;
        vote += 1; //add one to current number of votes 
        try{
            mUpVoteOne.setInt(1, vote); //set new number of votes as the updated one
            mUpVoteOne.setInt(2, id);
            res = mUpVoteOne.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    } 

    /**
     * Subtract one from the vote total for a row in the database
     * 
     * @param id      The id of the row to update
     * @param vote    Current number of votes. Add one for upvote, subtract one for downvote
     * 
     * @return The number of rows that were updated. -1 indicates an error.
     */
    int DownVoteOne(int id, int vote){
        int res = -1;
        vote -= 1;  //subtract one from current number of votes
        try{
            mDownVoteOne.setInt(1, vote);
            mDownVoteOne.setInt(2, id);     //set new number of votes
            res = mDownVoteOne.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    } 

    /**
     * Create tblData. If it already exists, this will print an error (due to only having one table right now. technical debt for later)
     */
    void createTable() {
        try {
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove tblData from the database. If it does not exist, this will print an
     * error. Again, there is only one table right now so we may need to change this for later. 
     */
    void dropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
