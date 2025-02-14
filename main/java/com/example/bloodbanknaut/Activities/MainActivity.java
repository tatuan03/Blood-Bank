package com.example.bloodbanknaut.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbanknaut.Adapters.RequestAdapter;
import com.example.bloodbanknaut.DataModels.RequestDataModel;
import com.example.bloodbanknaut.R;
import com.example.bloodbanknaut.Utils.Endpoints;
import com.example.bloodbanknaut.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<RequestDataModel> requestDataModels;
    private RequestAdapter requestAdapter;
    private TextView make_request_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();
    }

    private void addControls(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.search_button){
                    //open search
                    startActivity(new Intent(
                            MainActivity.this,
                            SearchActivity.class
                    ));
                }
                return false;
            }
        });
        requestDataModels = new ArrayList<>();
        recyclerView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        requestAdapter = new RequestAdapter(requestDataModels,this);
        recyclerView.setAdapter(requestAdapter);
        popularHomePage();
        make_request_button = findViewById(R.id.makeRequestButton);

        TextView pick_location = findViewById(R.id.pickLocation);
        String location = PreferenceManager.getDefaultSharedPreferences(this).getString("city","no_city_found");

        if(!location.equals("no_city_found")){
            pick_location.setText(location);
        }
    }

    private void popularHomePage() {
        final String city = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getString("city", "no_city");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, Endpoints.get_requests, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<RequestDataModel>>(){}.getType();
                List<RequestDataModel> dataModels = gson.fromJson(response, type);
                requestDataModels.addAll(dataModels);
                requestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city", city);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

//    private void deleteItem() {
//        final String city = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
//                .getString("city", "no_city");
//        StringRequest stringRequest = new StringRequest(
//                Request.Method.POST, Endpoints.get_requests, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Gson gson = new Gson();
//                Type type = new TypeToken<List<RequestDataModel>>(){}.getType();
//                List<RequestDataModel> dataModels = gson.fromJson(response, type);
//                requestDataModels.addAll(dataModels);
//                requestAdapter.notifyDataSetChanged();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MainActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
//                Log.d("VOLLEY", Objects.requireNonNull(error.getMessage()));
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("city", city);
//                return params;
//            }
//        };
//        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
//    }


    private void addEvents(){
        make_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        MainActivity.this,
                        MakeRequestActivity.class
                ));
            }
        });

    }



}