package edu.lehigh.cse216.bmw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    /**
     * mData holds the data we get from Volley for a post
     */
    public static ArrayList<Datum> mData = new ArrayList<>();
    public List<String> mylist = new ArrayList<>();
    ItemListAdapter adapter;
    ArrayAdapter<String> arrayAdapter;
    GoogleSignInClient mGoogleSignInClient;
    public static SignInButton signIn;
    int RC_SIGN_IN = 0;
    public static String TAG = "MainActivity";
    public static String postId = "";
    private String newText;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);                                                     // sets up toolbar/layout for main page
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("The Buzz");
        setSupportActionBar(toolbar);
        rv = findViewById(R.id.datum_list_view);

        rv.setNestedScrollingEnabled(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        //ListView listView = findViewById(R.id.my_list);

        FloatingActionButton fab = findViewById(R.id.fab);                                          // create floating action button for posting, set so a click starts post activity

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                    Intent i = new Intent(getApplicationContext(), PostActivity.class);
                    startActivityForResult(i, 789);
=======
                Intent i = new Intent(getApplicationContext(), PostActivity.class);
                startActivityForResult(i, 789); // 789 is the number that will come back to us
>>>>>>> remotes/origin/android
            }
        });

        //CODE FOR GOOGLE OAUTH BUTTON
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("93677453784-ao9b50mlfdlq66ppdqtro9b81uqr1738.apps.googleusercontent.com")  // this is the client id for bmw-cse216-android that Alli set up. Would not work with the correct client ID. Technical debt.
                .requestEmail()                                                                             // 433710337180-kb5utql63enl0kp57lmsfdvl1f97ciuj.apps.googleusercontent.com
                .build();                                                                                   // 93677453784-ao9b50mlfdlq66ppdqtro9b81uqr1738.apps.googleusercontent.com
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn = findViewById(R.id.sign_in_button);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();                                                                   // if sign in button clicked, call sign in method
                        break;
                }
            }
        });

<<<<<<< HEAD
        if(!VolleySingleton.OFFLINE) {                                                              // volley singleton to send request for all of the posts
=======
        if (!VolleySingleton.OFFLINE) {
>>>>>>> remotes/origin/android
            VolleySingleton vs = VolleySingleton.getInstance(this);
            Log.d("Volley", "vs initialized");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, Routes.getBuzz(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(TAG, "Ffffffffffffffffffffffffffffffffff");
                            populateListFromVolley(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("BMW error", Routes.getBuzz());
                            Log.d("BMW", "Server hasn't given us a response");
                        }
                    }
            );
            vs.addRequest(stringRequest);

        } else {
            Log.d("BMW", "failed");
        }

    }

