package com.example.ukk2025_xiipplg1_7871;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class add_kategori_dialog extends DialogFragment {
    private EditText nama_kategori;
    private Button save;
    private RequestQueue requestQueue;
    private Context context;
    private static final String URL = "http://172.16.0.179/UKK_2025_7871/add_kategori.php";

    public add_kategori_dialog(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.add_kategori, null);
        builder.setView(view);

        // Inisialisasi UI
        nama_kategori = view.findViewById(R.id.nama_kategori);
        save = view.findViewById(R.id.save);

        // Inisialisasi RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        save.setOnClickListener(v -> updateData());

        return builder.create();
    }

    private void updateData() {
        String kategori = nama_kategori.getText().toString().trim();

        if (kategori.isEmpty()) {
            Toast.makeText(requireContext(), "Nama kategori tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat JSON Object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nama_kategori", kategori);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Kirim Request ke Server
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                response -> {
                    try {
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                        if (status.equals("success")) {
                            new AlertDialog.Builder(requireContext())
                                    .setTitle("Sukses!")
                                    .setMessage("Data berhasil ditambahkan.")
                                    .setPositiveButton("OK", (dialog, which) -> {
                                        dismiss(); // Tutup dialog tanpa berpindah halaman
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                    } catch (JSONException e) {
                        Log.e("VolleyError", "Error parsing response: " + e.getMessage());
                        Toast.makeText(requireContext(), "Format respons tidak sesuai!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        String errorResponse = new String(error.networkResponse.data);
                        Log.e("VolleyError", "Error Response: " + errorResponse);
                    }
                    Log.e("VolleyError", "Volley Error: " + error.toString());
                    Toast.makeText(requireContext(), "Terjadi kesalahan jaringan!", Toast.LENGTH_SHORT).show();
                });

        // Tambahkan request ke queue
        requestQueue.add(jsonObjectRequest);
    }
}

