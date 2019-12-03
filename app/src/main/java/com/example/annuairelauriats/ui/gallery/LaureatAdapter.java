package com.example.annuairelauriats.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.annuairelauriats.R;

import java.util.ArrayList;

import static com.example.annuairelauriats.ui.home.Classtest.base64toImage;

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
        final Laureat LaureatCourant = listLaureats.get(position);
        if (!LaureatCourant.getNameLaureat().isEmpty()){
            holder.nomlaureat.setText(LaureatCourant.getNameLaureat());
        }
        else{
            holder.nomlaureat.setText("utilisateur app");
        }
        if (!LaureatCourant.getOrganisation().isEmpty()){
            holder.orgLaureat.setText(LaureatCourant.getOrganisation());
        }
        else{
            holder.orgLaureat.setText("organisation");
        }
        if (!LaureatCourant.getDescription().isEmpty()){
            holder.descLaureat.setText(LaureatCourant.getDescription());
        }
        else{
            holder.descLaureat.setText("description");
        }
        if (!LaureatCourant.getImage().isEmpty()){
            holder.imageLaureat.setImageBitmap(base64toImage(LaureatCourant.getImage()));
        }
        else{
            holder.imageLaureat.setImageResource(R.drawable.ing);
        }


        //holder.imageLaureat.setImageResource(R.drawable.avatar);

        return convertView;
    }
}
