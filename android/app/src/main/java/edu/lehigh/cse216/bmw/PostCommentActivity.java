package edu.lehigh.cse216.bmw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class PostCommentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post_comment);

        // Get the parameter from the calling activity, and put it in the TextView
        Intent input = getIntent();
        String label_contents = input.getStringExtra("label_contents");
        TextView tv =  findViewById(R.id.specialMessage);
        tv.setText(label_contents);

        int id = Integer.parseInt(MainActivity.postId);
        final String URL = String.format(Routes.getCommentUrl(), id);

        Button bOk = findViewById(R.id.buttonOk);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // EditText et = findViewById(R.id.editTitle);
                EditText em = findViewById(R.id.editMessage);
                if (!em.getText().toString().equals("")) {

                    try {
                        Log.d("bmw","click worked");
                        VolleySingleton vs = VolleySingleton.getInstance(view.getContext());
                        Log.d("bmw","instance of volley singleton");
                        String inputjson = usefulTools.commentSend(em.getText().toString());
                        Log.d("bmw", inputjson);
                        JSONObject ob = new JSONObject(inputjson);
                        Log.d("bmw","created json object");
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, ob,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("bmw","post success");
                                        Toast.makeText(PostCommentActivity.this, "Comment Successful", Toast.LENGTH_LONG).show();
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("TheBuzz", "Error posting comment");
                                    }
                                }
                        );
                        vs.addRequest(jsonObjectRequest);
                    }
                    catch(Exception e){
                        Log.d("bmw","Posting error");
                    }
                }
            }
        });

        // The Cancel button returns to the caller without sending any data
        Button bCancel = findViewById(R.id.buttonCancel);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}
