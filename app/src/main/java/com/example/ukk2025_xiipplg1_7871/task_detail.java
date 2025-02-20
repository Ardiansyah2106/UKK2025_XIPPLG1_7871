package com.example.ukk2025_xiipplg1_7871;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class task_detail extends AppCompatActivity {

    private TextView textView,textView1,textView2,textView3,textView4,textView5;
    private Button selesai;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        String deskripsi = getIntent().getStringExtra("deskripsi");
        String kategori = getIntent().getStringExtra("kategori");
        String tipe = getIntent().getStringExtra("tipe");
        String tanggal = getIntent().getStringExtra("tanggal");
        String jam = getIntent().getStringExtra("jam");
        String status = getIntent().getStringExtra("status");

        textView = findViewById(R.id.description);
        textView.setText(deskripsi);
        textView1 = findViewById(R.id.kategori);
        textView1.setText(kategori);
        textView2 = findViewById(R.id.tipe);
        textView2.setText(tipe);
        textView3 = findViewById(R.id.date);
        textView3.setText(tanggal);
        textView4 = findViewById(R.id.jam);
        textView4.setText(jam);
        textView5 = findViewById(R.id.status);
        textView5.setText(status);

        selesai = findViewById(R.id.complete);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke SecondActivity
                Intent intent = new Intent(task_detail.this, dashboard.class);
                startActivity(intent);
            }
        });
        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deskripsiTugas = textView.getText().toString().trim();
                konfirmasiSelesaikanTugas(deskripsiTugas);
            }
        });
    }
    private void konfirmasiSelesaikanTugas(String deskripsiTugas) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda ingin menyelesaikan tugas ini?")
                .setPositiveButton("Selesaikan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStatusToSelesai(deskripsiTugas);
                    }
                })
                .setNegativeButton("Batal", null)
                .show();
    }
    private void updateStatusToSelesai(String deskripsi) {
        String url = "http://172.16.0.179/UKK_2025_7871/update_status.php"; // Ganti dengan URL PHP-mu
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Tugas selesai!", Toast.LENGTH_SHORT).show();
                        // Bisa tambahkan refresh tampilan di sini
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal memperbarui status", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("deskripsi", deskripsi);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}