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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bloodbanknaut.R;
import com.example.bloodbanknaut.Utils.Endpoints;
import com.example.bloodbanknaut.Utils.VolleySingleton;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEt, cityEt, bloodTypeEt, passwordEt, phoneNumEt;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addControls();
        addEvents();
    }

    private void addControls(){
        nameEt = findViewById(R.id.txtUsername);
        cityEt = findViewById(R.id.txtCity);
        bloodTypeEt = findViewById(R.id.txtBloodType);
        passwordEt = findViewById(R.id.txtPassword);
        phoneNumEt = findViewById(R.id.txtNumber);
        registerBtn = findViewById(R.id.btnSubmit);
    }

    private void addEvents(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, city, blood_type, password, phoneNum;
                name = nameEt.getText().toString();
                city = cityEt.getText().toString();
                blood_type = bloodTypeEt.getText().toString();
                password = passwordEt.getText().toString();
                phoneNum = phoneNumEt.getText().toString();
                if(isValid(name, city, blood_type, password, phoneNum)) {
                    register(name, city, blood_type, password, phoneNum);
                }
            }
        });
    }

    private boolean isValid(String name, String city , String blood_type, String password, String phoneNum){
        List<String> valid_blood_types = new ArrayList<>();
        valid_blood_types.add("A+");
        valid_blood_types.add("A-");
        valid_blood_types.add("B+");
        valid_blood_types.add("B-");
        valid_blood_types.add("AB+");
        valid_blood_types.add("AB-");
        valid_blood_types.add("O+");
        valid_blood_types.add("O-");

        if(name.isEmpty()){
            showMessage("Name is empty");
            return false;
        }
        else if(city.isEmpty()){
            showMessage("City is empty");
            return false;
        }
        else if (!valid_blood_types.contains(blood_type)){
            showMessage("Blood type invalid choose from "+ valid_blood_types);
            return false;
        }
        else if(password.isEmpty()){
            showMessage("Password is empty");
            return false;
        }
        else if(phoneNum.isEmpty() || phoneNum.length() != 10){
            showMessage("Invalid phone number (should be of 10 digits) or it is empty");
            return false;
        }

        return true;
    }
    private void showMessage (String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void register(String name, String city, String blood_type, String password, String phoneNum){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("city",city).apply();
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                    RegisterActivity.this.finish();
                }
                else {
                    Toast.makeText(RegisterActivity.this, response , Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY",error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",name);
                params.put("city",city);
                params.put("blood_type",blood_type);
                params.put("password",password);
                params.put("phoneNum",phoneNum);
                return  params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


}