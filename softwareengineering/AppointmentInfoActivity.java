package com.example.zhaoyong.softwareengineering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The Class and User Interface where the User can key in the details for the Appointment in the
 * text boxes, with respective hints on what to key in. Subsequently, when the details and keyed,
 * User can click on the Make Appointment button to complete the action.
 * Once the Appointment is made the UI will jump back to Main.
 * @author Edwin
 */
public class AppointmentInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private String name;
    private String Adate;
    private String Atime;
    private String pet;
    private String notes;
    private String clinic;
    private String contact;

    /**
     * Default Initialisation
     * @param savedInstanceState / clinic variable is passed form previous Activity to this
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_info);
        Bundle bundle = getIntent().getExtras();
        clinic = bundle.getString("Cname");
        Button makeAppoint = (Button) findViewById(R.id.makeAppoint);
        makeAppoint.setOnClickListener(this);
    }

    /**
     * Create Appointment Objects based on keyed in details and send to Firebase database with contact as a key when Make Appointment Button is clicked
     * @param v /View of the current interface
     */
    @Override
    public void onClick(View v) {
        name = ((EditText)findViewById(R.id.name)).getText().toString();
        Adate = ((EditText)findViewById(R.id.date)).getText().toString();
        Atime = ((EditText)findViewById(R.id.time)).getText().toString();
        pet = ((EditText)findViewById(R.id.pet)).getText().toString();
        contact =((EditText)findViewById(R.id.contact)).getText().toString();
        notes = ((EditText)findViewById(R.id.notes)).getText().toString();
        Appointment Appoint = new Appointment(name, Adate,Atime,pet,contact,notes,clinic);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child(clinic).child(contact).setValue(Appoint);
        Toast.makeText(this, "Appointment Made",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(AppointmentInfoActivity.this, MainPageActivity.class);
        startActivity(intent);
    }
}
