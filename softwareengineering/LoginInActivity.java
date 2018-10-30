package com.example.zhaoyong.softwareengineering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


/**
 * A login screen that offers login via vet center name&password.
 * @author Edwin
 */
public class LoginInActivity extends AppCompatActivity {
    Button signin;
    EditText clinicnametxt, passwordtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        signin = (Button) findViewById(R.id.email_sign_in_button);
        clinicnametxt = (EditText) findViewById(R.id.clinicName);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mytext =clinicnametxt.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("Vet Center Name",mytext);
                Intent intent = new Intent(LoginInActivity.this,DisplayActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }

}

