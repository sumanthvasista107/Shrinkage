package com.example.shrinkage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.ViewHolder {

    TextView employee_id, g_activity_Desc, percentage;

    public MyAdapter(@NonNull View itemView) {
        super(itemView);
        employee_id =(TextView) itemView.findViewById(R.id.card_emp);
        g_activity_Desc = (TextView) itemView.findViewById(R.id.card_activity);
        percentage = (TextView) itemView.findViewById(R.id.card_percentage);

    }
}

