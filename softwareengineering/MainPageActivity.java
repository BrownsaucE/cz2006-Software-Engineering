package com.example.zhaoyong.softwareengineering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Class and User Interface which consist of 2 button which will navigate to different pages.
 * 1)Vet Center, navigate to the login page for vet center
 * 2)Pet Owner, navigate to the Main page for pet owner
 */
public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        initPetOwner();
        initVetCenter();
    }

    /**
     * function to jump to LoginIn activity
     */
    private void initVetCenter() {
        Button btnMap = (Button) findViewById(R.id.btnVet);
        btnMap.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, LoginInActivity.class);
                startActivity(intent);
            }
        }));
    }

    /**
     * function to jump to Main activity
     */
    private void initPetOwner() {
        Button btnMap = (Button) findViewById(R.id.btnOwner);
        btnMap.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }));
    }
}
