package com.example.annuairelauriats.ui.home;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.annuairelauriats.ui.standards.StandardsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.annuairelauriats.ui.home.Classtest.get_pref;
import static com.example.annuairelauriats.ui.home.Classtest.set_pref;

public class DatabaseHandler extends SQLiteOpenHelper {
    private SQLiteDatabase database;
    public DatabaseHandler(Context context) {
        super(context, "Laureats.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE filieres (     id int(11) PRIMARY KEY  AUTOINCREMENT,     Nom text NOT NULL,     Date_Creation date NOT NULL   ) ");
        db.execSQL("CREATE TABLE laureats (     id int(11) PRIMARY KEY AUTOINCREMENT ,  " +
                "   Nom varchar(100) NOT NULL,     Prenom varchar(100) NOT NULL,  " +
                "   Gender char(1) NOT NULL,     Promotion int(11) NOT NULL,   " +
                "  Filiere int(11) NOT NULL,     DATE_INSCRIPTION datetime NOT NULL,    " +
                " Description varchar(1000) NOT NULL,     Telephone varchar(20) NOT NULL,   " +
                "  email varchar(260) NOT NULL UNIQUE,     Pass_word varchar(50) NOT NULL,  " +
                "   Role int(11) NOT NULL,     Actif tinyint(1) DEFAULT 0   ) "   );
        db.execSQL("CREATE TABLE laureat_org (     org int(11),     laureat int(11) NOT NULL,   " +
                "  En_cours tinyint(1) NOT NULL,     DATE_DEBUT date NOT NULL,   " +
                "  Fonction varchar(260) NOT NULL,     PRIMARY KEY (org,laureat)   ) " );
        db.execSQL("CREATE TABLE organisme (     id_org int(11) PRIMARY KEY ,     Nom varchar(350) NOT NULL,  " +
                "   secteur char(1) DEFAULT NULL,     Latitude double DEFAULT NULL,     Longitude double DEFAULT NULL   ) " );
        db.execSQL("CREATE TABLE organisme_en_attente (     id int(11) NOT NULL PRIMARY KEY,     Nom varchar(350) NOT NULL, " +
                "    Latitude double DEFAULT NULL,     Longitude double DEFAULT NULL,   " +
                "  laureat int(11) NOT NULL,     intitule varchar(255) NOT NULL,    " +
                " secteur varchar(20) NOT NULL,     date_debut date NOT NULL   ) ");
        db.execSQL("CREATE TABLE photos (     laureat int(11) ,     Path TEXT DEFAULT NULL   ) " );
        db.execSQL("CREATE TABLE posts (     laureat int(11) NOT NULL,     IME varchar(50) DEFAULT NULL   ) ");
        db.execSQL("CREATE TABLE roles (     id int(11)  PRIMARY KEY,     nom varchar(50) DEFAULT NULL   ) " );
    }

    public List<String> return_laureats(){
        List<String> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM laureats;";
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1)+" +++++ "+cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }
    public void insert_laureat(String nom,String prenom/*,String gender,String promotion,String filiere*/){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        long id=get_pref(StandardsFragment.getContexte(),"laureats")+1;
        set_pref(StandardsFragment.getContexte(),"laureats",id);
        Toast.makeText(StandardsFragment.getContexte(),id+"",Toast.LENGTH_LONG).show();
        values.put("Nom",nom );values.put("Prenom",prenom );values.put("Gender", nom.substring(2));values.put("Promotion","M");
        values.put("Filiere",6 );values.put("DATE_INSCRIPTION", "dattdttd");values.put("Description","hhfhfhhhfhf" );
        values.put("Telephone","777747774747774" );values.put("id",id);
        values.put("email", nom+"@"+prenom+".com");values.put("Pass_word", "20202020");values.put("Role",1 );
        database.insert("laureats", null, values);
        database.close();
    }
    public void insert_filiere(String nom,String date){
        database=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("Nom",nom);values.put("Date_Creation",date);
        long id=get_pref(StandardsFragment.getContexte(),"filiere")+1;
        set_pref(StandardsFragment.getContexte(),"filiere",id);
        values.put("id",id);
        database.insert("filieres",null,values);
        database.close();
    }
    public List<String> return_filieres(){
        List<String> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM filieres;";
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getInt(0)+"");
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return list;
    }
    public void insert_photo(String base64){
        database=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("Path",base64);
        long id=get_pref(StandardsFragment.getContexte(),"photos")+1;
        set_pref(StandardsFragment.getContexte(),"photos",id);
        values.put("laureat",id);
        database.insert("photos",null,values);
        database.close();
    }
    public String return_photo(){
        String s="";
        String selectQuery = "SELECT * FROM photos;";
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                s=cursor.getString(1)+"";
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return s;
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
