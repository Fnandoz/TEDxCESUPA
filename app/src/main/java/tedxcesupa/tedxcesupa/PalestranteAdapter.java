package tedxcesupa.tedxcesupa;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fernando on 19/11/2017.
 */

public class PalestranteAdapter extends BaseAdapter {
    private final List<Palestrante> palestrantes;
    Activity activity;

    public PalestranteAdapter(List<Palestrante> palestrantes, Activity activity) {
        this.palestrantes = palestrantes;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return palestrantes.size();
    }

    @Override
    public Object getItem(int i) {
        return palestrantes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = activity.getLayoutInflater().
                inflate(R.layout.palestrantes_item_list, viewGroup, false);

        Palestrante palestrante = palestrantes.get(i);
        ImageView fotoPerfil = view.findViewById(R.id.palestranteImageView);
        TextView nome = view.findViewById(R.id.palestranteTextView);

        fotoPerfil.setImageDrawable(activity.getResources().getDrawable(R.mipmap.ic_launcher));
        nome.setText(palestrante.getNome());

        return view;
    }
}
