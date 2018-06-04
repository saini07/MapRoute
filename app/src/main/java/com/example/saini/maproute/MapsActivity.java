package com.example.saini.maproute;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.saini.maproute.FirstActivity.user;
import static com.example.saini.maproute.UserActivity.driver;
import static com.example.saini.maproute.UserActivity.isUser;

public class MapsActivity extends NavigateActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentLocationMarker;
    public static String TAG = "bvshi";
    public static final int REQUEST_LOCATION_CODE = 999;
    String origin, dest;
    EditText loc1, loc2;
    Button search, history;
    List<Address> first = null,last = null;
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    public static final int REQUEST_LOCATION = 1;
    DatabaseReference databaseTracker;
    DatabaseReference databaseDriver;
    DatabaseReference databaseVehicle;
    public static int flag = 0;
    double latitude,longitude;
   // Map<VehicleInformation,DriverInformation> locator;
    private ArrayList<LatLng> points;
    Polyline line;
    BitmapDescriptor icon;
    String customerid;
    Map<String,Driver> driver_info;
    Map<String,ArrayList<LatLng>> driver_movement;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        FrameLayout layout=(FrameLayout) findViewById(R.id.layout);
        getLayoutInflater().inflate(R.layout.activity_maps,layout);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        databaseTracker = FirebaseDatabase.getInstance().getReference("track");
        databaseDriver = FirebaseDatabase.getInstance().getReference("Driver");
        // databaseVehicle = FirebaseDatabase.getInstance().getReference("VehicleInformation");

        if(user == 2) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.truck);
        }
        else {
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }

        // vehicle_list = new ArrayList<>();

       // locator = new HashMap<VehicleInformation, DriverInformation>();
        points = new ArrayList<LatLng>();
        driver_info = new HashMap<String, Driver>();
        driver_movement = new HashMap<String, ArrayList<LatLng>>();
        //verifyPermissions(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Toast.makeText(this,flag,Toast.LENGTH_LONG).show();


    }

    public void onClick(View view) {
        if (view.getId() == R.id.search) {
            // LocateVehicles();
            //PointLocators();
            Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();

            EditText loc1 = (EditText) findViewById(R.id.loc1);
            EditText loc2 = (EditText) findViewById(R.id.loc2);
            String origin = loc1.getText().toString();
            String dest = loc2.getText().toString();


            if (!TextUtils.isEmpty(origin) && !TextUtils.isEmpty(dest)) {

                String id= databaseTracker.push().getKey();
                Tracker tracker=new Tracker(id,origin,dest);
                databaseTracker.child(id).setValue(tracker);

                first = null; last = null;


                Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();

                Locate(origin,dest);
            }
            else {
                Toast.makeText(this,"Fill in the details",Toast.LENGTH_LONG).show();
                if(user!=2) {
                    // LocateVehicles();
                }

            }
        }

        else if(view.getId() == R.id.history) {
            Intent i = new Intent(this,History.class);
            startActivity(i);

        }
    }

    private void Locate(String origin,String dest) {   ///For directions

        Log.e(TAG,origin);
        Log.e(TAG,dest);
        first = null; last = null;
        // Toast.makeText(this,origin+dest,Toast.LENGTH_LONG).show();
        mMap.clear();
        Geocoder geocoder = new Geocoder(this);
        try {
            first = geocoder.getFromLocationName(origin, 1);
            last = geocoder.getFromLocationName(dest, 1);
            if(first != null)
            {

                LatLng latLng = new LatLng(first.get(0).getLatitude() , first.get(0).getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Origin is here : "+origin);
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            }
            if(last != null)
            {

                LatLng latLng = new LatLng(last.get(0).getLatitude() , last.get(0).getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Destination is here :"+dest);
                mMap.addMarker(markerOptions);
                // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                // mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

            }

            if (first != null && last != null) {
                Object dataTransfer[] = new Object[2];
                String url = getDirectionsUrl();
                Log.e(TAG,url);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                GetDirectionsData getDirectionsData = new GetDirectionsData();
                getDirectionsData.execute(dataTransfer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }  //For directions


  /*  private void LocateVehicles() {
        if(locator!=null) locator.clear();
        mMap.clear();



        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You are here");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        Log.e(TAG,"Adding initial marker");
        currentLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        databaseVehicle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //vehicle_list.clear();
                for(DataSnapshot vehicle: dataSnapshot.getChildren()) {
                    final VehicleInformation vehicleInformation = vehicle.getValue(VehicleInformation.class);
                    // final VehicleInformation temp_vehicle = vehicleInformation;
                    Log.e(TAG,"in vehicle data");
                    float[] results = new float[10];
                    Location.distanceBetween(latitude,longitude,vehicleInformation.getLatitude(),vehicleInformation.getLongitude(),results);
                    float distance = (float) (results[0]/1000*1.0);

                    databaseDriver.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot driver : dataSnapshot.getChildren()) {
                                Log.e(TAG,"in driver data");
                                // Toast.makeText(getApplicationContext(),"In driver data added",Toast.LENGTH_LONG).show();
                                DriverInformation driverInformation = driver.getValue(DriverInformation.class);
                                if(driverInformation.getId() == vehicleInformation.getId()) {
                                    locator.put(vehicleInformation,driverInformation);
                                    Log.e(TAG,"in map data added");
                                    //  Toast.makeText(getApplicationContext(),"In map data added ",Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                            PointLocators();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    // if(distance < 50.0) {
                    Log.e(TAG,"in vehicle data added"+vehicleInformation.getVehicle_Type()+vehicleInformation.getVehicle_No());
                    //  Toast.makeText(getApplicationContext(),"in vehicle data added",Toast.LENGTH_LONG).show();
                    //------vehicle_list.add(vehicleInformation);
                    //}
                    //retrievalList.add(tracker);

                }
                flag=1;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //while(flag==0) {}


    }

    private void PointLocators() {
        Log.e(TAG,"fjghg");
        if(locator!=null) {
            Log.e(TAG,"khvk "+locator.size());
            for (Map.Entry<VehicleInformation, DriverInformation> entry : locator.entrySet()) {

                Log.e(TAG,entry.getKey().getVehicle_Type());
                Log.e(TAG,entry.getValue().getDriver_name());
            }
            for (Map.Entry<VehicleInformation, DriverInformation> entry : locator.entrySet()) {
                VehicleInformation vehicleInformation = entry.getKey();
                DriverInformation driverInformation = entry.getValue();
                Log.e(TAG,Long.toString(vehicleInformation.getId()));
                Log.e(TAG,Long.toString(driverInformation.getId()));
                // Toast.makeText(getApplicationContext(),Long.toString(driverInformation.getId())+Long.toString(vehicleInformation.getId()),Toast.LENGTH_LONG).show();
                LatLng latLng = new LatLng(vehicleInformation.getLatitude(),vehicleInformation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                if (vehicleInformation.getVehicle_Type().equals("Truck")) {
                    markerOptions.title("Truck");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.truck));
                    markerOptions.snippet("Driver " + driverInformation.getDriver_name()+"Phone Number" + driverInformation.getPhone()+"Vehicle Number" + vehicleInformation.getVehicle_No());

                } else if (vehicleInformation.getVehicle_Type().equals("DCM")) {
                    markerOptions.title("DCM");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.truck));
                    markerOptions.snippet("Driver " + driverInformation.getDriver_name()+"Phone Number" + driverInformation.getPhone()+"Vehicle Number" + vehicleInformation.getVehicle_No());


                } else if (vehicleInformation.getVehicle_Type().equals("MiniTruck")) {
                    markerOptions.title("MiniTruck");
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.mini_truck));
                    markerOptions.snippet("Driver " + driverInformation.getDriver_name()+"Phone Number" + driverInformation.getPhone()+"Vehicle Number" + vehicleInformation.getVehicle_No());


                }
                mMap.addMarker(markerOptions);
            }
        }
    }
*/

    private String getDirectionsUrl() {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("origin=" + first.get(0).getLatitude() + "," + first.get(0).getLongitude());
        googleDirectionsUrl.append("&destination=" + last.get(0).getLatitude() + "," + last.get(0).getLongitude());
        googleDirectionsUrl.append("&key=AIzaSyDAIse9T-3Ksseg25CEgozVsn0pk6-CBI0");
        //AIzaSyBVwa9BGUeHg46NVfy3l65wahffhO6ufds
        Log.e(TAG,googleDirectionsUrl.toString());
        return googleDirectionsUrl.toString();
    } //Get Directions from url

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED) {
                        if(client==null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else {
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_LONG).show();
                }
                return;
        }
    } //Permissions


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {


            buildGoogleApiClient();
            Log.e(TAG, "about to add location");
            mMap.setMyLocationEnabled(true);
            // LocateVehicles();
            if (flag == 1) {
                flag = 0;
                String origin = getIntent().getStringExtra("origin");
                String dest = getIntent().getStringExtra("dest");
                Log.e(TAG, "Function called");
                Toast.makeText(this, "Function called", Toast.LENGTH_LONG).show();
                Locate(origin, dest);
            }


            if (user != 2) {
                databaseDriver.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot drives : dataSnapshot.getChildren()) {
                            Driver temp_driver = drives.getValue(Driver.class);
                            driver_info.put(temp_driver.getId(), temp_driver);
                            ArrayList<LatLng> list = new ArrayList<>();
                            list.add(new LatLng(temp_driver.getLatitude(), temp_driver.getLongitude()));
                            driver_movement.put(temp_driver.getId(), list);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Log.e(TAG, "initials added");

            }
        }

    }

    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        client.connect();

    } //apiclient
    @Override
    public void onLocationChanged(Location location) {

        lastLocation = location;
        if(currentLocationMarker!=null) {
            currentLocationMarker.remove();
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
/*testing
        if(user==2) {

            databaseDriver.child(customerid).child("latitude").setValue(location.getLatitude());
            databaseDriver.child(customerid).child("longitude").setValue(location.getLongitude());
        }*/
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        points.add(latLng);
        if(user!=2) {
            Log.e(TAG," here in retrieving the initials");
            databaseDriver.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    String id = dataSnapshot.getKey();
                    Log.e(TAG,"sc "+id);
                    Driver temp_driver = dataSnapshot.getValue(Driver.class);
                    if (driver_info.containsKey(id)) {
                        ArrayList<LatLng> list = driver_movement.get(id);
                        list.add(new LatLng(temp_driver.getLatitude(), temp_driver.getLongitude()));
                        driver_movement.put(id, list);
                    } else {
                        driver_info.put(id, temp_driver);
                        ArrayList<LatLng> list = new ArrayList<>();
                        list.add(new LatLng(temp_driver.getLatitude(), temp_driver.getLongitude()));
                        driver_movement.put(id, list);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        redrawLine();
       /* MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You are here");
        markerOptions.icon(icon);
        Log.e(TAG,"Adding initial marker");
        currentLocationMarker = mMap.addMarker(markerOptions);*/
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        // mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

       /* if(client!=null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }*/
    }


    private  void redrawLine() {
        mMap.clear();
        Log.e(TAG,"map cleared before redrawing lines");
        PolylineOptions options = new PolylineOptions().width(5).geodesic(true);
        for(int i = 0;i<points.size();i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
        LatLng latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("You are here");
        markerOptions.icon(icon);
        Log.e(TAG,"Adding redrawing marker");
        mMap.addMarker(markerOptions);

        if(user!=2) {
            Log.e(TAG, "driver Movement"+driver_movement.size());
            for (Map.Entry<String, ArrayList<LatLng>> entry : driver_movement.entrySet()) {
                String id = entry.getKey();
                Driver temp_driver = driver_info.get("id");
                double t_latitude = 0.0, t_longitude = 0.0;
                PolylineOptions temp_options = new PolylineOptions().width(5).geodesic(true);
                ArrayList<LatLng> temp_points = driver_movement.get(id);
                for (int i = 0; i < temp_points.size(); i++) {
                    LatLng point = temp_points.get(i);
                    temp_options.add(point);
                    t_latitude = point.latitude;
                    t_longitude = point.longitude;
                }
                latLng = new LatLng(t_latitude, t_longitude);
                markerOptions.position(latLng);
                BitmapDescriptor temp_icon;
                temp_icon = BitmapDescriptorFactory.fromResource(R.drawable.truck);
                if(temp_driver!=null) {
                    Log.e(TAG, " info " + temp_driver.getDriver_name() + temp_driver.getVehicle_type());
                    markerOptions.title(temp_driver.getDriver_name());
                    markerOptions.snippet(temp_driver.getVehicle_type() + " " + temp_driver.getVehicle_no() + " " + temp_driver.getPhone_no());

                    if (temp_driver.getVehicle_type() == "Truck") {
                        temp_icon = BitmapDescriptorFactory.fromResource(R.drawable.truck);
                    } else {
                        temp_icon = BitmapDescriptorFactory.fromResource(R.drawable.mini_truck);
                    }
                }
                markerOptions.icon(temp_icon);
                mMap.addMarker(markerOptions);
            }
        }
        //line = mMap.addPolyline(options);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location loc = LocationServices.FusedLocationApi.getLastLocation(client);
            if(loc!=null) {
                LatLng latLng = new LatLng(loc.getLatitude(),loc.getLongitude());
                MarkerOptions mark = new MarkerOptions();
                mark.position(latLng);
                mark.title("You are here");
                mark.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                currentLocationMarker = mMap.addMarker(mark);
            }

        }
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, (LocationListener) this);
        }
    }

    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);
            }
            else{
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION_CODE);

            }
            return false;
        }
        else
            return true;
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public static void verifyPermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION
            );
        }
    }
}
