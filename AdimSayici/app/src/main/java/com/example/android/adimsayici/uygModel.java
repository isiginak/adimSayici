package com.example.android.adimsayici;

import java.util.Date;

/**
 * Created by HP on 20.6.2017.
 */

public class uygModel {
    Date tarih;
    String sayac;
    String adimSayisi;
    public uygModel(){

    }

    public uygModel(Date tarih, String sayac, String adimSayisi) {
        this.tarih = tarih;
        this.sayac = sayac;
        this.adimSayisi = adimSayisi;
    }

    public Date getTarih() {
        return tarih;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public String getSayac() {
        return sayac;
    }

    public void setSayac(String sayac) {
        this.sayac = sayac;
    }

    public String getAdimSayisi() {
        return adimSayisi;
    }

    public void setAdimSayisi(String adimSayisi) {
        this.adimSayisi = adimSayisi;
    }
}
