package com.example.zhaoyong.softwareengineering;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * The Class is display a list layout of appointment details
 * @author Edwin
 */

public class AppointmentList extends ArrayAdapter<Appointment> {
    private Activity context;
    List<Appointment> Appoints;

    /**
     * Constructor of AppointmentList
     * @param context
     * @param Appoints /List of appointment object
     */
    public AppointmentList(Activity context, List<Appointment> Appoints) {
        super(context, R.layout.layout_appoint_list, Appoints);
        this.context = context;
        this.Appoints = Appoints;
    }

    /**
     * Insert details of the appointment into text view to be display as a single item
     * @param position /list item position
     * @param convertView / layout of UI
     * @param parent
     * @return
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_appoint_list, null, true);

        TextView textViewContact = (TextView) listViewItem.findViewById(R.id.textViewContact);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textViewTime);
        TextView textViewDate = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView textViewClinic = (TextView) listViewItem.findViewById(R.id.textViewClinic);
        TextView textViewPet    = (TextView) listViewItem.findViewById(R.id.textViewPet);
        TextView textViewNotes = (TextView) listViewItem.findViewById(R.id.textViewNotes);


        Appointment Appoint = Appoints.get(position);
        textViewContact.setText(Appoint.getContact());
        textViewName.setText(Appoint.getAname());
        textViewTime.setText(Appoint.getAtime());
        textViewDate.setText(Appoint.getAdate());
        textViewClinic.setText(Appoint.getClinic());
        textViewPet.setText(Appoint.getPet());
        textViewNotes.setText(Appoint.getNotes());
        Log.v("MSG", "String"+Appoint.getAname());

        return listViewItem;
    }
}
