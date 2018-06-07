package com.example.saini.maproute;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.saini.maproute.FirstActivity.user;

public class NavigateActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference databaseOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseOrder = FirebaseDatabase.getInstance().getReference("order");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(user==0)
        navigationView.inflateMenu(R.menu.admin);
        else if(user==1)
            navigationView.inflateMenu(R.menu.activity_navigate_drawer);
        else if(user==2)
            navigationView.inflateMenu(R.menu.driver);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id) {
            case R.id.home:
                MapsActivity.view_map = 0;
                Intent h = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(h);
                Toast.makeText(getApplicationContext(),"home",Toast.LENGTH_LONG).show();
                break;

            case R.id.view_maps:
                MapsActivity.view_map = 1;
                Intent vm = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(vm);
                Toast.makeText(getApplicationContext(),"view Maps",Toast.LENGTH_LONG).show();
                break;

            case R.id.place_order_cus:

               Intent poc = new Intent(getApplicationContext(),PaymentActivity.class);
               startActivity(poc);
                Toast.makeText(getApplicationContext(),"order gets placed after payment",Toast.LENGTH_LONG).show();
                break;

            case R.id.view_order_cus:
                Intent oc = new Intent(getApplicationContext(),ViewOrdersC.class);
                startActivity(oc);
                Toast.makeText(getApplicationContext(),"view order",Toast.LENGTH_LONG).show();
                break;

            case R.id.feedback_cus:
                Toast.makeText(getApplicationContext(),"feedback",Toast.LENGTH_LONG).show();
                break;

            case R.id.drivers_admin:
                Intent da=new Intent(getApplicationContext(),ViewDriversA.class);
                startActivity(da);
                Toast.makeText(getApplicationContext(),"driver details",Toast.LENGTH_LONG).show();
                break;

            case R.id.orders_admin:
                Intent oa = new Intent(getApplicationContext(),ViewOrdersA.class);
                startActivity(oa);
                Toast.makeText(getApplicationContext(),"order details",Toast.LENGTH_LONG).show();
                break;

            case R.id.trips_admin:
                Intent ta = new Intent(getApplicationContext(),ViewTripsA.class);
                startActivity(ta);
                Toast.makeText(getApplicationContext(),"trips details",Toast.LENGTH_LONG).show();
                break;

            case R.id.orders_driver:
                Intent od = new Intent(getApplicationContext(),ViewOrdersD.class);
                startActivity(od);
                Toast.makeText(getApplicationContext(),"new orders",Toast.LENGTH_LONG).show();
                break;

            case R.id.trips_driver:
                Intent td = new Intent(getApplicationContext(),ViewTripsD.class);
                startActivity(td);
                Toast.makeText(getApplicationContext(),"my trips",Toast.LENGTH_LONG).show();
                break;

            case R.id.signout:
                Intent signout = new Intent(getApplicationContext(),FirstActivity.class);
                startActivity(signout);
                Toast.makeText(getApplicationContext(),"signout",Toast.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
