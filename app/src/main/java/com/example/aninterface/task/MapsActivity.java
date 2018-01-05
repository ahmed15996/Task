package com.example.aninterface.task;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aninterface.task.Model.ItemModel;
import com.example.aninterface.task.Utils.DataParser;
import com.example.aninterface.task.Utils.Singleton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double lon, lat;
    private ItemModel.Result result;
    private ItemModel.Product product;
    private String REQUEST_TAG = "route";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (getIntent().hasExtra("result") && getIntent().hasExtra("product")) {

            result = getIntent().getParcelableExtra("result");
            product = getIntent().getParcelableExtra("product");

            lon = Double.parseDouble(result.getLong());
            lat = Double.parseDouble(result.getLat());
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(lon, lat);
        LatLng resturant = new LatLng(31.037149, 31.357652);
        mMap.addMarker(new MarkerOptions().position(sydney)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(resturant).title(product.getUserName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12));

        getRoute(sydney, resturant);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                dailog();
            }
        });
    }

    private void dailog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        TextView name = dialog.findViewById(R.id.customerName_dailog);
        TextView phone = dialog.findViewById(R.id.phoneNumber_dailog);
        final CircleImageView imageView = dialog.findViewById(R.id.myProfile_image);
        final ProgressBar progressBar = dialog.findViewById(R.id.myProfile_image_progress);


        if (product != null) {
            name.setText(product.getUserName());
            phone.setText(String.valueOf(product.getOrderPhone()));
            Picasso.with(this).load(product.getUserImage()).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {

                }
            });
        }
        Button button = dialog.findViewById(R.id.more);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Customer Name : " + String.valueOf(product.getUserName()), Toast.LENGTH_LONG)
                        .show();
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window dialogWindow = dialog.getWindow();
        lp.copyFrom(dialogWindow.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);

        dialog.show();

    }

    private void getRoute(LatLng p1, LatLng p2) {
        String url = getUrl(p1, p2);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("map", response.toString());
                DataParser route = new DataParser();

                ArrayList<LatLng> points;
                PolylineOptions options = null;
                for (List<HashMap<String, String>> path : route.parse(response)) {
                    points = new ArrayList<>();
                    options = new PolylineOptions();
                    for (HashMap<String, String> point : path) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }
                    options.addAll(points)
                            .width(8)
                            .geodesic(true)
                            .color(Color.RED);
                }
                if (options != null) {
                    mMap.addPolyline(options);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("map", error.getMessage());
            }
        });

        Singleton.getInstance(this).addToRequestQueue(request, REQUEST_TAG);
    }


    private String getUrl(LatLng p1, LatLng p2) {
        String origin = "origin=" + p1.latitude + "," + p1.longitude;
        String destination = "destination=" + p2.latitude + "," + p2.longitude;
        String sensor = "sensor=false";

        String params = origin + "&" + destination + "&" + sensor;

        return "https://maps.googleapis.com/maps/api/directions/json?" + params;
    }
}
