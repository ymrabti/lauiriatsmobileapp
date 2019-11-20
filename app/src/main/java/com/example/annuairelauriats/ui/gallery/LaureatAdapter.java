package com.example.annuairelauriats.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

public class LaureatAdapter extends ArrayAdapter {
    private ArrayList<Laureat> listLaureats;
    private Context mContext;
    private Bitmap bm;

    LaureatAdapter(Context context, ArrayList<Laureat> listLaureat)
    {
        super(context ,0,listLaureat);this.listLaureats= listLaureat;this.mContext = context;
    }
    static class ViewHolder{
        TextView nomlaureat;
        TextView orgLaureat;
        TextView descLaureat;
        ImageView imageLaureat;
    }
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
        holder.nomlaureat.setText(LaureatCourant.getNameLaureat());
        holder.orgLaureat.setText(LaureatCourant.getOrganisation());
        holder.descLaureat.setText(LaureatCourant.getDescription());
        holder.imageLaureat.setImageBitmap(base642Image( LaureatCourant.getImage()));

        return convertView;
    }
    @SuppressLint("StaticFieldLeak")
    private Bitmap base642Image(final String imageString){
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... voids) {
                byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                bm=decodedImage;
                return decodedImage;
            }
            protected void onPostExecute(Bitmap bitmap){
            }
        }.execute();
        return bm;
    }
}
