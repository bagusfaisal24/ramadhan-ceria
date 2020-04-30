package com.example.widgetimsakiyah;

import java.util.List;

public class ModelData {

    private List<ModelDetailData> waktu;
    private String dateMasehi;
    private String dateHijriah;


    public void setWaktu(List<ModelDetailData> waktu) {
        this.waktu = waktu;
    }

    void setDateMasehi(String date) {
        this.dateMasehi = date;
    }

    public List<ModelDetailData> getWaktu() {
        return waktu;
    }

    public String getDateMasehi() {
        return dateMasehi;
    }

    public String getDateHijriah() {
        return dateHijriah;
    }

    void setDateHijriah(String dateHijriah) {
        this.dateHijriah = dateHijriah;
    }
}
