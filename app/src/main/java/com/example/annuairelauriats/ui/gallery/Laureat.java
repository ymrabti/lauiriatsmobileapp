package com.example.annuairelauriats.ui.gallery;

public class Laureat {
    private String imgbase64;
    private String nom;
    private String organisation;
    private String description;
    private boolean status;
    public Laureat(String image ,String name , String org,String desc){
        this.imgbase64= image;this.nom=name;this.organisation=org;this.description=desc;
    }

    public String getImage() {
        return imgbase64;
    }

    public String getNameLaureat() {
        return nom;
    }

    public String getDescription() {
        return description;
    }
    public String getOrganisation(){return organisation;}
}
