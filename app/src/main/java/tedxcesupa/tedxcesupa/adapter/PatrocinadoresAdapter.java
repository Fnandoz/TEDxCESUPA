package tedxcesupa.tedxcesupa.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import tedxcesupa.tedxcesupa.R;
import tedxcesupa.tedxcesupa.model.Patrocinador;

/**
 * Created by fernando on 14/12/2017.
 */

public class PatrocinadoresAdapter extends BaseAdapter {
    private Context mContext;
    private Activity activity;
    private List<Patrocinador> patrocinadores;

    public PatrocinadoresAdapter(Context c, List<Patrocinador> p, Activity act) {
        this.mContext = c;
        this.patrocinadores = p;
        this.activity = act;
    }

    public int getCount() {
        return patrocinadores.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().
                inflate(R.layout.patrocinador_item_list, parent, false);

        ImageView foto = view.findViewById(R.id.foto_patrocinador);
        byte[] decodedString = Base64.decode(patrocinadores.get(position).getImagem(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        foto.setImageBitmap(decodedByte);
        return foto;
    }
}

