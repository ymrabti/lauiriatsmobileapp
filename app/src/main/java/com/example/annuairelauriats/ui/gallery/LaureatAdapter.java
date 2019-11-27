package com.example.annuairelauriats.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.annuairelauriats.MainActivity;
import com.example.annuairelauriats.R;

import java.util.ArrayList;

public class LaureatAdapter extends ArrayAdapter {
    private ArrayList<Laureat> listLaureats;
    private Context mContext;

    public LaureatAdapter(Context context, ArrayList<Laureat> listLaureat)
    {
        super(context ,0,listLaureat);this.listLaureats= listLaureat;this.mContext = context;
    }
    static class ViewHolder{
        TextView nomlaureat;
        TextView orgLaureat;
        TextView descLaureat;
        ImageView imageLaureat;
    }
    @SuppressLint("SetTextI18n")
    @Override
    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.laureat_item, null);
            holder = new ViewHolder();
            holder.nomlaureat =  convertView.findViewById(R.id.nom_monu);
            holder.orgLaureat = convertView.findViewById(R.id.org_laureat);
            holder.descLaureat = convertView.findViewById(R.id.description_laureat);
            holder.imageLaureat =  convertView.findViewById(R.id.image_laureat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Laureat LaureatCourant = listLaureats.get(position);Context context = MainActivity.getContext();
        if (LaureatCourant.getNameLaureat().isEmpty()){holder.nomlaureat.setText("Utilisateur Annuaire");}
        else {holder.nomlaureat.setText(LaureatCourant.getNameLaureat());}
        if (LaureatCourant.getOrganisation().isEmpty()){holder.orgLaureat.setText("Organisation");}
        else{holder.orgLaureat.setText(LaureatCourant.getOrganisation());}
        if (LaureatCourant.getDescription().isEmpty()){holder.descLaureat.setText("example@domaine.com");}
        else{holder.descLaureat.setText(LaureatCourant.getDescription());
        holder.descLaureat.setTextColor(context.getResources().getColor(R.color.rouge));}
        if (LaureatCourant.getImage().isEmpty()){ holder.imageLaureat.setImageResource(R.drawable.avatar); }
        else{ holder.imageLaureat.setImageBitmap(base64toImage( LaureatCourant.getImage())); }
        return convertView;
    }
    @SuppressLint("StaticFieldLeak")
    private Bitmap base64toImage(final String imageString){
        Bitmap new_bitmap;
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        new_bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return new_bitmap;
    }
}
