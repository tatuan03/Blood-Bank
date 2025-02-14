package com.example.bloodbanknaut.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.bloodbanknaut.R;
import com.example.bloodbanknaut.Utils.Endpoints;
import com.example.bloodbanknaut.Utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText numberEt, passwordEt;
    Button btnLogin;
    TextView signUpText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();
        addEvents();
    }

    private void addControls(){
        numberEt = findViewById(R.id.txtNumber);
        passwordEt = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        signUpText = findViewById(R.id.txtsignUp);
    }
    private void addEvents(){
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        LoginActivity.this, RegisterActivity.class)
                );
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberEt.setError(null);
                passwordEt.setError(null);
                String phoneNum = numberEt.getText().toString();
                String password = passwordEt.getText().toString();
                if(isValid(phoneNum,password)){
                    login(phoneNum,password);
                }
            }
        });
    }

    private void login(final String phoneNum, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("Invalid Credentials")) {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("phoneNum",phoneNum).apply();
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                            .putString("city", response).apply();
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong:(", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("password", password);
                params.put("phoneNum", phoneNum);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    private boolean isValid(String number, String password){
        if(number.isEmpty()){
            showMessage("Empty Phone Number");
            numberEt.setError("Empty Phone Number");
            return false;
        }
        else if(password.isEmpty()){
            showMessage("Empty Password");
            passwordEt.setError("Empty Password");
            return false;
        }
        return true;
    }

    private void showMessage (String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}