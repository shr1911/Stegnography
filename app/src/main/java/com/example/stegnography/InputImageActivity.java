package com.example.stegnography;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputImageActivity extends AppCompatActivity {

    Button browseImageButton;
    ImageView displayImage;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap originalBitmap;
    Bitmap bitmap = null;
    Bitmap encodedBitmap = null;
    String InputDataString;
    FileOutputStream output;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_image);


        final Bundle DataString = getIntent().getExtras();
        if(DataString==null){
            return;
        }
        InputDataString = DataString.getString("InputDataString");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        browseImageButton = (Button) findViewById(R.id.browseImageButton);
        browseImageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //PackageManager pm = getPackageManager();
                        //pm.clearPackagePreferredActivities();

                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                }
        );

        Button hideTextButton = (Button) findViewById(R.id.hideTextButton);
        Button captureImageButton = (Button) findViewById(R.id.captureImageButton);

        hideTextButton.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View v){
                        if(bitmap != null){
                            encodedBitmap = Alogorithm.encode(bitmap, InputDataString);

                            String text = Alogorithm.decode(encodedBitmap);
                            Log.d("before Please God ",text);
                            Log.d("Please God ",text);
                            Log.d("after Please God ",text);
                            Log.d("After toast", "asdasd");

                            //MediaStore.Images.Media.insertImage(getContentResolver(), encodedBitmap, "wallpaper.png", "");

                            File filepath = Environment.getExternalStorageDirectory();

                            // Create a new folder in SD Card
                            File dir = new File(filepath.getAbsolutePath()+ "/Stegno/");
                            dir.mkdirs();

                            // Create a name for the saved image
                            Date date = new Date();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-h:mm:ss-a");
                            String formattedDate = sdf.format(date);
                            File file = new File(dir, "Wallpaper"+formattedDate+".png" );
                            output=null;
                            try {
                                Log.d("PATH", file.getAbsolutePath().toString());
                                output = new FileOutputStream(file, true);

                                // Compress into png format image from 0% - 100%
                                encodedBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
                                output.flush();
                                output.close();
                                addImageToGallery(file.getAbsolutePath(), InputImageActivity.this);

                            }
                            catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            Intent i = new Intent(v.getContext() , SuccessfulEncodeActivity.class);
                            startActivity(i);
                        }
                        else {
                            Toast.makeText(getApplicationContext() , "Please select image first !", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );


        captureImageButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
        );



    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            //Bitmap temp = bitmap;
            //bitmap.recycle();
            bitmap = bitmap.copy(bitmap.getConfig(), true);

            displayImage = (ImageView) findViewById(R.id.displayImage);
            displayImage.setImageBitmap(bitmap);

        }

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();  // get the uri that points to selected image
            String[] imagePathColumn = {MediaStore.Images.Media.DATA};

            // Perform the query on the contact to get the NUMBER column
            // We don't need a selection or sort order (there's only one result for the given URI)
            // CAUTION: The query() method should be called from a separate thread to avoid blocking
            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
            // Consider using CursorLoader to perform the query.

            Cursor cursor = getContentResolver().query(selectedImage, imagePathColumn, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(imagePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            Log.d("Path for image", picturePath);
            cursor.moveToFirst();
            cursor.close();

            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inPreferredConfig = Bitmap.Config.ARGB_8888;

            //if(originalBitmap != null)
            //    originalBitmap.recycle();
            //originalBitmap = BitmapFactory.decodeFile(picturePath, option);

            displayImage = (ImageView) findViewById(R.id.displayImage);
            bitmap = DisplayImage.decodeSampledBitmapFromPathname(picturePath, 350, 350);
            displayImage.setImageBitmap(bitmap);

        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_name) {
            Intent i = new Intent(getApplicationContext(), WecomeActivity.class);
            i.putExtra("check","true");
            startActivity(i);

            Log.d("I","In fb button");
            return true;

        }
        else{
            Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivityForResult(myIntent, 0);
            Log.d("I","In back button");
            return true;
        }
    }
}
