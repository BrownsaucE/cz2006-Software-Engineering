package com.example.zhaoyong.softwareengineering;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * This Class Get Information from the Source API and and plots them as marker on the map
 * It gets the Current location of user
 * It gets the Clinic information and pass them to a Clinic object
 * Plots the Clinic and User Current Location on the Map as Marker
 * Display out the Clinic Information on the Marker
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static  final float DEFAULT_ZOOM = 15f; // Default Zoom in on the Map
    private static final String TAG = "MapActivity"; //Use to Check the Log of the android application
    private boolean mLocationPermissionGranted = false; // Default State of Location Permission is false.
    private GoogleMap mMap; //Google Map as Map
    private FusedLocationProviderClient mFusedLocationProviderClient; //Location Service Client to get CurrentLocation
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION; // String of Fine location
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION; //String of Coarse location

    private RequestQueue mQueue; // Queue for JSON Oject
    private Clinic[] CInfo = new Clinic[100]; // Array of Clinic Objects

    //newCode
    private ViewGroup infoWindow; // Create a Info Window
    private TextView infoTitle; // Display Clinic name on Modified info Window
    private TextView infoSnippet; // Display Clinic Information on Modified info Window
    private Button infoButton1; // Button for Book Appointment
    private OnInfoWindowElemTouchListener infoButtonListener; //Modified GoogleMap marker info Window

    /**
     * On Creation of the Map, Create a new Modified Infomation window for the Marker
     * get the location permission from the user
     * call the getJSON to retrieve clinic information from the API
     * Create a new empty map
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mQueue = Volley.newRequestQueue(this);
        final SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map); //Create a map on the interface
        mapFragment.getMapAsync(this);// SYNC the map
        getLocationPermission();// get Location Permission
        getJSON();// get JSON from API
    }

    /**
     * Start the Map
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();// DIsplay out map is ready when the map is ready
        mMap = googleMap;// new google map

        if(mLocationPermissionGranted){
            getDeviceLocation();// get current location
        }

        //newCode
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(mMap, getPixelsFromDp(this, 39 + 20));

        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.custom_infowindow, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.nameTxt);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.addressTxt);
        this.infoButton1 = (Button)infoWindow.findViewById(R.id.btnOne);
        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up

        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton1, getResources().getDrawable(R.drawable.rectangle), getResources().getDrawable(R.drawable.rectangle)){
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // perform some action triggered after clicking the button
                Bundle bundle = new Bundle();
                bundle.putString("Cname",marker.getTitle());
                Log.v("E_MSG","Clinic Name : "+ marker.getTitle());
                Intent intent = new Intent(MapActivity.this,AppointmentInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(MapActivity.this, "click on button 1", Toast.LENGTH_SHORT).show();
            }
        };
        this.infoButton1.setOnTouchListener(infoButtonListener);
        /*infoWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "click on infowindow", Toast.LENGTH_LONG).show();
            }
        });*/

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoSnippet.setText(marker.getSnippet());
                infoTitle.setText(marker.getTitle());
                infoButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });


    }

    /**
     * This Method Gets the Current Device Location and move the camera to the user current location on Maps
     */
    private void getDeviceLocation(){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), DEFAULT_ZOOM);
                        }else{
                            Log.d(TAG, "onComplete: unable to find location");
                            Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch( SecurityException e){
            Log.d(TAG, "getDeviceLocation: Security Exception: "+ e.getMessage());
        }
    }
    private void initMap(){

    }

    /**
     * Move the Camera to the the user Current Location
     * @param latLng
     * @param zoom
     */
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving camera");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        MarkerOptions Coptions = new MarkerOptions().position(latLng).title("Current Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(Coptions);
    }

    /**
     * Get Location Permission from the Android Phone
     * Prompt the user to allow location access if permission isnt granted
     * initialise the Map when there is location service
     */
    private void getLocationPermission() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                initMap();
                mLocationPermissionGranted = true;
            }else{
                ActivityCompat.requestPermissions(this, permission, 1234);
            }
        }else{
            ActivityCompat.requestPermissions(this, permission, 1234);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      mLocationPermissionGranted= false;
      switch (requestCode){
          case 1234:{
              if(grantResults.length > 0){
                  for(int i = 0; i <grantResults.length;i++){
                      if(grantResults[i] !=  PackageManager.PERMISSION_GRANTED){
                          mLocationPermissionGranted = false;
                          return;
                      }
                  }
                  mLocationPermissionGranted = true;
                  initMap();
              }
          }
      }
    }

    /**
     * The Method Get the Clinic Information from the API
     * Get A JSON Object and JSON Array from the API
     * Retrieve the information and pass it to the Clinic Class to create a Clinic Object
     * Call the method getLocationFromAddress() to convert address to long and lat
     * Plot the Clinic Object on Google Map
     */
    private void getJSON(){
        String url = "https://data.gov.sg/api/action/datastore_search?resource_id=b2871270-4eef-44a3-be98-908e2a73b19f";
        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) { //Get JSON object Request from API
                try {
                    JSONObject JO = response.getJSONObject("result");//Create A JSON Object to get the JSON object from the API
                    JSONArray JA = JO.getJSONArray("records");//Create A JSON array to get hold of all the clinic information in the API
                    for(int i = 0; i <JA.length(); i++){    //Loop the JA array and create a marker and a clinic object
                        JSONObject rec = JA.getJSONObject(i);
                        String cname = rec.getString("name"); // Information from API: Clinic Name
                        String cfax = rec.getString("fax_office");// Information from API: Clinic Fax
                        String cpostal = rec.getString("postal_code");// Information from API: Clinic Postal Code
                        String cAdd = rec.getString("address");// Information from API: Clinic Address
                        String cId =  rec.getString("_id");// Information from API: Clinic ID
                        String Ctype = rec.getString("type");// Information from API: Clinic type
                        String Ccontact = rec.getString("tel_office_1");// Information from API: Clinic Telephone Number
                        CInfo[i] = new Clinic(cname, cfax, cpostal, cAdd, cId, Ccontact, Ctype);//create a new Clinic Object
                        String Combine = "Address:  " + CInfo[i].getClinicAddress() + "\n" + "Tel No: " +CInfo[i].getClinicContact() + "\n" + "Postal Code: "+CInfo[i].getClinicPostal();//Combine all the information to a Single String
                        getLocationFromAddress(CInfo[i].getClinicname(), Combine);// Call Get Location to convert Address to Lat Long to plot on Map
                        Log.d(TAG, "onResponse: " + CInfo[i].cname);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    /**
     * Get the longitude and Latitude of the Clinic Object
     * Since the API does not provide the Longitude and Latitude of the Clinic, a Conversion from address to long, lat is needed
     * Converts Address of the clinic to long and lat
     * Place a Marker on Map using the long and lat and a created a infowindow for each marker with the clinic information
     * @param strAddress
     * @param ContactNumber
     */
    public void getLocationFromAddress(String strAddress, String ContactNumber){
        Geocoder geoCoder = new Geocoder(this); // coder to convert address to Lat Long.
        try {
            List<Address> addressList = geoCoder.getFromLocationName(strAddress, 1);
            if (addressList != null && addressList.size() > 0) {
                double lat = addressList.get(0).getLatitude(); //get Latitude from the given address
                Log.d(TAG, "getLocationFromAddress: " + lat);
                double lng = addressList.get(0).getLongitude();//get Longitude from the given address
                Log.d(TAG, "getLocationFromAddress: "+ lng);
                LatLng Markthis = new LatLng(lat, lng);// Combine the Long and Lat into a Single Variable
                Log.d(TAG, "getLocationFromAddress: " + Markthis);
                MarkerOptions Coptions = new MarkerOptions().position(Markthis).title(strAddress).snippet(ContactNumber).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mMap.addMarker(Coptions);// add a marker on google map using the Lat Long.
            }
        } catch (Exception e) {
            e.printStackTrace();
        } // end catch
    }

    /**
     * Setting of the Modified Infomation Window on Google Map
     * @param context
     * @param dp
     * @return
     */
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
}
