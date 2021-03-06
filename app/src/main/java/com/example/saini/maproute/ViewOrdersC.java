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

public class ViewOrdersC extends AppCompatActivity {

    private OrderCusAdapter orderCusAdapter;
    private ListView listView;
    DatabaseReference databaseOrders;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_c);
        listView = (ListView) findViewById(R.id.cus_order_list);

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
        orderCusAdapter = new OrderCusAdapter(getApplicationContext(), R.layout.orders_cus_card);
        reload();
    }

    private void reload() {
        databaseOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot orders : dataSnapshot.getChildren()) {
                    Order order = orders.getValue(Order.class);
                    Driver temp_driver = order.getDriver();
                    Customer temp_cus = order.getCustomer();
                    if (order.getAccepted().equals("forwarded")) {

                        if (temp_cus.getId().equals(MapsActivity.customer.getId())) {
                            OrderCard orderCard;
                            String fd = "";
                            if (order.getFeedback() == null) {
                                orderCard = new OrderCard(temp_driver.getDriver_name(), Long.toString(temp_driver.getPhone_no()), Long.toString(temp_driver.getLoads()), order.getStatus(), fd,order.getId());
                            } else {
                                fd = order.getFeedback();
                                orderCard = new OrderCard(temp_driver.getDriver_name(), Long.toString(temp_driver.getPhone_no()), Long.toString(temp_driver.getLoads()), order.getStatus(), fd,order.getId());
                            }

                            orderCusAdapter.add(orderCard);
                        }

                    }
                }

                listView.setAdapter(orderCusAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
