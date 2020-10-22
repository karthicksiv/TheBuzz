package edu.lehigh.cse216.bmw;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.hardware.camera2.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class PostActivity extends AppCompatActivity {
    ImageView imv;
    //private CameraDevice mCamera;
    static final int CAMERA_REQUEST = 1;
    static final int PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post);

        // Get the parameter from the calling activity, and put it in the TextView
        Intent input = getIntent();
        String label_contents = input.getStringExtra("label_contents");
        TextView tv =  findViewById(R.id.specialMessage);
        tv.setText(label_contents);

        Button bOk = findViewById(R.id.buttonOk);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = findViewById(R.id.editTitle);
                EditText em = findViewById(R.id.editMessage);
                byte[] imageToSend = null;
                if (imv.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) imv.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    imageToSend = baos.toByteArray();
                }

                    try {
                        Log.d("bmw","click worked");
                        VolleySingleton vs = VolleySingleton.getInstance(view.getContext());
                        Log.d("bmw","instance of volley singleton");
                        String inputjson = null;
                        JSONObject jsonBody = new JSONObject();
                        jsonBody.put("mTitle", et.getText().toString());
                        jsonBody.put("mMessage", em.getText().toString());
                        if (imageToSend != null) {
                            jsonBody.put("mAttach", imageToSend);
                        }
                        //jsonBody.put("mLink", "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/bmw-logo-2020-1583084471.jpg");
                        Log.d("bmw","created json object");
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Routes.getBuzz(), jsonBody,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("bmw","post success");
                                       // Toast.makeText(PostActivity.this, "Post Successful!", Toast.LENGTH_SHORT).show();
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //Log.d("TheBuzz", "Error posting message");
                                        NetworkResponse networkResponse = error.networkResponse;
                                        if (networkResponse != null) {
                                            Log.e("Volley", "Error. HTTP Status Code:"+networkResponse.statusCode);
                                        }

                                        if (error instanceof TimeoutError) {
                                            Log.e("Volley", "TimeoutError");
                                        }else if(error instanceof NoConnectionError){
                                            Log.e("Volley", "NoConnectionError");
                                        } else if (error instanceof AuthFailureError) {
                                            Log.e("Volley", "AuthFailureError");
                                        } else if (error instanceof ServerError) {
                                            Log.e("Volley", "ServerError");
                                        } else if (error instanceof NetworkError) {
                                            Log.e("Volley", "NetworkError");
                                        } else if (error instanceof ParseError) {
                                            Log.e("Volley", "ParseError");
                                        }
                                    }
                                }
                        );
                        vs.addRequest(jsonObjectRequest);
                    }
                    catch(Exception e){
                        Log.d("bmw","Posting error");
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

        //allows user to pick a file from their phone
        Button bGallery = findViewById(R.id.getfile);
        bGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });

        // The Open Camera button opens the camera of the phone
        Button bPic = findViewById(R.id.getpic);
        imv = findViewById(R.id.image_view);
        bPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);
                /*Intent cam_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                cam_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cam_intent, CAMERA_REQUEST);*/
            }
        });

        if (checkPermission()) {
            //main logic or main code
            Log.d("bmw", "checks permissions if");
            // . write your main code to execute, It will execute if the permission is already given.

        } else {
            Log.d("bmw", "else request permission");
            requestPermission();
        }
    }

    public String getStringImage(Bitmap bm){
        ByteArrayOutputStream ba=new ByteArrayOutputStream(  );
        bm.compress( Bitmap.CompressFormat.PNG,90,ba );
        byte[] by=ba.toByteArray();
        String encod= Base64.encodeToString( by,Base64.DEFAULT );
        return encod;
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //String path = "sdcard/camera_app/cam_image.jpg";
        //imv.setImageDrawable(Drawable.createFromPath(path));
        Log.d("bmw",  "Post activity");
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        imv.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        imv.setImageURI(selectedImage);
                    }
                    break;
            }
        }
    }
}
