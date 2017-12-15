package tedxcesupa.tedxcesupa.fragments;


import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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
    ListView listaPalestrantes;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("palestrantes");

    ArrayList<Palestrante> palestranteArrayList;
    PalestranteAdapter adapter;

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

        palestranteArrayList = new ArrayList<>();
        getPalestrantes();

        listaPalestrantes = view.findViewById(R.id.palestrantesList);
        adapter = new PalestranteAdapter(palestranteArrayList, getActivity());

        listaPalestrantes.setAdapter(adapter);


        listaPalestrantes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogoPalestrante(palestranteArrayList.get(i));
            }
        });
        return view;
    }

    public void getPalestrantes(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<HashMap<String, String>> dados = (ArrayList<HashMap<String, String>>)dataSnapshot.getValue();
                dados.remove(0);
                for (HashMap<String, String> palestrante : dados){
                    Log.d("TAG", "onDataChange: "+palestrante);
                    String foto = palestrante.get("imagem");
                    String nome = palestrante.get("nome");
                    String descricao = palestrante.get("palestrante");
                    int avaliacao = 5;//Integer.parseInt(palestrante.get("avaliacao"));

                    Palestrante p = new Palestrante(foto, nome, descricao, avaliacao);
                    palestranteArrayList.add(p);
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        byte[] decodedString = Base64.decode(palestrante.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        nomePalestrante.setText(palestrante.getNome());
        ratingPalestrante.setRating(palestrante.getEstrelas());
        descricaoPalestrante.setText(palestrante.getDescricao());
        fotoPalestrante.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 250, 250, false));

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
