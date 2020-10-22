package edu.lehigh.cse216.bmw.backend;

import edu.lehigh.cse216.bmw.backend.GoogleDriveUtils;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import spark.Spark;
import java.security.MessageDigest;

// Import Google's JSON library
import com.google.gson.*;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import java.util.*;
import java.util.concurrent.TimeoutException;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

// Memcache imports
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;
// End of memcache imports

//import for Google Drive API support
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    // Directory to store user credentials for this application.
    private static final java.io.File CREDENTIALS_FOLDER = new java.io.File(System.getProperty("user.home"), "credentials");

    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";

    //
    // Global instance of the scopes required by this quickstart. If modifying these
    // scopes, delete your previously saved credentials/ folder.
    //
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
 
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
 
        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);
 
        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME //
                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
        }
 
        // Load client secrets.
        InputStream in = new FileInputStream(clientSecretFilePath);
 
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JACKSON_FACTORY, new InputStreamReader(in));
 
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JACKSON_FACTORY,
                clientSecrets, SCOPES).setDataStoreFactory(new FileDataStoreFactory(CREDENTIALS_FOLDER))
                        .setAccessType("offline").build();
 
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }


    // Create a File on Drive
    // PRIVATE!
    private static File _createGoogleFile(String googleFolderIdParent, String contentType, //
            String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException {
 
        File fileMetadata = new File();
        fileMetadata.setName(customFileName);
 
        List<String> parents = Arrays.asList(googleFolderIdParent);
        fileMetadata.setParents(parents);
        //
        Drive driveService = GoogleDriveUtils.getDriveService();
 
        File file = driveService.files().create(fileMetadata, uploadStreamContent)
                .setFields("id, webContentLink, webViewLink, parents").execute();
 
        return file;
    }
 
    // Create Google File from byte[]
    public static File createGoogleFile(String googleFolderIdParent, String contentType, //
            String customFileName, byte[] uploadData) throws IOException {
        //
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }
 
    // Create Google File from java.io.File
    public static File createGoogleFile(String googleFolderIdParent, String contentType, //
            String customFileName, java.io.File uploadFile) throws IOException {
 
        //
        AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }
 
    // Create Google File from InputStream
    public static File createGoogleFile(String googleFolderIdParent, String contentType, //
            String customFileName, InputStream inputStream) throws IOException {
 
        //
        AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
        //
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }
    // End of methods to create a file on drive

    /**
     * Creates an authorized Credential object.
     * 
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

    /** Global instance of the HTTP transport. */
    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final JsonFactory JACKSON_FACTORY = new JacksonFactory();

    private static HashMap<String, String> session = new HashMap<>();

    private static final String CLIENT_ID = "433710337180-kb5utql63enl0kp57lmsfdvl1f97ciuj.apps.googleusercontent.com";

    /********************************MAIN METHOD******************************/
    public static void main(String[] args) {
        AuthInfo authInfo = AuthInfo.plain("DA9D72", "76674A6D7F0C418D606D693BB4720FBF");
        String memServer = "mc5.dev.ec2.memcachier.com:11211";
        List<InetSocketAddress> servers = AddrUtil.getAddresses(memServer);

        MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);

        // Configure SASL auth for each server
        for (InetSocketAddress server : servers) {
            builder.addAuthInfo(server, authInfo);
        }

        // Use binary protocol
        builder.setCommandFactory(new BinaryCommandFactory());
        // Connection timeout in milliseconds (default: )
        builder.setConnectTimeout(1000);
        // Reconnect to servers (default: true)
        builder.setEnableHealSession(true);
        // Delay until reconnect attempt in milliseconds (default: 2000)
        builder.setHealSessionInterval(2000);

        try {
            MemcachedClient mc = builder.build();
            try {
                mc.set("foo", 0, "bar");
                String val = mc.get("foo");
                // System.out.println(val);
            } catch (TimeoutException te) {
                te.printStackTrace();
                // System.err.println("Timeout during set or get: " + te.getMessage());
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                // System.err.println("Interrupt during set or get: " + ie.getMessage());
            } catch (MemcachedException me) {
                me.printStackTrace();
                // System.err.println("Memcached error during get or set: " + me.getMessage());
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            // System.err.println("Couldn't create a connection to MemCachier: " + ioe.getMessage());
        }

        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe. See
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        // Google Drive Stuff
        System.out.println("CREDENTIALS_FOLDER: " + CREDENTIALS_FOLDER.getAbsolutePath());
 
        // 1: Create CREDENTIALS_FOLDER
        // if (!CREDENTIALS_FOLDER.exists()) {
        //     CREDENTIALS_FOLDER.mkdirs();
 
        //     System.out.println("Created Folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
        //     System.out.println("Copy file " + CLIENT_SECRET_FILE_NAME + " into folder above.. and rerun this class!!");
        //     return;
        // }
 
        // 2: Build a new authorized API client service.
        try {
            final NetHttpTransport DRIVE_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            // 3: Read client_secret.json file & create Credential object.
            Credential credential = getCredentials(DRIVE_TRANSPORT);

            // 5: Create Google Drive Service.
            Drive service = new Drive.Builder(DRIVE_TRANSPORT, JACKSON_FACTORY, credential) //
                    .setApplicationName(APPLICATION_NAME).build();

            // Print the names and IDs for up to 10 files.
            FileList result = service.files().list().setPageSize(10).setFields("nextPageToken, files(id, name)")
                    .execute();
            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                // System.out.println("No files found.");
            } else {
                // System.out.println("Files:");
                for (File file : files) {
                    // System.out.printf("%s (%s)\n", file.getName(), file.getId());
                }
            }

            // Demonstrate that I can write to the Drive
            java.io.File godFile = new java.io.File("C:\\Users\\Ryan\\Desktop\\god_file.txt");
            FileWriter myWriter = new FileWriter(godFile.getName());
            myWriter.write("216 is the bane of my existance");
            myWriter.close();
            File googFile = createGoogleFile(null, "text/plain", godFile.getName(), godFile);
            System.out.println(googFile.getWebViewLink());
        } catch (GeneralSecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));

        // Connect to the database used in the admin tutorial
        // Get credentials through environment

        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");        //Hardcoded in BMW Database. Technical debt to be fixed later.
        db_url = "postgres://yoikxsezpixhoa:f2a6909356ae1a5af25353fdd4f07c5a647ae407431beda30572e050c775fc1f@ec2-52-73-247-67.compute-1.amazonaws.com:5432/dd134cv3dbo47v";
        
        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        // Set up the location for serving static files
        // Spark.staticFileLocation("/web");

        // Set up the location for serving static files. If the STATIC_LOCATION
        // environment variable is set, we will serve from it. Otherwise, serve
        // from "/web"
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        }

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        Spark.post("/auth", (req, res) -> {

            ////-----  Grant access only to domain ended with .edu ------///
            
            //if (!req.host().endsWith("edu")) {
            //    res.status(400);
            //    return "BAD";
           // }
            

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY)
                    // Specify the CLIENT_ID of the app that accesses the backend:
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    // Or, if multiple clients access the backend:
                    //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                    .build();


            OAuth auth =  gson.fromJson(req.body(), OAuth.class);
            System.out.println("User token: " + auth.token);
            try {
                GoogleIdToken idToken = verifier.verify(auth.token);

                if (idToken != null) {
                    Payload payload = idToken.getPayload();
                    // Print user identifier
                    String userId = payload.getSubject();
                    System.out.println("User ID: " + userId);
                    // Get profile information from payload
                    String email = payload.getEmail();
                    boolean emailVerified = payload.getEmailVerified();
                    String name = (String) payload.get("name");
                    String pictureUrl = (String) payload.get("picture");
                    String locale = (String) payload.get("locale");
                    String familyName = (String) payload.get("family_name");
                    String givenName = (String) payload.get("given_name");

                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update(userId.getBytes());
                    String encryptedString = Base64.getEncoder().encodeToString(messageDigest.digest());

//                    int userID = Integer.parseInt(userId);
                    session.put(userId, encryptedString);
                    res.status(200);
                    res.type("application/json");
                    SessionData data = new SessionData(encryptedString);
                    try {
                        MemcachedClient mcc = builder.build();
                        try {
                            mcc.set(userId, (24*60*60), encryptedString);
                            String val = mcc.get(userId);
                            // System.out.println(val);
                        } catch (TimeoutException te) {
                            te.printStackTrace();
                            // System.err.println("Timeout during set or get: " + te.getMessage());
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                            // System.err.println("Interrupt during set or get: " + ie.getMessage());
                        } catch (MemcachedException me) {
                            me.printStackTrace();
                            // System.err.println("Memcached error during get or set: " + me.getMessage());
                        }
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        // System.err.println("Couldn't create a connection to MemCachier: " + ioe.getMessage());
                    }
//                    int result= db.addUser(userID,name);
//                    if (result == -1) {
//                        return gson.toJson(new StructuredResponse("error", "unable to insert user " + name, null));
//                    } else {
                    return gson.toJson(new StructuredResponse("ok", "sign in success", data));
//                    }
                } else {
                    System.out.println("Invalid ID token.");
                    res.status(401);
                    return "Invalid ID token.";
                }
            } catch (Exception e) {
                System.out.println("Invalid ID token.");
                System.out.print(Arrays.toString(e.getStackTrace()));
                res.status(401);
                return "Invalid ID token.";
            }
        });

        Spark.get("/buzz", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAll()));
        });
 /**
        Spark.get("/buzz/:u_id/profile", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            String encryptedString = request.queryParams("token");
            String userId = session.get(encryptedString);
            int UID = Integer.parseInt(encryptedString);
            int idx = Integer.parseInt(request.params("u_id"));
            response.status(200);
            response.type("application/json");
            Database.RowData data = db.USelectOne(1);
            //ArrayList<Database.RowData> UData = db.UselectAll(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });
*/
        Spark.get("/buzz/:u_id/profile", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            String encryptedString = request.queryParams("token");
             String userId = session.get(encryptedString);
             int idx = Integer.parseInt(request.params("u_id"));
             response.status(200);
             response.type("application/json");
             Database.RowData data = db.USelectOne(idx);
             ArrayList<Database.RowData> UData = new ArrayList<Database.RowData>();
             UData.add(data);
             if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
             } else {
                return gson.toJson(new StructuredResponse("ok", null, db.UselectAll(idx,data)));
            }
        });
