/*
 * Copyright (c) 2018. TEDxCESUPA
 * Grupo de Estudos em Tecnologia Assistiva - Centro Universitário do Estado do Pará
 * dgp.cnpq.br/dgp/espelhogrupo/6411407947674167
 * Desenvolvido por:
 *   Luis Fernando Gomes Sales - lfgsnando@gmail.com
 *   Matheus Henrique dos Santos - mhenrique.as@gmail.com
 *
 */

package tedxcesupa.tedxcesupa.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tedxcesupa.tedxcesupa.R;
import tedxcesupa.tedxcesupa.adapter.PatrocinadoresAdapter;
import tedxcesupa.tedxcesupa.model.Patrocinador;


public class InformacoesFragment extends Fragment {

    Button maps;
    TextView email, facebook, site;
    List<Patrocinador> patrocinadorList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("patrocinadores");
    PatrocinadoresAdapter adapter;

    public InformacoesFragment() {
        // Required empty public constructor
    }

    public static InformacoesFragment newInstance(){
        InformacoesFragment fragment = new InformacoesFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_informacoes, container, false);

        maps = view.findViewById(R.id.maps_button);
        email = view.findViewById(R.id.email_button);
        facebook = view.findViewById(R.id.facebook_button);
        site = view.findViewById(R.id.site_button);
        patrocinadorList = new ArrayList<>();

        GridView gridView = view.findViewById(R.id.patrocinadores_grid_view);
        adapter = new PatrocinadoresAdapter(getContext(), patrocinadorList, getActivity());

        getData();
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                abrirUrl(patrocinadorList.get(i).getUrl());
            }
        });

        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoMapa();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirUrl("https://www.facebook.com/tedxcesupa/");
            }
        });

        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirUrl("http://tedxcesupa.com.br/");
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","tedxcesupa@gmail.com", null));
                startActivity(Intent.createChooser(emailIntent, "Abrindo e-mail"));
            }
        });

        return view;
    }

    public void getData(){
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<HashMap<String, String>> dados = (ArrayList<HashMap<String, String>>) dataSnapshot.getValue();
                dados.remove(0);
                for (HashMap<String, String> patrocinadores : dados) {
                    String titulo = patrocinadores.get("titulo");
                    String foto = patrocinadores.get("imagem");
                    String url = patrocinadores.get("url");

                    Patrocinador p = new Patrocinador(titulo, url, foto);
                    patrocinadorList.add(p);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void dialogoMapa(){
        Dialog mDialogMaps = new Dialog(getContext());
        mDialogMaps.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogMaps.setContentView(R.layout.maps_dialog);
        mDialogMaps.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = mDialogMaps.getWindow();
        //layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);

        MapView mMapView = mDialogMaps.findViewById(R.id.mapView);
        mMapView.setCameraDistance(18);

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng gasometro = new LatLng(-1.45186113, -48.4733668);
                googleMap.addMarker(new MarkerOptions().position(gasometro)
                        .title("TEDxCESUPA"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gasometro, 18));
            }
        });

        mMapView.onCreate(mDialogMaps.onSaveInstanceState());
        mMapView.onResume();

        mDialogMaps.show();
    }

    public void abrirUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void onClick(View v) {
        Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
    }

}
