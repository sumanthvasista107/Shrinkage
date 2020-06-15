package com.example.shrinkage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, ValueEventListener {

    private Spinner activity_spr,activity_description_spr;
    private EditText emp_id,start_time, end_time, min;
    private TextView dateText;
    private Button submitBtn, analysisBtn;
    private float total_time = 60, finalval =0;
    private long sum =0;



    private DatabaseReference rootRef, timeref;

    //DatabaseReference userRef = rootRef.child("Employees");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_spr= (Spinner) findViewById(R.id.activity_spr);
        activity_description_spr= (Spinner) findViewById(R.id.activity_description_spr);
        activity_spr.setOnItemSelectedListener(this);

        dateText = (TextView)findViewById(R.id.date_text);
        emp_id = (EditText)findViewById(R.id.emp_id);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        analysisBtn = (Button) findViewById(R.id.analysisbtn);
        start_time = (EditText) findViewById(R.id.start_time);
        end_time = (EditText) findViewById(R.id.end_time);
        min = (EditText) findViewById(R.id.minutes);



        findViewById(R.id.select_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDailog();
            }
        });

        rootRef = FirebaseDatabase.getInstance().getReference().child("Employees");
        timeref = FirebaseDatabase.getInstance().getReference().child("Activiy_time");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String date = dateText.getText().toString().trim();
                String empid = emp_id.getText().toString();
                String act_spr = activity_spr.getSelectedItem().toString().trim();
                String activity_desc_spr = activity_description_spr.getSelectedItem().toString().trim();
                String starttime = start_time.getText().toString().trim();
                String endtime = end_time.getText().toString().trim();
                String minutes = min.getText().toString().trim();
                final long min_value = Long.parseLong(minutes);
                finalval = Integer.parseInt(minutes);
                finalval = (float) ((finalval/total_time)*100);

                Log.d("TAG", String.valueOf(finalval));


                HashMap<String, String> hashMap = new HashMap<String, String>();

                    hashMap.put("date", date);
                    hashMap.put("employee_id", String.valueOf(empid));
                    hashMap.put("f_activity", act_spr);
                    hashMap.put("g_activity_Desc", activity_desc_spr);
                    hashMap.put("h_startTime", starttime);
                    hashMap.put("i_endTime", endtime);
                    hashMap.put("minutes", minutes);
                    hashMap.put("percentage", String.valueOf(finalval));

                rootRef.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "SUBMITTED", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                if(activity_desc_spr == "PKT"){
                    timeref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long count = (long) dataSnapshot.child("pkt").child("total_time").getValue();
                            sum= count+min_value;
                            timeref.child("pkt").child("total_time").setValue(sum);
                            Log.d("TAG", String.valueOf(sum));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                dateText.setText(null);
                emp_id.setText(null);
                start_time.setText(null);
                end_time.setText(null);
                min.setText(null);

            }
        });


        analysisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Analysis.class);
                startActivity(intent);
            }
        });


    }

    private void sum(String minutes) {

        finalval = Integer.parseInt(minutes);
        finalval = (float) ((finalval/total_time)*100);
        Log.d("TAG", String.valueOf(finalval));

        return ;

    }

    private void addListenerOnButton() {

    }

    private void setCurrentTimeOnView() {

    }

    private void showDatePickerDailog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int date){
        month = month+1;
        String time = date + "/"+ month+ "/" +year;
        dateText.setText(time);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String s1= String.valueOf(activity_spr.getSelectedItem());
        Log.d("s1", String.valueOf(s1));
        if(s1.contentEquals("Accenture_Activities")){
            List<String> list = new ArrayList<String>();
            list.add("Accenture Training");
            list.add("Early Logout");
            list.add("Fun Activity");
            list.add("HR Meeting");
            list.add("Late Login");
            list.add("PKT");
            list.add("Town Hall(RNR)");
            list.add("Transport Session");
            list.add("Wellness");
            list.add("Floor Walk");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            activity_description_spr.setAdapter(dataAdapter);
        }
        if(s1.contentEquals("Error_Discussion")){
            List<String> list = new ArrayList<String>();
            list.add("Individual Error Discussion");
            list.add("Live Audit");
            list.add("Smart Key / GF");
            list.add("Team Error Discussion");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            activity_description_spr.setAdapter(dataAdapter2);
        }
        if(s1.contentEquals("Meet")){
            List<String> list = new ArrayList<String>();
            list.add("Team Meet");
            list.add("Meet With TL");
            list.add("Meet With Span Lead");
            list.add("Meet With Ops Lead");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            activity_description_spr.setAdapter(dataAdapter3);

        }
        if(s1.contentEquals("Policy_Update_Accenture")){
            List<String> list = new ArrayList<String>();
            list.add("MDP");
            list.add("PVIL");
            list.add("Queue Trainings");
            list.add("SOP Updates");
            list.add("ACE of Policies/VIVA");
            ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter4.notifyDataSetChanged();
            activity_description_spr.setAdapter(dataAdapter4);

        }

        if(s1.contentEquals("SRT_Related")){
            List<String> list = new ArrayList<String>();
            list.add("Bug ID Creation");
            list.add("Congratulation Message");
            list.add("Dumping Issue");
            list.add("Fetching/Loading");
            list.add("Incorrect Queue JID");
            list.add("Latency Issue");
            list.add("Low Volume");
            list.add("No Volume");
            list.add("Page Preview Not Available");
            list.add("SRT Survey");
            list.add("SRT Connectivity Issue");
            list.add("SRT Down");
            list.add("SRT Login Issue");
            list.add("Tagging Tree Issue");
            ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter5.notifyDataSetChanged();
            activity_description_spr.setAdapter(dataAdapter5);
        }


        if(s1.contentEquals("WFH_Tech_Issues")){
            List<String> list = new ArrayList<String>();
            list.add("Bandwidth Issue due to Dongle Connection");
            list.add("Bandwidth Issue due to Broadband Connection");
            list.add("Bandwidth Issue due to Mobile Tethering Connection");
            list.add("Bandwidth Issue due to Mobile Hotspot Connection");
            list.add("LAN/Pulse No Connection");
            list.add("System Login Issue");
            list.add("System Performance Issue");
            list.add("Internet Issue");
            list.add("Outlook/Teams Issue");
            ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter6.notifyDataSetChanged();
            activity_description_spr.setAdapter(dataAdapter6);
        }

        /*if(s1.contentEquals("Accenture_Activities")){
            List<String> list = new ArrayList<String>();
            list.add("Accenture Training");
            list.add("Early Logout");
            list.add("Fun Activity");
            list.add("HR Meeting");
            list.add("Late Login");
            list.add("PKT");
            list.add("Town Hall(RNR)");
            list.add("Transport Session");
            list.add("Wellness");
            list.add("Floor Walk");
        }
        if(s1.contentEquals("Accenture_Activities")){
            List<String> list = new ArrayList<String>();
            list.add("Accenture Training");
            list.add("Early Logout");
            list.add("Fun Activity");
            list.add("HR Meeting");
            list.add("Late Login");
            list.add("PKT");
            list.add("Town Hall(RNR)");
            list.add("Transport Session");
            list.add("Wellness");
            list.add("Floor Walk");
        }
        if(s1.contentEquals("Accenture_Activities")){
            List<String> list = new ArrayList<String>();
            list.add("Accenture Training");
            list.add("Early Logout");
            list.add("Fun Activity");
            list.add("HR Meeting");
            list.add("Late Login");
            list.add("PKT");
            list.add("Town Hall(RNR)");
            list.add("Transport Session");
            list.add("Wellness");
            list.add("Floor Walk");
        }
        if(s1.contentEquals("Accenture_Activities")){
            List<String> list = new ArrayList<String>();
            list.add("Accenture Training");
            list.add("Early Logout");
            list.add("Fun Activity");
            list.add("HR Meeting");
            list.add("Late Login");
            list.add("PKT");
            list.add("Town Hall(RNR)");
            list.add("Transport Session");
            list.add("Wellness");
            list.add("Floor Walk");
        }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

}


