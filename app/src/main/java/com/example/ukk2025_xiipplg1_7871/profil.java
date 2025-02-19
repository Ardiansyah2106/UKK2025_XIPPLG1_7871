package com.example.ukk2025_xiipplg1_7871;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class profil extends AppCompatActivity {

    private Button logout, edit;
    private SwipeRefreshLayout swipeRefresh;
    private SharedPreferences sharedPreferences;
    private TextView tvNama, tvGmail;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(); // Memuat ulang data dari SharedPreferences
                swipeRefresh.setRefreshing(false); // Hentikan animasi refresh setelah selesai
            }
        });

        edit = findViewById(R.id.edit_data);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfilDialog editDialog = new EditProfilDialog(profil.this);
                editDialog.show(getSupportFragmentManager(), "Edit Profil");
            }
        });

        logout = findViewById(R.id.keluar);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hapus data sesi
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Kembali ke halaman login
                Intent intent = new Intent(profil.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tvGmail = findViewById(R.id.Gmail);
        tvNama = findViewById(R.id.nama);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        String email = sharedPreferences.getString("email", "Email tidak ditemukan");
        String nama = sharedPreferences.getString("nama", "Tidak tersedia");

        tvGmail.setText("Email: " + email);
        tvNama.setText("Nama: " + nama);


        findViewById(R.id.back_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke SecondActivity
                Intent intent = new Intent(profil.this, dashboard.class);
                startActivity(intent);
            }
        });
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "Tidak tersedia");

        tvNama.setText("Nama: " + nama);
    }
}