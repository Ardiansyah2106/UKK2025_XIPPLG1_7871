package com.example.ukk2025_xiipplg1_7871;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class add_list extends AppCompatActivity {

    private Spinner Kategori, Tipe;
    private EditText edtDeskripsi;
    private Button date,time,tambah;
    private TextView txtDate,txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private RequestQueue requestQueue;
    private static final String URL = "http://172.16.0.179/UKK_2025_7871/add_task.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        edtDeskripsi = findViewById(R.id.desc);
        Kategori = findViewById(R.id.category);
        Tipe = findViewById(R.id.type);
        date = findViewById(R.id.date);
        time = findViewById(R.id.add_time);
        txtDate = findViewById(R.id.pick_date);
        txtTime = findViewById(R.id.pick_time);
        tambah = findViewById(R.id.add_data);

        requestQueue = Volley.newRequestQueue(this);

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahTugas();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        Kategori = findViewById(R.id.category);
        Tipe = findViewById(R.id.type);

        new GetCategoriesTask().execute("http://172.16.0.179/UKK_2025_7871/kategori.php");
        new GetTypeTask().execute("http://172.16.0.179/UKK_2025_7871/tipe.php");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke SecondActivity
                Intent intent = new Intent(add_list.this, dashboard.class);
                startActivity(intent);
            }
        });
    }
    private class GetCategoriesTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            ArrayList<String> categoryNames = new ArrayList<>();
            try {
                // Membuat URL dan membuka koneksi
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Membaca respon
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parsing JSON
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject category = jsonArray.getJSONObject(i);
                    categoryNames.add(category.getString("nama_kategori"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return categoryNames;
        }

        @Override
        protected void onPostExecute(ArrayList<String> categoryNames) {
            // Menampilkan data di Spinner
            if (categoryNames.isEmpty()) {
                Toast.makeText(add_list.this, "Tidak ada kategori", Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(add_list.this,
                        android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Kategori.setAdapter(adapter);
            }
        }
    }
    private class GetTypeTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            ArrayList<String> typeNames = new ArrayList<>();
            try {
                // Membuat URL dan membuka koneksi
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Membaca respon
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parsing JSON
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject category = jsonArray.getJSONObject(i);
                    typeNames.add(category.getString("nama_tipe"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return typeNames;
        }

        @Override
        protected void onPostExecute(ArrayList<String> typeNames) {
            // Menampilkan data di Spinner
            if (typeNames.isEmpty()) {
                Toast.makeText(add_list.this, "Tidak ada tipe", Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(add_list.this,
                        android.R.layout.simple_spinner_item, typeNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Tipe.setAdapter(adapter);
            }
        }
    }
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                add_list.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;

                        // Update TextView untuk tanggal
                        updateDateText();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                add_list.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;

                        // Update TextView untuk waktu
                        updateTimeText();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }
    private void updateDateText() {
        if (mYear != 0 && mMonth != 0 && mDay != 0) {
            String date = String.format("%02d-%02d-%d",
                    mYear,
                    mMonth + 1,
                    mDay);
            txtDate.setText(date);
        }
    }
    private void updateTimeText() {
        if (mHour != 0 && mMinute != 0) {
            String time = String.format("%02d:%02d", mHour, mMinute);
            txtTime.setText(time);
        }
    }
    private void tambahTugas() {
        String deskripsi = edtDeskripsi.getText().toString().trim();
        String kategori = Kategori.getSelectedItem().toString();
        String tipe = Tipe.getSelectedItem().toString();
        String date = txtDate.getText().toString().trim();
        String time = txtTime.getText().toString().trim();

        if (deskripsi.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat JSON Object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deskripsi", deskripsi);
            jsonObject.put("kategori", kategori);
            jsonObject.put("tipe", tipe);
            jsonObject.put("tanggal", date);
            jsonObject.put("jam", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Kirim Request ke Server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Toast.makeText(add_list.this, message, Toast.LENGTH_SHORT).show();

                            if (status.equals("success")) {
                                startActivity(new Intent(add_list.this, MainActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e("VolleyError", "Error parsing response: " + e.getMessage());
                            Toast.makeText(add_list.this, "Format respons tidak sesuai!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorResponse = new String(error.networkResponse.data);
                            Log.e("VolleyError", "Error Response: " + errorResponse);
                        }
                        Log.e("VolleyError", "Volley Error: " + error.toString());
                        Toast.makeText(add_list.this, "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Tambahkan request ke queue
        requestQueue.add(jsonObjectRequest);
    }
}