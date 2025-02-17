package com.example.ukk2025_xiipplg1_7871;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class register extends AppCompatActivity {

    private EditText etNama, etEmail, etPassword;
    private Button daftar;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNama = findViewById(R.id.namleng);
        etEmail = findViewById(R.id.Gmail);
        etPassword = findViewById(R.id.create_pw);
        daftar = findViewById(R.id.daftar);

        requestQueue = Volley.newRequestQueue(this);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        final String nama = etNama.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        // Validasi input sebelum dikirim ke server
        if (nama.isEmpty()) {
            etNama.setError("Nama tidak boleh kosong!");
            etNama.requestFocus();
            return;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Masukkan email yang valid!");
            etEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password minimal 6 karakter!");
            etPassword.requestFocus();
            return;
        }

        // Buat objek JSON
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nama", nama);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (Exception e) {
            Toast.makeText(this, "Terjadi kesalahan saat memproses data!", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL API
        String url = "http://172.16.0.227/UKK_2025_7871/register.php";

        // Request ke API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Toast.makeText(register.this, message, Toast.LENGTH_SHORT).show();

                            if (status.equals("success")) {
                                startActivity(new Intent(register.this, MainActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            Log.e("VolleyError", "Error parsing response: " + e.getMessage());
                            Toast.makeText(register.this, "Format respons tidak sesuai!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(register.this, "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                    }
                });

        // Tambahkan request ke queue
        requestQueue.add(jsonObjectRequest);
    }
}