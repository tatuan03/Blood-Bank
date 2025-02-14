package com.example.bloodbanknaut.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbanknaut.Adapters.SearchAdapter;
import com.example.bloodbanknaut.DataModels.Donor;
import com.example.bloodbanknaut.DataModels.RequestDataModel;
import com.example.bloodbanknaut.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResult extends AppCompatActivity {

    List<Donor> donorList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addControls();

    }

    private void addControls(){
        String json;
        String city,blood_type;
        donorList = new ArrayList<>();
        Intent intent = getIntent();
        json = intent.getStringExtra("json");
        city = intent.getStringExtra("city");
        blood_type = intent.getStringExtra("blood_type");
        TextView heading = findViewById(R.id.heading);
        String str = "Donors in "+city+" with blood type "+blood_type;
        heading.setText(str);
        Gson gson = new Gson();
        Type type = new TypeToken<List<Donor>>(){}.getType();
        List<Donor> dataModels = gson.fromJson(json,type);
        if (dataModels.isEmpty()){
            heading.setText("No results");
        } else if (dataModels != null) {
            donorList.addAll(dataModels);
        }

        RecyclerView recyclerView = findViewById(R.id.recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        SearchAdapter adapter = new SearchAdapter(donorList, SearchResult.this);
        recyclerView.setAdapter(adapter);

    }
}