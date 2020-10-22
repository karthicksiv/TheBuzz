package edu.lehigh.cse216.bmw.admin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
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

import java.io.FileInputStream;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Map;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.api.services.drive.model.About;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

    private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
    // Directory to store user credentials for this application.
    private static final java.io.File CREDENTIALS_FOLDER = new java.io.File(System.getProperty("user.home"),
            "credentials");

    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";

    private static final JsonFactory JACKSON_FACTORY = new JacksonFactory();
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

    /**
     * Print the menu for our program
     */
    static void initMenu() {
        System.out.println("Main Menu");
        System.out.println("  [?] Help (this message)");
        System.out.println("  [0] Quit Program");
        System.out.println("  [1] Content Management");
        System.out.println("  [2] Database/Table Management");
        System.out.println("  [3] Search Database");
        System.out.println();
    }

    static void adminMenu() {
        System.out.println("Content Management");
        System.out.println(" [?] Help (this message)");
        System.out.println(" [0] Back to Main Menu");

        System.out.println("\nBuzz Message Management");
        System.out.println(" [a] Display all messages");
        System.out.println(" [b] View Message by ID");
        System.out.println(" [c] Delete Message by ID");
        System.out.println(" [d] Delete Outdated Messages");

        System.out.println("\nBuzz Drive Management");
        System.out.println(" [1] Display all Attachments");
        System.out.println(" [2] Display all Items in Google Drive");
        System.out.println(" [3] Delete an Item in Google Drive");
        System.out.println(" [4] Print Google Drive Information");
        System.out.println(" [5] Delete Outdate Files from Drive");
        System.out.println();
    }

    static void menu() {
        System.out.println("Database Table Management");
        System.out.println("  [?] Help (this message)");
        System.out.println("  [0] Back to Main Menu");

        System.out.println("\nFor all tables");
        System.out.println("  [+] Create tables");
        System.out.println("  [-] Drop tables");

        System.out.println("\nFor users table");
        System.out.println("  [a] Query for a specific row in users");
        System.out.println("  [b] Query for all rows in users");
        System.out.println("  [c] Delete a row in users");
        System.out.println("  [d] Insert a new row in users");
        System.out.println("  [e] Update a row in users");

        System.out.println("\nFor messages table");
        System.out.println("  [h] Query for a specific row");
        System.out.println("  [i] Query for all rows");
        System.out.println("  [j] Delete a row");
        System.out.println("  [k] Insert a new row");
        System.out.println("  [l] Update a row");

        System.out.println("\nFor comments table");
        System.out.println("  [p] Query for a specific row");
        System.out.println("  [q] Query for all rows");
        System.out.println("  [r] Delete a row");
        System.out.println("  [s] Insert a new row");
        System.out.println("  [t] Update a row");

        System.out.println("\nFor votes table");
        System.out.println("  [v] Query for a specific row");
        System.out.println("  [w] Query for all rows");
        System.out.println("  [x] Delete a row");
        System.out.println("  [y] Insert a new row");
        System.out.println("  [z] Update a row");
        System.out.println();
    }

    static void searchMenu() {
        System.out.println("Search Menu");
        System.out.println("  [?] Help (this message)");
        System.out.println("  [0] Back to Main Menu");
        System.out.println("  [1] Search by title");
        System.out.println("  [2] Search by message");
        System.out.println("  [3] Search by author");
        System.out.println("  [4] Search by date");
        System.out.println("  [5] Search by all");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "?0+-abcdehijklpqrstvwxyzVUMC";

        // We repeat until a valid single-character option is selected
        while (true) {
            // System.out.print("[" + actions + "] :> ");

            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");

        }
    }

    static char initPrompt(BufferedReader in) {
        // The valid actions:
        String actions = "?0123";

        // We repeat until a valid single-character option is selected
        while (true) {
            // System.out.print("[" + actions + "] :> ");

            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    static char cPrompt(BufferedReader in) {
        // The valid actions:
        String actions = "?012345abcd";

        // We repeat until a valid single-character option is selected
        while (true) {
            // System.out.print("[" + actions + "] :> ");

            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    static char confirmPrompt(BufferedReader in) {
        // The valid actions:
        String actions = "yn";

        // We repeat until a valid single-character option is selected
        while (true) {
            // System.out.print("[" + actions + "] :> ");

            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    static char searchPrompt(BufferedReader in) {
        // The valid actions:
        String actions = "?012345";

        // We repeat until a valid single-character option is selected
        while (true) {
            // System.out.print("[" + actions + "] :> ");

            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided. May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

    /**
     * The main routine runs a loop that gets a request from the user and processes
     * it
     * 
     * @param argv Command-line options. Ignored by this program.
     * @throws ParseException
     * 
     */
    public static void main(String[] argv) throws ParseException {

        // final Gson gson = new Gson();

        // Google Drive Stuff
        System.out.println("CREDENTIALS_FOLDER: " + CREDENTIALS_FOLDER.getAbsolutePath());

        // 1: Create CREDENTIALS_FOLDER
        /*
         * if (!CREDENTIALS_FOLDER.exists()) { CREDENTIALS_FOLDER.mkdirs();
         * 
         * System.out.println("Created Folder: " +
         * CREDENTIALS_FOLDER.getAbsolutePath()); System.out.println("Copy file " +
         * CLIENT_SECRET_FILE_NAME + " into folder above.. and rerun this class!!");
         * return; }
         */

        // get the Postgres configuration from the environment
        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL");
        db_url = "postgres://yoikxsezpixhoa:f2a6909356ae1a5af25353fdd4f07c5a647ae407431beda30572e050c775fc1f@ec2-52-73-247-67.compute-1.amazonaws.com:5432/dd134cv3dbo47v";

        // Get a fully-configured connection to the database, or exit
        // immediately
        Database db = Database.getDatabase(db_url);
        if (db == null)
            return;

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("");
        System.out.println("Welcome to Buzz Admin Interface");
        System.out.println("Please start by selecting one of the following:");
        initMenu();
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            // function call
            System.out.println();
            System.out.print("Please enter command: ");
            char input = initPrompt(in);
            if (input == '?') {
                initMenu();
            } else if (input == '0') {
                break;
            } else if (input == '1') {
                System.out.println("You have selected the Content Management interface");
                while (true) {
                    System.out.println();
                    adminMenu();
                    System.out.print("Please enter command:");
                    char cin = cPrompt(in);
                    System.out.println("selected:" + cin);
                    if (cin == '?') {// help
                        adminMenu();
                    } else if (cin == '0') {
                        initMenu();
                        break;
                    } else if (cin == 'a') {
                        ArrayList<Database.RowData> res = db.mSelectAll();
                        if (res == null)
                            continue;
                        printMDB(res); //Refactored phase 4
                    } else if (cin == 'b') {
                        System.out.println("Selected View Message by ID");
                        int id = getInt(in, "Enter the row ID");
                        if (id == -1)
                            continue;
                        Database.RowData res = db.mSelectOne(id);
                        if (res != null) {
                            System.out.println("  [" + res.mId + "] " + res.mTitle);
                            System.out.println("  --> " + res.mMessage);
                            System.out.println(" Attachment: " + res.attachment);
                            System.out.println(" Link: " + res.mLink);
                            System.out.println("  Written by: [" + res.uId + "]");
                            System.out.println("  Date Created: [" + res.date_created + "]");
                            System.out.println("\n Votes: " + res.totalVotes + "\n");
                        }
                    } else if (cin == 'c') {
                        int id = getInt(in, "Enter the row ID");
                        if (id == -1)
                            continue;
                        int res = db.mDeleteRow(id);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows deleted");
                    } else if (cin == 'd') {
                        java.util.Date date = new java.util.Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        c.add(Calendar.MONTH, -1);
                        // c.add(Calendar.DAY_OF_MONTH, -1);
                        java.util.Date newDate = c.getTime();
                        java.sql.Date sqlDate = new java.sql.Date(newDate.getTime());
                        System.out
                                .println("\nThis will delete all the posts that were posted before the following date: "
                                        + (sqlDate));
                        System.out.println("Is that Okay?");
                        System.out.println("[y] or [n]");
                        char confirm = confirmPrompt(in);
                        if (confirm == 'y') {
                            System.out.println("\nnow deleting all messages posted before " + sqlDate);
                            int res = db.mDeleteDate(sqlDate);
                            if (res == -1)
                                continue;
                            System.out.println("Deleted the messages");

                        } else if (confirm == 'n') {
                            System.out.println("deletion cancelled.");
                        }
                    } else if (cin == '1') {
                        ArrayList<Database.RowData> res = db.mDisplayAttachment();
                        if (res == null)
                            continue;
                        System.out.println("\n  Attachments by Recent Date");
                        System.out.println("  -------------------------");
                        for (Database.RowData rd : res) {
                            System.out.println("  [" + rd.mId + "]" + " Attachment: " + rd.attachment + " Owner: ["
                                    + rd.uName + "]" + " Date_Created: " + rd.date_created);
                        }
                    } else if (cin == '2') {
                        try {
                            // 3: Read client_secret.json file & create Credential object.
                            final NetHttpTransport DRIVE_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                            Credential credential = getCredentials(DRIVE_TRANSPORT);

                            // 5: this is the current service
                            Drive service = new Drive.Builder(DRIVE_TRANSPORT, JACKSON_FACTORY, credential) //
                                    .setApplicationName(APPLICATION_NAME).build();

                            System.out.println("\nDisplaying all Files in Google Drive");
                            System.out.println("-------------------------------------------");
                            FileList result = service.files().list().setPageSize(30).setFields("nextPageToken, files")
                                    .execute();
                            List<File> files = result.getFiles();
                            if (files == null || files.isEmpty()) {
                                System.out.println("There are no attachments in the google drive");
                            } else {

                                for (File file : files) {
                                    System.out.printf("created/modified on:    %s id:    %s Title:    %s\n",
                                            file.getModifiedTime(), file.getId(), file.getName());
                                    System.out.println("Owner Information: " + file.getOwners());
                                    System.out.println();
                                }
                            }
                        } catch (Error e) {
                            // e.printStackTrace();
                            break;
                        } catch (IOException e) {
                            break;
                        } catch (GeneralSecurityException e) {
                            System.out.println("general security exception");
                            break;
                        }
                    } else if (cin == '3') {
                        try {
                            // 3: Read client_secret.json file & create Credential object.
                            final NetHttpTransport DRIVE_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                            Credential credential = getCredentials(DRIVE_TRANSPORT);

                            // 5: this is the current service
                            Drive service = new Drive.Builder(DRIVE_TRANSPORT, JACKSON_FACTORY, credential) //
                                    .setApplicationName(APPLICATION_NAME).build();

                            System.out.println("\nDisplaying all Files in Google Drive");
                            System.out.println("-------------------------------------------");
                            // FileList result =
                            // service.files().list().setPageSize(30).setFields("nextPageToken, files(id,
                            // name, modifiedTime)").execute();
                            FileList result = service.files().list().setPageSize(30).setFields("nextPageToken, files")
                                    .execute();
                            List<File> files = result.getFiles();
                            if (files == null || files.isEmpty()) {
                                System.out.println("There are no attachments in the google drive");
                                adminMenu();
                                break;
                            } else {
                                int i = 0;
                                for (File file : files) {
                                    System.out.printf("[%d]   created/modified on:    %s id:    %s Title:    %s\n", i,
                                            file.getModifiedTime(), file.getId(), file.getName());

                                    System.out.println("Owner Information: " + file.getOwners());
                                    System.out.println();
                                    i++;
                                }
                            }
                            System.out
                                    .print("\nplease type in the [number] of the file that you would like to delete:");
                            // Scanner myScanner = new Scanner(System.in);
                            // int ref = myScanner.nextInt();
                            int ref = Integer.parseInt(in.readLine());
                            String delfile = files.get(ref).getName();
                            service.files().delete(files.get(ref).getId()).execute();
                            System.out.println(delfile + " was successfully deleted");

                            // myScanner.close();
                        } catch (Error e) {
                            // e.printStackTrace();

                        } catch (IOException e) {
                            // e.printStackTrace();

                        } catch (GeneralSecurityException e) {
                            // e.printStackTrace();
                            System.out.println("general security exception");

                        }

                    } else if (cin == '4') {
                        try {
                            // 3: Read client_secret.json file & create Credential object.
                            final NetHttpTransport DRIVE_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                            Credential credential = getCredentials(DRIVE_TRANSPORT);

                            // 5: this is the current service
                            Drive service = new Drive.Builder(DRIVE_TRANSPORT, JACKSON_FACTORY, credential) //
                                    .setApplicationName(APPLICATION_NAME).build();

                            // About about = service.about().get().execute();
                            About about = service.about().get().setFields("user, storageQuota").execute();

                            System.out.println("\nBuzz Google Storage Information");
                            System.out.println("-------------------------------------------------");
                            // System.out.println("Current user name: " + about.getUser());
                            System.out.println(
                                    "Storage Quota(Usage limit): " + about.getStorageQuota().getLimit() + " bytes");
                            System.out.println("Storage Quota(Usage across all services): "
                                    + about.getStorageQuota().getUsage() + " bytes");
                            System.out.println("Storage Quota(Usage by all files): "
                                    + about.getStorageQuota().getUsageInDrive() + " bytes");
                            System.out.println("Storage Quota(Usage in Drive Trash): "
                                    + about.getStorageQuota().getUsageInDriveTrash() + " bytes");

                            System.out.println("Remaining bytes: "
                                    + (about.getStorageQuota().getLimit() - about.getStorageQuota().getUsage())
                                    + " bytes");
                            System.out.println("User :" + about.getUser().getDisplayName());

                        } catch (Error e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                            System.out.println("general security exception");
                        }
                    } else if (cin == '5') {
                        try {
                            // 3: Read client_secret.json file & create Credential object.

                            java.util.Date date = new java.util.Date();
                            Calendar c = Calendar.getInstance();
                            c.setTime(date);
                            c.add(Calendar.MONTH, -1);
                            // c.add(Calendar.DAY_OF_MONTH, +4);
                            java.util.Date newDate = c.getTime();

                            System.out.println(
                                    "\nThis will delete all the posts that were posted before the following date: "
                                            + (newDate));
                            System.out.println("Is that Okay?");
                            System.out.println("[y] or [n]");

                            char confirm = confirmPrompt(in);

                            if (confirm == 'y') {
                                final NetHttpTransport DRIVE_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
                                Credential credential = getCredentials(DRIVE_TRANSPORT);
                                Drive service = new Drive.Builder(DRIVE_TRANSPORT, JACKSON_FACTORY, credential) //
                                        .setApplicationName(APPLICATION_NAME).build();

                                FileList result = service.files().list().setPageSize(30)
                                        .setFields("nextPageToken, files").execute();
                                List<File> files = result.getFiles();
                                if (files == null || files.isEmpty()) {
                                    System.out.println("There are no attachments in the google drive");
                                    adminMenu();
                                    break;
                                }

                                try {

                                    for (File file : files) {

                                        String time = file.getModifiedTime().toString();
                                        String dateString = time.substring(0, 10);
                                        java.util.Date convertdate = new SimpleDateFormat("yyyy-MM-dd")
                                                .parse(dateString);
                                        if (newDate.compareTo(convertdate) > 0) {
                                            String delfile = file.getName();
                                            service.files().delete(file.getId()).execute();
                                            System.out.println(delfile + " was successfully deleted");
                                        }
                                    }

                                } catch (Exception e) {
                                    System.out.println("parse failed");
                                }

                            } else if (confirm == 'n') {
                                System.out.println("deletion cancelled.");
                            }

                        } catch (Error e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();

                        } catch (GeneralSecurityException e) {
                            e.printStackTrace();
                            System.out.println("general security exception");
                        }
                    }
                }
            } else if (input == '2') {
                System.out.println("You have selected the Database/table management\n");
                menu();
                while (true) {
                    System.out.print("Please enter command:");
                    char action = prompt(in);
                    System.out.println("selected: " + action);
                    if (action == '?') {// help
                        menu();
                    } else if (action == '0') { // quit
                        initMenu();
                        break;
                    } else if (action == '+') { // create tables
                        db.createTable();
                    } else if (action == '-') { // drop tables
                        db.dropTable();
                    } else if (action == 'a') { // START OF OPTIONS FOR USERS
                        int id = getInt(in, "Enter the row ID");
                        if (id == -1)
                            continue;
                        Database.RowData res = db.uSelectOne(id);
                        if (res != null) {
                            System.out.println("  [" + res.uId + "] " + res.uName + "\t BIO: \n" + res.uBio);
                        }
                    } else if (action == 'b') {
                        ArrayList<Database.RowData> res = db.uSelectAll();
                        if (res == null)
                            continue;
                        System.out.println("  Current Database Contents");
                        System.out.println("  -------------------------");
                        for (Database.RowData rd : res) {
                            System.out.println("  [" + rd.uId + "] Name: " + rd.uName + " BIO: \n" + rd.uBio);
                        }
                    } else if (action == 'c') {
                        int id = getInt(in, "Enter the row ID");
                        if (id == -1)
                            continue;
                        int res = db.uDeleteRow(id);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows deleted");
                    } else if (action == 'd') {
                        String name = getString(in, "Enter the user's name");
                        String bio = getString(in, "Enter a bio for the user");
                        if (name.equals("")) {
                            System.out.println("Name was null");
                            continue;
                        }
                        int res = db.uInsertRow(name, bio);
                        System.out.println(res + " rows added");
                    } else if (action == 'e') {
                        int id = getInt(in, "Enter the row ID :> ");
                        if (id == -1)
                            continue;
                        String newName = getString(in, "Enter the new user name");
                        int res = db.uUpdateOne(newName, id);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows updated");
                        // END OF OPTIONS FOR USERS
                    } else if (action == 'h') {
                        int id = getInt(in, "Enter the row ID");
                        if (id == -1)
                            continue;
                        Database.RowData res = db.mSelectOne(id);
                        if (res != null) {
                            System.out.println("  [" + res.mId + "] " + res.mTitle);
                            System.out.println("  --> " + res.mMessage);
                            System.out.println(" Attachment: " + res.attachment);
                            System.out.println(" Link: " + res.mLink);
                            System.out.println("  Written by: [" + res.uId + "]");
                            System.out.println("  Date Created: [" + res.date_created + "]");
                            System.out.println("\n Votes: " + res.totalVotes + "\n");
                        }
                    } else if (action == 'i') {
                        ArrayList<Database.RowData> res = db.mSelectAll();
                        if (res == null)
                            continue;
                        printMDB(res); //refactored phase 4
                    } else if (action == 'j') {
                        int id = getInt(in, "Enter the row ID");
                        if (id == -1)
                            continue;
                        int res = db.mDeleteRow(id);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows deleted");
                    } else if (action == 'k') {
                        String subject = getString(in, "Enter the subject");
                        String message = getString(in, "Enter the message");
                        String attachment = getString(in, "Enter the Attachment:");
                        String mLink = getString(in, "Enter the Link:");
                        int u_id = getInt(in, "Enter the user id");
                        System.out.println(subject + " rows added " + u_id);
                        if (subject.equals("") || message.equals("") || u_id == 0)
                            continue;
                        int res = db.mInsertRow(subject, message, u_id, attachment, mLink);
                        System.out.println(res + " rows added");
                    } else if (action == 'l') {
                        int id = getInt(in, "Enter the row ID :> ");
                        if (id == -1)
                            continue;
                        String newSubject = getString(in, "Enter the new title");
                        String newMessage = getString(in, "Enter the new message");
                        String newAttachment = getString(in, "Enter the new attachment");
                        String newLink = getString(in, "Enter the new link");
                        int res = db.mUpdateOne(id, newSubject, newMessage, newAttachment, newLink);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows updated");
                    } else if (action == 'p') {
                        int id = getInt(in, "Enter the message ID");
                        if (id == -1)
                            continue;
                        ArrayList<Database.RowData> res = db.cSelectOne(id);
                        for (Database.RowData rd : res) {
                            System.out.println("  comment id: [" + rd.cId + "]");
                            System.out.println("  message id: [" + rd.mId + "]");
                            System.out.println("  user id: [" + rd.uId + "]");
                            System.out.println(rd.cMessage);
                            System.out.println("  posted by: [" + rd.uName + "]\n");
                        }
                    } else if (action == 'q') {
                        ArrayList<Database.RowData> res = db.cSelectAll();
                        if (res == null)
                            continue;
                        System.out.println("  Current Database Contents");
                        System.out.println("  -------------------------");
                        for (Database.RowData rd : res) {
                            System.out.println("  comment id: [" + rd.cId + "]");
                            System.out.println("  message id: [" + rd.mId + "]");
                            System.out.println("  user id: [" + rd.uId + "]");
                            System.out.println(rd.cMessage + "\n");
                        }
                    } else if (action == 'r') {
                        int id = getInt(in, "Enter the row ID");
                        if (id == -1)
                            continue;
                        int res = db.cDeleteRow(id);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows deleted");
                    } else if (action == 's') {
                        int mId = getInt(in, "Enter the message id");
                        int uId = getInt(in, "Enter the user id");
                        String comment = getString(in, "Enter the comment");
                        if (comment.equals(""))
                            continue;
                        int res = db.cInsertRow(comment, mId, uId);
                        System.out.println(res + " rows added");
                    } else if (action == 't') {
                        int id = getInt(in, "Enter the row ID :> ");
                        if (id == -1)
                            continue;
                        String newComment = getString(in, "Enter the new comment");
                        int res = db.cUpdateOne(id, newComment);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows updated");
                    } else if (action == 'v') {
                        int id = getInt(in, "Enter the message ID");
                        if (id == -1)
                            continue;
                        ArrayList<Database.RowData> res = db.vSelectOne(id);
                        for (Database.RowData rd : res) {
                            System.out.println("Message id: [" + rd.mId + "]   User id: [" + rd.uId + "]   Vote: ["
                                    + rd.vote + "]");
                        }
                    } else if (action == 'w') {
                        ArrayList<Database.RowData> res = db.vSelectAll();
                        if (res == null)
                            continue;
                        System.out.println("  Current Database Contents");
                        System.out.println("  -------------------------");
                        for (Database.RowData rd : res) {
                            System.out.println(
                                    "  Message id: [" + rd.mId + "] User id: [" + rd.uId + "] Vote: " + rd.vote);
                        }
                    } else if (action == 'x') {
                        int id = getInt(in, "Enter the user ID");
                        if (id == -1)
                            continue;
                        int res = db.vDeleteRow(id);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows deleted");
                    } else if (action == 'y') {
                        int u_id = getInt(in, "Enter the user id");
                        int m_id = getInt(in, "Enter the message id");
                        int vote = getInt(in, "Enter their vote (-1 or 1)");
                        if (u_id == -1 || m_id == -1)
                            continue;
                        int res = db.vInsertRow(u_id, m_id, vote);
                        System.out.println(res + " rows added");
                    } else if (action == 'z') {
                        int m_id = getInt(in, "Enter the message ID ");
                        int u_id = getInt(in, "Enter the user ID ");
                        if (m_id == -1 || u_id == -1)
                            continue;
                        int newVote = getInt(in, "Enter the new vote");
                        int res = db.vUpdateOne(u_id, m_id, newVote);
                        if (res == -1)
                            continue;
                        System.out.println("  " + res + " rows updated");
                    } else if (action == 'V') {
                        db.vDropTable();
                    } else if (action == 'M') {
                        db.mDropTable();
                    } else if (action == 'U') {
                        db.uDropTable();
                    } else if (action == 'C') {
                        db.cDropTable();
                    }
                }
            } else if (input == '3') { /*NEW TO PHASE 4: Search Menu */
                System.out.println("You have selected Search Database\n");
                searchMenu();
                while (true) {
                    System.out.println("Please enter command:");
                    char action = searchPrompt(in);
                    System.out.println("selected: " + action);
                    if (action == '?') {// help
                        searchMenu();
                    } else if (action == '0') { // quit
                        initMenu();
                        break;
                    } else if (action == '1') {  //Search Menu
                        System.out.println("You entered search by title.\n");

                        String titleString = getString(in, "Enter title keywords"); //Will accept any string
                        ArrayList<Integer> ids = searchTitle(titleString, db); //actions performed in searchTitle method for modularity/readibility
                        printResults(ids, db);                                  //printResults method used for all results

                    } else if (action == '2') {
                        System.out.println("You entered search by message.\n");

                        String messageString = getString(in, "Enter message keywords"); //will accept any string
                        ArrayList<Integer> ids = searchMsg(messageString, db);          
                        printResults(ids, db);                                          

                    } else if (action == '3') {
                        System.out.println("You entered search by author \n");

                        String nameString = getString(in, "Enter name of author: "); //will accept any string
                        ArrayList<Integer> ids = searchUser(nameString, db);
                        printResults(ids, db);

                    } else if (action == '4') {
                        System.out.println("You entered search by date \n");   //input parsed within method due to complexities of formatting

                        ArrayList<Integer> ids = searchDate(in, db);
                        printResults(ids, db);

                    } else {
                        System.out.println("You entered search all");

                        String uInput = getString(in, "Enter search keywords"); //accepts any string
                        ArrayList<Integer> ids = searchAll(uInput, db);

                        printResults(ids, db);

                    }

                }

            }
        }
        // Always remember to disconnect from the database when the program
        // exits
        db.disconnect();
    }

    /**
     * searchTitle method searches titles of every post in database for keyword(s) entered by the user 
     * 
     * @param titleString - the string entered by the user
     * @param db - the database
     * @return ids is an ArrayList containing the mIds of all posts that matched the keyword(s) entered by the user
     */
    static ArrayList<Integer> searchTitle(String titleString, Database db) {
        String[] uTitle = titleString.split(" "); // title the user entered

        ArrayList<Database.RowData> res = db.mSelectAll(); // all data associated with message table
        ArrayList<Integer> ids = new ArrayList<Integer>();

        for (Database.RowData rd : res) { // go through all posts
            String dbTitle = rd.mTitle; // title from one message
            for (int i = 0; i < uTitle.length; i++) { // go through all keywords user entered
                // uTitle[i].toLowerCase();
                if (dbTitle.toLowerCase().matches("(.*)" + uTitle[i].toLowerCase() + "(.*)") && !ids.contains(rd.mId)) {  //case insensitive
                                                                                                                          //checks to see if keyword is contained in any part of the message
                                                                                                                          //ensures the message is not already accounted for in the ids arraylist
                    ids.add(rd.mId); 
                }
            }
        }

        return ids;
    }

    /**
     * searchMsg searches all of the messages in the database for keyword(s) entered by the user
     * Has same functionality as searchTitle but for messages
     * @param messageString - entered by user
     * @param db 
     * @return arraylist of matched mIds
     */
    static ArrayList<Integer> searchMsg(String messageString, Database db) {
        String[] uMessage = messageString.split(" "); // title the user entered

        ArrayList<Database.RowData> res = db.mSelectAll(); // all data associated with message table
        ArrayList<Integer> ids = new ArrayList<Integer>();

        for (Database.RowData rd : res) { // go through all posts
            String dbMessage = rd.mMessage; // title from one message

            for (int i = 0; i < uMessage.length; i++) { // go through all keywords user entered
                if (dbMessage.toLowerCase().matches("(.*)" + uMessage[i].toLowerCase() + "(.*)")
                        && !ids.contains(rd.mId)) { 
                    ids.add(rd.mId);
                }
            }
        }

        return ids;
    }

    /**
     * searchUser checks to see if keywords entered by the admin match the name of a user who has made a post in the database
     * Same logic as searchTitle and searchMsg
     * 
     * @param nameString - name input by admin
     * @param db
     * @return - mIds of posts associated with the user name input by admin
     */
    static ArrayList<Integer> searchUser(String nameString, Database db) {
        String[] uName = nameString.split(" ");

        ArrayList<Database.RowData> users = db.uSelectAll();
        ArrayList<Integer> uIds = new ArrayList<Integer>();

        for (Database.RowData rd : users) { // go through all users
            String dbUser = rd.uName; // name of user

            for (int i = 0; i < uName.length; i++) {
                if (dbUser.toLowerCase().matches("(.*)" + uName[i].toLowerCase() + "(.*)") && !uIds.contains(rd.uId)) {
                    uIds.add(rd.uId);
                }
            }

        }

        ArrayList<Integer> mIds = new ArrayList<Integer>();
        ArrayList<Database.RowData> res = db.mSelectAll();

        for (int i = 0; i < uIds.size(); i++) {
            for (Database.RowData rd : res) {
                int userId = rd.uId;
                if (userId == uIds.get(i)) {
                    mIds.add(rd.mId);
                }
            }
        }

        return mIds;
    }

    /**
     * searchDate finds all posts made on a date input by user
     * Parses input in YYYY-MM-DD format
     * returns mIds of all posts made on that day
     * @param in - buffered reader
     * @param db - database
     * @return - arraylist of mIds
     * @throws ParseException
     */
    static ArrayList<Integer> searchDate(BufferedReader in, Database db) throws ParseException {
        String year, month, day;
        boolean valYear = false; //must be true to be valid input
        boolean valMonth = false;
        boolean valDay = false;

        String date;

        do { //do-while loop to prompt user for input until they enter a valid year
            year = getString(in, "Enter numeric year in format YYYY: ");

            try {
                if (Integer.parseInt(year) <= 999 || Integer.parseInt(year) > 9999)
                    System.out.println("Invalid year");
                else
                    valYear = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid year");
            }

        } while (valYear == false);

        do { //do-while loop to prompt user to enter a valid month
            month = getString(in, "Enter numeric month in format MM: ");

            try {
                if (month.length() != 2 || Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12)
                    System.out.println("Invalid month");
                else
                    valMonth = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid month");
            }

        } while (valMonth == false);

        do { //do-while loop to prompt user to enter a valid day
            day = getString(in, "Enter numeric day in format DD: ");

            try {
                if (day.length() != 2 || Integer.parseInt(day) < 1)
                    valDay = false;
                else { //checks how many days max in month to see max date
                    switch (Integer.parseInt(month)) {
                        case 4:
                        case 6:
                        case 9:
                        case 11:
                            if (Integer.parseInt(day) <= 30)
                                valDay = true;
                            break;
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                        case 8:
                        case 10:
                        case 12:
                            if (Integer.parseInt(day) <= 31)
                                valDay = true;
                            break;
                        case 2:
                            if (Integer.parseInt(day) <= 29)
                                valDay = true;
                            break;
                    }
                }

                if (valDay == false)
                    System.out.println("Invalid day");

            } catch (NumberFormatException e) {
                System.out.println("Invalid day");
            }

        } while (valDay == false);

        date = year + "-" + month + "-" + day; //formats string entered
        System.out.println("You entered: " + date);

        ArrayList<Database.RowData> res = db.mSelectAll(); // all data associated with message table
        ArrayList<Integer> ids = new ArrayList<Integer>();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern); 

        Date userDate = format.parse(date); //puts string user entered into Date datatype

        for (Database.RowData rd : res) { // go through all posts
            try {
                if (userDate.compareTo(rd.date_created) == 0 && !ids.contains(rd.mId)) { //check to see if dates match and not already in mIds
                    ids.add(rd.mId);
                }
            } catch (NullPointerException e) {
                continue;
            }
        }

        return ids;
    }

    /**
     * searchAll utilizes searchTitle, searchMsg, and searchUser to look for all posts that contain any/all of the keywords entered by the user in
     * their title, message, or user name
     * 
     * Ensures double counting does not occur
     * 
     * @param uInput - input keywords from user
     * @param db
     * @return - arraylist of mIds
     */
    static ArrayList<Integer> searchAll(String uInput, Database db) {
        ArrayList<Integer> tIds = searchTitle(uInput, db);
        ArrayList<Integer> mIds = searchMsg(uInput, db);
        ArrayList<Integer> uIds = searchUser(uInput, db);

        ArrayList<Integer> ids = new ArrayList<Integer>();

        for (int i = 0; i < tIds.size(); i++) { //for loops ensure double counting does not occur
            if (!ids.contains(tIds.get(i))) {
                ids.add(tIds.get(i));
            }
        }

        for (int i = 0; i < mIds.size(); i++) {
            if (!ids.contains(mIds.get(i))) {
                ids.add(mIds.get(i));
            }
        }

        for (int i = 0; i < uIds.size(); i++) {
            if (!ids.contains(uIds.get(i))) {
                ids.add(uIds.get(i));
            }
        }

        return ids;
    }

    /**
     * printResults method to format and print results of messages that matched with keywords 
     * @param ids - arraylist of mIds from matched posts
     * @param db
     */
    static void printResults(ArrayList<Integer> ids, Database db) {
        System.out.println(ids.size() + " results found\n"); //size is number of matches
        for (int i = 0; i < ids.size(); i++) {
            Database.RowData sel = db.mSelectOne(ids.get(i)); //get info about post associated with mId
            if (sel != null) {
                System.out.print("  [" + sel.mId + "] " + sel.mTitle);
                System.out.print("  --> " + sel.mMessage);
                System.out.print(" Attachment: " + sel.attachment);
                System.out.print(" Link: " + sel.mLink);
                System.out.print("  Written by: [" + sel.uId + "]");
                System.out.print("  Date Created: [" + sel.date_created + "]");
                System.out.println(" Votes: " + sel.totalVotes + "\n");
            }
        }
    }

    /**
     * printMDB prints message database in table format
     * @param res - row data for mSelectAll
     */
    static void printMDB(ArrayList<Database.RowData> res){
        System.out.println("\n  Current Database Contents");
        System.out.println("  -------------------------");
        for (Database.RowData rd : res) {
            System.out.println("  [" + rd.mId + "] Title: " + rd.mTitle + " Attachment: "
                + rd.attachment + " Link: " + rd.mLink + " Votes: " + rd.totalVotes
                + " WRITTEN BY: [" + rd.uId + "]" + " Date_Created: " + rd.date_created);
            }
    }

}
