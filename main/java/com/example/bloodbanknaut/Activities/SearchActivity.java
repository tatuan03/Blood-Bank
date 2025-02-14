package com.example.bloodbanknaut.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bloodbanknaut.DataModels.RequestDataModel;
import com.example.bloodbanknaut.R;
import com.example.bloodbanknaut.Utils.Endpoints;
import com.example.bloodbanknaut.Utils.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    Button submit_button;
    EditText et_blood_type, et_city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();
    }

    private void addControls(){
        et_blood_type = findViewById(R.id.et_blood_type);
        et_city = findViewById(R.id.et_city);
        submit_button = findViewById(R.id.btnSubmit);
    }

    private void addEvents(){
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blood_type = et_blood_type.getText().toString();
                String city = et_city.getText().toString();
                if(isValid(blood_type,city)){
                    get_search_results(blood_type,city);
                }
            }
        });
    }

    private void get_search_results(String blood_type, String city){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.search_donors, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(SearchActivity.this, SearchResult.class);
                intent.putExtra("city",city);
                intent.putExtra("blood_type",blood_type);
                intent.putExtra("json",response);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city",city);
                params.put("blood_type",blood_type);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String blood_type, String city){

        List<String> valid_blood_types = new ArrayList<>();
        valid_blood_types.add("A+");
        valid_blood_types.add("A-");
        valid_blood_types.add("B+");
        valid_blood_types.add("B-");
        valid_blood_types.add("AB+");
        valid_blood_types.add("AB-");
        valid_blood_types.add("O+");
        valid_blood_types.add("O-");
        if(!valid_blood_types.contains(blood_type)){
            showMessage("Blood type invalid choose from " + valid_blood_types);
            return false;
        }
        else if (city.isEmpty()){
            showMessage("Enter city");
            return false;
        }

        return true;
    }

    private void showMessage (String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}