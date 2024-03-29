/*
 * Copyright (c) 2017. TEDxCESUPA
 * Grupo de Estudos em Tecnologia Assistiva - Centro Universitário do Estado do Pará
 * dgp.cnpq.br/dgp/espelhogrupo/6411407947674167
 * Desenvolvido por:
 *   Luis Fernando Gomes Sales - lfgsnando@gmail.com
 *   Matheus Henrique dos Santos - mhenrique.as@gmail.com
 *
 */

package tedxcesupa.tedxcesupa.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tedxcesupa.tedxcesupa.R;
import tedxcesupa.tedxcesupa.model.Palestrante;

/**
 * Created by fernando on 19/11/2017.
 */

public class PalestranteAdapter extends BaseAdapter {
    Activity activity;
    private List<Palestrante> palestrantes;

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
        byte[] decodedString = Base64.decode(palestrante.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        fotoPerfil.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 160, 160, false));
        nome.setText(palestrante.getNome());

        return view;
    }
}
