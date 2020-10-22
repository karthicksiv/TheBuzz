package edu.lehigh.cse216.bmw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewPostActivity extends AppCompatActivity {

    private String mTitle;

    private String mMessage;

    private String mImage;

    private int mVote;

    private String mId;

    private String uId;

    TextView title, message, vote, messageId, userId;
    Button upvote, downvote, backButton, viewProfile, postComment;

    public static String user_id;

    public static ArrayList<Datum> mComments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_post);

        int id = Integer.parseInt(MainActivity.postId);
        final String URL = String.format(Routes.getShow(), id);

        Log.d("bmw", "url is: " + URL);

        if (!VolleySingleton.OFFLINE) {
            VolleySingleton vs = VolleySingleton.getInstance(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("bmw", "got response");
                            try {
                                JSONObject ob = new JSONObject(response);
                                JSONObject json = ob.getJSONObject("mData");
                                Log.d("JSON received", "object is: " + json);

                                mTitle = json.getString("mTitle");
                                title = (TextView) findViewById(R.id.title);
                                title.setText(mTitle);

                                mMessage = json.getString("mMessage");
                                message = (TextView) findViewById(R.id.message);
                                message.setText(mMessage);

                                mVote = json.getInt("mVote");
                                vote = (TextView) findViewById(R.id.vote);
                                vote.setText(Integer.toString(mVote));

                                mId = json.getString("mId");
                                messageId = (TextView) findViewById(R.id.m_id);
                                messageId.setText(mId);

                                uId = json.getString("uId");
                                userId = (TextView) findViewById(R.id.u_id);
                                userId.setText(uId);

                                /*mImage = json.getString("mAttach");
                                byte[] imageByteArray = Base64.decode(mImage, Base64.DEFAULT);
                                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                                Glide
                                        .with(getApplicationContext())
                                        .load(imageByteArray)
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imageView);*/
                                ImageView imageView = (ImageView) findViewById(R.id.image_view);
                                Glide
                                        .with(getApplicationContext())
                                        .load("https://clubrunner.blob.core.windows.net/00000006560/Stories/da4336bb-3bb9-45de-ba1e-8f6615e90525.JPG")
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imageView);

                                checkComments();

                            } catch (final JSONException e) {
                                Log.d("BMW", "Error parsing JSON file: " + e.getMessage());
                                return;
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("BMW", error.getMessage());
                }
            }); //see how to make a simple request on android
            vs.addRequest(stringRequest);
        } else {
            Log.d("BMW", "failed to get json, vs offline");
        }

        upvote = findViewById(R.id.buttonUpvote);
        upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolleySingleton vs = VolleySingleton.getInstance(view.getContext());
                int id = Integer.parseInt(mId);
                final String URL = String.format(Routes.getMessageUpvoteUrl(), id);
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ViewPostActivity.this, "upvote success!", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BMW", "error while upvote");
                        Toast.makeText(ViewPostActivity.this, "upvote failed", Toast.LENGTH_SHORT).show();
                    }
                }); //see how to make a simple request on android
                vs.addRequest(stringRequest);
            }
        });

        downvote = findViewById(R.id.buttonDownvote);
        downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolleySingleton vs = VolleySingleton.getInstance(view.getContext());
                int id = Integer.parseInt(mId);
                final String URL = String.format(Routes.getMessageDownvoteUrl(), id);
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(ViewPostActivity.this, "downvote success!", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("BMW", "error while upvote");
                        Toast.makeText(ViewPostActivity.this, "downvote failed", Toast.LENGTH_SHORT).show();
                    }
                }); //see how to make a simple request on android
                vs.addRequest(stringRequest);
            }
        });

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.back_button:

                        finish();
                }
            }
        });

        viewProfile = findViewById(R.id.view_user_profile);
        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.view_user_profile:
                        Intent i = new Intent(getApplicationContext(), ProfilePage.class);
                        startActivityForResult(i, 789); // 789 is the number that will come back to us
                }
            }
        });

        postComment = findViewById(R.id.make_comment);
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.make_comment:
                        Intent i = new Intent(getApplicationContext(), PostCommentActivity.class);
                        startActivityForResult(i, 789); // 789 is the number that will come back to us
                }
            }
        });

    }

    private void checkComments(){

        int id = Integer.parseInt(MainActivity.postId);
        final String URL = String.format(Routes.getCommentUrl(), id);
        Log.d("url is: ", "url = "+URL);

        if (!VolleySingleton.OFFLINE) {
            VolleySingleton vs = VolleySingleton.getInstance(this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("bmw", "got response");
                            populateListFromVolley(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("BMW", error.getMessage());
                }
            });
            vs.addRequest(stringRequest);
        }
        else Log.d("BMW","failed");

    }

    private void populateListFromVolley(String response){
        try {

            if(mComments!=null){
                mComments.clear();
            }

            JSONObject ob = new JSONObject(response);
            JSONArray json= ob.getJSONArray("mData");
            Log.d("bmw", "got mdata: "+json);
            for (int i = 1; i < json.length(); ++i) {
                String msg_id = json.getJSONObject(i).getString("mId");
                String usr_id = json.getJSONObject(i).getString("uId");
                String cmt_id = json.getJSONObject(i).getString("cId");
                String cmt_msg = json.getJSONObject(i).getString("cMessage");

                mComments.add(new Datum(msg_id, usr_id, cmt_id, cmt_msg));

                Log.d("bmw","entry added");
                ++i;
            }
        } catch (final JSONException e) {
            Log.d("BMW", "Error parsing JSON file: " + e.getMessage());
            return;
        }
        Log.d("BMW", "Successfully parsed JSON file.");

        RecyclerView rv2 = findViewById(R.id.datum_list_view2);
        rv2.setLayoutManager(new LinearLayoutManager(this));
        rv2.setNestedScrollingEnabled(true);
        ItemListAdapter2 adapter = new ItemListAdapter2(this, mComments);
        rv2.setAdapter(adapter);

        adapter.setClickListener(new ItemListAdapter2.ClickListener() {
            @Override
            public void onClick(Datum d) {
                Log.d("BMW", "Edit Post");

                Intent i = new Intent(getApplicationContext(), EditCommentActivity.class);
                startActivityForResult(i, 789); // 789 is the number that will come back to us
            }
        });

    }

}
