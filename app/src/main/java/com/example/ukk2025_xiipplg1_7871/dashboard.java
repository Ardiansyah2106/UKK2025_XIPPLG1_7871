package com.example.ukk2025_xiipplg1_7871;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dashboard extends AppCompatActivity {

    private ListView listView;
    private ImageView menu;
    private taskAdapter adapter;
    private List<task> taskList;
    private SwipeRefreshLayout swipeRefresh;
    private SearchView search;
    private static final String URL = "http://172.16.0.179/UKK_2025_7871/daftar_tugas.php";
    private static final String DELETE_BOOK_URL = "http://172.16.0.179/UKK_2025_7871/delete_task.php";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        search = findViewById(R.id.search);
        search.clearFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        menu = findViewById(R.id.Menu);
        listView = findViewById(R.id.list_tugas);
        taskList = new ArrayList<>();

        adapter = new taskAdapter(this, taskList);
        listView.setAdapter(adapter);

        fetchData();

        swipeRefresh = findViewById(R.id.swiperefreshh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(); // Memuat ulang data dari SharedPreferences
                swipeRefresh.setRefreshing(false); // Hentikan animasi refresh setelah selesai
            }
        });

        findViewById(R.id.profil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent untuk pindah ke SecondActivity
                Intent intent = new Intent(dashboard.this, profil.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Ambil objek task yang diklik
                task selectedTask = taskList.get(position);

                // Buat intent untuk pindah ke halaman berikutnya
                Intent intent = new Intent(dashboard.this, task_detail.class);

                // Kirim data deskripsi ke activity baru
                intent.putExtra("deskripsi", selectedTask.getDescription());
                intent.putExtra("kategori", selectedTask.getKategori());
                intent.putExtra("tipe", selectedTask.getTipe());
                intent.putExtra("tanggal", selectedTask.getDate());
                intent.putExtra("jam", selectedTask.getTime());
                intent.putExtra("status", selectedTask.getStatus());

                // Pindah ke halaman baru
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmation(position);
                return true;
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

    private void filterList(String text) {
        List<task> filteredList = new ArrayList<>();

        for (task item : taskList) {
            if (item.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.setFilteredList(filteredList);
    }



    private void showDeleteConfirmation(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Hapus");
        builder.setMessage("Apakah Anda yakin ingin menghapus buku ini?");
        builder.setPositiveButton("Hapus", (dialog, which) -> {
            deleteBookFromServer(taskList.get(position).getDescription());
        });
        builder.setNegativeButton("Batal", null);
        builder.show();
    }
    private void deleteBookFromServer(String title) {
        StringRequest request = new StringRequest(Request.Method.POST, DELETE_BOOK_URL,
                response -> {
                    Toast.makeText(dashboard.this, "tugas berhasil dihapus!", Toast.LENGTH_SHORT).show();
                    fetchBooks();
                },
                error -> Toast.makeText(dashboard.this, "Gagal menghapus tugas!", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("deskripsi", title);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
    private void fetchBooks() {
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
                                String status = bookObject.getString("status");
                                taskList.add(new task(deskripsi, kategori, tipe, date, time, status));
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
    private void fetchData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("API Response", response.toString()); // Debugging API response
                        taskList.clear();
                        List<task> tempList = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject bookObject = response.getJSONObject(i);
                                String deskripsi = bookObject.getString("deskripsi");
                                String kategori = bookObject.getString("kategori");
                                String tipe = bookObject.getString("tipe");
                                String date = bookObject.getString("tanggal");
                                String time = bookObject.getString("jam");
                                String status = bookObject.getString("status");

                                tempList.add(new task(deskripsi, kategori, tipe, date, time, status));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // **Pastikan taskList dan originalList diperbarui dengan data yang didapat**
                        taskList.addAll(tempList);
                        adapter.setFilteredList(taskList); // Pastikan adapter menampilkan data baru

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

    private void loadData(String deskripsi, String kategori, String tipe, String date, String time, String status) {
        String URL = "http://172.16.0.179/UKK_2025_7871/get_kategori.php"; // Ganti dengan URL API Anda

        // Tampilkan indikator refresh (opsional)
        swipeRefresh.setRefreshing(true);

        // Buat request ke server
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        taskList.clear(); // Kosongkan daftar lama sebelum memuat yang baru
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String deskripsi = jsonObject.getString("deskripsi");
                                String kategori = jsonObject.getString("kategori");
                                String tipe = jsonObject.getString("tipe");
                                String date = jsonObject.getString("tanggal");
                                String time = jsonObject.getString("jam");
                                String status = jsonObject.getString("status");
                                taskList.add(new task(deskripsi, kategori, tipe, date, time, status));
                            }

                            // Beritahu adapter bahwa data telah berubah
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Gagal memproses data!", Toast.LENGTH_SHORT).show();
                        }

                        // Hentikan animasi refresh setelah selesai
                        swipeRefresh.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal memuat data!", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    private void loadData() {
        String URL = "http://172.16.0.179/UKK_2025_7871/get_task.php"; // Ganti dengan URL API Anda

        // Tampilkan indikator refresh (opsional)
        swipeRefresh.setRefreshing(true);

        // Buat request ke server
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        taskList.clear(); // Kosongkan daftar lama sebelum memuat yang baru
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String deskripsi = jsonObject.getString("deskripsi");
                                String kategori = jsonObject.getString("kategori");
                                String tipe = jsonObject.getString("tipe");
                                String date = jsonObject.getString("tanggal");
                                String time = jsonObject.getString("jam");
                                String status = jsonObject.getString("status");
                                taskList.add(new task(deskripsi, kategori, tipe, date, time, status));
                            }

                            // Beritahu adapter bahwa data telah berubah
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Gagal memproses data!", Toast.LENGTH_SHORT).show();
                        }

                        // Hentikan animasi refresh setelah selesai
                        swipeRefresh.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Gagal memuat data!", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });

        // Tambahkan request ke queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}