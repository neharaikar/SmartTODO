package com.example.project_testing;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.example.api.APIClient;
import com.example.api.GoogleMapAPI;
import com.example.entities.Geometry;
import com.example.entities.OpeningHours;
import com.example.entities.PlacesResults;
import com.example.entities.Result;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 12/2/18.
 */
@SuppressLint("NewApi")
public class MyService extends JobService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<Status> {
        String placename="";
    DatabaseReference reff;
    DATA_REFER data_ref;
    String itemlist;
    ArrayList<String> arrayList;
    int position;
    /**
     * Update interval of location request
     */
    private final int UPDATE_INTERVAL = 1000;

    /**
     * fastest possible interval of location request
     */
    private final int FASTEST_INTERVAL = 900;

    /**
     * The Job scheduler.
     */
    JobScheduler jobScheduler;

    /**
     * The Tag.
     */
    String TAG = "MyService";

    /**
     * LocationRequest instance
     */
    private LocationRequest locationRequest;

    /**
     * GoogleApiClient instance
     */
    private GoogleApiClient googleApiClient;


    /**
     * Location instance
     */
    private Location lastLocation;

    /**
     * Method is called when location is changed
     * @param location - location from fused location provider
     */
    @Override
    public void onLocationChanged(Location location) {

        if (placename!=null) {

            String key = getText(R.string.google_maps_key).toString();
            String keyword = placename;
            String currentLocation = location.getLatitude() + "," + location.getLongitude();
            final double curlat=location.getLatitude();
            final double curlng=location.getLongitude();

            int radius = 50;
            String type = "All";
            GoogleMapAPI googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
            googleMapAPI.getNearBy(currentLocation, radius, type, keyword, key).enqueue(new Callback<PlacesResults>() {
                String notiplaces;

                @Override
                public void onResponse(Call<PlacesResults> call, Response<PlacesResults> response) {
                    if (response.isSuccessful()) {
                        List<Result> results = response.body().getResults();
                        int count = 0;
                        notiplaces="";
                        int len = results.size();
                        if (!(results.isEmpty())) {

                            System.out.println("000000000000000000000000000000000000000000000000000");
                            count++;
                            for (int i = 0; i < len; i++) {


                                Result result = results.get(i);
                                Geometry g = result.getGeometry();
                                com.example.entities.Location l = g.getLocation();
                                System.out.println("Geometry:" + l.getLat() + "," + l.getLng());

                                float[] r = new float[1];
                                Location.distanceBetween(l.getLat(), l.getLng(),
                                        curlat, curlng, r);
                                System.out.println(" distanceee: "+r[0]);
                               if (r[0] <= 50)
                                {

                                    System.out.println(count);
                                    notiplaces = notiplaces + count + ")";

                                    count++;

                                    System.out.println("Name:" + result.getName());
                                    notiplaces = notiplaces + result.getName();


                                    System.out.println("Address:" + result.getVicinity());
                                    notiplaces = notiplaces + System.lineSeparator() + result.getVicinity();


                                    OpeningHours op = result.getOpeningHours();

                                    if (op != null && op.getOpenNow() != null) {

                                        if (op.getOpenNow()) {
                                            System.out.println("Availability: Open");
                                            notiplaces = notiplaces + "(OPEN)" + System.lineSeparator();


                                        } else {
                                            System.out.println("Availability: Closed");
                                            notiplaces = notiplaces + "(CLOSED)" + System.lineSeparator();


                                        }
                                    } else
                                        notiplaces = notiplaces + System.lineSeparator();
                                }
                                else
                                    System.out.println("Not within the radiusssssss, distanceee: "+r[0]);
                            }
                                System.out.println("0000000000000000000000000000000000000000000000000000");
                                System.out.println(notiplaces + "...........");
                                //NOTIFICATION
                            if(notiplaces!="")
                                addNotification(notiplaces);
                            else
                                System.out.println("Notiplacess is nulllll");

                        }
                        if (results.isEmpty()) {

                            Toast.makeText(getApplicationContext(), "Sorry, No results found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PlacesResults> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        System.out.print(placename+",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
        writeActualLocation(location);
    }

    /**
     * extract last location if location is not available
     */
    @SuppressLint("MissingPermission")
    private void getLastKnownLocation() {
        //Log.d(TAG, "getLastKnownLocation()");
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            Log.i(TAG, "LasKnown location. " +
                    "Long: " + lastLocation.getLongitude() +
                    " | Lat: " + lastLocation.getLatitude());
            writeLastLocation();
            startLocationUpdates();

        } else {
            Log.w(TAG, "No location retrieved yet");
            startLocationUpdates();

            //here we can show Alert to start location
        }
    }

    /**
     * this method writes location to text view or shared preferences
     * @param location - location from fused location provider
     */
    @SuppressLint("SetTextI18n")
    private void writeActualLocation(Location location) {
        Log.d(TAG, location.getLatitude() + ", " + location.getLongitude());
        //here in this method you can use web service or any other thing
    }

    /**
     * this method only provokes writeActualLocation().
     */
    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }


    /**
     * this method fetches location from fused location provider and passes to writeLastLocation
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        //Log.i(TAG, "startLocationUpdates()");

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    /**
     * Default method of service
     * @param params - JobParameters params
     * @return boolean
     */
    @Override
    public boolean onStartJob(JobParameters params) {

       // placename= params.getExtras().getString("placename");
       // System.out.println(placename +"....................place service class");
        arrayList = new ArrayList<String>();
        reff = FirebaseDatabase.getInstance().getReference().child("DATA_REFER");
        reff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arrayList.add(dataSnapshot.getValue().toString());
                int l=arrayList.size();
                System.out.println("Count is --------------------->"+l);
                System.out.println(arrayList);
                System.out.println("-------------------------------------------------");
                addplacename();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                arrayList.remove(dataSnapshot.getValue().toString());
                dataSnapshot.getRef().removeValue();

                int l=arrayList.size();
                System.out.println("Count is --------------------->"+l);
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

        startJobAgain();

        createGoogleApi();

        return false;
    }
    public void addplacename()
    {
        placename=null;
        itemlist=null;
        int ln=arrayList.size();
        System.out.println(ln+"  Arraylist LENGTH    ..... ...................!!!!!!!!!!!!!!!!");
        if (ln>0) {
            System.out.println(ln+"  Arraylist LENGTH    ..... ...................!!!!!!!!!!!!!!!!");
            for (int i = 0; i < ln; i++) {
                String yo = arrayList.get(i);
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
                    if(placename.length()>0) {
                        itemlist=yo1[1];
                        position=i;
                        break;
                    }
                }

            }


            System.out.println(placename+"...................!!!!!!!!!!!!!!!!-------service class");
        }
        else
            System.out.println("ADAPTER IS EMPTY      ..... ...................!!!!!!!!!!!!!!!!");
        System.out.println(placename+"...................!!!!!!!!!!!!!!!!");

    }


    /**
     * Create google api instance
     */
    private void createGoogleApi() {
        //Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //connect google api
        googleApiClient.connect();

    }

    /**
     * disconnect google api
     * @param params - JobParameters params
     * @return result
     */
    @Override
    public boolean onStopJob(JobParameters params) {

        googleApiClient.disconnect();
        return false;
    }

    /**
     * starting job again
     */
    private void startJobAgain() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Log.d(TAG, "Job Started");
            ComponentName componentName = new ComponentName(getApplicationContext(),
                    MyService.class);
            jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(1, componentName)
                    .setMinimumLatency(10000*3) //30 sec interval
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
            jobScheduler.schedule(jobInfo);
        }
    }

    /**
     * this method tells whether google api client connected.
     * @param bundle - to get api instance
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    /**
     * this method returns whether connection is suspended
     * @param i - 0/1
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"connection suspended");
    }

    /**
     * this method checks connection status
     * @param connectionResult - connected or failed
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"connection failed");
    }

    /**
     * this method tells the result of status of google api client
     * @param status - success or failure
     */
    @Override
    public void onResult(@NonNull Status status) {
        Log.d(TAG,"result of google api client : " + status);
    }
    public void addNotification(String notiplace) {
        System.out.println(notiplace +"notification called....................place service class");
        System.out.println(position +"is the position....................place service class");
        //        //
        //
        //
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("Alert !!")
                .setWhen(System.currentTimeMillis())
                .setContentText("You have a task on hand...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notiplace))
                .setAutoCancel(true); // clear notification after click
        Intent intent2 = new Intent(getApplicationContext(), Items.class);
        intent2.putExtra("position", String.valueOf(position));
        intent2.putExtra("itemlist", itemlist);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pi);
        mBuilder.setAutoCancel(true);
        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);


        mNotificationManager.notify(0, mBuilder.build());

    }
}
