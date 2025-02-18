package com.example.ukk2025_xiipplg1_7871;

public class task {
    private String description;
    private String kategori;
    private String tipe;
    private String date;
    private String time;

    public task(String description, String kategori, String tipe, String date, String time) {
        this.description = description;
        this.kategori = kategori;
        this.tipe = tipe;
        this.date = date;
        this.time = time;
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
}
