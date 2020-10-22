package edu.lehigh.cse216.bmw.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.Calendar;
import java.util.ArrayList;

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

    private PreparedStatement uSelectAll;

    private PreparedStatement cSelectAll;

    private PreparedStatement vSelectAll;

    /**
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    private PreparedStatement uSelectOne;

    private PreparedStatement cSelectOne;

    private PreparedStatement vSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;
    
    private PreparedStatement uDeleteOne;
    
    private PreparedStatement cDeleteOne;

    private PreparedStatement vDeleteOne;

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

    private PreparedStatement uUpdateOne;

    private PreparedStatement cUpdateOne;

    private PreparedStatement vUpdateOne;
    
    /**
     * Prepared statement for voting
     */
    private PreparedStatement mUpVoteOne;
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

    /**
     * prepared statements for checking the existance of our tables
     * for testing purposes
     */
    private PreparedStatement uTableExists;
    private PreparedStatement mTableExists;
    private PreparedStatement cTableExists;
    private PreparedStatement vTableExists;


    /**
     * prepared statements for managing the content of the database for admin
     */
    private PreparedStatement mDeleteByDate;

    //displays the documents and the user that owns the document
    private PreparedStatement mDisplayAttachment;

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

        String attachment; // attachment link

        String mLink; //link that the user assigns in the post 

        // user id
        int uId;

        // user name
        String uName;

        // user bio
        String uBio;

        // comment id
        int cId;

        // comment content
        String cMessage;       
        
        // vote
        int vote;

        //totalVotes
        int totalVotes;

        //variable created and set for testing purposes
        int numElements;

        Date date_created;
        

        /**
         * For the messages table
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int m_id, String subject, String message, int u_id, String attach, String link, int votes) {
            uId = u_id;
            mId = m_id;
            mTitle = subject;
            mMessage = message;
            attachment = attach;
            mLink = link;
            totalVotes = votes;
            numElements = 7;
        }
        public RowData(int m_id, String subject, String message, int u_id, String attach, String link,Date creationdate, int votes) {
            uId = u_id;
            mId = m_id;
            mTitle = subject;
            mMessage = message;
            attachment = attach;
            mLink = link;
            totalVotes = votes;
            date_created = creationdate;
            numElements = 7;
        }

        /**
         * For the users table
         */
        public RowData(int u_id, String u_name, String u_bio){
            uId = u_id;
            uName = u_name;
            uBio = u_bio;
            numElements = 3;
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
            numElements = 4;
        }
        // for select one
        public RowData(int c_id, String comment, int m_id, int u_id, String name){
            cId = c_id;
            cMessage = comment;
            mId = m_id;
            uId = u_id;
            uName = name;
            numElements = 5;
        }

        /**
         * For the votes table
         */
        public RowData(int m_id, int u_id, int vVote){
            uId = u_id;
            mId = m_id;
            vote = vVote;
            numElements = 3;
        }

        public RowData(int m_id, String name, String att, Date dat){
            mId = m_id;
            uName = name;
            attachment = att;
            date_created = dat;

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
            // u_id is a foreign key, if we add the ability to delete users it will delete their messages, comments, and votes
            db.mCreateTable = db.mConnection
                    .prepareStatement("CREATE TABLE messages (id SERIAL PRIMARY KEY, subject VARCHAR(50) "
                            + "NOT NULL, message VARCHAR(500) NOT NULL, attachment VARCHAR(100), mLink VARCHAR(100) , date_created DATE, u_id Integer REFERENCES users(id) ON DELETE CASCADE);");
                            
            db.mDropTable = db.mConnection.prepareStatement("DROP TABLE messages CASCADE");

            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM messages WHERE id = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO messages VALUES (DEFAULT, ?, ?, ?, ? ,?, ?)");
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
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE messages SET subject = ?,  message = ? , attachment = ?, mLink = ? WHERE id = ?");

            /**
             *  SQL for the users table
             */
            db.uCreateTable = db.mConnection.prepareStatement("CREATE TABLE users (id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL, bio VARCHAR(500) NOT NULL);");

            db.uDropTable = db.mConnection.prepareStatement("DROP TABLE users CASCADE");

            // Standard CRUD ops
            db.uDeleteOne = db.mConnection.prepareStatement("DELETE FROM users WHERE id = ?;");
            db.uInsertOne = db.mConnection.prepareStatement("INSERT INTO users VALUES (default, ?, ?);");
            db.uSelectAll = db.mConnection.prepareStatement("SELECT * FROM users");
            db.uSelectOne = db.mConnection.prepareStatement("SELECT * FROM users WHERE id = ?");
            db.uUpdateOne = db.mConnection.prepareStatement("UPDATE users SET name = ? WHERE id = ?");


            /**
             * SQL for comments table
             */
            db.cCreateTable = db.mConnection.prepareStatement("CREATE TABLE comments (id SERIAL PRIMARY KEY, comment VARCHAR(500) "
                                            + "NOT NULL, m_id INTEGER REFERENCES messages(id) ON DELETE CASCADE, u_id INTEGER REFERENCES users(id) ON DELETE CASCADE);");

            db.cDropTable = db.mConnection.prepareStatement("DROP TABLE comments CASCADE");

            /**
             * Standard CRUD ops
             * No update available since comments cannot be edited
             * Delete used because if the message gets deleted the comments should also get deleted
             */      
            db.cDeleteOne = db.mConnection.prepareStatement("DELETE FROM comments WHERE id = ?");
            db.cInsertOne = db.mConnection.prepareStatement("INSERT INTO comments VALUES (default, ?, ?, ?)");
            db.cSelectAll = db.mConnection.prepareStatement("SELECT * FROM comments");
            db.cSelectOne = db.mConnection.prepareStatement("SELECT c.comment, c.id, c.m_id, c.u_id, u.name "+
                                                            "FROM comments AS c "+
                                                            "LEFT JOIN users AS u ON u.id = c.u_id "+
                                                            "WHERE c.m_id = ?;");  
                                                          
            db.cUpdateOne = db.mConnection.prepareStatement("UPDATE comments SET comment = ? WHERE id = ?");

            /**
             * SQL for votes table
             */
            db.vCreateTable = db.mConnection.prepareStatement("CREATE TABLE votes (u_id INTEGER REFERENCES users(id), m_id INTEGER REFERENCES messages(id), "
                                            + "vote SMALLINT, PRIMARY KEY (u_id, m_id));");

            db.vDropTable = db.mConnection.prepareStatement("DROP TABLE votes CASCADE");
            
            // Standard CRUD ops
            db.vDeleteOne = db.mConnection.prepareStatement("DELETE FROM votes WHERE u_id = ?");
            db.vInsertOne = db.mConnection.prepareStatement("INSERT INTO votes VALUES (?, ?, ?)");
            db.vSelectAll = db.mConnection.prepareStatement("SELECT * FROM votes GROUP BY m_id, u_id ORDER BY m_id;");
            db.vSelectOne = db.mConnection.prepareStatement("SELECT * FROM votes WHERE m_id = ? GROUP BY m_id, u_id ORDER BY u_id;");
            db.vUpdateOne = db.mConnection.prepareStatement("UPDATE votes SET vote = ? WHERE m_id = ? AND u_id = ?");

            //this is for deletion in the message table pertaining to the content management
            db.mDeleteByDate = db.mConnection.prepareStatement("DELETE FROM messages WHERE date_created < ?");

            db.mDisplayAttachment = db.mConnection.prepareStatement("SELECT " +
                                                                "m.id, "+
                                                                "name," +
                                                                "m.attachment, "+
                                                                "m.date_created "+
                                                            "FROM "+
                                                                "messages as m "+
                                                            "INNER JOIN "+
                                                                "users ON m.u_id = users.id "+
                                                            "ORDER BY m.date_created");

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

    int uInsertRow(String name, String bio) {
        int count = 0;
        try {
            uInsertOne.setString(1, name);
            uInsertOne.setString(2, bio);
            count += uInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    int cInsertRow(String comment, int m_id, int u_id) {
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

    int vInsertRow(int u_id, int m_id, int vote){
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

    /**
     * Query the database for a list of all subjects and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> mSelectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"),rs.getString("attachment"),rs.getString("mLink"), rs.getDate("date_created"),rs.getInt("tot_votes")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //displays attachments or in this case, documents
    ArrayList<RowData> mDisplayAttachment() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mDisplayAttachment.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("name"), rs.getString("attachment"), rs.getDate("date_created")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Users select all
    ArrayList<RowData> uSelectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = uSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("name"), rs.getString("bio")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Comments select all
    ArrayList<RowData> cSelectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = cSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("comment"), rs.getInt("m_id"), rs.getInt("u_id")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Votes select all
    ArrayList<RowData> vSelectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = vSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("m_id"), rs.getInt("u_id"), rs.getInt("vote")));
            }
            rs.close();
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
    // users select one
    RowData uSelectOne(int id) {
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

    // messages select one
    RowData mSelectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("u_id"), rs.getString("attachment"),rs.getString("mLink"),rs.getDate("date_created"), rs.getInt("tot_votes"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    // comments select one
    // should return all comments from a single message
    ArrayList<RowData> cSelectOne(int id){
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            cSelectOne.setInt(1, id);
            ResultSet rs = cSelectOne.executeQuery();
            while(rs.next()) {
                res.add(new RowData(rs.getInt("id"), rs.getString("comment"), rs.getInt("m_id"), rs.getInt("u_id"), rs.getString("name")));
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // RowData cSelectOne(int id) {
    //     RowData res = null;
    //     try {
    //         cSelectOne.setInt(1, id);
    //         ResultSet rs = cSelectOne.executeQuery();
    //         if (rs.next()) {
    //             res = new RowData(rs.getInt("id"), rs.getString("comment"), rs.getInt("m_id"), rs.getInt("u_id"), rs.getString("name"));
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return res;
    // }

    // votes select one
    ArrayList<RowData> vSelectOne(int id){
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
    // RowData vSelectOne(int id) {
    //     RowData res = null;
    //     try {
    //         vSelectOne.setInt(1, id);
    //         ResultSet rs = vSelectOne.executeQuery();
    //         if (rs.next()) {
    //             res = new RowData(rs.getInt("m_id"), rs.getInt("u_id"), rs.getInt("vote"));
    //         }
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    //     return res;
    // }
    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted. -1 indicates an error.
     */
    int mDeleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int uDeleteRow(int id) {
        int res = -1;
        try {
            uDeleteOne.setInt(1, id);
            res = uDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int cDeleteRow(int id) {
        int res = -1;
        try {
            cDeleteOne.setInt(1, id);
            res = cDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    int vDeleteRow(int id) {
        int res = -1;
        try {
            vDeleteOne.setInt(1, id);
            res = vDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //content management interface:
    int mDeleteDate (Date d) {
        int res = -1;
        try {
            mDeleteByDate.setDate(1, d);
            res = mDeleteByDate.executeUpdate();
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
    int mUpdateOne(int id, String subject, String message, String attachment, String link) {
        int res = -1;
        try {
            mUpdateOne.setInt(5, id);
            mUpdateOne.setString(1, subject);
            mUpdateOne.setString(2, message);
            mUpdateOne.setString(3, attachment);
            mUpdateOne.setString(4, link);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    // update single User.  replace existing name with name provided for user id provided.
    int uUpdateOne(String name, int id) {
        int res = -1;
        try {
            uUpdateOne.setString(1, name);
            uUpdateOne.setInt(2, id);
            res = uUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    // update single comment from c_id.  Do not update message id or user id.
    int cUpdateOne(int id, String comment) {
        int res = -1;
        try {
            cUpdateOne.setInt(2, id);
            cUpdateOne.setString(1, comment);
            res = cUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    // update vote for m_id and u_id provided to new value provided
    int vUpdateOne(int u_id, int m_id, int vote) {
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
    * Add one to the vote total for a row in the database
    *
    * @param id The id of the row to update
    * @param vote Current number of votes. Add one for upvote, subtract one for downvote
    *
    * @return The number of rows that were updated. -1 indicates an error.
    */
    int UpVoteOne(int id, int vote){
        int res = -1;
        vote += 1;
        try{
        mUpVoteOne.setInt(1, vote);
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
    * @param id The id of the row to update
    * @param vote Current number of votes. Add one for upvote, subtract one for downvote
    *
    * @return The number of rows that were updated. -1 indicates an error.
    */
    int DownVoteOne(int id, int vote){
        int res = -1;
        vote -= 1;
        try{
        mDownVoteOne.setInt(1, vote);
        mDownVoteOne.setInt(2, id);
        res = mDownVoteOne.executeUpdate();
        } catch (SQLException e){
        e.printStackTrace();
        }
        return res;
    }

    /**
     * Create all the tables. If they already exist, this will print an error
     */
    void createTable() {
        try {
            uCreateTable.execute();
            mCreateTable.execute();
            cCreateTable.execute();
            vCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // void tableExists() {
    //     try {
    //         uTableExists.execute();
    //         mTableExists.execute();
    //         cTableExists.execute();
    //         vTableExists.execute();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * Remove tables from the database. If it does not exist, this will print an
     * error.
     * 
     * We remove all the tables because we created them all together.
     */
    void dropTable() {
        try {
            vDropTable.execute();
            cDropTable.execute();
            mDropTable.execute();
            uDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void uDropTable() {
        try {
            uDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void mDropTable() {
        try {
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void cDropTable() {
        try {
            cDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void vDropTable() {
        try {
            vDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
