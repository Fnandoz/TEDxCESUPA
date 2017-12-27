/*
 * Copyright (c) 2017. TEDxCESUPA
 * Grupo de Estudos em Tecnologia Assistiva - Centro Universitário do Estado do Pará
 * dgp.cnpq.br/dgp/espelhogrupo/6411407947674167
 * Desenvolvido por:
 *   Luis Fernando Gomes Sales - lfgsnando@gmail.com
 *   Matheus Henrique dos Santos - mhenrique.as@gmail.com
 *
 */

package tedxcesupa.tedxcesupa.fragments;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import tedxcesupa.tedxcesupa.R;
import tedxcesupa.tedxcesupa.adapter.PalestranteAdapter;
import tedxcesupa.tedxcesupa.model.Palestrante;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


/**
 * A simple {@link Fragment} subclass.
 */
public class PalestrantesFragment extends Fragment {

    AlertDialog dialog;
    ListView listaPalestrantes;
    HashMap<String, Float> avaliacoes_palestrante_map;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("palestrantes");
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    ArrayList<Palestrante> palestranteArrayList;
    PalestranteAdapter adapter;

    ProgressBar progressBar;
    RatingBar ratingPalestrante;

    float soma_avaliacao=0, media_avaliacao=0, valor_avaliacao=0;

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
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        progressBar = view.findViewById(R.id.palestrantes_progress);

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
                    String foto = palestrante.get("imagem");
                    String nome = palestrante.get("nome");
                    String descricao = palestrante.get("descricao");
                    int avaliacao = 5;//Integer.parseInt(palestrante.get("avaliacao"));

                    Palestrante p = new Palestrante(foto, nome, descricao, avaliacao);
                    palestranteArrayList.add(p);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void dialogoPalestrante(final Palestrante palestrante){
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = inflater.inflate(R.layout.palestrante_dialog, null);
        builder.setView(dialogView);

        ImageButton close = dialogView.findViewById(R.id.closeButton);
        ImageView fotoPalestrante = dialogView.findViewById(R.id.fotoPalestranteDialog);
        TextView nomePalestrante = dialogView.findViewById(R.id.nomePalestranteDialog);
        ratingPalestrante = dialogView.findViewById(R.id.ratingPalestranteDialog);
        TextView descricaoPalestrante = dialogView.findViewById(R.id.descricaoPalestranteDialog);

        byte[] decodedString = Base64.decode(palestrante.getFoto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        getAvaliacao(palestrante.getNome());
        nomePalestrante.setText(palestrante.getNome());
        //ratingPalestrante.setRating(getAvaliacao(palestrante.getNome()));
        descricaoPalestrante.setText(palestrante.getDescricao());
        fotoPalestrante.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 250, 250, false));

        ratingPalestrante.setNumStars(5);
        ratingPalestrante.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                dialogoAvaliacao(palestrante);
                return false;
            }
        });

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

    public void enviarAvaliacao(String palestrante, float avaliacao){
        DatabaseReference AvaliacaoRef = database.getReference("avaliacoes").child(palestrante).child(mUser.getUid());
        AvaliacaoRef.setValue(avaliacao).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(getContext(), "Avaliação enviada.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Tente novamente"+task.getException(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void getAvaliacao(final String palestrante) {
        soma_avaliacao = 0;
        media_avaliacao = 0;
        DatabaseReference AvaliacaoRef = database.getReference("avaliacoes").child(palestrante);
        AvaliacaoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(palestrante)) {
                    avaliacoes_palestrante_map = (HashMap<String, Float>) dataSnapshot.getValue();
                    for (Object i : avaliacoes_palestrante_map.values()) {
                        soma_avaliacao += Float.parseFloat(i.toString());
                    }
                    media_avaliacao = soma_avaliacao / avaliacoes_palestrante_map.size();
                    ratingPalestrante.setRating(media_avaliacao);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void dialogoAvaliacao(final Palestrante palestrante){

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.avaliacao_dialog, null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Avalie "+palestrante.getNome());
        builder.setView(view);


        RatingBar ratingBar = view.findViewById(R.id.palestrante_dialog_ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                valor_avaliacao = v;
            }
        });

        builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enviarAvaliacao(palestrante.getNome(), valor_avaliacao);
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

    }

}
