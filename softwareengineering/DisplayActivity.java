package com.example.zhaoyong.softwareengineering;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This Class and User Interface displays the appointments retrieved from firebase in a list.
 * When long pressed on list item is detected, a dialog will be created
 * @author Edwin
 */
public class DisplayActivity extends AppCompatActivity {
    private ListView listViewAppoint;
    private DatabaseReference databaseref;
    private List<Appointment> Appoints;

    /**
     * Default initialisatio
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Bundle bundle = getIntent().getExtras();
        String showtext = bundle.getString("Vet Center Name");
        listViewAppoint=(ListView)findViewById(R.id.AppointList);
        databaseref = FirebaseDatabase.getInstance().getReference(showtext);
        Appoints = new ArrayList<>();
        listViewAppoint.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appointment appoint= Appoints.get(i);
                showDeleteCallDialog(appoint.getContact(), appoint.getClinic());
                return true;
            }
        });
    }

    /**
     * Retrieve appointments of clinic from firebase and store in list and displayed in real time.
     */
    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous appointment list
                Appoints.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting appointment
                    Log.v("E_MSG","Data : " +postSnapshot.getValue(Appointment.class));
                    Appointment appointment = postSnapshot.getValue(Appointment.class);
                    //adding appointment to the list
                    Appoints.add(appointment);
                }

                //creating adapter
                AppointmentList artistAdapter = new AppointmentList(DisplayActivity.this, Appoints);
                //attaching adapter to the listview
                listViewAppoint.setAdapter(artistAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /**
        *  Method to show pop up dialog with 2 button and a title indication the contact
         *  1)Delete button, which delete the appointment from list
         *  2)Call button, calls the phone number indicated in the title
        */
        }
        private void showDeleteCallDialog(final String contact, final String clinic) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.delete_call_dialog, null);
            dialogBuilder.setView(dialogView);

            final Button buttonCall = (Button) dialogView.findViewById(R.id.btnCall);
            final Button buttonDelete = (Button) dialogView.findViewById(R.id.btnDelete);

            dialogBuilder.setTitle(contact);
            final AlertDialog b = dialogBuilder.create();
            b.show();


            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteAppoint(contact,clinic);
                }
            });
            buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String call= String.format("tel: %s",contact);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(call));
                startActivity(intent);

            }
        });
    }

    /**
     * Backend deletion of appointment from firebase
     * @param contact / contact no
     * @param clinic / clinic name
     * @return
     */
    private boolean deleteAppoint(String contact, String clinic) {
        //getting the specified appointment reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(clinic).child(contact);

        //removing appointment
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Appointment Deleted", Toast.LENGTH_LONG).show();

        return true;
    }
}
