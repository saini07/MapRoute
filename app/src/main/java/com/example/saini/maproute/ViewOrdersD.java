package com.example.saini.maproute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewOrdersD extends AppCompatActivity {

    private OrderDAdapter orderDAdapter;
    private ListView listView;
    Toolbar toolbar;
    DatabaseReference databaseOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_d);
        listView = (ListView) findViewById(R.id.d_order_list);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Orders");
        toolbar.setTitleTextColor(android.graphics.Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        databaseOrders = FirebaseDatabase.getInstance().getReference("order");




    }

    @Override
    protected void onResume() {
        super.onResume();
        orderDAdapter = new OrderDAdapter(getApplicationContext(), R.layout.orders_dr_card);
        reload();
    }

    private void reload() {
        databaseOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderDAdapter.clear();
                for(DataSnapshot orders : dataSnapshot.getChildren()) {
                    Order order = orders.getValue(Order.class);
                    Driver temp_driver = order.getDriver();
                    Customer temp_cus = order.getCustomer();
                    if (order.getAccepted().equals("forwarded")&&(order.getStatus().equals("pending")||order.getStatus().equals("processing")) && temp_driver.getId().equals(MapsActivity.driver.getId()) ) {


                        OrderDcard orderCard;
                        String fd = "";
                        if (order.getFeedback() == null) {
                            orderCard = new OrderDcard(temp_cus.getCus_name(), Long.toString(temp_cus.getPhone()), order.getStatus(), fd,order.getId());
                        } else {
                            fd = order.getFeedback();
                            orderCard = new OrderDcard(temp_cus.getCus_name(), Long.toString(temp_cus.getPhone()), order.getStatus(), fd,order.getId());
                        }

                        orderDAdapter.add(orderCard);


                    }
                }

                listView.setAdapter(orderDAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
