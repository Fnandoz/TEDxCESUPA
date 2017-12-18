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


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tedxcesupa.tedxcesupa.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnotacoesFragment extends Fragment {

    EditText anotacao;
    ProgressBar progressBar;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    String textoOriginal = "";
    boolean status_edicao = false;

    public AnotacoesFragment() {
        // Required empty public constructor
    }

    public static AnotacoesFragment newInstance(){
        AnotacoesFragment fragment = new AnotacoesFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anotacoes, container, false);

        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mReference = mDatabase.getReference("usuarios").child(mUser.getUid()).child("anotacao");


        anotacao = view.findViewById(R.id.anotacaoEditText);
        progressBar = view.findViewById(R.id.AnotacaoprogressBar);

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anotacao.setText(dataSnapshot.getValue().toString());
                textoOriginal = dataSnapshot.getValue().toString();
                progressBar.setVisibility(View.GONE);
                status_edicao = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /* Salvar o conteudo do campo anotacao */
        updateAnotation();
    }

    public void updateAnotation(){
        if (status_edicao)
            mReference.setValue(anotacao.getText().toString());
    }
}
