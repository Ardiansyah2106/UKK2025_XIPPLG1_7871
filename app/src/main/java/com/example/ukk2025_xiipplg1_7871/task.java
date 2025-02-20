package com.example.ukk2025_xiipplg1_7871;

public class task {
    private String description;
    private String kategori;
    private String tipe;
    private String date;
    private String time;
    private String status;

    public task(String description, String kategori, String tipe, String date, String time, String status) {
        this.description = description;
        this.kategori = kategori;
        this.tipe = tipe;
        this.date = date;
        this.time = time;
        this.status = status;
    }
    public String getKategori() {
        return kategori;
    }
    public String getTipe() {
        return tipe;
    }
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public String getStatus() {
        return status;
    }
}
