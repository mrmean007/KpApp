package com.example.mrmean.hakomataaa;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.view.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    String mCurrentPhotoPath;
    private Button cameraButton;
    private Button chooseButton;
    private ImageView imageview;
    private String path, encodedString, imageName;

    int CAPTURE_IMAGE = 2,
        CHOOSE_IMAGE = 1;
    
    GPSTracker gpsTracker;
    ConnectionDetector connectionDetector;
    private String UPLOAD_URL = "http://innovatepp.com/hakomata/uploadimage.php";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "imagename";
    private Spinner categarySpinner;
    private String comments = "no comments",
                    name = "no name";
    private EditText commentEt, nameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cameraButton = (Button)findViewById(R.id.cameraButton);
        chooseButton = (Button)findViewById(R.id.chooseButton);
        gpsTracker = new GPSTracker(this);
        connectionDetector = new ConnectionDetector(this);

    }

    public void chooseImage(View v){
//        Toast.makeText(MainActivity.this, "choose button is pressed", Toast.LENGTH_SHORT).show();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i,"Select Image"),CHOOSE_IMAGE);
    }

    public void viewMap(View v){

        startActivity(new Intent(MainActivity.this,MapsActivity.class));
    }

    public void takeImage(View v){

//        Toast.makeText(MainActivity.this,"working",Toast.LENGTH_SHORT).show();
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(camIntent.resolveActivity(getPackageManager()) != null){

            File photoFile = null;
            try {
                photoFile = new File("hello.jpg");//createImageFile();
            } catch (Exception ex) {
                // Error occurred while creating the File
                Log.e("file", "exception thrown in file creation " + ex.getMessage());
            }
            if(photoFile != null) {
//                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                if(gpsTracker.canGetLocation())
                startActivityForResult(camIntent, CAPTURE_IMAGE);
//            galleryAddPic();
            }else
                Log.e("photoFile","photofile is null");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.extra_layout, null);
        Button closeButton = (Button)v.findViewById(R.id.closeButton);
        categarySpinner = (Spinner)v.findViewById(R.id.spinner);
        ImageView extraImageView = (ImageView)v.findViewById(R.id.extralayoutImageView);
        nameEt = (EditText)v.findViewById(R.id.nameEt);
        commentEt = (EditText)v.findViewById(R.id.commentEt);
        final AlertDialog.Builder extraAlert = new AlertDialog.Builder(MainActivity.this);
        extraAlert.setTitle("Upload Image");
        extraAlert.setView(v);
        extraAlert.setCancelable(false);
        final AlertDialog alert = extraAlert.create();

        if(requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK ){
            Toast.makeText(MainActivity.this, "capture image activity ", Toast.LENGTH_SHORT).show();
            Bundle extra = data.getExtras();
            Bitmap imageTaken = (Bitmap)extra.get("data");

            path = saveImageToExternalStorage(imageTaken);

//            path = saveToInternalStorage(imageTaken);

            Bitmap bitmapimage = loadImageFromStorage(path);
            //imageview.setImageBitmap(bitmapimage);

            encodedString = encodeImage(bitmapimage);

            extraImageView.setImageBitmap(bitmapimage);


            alert.show();

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

        }
         else if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK){


            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmapimage = BitmapFactory.decodeFile(picturePath);

            Bitmap scaledImage = Bitmap.createScaledBitmap(bitmapimage, 200, 200, false);
            saveImageToExternalStorage(scaledImage);
            encodedString = encodeImage(scaledImage);


            extraImageView.setImageBitmap(scaledImage);

            alert.show();

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });


//            Toast.makeText(MainActivity.this,encodeImage(bitmapimage), Toast.LENGTH_SHORT).show();

        }
        if(data != null) {
        }
    }
    
    public void uploadButtonClicked(View v){

        String categaryStr = categarySpinner.getSelectedItem().toString();
        if(nameEt.getText().toString().length() != 0 )
            name = nameEt.getText().toString();
        if(commentEt.getText().toString().length() != 0)
            comments = commentEt.getText().toString();
        if(connectionDetector.isConnectingToInternet()){
            if(gpsTracker.canGetLocation()) {
                String latitude = "" + gpsTracker.getLatitude();
                String longitude = "" + gpsTracker.getLongitude();


                uploadImage(encodedString, categaryStr, latitude, longitude, name, comments);
            }else
                Toast.makeText(MainActivity.this, "enable your gps please", Toast.LENGTH_SHORT).show();
        }else
            Toast.makeText(MainActivity.this, "Internet is not connected", Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, "uploading has been started " + categaryStr, Toast.LENGTH_SHORT).show();
    }

    private String saveToInternalStorage(Bitmap bitmapImage){

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String path)
    {
        Bitmap b = null;
        try {

            File f=new File(path);//, "profile.jpg");
             b = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }

    private String saveImageToExternalStorage(Bitmap finalBitmap) {

        String path = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            path = file.getAbsolutePath();
            Toast.makeText(MainActivity.this,"image is saved to " + path,Toast.LENGTH_SHORT).show();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public String encodeImage(Bitmap bitmap){

        Log.i("imageEncoding","image encoding started");
        String encodedImage = "";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] goldenBytes = baos.toByteArray();
        encodedImage = Base64.encodeToString(goldenBytes,Base64.DEFAULT);
//        Log.i("base64", "encoded image" + encodedImage);
//        Toast.makeText(MainActivity.this, encodedImage, Toast.LENGTH_SHORT).show();
        Log.i("imageEncoding","image encoding stoped");
        return encodedImage;
    }



    private void uploadImage(final String encodedString, final String imagename, final String lat, final String lon, final String namestr, final String commentstr){
        //Showing the progress dialog

        final ProgressDialog loading = new ProgressDialog(MainActivity.this);
        loading.setMessage("your image is uploading");
        loading.setCancelable(false);
        loading.setTitle("Uploading");
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(MainActivity.this, "erro has been occured"/*volleyError.getMessage().toString()*/, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = encodedString;

                //Getting Image Name
                String name = imagename;

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);
                params.put("latitude",lat);
                params.put("longitude",lon);
                params.put("name",namestr);
                params.put("comment",commentstr);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
