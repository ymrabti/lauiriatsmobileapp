package com.example.annuairelauriats.ui.home;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    DatabaseHandler(Context context) {
        super(context, "Laureats", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE filieres (     id int(11) PRIMARY KEY ,     Nom text NOT NULL,     Date_Creation date NOT NULL   ) ");
        db.execSQL("CREATE TABLE laureats (     id int(11) PRIMARY KEY ,     Nom varchar(100) NOT NULL,     Prenom varchar(100) NOT NULL,     Gender char(1) NOT NULL,     Promotion int(11) NOT NULL,     Filiere int(11) NOT NULL,     DATE_INSCRIPTION datetime NOT NULL,     Description varchar(1000) NOT NULL,     Telephone varchar(20) NOT NULL,     email varchar(260) NOT NULL UNIQUE,     Pass_word varchar(50) NOT NULL,     Role int(11) NOT NULL,     Actif tinyint(1) DEFAULT 0   ) "   );
        db.execSQL("CREATE TABLE laureat_org (     org int(11),     laureat int(11) NOT NULL,     En_cours tinyint(1) NOT NULL,     DATE_DEBUT date NOT NULL,     Fonction varchar(260) NOT NULL,     PRIMARY KEY (org,laureat)   ) " );
        db.execSQL("CREATE TABLE organisme (     id_org int(11) PRIMARY KEY ,     Nom varchar(350) NOT NULL,     secteur char(1) DEFAULT NULL,     Latitude double DEFAULT NULL,     Longitude double DEFAULT NULL   ) " );
        db.execSQL("CREATE TABLE organisme_en_attente (     id_org int(11) NOT NULL ,     Nom varchar(350) NOT NULL,     Latitude double DEFAULT NULL,     Longitude double DEFAULT NULL,     laureat int(11) NOT NULL,     intitule varchar(255) NOT NULL,     secteur varchar(20) NOT NULL,     date_debut date NOT NULL   ) ");
        db.execSQL("CREATE TABLE photos (     laureat int(11) ,     Path TEXT DEFAULT NULL   ) " );
        db.execSQL("CREATE TABLE posts (     laureat int(11) NOT NULL,     IME varchar(50) DEFAULT NULL   ) ");
        db.execSQL("CREATE TABLE roles (     id int(11)  PRIMARY KEY,     nom varchar(50) DEFAULT NULL   ) " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS filieres");
        db.execSQL("DROP TABLE IF EXISTS laureats");
        db.execSQL("DROP TABLE IF EXISTS laureat_org");
        db.execSQL("DROP TABLE IF EXISTS organisme");
        db.execSQL("DROP TABLE IF EXISTS organisme_en_attente");
        db.execSQL("DROP TABLE IF EXISTS photos");
        db.execSQL("DROP TABLE IF EXISTS posts");
        db.execSQL("DROP TABLE IF EXISTS roles");
    }
}
