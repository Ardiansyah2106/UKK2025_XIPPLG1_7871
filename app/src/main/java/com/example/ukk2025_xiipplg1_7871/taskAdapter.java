package com.example.ukk2025_xiipplg1_7871;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class taskAdapter extends ArrayAdapter<task> {

    private List<task> taskList;
    private List<task> originalList;
    private LayoutInflater inflater;

    public taskAdapter(Context context, List<task> taskList) {
        super(context, 0, taskList);
        this.inflater = LayoutInflater.from(context);
        this.taskList = new ArrayList<>(taskList); // Pastikan ListView langsung terisi data
        this.originalList = new ArrayList<>(taskList);
    }

    public void setFilteredList(List<task> filteredList) {
        this.taskList.clear();
        if (filteredList.isEmpty()) {
            this.taskList.addAll(originalList); // Kembalikan data asli jika pencarian kosong
        } else {
            this.taskList.addAll(filteredList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return taskList.size(); // Pastikan jumlah item sesuai dengan taskList yang sedang aktif
    }

    @Override
    public task getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_item, parent, false);
        }

        TextView desc = convertView.findViewById(R.id.description);
        TextView kategori = convertView.findViewById(R.id.kategori);
        TextView tipe = convertView.findViewById(R.id.tipe);
        TextView waktu = convertView.findViewById(R.id.Date);
        TextView jam = convertView.findViewById(R.id.time);
        TextView status = convertView.findViewById(R.id.status);

        task book = getItem(position);
        if (book != null) {
            desc.setText(book.getDescription());
            kategori.setText("Kategori: " + book.getKategori());
            tipe.setText("Tipe: " + book.getTipe());
            waktu.setText("Waktu: " + book.getDate());
            jam.setText("Jam: " + book.getTime());
            status.setText("Status Tugas: " + book.getStatus());
        }

        return convertView;
    }
}


