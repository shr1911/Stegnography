package com.example.stegnography;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class SuccessfulEncodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_encode);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
