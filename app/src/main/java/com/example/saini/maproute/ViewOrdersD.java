package com.example.saini.maproute;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    DatabaseReference databaseOrders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders_d);
        listView = (ListView) findViewById(R.id.d_order_list);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        orderDAdapter = new OrderDAdapter(getApplicationContext(), R.layout.orders_dr_card);

        databaseOrders = FirebaseDatabase.getInstance().getReference("order");

        databaseOrders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot orders : dataSnapshot.getChildren()) {
                    Order order = orders.getValue(Order.class);
                    Driver temp_driver = order.getDriver();
                    Customer temp_cus = order.getCustomer();
                    if (order.getAccepted().equals("forwarded")&&(order.getStatus().equals("pending")||order.getStatus().equals("processing")) ) {


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
