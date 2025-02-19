package com.example.ukk2025_xiipplg1_7871;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfilDialog extends DialogFragment {
    private EditText editNama;
    private TextView editEmail;
    private Button btnSimpan;
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String UPDATE_URL = "http://172.16.0.179/UKK_2025_7871/edit_profil.php";
    public EditProfilDialog(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_data_profil, null);
        builder.setView(view);

        editNama = view.findViewById(R.id.editNama);
        editEmail = view.findViewById(R.id.editAlamat);
        btnSimpan = view.findViewById(R.id.btnSimpan);

        // Ambil data dari SharedPreferences
        sharedPreferences = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama", "");
        String email = sharedPreferences.getString("email", "");

        // Tampilkan data di form
        editNama.setText(nama);
        editEmail.setText(email);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });

        return builder.create();
    }
    private void updateData() {
        String nama = editNama.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (nama.isEmpty() || email.isEmpty()) {
            Toast.makeText(context, "Semua data harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("UpdateResponse", "Server Response: " + response); // Debug log
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");

                            if (status.equals("success")) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                                // Simpan data baru ke SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("nama", editNama.getText().toString().trim());
                                editor.putString("email", editEmail.getText().toString().trim());
                                editor.apply();

                                dismiss(); // Tutup pop-up setelah update
                            } else {
                                Toast.makeText(context, "Gagal: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONError", "Invalid JSON: " + response); // Debugging
                            Toast.makeText(context, "Response error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
