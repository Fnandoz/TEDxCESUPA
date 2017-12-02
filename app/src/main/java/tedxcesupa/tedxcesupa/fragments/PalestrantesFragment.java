package tedxcesupa.tedxcesupa.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tedxcesupa.tedxcesupa.model.Palestrante;
import tedxcesupa.tedxcesupa.adapter.PalestranteAdapter;
import tedxcesupa.tedxcesupa.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 * A simple {@link Fragment} subclass.
 */
public class PalestrantesFragment extends Fragment {

    AlertDialog dialog;

    public PalestrantesFragment() {
        // Required empty public constructor
    }

    public static PalestrantesFragment newInstance(){
        PalestrantesFragment fragment = new PalestrantesFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_palestrantes, container, false);

        final List<Palestrante> palestrantes = new ArrayList<>();
        palestrantes.add(new Palestrante(1, "Nome 1", "okok", 2));
        palestrantes.add(new Palestrante(1, "Nome 2", "okok", 1));
        palestrantes.add(new Palestrante(1, "Nome 3", "okok", 3));
        palestrantes.add(new Palestrante(1, "Nome 4", "okok", 5));

        ListView listaPalestrantes = view.findViewById(R.id.palestrantesList);
        final PalestranteAdapter adapter = new PalestranteAdapter(palestrantes, getActivity());

        listaPalestrantes.setAdapter(adapter);

        listaPalestrantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogoPalestrante(palestrantes.get(i));
            }
        });
        return view;
    }

    public void dialogoPalestrante(Palestrante palestrante){
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = inflater.inflate(R.layout.palestrante_dialog, null);
        builder.setView(dialogView);

        ImageButton close = dialogView.findViewById(R.id.closeButton);
        ImageView fotoPalestrante = dialogView.findViewById(R.id.fotoPalestranteDialog);
        TextView nomePalestrante = dialogView.findViewById(R.id.nomePalestranteDialog);
        RatingBar ratingPalestrante = dialogView.findViewById(R.id.ratingPalestranteDialog);
        TextView descricaoPalestrante = dialogView.findViewById(R.id.descricaoPalestranteDialog);

        nomePalestrante.setText(palestrante.getNome());
        ratingPalestrante.setRating(palestrante.getEstrelas());
        descricaoPalestrante.setText(palestrante.getDescricao());

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog = builder.create();
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(MATCH_PARENT, MATCH_PARENT);

    }

}