<<<<<<< HEAD
    private void populateListFromVolley(String response){                                           //after getting all the posts from buzz route, populate datum array list with all the info
=======

    private void populateListFromVolley(String response) {
>>>>>>> remotes/origin/android
        try {
            JSONObject ob = new JSONObject(response);
            Log.d("BMW", response);
            JSONArray json = ob.getJSONArray("mData");
            for (int i = 0; i < json.length(); ++i) {
                String id = json.getJSONObject(i).getString("mId");
                String title = json.getJSONObject(i).getString("mTitle");
                int vote = json.getJSONObject(i).getInt("mVote");
                String user_id = json.getJSONObject(i).getString("uId");
                String message = json.getJSONObject(i).getString("mMessage");
                Log.d("BMW", message);
                try {
                    String date = json.getJSONObject(i).getString("date_created");
                    mData.add(new Datum(title, message, vote, id, user_id,date));
                }catch(final JSONException e){
                    String date = "May 12, 2020";
                    mData.add(new Datum(title, message, vote, id, user_id,date));
                }

                //mData.add(new Datum(title, message, vote, id, user_id,date));
                adapter = new ItemListAdapter(MainActivity.this, mData);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //mylist.add(title);
                Log.d("bmw", "entry added");
            }
        } catch (final JSONException e) {
            Log.d("BMW", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("BMW", "Successfully parsed JSON file.");
<<<<<<< HEAD
        RecyclerView rv = findViewById(R.id.datum_list_view);                                       //sets up recyclerview for displaying all of the posts
        rv.setNestedScrollingEnabled(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        ItemListAdapter adapter = new ItemListAdapter(this, mData);
        rv.setAdapter(adapter);
=======

        //ItemListAdapter adapter = new ItemListAdapter(this, mData);
        //rv.setAdapter(adapter);
//        List<String> mylist = new ArrayList<>();
        //ListView listView = findViewById(R.id.my_list);
        //arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mylist);
//        listView.setAdapter(arrayAdapter);
>>>>>>> remotes/origin/android

        adapter.setClickListener(new ItemListAdapter.ClickListener() {
            @Override
            public void onClick(Datum d) {                                                            // if one of the posts is clicked, it will bring you to a new page that displays the message and comments
                Log.d("BMW", "Go to Message Page");

                postId = d.getmId();                                                                //global var to hold id so that new activity can be launched and get all data from that id

                Intent i = new Intent(getApplicationContext(), ViewPostActivity.class);
                startActivityForResult(i, 789);
            }
        });
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(" Search Here ");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);

                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                 //for settings/profile
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(" Search Here ");
        initadapter(new ArrayList<Datum>());
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    initadapter(mData);
                    return false;
                }
                MainActivity.this.newText = newText;
                initDate(newText);
                //adapter.getFilter().filter(newText);

                return true;
            }
        });
        return true;
    }

    private void initDate(String newText) {
        //TODO
        ArrayList<Datum> data = new ArrayList<>();
        for (Datum mDatum : mData) {
            if (mDatum.getmTitle().contains(newText)|mDatum.getmMessage().contains(newText)|mDatum.getDate().contains(newText)) {
                data.add(mDatum);
            }
        }
        if (data.size() > 0) {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
        initadapter(data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                           //for clicking action bar
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {                                                           //not used currently, left for potential use later
            Intent i = new Intent(getApplicationContext(), SecondActivity.class);
            i.putExtra("label_contents", "CSE216 is the best");
            startActivityForResult(i, 789); // 789 is the number that will come back to us
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile) {                                                                   //brings to profile of logged in user
            Intent i = new Intent(getApplicationContext(), ProfilePage.class);
            startActivityForResult(i, 789); // 789 is the number that will come back to us
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signIn() {                                                                         //starts sign in intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {                    //starts on result of sign in
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Log.i(TAG, "FFFFFFFFFFFFFFFFFFF");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

<<<<<<< HEAD
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {                      //get the token from sign in, send to backend
=======
    private void initadapter(ArrayList<Datum> list) {
        adapter = new ItemListAdapter(MainActivity.this, list);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
>>>>>>> remotes/origin/android
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            sendToBackend(idToken);

            //Update user interface to get rid of sign in button
            signIn.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "Signed into frontend", Toast.LENGTH_LONG).show();

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.v("Error", "signInResult:failed code=" + e.getStatusCode());
        }
    }

<<<<<<< HEAD
    public void sendToBackend(String token){
        //TODO send token to backend, get back, authenticate, currently getting back error code 401
=======
    public void sendToBackend(String token) {
        //TODO send token to backend, get back, authenticate
>>>>>>> remotes/origin/android
        try {

            VolleySingleton vs = VolleySingleton.getInstance(MainActivity.this);
            Log.d("bmw", "instance of volley singleton");
            String inputjson = usefulTools.tokenSend(token);
            Log.d("bmw", inputjson);
            JSONObject ob = new JSONObject(inputjson);
            Log.d("bmw", "created json object");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Routes.getAuthorizeUserUrl(), ob,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
<<<<<<< HEAD
                            Log.d("bmw","post success");
                            Toast.makeText(MainActivity.this, "token send success!", Toast.LENGTH_SHORT).show();
=======
                            Log.d("bmw", "post success");
                            Toast.makeText(MainActivity.this, "Post Successful!", Toast.LENGTH_SHORT).show();
>>>>>>> remotes/origin/android
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TheBuzz", "error with sending token to backend");
                        }
                    }
            );
            vs.addRequest(jsonObjectRequest);
        } catch (Exception e) {
            Log.d("bmw", "Posting error");
        }

    }

    @Override
    protected void onStart() {                                                                      //if account was previously signed in, it will remain signed in
        super.onStart();

        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(
                        this,
                        new OnCompleteListener<GoogleSignInAccount>() {
                            @Override
                            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                                handleSignInResult(task);
                            }
                        });
    }

    public void onErrorResponse(VolleyError volleyError) {
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (volleyError instanceof NoConnectionError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
    }

}




