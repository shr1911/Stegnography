package com.example.stegnography;

import com.example.stegnography.DisplayImage;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class DecodingActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    Button selectImageButton;
    Button decodeTextButton;
    ImageView displaySelectedImage;
    Bitmap selectedBitmap = null;
    String decodedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoding);

        selectImageButton = (Button) findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, RESULT_LOAD_IMAGE);
                    }
                }
        );

        decodeTextButton = (Button)findViewById(R.id.decodeTextButton);
        decodeTextButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(selectedBitmap != null){
                            String decodedText = Alogorithm.decode(selectedBitmap);

                            Intent i = new Intent(view.getContext() , OutputTextActivity.class);
                            i.putExtra("OutputDataString", decodedText);

                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext() , "Please select image first !",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();  // get the uri that points to selected image
            //String[] imagePathColumn = {MediaStore.Images.Media.DATA};


            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                Log.d("height",selectedBitmap.getHeight()+"");
                Log.d("width",selectedBitmap.getWidth()+"");

                if(selectedBitmap != null) {
                    //selectedBitmap.recycle();
                    displaySelectedImage = (ImageView) findViewById(R.id.displaySelectedImage);
                    displaySelectedImage.setImageBitmap(selectedBitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }





            // Perform the query on the contact to get the NUMBER column
            // We don't need a selection or sort order (there's only one result for the given URI)
            // CAUTION: The query() method should be called from a separate thread to avoid blocking
            // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
            // Consider using CursorLoader to perform the query.


            /*
            Cursor cursor = getContentResolver().query(selectedImage, imagePathColumn, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(imagePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            Log.d("Path for image", picturePath);
            cursor.moveToFirst();
            cursor.close();

            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inPreferredConfig = Bitmap.Config.ARGB_8888;

            displaySelectedImage = (ImageView) findViewById(R.id.displaySelectedImage);
            if(displaySelectedImage != null){
                selectedBitmap = null;
            }

            selectedBitmap = DisplayImage.decodeSampledBitmapFromPathname(picturePath, 350, 350);
            selectedBitmap = BitmapFactory.decodeFile(picturePath);
            if(selectedBitmap != null)
                displaySelectedImage.setImageBitmap(selectedBitmap);
            */
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
        return true;
    }
}
