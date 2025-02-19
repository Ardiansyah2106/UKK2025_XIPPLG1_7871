package com.example.ukk2025_xiipplg1_7871;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    private ListView listView;
    private ImageView menu;
    private taskAdapter adapter;
    private List<task> taskList;
    private static final String URL = "http://172.16.0.179/UKK_2025_7871/daftar_tugas.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        menu = findViewById(R.id.Menu);
        listView = findViewById(R.id.list_tugas);
        taskList = new ArrayList<>();

        adapter = new taskAdapter(this, taskList);
        listView.setAdapter(adapter);

        fetchData();

        findViewById(R.id.profil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke SecondActivity
                Intent intent = new Intent(dashboard.this, profil.class);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuDialog dialog = new menuDialog();
                dialog.show(getSupportFragmentManager(), "HalfScreenDialog");
            }
        });

        findViewById(R.id.addlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke SecondActivity
                Intent intent = new Intent(dashboard.this, add_list.class);
                startActivity(intent);
            }
        });
    }
    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("API Response", response.toString()); // Debugging API response
                        taskList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject bookObject = response.getJSONObject(i);
                                String deskripsi = bookObject.getString("deskripsi");
                                String kategori = bookObject.getString("kategori");
                                String tipe = bookObject.getString("tipe");
                                String date = bookObject.getString("tanggal");
                                String time = bookObject.getString("jam");
                                taskList.add(new task(deskripsi, kategori, tipe, date, time));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley Error", error.toString());
                    }
                });
        queue.add(request);
    }
}