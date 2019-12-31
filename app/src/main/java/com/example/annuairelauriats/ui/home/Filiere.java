package com.example.annuairelauriats.ui.home;

public class Filiere {
    private int primier_promo;
    private int id ;
    public Filiere(int primier_promo,int id){
        this.id=id;this.primier_promo=primier_promo;
    }

    public int get_Id() {
        return id;
    }

    public int getPrimier_promo() {
        return primier_promo;
    }
}
