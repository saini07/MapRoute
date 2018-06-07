package com.example.saini.maproute;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
//import static com.example.saini.maproute.UserActivity.driver;
import static com.example.saini.maproute.UserActivity.isUser;

public class MapsActivity extends NavigateActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
        //, LocationListener {

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
    public static Driver driver;
    public static Customer customer;
    DatabaseReference databaseTracker;
    DatabaseReference databaseDriver;
    DatabaseReference databaseCustomer;
    DatabaseReference databaseOrder;
    public static int flag = 0;
    ArrayList<String> check_order_customer ;
    ArrayList<String> check_order_driver ;
    double latitude,longitude;
   // Map<VehicleInformation,DriverInformation> locator;
    private ArrayList<LatLng> points;
    Polyline line;
    BitmapDescriptor icon;
    String customerid;
    Map<String,Driver> driver_info;
    Map<String,ArrayList<LatLng>> driver_movement;
    Map<String,Customer> customer_info;
    Map<String,ArrayList<LatLng>> customer_movement;
    public static int view_map = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);
        FrameLayout layout=(FrameLayout) findViewById(R.id.layout);
        getLayoutInflater().inflate(R.layout.activity_maps,layout);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager lm = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(!gps_enabled&&!network_enabled) {
            Toast.makeText(getApplicationContext(),"Provide GPS location ",Toast.LENGTH_LONG).show();
            finish();
        }
        else{

        }

        startService();
        Log.e(TAG,"n create");
        databaseTracker = FirebaseDatabase.getInstance().getReference("track");
        databaseDriver = FirebaseDatabase.getInstance().getReference("Driver");
        databaseCustomer = FirebaseDatabase.getInstance().getReference("Customer");
        databaseOrder = FirebaseDatabase.getInstance().getReference("order");

        check_order_customer = new ArrayList<String>();
        check_order_driver = new ArrayList<String>();
       // Toast.makeText(getApplicationContext()," user "+user,Toast.LENGTH_LONG).show();


        if(user == 2) {
            icon = BitmapDescriptorFactory.fromResource(R.drawable.truck);
        }
        else if (user==1){
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }
        else {
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        }



        // vehicle_list = new ArrayList<>();

       // locator = new HashMap<VehicleInformation, DriverInformation>();



        points = new ArrayList<LatLng>();

        driver_info = new HashMap<String, Driver>();
        driver_movement = new HashMap<String, ArrayList<LatLng>>();

        customer_info = new HashMap<String, Customer>();
        customer_movement = new HashMap<String, ArrayList<LatLng>>();

        loc1 = (EditText) findViewById(R.id.loc1);
        loc2 = (EditText) findViewById(R.id.loc2);
        search = (Button) findViewById(R.id.search);
        history = (Button) findViewById(R.id.history);


        if(view_map==1){

            loc1.setVisibility(View.GONE);
            loc2.setVisibility(View.GONE);
            search.setVisibility(View.GONE);
            history.setVisibility(View.GONE);
        }
        else if(view_map == 0) {
            loc1.setVisibility(View.VISIBLE);
            loc2.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            history.setVisibility(View.VISIBLE);
        }

        Log.e(TAG," sekvsb "+view_map);
        init();


        //verifyPermissions(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        //Toast.makeText(this,flag,Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MyService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, MyService.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, MyService.class));

    }

    private void startService() {
        startService(new Intent(this, MyService.class));
       // finish();
    }

    private void init() {

        Log.e(TAG," sekbhi init ");

            databaseDriver.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot drives : dataSnapshot.getChildren()) {
                        Driver temp_driver = drives.getValue(Driver.class);
                        driver_info.put(temp_driver.getId(), temp_driver);
                        ArrayList<LatLng> list = new ArrayList<>();
                        list.add(new LatLng(temp_driver.getLatitude(), temp_driver.getLongitude()));
                        driver_movement.put(temp_driver.getId(), list);
                        Log.e(TAG,"retrieving driver init");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.e(TAG, "initials added");





            databaseCustomer.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot customers : dataSnapshot.getChildren()) {
                        Customer temp_customer = customers.getValue(Customer.class);
                        Log.e(TAG,temp_customer.getId());
                        customer_info.put(temp_customer.getId(), temp_customer);
                        ArrayList<LatLng> list = new ArrayList<>();
                        list.add(new LatLng(temp_customer.getLatitude(), temp_customer.getLongitude()));
                        customer_movement.put(temp_customer.getId(), list);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Log.e(TAG, "initials added");


    }

    public void onClick(View view) {
        if (view.getId() == R.id.search) {
            // LocateVehicles();
            //PointLocators();
           //Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();


            String origin = loc1.getText().toString();
            String dest = loc2.getText().toString();


            if (!TextUtils.isEmpty(origin) && !TextUtils.isEmpty(dest)) {

                String id= databaseTracker.push().getKey();
                Tracker tracker=new Tracker(id,origin,dest);
                databaseTracker.child(id).setValue(tracker);

                first = null; last = null;


               // Toast.makeText(this,"Added",Toast.LENGTH_SHORT).show();

                Locate(origin,dest);
            }
            else {
                Toast.makeText(this,"Fill in the details",Toast.LENGTH_LONG).show();

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
                            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
                            intent.putExtra("enabled", true);
                            sendBroadcast(intent);

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
                //Toast.makeText(this, "Function called", Toast.LENGTH_LONG).show();
                Locate(origin, dest);
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


    private void requestLocationUpdates() {
        // Functionality coming next step

        LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);








        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        //final String path = getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {

                    Log.e(TAG,"request location updates");
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                           // mMap.clear();
                        lastLocation = location;
                        if(currentLocationMarker!=null) {
                            currentLocationMarker.remove();
                        }
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            //Toast.makeText(getApplicationContext()," "+location.getLatitude()+" "+location.getLongitude(),Toast.LENGTH_LONG).show();
                            MarkerOptions mark = new MarkerOptions();
                            mark.position(latLng);
                            mark.title("You are here");
                            mark.icon(icon);
                            currentLocationMarker = mMap.addMarker(mark);
                            points.add(latLng);

                        Log.e(TAG, "location update " + location);


                        //redrawLine();



                    }
                    if(view_map == 1) {
                        check();
                    }


                }
            }, null);
        }
    }


    private void check() {

        //Toast.makeText(getApplicationContext(),"user check "+user,Toast.LENGTH_LONG).show();
        Log.e(TAG, "user check "+user);
        if(user==0) {
           adminPlots();
        }

        if(user==1) {

            check_order_customer.clear();
            checkOrdersCustomer();



        }

        if(user==2) {
            Log.e(TAG,"list cleared");

            Log.e(TAG,"driver login "+driver.getId());
            check_order_driver.clear();
            checkOrdersDriver();




        }


    }

    private void checkOrdersCustomer() {



           databaseOrder.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {


                   for(DataSnapshot orders : dataSnapshot.getChildren()) {
                       Order temp_order = orders.getValue(Order.class);
                       if((temp_order.getStatus().equals("processing"))&&temp_order.getAccepted().equals("forwarded")&&temp_order.getCustomer().getId().equals(customer.getId())) {
                           check_order_customer.add(temp_order.getDriver().getId());
                       }
                   }
                   adminPlots();
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });


    }

    private void checkOrdersDriver() {
        Log.e(TAG,"check orders driver");

        databaseOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    //check_order_driver.clear();
                for(DataSnapshot orders : dataSnapshot.getChildren()) {
                    Order temp_order = orders.getValue(Order.class);
                    if((temp_order.getStatus().equals("processing"))&&temp_order.getAccepted().equals("forwarded")&&temp_order.getDriver().getId().equals(driver.getId())) {
                        check_order_driver.add(temp_order.getCustomer().getId());
                    }
                }
                adminPlots();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    private void adminPlots () {
        Log.e(TAG," here in retrieving the initials driver ");
        Log.e(TAG," checkOrderDriver"+check_order_driver.size());
        databaseDriver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot drivers : dataSnapshot.getChildren()) {

                    //Log.e(TAG, "sc " + id);
                    Driver temp_driver = drivers.getValue(Driver.class);
                    String id = temp_driver.getId();
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
                redrawLine();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.e(TAG," here in retrieving the initials customer");

        databaseCustomer.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot customers : dataSnapshot.getChildren()) {
                    Customer temp_customer = dataSnapshot.getValue(Customer.class);
                    String id = temp_customer.getId();
                    if (customer_info.containsKey(id)) {
                        ArrayList<LatLng> list = customer_movement.get(id);
                        list.add(new LatLng(temp_customer.getLatitude(), temp_customer.getLongitude()));
                        customer_movement.put(id, list);
                    } else {
                        customer_info.put(id, temp_customer);
                        ArrayList<LatLng> list = new ArrayList<>();
                        list.add(new LatLng(temp_customer.getLatitude(), temp_customer.getLongitude()));
                        customer_movement.put(id, list);
                    }
                }
                redrawLine();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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



        if(user == 0) {
            Log.e(TAG, "driver Movement" + driver_movement.size());
            for (Map.Entry<String, ArrayList<LatLng>> entry : driver_movement.entrySet()) {
                String id = entry.getKey();
                Driver temp_driver = driver_info.get(id);
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
                if (temp_driver != null) {
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


            Log.e(TAG, "customer Movement" + customer_movement.size());
            for (Map.Entry<String, ArrayList<LatLng>> entry : customer_movement.entrySet()) {
                String id = entry.getKey();
                Customer temp_customer = customer_info.get(id);
                double t_latitude = 0.0, t_longitude = 0.0;
                PolylineOptions temp_options = new PolylineOptions().width(5).geodesic(true);
                ArrayList<LatLng> temp_points = customer_movement.get(id);
                for (int i = 0; i < temp_points.size(); i++) {
                    LatLng point = temp_points.get(i);
                    temp_options.add(point);
                    t_latitude = point.latitude;
                    t_longitude = point.longitude;
                }
                latLng = new LatLng(t_latitude, t_longitude);
                markerOptions.position(latLng);
                BitmapDescriptor temp_icon;
                temp_icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                //if(temp_customer!=null) {
                Log.e(TAG, " info " + temp_customer.getCus_name() + temp_customer.getType());
                markerOptions.title(temp_customer.getCus_name());
                markerOptions.snippet(" " + temp_customer.getPhone());


                //}
                markerOptions.icon(temp_icon);
                mMap.addMarker(markerOptions);
            }
        }

        else if(user ==1) {
            if(check_order_customer.size()==0) {
                Log.e(TAG, "driver Movement" + driver_movement.size());
                for (Map.Entry<String, ArrayList<LatLng>> entry : driver_movement.entrySet()) {
                    String id = entry.getKey();
                    Driver temp_driver = driver_info.get(id);
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
                    if (temp_driver != null) {
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
            else{
                Log.e(TAG, "driver Movement" + driver_movement.size());
                for (Map.Entry<String, ArrayList<LatLng>> entry : driver_movement.entrySet()) {
                    String id = entry.getKey();
                    Driver temp_driver = driver_info.get(id);
                    double t_latitude = 0.0, t_longitude = 0.0;
                    if (check_order_customer.contains(id)) {
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
                        if (temp_driver != null) {
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
            }
        }

        else if(user == 2) {
            Toast.makeText(getApplicationContext(),"check order driver "+check_order_driver.size(),Toast.LENGTH_LONG).show();
            if(check_order_driver.size()!=0) {

                Log.e(TAG,"redraawing lines");

                Log.e(TAG, "customer Movement" + customer_movement.get(0));
                for (Map.Entry<String, ArrayList<LatLng>> entry : customer_movement.entrySet()) {
                    String id = entry.getKey();
                    Log.e(TAG,"while redrawing "+id + " "+check_order_driver.get(0));
                    if (check_order_driver.contains(id)) {
                        Customer temp_customer = customer_info.get(id);
                        double t_latitude = 0.0, t_longitude = 0.0;
                        PolylineOptions temp_options = new PolylineOptions().width(5).geodesic(true);
                        ArrayList<LatLng> temp_points = customer_movement.get(id);
                        for (int i = 0; i < temp_points.size(); i++) {
                            LatLng point = temp_points.get(i);
                            temp_options.add(point);
                            t_latitude = point.latitude;
                            t_longitude = point.longitude;
                        }
                        latLng = new LatLng(t_latitude, t_longitude);
                        markerOptions.position(latLng);
                        BitmapDescriptor temp_icon;
                        temp_icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                        //if(temp_customer!=null) {
                        Log.e(TAG, " info " + temp_customer.getCus_name() + temp_customer.getType());
                        markerOptions.title(temp_customer.getCus_name());
                        markerOptions.snippet(" " + temp_customer.getPhone());


                        //}
                        markerOptions.icon(temp_icon);
                        mMap.addMarker(markerOptions);
                    }
                }
            }
        }


        //line = mMap.addPolyline(options);
    }



//    @Override
//    public void onResume() {
//        super.onResume();
//        if (client != null &&
//                ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION)
//                        == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        //stop location updates when Activity is no longer active
//        if (client != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(client, this);
//        }
//    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {



           Location loc = LocationServices.FusedLocationApi.getLastLocation(client);
            if(loc!=null) {
                Log.e(TAG,"on Connected");
                LatLng latLng = new LatLng(loc.getLatitude(),loc.getLongitude());
                MarkerOptions mark = new MarkerOptions();
                mark.position(latLng);
                mark.title("You are here");
                mark.icon(icon);
                currentLocationMarker = mMap.addMarker(mark);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
            }

            requestLocationUpdates();

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

}
