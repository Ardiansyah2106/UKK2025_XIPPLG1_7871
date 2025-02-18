package com.example.ukk2025_xiipplg1_7871;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    private TextView buat;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.mailEt);
        etPassword = findViewById(R.id.EtPw);
        btnLogin = findViewById(R.id.btn_login);
        buat = findViewById(R.id.buat);

        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            Intent intent = new Intent(MainActivity.this, dashboard.class);
            startActivity(intent);
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        findViewById(R.id.buat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke SecondActivity
                Intent intent = new Intent(MainActivity.this, register.class);
                startActivity(intent);
            }
        });

    }
    private void loginUser() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Masukkan email!");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Masukkan password!");
            etPassword.requestFocus();
            return;
        }

        String url = "http://172.16.0.179/UKK_2025_7871/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("success")) {
                                JSONObject userObject = jsonObject.getJSONObject("user");

                                // Tangani nilai NULL dengan mengganti menjadi "Data masih kosong"
                                String idUser = userObject.isNull("id_user") ? "Data masih kosong" : userObject.getString("id_user");
                                String nama = userObject.isNull("nama") ? "Data masih kosong" : userObject.getString("nama");
                                String email = userObject.isNull("email") ? "Data masih kosong" : userObject.getString("email");

                                // Simpan data ke SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("id_user", idUser);
                                editor.putString("nama", nama);
                                editor.putString("email", email);
                                editor.apply();

                                Toast.makeText(MainActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, dashboard.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(MainActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Terjadi kesalahan dalam membaca respons server!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}