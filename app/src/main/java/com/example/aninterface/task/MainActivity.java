package com.example.aninterface.task;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.aninterface.task.Adapter.Callback;
import com.example.aninterface.task.Adapter.ItemAdapter;
import com.example.aninterface.task.Model.ItemModel;
import com.example.aninterface.task.Utils.Network;
import com.example.aninterface.task.Utils.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements Callback{
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ItemAdapter adapter;
    private String REQUEST_TAG = "load_data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler);
        progressBar = findViewById(R.id.progress_mainActivity);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Accepted Products");
        adapter = new ItemAdapter(this);
        adapter.setCallback(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        if (Network.isConeccted(this)){
            loadData();
        }else {
            Toast.makeText(this,"No internet connection",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }

    private void loadData(){
        String url = "http://home-station.info/home_station/public/api/order/trader/accepted/ar";
        Map<String,String> map = new HashMap<>();
        map.put("user_id","49");
        final JSONObject object = new JSONObject(map);
        final JsonObjectRequest joRequest = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("re",response.toString());
                List<ItemModel.Result> list = new ArrayList<>();
                try {
                    JSONArray result = response.getJSONArray("Result");
                    for (int x = 0; x<result.length();x++){
                        JSONObject jsonObject = result.getJSONObject(x);
                        int order_id = jsonObject.getInt("order_id");
                        int order_user_id = jsonObject.getInt("order_user_id");
                        int status = jsonObject.getInt("status");
                        String lat = jsonObject.getString("lat");
                        String lon = jsonObject.getString("long");
                        String order_date = jsonObject.getString("order_date");
                        JSONArray product = jsonObject.getJSONArray("products");
                        List<ItemModel.Product> products = new ArrayList<>();
                        for (int i = 0; i <product.length();i++){
                            JSONObject object1 = product.getJSONObject(i);
                            String product_name = object1.getString("product_name");
                            String product_image = object1.getString("product_image");
                            int product_rate = object1.getInt("product_rate");
                            String user_name = object1.getString("user_name");
                            String user_mobile = object1.getString("user_mobile");
                            String user_image = object1.getString("user_image");
                            int order_phone = object1.getInt("order_phone");
                            products.add(new ItemModel.Product(
                                    product_name,
                                    product_image,
                                    product_rate,
                                    user_name,
                                    user_mobile,
                                    user_image,
                                    order_phone
                            ));
                        }
                        list.add(new ItemModel.Result(order_id,order_user_id,status,lat,lon,order_date,products));
                    }

                    adapter.addlist(list);
                    progressBar.setVisibility(View.GONE);
                    Log.e("d", response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("d",e.getMessage()+"Done");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("d",error.getMessage());
            }
        });
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(joRequest,REQUEST_TAG);
        recyclerView.setVisibility(View.VISIBLE);



    }

    @Override
    public void onClick(View view, int position) {
        adapter.deleteItem(position);
    }
}
