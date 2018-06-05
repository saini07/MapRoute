package com.example.saini.maproute;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedbackList extends ArrayAdapter<Order> {

private Activity context;
private List<Order> list;

public FeedbackList(Activity context, List<Order> list) {
        super(context,R.layout.feedback_card,list);
        this.context = context;
        this.list = list;
        }

@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listItem = layoutInflater.inflate(R.layout.feedback_card,null,true);

        TextView cus_fb = (TextView) listItem.findViewById(R.id.cus_fb);
        TextView fb = (TextView) listItem.findViewById(R.id.fb);

        Order order = list.get(position);

        cus_fb.setText(order.getCustomer().getCus_name());
        fb.setText(order.getFeedback());

        return listItem;
        }
        }