package com.example.annuairelauriats.ui.gallery;

public class Laureat {
    private String imgbase64;
    private String nom;
    private String organisation;
    private String description;
    private String id;
    private boolean status;
    public Laureat(String id,String image ,String name , String org,String desc){
        this.imgbase64= image;this.nom=name;this.organisation=org;this.description=desc;this.id = id;
    }

    public String getImage() {
        return imgbase64;
    }

    public String getId() {
        return id;
    }

    public String getNameLaureat() {
        return nom;
    }

    public String getDescription() {
        return description;
    }
    public String getOrganisation(){return organisation;}
}
