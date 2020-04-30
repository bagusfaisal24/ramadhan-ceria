package com.example.widgetimsakiyah;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    private List<ModelData> dataList;
    private List<ModelDetailData> detailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.hellow);
        dataList = new ArrayList<>();
        detailList = new ArrayList<>();
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            setAddress(location.getLatitude(), location.getLongitude());
                        } else {
                            Toast.makeText(getApplicationContext(), "GPS Tidak Aktif", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void setAddress(Double latitude, Double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            if (addresses.size() > 0) {
                Log.d("max", " " + addresses.get(0).getMaxAddressLineIndex());

                String kecamatan = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String city = addresses.get(0).getSubAdminArea();
                String country = addresses.get(0).getCountryName();
                getData(city, country, kecamatan, state);

                addresses.get(0).getAdminArea();
            }
        }
    }

    private void getData(final String city, final String country, final String kecamatan, final String state) {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, "http://api.aladhan.com/v1/timingsByCity?city=" + city + "&country=" + country + "&method=11",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            ModelData data = new ModelData();
                            ModelDetailData detailData = new ModelDetailData();
                            String hijri = String.format("%s-%s-%s", jsonObject.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getString("day"),
                                    jsonObject.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getJSONObject("month").getString("en"),
                                    jsonObject.getJSONObject("data").getJSONObject("date").getJSONObject("hijri").getString("year"));
                            data.setDateHijriah(hijri);
                            data.setDateMasehi(jsonObject.getJSONObject("data").getJSONObject("date").getString("readable"));
                            detailData.setSubuh(jsonObject.getJSONObject("data").getJSONObject("timings").getString("Fajr"));
                            detailData.setImsak(jsonObject.getJSONObject("data").getJSONObject("timings").getString("Imsak"));
                            detailData.setDzuhur(jsonObject.getJSONObject("data").getJSONObject("timings").getString("Dhuhr"));
                            detailData.setAshar(jsonObject.getJSONObject("data").getJSONObject("timings").getString("Asr"));
                            detailData.setMaghrib(jsonObject.getJSONObject("data").getJSONObject("timings").getString("Maghrib"));
                            detailData.setIsya(jsonObject.getJSONObject("data").getJSONObject("timings").getString("Isha"));
                            detailList.add(detailData);
                            data.setWaktu(detailList);
                            dataList.add(data);
                            Log.v("data", dataList.get(0).getDateHijriah());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new Gson();
                        String jsonData = gson.toJson(dataList);
                        Intent intent = new Intent(MainActivity.this, Imsakiyah.class);
                        intent.putExtra("waktu", jsonData);
                        String CONCAT_WILAYAH = "%s, %s, %s, %s";
                        intent.putExtra("location", String.format(CONCAT_WILAYAH, kecamatan, city, state, country));
                        getApplicationContext().sendBroadcast(intent);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
