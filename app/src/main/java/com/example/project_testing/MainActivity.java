package com.example.project_testing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String item = "", place = "";
    String date = "", time = "";
    int worked = 0;
    EditText et;
    Button bt;
    int sq;
    int ln;
    ListView lv;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    DatabaseReference reff;
    DATA_REFER data_ref;
    NORMAL_TASK normal_task;
    String result;
    String yo = "";
    String TAG = "MainActivity";
    private JobScheduler jobScheduler;
    private ComponentName componentName;
    private JobInfo jobInfo;
    String placename = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(MainActivity.this, "Firebase connected successfully", Toast.LENGTH_SHORT).show();
        data_ref = new DATA_REFER("", "", "", "");
        normal_task = new NORMAL_TASK("");
        et = this.findViewById(R.id.et);
        bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAdd_onClick(v);
            }
        });

        lv = (ListView) this.findViewById(R.id.listview_lv);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view,int position, long l) {
                final int which_item = position;
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Task completed")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                arrayList.remove(which_item);
                                adapter.notifyDataSetChanged();
                                addplacename();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList);
        lv.setAdapter(adapter);

        reff = FirebaseDatabase.getInstance().getReference().child("DATA_REFER");

        System.out.println(ln+"  ADAPTER LENGTH    ..... ...................!!!!!!!!!!!!!!!!");
        System.out.println("Hello1");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                yo = lv.getItemAtPosition(position).toString();
                yo = yo.subSequence(yo.indexOf("{") + 1, yo.indexOf("}") - 1).toString();
                String[] yo1 = yo.split(", ");
                if (yo.contains("time") || yo.contains("place") || yo.contains("item") || yo.contains("date")) {
                    Toast.makeText(MainActivity.this, yo1[0] + "\n" + yo1[2], Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Normal Task", Toast.LENGTH_SHORT).show();
                }
            }
        });


        reff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayList.add(dataSnapshot.getValue().toString());
                notify1();
                ln=adapter.getCount();
                System.out.println("Count is --------------------->"+ln);
                System.out.println("-------------------------------------------------");
                addplacename();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                notify1();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                arrayList.remove(dataSnapshot.getValue().toString());
                dataSnapshot.getRef().removeValue();
                notify1();
                ln=adapter.getCount();
                System.out.println("Count is --------------------->"+ln);
                System.out.println("-------------------------------------------------");
                addplacename();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        notify1();

    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }


    private void buttonAdd_onClick(View view) {
        result = et.getText().toString();
        et.setText("");
        System.out.println(result);
        String[] aa = result.split(" ");
        String previousWord = " ";
        for (String word : aa) {
            if (previousWord.equalsIgnoreCase("buy") ||
                    previousWord.equalsIgnoreCase("purchase") ||
                    previousWord.equalsIgnoreCase("bring")) {
                item = word;
                data_ref.setItem(item);
                worked = 1;
                sq = 1;
                System.out.println("---------------------------");
            } else if (previousWord.equalsIgnoreCase("from")) {
                place = word;
                data_ref.setPlace(place);

                worked = 1;
            } else if (previousWord.equalsIgnoreCase("at")) {
                time = word;
                data_ref.setTime(time);
                worked = 1;
            } else if (previousWord.equalsIgnoreCase("on")) {
                date = word;
                data_ref.setDate(date);
                worked = 1;
            }
            previousWord = word;
        }
        if (worked == 1) {
            reff.push().setValue(data_ref);
            Toast.makeText(MainActivity.this, "Data added to firebase", Toast.LENGTH_SHORT).show();

        } else {
            sq = 0;
            normal_task.setNormal_task(result);
            reff.push().setValue(normal_task);
            Toast.makeText(MainActivity.this, "Normal task added to firebase", Toast.LENGTH_SHORT).show();

        }
        et.setText("");
        worked = 0;


    }




    public final void notify1() {
        this.adapter.notifyDataSetChanged();

    }

    public void addplacename()
    {
        placename="";
        ln = adapter.getCount();
        System.out.println(ln+"  ADAPTER LENGTH    ..... ...................!!!!!!!!!!!!!!!!");
        if (ln>0) {
            System.out.println(ln+"  ADAPTER LENGTH    ..... ...................!!!!!!!!!!!!!!!!");
            for (int i = 0; i < ln; i++) {
                yo = lv.getItemAtPosition(i).toString();
                System.out.println(yo+"    items  ..... ...................!!!!!!!!!!!!!!!!");
                yo = yo.subSequence(yo.indexOf("{") + 1, yo.indexOf("}") - 1).toString();
                String[] yo1 = yo.split(", ");
                if (yo.contains("place")) {
                    String s = "";
                    int p = 0;
                    int l = yo1[2].length();
                    for (int j = 0; j < l; j++) {
                        if (yo1[2].charAt(j) == '=') {
                            break;
                        }
                        p++;
                    }
                    p++;
                    placename = yo1[2].substring(p, l);
                    System.out.println(placename+"substring...................!!!!!!!!!!!!!!!!");
                    if(placename.length()>0)
                    break;
                }

            }
            if(  placename!=null) {
                if(placename.length()>0) {
                    System.out.println(placename + "is not empty...................!!!!!!!!!!!!!!!! length:" + placename.length());
                    permission();
                    StartBackgroundTask();

                }

            }

            System.out.println(placename+"...................!!!!!!!!!!!!!!!!");
        }
        else
            System.out.println("ADAPTER IS EMPTY      ..... ...................!!!!!!!!!!!!!!!!");
        System.out.println(placename+"...................!!!!!!!!!!!!!!!!");

    }
    public void permission()
    {
        boolean hasForegroundLocationPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean hasForegroundLocationPermissionfine = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (hasForegroundLocationPermission || hasForegroundLocationPermissionfine) {
            boolean hasBackgroundLocationPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
            if (hasBackgroundLocationPermission) {
                if (isLocationEnabled()) {
                     System.out.println(placename+"       calling service ...................!!!!!!!!!!!!!!!!");
                    Toast.makeText(this, "Location enabled", Toast.LENGTH_LONG).show();



                } else {
                    Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "This application will not work in background unless you allow it all the time", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 12);
            }
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 12);

        }
    }
    @SuppressLint("NewApi")
    public void StartBackgroundTask() {

        jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
        componentName = new ComponentName(getApplicationContext(), MyService.class);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("placename", placename+"... calling service class");

        jobInfo = new JobInfo.Builder(1, componentName)
                .setExtras(bundle)
                .setMinimumLatency(10000) //10 sec interval
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
        jobScheduler.schedule(jobInfo);
    }
}
