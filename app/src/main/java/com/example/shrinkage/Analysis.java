package com.example.shrinkage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Analysis extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference mDatabaseRef;
    private ArrayList<Model> arrayList = new ArrayList<Model>();
    private Spinner spinner;
    private TextView activity, total_time;
    private FirebaseRecyclerOptions<Model> options;
    private FirebaseRecyclerAdapter<Model, MyAdapter> adapter;

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Employees");
        mDatabaseRef.keepSynced(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        spinner = (Spinner) findViewById(R.id.select_spinner);
        activity = (TextView) findViewById(R.id.spinner_activity);
        total_time = (TextView) findViewById(R.id.spinner_total);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        options = new FirebaseRecyclerOptions.Builder<Model>().setQuery(mDatabaseRef, Model.class).build();

        adapter = new FirebaseRecyclerAdapter<Model, MyAdapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyAdapter holder, int position, @NonNull Model model) {
                holder.employee_id.setText(model.getEmployee_id());
                holder.g_activity_Desc.setText(model.getG_activity_Desc());
                holder.percentage.setText(model.getPercentage());
            }

            @NonNull
            @Override
            public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyAdapter(LayoutInflater.from(Analysis.this).inflate(R.layout.emp_data, parent, false));
            }
        };

        recyclerView.setAdapter(adapter);

    }
}