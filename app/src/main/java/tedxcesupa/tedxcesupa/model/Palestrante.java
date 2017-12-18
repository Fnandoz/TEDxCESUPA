/*
 * Copyright (c) 2017. TEDxCESUPA
 * Grupo de Estudos em Tecnologia Assistiva - Centro Universitário do Estado do Pará
 * dgp.cnpq.br/dgp/espelhogrupo/6411407947674167
 * Desenvolvido por:
 *   Luis Fernando Gomes Sales - lfgsnando@gmail.com
 *   Matheus Henrique dos Santos - mhenrique.as@gmail.com
 *
 */

package tedxcesupa.tedxcesupa.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by fernando on 19/11/2017.
 */

public class Palestrante {
    String foto;
    String nome;
    String descricao;
    int estrelas;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("palestrantes");

    public Palestrante(String foto, String nome) {
        this.foto = foto;
        this.nome = nome;
    }

    public Palestrante(String foto, String nome, String descricao, int estrelas) {
        this.foto = foto;
        this.nome = nome;
        this.descricao = descricao;
        this.estrelas = estrelas;
    }

    public Palestrante(){}

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getEstrelas() {
        return estrelas;
    }

    public void setEstrelas(int estrelas) {
        this.estrelas = estrelas;
    }

    public ArrayList<Palestrante> getPalestrantes(){
        final ArrayList<Palestrante> palestranteArrayList = new ArrayList<>();

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return palestranteArrayList;
    }


}
