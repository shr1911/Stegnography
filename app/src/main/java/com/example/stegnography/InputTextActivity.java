package com.example.stegnography;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputTextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_text);


        Button nextImageButton = (Button)findViewById(R.id.nextImageButton);

        nextImageButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        final EditText inputTextString = (EditText) findViewById(R.id.inputTextString);

                        String InputDataString = inputTextString.getText().toString();

                        Log.d("Input data string",InputDataString);

                        if(InputDataString.length() == 0 ) {
                            Log.d("Input data string","EMPTY STRING");
                            Toast.makeText(getApplicationContext(), "Enter message first", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Intent i = new Intent(v.getContext(), InputImageActivity.class);
                            i.putExtra("InputDataString", inputTextString.getText().toString());
                            startActivity(i);
                        }
                    }
                }
        );

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
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
