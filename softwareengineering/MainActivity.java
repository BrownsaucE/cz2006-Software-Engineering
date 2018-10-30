package com.example.zhaoyong.softwareengineering;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * The Class and User Interface for the user to Select the desired choice of feature
 * contain 2 buttons for the user to select
 * 1) Display all nearby clinic around the user using google map
 * 2) Display out a list of clinic information
 */
public class MainActivity extends AppCompatActivity {
    private static final  String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    /**
     * Default Initialisation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isServiceOK()){
            init();
            init_List();
        }
    }

    /**
     * init()
     * Go to the MapActivity when the button is pressed
     * to view the clinic and user position on map
     */
    private void init(){
        Button btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        }));
    }

    /**
     * init_List()
     * Go to the ListActivity when the button is pressed
     * to view the clinic information on a list box
     */
    public void init_List(){
        Button btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * isServiceOK() Check the Google Play services, if the map is installed or not installed.
     * Check the Version of the Phone if it supports the Application
     * @return
     */
    public boolean isServiceOK(){
        Log.d(TAG, "isServiceOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServiceOK: Google play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG, "isServiceOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else {
            Toast.makeText(this, "you cant make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