//dh

        // GET route that returns data for a specific row based on ID.
        // The ":id" suffix in the first parameter to get() becomes
        // request.params("id"), so that we can get the requested row ID. If
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error. Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        Spark.get("/buzz/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowData data = db.selectOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        Spark.get("/buzz/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowData data = db.selectOne(idx);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        Spark.get("/buzz/search_title/:subject", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            ArrayList<Database.RowData> data = db.searchTitle((request.params("subject")).toLowerCase());
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", "title not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        Spark.get("/buzz/search_message/:content", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            ArrayList<Database.RowData> data = db.searchMessage((request.params("content")).toLowerCase());
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", "Message not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        Spark.get("/buzz/search_author/:writer", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            ArrayList<Database.RowData> data = db.searchAuthor(request.params("writer"));
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", "User not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });
        
        Spark.get("/buzz/search_date/:date", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            String udate = request.params("date");
            Date convertdate = new SimpleDateFormat("yyyy-MM-dd").parse(udate);
            
            java.sql.Date newdate = new java.sql.Date(convertdate.getTime());
            ArrayList<Database.RowData> data = db.searchDate(newdate);
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", "date not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        Spark.get("/buzz/search_TM/:key", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            ArrayList<Database.RowData> data = db.searchTM((request.params("key")).toLowerCase());
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", "Message not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });
        
        // GET route that returns data for a specific row based on ID.
        // The ":id" suffix in the first parameter to get() becomes
        // request.params("id"), so that we can get the requested row ID. If
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error. Otherwise, we have an integer, and the only possible
        // error is that it doesn't correspond to a row with data.
        Spark.get("/buzz/:id/comment", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.CSelectAll(idx)));
        });

        // POST route for adding a new element to the database. This will read
        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and message, insert them, set the number of likes
        // to zero, and return the
        // ID of the newly created row.
        Spark.post("/buzz/:id/comment", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            int idx = Integer.parseInt(request.params("id"));
            //int UID = Integer.parseInt(session.get(req.encryptedString));
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.CInsertRow(req.cMessage, idx, 1); // zero here for initial likes 0
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // POST route for adding a new element to the database. This will read
        // JSON from the body of the request, turn it into a SimpleRequest
        // object, extract the title and message, insert them, set the number of likes
        // to zero, and return the
        // ID of the newly created row.
        Spark.post("/buzz", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            //int UID = Integer.parseInt(session.get(req.encryptedString));
            int UID = 1;
            //Upload a file to google drive
            // java.io.File uploadFile = req.mFile;
            // byte[] upFile = req.mAttach;
            // String fName = req.mFName;
            // Create Google File:
            String attachment = "";
            // byte[] array = {};
            try {
                // if (fName.contains("pdf")) {    
                java.io.File godFile = new java.io.File("C:\\Users\\Ryan\\Desktop\\god_file.txt");
                FileWriter myWriter = new FileWriter(godFile.getName());
                myWriter.write("216 is the bane of my existance");
                myWriter.close();

                // File googleFile = createGoogleFile(null, "application/pdf", "backend files", );
                File googleFile = createGoogleFile(null, "text/plain", godFile.getName(), godFile);
                attachment = googleFile.getWebViewLink();
                // } else if (fName.contains("jpg")) {
                //     File googleFile = createGoogleFile(null, "image/jpeg", fName, upFile);
                //     attachment = googleFile.getWebViewLink();
                // } else if (fName.contains("png")) {
                //     File googleFile = createGoogleFile(null, "image/png", fName, upFile);
                //     attachment = googleFile.getWebViewLink();
                // }
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            String link = req.mLink;
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            // describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.mInsertRow(req.mTitle, req.mMessage, UID, "this is for android", req.mLink); // zero here for initial likes 0
            //int voteTable = db.VInsertRow(UID,newId,0);
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });

        // PUT route for updating a row (with a message) in the Database. This is
        // almost
        // exactly the same as POST
        Spark.put("/buzz/:id/message", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int uResult = db.updateOne(idx, req.mTitle, req.mMessage);
            if (uResult == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, uResult));
            }
        });

        // PUT route for updating a row (with a message) in the Database. This is
        // almost
        // exactly the same as POST
        Spark.put("/buzz/:c_id/comment/edit", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("c_id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // int UID = Integer.parseInt(session.get(req.encryptedString));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int cResult = db.CUpdateOne(idx, 1, req.cMessage);
            if (cResult == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, cResult));
            }
        });

        // PUT route for updating a row (with an upvote) in the Database. This is almost
        // exactly the same as POST
        Spark.put("/buzz/:id/upvote", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            // int UID = Integer.parseInt(session.get(req.encryptedString));
            int UID = 1;
            //int vtCnt = Integer.parseInt(request.params("mVote"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int newId = db.VInsertRow(UID,idx,0);
            int VCheck = db.VUpdateOne(UID,idx,1);// upvote to be 1
            if (VCheck == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
           // int currentVotes = db.getVotes(idx); //get the current number of votes from getVotes method in the database
           // int result = db.UpVoteOne(idx, currentVotes); //add one to the current number of votes at the index. Pass in currentVotes
            //if (result == -1) {
                //return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            //} else {
                //return gson.toJson(new StructuredResponse("ok", null, result));
            //}
        });

        // PUT route for updating a row (with a downvote) in the Database. This is almost
        // exactly the same as POST
        Spark.put("/buzz/:id/downvote", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            int idx = Integer.parseInt(request.params("id"));
            //int UID = Integer.parseInt(session.get(req.encryptedString));
            int UID = 1;

            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int newId = db.VInsertRow(UID,idx,0);
            int VCheck = db.VUpdateOne(UID,idx,-1);
            if (VCheck == -1) {
            //    int newId = db.VInsertRow(UID,idx,-1);// zero here for initial likes 0
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
            //int currentVotes = db.getVotes(idx); //get the current number of votes from getVotes method in the database
            //int result = db.DownVoteOne(idx, currentVotes); //subtract one to the current number of votes at the index. Pass in currentVotes
            //if (result == -1) {
            //    return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            //} else {
            //    return gson.toJson(new StructuredResponse("ok", null, result));
            //}
        });

        // DELETE route for removing a row from the Database
        Spark.delete("/buzz/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the
            // message sent on a successful delete
            int dResult = db.deleteRow(idx);  //delete row at specified index
            if (dResult == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

    }

    /**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

}

class OAuth {
    public String token;
}

class SessionData {
    public String session;

    public SessionData(String session) {
        this.session = session;
    }
}
